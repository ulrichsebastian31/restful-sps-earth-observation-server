/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 */
public interface RestGetParser {
    
    public XmlObject createXMLFromGetRequest(String request, MultivaluedMap<String, String> params);
}
