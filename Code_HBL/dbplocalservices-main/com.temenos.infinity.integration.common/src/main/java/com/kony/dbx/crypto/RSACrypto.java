package com.kony.dbx.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACrypto {

	public static String getEncrypted(String data, String Key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/GCM/OAEPWithMD5AndMGF1Padding");
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(Key.getBytes())));
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedbytes = cipher.doFinal(data.getBytes());
		return new String(Base64.getEncoder().encode(encryptedbytes));
	}

	public static String getDecrypted(String data, String Key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/GCM/OAEPWithMD5AndMGF1Padding");
		PrivateKey pk = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(Key.getBytes())));
		cipher.init(Cipher.DECRYPT_MODE, pk);
		byte[] encryptedbytes = cipher.doFinal(Base64.getDecoder().decode(data.getBytes()));
		return new String(encryptedbytes);
	}
}
