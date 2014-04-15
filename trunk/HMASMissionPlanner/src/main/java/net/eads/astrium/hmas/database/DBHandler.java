/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DBHandler.java
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
package net.eads.astrium.hmas.database;

import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.dbhandler.DatabaseLoader;

/**
 *
 * @author re-sulrich
 */
public abstract class DBHandler extends DatabaseLoader {

    public DBHandler(DBOperations dboperations) {
        super(dboperations);
    }

    public DBHandler(String databaseName) {
        super(databaseName);
    }

    public DBHandler(String databaseURL, String user, String pass, String schema) {
        super(databaseURL, user, pass, schema);
    }
    
    public abstract String getService();
    public abstract String getServiceId();
}
