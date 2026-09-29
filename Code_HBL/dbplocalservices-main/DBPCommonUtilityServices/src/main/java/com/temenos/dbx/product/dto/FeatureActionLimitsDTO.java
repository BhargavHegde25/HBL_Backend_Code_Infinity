package com.temenos.dbx.product.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonObject;

public class FeatureActionLimitsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 3625831875563550466L;
    private String companyLegalUnit;

    /**
	 * 
	 */
	private Set<String> accountLevelActions = new HashSet<String>();

	private Set<String> globalLevelActions = new HashSet<String>();

	private Set<String> monetaryActions = new HashSet<String>();
	/**
     * Key : featureId , Value : Set of actions allowed for the feature
     */
    private Map<String, Set<String>> featureaction = new HashMap<>();
    /**
	 * Key : featureId , Value : Set of actions allowed for the feature
	 */
	private Map<String, Set<String>> newFeatureAction = new HashMap<>();
	/**
     * Key : featureId , Value : Feature information
     */
    private Map<String, JsonObject> featureInfo = new HashMap<>();
    /**
     * Key : actionId , Value : Action information
     */
    private Map<String, JsonObject> actionsInfo = new HashMap<>();
    /**
     * Key : featureId , Value : ( Key : monetary action id , value : Jsonarray of limits )
     */
    private Map<String, Map<String, Map<String, String>>> monetaryActionLimits = new HashMap<>();
    /**
     * Key : accountId , Value : ( Key : monetary feature id , value : action id )
     */
    private Map<String,Map<String, Set<String>>> asscoiatedAccountActions = new HashMap<>();
	/**
	 * Key : featureId , Value : ( Key : monetary action id , value : Jsonarray of
	 * limits )
	 */
	private Map<String, Map<String, Map<String, String>>> newMonetaryActionLimits = new HashMap<>();

	public Map<String, Set<String>> getGlobalLevelPermissions() {
        return globalLevelPermissions;
    }

    public void setGlobalLevelPermissions(Map<String, Set<String>> globalLevelPermissions) {
        this.globalLevelPermissions = globalLevelPermissions;
    }

    public Map<String, Set<String>> getAccountLevelPermissions() {
        return accountLevelPermissions;
    }

    public void setAccountLevelPermissions(Map<String, Set<String>> accountLevelPermissions) {
        this.accountLevelPermissions = accountLevelPermissions;
    }

    public Map<String, Set<String>> getTransactionLimits() {
        return transactionLimits;
    }

    public void setTransactionLimits(Map<String, Set<String>> transactionLimits) {
        this.transactionLimits = transactionLimits;
    }

    public Map<String, Set<String>> getCoreCustomerGlobalLevelPermissions() {
        return coreCustomerGlobalLevelPermissions;
    }

    public void setCoreCustomerGlobalLevelPermissions(
    		Map<String, Set<String>> coreCustomerGlobalLevelPermissions) {
        this.coreCustomerGlobalLevelPermissions = coreCustomerGlobalLevelPermissions;
    }

    public Map<String, Map<String, Map<String, Set<String>>>> getCoreCustomerAccountLevelPermissions() {
        return coreCustomerAccountLevelPermissions;
    }

    public void setCoreCustomerAccountLevelPermissions(
            Map<String, Map<String, Map<String, Set<String>>>> coreCustomerAccountLevelPermissions) {
        this.coreCustomerAccountLevelPermissions = coreCustomerAccountLevelPermissions;
    }

    public Map<String, Map<String, Map<String, String>>> getCoreCustomerTransactionLimits() {
        return coreCustomerTransactionLimits;
    }

    public void setCoreCustomerTransactionLimits(
    		Map<String, Map<String, Map<String, String>>> coreCustomerTransactionLimits) {
        this.coreCustomerTransactionLimits = coreCustomerTransactionLimits;
    }

    private Map<String, Set<String>> globalLevelPermissions = new HashMap<>();
    private Map<String, Set<String>> accountLevelPermissions = new HashMap<>();
    private Map<String, Set<String>> transactionLimits = new HashMap<>();

    private Map<String, Set<String>> coreCustomerGlobalLevelPermissions = new HashMap<>();
    private Map<String, Map<String, Map<String, Set<String>>>> coreCustomerAccountLevelPermissions = new HashMap<>();
    Map<String, Map<String, Map<String, String>>> coreCustomerTransactionLimits = new HashMap<>();

    Map<String, Map<String, Set<String>>> accountLevelPermissionsForContract = new HashMap<>();
    Map<String, Set<String>> globalLevelPermissionsForContract= new HashMap<>();
    
    public Map<String, Set<String>> getFeatureaction() {
        return featureaction;
    }

    public void setFeatureaction(Map<String, Set<String>> featureaction) {
        this.featureaction = featureaction;
    }

    public Map<String, JsonObject> getFeatureInfo() {
        return featureInfo;
    }

    public void setFeatureInfo(Map<String, JsonObject> featureInfo) {
        this.featureInfo = featureInfo;
    }

    public Map<String, JsonObject> getActionsInfo() {
        return actionsInfo;
    }

    public void setActionsInfo(Map<String, JsonObject> actionsInfo) {
        this.actionsInfo = actionsInfo;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Map<String, Map<String, Map<String, String>>> getMonetaryActionLimits() {
        return monetaryActionLimits;
    }

    public void setMonetaryActionLimits(Map<String, Map<String, Map<String, String>>> monetaryActionLimits) {
        this.monetaryActionLimits = monetaryActionLimits;
    }

	public Map<String, Map<String, Map<String, String>>> getNewMonetaryActionLimits() {
		return newMonetaryActionLimits;
	}

	public void setNewMonetaryActionLimits(Map<String, Map<String, Map<String, String>>> newMonetaryActionLimits) {
		this.newMonetaryActionLimits = newMonetaryActionLimits;
	}

	public Map<String, Set<String>> getNewFeatureAction() {
		return newFeatureAction;
	}

	public void setNewFeatureAction(Map<String, Set<String>> newFeatureAction) {
		this.newFeatureAction = newFeatureAction;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

	public Set<String> getAccountLevelActions() {
		return accountLevelActions;
	}

	public void setAccountLevelActions(Set<String> accountLevelActions) {
		this.accountLevelActions = accountLevelActions;
	}

	public Set<String> getGlobalLevelActions() {
		return globalLevelActions;
	}

	public void setGlobalLevelActions(Set<String> globalLevelActions) {
		this.globalLevelActions = globalLevelActions;
	}

	public Set<String> getMonetaryActions() {
		return monetaryActions;
	}

	public void setMonetaryActions(Set<String> monetaryActions) {
		this.monetaryActions = monetaryActions;
	}
	
	public Map<String, Map<String, Set<String>>> getAccountLevelPermissionsForContract() {
        return accountLevelPermissionsForContract;
        
    }

    public void setAccountLevelPermissions1(Map<String, Map<String, Set<String>>> coreCustomerAccountLevelActions) {
        this.accountLevelPermissionsForContract = coreCustomerAccountLevelActions;
        
    }

    public Map<String, Set<String>> getGlobalLevelPermissionsForContract() {
        return globalLevelPermissionsForContract;
        
    }

    public void setGlobalLevelPermissions1(Map<String, Set<String>> globalLevelPermissionsForContract) {
        this.globalLevelPermissionsForContract = globalLevelPermissionsForContract;
    }
    public Map<String, Map<String, Set<String>>> getAsscoiatedAccountActions() {
		return asscoiatedAccountActions;
	}

	public void setAsscoiatedAccountActions(Map<String, Map<String, Set<String>>> asscoiatedAccountActions) {
		this.asscoiatedAccountActions = asscoiatedAccountActions;
	}

}
