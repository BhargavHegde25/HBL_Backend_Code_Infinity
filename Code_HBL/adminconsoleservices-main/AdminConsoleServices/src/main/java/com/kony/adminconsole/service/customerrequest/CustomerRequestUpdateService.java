package com.kony.adminconsole.service.customerrequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * <p>
 * Service to Update Customer Requests
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class CustomerRequestUpdateService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    private static final String CSR_ID_PARAM = "csrId";
    private static final String REQUEST_IDS_PARAM = "requestIds";

    private static final String ASSIGN_REQUESTS_METHOD_ID = "assignRequests";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {

        try {

            Result processedResult = new Result();

            if (StringUtils.equals(methodID, ASSIGN_REQUESTS_METHOD_ID)) {
                String csrId = requestInstance.getParameter(CSR_ID_PARAM);
                diagnostic.prepareDebug("Is CSR ID Null?" + StringUtils.isBlank(csrId)).log();

                String requestIdsStr = requestInstance.getParameter(REQUEST_IDS_PARAM);
                diagnostic.prepareDebug("Is RequestIds Null?" + StringUtils.isBlank(requestIdsStr)).log();

                List<String> requestIds = new ArrayList<>();
                if (requestIdsStr != null) {
                    requestIds = Arrays.stream(requestIdsStr.split("\\s*,\\s*")).collect(Collectors.toList());
                    assignRequests(requestIds, csrId, requestInstance);
                }
                processedResult.addParam(
                        new Param("assignedRequests", Integer.toString(requestIds.size()), FabricConstants.INT));
            }

            return processedResult;

        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20921.setErrorCode(errorResult);
            return errorResult;
        }

    }

    /**
     * Method to assign Customer Request(s) to CSR
     * 
     * @param requestIds
     * @param csrId
     * @param requestInstance
     * @throws ApplicationException
     */
    private void assignRequests(List<String> requestIds, String csrId, DataControllerRequest requestInstance)
            throws ApplicationException {

        // Validate Inputs
        if (requestIds == null || requestIds.isEmpty()) {
            diagnostic.prepareDebug("Request Ids list is empty. Returning..").log();
            return;
        }
        if (StringUtils.isBlank(csrId)) {
            alert.prepareError("Customer Id not provided. Throwing Exception..").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20688);
        }

        // Execute Update Operation
        Map<String, String> inputMap = new HashMap<>();
        String requestIdsStr = String.join(",", requestIds);
        inputMap.put("_requestIds", requestIdsStr);
        inputMap.put("_csrID", csrId);
        String serviceResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_REQUESTS_ASSIGN_PROC_SERVICE, inputMap,
                null, requestInstance);
        JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
        if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            alert.prepareError("Failed Operation. Service Response:" + serviceResponse).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20147);
        }
        diagnostic.prepareDebug("Assigned Customer Request(s) to CSR").log();
    }

}
