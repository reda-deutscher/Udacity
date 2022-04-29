/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceSalesHeaderSales;
import com.sig.model.InvoiceSalesHeaderTableModel;
import com.sig.model.InvoicSaleseLine;
import com.sig.model.InvoiceSalesLineTableModel;
import com.sig.view.InvoiceSalesFrame;
import com.sig.view.InvoiceSalesHeaderDialog;
import com.sig.view.InvoiceSalesLineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class InvoiceSalesActionListener implements ActionListener {

    private InvoiceSalesFrame framee;
    private InvoiceSalesHeaderDialog headerSalesDialog;
    private InvoiceSalesLineDialog lineSalesDialog;

    public InvoiceSalesActionListener(InvoiceSalesFrame frame) {
        this.framee = frame;
    }

   

    private void loadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showOpenDialog(framee);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceSalesHeaderSales> invoiceHeaders = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] arry = headerLine.split(",");
                    String str1 = arry[0];
                    String str2 = arry[1];
                    String str3 = arry[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = InvoiceSalesFrame.dateFormat.parse(str2);
                    InvoiceSalesHeaderSales header = new InvoiceSalesHeaderSales(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
                }
                framee.setInvoicesArray(invoiceHeaders);

                result = fileChooser.showOpenDialog(framee);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    ArrayList<InvoicSaleseLine> invoiceLines = new ArrayList<>();
                    for (String lineLine : lineLines) {
                        String[] array = lineLine.split(",");
                        String str1 = array[0];    // invoice num (int)
                        String str2 = array[1];    // item name   (String)
                        String str3 = array[2];    // price       (double)
                        String str4 = array[3];    // count       (int)
                        int invCode = Integer.parseInt(str1);
                        double price = Double.parseDouble(str3);
                        int count = Integer.parseInt(str4);
                        InvoiceSalesHeaderSales inv = framee.getInvObject(invCode);
                        InvoicSaleseLine line = new InvoicSaleseLine(str2, price, count, inv);
                        inv.getLines().add(line);
                    }
                }
                InvoiceSalesHeaderTableModel headerTableModel = new InvoiceSalesHeaderTableModel(invoiceHeaders);
                framee.setHeaderTableModel(headerTableModel);
                framee.getInvHTbl().setModel(headerTableModel);
                System.out.println("files read");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(framee, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(framee, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInvoice() {
        headerSalesDialog = new InvoiceSalesHeaderDialog(framee);
        headerSalesDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedInvoiceIndex = framee.getInvHTbl().getSelectedRow();
        if (selectedInvoiceIndex != -1) {
            framee.getInvoicesArray().remove(selectedInvoiceIndex);
            framee.getHeaderTableModel().fireTableDataChanged();
            framee.getInvLTbl().setModel(new InvoiceSalesLineTableModel(null));
            framee.setLinesArray(null);
            framee.getCustNameLbl().setText("");
            framee.getInvNumLbl().setText("");
            framee.getInvTotalIbl().setText("");
            framee.getInvDateLbl().setText("");
        }
    }

    

    private void deleteLine() {
        int selectedLineIndex = framee.getInvLTbl().getSelectedRow();
        int selectedInvoiceIndex = framee.getInvHTbl().getSelectedRow();
        if (selectedLineIndex != -1) {
            framee.getLinesArray().remove(selectedLineIndex);
            InvoiceSalesLineTableModel lineTableModel = (InvoiceSalesLineTableModel) framee.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            framee.getInvTotalIbl().setText("" + framee.getInvoicesArray().get(selectedInvoiceIndex).getInvoiceTotal());
            framee.getHeaderTableModel().fireTableDataChanged();
            framee.getInvHTbl().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

    private void saveFiles() {
        ArrayList<InvoiceSalesHeaderSales> invoicesArray = framee.getInvoicesArray();
        JFileChooser fcc = new JFileChooser();
        try {
            int result = fcc.showSaveDialog(framee);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fcc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                String headers = "";
                String lines = "";
                for (InvoiceSalesHeaderSales invoice : invoicesArray) {
                    headers += invoice.toString();
                    headers += "\n";
                    for (InvoicSaleseLine line : invoice.getLines()) {
                        lines += line.toString();
                        lines += "\n";
                    }
                }
                //  w e l c o m e
                //  0 1 2 3 4 5 6
                //  1 2 3 4 5 6 7
                headers = headers.substring(0, headers.length()-1);
                lines = lines.substring(0, lines.length()-1);
                result = fcc.showSaveDialog(framee);
                File lineFile = fcc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                hfw.write(headers);
                lfw.write(lines);
                hfw.close();
                lfw.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(framee, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newInvoiceDialogCancel() {
        headerSalesDialog.setVisible(false);
        headerSalesDialog.dispose();
        headerSalesDialog = null;
    }

    private void newInvoiceDialogOK() {
        headerSalesDialog.setVisible(false);

        String custName = headerSalesDialog.getCustNameField().getText();
        String str = headerSalesDialog.getInvDateField().getText();
        Date d = new Date();
        try {
            d = InvoiceSalesFrame.dateFormat.parse(str);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(framee, "Cannot parse date, resetting to today.", "Invalid date format", JOptionPane.ERROR_MESSAGE);
        }

        int invNum = 0;
        for (InvoiceSalesHeaderSales inv : framee.getInvoicesArray()) {
            if (inv.getNum() > invNum) {
                invNum = inv.getNum();
            }
        }
        invNum++;
        InvoiceSalesHeaderSales newInv = new InvoiceSalesHeaderSales(invNum, custName, d);
        framee.getInvoicesArray().add(newInv);
        framee.getHeaderTableModel().fireTableDataChanged();
        headerSalesDialog.dispose();
        headerSalesDialog = null;
    }

    private void newLineDialogCancel() {
        lineSalesDialog.setVisible(false);
        lineSalesDialog.dispose();
        lineSalesDialog = null;
    }

    private void newLineDialogOK() {
        lineSalesDialog.setVisible(false);

        String name = lineSalesDialog.getItemNameField().getText();
        String str1 = lineSalesDialog.getItemCountField().getText();
        String str2 = lineSalesDialog.getItemPriceField().getText();
        int count = 1;
        double price = 1;
        try {
            count = Integer.parseInt(str1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(framee, "Cannot convert number", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }

        try {
            price = Double.parseDouble(str2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(framee, "Cannot convert price", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }
        int selectedInvHeader = framee.getInvHTbl().getSelectedRow();
        if (selectedInvHeader != -1) {
            InvoiceSalesHeaderSales invHeader = framee.getInvoicesArray().get(selectedInvHeader);
            InvoicSaleseLine line = new InvoicSaleseLine(name, price, count, invHeader);
            //invHeader.getLines().add(line);
            framee.getLinesArray().add(line);
            InvoiceSalesLineTableModel lineTableModel = (InvoiceSalesLineTableModel) framee.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            framee.getHeaderTableModel().fireTableDataChanged();
        }
        framee.getInvHTbl().setRowSelectionInterval(selectedInvHeader, selectedInvHeader);
        lineSalesDialog.dispose();
        lineSalesDialog = null;
    }
    private void createNewLine() {
        lineSalesDialog = new InvoiceSalesLineDialog(framee);
        lineSalesDialog.setVisible(true);
    }
     @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Save Files":
                saveFiles();
                break;

            case "Load Files":
                loadFiles();
                break;

            case "New Invoice":
                createNewInvoice();
                break;

            case "Delete Invoice":
                deleteInvoice();
                break;

            case "New Line":
                createNewLine();
                break;

            case "Delete Line":
                deleteLine();
                break;

            case "newInvoiceOK":
                newInvoiceDialogOK();
                break;

            case "newInvoiceCancel":
                newInvoiceDialogCancel();
                break;

            case "newLineCancel":
                newLineDialogCancel();
                break;

            case "newLineOK":
                newLineDialogOK();
                break;
        }
    }

}
