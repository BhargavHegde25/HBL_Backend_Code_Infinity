package com.temenos.dbx.product.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class AddressBackendDelegateImpl implements AddressBackendDelegate {

    @Override
    public List<CustomerAddressViewDTO> getCustomerAddress(String customerId, Map<String, Object> headerMap)
            throws ApplicationException {
        List<CustomerAddressViewDTO> dtoList = new ArrayList<>();
        try {
            if (StringUtils.isBlank(customerId)) {
                return dtoList;
            }

            Map<String, Object> inputParams = new HashMap<>();
            String filter = "CustomerId" + DBPUtilitiesConstants.EQUAL + customerId;
            String select =
                    "Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary";
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            inputParams.put(DBPUtilitiesConstants.SELECT, select);

            JsonObject addressJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CUSTOMER_ADDRESS_VIEW_GET);

            if (JSONUtil.isJsonNotNull(addressJson)
                    && JSONUtil.hasKey(addressJson, DBPDatasetConstants.DATASET_CUSTOMERADDRESS_VIEW)
                    && addressJson.get(DBPDatasetConstants.DATASET_CUSTOMERADDRESS_VIEW).isJsonArray()) {
                JsonArray addressArray =
                        addressJson.get(DBPDatasetConstants.DATASET_CUSTOMERADDRESS_VIEW).getAsJsonArray();

                for (JsonElement element : addressArray) {
                    if (element.isJsonObject()) {
                        CustomerAddressViewDTO dto =
                                (CustomerAddressViewDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                                        CustomerAddressViewDTO.class, true);
                        if (null != dto) {

                            if (dto.getCityId() != null && StringUtils.isBlank(dto.getCityName())) {
                                dto.setCityName(getCityName(dto.getCityId(), headerMap));
                            }

                            dtoList.add(dto);
                        }

                    }

                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10224);

        }

        return dtoList;
    }

    private String getCityName(String cityId, Map<String, Object> headerMap) {

        String filter = "id" + DBPUtilitiesConstants.EQUAL + cityId;
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject jsonObject =
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.CITY_GET);

        if (jsonObject.has(DBPDatasetConstants.DATASET_CITY)) {
            JsonElement array = jsonObject.get(DBPDatasetConstants.DATASET_CITY);
            JsonArray cities = !array.isJsonNull() && array.isJsonArray() ? array.getAsJsonArray() : new JsonArray();

            if (cities.size() > 0) {
                JsonElement city = cities.get(0).getAsJsonObject().get("Name");
                return !city.isJsonNull() ? city.getAsString() : null;
            }
        }

        return null;
    }

    @Override
    public DBXResult getCustomerAddressForUserResponse(CustomerAddressViewDTO addressDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        List<CustomerAddressViewDTO> customerAddressDTOs = (List<CustomerAddressViewDTO>) addressDTO.loadDTO();

        if (customerAddressDTOs != null && customerAddressDTOs.size() > 0) {

            for (CustomerAddressViewDTO dto : customerAddressDTOs) {

                if (dto.getCityId() != null && StringUtils.isBlank(dto.getCityName())) {
                    dto.setCityName(getCityName(dto.getCityId(), headerMap));
                }
            }

            dbxResult.setResponse(customerAddressDTOs);

        }

        return dbxResult;
    }

    @Override
    public AddressDTO getAddressDetails(String addressId, Map<String, Object> headerMap)
            throws ApplicationException {
        AddressDTO dto = null;
        try {
            if (StringUtils.isBlank(addressId)) {
                return null;
            }
            Map<String, Object> inputParams = new HashMap<>();
            String filter = "id" + DBPUtilitiesConstants.EQUAL + addressId;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject addressJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.ADDRESS_GET);
            if (JSONUtil.isJsonNotNull(addressJson)
                    && JSONUtil.hasKey(addressJson, DBPDatasetConstants.DATASET_ADDRESS)
                    && addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).isJsonArray()) {
                JsonArray addressArray =
                        addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).getAsJsonArray();

                JsonElement element = addressArray.size() > 0 ? addressArray.get(0) : new JsonObject();
                dto = (AddressDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                        AddressDTO.class, true);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10261);
        }
        return dto;
    }

}
