/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.database;

import net.eads.astrium.hmas.dbhandler.DBOperations;
import net.eads.astrium.hmas.dbhandler.SensorLoader;

/**
 *
 * @author re-sulrich
 */
public abstract class EOSPSDBHandler extends DBHandler{

    public EOSPSDBHandler(DBOperations dboperations) {
        super(dboperations);
    }

    public EOSPSDBHandler(String databaseName) {
        super(databaseName);
    }

    public EOSPSDBHandler(String databaseURL, String user, String pass, String schema) {
        super(databaseURL, user, pass, schema);
    }
    
    public abstract SensorLoader getSensorLoader();
    
}
