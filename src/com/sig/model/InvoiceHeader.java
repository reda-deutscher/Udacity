
package com.sig.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    private int number;
    private String customer;
    private Date invoiceDate;
    private ArrayList<InvoiceLine> lines;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public InvoiceHeader() {
    }

    public InvoiceHeader(int number, String customer, Date invoiceDate) {
        this.number = number;
        this.customer = customer;
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getNumber() {
        return number;
    }

   

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
     public void setNumber(int number) {
        this.number = number;
    }

    

    public void setLines(ArrayList<InvoiceLine> lines) {
        this.lines = lines;
    }
    
    public double getInvoiceTotal() {
        double total = 0.0;
        
        for (int i = 0; i < getLines().size(); i++) {
            total += getLines().get(i).getLineTotal();
        }
        
        return total;
    }

    @Override
    public String toString() {
        return number + "," + dateFormat.format(invoiceDate) + "," + customer;
    }
    public ArrayList<InvoiceLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }
    
}
