/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.urlhandling;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;

/**
 *
 * @author re-sulrich
 */
public class TomcatURIHandler implements URIHandling {
    
    @Override
    public String getServerBaseURL() throws MalformedObjectNameException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, Exception {
        return getTomcatListeningAddress();
    }
    
    public static String getTomcatListeningAddress() throws MalformedObjectNameException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, Exception {
        
        String url = null;
        
        MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
        ObjectName name = new ObjectName("Catalina", "type", "Server");

        System.out.println("Got server catalina.");
        Server s = (Server) mBeanServer.getAttribute(name, "managedResource");

        System.out.println("Get Server object");
        Service[] serv = s.findServices();

        Service ss = null;
        if (serv != null && serv.length > 0) {

            System.out.println("Services : " + serv.length);
            for (int i = 0; i < serv.length; i++) {
                Service service = serv[i];
                if (service.getName().equalsIgnoreCase("catalina")) {
                    ss = serv[i];
                }
            }
        }
        else {
            System.out.println("Could not find Tomcat server configuration");
        }
        if (ss != null) {

            Connector[] conns = ss.findConnectors();

            System.out.println("NbConnectors : " + conns.length);

//            s.stop();

            for (Connector connector : conns) {
                String connName = (String)connector.getAttribute("id");
                
                if (connName != null && connName.equalsIgnoreCase("dream")) {
                    
                    System.out.println("Connector name : " + connName);
                
                    String addr = connector.getAttribute("address").toString();
                    if (addr == null) {
                        throw new Exception("Could not read the address parameter from the Tomcat server configuration.");
                    }
                    else {
                        while (addr.startsWith("/")) {
                            addr = addr.substring(1);
                        }
                    }
                    String port = connector.getAttribute("port").toString(); 
                    if (port == null) {
                        throw new Exception("Could not read the port parameter from the Tomcat server configuration.");
                    }
                    
                    url = "http://" + addr + ":" + port;
                    
                    System.out.println("Connector : " + 
                            url + " from addr : " + addr + " and port : " + port);
                }
            }
        }
        
        return url;
    }
}
