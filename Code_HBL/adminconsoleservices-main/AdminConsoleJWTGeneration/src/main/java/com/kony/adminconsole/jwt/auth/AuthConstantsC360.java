/**
 * 
 */
package com.kony.adminconsole.jwt.auth;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface AuthConstantsC360 {
	
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
    String ROLE_ID = "roleId";
    
 // DBX Properties File
 	String PROPERTIES_FILE = "TemenosTokenGen.properties";
    String LEGAL_ENTITY_ID_MS_EXCLUSION_PROPERTIES_FILE = "LegalEntityIdExclusionList.properties";
 	
 // Properties
    String PROP_PREFIX_TEMENOS = "Temenos";
    String PROP_PREFIX_GENERAL = "General";
    String PROP_PREFIX_VERSION = "Version";
    String PROP_ROLE_ID = "RoleId";
    String PROP_ROLE_CAMPAIGN = "CampaignRoleId";
    String PROP_ROLE_ORDMS = "ORDMSRoleId";
    String PROP_ROLE_T24 = "T24RoleId";
    String PROP_ROLE_MARKETINGCATALOGUE = "MarketingCatalogueRoleId";
    String PROP_ROLE_CONSENT = "ConsentRoleId";
    String PROP_ROLE_ODMS= "INFINITY.ONBOARDING";
    String PROP_ROLE_METERING = "Admin";
    
  //Properties
  	String PROP_SECTION_ENROLLMENT = "Enrollment";
  	String PROP_PRE_LOGIN_USERNAME = "PreLoginUserName";
  	String PROP_PRE_LOGIN_USER_ID = "PreLoginUserId";
  	
  	String PARAM_USERNAME = "UserName";
    String PARAM_USER_ID = "userId";
    String PARAM_ROLE_ID = "roleId";
    String PARAM_DBX_USER_ID = "dbxUserId";
    String PARAM_DBX_USER_LEGAL_ENTITY_ID = "legalEntityId";
    String PARAM_CERT_PRIVATE_KEY = "CertPrivateKey";
    String PARAM_CERT_PUBLIC_KEY = "CertPublicKey";
    String DBP_HOST_URL = "DBP_HOST_URL";
    String PARAM_X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String PARAM_BACKEND_NAME = "BackendName";
    String PARAM_CERT_NAME = "CertName";
    String PARAM_BACKEND_CERTIFICATE = "backendcertificate";
    String CONSTANT_TEMPLATE_NAME = "T24";
    String PARAM_USERNAME_LOWERCASE = "username";
    String PARAM_ID_LOWERCASE = "id";
    
    String EQ = "eq";
    String AND = "and";
    String $FILTER = "filter";
    String PRIVATE_ENCRYPTION_KEY = "T24_PRIVATE_ENCRYPTION_KEY";
    
    //Constants for integration services
    String SERVICE_BACKEND_CERTIFICATE = "CRUDLayer";
    String OPERATION_BACKEND_CERTIFICATE_GET = "dbxdb_backendcertificate_get";
    
    //For Analytics
    String JA_BACKEND_NAME = "JOURNEYANALYTICS";
	String JA_AUTH_CERT_CACHE_KEY = "JA_AUTH_CERT";
	String PARAM_REQUEST_KEY = "requestKey";
	String PARAM_SESSION_ID = "sessionId";
	String PARAM_FORM_REQUEST_JSON = "insights.avoka.com/FormRequest";
	String INSIGHTS_APPLICATION_ID = "InsightsApplicationId";
	String LICENSE_GUID = "LicenseGuid";
	String USER_ORG_ID = "UserOrgIds";
	String IS_ADMIN = "Admin";
	String LOGIN_URL = "loginUrl";
	String LOGOUT_URL = "logoutUrl";
	String RETURN_URL = "returnUrl";
	
	//
    String ENTITLEMENT_ADMIN_USER_ID = "ENTITLEMENT_ADMIN_USER_ID";

    String PROP_PREFIX_MULTI_ENTITY = "MultiEntity";
    String MS_EXCLUSION_LIST = "MS_EXCLUSION_LIST";
}
