package com.temenos.infinity.api.loanspayoff.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.dataobject.Result;

public interface LoansPayoffResource extends Resource {

	Result createSimulation(String arrangementId, String activityId, String productId, String effectiveDate,String backendToken);

	Result getBillDetails(String arrangementId, String billType, String paymentDate,String backendToken);

}
