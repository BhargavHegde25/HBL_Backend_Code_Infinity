package com.kony.adminconsole.service.campaignmanagement;

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
 * Service to delete customers to groups in 'customergroup' table in the back end ---- >>>> FOR TEMPORARY USE ONLY! ----
 * 
 * @author Sowmya Mortha (KH2256)
 */

public class CustomerGroupUnMappingService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
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
            
            boolean isExisingCustomer = false;
            String customerId = null;
            Dataset unmappedCustomers = new Dataset("unmappedCustomers");
            for (int i = 0; i < customers.length(); i++) {
                customerId = customers.optString(i) ;
                isExisingCustomer = isCustomerExistingToGroup(requestInstance, customerId , groupId);
                if(isExisingCustomer) {
                    deleteCustomersToCampaignGroups(requestInstance, customerId, groupId);
                } else {
                 // customer does not exists
                    Record record = new Record();
                    record.addParam("customerId", customerId);
                    unmappedCustomers.addRecord(record);
                }
            }
            if(unmappedCustomers.getAllRecords().size() > 0) {
                result.addDataset(unmappedCustomers);
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

    private void deleteCustomersToCampaignGroups(DataControllerRequest requestInstance,
            String customerId, String groupId) throws ApplicationException {

        Map<String, String> campaignGroupMap = new HashMap<>();
        campaignGroupMap.put("Customer_id", customerId);
        campaignGroupMap.put("Group_id", groupId);

        String response =
                Executor.invokeService(ServiceURLEnum.CUSTOMERGROUP_DELETE, campaignGroupMap, null, requestInstance);

        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);

        if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
                || responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            alert.prepareError("Error in deleting customer in CustomerGroupUnMappingService service" + responseJSON).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21866);
        }

    }

}