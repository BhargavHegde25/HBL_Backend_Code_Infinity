package com.hbl.productservicesExtn.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.impl.FeatureActionBusinessDelegateImpl;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class FeatureActionBusinessDelegateImplExtn extends FeatureActionBusinessDelegateImpl{
	private static final Logger LOG = LogManager.getLogger(FeatureActionBusinessDelegateImplExtn.class);
	public JSONArray fetchAllLimits(String actionId, String legalEntityId) {
		LimitsDTO limitsDTO = null;
		JSONArray limitsArray = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_ACTIONLIMIT_GET;
		
		String filter = "Action_id" + DBPUtilitiesConstants.EQUAL + actionId + DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		LOG.debug("HBL::FeatureActionBusinessDelegateImplExtn::fetchAllLimits(actionlimit)::: payload:"+ requestParameters.toString());
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				limitsArray = null;
			}
			LOG.debug("HBL::FeatureActionBusinessDelegateImplExtn::fetchAllLimits(actionlimit)::: response:"+ response);
			JSONObject limitsJSON = new JSONObject(response);
			 limitsArray = CommonUtils.getFirstOccuringArray(limitsJSON);
			//limitsDTO = _fetchLimitsDTO(limitsArray);
			
		} catch (Exception e) {
			LOG.error("Exception caught while fetching global FI level limits", e);
			return null;
		}
		return limitsArray;
	}
	

}
