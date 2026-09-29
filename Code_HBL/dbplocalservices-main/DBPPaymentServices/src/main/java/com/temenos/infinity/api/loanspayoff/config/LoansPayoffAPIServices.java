package com.temenos.infinity.api.loanspayoff.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum LoansPayoffAPIServices implements InfinityServices {

    SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get"),
    LOANSERVICEJSON_CREATESIMULATION("LoanServiceJSON", "createSimulation"),
    LOANSERVICEJSON_GETBILLDETAILS("LoanServiceJSON", "getBillDetails"),
	LOANMOCKSERVICE_GETBILLDETAILS("LoanMockService", "getBillDetails");

    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private LoansPayoffAPIServices(String serviceName, String operationName) {
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
