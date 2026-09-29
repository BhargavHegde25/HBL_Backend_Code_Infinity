package com.kony.adminconsole.service.usermanagement.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.service.featuresandactions.dto.ActionDependencyDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserActionsDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserActionsViewDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserFeaturesDTO;
import com.kony.adminconsole.service.usermanagement.dto.InternalUserFeaturesViewDTO;


public interface UserFeatureBusinessDelegate extends BusinessDelegate {

    public List<InternalUserFeaturesViewDTO> getInternalUserFeatures(String featureId);
    
    public List<InternalUserActionsViewDTO> getInternalUserFeatureActions(String featureId);
    
    public InternalUserFeaturesDTO editInternalUserFeatureDetails(InternalUserFeaturesDTO featureDTO);
    
    public InternalUserFeaturesDTO editInternalUserFeatureDisplayNameDetails(InternalUserFeaturesDTO featureDTO);
    
    public InternalUserActionsDTO editInternalUserActionDisplayName(InternalUserActionsDTO featureDTO);
    
    public List<ActionDependencyDTO> fetchInternalActionDependencies(String featureId);
    
    public InternalUserActionsDTO editInternalUserActionDetails(InternalUserActionsDTO actionsDTO);
    
    public List<InternalUserActionsDTO> getInternalUserActionDetails(String featureId);
    
    public InternalUserFeaturesDTO getInternalUserFeatureName(String actionId);
    
    public InternalUserFeaturesDTO getInternalFeatureDetails(String featureId);
}
