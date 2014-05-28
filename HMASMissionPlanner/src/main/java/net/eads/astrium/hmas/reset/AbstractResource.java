/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.reset;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import net.eads.astrium.hmas.mp.MissionPlannerWorker;
import net.eads.astrium.hmas.util.logging.SysoRedirect;
import net.eads.astrium.hmas.util.urlhandling.TomcatURIHandler;

/**
 *
 * @author re-sulrich
 */
public class AbstractResource {
    
    @Context 
    volatile UriInfo ui;
    
    @Context
    volatile HttpContext httpContext;
    
    @Context
    volatile ServletContext servletContext;
    
    String request;
    String format;
    String serversAdress;
    
    
    static MissionPlannerWorker worker = initWorker();
    
    public static MissionPlannerWorker initWorker() {
        MissionPlannerWorker w = null;
        
        try {
            SysoRedirect.redirectSysoToFiles("log", "log");
        } catch (Exception ex) {
            System.out.println("Error redirecting sout and serr to files.");
        }
        
        
        String url = "http://127.0.0.1:8080";
        
        try {
            url = new TomcatURIHandler().getServerBaseURL();
        } catch (Exception ex) {
            System.out.println("" + ex.getClass().getName() + " : " + ex.getMessage());
        }
        
        try {
            w = new MissionPlannerWorker("s1a-missionplanning", "00001AAA", url);
        } catch (SQLException|IOException ex) {
            System.out.println("" + ex.getClass().getName() + " : " + ex.getMessage());
        }
        
        return w;
    }
   
    public static MultivaluedMap addEOSPSExtensibleRequestParams(MultivaluedMap in) {
        if (in == null) {
            in = new MultivaluedMapImpl();
        }
        
        in.putSingle("service", "eosps");
        in.putSingle("version", "2.0");
        in.putSingle("acceptFormat", "text/xml");
        
        return in;
    }
}
