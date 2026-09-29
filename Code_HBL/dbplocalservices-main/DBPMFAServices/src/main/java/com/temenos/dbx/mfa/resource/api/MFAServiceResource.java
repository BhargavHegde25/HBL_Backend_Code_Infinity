package com.temenos.dbx.mfa.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MFAServiceResource extends Resource {

    public Result createMFAServiceFromCommunication(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException;

}
