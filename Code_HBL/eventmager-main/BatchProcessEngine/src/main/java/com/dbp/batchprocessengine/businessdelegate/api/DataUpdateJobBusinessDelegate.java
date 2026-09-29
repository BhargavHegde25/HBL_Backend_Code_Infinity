package com.dbp.batchprocessengine.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

public interface DataUpdateJobBusinessDelegate {

	List<String> getEventTypes(String servicetype);

	Set<String>  getSubscribers(List<String> eventtypes);

	JsonObject callCoreService(Set<String> customeraccountdata, String servicetype);

}
