package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.utils.CustomerSession;

public class QRPaymentTransactionHistory implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(QRPaymentTransactionHistory.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		LOG.debug("In QRPaymentTransactionHistory:::");
		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
			LOG.debug("customer object in session::" + customer);
			String customerId = CustomerSession.getCustomerId(customer);
			LOG.debug("customerId from session obj::" + customerId);
			Map<String, Object> inputmap = new HashMap<>();
			String filter = "";
			if (StringUtils.isNotBlank(customerId)) {
				filter = "Customer_id eq '" + customerId + "' and softdeleteflag eq 0";
			}
			inputmap.put(HBLURLConstants.FILTER, filter);
			LOG.debug("QRPaymentTransactionHistory inputmap:::" + inputmap.toString());
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.QR_TRANSACTION_HISTORY_GET).withRequestParameters(inputmap)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(dcRequest.getHeaderMap())
					.build().getResponse();
			LOG.debug("QRPaymentTransactionHistory QR Transactions response:::" + dbresponse);
			result = JSONToResult.convert(dbresponse);
			LOG.debug("Result qr transactionHistory:::" + ResultToJSON.convert(result));
			result.addParam(new Param("opstatus", "0"));
			result.addParam(new Param("httpStatusCode", "200"));
			result.addParam(new Param("success", "true"));
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the qr transactions:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

}
