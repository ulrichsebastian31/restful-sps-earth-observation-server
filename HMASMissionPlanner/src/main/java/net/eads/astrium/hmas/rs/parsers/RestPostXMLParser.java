/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.parsers;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 */
public interface RestPostXMLParser {
    
    public XmlObject createXMLFromPostRequest(String request, String postRequest) throws XmlException;
}
