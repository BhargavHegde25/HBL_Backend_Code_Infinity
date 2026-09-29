/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.businessdelegate.api;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author muthukumarv
 *
 */
public interface UserLinkingBusinessDelegate extends BusinessDelegate {
	/**
     * 
     * @param inputArray
     * @param dcRequest
     * @return Result
     * @throws ApplicationException
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
     */
	public Result userLinkingOperation(Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException, JSONException, UnsupportedEncodingException;
	
	/**
     * 
     * @param inputArray
     * @param dcRequest
     * @return Result
     * @throws ApplicationException
     */
	public Result userDeLinkingOperation(Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
}
