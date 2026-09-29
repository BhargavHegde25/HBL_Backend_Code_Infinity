package com.kony.dbputilities.demoservices;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
/**
 * AES Algo.
 * 
 * @author KH2167
 *
 */
public class CryptoText {

	private static final String ALGO = "AES/GCM/PKCS5Padding";
	private static final char[] keyValue = new char[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't',
			'K', 'e', 'y' };

	
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
	
	
	/**
	 * Encrypt a string with AES algorithm.
	 *
	 * @param data is a string
	 * @return the encrypted string
	 * @throws Exception
	 */
	public static String encrypt(String data) throws Exception {
		byte[] stretchedKey = null;
		byte[] ivKey = null;
		byte[] SALT = getSalt();
		try {
			stretchedKey = stretchPassword(keyValue, SALT, 10000, 128);
			ivKey = stretchPassword(new String(stretchedKey, StandardCharsets.UTF_8).toCharArray(), SALT, 10000, 128);
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, SALT));
			byte[] encVal = c.doFinal(data.getBytes(StandardCharsets.UTF_8));
			return Base64.encodeBase64URLSafeString(encVal);
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

	/**
	 * Decrypt a string with AES algorithm.
	 *
	 * @param encryptedData is a string
	 * @return the decrypted string
	 */
	public static String decrypt(String encryptedData) throws Exception {
		byte[] stretchedKey = null;
		byte[] ivKey = null;
		byte[] SALT = getSalt();
		try {
			stretchedKey = stretchPassword(keyValue, SALT, 10000, 128);
			ivKey = stretchPassword(new String(stretchedKey, StandardCharsets.UTF_8).toCharArray(), SALT, 10000, 128);
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, SALT));
			byte[] encVal = c.doFinal(encryptedData.getBytes(StandardCharsets.UTF_8));
			return Base64.encodeBase64URLSafeString(encVal);
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

	/**
	 * Generate a new encryption key.
	 */
	private static Key generateKey() throws Exception {
		byte[] SALT = getSalt();
		return new SecretKeySpec(SALT, ALGO);
	}
}
