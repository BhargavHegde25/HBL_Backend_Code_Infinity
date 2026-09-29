package com.temenos.dbx.product.transactionservices.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.BWConstants;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireFileBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.*;
import com.temenos.dbx.product.transactionservices.resource.api.BWFileValidationResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.*;

public class BWFileValidationResourceImpl implements BWFileValidationResource{
	
	 static Set<String> countries  = new HashSet<String>();
	 static Set<String> currencies = new HashSet<String>();
	 static Map<String, AccountsDTO> accounts = new HashMap<String,AccountsDTO>();
	 static Map<String,String> accountTypes = new HashMap<String,String>();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	 private static String userId;
	 private static String orgId;
	
	@Override
	public Map<String, Object> validateFile(File inputFile,DataControllerRequest requestInstance) throws Exception {
		
		Map<String, Object> fileContents = new HashMap<>();
		fileContents = constructContentsMap(fileContents);
		String name = inputFile.getName();
		int lastIndexOf = name.lastIndexOf(".");
		String fileType = name.substring(lastIndexOf);
		Map<String, Object> customer = CustomerSession.getCustomerMap(requestInstance);
		userId = CustomerSession.getCustomerId(customer);
		BulkWireFileBusinessDelegate BWFileBusinessDeligate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);
		List<CountryListDTO> countriesList  = BWFileBusinessDeligate.getCountryList();
//		List<CurrencyListDTO> currenciesList = BWFileBusinessDeligate.getCurrencyList();
		List<AccountsDTO> accountsList = BWFileBusinessDeligate.getAccounts();
		List<AccountTypesDTO> accountTypesList = BWFileBusinessDeligate.getAccountTypes();
		setCountriesSet(countriesList);
//		setCurrencyMap(currenciesList);
		setCurrencyMap();
		setAccountsMap(accountsList);
		setAccountTypesMap(accountTypesList);
		List<ApplicationDTO> applicationProp = BWFileBusinessDeligate.getApplicationProp();
		int lineItemsLimit = applicationProp.get(0).getBwFileTransactionsLimit();
		try {
			switch(fileType){
				case ".xls": 
					return parsexlsFile(inputFile,fileContents,lineItemsLimit);
				case ".xlsx": return parsexlsxFile(inputFile,fileContents,lineItemsLimit);
				case ".csv" : return parsecsvFile(inputFile, fileContents,lineItemsLimit);
				default :  fileContents.put("status","Denied");
					fileContents.put("message","denied due to incorrect file format");
						break;
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return fileContents;
	}
	
	

	@Override
	public JSONObject validateTemplateFile(File uploadedFile, DataControllerRequest requestInstance) {
		try {
			String name = uploadedFile.getName();
			int lastIndexOf = name.lastIndexOf(".");
			String fileType = name.substring(lastIndexOf);
			Map<String, Object> customer = CustomerSession.getCustomerMap(requestInstance);
			userId = CustomerSession.getCustomerId(customer);
			BulkWireFileBusinessDelegate BWFileBusinessDeligate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);
			List<CountryListDTO> countriesList  = BWFileBusinessDeligate.getCountryList();
			setCountriesSet(countriesList);
			List<ApplicationDTO> applicationProp = BWFileBusinessDeligate.getApplicationProp();
			int lineItemsLimit = applicationProp.get(0).getBwFileTransactionsLimit();
			switch(fileType){
				case ".xls":  return parsexlsTemplateFile(uploadedFile,lineItemsLimit);
				case ".xlsx": return parsexlsxTemplateFile(uploadedFile,lineItemsLimit);
				case ".csv" : return parsecsvTemplateFile(uploadedFile,lineItemsLimit);
			}
			return null;
		}catch(Exception e) {
			alert.prepareError("Error while fetching data to validate the file").log();
			return returnTemplateError(ErrorCodeEnum.ERR_12000);
		}
	}
	
