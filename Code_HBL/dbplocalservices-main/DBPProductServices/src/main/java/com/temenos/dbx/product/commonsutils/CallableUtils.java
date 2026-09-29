package com.temenos.dbx.product.commonsutils;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class CallableUtils {

	DataControllerResponse response;
	DataControllerRequest request;

    public CallableUtils(DataControllerResponse response, DataControllerRequest request) {
        this.request = request;
        this.response = response;

    }

}