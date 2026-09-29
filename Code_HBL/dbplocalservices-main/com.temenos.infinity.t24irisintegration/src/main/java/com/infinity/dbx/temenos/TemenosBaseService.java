package com.infinity.dbx.temenos;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TemenosBaseService implements JavaService2 {

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest arg2, DataControllerResponse arg3)
			throws Exception {

		return new Result();
	}

}
