package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.iequa.contracts.request.RecordsReportRequest;
import ru.iequa.contracts.response.RecordsReportResponse;
import ru.iequa.database.DB;
import ru.iequa.models.db.Column;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;
import ru.iequa.utils.TokenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

public class RecordsReportHandler extends HandlerBase {

    public static final String PATH = "get-date-stat";

    public static final String METHOD = "POST";

    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    public String getMethod() {
        return METHOD;
    }

    @Override
    public boolean needsAuth() {
        return true;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        RecordsReportRequest request = JsonWorker.getInstance().deserialize(json, RecordsReportRequest.class);
        final var token = TokenHelper.checkAndGetValidUserToken(exchange);
        if (token != null) {
            final var frst = Timestamp.valueOf(LocalDateTime.parse(request.firstDate, DateTimeFormatter.ISO_DATE_TIME));
            final var scd = Timestamp.valueOf(LocalDateTime.parse(request.secondDate, DateTimeFormatter.ISO_DATE_TIME));

            final var sql = """
                    select
                        us.provision_date "Дата предоставления",
                        s.name "Название услуги",
                        s.type "Тип услуги",
                        COALESCE(s.cost, null) "Стоимость",
                        u.id "Идентификатор",
                        ud.name "Имя",
                        ud.surname "Фамилия",
                        ud.birthdate "Дата рождения"
                    from
                        user_services us
                        inner join users u on us.user_id = u.id
                        inner join user_data ud on us.user_id = ud.user_id
                        inner join services s on us.service_id = s.id
                    where us.provision_date between ':start' and ':end'
                    order by us.provision_date"""
                    .replace(":start", frst.toString())
                    .replace(":end", scd.toString());
            final var res = DB.getInstance().ExecQuery(sql);
            if (res != null && res.getRows() != null) {
                final var dbrows = res.getRows();
                if ((long) dbrows.size() == 0) {
                    new ResponseCreator().sendOkResponseWithMessage(exchange, "Записей на указнный период не найдено.");
                    return;
                }
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Результат");
                for (int i = 0; i < 10; i++) {
                    sheet.setColumnWidth(i, 7000);
                }
                prepareHeaderCells(workbook, sheet, res.getColumns());
                fillCells(workbook, sheet, res.getRows(), res.getColumns());

                File tempFile = File.createTempFile("rep", "xml");
                final var cdt = LocalDateTime.now();
                final String fileName = "отчёт с :frst по :second от :cdt.xlsx"
                        .replace(":frst", frst.toLocalDateTime().toLocalDate().toString())
                        .replace(":second", scd.toLocalDateTime().toLocalDate().toString())
                        .replace(":cdt", cdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")));
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                final var resFile = new FileInputStream(tempFile);
                new ResponseCreator().sendResponseWithBody(exchange, new RecordsReportResponse(resFile.readAllBytes().clone(), fileName));
                resFile.close();
                tempFile.delete();
                return;
            }
            new ResponseCreator().sendOkResponseWithMessage(exchange, "Ошибка формирования файла");
            return;
        }
        new ResponseCreator().sendNotAuthorizedResponse(exchange, "Ошибка. Попробуйте авторизироваться повторно");
    }

    private void fillCells(Workbook workbook, Sheet sheet, LinkedHashSet<ru.iequa.models.db.Row> dbrows, LinkedHashSet<Column> columns) {
        //Готовим стили для колонок
        CellStyle valueCellStyle = workbook.createCellStyle();
        valueCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(false);
        valueCellStyle.setFont(font);
        final var colList = columns.stream().toList();
        final var dbrowsList = dbrows.stream().toList();
        for (int i = 0; i < dbrowsList.size(); i++) {
            final var currDbRow = dbrowsList.get(i);
            Row currRow = sheet.createRow(i + 1);// +1 Т.к. 0 уже занят хедером
            for (int j = 0; j < colList.size(); j++) {
                final var currCol = colList.get(j);
                Cell cell = currRow.createCell(j);
                final Object dbElem = currDbRow.getElement(currCol.name);
                if (dbElem != null) {
                    try {
                        switch (currCol.type) {
                            case INT, SERIAL -> cell.setCellValue((int) dbElem);
                            case DATE -> cell.setCellValue(
                                    (Instant.ofEpochMilli(((Date) dbElem).getTime())
                                            .atZone(ZoneId.systemDefault()).toLocalDateTime()
                                    )
                                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            );
                            case TIMESTAMP -> cell.setCellValue(
                                    ((Timestamp) dbElem)
                                            .toLocalDateTime()
                                            .format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))
                            );
                            case VARCHAR -> cell.setCellValue((String) dbElem);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getLocalizedMessage());
                    }

                }

                cell.setCellStyle(valueCellStyle);
            }
        }
    }

    private void prepareHeaderCells(Workbook workbook, Sheet sheet, LinkedHashSet<Column> columns) {
        Row headerRow = sheet.createRow(0);
        //Готовим стили для колонок
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);
        final var colList = columns.stream().toList();
        for (int i = 0; i < colList.size(); i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(colList.get(i).name);
            headerCell.setCellStyle(headerStyle);
        }
    }
}
