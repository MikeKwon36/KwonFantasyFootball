package com.kwon.mike.pr2;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
/**
 * Created by Todo on 2/4/2016.
 */

public class UnitTests {

    public void sampleUnitTest(){
        int x = 2;
        int y = 4;
        int z = 2;
        int expectedValue = 16;
        int actualValue = MathUtils.multiply(x,y,z);

        assertEquals(expectedValue,actualValue);
        assertEquals(4, 2 + 2);
        assertTrue(true);
        assertFalse(false);
        assertNull(null);
        assertNotNull("Not null");
    }

}
