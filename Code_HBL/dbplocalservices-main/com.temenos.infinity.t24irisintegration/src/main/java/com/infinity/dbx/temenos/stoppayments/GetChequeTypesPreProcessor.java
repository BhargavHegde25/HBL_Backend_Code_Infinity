package com.infinity.dbx.temenos.stoppayments;

import java.util.HashMap;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeTypesPreProcessor extends TemenosBasePreProcessor{
    
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        super.execute(params, request, response, result);
        return true;
    }

}
