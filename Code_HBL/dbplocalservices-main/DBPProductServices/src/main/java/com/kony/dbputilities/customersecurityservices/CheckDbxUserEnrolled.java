package com.kony.dbputilities.customersecurityservices;


import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.javaservice.CheckUserEnrolledOperation;

public class CheckDbxUserEnrolled implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
       return new CheckUserEnrolledOperation().invoke(methodID, inputArray, dcRequest, dcResponse);
    }
}