package ru.iequa.httpserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class Server extends HttpServer {
    private HttpServer server;

    private InetSocketAddress address;
    private int backlog;


    public Server(InetSocketAddress address, int backlog) {
        this.address = address;
        this.backlog = backlog;
        try {
            server = HttpServer.create(this.address, this.backlog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bind(InetSocketAddress addr, int backlog) {
        address = addr;
        this.backlog = backlog;
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void setExecutor(Executor executor) {

    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void stop(int delay) {
        server.stop(delay);
    }

    @Override
    public HttpContext createContext(String path, HttpHandler handler) {
        return server.createContext(path, handler);
    }

    @Override
    public HttpContext createContext(String path) {
        return null;
    }

    @Override
    public void removeContext(String path) throws IllegalArgumentException {
    }

    @Override
    public void removeContext(HttpContext context) {
    }

    @Override
    public InetSocketAddress getAddress() {
        return server.getAddress();
    }
}
