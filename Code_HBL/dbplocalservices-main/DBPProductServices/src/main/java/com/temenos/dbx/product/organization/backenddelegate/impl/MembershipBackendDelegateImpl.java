package com.temenos.dbx.product.organization.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.organization.backenddelegate.api.MembershipBackendDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class MembershipBackendDelegateImpl implements MembershipBackendDelegate {

    @Override
    public MembershipDTO getMembershipDetails(String membershipId, Map<String, Object> headerMap)
            throws ApplicationException {
        MembershipDTO dto = null;
        JsonObject membershipJson = null;
        try {
            if (StringUtils.isBlank(membershipId)) {
                return null;
            }
            final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
            Map<String, Object> inputParams = new HashMap<>();
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                inputParams.put("Cif", membershipId);
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.GET_COMPANY_DETAILS;
                headerMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headerMap);
            } else {
                String filter = "id" + DBPUtilitiesConstants.EQUAL + membershipId;
                inputParams.put(DBPUtilitiesConstants.FILTER, filter);
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                        URLConstants.MEMBERSHIP_GET);
            }
            if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_MEMBERSHIP)
                    && membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()) {
                JsonArray array = membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                dto = (MembershipDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), MembershipDTO.class,
                        false);
            }
            if (null != dto && StringUtils.isNotBlank(dto.getAddressId())) {
                AddressBusinessDelegate addressBD = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(AddressBusinessDelegate.class);
                AddressDTO addressDTO = addressBD.getAddressDetails(dto.getAddressId(), headerMap);
                dto.setAddressDTO(addressDTO);
            } else if (null != dto && StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.GET_COMPANY_ADDRESS;
                headerMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                JsonObject addressJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headerMap);

                if (JSONUtil.isJsonNotNull(addressJson)
                        && JSONUtil.hasKey(addressJson, DBPDatasetConstants.DATASET_ADDRESS)
                        && addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).isJsonArray()) {
                    JsonArray addressArray =
                            addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).getAsJsonArray();

                    JsonElement element = addressArray.size() > 0 ? addressArray.get(0) : new JsonObject();
                    AddressDTO addressDTO = (AddressDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                            AddressDTO.class, true);
                    dto.setAddressDTO(addressDTO);
                }

            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10238);
        }
        return dto;
    }

    @Override
    public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, Map<String, Object> headerMap)
            throws ApplicationException {
        MembershipDTO dto = new MembershipDTO();
        JsonObject membershipJson = null;
        try {
            if (StringUtils.isBlank(taxID) || StringUtils.isBlank(companyName)) {
                return null;
            }
            final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
            Map<String, Object> inputParams = new HashMap<>();
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                inputParams.put("Taxid", taxID);
                inputParams.put("companyName", companyName);
                String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                String operationName = OperationName.GET_COMPANYID_FROM_TAXID;
                headerMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headerMap);
                if (null != membershipJson && membershipJson.has("companyId")) {
                    dto.setId(membershipJson.get("companyId").getAsString());
                }
            } else {

                String filter = "taxId" + DBPUtilitiesConstants.EQUAL + taxID + DBPUtilitiesConstants.AND + "name"
                        + DBPUtilitiesConstants.EQUAL + companyName;
                inputParams.put(DBPUtilitiesConstants.FILTER, filter);
                membershipJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                        URLConstants.MEMBERSHIP_GET);

                if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_MEMBERSHIP)
                        && membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()) {
                    JsonArray array = membershipJson.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray();
                    JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                    dto = (MembershipDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                            MembershipDTO.class,
                            false);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10238);
        }
        return dto;
    }

}
