package com.temenos.dbx.product.organization.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.organization.backenddelegate.api.AuthorizedSignatoriesBackendDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.MembershipBusinessDelegate;

public class AuthorizedSignatoriesBackendDelegateImpl implements AuthorizedSignatoriesBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public List<MembershipOwnerDTO> getAuthorizedSignatories(String cif, Map<String, Object> headerMap)
            throws ApplicationException {
        List<MembershipOwnerDTO> dtoList = new ArrayList<>();
        MembershipDTO membershipDTO = null;
        JsonObject authorizedSignatoryJson = null;
        try {
            if (StringUtils.isBlank(cif)) {
                return dtoList;
            }
            Map<String, Object> inputParams = new HashMap<>();
            final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);

            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                inputParams.put("Cif", cif);
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.GET_AUTHORIZED_SIGNATORIES;
                headerMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                authorizedSignatoryJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headerMap);
            } else {
                String filter = "membershipId" + DBPUtilitiesConstants.EQUAL + cif;
                inputParams.put(DBPUtilitiesConstants.FILTER, filter);
                authorizedSignatoryJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                        URLConstants.MEMBERSHIPOWNER_GET);
            }
            if (null != authorizedSignatoryJson
                    && JSONUtil.hasKey(authorizedSignatoryJson, DBPDatasetConstants.DATASET_MEMBERSHIPOWNER)
                    && authorizedSignatoryJson.get(DBPDatasetConstants.DATASET_MEMBERSHIPOWNER).isJsonArray()) {
                JsonArray array = authorizedSignatoryJson.get(DBPDatasetConstants.DATASET_MEMBERSHIPOWNER)
                        .getAsJsonArray();
                dtoList = JSONUtils.parseAsList(array.toString(), MembershipOwnerDTO.class);

                MembershipBusinessDelegate membershipBD = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(MembershipBusinessDelegate.class);
                membershipDTO = membershipBD.getMembershipDetails(cif, headerMap);
                if (null != membershipDTO && StringUtils.isNotBlank(membershipDTO.getName())
                        && StringUtils.isNotBlank(membershipDTO.getTaxId())) {
                    for (MembershipOwnerDTO ownerDTO : dtoList) {
                        ownerDTO.setMembershipDTO(membershipDTO);
                    }
                }

            } else {
                diagnostic.prepareDebug("No details found from the given cif").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10230);
            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10230);
        }
        return dtoList;
    }

}
