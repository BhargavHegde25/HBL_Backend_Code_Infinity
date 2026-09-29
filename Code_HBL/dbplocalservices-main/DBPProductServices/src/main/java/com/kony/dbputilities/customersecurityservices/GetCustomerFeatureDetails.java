package com.kony.dbputilities.customersecurityservices;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbp.handler.FeaturesAndActionsHandler;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerFeatureDetails implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String DEFAULT_LOCALE = "en-US";
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		try {
			
			String locale = dcRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			if (StringUtils.isBlank(locale)) {
				// Consider Default Locale if Accept-Language Header is Blank
				locale = DEFAULT_LOCALE;
				diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
			}
			locale = CommonUtils.formatLanguageIdentifier(locale);
			
			JSONArray featureDetails = FeaturesAndActionsHandler.getCustomerFeatureDetails(locale, dcRequest, result);
			Dataset featureDetailsSet = new Dataset("features");
			for(int i=0; i<featureDetails.length(); i++) {
				JSONObject featureObject = featureDetails.getJSONObject(i);
				Record feature = new Record();
				
				feature.addParam(new Param("id",featureObject.optString("id"), MWConstants.STRING));
				feature.addParam(new Param("name",featureObject.optString("name"), MWConstants.STRING));
				feature.addParam(new Param("description",featureObject.optString("description"), MWConstants.STRING));
				feature.addParam(new Param("Type_id",featureObject.optString("Type_id"), MWConstants.STRING));
				feature.addParam(new Param("Status_id",featureObject.optString("Status_id"), MWConstants.STRING));
				feature.addParam(new Param("Locale_id",featureObject.optString("Locale_id"), MWConstants.STRING));
				feature.addParam(new Param("displayName",featureObject.optString("displayName"), MWConstants.STRING));
				feature.addParam(new Param("displayDescription",featureObject.optString("displayDescription"), MWConstants.STRING));
				
				featureDetailsSet.addRecord(feature);
			}
			
			result.addDataset(featureDetailsSet);
			
		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("Exception occured in GetCustomerFeatureDetails JAVA service. ApplicationException: ", e).log();
		} catch (Exception e) {
			ErrorCodeEnum.ERR_13503.setErrorCode(result);
			result.addParam(new Param("FailureReason", e.getMessage(), MWConstants.STRING));
			alert.prepareError("Exception occured in GetCustomerFeatureDetails JAVA service. Exception: ", e).log();
		}
		return result;

	}

}
