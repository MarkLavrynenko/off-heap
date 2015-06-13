package com.company;

import junit.framework.TestCase;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class AdvancedTableTest extends TestCase {
    private Unsafe _unsafe;
    private long _address = -1;
    private AdvancedTable.DirectMemoryTrade flyweight;

    public void testVenueCode() {
        final int venueCode = 213214211;
        flyweight.setVenueCode(venueCode);
        assertEquals (venueCode, flyweight.getVenueCode());
    }

    public void testProductPrice() {
        final int productPrice =  321333;
        flyweight.setProductPrice(productPrice);
        assertEquals (productPrice, flyweight.getProductPrice());
    }

    public void testSide() {
        final char side = 'G';
        flyweight.setSide(side);
        assertEquals(side, flyweight.getSide());
    }

    public void testIntegration() {
        final char side = 'G';
        final int productPrice = 331321312;
        final int venueCode = 213214211;

        flyweight.setSide(side);
        flyweight.setProductPrice(productPrice);
        flyweight.setVenueCode(venueCode);

        assertEquals(side, flyweight.getSide());
        assertEquals(productPrice, flyweight.getProductPrice());
        assertEquals(venueCode, flyweight.getVenueCode());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        _unsafe = (Unsafe)field.get(null);
        _address = _unsafe.allocateMemory(100);
        flyweight = new AdvancedTable.DirectMemoryTrade(_unsafe);
        flyweight.setObjOffset(_address);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        if (_address != -1) {
            _unsafe.freeMemory(_address);
            _address = -1;
        }
    }
}