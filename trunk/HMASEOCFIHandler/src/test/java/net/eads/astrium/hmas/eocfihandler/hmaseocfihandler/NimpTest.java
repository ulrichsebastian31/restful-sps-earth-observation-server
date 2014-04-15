/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               NimpTest.java
 *   File Type                                          :               Source Code
 *   Description                                        :                *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (coffee) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

import net.eads.astrium.hmas.util.structures.tasking.geometries.Point;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class NimpTest {
    
    @Test
    public void test() {
        String[] s = "C:\\Program Files (x86)\\Java\\jdk1.7.0_03\\bin;C:\\Windows\\Sun\\Java\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Program Files\\Common Files\\Microsoft Shared\\Windows Live;C:\\Program Files (x86)\\Common Files\\Microsoft Shared\\Windows Live;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files (x86)\\Liquid Technologies\\Liquid XML Studio 2011\\XmlDataBinder9\\Redist9\\cpp\\win32\\bin;C:\\Program Files\\doxygen\\bin;C:\\Program Files\\Java\\jdk1.7.0_15\\bin;C:\\Java\\jboss-as-7.1.1.Final\\bin;C:\\Java\\axis2-1.6.2\\bin;C:\\Java\\xmlbeans\\xmlbeans-2.6.0\\bin;C:\\Java\\apache-ant-1.8.3\\bin;C:\\Java\\apache-maven-3.0.4\\bin;C:\\Program Files\\TortoiseSVN\\bin;C:\\Program Files (x86)\\Windows Live\\Shared;C:\\Program Files\\Microsoft Windows Performance Toolkit\\;c:\\Program Files (x86)\\Microsoft SQL Server\\100\\Tools\\Binn\\;c:\\Program Files\\Microsoft SQL Server\\100\\Tools\\Binn\\;c:\\Program Files\\Microsoft SQL Server\\100\\DTS\\Binn\\;C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.34\\bin;".split(";");

        for (int i = 0; i < s.length; i++) {
            
            System.out.println("" + s[i]);
        }
    }
    
    
//    @Test
//    public void testCalcPoints() {
//        Point one = new Point(1, 1, 0);
//        Point two = new Point(2, 2, 0);
//        
//        double a = (two.getLatitude() - one.getLatitude()) / (two.getLongitude() - one.getLongitude());
//        
//        double ainv = -1 / a;
//        
//        double bInvOne = one.getLatitude() / (ainv * one.getLongitude());
//        
//        
//    }
}
