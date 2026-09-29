package com.kony.adminconsole.multientity.backenddelegate.api;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface MultiEntityBackendDelegate extends BackendDelegate {

	public JSONObject getAllCompanyLegalUnits(String authToken,boolean singleentity, Object sessionId)throws DBPApplicationException;
	public JSONObject getAllCompanyLegalUnitsFromDB(boolean singleentity)throws DBPApplicationException;
}
