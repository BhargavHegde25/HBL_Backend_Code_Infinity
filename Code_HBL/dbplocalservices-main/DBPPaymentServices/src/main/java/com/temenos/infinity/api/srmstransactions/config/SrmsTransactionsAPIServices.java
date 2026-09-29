package com.temenos.infinity.api.srmstransactions.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum SrmsTransactionsAPIServices implements InfinityServices {

    SERVICEREQUESTJAVA_CREATEORDER("ServiceRequestJavaService", "createOrder"),
    SERVICEREQUESTJAVA_GETORDERDETAILS("ServiceRequestJavaService","getOrderDetails"),
    SERVICEREQUESTJAVA_GETORDERDETAILSBYCATEGORY("ServiceRequestJavaService","getServiceRequestBasedOnCriteria"),
    SERVICEREQUESTJAVA_GETORDERDETAILSBYID("ServiceRequestJavaService","getServiceRequestsByID"),
    T24ISPAYMENTORDERS_CREATEPAYMENTWITHOUTAPPROVER("T24ISPaymentOrders","createPaymentWithoutApprover"),
    T24ISSTANDINGORDERS_CREATESTOWITHOUTAPPROVER("T24ISStandingOrders","createStandingOrderWithoutApprover");

    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private SrmsTransactionsAPIServices(String serviceName, String operationName) {
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
