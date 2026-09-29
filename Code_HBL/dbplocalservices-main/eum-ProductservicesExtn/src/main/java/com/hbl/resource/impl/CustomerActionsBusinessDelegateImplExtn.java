package com.hbl.resource.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.hbl.resource.constants.HBLConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerActionsBusinessDelegateImpl;

public class CustomerActionsBusinessDelegateImplExtn extends CustomerActionsBusinessDelegateImpl{
	LoggerUtil logger = new LoggerUtil(CustomerActionsBusinessDelegateImplExtn.class);
	@Override
    public void createCustomerLimitGroupLimits(String userId, String contractId, String coreCustomerId, String legalEntityId,
            Map<String, Object> headersMap) throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();

        try {
            inputParams.put("_userId", userId);
            inputParams.put("_coreCustomerId", coreCustomerId);
            inputParams.put("_contractId", contractId);
            inputParams.put("_legalEntityId", legalEntityId);
            logger.debug("HBL::CustomerActionsBusinessDelegateImplExtn: createCustomerLimitGroupLimits:"+inputParams.toString());
           JsonObject res = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    HBLConstants.USER_LIMITGROUP_LIMITS_CREATE_PROC);
           logger.debug("HBL::CustomerActionsBusinessDelegateImplExtn: createCustomerLimitGroupLimits: reponse:"+inputParams.toString());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10402);
        }

    }

}
