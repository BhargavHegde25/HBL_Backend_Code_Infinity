package com.temenos.dbx.party.resource.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.businessdelegate.api.UpdateCustomerApplicationBusinessDelegate;
import com.temenos.dbx.party.resource.api.UpdateCustomerApplicationResource;
import com.temenos.dbx.product.dto.CustomerApplicationDTO;

public class UpdateCustomerApplicationResourceImpl implements UpdateCustomerApplicationResource {

    @Override
    public Result updateCustomerApplication(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);
        Result result = new Result();
        String partyId = inputMap.get("partyID");
        String customerId = inputMap.get("id");
        String coreCustomerId = inputMap.get("coreCustomerID");
        if (StringUtils.isBlank(partyId)) {
            partyId = dcRequest.getParameter("partyID");
        }
        if (StringUtils.isBlank(customerId)) {
            customerId = dcRequest.getParameter("id");
        }
        if (StringUtils.isBlank(coreCustomerId)) {
            coreCustomerId = dcRequest.getParameter("coreCustomerID");
        }

        if (StringUtils.isBlank(partyId)) {
            ErrorCodeEnum.ERR_12607.setErrorCode(result);
        } else {
            CustomerApplicationDTO dto = new CustomerApplicationDTO();
            dto.setPartyId(partyId);
            dto.setCustomerId(customerId);
            dto.setCoreCustomerId(coreCustomerId);
            UpdateCustomerApplicationBusinessDelegate updateBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(UpdateCustomerApplicationBusinessDelegate.class);
            JsonObject object =
                    updateBD.updateCustomerApplication(dto, URLConstants.CUSTOMERAPPLICATION_UPDATE,
                            URLConstants.CUSTOMERAPPLICATION_GET, HelperMethods.getHeaders(dcRequest));
            if (!JSONUtil.hasKey(object, DBPUtilitiesConstants.VALIDATION_ERROR)) {
                Param param = new Param("result", "update successful", DBPUtilitiesConstants.STRING_TYPE);
                result.addParam(param);
            } else {
                ErrorCodeEnum.ERR_12608.setErrorCode(result);
            }

        }

        return result;
    }

}
