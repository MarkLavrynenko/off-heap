package com.company;

public class Main {
    private static final int tradesCount = 5 * 1000 * 1000;
    public static void main(String[] args) {
        ITable table = (args[0].equals("simple")) ? new SimpleTable(tradesCount) : new AdvancedTable(tradesCount);
        profile(table);
    }

    private static void profile(ITable table) {
        table.init();
        for (int i = 0; i < 5; ++i){
            System.gc();
            table.singleStep();
        }
        table.cleanUp();
    }
}
