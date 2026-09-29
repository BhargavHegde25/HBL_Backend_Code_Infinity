package com.kony.adminconsole.preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GenerateCDPReportPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			
			String applicationId = request.getParameter("applicationId");
			String applicationType = request.getParameter("applicationType");
			String entityDefinitionCode = applicationType.equalsIgnoreCase("SME")
					? EnvironmentConfiguration.SME_ONBOARDING_ENTITY_DEFINTION.getValue(request)
					: EnvironmentConfiguration.ONBOARDING_ENTITY_DEFINTION.getValue(request);

			JSONObject applicationDetails = getAllEntityItems(entityDefinitionCode, applicationId);
			JSONArray entityItems = applicationDetails.getJSONArray("entityItems");
			if (entityItems == null || entityItems.length() == 0) {
				request.addRequestParam_("entityItems", "failed");
				return true;
			} else {
				request.addRequestParam_("entityItems", entityItems.toString());
			}
			// to get lead id's from odms and store in list
			String primaryApplicantId = "";
			String mainApplicantDPId = "";
			String leadId = "";
			String coAppLeadId = "";
			String coApplicantDPIds = "";
			JSONArray applicantIds = new JSONArray();
			JSONObject applicantMeta = new JSONObject();
			JSONObject metaDataEntityEntry = CommonUtilities.getEntityItemEntry("MetaData", entityItems, "MetaData");
			if ((metaDataEntityEntry != null) && metaDataEntityEntry.has("entry")) {
				JSONObject metaDataEntry = new JSONObject(metaDataEntityEntry.optString("entry"));
				if (metaDataEntry != null) {
					primaryApplicantId = metaDataEntry.optString("PrimaryApplicant");
					applicantIds = metaDataEntry.optJSONArray("CoApplicants");
				}
			}

			ArrayList<String> leadList = new ArrayList<String>();
			if (!StringUtils.isBlank(primaryApplicantId)) {
				mainApplicantDPId = primaryApplicantId.split("_")[1];
				applicantMeta = CommonUtilities.getEntityItemEntry("ApplicantMetaData", entityItems,
						"ApplicantMetaData_" + mainApplicantDPId);
				if ((applicantMeta != null) && applicantMeta.has("entry")) {
					JSONObject coApplicantMetaDataEntry = new JSONObject(applicantMeta.optString("entry"));
					if (coApplicantMetaDataEntry != null) {
						leadId = coApplicantMetaDataEntry.optString("LeadId");
					}
				}
				leadList.add(leadId);
			}

			if ((applicantIds != null) && applicantIds.length() != 0) {
				for (int i = 0; i < applicantIds.length(); i++) {
					coApplicantDPIds = applicantIds.getString(i).split("_")[1];
					applicantMeta = CommonUtilities.getEntityItemEntry("ApplicantMetaData", entityItems,
							"ApplicantMetaData_" + coApplicantDPIds);
					if ((applicantMeta != null) && applicantMeta.has("entry")) {
						JSONObject coApplicantMetaDataEntry = new JSONObject(applicantMeta.optString("entry"));
						if (coApplicantMetaDataEntry != null) {
							coAppLeadId = coApplicantMetaDataEntry.optString("LeadId");
						}
					}
					leadList.add(coAppLeadId);
				}
			}
			request.addRequestParam_("leadIds", leadList.toString());
			return true;
		} catch (Exception e) {
			alert.prepareError("[GenerateCDPReportPreProcessor] Error occured in PreProcessor", e).log();
			return false;
		}
	}
	//
	private JSONObject getAllEntityItems(String entityDefinitionCode, String applicationId)
			throws DBPApplicationException, Exception {
		String response = "";
		try {
			Map<String, Object> mapPayload = new HashMap<String, Object>();
			mapPayload.put("trackingCode", applicationId);
			mapPayload.put("entityDefinitionCode", entityDefinitionCode);
			response = DBPServiceExecutorBuilder.builder().withServiceId("SpotlightDataStorageAPIs")
					.withOperationId("GetAllEntityItems").withRequestParameters(mapPayload)
					.build().getResponse();
		} catch (DBPApplicationException exception) {
			alert.prepareError("Failed to fetch all entity items").log();
		}
		return new JSONObject(response);
	}
}