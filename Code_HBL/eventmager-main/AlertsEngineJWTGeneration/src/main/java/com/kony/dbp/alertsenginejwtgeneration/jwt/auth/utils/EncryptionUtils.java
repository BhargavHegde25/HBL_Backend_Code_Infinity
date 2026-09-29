package com.kony.dbp.alertsenginejwtgeneration.jwt.auth.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class EncryptionUtils {
	
	
	private static final int TAG_LENGTH = 128;
	
	 /**
     * Returns the salt by fetching it from Fabric runtime params
     */
    private static byte[] getSalt() throws Exception {
    	try {
    		byte[] decodedBytes = Base64.decodeBase64(EnvironmentConfigurationsHandler.getServerAppProperty("COMMON_ENCRYPTION_DECRYPTION_SALT"));
    		String decodedString = new String(decodedBytes);	
    		byte[] salt = new byte[8];
    		String[] obj = decodedString.split(",");
    		for(int i=0;i<obj.length;i++) {
    			salt[i] = Byte.parseByte(obj[i]);
    		}
    		return salt;
    	}catch(Exception e) {
    		throw e;
    	}
    }
    
	public static String encrypt(String text, String key) throws Exception {
		byte[] stretchedKey = null;
		byte[] ivKey = null;
		byte[] SALT = getSalt();
		try {
			stretchedKey = stretchPassword(key.toCharArray(), SALT, 10000, 128);
			ivKey = stretchPassword(new String(stretchedKey, StandardCharsets.UTF_8).toCharArray(), SALT, 10000, 128);

			Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
			cipher.init(1, new SecretKeySpec(stretchedKey, "AES"), new GCMParameterSpec(TAG_LENGTH,ivKey));
			byte[] encodedValue = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
			return Base64.encodeBase64URLSafeString(encodedValue);
		} catch (Exception e) {
			throw e;
		} finally {
			if ((stretchedKey != null) && (stretchedKey.length > 0)) {
				Arrays.fill(stretchedKey, (byte) 0);
			}
			if ((ivKey != null) && (ivKey.length > 0)) {
				Arrays.fill(ivKey, (byte) 0);
			}
		}
	}
	
	public static String decrypt(String text, String key) throws Exception {
		byte[] stretchedKey = null;
		byte[] ivKey = null;
		byte[] SALT = getSalt();
		try {
			stretchedKey = stretchPassword(key.toCharArray(), SALT, 10000, 128);
			ivKey = stretchPassword(new String(stretchedKey, StandardCharsets.UTF_8).toCharArray(), SALT, 10000, 128);

			Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
			cipher.init(2, new SecretKeySpec(stretchedKey, "AES"), new GCMParameterSpec(TAG_LENGTH,ivKey));
			byte[] decodedValue = cipher.doFinal(Base64.decodeBase64(text));
			return new String(decodedValue, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw e;
		} finally {
			if ((stretchedKey != null) && (stretchedKey.length > 0)) {
				Arrays.fill(stretchedKey, (byte) 0);
			}
			if ((ivKey != null) && (ivKey.length > 0)) {
				Arrays.fill(ivKey, (byte) 0);
			}
		}
	}

	public static byte[] stretchPassword(char[] password, byte[] salt, int iterations, int keyLength) throws Exception {
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
			SecretKey secretKey = skf.generateSecret(spec);
			return secretKey.getEncoded();
		} catch (Exception e) {
			throw e;
		}
	}
	
}