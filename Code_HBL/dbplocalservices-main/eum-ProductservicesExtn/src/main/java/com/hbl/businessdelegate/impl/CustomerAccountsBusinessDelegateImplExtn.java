package com.hbl.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hbl.backenddeligate.api.CustomerAccountsBusinessDelegateExtn;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerAccountsBusinessDelegateImpl;

public class CustomerAccountsBusinessDelegateImplExtn extends CustomerAccountsBusinessDelegateImpl implements CustomerAccountsBusinessDelegateExtn{
	LoggerUtil logger = new LoggerUtil(CustomerAccountsBusinessDelegateImplExtn.class);
	@Override
	    public void createCustomerAccounts(String userId, String contractId, String coreCustomerId, String legalEntityId, Set<String> accounts,
	            Map<String, Object> headersMap, String defaultAccount) throws ApplicationException {

	        Map<String, Object> inputParams = new HashMap<>();
	        StringBuilder accountsCSV = new StringBuilder();
	        for (String account : accounts) {
	            accountsCSV.append(account).append(DBPUtilitiesConstants.COMMA_SEPERATOR);
	        }
	        if (accountsCSV.length() > 0)
	            accountsCSV.replace(accountsCSV.length() - 1, accountsCSV.length(), "");
	        
	        try {
	            inputParams.put("_userId", userId);
	            inputParams.put("_accountsCSV", accountsCSV.toString());
	            inputParams.put("_coreCustomerId", coreCustomerId);
	            inputParams.put("_contractId", contractId);
	            inputParams.put("_legalEntityId", legalEntityId);
	            inputParams.put("_defaultAccount", defaultAccount);
	            logger.debug("HBL::CustomerAccountsBusinessDelegateImplExtn:createCustomerAccounts:inputParams:"+inputParams);
	            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
	                    URLConstants.USERACCOUNTS_CREATE_PROC);

	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10387);
	        }

	    }
}
