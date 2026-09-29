package com.kony.adminconsole.service.group;

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
import com.kony.adminconsole.utilities.EnvironmentParamRead;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to fetch Group Suggestions
 *
 * @author Aditya Mankal
 * 
 */
public class GroupSuggestionsFetchService implements JavaService2 {

    private static final String GET_GROUP_SUGGESTIONS = "getGroupSuggestions";

//    private static final int FETCH_RECORDS_LIMIT = 10;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            if (StringUtils.equalsIgnoreCase(methodID, GET_GROUP_SUGGESTIONS)) {
                String searchText = StringUtils.trim(requestInstance.getParameter("searchText"));
                return getGroupSuggestions(requestInstance, searchText);
            }
            return new Result();
        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

    /**
     * Method to get a list groups who's name starts with the search text
     * 
     * @param requestInstance
     * @param searchText
     * @return Result with two datasets i.e. Customers and Groups
     * @throws ApplicationException
     */
    private Result getGroupSuggestions(DataControllerRequest requestInstance, String searchText)
            throws ApplicationException {

        Result processedResult = new Result();

        if (StringUtils.isNotBlank(searchText)) {

            // Fetch Groups
            Map<String, String> queryMap = new HashMap<>();
            queryMap.clear();
            queryMap.put(ODataQueryConstants.SELECT, "id,description");
            queryMap.put(ODataQueryConstants.ORDER_BY, "description asc");
            /*queryMap.put(ODataQueryConstants.TOP, 
            		Integer.toString(EnvironmentParamRead.getSuggestionRecordLimit(requestInstance)));*/
            queryMap.put(ODataQueryConstants.FILTER, "startswith(description,'" + searchText + "') eq true");
            String serviceResponse = Executor.invokeService(ServiceURLEnum.MEMBERGROUPTYPE_READ, queryMap, null,
                    requestInstance);
            JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
            if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                    || serviceResponseJSON.optInt(FabricConstants.OPSTATUS) != 0
                    || serviceResponseJSON.optJSONArray("membergrouptype") == null) {
                // Failed CRUD Operation
                alert.prepareError("Failed to read Member Groups Type data. Response:" + serviceResponse).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20404);
            }
            // Construct Groups Dataset
            JSONArray records = serviceResponseJSON.optJSONArray("membergrouptype");
            Dataset groupsDataset = CommonUtilities.constructDatasetFromJSONArray(records);
            groupsDataset.setId("serviceType");
            processedResult.addDataset(groupsDataset);

        }

        // Return result
        return processedResult;
    }

}
