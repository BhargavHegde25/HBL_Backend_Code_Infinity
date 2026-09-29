package com.kony.adminconsole.service.customermanagement;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.businesstype.BusinessTypeCustomersGetService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCombinedUserCount implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
        Result processedResult = new Result();
        try {
        	Map<String, String> postParametersMap = new HashMap<String, String>();
            postParametersMap.put(ODataQueryConstants.FILTER,"isCombinedUser eq '1'");
            String readCustomersResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_READ,
                    postParametersMap, null, requestInstance);
            JSONObject readCustomersResponseJSON = CommonUtilities
                    .getStringAsJSONObject(readCustomersResponse);
            Integer combinedUsersCount =0;
            if (readCustomersResponseJSON != null && readCustomersResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomersResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                JSONArray readCustomerIdsJSONArray = readCustomersResponseJSON.getJSONArray("customer");
                if (readCustomerIdsJSONArray == null || readCustomerIdsJSONArray.length() < 1) {
                	combinedUsersCount = 0;
                } else {
                	combinedUsersCount = readCustomerIdsJSONArray.length();
                }
            }
            Param countParam = new Param("CombinedUserCount",combinedUsersCount.toString(), FabricConstants.STRING);
            Param statusParam = new Param("Status", "Successful", FabricConstants.STRING);
            processedResult.addParam(countParam);
            processedResult.addParam(statusParam);
            return processedResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }
}
