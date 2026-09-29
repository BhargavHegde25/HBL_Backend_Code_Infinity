package com.kony.adminconsole.jwt.auth.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class EncryptionUtilsC360 {
	
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
	
	public static void main(String[] args) throws Exception {
		switch (args.length) {
		case 0:
			printHelp();
			return;
		case 1:
			if("-help".equalsIgnoreCase(args[0]))
			{
				printHelp();
			}
			else
				printArg0Message(args[0], true);
			return;
		case 2:
			printArg1Message(args, false);
			return;
		default:
			break;
		}
		if (args.length > 3) {
			printHelp();
			return;
		}
		String type = args[0];
		String key = args[1];
		String text = args[2];
		if (StringUtils.isNotBlank(key)) {
			if ("-e".equalsIgnoreCase(type) || "-d".equalsIgnoreCase(type)) {
				if ("-e".equalsIgnoreCase(type)) {
					if (checkKeyLength(key)) {
						System.out.println("encypted value "+encrypt(text, key));
					}
				} else {
					try {
						if (checkKeyLength(key)) {
							System.out.println("decrypted value "+decrypt(text, key));
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
					}
				}
			} else {
				System.out.println("type is not valid. valid type are -e for encrypt , -d for decrypt");
			}
		}
		else
		{
			System.out.println("type is empty/null. allowed types are -e for encrypt , -d for decrypt");
		}
	}
	
	public static void printHelp()
	{
		System.out.println("usage : java -jar encryption.jar <option> <key> <text>");
		System.out.println("The following commands are supported");
		System.out.println("option : -e for encrypt");
		System.out.println("       : -d for decrypt ");
		System.out.println("key : key to encrypt or decrypt the text");
		System.out.println("text : text to encrypt or decrypt");
	}
	
	public static void printArg0Message(String type, boolean valid)
	{
		if("-e".equalsIgnoreCase(type) || "-d".equalsIgnoreCase(type))
		{
			if(valid) {
				System.out.println("usage : java -jar encryption.jar "+ type+" <key> <text>");
				System.out.println("The following arguements are required");
				System.out.println("key : key to encrypt or decrypt the text");
				System.out.println("text : text to encrypt or decrypt");
			}
		}
		else
		{
			System.out.println("The following options are supported");
			System.out.println("option : -e for encrypt");
			System.out.println("       : -d for decrypt ");
		}
	}
	
	public static void printArg1Message(String[] args, boolean valid)
	{
			System.out.println("usage : java -jar encryption.jar "+ args[0]+" "+args[1]+ " <text>");
			System.out.println("The following arguements are required");
			System.out.println("text : text to encrypt or decrypt");
	}
	
	public static boolean checkKeyLength(String key)
	{
		boolean isValid = Boolean.TRUE;
		if (key.length() > 64) {
			System.out.println("Allowed key length 64 characters");
			isValid = Boolean.FALSE;
		}
		return isValid;
	}
	
}