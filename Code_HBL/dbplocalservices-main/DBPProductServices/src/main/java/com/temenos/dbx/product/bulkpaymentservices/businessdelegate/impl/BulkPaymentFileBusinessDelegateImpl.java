package com.temenos.dbx.product.bulkpaymentservices.businessdelegate.impl;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.ObjectId;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentFileBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentFileBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;

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
				Document doc = dBuilder.parse(file);
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
				Document doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				XPath xPath =  XPathFactory.newInstance().newXPath();
				
				NodeList paymentInfoList = (NodeList) xPath.evaluate(PAYMENT_INFO_EXPR,doc, XPathConstants.NODESET);
				if(paymentInfoList.getLength() > 1) {
					bulkpaymentrecordDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21251);
					return bulkpaymentrecordDTO;
				}
				bulkpaymentrecordDTO.setPaymentDate(xPath.evaluate(PAYMENT_DATE_EXPR,doc));	
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
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.RBOBJECTS)
					.withObjectId(ObjectId.TRANSACTIONS).withOperationId(OperationName.GETBANKDATE)
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
	public byte[] getRecordPDFAsBytes(String recordId, String requestId, String customerId,
									  DataControllerRequest request) {
		return new byte[0];
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
	public List<BulkPaymentFileDTO> fetchBulkPaymentUploadedFilesfromBackend(String fromDate, String toDate, DataControllerRequest request) {
		List<BulkPaymentFileDTO> uploadedFiles;
		List<BulkPaymentFileDTO> fetchedFiles = bulkPaymentFileBackendDelegate.fetchBulkPaymentUploadedFiles(fromDate, toDate, request); 

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

}
