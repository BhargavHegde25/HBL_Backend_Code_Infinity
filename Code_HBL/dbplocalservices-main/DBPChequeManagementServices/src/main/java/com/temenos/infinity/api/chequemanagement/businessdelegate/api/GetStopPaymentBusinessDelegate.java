package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface GetStopPaymentBusinessDelegate extends BusinessDelegate {
    /**
     * method to get the create stop payment return result Object
     * 
     * @param request
     * @throws com.temenos.infinity.api.commons.exception.ApplicationException 
     */
    List<StopPayment> getStopPaymentRequests(StopPayment stopPaymentInstance, DataControllerRequest request)
            throws ApplicationException; 
}

