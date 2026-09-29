package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface ManualMerchantsBusinessDelagate extends BusinessDelegate {
	public JSONArray getAvailableMerchantsForCreate(String input, DataControllerRequest dcRequest) throws ApplicationException;

}
