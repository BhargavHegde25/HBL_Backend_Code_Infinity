/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.config;

import static com.kony.dbputilities.util.EnvironmentConfigurationsHandler.getValue;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * @author mrunalini.adepu
 *
 */
public enum TradeLendingAPIServices implements InfinityServices{

	
	DBPRBLOCALSERVICES_LD_RECORD_CREATE("dbpRbLocalServicesdb", getValue("DBX_SCHEMA_NAME") + "_ld_records_create"),
    DBPRBLOCALSERVICES_LD_RECORD_UPDATE("dbpRbLocalServicesdb", getValue("DBX_SCHEMA_NAME") + "_ld_records_update"),
    DBPRBLOCALSERVICES_LD_RECORDS_GET("dbpRbLocalServicesdb", getValue("DBX_SCHEMA_NAME") + "_ld_records_get");    

    private final String serviceName;
    private final String operationName;

    TradeLendingAPIServices(String serviceName, String operationName) {
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
