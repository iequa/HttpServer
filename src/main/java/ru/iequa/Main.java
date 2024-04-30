package ru.iequa;

import ru.iequa.handlers.*;
import ru.iequa.httpserver.Server;

import java.net.InetSocketAddress;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new InetSocketAddress("192.168.0.164", 8085), 0);
        try {
            var handlers = new HashSet<HandlerBase>();
            handlers.add(new GetNewsHandler());
            handlers.add(new GetDonorBloodLightHandler());
            handlers.add(new GetNewsDataHandler());
            handlers.add(new GetCalendarDataHandler());
            handlers.add(new SetCalendarDatetimeHandler());

            var handlerResolver = new HandlerResolver(handlers);
            server.createContext("/", handlerResolver);
            server.setExecutor(null);
            server.start();
            System.out.println("Server started and working");
            while (true) {
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            server.stop(0);
        }
    }
}