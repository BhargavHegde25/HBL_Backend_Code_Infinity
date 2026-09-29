package com.bct.custom.constants;


import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;

/**
 * Jose constant for storing its properties.
 * <br>
 * Author: Arjun Bhat<br>
 * Date: 2024-09-27<br>
 * Version: 1.0
 */
public class JoseConstant {
    /**
     * Token Type - Used in JWS and JWE header.
     *
     * @var string
     */
    public final static JOSEObjectType TOKEN_TYPE = JOSEObjectType.JWT;

    /**
     * JWS (JSON Web Signature) Signature Algorithm - This parameter identifies the cryptographic algorithm used to
     * secure the JWS.
     *
     * @var string
     */
    public final static JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.PS256;

    /**
     * JWE (JSON Web Encryption) Key Encryption Algorithm - This parameter identifies the cryptographic algorithm
     * used to secure the JWE.
     *
     * @var string
     */
    @SuppressWarnings("Recommended: JWEAlgorithm.RSA_OAEP_256")
    public final static JWEAlgorithm JWE_ALGORITHM = JWEAlgorithm.RSA_OAEP_256;

    /**
     * JWE (JSON Web Encryption) Content Encryption Algorithm - This parameter identifies the content encryption
     * algorithm used on the plaintext to produce the encrypted ciphertext.
     *
     * @var string
     */
    public final static EncryptionMethod JWE_ENCRYPTION_ALGORITHM = EncryptionMethod.A256GCM;

    public final static String JWTEncryptionId = "LwttMj6iPZIiX4BvGx5QKd6rfBq1ZDyCHSkCcpUPcY0\\=";//"LwttMj6iPZIiX4BvGx5QKd6rfBq1ZDyCHSkCcpUPcY0\\=";//"LwttMj6iPZIiX4BvGx5QKd6rfBq1ZDyCHSkCcpUPcY0\\\\\\=";

}

