/**
 * 
 */
package com.infinity.dbx.temenos.auth;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public class Logout implements JavaService2{

	@Override
	public Object invoke(String methodId, Object[] paramsArray, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result result = new Result();
//		TemenosUtils.getInstance().invalidateSession(request);
		return result;
	}

}
