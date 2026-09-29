package com.temenos.infinity.api.visaintegrationapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.Map.Entry;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.HTTPOperations.operations;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.visaintegrationapi.config.VisaIntegrationApiAPIServices;
import com.temenos.infinity.api.visaintegrationapi.constants.*;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.util.IOUtils;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p>
 * Class to load and access values of transactiontype.properties file
 * </p>
 * 
 * @author Venkat
 *
 */
public class VisaIntegrationUtils {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public VisaIntegrationUtils() {
		// Private Constructor
	}

	public static Properties loadProps(String fileName) {
		Properties properties = new Properties();
		try (InputStream inputStream = VisaIntegrationUtils.class.getClassLoader()
				.getResourceAsStream(fileName + ".properties")) {
			properties.load(inputStream);
			return properties;
		} catch (Exception e) {
			alert.prepareError("Error while loading properties", e).log();
		}
		return properties;
	}

	public static String getXpayToken(VisaDTO visaDTO) {

		final String queryParams = "apikey=" + getVisaAPIKey();
		String resourcePath = null;
		String requestBody = null;

		if (visaDTO.getOperation().contentEquals(VisaIntegrationConstants.PARAM_OPERATION_LINKCARD)) {

			requestBody = getRequestBodyForLinkingCard(visaDTO);

			if (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_SAMSUNGPAY)) {
				resourcePath = VisaIntegrationConstants.PARAM_SAMSUNGPAY_RESOURCEPATH;

			} else if (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_APPLEPAY)) {
				resourcePath = VisaIntegrationConstants.PARAM_APPLEPAY_RESOURCEPATH;
			}
			if (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_GOOGLEPAY)) {
				resourcePath = VisaIntegrationConstants.PARAM_GOOGLEPAY_RESOURCEPATH;
			}
		}

		if (visaDTO.getOperation().contentEquals(VisaIntegrationConstants.PARAM_OPERATION_ENROLLCARD)) {
			resourcePath = VisaIntegrationConstants.PARAM_ENROLLCARD_RESOURCEPATH;
			requestBody = visaDTO.getEncCard();
		}

		final String sharedSecret = VisaIntegrationConstants.PARAM_SHAREDSECRET;
		String timestamp = timeStamp();
		String beforeHash = timestamp + resourcePath + queryParams + requestBody;
		String hash = null;
		try {
			hash = hmacSha256Digest(beforeHash, sharedSecret);
		} catch (SignatureException e) {
			alert.prepareError(e.toString()).log();
		}
		String token = "xv2:" + timestamp + ":" + hash;
		return (token);

	}

	private static String getVisaAPIKey() {
		// TODO Auto-generated method stub
		return URLFinder.getServerRuntimeProperty(VisaIntegrationConstants.VISA_API_KEY);
	}

	private static String timeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000L);
	}

	private static String hmacSha256Digest(String data, String sharedSecret) throws SignatureException {
		return getDigest("HmacSHA256", sharedSecret, data, true);
	}

	private static String getDigest(String algorithm, String sharedSecret, String data, boolean toLower)
			throws SignatureException {
		try {
			Mac sha256HMAC = Mac.getInstance(algorithm);
			SecretKeySpec secretKey = new SecretKeySpec(sharedSecret.getBytes(StandardCharsets.UTF_8), algorithm);
			sha256HMAC.init(secretKey);

			byte[] hashByte = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
			String hashString = toHex(hashByte);

			return toLower ? hashString.toLowerCase() : hashString;
		} catch (Exception e) {
			throw new SignatureException(e);
		}
	}

	private static String toHex(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi);
	}

	public static String getRequestBodyForLinkingCard(VisaDTO visaDTO) {
		JSONObject requestBody = new JSONObject();

		if (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_SAMSUNGPAY)
				|| visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_GOOGLEPAY)) {

			if (StringUtils.isNotBlank(visaDTO.getPaymentAccountReference())) {
				requestBody.put("paymentAccountReference", visaDTO.getPaymentAccountReference());
			}
			requestBody.put("vCardID", visaDTO.getCardID());
			requestBody.put("deviceID", visaDTO.getDeviceID());
			requestBody.put("clientCustomerID", visaDTO.getClientCustomerId());
		}

		if (visaDTO.getPaymentService().contentEquals(VisaIntegrationConstants.PARAM_APPLEPAY)) {

			if (StringUtils.isNotBlank(visaDTO.getPaymentAccountReference())) {
				requestBody.put("paymentAccountReference", visaDTO.getPaymentAccountReference());
			}
			requestBody.put("vCardID", visaDTO.getCardID());
			requestBody.put("deviceCert", visaDTO.getDeviceCert());
			requestBody.put("nonceSignature", visaDTO.getNonceSignature());
			requestBody.put("nonce", visaDTO.getNonce());
		}
		return requestBody.toString();
	}

	public static String getRequestBodyForEnrollCard(VisaDTO visaDTO) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("encCard", visaDTO.getEncCard());
		return requestBody.toString();
	}

	public static String getRequestBodyForEncryption(VisaDTO visaDTO) {
		JSONObject requestBody = new JSONObject();

		if (StringUtils.isNotBlank(visaDTO.getAccountNumber())) {
			requestBody.put("accountNumber", visaDTO.getAccountNumber());
		}
		if (StringUtils.isNotBlank(visaDTO.getNameOnCard())) {
			requestBody.put("nameOnCard", visaDTO.getNameOnCard());
		}
		if (StringUtils.isNotBlank(visaDTO.getCvv2())) {
			requestBody.put("cvv2", visaDTO.getCvv2());
		}
		if (StringUtils.isNotBlank(visaDTO.getClientCustomerId())) {
			requestBody.put("clientCustomerId", visaDTO.getClientCustomerId());
		}
		if (StringUtils.isNotBlank(visaDTO.getvCustomerIDForPartner())) {
			requestBody.put("vCustomerIDForPartner", visaDTO.getvCustomerIDForPartner());
		}
		if (StringUtils.isNotBlank(visaDTO.getvClientIDForPartner())) {
			requestBody.put("vClientIDForPartner", visaDTO.getvClientIDForPartner());
		}
		if (StringUtils.isNotBlank(visaDTO.getPaymentAccountReference())) {
			requestBody.put("paymentAccountReference", visaDTO.getPaymentAccountReference());
		}

		JSONObject expirationDate = new JSONObject();
		if (StringUtils.isNotBlank(visaDTO.getExpiryMonth())) {
			expirationDate.put("month", visaDTO.getExpiryMonth());
		}
		if (StringUtils.isNotBlank(visaDTO.getExpiryYear())) {
			expirationDate.put("year", visaDTO.getExpiryYear());
		}
		requestBody.put("expirationDate", expirationDate);

		JSONObject billingAddress = new JSONObject();
		if (StringUtils.isNotBlank(visaDTO.getName())) {
			billingAddress.put("name", visaDTO.getName());
		}
		if (StringUtils.isNotBlank(visaDTO.getAddrLine1())) {
			billingAddress.put("line1", visaDTO.getAddrLine1());
		}
		if (StringUtils.isNotBlank(visaDTO.getAddrLine2())) {
			billingAddress.put("line2", visaDTO.getAddrLine2());
		}
		if (StringUtils.isNotBlank(visaDTO.getAddrLine3())) {
			billingAddress.put("line3", visaDTO.getAddrLine3());
		}
		if (StringUtils.isNotBlank(visaDTO.getCity())) {
			billingAddress.put("city", visaDTO.getCity());
		}
		if (StringUtils.isNotBlank(visaDTO.getState())) {
			billingAddress.put("state", visaDTO.getState());
		}
		if (StringUtils.isNotBlank(visaDTO.getCountryCode())) {
			billingAddress.put("countryCode", visaDTO.getCountryCode());
		}
		if (StringUtils.isNotBlank(visaDTO.getPostalCode())) {
			billingAddress.put("postalCode", visaDTO.getPostalCode());
		}

		requestBody.put("billingAddress", billingAddress);

//		ObjectMapper mapper = new ObjectMapper();
//		Object json = null;
//		String body = null;
//		try {
//			json = mapper.readValue(requestBody.toString(), Object.class);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			alert.prepareError(e1).log();
//		}
//		try {
//			body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//		} catch (JsonProcessingException e1) {
//			// TODO Auto-generated catch block
//			alert.prepareError(e1).log();
//		}
		return requestBody.toString();
	}

	public static String getEncryptedPayload(String mleServerPublicCertificatePath, String requestPayload, String keyId)
			throws CertificateException, JOSEException, IOException {
		JWEHeader.Builder headerBuilder = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
		headerBuilder.keyID(keyId);
		headerBuilder.customParam("iat", System.currentTimeMillis());
		JWEObject jweObject = new JWEObject(headerBuilder.build(), new Payload(requestPayload));
		jweObject.encrypt(new RSAEncrypter(getRSAPublicKey(mleServerPublicCertificatePath)));
		return "{\"encCard\":\"" + jweObject.serialize() + "\"}";
	}

	private static RSAPublicKey getRSAPublicKey(final String mleServerPublicCertificatePath)
			throws CertificateException, IOException {
		final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
		final String END_CERT = "-----END CERTIFICATE-----";
		String rawString = VisaIntegrationConstants.PARAM_mleServerPublicCertificate;
		byte[] bytes = rawString.getBytes(StandardCharsets.UTF_8);

		String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
		final String pemEncodedPublicKey = utf8EncodedString;
		final com.nimbusds.jose.util.Base64 base64 = new com.nimbusds.jose.util.Base64(
				pemEncodedPublicKey.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""));
		final Certificate cf = CertificateFactory.getInstance("X.509")
				.generateCertificate(new ByteArrayInputStream(base64.decode()));
		return (RSAPublicKey) cf.getPublicKey();
	}

	public static VisaDTO setCardDetails(String maskedAccountNumber) {
		// TODO Auto-generated method stub
		String cardVisaStub = URLFinder.getServerRuntimeProperty(VisaIntegrationConstants.CARD_VISA_STUB);

		VisaDTO visaDTO = new VisaDTO();

		if (cardVisaStub.contentEquals("true")) {
			visaDTO.setAccountNumber(VisaIntegrationConstants.ACCOUNTNUMBER);
			visaDTO.setExpiryMonth(VisaIntegrationConstants.EXPIRYMONTH);
			visaDTO.setExpiryYear(VisaIntegrationConstants.EXPIRYYEAR);
			visaDTO.setCvv2(VisaIntegrationConstants.CVV);
			visaDTO.setNameOnCard(VisaIntegrationConstants.NAMEONCARD);

		} else {
			JSONObject serviceResponseJSON = getCardDetailsFromDB(maskedAccountNumber);

			JSONObject cardDetails = (JSONObject) serviceResponseJSON.getJSONArray("card").get(0);

			if (cardDetails.has("cardNumber")) {
				visaDTO.setAccountNumber(cardDetails.get("cardNumber").toString());
			}
			if (cardDetails.has("expirationDate")) {
				String expiryDate = cardDetails.get("expirationDate").toString();
				String[] parts = expiryDate.split("-");
				visaDTO.setExpiryMonth(parts[1]);
				visaDTO.setExpiryYear(parts[0]);
			}
			if (cardDetails.has("cvv")) {
				visaDTO.setCvv2(cardDetails.get("cvv").toString());
			}
			if (cardDetails.has("cardHolderName")) {
				visaDTO.setNameOnCard(cardDetails.get("cardHolderName").toString());
			}

		}
		return visaDTO;
	}

	private static JSONObject getCardDetailsFromDB(String maskedAccountNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> inputMap = new HashMap<>();
		StringBuffer queryString = new StringBuffer();
		queryString.append("Id" + " ");
		queryString.append("eq" + " ");
		queryString.append(maskedAccountNumber);

		inputMap.put("$filter", queryString.toString());
		String Response = null;
		try {
			Response = Executor.invokeService(VisaIntegrationApiAPIServices.GETCARDDETAILS, inputMap, null);
			diagnostic.prepareDebug("Card Details" + Response).log();
		} catch (Exception e1) {
			alert.prepareError("Service call to dbxdb failed " + e1.toString()).log();
		}
		JSONObject serviceResponseJSON = Utilities.convertStringToJSON(Response);
		if (serviceResponseJSON == null) {
			alert.prepareError("EmptyResponse no card details available for cardID").log();
		}

		return serviceResponseJSON;

	}

	public static boolean checkUserAlreadyExists(String maskedAccountNumber, DataControllerRequest dcRequest) throws HttpCallException {

		boolean status = true;
		String cardVisaStub = URLFinder.getServerRuntimeProperty(VisaIntegrationConstants.CARD_VISA_STUB);

		if (cardVisaStub.contentEquals("true")) {
			status = true;
		} else {

			String id = HelperMethods.getCustomerIdFromSession(dcRequest);
			String filter = "User_id" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "Id"
					+ DBPUtilitiesConstants.EQUAL + maskedAccountNumber;

			Result chkResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CARD_GET);

			if (HelperMethods.hasRecords(chkResult)) {
				status = true;
			} else {

				status = false;
			}
		}

		return status;
	}
}
