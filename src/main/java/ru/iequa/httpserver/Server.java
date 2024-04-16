package ru.iequa.httpserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class Server {
    private final HttpServer server;

    private final InetSocketAddress address;
    private final int backlog;


    public Server(InetSocketAddress address, int backlog) {
        this.address = address;
        this.backlog = backlog;
        try {
            server = HttpServer.create(this.address, this.backlog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        server.start();
    }

    public void setExecutor(Executor executor) {
        server.setExecutor(executor);
    }

    public void stop(int delay) {
        server.stop(delay);
    }

    public HttpContext createContext(String path, HttpHandler handler) {
        return server.createContext(path, handler);
    }
}
