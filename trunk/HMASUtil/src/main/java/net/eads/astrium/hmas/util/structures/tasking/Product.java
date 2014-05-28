/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.util.structures.tasking;

import java.util.Date;

/**
 *
 * @author re-sulrich
 */
public class Product {

    private String productID;
    private String mmfasSegmentID;
    private String downloadURL;
    private boolean isAvailable;
    private Date lastUpdateTime;
    private long size;
    
    public Product(
            String productID, 
            String mmfasSegmentID, 
            String downloadURL, 
            boolean isAvailable, 
            Date lastUpdateTime, 
            long size) {
        this.productID = productID;
        this.mmfasSegmentID = mmfasSegmentID;
        this.downloadURL = downloadURL;
        this.isAvailable = isAvailable;
        this.lastUpdateTime = lastUpdateTime;
        this.size = size;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getMmfasSegmentID() {
        return mmfasSegmentID;
    }

    public void setMmfasSegmentID(String mmfasSegmentID) {
        this.mmfasSegmentID = mmfasSegmentID;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "Product{" + 
                "productID=" + productID + ", "
                + "mmfasResultID=" + mmfasSegmentID + ", "
                + "downloadURL=" + downloadURL + ", "
                + "isAvailable=" + isAvailable + ", "
                + "lastUpdateTime=" + lastUpdateTime + ", "
                + "size=" + size + '}';
    }
}
