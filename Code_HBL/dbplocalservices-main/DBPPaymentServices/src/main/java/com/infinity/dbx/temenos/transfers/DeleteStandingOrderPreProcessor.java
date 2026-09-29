package com.infinity.dbx.temenos.transfers;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DeleteStandingOrderPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			super.execute(params, request, response, result);
			String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
			transactionType = transactionType.replace(" ", "");
			alert.prepareError("Delete Transfer Params:"+params).log();
			if (StringUtils.isNotBlank(transactionType)
					&& !TransferConstants.TRANSCTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)
					&& !TransferConstants.TRANSCTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
				CommonUtils.setOpStatusOk(result);
				result.addParam(new Param(Constants.PARAM_HTTP_STATUS_CODE, Constants.PARAM_HTTP_STATUS_OK,
						Constants.PARAM_DATATYPE_STRING));
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Exception occurred in Deleting Transfer:" + e).log();
			return false;
		}

		return Boolean.TRUE;
	}

}
