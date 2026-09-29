package com.temenos.dbx.product.usermanagement.resource.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerPreferenceResource;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerPreferenceResourceImpl implements CustomerPreferenceResource {

    @Override
    public Result updateCustomerPreference(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO =
                (CustomerPreferenceDTO) new CustomerPreferenceDTO().loadDTO(customerId);
        if (customerPreferenceDTO == null) {
            customerPreferenceDTO = new CustomerPreferenceDTO();
            customerPreferenceDTO.setId(HelperMethods.getNewId());
            customerPreferenceDTO.setCustomer_id(customerId);
            customerPreferenceDTO.setIsNew(true);
        } else {
            customerPreferenceDTO.setIsChanged(true);
        }

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        HelperMethods.removeNullValues(inputParams);
        DTOUtils.loadInputIntoDTO(customerPreferenceDTO, inputParams, true);

        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        DBXResult response = customerPreferenceBusinessDelegate.update(customerPreferenceDTO, dcRequest.getHeaderMap());

        if (StringUtils.isBlank((String) response.getResponse())) {
            HelperMethods.setValidationMsg("Preference update failed.", dcRequest, result);
        }

        return result;
    }

    @Override
    public Result getisP2pActivated(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setCustomer_id(customerId);
        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);
        DBXResult response = customerPreferenceBusinessDelegate.get(customerPreferenceDTO, dcRequest.getHeaderMap());
        customerPreferenceDTO = (CustomerPreferenceDTO) response.getResponse();
        String message = "Not Eligible";
        if (customerPreferenceDTO != null) {
            if (Boolean.parseBoolean(customerPreferenceDTO.getIsP2PSupported())) {
                if (Boolean.parseBoolean(customerPreferenceDTO.getIsP2PActivated())) {
                    message = "Activated";
                } else {
                    message = "Not Activated";
                }
            }
        }

        result.addParam(new Param("result", message, DBPUtilitiesConstants.STRING_TYPE));
        return result;
    }

    @Override
    public Result activateBillPay(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO =
                (CustomerPreferenceDTO) new CustomerPreferenceDTO().loadDTO(customerId);
        if (customerPreferenceDTO == null) {
            customerPreferenceDTO = new CustomerPreferenceDTO();
            customerPreferenceDTO.setId(HelperMethods.getNewId());
            customerPreferenceDTO.setCustomer_id(customerId);
            customerPreferenceDTO.setIsNew(true);
        } else {
            customerPreferenceDTO.setIsChanged(true);
        }

        customerPreferenceDTO.setIsBillPayActivated("true");
        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        DBXResult response = customerPreferenceBusinessDelegate.update(customerPreferenceDTO, dcRequest.getHeaderMap());

        if (StringUtils.isBlank((String) response.getResponse())) {
            HelperMethods.setValidationMsg("Preference update failed.", dcRequest, result);
        }

        return result;
    }

    @Override
    public Result deActivateP2P(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO =
                (CustomerPreferenceDTO) new CustomerPreferenceDTO().loadDTO(customerId);
        if (customerPreferenceDTO == null) {
            customerPreferenceDTO = new CustomerPreferenceDTO();
            customerPreferenceDTO.setId(HelperMethods.getNewId());
            customerPreferenceDTO.setCustomer_id(customerId);
            customerPreferenceDTO.setIsNew(true);
        } else {
            customerPreferenceDTO.setIsChanged(true);
        }

        customerPreferenceDTO.setIsP2PActivated("false");
        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        DBXResult response = customerPreferenceBusinessDelegate.update(customerPreferenceDTO, dcRequest.getHeaderMap());

        if (StringUtils.isBlank((String) response.getResponse())) {
            HelperMethods.setValidationMsg("Preference update failed.", dcRequest, result);
        }

        return result;
    }

    @Override
    public Result activateP2P(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO =
                (CustomerPreferenceDTO) new CustomerPreferenceDTO().loadDTO(customerId);
        if (customerPreferenceDTO == null) {
            customerPreferenceDTO = new CustomerPreferenceDTO();
            customerPreferenceDTO.setId(HelperMethods.getNewId());
            customerPreferenceDTO.setCustomer_id(customerId);
            customerPreferenceDTO.setIsNew(true);
        } else {
            customerPreferenceDTO.setIsChanged(true);
        }

        customerPreferenceDTO.setIsP2PActivated("true");
        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        DBXResult response = customerPreferenceBusinessDelegate.update(customerPreferenceDTO, dcRequest.getHeaderMap());

        if (StringUtils.isBlank((String) response.getResponse())) {
            HelperMethods.setValidationMsg("Preference update failed.", dcRequest, result);
        }

        return result;
    }

    @Override
    public Result updatePreferredDbxP2PAccounts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO =
                (CustomerPreferenceDTO) new CustomerPreferenceDTO().loadDTO(customerId);
        if (customerPreferenceDTO == null) {
            customerPreferenceDTO = new CustomerPreferenceDTO();
            customerPreferenceDTO.setId(HelperMethods.getNewId());
            customerPreferenceDTO.setCustomer_id(customerId);
            customerPreferenceDTO.setIsNew(true);
        } else {
            customerPreferenceDTO.setIsChanged(true);
        }

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (StringUtils.isNotBlank(inputParams.get("default_from_account_p2p"))) {
            customerPreferenceDTO.setDefaultFromAccountP2P(inputParams.get("default_from_account_p2p"));
        }
        if (StringUtils.isNotBlank(inputParams.get("default_to_account_p2p"))) {
            customerPreferenceDTO.setDefaultToAccountP2P(inputParams.get("default_to_account_p2p"));
        }

        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        DBXResult response = customerPreferenceBusinessDelegate.update(customerPreferenceDTO, dcRequest.getHeaderMap());

        if (StringUtils.isBlank((String) response.getResponse())) {
            HelperMethods.setValidationMsg("Preference update failed.", dcRequest, result);
        }

        return result;
    }

    @Override
    public Result checkBillPayEligibilityForUser(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setCustomer_id(customerId);
        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);
        DBXResult response = customerPreferenceBusinessDelegate.get(customerPreferenceDTO, dcRequest.getHeaderMap());
        customerPreferenceDTO = (CustomerPreferenceDTO) response.getResponse();
        String message = "Not Eligible";
        if (customerPreferenceDTO != null) {
            if (Boolean.parseBoolean(customerPreferenceDTO.getIsBillPaySupported())) {
                if (Boolean.parseBoolean(customerPreferenceDTO.getIsBillPayActivated())) {
                    message = "Activated";
                } else {
                    message = "Not Activated";
                }
            }
        }
        result.addParam(new Param("result", message, DBPUtilitiesConstants.STRING_TYPE));
        return result;
    }

    @Override
    public Object getPreferencesForUserResponse(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        CustomerPreferenceBusinessDelegate impl =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String id = inputParams.get("Customer_id");

        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setCustomer_id(id);
        DBXResult dbxResult = impl.getPreferencesForUserResponse(customerPreferenceDTO, dcRequest.getHeaderMap());
        if (dbxResult.getResponse() != null) {

            Record record = new Record();

            Map<String, String> map = (Map<String, String>) dbxResult.getResponse();

            for (Entry<String, String> entry : map.entrySet()) {
                record.addParam(entry.getKey(), entry.getValue());
            }

            record.setId("CustomerPreferences");
            result.addRecord(record);

        }

        return result;
    }

    @Override
    public Object getPreferencesForLogin(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String customer_id = inputParams.get("id");

        if (StringUtils.isBlank(customer_id)) {
            return result;
        }
        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setCustomer_id(customer_id);
        CustomerPreferenceBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);
        DBXResult dbxResult = businessDelegate.getPreferencesForLogin(customerPreferenceDTO, dcRequest.getHeaderMap());
        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            result = JSONToResult.convert(jsonObject.toString());
        }

        return result;
    }
}
