package com.temenos.infinity.api.chequemanagement.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Funds Authorisation Experience API
 * 
 * @author smugesh
 *
 */
public enum ChequeManagementAPIServices implements InfinityServices {

    SERVICEREQUESTJAVA_CREATEORDER("ServiceRequestJavaService", "createOrder"),
    SERVICEREQUESTJAVA_GETORDERDETAILS("ServiceRequestJavaService","getOrderDetails"),
    SERVICEREQUESTJAVASERVICE_GETTEMPLATEDETAILS("ServiceRequestJavaService","getTemplateDetails"),
    SERVICEREQUESTJAVASERVICE_WITHDRAW("ServiceRequestJavaService","withdrawServiceRequest"),
    SERVICEREQUESTJAVASERVICE_REJECT("ServiceRequestJavaService", "rejectServiceRequest"),
    SERVICEREQUESTJAVA_GETCHEQUEDETAILS("ServiceRequestJavaService","getServiceRequestsByID"),
    SERVICEREQUESTJSON_UPDATEORDER("ServiceRequestJSON", "updateOrder"),
    SERVICEREQUESTJSON_GETTEMPLATEDETAILS("ServiceRequestJSON","getTemplateDetails"),
    SERVICEREQUESTJSON_TRIGGERFORSTATUS("ServiceRequestJSON","triggerForStatus"),
    DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS("dbpUserAttributes","getUserAttributesDetails"),
    T24TRANSACT_CREATECHEQUEBOOK("ChequeManagementT24Services","createChequeBookRequests"),
    T24TRANSACT_CREATESTOPPAYMENT("dbpChequeManagementServices","createStopChequePayments"),
	T24TRANSACT_REVOKESTOPPAYMENT("dbpChequeManagementServices","revokeStopChequePayments"),
	SERVICEREQUESTJAVA_APPROVECHECKBOOKREQUEST("ServiceRequestJavaService", "approveServiceRequest");

    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private ChequeManagementAPIServices(String serviceName, String operationName) {
        this.serviceName = serviceName;
        this.operationName = operationName;
    }

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public String getOperationName() {
        return this.operationName;
    }

}
