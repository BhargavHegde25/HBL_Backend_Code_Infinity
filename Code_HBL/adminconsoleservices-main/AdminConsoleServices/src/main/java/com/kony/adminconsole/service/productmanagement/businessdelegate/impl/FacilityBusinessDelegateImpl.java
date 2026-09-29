package com.kony.adminconsole.service.productmanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.FacilityBackendDelegate;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.ProductBackendDelegate;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.FacilityBusinessDelegate;

public class FacilityBusinessDelegateImpl implements FacilityBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
			
	private static final String OUTPUT_FEATURES_ENTITY_ID = "featuresEntityId";
	private static final String OUTPUT_DBP_ERR_CODE = "dbpErrCode";
	private static final String OUTPUT_DBP_ERR_MESSAGE = "dbpErrMsg";
	private static final String OUTPUT_FEATURE_ERR_MESSAGE = "featureErrMsg";
	private static final String OUTPUT_FEATURE_ERR_CODE = "featureErrCode";
	private static final String OUTPUT_FACILITY_ID = "facilityId";
	private static final String OUTPUT_FACILITIES = "facilities";
	private static final String OUTPUT_OPSTATUS = "opstatus";
	private static final String INPUT_FEATURES = "features";
	private static final String OUTPUT_FEATURE_DETAILS = "featureDetails";
	private static final String PARAM_FEATURE_ID = "featureId";
	private static final String PARAM_FEATURE_ENTITY_ID = "featureEntityId";
	private static final String PARAM_DELETE_FEATURES = "deleteFeatures";
	private static final String PARAM_UPDATE_FEATURES = "updateFeatures";
	private static final String PARAM_CREATE_FEATURES = "createFeatures";
	private static final String PARAM_FEATURE_CREATE_ERR_MSG = "featureCreateErrMsg";
	private static final String PARAM_FEATURE_UPDATE_ERR_MSG = "featureUpdateErrMsg";
	private static final String PARAM_FEATURE_DELETE_ERR_MSG = "featureDeleteErrMsg";
	
	private static final String INPUT_FEATURES_ENTITY_IDS = "featuresEntityIds";
	
	
	FacilityBackendDelegate facilityBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(FacilityBackendDelegate.class);
	
	ProductBackendDelegate productBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(ProductBackendDelegate.class);
	
	@Override
	public JSONObject createFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		JSONObject responseObject = null;
		try {
			
			responseObject = facilityBackendDelegate.createFacility(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_FACILITY_ID) 
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_FACILITY_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				if(StringUtils.isBlank(postParametersMap.get(INPUT_FEATURES).toString())) {
					return responseObject;
				}
				String facilityId = responseObject.getString(OUTPUT_FACILITY_ID);
				
				try {
					postParametersMap.put(OUTPUT_FACILITY_ID, facilityId);
					JSONObject featuresResponseObject = facilityBackendDelegate.createFacilityFeatures(postParametersMap, backendToken);
					
					if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_FEATURES_ENTITY_ID) 
							&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID))) {
						responseObject.put(OUTPUT_FEATURES_ENTITY_ID, featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID));
						
					}else if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
							&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
						responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
						responseObject.put(OUTPUT_FEATURE_ERR_CODE, featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE));
					}
					
				} catch(Exception exp) {
					alert.prepareError("Encountered exception while trying to create facility features in MS: "+exp).log();
					responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, "Failed to create features");
					responseObject.put(OUTPUT_FEATURE_ERR_CODE, "22124");
				}
				
						
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_FACILITY_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to create facility in MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22124");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while Creating Facility in MS");
		}
			
		return responseObject;
	}

	@Override
	public JSONObject editFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			responseObject = facilityBackendDelegate.editFacility(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_FACILITY_ID) 
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_FACILITY_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				if(StringUtils.isBlank(postParametersMap.get(INPUT_FEATURES).toString())) {
					return responseObject;
				}
				
				try {
					
					JSONObject facilityFeatureResponse = facilityBackendDelegate.getFacilityFeatures(postParametersMap, backendToken);

					Map<String, String> fatureEntityMap = getFatureEntityMap(facilityFeatureResponse);
					
					String features = postParametersMap.get(INPUT_FEATURES).toString();
					JSONObject featuresArrayJson = prepareFeaturesArray(fatureEntityMap, 
							new JSONArray(features));
					
					if(featuresArrayJson.getJSONArray(PARAM_CREATE_FEATURES).length()>0) {
						try {
							postParametersMap.put(INPUT_FEATURES, featuresArrayJson.getJSONArray(PARAM_CREATE_FEATURES));
							JSONObject featuresResponseObject = facilityBackendDelegate.createFacilityFeatures(postParametersMap, backendToken);
							if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_FEATURES_ENTITY_ID) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID))) {
								responseObject.put(OUTPUT_FEATURES_ENTITY_ID, featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID));
								
							}else if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_FEATURE_CREATE_ERR_MSG, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
							
						} catch(Exception exp) {
							
							alert.prepareError("Encountered exception while trying to create features in MS in editFacilityFlow: "+exp).log();
							responseObject.put(PARAM_FEATURE_CREATE_ERR_MSG, "Failed to create features");
						}
					}
					
					if(featuresArrayJson.getJSONArray(PARAM_UPDATE_FEATURES).length()>0) {
						try {
							postParametersMap.put(INPUT_FEATURES, featuresArrayJson.getJSONArray(PARAM_UPDATE_FEATURES));
							JSONObject featuresResponseObject = facilityBackendDelegate.editFacilityFeatures(postParametersMap, backendToken);
							if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_FEATURE_UPDATE_ERR_MSG, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
							
						} catch(Exception exp) {
							
							alert.prepareError("Encountered exception while trying to update features in MS in editFacilityFlow: "+exp).log();
							responseObject.put(PARAM_FEATURE_UPDATE_ERR_MSG, "Failed to update features");
						}
					}
					
					if(featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES).length()>0) {
						try {
							
							StringJoiner joiner = new StringJoiner("&");
							for(int i=0; i<featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES).length(); i++) {
								String featureEntityId = featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES).get(i).toString();
								joiner.add(PARAM_FEATURE_ID+"="+featureEntityId);
							}
							postParametersMap.put(INPUT_FEATURES_ENTITY_IDS, joiner.toString());
							
							JSONObject featuresResponseObject = facilityBackendDelegate.deleteFacilityFeatures(postParametersMap, backendToken);
							if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_FEATURE_DELETE_ERR_MSG, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
							
						} catch(Exception exp) {
							
							alert.prepareError("Encountered exception while trying to delete features in MS in editFacilityFlow: "+exp).log();
							responseObject.put(PARAM_FEATURE_DELETE_ERR_MSG, "Failed to delete features");
						}
					}
					
				} catch(Exception exp) {
					alert.prepareError("Encountered exception while trying to features for facility in MS: "+exp).log();
					responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, "Failed to update feaures");
				}
				
			
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_FACILITY_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to edit facility in MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22125");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to edit Facility in MS");
		}
			
		return responseObject;
	}

	@Override
	public JSONObject getFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			responseObject = facilityBackendDelegate.getFacility(postParametersMap, backendToken);
			
			if((null != responseObject && !responseObject.has(OUTPUT_DBP_ERR_CODE) ) ||
					(responseObject.has(OUTPUT_DBP_ERR_CODE) && StringUtils.isBlank(responseObject.getString(OUTPUT_DBP_ERR_CODE)))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				
				if(postParametersMap.containsKey(OUTPUT_FACILITY_ID)) {
					
					JSONArray facilities = responseObject.getJSONArray(OUTPUT_FACILITIES);
					JSONArray features = facilities.getJSONObject(0).has(INPUT_FEATURES)?
							facilities.getJSONObject(0).getJSONArray(INPUT_FEATURES) : new JSONArray() ;
					
					JSONObject masterFeatures = productBackendDelegate.getAccountLevelFeatureDetails(new HashMap<>(),null);
					JSONArray featuresModified = prepareFeatureArry(masterFeatures.getJSONArray(INPUT_FEATURES), features);
					responseObject.getJSONArray(OUTPUT_FACILITIES)
					.getJSONObject(0).put(INPUT_FEATURES, featuresModified);
					
				} else {
					JSONArray facilities = responseObject.getJSONArray(OUTPUT_FACILITIES);
					for(int i=0; i<facilities.length(); i++) {
						if(facilities.getJSONObject(i).has(INPUT_FEATURES)){
							
							JSONArray features = facilities.getJSONObject(i).getJSONArray(INPUT_FEATURES);
							String numOfFeatures = features.length() < 10 ? ("0"+features.length()) : String.valueOf(features.length());
							responseObject.getJSONArray(OUTPUT_FACILITIES).getJSONObject(i).put("numOfFeatures", numOfFeatures);
							responseObject.getJSONArray(OUTPUT_FACILITIES).getJSONObject(i).remove(INPUT_FEATURES);
						}else {
							responseObject.getJSONArray(OUTPUT_FACILITIES).getJSONObject(i).put("numOfFeatures", "00");
						}
						
					}
				}
			
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_FACILITIES);				
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while fetching facility from MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.put(OUTPUT_DBP_ERR_CODE, "22126");
			responseObject.put(OUTPUT_DBP_ERR_MESSAGE, "Error while facing Facility from MS");
		}
			
		return responseObject;
	}
	
	private Map<String, String> getFatureEntityMap(JSONObject responseJson){
		
		Map<String, String> fatureEntityMap = new HashMap<>();
		try {
			if(null != responseJson && responseJson.has(OUTPUT_FEATURE_DETAILS) 
					&& responseJson.getJSONArray(OUTPUT_FEATURE_DETAILS).length()>0) {
				
				JSONObject jsonRecord= responseJson.getJSONArray(OUTPUT_FEATURE_DETAILS).getJSONObject(0);
				if(null != jsonRecord && jsonRecord.has(INPUT_FEATURES)) {
					JSONArray features = jsonRecord.getJSONArray(INPUT_FEATURES);
					if(null != features && features.length()>0) {
						
						for(int i=0; i<features.length(); i++) {
							
							JSONObject featureJson = features.getJSONObject(i);
							String featureId = featureJson.getString(PARAM_FEATURE_ID);
							String featureEntityId = featureJson.getString(PARAM_FEATURE_ENTITY_ID);
							if(StringUtils.isAnyBlank(featureId,featureEntityId)) {
								continue;
							}
							fatureEntityMap.put(featureId, featureEntityId);
						}
					}
				}
			}
		} catch(Exception exp) {
			alert.prepareError("Encountered exception while preparing fatureEntityMap: "+exp).log();
		}
		
		return fatureEntityMap;
	}
	
	private JSONObject prepareFeaturesArray(Map<String, String> fatureEntityMap, JSONArray features) {
		JSONArray deleteFeatures = new JSONArray();
		JSONArray updateFeatures = new JSONArray();
		JSONArray createFeatures = new JSONArray();
		JSONObject featuresArrayJson = new JSONObject();
		featuresArrayJson.put(PARAM_DELETE_FEATURES, deleteFeatures);
		featuresArrayJson.put(PARAM_UPDATE_FEATURES, updateFeatures);
		featuresArrayJson.put(PARAM_CREATE_FEATURES, createFeatures);
		
		if(features.length() ==0) {
			for(Entry<String,String> entry :fatureEntityMap.entrySet()){
				deleteFeatures.put(entry.getValue());
			}
			featuresArrayJson.put(PARAM_DELETE_FEATURES, deleteFeatures);
			return featuresArrayJson;
		}
		if(fatureEntityMap.isEmpty()) {
			featuresArrayJson.put(PARAM_CREATE_FEATURES, features);
			return featuresArrayJson;
		}
		
		for(int i=0; i<features.length();i++) {
			
			JSONObject featureJson = features.getJSONObject(i);
			String featureId = featureJson.getString(PARAM_FEATURE_ID);
			if(fatureEntityMap.containsKey(featureId)) {
				featureJson.put(PARAM_FEATURE_ENTITY_ID, fatureEntityMap.get(featureId));
				updateFeatures.put(featureJson);
				fatureEntityMap.remove(featureId);
				continue;
			}
			createFeatures.put(featureJson);
		}
		
		for(Entry<String,String> entry :fatureEntityMap.entrySet()){
			deleteFeatures.put(entry.getValue());
		}
		
		featuresArrayJson.put(PARAM_DELETE_FEATURES, deleteFeatures);
		featuresArrayJson.put(PARAM_UPDATE_FEATURES, updateFeatures);
		featuresArrayJson.put(PARAM_CREATE_FEATURES, createFeatures);
		
		return featuresArrayJson;
	}
	
	private JSONArray prepareFeatureArry(JSONArray masterData, JSONArray featureData) {
		
		JSONArray finalArray = new JSONArray();
		
		if(featureData.length() == 0) {
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				featureObject.put("isSelected", "false");
				featureObject.put("featureId", feature.getString("id"));
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("featureStatus", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					actionObject.put("isSelected", "false");
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("actionStatus", action.getString("status"));
					
					if(action.has("dependentActions")) {
						actionObject.put("dependentActions", action.get("dependentActions"));
					}else {
						actionObject.put("dependentActions", new JSONArray());
					}
					
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
		} else {
			
			Map<String, Set<String>> featureMap = new HashMap<>();
			for(int i=0; i<featureData.length(); i++) {
				Set<String> actions = new HashSet<String>();
				JSONObject featureJson = featureData.getJSONObject(i);
				JSONArray actionArr = featureJson.getJSONArray("actions");
				for(int j= 0; j<actionArr.length(); j++) {
					JSONObject action = actionArr.getJSONObject(j);
					actions.add(action.getString("actionId"));
				}
				featureMap.put(featureJson.getString("featureId"), actions);
			}
			
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				String featureId = feature.getString("id");
				boolean skipActionSelected = true; 
				if(featureMap.containsKey(featureId)) {
					featureObject.put("isSelected", "true");
					skipActionSelected = false;
				}	else {
					featureObject.put("isSelected", "false");
				}
				featureObject.put("featureId", featureId);
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("featureStatus", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				Set<String> selectedActions = featureMap.get(featureId);
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					if(skipActionSelected || !selectedActions.contains(action.getString("id"))) {
						actionObject.put("isSelected", "false");
					}else {
						actionObject.put("isSelected", "true");
					}
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("actionStatus", action.getString("status"));
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
			
		}
		
		return finalArray;
	}

}
