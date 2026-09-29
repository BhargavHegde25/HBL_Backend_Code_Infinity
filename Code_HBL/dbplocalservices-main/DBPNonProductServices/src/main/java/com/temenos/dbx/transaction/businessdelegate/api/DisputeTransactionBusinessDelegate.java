package com.temenos.dbx.transaction.businessdelegate.api;

import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface DisputeTransactionBusinessDelegate extends BusinessDelegate {

	public Result createDisputeCoreTransaction(Map<String, Object> postParametersMap, Map<String, Object> map);

	public Result createDisputeBillPayTransaction(Map<String, Object> postParametersMap, Map<String, Object> map);

	public Result crearteDisputePayAPersonTransaction(Map<String, Object> postParametersMap,
			Map<String, Object> map);

	public Result crearteDisputeCardTransaction(Map<String, Object> postParametersMap, Map<String, Object> map);

}
