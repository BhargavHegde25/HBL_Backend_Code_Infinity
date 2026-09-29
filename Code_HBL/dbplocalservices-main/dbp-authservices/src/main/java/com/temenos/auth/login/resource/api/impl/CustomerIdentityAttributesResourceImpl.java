package com.temenos.auth.login.resource.api.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.sessionmanager.SessionTokenUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.auth.login.businessdelegate.api.CustomerIdentityAttributesBusinessDelegate;
import com.temenos.auth.login.resource.api.CustomerIdentityAttributesResource;
import com.temenos.dbx.product.dto.CustomerDTO;

public class CustomerIdentityAttributesResourceImpl implements CustomerIdentityAttributesResource {

    @Override
    public Result getCustomerIdentityAttributes(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_USERNAME = "userName";
        final String INPUT_USERID = "userId";
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String userName = StringUtils.isNotBlank(inputParams.get(INPUT_USERNAME)) ? inputParams.get(INPUT_USERNAME)
                    : dcRequest.getParameter(INPUT_USERNAME);
            String userId = StringUtils.isNotBlank(inputParams.get(INPUT_USERID)) ? inputParams.get(INPUT_USERID)
                    : dcRequest.getParameter(INPUT_USERID);
            if (StringUtils.isBlank(userName) &&
            		StringUtils.isBlank(userId)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10338);
            }

            CustomerIdentityAttributesBusinessDelegate identityAttributesBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CustomerIdentityAttributesBusinessDelegate.class);
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUserName(userName);
            customerDTO.setId(userId);
            String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSession(dcRequest);
            JsonObject userAttributes = identityAttributesBD.getUserAttributes(customerDTO, dcRequest.getHeaderMap());
            JsonObject securityAttributes =
                    identityAttributesBD.getSecurityAttributes(customerDTO, dcRequest.getHeaderMap(), legalEntityId);
            securityAttributes.addProperty("session_token", SessionTokenUtil.getNewSessionToken());
            JsonObject resultJson = new JsonObject();
            resultJson.add("user_attributes", userAttributes);
            resultJson.add("security_attributes", securityAttributes);
            result = JSONToResult.convert(resultJson.toString());

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10339);
        }
        return result;
    }

}
