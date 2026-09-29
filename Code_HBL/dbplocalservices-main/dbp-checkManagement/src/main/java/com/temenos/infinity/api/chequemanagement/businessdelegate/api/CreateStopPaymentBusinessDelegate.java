package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

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
public interface CreateStopPaymentBusinessDelegate extends BusinessDelegate {
    /**
     * method to get the create stop payment return result Object
     * 
     * @param request
     */
    StopPayment createStopPayment(StopPayment stopPaymentInstance, DataControllerRequest request)
            throws Exception; 
    
    /**
     * method to get the validate stop payment return result Object
     * 
     * @param request
     */
    StopPayment validateStopPayment(StopPayment stopPaymentInstance, DataControllerRequest request)
            throws ApplicationException; 

}
