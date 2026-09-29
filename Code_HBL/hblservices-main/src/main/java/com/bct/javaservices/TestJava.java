package com.bct.javaservices;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class TestJava {
	public static void main(String[] args) throws Exception {
		        final String username = "3789520217";

		        // Here the magic numbers
		        final int pswdIterations = 10000;
		        final int keySize = 128;

		        final byte[] saltBytes = { -83, 69, -15, -93, -60, 62, 111, -83 };

		        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		        PBEKeySpec spec = new PBEKeySpec(username.toCharArray(), saltBytes, pswdIterations, keySize);
		        SecretKey secretKey = factory.generateSecret(spec);
		        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		        String encodedKey = Base64.getEncoder().encodeToString(secret.getEncoded());
		        System.out.println("secret:"+encodedKey);
		        
		    
		}
	
		/*
		 * public static String getTOTPCode(String secretKey) { //Base32 base32 = new
		 * Base32(); //byte[] bytes = base32.decode(secretKey);
		 * 
		 * Decoder decoder = Base64.getDecoder(); byte[] bytes =
		 * decoder.decode(secretKey);
		 * 
		 * System.out.println("bytes ***"+ bytes); String hexKey =
		 * Hex.encodeHexString(bytes); System.out.println("hexKey ***"+ hexKey); return
		 * TOTP.getOTP(hexKey); }
		 */

}
