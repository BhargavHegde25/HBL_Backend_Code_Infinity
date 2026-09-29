package com.infinity.dbx.temenos;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the T24 Experience API
 * 
 * @author Suryaa Charan S
 *
 */
public enum T24IrisAPIServices implements InfinityServices {

    
	SERVICE_BACKEND_IDENTIFIER("dbpRbLocalServicesdb", "dbxdb_backendidentifier_get");
	
    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private T24IrisAPIServices(String serviceName, String operationName) {
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