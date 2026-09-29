package com.temenos.dbx.product.organization.resource.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.MembershipBusinessDelegate;
import com.temenos.dbx.product.organization.resource.api.MembershipResource;

public class MembershipResourceImpl implements MembershipResource {

    @Override
    public Result getMembershipDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        final String INPUT_MEMBERSHIP = "membershipId";
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String membershipId =
                    StringUtils.isNotBlank(inputParams.get(INPUT_MEMBERSHIP)) ? inputParams.get(INPUT_MEMBERSHIP)
                            : dcRequest.getParameter(INPUT_MEMBERSHIP);
            if (StringUtils.isBlank(membershipId)) {
                ErrorCodeEnum.ERR_10262.setErrorCode(result);
                return result;
            }
            MembershipBusinessDelegate membershipBD = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(MembershipBusinessDelegate.class);
            MembershipDTO dto = membershipBD.getMembershipDetails(membershipId, dcRequest.getHeaderMap());
            String membershipJsonString = JSONUtils.stringify(dto);
            if (null != dto && null != dto.getAddressDTO()) {
                AddressDTO addressDTO = dto.getAddressDTO();
                String addressJsonString = JSONUtils.stringify(addressDTO);
            }
            JsonParser parser = new JsonParser();

            JSONObject membershipJson = new JSONObject(membershipJsonString);
            JSONObject addressJson = membershipJson.has("addressDTO") && null != membershipJson.get("addressDTO")
                    ? membershipJson.getJSONObject("addressDTO")
                    : null;
            if (null != addressJson) {
                membershipJson.remove("addressDTO");
                JSONArray array = new JSONArray();
                array.put(addressJson);
                membershipJson.put("Address", array);
            }
            result = JSONToResult.convert(membershipJson.toString());

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10238);
        }
        return result;
    }

}
