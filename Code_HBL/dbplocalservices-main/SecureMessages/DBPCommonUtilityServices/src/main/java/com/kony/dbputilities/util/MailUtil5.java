package com.kony.dbputilities.util;

import java.util.Map;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class MailUtil5 implements JavaService2 {

    @Override
    public Object invoke(String arg0, Object[] arg1, DataControllerRequest arg2, DataControllerResponse arg3)
            throws Exception {
        Map<String, String> inputmap = HelperMethods.getInputParamMap(arg1);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        MailHelper.sendMail("InfinityDemos@temenos.com", "Your Credentials for DBX Digital Banking",
                inputmap.get("email"), inputmap.get("newUsername"), inputmap.get("newPassword"),
                inputmap.get("newUsername"), "United States", null);
        return new Result();
    }
}
