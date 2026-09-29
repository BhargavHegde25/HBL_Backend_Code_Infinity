package com.temenos.dbx.product.achservices.resource.api;
import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

/**
 * This Class takes in the request payload from JavaService(Integration Layer)
 * and massages the data as per required by Business Delegate contracts to fetch
 * ACH Template Records
 * @author KH2544
 * @version 1.0
 * **/

public interface ACHTemplateRecordResource extends Resource {

    /** This method takes user input and delegates fetching ACHTemplateRecords
     *@param inputArray contains the input parameter to create template
     *@param dcRequest contains request handler
     *@author KH2544
     *@version 1.0
     ***/
    public Result fetchACHTemplateRecord(Object[] inputArray, DataControllerRequest dcRequest);
}
