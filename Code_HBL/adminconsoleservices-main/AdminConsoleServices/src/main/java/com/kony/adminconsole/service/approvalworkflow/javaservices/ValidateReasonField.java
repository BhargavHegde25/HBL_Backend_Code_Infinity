package com.kony.adminconsole.service.approvalworkflow.javaservices;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ValidateReasonField implements JavaService2{
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();

        Map<String, Object> requestInputMap = new HashMap<String, Object>();
        requestInputMap = (Map<String, Object>) inputArray[1];
        String reason = requestInputMap.get("reason").toString();
        String regExp = "[^a-zA-Z0-9.,\"\' ]";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(reason);
        Boolean val = m.find();
        result.addStringParam("validation",val.toString());
        return result;
	}
}
