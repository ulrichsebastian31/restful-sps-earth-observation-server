/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.workers;

import net.eads.astrium.hmas.database.EOSPSDBHandler;
import net.eads.astrium.hmas.exceptions.GetCapabilitiesFault;
import net.eads.astrium.hmas.operations.GetCapabilitiesOperation;
import net.opengis.sps.x21.CapabilitiesDocument;
import net.opengis.sps.x21.GetCapabilitiesDocument;

/**
 *
 * @author re-sulrich
 */
public abstract class GenericEOSPSWorker implements EOSPSBasicWorker {
    
    @Override
    public CapabilitiesDocument getCapabilities(GetCapabilitiesDocument request) throws GetCapabilitiesFault {
        GetCapabilitiesOperation operation = 
                new GetCapabilitiesOperation(
                        (EOSPSDBHandler)getDatabaseConfiguration(), request, getServerBaseURI(),this);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }
}
