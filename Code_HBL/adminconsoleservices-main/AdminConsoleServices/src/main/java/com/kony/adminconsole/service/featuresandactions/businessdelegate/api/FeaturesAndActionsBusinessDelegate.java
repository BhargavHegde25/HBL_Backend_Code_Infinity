package com.kony.adminconsole.service.featuresandactions.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.service.featuresandactions.dto.AccessPolicyDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionDependencyDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionLevelDTO;
import com.kony.adminconsole.service.featuresandactions.dto.ActionsDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureActionsViewDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureDTO;
import com.kony.adminconsole.service.featuresandactions.dto.FeaturesViewDTO;
import com.kony.adminconsole.service.featuresandactions.dto.LimitGroupDTO;

/**
 * Handles all the operations on Features and actions
 * @author KH2660
 * extends {@link BusinessDelegate}
 */
public interface FeaturesAndActionsBusinessDelegate extends BusinessDelegate {
	
	/**
	 * Returns the list of limit group records
	 * @return List of {@link LimitGroupDTO}
	 */
	public List<LimitGroupDTO> fetchAllLimitGroups();
	
	/**
	 * Fetches the list of the existing features
	 * @return List of {@link FeaturesViewDTO}
	 */
	public List<FeaturesViewDTO> fetchAllFeatures(String legalEntityId);
	
	/**
	 * Edits the existing limit group
	 * @param limitGroupDTO contains the values to be edited
	 * @return {@link LimitGroupDTO}
	 */
	public LimitGroupDTO editLimitGroup(LimitGroupDTO limitGroupDTO);
	
	/**
	 * Edits the existing feature
	 * @param featureDTO contains the details to be edited
	 * @return {@link FeatureDTO}
	 */
	public FeatureDTO editFeatureDetails(FeatureDTO featureDTO);
	
	/**
	 *  Edits the existing feature display name details
	 * @param featureDTO contains the details to be edited
	 * @return {@link FeatureDTO}
	 */
	public FeatureDTO editFeatureDisplayNameDetails(FeatureDTO featureDTO);
	
	/**
	 * Edits the existing action
	 * @param actionsDTO contains the details to be edited
	 * @return {@link ActionsDTO}
	 */
	public ActionsDTO editActionDetails(ActionsDTO actionsDTO);
	
	/**
	 * Edits the existing action limits
	 * @param actionsDTO
	 * @return true if edit is successful
	 */
	public boolean editActionLimits(ActionsDTO actionsDTO);

	/**
	 * Edits the existing action display name details
	 * @param actionsDTO
	 * @return {@link ActionsDTO}
	 */
	public ActionsDTO editActionDisplayName(ActionsDTO actionsDTO);
	
	/**
	 * Fetches the list of the existing actions of a particular feature
	 * @param featureId
	 * @param legalEntityId
	 * @return List of {@link FeatureActionsViewDTO }
	 */
	public List<FeatureActionsViewDTO> fetchFeatureActions(String featureId, String legalEntityId);
	
	/**
	 * Fetches the list of the existing monetary actions
	 * @param typeId 
	 * @return List of {@link FeatureActionsViewDTO}
	 */
	public List<FeatureActionsViewDTO> fetchMonetaryActions(String typeId);
	
	/**
	 * Fetches the list of the existing feature actions
	 * @return List of {@link FeatureActionsViewDTO}
	 */
	public List<FeatureActionsViewDTO> fetchFeatureActionsByType(String roleTypeId, String companyLegalUnit);
	
	/**
	 * Fetches the list of access policies
	 * @return List of {@link AccessPolicyDTO}
	 */
	public List<AccessPolicyDTO> fetchAccessPolicies();
	
	/**
	 * Fetches the list of action dependencies
	 * @return
	 */
	public List<ActionDependencyDTO> fetchActionDependencies(String featureId, String legalEntityId);
	
	/**
	 * 
	 * @param actionsDTO
	 * @return
	 */
	public ActionsDTO editActionStatus(ActionsDTO actionsDTO);
	
	/**
	 * Fetches the list of action levels
	 * @return List of {@link ActionLevelDTO}
	 */
	public List<ActionLevelDTO> fetchActionLevels();
	
	/**
	 * @param filter
	 * downloads the list of the existing features
	 * @return List of {@link FeaturesViewDTO}
	 */
	public List<FeaturesViewDTO> downloadFeaturesList(String filter);
	
	/**
	 * Fetches the details of the feature based on the action id
	 * @param actionId
	 * @return {@link FeatureDTO}
	 */
	public FeatureDTO getFeatureName(String actionId, String companyLegalUnit);
	
	/**
	 * Fetches the details of the feature based on the feature id
	 * @param featureId
	 * @return
	 */
	public FeatureDTO getFeatureDetails(String featureId);
	
	/**
	 * Fetches the action details based on the featureId
	 * @param featureId
	 * @return List of {@link ActionsDTO}
	 */
	public List<ActionsDTO> getActionDetails(String featureId);
	
	/**
	 * Updates the limits in service definition, customer role, contracts 
	 * @param actionsDTO
	 * @return
	 */
	public boolean updateLimits(ActionsDTO actionsDTO);
	
	/**
	 * Fetches the limits based on the actionId
	 * @param actionId
	 * @return
	 */
	public List<ActionsDTO> getActionsLimits(String actionId);
	
	/**
	 * Fetches service fee for the featureId
	 * @param featureId
	 * @return JSONObject having serviceFee
	 */
	public JSONObject getServiceFee (String featureId, Map<String, Object> headerMap);
	
	/**
	 * Fetches the list of the existing monetary actions
	 * @param typeId 
	 * @return List of {@link FeatureActionsViewDTO}
	 */
	public List<FeatureActionsViewDTO> fetchAccountLevelActions(String isAccountLevel);
}