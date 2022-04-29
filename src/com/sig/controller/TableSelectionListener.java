/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceSalesHeaderSales;
import com.sig.model.InvoicSaleseLine;
import com.sig.model.InvoiceSalesLineTableModel;
import com.sig.view.InvoiceSalesFrame;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author DELL
 */
public class TableSelectionListener implements ListSelectionListener {

    private InvoiceSalesFrame framee;

    public TableSelectionListener(InvoiceSalesFrame framee) {
        this.framee = framee;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedInvIndex = framee.getInvHTbl().getSelectedRow();
        System.out.println("Invoice selected: " + selectedInvIndex);
        if (selectedInvIndex != -1) {
            InvoiceSalesHeaderSales selectedInv = framee.getInvoicesArray().get(selectedInvIndex);
            ArrayList<InvoicSaleseLine> lines = selectedInv.getLines();
            InvoiceSalesLineTableModel lineTableModel = new InvoiceSalesLineTableModel(lines);
            framee.setLinesArray(lines);
            framee.getInvLTbl().setModel(lineTableModel);
            framee.getCustNameLbl().setText(selectedInv.getCustomer());
            framee.getInvNumLbl().setText("" + selectedInv.getNum());
            framee.getInvTotalIbl().setText("" + selectedInv.getInvoiceTotal());
            framee.getInvDateLbl().setText(InvoiceSalesFrame.dateFormat.format(selectedInv.getInvDate()));
        }
    }

}
