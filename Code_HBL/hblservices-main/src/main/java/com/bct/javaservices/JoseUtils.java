package com.bct.javaservices;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import javax.crypto.KeyGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.bct.custom.backenddeligate.impl.MerchantDetailsBackendDeligateImpl;
import com.bct.custom.constants.JoseConstant;
import com.bct.custom.exceptions.InvalidSignatureException;
import com.kony.dbputilities.util.HelperMethods;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;



/**
 * Security Utils for Encryption, Signing, Decryption and Verifications.
 * <br>
 * Note:
 * <ui> Client send their both Signing and Encryption PublicKeys to Server Before this Operations.</ui>
 * <br>
 * Author: Janakiram<br>
 * Date: 2024-12-14<br>
 * Version: 1.0
 */

@Component
public class JoseUtils {
    /**
     * Use this method for encryption of raw payload.
     * <br>Key Points:<br>
     * <li>Client signs the payload using Client's Signing Private Key</li>
     * <li>Client encrypts the payload using Server's Encrypting Public Key</li>
     * <li>Server decrypts the payload using Server's Private Key.</li>
     * <li>Server verifies the signature of response using Client's Public Key</li>
     *
     * @param payload Raw String Object
     * @return JWE(JWS ( payload)) Returns Signed and Encrypted Payload
     * @throws JOSEException
     */
	private static final Logger log = LogManager.getLogger(JoseUtils.class);
    public static String signAndEncryptPayload(String payload, JSONObject certObj) throws Exception {
		try {
		RSAPrivateKey signingPrivateKey = getRSAPrivateKey(certObj.getString("senderCertPath"), certObj.getString("senderCertPass"));
        JWSObject jwsObject = createJWSObjectPayload(payload);
        signJWSObjectPayload(signingPrivateKey, jwsObject);

        RSAPublicKey encryptingPublicKey = getRSAPublicKey(certObj.getString("receiverCertPath"), certObj.getString("receiverCertPass"));
        JWEObject jweObject = createJWEObjectPayload(jwsObject);
        encryptJWEObjectPayload(encryptingPublicKey, jweObject);
        String token= getEncryptedPayload(jweObject);
        return token;
		}catch (KeyStoreException | IllegalArgumentException | NoSuchAlgorithmException| CertificateException| IOException| UnrecoverableEntryException e) {
		throw new Exception("Couldn't generate token at the moment."+e.getMessage());
		}
    }

    /**
     * Use this method for decryption of response.
     * <br>Key Points:<br>
     * <li>Server signs the response using Server's Signing Private Key</li>
     * <li>Server encrypts the response using Client's Encrypting Public Key.</li>
     * <li>Client decrypts the response using Client's Encrypting Private Key</li>
     * <li>Server verifies the signature of response using Server's Signing Public Key</li>
     *
     * @param response JWE(JWS(response)) Signed and Encrypted Payload
     * @return DecryptedString Returns the usable response.
     * @throws Exception 
     */
    public static String decryptAndVerifyResponse(String response, JSONObject certObj) throws Exception {
		try {
		RSAPrivateKey decryptingPrivateKey = getRSAPrivateKey(certObj.getString("receiverCertPath"), certObj.getString("receiverCertPass"));
        JWEObject jweObject = parseToJWEObjectResponse(response);
        decryptJWEObjectResponse(decryptingPrivateKey, jweObject);
        RSAPublicKey verifyingPublicKey = getRSAPublicKey(certObj.getString("senderCertPath"), certObj.getString("senderCertPass"));
        SignedJWT signedJWSObject = parseJWEObjectToSignedJWSObjectResponse(jweObject);
        verifyJWSObject(signedJWSObject, verifyingPublicKey);
        return getDecryptedResponse(signedJWSObject);
		}catch (KeyStoreException | IllegalArgumentException | NoSuchAlgorithmException| CertificateException| IOException| UnrecoverableEntryException e) {
		throw new Exception("Couldn't generate token at the moment."+e.getMessage());
		}
    }


