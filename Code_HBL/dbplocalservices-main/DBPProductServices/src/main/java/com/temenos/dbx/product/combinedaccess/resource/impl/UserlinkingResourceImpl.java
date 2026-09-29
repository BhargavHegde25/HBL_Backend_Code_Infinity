/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.resource.impl;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.combinedaccess.businessdelegate.api.UserLinkingBusinessDelegate;
import com.temenos.dbx.product.combinedaccess.resource.api.UserLinkingResource;

/**
 * @author muthukumarv
 *
 */
public class UserlinkingResourceImpl implements UserLinkingResource {
	private static LoggerUtil logger = new LoggerUtil(UserlinkingResourceImpl.class);
	@Override
	public Result userLinkingOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException, JSONException, UnsupportedEncodingException {
		Result result = new Result();
			UserLinkingBusinessDelegate userLinkingBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(UserLinkingBusinessDelegate.class);
			result = userLinkingBusinessDelegate.userLinkingOperation(inputArray, dcRequest, dcResponse);
		return result;
	}
	@Override
	public Result userDeLinkingOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
			UserLinkingBusinessDelegate userDeLinkingBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(UserLinkingBusinessDelegate.class);
			result = userDeLinkingBusinessDelegate.userDeLinkingOperation(inputArray, dcRequest, dcResponse);
		return result;
	}

}
