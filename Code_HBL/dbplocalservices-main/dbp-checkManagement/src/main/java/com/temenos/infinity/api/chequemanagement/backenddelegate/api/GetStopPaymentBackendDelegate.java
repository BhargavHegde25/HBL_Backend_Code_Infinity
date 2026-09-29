package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface GetStopPaymentBackendDelegate extends BackendDelegate{
    /**
     * method to get the stop Payment requests
     * 
     * @param request
     */
    List<StopPayment> getStopPaymentOrdersFromOMS(StopPayment stopPayment, DataControllerRequest request)
            throws ApplicationException;

	List<StopPayment> getStopPaymentOrdersFromT24(StopPayment stopPaymentInstance, DataControllerRequest request)
			throws ApplicationException;

}