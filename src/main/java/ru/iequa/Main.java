package ru.iequa;

import ru.iequa.handlers.*;
import ru.iequa.httpserver.Server;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
            handlers.add(new LoginHandler());
            handlers.add(new GetUserDataHandler());
            handlers.add(new LogoutHandler());
            handlers.add(new SetNewsDataHandler());
            var handlerResolver = new HandlerResolver(handlers);
            server.createContext("/", handlerResolver);
            ExecutorService executor = Executors.newFixedThreadPool(5);
            server.setExecutor(executor);
            server.start();
            System.out.println("Server started and working");
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            server.stop(0);
        }
    }
}