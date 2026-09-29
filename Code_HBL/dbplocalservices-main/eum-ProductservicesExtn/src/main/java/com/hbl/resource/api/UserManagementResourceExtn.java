package com.hbl.resource.api;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.Resource;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public interface UserManagementResourceExtn extends Resource {

	 public Result verifyCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws JSONException, UnsupportedEncodingException, DBPApplicationException, MiddlewareException;
}
