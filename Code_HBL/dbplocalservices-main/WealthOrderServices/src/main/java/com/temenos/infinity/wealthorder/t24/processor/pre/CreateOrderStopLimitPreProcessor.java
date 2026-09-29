/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class CreateOrderStopLimitPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  CreateOrderStopLimitPreProcessor T24 - Entered").log();
		String ordertype = inputMap.get(TemenosConstants.ORDERTYPE).toString();
		if (ordertype.equalsIgnoreCase("STOP LIMIT")) {

			if (request.getParameter(TemenosConstants.OPSTATUS).equalsIgnoreCase("0")) {
				if (request.getParameter("id") != null) {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========>  CreateOrderStopLimitPreProcessor T24 - Exiting stoplimit order flow").log();
					return false;
				} else {
					// String ordertype = inputMap.get(TemenosConstants.ORDERTYPE).toString();
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
					String bankDate, currDate = null;
					if(null != request.getParameter("bankDate") && request.getParameter("bankDate").toString().length()>0)
					{
						bankDate = request.getParameter("bankDate").toString();
						currDate= bankDate.replace("-","");
					}else
					{
					Calendar cal = Calendar.getInstance();
						currDate = sdformat.format(cal.getTime());
					}
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

						} else {
							// request.addRequestParam_(TemenosConstants.VALIDATEONLY, validate);
							inputMap.put(TemenosConstants.VALIDATEONLY, validate);
						}

						// inputMap.remove(TemenosConstants.VALIDATEONLY);

						String order = inputMap.get(TemenosConstants.TRANSACTIONTYPE).toString().toUpperCase();
						inputMap.put(TemenosConstants.TRANSACTIONTYPE, order);
						inputMap.put(TemenosConstants.ORDERTYPE, ordertype.substring(0, 4).toUpperCase()
								.concat(ordertype.substring(5, 10).toUpperCase()));
						inputMap.put(TemenosConstants.ORDERDATE, currDate);
						inputMap.put(TemenosConstants.VALUEDATE, currDate);
						inputMap.put(TemenosConstants.DEALSTATUS, "TRANSMITTED");
						inputMap.put(TemenosConstants.CALCULATECHARGES, "YES");
						diagnostic.prepareDebug("==========>  CreateOrderStopLimitPreProcessor T24 - Entering stoplimit order flow").log();
						return true;
					} catch (Exception e) {
						alert.prepareError("==========> CreateOrderStopLimitPreProcessor T24 - Error: " + e.getMessage()).log();
						return false;
					}
				}
			}
			return false;
		} else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========>  CreateOrderStopLimitPreProcessor T24 - Exiting stoplimit order flow").log();
			return false;
		}
	}
}
