package com.kony.fabricreports.util;

import org.json.JSONArray;

import com.kony.fabricreports.FabricReportsManageService;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ViewFabricReportOrch implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result res = new Result();
		JSONArray resArray = FabricReportsManageService.processReportsAction(
				Integer.parseInt(request.getParameter("pageno")), request.getParameter("methodId"),
				request.getParameter("reportId"), request.getParameter("filters"));
		res.addParam(new Param(request.getParameter("methodId"), resArray.toString(), "String"));
		return res;
	}

}
