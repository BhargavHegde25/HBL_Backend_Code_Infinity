package com.kony.dbputilities.fileutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.kony.dbputilities.fileutil.FileGenerator;
import com.kony.dbputilities.util.HelperMethods;

public class PDFGeneratorSingleTransaction  {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private JsonObject userDetails;
    private String imgFileName = "RetailBankingBanner.png";
    private String[] fields = new String[] { "transactionDate", "description", "transactionId" };
    private String[] userDisplayName = new String[] { "Name: ", "Account: ", "Branch: ", "Phone: ", "E-Mail: " };
    private String[] userKeys = new String[] { "userfirstname", "account", "bankName", "phone", "email" };
    private Set<String> depositTypes;

    
    public static class TransactionDetails {
    	String date;
    	String description;
    	String TransactionID;
    	String withdrawl;
    	String deposit;
    	String balance;
    	
    	public static String[] getKeys() {
    		return new String[] {"Date","TransactionID", "Description", "Amount" };
    	}
    	
    	public String[] getValues() {
    		return new String[] {this.date, this.TransactionID, this.description, this.withdrawl };
    	}

		public TransactionDetails(String date, String description, String transactionID, String withdrawl,
				String deposit, String balance) {
			super();
			this.date = date;
			this.description = description;
			TransactionID = transactionID;
			this.withdrawl = withdrawl;
			this.deposit = deposit;
			this.balance = balance;
		}
    	
    	
    	
    	
    }
    
    
   
