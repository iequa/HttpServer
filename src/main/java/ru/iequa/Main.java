package ru.iequa;

import ru.iequa.handlers.CheckServerHandler;
import ru.iequa.handlers.GetUUIDHandler;
import ru.iequa.handlers.HandlerBase;
import ru.iequa.handlers.HandlerResolver;
import ru.iequa.httpserver.Server;

import java.net.InetSocketAddress;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new InetSocketAddress("192.168.0.164", 8084), 0);
        try {
            var handlers = new HashSet<HandlerBase>();
            handlers.add(new CheckServerHandler());
            handlers.add(new GetUUIDHandler());

            var handlerResolver = new HandlerResolver(handlers);
            server.createContext("/", handlerResolver);
            server.setExecutor(null);
            server.start();
            while (true) {
            }
            //
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            server.stop(0);
        }
    }
}