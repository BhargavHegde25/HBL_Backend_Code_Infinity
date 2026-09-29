/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.businessdelegate.impl;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.combinedaccess.backenddelegate.api.UserLinkingBackendDelegate;
import com.temenos.dbx.product.combinedaccess.businessdelegate.api.UserLinkingBusinessDelegate;

/**
 * @author muthukumarv
 *
 */
public class UserLinkingBusinessDelegateImpl implements UserLinkingBusinessDelegate {

	public Result userLinkingOperation(Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException, JSONException, UnsupportedEncodingException{
		Result result = new Result();
	            UserLinkingBackendDelegate userLinkingBackendDelegate = DBPAPIAbstractFactoryImpl
	                    .getBackendDelegate(UserLinkingBackendDelegate.class);
	            result = userLinkingBackendDelegate.userLinkingOperation(inputArray, dcRequest,dcResponse);
	        
		return result;
	}

	@Override
	public Result userDeLinkingOperation(Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
	            UserLinkingBackendDelegate userDeLinkingBackendDelegate = DBPAPIAbstractFactoryImpl
	                    .getBackendDelegate(UserLinkingBackendDelegate.class);
	            result = userDeLinkingBackendDelegate.userDeLinkingOperation(inputArray, dcRequest,dcResponse);
	        
		return result;
	}
}
