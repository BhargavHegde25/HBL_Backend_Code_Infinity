package com.kony.testscripts.updatescripts;

import org.apache.commons.lang3.StringUtils;

import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;

class CacheManagementService implements JavaService2 {

	@Override
	public Object invoke(String arg0, Object[] arg1, 
			DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result res = new Result();
		String key = request.getParameter("key");
		String remove = request.getParameter("remove");
		String newValue = request.getParameter("newValue");
		String time = request.getParameter("time");
		ResultCache cache = ServicesManagerHelper.getServicesManager().getResultCache();
		String oldValue = (String) cache.retrieveFromCache(key);
		res.addStringParam("key", key);
		res.addStringParam("oldValue", oldValue);
		if(StringUtils.isNoneBlank(time)) {
			time = "1200";
		}
		res.addStringParam("time", time);
		if(StringUtils.isNoneBlank(newValue)) {
			cache.insertIntoCache(key, newValue, Integer.getInteger(time));
			res.addStringParam("newValue", newValue);
		}
		if(StringUtils.isNoneBlank(remove)) {
			cache.removeFromCache(key);
			res.addStringParam("removed", "true");
		}
		
		return res;
	}
}
