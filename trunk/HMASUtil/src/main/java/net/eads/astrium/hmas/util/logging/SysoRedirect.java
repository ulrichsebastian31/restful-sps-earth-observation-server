/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author re-sulrich
 */
public class SysoRedirect {
    
    public static void redirectSysoToFiles(String service, String instanceId) throws Exception
   {
        String confFolderBasePath = System.getProperty("user.home") + File.separator + ".dream" + File.separator + 
                service + File.separator + 
                instanceId + File.separator + "log" + File.separator;
        
        new File(confFolderBasePath).mkdirs();
        
       System.setOut(outputFile(confFolderBasePath + "out"));
       System.setErr(outputFile(confFolderBasePath + "err"));
   }
   protected static PrintStream outputFile(String name) throws IOException {
       if (!new File(name).exists())
        new File(name).createNewFile();
       return new PrintStream(new FileOutputStream(new File(name), true), true);
   }
}
