package com.kony.adminconsole.service.termandcondition.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface TnCBusinessDelegate extends BusinessDelegate {

    public Result createTermsAndConditionsVersion(Map<String, Object> postParametersMap, String backendToken)
            throws Exception;
    
    public Result editTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
    
    public Result editTermsAndConditionsDBXDB(Map<String, Object> postParametersMap, String backendToken, UserDetailsBean loggedInUserDetails)
            throws DBPApplicationException;
    
    public Result deleteTermsAndConditionsVersion(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
    
    public Result getAllTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
    
    public Result getAllTermsAndConditions(Map<String, Object> postParametersMap, String backendToken, DataControllerRequest requestInstance, boolean isDBXDBIntegrated)
            throws DBPApplicationException;
    
    public Result getTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
    
	public Result getRequiredTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException;
	
	public Result deleteTermsAndConditionsVersionDBXDB(Map<String, Object> postParametersMap)
            throws DBPApplicationException;

}