	private JSONObject parsecsvTemplateFile(File uploadedFile, int lineItemsLimit) throws Exception {
		ICsvMapReader beanReader = new CsvMapReader(new FileReader(uploadedFile), CsvPreference.STANDARD_PREFERENCE);
		try {
		String[] header = beanReader.getHeader(true);
		//final String[] headers = new String[]{BWConstants.TRANSFER_TYPE, BWConstants.COUNTRY_NAME, BWConstants.RECIPIENT_NAME, BWConstants.INDIVIDUAL_BUSINESS,BWConstants.RECIPIENT_ADDRESS_LINE1,BWConstants.RECIPIENT_ADDRESS_LINE2,BWConstants.CITY,BWConstants.STATE,BWConstants.ZIP_CODE,BWConstants.SWIFT_CODE,BWConstants.RECIPIENT_ACCOUNT_NUMBER,BWConstants.ROUTING_NUMBER,BWConstants.INTERNATIONAL_ROUTING_NUMBER,BWConstants.ACCOUNT_NUMBER,BWConstants.RECIPIENT_BANK_NAME,BWConstants.BANK_ADDRESS_LINE1,BWConstants.BANK_ADDRESS_LINE2,BWConstants.BANK_CITY,BWConstants.BANK_STATE,BWConstants.BANK_ZIP_CODE};
		final String[] headers = new String[]{BWConstants.TRANSFER_TYPE,
			BWConstants.COUNTRY_NAME,
			BWConstants.RECIPIENT_NAME,
			BWConstants.INDIVIDUAL_BUSINESS,
			BWConstants.RECIPIENT_ADDRESS_LINE1,
			BWConstants.RECIPIENT_ADDRESS_LINE2,
			BWConstants.CITY,
			BWConstants.STATE,
			BWConstants.ZIP_CODE,
			BWConstants.SWIFT_CODE,
			BWConstants.RECIPIENT_ACCOUNT_NUMBER,
			BWConstants.ROUTING_NUMBER,
			BWConstants.INTERNATIONAL_ROUTING_NUMBER,
			BWConstants.ACCOUNT_NUMBER,
			BWConstants.RECIPIENT_BANK_NAME,
			BWConstants.BANK_ADDRESS_LINE1,
			BWConstants.BANK_ADDRESS_LINE2,
			BWConstants.BANK_CITY,
			BWConstants.BANK_STATE,
			BWConstants.BANK_ZIP_CODE,
			BWConstants.FROM_ACCOUNT,
			BWConstants.CURRENCY,
			BWConstants.AMOUNT,
			BWConstants.NOTE};
		final CellProcessor[] processors = buildCSVTemplateRow();
		Map<String, Object> fieldsInCurrentRow;
		int linesCount = getNumberOfLinesInCSVFile(uploadedFile);
	     if(linesCount>lineItemsLimit+1)
	    	 return returnTemplateError(ErrorCodeEnum.ERR_14006);
	     if(linesCount < 0)
	    	 return returnTemplateError(ErrorCodeEnum.ERR_14005);
	     if(header[0] == null || !header[0].trim().equalsIgnoreCase(headers[0].trim()) && !header[0].trim().substring(1).equalsIgnoreCase(headers[0].trim()))
			return returnTemplateError(ErrorCodeEnum.ERR_14005);
	     for(int i = 1; i < 20; i++) {
			if(header[i] == null || !header[i].trim().equalsIgnoreCase(headers[i].trim()))
				return returnTemplateError(ErrorCodeEnum.ERR_14005);
		}
	     JSONObject fileRecords = new JSONObject();
	     JSONArray recipients = new JSONArray();
	    try {
			while ((fieldsInCurrentRow = beanReader.read(headers, processors)) != null) {
				Map<String, String> rowMap = new HashMap<>();
				rowMap = constructCSVRowMap(fieldsInCurrentRow);
				if(isValidTemplateRecord(rowMap)) {
					rowMap.put("templateRecipientCategory","EXTRACTEDFROMFILE"); 
					recipients.put(rowMap);
				} else {
					return returnTemplateError(ErrorCodeEnum.ERR_14005);
				}
	        }
	    }catch(Exception e) {
			alert.prepareError("Error while validating the file").log();
			return returnTemplateError(ErrorCodeEnum.ERR_14005);
		}
		fileRecords.append("recipients", recipients);
		return fileRecords;
	}
	catch(Exception e) {
		alert.prepareError("Error while validating the file").log();
		return returnTemplateError(ErrorCodeEnum.ERR_14005);
	}finally {
    	if (beanReader!=null) {
    		try {
    			beanReader.close();			
    		}
    		catch(Exception e)
    		{
    			alert.prepareError(e.toString()).log();
    		}
    	}
    }
	}

	private JSONObject parsexlsTemplateFile(File uploadedFile, int lineItemsLimit) {
		try {
			HSSFSheet sheet = getXlsSheet(uploadedFile);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			int lastRowNum = sheet.getLastRowNum();
			if(lastRowNum>lineItemsLimit) {
				return returnTemplateError(ErrorCodeEnum.ERR_14006);
			}
			if(!validateHeader(row,20)) {
				return returnTemplateError(ErrorCodeEnum.ERR_14005);
			}
			JSONObject fileRecords = new JSONObject();
			JSONArray recipients = new JSONArray();
			Map<String, String> record = new HashMap<String, String>();
			while (rowIterator.hasNext()){
				Map<String, String> rowMap = new HashMap<>();
				row = rowIterator.next();
				rowMap = buildrowMap(row,20);
				if(isValidTemplateRecord(rowMap)) {
					rowMap.put("templateRecipientCategory","EXTRACTEDFROMFILE");
					recipients.put(rowMap);
				} else {
					return returnTemplateError(ErrorCodeEnum.ERR_14005);
				}
			}
			fileRecords.append("recipients", recipients);
			return fileRecords;
		}
		catch(Exception e) {
			alert.prepareError("Error while validating the file").log();
			return returnTemplateError(ErrorCodeEnum.ERR_14005);
		}
	}

	private JSONObject parsexlsxTemplateFile(File inputFile, int lineItemsLimit) {
		try {		
			Sheet sheet = getXlsxSheet(inputFile);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			int lastRowNum = sheet.getLastRowNum();
			if(lastRowNum>lineItemsLimit) {
				return returnTemplateError(ErrorCodeEnum.ERR_14006);
			}
			if(!validateHeader(row,20)) {
				return returnTemplateError(ErrorCodeEnum.ERR_14005);
			}
			JSONObject fileRecords = new JSONObject();
			JSONArray recipients = new JSONArray();
			Map<String, String> record = new HashMap<String, String>();
			while (rowIterator.hasNext()){
				Map<String, String> rowMap = new HashMap<>();
				row = rowIterator.next();
				rowMap = buildrowMap(row,20);
				if(isValidTemplateRecord(rowMap)) {
					rowMap.put("templateRecipientCategory","EXTRACTEDFROMFILE");
					recipients.put(rowMap);
				} else {
					return returnTemplateError(ErrorCodeEnum.ERR_14005);
				}
			}
			fileRecords.append("recipients", recipients);
			return fileRecords;
		}
		catch(Exception e) {
			alert.prepareError("Error while validating the file").log();
			return returnTemplateError(ErrorCodeEnum.ERR_14005);
		}
	}

