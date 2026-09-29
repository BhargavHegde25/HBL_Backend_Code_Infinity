package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface FeatureActionBusinessDelegate extends BusinessDelegate {

	/**
	 * fetches all the feature actions
	 * @return List<FeatureActionDTO>
	 */
	public List<FeatureActionDTO> fetchFeatureActions();
	
	/**
	 * fetches all the feature actions with limitgroup details
	 * @return List<FeatureActionDTO>
	 */
	public List<FeatureActionDTO> fetchFeatureActionsWithLimitGroupDetails();
	
	
	/**
	 * fetches all the feature actions with feature details
	 * @return List<FeatureActionDTO>
	 */
	public List<FeatureActionDTO> fetchFeatureActionsWithFeatureDetails();
	
	/**

	 * Gets the LimitGroupId for a given featureActionID
	 * @param featureActionID
	 * @return String
	 */
	public String getLimitGroupId(String featureActionID,String legalEntityId);

	/**
	 * Gets the feature actions for a given limitgroupId
	 * @param limitgroupId
	 * @return List<String>
	 */
	public List<String> getFeatureActionsForLimitGroupId(String limitgroupId);

	/**
	 * To fetch the exhausted limits for a given companyId,customerId, featureActionID, date
	 * @param companyId
	 * @param customerId
	 * @param featureActionID
	 * @return LimitsDTO
	 */
	public LimitsDTO fetchExhaustedLimitsOfFeatureAction(String companyId, String customerId, String featureActionID, String date);	
	
	/**
	 * fetches feature actions for given featureAtion
	 * @param givenFeatuerAcion
	 * @return String
	 */
	public String getApproveFeatureAction(String givenFeatuerAcion);
	
	/**
	 * fetches approval matrix feature actions for given features
	 * @param features
	 * @return Set<String>
	 */
	public Set<String> getApprovalMatrixFeatureActions(Set<String> features);
	
	/**
	 * fetches feature actions dto for given featureAtion	
	 * @param id
	 * @return FeatureActionDTO
	 */
	public FeatureActionDTO getFeatureActionById(String id);
	
	/**
	 * fetched the global limits of given actionId
	 *
	 * @param actionId
	 * @param legalEntityId
	 * @return
	 */
	public LimitsDTO fetchLimits(String actionId, String legalEntityId);
	
}
