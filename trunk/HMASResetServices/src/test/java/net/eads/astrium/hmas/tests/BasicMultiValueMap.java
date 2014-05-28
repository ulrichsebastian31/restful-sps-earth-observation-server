/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               BasicMultiValueMap.java
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
 *    Constellation - An open source and standard compliant SDI
 *    http://www.constellation-sdi.org
 *
 *    (C) 2010, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package net.eads.astrium.hmas.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author re-sulrich
 */
public class BasicMultiValueMap<K,V> extends HashMap<K, List<V>> implements MultivaluedMap<K, V>{

    @Override
    public void putSingle(K k, V v) {
        final List<V> values = new ArrayList<V>();
        values.add(v);
        put(k, values);
    }

    @Override
    public void add(K k, V v) {
        List<V> values = get(k);
        if(values == null){
            putSingle(k, v);
        }else{
            values.add(v);
        }
    }

    @Override
    public V getFirst(K k) {
        List<V> values = get(k);
        if(values == null || values.isEmpty()){
            return null;
        }else{
            return values.get(0);
        }
    }

}
