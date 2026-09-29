package com.dbp.externalevent.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;

public interface UpdateEventConfigBusinessDelegate extends BusinessDelegate {

	boolean updateEventConfigData();

	Map<String, String> getEventConfigData();

}
