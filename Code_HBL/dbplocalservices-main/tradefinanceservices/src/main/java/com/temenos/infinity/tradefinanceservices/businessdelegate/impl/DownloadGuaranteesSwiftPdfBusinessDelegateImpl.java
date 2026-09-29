/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.fileutil.PDFGenerator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.DownloadGuaranteesSwiftPdfBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class DownloadGuaranteesSwiftPdfBusinessDelegateImpl implements DownloadGuaranteesSwiftPdfBusinessDelegate, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	static LinkedHashMap<String, String> gauranteeDtoFields ;
	static {
		gauranteeDtoFields = new LinkedHashMap<>();
		gauranteeDtoFields.put("sequenceOfTotal", "Sequence of total");
		gauranteeDtoFields.put("purposeOfMessage", "Purpose of Message");
		gauranteeDtoFields.put("newSequence1", "New Sequence1");
		gauranteeDtoFields.put("undertakingNumber", "Undertaking Number");
		gauranteeDtoFields.put("issueDate", "Date of issue");
		gauranteeDtoFields.put("formOfUndertaking", "Form of Undertaking");
		gauranteeDtoFields.put("applicationRules", "Application Rules");
		gauranteeDtoFields.put("expiryType", "Expiry Type");
		gauranteeDtoFields.put("expiryDate", "Date of Expiry");
		gauranteeDtoFields.put("applicant", "Applicant");
		gauranteeDtoFields.put("issue", "Issue");
		gauranteeDtoFields.put("beneficiaryName", "Beneficiary");
		gauranteeDtoFields.put("advisingBank", "Advising Bank");
		gauranteeDtoFields.put("adviseThroughBank", "Advise Through Bank");
		gauranteeDtoFields.put("undertakingAmount", "Undertaking Amount");
		gauranteeDtoFields.put("availableWith", "Available With");
		gauranteeDtoFields.put("charges", "Charges");
		gauranteeDtoFields.put("documentAndPresentationInstructions", "Document and Presentation Instructions");
		gauranteeDtoFields.put("undertakingTermsAndConditions", "Undertaking Terms and Conditions");
		gauranteeDtoFields.put("confirmationInstructions", "Confirmation Instructions");
		gauranteeDtoFields.put("governingLawAnd/orPlacedOfJurisdiction", "Governing Law and/or Placed of Jurisdiction");
		gauranteeDtoFields.put("automaticExtensionPeriod", "Automatic Extension Period");
		gauranteeDtoFields.put("automaticExtensionNon-ExtensionNotification", "Automatic Extension Non-Extension Notification");
		gauranteeDtoFields.put("automaticExtensionNotificationPeriod", "Automatic Extension Notification Period");
		gauranteeDtoFields.put("automaticExtensionFinalExpiryDate", "Automatic Extension Final Expiry Date");
		gauranteeDtoFields.put("demandIndicator", "Demand Indicator");
		gauranteeDtoFields.put("underlyingTransactionDetails", "Underlying Transaction Details");
		gauranteeDtoFields.put("deliveryOfOriginalUndertaking", "Delivery of Original Undertaking");
		gauranteeDtoFields.put("deliveryToCollectionBy", "Delivery ToCollection By");
	}
	public static void addSwiftHeadersAndFootersPDF(Document document) {
        Image logo = null;
        try(InputStream ipStream = PDFGenerator.class.getClassLoader().getResourceAsStream(LOGO_INFINITY)) {
            logo = Image.getInstance(IOUtils.toByteArray(Objects.requireNonNull(ipStream)));
			logo.scaleAbsoluteHeight(23);
			logo.scaleAbsoluteWidth(70);
		} catch (Exception e) {
			alert.prepareError("Error occurred while creating trade finance details pdf ", e).log();
		}
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.addCell(_noBorderCell("Message Key", darkerFont));
		table.addCell(_noBorderCell("R2022010968244-1408501                ISSUE GUARANTEES & STANDBY LC", darkerFont));


		Phrase phrase = new Phrase();
		phrase.add(new Chunk(logo, 0, 0, true));
		SimpleDateFormat formatDate1 = new SimpleDateFormat("dd MMMMM yyyy");
		SimpleDateFormat formatdate2 = new SimpleDateFormat("HH:mm:ss");
		Date date1 = new Date();
		Date date2 = new Date();
		String str2 = String.format(formatdate2.format(date2));
		String str1 = String.format(formatDate1.format(date1));
		// heading
		Paragraph date = new Paragraph(addSpacesPrefix(str1, 157 - str1.length()), new Font(Font.HELVETICA, 9, Font.NORMAL, darkFont));
		phrase.add(date);
		Paragraph time = new Paragraph(addSpacesPrefix(str2, 205 - str2.length()), new Font(Font.HELVETICA, 9, Font.NORMAL, darkFont));
		phrase.add(time);


		HeaderFooter header = new HeaderFooter(phrase, false);
		header.setBorderColor(Color.WHITE);
		document.setHeader(header);

		Paragraph pageNumber = new Paragraph(addSpacesPrefix("Page ", 176), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, Font.NORMAL, darkFont));
		com.lowagie.text.HeaderFooter footer = new HeaderFooter(new Phrase(pageNumber), true);
		footer.setBorderColor(Color.WHITE);
		document.setFooter(footer);

	}
	@Override
	public byte[] generateGuaranteesSwiftPdf(GuaranteesDTO guaranteesDTO, DataControllerRequest request) {

		Map<String, String> input = null;
		try {
			input = JSONUtils.parseAsMap(new JSONObject(guaranteesDTO).toString(), String.class, String.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while parsing response",e).log();
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Document document = new Document();
		document.setPageSize(PageSize.A4);
		PdfWriter.getInstance(document, os);
		addSwiftHeadersAndFootersPDF(document);
		try {
			document.open();
			document.add(_messageDetais(guaranteesDTO));
			document.add(lcInfo(guaranteesDTO, request,input));
			document.close();
			return os.toByteArray();
		} catch (Exception e) {
			alert.prepareError("Error occurred while creating pdf ", e).log();
			return null;
		} finally {
			if (os!=null) {
				try {
					os.close();
				} catch(Exception e) {
					alert.prepareError("Error occurred while closing input stream. " + e).log();
				}
			}
		}
	}

	float[] twoColumnWidth = { 30, 30 };

	public PdfPTable _messageDetais(GuaranteesDTO guaranteesDTO ) {

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.addCell(_noBorderHeadingCell("Incoming Message's Tag Details", darkerFont));

		table.addCell(_noBorderCell("Message Key", darkerFont));
		table.addCell(_noBorderCell("R2022010968244-1408501                ISSUE GUARANTEES & STANDBY LC", darkerFont));

		table.addCell(_noBorderCell("Message Type", darkerFont));
		table.addCell(_noBorderCell("760", darkerFont));

		table.addCell(_noBorderCell("Receiver Date And Time", darkerFont));
		table.addCell(_noBorderCell("20220109 03:54:45", darkerFont));

		table.addCell(_noBorderCell("Sender", darkerFont));
		table.addCell(_noBorderCell("AEIBSGSGXXXX", darkerFont));

		table.addCell(_noBorderCell("Receiver", darkerFont));
		table.addCell(_noBorderCell("DEMOGBPXAXXX", darkerFont));

		return table;
	}

	// Adding Guarantees Data
	public PdfPTable lcInfo(GuaranteesDTO guaranteesDTO, DataControllerRequest request, Map<String, String> input) {
		JSONObject bundleConfig = TemenosUtils.getBundleConfigurations(TransactionConstants.DBP_BUNDLE,
				"SWIFT_GUARANTEES_TAG", request);
		JSONArray swift = bundleConfig.getJSONArray("configurations");
		JSONObject swiftDetails = new JSONObject(swift.getJSONObject(0).getString("config_value"));

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		table.addCell(_noBorderCell("Message", darkFont, WebColors.getRGBColor("#e3e3e3")));
		table.addCell(_noBorderCell("", darkFont, WebColors.getRGBColor("#e3e3e3")));

		table.addCell(
				_noBorderCell("{1:F01L20AXXX0001000001}{2:I760AABBCC0XXXXN}{3:{108:XXX988033452878}}{4:", darkFont, 6));
		table.addCell(_noBorderCell("", darkFont));

		int i = 0;

		//gauranteeDtoFields
		for(Map.Entry<String, String> entry : gauranteeDtoFields.entrySet()){

			if (input.containsKey(entry.getKey()) && StringUtils.isNotBlank(input.get(entry.getKey()))) {

				String space = swiftDetails.getString(entry.getValue()).length()==2 ? "  " : "";
				Color color = i % 2 == 0 ? WebColors.getRGBColor("#f6f7ef") : WebColors.getRGBColor("#ffffff");
				table.addCell(_noBorderCell(":" + swiftDetails.getString(entry.getValue()) + ":\t\t       " + space + entry.getValue(), darkFont, color));
				table.addCell(_noBorderCell(input.get(entry.getKey()), darkFont, color));
				i++;
			}
		}

		table.addCell(_noBorderCell("-}", darkFont, i % 2 == 0 ? WebColors.getRGBColor("#f6f7ef") : WebColors.getRGBColor("#ffffff")));

		return table;
	}

	public static PdfPCell _noBorderHeadingCell(String text, Color fontColor) {
		Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 7, Font.BOLD, fontColor));
		PdfPCell cell = new PdfPCell();
		cell.addElement(cellText);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(4);
		return cell;
	}
	public static PdfPCell _noBorderCell(String text, Color fontColor) {
		Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 6,Font.BOLD,fontColor));
		PdfPCell cell = new PdfPCell();
		cell.addElement(cellText);
		cell.setBorder(0);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(4);
		return cell;
	}
	public static PdfPCell _noBorderCell(String text, Color fontColor, Color backGroundColor) {
		Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 7,Font.BOLD,fontColor));
		PdfPCell cell = new PdfPCell();
		cell.addElement(cellText);
		cell.setBorder(0);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(4);
		cell.setBackgroundColor(backGroundColor);
		return cell;
	}

	public static PdfPCell _noBorderCell(String text, Color fontColor, int fontSize) {
		Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, fontSize,Font.BOLD,fontColor));
		PdfPCell cell = new PdfPCell();
		cell.addElement(cellText);
		cell.setBorder(0);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(4);
		return cell;
	}

	public static String addSpacesPrefix(String input, int count) {
		char[] repeatArr = new char[count];
		Arrays.fill(repeatArr, ' ');
		input = new String(repeatArr) + input;
		return input;
	}

}
