/**
 * 
 */
package com.infinity.dbx.dbp.jwt.auth;

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
	
//	String PRIVATE_KEY = "private.key";
//	String PUBLIC_KEY = "public.key";
	String AUTH_CERT_CACHE_KEY = "AUTH_CERT";
	int AUTH_CERT_CACHE_TIME = 3600;
	String AUTH_CERT_NAME = "AUTH";
	String AUTH_PUBLIC_KEY_METADATA = "/services/T24ISExtra/getpublickey";
	String AUTH_PROTOCOL = "https://";
	
	// Login Flows
    String FLOW_TYPE = "flowType";
    String PRE_LOGIN_FLOW = "PreLogin";
    String LOGIN_FLOW = "Login";
    String POST_LOGIN_FLOW = "PostLogin";
    
 // DBX Properties File
 	String PROPERTIES_FILE = "DBXTemenosTokenGen.properties";
 	
 // Properties
    String PROP_PREFIX_TEMENOS = "Temenos";
    String PROP_PREFIX_GENERAL = "General";
    String PROP_PREFIX_VERSION = "Version";
    String PROP_ROLE_ID = "RoleId";
    String PROP_PREFIX_PARTY = "Party";
    
  //Properties
  	String PROP_SECTION_ENROLLMENT = "Enrollment";
  	String PARTY_SECTION_ENROLLMENT= "PartyEnrollment";
  	String PROP_PRE_LOGIN_USERNAME = "PreLoginUserName";
  	String PROP_PRE_LOGIN_USER_ID = "PreLoginUserId";
  	
  	String PARAM_USERNAME = "UserName";
    String PARAM_USER_ID = "userId";
    String PARAM_ROLE_ID = "roleId";
    String PARAM_DBX_USER_ID = "dbxUserId";
    String PARAM_CERT_PRIVATE_KEY = "CertPrivateKey";
    String PARAM_CERT_PUBLIC_KEY = "CertPublicKey";
    String DBP_HOST_URL = "DBP_HOST_URL";
    String PARAM_X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String PARAM_BACKEND_NAME = "BackendName";
    String PARAM_CERT_NAME = "CertName";
    String PARAM_BACKEND_CERTIFICATE = "backendcertificate";
    String CONSTANT_TEMPLATE_NAME = "T24";
    
    String EQ = "eq";
    String AND = "and";
    String $FILTER = "filter";
    String PRIVATE_ENCRYPTION_KEY = "T24_PRIVATE_ENCRYPTION_KEY";
    
    //Constants for integration services
    String SERVICE_BACKEND_CERTIFICATE = "dbpRbLocalServicesdb";
    String OPERATION_BACKEND_CERTIFICATE_GET = "dbxdb_backendcertificate_get";
    String PROP_PREFIX_MULTI_ENTITY = "MultiEntity";
    String MS_EXCLUSION_LIST = "MS_EXCLUSION_LIST";
    String PARAM_DBX_USER_LEGAL_ENTITY_ID = "legalEntityId";
    String LEGAL_ENTITY_ID_MS_EXCLUSION_PROPERTIES_FILE = "LegalEntityIdExclusionList.properties";
    
    
}
