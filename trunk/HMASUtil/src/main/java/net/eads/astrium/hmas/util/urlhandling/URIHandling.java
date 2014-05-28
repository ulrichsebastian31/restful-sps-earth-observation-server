/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.urlhandling;

/**
 *
 * @author re-sulrich
 */
public interface URIHandling {
    
    public class Factory {
        public static URIHandling getHandler(String server) {
            
            if (server == null) server = "";
            
            URIHandling handler = null;
            switch (server.toLowerCase()) {
                case "tomcat":
                    handler = new TomcatURIHandler();
                break;
                default:
                    handler = new URIHandler();
                break;
            }
            return handler;
        }
    }
    
    public String getServerBaseURL() throws Exception;
}
