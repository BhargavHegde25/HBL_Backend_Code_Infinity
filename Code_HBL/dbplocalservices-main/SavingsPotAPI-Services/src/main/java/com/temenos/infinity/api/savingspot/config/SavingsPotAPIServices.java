package com.temenos.infinity.api.savingspot.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author KH1769
 *
 */
public enum SavingsPotAPIServices implements InfinityServices {

	SAVINGSPOTJSON_GETALLSAVINGSPOT("SavingsPotMS", "getAllSavingsPotMS"),
	SAVINGSPOTJSON_GETCATEGORIES("SavingsPotMS", "getCategoriesForGoalMS"),
	SAVINGSPOTJSON_CREATESAVINGSPOT("SavingsPotMS", "createSavingsPotMS"),
	SAVINGSPOTJSON_CREATEBUDGET("SavingsPotMS", "createBudgetMS"),
	SAVINGSPOTJSON_CLOSESAVINGSPOT("SavingsPotMS", "closeSavingsPotMS"),
	SAVINGSPOTJSON_UPDATESAVINGSPOT("SavingsPotMS", "updateSavingsPotMS"),
	SAVINGSPOTJSON_UPDATESAVINGSBUDGET("SavingsPotMS", "updateBudgetMS"),
	SAVINGSPOTJSON_UPDATESAVINGSPOTBALANCE("SavingsPotMS", "updateSavingsPotBalanceMS");
	
    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private SavingsPotAPIServices(String serviceName, String operationName) {
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