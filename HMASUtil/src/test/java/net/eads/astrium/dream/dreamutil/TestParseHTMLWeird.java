/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               TestParseHTMLWeird.java
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
package net.eads.astrium.dream.dreamutil;

import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestParseHTMLWeird {
    
    @Test
    public void test() {
        
        String toParse = "&lt;ul&gt;	&lt;li&gt; &lt;b&gt;Date&lt;/b&gt;: 2010-01-22 &lt;br/&gt;\n" +
"	(2010-01-22T01:44:41.316Z / 2010-01-22T01:44:57.596Z) &lt;/li&gt;\n" +
"	&lt;li&gt; &lt;b&gt;Orbit Number&lt;/b&gt;: 41282 &lt;/li&gt;\n" +
"	&lt;li&gt; &lt;b&gt;Processing Center&lt;/b&gt;: PDHS-E &lt;/li&gt;         \n" +
"	&lt;li&gt; &lt;b&gt;Processing Date&lt;/b&gt;: 2010-01-22 &lt;/li&gt;\n" +
"	&lt;br/&gt;\n" +
"	&lt;a href=\"https://eo-virtual-archive4.esa.int/supersites/ASA_IM__0CNPDE20100122_014441_000000162086_00146_41282_7918.N1\"&gt;Product Download &lt;/a&gt;\n" +
"	&lt;/ul&gt;";
        String res = toParse.replaceAll("&lt;", "<");
        
        System.out.println("" + res);
        res = res.replaceAll("&gt;", ">");
        System.out.println("" + res);
        
//        String[] results = res.split("<li>");
//        
//        for (int i = 0; i < results.length; i++) {
//            String string = results[i];
//            
//            String[] values = string.split(":", 2);
//            
//            String string1 = values[0];
//            int b = string1.indexOf("<b>");
//            if(b > 0) 
//            {
//                b = b + "<b>".length();
//                int e = string1.indexOf("</b>");
//
//                if (e > 0) {
//
//                    System.out.print("" + string1.substring(b, e) + "      ");
//                }
//            }
//            
//            if (values.length > 1)
//            {
//                String string2 = values[1];
//                int li = string2.lastIndexOf("</li>");
//                if (li > 0)
//                {
//                    System.out.println("" + string2.substring(0, li));
//                }
//            }
//        }
        
        String su = res.substring(res.lastIndexOf("</li>"));
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("" + su);
    }
}
