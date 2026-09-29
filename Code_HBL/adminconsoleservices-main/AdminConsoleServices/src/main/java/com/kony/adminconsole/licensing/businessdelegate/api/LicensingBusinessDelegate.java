package com.kony.adminconsole.licensing.businessdelegate.api;


import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.dataobject.Result;

public interface LicensingBusinessDelegate extends BusinessDelegate {
	
	public Result getInternalUsersCount() throws DBPApplicationException;

	public Result getExternalUsersCount();


}
