package com.kony.dbputilities.demoservices;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public interface DemoDataService {

    String FORMAT = "yyyy-MM-dd'T'00:00:00";

    Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception;
}