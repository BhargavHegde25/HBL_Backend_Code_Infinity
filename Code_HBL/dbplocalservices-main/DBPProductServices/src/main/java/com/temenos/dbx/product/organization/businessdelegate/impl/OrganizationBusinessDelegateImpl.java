package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.dto.BusinessTypeDTO;
import com.temenos.dbx.product.dto.OrganisationDTO;
import com.temenos.dbx.product.dto.OrganizationMembershipDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

/**
 * 
 * @author Infinity DBX
 *
 */
public class OrganizationBusinessDelegateImpl implements OrganizationBusinessDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<OrganisationDTO> getListOfOrganisationsByStatus(String statusId, Map<String, Object> headerMap)
            throws ApplicationException {
        List<OrganisationDTO> dtoList = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = "StatusId" + DBPUtilitiesConstants.EQUAL + statusId;

            inputParams.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject organisationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.ORGANISATION_GET);

            if (JSONUtil.isJsonNotNull(organisationJson)
                    && JSONUtil.hasKey(organisationJson, DBPDatasetConstants.DATASET_ORGANISATION)
                    && organisationJson.get(DBPDatasetConstants.DATASET_ORGANISATION).isJsonArray()) {
                JsonArray organisationArray = organisationJson.get(DBPDatasetConstants.DATASET_ORGANISATION)
                        .getAsJsonArray();

                for (JsonElement element : organisationArray) {
                    if (element.isJsonObject()) {
                        OrganisationDTO dto = (OrganisationDTO) DTOUtils
                                .loadJsonObjectIntoObject(element.getAsJsonObject(), OrganisationDTO.class, true);
                        if (null != dto) {
                            updateBusinessType(dto, headerMap);
                            dtoList.add(dto);
                        }

                    }

                }
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10225);
        }

        return dtoList;
    }

    private void updateBusinessType(OrganisationDTO dto, Map<String, Object> headerMap) throws ApplicationException {
        try {
            String businessTypeId = dto.getBusinessTypeId();
            if (StringUtils.isBlank(businessTypeId)) {
                return;
            }
            BusinessTypeBusinessDelegate businessTypeBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(BusinessTypeBusinessDelegate.class);

            List<BusinessTypeDTO> dtoList = businessTypeBD.getBusinessType(headerMap);
            for (BusinessTypeDTO businessTypeDTO : dtoList) {
                if (businessTypeId.equalsIgnoreCase(businessTypeDTO.getId())) {
                    dto.setBusinessType(businessTypeDTO);
                    return;
                }
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        }

    }

    @Override
    public boolean updateOrganizationStatus(OrganisationDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException {

        boolean isUpdateSuccess = false;
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("id", inputDTO.getId());
        inputParams.put("StatusId", inputDTO.getStatusId());
        if (StringUtils.isNotBlank(inputDTO.getRejectedReason())) {
            inputParams.put("rejectedReason", inputDTO.getRejectedReason());
        }
        if (StringUtils.isNotBlank(inputDTO.getRejectedby())) {
            inputParams.put("rejectedby", inputDTO.getRejectedby());
        }
        if (StringUtils.isNotBlank(inputDTO.getRejectedts())) {
            inputParams.put("rejectedts", inputDTO.getRejectedts());
        }
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.ORGANISATION_UPDATE);
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating organisation status :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10263);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("updatedRecords")) {
            alert.prepareError("Exception occured while updating organisation status :" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10263);
        }

        if (response.get("updatedRecords").getAsInt() == 1) {
            isUpdateSuccess = true;
        }
        return isUpdateSuccess;
    }

    @Override
    public List<OrganizationMembershipDTO> getOrganizationMembershipDetails(OrganizationMembershipDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        List<OrganizationMembershipDTO> orgmemberDTOList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();

        inputParams.put(DBPUtilitiesConstants.FILTER,
                "Organization_id" + DBPUtilitiesConstants.EQUAL + inputDTO.getOrganisationId());

        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATIONMEMBERSHIP_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching organization membership details :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10268);
        }
        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0
                || !response.has("organisationmembership")) {
            alert.prepareError("Exception occured while fetching organization membership details :" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10268);
        }
        if (response.get("organisationmembership").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10269);
        }
        try {
            for (JsonElement dto : response.get("organisationmembership").getAsJsonArray()) {
                orgmemberDTOList.add((OrganizationMembershipDTO) DTOUtils
                        .loadJsonObjectIntoObject(dto.getAsJsonObject(), OrganizationMembershipDTO.class, true));
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10268);
        }

        return orgmemberDTOList;
    }

    @Override
    public OrganisationDTO getOrganization(OrganisationDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {

        OrganisationDTO orgDTO = new OrganisationDTO();
        Map<String, Object> inputParams = new HashMap<>();

        inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + inputDTO.getId());
        JsonObject response = new JsonObject();
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATION_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the organisation :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10273);
        }
        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("organisation")) {
            alert.prepareError("Exception occured while fetching organization membership details :" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10273);
        }
        if (response.get("organisation").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10274);
        }
        try {
            for (JsonElement dto : response.get("organisation").getAsJsonArray()) {
                orgDTO = ((OrganisationDTO) DTOUtils.loadJsonObjectIntoObject(dto.getAsJsonObject(),
                        OrganisationDTO.class, true));
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10273);
        }
        return orgDTO;
    }

    @Override
    public OrganisationDTO createOrganization(OrganisationDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        OrganisationDTO dto = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getName())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10282);
        }

        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);

            HelperMethods.removeNullValues(inputParams);
            JsonObject organisationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATION_CREATE);
            if (JSONUtil.isJsonNotNull(organisationJson)
                    && JSONUtil.hasKey(organisationJson, DBPDatasetConstants.DATASET_ORGANISATION)
                    && organisationJson.get(DBPDatasetConstants.DATASET_ORGANISATION).isJsonArray()) {
                JsonArray organisationArray = organisationJson.get(DBPDatasetConstants.DATASET_ORGANISATION)
                        .getAsJsonArray();
                JsonObject object = organisationArray.size() > 0 ? organisationArray.get(0).getAsJsonObject()
                        : new JsonObject();
                dto = (OrganisationDTO) DTOUtils.loadJsonObjectIntoObject(object, OrganisationDTO.class, true);
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10284);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10284);
        }
        return dto;
    }

    @Override
    public OrganisationDTO getOrganizationByName(String organizationName, Map<String, Object> headersMap)
            throws ApplicationException {

        OrganisationDTO dto = null;
        try {
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put(DBPUtilitiesConstants.FILTER, "Name" + DBPUtilitiesConstants.EQUAL + organizationName);
            JsonObject organisationJson = null;
            organisationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATION_GET);
            if (JSONUtil.isJsonNotNull(organisationJson)
                    && JSONUtil.hasKey(organisationJson, DBPDatasetConstants.DATASET_ORGANISATION)
                    && organisationJson.get(DBPDatasetConstants.DATASET_ORGANISATION).isJsonArray()) {
                JsonArray organisationArray = organisationJson.get(DBPDatasetConstants.DATASET_ORGANISATION)
                        .getAsJsonArray();
                JsonObject object = organisationArray.size() > 0 ? organisationArray.get(0).getAsJsonObject()
                        : new JsonObject();
                dto = (OrganisationDTO) DTOUtils.loadJsonObjectIntoObject(object, OrganisationDTO.class, true);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10273);
        }

        return dto;
    }

}
