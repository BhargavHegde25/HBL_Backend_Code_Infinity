package com.temenos.infinity.api.transactionadviceapi.javaservice;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.transactionadviceapi.constants.ErrorCodeEnum;
import com.temenos.infinity.api.transactionadviceapi.resource.api.TransactionAdviceResource;
import com.temenos.infinity.api.transactionadviceapi.resource.impl.TransactionAdviceResourceImpl;
import com.temenos.infinity.api.transactionadviceapi.utils.TransactionAdviceUtilities;

/**
 * 
 * @author suryaacharans
 * @version Java Service end point to login and get autoform file as base 64 string 
 * 
 */
public class ViewFileOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			TransactionAdviceResource downloadResource = DBPAPIAbstractFactoryImpl
					.getResource(TransactionAdviceResource.class);
			String customerId = TransactionAdviceUtilities.getT24BackendId(request);
			String accountId = request.getParameter("accountId");
			String transactionRef = request.getParameter("transactionRef");
			String mediaType = request.getParameter("mediaType");
			String transactionType = request.getParameter("transactionType");
			String page = request.getParameter("page");
			//Since security level is already authenticated app user, no need to overwrite. Hence setting it as null
			String auth_token=null;
			String operation="view";
			
			if (StringUtils.isEmpty(customerId)) {
				diagnostic.prepareDebug("Not authorized to get backend id of customer,proceeding with customer ID").log();
				customerId=TransactionAdviceUtilities.getUserAttributeFromIdentity(request, "customer_id"); 
			}
			
			Result result = downloadResource.loginAndDownload(customerId, accountId, transactionRef, mediaType,
					transactionType, page,operation,auth_token);
			return result;
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());

		}

	}

}
