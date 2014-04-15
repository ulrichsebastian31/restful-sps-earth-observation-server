/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FormatHandler.java
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
package net.eads.astrium.hmas.util;


/**
 *
 * @author re-sulrich
 */
public class FormatHandler {

    /**
     * This method returns the input type if it is supported by the server Or
     * text/plain otherwise
     *
     * @param inType
     * @return type Mime type
     */
    public static String isSupportedType(String inType) {
        String type = null;

        System.out.println("Type : " + inType + "\n\rJson ? " + inType.contains("json"));

        if (inType.contains("xml")) {
            type = inType;
        }
        if (inType.contains("json")) {
            type = inType;
        }
        if (inType.equals("text/plain")) {
            type = inType;
        }

        if (type == null) {
            type = "text/plain";
        }

        return type;
    }

    public static String getFirstSupportedType(String[] types) {
        String type = null;
        if (types != null && types.length > 0) {
            for (int i = 0; (i < types.length) && (type == null); i++) {
                if (types[i].contains("xml")) {
                    type = types[i];
                }
                if (types[i].contains("json")) {
                    type = types[i];
                }
            }
        }
        if (type == null) {
            type = "text/plain";
        }

        return type;
    }
}
