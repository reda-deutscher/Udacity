
package com.sig.model;

public class InvoicSaleseLine {
    private String item;
    private double price;
    private int count;
    private InvoiceSalesHeaderSales header;

    public InvoicSaleseLine() {
    }

    public InvoicSaleseLine(String item, double price, int count, InvoiceSalesHeaderSales header) {
        this.item = item;
        this.price = price;
        this.count = count;
        this.header = header;
    }

    public InvoiceSalesHeaderSales getHeader() {
        return header;
    }

    public void setHeader(InvoiceSalesHeaderSales header) {
        this.header = header;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public double getLineTotal() {
        return price * count;
    }

    @Override
    public String toString() {
        return header.getNum() + "," + item + "," + price + "," + count;
    }

    
    
}
