package com.bct.utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.dbx.util.HikariConfiguration;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.bct.custom.constants.HBLCrossBorderConstants;

public class HBLCommonUtility {
	private static final Logger logger = LogManager.getLogger(HBLCommonUtility.class);
	private static JSONObject customerDetails;

	public static Result batchInsert(String tableName, String colSpec, String[] insertRecordsArray,
			DataControllerRequest request) {
		HikariConfiguration.getDataSource(request);
		int count = 0;
		logger.debug("BCT::HBLCommonUtility:batchInsert: tableName =>:" + tableName);
		Connection connection = HikariConfiguration.getconnection();
		Statement stmt = null;
		int[] result = {};
		int batchSize = 0;
		Result response = new Result();
		try {
			batchSize = insertRecordsArray.length;
		} catch (NumberFormatException nfe) {
			logger.info("Batch Size for insert not provided in the request. Using the defaul value:" + batchSize);
		}
		logger.debug("BCT::HBLCommonUtility:batchInsert: batchSize:" + batchSize);
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			for (int i = 0; i < insertRecordsArray.length; i++) {

				String query = "insert into " + tableName + " " + colSpec + " values " + insertRecordsArray[i];
				String sqlToInsert = "insert into " + tableName;
				sqlToInsert += colSpec != null ? colSpec : "";
				sqlToInsert += " values " + insertRecordsArray[i];
				stmt.addBatch(sqlToInsert);

				count++;

				if (count % batchSize == 0) {
					result = stmt.executeBatch();
					if (batchSize != result.length) { // Something went wrong.
						logger.error("Unexpected error while inserting records");
					}
					connection.commit(); // All well, commit
					logger.info("Succesfully committed batch insert");
					response.setParam(new Param("success", "true"));
				}
			}

			if (count % batchSize != 0) { // Commit any leftovers
				result = stmt.executeBatch();
				if ((count % batchSize) != result.length) { // Something went wrong.
					logger.error("Unexpected error while inserting records");
				}
				connection.commit(); // All well, commit
				logger.info("Succesfully committed batch insert");
				response.setParam(new Param("success", "true"));
			}
		} catch (Exception e) {
			logger.error("Exception while executing batch insert:", e);
			try {
				connection.rollback();
				response.setParam(new Param("success", "false"));
				response.addParam(new Param("dbpErrMsg", e.getMessage().toString()));
				response.addParam(new Param("dbpErrCode", e.getLocalizedMessage().toString()));
			} catch (SQLException e1) {

				logger.error(e1);
			}
		} finally {
			HikariConfiguration.close(stmt);
			HikariConfiguration.close(connection);
		}
		return response;
	}

	public static Result batchUpdate(String tableName, String colSpec, Map<String, Object> inputParams,
			String[] insertRecordsArray, DataControllerRequest request) {
		HikariConfiguration.getDataSource(request);
		int count = 0;
		logger.debug("BCT::HBLCommonUtility:batchUpdate: tableName =>:" + tableName);
		Connection connection = HikariConfiguration.getconnection();
		Statement stmt = null;
		int[] result = {};
		int batchSize = 0;
		Result response = new Result();
		try {
			batchSize = insertRecordsArray.length;
		} catch (NumberFormatException nfe) {
			logger.info("Batch Size for insert not provided in the request. Using the defaul value:" + batchSize);
		}
		logger.debug("BCT::HBLCommonUtility:batchInsert: batchSize:" + batchSize);
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			HelperMethods.removeNullValues(inputParams);
			List<String> keys = new ArrayList<>(inputParams.keySet());
			String sqlToInsert = "UPDATE " + tableName + "SET ";
			for (int i = 0; i < keys.size(); i++) {
				sqlToInsert += keys.get(i) + "= '" + inputParams.get(keys.get(i)) + "',";
				sqlToInsert += " WHERE id = '" + inputParams.get("id");
				stmt.addBatch(sqlToInsert);

				count++;

				if (count % batchSize == 0) {
					result = stmt.executeBatch();
					if (batchSize != result.length) { // Something went wrong.
						logger.error("Unexpected error while inserting records");
					}
					connection.commit(); // All well, commit
					logger.info("Succesfully committed batch insert");
					response.setParam(new Param("success", "true"));
				}
			}

			if (count % batchSize != 0) { // Commit any leftovers
				result = stmt.executeBatch();
				if ((count % batchSize) != result.length) { // Something went wrong.
					logger.error("Unexpected error while inserting records");
				}
				connection.commit(); // All well, commit
				logger.info("Succesfully committed batch insert");
				response.setParam(new Param("success", "true"));
			}
		} catch (Exception e) {
			logger.error("Exception while executing batch insert:", e);
			try {
				connection.rollback();
				response.setParam(new Param("success", "false"));
				response.addParam(new Param("dbpErrMsg", e.getMessage().toString()));
				response.addParam(new Param("dbpErrCode", e.getLocalizedMessage().toString()));
			} catch (SQLException e1) {

				logger.error(e1);
			}
		} finally {
			HikariConfiguration.close(stmt);
			HikariConfiguration.close(connection);
		}
		return response;
	}

	public static boolean triggerEmail(DataControllerRequest dcRequest, JSONObject customerInfo) {
		boolean isMailSent = false;
		try {
			String email = Utils.getCustomerEmialfromCore(dcRequest, customerInfo.getString("id"));
			logger.debug("triggerEmail value:" + email);
			String userName = dcRequest.getParameter("userName");
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			String customerName = customerInfo.has("FullName") ? customerInfo.getString("FullName") : "";
			if (StringUtils.isBlank(customerName)) {
				input.put("FirstName", customerInfo.getString("FirstName"));
				input.put("LastName", customerInfo.getString("LastName"));
			} else if (StringUtils.isNotBlank(customerName)) {
				input.put("FirstName", customerName);
			}
			input.put("EmailType", customerInfo.getString("emailTemplate"));
			// String
			// activationLink=EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL");
			JSONObject addContext = new JSONObject();
			addContext.put("playstoreLink", customerInfo.getString("playstoreLink"));
			addContext.put("appstoreLink", customerInfo.getString("appstoreLink"));
			addContext.put("deviceId", customerInfo.getString("deviceId"));
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

			HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
			isMailSent = true;
		} catch (Exception e) {
			logger.error("Failed to send an email:" + e.getLocalizedMessage());
			isMailSent = false;
		}
		return isMailSent;

	}

	public static Boolean checkCodeAvailableInDatabase(String code, String tableName, DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		JSONArray types = new JSONArray();
		Boolean isCodeAvailable = null;
		String key = "";
		try {
			String filter = "code eq '" + code + "'";
			inputmap.put(HBLURLConstants.FILTER, filter);
			key = tableName;
			if (tableName.equalsIgnoreCase("merchantcategory")) {
				tableName = HBLURLConstants.GET_ALL_MERCHANT_CATEGORIES_OPERATION;
			} else if (tableName.equalsIgnoreCase("merchantdetails")) {
				tableName = HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION;
			}
			logger.debug("BCT::HBLCommonUtility: checkCodeAvailableInDatabase payload:" + inputmap);
			String response = DBPServiceExecutorBuilder.builder().withOperationId(tableName)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::HBLCommonUtility: checkCodeAvailableInDatabase response:" + response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray(key);
			if (types.length() > 0) {
				isCodeAvailable = true;
			} else {
				isCodeAvailable = false;
			}
		} catch (Exception e) {
			logger.error("Exception occured while fetching:" + tableName + ":" + e.toString());
			throw new ApplicationException(ErrorCodeEnum.ERR_10021,
					"Failed to check data availability in " + tableName);

		}
		return isCodeAvailable;
	}

	public static String getConsentCreateReqBody(String consent, String vpaId, String fullName,String documentType, String documentNumber, String nationality, String countryCode,
			String direction) {
		
		logger.debug("documentType:##"+ documentType);
		logger.debug("documentNumber:##"+ documentNumber);
		logger.debug("nationality:##"+ nationality);
		logger.debug("countryCode:##"+ countryCode);
		logger.debug("direction:##"+ direction);
		documentType = StringUtils.isNotBlank(documentType)?documentType:"";
		documentNumber = StringUtils.isNotBlank(documentNumber)?documentNumber:"";
		nationality = StringUtils.isNotBlank(nationality)?nationality:"";
		countryCode = StringUtils.isNotBlank(countryCode)?countryCode:"";
		
		JsonObject obj = new JsonObject();
		obj.addProperty("issuedDate", HelperMethods.getCurrentTimeStamp());
		obj.addProperty("bankCode", HBLCrossBorderConstants.BANK_CODE);
		obj.addProperty("documentType", documentType);
		obj.addProperty("documentNumber", documentNumber);
		obj.addProperty("fullName", fullName);
		obj.addProperty("instrument", HBLCrossBorderConstants.CROSSBORDER_INSTRUMENT);
		obj.addProperty("consent", consent);
		obj.addProperty("vpaId", vpaId);
		obj.addProperty("nationality", nationality);//NP
		obj.addProperty("countryCode", countryCode);//356
		//obj.addProperty("uniqueTransactingId", "15482559");
		obj.addProperty("uniqueTransactingId", Long.toString(sixteenDigitRandomNumbr()));
		obj.addProperty("issuedPlace", "BHAKTAPUR");
		obj.addProperty("participantCode", HBLCrossBorderConstants.HBL_PARTICIPATION_CODE);
		obj.addProperty("participantService", HBLCrossBorderConstants.CROSSBORDER_PARTICIPANT_CREATE_SERVICE);
		obj.addProperty("direction", direction);

		System.out.println("JSON Object:" + obj.toString());
		return obj.toString();
	}

	public static String getUpdateConsentReqBody(String consent,String vpaId, String fullName,String documentType, String documentNumber, String nationality, String countryCode,
			String direction) {
		JsonObject obj = new JsonObject();
		logger.debug("Current time:##"+ HelperMethods.getCurrentTimeStamp());
		logger.debug("documentType:##"+ documentType);
		logger.debug("documentNumber:##"+ documentNumber);
		logger.debug("nationality:##"+ nationality);
		logger.debug("countryCode:##"+ countryCode);
		logger.debug("direction:##"+ direction);
		documentType = StringUtils.isNotBlank(documentType)?documentType:"";
		documentNumber = StringUtils.isNotBlank(documentNumber)?documentNumber:"";
		nationality = StringUtils.isNotBlank(nationality)?nationality:"";
		countryCode = StringUtils.isNotBlank(countryCode)?countryCode:"";
			
		//obj.addProperty("issuedDate", "1/7/2009 12:00:00 AM");
		obj.addProperty("issuedDate", HelperMethods.getCurrentTimeStamp());
		obj.addProperty("bankCode", HBLCrossBorderConstants.BANK_CODE);
		obj.addProperty("documentType", documentType);
		obj.addProperty("documentNumber", documentNumber);
		obj.addProperty("fullName", fullName);
		obj.addProperty("instrument", HBLCrossBorderConstants.CROSSBORDER_INSTRUMENT);
		obj.addProperty("consent", consent);
		obj.addProperty("vpaId", vpaId);
		obj.addProperty("nationality", nationality);//NP
		obj.addProperty("countryCode", countryCode);//356
		//obj.addProperty("uniqueTransactingId", "01908433710016");
		obj.addProperty("uniqueTransactingId", Long.toString(sixteenDigitRandomNumbr()));
		obj.addProperty("issuedPlace", "BHAKTAPUR");
		obj.addProperty("participantCode", HBLCrossBorderConstants.HBL_PARTICIPATION_CODE);
		obj.addProperty("participantService", HBLCrossBorderConstants.CROSSBORDER_PARTICIPANT_UPDATE_SERVICE);
		obj.addProperty("direction", direction);
		return obj.toString();
	}
	
	public static String getCrossBorderPaymentBody(String orgRequestUniqueId, String endToEndTxnId, String amount) {
		JsonObject obj = new JsonObject();
		obj.addProperty("participantCode", HBLCrossBorderConstants.HBL_PARTICIPATION_CODE);
		obj.addProperty("participantService", HBLCrossBorderConstants.CROSSBORDER_PARTICIPANT_PAYMENT_SERVICE);
		obj.addProperty("orgRequestUniqueId", orgRequestUniqueId);
		obj.addProperty("endToEndTxnId", endToEndTxnId);
		obj.addProperty("amount", amount);
		
		return obj.toString();
	}

	public static String getPurposeRelationshipReqBody(String country) {
		JsonObject obj = new JsonObject();
		obj.addProperty("countryCode", country);
		return obj.toString();
	}

	public static String getLimitReqBody(String amount, String vpa) {
		JsonObject obj = new JsonObject();
		//obj.addProperty("amount", "200.00");
		obj.addProperty("amount", amount);
		//obj.addProperty("vpa", "86085045802");
		obj.addProperty("vpa", vpa);
		obj.addProperty("service", HBLCrossBorderConstants.CROSSBORDER_LIMIT_SERVICE);
		return obj.toString();
	}

	public static String getCustomerValidateReqPayload(String channel, String plat, String amount, String purpose, String countryCode, String chargeAmount,
			String remarks,String payeeName , String payeeRelationShip, String payeeVPA, String payerName, String payerVPA,
			String payerAccType, String payerAccNumber, String currency, String latitude, String longitude, String documentType, String documentNumber) {
		JsonObject obj = new JsonObject();
		
		logger.debug("documentType:##"+ documentType);
		logger.debug("documentNumber:##"+ documentNumber);
		//logger.debug("branchCode:##"+ branchCode);
		documentType = StringUtils.isNotBlank(documentType)?documentType:"";
		documentNumber = StringUtils.isNotBlank(documentNumber)?documentNumber:"";
		//branchCode = StringUtils.isNotBlank(branchCode)?branchCode:"";

		JsonObject payeeDetail = new JsonObject();
		JsonObject accountDetail = new JsonObject();
		accountDetail.addProperty("vpa", payeeVPA);
		payeeDetail.add("accountDetail", accountDetail);
		payeeDetail.addProperty("accountType", HBLCrossBorderConstants.VALIDATE_CUSTOMER_ACCTYPE);
		payeeDetail.addProperty("name", payeeName);
		payeeDetail.addProperty("type", HBLCrossBorderConstants.VALIDATE_CUSTOMER_TYPE);
		payeeDetail.addProperty("payeeRelationShip", payeeRelationShip);

		JsonObject payerDetail = new JsonObject();

		JsonObject address = new JsonObject();
		address.addProperty("country", "Nepal");
		address.addProperty("city", "BHAKTAPUR Bhaktapur-Municipality 12 GAHITI");
		address.addProperty("geoCode", "27.7151,85.3278");
		address.addProperty("location", "LALITPUR Lalitpur-Metropolitan 25 BHAISEPATI");
		payerDetail.add("address", address);

		JsonObject accountDetail1 = new JsonObject();
		//accountDetail1.addProperty("branchCode", branchCode);
		accountDetail1.addProperty("branchCode", "1");
		accountDetail1.addProperty("bankCode", HBLCrossBorderConstants.BANK_CODE);
		accountDetail1.addProperty("vpa", payerVPA);
		accountDetail1.addProperty("accountType", payerAccType);
		accountDetail1.addProperty("accountNumber", payerAccNumber);
		payerDetail.add("accountDetail", accountDetail1);

		JsonObject deviceInformation = new JsonObject();
		deviceInformation.addProperty("os", plat);
		deviceInformation.addProperty("ip", "192.168.1.111");
		deviceInformation.addProperty("mobile", "9841257125");
		//deviceInformation.addProperty("mobile", channel);
		deviceInformation.addProperty("geoCode", latitude + ","+ longitude);
		deviceInformation.addProperty("location", "LALITPUR Lalitpur-Metropolitan 25 BHAISEPATI");
		deviceInformation.addProperty("teleCom", "NTC");
		payerDetail.add("deviceInformation", deviceInformation);
		payerDetail.addProperty("accountType", HBLCrossBorderConstants.VALIDATE_CUSTOMER_ACCTYPE);
		payerDetail.addProperty("name", payerName);
		payerDetail.addProperty("identificationNumber", documentNumber);
		payerDetail.addProperty("identificationType", documentType);
		payerDetail.addProperty("type", HBLCrossBorderConstants.VALIDATE_CUSTOMER_TYPE);
		obj.add("payerDetail", payerDetail);

		obj.addProperty("amount", amount);
		obj.addProperty("purpose", purpose);
		obj.addProperty("countryCode", countryCode);
		obj.addProperty("chargeAmount", chargeAmount);
		obj.addProperty("currency", currency);
		obj.addProperty("txnType", HBLCrossBorderConstants.VALIDATE_CUSTOMER_TXNTYPE);
		obj.add("payeeDetail", payeeDetail);
		obj.addProperty("participantCode", HBLCrossBorderConstants.HBL_PARTICIPATION_CODE);
		obj.addProperty("participantService", HBLCrossBorderConstants.VALIDATE_CUSTOMER_PARTICIPANT_SERVICE);
		obj.addProperty("remarks", remarks);
		obj.addProperty("requestUniqueId", generateReqUniqueId());

		return obj.toString();
	}

	public static String generateReqUniqueId() {
		String batchId = "HBLNPIXTRANSACT" + getRandomNumberString()
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		logger.debug("batchId:" + batchId);
		return batchId;
	}

	public static String getRandomNumberString() {
		// It will generate 6 digit random Number.
		// from 0 to 999999
		Random rnd = new Random();
		int number = rnd.nextInt(999999);

		// this will convert any number sequence into 6 character.
		return String.format("%06d", number);
	}
	
	public static long sixteenDigitRandomNumbr() {

		long smallest = 1000_0000_0000_0000L;
		long biggest = 9999_9999_9999_9999L;

		long random = ThreadLocalRandom.current().nextLong(smallest, biggest + 1);
		logger.debug("random:" + random);
		return random;
	}

	public static int updateConsentStatus(DataControllerRequest request, String vpaId, String consentStatus, String id)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		logger.debug("consentStatus ###"+ consentStatus);
		logger.debug("vpaId ###"+ vpaId);
		inputParams.put("consentStatus", consentStatus);
		logger.debug("id ###"+ id);
		inputParams.put("vpaId", vpaId);
		inputParams.put("id", id);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customeraccounts_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		logger.debug("Post updateConsentStatus");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			logger.error("Couldn't create entry in dbxDb customeraccounts Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
					return 1;
				else
					return 0;
			} catch (Exception e) {
				logger.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;
	}
	
	
	public static Result updateTransactConsentStatus(Map<String, Object> inputmap,
			Map<String, Object> headers) throws DBPApplicationException {
		
		logger.debug(
				"HBL::updateTransactConsentStatus: inputmap:" + inputmap.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(HBLCrossBorderConstants.UPDATE_VPA_OPERATION).withRequestParameters(inputmap)
				.withServiceId(HBLCrossBorderConstants.VPA_SEVICE_ID).withRequestHeaders(headers).build().getResult();
		logger.debug("HBL::updateTransactConsentStatus: response:"
				+ res.getHttpStatusCodeParamValue());
 
		return res;
	}
	
	
	public static String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {
		String customerid = "";
		try {
			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				logger.debug("Else getThirdpartyAuthFlag:");
			}
			logger.debug("getThirdpartyAuthFlag id:" + customerid);
		} catch (Exception e) {
			logger.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;
	}

	public static String getIdfromCustomerAccTable(String customerID, String vpaId) {
		logger.debug("customerID **:" + customerID);
		logger.debug("vpaID **:" + vpaId);
		JSONArray records = new JSONArray();
		String idValue = "";
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "vpaId" + DBPUtilitiesConstants.EQUAL + vpaId);

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();

			logger.debug("Id Val:" + response);
			JSONObject responseJSON = new JSONObject(response);
			records = responseJSON.getJSONArray("customeraccounts");

			logger.debug("response" + records);
			JSONObject acc = records.getJSONObject(0);
			idValue = acc.getString("id");
			logger.debug("id:::" + idValue);

		} catch (Exception e) {
			logger.error("Exception caught while getIdfromCustomerAccTable", e);
		}
		return idValue;
	}
	
	public static String telecomProviderFinder(String mobileNumber) {
		String msg = "Unknown telecom provider.";
		
		final Map<String, String> prefixToProviderMap = new HashMap<>();

	        prefixToProviderMap.put("980", "Ncell");
	        prefixToProviderMap.put("981", "Ncell");
	        prefixToProviderMap.put("982", "Ncell");
	        prefixToProviderMap.put("970", "Ncell");
	        prefixToProviderMap.put("984", "NTC");
	        prefixToProviderMap.put("985", "NTC");
	        prefixToProviderMap.put("986", "NTC");
	        prefixToProviderMap.put("976", "NTC");
	        prefixToProviderMap.put("974", "NTC");
	        prefixToProviderMap.put("975", "NTC");
	        prefixToProviderMap.put("961", "Smart Telecom");
	        prefixToProviderMap.put("962", "Smart Telecom");
	    
	     
		if (mobileNumber == null || mobileNumber.length() < 10) {
            return "Invalid mobile number.";
        }

        /** Extract the first three digits of the mobile number
         * For compare
         */
		
        String prefix = mobileNumber.substring(0, 3);

        /** Retrieve the Telecom provider based on the prefix */
        String provider = prefixToProviderMap.get(prefix);

        if (provider != null) {
            return provider;
        } else {
            return msg;
        }
		
		
	}
	
	 public static String getCoreBackendId(DataControllerRequest dcreq)  {		
	    	String backendId = null;
			try {
				
				if (dcreq.getServicesManager().getIdentityHandler() != null) {
					Map<String, Object> userAttributesMap = dcreq.getServicesManager().getIdentityHandler().getUserAttributes();               
					String backendIdentifier = (String)userAttributesMap.get("backendIdentifiers");
					if(logger.isDebugEnabled()){
						logger.debug("backendIdentifier is" + backendIdentifier);
					}
					if(StringUtils.isNotBlank(backendIdentifier)) { 				
						backendId = getCoreIDFromJson(backendIdentifier);				
					}
				}
				else
				{
					logger.error("NULL IDENTITYHANDLER");
				}
				
			} catch (Exception e) {
				logger.error(e);	
				
			}
			if(logger.isDebugEnabled()){
				logger.debug("backendId is" + backendId);
			}
			return backendId;

		}
	 
	 protected static String getCoreIDFromJson(String backendIdentifier) {
			String backendId = null;
			JsonObject backendIdentifiersJSON = new JsonParser().parse(backendIdentifier).getAsJsonObject();
			if(backendIdentifiersJSON.entrySet().size() == 1) {
				for ( Entry<String, JsonElement> entry : backendIdentifiersJSON.entrySet()) {
					backendId = getBackendIdFromCoreType(backendIdentifiersJSON, entry.getKey());
				}
				if(logger.isDebugEnabled()){
					logger.debug("backendId is" + backendId);
				}
			}else {
				String coreType = null;
				try {
					coreType = EnvironmentConfigurationsHandler.getServerAppProperty("ALERTS_CORETYPE");
				} catch (Exception e) {
					logger.error("ALERTS_CORETYPE is not available" + e);
					logger.error(e);
				}
				if(coreType == null)
				{
					logger.error("ALERTS_CORETYPE is not available");
				}
				else
				{
					if(StringUtils.isNotEmpty(coreType) && backendIdentifiersJSON.has(coreType)){					
						backendId = getBackendIdFromCoreType(backendIdentifiersJSON,coreType);
					}
					if(logger.isDebugEnabled()){
						logger.debug("backendId is" + backendId);
					}
				}
				
			}
			return backendId;
		}
	 
	 protected static String getBackendIdFromCoreType(JsonObject backendIdentifiersJSON, String key) {
			JsonArray backendTypeObj = backendIdentifiersJSON.get(key).getAsJsonArray();
			String backendId = null;
			if(backendTypeObj.size() > 0) {
				backendId  =	backendTypeObj.get(0).getAsJsonObject().get("BackendId").getAsString();
			}
			if(logger.isDebugEnabled()){
				logger.debug("backendId is" + backendId);
			}
			return backendId;
		}
	 
	 /*** implementation of a new method for calculating top-up fees, developed based on the recommendations provided by Girish Sr.
	  *  This methodology establishes specific slabs corresponding to various top-up amounts, ensuring a structured and fair fee assessment.
	  *  Please note that these values are provisional and will be subject to updates by HBL in due course.
	  */

		public static double getTopupFee(double amount, DataControllerRequest request) {
			ServicesManager sm;
			double topupFee = 0.00;
			try {
				sm = request.getServicesManager();
				ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
				double SLAB1_MAX_VAL = Double
						.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB1_MAX_VAL"));
				double SLAB1_FEE = Double.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB1_FEE"));
				double SLAB2_MIN_VAL = Double
						.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB2_MIN_VAL"));
				double SLAB2_MAX_VAL = Double
						.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB2_MAX_VAL"));
				double SLAB2_FEE = Double.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB2_FEE"));
				double SLAB3_MIN_VAL = Double
						.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB3_MIN_VAL"));
				double SLAB3_MAX_VAL = Double
						.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB3_MAX_VAL"));
				double SLAB3_FEE = Double.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB3_FEE"));
				double SLAB4_MIN_VAL = Double
						.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB4_MIN_VAL"));
				double SLAB4_FEE = Double.parseDouble(paramHelper.getServerProperty("CARD_TOPUP_FEE_SLAB4_FEE"));

				if (amount <= SLAB1_MAX_VAL) {
					topupFee = SLAB1_FEE;
				} else if ((amount > SLAB2_MIN_VAL) && (amount <= SLAB2_MAX_VAL)) {
					topupFee = SLAB2_FEE;
				} else if ((amount > SLAB3_MIN_VAL) && (amount <= SLAB3_MAX_VAL)) {
					topupFee = SLAB3_FEE;
				} else if (amount > SLAB4_MIN_VAL) {
					topupFee = SLAB4_FEE;
				} else {
					return topupFee;
				}
			} catch (AppRegistryException e) {
				e.printStackTrace();
			}
			return topupFee;

		}
		
		public static String createLimitJSON(DataControllerRequest request) {
			JSONObject jsonObject = new JSONObject();
			Map<String, String> strStrMap = new HashMap<String, String>();
			String key1 = request.getParameter("key1");
			String value1 = request.getParameter("value1");

			String key2 = request.getParameter("key2");
			String value2 = request.getParameter("value2");
			
			String key3 = request.getParameter("key3");
			String value3 = request.getParameter("value3");
			
			if(StringUtils.isNotBlank(key1) && StringUtils.isNotBlank(value1)) {
				strStrMap.put(key1, value1);
				logger.debug("Key1:"+ key1 + "value1:"+ value1);
			}
			if(StringUtils.isNotBlank(key2) && StringUtils.isNotBlank(value2)) {
				strStrMap.put(key2, value2);
				logger.debug("Key2:"+ key2 + "value2:"+ value2);
			}

			if(StringUtils.isNotBlank(key3) && StringUtils.isNotBlank(value3)) {
				strStrMap.put(key3, value3);
				logger.debug("Key3:"+ key3 + "value3:"+ value3);
			}
			
			jsonObject.put("limits", strStrMap);

			logger.debug("limit JSON Value"+jsonObject.toString());
			
			return jsonObject.toString();
		}
		
		public static String getProductId(String productId) {
			
			
			switch (productId) {
			case "100 Days HBL Special FD*":
				return "100.SPECIAL	100";
			case "200 Days HBL Special FD":
				return "200.SPECIAL";
			case "Dep 1 Year Up to 5 Years":
				return "DEPOSIT.1TO5YRS";
			case "Dep 5 Years Up to 10 Years":
				return "DEPOSIT.5TO10YRS";
			case "Deposit 3 months":
				return "DEPOSIT.3MONTHS";
			case "Deposit 6 months":
				return "DEPOSIT.6MONTHS";
			case "Deposit 9 months":
				return "DEPOSIT.9MONTHS";
			case "Himal Remit Fixed Deposit":
				return "HIMAL.REMIT.DEP";
			case "Structured Dep (3M to 5 yrs)":
				return "STRUCTURED";
			default:
				return "NOT.A.VALID.PRODUCT";
			//	return "DEPOSIT.6MONTHS";
			}
			
		}
		
		public static  Map<String, String> getCustomerDetails(String customerId, DataControllerRequest request) {
			customerDetails = CustomerCacheUtil.fetchAndCacheCustomerDetails(customerId, request);
			logger.debug("customerDetails from CustomerCacheUtil:::" + customerDetails);
			
			JSONObject userObj = customerDetails.getJSONArray("user").getJSONObject(0);
			
			String mobileNo = "";
			String countryCode = "";
			String email = "";
			String branch = "";
			String customerName = "";
			JSONArray contactNumbers = userObj.optJSONArray("ContactNumbers");
			if (contactNumbers != null && contactNumbers.length() > 0) {
				JSONObject mobileObj = contactNumbers.getJSONObject(0);
				mobileNo = mobileObj.optString("Value");
				
				countryCode = mobileObj.optString("phoneCountryCode");
				if(StringUtils.isNotBlank(countryCode))
					mobileNo = countryCode +"-"+ mobileNo;
			} else {
				JSONArray contactDetails = userObj.optJSONArray("contactDetails");
				if (contactDetails != null && contactDetails.length() > 0) {
					mobileNo = contactDetails.getJSONObject(0).optString("phone");
				}
			}
			
			customerName = userObj.getString("firstName") +" " +userObj.getString("lastName");
			logger.debug("customerName:::" + customerName);
			
			branch = userObj.getString("companyLegalUnit");
			logger.debug("branch:::" + branch);
			
			email = userObj.getString("email");
			logger.debug("email:::" + email);
			
			Map<String, String> response = new HashMap<String, String>();
			response.put("mobile", mobileNo);
			response.put("email", email);
			response.put("branch", branch);
			response.put("customerName", customerName);
			
			
			logger.debug("customer Mobiel Number:::" + mobileNo);
			return response;
			
		}
		
		public static String maskAccountNumber(String data, int fromIndex, int toIndex, String maskWith) {
			if (StringUtils.isNotBlank(data)) {
				StringBuilder maskedPart = new StringBuilder();
				if (StringUtils.isNotBlank(data)) {
					for (int i = fromIndex; i < toIndex; i++)
						maskedPart.append(maskWith);
					return data.replace(data.substring(fromIndex, toIndex), maskedPart.toString());
				}
				return data;
			} else {
				return data;
			}
		}
		
		public static String getBranchEmail(DataControllerRequest request, String branchName) {
			String email = "";
			
			try {
				
				String filter = CommonUtils.buildOdataCondition("emailto", Constants.EQUAL, branchName);
				HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
				HashMap<String, Object> svcParams = new HashMap<String, Object>();

				svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
				Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
						"dbpRbLocalServicesdb", "dbxdb_hblBranchList_get", false);
				Dataset branchDataset =  result.getDatasetById("hblBranchList");
				if (null != branchDataset) {
					email = branchDataset.getRecord(0).getParamValueByName("id");
				} else {
					logger.debug("Else getBranchEmail:");
				}
				logger.debug("getBranchEmail id:" + email);
			} catch (Exception e) {
				logger.error("Error in getHBLBranches" );
			}
		
			return email;
		}
 
}
