package com.infinity.dbx.temenos.fileutil;

import static java.awt.Color.BLACK;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGeneratorPartialRepaymentAck {
	
	private Map<String, Object> mortgageFacilityDetails;
	private JSONArray mortgageSimulatedValues;
	private JSONArray mortgageCurrentValues;
	private Map<String, Object> paymentDetails;
	
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public PDFGeneratorPartialRepaymentAck(Map<String, Object> paymentDetails,JSONArray mortgageSimulatedValues,JSONArray mortgageCurrentValues,Map<String, Object> mortgageFacilityDetails) {
		this.mortgageCurrentValues = mortgageCurrentValues;
		this.mortgageSimulatedValues = mortgageSimulatedValues;
		this.mortgageFacilityDetails = mortgageFacilityDetails;
		this.paymentDetails = paymentDetails;
	}

	public String generateFileAndGetBase64() {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();

			Rectangle rect = new Rectangle(36, 108);
			rect.enableBorderSide(1);
			rect.enableBorderSide(2);
			rect.enableBorderSide(4);
			rect.enableBorderSide(8);
			rect.setBorder(2);
			rect.setBorderColor(BLACK);
			document.add(rect);

			//Adding Infinity Logo
			addLogo(document);

			//adding header
			addHeaderLabel(document, "Mortgage Facility Details");

			//Creating Table
			document.add(this.getTable(this.mortgageFacilityDetails));
			
			addHeaderLabel(document,"Mortgage Current Values");
			
			document.add(this.getCurrentValuesTable());
			
			addHeaderLabel(document,"Mortgage Simulated Values");
			
			document.add(this.getSimulatedValuesTable());
			
			addHeaderLabel(document, "Payment Details");
			
			document.add(this.getTable(this.paymentDetails));
			
			document.close();
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] generateFileAndGetByteArray() {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();

			Rectangle rect = new Rectangle(36, 108);
			rect.enableBorderSide(1);
			rect.enableBorderSide(2);
			rect.enableBorderSide(4);
			rect.enableBorderSide(8);
			rect.setBorder(2);
			rect.setBorderColor(BLACK);
			document.add(rect);

			//Adding Infinity Logo
			addLogo(document);

			//adding header
			addHeaderLabel(document, "Mortgage Facility Details");

			//Creating Table
			document.add(this.getTable(this.mortgageFacilityDetails));
			
			addHeaderLabel(document,"Mortgage Current Values");
			
			document.add(this.getCurrentValuesTable());
			
			addHeaderLabel(document,"Mortgage Simulated Values");
			
			document.add(this.getSimulatedValuesTable());
			
			addHeaderLabel(document, "Payment Details");
			
			document.add(this.getTable(this.paymentDetails));
			
			
			document.close();
			return os.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}


	public void addLogo(Document document) {
		InputStream is = PDFGeneratorChangeRepaymentDayReqAck.class.getClassLoader().getResourceAsStream("infinity_logo.png");
		try {
			Image infinityLogo = Image.getInstance(IOUtils.toByteArray(is));
			infinityLogo.setSpacingAfter(70);
			infinityLogo.scaleAbsoluteHeight(40);
			infinityLogo.scaleAbsoluteWidth(90);
			document.add(infinityLogo);
		} catch (Exception e) {

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

	public void addHeaderLabel(Document document, String label) {
		PdfPTable tabAcc = new PdfPTable(1);
		tabAcc.setSpacingBefore(10);
		tabAcc.setWidthPercentage(100);
		Font font = new Font(Font.TIMES_ROMAN, 24, Font.BOLD, Color.BLACK);
		Phrase stat1=new Phrase("\n" + label + "\n\n",font);
		PdfPCell lbl = new PdfPCell(stat1);
		lbl.setBorderWidthTop(2);
		lbl.setBorderWidthBottom(2);
		lbl.setBorderWidthLeft(0);
		lbl.setBorderWidthRight(0);
		tabAcc.addCell(lbl);
		document.add(tabAcc);
	}

	private PdfPTable getTable(Map<String, Object> tableDetails) {
		PdfPTable table = new PdfPTable(2);
		table.setSpacingBefore(10);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{1.5f, 2.2f});

		for (Map.Entry<String, Object> entry : tableDetails.entrySet()) {
			try {
				Paragraph key = new Paragraph(entry.getKey());
				if (entry.getValue().toString().isEmpty()) {
					continue;
				}
				Paragraph value = new Paragraph(entry.getValue().toString());
				PdfPCell keyCell = new PdfPCell();
				PdfPCell valueCell = new PdfPCell();

				keyCell.setFixedHeight(1.0f);
				keyCell.setPadding(1.0f);
				keyCell.setPaddingTop(2);
				keyCell.setPaddingBottom(2);
				keyCell.addElement(key);


				valueCell.addElement(value);

				table.addCell(keyCell);
				table.addCell(valueCell);
				table.setSpacingAfter(24);

			} catch (Exception e) {
				alert.prepareError("Error Occurred while creating table in PDF ", e).log();
			}
		}

		return table;
	}
	private PdfPTable getSimulatedValuesTable() {

		PdfPTable table = new PdfPTable(3); // Create 3 columns in table.

		table.setSpacingBefore(10);

        PdfPCell cell1 = new PdfPCell(new Paragraph("InstallmentAmount"));

        PdfPCell cell2 = new PdfPCell(new Paragraph("Next Repayment Date"));

        PdfPCell cell3 = new PdfPCell(new Paragraph("EndDate (Maturity Date)"));

  
        table.addCell(cell1);

        table.addCell(cell2);

        table.addCell(cell3);

        PdfPCell installmentAmountCell,nextRepaymentDateCell,EndDateCell;
        
        JSONArray simulatedValuesArray = this.mortgageSimulatedValues; 
        for(int i=0;i<simulatedValuesArray.length();i++) {
        	JSONObject simulatedValuesObj =
        			simulatedValuesArray.getJSONObject(i);    
        installmentAmountCell = new PdfPCell(new Paragraph(simulatedValuesObj.optString("Installment Amount"))); 
		 
		  nextRepaymentDateCell = new PdfPCell(new Paragraph(simulatedValuesObj.optString("Next Repayment Date")));
		 
		  EndDateCell = new PdfPCell( new Paragraph(simulatedValuesObj.optString("EndDate")));
		 
		  table.addCell(installmentAmountCell); 
		  table.addCell(nextRepaymentDateCell);
		  table.addCell(EndDateCell); 
		  table.setSpacingAfter(24);
        }
		return table;
	}
	
	private PdfPTable getCurrentValuesTable() {

		PdfPTable table = new PdfPTable(3); // Create 3 columns in table.

		table.setSpacingBefore(10);

        PdfPCell cell1 = new PdfPCell(new Paragraph("InstallmentAmount"));

        PdfPCell cell2 = new PdfPCell(new Paragraph("Next Repayment Date"));

        PdfPCell cell3 = new PdfPCell(new Paragraph("EndDate (Maturity Date)"));

  
        table.addCell(cell1);

        table.addCell(cell2);

        table.addCell(cell3);

        PdfPCell installmentAmountCell,nextRepaymentDateCell,EndDateCell;
        JSONArray currentValuesArray = this.mortgageCurrentValues; 

        for(int i=0;i<currentValuesArray.length();i++) {
        	JSONObject currentValuesObj =
        			currentValuesArray.getJSONObject(i);          
        installmentAmountCell = new PdfPCell(new Paragraph(currentValuesObj.optString("Installment Amount"))); 
		 
		  nextRepaymentDateCell = new PdfPCell(new Paragraph(currentValuesObj.optString("Next Repayment Date")));
		 
		  EndDateCell = new PdfPCell( new Paragraph(currentValuesObj.optString("EndDate")));
		 
		  table.addCell(installmentAmountCell); 
		  table.addCell(nextRepaymentDateCell);
		  table.addCell(EndDateCell); 
		  table.setSpacingAfter(24);	
        }
		return table;
	}

}