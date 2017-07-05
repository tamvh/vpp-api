/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.main;

import com.gbc.gateway.common.Config;
import com.gbc.gateway.controller.LoginController;
import com.gbc.gateway.controller.AccessDoorController;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;


/**
 *
 * @author diepth
 */
public class WebServer implements Runnable{
    
    private static final Logger logger = Logger.getLogger(WebServer.class);
    private Server server = new Server();
    private static WebServer _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    
    public static WebServer getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new WebServer();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    @Override
    public void run() {
        try {
            int http_port = Integer.valueOf(Config.getParam("server", "http_port"));
            
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(http_port);
            connector.setIdleTimeout(30000);
            
            server.setConnectors(new Connector[]{connector});
            logger.info("Start server...");
            
            ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            servletContext.setContextPath("/");
                      
            servletContext.addServlet(LoginController.class, "/v001/vpp/api/login/*");
            servletContext.addServlet(AccessDoorController.class, "/v001/vpp/api/accessdoor/*");

            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setResourceBase("./static/");
            ContextHandler resourceContext = new ContextHandler();
            resourceContext.setContextPath("/static");
            resourceContext.setHandler(resource_handler);
            
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceContext, servletContext, new DefaultHandler()});
            server.setHandler(handlers);
            
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Cannot start web server: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void stop() throws Exception {
        server.stop();
    }
}
