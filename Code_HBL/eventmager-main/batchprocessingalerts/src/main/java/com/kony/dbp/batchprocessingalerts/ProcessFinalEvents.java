package com.kony.dbp.batchprocessingalerts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class ProcessFinalEvents implements JavaService2{

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result res=new Result();
        String events= request.getParameter("events");
        JsonParser parser = new JsonParser();
        JsonArray eventsarray = parser.parse(events).getAsJsonArray();
        JsonObject result=new JsonObject();
        result.add("events", eventsarray);
        res=JSONToResult.convert(result.toString());
		return res;
		
		
		
	}

}
