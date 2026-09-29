package com.temenos.infinity.api.accountsweeps.javaservice;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;

import static com.temenos.infinity.api.accountsweeps.utils.AccountSweepsAPIServices.DB_CREATEACCOUNTSWEEP;

/**
 * @author naveen.yerra
 */
public class CreateSweepAtBackend implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();

        String serviceName = DB_CREATEACCOUNTSWEEP.getServiceName(),
               operationName = DB_CREATEACCOUNTSWEEP.getOperationName();

        HashMap<String, Object> inputMap = (HashMap<String, Object>) objects[1];
        inputMap.put("serviceRequestId",dataControllerRequest.getParameter("requestId"));

        String response =  DBPServiceExecutorBuilder.builder().
                withServiceId(serviceName).
                withObjectId(null).
                withOperationId(operationName).
                withRequestParameters(inputMap).
                withRequestHeaders(dataControllerRequest.getHeaderMap()).
                withDataControllerRequest(dataControllerRequest).
                build().getResponse();

        JSONObject responseObj = new JSONObject(response);
        // setting to success, since mock
        responseObj.put("status", "Success");

        return JSONToResult.convert(responseObj.toString());
    }
}
