package com.temenos.infinity.tradefinanceservices.javaservice;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;


/**
* (INFO) Prepares Result object from the mock data returned by the Util.
*
*/



public class CreateLetterOfCreditMockOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
@Override
public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
	
	Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
	Result result = new Result();
	Record rec = new Record();
//	Param p = new Param();
    JSONObject responseObj = new JSONObject();
    responseObj.put("status", "Success");
    result = JSONToResult.convert(responseObj.toString());
//	p.setName("Status");
//	p.setValue("Success");
//	rec.addParam(p);
//	result.addRecord(responseObj);
	return result;
}



}