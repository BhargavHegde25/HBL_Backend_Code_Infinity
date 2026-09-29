package com.kony.dbputilities.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class AESEncyptor {
	

	private static String algo = "AES";
	private static String mode = "CTR";
	private static String padding = "NoPadding";

	
	private static final String DEFAULT_ENCRYPTED_KEY = "1234567890123456";
	private static final String E2E_ENCRYPTION_KEY = "E2E_ENCRYPTION_KEY";
	private static final String E2E_ENCRYPTION_VECTOR = "E2E_ENCRYPTION_VECTOR";

	public static String encrypt(String key, String iv, String plainText) throws Exception {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
		byte[] dataBytes = plainText.getBytes(StandardCharsets.UTF_8);

		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algo);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance(algo + "/" + mode + "/" + padding);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

		byte[] encryptedBytes = cipher.doFinal(dataBytes);
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String key, String iv, String cipherText) throws Exception {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);

		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance(algo + "/" + mode + "/" + padding);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	@SuppressWarnings("deprecation")
	public static Result encryptJsonStringToResult(String jsonString) throws Exception {
		JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
		JsonObject payload = json.getAsJsonObject();
		String privateKey = getSecretKey();
		String privateIv = getSecretIV();
		JsonArray records = json.getAsJsonObject().getAsJsonArray("user");
		for (int j = 0; j < records.size(); j++) {
			JsonObject rec = records.get(j).getAsJsonObject();
			for (String key : rec.keySet()) {
				if ("Ssn".equalsIgnoreCase(key) || "DateOfBirth".equalsIgnoreCase(key)
						|| "phone".equalsIgnoreCase(key) || "email".equalsIgnoreCase(key)) {
					String value = rec.get(key).getAsString();
					value = encrypt(privateKey, privateIv, value);
					rec.addProperty(key, value);
				} else if ("Addresses".equalsIgnoreCase(key) || "ContactNumbers".equalsIgnoreCase(key)
						|| "EmailIds".equalsIgnoreCase(key)) {
					JsonArray array = rec.get(key).getAsJsonArray();
					encryptArray(privateKey, privateIv, array);
				}
			}
		}
		return JSONToResult.convert(json.toString());
	}
	
	public static void main(String args[]) {
		String key = "1234567890123456";
		String iv = "1234567890123456";
		String data = "Hello, world!";
		try {
			String encryptedData = encrypt(key, iv, data);
			System.out.println(encryptedData);
			String decryptedData = decrypt(key, iv, encryptedData);
			System.out.println(decryptedData);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static JsonObject decryptJsonObject(JsonObject response) throws Exception {
		String privateKey = getSecretKey();
		String privateIv = getSecretIV();
		JsonArray records = response.getAsJsonArray("records");
		for (int j = 0; j < records.size(); j++) {
			JsonObject rec = records.get(j).getAsJsonObject();
			for (String key : rec.keySet()) {
				if ("Ssn".equalsIgnoreCase(key) || "DateOfBirth".equalsIgnoreCase(key)
						|| "phone".equalsIgnoreCase(key) || "email".equalsIgnoreCase(key)) {
					String value = rec.get(key).getAsString();
					value = decrypt(privateKey, privateIv, value);
					rec.addProperty(key, value);
				} else if ("Addresses".equalsIgnoreCase(key) || "ContactNumbers".equalsIgnoreCase(key)
						|| "EmailIds".equalsIgnoreCase(key)) {
					JsonArray array = rec.get(key).getAsJsonArray();
					decryptArray(privateKey, privateIv, array);
				}
			}
		}
		return response;
	}
	
	private static String getSecretKey() {
		String encryptionKey = "";
		try {
			encryptionKey = EnvironmentConfigurationsHandler.getClientAppProperty(E2E_ENCRYPTION_KEY);
		} catch (Exception e) {
			encryptionKey = DEFAULT_ENCRYPTED_KEY;
		}
		if(StringUtils.isBlank(encryptionKey)) {
			encryptionKey = DEFAULT_ENCRYPTED_KEY;
		}
		return encryptionKey;
	}

	private static String getSecretIV() {
		String encryptionVector = "";
		try {
			encryptionVector = EnvironmentConfigurationsHandler.getClientAppProperty(E2E_ENCRYPTION_VECTOR);
		} catch (Exception e) {
			encryptionVector = DEFAULT_ENCRYPTED_KEY;
		}
		if(StringUtils.isBlank(encryptionVector)) {
			encryptionVector = DEFAULT_ENCRYPTED_KEY;
		}
		return encryptionVector;
	}
	
	private static void encryptArray(String key, String iv, JsonArray array) throws Exception {
		for (JsonElement elem : array) {
			JsonObject elemObj = elem.getAsJsonObject();
			for (String innerKey : elemObj.keySet()) {
				String value = elemObj.get(innerKey).getAsString();
				value = encrypt(key, iv, value);
				if (value != null) {
					elemObj.addProperty(innerKey, value);
				}
			}
		}
	}
	private static void decryptArray(String key, String iv, JsonArray array) throws Exception {
		for (JsonElement elem : array) {
			JsonObject elemObj = elem.getAsJsonObject();
			for (String innerKey : elemObj.keySet()) {
				String value = elemObj.get(innerKey).getAsString();
				value = decrypt(key, iv, value);
				if (value != null) {
					elemObj.addProperty(innerKey, value);
				}
			}
		}
	}
	
	public static JsonArray decryptArray(JsonArray array) throws Exception {
		String privateKey = getSecretKey();
		String privateIv = getSecretIV();
		for (JsonElement elem : array) {
			JsonObject elemObj = elem.getAsJsonObject();
			for (String innerKey : elemObj.keySet()) {
				String value = elemObj.get(innerKey).getAsString();
				value = decrypt(privateKey, privateIv, value);
				if (value != null) {
					elemObj.addProperty(innerKey, value);
				}
			}
		}
		return array;
	}
}
