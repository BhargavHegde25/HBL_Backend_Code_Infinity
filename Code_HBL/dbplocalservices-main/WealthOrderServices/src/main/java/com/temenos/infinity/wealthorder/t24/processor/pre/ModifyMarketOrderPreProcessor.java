/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * 
 * @author muthukumarv
 *
 */
public class ModifyMarketOrderPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> ModifyMarketOrderPreProcessor T24 - Entered ").log();
		// String ordertype = inputMap.get(TemenosConstants.ORDERTYPE).toString();
		// SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		// Calendar cal = Calendar.getInstance();
		try {
			/*
			 * String backendToken =
			 * TokenGenerator.generateAuthToken(T24CertificateConstants.BACKEND,
			 * ServerConfigurations.DBP_HOST_URL.getValue(),
			 * WealthUtils.getUserAttributeFromIdentity(request, "UserName"),
			 * WealthUtils.getUserAttributeFromIdentity(request, "customer_id"),
			 * T24CertificateConstants.ROLEID, T24CertificateConstants.TOKEN_VALIDITY,
			 * true); request.addRequestParam_(TemenosConstants.AUTHORIZATION,
			 * backendToken);
			 */
			request.addRequestParam_(TemenosConstants.CHANNELNAME, "INFINITY");
			String validate = inputMap.get(TemenosConstants.VALIDATEONLY) != null
					? inputMap.get(TemenosConstants.VALIDATEONLY).toString()
					: "";
			if (validate.equals("")) {
				diagnostic.prepareDebug("==========> ModifyMarketOrderPreProcessor T24 - validate_only not set").log();
			} else {
				diagnostic.prepareDebug("==========> ModifyMarketOrderPreProcessor T24 - validate_only set").log();
				inputMap.put(TemenosConstants.VALIDATEONLY, validate);
			}

			/*
			 * inputMap.put(TemenosConstants.ORDERDATE, currDate);
			 * inputMap.put(TemenosConstants.VALUEDATE, currDate);
			 * inputMap.put(TemenosConstants.DEPOSITORYID, "100492");
			 * inputMap.put(TemenosConstants.DEALSTATUS, "TRANSMITTED");
			 */
			inputMap.put(TemenosConstants.CALCULATECHARGES, "YES");
			diagnostic.prepareDebug("==========> ModifyMarketOrderPreProcessor T24 - Entering into integration ").log();
			return true;
		} catch (Exception e) {
			alert.prepareError("==========> ModifyMarketOrderPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;

		}

	}

}
