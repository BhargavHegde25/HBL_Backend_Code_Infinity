package com.temenos.dbx.eum.sca;

public interface SCAConstants {
	//Environment Properties
	String ENV_IS_SCA_ENABLED = "IS_SCA_ENABLED";
	String ENV_SCA_PI_EXTENSION = "SCA_PI_EXTENSION";
	
	//SCA Service Details
	String INTEGRATION_SERVICE_SCA_SERVICE_ID = "SCAServices";
	String SCA_CREATE_USER_OPERATION_ID = "createUser";
	String SCA_RESEND_ACTIVATION_CODE_OPERATION_ID = "sendActivationCode";
	
	//SCA Service Payload Attributes
	String PAYLOAD_USER_ID = "userId";
	String PAYLOAD_ACTIVATION_CODE = "activationCode";
	String PAYLOAD_FIRST_NAME = "firstName";
	String PAYLOAD_LAST_NAME = "lastName";
	String PAYLOAD_MIDDLE_NAME = "middleName";
	String PAYLOAD_EMAIL_ID = "emailId";
	String PAYLOAD_PHONE = "phone";
}
