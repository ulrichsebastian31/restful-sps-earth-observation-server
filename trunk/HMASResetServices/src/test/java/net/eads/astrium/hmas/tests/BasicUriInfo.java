/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               BasicUriInfo.java
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
package net.eads.astrium.hmas.tests;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author re-sulrich
 */
public class BasicUriInfo implements UriInfo{

    private MultivaluedMap<String,String> queryParameters;
    private MultivaluedMap<String,String> pathParameters;

    public BasicUriInfo(MultivaluedMap<String, String> queryParameters,
                          MultivaluedMap<String, String> pathParameters) {
        this.queryParameters = queryParameters;
        this.pathParameters = pathParameters;
    }

    public void setQueryParameters(MultivaluedMap<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public void setPathParameters(MultivaluedMap<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }
    
    @Override
    public String getPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPath(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PathSegment> getPathSegments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PathSegment> getPathSegments(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI getRequestUri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI getAbsolutePath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI getBaseUri() {
        return URI.create("http://something.com");
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean bln) {
        return pathParameters;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        return queryParameters;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean bln) {
        return queryParameters;
    }

    @Override
    public List<String> getMatchedURIs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getMatchedURIs(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Object> getMatchedResources() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

