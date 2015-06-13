package com.company;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by mlavrynenko on 13.06.2015.
 */
public class AdvancedTable implements ITable {
    public AdvancedTable(int tradesCount) {
        this._tradesCount = tradesCount;
    }
    private int _tradesCount;
    private Unsafe _unsafe;
    private long _address = -1;
    private DirectMemoryTrade flyweight;

    private static class DirectMemoryTrade {
        private static final long venueOffset        = 0;
        private static final long productPriceOffset = 8;
        private static final long sideOffset         = 16;
        private static long objectSize               = 18;

        private Unsafe _unsafe;
        private static long _objectOffset;
        public DirectMemoryTrade(Unsafe unsafe) {
            this._unsafe = unsafe;
        }

        public void setObjOffset(long offset) {
            this._objectOffset = offset;
        }

        public static long getObjSize() {
            return objectSize;
        }

        public void setVenueCode(int venueCode) {
            _unsafe.putInt(_objectOffset + venueOffset, venueCode);
        }

        public void setProductPrice(long productPrice) {
            _unsafe.putLong(_objectOffset + productPriceOffset, productPrice);
        }

        public void setSide(char side) {
            _unsafe.putChar(_objectOffset + sideOffset, side);
        }

        public long getVenueCode() {
            return _unsafe.getLong(_objectOffset + venueOffset);
        }

        public long getProductPrice() {
            return _unsafe.getLong(_objectOffset + productPriceOffset);
        }

        public char getSide() {
            return _unsafe.getChar(_objectOffset + sideOffset);
        }
    }

    private  DirectMemoryTrade getConfiguredFlyweight(int index) {
        long offset = index * DirectMemoryTrade.getObjSize();
        flyweight.setObjOffset(_address + offset);
        return flyweight;
    }

    @Override
    public void singleStep() {
        long start = System.currentTimeMillis();
        System.out.format("Memory: total %,d  free %,d\n",
                Runtime.getRuntime().totalMemory(),
                Runtime.getRuntime().freeMemory());
        long Bprices = 0, Aprices = 0;
        for (int i = 0; i < _tradesCount; ++i) {
            DirectMemoryTrade trade = getConfiguredFlyweight(i);
            if (trade.getSide() == 'B') {
                Bprices += trade.getProductPrice();
            } else {
                Aprices += trade.getProductPrice();
            }
        }
        long finish = System.currentTimeMillis();
        System.out.format("Time elapsed %d ms. Sums are %d, %d \n", finish - start, Aprices, Bprices);
    }

    @Override
    public void init() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            _unsafe = (Unsafe)field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        _address = _unsafe.allocateMemory(_tradesCount * DirectMemoryTrade.getObjSize());
        flyweight = new DirectMemoryTrade(_unsafe);
        System.out.format("address of allocated memory is %d\n", _address);
        for (int i = 0; i < _tradesCount; ++i){
            DirectMemoryTrade trade = getConfiguredFlyweight(i);
            trade.setVenueCode(i % 20);
            trade.setProductPrice((i * 30) % 100);
            trade.setSide(i % 3 == 0 ? 'B' : 'A');
        }
    }

    @Override
    public void cleanUp() {
        _unsafe.freeMemory(_address);
        _address = -1;
    }
}
