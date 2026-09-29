package com.bct.eSewa;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EsewaValidationClient {

	private static final Logger logger = LogManager.getLogger(EsewaValidationClient.class);

	// Validation request model
	public static class ValidationRequest {
		public String client_id;
		public String initiatorClientCode;
		public String initiatorMobile;
		public String targetedMobile;
		public String targetedAmount;

		public ValidationRequest(String clientId, String initiatorClientCode, String initiatorMobile,
				String targetedMobile, String targetedAmount) {
			logger.debug("ValidationRequest##...targetedAmount"+ targetedAmount);
			logger.debug("ValidationRequest##...initiatorMobile"+ initiatorMobile);
			this.client_id = clientId;
			this.initiatorClientCode = initiatorClientCode;
			this.initiatorMobile = initiatorMobile;
			this.targetedMobile = targetedMobile;
			this.targetedAmount = targetedAmount;
		}

		public String toJson() {
			return String.format(
					"{\"client_id\":\"%s\",\"initiatorClientCode\":\"%s\",\"initiatorMobile\":\"%s\",\"targetedMobile\":\"%s\",\"targetedAmount\":\"%s\"}",
					client_id, initiatorClientCode, initiatorMobile, targetedMobile, targetedAmount);
		}
	}

	// PerformPayment request model
	public static class PerformPaymentRequest {
		public String client_id;
		public String batch_id;
		public String swift_code;
		public String esewa_load_requests;

		public PerformPaymentRequest(String clientId, String batchId, String swiftCode, String esewaloadrequests) {
			this.client_id = clientId;
			this.batch_id = batchId;
			this.swift_code = swiftCode;
			this.esewa_load_requests = esewaloadrequests;
		}

		public String toJson() {
			return String.format(
					"{\"client_id\":\"%s\",\"initiatorClientCode\":\"%s\",\"initiatorMobile\":\"%s\",\"targetedMobile\":\"%s\",\"targetedAmount\":\"%s\"}",
					client_id, esewa_load_requests);
		}
	}

	// Response model
	public static class EsewaResponse {
		public String data;
		public String secret_key;
		public String signature;
		public String code;
		public String message;
	}

	public static String validateeSewaId(DataControllerRequest request, String targetedMobile, String targetedAmount,
			String SERVER_DATA_ENCRYPTION_PUBLICKEY, String CLIENT_SIGNATURE_PRIVATEKEY,
			String CLIENT_DATA_ENCRYPTION_PRIVATE_KEY, String SERVER_SIGNATURE_PUBLICKEY, String CLINET_ID,
			String SWIFT_CODE, String VALIDATION_URL) throws Exception {

		logger.error("Plain Request##...targetedMobile"+ targetedMobile);
		logger.error("Plain Request##...targetedAmount"+ targetedAmount);
		// Step 1: Prepare validationRequest object
		ValidationRequest validationRequest = new ValidationRequest(CLINET_ID, CLINET_ID, "", targetedMobile,
				targetedAmount);

		// Step 2: Generate 32-byte AES Secret Key
		SecretKey secretKey = generateAESKey();
		String secretKeyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());

		// Step 3: Encrypt JSON with AES-256-ECB
		String jsonData = validationRequest.toJson();
		logger.error("Plain Request##..."+ jsonData);
		String encryptedData = encryptAES(jsonData, secretKey);

		// Step 4: Encrypt secret key with RSA public key
		String encryptedSecretKey = encryptRSA(secretKeyBase64, SERVER_DATA_ENCRYPTION_PUBLICKEY);

		// Step 5: Sign the AES-encrypted data
		String signature = signData(encryptedData, CLIENT_SIGNATURE_PRIVATEKEY);

		// Step 6: Prepare Validation final payload
		Map<String, String> payload = new HashMap<>();
		payload.put("client_id", CLINET_ID);
		payload.put("secret_key", encryptedSecretKey);
		payload.put("data", encryptedData);
		payload.put("signature", signature);

		logger.error("Sending request to eSewa...");

		// Step 7: Send VALIDATION POST request
		String responseJson = "";
		
		try {
			responseJson = sendPostRequest(VALIDATION_URL, payload);
		} catch (Exception e) {
			recordApiAuditLog(request, "validateEsewaId", jsonData, payload.toString(), e.toString(),
					e.toString());
			responseJson = "";
			return responseJson;
		}
		logger.error(" VALIDATION responseJson : " + responseJson);

		// Parse the response
		EsewaLoadResponse esewaResponse = parseResponse(responseJson);

		// Step 8-9: Process successful response
		if (esewaResponse.data != null && esewaResponse.secret_key != null && esewaResponse.signature != null) {
			String decryptedResponse = processEsewaResponse(esewaResponse.data, esewaResponse.secret_key,
					esewaResponse.signature, SERVER_SIGNATURE_PUBLICKEY, CLIENT_DATA_ENCRYPTION_PRIVATE_KEY);

			logger.debug("VALIDATION Decrypted response from eSewa:" + decryptedResponse);
			recordApiAuditLog(request, "validateEsewaId", jsonData, payload.toString(), esewaResponse.toString(), decryptedResponse);
			return decryptedResponse;
		} else {
			logger.error("Unexpected response format: " + responseJson);
			return responseJson;
		}
	}

	public static String performPaymentRequest(DataControllerRequest request, String AccNumber, String AccountName,
			String paymentDesc, String field1, String field2, String amount, String eSewaId, String batchId,
			String SERVER_DATA_ENCRYPTION_PUBLICKEY, String CLIENT_SIGNATURE_PRIVATEKEY,
			String CLIENT_DATA_ENCRYPTION_PRIVATE_KEY, String SERVER_SIGNATURE_PUBLICKEY, String CLIENT_ID,
			String SWIFT_CODE, String PAYMENT_URL) {
		String serviceStatus  = "Fail";
		try {
			// Prepare payment request data
			InitiatorDetails initiatorDetails = new InitiatorDetails(AccNumber, AccountName, field1, field2,
					paymentDesc);

			EsewaLoadRequest[] loadRequests = { new EsewaLoadRequest(amount, eSewaId, batchId, initiatorDetails) };

			PaymentRequest paymentRequest = new PaymentRequest(CLIENT_ID, batchId, SWIFT_CODE, loadRequests);

			logger.debug("AccNumber ##: " + AccNumber);
			logger.debug("AccountName ##: " + AccountName);
			logger.debug("paymentDesc ##: " + paymentDesc);
			logger.debug("field1 ##: " + field1);
			logger.debug("field2 ##: " + field2);
			logger.debug("amount ##: " + amount);
			logger.debug("eSewaId ##: " + eSewaId);
			logger.debug("batchId ##: " + batchId);
			// Generate AES key and encrypt the payment data
			SecretKey secretKey;
			secretKey = generateAESKey();
			String secretKeyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());

			String jsonData = paymentRequest.toJson();
			String encryptedData = encryptAES(jsonData, secretKey);

			// Encrypt secret key with RSA public key
			String encryptedSecretKey = encryptRSA(secretKeyBase64, SERVER_DATA_ENCRYPTION_PUBLICKEY);

			// Sign the encrypted data
			String signature = signData(encryptedData, CLIENT_SIGNATURE_PRIVATEKEY);

			// Prepare final payload
			Map<String, String> payload = new HashMap<>();
			payload.put("client_id", CLIENT_ID);
			payload.put("secret_key", encryptedSecretKey);
			payload.put("data", encryptedData);
			payload.put("signature", signature);

			logger.debug("Sending payment request to eSewa...");
			logger.debug("Original payment data: " + jsonData);

			// Send POST request
			String responseJson = "";
			
			try {
				responseJson = sendPostRequest(PAYMENT_URL, payload);
			} catch (Exception e) {
				recordApiAuditLog(request, "performPayment", jsonData, payload.toString(), e.toString(), e.toString());
			}

			// Parse the response
			EsewaLoadResponse esewaResponse = parseResponse(responseJson);

			if (esewaResponse.code != null && !esewaResponse.code.equals("0000")) {
				logger.debug("Payment error: " + esewaResponse.code + " - " + esewaResponse.message);
				return responseJson;
			}
			// Process successful payment response
			if (esewaResponse.data != null && esewaResponse.secret_key != null && esewaResponse.signature != null) {
				String decryptedResponse = processEsewaResponse(esewaResponse.data, esewaResponse.secret_key,
						esewaResponse.signature, SERVER_SIGNATURE_PUBLICKEY, CLIENT_DATA_ENCRYPTION_PRIVATE_KEY);

				logger.debug("Decrypted payment response:" + decryptedResponse);
				recordApiAuditLog(request, "performPayment", jsonData, payload.toString(), esewaResponse.toString(),
						decryptedResponse);

				return decryptedResponse;
			} else {
				logger.debug("Payment response: " + responseJson);
				return responseJson;
			}
		} catch (Exception e) {
			logger.debug("performPaymentRequest Exception: " + e);
			e.printStackTrace();
		}
		return serviceStatus;
	}

	public static String checkTransactionStatus(DataControllerRequest request, String[] transactionIds, String SERVER_DATA_ENCRYPTION_PUBLICKEY,
			String CLIENT_SIGNATURE_PRIVATEKEY, String CLIENT_DATA_ENCRYPTION_PRIVATE_KEY,
			String SERVER_SIGNATURE_PUBLICKEY, String CLIENT_ID, String ESEWA_SWIFT_CODE, String STATUS_CHECK_URL,
			String core_identifier,String Customer_id,String username)
			throws Exception {

		// Step 1: Prepare status request object
		TransactionStatusRequest statusRequest = new TransactionStatusRequest(CLIENT_ID, transactionIds);

		// Generate AES key and encrypt the payment data
		SecretKey secretKey = generateAESKey();
		String secretKeyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());

		String jsonData = statusRequest.toJson();
		String encryptedData = encryptAES(jsonData, secretKey);
		logger.debug("Check Transaction status Plain request:##"+ jsonData);

		// Encrypt secret key with RSA public key
		String encryptedSecretKey = encryptRSA(secretKeyBase64, SERVER_DATA_ENCRYPTION_PUBLICKEY);

		// Sign the encrypted data
		String signature = signData(encryptedData, CLIENT_SIGNATURE_PRIVATEKEY);

		// Step 6: Prepare final payload
		Map<String, String> payload = new HashMap<>();
		payload.put("client_id", CLIENT_ID);
		payload.put("secret_key", encryptedSecretKey);
		payload.put("data", encryptedData);
		payload.put("signature", signature);

		logger.debug("Original status request: " + jsonData);
		logger.debug("Sending status request to eSewa...");

		// Step 7: Send POST request
		String responseJson = "";
		try {
			responseJson = sendPostRequest(STATUS_CHECK_URL, payload);
		} catch (Exception e) {
			recordApiAuditCheckStatusLog(request, "TransactionStatusCheck", jsonData, payload.toString(), e.toString(),
					e.toString(),core_identifier,Customer_id,username);
			responseJson = "";
			return responseJson;
		}

		logger.debug("Check Transaction status encryptedData request:##"+ payload.toString());
		// Parse the response
		EsewaLoadResponse esewaResponse = parseResponse(responseJson);
		logger.debug("Check Transaction status encrypted status response:##"+ esewaResponse.toString());

		if (esewaResponse.code != null && esewaResponse.code != "0000") {
			logger.debug("Error from eSewa: " + esewaResponse.code + " - " + esewaResponse.message);
			String decryptedResponse = processEsewaResponse(esewaResponse.data, esewaResponse.secret_key,
					esewaResponse.signature, SERVER_SIGNATURE_PUBLICKEY, CLIENT_DATA_ENCRYPTION_PRIVATE_KEY);
			recordApiAuditCheckStatusLog(request, "TransactionStatusCheck", jsonData, payload.toString(), esewaResponse.toString(), decryptedResponse, core_identifier,Customer_id,username);
			return responseJson;
		}

		// Process successful response
		if (esewaResponse.data != null && esewaResponse.secret_key != null && esewaResponse.signature != null) {
			String decryptedResponse = processEsewaResponse(esewaResponse.data, esewaResponse.secret_key,
					esewaResponse.signature, SERVER_SIGNATURE_PUBLICKEY, CLIENT_DATA_ENCRYPTION_PRIVATE_KEY);

			logger.debug("Check Transaction status Decrypted status response:##"+ decryptedResponse);
			recordApiAuditCheckStatusLog(request, "TransactionStatusCheck", jsonData, payload.toString(), esewaResponse.toString(), decryptedResponse, core_identifier,Customer_id,username);

			return decryptedResponse;

			// Parse the decrypted status data
			// parseStatusDetails(decryptedResponse, esewaResponse);

		} else {
			logger.debug("Unexpected Transaction response format: " + responseJson);
			recordApiAuditCheckStatusLog(request, "TransactionStatusCheck", jsonData, payload.toString(), esewaResponse.toString(), esewaResponse.toString(), core_identifier,Customer_id,username);
			return responseJson;
		}
	}
	
	public static boolean recordApiAuditCheckStatusLog(DataControllerRequest request, String ApiName,
			String PlainPayload,
			String EncryptedRequest,
			String EncryptedResponse,
			String ValidatedResponse, String coreIdentifier, String customerID, String userSignOnName)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();

			logger.debug("recordApiAuditLog ApiName" + ApiName);
			logger.debug("recordApiAuditLog PlainPayload" + PlainPayload);
			logger.debug("recordApiAuditLog EncryptedRequest" + EncryptedRequest);
			logger.debug("recordApiAuditLog EncryptedResponse" + EncryptedResponse);
			logger.debug("recordApiAuditLog ValidatedResponse" + ValidatedResponse);

			inputParams.put("id", id);
			inputParams.put("Customer_id", customerID);
			inputParams.put("username", userSignOnName);
			inputParams.put("core_identifier", coreIdentifier);
			inputParams.put("ApiName", ApiName);
			inputParams.put("PlainPayload", PlainPayload);
			inputParams.put("EncryptedRequest", EncryptedRequest);
			inputParams.put("EncryptedResponse", EncryptedResponse);
			inputParams.put("ValidatedResponse", ValidatedResponse);
			
			inputParams.put("createdby", getTimestamp());
			inputParams.put("modifiedby", coreIdentifier);
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::recordApiAuditLog: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.ESEWA_API_LOG_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::recordApiAuditLog: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::recordApiAuditLog failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::recordApiAuditLog success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create recordApiAuditLog");
			return false;
		}

		return isinvalidattemptEntryMade;
	}

