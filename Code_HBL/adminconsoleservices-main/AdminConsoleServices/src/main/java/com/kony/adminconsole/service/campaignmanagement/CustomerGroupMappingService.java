package com.kony.adminconsole.service.campaignmanagement;

import com.hbl.adminconsole.getlistcache.GetListCacheInvalidator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to add customers to groups in 'customergroup' table in the back end ---- >>>> FOR TEMPORARY USE ONLY! ----
 * ---- <<<< ----
 * 
 * @author Sowmya Mortha (KH2256)
 */

public class CustomerGroupMappingService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            return invokeOperation(methodID, inputArray, requestInstance, responseInstance);
        } finally {
            // Online-banking getList cache: this operation changes data getList returns. Runs even
            // after a part-way failure, because some rows may already be written.
            GetListCacheInvalidator.permissionsChanged();
        }
    }

    private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();

        try {
            JSONArray customers = new JSONArray(requestInstance.getParameter("customers"));
            String groupId = requestInstance.getParameter("groupId");
            
            if(StringUtils.isEmpty(groupId.trim())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_21729);
            }
            if(customers.length() == 0) {
                throw new ApplicationException(ErrorCodeEnum.ERR_21863);
            }
            
            Dataset existingCustomers = new Dataset("existingCustomers");
            boolean isExisingCustomer = false;
            String customerId = null;
            
            for (int i = 0; i < customers.length(); i++) {
                addCustomersToCampaignGroups(requestInstance, customers.optString(i), groupId);
                customerId = customers.optString(i) ;
                isExisingCustomer = isCustomerExistingToGroup(requestInstance, customerId , groupId);
                if(isExisingCustomer) {
                    // customer exists
                    Record record = new Record();
                    record.addParam("customerId", customerId);
                    existingCustomers.addRecord(record);
                } else {
                    addCustomersToCampaignGroups(requestInstance, customerId, groupId);
                }
            }
            if(existingCustomers.getAllRecords().size() > 0) {
                result.addDataset(existingCustomers);
                result.addParam(FabricConstants.OPSTATUS, "-1");
            }

        } catch (ApplicationException ae) {
            ae.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("ApplicationException occured in CustomerGroupMappingService JAVA service. Error: ", ae).log();
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            alert.prepareError("Exception occured in CustomerGroupMappingService JAVA service. Error: ", e).log();
        }

        return result;
    }

    private boolean isCustomerExistingToGroup(DataControllerRequest requestInstance,
            String customerId, String groupId) throws ApplicationException {
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerId + "' and Group_id eq '" + groupId + "'");

        String readCustomerGroupResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERGROUP_READ, inputMap, null,
                requestInstance);
        JSONObject readCustomerGroupResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerGroupResponse);
        
        if (readCustomerGroupResponseJSON == null || !readCustomerGroupResponseJSON.has(FabricConstants.OPSTATUS)
                || readCustomerGroupResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_21865);
        }
        JSONArray readCustomerGroupJSONArray = readCustomerGroupResponseJSON.optJSONArray("customergroup");
        return readCustomerGroupJSONArray.length() > 0 ;
    }

    private void addCustomersToCampaignGroups(DataControllerRequest requestInstance,
            String customerId, String groupId) throws ApplicationException {

        Map<String, String> campaignGroupMap = new HashMap<>();
        campaignGroupMap.put("Customer_id", customerId);
        campaignGroupMap.put("Group_id", groupId);
        campaignGroupMap.put("createdby", "T24");

        String response =
                Executor.invokeService(ServiceURLEnum.CUSTOMERGROUP_CREATE, campaignGroupMap, null, requestInstance);

        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);

        if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
                || responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_21864);
        }

    }

}