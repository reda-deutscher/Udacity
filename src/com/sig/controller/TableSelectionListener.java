/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceLine;
import com.sig.model.InvoiceLineTableModel;
import com.sig.view.InvoiceFrame;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author DELL
 */
public class TableSelectionListener implements ListSelectionListener {

    private InvoiceFrame frameInvo;

    public TableSelectionListener(InvoiceFrame frame) {
        this.frameInvo = frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedInvIndex = frameInvo.getInvHTbl().getSelectedRow();
        System.out.println("Invoice selected: " + selectedInvIndex);
        if (selectedInvIndex != -1) {
            InvoiceHeader selectedInv = frameInvo.getInvoicesArray().get(selectedInvIndex);
            ArrayList<InvoiceLine> lines = selectedInv.getLines();
            InvoiceLineTableModel lineTableModel = new InvoiceLineTableModel(lines);
            frameInvo.setLinesArray(lines);
            frameInvo.getInvLTbl().setModel(lineTableModel);
            frameInvo.getCustNameLbl().setText(selectedInv.getCustomer());
            frameInvo.getInvNumLbl().setText("" + selectedInv.getNumber());
            frameInvo.getInvTotalIbl().setText("" + selectedInv.getInvoiceTotal());
            frameInvo.getInvDateLbl().setText(InvoiceFrame.dateFormat.format(selectedInv.getInvoiceDate()));
        }
    }

}