    public byte[] generateFile( TransactionDetails details, Map<String, Object> otherData, String filters) throws IOException {
        init();
//        if (null != otherData.get("userDetails")) {
//            this.userDetails = (JsonObject) otherData.get("userDetails");
//        }
//        if (StringUtils.isNotBlank((String) otherData.get("imgFileName"))) {
//            this.imgFileName = (String) otherData.get("imgFileName");
//        }
        InputStream log4jInputStream = PDFGeneratorSingleTransaction.class.getClassLoader().getResourceAsStream("log4j.properties");
        PropertyConfigurator.configure(log4jInputStream);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(os, new WriterProperties());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            addFont(document);
            addLogoImage(document);
            // Table userDetailsTable = new Table(UnitValue.createPercentArray(new float[] {
            // 1, 1 }));
            // userDetailsTable.addCell(new
            // Cell().add(getUserDatilsDiv()).setBorder(Border.NO_BORDER));
            // userDetailsTable.addCell(new
            // Cell().add(getNoteDiv()).setBorder(Border.NO_BORDER));
            // userDetailsTable.addCell(new Cell().setBorder(Border.NO_BORDER));
            //

            // document.add(userDetailsTable);
            Paragraph p1 = new Paragraph("Transaction Details");
            Style headingStyle = new Style();
            headingStyle.setFontSize(24);
            p1.addStyle(headingStyle);
            document.add(p1);
            document.add(this.getTransactionDetailsDiv(details));
            document.close();
        } catch (Exception e) {
            alert.prepareError(e).log();
        } finally {
            if (log4jInputStream != null) {
                try {
                    log4jInputStream.close();
                } catch (Exception e) {
                    alert.prepareError(e).log();
                }
            }
        }
        return os.toByteArray();
    }

    private void init() {
        depositTypes = new HashSet<>();
        depositTypes.add("Deposit");
        depositTypes.add("Interest");
        depositTypes.add("ReceivedP2P");
        depositTypes.add("ReceivedRequest");
        depositTypes.add("Credit");
    }



 
    private IBlockElement getDateDiv(String from, String to) {
        Div dateDiv = new Div();
        Paragraph p = new Paragraph("Account Statement").setBold();
        dateDiv.add(p);
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            Paragraph date = new Paragraph("Statement from " + from + " to " + to);
            dateDiv.add(date);
        }
        return dateDiv;
    }

   

   

    public void addLogoImage(Document document) throws IOException {
        InputStream is = PDFGeneratorSingleTransaction.class.getClassLoader().getResourceAsStream(this.imgFileName);
        try {
            Image konyLogo = new Image(ImageDataFactory.create(IOUtils.toByteArray(is)));
            Div imageDiv = new Div().setBorder(new SolidBorder(0.2f));
            imageDiv.setHeight(50.5f);
            imageDiv.setWidth(UnitValue.createPercentValue(100));
            konyLogo.setWidth(imageDiv.getWidth());
            konyLogo.setHeight(imageDiv.getHeight());
            imageDiv.add(konyLogo);
            document.add(imageDiv);
        } catch (Exception e) {
            alert.prepareError(e).log();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    alert.prepareError(e).log();
                }
            }
        }
    }

    public Div getUserDatilsDiv() {
        float containerMargin = 11f;
        float userMargin = 5f;
        Div userContainer = new Div().setBorder(new SolidBorder(0.2f))
                .setWidth(PageSize.A4.getWidth() / 2 - 2 * containerMargin);
        userContainer.setMargin(containerMargin);

        Table userTable = new Table(2);
        userTable.setBorder(Border.NO_BORDER);

        Cell key = null;
        Cell value = null;
        for (int i = 0; i < userDisplayName.length; i++) {
            key = getNoBorderCell();
            value = getNoBorderCell();
            key.add(new Paragraph(userDisplayName[i]));
            if (null != this.userDetails && this.userDetails.has(userKeys[i])
                    && !userDetails.get(userKeys[i]).isJsonNull()) {
                value.add(new Paragraph(userDetails.get(userKeys[i]).getAsString()));
            }
            userTable.addCell(key);
            userTable.addCell(value);
        }

        key = getNoBorderCell();
        value = getNoBorderCell();
        key.add(new Paragraph("Address: "));
        StringBuilder sb = new StringBuilder();
        String[] addressComps = new String[] { "addressLine1", "addressLine2", "city", "zipcode" };
        for (int i = 0; i < addressComps.length; i++) {
            if (null != this.userDetails && this.userDetails.has(addressComps[i])) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(this.userDetails.get(addressComps[i]).getAsString());
            }
        }
        value.add(new Paragraph(sb.toString()));
        userTable.addCell(key);
        userTable.addCell(value);

        Div userDiv = new Div();
        userDiv.setMargin(userMargin);
        userDiv.add(userTable);
        userContainer.add(userDiv);
        return userContainer;
    }
    
    public Div getTransactionDetailsDiv(TransactionDetails details) {
        float containerMargin = 11f;
        float userMargin = 5f;
        Div transactionContainerContainer = new Div().setBorder(new SolidBorder(0.2f))
                .setWidth(PageSize.A4.getWidth() / 2 - 2 * containerMargin);
        transactionContainerContainer.setMargin(containerMargin);

        Table usertransactionTable = new Table(2);
        usertransactionTable.setBorder(Border.NO_BORDER);

        
        Cell key = null;
        Cell value = null;
        String[] keys = TransactionDetails.getKeys();
        String[] values = details.getValues();
        for (int i = 0; i < keys.length; i++) {
        	  key = getNoBorderCell();
              value = getNoBorderCell();
        	key.add(new Paragraph(keys[i]));
        	alert.prepareError("Adding value" + values[i]).log();
                value.add(new Paragraph(values[i]));
            usertransactionTable.addCell(key);
            usertransactionTable.addCell(value);
        }

        Div userDiv = new Div();
        userDiv.setMargin(userMargin);
        userDiv.add(usertransactionTable);
        transactionContainerContainer.add(userDiv);
        return transactionContainerContainer;
    }

    public Div getNoteDiv() {
        Div noteDiv = new Div();
        noteDiv.add(new Paragraph(
                "Note:\n Please note that the content of this statement will be considered correct if no error is reported within 30 days of reciept of statement. The "
                        + "address of the statement is that on record with the bank as the day of requesting this content. Please note that the content of this statement"
                        + "will be considered correct if no error is reported within 30 days of reciept of statement. The address of the statement is that on record with the bank as the day of requesting this content.")
                                .setFontSize(11f));
        return noteDiv;
    }

    public Cell getNoBorderCell() {
        return new Cell().setBorder(Border.NO_BORDER);
    }

    public void addFont(Document document) throws IOException {
        PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        document.setFont(font);
    }

    public String getContentType() {
        return "application/pdf";
    }
}
