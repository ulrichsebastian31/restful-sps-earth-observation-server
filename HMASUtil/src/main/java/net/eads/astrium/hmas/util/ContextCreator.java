/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.eads.astrium.hmas.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import net.eads.astrium.hmas.util.logging.SysoRedirect;

/**
 *
 * @author re-sulrich
 */
@WebListener
public class ContextCreator implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SysoRedirect.redirectSysoToFiles("log", "log");
        } catch (Exception ex) {
            System.out.println("Error redirecting sout and serr to files.");
        }
        
        ServletContext context = sce.getServletContext();
        
        System.out.println("##### Initializing web application path...");
        
        String path = context.getContextPath();
        //Removing the '/' character at the beginning of the context
        if (path.startsWith("/")) 
            path = path.substring(1);
        
        ServletRegistration registration = null;
        for (String string : context.getServletRegistrations().keySet()) {
//            System.out.println("Registration for " + string);
            if (string.contains("HMAS") && string.contains("Services")) {
                registration = context.getServletRegistration(string);
            }
        }
        String mapping = null;
        if (registration != null) {
            mapping = registration.getMappings().iterator().next();
            if (mapping != null) {
                mapping = mapping.substring(0, mapping.lastIndexOf("/*"));
            }
        }
        else System.out.println("\tNot found registration. \r\n\tPlease register a servlet with a name containing the words 'HMAS' and 'Services'");
        
        System.out.println("\r\n\tLoading context : \r\n"
                + "\tPath is : " + path + "\r\n"
                + "\tMapping is :  " + mapping);
        
        Constants.WAR_FILE_PATH = path + mapping;
        
        System.out.println("##### Initialized to : " + Constants.WAR_FILE_PATH);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //Nothing to do here yet
    }
}
