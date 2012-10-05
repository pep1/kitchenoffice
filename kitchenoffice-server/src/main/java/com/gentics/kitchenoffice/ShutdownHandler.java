package com.gentics.kitchenoffice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Inspiration from blogpost by Johannes Brodwall.
 */
public class ShutdownHandler extends AbstractHandler {

    private final Server server;
    private final WebAppContext context;

    public ShutdownHandler(Server server, WebAppContext context) {
        this.server = server;
        this.context = context;
    }

    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int arg3) throws IOException,
            ServletException {
        if (!target.equals("/shutdown") || !request.getMethod().equals("POST"))
            return;

        try {
            // You should probably do something clever with this if you handle a
            // lot of transactions etc
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.println("Shutting down");
            try {
                out.close();
            } catch (Exception ignored) {
            }
            try {
                context.stop();
            } catch (Exception ignored) {
            }
            try {
                server.stop();
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
        }
        System.exit(0);

    }

    public static String shutdown(int port) {
        return "http://127.0.0.1:" + port + "/shutdown";
    }
}
