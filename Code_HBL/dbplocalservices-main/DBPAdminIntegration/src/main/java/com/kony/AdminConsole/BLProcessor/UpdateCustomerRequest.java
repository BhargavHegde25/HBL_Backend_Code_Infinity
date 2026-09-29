package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

public class UpdateCustomerRequest implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Map<String, Object> inputParams = CommonUtilities.getInputMapFromInputArray(inputArray);

        String requestid = requestInstance.getParameter("requestid");
        String harddelete = requestInstance.getParameter("harddelete");
        String softdelete = requestInstance.getParameter("softdelete");
        String markallasread = requestInstance.getParameter("markallasread");
        String Priority = requestInstance.getParameter("Priority");
        String requestcategory_id = requestInstance.getParameter("requestcategory_id");
        String Status = requestInstance.getParameter("Status");
        String modifiedby = (String) inputParams.get("modifiedby");
		if (StringUtils.isBlank(modifiedby)) {
			modifiedby = CustomerSessionsUtil.getLoggedInUserAttributesMap(requestInstance)
					.get(DBPUtilitiesConstants.PARAM_USERNAME).toString();
		}
        String Requestsubject = requestInstance.getParameter("Requestsubject");
        String accountid = requestInstance.getParameter("accountid");
        JSONObject getResponse = getCustomerRequests(requestid, harddelete, modifiedby, softdelete, markallasread,
                requestcategory_id, Priority, Status, Requestsubject, accountid, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getCustomerRequests(requestid, harddelete, modifiedby, softdelete, markallasread,
                    requestcategory_id, Priority, Status, Requestsubject, accountid, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject getCustomerRequests(String requestid, String harddelete, String modifiedby, String softdelete,
            String markallasread, String requestcategory_id, String Priority, String Status, String Requestsubject,
            String accountid, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("requestid", requestid);
        postParametersMap.put("modifiedby", modifiedby);
        postParametersMap.put("username", modifiedby);

        if (harddelete != null) {
            postParametersMap.put("harddelete", harddelete);
        }
        if (markallasread != null) {
            postParametersMap.put("markallasread", markallasread);
        }
        if (softdelete != null) {
            postParametersMap.put("softdelete", softdelete);
        }
        if (requestcategory_id != null) {
            postParametersMap.put("requestcategory_id", requestcategory_id);
        }
        if (Priority != null) {
            postParametersMap.put("Priority", Priority);
        }
        if (Status != null) {
            postParametersMap.put("Status", Status);
        }
        if (Requestsubject != null) {
            postParametersMap.put("Requestsubject", Requestsubject);
        }
        if (accountid != null) {
            postParametersMap.put("accountid", accountid);
        }

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "updateCustomerRequestURL");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
