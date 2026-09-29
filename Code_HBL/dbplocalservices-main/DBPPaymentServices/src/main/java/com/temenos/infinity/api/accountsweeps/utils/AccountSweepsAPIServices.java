package com.temenos.infinity.api.accountsweeps.utils;

import com.temenos.infinity.api.commons.config.InfinityServices;

import static com.kony.dbputilities.util.EnvironmentConfigurationsHandler.getValue;

/**
 * @author naveen.yerra
 */
public enum AccountSweepsAPIServices implements InfinityServices {

    SERVICEREQUESTJAVA_CREATEORDER("ServiceRequestJavaService", "createOrder"),
    SERVICEREQUESTJAVA_GETORDERDETAILS("ServiceRequestJavaService", "getOrderDetails"),
    SERVICEREQUESTJAVA_GETSERVICEREQUESTBYID("ServiceRequestJavaService", "getServiceRequestsByID"),
    SERVICEREQUESTJAVA_UPDATEORDER("ServiceRequestJavaService", "updateServiceRequest"),
    SERVICEREQUESTJAVA_GETORDERDETAILSBYCATEGORY("ServiceRequestJavaService","getServiceRequestBasedOnCriteria"),
    DB_UPDATEACCOUNTSWEEP("dbpRbLocalServicesdb",getValue("DBX_SCHEMA_NAME") +"_accountsweeps_update"),
    DB_GETACCOUNTSWEEP("dbpRbLocalServicesdb",getValue("DBX_SCHEMA_NAME") + "_accountsweeps_get"),
    DB_CREATEACCOUNTSWEEP("dbpRbLocalServicesdb",getValue("DBX_SCHEMA_NAME") + "_accountsweeps_create"),
    DB_DELETEACCOUNTSWEEP("dbpRbLocalServicesdb",getValue("DBX_SCHEMA_NAME") + "_accountsweeps_delete"),
    DB_UPDATEACCOUNTSWEEPINFO("dbpRbLocalServicesdb",getValue("DBX_SCHEMA_NAME") + "_customeraccounts_sweepinfoupdate_proc");
    private final String serviceName, operationName;

    AccountSweepsAPIServices(String serviceName, String operationName) {
        this.serviceName = serviceName;
        this.operationName = operationName;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }
}
