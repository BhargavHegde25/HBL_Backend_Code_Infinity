package com.hbl.adminconsoleextn.api;

import java.util.List;

import com.hbl.adminconsoleextn.dto.ActionsDTOExtn;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.api.FeaturesAndActionsBusinessDelegate;
import com.kony.adminconsole.service.featuresandactions.dto.ActionsDTO;

public interface FeaturesAndActionsBusinessDelegateExtn extends FeaturesAndActionsBusinessDelegate{

	/**
	 * Fetches the limits based on the actionId
	 * @param actionId
	 * @return
	 */
	
	public List<ActionsDTOExtn> getActionsLimitsHBL(String actionId);
	public boolean editActionLimitsHBL(ActionsDTOExtn actionsDTO);
	public boolean updateLimitsHBL(ActionsDTOExtn actionsDTO);

}
