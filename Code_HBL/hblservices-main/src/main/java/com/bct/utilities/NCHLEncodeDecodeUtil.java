package com.bct.utilities;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import codec.asn1.DERDecoder;
import codec.pkcs12.PFX;
import codec.pkcs12.PKCS8ShroudedKeyBag;
import codec.pkcs12.SafeBag;
import de.flexiprovider.core.FlexiCoreProvider;

public class NCHLEncodeDecodeUtil {

	public static String encryption(String encString, String certFile) throws Exception {
		FileInputStream is = new FileInputStream(certFile);
		CertificateFactory f = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) f.generateCertificate(is);
		PublicKey pubkey = certificate.getPublicKey();

		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);

		byte[] input = encString.getBytes();
		cipher.update(input);
		byte[] cipherText = cipher.doFinal();
		String encStringAfter = byteArray2Base64(cipherText);
//        String encStringAfter = new String(Base64.getEncoder().encode(cipherText));

		System.out.println("Byte: " + cipherText.length + "\n");
		System.out.println("ENCRYPT: " + encStringAfter + "\n");
		return encStringAfter;
	}

	public static String decryption(String encString, String pfxFile, String pfxFilePassword) throws Exception {

		Security.addProvider(new FlexiCoreProvider());
		DERDecoder dec = new DERDecoder(new FileInputStream(pfxFile));
		PFX pfx = new PFX();
		pfx.decode(dec);
		SafeBag safeBag = pfx.getAuthSafe().getSafeContents(0).getSafeBag(0);
		PKCS8ShroudedKeyBag kBag = (PKCS8ShroudedKeyBag) safeBag.getBagValue();
		PrivateKey privatekey = kBag.getPrivateKey(pfxFilePassword.toCharArray());

		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
		cipher.init(Cipher.PRIVATE_KEY, privatekey);

		byte[] input = base64ToByteArray(encString);
		cipher.update(input);
		byte[] cipherText = cipher.doFinal();
		String decString = new String(cipherText);
		System.out.println("DECRYPT " + encString + "\n" + decString);
		return decString;
	}

	private static byte[] base64ToByteArray(String encString) throws UnsupportedEncodingException {
		byte[] decodedString = Base64.decodeBase64(encString.getBytes());
		return decodedString;
	}

	private static String byteArray2Base64(byte[] cipherText) {
		String decoded = new String(Base64.encodeBase64(cipherText));
		return decoded;
	}

}
