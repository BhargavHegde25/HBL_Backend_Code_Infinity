package com.kony.adminconsole.multientity.businessdelegate.api;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.dataobject.Result;



public interface MultiEntityBusinessDelegate extends BusinessDelegate  {
	
	public Result getAllCompanyLegalUnits(String authToken,boolean singleentity, Object sessionId) throws DBPApplicationException;

}
