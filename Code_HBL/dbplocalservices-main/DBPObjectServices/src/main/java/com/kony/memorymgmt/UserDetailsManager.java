package com.kony.memorymgmt;

import org.apache.commons.lang3.StringUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.model.UserDetailsHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.kony.dbputilities.memorymanagement.MemoryManager;

public class UserDetailsManager {
	private FabricRequestManager fabricRequestManager = null;
	private String customerId = null;
	private static final String USERDETAILS = "USER_DETAILS";

	public UserDetailsManager(FabricRequestManager fabricRequestManager) {
		this.fabricRequestManager = fabricRequestManager;
		this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
	}

	public void saveUserDetailsIntoSession(String userDetails) {
		if (StringUtils.isNotBlank(userDetails)) {
			MemoryManager.saveIntoCache(UserDetailsManager.USERDETAILS + this.customerId, userDetails);
		}
	}

	public String getUserDetailsFromSession() {
		String userDetails = (String)MemoryManager.getFromCache(UserDetailsManager.USERDETAILS + this.customerId);
		if (StringUtils.isBlank(userDetails)) {
			userDetails = UserDetailsHelper.reloadUserDetailsIntoSession(fabricRequestManager);
		}
		return userDetails;
	}

	public void deleteUserDetailsFromCache() {
		MemoryManager.removeFromCache(UserDetailsManager.USERDETAILS+this.customerId);
	}
}
