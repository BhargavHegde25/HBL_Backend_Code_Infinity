package com.temenos.infinity.dms.businessdelegate.api;

import java.util.ArrayList;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import org.json.JSONObject;

public interface DMSBusinessDelegate extends BusinessDelegate {

	String download(String documentId, String revision, String authToken);

	ArrayList<JSONObject> search(DataControllerRequest request,String page, String accountNnumber, String customerNumber, String year,
			String subType, String authToken) throws ApplicationException;

}
