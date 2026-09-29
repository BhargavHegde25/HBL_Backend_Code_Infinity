package com.temenos.dbx.filesgenerator.businessdelegate.impl;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.dbx.filesgenerator.businessdelegate.api.TransactionReportPDFGeneratorBD;
import com.temenos.dbx.transaction.dto.BankDTO;
import com.temenos.dbx.transaction.dto.TransactionDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionReportPDFGeneratorBDImpl implements TransactionReportPDFGeneratorBD {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final Color TRANS_TABLE_FIELD_FONT_COLOR = new Color(128, 128, 128);
    private final Color TRANS_TABLE_VALUE_FONT_COLOR = new Color(0, 0, 0);
    private final String BASE_CURRENCY=EnvironmentConfigurationsHandler.getServerProperty("HBL_BASE_CURRENCY");
    private final String HEAD_BRANCH=EnvironmentConfigurationsHandler.getServerProperty("HBL_HEAD_BRANCH");
    private TransactionDTO transaction;
    private BankDTO bank;

    @Override
    public byte[] generateFileAsByte(TransactionDTO transaction, BankDTO bank) {
        this.transaction = transaction;
        this.bank = bank;
        String type = "";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        Rectangle pageSize = document.getPageSize();
        final float pageTop = pageSize.getTop() - 70;
        final float pageLeft = pageSize.getLeft() + 45;
        PdfWriter writer = PdfWriter.getInstance(document, os);
        document.open();

        // main table
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        // add Logo
        addLogo(table, pageTop, pageLeft);

        if(StringUtils.isNotBlank(transaction.getFrequencyType())
                && "eSewa".equalsIgnoreCase(transaction.getFrequencyType())) {
        	// add esewa Transfer Details
            table.addCell(_createeSewaTransactionTable());
        }else {
        	// add Transfer Details
            table.addCell(_createBankTable());
        }
         
        document.add(table);
        try {
            os.close();
            document.close();
            writer.close();
        } catch (IOException e) {
            alert.prepareError("Error occurred while closing output stream").log();
        }

        return os.toByteArray();

    }


    private void addLogo(PdfPTable table, float pageTop, float pageLeft) {
        InputStream is = TransactionReportPDFGeneratorBDImpl.class.getClassLoader().getResourceAsStream("infinity_logo.png");

        try {
            Image infinityLogo = Image.getInstance(IOUtils.toByteArray(is));
            infinityLogo.setSpacingAfter(10);
            infinityLogo.scaleAbsolute(100, 28);
            infinityLogo.setAbsolutePosition(pageLeft, pageTop);
            PdfPCell cellLogo = new PdfPCell();
            cellLogo.addElement(infinityLogo);
            Paragraph bankAddrParagraph = new Paragraph(
            		HEAD_BRANCH,
                    new Font(Font.HELVETICA, 8, Font.NORMAL, TRANS_TABLE_FIELD_FONT_COLOR));
            cellLogo.addElement(bankAddrParagraph);
            cellLogo.setPaddingTop(15);
            cellLogo.setPaddingLeft(15f);
            cellLogo.setPaddingBottom(20);
            cellLogo.setBorder(Rectangle.BOX);
            cellLogo.setBorderColor(Color.DARK_GRAY);
            table.addCell(cellLogo);
        } catch (Exception e) {
            alert.prepareError("Error creating file", e).log();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    alert.prepareError("Error Occurred while adding infinity logo in PDF ", e).log();
                }
            }
        }
    }

    private PdfPTable _createBankTable() {

        PdfPTable bankTable = new PdfPTable(2);
        bankTable.setSpacingBefore(10);
        bankTable.setWidthPercentage(100);
        bankTable.setWidths(new float[]{0.5f, 2.2f});
        PdfPCell addrCell = new PdfPCell(new Paragraph("Transfer Details",
                new Font(Font.HELVETICA, 8, Font.NORMAL, TRANS_TABLE_VALUE_FONT_COLOR)));
        addrCell.setColspan(2);
        _setBorder(addrCell);
        bankTable.addCell(addrCell);
        bankTable.completeRow();

        bankTable.addCell(_noBorderCell3("Reference Number:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getTransactionId())) {
            bankTable.addCell(_noBorderCell1(transaction.getTransactionId(), TRANS_TABLE_VALUE_FONT_COLOR));
        }

        bankTable.completeRow();

        bankTable.addCell(_noBorderCell("From Account:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getFromNickName())) {
            bankTable.addCell(_noBorderCell2(transaction.getFromNickName(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        createRecipientCell(bankTable);
        bankTable.addCell(_noBorderCell("Date:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getTransactionDate())) {
            bankTable.addCell(_noBorderCell2(transaction.getTransactionDate(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        String transType=transaction.getTransactionType();
        String transCurrency= transaction.getTransactionCurrency();
        bankTable.addCell(_noBorderCell("Amount:", TRANS_TABLE_FIELD_FONT_COLOR));
        if(StringUtils.isNotBlank(transType) && transType.equalsIgnoreCase("BillPay")) {
        	bankTable.addCell(_noBorderCell2(transCurrency+" "+transaction.getTransactionAmount(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        else if (StringUtils.isNotBlank(transaction.getAmount())) {
            bankTable.addCell(_noBorderCell2(transaction.getAmount(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        bankTable.addCell(_noBorderCell("Payment Description:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getDescription())) {
            bankTable.addCell(_noBorderCell2(transaction.getDescription(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        bankTable.addCell(_noBorderCell("Transaction Type:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getTransactionType())) {
            bankTable.addCell(_noBorderCell2(transaction.getTransactionType(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getFrequencyType())
                && !"Once".equalsIgnoreCase(transaction.getFrequencyType())) {
            bankTable.addCell(_noBorderCell("Frequency:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transaction.getFrequencyType(), TRANS_TABLE_VALUE_FONT_COLOR));

        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getRecurrenceDesc())) {
            bankTable.addCell(_noBorderCell("Recurrence:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transaction.getRecurrenceDesc(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getFrequencyStartDate())) {
            bankTable.addCell(_noBorderCell("Start Date:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transaction.getFrequencyStartDate(), TRANS_TABLE_VALUE_FONT_COLOR));

        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getFrequencyEndDate())) {
            bankTable.addCell(_noBorderCell("End Date:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transaction.getFrequencyEndDate(), TRANS_TABLE_VALUE_FONT_COLOR));

        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getTransactionsNotes())) {
            bankTable.addCell(_noBorderCell("Note:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transaction.getTransactionsNotes(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        /*
         * Bill pay download transaction changes
         */
        if(StringUtils.isNotBlank(transType) && transType.equalsIgnoreCase("BillPay")) {
            bankTable.addCell(_noBorderCell("Total Debit Amount :", TRANS_TABLE_FIELD_FONT_COLOR));
            if (StringUtils.isNotBlank(transaction.getAmount())) {
            	transCurrency= BASE_CURRENCY;
                bankTable.addCell(_noBorderCell2(transCurrency+" "+formatToTwoDecimals(transaction.getAmount()), TRANS_TABLE_VALUE_FONT_COLOR));
            }
        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getServiceCharge())) {
            bankTable.addCell(_noBorderCell("Service Fee:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transCurrency+" "+formatToTwoDecimals(transaction.getServiceCharge()), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        if (StringUtils.isNotBlank(transaction.getExchangeRate())) {
            bankTable.addCell(_noBorderCell("Exchange Rate:", TRANS_TABLE_FIELD_FONT_COLOR));
            bankTable.addCell(_noBorderCell2(transaction.getExchangeRate(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        PdfPCell blankCell2 = new PdfPCell(new Paragraph(""));
        blankCell2.setColspan(2);
        blankCell2.setPaddingTop(6);
        blankCell2.setPaddingBottom(150);
        blankCell2.setBorderColor(Color.WHITE);
        bankTable.addCell(blankCell2);
        return bankTable;
    }
    
    public static String formatToTwoDecimals(String amountString) {
        BigDecimal amount = new BigDecimal(amountString);

        // 2. Set the scale to 2 and define the rounding mode
        // RoundingMode.HALF_UP is a common choice for rounding
        amount = amount.setScale(2, RoundingMode.HALF_UP);

        // 3. Convert the BigDecimal back to a String using String.format()
        // The "%.2f" format specifier ensures two decimal places
        return String.format("%.2f", amount);
    }

    
    private PdfPTable _createeSewaTransactionTable() {

        PdfPTable bankTable = new PdfPTable(2);
        bankTable.setSpacingBefore(10);
        bankTable.setWidthPercentage(100);
        bankTable.setWidths(new float[]{0.5f, 2.2f});
        PdfPCell addrCell = new PdfPCell(new Paragraph("eSewa Wallet Topup Details",
                new Font(Font.HELVETICA, 8, Font.NORMAL, TRANS_TABLE_VALUE_FONT_COLOR)));
        addrCell.setColspan(2);
        _setBorder(addrCell);
        bankTable.addCell(addrCell);
        bankTable.completeRow();

        bankTable.addCell(_noBorderCell3("Source Account Number:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getFromAccountNumber())) {
            bankTable.addCell(_noBorderCell1(transaction.getFromAccountNumber(), TRANS_TABLE_VALUE_FONT_COLOR));
        }

        bankTable.completeRow();

        bankTable.addCell(_noBorderCell("Sender Name:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getFromNickName())) {
            bankTable.addCell(_noBorderCell2(transaction.getFromNickName(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        //createRecipientCell(bankTable);
        bankTable.addCell(_noBorderCell("eSewa Id:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getServiceCharge())) {
            bankTable.addCell(_noBorderCell2(transaction.getServiceCharge(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        
        bankTable.addCell(_noBorderCell("Amount:", TRANS_TABLE_FIELD_FONT_COLOR));
        if(StringUtils.isNotBlank(transaction.getAmount())) {
        	bankTable.addCell(_noBorderCell2(transaction.getAmount(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        
        bankTable.completeRow();
       /* bankTable.addCell(_noBorderCell("SwiftCode:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getRecurrenceDesc())) {
            bankTable.addCell(_noBorderCell2(transaction.getRecurrenceDesc(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();*/
        bankTable.addCell(_noBorderCell("Transaction Date:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getTransactionDate())) {
            bankTable.addCell(_noBorderCell2(transaction.getTransactionDate(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        
        bankTable.addCell(_noBorderCell("Channel Name:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getCashlessPersonName())) {
            bankTable.addCell(_noBorderCell2(transaction.getCashlessPersonName(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        
        bankTable.addCell(_noBorderCell("Reference Id:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getTransactionId())) {
            bankTable.addCell(_noBorderCell2(transaction.getTransactionId(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        
        bankTable.addCell(_noBorderCell("Status:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getPayeeName())) {
            bankTable.addCell(_noBorderCell2(transaction.getPayeeName(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        
        bankTable.addCell(_noBorderCell("Purpose:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getDescription())) {
            bankTable.addCell(_noBorderCell2(transaction.getDescription(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow();
        
      /*  bankTable.addCell(_noBorderCell("Transaction Id:", TRANS_TABLE_FIELD_FONT_COLOR));
        if (StringUtils.isNotBlank(transaction.getTransactionCurrency())) {
            bankTable.addCell(_noBorderCell2(transaction.getTransactionCurrency(), TRANS_TABLE_VALUE_FONT_COLOR));
        }
        bankTable.completeRow(); */
        
        PdfPCell blankCell2 = new PdfPCell(new Paragraph(""));
        blankCell2.setColspan(2);
        blankCell2.setPaddingTop(6);
        blankCell2.setPaddingBottom(150);
        blankCell2.setBorderColor(Color.WHITE);
        bankTable.addCell(blankCell2);
        return bankTable;
    }
    
    
    public void createRecipientCell(PdfPTable transferTable) {
        String toRecipient = null;
        if (StringUtils.isNotBlank(transaction.getToNickName())) {
            toRecipient = transaction.getToNickName();
        } else if (StringUtils.isNotBlank(transaction.getToAccountName())) {
            toRecipient = transaction.getToAccountName();
        } else if (StringUtils.isNotBlank(transaction.getPayeeNickName())) {
            toRecipient = transaction.getPayeeNickName();
        } else if (StringUtils.isNotBlank(transaction.getPayeeName())) {
            toRecipient = transaction.getPayeeName();
        } else if (StringUtils.isNotBlank(transaction.getPayPersonName())) {
            toRecipient = transaction.getPayPersonName();
        } else if (StringUtils.isNotBlank(transaction.getPayPersonPhone())) {
            toRecipient = transaction.getPayPersonPhone();
        } else if (StringUtils.isNotBlank(transaction.getPayPersonEmail())) {
            toRecipient = transaction.getPayPersonEmail();
        }
        if (StringUtils.isNotBlank(toRecipient)) {
            transferTable.addCell(_noBorderCell("To Recipient:", TRANS_TABLE_FIELD_FONT_COLOR));
            transferTable.addCell(_noBorderCell2(toRecipient, TRANS_TABLE_VALUE_FONT_COLOR));
            transferTable.completeRow();
        }

    }

    private PdfPCell _setBorder(PdfPCell cell) {
        cell.setPaddingLeft(15f);
        cell.setPaddingTop(7f);
        cell.setPaddingBottom(7f);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(Color.BLACK);
        cell.setPaddingLeft(15f);

        return cell;
    }

    public static PdfPCell _noBorderCell(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 8, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorderColor(Color.WHITE);
        cell.setPaddingTop(2);
        cell.setPaddingLeft(15f);
        cell.setPaddingBottom(2);
        return cell;
    }

    public static PdfPCell _noBorderCell2(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 8, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorderColor(Color.WHITE);
        cell.setPaddingTop(2);
        cell.setPaddingBottom(2);
        return cell;
    }

    public static PdfPCell _noBorderCell1(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 8, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(Rectangle.TOP);
        cell.setBorderColor(Color.BLACK);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(2);
        return cell;
    }

    public static PdfPCell _noBorderCell3(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 8, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(Rectangle.TOP);
        cell.setBorderColor(Color.BLACK);
        cell.setPaddingTop(2);
        cell.setPaddingLeft(15f);
        cell.setPaddingBottom(2);
        return cell;
    }
}