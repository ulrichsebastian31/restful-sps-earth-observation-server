/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.hmasdbinitialiser;

/**
 *
 * @author re-sulrich
 */
public class TestConnexionParameter {
    private static String url;
    private static String user;
    private static String pass;
    private static String mmfasDatabase;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        TestConnexionParameter.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        TestConnexionParameter.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        TestConnexionParameter.pass = pass;
    }

    public static String getMmfasDatabase() {
        return mmfasDatabase;
    }

    public static void setMmfasDatabase(String mmfasDatabase) {
        TestConnexionParameter.mmfasDatabase = mmfasDatabase;
    }

}
