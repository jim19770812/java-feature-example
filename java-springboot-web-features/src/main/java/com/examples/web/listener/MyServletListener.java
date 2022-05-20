package com.examples.web.listener;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Component
@ComponentScan
@WebListener
public class MyServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("listener initialized..\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("listener destroyed..\n");
    }
}