		private static Map<String, Object> constructContentsMap(Map<String, Object> fileContents) {
			fileContents.put("bulkWireTransferType","");
			fileContents.put("recipientCountryName","");
			fileContents.put("recipientName","");
			fileContents.put("transactionType","");
			fileContents.put("recipientAddressLine1","");
			fileContents.put("recipientAddressLine2","");
			fileContents.put("recipientCity","");
			fileContents.put("recipientState","");
			fileContents.put("recipientZipCode","");
			fileContents.put("swiftCode","");
			fileContents.put("recipientAccountNumber","");
			fileContents.put("routingNumber","");
			fileContents.put("internationalRoutingNumber","");
			fileContents.put("accountNickname","");
			fileContents.put("recipientBankName","");
			fileContents.put("recipientBankAddress1","");
			fileContents.put("recipientBankAddress2","");
			fileContents.put("recipientBankcity","");
			fileContents.put("recipientBankstate","");
			fileContents.put("recipientBankZipCode","");
			fileContents.put("fromAccountNumber","");
			fileContents.put("currency","");
			fileContents.put("amount","");
			fileContents.put("note","");
		return fileContents;
	}

		public static Map<String, Object> parsexlsxFile(File inputFile, Map<String, Object> fileContents, int lineItemsLimit) {
			try {
//				FileInputStream fis = new FileInputStream(inputFile);
//				XSSFWorkbook workbook = new XSSFWorkbook(fis);				
				Sheet sheet = getXlsxSheet(inputFile);
				if(sheet.getPhysicalNumberOfRows() <= 1) {
					return returnError(ErrorCodeEnum.ERR_14019);
				}
				Iterator<Row> rowIterator = sheet.iterator();
				int domesticRecords = 0, internationalRecords = 0;
				Row row = rowIterator.next();
				if(sheet.getLastRowNum()>lineItemsLimit) {
					return returnError(ErrorCodeEnum.ERR_14006);
				}
				if(!validateHeader(row,24)) {
					return returnError(ErrorCodeEnum.ERR_14005);
				}
				while (rowIterator.hasNext()){
					Map<String, String> rowMap = new HashMap<>();
					row = rowIterator.next();
					rowMap = buildrowMap(row,24);
					if(isValidRecord(rowMap)) {
						fileContents = addRecordtoContents(rowMap,fileContents);
						if(rowMap.get("bulkWireTransferType").trim().equalsIgnoreCase("Domestic"))
							domesticRecords++;
						else
							internationalRecords++;
					}
					else {
						return returnError(ErrorCodeEnum.ERR_14005);
					}
				}
				fileContents.put("domesticRecords", domesticRecords+"");
				fileContents.put("internationalRecords", internationalRecords+"");
				fileContents.put("noOfLineItems", internationalRecords+domesticRecords+"");
				fileContents.put("status","Accepted");
				return fileContents;
			}
			catch(Exception e) {
				alert.prepareError("Error while validating the file").log();
				return returnError(ErrorCodeEnum.ERR_12000);
			}
		}
		

		public static Map<String, Object> parsexlsFile(File inputFile, Map<String, Object> fileContents, int lineItemsLimit) {
			try {
//				FileInputStream fis = new FileInputStream(inputFile);
//				HSSFWorkbook workbook = new HSSFWorkbook(fis);
				HSSFSheet sheet = getXlsSheet(inputFile);
				if(sheet.getPhysicalNumberOfRows() <= 1) {
					return returnError(ErrorCodeEnum.ERR_14019);
				}
				Iterator<Row> rowIterator = sheet.iterator();
				int domesticRecords = 0, internationalRecords = 0;
				Row row = rowIterator.next();
				if(sheet.getLastRowNum()>lineItemsLimit) {
					return returnError(ErrorCodeEnum.ERR_14006);
				}
				if(!validateHeader(row,24)) {
					return returnError(ErrorCodeEnum.ERR_14005);
				}
				while (rowIterator.hasNext()){
					row = rowIterator.next();
					Map<String, String> rowMap = new HashMap<>();
					rowMap = buildrowMap(row,24);
					if(isValidRecord(rowMap)) {
						fileContents = addRecordtoContents(rowMap,fileContents);
						if(rowMap.get("bulkWireTransferType").trim().equalsIgnoreCase("Domestic"))
							domesticRecords++;
						else
							internationalRecords++;
					}
					else {
						return returnError(ErrorCodeEnum.ERR_14005);
					}
				}
				fileContents.put("domesticRecords", Integer.toString(domesticRecords));
				fileContents.put("internationalRecords", Integer.toString(internationalRecords));
				fileContents.put("noOfLineItems", Integer.toString(internationalRecords+domesticRecords));
				fileContents.put("status","Accepted");
				return fileContents;
			}
			catch(Exception e) {
				alert.prepareError("Error while validating the file").log();
				return returnError(ErrorCodeEnum.ERR_12000);
			}
		}
		
