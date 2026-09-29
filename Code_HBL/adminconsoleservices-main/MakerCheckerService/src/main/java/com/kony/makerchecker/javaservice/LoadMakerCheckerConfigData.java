package com.kony.makerchecker.javaservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class LoadMakerCheckerConfigData implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	private static JSONArray MAKER_CHECKER_CONFIG_DATA = null;
	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			result = loadDataFromDB();
			return result;
		}
		catch(Exception e) {
			alert.prepareError("Exception occurred in invoking LoadConfigDataService: " + e).log();
            return ErrorCodesEnum.ERR_10005.setErrorCode(new Result());
		}
	}
	
	public synchronized Result loadDataFromDB() {
		Result result = new Result();
		String serviceName = UtilConstants.MAKERCHECKERCRUD;
        String operationName = UtilConstants.MAKER_CHECKER_CONFIG_GET;
       
		String permissionapprovalsResponse = null;
		try {
			permissionapprovalsResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(null)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(permissionapprovalsResponse);
			if(responseObj!=null && responseObj.has(UtilConstants.MAKER_CHECKER_CONFIG) &&
					responseObj.optJSONArray(UtilConstants.MAKER_CHECKER_CONFIG).length()>0) {
				MAKER_CHECKER_CONFIG_DATA = responseObj.optJSONArray(UtilConstants.MAKER_CHECKER_CONFIG);
			}
			result.addParam(UtilConstants.MAKER_CHECKER_CONFIG,MAKER_CHECKER_CONFIG_DATA.toString());
			result.addParam(new Param(UtilConstants.SUCCESS, UtilConstants.TRUE));
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch data from makercheckerconfig: " + e).log();
			result.addParam(new Param(UtilConstants.SUCCESS, UtilConstants.FALSE));
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at isMakerCheckerEnabled: " + e).log();
			result.addParam(new Param(UtilConstants.SUCCESS, UtilConstants.FALSE));
		}
		return result;		
	}
	
	public static JSONArray getMakerCheckerConfigData() {
		if (MAKER_CHECKER_CONFIG_DATA == null) {
			LoadMakerCheckerConfigData obj = new LoadMakerCheckerConfigData();
			obj.loadDataFromDB();
		}
		return MAKER_CHECKER_CONFIG_DATA;
	}
}
