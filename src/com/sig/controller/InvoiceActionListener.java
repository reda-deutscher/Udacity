/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceHeaderTableModel;
import com.sig.model.InvoiceLine;
import com.sig.model.InvoiceLineTableModel;
import com.sig.view.InvoiceFrame;
import com.sig.view.InvoiceHeaderDialog;
import com.sig.view.InvoiceLineDialog;
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
public class InvoiceActionListener implements ActionListener {

    private InvoiceFrame frameInvo;
    private InvoiceHeaderDialog headerDialog;
    private InvoiceLineDialog lineDialog;

    public InvoiceActionListener(InvoiceFrame frame) {
        this.frameInvo = frame;
    }

   

    private void loadInvoFiles() {
        JFileChooser fileInvoChooser = new JFileChooser();
        try {
            int result = fileInvoChooser.showOpenDialog(frameInvo);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileInvoChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] arry = headerLine.split(",");
                    String str1 = arry[0];
                    String str2 = arry[1];
                    String str3 = arry[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = InvoiceFrame.dateFormat.parse(str2);
                    InvoiceHeader header = new InvoiceHeader(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
                }
                frameInvo.setInvoicesArray(invoiceHeaders);

                result = fileInvoChooser.showOpenDialog(frameInvo);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileInvoChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
                    for (String lineLine : lineLines) {
                        String[] array = lineLine.split(",");
                        String str1 = array[0];    // invoice num (int)
                        String str2 = array[1];    // item name   (String)
                        String str3 = array[2];    // price       (double)
                        String str4 = array[3];    // count       (int)
                        int invCode = Integer.parseInt(str1);
                        double price = Double.parseDouble(str3);
                        int count = Integer.parseInt(str4);
                        InvoiceHeader inv = frameInvo.getInvObject(invCode);
                        InvoiceLine line = new InvoiceLine(str2, price, count, inv);
                        inv.getLines().add(line);
                    }
                }
                InvoiceHeaderTableModel headerTableModel = new InvoiceHeaderTableModel(invoiceHeaders);
                frameInvo.setHeaderTableModel(headerTableModel);
                frameInvo.getInvHTbl().setModel(headerTableModel);
                System.out.println("files read");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frameInvo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frameInvo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInvoice() {
        headerDialog = new InvoiceHeaderDialog(frameInvo);
        headerDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedInvoiceIndex = frameInvo.getInvHTbl().getSelectedRow();
        if (selectedInvoiceIndex != -1) {
            frameInvo.getInvoicesArray().remove(selectedInvoiceIndex);
            frameInvo.getHeaderTableModel().fireTableDataChanged();

            frameInvo.getInvLTbl().setModel(new InvoiceLineTableModel(null));
            frameInvo.setLinesArray(null);
            frameInvo.getCustNameLbl().setText("");
            frameInvo.getInvNumLbl().setText("");
            frameInvo.getInvTotalIbl().setText("");
            frameInvo.getInvDateLbl().setText("");
        }
    }

    private void createNewLine() {
        lineDialog = new InvoiceLineDialog(frameInvo);
        lineDialog.setVisible(true);
    }

    private void deleteLine() {
        int selectedLineIndex = frameInvo.getInvLTbl().getSelectedRow();
        int selectedInvoiceIndex = frameInvo.getInvHTbl().getSelectedRow();
        if (selectedLineIndex != -1) {
            frameInvo.getLinesArray().remove(selectedLineIndex);
            InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) frameInvo.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            frameInvo.getInvTotalIbl().setText("" + frameInvo.getInvoicesArray().get(selectedInvoiceIndex).getInvoiceTotal());
            frameInvo.getHeaderTableModel().fireTableDataChanged();
            frameInvo.getInvHTbl().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

    private void saveFiles() {
        ArrayList<InvoiceHeader> invoicesArray = frameInvo.getInvoicesArray();
        JFileChooser jfc = new JFileChooser();
        try {
            int result = jfc.showSaveDialog(frameInvo);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = jfc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                String headers = "";
                String lines = "";
                for (InvoiceHeader invoice : invoicesArray) {
                    headers += invoice.toString();
                    headers += "\n";
                    for (InvoiceLine line : invoice.getLines()) {
                        lines += line.toString();
                        lines += "\n";
                    }
                }
                //  w e l c o m e
                //  0 1 2 3 4 5 6
                //  1 2 3 4 5 6 7
                headers = headers.substring(0, headers.length()-1);
                lines = lines.substring(0, lines.length()-1);
                result = jfc.showSaveDialog(frameInvo);
                File lineFile = jfc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                hfw.write(headers);
                lfw.write(lines);
                hfw.close();
                lfw.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frameInvo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newInvoiceDialogCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newInvoiceDialogOK() {
        headerDialog.setVisible(false);

        String customName = headerDialog.getCustNameField().getText();
        String str = headerDialog.getInvDateField().getText();
        Date d = new Date();
        try {
            d = InvoiceFrame.dateFormat.parse(str);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frameInvo, "Cannot parse date, resetting to today.", "Invalid date format", JOptionPane.ERROR_MESSAGE);
        }

        int invNum = 0;
        for (InvoiceHeader inv : frameInvo.getInvoicesArray()) {
            if (inv.getNumber() > invNum) {
                invNum = inv.getNumber();
            }
        }
        invNum++;
        InvoiceHeader newInvice = new InvoiceHeader(invNum, customName, d);
        frameInvo.getInvoicesArray().add(newInvice);
        frameInvo.getHeaderTableModel().fireTableDataChanged();
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newLineDialogCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void newLineDialogOK() {
        lineDialog.setVisible(false);

        String name = lineDialog.getItemNameField().getText();
        String str1 = lineDialog.getItemCountField().getText();
        String str2 = lineDialog.getItemPriceField().getText();
        int count = 1;
        double price = 1;
        try {
            count = Integer.parseInt(str1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frameInvo, "Cannot convert number", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }

        try {
            price = Double.parseDouble(str2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frameInvo, "Cannot convert price", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }
        int selectedInvHeader = frameInvo.getInvHTbl().getSelectedRow();
        if (selectedInvHeader != -1) {
            InvoiceHeader invHeader = frameInvo.getInvoicesArray().get(selectedInvHeader);
            InvoiceLine line = new InvoiceLine(name, price, count, invHeader);
            //invHeader.getLines().add(line);
            frameInvo.getLinesArray().add(line);
            InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) frameInvo.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            frameInvo.getHeaderTableModel().fireTableDataChanged();
        }
        frameInvo.getInvHTbl().setRowSelectionInterval(selectedInvHeader, selectedInvHeader);
        lineDialog.dispose();
        lineDialog = null;
    }
     @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Save Files":
                saveFiles();
                break;

            case "Load Files":
                loadInvoFiles();
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