		private static HSSFSheet getXlsSheet(File inputFile) throws Exception {
			FileInputStream fis = new FileInputStream(inputFile);
			try {
				HSSFWorkbook workbook = new HSSFWorkbook(fis);
				return workbook.getSheetAt(0);
			} catch (IOException e) {
				alert.prepareError("Error while readin the file").log();
			}finally {
	        	if (fis!=null) {
	        		try {
	        			fis.close();			
	        		}
	        		catch(Exception e)
	        		{
	        			alert.prepareError(e.toString()).log();
	        		}
	        	}
	        }
			return null;
		}
		
		private static Sheet getXlsxSheet(File inputFile) throws Exception {
			FileInputStream fis = new FileInputStream(inputFile);
			try {
				XSSFWorkbook workbook = new XSSFWorkbook(fis);				
				return workbook.getSheetAt(0);
			} catch (IOException e) {
				alert.prepareError("Error while readin the file").log();
			}finally {
	        	if (fis!=null) {
	        		try {
	        			fis.close();			
	        		}
	        		catch(Exception e)
	        		{
	        			alert.prepareError(e.toString()).log();
	        		}
	        	}
	        }
			return null;
		}
		
		private static boolean validateHeader(Row row,int count) {
			for(int j = 0; j < count; j++) {
				Cell cell = row.getCell(j);
				switch(j) {
				case 0 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.TRANSFER_TYPE))
					return false;
					break;
				case 1 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.COUNTRY_NAME))
					return false;
					break;
				case 2 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.RECIPIENT_NAME))
					return false;
					break;
				case 3 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.INDIVIDUAL_BUSINESS))
					return false;
				break;
				case 4 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.RECIPIENT_ADDRESS_LINE1))
					return false;
				break;
				case 5 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.RECIPIENT_ADDRESS_LINE2))
					return false;
				break;
				case 6 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.CITY))
					return false;
				break;
				case 7 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.STATE))
					return false;
				break;
				case 8 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.ZIP_CODE))
					return false;
				break;
				case 9 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.SWIFT_CODE))
					return false;
				break;
				case 10 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.RECIPIENT_ACCOUNT_NUMBER))
					return false;
				break;
				case 11 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.ROUTING_NUMBER))
					return false;
				break;
				case 12 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.INTERNATIONAL_ROUTING_NUMBER))
					return false;
				break;
				case 13 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.ACCOUNT_NUMBER))
					return false;
				break;
				case 14 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.RECIPIENT_BANK_NAME))
					return false;
				break;
				case 15 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.BANK_ADDRESS_LINE1))
					return false;
				break;
				case 16 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.BANK_ADDRESS_LINE2))
					return false;
				break;
				case 17 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.BANK_CITY))
					return false;
				break;
				case 18 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.BANK_STATE))
					return false;
				break;
				case 19 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.BANK_ZIP_CODE))
					return false;
				break;
				case 20 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.FROM_ACCOUNT))
					return false;
				break;
				case 21 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.CURRENCY))
					return false;
				break;
				case 22 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.AMOUNT))
					return false;
				break;
				case 23 : if(cell == null || !cell.getStringCellValue().equalsIgnoreCase(BWConstants.NOTE))
					return false;
				break;
				}
			}
			return true;
		}

		private static Map<String, Object> returnError(ErrorCodeEnum errorCode){
			Map<String, Object> error = new HashMap<>();
			error.put("status","Denied");
			error.put("errorCode",errorCode);
			return error;
		}
		
		private static JSONObject returnTemplateError(ErrorCodeEnum errorCode) {
			JSONObject error = new JSONObject();
			error.append("error", returnError(errorCode));
			return error;
		}
		
		public static Map<String, Object> parsecsvFile(File inputFile, Map<String, Object> fileContents, int lineItemsLimit) throws Exception {
			ICsvMapReader beanReader = new CsvMapReader(new FileReader(inputFile), CsvPreference.STANDARD_PREFERENCE);
			try {	
				String[] header = beanReader.getHeader(true);
				final String[] headers = new String[]{BWConstants.TRANSFER_TYPE, BWConstants.COUNTRY_NAME, BWConstants.RECIPIENT_NAME, BWConstants.INDIVIDUAL_BUSINESS,BWConstants.RECIPIENT_ADDRESS_LINE1,BWConstants.RECIPIENT_ADDRESS_LINE2,BWConstants.CITY,BWConstants.STATE,BWConstants.ZIP_CODE,BWConstants.SWIFT_CODE,BWConstants.RECIPIENT_ACCOUNT_NUMBER,BWConstants.ROUTING_NUMBER,BWConstants.INTERNATIONAL_ROUTING_NUMBER,BWConstants.ACCOUNT_NUMBER,BWConstants.RECIPIENT_BANK_NAME,BWConstants.BANK_ADDRESS_LINE1,BWConstants.BANK_ADDRESS_LINE2,BWConstants.BANK_CITY,BWConstants.BANK_STATE,BWConstants.BANK_ZIP_CODE,BWConstants.FROM_ACCOUNT,BWConstants.CURRENCY,BWConstants.AMOUNT,BWConstants.NOTE};
				final CellProcessor[] processors = buildCSVRow();
				Map<String, Object> fieldsInCurrentRow;
				int domesticRecords = 0, internationalRecords = 0;
				int linesCount = getNumberOfLinesInCSVFile(inputFile);
				if(linesCount <= 1) {
					return returnError(ErrorCodeEnum.ERR_14019);
				}
			     if(linesCount>lineItemsLimit+1)
			    	 return returnError(ErrorCodeEnum.ERR_14006);
			     if(linesCount < 0)
			    	 return returnError(ErrorCodeEnum.ERR_14005);
			     if(header[0] == null || !header[0].trim().equalsIgnoreCase(headers[0].trim()) && !header[0].trim().substring(1).equalsIgnoreCase(headers[0].trim()))
					return returnError(ErrorCodeEnum.ERR_14005);
			     for(int i = 1; i < 24; i++) {
					if(header.length!=headers.length || header[i] == null || !header[i].trim().equalsIgnoreCase(headers[i].trim()))
						return returnError(ErrorCodeEnum.ERR_14005);
				}
				while ((fieldsInCurrentRow = beanReader.read(headers, processors)) != null) {
					Map<String, String> rowMap = new HashMap<>();
					rowMap = constructCSVRowMap(fieldsInCurrentRow);
					if(isValidRecord(rowMap)) {
						fileContents = addRecordtoContents(rowMap,fileContents);
						if(rowMap.get("bulkWireTransferType").trim().equalsIgnoreCase("Domestic"))
							domesticRecords++;
						else
							internationalRecords++;
					}
					else {
						return returnError(ErrorCodeEnum.ERR_14005);
					}
	            }
				fileContents.put("domesticRecords", domesticRecords+"");
				fileContents.put("internationalRecords", internationalRecords+"");
				fileContents.put("noOfLineItems", internationalRecords+domesticRecords+"");
				fileContents.put("status","Accepted");
				return fileContents;
			}
			catch(Exception e) {
				alert.prepareError("Error while validating the file").log();
				return returnError(ErrorCodeEnum.ERR_12000);
			}finally {
	        	if (beanReader!=null) {
	        		try {
	        			beanReader.close();			
	        		}
	        		catch(Exception e)
	        		{
	        			alert.prepareError(e.toString()).log();
	        		}
	        	}
	        }
		}
		
		private static Map<String, String> constructCSVRowMap(Map<String, Object> fieldsInCurrentRow) {
			Map<String, String> rowMap = new HashMap<>();
			rowMap.put("bulkWireTransferType",fieldsInCurrentRow.get(BWConstants.TRANSFER_TYPE)+"");
			rowMap.put("recipientCountryName",fieldsInCurrentRow.get(BWConstants.COUNTRY_NAME)+"");
			rowMap.put("recipientName",fieldsInCurrentRow.get(BWConstants.RECIPIENT_NAME)+"");
			rowMap.put("transactionType",fieldsInCurrentRow.get(BWConstants.INDIVIDUAL_BUSINESS)+"");
			rowMap.put("recipientAddressLine1",fieldsInCurrentRow.get(BWConstants.RECIPIENT_ADDRESS_LINE1)+"");
			rowMap.put("recipientAddressLine2",fieldsInCurrentRow.get(BWConstants.RECIPIENT_ADDRESS_LINE2)+"");
			rowMap.put("recipientCity",fieldsInCurrentRow.get(BWConstants.CITY)+"");
			rowMap.put("recipientState",fieldsInCurrentRow.get(BWConstants.STATE)+"");
			rowMap.put("recipientZipCode",String.valueOf(fieldsInCurrentRow.get(BWConstants.ZIP_CODE))+"");
			rowMap.put("swiftCode",fieldsInCurrentRow.get(BWConstants.SWIFT_CODE)+"");
			rowMap.put("recipientAccountNumber",String.valueOf(fieldsInCurrentRow.get(BWConstants.RECIPIENT_ACCOUNT_NUMBER))+"");
			rowMap.put("routingNumber",String.valueOf(fieldsInCurrentRow.get(BWConstants.ROUTING_NUMBER))+"");
			rowMap.put("internationalRoutingNumber",String.valueOf(fieldsInCurrentRow.get(BWConstants.INTERNATIONAL_ROUTING_NUMBER))+"");
			rowMap.put("accountNickname",fieldsInCurrentRow.get(BWConstants.ACCOUNT_NUMBER)+"");
			rowMap.put("recipientBankName",fieldsInCurrentRow.get(BWConstants.RECIPIENT_BANK_NAME)+"");
			rowMap.put("recipientBankAddress1",fieldsInCurrentRow.get(BWConstants.BANK_ADDRESS_LINE1)+"");
			rowMap.put("recipientBankAddress2",fieldsInCurrentRow.get(BWConstants.BANK_ADDRESS_LINE2)+"");
			rowMap.put("recipientBankcity",fieldsInCurrentRow.get(BWConstants.BANK_CITY)+"");
			rowMap.put("recipientBankstate",fieldsInCurrentRow.get(BWConstants.BANK_STATE)+"");
			rowMap.put("recipientBankZipCode",String.valueOf(fieldsInCurrentRow.get(BWConstants.BANK_ZIP_CODE))+"");
			if(fieldsInCurrentRow.size() == 20)
				return rowMap;
			rowMap.put("fromAccountNumber",fieldsInCurrentRow.get(BWConstants.FROM_ACCOUNT)+"");
			rowMap.put("currency",fieldsInCurrentRow.get(BWConstants.CURRENCY)+"");
			rowMap.put("amount",String.valueOf(fieldsInCurrentRow.get(BWConstants.AMOUNT))+"");
			rowMap.put("note",fieldsInCurrentRow.get(BWConstants.NOTE)+"");
			return rowMap;
		}

		private static Map<String, Object> addRecordtoContents(Map<String, String> rowMap, Map<String, Object> fileContents) {
			fileContents.put("bulkWireTransferType", fileContents.get("bulkWireTransferType")+rowMap.get("bulkWireTransferType")+",");
			fileContents.put("recipientCountryName", fileContents.get("recipientCountryName")+rowMap.get("recipientCountryName")+",");
			fileContents.put("recipientName", fileContents.get("recipientName")+rowMap.get("recipientName")+",");
			fileContents.put("transactionType", fileContents.get("transactionType")+rowMap.get("transactionType")+",");
			fileContents.put("recipientAddressLine1", fileContents.get("recipientAddressLine1")+rowMap.get("recipientAddressLine1")+",");
			fileContents.put("recipientAddressLine2", fileContents.get("recipientAddressLine2")+rowMap.get("recipientAddressLine2")+",");
			fileContents.put("recipientCity", fileContents.get("recipientCity")+rowMap.get("recipientCity")+",");
			fileContents.put("recipientState", fileContents.get("recipientState")+rowMap.get("recipientState")+",");
			fileContents.put("recipientZipCode", fileContents.get("recipientZipCode")+rowMap.get("recipientZipCode")+",");
			fileContents.put("swiftCode", fileContents.get("swiftCode")+rowMap.get("swiftCode")+",");
			fileContents.put("recipientAccountNumber", fileContents.get("recipientAccountNumber")+rowMap.get("recipientAccountNumber")+",");
			fileContents.put("routingNumber", fileContents.get("routingNumber")+rowMap.get("routingNumber")+",");
			fileContents.put("internationalRoutingNumber", fileContents.get("internationalRoutingNumber")+rowMap.get("internationalRoutingNumber")+",");
			fileContents.put("accountNickname", fileContents.get("accountNickname")+rowMap.get("accountNickname")+",");
			fileContents.put("recipientBankName", fileContents.get("recipientBankName")+rowMap.get("recipientBankName")+",");
			fileContents.put("recipientBankAddress1", fileContents.get("recipientBankAddress1")+rowMap.get("recipientBankAddress1")+",");
			fileContents.put("recipientBankAddress2", fileContents.get("recipientBankAddress2")+rowMap.get("recipientBankAddress2")+",");
			fileContents.put("recipientBankcity", fileContents.get("recipientBankcity")+rowMap.get("recipientBankcity")+",");
			fileContents.put("recipientBankstate", fileContents.get("recipientBankstate")+rowMap.get("recipientBankstate")+",");
			fileContents.put("recipientBankZipCode", fileContents.get("recipientBankZipCode")+rowMap.get("recipientBankZipCode")+",");
			fileContents.put("fromAccountNumber", fileContents.get("fromAccountNumber")+rowMap.get("fromAccountNumber")+",");
			fileContents.put("currency", fileContents.get("currency")+getCurrency(rowMap.get("currency"))+",");
			fileContents.put("amount", fileContents.get("amount")+rowMap.get("amount")+",");
			fileContents.put("note", fileContents.get("note")+rowMap.get("note")+",");
			
			return fileContents;
		}
		


		public static Map<String, String> buildrowMap(Row row, int columns) {
			Map<String, String> rowMap = new HashMap<>();
			try {
				for(int j = 0; j < columns; j++) {
					Cell cell = row.getCell(j);
					switch(j) {
					case 0 : rowMap.put("bulkWireTransferType",getCellValue(cell,0));
					break;
					case 1 : rowMap.put("recipientCountryName",getCellValue(cell,0));
					break;
					case 2 : rowMap.put("recipientName",getCellValue(cell,0));
					break;
					case 3 : rowMap.put("transactionType",getCellValue(cell,0));
					break;
					case 4 : rowMap.put("recipientAddressLine1",getCellValue(cell,0));
					break;
					case 5 : rowMap.put("recipientAddressLine2",getCellValue(cell,0));
					break;
					case 6 : rowMap.put("recipientCity",getCellValue(cell,0));
					break;
					case 7 : rowMap.put("recipientState",getCellValue(cell,0));
					break;
					case 8 : rowMap.put("recipientZipCode",getCellValue(cell,1));
					break;
					case 9 : rowMap.put("swiftCode",getCellValue(cell,0));
					break;
					case 10 : rowMap.put("recipientAccountNumber",getCellValue(cell,1));
					break;
					case 11 : rowMap.put("routingNumber",getCellValue(cell,1));
					break;
					case 12 : rowMap.put("internationalRoutingNumber",getCellValue(cell,1));
					break;
					case 13 : rowMap.put("accountNickname",getCellValue(cell,0));
					break;
					case 14 : rowMap.put("recipientBankName",getCellValue(cell,0));
					break;
					case 15 : rowMap.put("recipientBankAddress1",getCellValue(cell,0));
					break;
					case 16 : rowMap.put("recipientBankAddress2",getCellValue(cell,0));
					break;
					case 17 : rowMap.put("recipientBankcity",getCellValue(cell,0));
					break;
					case 18 : rowMap.put("recipientBankstate",getCellValue(cell,0));
					break;
					case 19 : rowMap.put("recipientBankZipCode",getCellValue(cell,1));
					break;
					case 20 : rowMap.put("fromAccountNumber",getCellValue(cell,0));
					break;
					case 21 : rowMap.put("currency",getCellValue(cell,0));
					break;
					case 22 : rowMap.put("amount",getCellValue(cell,2));
					break;
					case 23 : rowMap.put("note",getCellValue(cell,0));
					break;
					}
				}
			}
			catch(Exception e) {
				alert.prepareError("Exception while reading data from the file").log();
			}
			return rowMap;
		}
		
		private static String getCellValue(Cell cell, int type) {
			if(cell == null)
				return "";
			String str = "";
			try {
				if(cell.getStringCellValue()!=null) {
					str = cell.getStringCellValue();
					return str;
				}
			}
			catch(Exception e){
				
			}
			try {
				if(type == 2)
					str = String.valueOf(cell.getNumericCellValue());
				else
					str = String.valueOf((long)cell.getNumericCellValue());
				return str;
			
			}
			catch(Exception e){
				
			}
			return str;
		}
		
		public static CellProcessor[] buildCSVTemplateRow() {
			final CellProcessor[] processors = new CellProcessor[] {
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(new ParseInt()),
					new Optional(),
					new Optional(new ParseInt()),
					new Optional(new ParseInt()),
					new Optional(new ParseInt()),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(new ParseInt()),
					new Optional(),
					new Optional(),
					new Optional(new ParseBigDecimal()),
					new Optional()
			};
			return processors;
		}

		public static CellProcessor[] buildCSVRow() {
			final CellProcessor[] processors = new CellProcessor[] {
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(new ParseInt()),
					new Optional(),
					new Optional(new ParseInt()),
					new Optional(new ParseInt()),
					new Optional(new ParseInt()),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(new ParseInt()),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional()
					
					
			};
			return processors;
		}
		
		private static int getNumberOfLinesInCSVFile(File uploadedFile) throws FileNotFoundException {
			if(uploadedFile.length()/(1024*1024)>20) {
				alert.prepareError("The file size exceeds the maximum limit of 20 mb. Please retry with a lesser file size").log();
				return 0;
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(uploadedFile));
			try {
		     String input;
		     int count = 0;
		     while((input = bufferedReader.readLine()) != null)
		     {
		         count++;
		     }
		     return count;
			} catch(Exception e) {
				alert.prepareError("Error while counting no.of lines the file").log();
				return -1;
			}
			finally {
				if(bufferedReader!=null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						alert.prepareError("Error while closing the file",e).log();
					}
				}	
			}
			
		}
		
		private static boolean isValidTemplateRecord(Map<String,String> record) {
			boolean valid = false;
			if(record.get("bulkWireTransferType").trim().equalsIgnoreCase("Domestic"))
				valid = isValidDomesticTemplateRecord(record);
			else if (record.get("bulkWireTransferType").trim().equalsIgnoreCase("International"))
				valid = isValidInternationalTemplateRecord(record);
			return valid;
		}
		
		private static boolean isValidInternationalTemplateRecord(Map<String, String> record) {
			String[] keys = { "swiftCode", "internationalRoutingNumber","recipientName", "recipientAddressLine1", "recipientCity", "recipientState", "recipientZipCode", "recipientBankName", "recipientBankAddress1", "recipientBankZipCode", "recipientBankcity", "recipientBankstate", "accountNickname", "recipientAccountNumber"};
			if(isValidTransactionType(record.get("transactionType")) && isValidCountry(record.get("recipientCountryName"))){
				for(String key : keys) {
					if(!(isValid(record.get(key)))) {
						return false;
					}
				}
			}
			else {
				return false;
			}
			return true;
		}



		private static boolean isValidDomesticTemplateRecord(Map<String, String> record) {
			String[] keys = {"recipientName", "recipientAddressLine1", "recipientCity", "recipientState", "recipientZipCode", "recipientBankName", "recipientBankAddress1", "recipientBankZipCode", "recipientBankcity", "recipientBankstate", "accountNickname", "recipientAccountNumber", "routingNumber" };
			if(isValidTransactionType(record.get("transactionType")) && isValidCountry(record.get("recipientCountryName"))){
				for(String key : keys) {
					if(!(isValid(record.get(key)))) {
						return false;
					}
				}
			}
			else {
				return false;
			}
			return true;
		}



		public static boolean isValidRecord(Map<String,String> record) {
			boolean valid = false;
			if(record.get("bulkWireTransferType").trim().equalsIgnoreCase("Domestic"))
				valid = isValidDomesticRecord(record);
			else if (record.get("bulkWireTransferType").trim().equalsIgnoreCase("International"))
				valid = isValidInternationalRecord(record);
			return valid;
		}
		
		public static boolean isValidDomesticRecord(Map<String,String> record) {
			if(isValidTransactionType(record.get("transactionType")) && isValid(record.get("recipientName")) && isValid(record.get("recipientBankName")) && isValid(record.get("routingNumber")) && isValidAmount(record.get("amount")) && isValid(getCurrency(record.get("currency"))) && isValidAccountNumber(record.get("fromAccountNumber")) && isValid(record.get("recipientAccountNumber")) && isValidCountry(record.get("recipientCountryName")))
				return true;
			return false;
		}

		public static boolean isValidInternationalRecord(Map<String,String> record) {
			if(isValidTransactionType(record.get("transactionType")) && isValid(record.get("recipientName")) && isValid(record.get("recipientBankName")) && isValid(record.get("internationalRoutingNumber")) && isValid(record.get("swiftCode")) && isValidAmount(record.get("amount")) && isValid(getCurrency(record.get("currency"))) && isValidAccountNumber(record.get("fromAccountNumber")) && isValid(record.get("recipientAccountNumber")) && isValidCountry(record.get("recipientCountryName")))
				return true;
			return false;
		}
		
		private static boolean isValidTransactionType(String transactionType) {
			if(transactionType.trim().equalsIgnoreCase("Individual") || transactionType.trim().equalsIgnoreCase("Business"))
				return true;
			return false;
		}

		private static boolean isValidCountry(String country) {
			return countries.contains(country);
		}

		public static boolean isValidAmount(String amount) {
			String regExp = "^\\d+\\.\\d{0,2}$";
			return amount.matches(regExp);
		}
		
		public static boolean isValidAccountNumber(String accountNumber) {
			if(accounts.containsKey(accountNumber)) {
				AccountsDTO account = accounts.get(accountNumber);
				AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
				if(accountTypes.get(account.getTypeId()).equalsIgnoreCase("Checking") &&  authorizationChecksBusinessDelegate.isOneOfMyAccounts(userId,accountNumber))
					return true;
			}
			return false;
		}
		
		
		public static boolean isValid(String val) {
			if(val == null || val.trim() == "")
				return false;
			return true;
		}
		
		private static String getCurrency(String currency) {
			if(currencies.contains(currency.trim().toUpperCase()))
				return currency.trim().toUpperCase();
			return "";
		}
		
		private void setCountriesSet(List<CountryListDTO> countriesList) {
			try {
				int countriesCount = countriesList.size();
				for(int i = 0; i < countriesCount; i++) 
					countries.add(countriesList.get(i).getName());
			}
			catch(Error e) {
				alert.prepareError("Error while getting countries list").log();
			}
		}
		
