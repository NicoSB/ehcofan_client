package com.nicosb.apps.ehcofan;

import com.nicosb.apps.ehcofan.models.Match;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Nico on 19.08.2016.
 */
public class CacheTest {
    @Test
    public void decode_isCorrect_3numbers(){
        String scores = "1;2;3";
        int[] decoded = CacheDBHelper.decodeScores(scores);

        Assert.assertEquals(1, decoded[0]);
        Assert.assertEquals(2, decoded[1]);
        Assert.assertEquals(3, decoded[2]);
    }

    @Test
    public void decode_isCorrect_4numbers(){
        String scores = "1;2;3;4";
        int[] decoded = CacheDBHelper.decodeScores(scores);

        Assert.assertEquals(1, decoded[0]);
        Assert.assertEquals(2, decoded[1]);
        Assert.assertEquals(3, decoded[2]);
        Assert.assertEquals(4, decoded[3]);
    }


    @Test
    public void decode_isCorrect_doubleDigits(){
        String scores = "10;2;30;4";
        int[] decoded = CacheDBHelper.decodeScores(scores);

        Assert.assertEquals(10, decoded[0]);
        Assert.assertEquals(2, decoded[1]);
        Assert.assertEquals(30, decoded[2]);
        Assert.assertEquals(4, decoded[3]);
    }

    @Test
    public void encode_isCorrect(){
        int scores[] = {1,2,3,4,5};
        String encoded = CacheDBHelper.encodeScores(scores);

        Assert.assertEquals("1;2;3;4;5", encoded);
    }
}
