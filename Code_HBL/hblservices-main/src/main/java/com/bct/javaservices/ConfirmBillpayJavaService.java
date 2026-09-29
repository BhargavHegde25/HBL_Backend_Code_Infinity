package com.bct.javaservices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.bct.custom.constants.HBLEnums;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.resource.api.BillPayTransactionResource;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmBillpayJavaService implements JavaService2 {
	String debtorName;
	String debtorAccount;
	String bankCode;
	String branchCode;
	String fromAccountCurrency;
	BigInteger id;
	String instructionId;
	String endToEndId;
	String refId;
	String appId;
	BigDecimal amount;
	HashMap cipsBatchDetailsObj = null;
	HashMap cipsTransactionDetailsObj = null;
	HashMap debitInfoObj = null;
	JSONArray errorArray = null;
	// private static final String paymentOperationName = "ConfirmBillPayTest";
	private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS).build();
	private static final String contentType = "application/x-www-form-urlencoded";
	private String AUTH_USERNAME ="";
	private String AUTH_PASSWORD ="";
	private String NPI_PAYMENT_BASE_URL = "";
	private String NPI_PAYMENT_SUFFIX_URL = "";
	private static final String NPI_BILLPAYMENT_SERVICE="NPIBillPayments";
	private static final String NPI_BILLPAYMENT_AUTH_OPEARATION="getAccessToken";
	private static final String KUKL_BILLPAYMENT_SERVICE="KUKL-Payments";
	private static final String KUKL_BILLPAYMENT_OPEARATION="confirmBillPay";
	private static final String NEA_BILLPAYMENT_SERVICE="NEA-Payments";
	private static final String NEA_BILLPAYMENT_OPEARATION="ConfirmBillPaid";
	private String paymentAggregator=null;
	public static LoggerUtil logger = new LoggerUtil(ConfirmBillpayJavaService.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, Object> inputMap = HelperMethods.getInputParamObjectMap(inputArray);
		paymentAggregator = inputMap.get("paymentAggregator") != null
				? inputMap.get("paymentAggregator").toString()
				: "";
		logger.debug("HBL:ConfirmBillpayJavaService: paymentAggregator:" + paymentAggregator);
			if (methodId.equalsIgnoreCase("KUKL-PaymentJavaService")) {
				result = makeKUKLPayment(methodId, inputArray, dcRequest, dcResponse);
			}else if(methodId.equalsIgnoreCase("TopupNepal-PaymentJavaService")) {
				result=makeTopupNepalPayment(methodId, inputArray, dcRequest, dcResponse);
			}
			else if(methodId.equalsIgnoreCase("NEA-PaymentJavaService")) {
				result = makeNEAPayment(methodId, inputArray, dcRequest, dcResponse);
			}
			else if(methodId.equalsIgnoreCase("NCHL-PaymentJavaService")) {
				result = makeNCHLPayment(methodId, inputArray, dcRequest, dcResponse);
			}
		logger.debug("HBL:ConfirmBillpayJavaService:Final result:" + ResultToJSON.convert(result));
		return result;
	}

	public Result makeKUKLPayment(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		
		Result result = new Result();
		Map<String, Object> headermap = new HashMap<String, Object>();
		Map<String, Object> inputMap= HelperMethods.getInputParamObjectMap(inputArray);
		try {
			JSONObject externalApiPayload = new JSONObject();
			externalApiPayload=generateKUKLPaymentPayload(inputMap, dcRequest, dcResponse, result);
			logger.debug("KUKL_Payments:confirmBillPay external api payload:"+externalApiPayload);
			result=processPayment(methodId, inputArray, externalApiPayload, dcRequest, dcResponse);
			}catch (Exception e) {
				JSONObject errorObject = new JSONObject();
				errorObject.put("dbpErrMsg", e.getLocalizedMessage());
				errorObject.put("dbpErrCode", e.toString());
				errorArray = new JSONArray();
				errorArray.put(errorObject);
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);
			}
		return result;

	}
	private JSONObject generateKUKLPaymentPayload(Map<String, Object> inputMap, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, Result result) {
	       logger.debug("HBL:KUKLBillpaymentsPreprocessor:payload:"+inputMap);
		 JSONObject payload = new JSONObject();
		 String connectionNo=inputMap.get("connectionNo") != null ? inputMap.get("connectionNo").toString() : "";
		 String customerNo=inputMap.get("customerNo") != null ? inputMap.get("customerNo").toString() : "";
		 String txnAmount=inputMap.get("transactionAmount") != null ? inputMap.get("transactionAmount").toString() : "";
		 String branchcode=inputMap.get("branchcode") != null ? inputMap.get("branchcode").toString() : "";
		 String module=inputMap.get("module") != null ? inputMap.get("module").toString() : "";
		 payload.put("connectionNo", connectionNo);
		 payload.put("customerNo", customerNo);
		 payload.put("txnAmount", txnAmount);
		 payload.put("branchcode", branchcode);
		 payload.put("module", module);
		 payload.put("paymentAggregator", inputMap.get("paymentAggregator")); 
		 //payload.put("amount", formatAmount(inputMap.get("transactionAmount").toString())); 
		 //payload.put("transactionAmount", formatAmount(inputMap.get("transactionAmount").toString())); 
		return payload;
	}

	public Result makeNEAPayment(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		Map<String, Object> inputMap= HelperMethods.getInputParamObjectMap(inputArray);
		try {
			JSONObject externalApiPayload = new JSONObject();
			externalApiPayload=generateNEAPaymentPayload(inputMap, dcRequest, dcResponse, result);
			logger.debug("NEA_Payments:confirmBillPay payload:"+externalApiPayload);
			result=processPayment(methodId, inputArray, externalApiPayload, dcRequest, dcResponse);
			}catch (Exception e) {
				JSONObject errorObject = new JSONObject();
				errorObject.put("dbpErrMsg", e.getLocalizedMessage());
				errorObject.put("dbpErrCode", e.toString());
				errorArray = new JSONArray();
				errorArray.put(errorObject);
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);
			}
		return result;

	}
	public Result makeTopupNepalPayment(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		Map<String, Object> inputMap= HelperMethods.getInputParamObjectMap(inputArray);
		errorArray = new JSONArray();
		try {
			if (isValidTopupNepalRequest(inputMap)) {
				JSONObject externalApiPayload = new JSONObject();
				externalApiPayload=generateTopupNepalPaymentPayload(inputMap, dcRequest, dcResponse, result);
				logger.debug("TopupNepal_Payments:confirmBillPay payload:"+externalApiPayload);
				result=processPayment(methodId, inputArray, externalApiPayload, dcRequest, dcResponse);
			} else {
				
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("dbpErrCode", errorArray.getJSONObject(0).getString("dbpErrCode")));
				result.addParam(new Param("dbpErrMsg",  errorArray.getJSONObject(0).getString("dbpErrMsg")));

			}
			}catch (Exception e) {
				JSONObject errorObject = new JSONObject();
				errorObject.put("dbpErrMsg", e.getLocalizedMessage());
				errorObject.put("dbpErrCode", e.toString());
				errorArray = new JSONArray();
				errorArray.put(errorObject);
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("dbpErrCode", errorArray.getJSONObject(0).getString("dbpErrCode")));
				result.addParam(new Param("dbpErrMsg",  errorArray.getJSONObject(0).getString("dbpErrMsg")));
			}
		return result;

	}
	public boolean isValidTopupNepalRequest(Map<String, Object> inputMap) {
		boolean isValidFields = true;
		logger.error("HBL:ConfirmBillpayJavaService:isValidTopupNepalRequest: payload:"+inputMap);
		String mobileno=inputMap.get("mobileno") != null ? inputMap.get("mobileno").toString() : "";
		 String serviceProvider=inputMap.get("serviceProvider") != null ? inputMap.get("serviceProvider").toString() : "";
		 String transactionAmount=inputMap.get("transactionAmount") != null ? inputMap.get("transactionAmount").toString() : "";
		 String merchantCode= inputMap.get("billerId") != null ? inputMap.get("billerId").toString() : "";
			try {
				if (inputMap == null) {
					JSONObject errorObject = new JSONObject();
					logger.error("HBL:ConfirmBillpayJavaService:isValidTopupNepalRequest:invalid payload:" + inputMap);
					errorObject.put("dbpErrMsg", HBLConstants.INVALID_INPUT);
					errorObject.put("dbpErrCode", HBLConstants.INVALID_INPUT);
					errorArray.put(errorObject);
					isValidFields = false;
					return isValidFields;
				}
				if (StringUtils.isNotBlank(merchantCode) && merchantCode.equalsIgnoreCase("TOP-UP-NEPAL-MOBILE")) {
					if (StringUtils.isBlank(mobileno)) {
						JSONObject errorObject = new JSONObject();
						logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid mobileno:" + mobileno);
						errorObject.put("dbpErrMsg", HBLConstants.INVALID_MOBILE_NO);
						errorObject.put("dbpErrCode", HBLConstants.INVALID_MOBILE_NO);
						errorArray.put(errorObject);
						isValidFields = false;
					}
					if (StringUtils.isNotBlank(mobileno)) {
						if (mobileno.length() < 10) {
							JSONObject errorObject = new JSONObject();
							errorObject.put("dbpErrMsg", HBLConstants.INVALID_MOBILE_NO);
							errorObject.put("dbpErrCode", HBLConstants.INVALID_MOBILE_NO);
							errorArray.put(errorObject);
							isValidFields = false;
						}

					}
					if (StringUtils.isBlank(transactionAmount)) {
						JSONObject errorObject = new JSONObject();
						logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid amount:" + amount);
						errorObject.put("dbpErrMsg", HBLConstants.INVALID_AMOUNT);
						errorObject.put("dbpErrCode", HBLConstants.INVALID_AMOUNT);
						errorArray.put(errorObject);
						isValidFields = false;
					}
					if (StringUtils.isNotBlank(transactionAmount)) {
						Double amount = new Double(transactionAmount);
						if (StringUtils.isNotBlank(serviceProvider)) {
							if (serviceProvider.equalsIgnoreCase("NCell")) {
								if (amount < 50 || amount > 5000) {
									logger.error(
											"HBL:ConfirmBillpayJavaService:validatePayload:invalid amount:" + amount);
									JSONObject errorObject = new JSONObject();
									errorObject.put("dbpErrMsg", HBLConstants.INVALID_BETWEEN_AMOUNT);
									errorObject.put("dbpErrCode", HBLConstants.INVALID_BETWEEN_AMOUNT);
									errorArray.put(errorObject);
									isValidFields = false;
								}

							} else if (amount < 10) {
								JSONObject errorObject = new JSONObject();
								errorObject.put("dbpErrMsg", HBLConstants.INVALID_LESS_THEN_AMOUNT);
								errorObject.put("dbpErrCode", HBLConstants.INVALID_LESS_THEN_AMOUNT);
								errorArray.put(errorObject);
								isValidFields = false;
							}

						} else {
							JSONObject errorObject = new JSONObject();
							logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid serviceProvider:"
									+ serviceProvider);
							errorObject.put("dbpErrMsg", HBLConstants.INVALID_SERVICE_PROVIDER);
							errorObject.put("dbpErrCode", HBLConstants.INVALID_SERVICE_PROVIDER);
							errorArray.put(errorObject);
							isValidFields = false;
						}
					}
				}
			} catch (Exception e) {
				JSONObject errorObject = new JSONObject();
				errorObject.put("dbpErrMsg", e.getLocalizedMessage());
				errorObject.put("dbpErrCode", HBLConstants.INVALID_INPUT);
				errorArray.put(errorObject);
				isValidFields = false;
			}
		logger.debug("HBL:ConfirmBillpayJavaService:validatePayload:isValidFields:" + isValidFields);
		return isValidFields;
	}

	public Result makeNCHLPayment(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		NPI_PAYMENT_BASE_URL = EnvironmentConfigurationsHandler.getServerProperty("NPI_PAYMENT_BASE_URL");
		NPI_PAYMENT_SUFFIX_URL = EnvironmentConfigurationsHandler.getServerProperty("NPI_PAYMENT_SUFFIX_URL");
		Map<String, Object> inputMap= HelperMethods.getInputParamObjectMap(inputArray);
		try {
			String transactionDetailsString = inputMap.get("transactionDetails").toString();
			//transactionDetailsString = StringEscapeUtils.unescapeJava(transactionDetailsString);
			JSONArray transactionDetailsArray = new JSONArray(transactionDetailsString);
			JSONObject transObj = transactionDetailsArray.getJSONObject(0);
			logger.debug("HBL:ConfirmBillpayJavaService:transObj :" + transObj);
			JSONObject cipsTransactionDetail = null;
			JSONObject cipsBatchDetail = null;
			if (transObj.has("cipsTransactionDetail")) {
				cipsTransactionDetail = transObj.getJSONObject("cipsTransactionDetail");
				id = cipsTransactionDetail.has("id") ? cipsTransactionDetail.getBigInteger("id") : null;
				instructionId = cipsTransactionDetail.has("instructionId")? cipsTransactionDetail.getString("instructionId"): "";
				endToEndId = cipsTransactionDetail.has("endToEndId") ? cipsTransactionDetail.getString("endToEndId"): "";
				appId = cipsTransactionDetail.has("appId") ? cipsTransactionDetail.getString("appId") : "";
				refId = cipsTransactionDetail.has("refId") ? cipsTransactionDetail.getString("refId") : "";
				amount = cipsTransactionDetail.has("amount") ? cipsTransactionDetail.getBigDecimal("amount") : null;
			}
			if (transObj.has("cipsBatchDetail")) {
				cipsBatchDetail = transObj.getJSONObject("cipsBatchDetail");
				removeEmptyAndNullFields(cipsBatchDetail);
				if (transObj.has("debitInformation")) {
					JSONObject debitInformation = transObj.getJSONObject("debitInformation");
					cipsBatchDetail.put("debtorAccount", debitInformation.get("debtorAccount"));
					cipsBatchDetail.put("debtorBranch", debitInformation.get("debtorBranch"));
					cipsBatchDetail.put("debtorAgent", debitInformation.get("debtorAgent"));
					cipsBatchDetail.put("debtorName", debitInformation.get("debtorName"));
					// cipsBatchDetail.put("categoryPurpose", HBLConstants.CIPS_CATEGORY_PURPOSE);
				}
			}
			logger.debug("HBL:ConfirmBillpayJavaService:cipsBatchDetail :" + cipsBatchDetail);
			JSONObject requestBody = new JSONObject();
			requestBody.put("cipsTransactionDetail", cipsTransactionDetail);
			requestBody.put("cipsBatchDetail", cipsBatchDetail);
			if (isValidNCHLPayload(requestBody)) {
				String token = generateTokenString(requestBody);
				requestBody.put("token", token);
				result=processPayment(methodId, inputArray, requestBody, dcRequest, dcResponse);
			} else {
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);

			}
		} catch (Exception e) {
			logger.error("Exception occured in ConfirmBillpayJavaService:" + e.getLocalizedMessage());
			JSONObject errorObject = new JSONObject();
			errorObject.put("dbpErrMsg", e.getLocalizedMessage());
			errorObject.put("dbpErrCode", e.toString());
			errorArray = new JSONArray();
			errorArray.put(errorObject);
			Dataset ds = constructDatasetFromJSONArray(errorArray);
			ds.setId("errorObj");
			result.addDataset(ds);
		}
		return result;

	}
	public String generateTokenString(JSONObject inputBody) throws Exception {
		String batchString = generateBatchString(inputBody);
		String txString = generateTransactionString(inputBody);
		String cipsApiUser = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");// "HBL@999";
		String tokenString = generateTokenString(batchString, txString, cipsApiUser);
		String encodeContent = signTokenString(tokenString);
		return encodeContent;
	}
	private String generateBatchString(JSONObject inputBody) {
		inputBody = inputBody.getJSONObject("cipsBatchDetail");
		return inputBody.get("batchId") + "," + inputBody.get("debtorAgent") + "," + inputBody.get("debtorBranch") + ","
				+ inputBody.get("debtorAccount") + "," + inputBody.get("batchAmount") + ","
				+ inputBody.get("batchCrncy");
	}

	private String generateTransactionString(JSONObject inputBody) {
		return id + "," + instructionId + "," + endToEndId + "," + appId + "," + refId + "," + amount;
	}
	public String generateTokenString(String batchString, String txString, String userId) {
		return batchString + "," + txString + "," + userId;
	}
	public String signTokenString(String contentToEncode) throws Exception {
		String cips_cert_path = EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PATH"); // "C:\\HBL-Certificate\\Certificate\\HBL.pfx";
		String cips_cert_password = EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PASSWORD"); // "123";
		String certPath = cips_cert_path;
		byte[] certStore = Files.readAllBytes(Paths.get(certPath));
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new ByteArrayInputStream(certStore), cips_cert_password.toCharArray());
		String alias = keyStore.aliases().nextElement();
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());
		byte[] utf8Data = contentToEncode.getBytes("UTF-8");
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(utf8Data);
		byte[] signedData = signature.sign();
		return Base64.getEncoder().encodeToString(signedData);
	}
	public String getAccessToken(DataControllerRequest request) {
		Map<String, Object> inputmap = new HashMap<String, Object>();
		Map<String, Object> inputmap1 = new HashMap<String, Object>();
		String access_token = "";
		String NCHL_BASIC_AUTHORIZATION = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_AUTHORIZATION");
		AUTH_USERNAME = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");
		AUTH_PASSWORD = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_PASSWORD");
		StringBuilder sb = new StringBuilder();
		sb.append("username").append("=").append(AUTH_USERNAME);
		sb.append("&");
		sb.append("password").append("=").append(AUTH_PASSWORD);
		sb.append("&");
		sb.append("grant_type").append("=").append("password");
		inputmap.put("filter", sb.toString());
		Map<String, Object> headermap = new HashMap<String, Object>();
		headermap.put("Content-Type", contentType);
		headermap.put("Authorization", NCHL_BASIC_AUTHORIZATION);
		try {
			Result res = callInternalServiceAndGetResult(NPI_BILLPAYMENT_SERVICE, NPI_BILLPAYMENT_AUTH_OPEARATION, inputmap, headermap);
			if (res.getHttpStatusCodeParamValue().equals("200")) {
				String refreshToken = res.getParamValueByName("refresh_token");
				if (StringUtils.isNotBlank(refreshToken)) {
					sb = new StringBuilder();
					sb.append("grant_type").append("=").append("refresh_token");
					sb.append("&");
					sb.append("refresh_token").append("=").append(refreshToken);
					inputmap1.put("filter", sb.toString());
					Result response = callInternalServiceAndGetResult(NPI_BILLPAYMENT_SERVICE, NPI_BILLPAYMENT_AUTH_OPEARATION, inputmap1,
							headermap);
					if (response.getHttpStatusCodeParamValue().equals("200")) {
						access_token = response.getParamValueByName("access_token");
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Error occured in getRefresh/Access Token" + e.toString());
		}
		return access_token;
	}

	public boolean isValidNCHLPayload(JSONObject inputMap) {
		boolean isValidFields = true;
		if(inputMap==null) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid payload:"+inputMap);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_INPUT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_INPUT);
			errorArray.put(errorObject);
			isValidFields = false;
			return isValidFields;
		}
		inputMap = inputMap.getJSONObject("cipsBatchDetail");
		logger.debug("HBL:ConfirmBillpayJavaService:validatePayload:inputMap :" + inputMap);
		String amount = inputMap.get("batchAmount") != null ? inputMap.get("batchAmount").toString() : "";
		String debtorAgent = inputMap.get("debtorAgent") != null ? inputMap.get("debtorAgent").toString() : "";
		String debtorBranch = inputMap.get("debtorBranch") != null ? inputMap.get("debtorBranch").toString() : "";
		String debtorName = inputMap.get("debtorName") != null ? inputMap.get("debtorName").toString() : "";
		String debtorAccount = inputMap.get("debtorAccount") != null ? inputMap.get("debtorAccount").toString() : "";

		if (StringUtils.isBlank(amount)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid amount:" + amount);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_AMOUNT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_AMOUNT);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorAgent)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid debtorAgent:" + debtorAgent);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_AGENT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_AGENT);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorBranch)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid debtorBranch:" + debtorBranch);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_BRANCH);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_BRANCH);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorName)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid debtor name:" + debtorName);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_NAME);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_NAME);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorAccount)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConfirmBillpayJavaService:validatePayload:invalid debtorBranch:" + debtorAccount);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_ACCOUNT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_ACCOUNT);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		logger.debug("HBL:ConfirmBillpayJavaService:validatePayload:isValidFields:" + isValidFields);
		return isValidFields;
	}
	public HashMap convertJSONToHashMap(JSONObject jsonObj) {
		Map<String, Object> map = jsonObj.toMap();
		HashMap hashMap = (map instanceof HashMap) ? (HashMap) map : new HashMap(map);
		return hashMap;
	}

	public void removeEmptyAndNullFields(Object object) throws Exception {
		if (object instanceof JSONArray) {
			JSONArray array = (JSONArray) object;
			for (int i = 0; i < array.length(); ++i)
				removeEmptyAndNullFields(array.get(i));
		} else if (object instanceof JSONObject) {
			JSONObject json = (JSONObject) object;
			JSONArray names = json.names();
			if (names == null)
				return;
			for (int i = 0; i < names.length(); ++i) {
				String key = names.getString(i);

				if (json.isNull(key) || json.get(key).equals("") || json.get(key) == null) {
					json.remove(key);
				} else {
					removeEmptyAndNullFields(json.get(key));
				}
			}
		}
	}

	public Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}
		return response;
	}

	public Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}
	public String generateBasicAuthorization(String userName, String password) {
		String authorization=userName+":"+password;
		authorization= Base64.getEncoder().encodeToString(authorization.getBytes());
		return "Basic "+authorization;
	}
	public JSONObject makePlainHTTPServiceCall(String url, String authorization, JSONObject requestBody)
			throws IOException {
		logger.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCall: requestBody:" + requestBody);
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestBody.toString());
		JSONObject returnJson = new JSONObject();
		try {
			Request request = new Request.Builder().url(url).method("POST", body)
					// .addHeader("Content-Type", "application/json")
					.addHeader("Authorization", authorization).build();

			try (Response response = client.newCall(request).execute()) {
				String responseBody = response.body().string();
				JSONObject serviceResponse = new JSONObject(responseBody);
				logger.debug(
						"HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCall: response body:" + serviceResponse);
				int statusCode = response.code();
				String responseMessage = serviceResponse.has("responseMessage")
						? serviceResponse.getString("responseMessage")
						: response.message();
				returnJson.put("statusCode", statusCode);
				if (statusCode != 404) {
					if (statusCode == 200) {
						JSONObject responseResult = serviceResponse.has("responseResult")
								&& !serviceResponse.isNull("responseResult")
										? serviceResponse.getJSONObject("responseResult")
										: new JSONObject();
						returnJson.put("responseMessage",
								responseResult.has("responseDescription")
										? responseResult.getString("responseDescription")
										: "");
						returnJson.put("responseCode",
								responseResult.has("responseCode") ? responseResult.getString("responseCode") : "");
						if (!responseResult.isEmpty())
							returnJson.put("responseResult", responseResult);
						returnJson.put("cipsTransactionDetail", serviceResponse.get("cipsTransactionDetail"));
						returnJson.put("cipsBatchDetail", serviceResponse.get("cipsBatchDetail"));
					} else {
						returnJson.put("responseMessage", responseMessage);
						returnJson.put("responseCode", serviceResponse.get("responseCode"));
						returnJson.put("responseResult", serviceResponse);
					}
				} else {
					String responseMessage1 = serviceResponse.getString("error");
					returnJson.put("responseMessage", responseMessage1);
					returnJson.put("responseCode", serviceResponse.get("responseCode"));

				}
			}
		} catch (Exception e) {
			returnJson = new JSONObject();
			returnJson.put("statusCode", 500);
			returnJson.put("responseCode", "500");
			returnJson.put("responseMessage", e.getLocalizedMessage());
		}
		return returnJson;
	}
	private Result callInternalServiceAndGetResult(String serviceid, String operationid, Map<String, Object> inputmap,
			Map<String, Object> headers) throws DBPApplicationException {
		logger.debug(
				"HBL::ConfirmBillpayJavaService: callInternalServiceAndGetString: inputmap:" + inputmap.toString());
		logger.debug("HBL::ConfirmBillpayJavaService: callInternalServiceAndGetString: headers:" + headers.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		logger.debug("HBL::ConfirmBillpayJavaService: callInternalServiceAndGetString: response:"
				+ res.getHttpStatusCodeParamValue());

		return res;
	}
	public static Result callObjectService(String serviceID,String objid, String operationID, Map<String, Object> inputmap,
			Map<String, Object> headermap, DataControllerRequest dcRequest) throws Exception {
		Result result = null;
		try
		{
			OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder().withServiceId(serviceID).withObjectId(objid)
					.withOperationId(operationID).build();
			
			ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputmap).withHeaders(headermap).withAuthorizationToken(dcRequest.getParameter("X-Kony-Authorization")).build();
			result = serviceRequest.invokeServiceAndGetResult();
		}
		catch(Exception e)
		{
			logger.error("Exception Occured at callObjectService ");
			throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : " + operationID + " objectid : " + objid +" ; "+ e.getMessage() );
		}
		return result;
	}
	public JSONObject generateTopupNepalPaymentPayload(Map<String, Object> inputMap, DataControllerRequest arg1, DataControllerResponse arg2, Result arg3)
			throws Exception {
		 JSONObject payload = new JSONObject();
		 payload.put("Amount", inputMap.get("transactionAmount"));
		 payload.put("mobileno", inputMap.get("mobileno"));
		 payload.put("Action", inputMap.get("Action"));
		 payload.put("paymentAggregator", inputMap.get("paymentAggregator"));
		 String userId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_USERID");
	     payload.put("userId", userId);
		return payload;
	}
	public JSONObject generateNEAPaymentPayload(Map<String, Object> inputMap, DataControllerRequest arg1, DataControllerResponse arg2, Result arg3)
			throws Exception {
		   String neaPaymentsUserId = EnvironmentConfigurationsHandler.getServerProperty("NEA_PAYMENTS_USERID");
	       String neaPaymentsPassword = EnvironmentConfigurationsHandler.getServerProperty("NEA_PAYMENTS_PASSWORD");
	       String neaPaymentsAccessCode = EnvironmentConfigurationsHandler.getServerProperty("NEA_PAYMENTS_ACCESS_CODE");
	       String neaSALT = EnvironmentConfigurationsHandler.getServerProperty("NEA_SALT");
	       String randomid=String.valueOf(generateNEAPaymentBatchId());
	       logger.debug("HBL:NEABillpaymentsPreprocessor:payload:"+inputMap);
	       // below two params are used for NEA ConfirmBillPaid service
	       String txnId="HBL-"+randomid;
	       String txnSignature=generateNEAPaymentTxnString(neaPaymentsUserId, neaPaymentsPassword, neaPaymentsAccessCode, txnId, neaSALT);
		  //String encodedContent = HBLTxnToken(txnSignature);
		 JSONObject payload = new JSONObject();
		 payload.put("userId", neaPaymentsUserId);
		 payload.put("password", neaPaymentsPassword);
		 payload.put("accessCode", neaPaymentsAccessCode);
		 payload.put("processId", randomid);
		 payload.put("txnId", txnId);
		 payload.put("txnSignature", txnSignature);
		 payload.put("paymentAggregator", inputMap.get("paymentAggregator")); 
		 payload.put("scno", inputMap.get("scno")); 
		 payload.put("consumerId", inputMap.get("consumerId")); 
		 payload.put("counterCode", inputMap.get("counterCode")); 
		 payload.put("amount", formatAmount(inputMap.get("transactionAmount").toString())); 
		 payload.put("transactionAmount", formatAmount(inputMap.get("transactionAmount").toString())); 
		return payload;
	}
	public String generateNEAPaymentTxnString(String userId, String password, String accessCode, String txnId, String neaSALT) throws Exception {
		String txnString=userId+password+accessCode+txnId+neaSALT;
		logger.debug("HBL:NEABillpaymentsPreprocessor:txnSignature before Hashing :"+txnString);
		 txnString = DigestUtils.sha256Hex(txnString);//HBLTxnToken(txnString);
		logger.debug("HBL:NEABillpaymentsPreprocessor:txnSignature after Hashing :"+txnString);
		return txnString;
	}
	public String generateNEAPaymentBatchId(){
		String batchId = "NEA" + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return batchId;
	}
	private Result processPayment(String methodId, Object[] inputArray, JSONObject requestBody, DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		Result result = new Result();
		Map<String, Object> inputParams= HelperMethods.getInputParamObjectMap(inputArray);
		String paymentAggregator = inputParams.get("paymentAggregator") != null ? inputParams.get("paymentAggregator").toString() : "";
		JSONObject transactionDetails= new JSONObject();
		transactionDetails.put("requestPayload", inputParams);
		
		inputParams.put("transactionAmount", formatAmount(inputParams.get("transactionAmount").toString()));
		inputParams.put("amount", formatAmount(inputParams.get("amount").toString()));
		String paymentId=HelperMethods.getRandomNumericString(10);
		inputParams.put("paymentId", paymentId);
		String fromAccountNumber=inputParams.get("fromAccountNumber").toString();
		if(requestBody!=null) {
			if(paymentAggregator.equalsIgnoreCase("TOP-UP-NEPAL")) {
				requestBody.put("txnId", paymentId);
			}
		inputParams.put("externalApiPayload", requestBody);
		logger.debug("HBL:ConfirmBillpayJavaService: processPayment:inputParams:"+inputParams);
		inputArray[1]=new Object();
		inputArray[1]=inputParams;
		}
		try {
			//Initializing of TransactionResource through Abstract factory method
			BillPayTransactionResource billpayTranscationResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(BillPayTransactionResource.class);
			result  = billpayTranscationResource.createTransaction(methodId, inputArray, dcRequest, dcResponse);
			logger.debug("HBL:ConfirmBillpayJavaService: processPayment:result:"+ResultToJSON.convert(result));
			
			String dbpErrMsg=result.getParamValueByName("dbpErrMsg");
			String dbpErrCode=result.getParamValueByName("dbpErrCode");
			String refId=result.getParamValueByName("referenceId");
			String status=result.getParamValueByName("status");
			if(StringUtils.isNotBlank(dbpErrCode) || StringUtils.isNotBlank(dbpErrMsg) ) {
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("paymentAggregator", paymentAggregator));
				if(dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage()) || dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())){
				String legalEntityId=result.getParamValueByName("legalEntityId");
				JSONObject accountBalanceArray=getAvailableBalanceByAccountId(fromAccountNumber, legalEntityId);
				String availableBalance=accountBalanceArray.get("availableBalance")!=null?accountBalanceArray.getString("availableBalance"):"";
				String currencyCode=accountBalanceArray.get("currencyCode")!=null?accountBalanceArray.getString("currencyCode"):"";
				if(StringUtils.isNotBlank(availableBalance)) {
				result.addParam(new Param("availableBalance", availableBalance));
				result.addParam(new Param("currencyCode", currencyCode));
				}
				result.appendResult(JSONToResult.convert(transactionDetails.toString()));
				return HBLEnums.ERR_20001.setErrorCode(result);
				}
				else if(StringUtils.isNotBlank(status) && status.equalsIgnoreCase(ErrorCodeEnum.ERR_10420.getMessage())) {
				result.addParam(new Param("dbpErrCode", dbpErrCode));
				result.addParam(new Param("dbpErrMsg", dbpErrMsg));
				}
				else if(dbpErrMsg.equalsIgnoreCase(ErrorCodeEnum.ERR_21210.getMessage())) {
					result.addParam(new Param("dbpErrCode", dbpErrCode));
					result.addParam(new Param("dbpErrMsg", dbpErrMsg));
				}
				else {
				return HBLEnums.ERR_20000.setErrorCode(result);
				}
			}
			else if(StringUtils.isNotBlank(refId)){
				result.addParam(new Param("opstatus", "0"));
				result.addParam(new Param("success", "true"));
				result.addParam(new Param("paymentAggregator", paymentAggregator));
				String legalEntityId=result.getParamValueByName("legalEntityId");
				JSONObject accountBalanceArray=getAvailableBalanceByAccountId(fromAccountNumber, legalEntityId);
				String availableBalance=accountBalanceArray.get("availableBalance")!=null?accountBalanceArray.getString("availableBalance"):"";
				String currencyCode=accountBalanceArray.get("currencyCode")!=null?accountBalanceArray.getString("currencyCode"):"";
				if(StringUtils.isNotBlank(availableBalance)) {
				result.addParam(new Param("availableBalance", availableBalance));
				result.addParam(new Param("currencyCode", currencyCode));
				}
			}
			else if(StringUtils.isBlank(refId)) {
				result.addParam(new Param("opstatus", "0"));
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("paymentAggregator", paymentAggregator));
				HBLEnums.ERR_20000.setErrorCode(result);
				}
		}	
		catch(Exception e) {
			logger.error("Error occured while invoking CreateBillPayTransaction: "+ e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}
	public String formatAmount(String amount){
		 if(StringUtils.isNotBlank(amount)) {
		 BigDecimal decimalAmount= new BigDecimal(amount).setScale(2, RoundingMode.FLOOR);
	     amount=String.valueOf(decimalAmount);
		 }
		 return amount;
	}
	private JSONObject getAvailableBalanceByAccountId(String AccountId, String companyId)
            throws ApplicationException {

        HashMap<String, Object> inputParams = new HashMap<>();
        Map<String, Object> headerParams = new HashMap<>();
        String stringResponse = null;
        JSONObject balanceResponse = new JSONObject();
        JSONObject accountDetails = new JSONObject();
        if(StringUtils.isNotBlank(AccountId) && StringUtils.isNotBlank(companyId)) {
        try {
        	inputParams.put("Account_id", AccountId);
			headerParams.put("companyId", companyId);
			stringResponse = Executor.invokeService(
					ArrangementsAPIServices.T24IRISARRANGEMENTSERVICES_GETLATESTBALANCES, inputParams,
					headerParams);
			logger.debug("Response from T24 GetLatestBalances Service: " + stringResponse);
			balanceResponse = new JSONObject(stringResponse);
			if(balanceResponse.has("Accounts")) {
				JSONArray accountBalanceArray = balanceResponse.getJSONArray("Accounts");
				if(accountBalanceArray.length()>0) {
					accountDetails=accountBalanceArray.getJSONObject(0);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception Occurred in getAvailableBalanceByAccountId:"+e.toString());
		}
        }
        return accountDetails;
     }

}
