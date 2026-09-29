package com.temenos.infinity.api.loanspayoff.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jose.JWSAlgorithm;

/**
 * <p>
 * DTO representation of Backend-Certificate
 * </p>
 * 
 * @implNote Default value for JWSAlgorithm is JWSAlgorithm.RS256
 * 
 * @author Aditya Mankal
 *
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackendCertificate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7439538094341794276L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("BackendName")
	private String backendName;

	@JsonProperty("CertPrivateKey")
	private String certificatePrivateKey;

	@JsonProperty("CertPublicKey")
	private String certificatePublicKey;

	private String getPublicKeyServiceURL;

	private String certificateEncryptionKey;

	private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the backendName
	 */
	public String getBackendName() {
		return backendName;
	}

	/**
	 * @param backendName the backendName to set
	 */
	public void setBackendName(String backendName) {
		this.backendName = backendName;
	}

	/**
	 * @return the certificatePrivateKey
	 */
	public String getCertificatePrivateKey() {
		return certificatePrivateKey;
	}

	/**
	 * @param certificatePrivateKey the certificatePrivateKey to set
	 */
	public void setCertificatePrivateKey(String certificatePrivateKey) {
		this.certificatePrivateKey = certificatePrivateKey;
	}

	/**
	 * @return the certificatePublicKey
	 */
	public String getCertificatePublicKey() {
		return certificatePublicKey;
	}

	/**
	 * @param certificatePublicKey the certificatePublicKey to set
	 */
	public void setCertificatePublicKey(String certificatePublicKey) {
		this.certificatePublicKey = certificatePublicKey;
	}

	/**
	 * @return the getPublicKeyServiceURL
	 */
	public String getGetPublicKeyServiceURL() {
		return getPublicKeyServiceURL;
	}

	/**
	 * @param getPublicKeyServiceURL the getPublicKeyServiceURL to set
	 */
	public void setGetPublicKeyServiceURL(String getPublicKeyServiceURL) {
		this.getPublicKeyServiceURL = getPublicKeyServiceURL;
	}

	/**
	 * @return the certificateEncryptionKey
	 */
	public String getCertificateEncryptionKey() {
		return certificateEncryptionKey;
	}

	/**
	 * @param certificateEncryptionKey the certificateEncryptionKey to set
	 */
	public void setCertificateEncryptionKey(String certificateEncryptionKey) {
		this.certificateEncryptionKey = certificateEncryptionKey;
	}

	/**
	 * @return the jwsAlgorithm
	 */
	public JWSAlgorithm getJwsAlgorithm() {
		return jwsAlgorithm;
	}

	/**
	 * @param jwsAlgorithm the jwsAlgorithm to set
	 */
	public void setJwsAlgorithm(JWSAlgorithm jwsAlgorithm) {
		this.jwsAlgorithm = jwsAlgorithm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BackendCertificate other = (BackendCertificate) obj;
		if (backendName == null) {
			if (other.backendName != null)
				return false;
		} else if (!backendName.equals(other.backendName))
			return false;
		return true;
	}

}
