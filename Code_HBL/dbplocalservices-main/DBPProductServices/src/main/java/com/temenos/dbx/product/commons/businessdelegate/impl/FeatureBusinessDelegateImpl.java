package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureDTO;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class FeatureBusinessDelegateImpl implements FeatureBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<FeatureDTO> fetchFeatures() {
		
		List<FeatureDTO> features = new ArrayList<FeatureDTO>();
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_FEATURE_GET).
					withRequestParameters(null).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray actions = CommonUtils.getFirstOccuringArray(res);
			features = JSONUtils.parseAsList(actions.toString(), FeatureDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return features;
	}
	
	@Override
	public FeatureDTO fetchFeatureById(String featureId) {
		
		FeatureDTO feature = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
		if(StringUtils.isNotEmpty(featureId)) {
			filter = "id" + DBPUtilitiesConstants.EQUAL + featureId;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_FEATURE_GET).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray actions = CommonUtils.getFirstOccuringArray(res);
			feature = JSONUtils.parse(actions.getJSONObject(0).toString(), FeatureDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return feature;
	}
	
}
