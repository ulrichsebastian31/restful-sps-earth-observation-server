/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.urlhandling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author re-sulrich
 */
public class URIHandler implements URIHandling {
    
    @Override
    public String getServerBaseURL() throws UnknownHostException {
        
        String url = null;
        
        InetAddress addr = InetAddress.getLocalHost(); 
        url = "http://" + addr.getHostAddress() + ":8080";
        System.out.println(url + " - name : " + addr.getHostName());
        
        return url;
    }
}
