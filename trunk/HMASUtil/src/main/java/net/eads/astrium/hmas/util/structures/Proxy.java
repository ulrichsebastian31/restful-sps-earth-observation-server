/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.structures;

/**
 *
 * @author re-sulrich
 */
public class Proxy {
    
    private String proxyId;
    private String proxyAddress;
    private int proxyPort;
    private String proxyUser;
    private String proxyPassword;


    /**
     * Copy constructor
     * Permits to create a new instance with the same parameters as the parameter
     * Permits proper casting
     * @param p 
     */
    public Proxy(Proxy p) {

        this.proxyId = p.getProxyId();
        this.proxyAddress = p.getProxyAddress();
        this.proxyPort = p.getProxyPort();
        this.proxyUser = p.getProxyUser();
        this.proxyPassword = p.getProxyPassword();
    }
    
    /**
     * Constructor with required parameters
     * @param proxyId
     * @param proxyAddress
     * @param proxyPort 
     */
    public Proxy(String proxyId, String proxyAddress, int proxyPort) {
        this.proxyId = proxyId;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        this.proxyUser = null;
        this.proxyPassword = null;
    }
    
    /**
     * Constructor with all parameters
     * Permits to put a proxy that needs authentication
     * @param proxyId
     * @param proxyAddress
     * @param proxyPort
     * @param proxyUser
     * @param proxyPassword 
     */
    public Proxy(String proxyId, String proxyAddress, int proxyPort, String proxyUser, String proxyPassword) {
        this.proxyId = proxyId;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPassword = proxyPassword;
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    @Override
    public String toString() {
        return "Proxy{" + "proxyId=" + proxyId + ", proxyAddress=" + proxyAddress + ", proxyPort=" + proxyPort + ", proxyUser=" + proxyUser + ", proxyPassword=" + proxyPassword + '}';
    }
}