//		private void setCurrencyMap(List<CurrencyListDTO> currenciesList) {
//			try {
//				int currenciesCount = currenciesList.size();
//				for(int i = 0; i < currenciesCount; i++) {
//					currencies.put(currenciesList.get(i).getSymbol(), currenciesList.get(i).getCode());
//				}
//			}
//			catch(Error e) {
//				alert.prepareError("Error while getting currencies list").log();
//			}
//		}
		
		private void setCurrencyMap() {
			try {
				currencies.add("EUR");
				currencies.add("GBP");
				currencies.add("USD");
				currencies.add("INR");
			}
			catch(Error e) {
				alert.prepareError("Error while getting currencies list").log();
			}
		}
		
		
		private void setAccountTypesMap(List<AccountTypesDTO> accountTypesList) {
			try {
				int accountTypesCount = accountTypesList.size();
				for(int i = 0; i < accountTypesCount; i++) {
					accountTypes.put(accountTypesList.get(i).getTypeid(), accountTypesList.get(i).getTypeDescription());
				}
			}
			catch(Error e) {
				alert.prepareError("Error while getting account types list").log();
			}
		}

		private void setAccountsMap(List<AccountsDTO> accountsList) {
			try {
				int accountsCount = accountsList.size();
				for(int i = 0; i < accountsCount; i++) {
					accounts.put(accountsList.get(i).getAccountId(),accountsList.get(i));
				}
			}
			catch(Error e) {
				alert.prepareError("Error while getting accounts list").log();
			}
		}
		
		@Override
		public Map<String, Object> appendFileId(Map<String, Object> bWfileMapContents, String fileId){
			int lineItems = Integer.parseInt((String) bWfileMapContents.get("noOfLineItems"));
			bWfileMapContents.put("bulkWireFileID", "");
			for(int i = 0; i < lineItems; i++) {
				bWfileMapContents.put("bulkWireFileID",bWfileMapContents.get("bulkWireFileID")+fileId+",");
			}
			return bWfileMapContents;
		}
		
		@Override
		public boolean isValidFileName(String fileName, String fileExtension) {
	    	String regExp = "^[a-zA-Z0-9]+$";
	    	if(!fileName.substring(0,fileName.length()-fileExtension.length()-1).matches(regExp)) {
		  		return false;
		  	}
			return true;
	    }
		
		@Override
		public String getFileFormat(String fileExtension) {
	    	BulkWireFileBusinessDelegate bwFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class)
	                .getBusinessDelegate(BulkWireFileBusinessDelegate.class);
	    	List<BulkWireFileFormatTypeDTO> fileTypes = bwFileBusinessDelegate.getBulkWireFileFormatTypes();
	        int fileTypesCount = fileTypes.size();
	        for (int i = 0; i < fileTypesCount; i++) {
	           if (fileExtension.equals(fileTypes.get(i).getBulkWiresFileFormatTypeName())) {
	             return fileTypes.get(i).getBulkWiresFileFormatTypeCode();
	           }
	        }
	        return "-1";
	    }
				
}
