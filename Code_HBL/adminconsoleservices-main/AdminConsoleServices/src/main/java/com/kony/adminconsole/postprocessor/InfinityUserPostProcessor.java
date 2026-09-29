package com.kony.adminconsole.postprocessor;

import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.kony.adminconsole.commons.utils.http.HTTPOperations;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.campaignmanagement.CampaignHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.api.OperationData;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.konylabs.middleware.api.ServiceRequest;

/**
 * Postprocessor for the createInfinityUser Service. Trigger default alert
 * subscription (& multiple API calls) at user onboarding
 * 
 * @author Kaushik Mesala
 *
 */
public class InfinityUserPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String serviceId = "AlertManagement";
	private static final String operationId = "subscribeDefaultAlerts";
	private static final String chunkedResults = "chunkedresults_json";
	private static final String createInfinityId = "id";
	private static final String infinityCustomerId = "userId";

	@Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		Log4j2Configurator.getInstance();

		diagnostic.prepareDebug("inside InfinityUserPostProcessor").log();
		
		String status = result.getParamValueByName("status");
		String dbpErrCode = result.getParamValueByName("dbpErrCode");
		
		/*if((StringUtils.isEmpty(status) || status.equalsIgnoreCase("success")) &&
				(StringUtils.isEmpty(dbpErrCode) || dbpErrCode.equalsIgnoreCase("0"))) {

			Callable<Result> enableServiceCallable = new Callable<Result>() {
				@Override
				public Result call() throws Exception {
					return invokeEnableService(request , response,result);
				}
			};

	        try {
	            ThreadExecutor.execute(enableServiceCallable);
	        } catch (InterruptedException e) {
	            alert.prepareError("InfinityUserPostProcessor throw error ", e).log();
	            Thread.currentThread().interrupt();
	        }
		}*/
		
        return result;
    }
    
    public Result invokeEnableService(DataControllerRequest request , DataControllerResponse response, Result result) throws ApplicationException {
    	
    	try {
    		
    		for(String name : result.getNameOfAllParams())
    		{
    			diagnostic.prepareDebug(name + " :: "+ result.getParamValueByName(name)).log();
    		}
    		
    			diagnostic.prepareDebug("createInfinityId :: "+result.getParamValueByName(createInfinityId)).log();
    		
                Map<String, Object>  attributes =new HashMap<String, Object>();
                String authToken = CommonUtilities.getAuthToken(request);
                
              
                attributes.put( infinityCustomerId  , result.getParamValueByName(createInfinityId) );
                attributes.put(HTTPOperations.X_KONY_AUTHORIZATION_HEADER, authToken);
                	
                return CampaignUtil.invokeService(serviceId ,operationId , attributes );	
    	
        } catch (Exception e) {
            alert.prepareError("Failed to execute service as inline method call for service/operation:" + serviceId + "/"
                    + operationId, e).log();
        }
    	return result;
    }
}
