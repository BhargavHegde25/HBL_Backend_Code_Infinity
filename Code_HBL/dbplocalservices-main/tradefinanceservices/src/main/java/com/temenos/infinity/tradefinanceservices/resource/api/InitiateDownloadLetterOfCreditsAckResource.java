package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface InitiateDownloadLetterOfCreditsAckResource extends Resource {

   Result initiateDownloadLetterOfCreditsAck(Object[] inputArray,DataControllerRequest request);

} 