    /**
     * ===================================================
     * Private and Public Key Loading Sub-Methods
     * ===================================================
     * @throws UnrecoverableEntryException 
     */
   /* private RSAPrivateKey getRSAPrivateKey() {
        // return PrivateKey here.
    }
    */
    private static RSAPrivateKey getRSAPrivateKey(String certPath, String certPassword) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException {
        //byte[] certStore = Files.readAllBytes(Paths.get(certPath));
    	FileInputStream pfxStream = new FileInputStream(certPath);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(pfxStream, certPassword.toCharArray());
        String alias = keyStore.aliases().nextElement();
        /*PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(certPassword.toCharArray()));
        PrivateKey privateKey = pkEntry.getPrivateKey();
        */
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyStore.getKey(alias, certPassword.toCharArray());
    	return rsaPrivateKey;
    }
    private static RSAPublicKey getRSAPublicKey(String certPath, String certPassword) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException {
        //byte[] certStore = Files.readAllBytes(Paths.get(certPath));
    	FileInputStream is = new FileInputStream(certPath);
    	 CertificateFactory f = CertificateFactory.getInstance("X.509");
         X509Certificate certificate = (X509Certificate) f.generateCertificate(is);
         RSAPublicKey rsaPublicKey = (RSAPublicKey) certificate.getPublicKey();
        /*KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new ByteArrayInputStream(certStore), certPassword.toCharArray());
        String alias = keyStore.aliases().nextElement();
        //PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(certPassword.toCharArray()));
        PrivateKey privateKey = pkEntry.getPrivateKey();
    	 X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
         PublicKey publicKey = x509Certificate.getPublicKey();
         HashMap keyPair = new HashMap<>();
         keyPair.put("Alias", alias);
         keyPair.put("PublicKey", publicKey);
         keyPair.put("PrivateKey", privateKey);
         keyPair.put("X509Certificate", x509Certificate);
         RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
         */
		return rsaPublicKey;
    }

