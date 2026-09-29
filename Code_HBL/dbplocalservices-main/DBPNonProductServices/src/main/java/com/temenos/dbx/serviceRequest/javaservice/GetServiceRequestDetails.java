package com.temenos.dbx.serviceRequest.javaservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class GetServiceRequestDetails implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();

		//if SERVICE_REQUEST_MAX_PERIOD_MONTH is not present in client app properties or it's not valid, we default it to 1
		Integer maxPeriodInt = 1;
		try {
			String maxPeriod = request.getServicesManager().getConfigurableParametersHelper()
					.getClientAppProperty("SERVICE_REQUEST_MAX_PERIOD_MONTH");
			maxPeriodInt = Integer.parseInt(maxPeriod);
		} catch (Exception e) {
		}

		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		String endDate = sdformat.format(currentDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -maxPeriodInt); 
		String startDate = sdformat.format(cal.getTime());
		
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String dateToParam = request.getParameter("dateTo");
			String dateFromParam = request.getParameter("dateFrom");
			if (dateToParam == null || dateToParam == "") {
				params.put("dateTo", endDate);				
			}
			if (dateFromParam == null || dateFromParam == "") {
				params.put("dateFrom", startDate);			
			}

			String serviceName = "ServiceRequestJavaService";
			String operationName = "getOrderDetails";

			String resultStr = DBPServiceExecutorBuilder.builder()
	                    .withServiceId(serviceName)
	                    .withOperationId(operationName)
	                    .withRequestParameters(params).withRequestHeaders(serviceHeaders)
	                    .withDataControllerRequest(request).build().getResponse();
			result = JSONToResult.convert(new JSONObject(resultStr).toString());			
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception Occured while fetching order details:" + e).log();
			return errorResult;
		}
		return result;
	}
}
