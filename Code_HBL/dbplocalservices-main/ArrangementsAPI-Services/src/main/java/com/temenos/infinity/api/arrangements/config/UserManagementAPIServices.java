package com.temenos.infinity.api.arrangements.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum UserManagementAPIServices implements InfinityServices {

	SERVICEREQUESTJAVA_CREATEORDER("ServiceRequestJavaService", "createOrder");
	
    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private UserManagementAPIServices(String serviceName, String operationName) {
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
