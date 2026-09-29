package com.temenos.dbx.actions.businessdelegate.api;

import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.temenos.dbx.constants.ActionConstant;

public interface AccountActionBusinessDelegate extends BusinessDelegate {
	void initFeatureAction(FabricRequestManager fabricRequestManager);
	boolean hasUserAction(ActionConstant action);
	Set<String> getAccountsHavingAction(ActionConstant action);
	Set<String> getAccountActions(String accountId);
	boolean hasUserAccountFeatureAction(String userId, String accountId, ActionConstant action);
	
}
