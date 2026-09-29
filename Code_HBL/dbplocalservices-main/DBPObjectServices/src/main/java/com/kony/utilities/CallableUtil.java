package com.kony.utilities;

import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CallableUtil {

    FabricResponseManager response;
    FabricRequestManager request;

    public CallableUtil(FabricResponseManager response, FabricRequestManager request) {
        this.request = request;
        this.response = response;

    }

}