package com.infinity.dbx.certificate.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import com.infinity.dbx.certificate.generator.util.EncryptionUtils;
import com.infinity.dbx.certificate.generator.util.KeyPairConstants;

public class KeyPairGeneratorApplication {
	public static void main(String[] args) {
		PrintStream stdout = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
		try {
			stdout = new PrintStream(new File("key_pair_generation" + dateFormat.format(new Date()) + ".log"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		finally {
			try {
				if(stdout!=null) {
					stdout.close();
				}
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		System.setOut(stdout);
		System.setErr(stdout);
		String encryptionKey = getEncryptionKey();
		if (StringUtils.isBlank(encryptionKey)) {
			throw new RuntimeException("Encryption Key Cannot be null Please provide -Ddb.encryptionKey");
		}
		generateCert();
	}

	public static void generateCert() {
		try {
			Security.addProvider(new BouncyCastleProvider());
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyPairConstants.KEY_ALGO, "BC");
			keyPairGenerator.initialize(KeyPairConstants.KEY_INITIALIZATION_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			createPublicKeyFile(publicKey);
			PrivateKey privateKey = keyPair.getPrivate();
			savePrivateKey(privateKey, publicKey);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void createPublicKeyFile(PublicKey publicKey) {
		String outputPath = getOutputFilePath();
		FileOutputStream fileOutputStream = null;
		if (StringUtils.isBlank(outputPath)) {
			outputPath = KeyPairConstants.PUBLIC_KEY_FILE_NAME;
		} else {
			outputPath = outputPath + "\\" + KeyPairConstants.PUBLIC_KEY_FILE_NAME;
		}
		try {
			RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
			File file = new File(outputPath);
			fileOutputStream = new FileOutputStream(file);
			PemWriter pemWriter = new PemWriter(new OutputStreamWriter(fileOutputStream));
			PemObject pemObject = new PemObject("RSA PUBLIC KEY", rsaPublicKey.getEncoded());
			pemWriter.writeObject(pemObject);
			pemWriter.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if(fileOutputStream!=null) {
					fileOutputStream.close();
				}
			} catch(Exception e) {
				System.out.println(e);
			}
			
		}
	}

	public static String getOutputFilePath() {
		String outputPath = System.getProperty(KeyPairConstants.PARAM_OUTPUT_FOLDER_PATH);
		return outputPath;
	}

	public static void savePrivateKey(PrivateKey privateKey, PublicKey publicKey) throws Exception {
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		PemObject pemPrivateKeyObject = new PemObject("PRIVATE KEY", rsaPrivateKey.getEncoded());
		StringWriter stringWriter = new StringWriter();
		writeContent(stringWriter, pemPrivateKeyObject);
		String privateKeyString = stringWriter.toString();
		stringWriter.close();
		stringWriter = new StringWriter();
		String encryptedPrivateKey = EncryptionUtils.encrypt(privateKeyString, getEncryptionKey());
		saveEncryptedPrivateKeyToFile(encryptedPrivateKey);
		savePrivateKeyToFile(privateKey);
		System.out.println("Keys Generated Successfully in the path "+getOutputFilePath());
	}

	private static String getEncryptionKey() {
		String encryptionKey = System.getProperty(KeyPairConstants.ENCRYPTION_KEY);
		return encryptionKey;
	}

	public static void saveEncryptedPrivateKeyToFile(String encryptedPrivateKey) {
		FileOutputStream fileOutputStream = null;
		try {
			String outputPath = getOutputFilePath();
			if (StringUtils.isBlank(outputPath)) {
				outputPath = KeyPairConstants.ENCRYPTED_PRIVATE_KEY_FILE_NAME;
			} else {
				outputPath = outputPath + "\\" + KeyPairConstants.ENCRYPTED_PRIVATE_KEY_FILE_NAME;
				;
			}
			File file = new File(outputPath);
			fileOutputStream = new FileOutputStream(file);
			String result = encryptedPrivateKey;
			fileOutputStream.write(result.getBytes());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if(fileOutputStream!=null) {
					fileOutputStream.close();
				}
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void savePrivateKeyToFile(PrivateKey privateKey) {
		FileOutputStream fileOutputStream = null;
		try {
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
			String outputPath = getOutputFilePath();
			if (StringUtils.isBlank(outputPath)) {
				outputPath = KeyPairConstants.PRIVATE_KEY_FILE_NAME;
				;
			} else {
				outputPath = outputPath + "\\" + KeyPairConstants.PRIVATE_KEY_FILE_NAME;
			}
			File file = new File(outputPath);
			fileOutputStream = new FileOutputStream(file);
			PemWriter pemWriter = new PemWriter(new OutputStreamWriter(fileOutputStream));
			PemObject pemObject = new PemObject("PRIVATE KEY", rsaPrivateKey.getEncoded());
			pemWriter.writeObject(pemObject);
			pemWriter.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if(fileOutputStream!=null) {
					fileOutputStream.close();
				}
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void writeContent(Writer writer, PemObject pemObject) {
		try {
			PemWriter pemWriter = new PemWriter(writer);
			pemWriter.writeObject(pemObject);
			pemWriter.flush();
			pemWriter.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
