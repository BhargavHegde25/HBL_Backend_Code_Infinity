package com.temenos.infinity.wealth.tap.processor.post;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;



import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetSuitabilityProfilePostProcessor implements DataPostProcessor2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

   @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
	   diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Entered ").log();
	   Dataset bodySet = result.getDatasetById("body");
       JSONObject responseObj = new JSONObject();
       Record bodyRec = bodySet.getRecord(0);
       String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
       if (bodyRec != null) {
    	   diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Data available ").log();
          JSONObject bodyObj = ResultToJSON.convertRecord(bodyRec);
          
          String startvalid = bodyObj.get("ip_start_validity_d").toString();
          String expdate = bodyObj.get("ext_ip_expiry_d").toString();
          Date sdf = new SimpleDateFormat("yyyy-MM-dd").parse(expdate);
          String date = new SimpleDateFormat("dd/MM/yyyy").format(sdf);
           
          String expvalid = bodyObj.get("ext_ip_cur_status").toString();
          String revstatus = bodyObj.get("ext_ip_rvw_status").toString();
          
          responseObj = new JSONObject();
          responseObj.put("expiryDate", date);
          
           if(expvalid.equalsIgnoreCase("2") && revstatus.equalsIgnoreCase("0")) {
        	   responseObj.put("isValid", "true");
        	   diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Validity is true ").log();
           } else {
        	   responseObj.put("isValid", "false");
        	   diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Validity is false ").log();
           }
           if(expvalid.equalsIgnoreCase("2") && revstatus.equalsIgnoreCase("0")) {
        	   responseObj.put("message", "Valid");
        	   diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Valid ").log();
           } else {
        	   responseObj.put("message", "Current profile expired");
        	   diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Expired ").log();
           }
           
      }

      Result resResult = Utilities.constructResultFromJSONObject(responseObj);
      resResult.addOpstatusParam("0");
      resResult.addHttpStatusCodeParam("200");
      resResult.addParam("portfolioId", TemenosConstants.PORTFOLIOID);
      resResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
      diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor TAP - Exited ").log();
       return resResult;
   }

private Instant getDateFromString(String expdate) {
	// TODO Auto-generated method stub
	return null;
}

}
