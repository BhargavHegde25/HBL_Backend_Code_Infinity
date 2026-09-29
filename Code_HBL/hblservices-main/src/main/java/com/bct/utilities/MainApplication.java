package com.bct.utilities;

import com.google.zxing.WriterException;

import de.taimos.totp.TOTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

public class MainApplication {

	public static void main(String[] args) throws IOException, WriterException {
		String secretKey = generateSecretKey();
		System.out.println("secretKey > " + secretKey);
		String totpCode = getTOTPCode(secretKey);
		System.out.println("totpCode > " + totpCode);
		String companyName = "Himalayan Bank Limited";
		
		System.out.print("Please enter your user name (email) -> ");
		Scanner scanner1 = new Scanner(System.in);
		String userName = scanner1.nextLine();
		
		String barCodeUrl = Utils.getGoogleAuthenticatorBarCode(secretKey, userName, companyName);
		System.out.println("barCodeUrl >> " + barCodeUrl);
		Utils.createQRCode(barCodeUrl, "QRCode.png", 400, 400);

		System.out.print("Please enter 2fA code here -> ");
		Scanner scanner2 = new Scanner(System.in);
		String code = scanner2.nextLine();
		
		if (code.equals(Utils.getTOTPCode(secretKey))) {
			System.out.println("Logged in successfully");
		} else {
			System.out.println("Invalid 2FA Code");
		}

	}
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
	    return TOTP.getOTP(hexKey);
	}
}
