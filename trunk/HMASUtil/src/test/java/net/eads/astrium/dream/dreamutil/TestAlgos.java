/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.dreamutil;

import java.util.ArrayList;
import java.util.List;
import net.eads.astrium.hmas.util.Algorithms;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestAlgos {
    
    @Test
    public void testRandom() {
        
        int nbNumbers = 100;
        testLoadsOfRandom(nbNumbers);
    }
//    @Test
    public void testRandomBigSpan() {
        
        int nbNumbers = 20;
        testLoadsOfRandom(nbNumbers);
    }
    
    public void testLoadsOfRandom(int nbNumbers) {
        
        int[] results = new int[nbNumbers + 1];
        
        for (int i = 0; i < 1000; i++) {
            int res = Algorithms.getRandom(0, nbNumbers);
            results[res]++;
        }
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("Nb " + i + " : " + results[i]);
        }
    }
//    @Test
    public void testLoop() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println("" + i + " - " + j);
                if (j == i) break;
            }
        }
    }
}
