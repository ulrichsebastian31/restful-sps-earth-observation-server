/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.dbhandler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Enumeration;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class ApplicationServerTests {
    
    private String url;
    private String user;
    private String passwd;
    private String schema;
    
    @Before
    public void init() {
        
//        url = "jdbc:postgresql://10.2.200.247:5432/HMASDatabase";
//        url = "jdbc:postgresql://192.168.0.20:5432/HMASDatabase";
        url = "jdbc:postgresql://127.0.0.1:5432/HMASDatabase";

        user = "postgres";
        passwd = "postgres";
        schema = "MissionPlannerDatabase";
    }
    
//    @Test
    public void test() throws SQLException {
        
        String baseURL = null;
       
        String applicationServerId = null;
        try {
            InetAddress addr = InetAddress.getLocalHost(); 
            baseURL = "http://" + addr.getHostAddress() + ":8080";
            System.out.println(baseURL + " - name : " + addr.getHostName());
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Not possible to get the local host address");
        }
//        ApplicationServerLoader as = ApplicationServerLoader.createApplicationServerLoaderFromURL(
//                new DBOperations(url, user, passwd),
//                baseURL);
//        
//        System.out.println("" + as.getApplicationServerId());
    }
    
    @Test
    public void testNetworkInterfaces() throws SocketException {
        Enumeration e=NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n=(NetworkInterface) e.nextElement();
            
            Enumeration ee = n.getInetAddresses();
            
            if (ee.hasMoreElements()) {
                System.out.println("" + n.getDisplayName() + " - " + n.getName());
            }
            
            while(ee.hasMoreElements())
            {
                InetAddress i= (InetAddress) ee.nextElement();
                System.out.println(" - " + i.getHostAddress() + ""
                        + " - name : " + i.getCanonicalHostName() + "  -  " + i.getHostName());
            }
        }
    }
}
