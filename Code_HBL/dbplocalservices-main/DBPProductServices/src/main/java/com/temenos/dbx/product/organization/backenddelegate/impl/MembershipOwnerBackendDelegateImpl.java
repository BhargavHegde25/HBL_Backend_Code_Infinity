package com.temenos.dbx.product.organization.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.organization.backenddelegate.api.MembershipOwnerBackendDelegate;

public class MembershipOwnerBackendDelegateImpl implements MembershipOwnerBackendDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<MembershipOwnerDTO> getMembershipOwnerDetails(List<MembershipOwnerDTO> inputDTOList,
            Map<String, Object> headerMap) throws ApplicationException {
        List<MembershipOwnerDTO> memOwnerDTOList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        int size = inputDTOList.size();
        for (MembershipOwnerDTO dto : inputDTOList) {
            sb.append("membershipId");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append(dto.getMembershipId());
            size--;
            if (size > 0)
                sb.append(DBPUtilitiesConstants.OR);
        }

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.MEMBERSHIPOWNER_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching membership owner details" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10270);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("membershipowner")) {
            alert.prepareError("Exception occured while fetching membership owner details" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10270);
        }
        if (response.get("membershipowner").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10271);
        }
        try {
            memOwnerDTOList = JSONUtils.parseAsList(response.get("membershipowner").toString(),
                    MembershipOwnerDTO.class);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10270);
        }

        return memOwnerDTOList;
    }

}
