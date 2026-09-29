package com.kony.adminconsole.campaign.resource.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.businessdelegate.api.DataContextBusinessDelegate;
import com.kony.adminconsole.campaign.resource.DataContextResource;
import com.kony.adminconsole.dto.campaign.DataContextRequestDTO;
import com.kony.adminconsole.utilities.ACConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class DataContextResourceImpl implements DataContextResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    @SuppressWarnings("unchecked")
    @Override
    public Result getActiveCountForSegment(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        Result res = new Result();
        try {
            Map<String, String> inputMap = (HashMap<String, String>) inputArray[1];
            String segmentID = inputMap.get(ACConstants.LOOP_PARAM_SEGMENT_ID);
            res.addParam(ACConstants.LOOP_PARAM_SEGMENT_ID, segmentID);
            DataContextRequestDTO dcReqDTO = new DataContextRequestDTO(segmentID,
                    inputMap.get(ACConstants.LOOP_PARAM_ENDPOINT_URL), inputMap.get(ACConstants.LOOP_PARAM_FILTER),
                    inputMap.get(ACConstants.LOOP_PARAM_DATACONTEXT_ID));

            DataContextBusinessDelegate dataContextBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                            .getBusinessDelegate(DataContextBusinessDelegate.class);
            String resString = dataContextBusinessDelegate.getActiveCustomersForSegment(dcReqDTO);
            if (resString.equals(ACConstants.DCCUSTOMERRMSG)) {
                res.addErrMsgParam("Error returned from DataContextProcessing");
            } else {
                res.addParam("Customers", resString);
            }
        } catch (Exception e) {
            alert.prepareError("Error while calling DataContext " + e.getMessage()).log();
        }
        return res;
    }

}
