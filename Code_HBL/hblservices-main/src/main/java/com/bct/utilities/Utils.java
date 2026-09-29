package com.bct.utilities;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.auth.google.totp.TOTP;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.dataobject.Record;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.user.UserConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;

public class Utils {
	private static final Logger LOG = LogManager.getLogger(Utils.class);
	public static String generateSecretKey() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[20];
		random.nextBytes(bytes);
		Base32 base32 = new Base32();
		return base32.encodeToString(bytes);
	}

	public static String getTOTPCode(String secretKey) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secretKey);
		String hexKey = Hex.encodeHexString(bytes);
		LOG.debug("hexKey ***"+ hexKey);
		LOG.debug("getTOTPCode ***"+ TOTP.getOTP(hexKey));
		return TOTP.getOTP(hexKey);
	}

	public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
		try {
			return "otpauth://totp/"
					+ URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
					+ "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
					+ "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
		
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void createQRCode(String barCodeData, String filePath, int height, int width)
			throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (FileOutputStream out = new FileOutputStream(filePath)) {
			MatrixToImageWriter.writeToStream(matrix, "png", out);
		}
		String base64 = null;
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			MatrixToImageWriter.writeToStream(matrix, "png", out);
			base64 = new String(Base64.getEncoder().encode(out.toByteArray()));
		}
		LOG.debug("base64 >> " + base64);
	}
	
	public static String returnQRCode (String barCodeData, int height, int width) throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
		
		String base64 = null;
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			MatrixToImageWriter.writeToStream(matrix, "png", out);
			base64 = new String(Base64.getEncoder().encode(out.toByteArray()));
		}
		LOG.debug("base64 >> " + base64);
		return base64;
	}
	
	
	public static String generateEncryptedKey(String UserName){

		/*String encodedKey = "";
		/*
		 * final int pswdIterations = 10000; final int keySize = 128;
		 * 
		 * final byte[] saltBytes = { -83, 69, -15, -93, -60, 62, 111, -83 };
		 * 
		 * SecretKeyFactory factory =
		 * SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); PBEKeySpec spec = new
		 * PBEKeySpec(UserName.toCharArray(), saltBytes, pswdIterations, keySize);
		 * SecretKey secretKey = factory.generateSecret(spec); SecretKeySpec secret =
		 * new SecretKeySpec(secretKey.getEncoded(), "AES"); String encodedKey =
		 * Base64.getEncoder().encodeToString(secret.getEncoded());
		 */
        
     /*   Base32 base32 = new Base32();
        encodedKey = base32.encodeAsString(UserName.getBytes());
        LOG.debug("secret:" +encodedKey);
		
		return encodedKey; */
		
		
		try {
		      // Master key (keep secret!)
		      String MASTER_KEY = "HBL_BANK_MASTER_SECRET_2025";
		      Mac hmac = Mac.getInstance("HmacSHA256");
		      SecretKeySpec keySpec = new SecretKeySpec(
		        MASTER_KEY.getBytes(), 
		        "HmacSHA256"
		      );
		      hmac.init(keySpec);
		      byte[] hash = hmac.doFinal(UserName.getBytes());
		      // Take first 20 bytes
		      byte[] secretBytes = new byte[20];
		      System.arraycopy(hash, 0, secretBytes, 0, 20);
		      Base32 base32 = new Base32();
		      String secret = base32.encodeToString(secretBytes);
		      return secret.replaceAll("=", "");
		    } catch (Exception e) {
		      throw new RuntimeException(e);
		    }
      
	}
	
	public static String customerEmailFromSession(DataControllerRequest request) {
		String email = "";
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		String customerResultfromSession = (String)temenosUtils.retreiveFromSession("customer", request);
		Result enrollResult = ConvertJsonToResult.convert(customerResultfromSession);
		
		LOG.debug("user session details ### :"+ ResultToJSON.convert(enrollResult));
		email = enrollResult.getParamValueByName("email");
		LOG.debug("user session details email ### :"+ email);
		
		return email;
	}
	
	public static String customerPhoneFromSession(DataControllerRequest request) {
		String phone = "";
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		String customerResultfromSession = (String)temenosUtils.retreiveFromSession("customer", request);
		Result enrollResult = ConvertJsonToResult.convert(customerResultfromSession);
		
		LOG.debug("user session details ### :"+ ResultToJSON.convert(enrollResult));
		phone = enrollResult.getParamValueByName("phone");
		LOG.debug("user session details phone ### :"+ phone);
		
		return phone;
	}
	
	public static String getCustomerEmialfromCore(DataControllerRequest request, String cutomerid) throws Exception {
		String email = "";
		String mobile = "";
		HashMap<String, Object> inputParams = new HashMap<>();
		String cif = fetchCifId(request, cutomerid);
		inputParams.put("userID", cif);
		request.addRequestParam_("userID", cif);

		JSONObject customerInfo = new JSONObject();
		LOG.debug("cif value in  getCustomerEmialfromCore### :" + cif);
		JSONArray accounts = new JSONArray();

		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		Result result = com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils.callIntegrationService(request, inputParams,
				serviceHeaders, "T24ISUser", "getuserDetalFromSpotlight", false);
		Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
		LOG.debug("userDS value in  getCustomerEmialfromCore### :" + userDS);
		if (userDS != null && !userDS.getAllRecords().isEmpty()) {

			JSONArray array = ResultToJSON.convertDataset(userDS);
			customerInfo = array.getJSONObject(0);
			accounts = customerInfo.getJSONArray("contactDetails");

			for (int i = 0; i < accounts.length(); i++) {
				if (accounts.getJSONObject(i).getString("contactType").equalsIgnoreCase("EMAIL")) {
					email = accounts.getJSONObject(i).getString("contactData");
					LOG.debug("Email val ### :" + email);
				} else if (accounts.getJSONObject(i).getString("contactType").equalsIgnoreCase("MOBILE")) {
					String prefix = accounts.getJSONObject(i).getString("iddPrefixPhone");
					String ph = accounts.getJSONObject(i).getString("contactData");
					LOG.debug("Phone val ### :" + ph);
					LOG.debug("prefix val ### :" + prefix);
					mobile = prefix + " " + ph;
				}
			}
		}

		return email;
	}
	
	public static String getCustomerPhonefromCore(DataControllerRequest request, String cutomerid) throws Exception {
		String email = "";
		String mobile = "";
		HashMap<String, Object> inputParams = new HashMap<>();
		String cif = fetchCifId(request, cutomerid);
		inputParams.put("userID", cif);
		request.addRequestParam_("userID", cif);

		JSONObject customerInfo = new JSONObject();
		LOG.debug("cif value in  getCustomerEmialfromCore### :" + cif);
		JSONArray accounts = new JSONArray();

		Result result = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
				UserConstants.SERVICE_ID_USER, "getuserDetalFromSpotlight", true);
		
		LOG.debug("getCustomerPhonefromCore result ### :" + ResultToJSON.convert(result));

		Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
		LOG.debug("userDS value in  getCustomerEmialfromCore### :" + userDS);
		if (userDS != null && !userDS.getAllRecords().isEmpty()) {

			JSONArray array = ResultToJSON.convertDataset(userDS);
			customerInfo = array.getJSONObject(0);
			accounts = customerInfo.getJSONArray("contactDetails");

			for (int i = 0; i < accounts.length(); i++) {
				if (accounts.getJSONObject(i).getString("contactType").equalsIgnoreCase("MOBILE")) {
					String prefix = accounts.getJSONObject(i).getString("iddPrefixPhone");
					String ph = accounts.getJSONObject(i).getString("contactData");
					LOG.debug("Phone val ### :" + ph);
					LOG.debug("prefix val ### :" + prefix);
					mobile = prefix + " " + ph;
				}
			}
		}

		return mobile;
	}
	
	
	public JSONObject getContactDetails(String customerId, DataControllerRequest request) {
		CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(customerId);
		DBXResult communicationResponse = communicationBackendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, request.getHeaderMap());
		JsonObject customerCommunication = ((JsonObject) communicationResponse.getResponse());
		JSONObject communicationObj= new JSONObject();
		if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
				&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
		JsonArray communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
			for (JsonElement jsonelement : communicationArray) {
				JsonObject object = jsonelement.getAsJsonObject();
					if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
						communicationObj.put("email", JSONUtil.getString(object, "Value"));
					if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
						communicationObj.put("phone", JSONUtil.getString(object, "Value"));
				}
			}
		LOG.debug("getContactDetails:" + communicationObj);
		return communicationObj;
}

	
	public static String fetchCifId(DataControllerRequest dcRequest, String customerId) throws HttpCallException {
		String cifId = "";
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.T24;
		Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BACKENDIDENTIFIER_GET);
		cifId = HelperMethods.getFieldValue(result, DTOConstants.BACKENDID);
		LOG.debug("cifId ### :" + cifId);
		return cifId;
	}
	
	public static JSONObject getContactDetails(DataControllerRequest request, String customerId) {
		CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CommunicationBackendDelegate.class);
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(customerId);
		DBXResult communicationResponse = communicationBackendDelegate
				.getPrimaryMFACommunicationDetails(customerCommunicationDTO, request.getHeaderMap());
		JsonObject customerCommunication = ((JsonObject) communicationResponse.getResponse());
		JSONObject communicationObj = new JSONObject();
		if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
				&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
			JsonArray communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					.getAsJsonArray();
			for (JsonElement jsonelement : communicationArray) {
				JsonObject object = jsonelement.getAsJsonObject();
				if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
					communicationObj.put("email", JSONUtil.getString(object, "Value"));
				if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
					communicationObj.put("phone", JSONUtil.getString(object, "Value"));
			}
		}
		LOG.error("getContactDetails:" + communicationObj);
		return communicationObj;
	}
	
	private static String getCustomerEmail(DataControllerRequest request, String customerId) {
		String customerEmail = "";
		try {
			String Type_id = "COMM_TYPE_EMAIL";
			LOG.debug("customerId **:" + customerId);
			String contractId = getContractId(request, customerId);
			LOG.debug("contractId **:" + contractId);
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("$filter", "contractId eq " + contractId + " and typeId eq " + Type_id);
			request.addRequestParam_("$filter", "contractId eq " + contractId + " and typeId eq " + Type_id);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CONTRACTCOMMUNICATION_GET, false);
			Dataset customerDataset = result.getDatasetById("contractcommunication");
			if (null != customerDataset) {
				customerEmail = customerDataset.getRecord(0).getParamValueByName("value");
			} else {
				LOG.debug("Else getCustomerEmail:");
			}
			LOG.debug("getCustomerEmail id:" + customerEmail);
		} catch (Exception e) {
			LOG.error("Error while retrieving customer email for Customer " + customerEmail);
		}
		return customerEmail;
	}
	
	private static String getContractId(DataControllerRequest request, String customerId) {
		String contractId = "";
		try {
			LOG.debug("customerId **" + customerId);
			String filter = CommonUtils.buildOdataCondition("customerId", Constants.EQUAL, customerId);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CONTRACT_CUSTOMERS_GET, false);
			Dataset customerDataset = result.getDatasetById("contractcustomers");
			if (null != customerDataset) {
				contractId = customerDataset.getRecord(0).getParamValueByName("contractId");
			} else {
				LOG.debug("Else getContractId:");
			}
			LOG.debug("getContractId :" + contractId);
		} catch (Exception e) {
			LOG.error("Error while retrieving Contract Id for Customer " + contractId);
		}
		return contractId;
	}
	
	
	public static String hashPin(String Pin) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] bytes = Pin.getBytes(StandardCharsets.UTF_8);
			byte[] hash = sha256.digest(bytes);
			return getStringFromHash(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not available", e);
		}
	}
 
	private static String getStringFromHash(byte[] hash) {
		StringBuilder result = new StringBuilder();
		for (byte b : hash) {
			result.append(String.format("%02X", b));
		}
		return result.toString();
	}
}
