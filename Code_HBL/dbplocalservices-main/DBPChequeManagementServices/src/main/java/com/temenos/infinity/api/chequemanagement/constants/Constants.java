package com.temenos.infinity.api.chequemanagement.constants;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface Constants {

	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");
	
    public static final String DEVICE_ID = "X-Kony-DeviceId";
    public static final String USER_ID_ANONYMOUS = "anonymous";
    public static final String SESSION_ATTRIB_ACCOUNT = "Accounts";
    public static final String FEATURE_ACTION_ID = "CHEQUE_BOOK_REQUEST_CREATE";
    public static final String DBP_APPROVAL_REQUEST_SERVICES = "dbpApprovalRequestServices";
    public static final String DBPRBLOCALSERVICEDB = "dbpRbLocalServicesdb";
    public static final String VALIDATE_FOR_APPROVALS = "ValidateForApprovals";
	public static final String UPDATE_BACKENDID_IN_APPROVALQUEUE = "UpdateBackendIdInApprovalQueue";
	public static final String DB_BBREQUEST_GET = SCHEMA_NAME +"_bbrequest_get";
	public static final String DB_BBREQUEST_DELETE = SCHEMA_NAME +"_bbrequest_delete";
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String SENT = "sent";
	
}