//Check Transaction Status request model
	public static class TransactionStatusRequest {
		public String client_id;
		public String[] originating_unique_id_list;

		public TransactionStatusRequest(String clientId, String[] uniqueIdList) {
			this.client_id = clientId;
			this.originating_unique_id_list = uniqueIdList;
		}

		public String toJson() {
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"client_id\":\"").append(client_id).append("\",");
			json.append("\"originating_unique_id_list\":[");

			for (int i = 0; i < originating_unique_id_list.length; i++) {
				if (i > 0)
					json.append(",");
				json.append("\"").append(originating_unique_id_list[i]).append("\"");
			}

			json.append("]");
			json.append("}");
			return json.toString();
		}
	}

	// Payment request model
	public static class PaymentRequest {
		public String client_id;
		public String batch_id;
		public String swift_code;
		public EsewaLoadRequest[] esewa_load_requests;

		public PaymentRequest(String clientId, String batchId, String swiftCode, EsewaLoadRequest[] loadRequests) {
			this.client_id = clientId;
			this.batch_id = batchId;
			this.swift_code = swiftCode;
			this.esewa_load_requests = loadRequests;
		}

		public String toJson() {
			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"client_id\":\"").append(client_id).append("\",");
			json.append("\"batch_id\":\"").append(batch_id).append("\",");
			json.append("\"swift_code\":\"").append(swift_code).append("\",");
			json.append("\"esewa_load_requests\":[");

			for (int i = 0; i < esewa_load_requests.length; i++) {
				if (i > 0)
					json.append(",");
				json.append(esewa_load_requests[i].toJson());
			}

			json.append("]");
			json.append("}");
			return json.toString();
		}
	}

	// Esewa Load Request model
	public static class EsewaLoadRequest {
		public String amount;
		public String esewa_id;
		public String originating_unique_id;
		public InitiatorDetails initiator_details;

		public EsewaLoadRequest(String amount, String esewaId, String originatingUniqueId,
				InitiatorDetails initiatorDetails) {
			this.amount = amount;
			this.esewa_id = esewaId;
			this.originating_unique_id = originatingUniqueId;
			this.initiator_details = initiatorDetails;
		}

		public String toJson() {
			return String.format(
					"{\"amount\":\"%s\",\"esewa_id\":\"%s\",\"originating_unique_id\":\"%s\",\"initiator_details\":%s}",
					amount, esewa_id, originating_unique_id, initiator_details.toJson());
		}
	}

	// Initiator Details model
	public static class InitiatorDetails {
		public String TCMAP0034; // Account number
		public String TCMAP0035; // Account name
		public String TCMAP0036; // Additional field 1
		public String TCMAP0037; // Additional field 2
		public String TCMAP0041; // Payment description

		public InitiatorDetails(String accountNumber, String accountName, String field1, String field2,
				String paymentDescription) {
			this.TCMAP0034 = accountNumber;
			this.TCMAP0035 = accountName;
			this.TCMAP0036 = field1;
			this.TCMAP0037 = field2;
			this.TCMAP0041 = paymentDescription;
		}

		public String toJson() {
			return String.format(
					"{\"TCMAP0034\":\"%s\",\"TCMAP0035\":\"%s\",\"TCMAP0036\":\"%s\",\"TCMAP0037\":\"%s\",\"TCMAP0041\":\"%s\"}",
					TCMAP0034, TCMAP0035, TCMAP0036, TCMAP0037, TCMAP0041);
		}
	}

	// Send POST request with HttpClient
	private static String sendPostRequest(String url, Map<String, String> payload) throws Exception {
		HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30))
				.version(HttpClient.Version.HTTP_1_1).build();

		// Convert payload to JSON
		String jsonPayload = String.format(
				"{\"client_id\":\"%s\",\"secret_key\":\"%s\",\"data\":\"%s\",\"signature\":\"%s\"}",
				payload.get("client_id"), payload.get("secret_key"), payload.get("data"), payload.get("signature"));

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(30))
				.header("Content-Type", "application/json").header("Accept", "application/json")
				.header("User-Agent", "EsewaValidationClient/1.0").POST(BodyPublishers.ofString(jsonPayload)).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// Check response status
		if (response.statusCode() != 200) {
			throw new RuntimeException("HTTP error code: " + response.statusCode() + " - " + response.body());
		}

		logger.error("Response status: " + response.statusCode());
		return response.body();
	}

	// Parse JSON response
	private static EsewaLoadResponse parseResponse(String jsonResponse) {
		EsewaLoadResponse response = new EsewaLoadResponse();

		// Simple JSON parsing (in real application, use Jackson/Gson)
		if (jsonResponse.contains("\"data\":")) {
			response.data = extractJsonField(jsonResponse, "data");
		}
		if (jsonResponse.contains("\"secret_key\":")) {
			response.secret_key = extractJsonField(jsonResponse, "secret_key");
		}
		if (jsonResponse.contains("\"signature\":")) {
			response.signature = extractJsonField(jsonResponse, "signature");
		}
		if (jsonResponse.contains("\"code\":")) {
			response.code = extractJsonField(jsonResponse, "code");
		}
		if (jsonResponse.contains("\"message\":")) {
			response.message = extractJsonField(jsonResponse, "message");
		}

		return response;
	}

	// Helper method to extract field from JSON
	private static String extractJsonField(String json, String fieldName) {
		try {
			String searchPattern = "\"" + fieldName + "\":\"";
			int startIndex = json.indexOf(searchPattern) + searchPattern.length();
			int endIndex = json.indexOf("\"", startIndex);
			return json.substring(startIndex, endIndex);
		} catch (Exception e) {
			return null;
		}
	}

	// Generate 32-byte AES key
	private static SecretKey generateAESKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		return keyGen.generateKey();
	}

	// AES-256-ECB encryption
	private static String encryptAES(String data, SecretKey secretKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	// RSA encryption
	private static String encryptRSA(String data, String publicKeyStr) throws Exception {
		byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	// RSA decryption
	private static String decryptRSA(String encryptedData, String privateKeyStr) throws Exception {
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedBytes, "UTF-8");
	}

	// Sign data with RSA private key
	private static String signData(String data, String privateKeyStr) throws Exception {
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(data.getBytes("UTF-8"));
		byte[] signatureBytes = signature.sign();
		return Base64.getEncoder().encodeToString(signatureBytes);
	}

	// Verify signature with RSA public key
	private static boolean verifySignature(String data, String signatureStr, String publicKeyStr) throws Exception {
		byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(data.getBytes("UTF-8"));
		return signature.verify(Base64.getDecoder().decode(signatureStr));
	}

	// AES decryption
	private static String decryptAES(String encryptedData, String secretKeyBase64) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);
		SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedBytes, "UTF-8");
	}

	// Process eSewa response
	public static String processEsewaResponse(String encryptedData, String encryptedSecretKey, String signature,
			String serverPublicKey, String clientPrivateKey) throws Exception {

		// Step 9a: Decrypt secret_key using client's private key
		String decryptedSecretKey = decryptRSA(encryptedSecretKey, clientPrivateKey);

		// Step 9b: Verify response data with server's public key
		boolean isSignatureValid = verifySignature(encryptedData, signature, serverPublicKey);

		if (!isSignatureValid) {
			throw new SecurityException("Invalid signature from server");
		}

		// Step 9c: Decrypt data using AES-256-ECB
		String decryptedData = decryptAES(encryptedData, decryptedSecretKey);

		return decryptedData;
	}
	
	public static boolean recordApiAuditLog(DataControllerRequest request, String ApiName,
			String PlainPayload,
			String EncryptedRequest,
			String EncryptedResponse,
			String ValidatedResponse)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			ServicesManager servicesManager;
			servicesManager = request.getServicesManager();
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName");

			logger.debug("recordApiAuditLog ApiName" + ApiName);
			logger.debug("recordApiAuditLog PlainPayload" + PlainPayload);
			logger.debug("recordApiAuditLog EncryptedRequest" + EncryptedRequest);
			logger.debug("recordApiAuditLog EncryptedResponse" + EncryptedResponse);
			logger.debug("recordApiAuditLog ValidatedResponse" + ValidatedResponse);

			inputParams.put("id", id);
			inputParams.put("Customer_id", customerID);
			inputParams.put("username", userSignOnName);
			inputParams.put("core_identifier", coreIdentifier);
			inputParams.put("ApiName", ApiName);
			inputParams.put("PlainPayload", PlainPayload);
			inputParams.put("EncryptedRequest", EncryptedRequest);
			inputParams.put("EncryptedResponse", EncryptedResponse);
			inputParams.put("ValidatedResponse", ValidatedResponse);
			
			inputParams.put("createdby", getTimestamp());
			inputParams.put("modifiedby", coreIdentifier);
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::recordApiAuditLog: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.ESEWA_API_LOG_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::recordApiAuditLog: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::recordApiAuditLog failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::recordApiAuditLog success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create recordApiAuditLog");
			return false;
		}

		return isinvalidattemptEntryMade;
	}

	
	public static boolean esewaTransactionLog(DataControllerRequest request, String BatchId,
			String SwiftCode,
			String EsewaId,
			String EsewaReceiverName,
			String Amount,
			String OriginatingUniqueId,
			String Status,
			String ResponseCode,
			String StatusCode,
			String RawResponse,
			String SourceAccountNo,
			String SenderName,
			String SenderMobileNo,
			String SenderAddress,
			String TransactionPurpose,
			String TransactionStatus,
			String TransactionDetailOriginatingUniqueId,
			String statuscheck,
			String channel,
			String Fee)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			ServicesManager servicesManager;
			servicesManager = request.getServicesManager();
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName");
			
			logger.debug("BCT::esewaTransactionLog: channel param:" + channel);
			
			inputParams.put("id", id);
			inputParams.put("Customer_id", customerID);
			inputParams.put("username", userSignOnName);
			inputParams.put("core_identifier", coreIdentifier);
			inputParams.put("BatchId", BatchId);
			inputParams.put("SwiftCode", SwiftCode);
			inputParams.put("EsewaId", EsewaId);
			inputParams.put("EsewaReceiverName", EsewaReceiverName);
			inputParams.put("Amount", Amount);
			inputParams.put("OriginatingUniqueId", OriginatingUniqueId);
			inputParams.put("Status", Status);
			inputParams.put("Fee", Fee);
			inputParams.put("ResponseCode", ResponseCode);
			inputParams.put("StatusCode", StatusCode);
			inputParams.put("RawResponse", RawResponse);
			inputParams.put("SourceAccountNo", SourceAccountNo);
			inputParams.put("SenderName", SenderName);
			inputParams.put("SenderMobileNo", SenderMobileNo);
			inputParams.put("SenderAddress", SenderAddress);
			inputParams.put("TransactionPurpose", TransactionPurpose);
			inputParams.put("TransactionStatus", TransactionStatus);
			inputParams.put("TransactionDetailOriginatingUniqueId", TransactionDetailOriginatingUniqueId);
			inputParams.put("statuscheck", statuscheck);
			inputParams.put("channelName", channel);
			inputParams.put("TransactionDate", getTimestamp());
			inputParams.put("createdby", getTimestamp());
			inputParams.put("modifiedby", coreIdentifier);
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::esewaTransactionLog: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.ESEWA_TRANSACTION_LOG_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::esewaTransactionLog: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::esewaTransactionLog failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::esewaTransactionLog success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create esewaTransactionLog");
			return false;
		}

		return isinvalidattemptEntryMade;
	}
	
	
	public static boolean esewaTransactionPendingLog(DataControllerRequest request, String BatchId,
			String SwiftCode,
			String EsewaId,
			String EsewaReceiverName,
			String Amount,
			String OriginatingUniqueId,
			String Status,
			String ResponseCode,
			String StatusCode,
			String RawResponse,
			String SourceAccountNo,
			String SenderName,
			String SenderMobileNo,
			String SenderAddress,
			String TransactionPurpose,
			String TransactionStatus,
			String TransactionDetailOriginatingUniqueId,
			String statuscheck,
			String channel,
			String Fee)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			ServicesManager servicesManager;
			servicesManager = request.getServicesManager();
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName");
			
			logger.debug("BCT::esewaTransactionPendingLog: channel param:" + channel);
			
			inputParams.put("id", id);
			inputParams.put("Customer_id", customerID);
			inputParams.put("username", userSignOnName);
			inputParams.put("core_identifier", coreIdentifier);
			inputParams.put("BatchId", BatchId);
			inputParams.put("SwiftCode", SwiftCode);
			inputParams.put("EsewaId", EsewaId);
			inputParams.put("EsewaReceiverName", EsewaReceiverName);
			inputParams.put("Amount", Amount);
			inputParams.put("OriginatingUniqueId", OriginatingUniqueId);
			inputParams.put("Status", Status);
			inputParams.put("Fee", Fee);
			inputParams.put("ResponseCode", ResponseCode);
			inputParams.put("StatusCode", StatusCode);
			inputParams.put("RawResponse", RawResponse);
			inputParams.put("SourceAccountNo", SourceAccountNo);
			inputParams.put("SenderName", SenderName);
			inputParams.put("SenderMobileNo", SenderMobileNo);
			inputParams.put("SenderAddress", SenderAddress);
			inputParams.put("TransactionPurpose", TransactionPurpose);
			inputParams.put("TransactionStatus", TransactionStatus);
			inputParams.put("TransactionDetailOriginatingUniqueId", TransactionDetailOriginatingUniqueId);
			inputParams.put("statuscheck", statuscheck);
			inputParams.put("channelName", channel);
			inputParams.put("TransactionDate", getTimestamp());
			inputParams.put("createdby", getTimestamp());
			inputParams.put("modifiedby", coreIdentifier);
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::esewaTransactionPendingLog: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.ESEWA_TRANSACTION_PENDING_LOG_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::esewaTransactionPendingLog: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::esewaTransactionPendingLog failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::esewaTransactionPendingLog success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create esewaTransactionLog");
			return false;
		}

		return isinvalidattemptEntryMade;
	}
	
    public static String getTimestamp() {
		String localDateTime;
		if (LocalDateTime.now().getSecond() == 0) {
			localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
		} else {
			localDateTime = LocalDateTime.now().withNano(0).toString();
		}
		return localDateTime;
	}
}
