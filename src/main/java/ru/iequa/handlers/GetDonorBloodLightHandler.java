package ru.iequa.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.iequa.contracts.request.Request;
import ru.iequa.contracts.response.BloodTrafficLightResponse;
import ru.iequa.models.blooddata.BloodData;
import ru.iequa.models.blooddata.RHData;
import ru.iequa.utils.JsonWorker;
import ru.iequa.utils.ResponseCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetDonorBloodLightHandler extends HandlerBase {

    public static final String PATH = "get-donorbloodlight";

    public static final String METHOD = "GET";

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
        return false;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Request request = JsonWorker.getInstance().deserialize(json, Request.class);
        final List<BloodData> bloodData = new ArrayList<>();
        bloodData.add(
                new BloodData(
                        "O(I)",
                        List.of(
                                new RHData("Rh+", 3),
                                new RHData("Rh-", 2)
                        )
                )
        );
        bloodData.add(
                new BloodData(
                        "A(II)",
                        List.of(
                                new RHData("Rh+", 1),
                                new RHData("Rh-", 2)
                        )
                )
        );
        bloodData.add(
                new BloodData(
                        "B(III)",
                        List.of(
                                new RHData("Rh+", 1),
                                new RHData("Rh-", 4)
                        )
                )
        );
        bloodData.add(
                new BloodData(
                        "AB(IV)",
                        List.of(
                                new RHData("Rh+", 3),
                                new RHData("Rh-", 3)
                        )
                )
        );
        final var resp = new BloodTrafficLightResponse(
                null, null, 200, bloodData);
        new ResponseCreator().sendOkResponseWithBody(exchange, resp);
    }
}