    private static RSAPublicKey getSenderRSAPublicKey(String certPath, String certPass) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableKeyException {
        byte[] certStore = Files.readAllBytes(Paths.get(certPath));
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new ByteArrayInputStream(certStore), certPass.toCharArray());
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, certPass.toCharArray());
    	 X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
         PublicKey publicKey = x509Certificate.getPublicKey();
         
         HashMap keyPair = new HashMap<>();
         keyPair.put("Alias", alias);
         keyPair.put("PublicKey", publicKey);
         keyPair.put("PrivateKey", privateKey);
         keyPair.put("X509Certificate", x509Certificate);
         RSAPublicKey rsaPubliKey = (RSAPublicKey) publicKey;
		return rsaPubliKey;
    }

    /**
     * ===================================================
     * Signature Signing and Encryption Sub-Methods
     * ===================================================
     */

    /**
     * Creates JWS Header
     *
     * @return
     */
    private static JWSHeader getJWSHeader() {
        return new JWSHeader
                .Builder(JoseConstant.JWS_ALGORITHM)
                .type(JoseConstant.TOKEN_TYPE)
                .build();
    }

    /**
     * Creates a JWSObject for a given Raw-Payload With Appropriate Signing Headers.
     *
     * @param payload
     * @return
     */
    private static JWSObject createJWSObjectPayload(String payload) {
        JWSHeader jwsHeader = getJWSHeader();
        Payload jwsPayload = new Payload(payload);
        return new JWSObject(jwsHeader, jwsPayload);
    }

    /**
     * Signs the JWSObject with Client's Signing Private Key
     *
     * @param signingKey
     * @param jwsObject
     * @throws JOSEException
     */
    private static void signJWSObjectPayload(RSAPrivateKey signingKey, JWSObject jwsObject) throws JOSEException {
        JWSSigner jwsSigner = new RSASSASigner(signingKey);
        jwsObject.sign(jwsSigner);
    }

    /**
     * Creates JWE Header
     *
     * @return
     */
    private static JWEHeader getJWEHeader() {
        return new JWEHeader
                .Builder(JoseConstant.JWE_ALGORITHM, JoseConstant.JWE_ENCRYPTION_ALGORITHM)
                .contentType(JoseConstant.TOKEN_TYPE.getType())
                .keyID(JoseConstant.JWTEncryptionId)
                .build();
    }

    /**
     * Creates a JWEObject for a Signed Payload with Appropriate Encrypting Headers
     *
     * @param jwsObject
     * @return
     */
    private static JWEObject createJWEObjectPayload(JWSObject jwsObject) {
        JWEHeader jweHeader = getJWEHeader();
        Payload jwePayload = new Payload(jwsObject);
        return new JWEObject(jweHeader, jwePayload);
    }

    /**
     * Encrypts the Signed Payload using Server's Public Key.
     *
     * @param encryptingKey
     * @param jweObject
     * @throws JOSEException
     */
    private static void encryptJWEObjectPayload(RSAPublicKey encryptingKey, JWEObject jweObject) throws JOSEException {
        JWEEncrypter jweEncrypter = new RSAEncrypter(encryptingKey);
        jweObject.encrypt(jweEncrypter);
    }


    /**
     * ===================================================
     * Decryption and Signature Verifications Sub-Methods
     * ===================================================
     */

    /**
     * Gets the actual Signed and Encrypted Payload
     *
     * @param jweObject
     * @return
     */
    private static String getEncryptedPayload(JWEObject jweObject) {
        return jweObject.serialize();
    }

    /**
     * Parses the Raw Response to the JWEObject
     *
     * @param token
     * @return
     * @throws ParseException
     */
    private static JWEObject parseToJWEObjectResponse(String token) throws ParseException {
        return JWEObject.parse(token);
    }

    /**
     * Decrypts the Parsed JWEObject using Client's Decrypting Private Key.
     *
     * @param decryptingKey
     * @param jweObject
     * @throws JOSEException
     */
    private static void decryptJWEObjectResponse(RSAPrivateKey decryptingKey, JWEObject jweObject) throws JOSEException {
        JWEDecrypter jweDecrypter = new RSADecrypter(decryptingKey);
        jweObject.decrypt(jweDecrypter);
    }

    /**
     * Converts the {@code JWEObject} (Decrypted Object) to {@code JWSObject} (Signing Object) which is SuperSet of {@code SignedJWT}
     *
     * @param jweObject
     * @return SignedJWT
     */
    private static SignedJWT parseJWEObjectToSignedJWSObjectResponse(JWEObject jweObject) {
        return jweObject.getPayload().toSignedJWT();
    }

    /**
     * Verifies the JWSObject, authenticity of the response.
     *
     * @param jwsObject
     * @param verifyingKey
     * @throws JOSEException
     * @throws InvalidSignatureException 
     */
    private static void verifyJWSObject(JWSObject jwsObject, RSAPublicKey verifyingKey) throws JOSEException, InvalidSignatureException {
        JWSVerifier jwsVerifier = new RSASSAVerifier(verifyingKey);
        if (!jwsObject.verify(jwsVerifier)) {
            log.error("Invalid Signature");
            throw new InvalidSignatureException("Invalid Signature.");
        }
    }

    /**
     * Returns the Actual Decrypted Response.
     *
     * @param jwsObject
     * @return
     */
    public static String getDecryptedResponse(JWSObject jwsObject) {
        Payload decryptedPayload = jwsObject.getPayload();
        return Objects.nonNull(decryptedPayload) ? decryptedPayload.toString() : null;
    }

    /**
     * Use to generate Symmetric JWE Header KEY ID
     *
     * @return
     */
    private static String generateJweSymmetricKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            byte[] keyBytes = keyGenerator.generateKey().getEncoded();
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (Exception e) {
            log.error("Exception in generating JWE Symmetric Key.", e);
        }
        return null;
    }
    public String generateSessionId(){
		String sessionId = "himalayan" + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return sessionId;
	}

}
