/**
 * 
 */
package com.kony.adminconsole.handler;

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
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * Handler to fetch records. Supports pagination and sorting
 * 
 * @author Aditya Mankal
 * 
 * 
 */
public class PaginationHandler {

    private static final int DEFAULT_COUNT_OF_RECORDS_TO_FETCH = 100;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    public static final String IS_LAST_PAGE_KEY = "isLastPage";
    public static final String RECORDS_PER_PAGE_KEY = "recordsPerPage";

    public static void setOffset(DataControllerRequest requestInstance, Map<String, String> postParametersMap) {
        String _$top = requestInstance.getParameter(ODataQueryConstants.TOP);
        String _$skip = requestInstance.getParameter(ODataQueryConstants.SKIP);

        diagnostic.prepareDebug("$top value:" + _$top).log();
        diagnostic.prepareDebug("$skip value:" + _$skip).log();

        if (StringUtils.isNotBlank(_$top)) {
            postParametersMap.put(ODataQueryConstants.TOP, _$top);
        }
        if (StringUtils.isNotBlank(_$skip)) {
            postParametersMap.put(ODataQueryConstants.SKIP, _$skip);
        }

    }

    public static JSONObject getPaginatedData(ServiceURLEnum serviceURLEnum, Map<String, String> oDataQueryMap,
            Map<String, String> headerMap, DataControllerRequest requestInstance) {

        diagnostic.prepareDebug("Get Service URL:" + serviceURLEnum.getServiceURL()).log();

        try {
            if (oDataQueryMap.containsKey(ODataQueryConstants.TOP)) {
                oDataQueryMap.put(ODataQueryConstants.TOP,
                        String.valueOf(Integer.parseInt(oDataQueryMap.get(ODataQueryConstants.TOP)) + 1));
            }
        } catch (NumberFormatException e) {
            alert.prepareError(e.toString()).log();
            oDataQueryMap.put(ODataQueryConstants.TOP, String.valueOf(DEFAULT_COUNT_OF_RECORDS_TO_FETCH));
        }
        String getResponse = Executor.invokeService(serviceURLEnum, oDataQueryMap, headerMap, requestInstance);
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(getResponse);
        if (responseJSON != null && responseJSON.has(FabricConstants.OPSTATUS)
                && responseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
            diagnostic.prepareDebug("Response Received. Call Successful. Opstatus value : 0").log();
            try {
                if (oDataQueryMap.containsKey(ODataQueryConstants.TOP)) {
                    int recordsPerPage = Integer.parseInt(oDataQueryMap.get(ODataQueryConstants.TOP)) - 1;
                    responseJSON.put("recordsPerPage", recordsPerPage);
                    JSONArray recordsJSONArray = null;
                    for (String currKey : responseJSON.keySet()) {
                        if (responseJSON.get(currKey) instanceof JSONArray) {
                            recordsJSONArray = responseJSON.getJSONArray(currKey);
                            break;
                        }
                    }
                    if (recordsJSONArray == null) {
                        alert.prepareError("Records JSON Array missing in the response").log();
                        return responseJSON;
                    }
                    int countOfFetchedRecords = recordsJSONArray.length();
                    if (countOfFetchedRecords > recordsPerPage) {
                        responseJSON.put(IS_LAST_PAGE_KEY, false);
                        recordsJSONArray.remove(recordsJSONArray.length() - 1);
                    } else {
                        responseJSON.put(IS_LAST_PAGE_KEY, true);
                    }
                    diagnostic.prepareDebug(IS_LAST_PAGE_KEY + responseJSON.optString(IS_LAST_PAGE_KEY)).log();
                    return responseJSON;
                }
            } catch (Exception e) {
                alert.prepareError(e.toString()).log();
            }
        }
        alert.prepareError("Response Received. Call Failed. Opstatus value : Non Zero/Not Found").log();
        return responseJSON;
    }

    public static void addPaginationMetadataToRecordObject(Record operationRecord,
            JSONObject paginationHandlerResponseJSON) {
        if (operationRecord == null || paginationHandlerResponseJSON == null
                || !paginationHandlerResponseJSON.has(IS_LAST_PAGE_KEY)) {
            return;
        }
        operationRecord.addParam(new Param(IS_LAST_PAGE_KEY, paginationHandlerResponseJSON.optString(IS_LAST_PAGE_KEY),
                FabricConstants.STRING));
    }

    public static void addPaginationMetadataToResultObject(Result processedResult,
            JSONObject paginationHandlerResponseJSON) {
        if (processedResult == null || paginationHandlerResponseJSON == null) {
            return;
        }
        if (paginationHandlerResponseJSON.has(IS_LAST_PAGE_KEY)) {
            processedResult.addParam(new Param(IS_LAST_PAGE_KEY,
                    paginationHandlerResponseJSON.optString(IS_LAST_PAGE_KEY), FabricConstants.STRING));
        }
        if (paginationHandlerResponseJSON.has(RECORDS_PER_PAGE_KEY)) {
            processedResult.addParam(new Param(RECORDS_PER_PAGE_KEY,
                    paginationHandlerResponseJSON.optString(RECORDS_PER_PAGE_KEY), FabricConstants.STRING));
        }
    }
}
