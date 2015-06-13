package com.company;

public class JavaMemoryTrade {
    public int getVenueCode() {
        return venueCode;
    }

    public void setVenueCode(int venueCode) {
        this.venueCode = venueCode;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    private int venueCode;
    private char side;
    private long productPrice;
}
