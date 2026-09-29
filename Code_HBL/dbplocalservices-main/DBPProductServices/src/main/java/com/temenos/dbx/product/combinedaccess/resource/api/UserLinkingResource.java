/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.resource.api;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author muthukumarv
 *
 */
public interface UserLinkingResource extends Resource {

	 /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
     */
    public Result userLinkingOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException, JSONException, UnsupportedEncodingException;
    
    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result userDeLinkingOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;
}
