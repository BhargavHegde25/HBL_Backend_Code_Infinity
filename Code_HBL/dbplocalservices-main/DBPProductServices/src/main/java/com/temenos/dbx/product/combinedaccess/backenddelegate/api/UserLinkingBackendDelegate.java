/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.backenddelegate.api;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author muthukumarv
 *
 */
public interface UserLinkingBackendDelegate extends BackendDelegate {
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
