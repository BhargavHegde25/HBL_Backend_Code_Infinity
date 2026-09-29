package com.kony.kmsinvoke.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface SendSmsResource extends Resource {

	Result sendSms(Object[] inputArray, DataControllerRequest request);
}
