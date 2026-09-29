/**
 * 
 */
package com.infinity.dbx.temenos.auth;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface AuthConstants {
	
	/**
	 * JWT Token Constants
	 */
	String TOKEN_ISSUER = "Fabric";
	String TOKEN_AUDIENCE = "IRIS";
	String TOKEN_ROLE_ID = "INFINITY.RETAIL";
	String TOKEN_HEADER_KEY = "KA01";
	
	String PRIVATE_KEY = "private.key";
	String PUBLIC_KEY = "public.key";
	String AUTH_CERT_CACHE_KEY = "AUTH_CERT_CORE_T24";
	int AUTH_CERT_CACHE_TIME = 3600;
	String AUTH_CERT_NAME = "AUTH";
	String AUTH_PUBLIC_KEY_METADATA = "/services/T24ISExtra/getpublickey";
	String AUTH_PROTOCOL = "https://";
	String PROP_PREFIX_MULTI_ENTITY = "MultiEntity";
	String MS_EXCLUSION_LIST = "MS_EXCLUSION_LIST";
	String PARAM_DBX_USER_LEGAL_ENTITY_ID = "legalEntityId";
	String LEGAL_ENTITY_ID_MS_EXCLUSION_PROPERTIES_FILE = "LegalEntityIdExclusionList.properties";
	String PROP_PREFIX_TEMENOS = "Temenos";
}
