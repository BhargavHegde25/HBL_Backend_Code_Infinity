package com.bct.javaservices;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class getNPIBillerData implements JavaService2{
	LoggerUtil logger = new LoggerUtil(getNPIBillerData.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Result result = new Result();
		try {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		logger.debug("HBL::getNPSBillerPayload:inputParams:"+inputParams);
		String senderCertPath= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_PFX_FILE_PATH");
		String senderCertPass= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_PFX_FILE_PASSWORD");
		String receiverCertPath= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_RECEIVER_PUBLIC_KEY");
		//String receiverCertPass= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_RECEIVER_FILE_PASSWORD");
		String payload=inputParams.get("npiObject");
		JSONObject certDetails= new JSONObject();
		certDetails.put("senderCertPath", receiverCertPath);
		certDetails.put("senderCertPass", "");
		certDetails.put("receiverCertPath", senderCertPath);
		certDetails.put("receiverCertPass", senderCertPass);
		String payloadString=JoseUtils.decryptAndVerifyResponse(payload, certDetails);
		JSONObject NPIObj= new JSONObject(payloadString);
		//removeEmptyAndNullFields(NPIObj);
		result.setParam(new Param("npiObjectData", NPIObj.toString()));
		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));
		}catch (Exception e) {
			logger.error("Exception Occured at:HBL::getNPSBillerPayload:DecryptAndVerifyPayload:"+e.getLocalizedMessage());
			result.addParam(new Param("dbpErrCode", "20005"));
			result.addParam(new Param("dbpErrMsg",e.getMessage()));
			result.setParam(new Param("httpStatusCode", "500"));
		}
		return result;
	}
	public void removeEmptyAndNullFields(Object object) throws Exception {
        if (object instanceof JSONArray) {
            JSONArray array = (JSONArray) object;
            for (int i = 0; i < array.length(); ++i) 
              removeEmptyAndNullFields(array.get(i));
        } else if (object instanceof JSONObject) {
            JSONObject json = (JSONObject) object;
            JSONArray names = json.names();
            if (names == null) return;
            for (int i = 0; i < names.length(); ++i) {
                String key = names.getString(i);
                
                if (json.isNull(key) || json.get(key) =="") {
                    json.remove(key);
                }
                else {
                    removeEmptyAndNullFields(json.get(key));
                }
            }
        }
    }

}
