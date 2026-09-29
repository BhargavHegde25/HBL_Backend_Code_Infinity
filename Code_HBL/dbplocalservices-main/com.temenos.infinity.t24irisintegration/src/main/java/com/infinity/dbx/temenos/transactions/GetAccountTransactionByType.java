package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountTransactionByType implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputMap, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
	Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		Result result = new Result();
		try {
			Dataset Transactions = new Dataset(TRANSACTION);
			String transactionType = request.getParameter(PARAM_TRANSACTION_TYPE);
			String isScheduled = request.getParameter(PARAM_IS_SCHEDULED);
			if (StringUtils.isNotBlank(isScheduled) && TRUE.equalsIgnoreCase(isScheduled))
				result = (Result) CommonUtils.callIntegrationService(request, null, null, SERVICE_ID_TRANSORCH,
						OPER_ID_SCHEDULED, true);
			
			else if (StringUtils.isNotBlank(transactionType)) {
				if (transactionType.equalsIgnoreCase("loanschedule")) {
					Map<String,Object> inputparam=new HashMap<String, Object>();
					if(inputMap.length >1) {
						inputparam=(Map<String,Object>) inputMap[1];
					}
					request.addRequestParam_("isFutureRequired", "false");
					result = (Result) CommonUtils.callIntegrationService(request,inputparam, null, SERVICE_ID_ARRTRANSACTIONS,
							OPER_ID_LOANSCHEDULE, true);
				} else {
					result = (Result) CommonUtils.callIntegrationService(request, null, null, SERVICE_ID_TRANSORCH,
							OPER_ID_TRANSACTIONS, true);
				}
			} else {
				result.addDataset(Transactions);
			}

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

}
