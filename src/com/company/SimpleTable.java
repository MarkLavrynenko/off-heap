package com.company;

/**
 * Created by mlavrynenko on 13.06.2015.
 */
public class SimpleTable implements ITable {
    public SimpleTable(int tradesCount) {
        this._tradesCount = tradesCount;
    }
    private int _tradesCount;

    @Override
    public void singleStep() {
        long start = System.currentTimeMillis();
        init();
        System.out.format("Memory: total %,d  free %,d\n",
                Runtime.getRuntime().totalMemory(),
                Runtime.getRuntime().freeMemory());
        long Bprices = 0, Aprices = 0;
        for (int i = 0; i < _tradesCount; ++i) {
            if (trades[i].getSide() == 'B') {
                Bprices += trades[i].getProductPrice();
            } else {
                Aprices += trades[i].getProductPrice();
            }
        }
        long finish = System.currentTimeMillis();
        System.out.format("Time ellapsed %d ms\n", finish - start);
    }
    private JavaMemoryTrade[] trades;

    @Override
    public void init() {
        trades = new JavaMemoryTrade[_tradesCount];
        for (int i = 0; i <_tradesCount; ++i) {
            trades[i] = new JavaMemoryTrade();
            trades[i].setProductPrice(i * 10 % 100);
            trades[i].setVenueCode((i * 3) % 14);
            trades[i].setSide(i % 2 == 0 ? 'B' : 'A');
        }
    }

    @Override
    public void cleanUp() {
        trades = null;
    }
}
