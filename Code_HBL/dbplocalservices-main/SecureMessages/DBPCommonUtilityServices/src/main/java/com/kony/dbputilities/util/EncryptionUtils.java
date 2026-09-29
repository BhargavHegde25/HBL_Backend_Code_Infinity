package com.kony.dbputilities.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

import org.apache.commons.codec.binary.Base64;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;


/**
 * Crypto related utilities
 * 
 * @author Venkateswara Rao Alla
 *
 */

public class EncryptionUtils {

    private static final int ITERATION_COUNT = 10000;
    private static final int KEY_LENGTH_IN_BITS = 128;
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
    
    /**
     * Encrypt the provided text using symmetric encryption technique and return it
     * 
     * @param text
     *            text to encrypt
     * @param key
     *            encryption key
     * @return
     * @throws Exception
     */
    public static String encrypt(String text, String key) throws Exception {
        byte[] stretchedKey = null;
        byte[] ivKey = null;
        byte[] SALT = getSalt();
        try {
            stretchedKey = stretchPassword(key.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH_IN_BITS);
            ivKey = stretchPassword(new String(stretchedKey, StandardCharsets.UTF_8).toCharArray(), SALT,
                    ITERATION_COUNT, KEY_LENGTH_IN_BITS);
            final Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(stretchedKey, "AES"), new GCMParameterSpec(TAG_LENGTH,ivKey));
            byte[] encodedValue = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64URLSafeString(encodedValue);
        } catch (Exception e) {
            throw e;
        } finally {
            if (stretchedKey != null && stretchedKey.length > 0) {
                Arrays.fill(stretchedKey, (byte) 0);
            }

            if (ivKey != null && ivKey.length > 0) {
                Arrays.fill(ivKey, (byte) 0);
            }
        }
    }

    /**
     * Decrypt the provided text and return it
     * 
     * @param text
     *            encrypted text to decrypt
     * @param key
     *            the encryption key which is used for encryption
     * @return
     * @throws Exception
     */
    public static String decrypt(String text, String key) throws Exception {
        byte[] stretchedKey = null;
        byte[] ivKey = null;
        byte[] SALT = getSalt();
        try {
            stretchedKey = stretchPassword(key.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH_IN_BITS);
            ivKey = stretchPassword(new String(stretchedKey, StandardCharsets.UTF_8).toCharArray(), SALT,
                    ITERATION_COUNT, KEY_LENGTH_IN_BITS);
            final Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(stretchedKey, "AES"), new GCMParameterSpec(TAG_LENGTH,ivKey));
            byte[] decodedValue = cipher.doFinal(Base64.decodeBase64(text));
            return new String(decodedValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw e;
        } finally {
            if (stretchedKey != null && stretchedKey.length > 0) {
                Arrays.fill(stretchedKey, (byte) 0);
            }

            if (ivKey != null && ivKey.length > 0) {
                Arrays.fill(ivKey, (byte) 0);
            }
        }
    }

    /**
     * Stretch the provided password
     * 
     * @param password
     * @param salt
     * @param iterations
     * @param keyLength
     * @return
     * @throws Exception
     * @see <a href="https://www.owasp.org/index.php/Hashing_Java">OWASP Key Stretching</a>
     */
    public static byte[] stretchPassword(final char[] password, final byte[] salt, final int iterations,
            final int keyLength) throws Exception {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey secretKey = skf.generateSecret(spec);
            byte[] res = secretKey.getEncoded();
            return res;
        } catch (Exception e) {
            throw e;
        }
    }
}
