package com.bct.custom.backenddeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface ManualMerchantsBackendDelagate extends BackendDelegate{
	public JSONArray getAvailableMerchantsForCreate(String input, DataControllerRequest dcRequest) throws ApplicationException;
}
