package com.temenos.msArrangement.utils;

import com.konylabs.middleware.dataobject.Param;

/**
 * A re-usable DBP constants class.
 * 
 * @author Venkateswara Rao Alla
 *
 */
public class DBPConstants {

    // Query Param Constants
    public static final String DBP_KONY_AUTHORIZATION_TOKEN_QUERY_PARAM = "authToken";

    // Header Constants
    public static final String X_KONY_AUTHORIZATION_HEADER = "X-Kony-Authorization";
    public static final String X_KONY_INTEGRITY_SALT_HEADER = "X-Kony-Integrity-Salt";

    // Servlet Constants
    public static final String SERVLET_CONTEXT_OBJECT_SERVICES = "AppServices";

    // DBP Error Constants
    public static final String DBP_ERROR_CODE_KEY = "dbpErrCode";
    public static final String DBP_ERROR_MESSAGE_KEY = "dbpErrMsg";

    // Fabric Result Constants
    public static final String FABRIC_OPSTATUS_KEY = Param.OPSTATUS;
    public static final String FABRIC_ERROR_MESSAGE_KEY = Param.ERR_MSG;
    public static final String FABRIC_HTTP_STATUS_CODE_KEY = Param.HTTP_STATUS_CODE;
    public static final String FABRIC_STRING_CONSTANT_KEY = Param.STRING_CONST;
    public static final String FABRIC_BOOLEAN_CONSTANT_KEY = Param.BOOLEAN_CONST;
    public static final String FABRIC_NUMBER_CONSTANT_KEY = Param.NUMBER_CONST;
    public static final String FABRIC_INT_CONSTANT_KEY = Param.INT_CONST;
    public static final String FABRIC_DOUBLE_CONSTANT_KEY = Param.DOUBLE_CONST;
    public static final String FABRIC_COLLECTION_CONSTANT_KEY = Param.COLLECTION_CONST;
    public static final String FABRIC_CHUNKED_RESULTS_IN_JSON = "chunkedresults_json";

    // Fabric Identity Constants
    public static final String IDENTITY_SECURITY_ATTRIBUTES = "security_attributes";
    public static final String IDENTITY_SESSION_TOKEN = "session_token";
    public static final String IDENTITY_SESSION_TTL = "session_ttl";

    // DBP Auth related Constants
    public static final String IS_DBP_AUTHZ_DISABLED_KEY = "IS_DBP_AUTHZ_DISABLED";
    public static final String CUSTOMER_TYPE_ID_IDENTITY_KEY = "customerTypeId";
    public static final String PERMISSIONS_IDENTITY_KEY = "permissions";
    public static final String PERMISSION_ALLOW = "ALLOW";
    public static final String PERMISSION_API_ACCESS = "API_ACCESS";

}
