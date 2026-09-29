package com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.infinity.dbx.temenos.fileutil.PDFGeneratorTransactionAcknowledgement;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireFileBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.CurrencyListDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import  com.lowagie.text.Document;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.RequestHistoryDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;
import com.temenos.infinity.product.bulkpaymentservices.backenddelegate.api.BulkPaymentFileBackendDelegate;
import com.temenos.infinity.product.bulkpaymentservices.backenddelegate.api.BulkPaymentPOBackendDelegate;
import com.temenos.infinity.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentFileBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentPODTO;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;
import com.kony.dbputilities.fileutil.PDFGenerator;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;

import com.lowagie.text.*;


public class BulkPaymentFileBusinessDelegateImpl implements BulkPaymentFileBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private final String PAYMENT_ORDER_EXPR = ".//CdtTrfTxInf";
	private final String ACCOUNT_NUMBER_EXPR = "./CdtrAcct/Id/IBAN";
	private final String SWIFT_CODE_EXPR = "./CdtrAgt/FinInstnId/BIC";
	private final String CURRENCY_EXPR = ".//InstdAmt/@Ccy";
	private final String PAYMENT_METHOD_EXPR = "//PmtInf/PmtTpInf/SvcLvl/Cd";
	private final String AMOUNT_EXPR = ".//InstdAmt";
	private final String RECIPIENT_NAME_EXPR = ".//Cdtr/Nm";
	private final String DESCRIPTION_EXPR = "//GrpHdr/MsgId";
	private final String FROM_ACCOUNT_EXPR = "//DbtrAcct/Id/IBAN";
	private final String FROM_ACCOUNT_ID_EXPR = "//DbtrAcct/Id/Othr/Id";
	private final String RECORD_CURRENCY_EXPR = "//DbtrAcct/Ccy";
	private final String PAYMENT_DATE_EXPR = "//PmtInf/ReqdExctnDt";
	private final String TOTAL_AMOUNT_EXPR = "//PmtInf/CtrlSum";
	private final String TOTAL_TRANSACTIONS_EXPR = "//PmtInf/NbOfTxs";
	private final String PAYMENT_INFO_EXPR = "//PmtInf";
	private final String BATCH_MODE_EXPR = "//PmtInf//BtchBookg";
	
	private final String FILE_DATE_FORMAT = "yyyyMMdd";
	private final String BACKEND_DATE_FORMAT = "yyyy-MM-dd";
	private final String BATCH_MODE_SINGLE = "SINGLE";
	private final String BATCH_MODE_MULTI = "MULTI";
	
	private final  String INFINITY_LOGO = "infinity_logo.png";
	private final Color BANK_ADDR_FONT_COLOR = new Color(128, 128, 128);
	private final  Color TRANS_HEADER_FONT_COLOR = new Color(0, 0, 0);
	private final  Color TRANS_TABLE_FIELD_FONT_COLOR = new Color(128, 128, 128);
	private final  Color TRANS_TABLE_VALUE_FONT_COLOR = new Color(0, 0, 0);
	//private final  Color BORDER_COLOR = new Color(125, 121, 121);
	private final  Color BORDER_COLOR = new Color(0, 0, 0);
	private final  Color WHITE_COLOR =new Color(255, 255, 255);
	private final  float BORDER_WIDTH = 0.55f;
	private final  float DEFAULT_MARGIN_LEFT = 20f;
	private final String SERVICEID_TRANSACTION_OBJECTS = "TransactionObjects";
	private final String OBJECTID_BANK_NAME="BankDate";
	
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	BulkPaymentFileBackendDelegate bulkPaymentFileBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentFileBackendDelegate.class);
	
	@Override
	public BulkPaymentFileDTO uploadBulkPaymentFileAtDBX(BulkPaymentFileDTO bulkPaymentfileDTO) {
		BulkPaymentFileDTO bulkPaymentDTO = new BulkPaymentFileDTO();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENT_FILE_CREATE ;
		Map<String, Object> requestParameters;
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentfileDTO).toString(), String.class, Object.class);
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params", e).log();						
			return null;
		}

		String uploadResponse = null;
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			uploadResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject response = new JSONObject(uploadResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			bulkPaymentDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), BulkPaymentFileDTO.class);
			
		} catch (JSONException e) {
			alert.prepareError("Unable to Upload Bulk Payment File", e).log();		
			return null;			
		} catch (Exception e) {
			alert.prepareError("Unable to store the file at Infinity", e).log();			
			return null;
		}
		
		return bulkPaymentDTO;
	}
		
	/**
	 * method to parse Bulk Payments File
	 * @param File  
	 * @return BulkPaymentFileDTO 
	 */
	@Override
	public BulkPaymentFileDTO parseFile(File file) {

		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		String fileType = name.substring(lastIndexOf);
		BulkPaymentFileDTO bulkpaymentfileDTO = new BulkPaymentFileDTO();		
		bulkpaymentfileDTO.setFileSize(String.valueOf(file.getTotalSpace()));

		try {
			bulkpaymentfileDTO.setBulkPaymentRecord(fetchBulkPaymentRecord(file));

			if(".csv".equalsIgnoreCase(fileType)) {
				List<BulkPaymentPODTO> paymentOrders = new ArrayList<>();
				double totalAmount = 0;
				@SuppressWarnings("resource")
				CSVParser csvParser = new CSVParser(new FileReader(file),CSVFormat.DEFAULT);
				List<CSVRecord> csvRecords = csvParser.getRecords();
				for (int i = 1; i < csvRecords.size(); i++) {
					BulkPaymentPODTO bulkPaymentPODTO = new BulkPaymentPODTO();
					CSVRecord record = csvRecords.get(i);
					String account = record.get(22) == null ? record.get(22) : record.get(21);
					bulkPaymentPODTO.setPaymentMethod(record.get(0));
					bulkPaymentPODTO.setAccountNumber(account);	
					bulkPaymentPODTO.setCurrency(record.get(64));
					bulkPaymentPODTO.setAmount(Double.valueOf(record.get(65)));
					bulkPaymentPODTO.setRecipientName(record.get(25));
					bulkPaymentPODTO.setFeesPaidBy(record.get(77));
					bulkPaymentPODTO.setSwift(record.get(38));
					//bulkPaymentPODTO.setPaymentReference(record[]);
					paymentOrders.add(bulkPaymentPODTO);
					totalAmount += Double.valueOf(record.get(65));
				}
				bulkpaymentfileDTO.setPaymentOrders(paymentOrders);	
				bulkpaymentfileDTO.setTotalAmount(totalAmount);
				bulkpaymentfileDTO.setTotalTransactions(csvRecords.size()-1);
				BulkPaymentRecordDTO bulkpaymentrecordDTO = bulkpaymentfileDTO.getBulkPaymentRecord();
				bulkpaymentrecordDTO.setTotalAmount(totalAmount);
				bulkpaymentrecordDTO.setTotalTransactions(csvRecords.size()-1);
				bulkpaymentfileDTO.setBulkPaymentRecord(bulkpaymentrecordDTO);
				csvParser.close();
			}
			else if(".xml".equalsIgnoreCase(fileType)) {			
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				String DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
				dbFactory.setFeature(DOCTYPE_DECL, true);
				dbFactory.setValidating(true);
				org.w3c.dom.Document  doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				XPath xPath =  XPathFactory.newInstance().newXPath();
				String paymentMethod = "";
				try {
					paymentMethod = xPath.evaluate(PAYMENT_METHOD_EXPR,doc);
				}
				catch(XPathExpressionException e) {
					//do nothing
				}
								
				NodeList nodeList = (NodeList) xPath.evaluate(PAYMENT_ORDER_EXPR,doc, XPathConstants.NODESET);
				List<BulkPaymentPODTO> paymentOrders=new ArrayList<>(); 

				for (int i = 0; i < nodeList.getLength(); i++) {
					BulkPaymentPODTO bulkPaymentPODTO = new BulkPaymentPODTO();
					Node nNode = nodeList.item(i);
					bulkPaymentPODTO.setPaymentMethod(paymentMethod);
					bulkPaymentPODTO.setAccountNumber(xPath.evaluate(ACCOUNT_NUMBER_EXPR,nNode));
					bulkPaymentPODTO.setSwift(xPath.evaluate(SWIFT_CODE_EXPR,nNode));
					bulkPaymentPODTO.setCurrency(xPath.evaluate(CURRENCY_EXPR,nNode));
					bulkPaymentPODTO.setAmount(Double.valueOf(xPath.evaluate(AMOUNT_EXPR,nNode)));
					bulkPaymentPODTO.setRecipientName(xPath.evaluate(RECIPIENT_NAME_EXPR,nNode));
					paymentOrders.add(bulkPaymentPODTO);
				}
				bulkpaymentfileDTO.setPaymentOrders(paymentOrders);
				bulkpaymentfileDTO.setTotalAmount(bulkpaymentfileDTO.getBulkPaymentRecord().getTotalAmount());
				bulkpaymentfileDTO.setTotalTransactions(bulkpaymentfileDTO.getBulkPaymentRecord().getTotalTransactions());
			}
			bulkpaymentfileDTO.setFromAccount(bulkpaymentfileDTO.getBulkPaymentRecord().getFromAccount());
		} catch (Exception e) {
			alert.prepareError("Error while parsing the bulk payments file", e).log();			
			return null;
		}
		return bulkpaymentfileDTO;			
	}

	/**
	 * method to fetch Bulk Payment Header Information    
	 * @param File  
	 * @return BulkPaymentRecordDTO 
	 */
	@Override
	public BulkPaymentRecordDTO fetchBulkPaymentRecord(File file) {

		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		String fileType = name.substring(lastIndexOf);
		BulkPaymentRecordDTO bulkpaymentrecordDTO = new BulkPaymentRecordDTO();
		
		try {
			if(".csv".equalsIgnoreCase(fileType)) {			
				@SuppressWarnings("resource")
				CSVParser csvParser = new CSVParser(new FileReader(file),CSVFormat.DEFAULT);
				List<CSVRecord> csvRecords = csvParser.getRecords();
				CSVRecord record = csvRecords.get(0);
				if (record != null) {		
					bulkpaymentrecordDTO.setDescription(record.get(0));
					if(StringUtils.isEmpty(record.get(3))) {
						bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21258);
						return bulkpaymentrecordDTO;
					}
					bulkpaymentrecordDTO.setBatchMode(record.get(3));
					
					if(!StringUtils.isEmpty(record.get(5))) {
					bulkpaymentrecordDTO.setFromAccount(record.get(5));
					}
					else bulkpaymentrecordDTO.setFromAccount(record.get(38));
					
					bulkpaymentrecordDTO.setCurrency(record.get(6));
					
					SimpleDateFormat fileFormat = new SimpleDateFormat(FILE_DATE_FORMAT);
					SimpleDateFormat backEndFormat = new SimpleDateFormat(BACKEND_DATE_FORMAT);
					Date backEndDate = backEndFormat.parse(backEndFormat.format(fileFormat.parse(record.get(10))));	
					bulkpaymentrecordDTO.setPaymentDate(backEndFormat.format(backEndDate));	
					
//					Date currentDate = backEndFormat.parse(application.getServerTimeStamp());
//					fileFormat.setLenient(false);
//					if(StringUtils.isNotBlank(record.get(10))) {
//						Date backEndDate = backEndFormat.parse(backEndFormat.format(fileFormat.parse(record.get(10))));	
//						if(currentDate.after(backEndDate)) {
//							bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_26002);
//							return bulkpaymentrecordDTO;
//						}
//						bulkpaymentrecordDTO.setPaymentDate(backEndFormat.format(backEndDate));	
//					}
//					
//					if (StringUtils.isNotBlank(record.get(11)) && currentDate.after(fileFormat.parse(record.get(11)))) {
//						bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_26002);
//						return bulkpaymentrecordDTO;
//					}
//
//					if (StringUtils.isNotBlank(record.get(12)) && currentDate.after(fileFormat.parse(record.get(12)))) {
//						bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_26002);
//						return bulkpaymentrecordDTO;
//					}
				}
				csvParser.close();
			}
			else if(".xml".equalsIgnoreCase(fileType)) {						
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				String DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
				dbFactory.setFeature(DOCTYPE_DECL, true);
				dbFactory.setValidating(true);
				org.w3c.dom.Document doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				XPath xPath =  XPathFactory.newInstance().newXPath();
				
				NodeList paymentInfoList = (NodeList) xPath.evaluate(PAYMENT_INFO_EXPR,doc, XPathConstants.NODESET);
				if(paymentInfoList.getLength() > 1) {
					bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21251);
					return bulkpaymentrecordDTO;
				}
				String date = xPath.evaluate(PAYMENT_DATE_EXPR,doc);
//				bulkpaymentrecordDTO.setPaymentDate(date.substring(4, 14));
				bulkpaymentrecordDTO.setPaymentDate(date);
//				
//				SimpleDateFormat fileFormat = new SimpleDateFormat(BACKEND_DATE_FORMAT);
//				fileFormat.setLenient(false);
//				if(StringUtils.isNotBlank(xPath.evaluate(PAYMENT_DATE_EXPR,doc))) {
//					Date paymentDate = fileFormat.parse(xPath.evaluate(PAYMENT_DATE_EXPR,doc));	
//					Date currentDate = fileFormat.parse(application.getServerTimeStamp());
//					if(currentDate.after(paymentDate)) {
//						bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_26002);
//						return bulkpaymentrecordDTO;
//					}
//					bulkpaymentrecordDTO.setPaymentDate(xPath.evaluate(PAYMENT_DATE_EXPR,doc));	
//				}

				bulkpaymentrecordDTO.setDescription(xPath.evaluate(DESCRIPTION_EXPR,doc));
				if(!StringUtils.isEmpty(xPath.evaluate(FROM_ACCOUNT_EXPR, doc))) {
					bulkpaymentrecordDTO.setFromAccount(xPath.evaluate(FROM_ACCOUNT_EXPR,doc));						
				}
				else {
					bulkpaymentrecordDTO.setFromAccount(xPath.evaluate(FROM_ACCOUNT_ID_EXPR,doc));	
				}								
				bulkpaymentrecordDTO.setCurrency(xPath.evaluate(RECORD_CURRENCY_EXPR,doc));	
				bulkpaymentrecordDTO.setTotalAmount(Double.valueOf(xPath.evaluate(TOTAL_AMOUNT_EXPR,doc)));
				bulkpaymentrecordDTO.setTotalTransactions(Double.valueOf(xPath.evaluate(TOTAL_TRANSACTIONS_EXPR,doc)));	
				if(StringUtils.isEmpty(xPath.evaluate(BATCH_MODE_EXPR, doc))) {
					bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21258);
					return bulkpaymentrecordDTO;
				}
				if ("true".equalsIgnoreCase(xPath.evaluate(BATCH_MODE_EXPR, doc)))
					bulkpaymentrecordDTO.setBatchMode(BATCH_MODE_SINGLE);
				else if("false".equalsIgnoreCase(xPath.evaluate(BATCH_MODE_EXPR, doc)))
					bulkpaymentrecordDTO.setBatchMode(BATCH_MODE_MULTI);
			}	
			else {
				alert.prepareError("You have added an Invalid file. Please upload file with correct filename and extension .csv or .xml").log();			
				bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21219);
				return bulkpaymentrecordDTO;
			}
		} catch (Exception e) {
			alert.prepareError("Error occured while parsing bulk payments file", e).log();
			return null;	         
		}	
		return bulkpaymentrecordDTO;
	}
	
	public String getCurrentDateForUpload(DataControllerRequest request) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(SERVICEID_TRANSACTION_OBJECTS)
					.withObjectId(OBJECTID_BANK_NAME).withOperationId(OperationName.GETBANKDATE)
					.withRequestParameters(null).withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			String workingDate = ((JSONObject) jsonArray.get(0)).getString(Constants.CURRENT_WORKING_DATE);
			DateFormat shortDateFormat = new SimpleDateFormat(Constants.SHORT_DATE_FORMAT);
			if (!shortDateFormat.format(new Date()).equalsIgnoreCase(workingDate)) {
				return dateFormat.format(shortDateFormat.parse(workingDate));
			}
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching Server TimeStamp", e).log();
		}

		ApplicationDTO props = new ApplicationDTO();

		String timezone = props.getTimeZoneOffset();
		String pos = null;

		if (timezone != null && timezone.indexOf("-") > 0) {
			pos = "-";
		} else if (timezone != null && timezone.indexOf("+") > 0) {
			pos = "+";
		} else {
			timezone = "UTC+00:00";
			pos = "+";
		}

		String[] offset = timezone.split("\\" + pos);
		String[] time = offset[1].split(":");
		int hours = Integer.parseInt(pos + time[0]);
		int minutes = Integer.parseInt(pos + time[1]);
		int offsetMillis = (hours * 60 * 60 + minutes * 60) * 1000;

		TimeZone tz = TimeZone.getDefault();
		tz.setRawOffset(offsetMillis);
		dateFormat.setTimeZone(tz);

		Date date = new Date();
		String finalDate = dateFormat.format(date);

		return finalDate;
	}

	@Override
	public List<BulkPaymentFileDTO> fetchBulkPaymentFiles(String customerId, List<String> companyIds) {
		
		List<BulkPaymentFileDTO> bulkPaymentFileDTOs;
		List<BulkPaymentFileDTO> filteredDTOs;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTFILE_GET;
		
		if (CollectionUtils.isNotEmpty(companyIds))
			filter =  DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;
		
		if(StringUtils.isNotEmpty(customerId)) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.OR;
			} 
			filter = filter + "uploadedBy" + DBPUtilitiesConstants.EQUAL + customerId;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		String uploadResponse;
		
		try {
			uploadResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(uploadResponse);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            bulkPaymentFileDTOs = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentFileDTO.class);
            
            Set<String> uploadedByList = new HashSet<String>();
            for(BulkPaymentFileDTO dto : bulkPaymentFileDTOs) {
            	uploadedByList.add(dto.getUploadedBy());
            	dto.setUploadedBy(null);
            }
            
            List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(uploadedByList);
            filteredDTOs = (new FilterDTO()).merge(bulkPaymentFileDTOs, customerDTOs, "createdby=id","");
			
		} catch (JSONException e) {
			alert.prepareError("Unable to fetch Bulk Payment File", e).log();		
			return null;			
		} catch (Exception e) {
			alert.prepareError("Unable to fetch the file at Infinity", e).log();			
			return null;
		}
		
		return filteredDTOs;
	}
	@Override
	public List<BulkPaymentFileDTO> fetchBulkPaymentFiles(Set<String> uploadIds) {
		//Performance Change - override Function
		List<BulkPaymentFileDTO> bulkPaymentFileDTOs;
		List<BulkPaymentFileDTO> filteredDTOs;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTFILE_GET;

		if (CollectionUtils.isNotEmpty(uploadIds))
			filter =  DBPUtilitiesConstants.OPEN_BRACE +
					"confirmationNumber" + DBPUtilitiesConstants.EQUAL +
					String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, uploadIds)
					+ DBPUtilitiesConstants.CLOSE_BRACE;

		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String uploadResponse;

		try {
			uploadResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();

			JSONObject responseObj = new JSONObject(uploadResponse);
			diagnostic.prepareDebug("Record Table Fetch Success : " + uploadResponse).log();
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			bulkPaymentFileDTOs = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentFileDTO.class);

			Set<String> uploadedByList = new LinkedHashSet<String>();
			for(BulkPaymentFileDTO dto : bulkPaymentFileDTOs) {
				uploadedByList.add(dto.getUploadedBy());
				dto.setUploadedBy(null);
			}

			List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(uploadedByList);
			filteredDTOs = (new FilterDTO()).merge(bulkPaymentFileDTOs, customerDTOs, "createdby=id","initiatedBy,uploadedBy");

		} catch (JSONException e) {
			alert.prepareError("Unable to fetch Bulk Payment File", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Unable to fetch the file at Infinity", e).log();
			return null;
		}
		return filteredDTOs;
	}
	@Override
	public List<BulkPaymentFileDTO> fetchBulkPaymentUploadedFilesfromBackend(String fromDate, String toDate,String userSignOnName ,DataControllerRequest request) {
		List<BulkPaymentFileDTO> uploadedFiles;
		List<BulkPaymentFileDTO> fetchedFiles = bulkPaymentFileBackendDelegate.fetchBulkPaymentUploadedFiles(fromDate, toDate, userSignOnName ,request);

		if(fetchedFiles == null || fetchedFiles.size() == 0) {
			return fetchedFiles;
		}
		
		if(fetchedFiles.size() > 0 && StringUtils.isNotBlank(fetchedFiles.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching bulk payments files from backend").log();
			return null;
		}
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
		contracts.add(CustomerSession.getCompanyId(customer));
				
		List<BulkPaymentFileDTO> files = fetchBulkPaymentFiles(customerId, contracts); 
		if(files == null) {
			return null;
		}
		uploadedFiles = (new FilterDTO()).merge(fetchedFiles, files, "fileId=confirmationNumber", "uploadedBy,uploadedDate");
		if(uploadedFiles.size() == 0){
			alert.prepareError("No Records Found").log();
		}
		return uploadedFiles;
	}
	
	@Override
	public byte[] getRecordPDFAsBytes(String recordId,String backendId ,String requestId, String customerId, DataControllerRequest request) {

		PropertyConfigurator.configure(PDFGenerator.class.getClassLoader().getResourceAsStream("log4j.properties"));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		Rectangle pageSize = document.getPageSize();
		 final float pageTop = pageSize.getTop() - 70;
		final float pageLeft = pageSize.getLeft() + 45;
		PdfWriter writer = PdfWriter.getInstance(document, os);
         document.open();


		//main table
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
//
//		try {
//			document.add(_createlogo(document));
//		} catch (Exception e) {
//			alert.prepareError("Error occured while creating bulk payments details pdf ", e).log();
//		}

		BulkPaymentRecordBackendDelegate bulkPaymentRecordBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
		BulkPaymentRecordDTO bulkRecord = bulkPaymentRecordBackendDelegate.fetchBulkPaymentRecordDetailsById(recordId, request);

		BulkPaymentPOBackendDelegate bulkPaymentPOBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentPOBackendDelegate.class);
		List<BulkPaymentPODTO> paymentOrders = bulkPaymentPOBackendDelegate.fetchPaymentOrders(recordId, request);

		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		List<RequestHistoryDTO> requestDto  = approvalQueueBusinessDelegate.fetchRequestHistory(requestId, customerId);

		CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
		JSONObject customerJson = customerBusinessDelegate.getUserDetails(customerId);
		String userName = customerJson.get(Constants.FirstName).toString();

		CustomerAccountsDTO account=null;
		String BULKPAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue("BULKPAYMENT_BACKEND");
		if (BULKPAYMENT_BACKEND != null && !BULKPAYMENT_BACKEND.equalsIgnoreCase("MOCK")) {
			AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
			account = actDelegate.getAccountDetails(customerId, bulkRecord.getFromAccount());
		}
		if(!backendId.equalsIgnoreCase(bulkRecord.getCreatedby())){
			return null;
		}
		String accountName=account!=null?account.getAccountName():null;

		//Addlogo
		addLogo(table,document,pageTop,pageLeft);

         //Add Payment Summary in table
		if(StringUtils.isNotBlank(recordId) && StringUtils.isNotBlank(userName)) {
			table.addCell(_createPaymentSummary(table, bulkRecord,userName,accountName));
		}


		BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);
		boolean isGroupMatrix = false;
		if (requestDTO != null) {
			isGroupMatrix = Boolean.parseBoolean(requestDTO.getIsGroupMatrix());
			if (requestDTO.getStatus().equalsIgnoreCase(TransactionStatusEnum.PENDING.getStatus())) {
				if (!isGroupMatrix) {
					JSONArray jsonArray = approvalQueueBusinessDelegate.fetchApprovers(requestId, customerId);
					Set<String> approverIds = new HashSet<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);
						approverIds.add((String) object.get("customerId"));
					}
					CustomerBusinessDelegate custDelegate = DBPAPIAbstractFactoryImpl
							.getBusinessDelegate(CustomerBusinessDelegate.class);
					List<CustomerDTO> approvers = custDelegate.getCustomerInfo(approverIds);
					Set<String> actedUser = new HashSet<String>();
					for (RequestHistoryDTO requestHistoryDTO : requestDto) {
						if (!requestHistoryDTO.getStatus().equals(TransactionStatusEnum.PENDING.getStatus())
								&& requestHistoryDTO.getSoftdeleteflag().equalsIgnoreCase(Constants.FALSE))
							actedUser.add(requestHistoryDTO.getCreatedby());
					}
					for (CustomerDTO customerDTO : approvers) {
						if (!actedUser.contains(customerDTO.getId())) {
							RequestHistoryDTO pendingApprover = new RequestHistoryDTO();
							pendingApprover.setUserName(customerDTO.getFullName());
							pendingApprover.setRequestId(requestId);
							pendingApprover.setStatus(Constants.APPROVAL_PENDING);
							pendingApprover.setAction(Constants.APPROVAL_PENDING);
							requestDto.add(pendingApprover);
						}
					}
				} else {
					List<SignatoryGroupDTO> approversList = null;
					try {
						approversList = JSONUtils.parseAsList(
								approvalQueueBusinessDelegate.fetchApprovers(requestId, customerId).toString(),
								SignatoryGroupDTO.class);
					} catch (IOException e) {
						alert.prepareError("Error occured while fetching signatory approverList ", e).log();
					}

					List<RequestHistoryDTO> pendingApproverList = new ArrayList<RequestHistoryDTO>();
					for (SignatoryGroupDTO approver : approversList) {
						RequestHistoryDTO pendingApprover = new RequestHistoryDTO();
						pendingApprover.setGroupName(approver.getSignatoryGroupName());
						pendingApprover.setRequestId(requestId);
						pendingApprover.setStatus(Constants.APPROVAL_PENDING);
						pendingApprover.setAction(Constants.APPROVAL_PENDING);
						pendingApprover.setGroupId(approver.getSignatoryGroupId());
						pendingApproverList.add(pendingApprover);
					}
					requestDto.addAll(pendingApproverList);

				}
			}
		}
		if(StringUtils.isNotBlank(requestId)) {
		table.addCell(_createApprovalHistoryInfo(table, requestDto, isGroupMatrix,bulkRecord));
		}

		table.addCell(_createPaymentOrders(table, paymentOrders, bulkRecord.getStatus()));
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

	private void addLogo(PdfPTable table,Document document,float pageTop,float pageLeft) {
		InputStream is = PDFGeneratorTransactionAcknowledgement.class.getClassLoader().getResourceAsStream("infinity_logo.png");
		try {
			Image infinityLogo = Image.getInstance(IOUtils.toByteArray(is));
			infinityLogo.setSpacingAfter(10);
			infinityLogo.scaleAbsolute(70, 28);
			infinityLogo.setAbsolutePosition(pageLeft, pageTop);
     		infinityLogo.scaleAbsoluteHeight(20);
     		infinityLogo.scaleAbsoluteWidth(90);
			PdfPCell cellLogo = new PdfPCell();
			cellLogo.addElement(infinityLogo);
			cellLogo.setBorder(Rectangle.BOX);
			cellLogo.setBorderColor(Color.DARK_GRAY);
			table.addCell(cellLogo);

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

	private PdfPTable _createPaymentSummary(PdfPTable table, BulkPaymentRecordDTO bulkRecord, String userName, String accountName) {

		PdfPTable bankTable = new PdfPTable(2);
		bankTable.setSpacingBefore(10);
		bankTable.setWidthPercentage(100);
		bankTable.setWidths(new float[]{1.5f, 2.2f});

		PdfPCell addrCell = new PdfPCell(new Paragraph("Payment Summary"));
		addrCell.setColspan(2);
		_setBorder(addrCell);
		bankTable.addCell(addrCell);
		bankTable.completeRow();

		bankTable.addCell(_noBorderCell1("From Account: ", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(bulkRecord.getFromAccount())) {
			String accNo = bulkRecord.getFromAccount();
			String maskedAccNo = new String("X" + accNo.substring(accNo.length() - 4));
			accountName = StringUtils.join(accountName,"....-",maskedAccNo);
			bankTable.addCell(_noBorderCell1(accountName, TRANS_TABLE_VALUE_FONT_COLOR));
		} else {
			bankTable.addCell(_noBorderCell1("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}
		bankTable.completeRow();

		bankTable.addCell(_noBorderCell("Payment Description:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(bulkRecord.getDescription())) {
			bankTable.addCell(_noBorderCell(bulkRecord.getDescription(), TRANS_TABLE_VALUE_FONT_COLOR));
		} else {
			bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}
		bankTable.completeRow();

		//commented as part of DBB-9210
		//transferTable.addCell(_getNoBorderCell().add(new Paragraph("Initiated By:")));
		//if(StringUtils.isNotBlank(userName)) {
		//	transferTable.addCell(_getNoBorderCellWithDefaultColor().add(new Paragraph(userName)));
		//} else {
		//	transferTable.addCell(_getNoBorderCellWithDefaultColor().add(new Paragraph("N/A")));
		//}

		try {
			bankTable.addCell(_noBorderCell("Execution Date:", TRANS_TABLE_FIELD_FONT_COLOR));
			if(StringUtils.isNotBlank(bulkRecord.getPaymentDate())) {
				String executionDate = HelperMethods.convertDateFormat(bulkRecord.getPaymentDate(), Constants.BULK_DATEFORMAT);
				bankTable.addCell(_noBorderCell(executionDate, TRANS_TABLE_VALUE_FONT_COLOR));
			} else {
				bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
			}

//			transferTable.addCell(_getNoBorderCell().add(new Paragraph("Value Date:")));
//			if(StringUtils.isNotBlank(bulkRecord.getScheduledDate())) {
//				String valueDate = HelperMethods.convertDateFormat(bulkRecord.getScheduledDate(), Constants.BULK_DATEFORMAT);
//				transferTable.addCell(_getNoBorderCellWithDefaultColor().add(new Paragraph(valueDate)));
//			} else {
//				transferTable.addCell(_getNoBorderCellWithDefaultColor().add(new Paragraph("N/A")));
//			}
		}
		catch(Exception e) {
			alert.prepareError("Error occured while creating bulk payments details pdf ", e).log();
		}

		BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);

		List<CurrencyListDTO> currDTO = bulkWireFileDelegate.getCurrencyList();

		List<CurrencyListDTO> currency = currDTO.stream()
				.filter(c -> (c.getCode()).equals(bulkRecord.getCurrency())).collect(Collectors.toList());

		boolean isEuropianGeography = com.kony.dbputilities.util.HelperMethods.isEuropieanGeography();
		String totalAmount="";
		NumberFormat nf;
		if(isEuropianGeography)
			nf = NumberFormat.getInstance(new Locale("da", "DK"));
		else
			nf = NumberFormat.getInstance(new Locale("en", "US"));
		nf.setMinimumFractionDigits(2);
		totalAmount = nf.format(bulkRecord.getTotalAmount());
		//String totalAmountWithCurrency = currency.get(0).getSymbol() + totalAmount ;

		bankTable.addCell(_noBorderCell("Batch Amount:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(String.valueOf(bulkRecord.getTotalAmount()))) {
			bankTable.addCell(_noBorderCell(totalAmount, TRANS_TABLE_VALUE_FONT_COLOR));
		} else {
			bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}

		bankTable.addCell(_noBorderCell("Number of Transactions: ", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(String.valueOf(bulkRecord.getTotalTransactions()))) {
			bankTable.addCell(_noBorderCell(String.valueOf( String.format("%.0f", bulkRecord.getTotalTransactions())), TRANS_TABLE_VALUE_FONT_COLOR));
		} else {
			bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));

		}

		bankTable.addCell(_noBorderCell("Bulk Payment ID:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(bulkRecord.getRecordId())) {
			bankTable.addCell(_noBorderCell(bulkRecord.getRecordId(), TRANS_TABLE_VALUE_FONT_COLOR));
		} else{
			bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}

		bankTable.addCell(_noBorderCell("Status:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(bulkRecord.getStatus())) {
			String translatedValue = getTranslatedStatus(bulkRecord.getStatus());
			bankTable.addCell(_noBorderCell(translatedValue, TRANS_TABLE_VALUE_FONT_COLOR));
		} else {
			bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}

		bankTable.addCell(_noBorderCell("Processing Mode:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(bulkRecord.getBatchMode())) {
			bankTable.addCell(_noBorderCell(bulkRecord.getBatchMode(), TRANS_TABLE_VALUE_FONT_COLOR));
		} else {
			bankTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}

		bankTable.addCell(_noBorderCell("Error Description:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(!bulkRecord.getBulkErrorDetails().isEmpty()) {
			for(int i=0; i<bulkRecord.getBulkErrorDetails().size(); i++) {
				bankTable.addCell(_noBorderCell(bulkRecord.getBulkErrorDetails().get(0).getErrorDescription(), TRANS_TABLE_VALUE_FONT_COLOR));
			}
		} else {
			bankTable.addCell(_noBorderCell("-", TRANS_TABLE_VALUE_FONT_COLOR));
		}
		PdfPCell blankCell2= new PdfPCell(new Paragraph(""));
		blankCell2.setColspan(2);
		blankCell2.setPaddingTop(6);
		blankCell2.setPaddingBottom(6);
        blankCell2.setBorderColor(Color.WHITE);
		bankTable.addCell(blankCell2);
     	return bankTable;
	}
	
	private String getTranslatedStatus(String status) {
		
		if((StringUtils.equalsIgnoreCase(status, "Discarded")) 
				||(StringUtils.equalsIgnoreCase(status, "Cancelled"))
				||(StringUtils.equalsIgnoreCase(status, "CANCELWAREHOUSE")))
			return "Cancelled" ;
		else if ((StringUtils.equalsIgnoreCase(status, "waitexec")) 
				||(StringUtils.equalsIgnoreCase(status, "waitack"))
				||(StringUtils.equalsIgnoreCase(status, "Processed")))
			return "Processing Payments";
		else if (StringUtils.equalsIgnoreCase(status, "CREATED")) 
			return "Ready for Review";		
		else if (StringUtils.equalsIgnoreCase(status, "Rejected")) 
			return "Rejected by Approver" ;
		else if ((StringUtils.equalsIgnoreCase(status, "READY"))
				||(StringUtils.equalsIgnoreCase(status, "Pending")))
			return "Pending for Approval" ;
		else if (StringUtils.equalsIgnoreCase(status, "WAREHOUSED")) 
			return "Scheduled";
		else if (StringUtils.equalsIgnoreCase(status, "Completed")) 
			return "Completed" ;		
				
		return status;
	}

	public static PdfPCell _noBorderCell(String text, Color fontColor) {
		Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.NORMAL, fontColor));
		PdfPCell cell = new PdfPCell();
		cell.addElement(cellText);
		cell.setBorderColor(Color.WHITE);
		cell.setPaddingTop(6);
		cell.setPaddingBottom(6);
		return cell;
	}
	public static PdfPCell _noBorderCell1(String text, Color fontColor) {
		Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.NORMAL, fontColor));
		PdfPCell cell = new PdfPCell();
		cell.addElement(cellText);
		cell.setBorder(Rectangle.TOP);
		cell.setBorderColor(Color.BLACK);
		cell.setPaddingTop(6);
		cell.setPaddingBottom(6);
		return cell;
	}
	private PdfPCell _setBorder(PdfPCell cell){
		cell.setPaddingLeft(10f);
		cell.setPaddingTop(7f);
		cell.setPaddingBottom(7f);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderColor(Color.BLACK);
		cell.setPaddingLeft(10f);

		return cell;
	}

	private PdfPCell _blanCellDesign(PdfPCell blankCell){
		blankCell.setPaddingTop(6);
		blankCell.setPaddingBottom(6);
		blankCell.setBorder(Rectangle.TOP);
		return blankCell;
	}

	private PdfPTable _createPaymentOrders(PdfPTable table, List<BulkPaymentPODTO> paymentOrders, String status) {

		PdfPTable historyTable = new PdfPTable(3);
		historyTable.setSpacingBefore(20);
		historyTable.setWidthPercentage(40);


		PdfPCell addrCell = new PdfPCell(new Paragraph("Payment Details"));
		addrCell.setColspan(3);
		_setBorder(addrCell);
		historyTable.addCell(addrCell);
		historyTable.completeRow();


		PdfPCell blankCell= new PdfPCell(new Paragraph(""));
		blankCell.setColspan(3);
		_blanCellDesign(blankCell);
		historyTable.addCell(blankCell);
		historyTable.completeRow();

		PdfPCell headingCell1 = new PdfPCell(new Paragraph("Beneficiary"));
		PdfPCell headingCell2 = new PdfPCell(new Paragraph("Amount"));
		PdfPCell headingCell3 = new PdfPCell(new Paragraph("Status"));

		headingCell1.setBorder(Rectangle.TOP);
		headingCell1.setBorderColor(Color.BLACK);

		headingCell2.setBorder(Rectangle.TOP);
		headingCell2.setBorderColor(Color.BLACK);

		headingCell3.setBorder(Rectangle.TOP);
		headingCell3.setBorderColor(Color.BLACK);

		historyTable.addCell(headingCell1);
		historyTable.addCell(headingCell2);
		historyTable.addCell(headingCell3);
		historyTable.completeRow();


		BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);

		List<CurrencyListDTO> currDTO = bulkWireFileDelegate.getCurrencyList();

		boolean isEuropianGeography = com.kony.dbputilities.util.HelperMethods.isEuropieanGeography();
		String totalAmount="";
		NumberFormat nf;
		if(isEuropianGeography)
			nf= NumberFormat.getInstance(new Locale("da", "DK"));
		else
			nf = NumberFormat.getInstance(new Locale("en", "US"));

		nf.setMinimumFractionDigits(2);
		for(BulkPaymentPODTO po : paymentOrders) {
			if((StringUtils.equalsIgnoreCase(po.getStatus(), "CANCELLED")) && (StringUtils.equalsIgnoreCase(status, "COMPLETED")))
			{

			}else {
				if(StringUtils.isNotBlank(po.getRecipientName())) {
					historyTable.addCell(_noBorderCell(po.getRecipientName(), TRANS_TABLE_VALUE_FONT_COLOR));
				} else {
					historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
				}

				List<CurrencyListDTO> currency = currDTO.stream().filter(c -> (c.getCode()).equals(po.getCurrency())).collect(Collectors.toList());
				if(StringUtils.isNotBlank(po.getCurrency()) && StringUtils.isNotBlank(Double.toString(po.getAmount())) && currency.size() > 0) {
					totalAmount = nf.format(po.getAmount());
					String amountWithCurrency = currency.get(0).getSymbol() + totalAmount ;
					historyTable.addCell(_noBorderCell(amountWithCurrency, TRANS_TABLE_VALUE_FONT_COLOR));
				} else {
					historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
				}
				if(StringUtils.isNotBlank(status)) {
					if(StringUtils.equalsIgnoreCase(status, "COMPLETED"))
					{
						if(StringUtils.isNotBlank(po.getPaymentStatus())) {
							historyTable.addCell(_noBorderCell(po.getPaymentStatus(), TRANS_TABLE_VALUE_FONT_COLOR));
						} else {
							historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
						}
					}
					else {
						if(StringUtils.isNotBlank(po.getStatus())) {
							historyTable.addCell(_noBorderCell(po.getStatus(), TRANS_TABLE_VALUE_FONT_COLOR));
						} else {
							historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
						}
					}
				} else {
					historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
				}
				historyTable.completeRow();

			}

		}
		return historyTable;

	}
	private PdfPTable _createApprovalHistoryInfo(PdfPTable table, List<RequestHistoryDTO> requestDto, boolean isGroupMatrix,BulkPaymentRecordDTO bulkRecord) {

		PdfPTable historyTable;
		PdfPCell addrCell = new PdfPCell(new Paragraph("Approval History Information"));
		PdfPCell blankCell= new PdfPCell(new Paragraph(""));

		if (!isGroupMatrix) {
			historyTable = new PdfPTable(4);
			addrCell.setColspan(4);
		  blankCell.setColspan(4);
		}
		else {
			historyTable = new PdfPTable(5);
			addrCell.setColspan(5);
		    blankCell.setColspan(5);
		}
		_setBorder(addrCell);
		historyTable.setSpacingBefore(20);
		historyTable.setWidthPercentage(100);
		historyTable.addCell(addrCell);
		historyTable.completeRow();


		blankCell.setPaddingTop(3);
		blankCell.setPaddingBottom(3);
		blankCell.setBorder(Rectangle.TOP);
		historyTable.addCell(blankCell);
		historyTable.completeRow();

		historyTable.addCell(_noBorderCell("Approval Status:", TRANS_TABLE_FIELD_FONT_COLOR));
		if(StringUtils.isNotBlank(bulkRecord.getStatus())) {
			historyTable.addCell(_noBorderCell(getTranslatedStatus(bulkRecord.getStatus()), TRANS_TABLE_VALUE_FONT_COLOR));
		} else{
			historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
		}

		//Adding blank Spaces in the cell according to number of columns
		if(!isGroupMatrix) {
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
		}
		else{
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));

		}

		historyTable.completeRow();

		String count=String.valueOf(requestDto.stream().filter(c->c.getStatus().equalsIgnoreCase("Approved")).count());

		historyTable.addCell(_noBorderCell("Approved:", TRANS_TABLE_FIELD_FONT_COLOR));
		historyTable.addCell(_noBorderCell(count, TRANS_TABLE_VALUE_FONT_COLOR));

		if(!isGroupMatrix) {
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
		}
		else{
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));
			historyTable.addCell(_noBorderCell(" ", TRANS_TABLE_VALUE_FONT_COLOR));

		}
		//historyTable.addCell(blankCell);
		historyTable.completeRow();

		blankCell.setPaddingTop(1);
		blankCell.setPaddingBottom(1);
		historyTable.addCell(blankCell);
		historyTable.completeRow();


		PdfPCell headingCell1 = new PdfPCell(new Paragraph("User"));
		historyTable.addCell(headingCell1);

		if (isGroupMatrix) {
			PdfPCell headingCell2 = new PdfPCell(new Paragraph("Signatory Group"));
			historyTable.addCell(headingCell2);
		}

		PdfPCell headingCell3 = new PdfPCell(new Paragraph("Status"));
		historyTable.addCell(headingCell3);
		PdfPCell headingCell4 = new PdfPCell(new Paragraph("Date & Time"));
		historyTable.addCell(headingCell4);
		PdfPCell headingCell5 = new PdfPCell(new Paragraph("Comment"));
		historyTable.addCell(headingCell5);
		historyTable.completeRow();

		for(RequestHistoryDTO request :requestDto) {
			if(StringUtils.isNotBlank(request.getCustomerName())) {
				historyTable.addCell(_noBorderCell(request.getCustomerName(), TRANS_TABLE_VALUE_FONT_COLOR));

			} else {
				historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));
			}
			if (isGroupMatrix) {
				if (StringUtils.isNotBlank(request.getGroupName())) {
					historyTable.addCell(_noBorderCell(request.getGroupName(), TRANS_TABLE_VALUE_FONT_COLOR));

				} else {
					historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));

				}
			}
			if (StringUtils.isNotBlank(request.getAction())) {
				if (request.getAction().equalsIgnoreCase("pending"))
					historyTable.addCell(_noBorderCell("Created Request", TRANS_TABLE_VALUE_FONT_COLOR));
				else if (request.getAction().equalsIgnoreCase("approved"))
					historyTable.addCell(_noBorderCell("Approved Request", TRANS_TABLE_VALUE_FONT_COLOR));

				else
					historyTable.addCell(_noBorderCell(request.getAction(), TRANS_TABLE_VALUE_FONT_COLOR));

			} else {
				historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));

			}

			try {
				if(StringUtils.isNotBlank(request.getActionts())) {
					String actionts = HelperMethods.convertDateFormat(request.getActionts(), Constants.BULK_DATEFORMAT);
					historyTable.addCell(_noBorderCell(actionts, TRANS_TABLE_VALUE_FONT_COLOR));

				} else {
					historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));

				}
			} catch (Exception e) {
				alert.prepareError("Error occured while creating bulk payments details pdf ", e).log();
			}

			if(StringUtils.isNotBlank(request.getComments())) {
				historyTable.addCell(_noBorderCell(request.getComments(), TRANS_TABLE_VALUE_FONT_COLOR));

			} else {
				historyTable.addCell(_noBorderCell("N/A", TRANS_TABLE_VALUE_FONT_COLOR));

			}
			historyTable.completeRow();
		}

		return historyTable;
	}

}
