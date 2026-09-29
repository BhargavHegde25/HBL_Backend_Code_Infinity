/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Generates Transact Token. Needs to be included for all transact
 * services. Prepares the input in the desired format.
 * @author himaja.sridhar
 *
 */
public class CreateOrderStopLossPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		String ordertype = inputMap.get(TemenosConstants.ORDERTYPE).toString();
		if (ordertype.equalsIgnoreCase("STOP LOSS")) {
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			String currDate = sdformat.format(cal.getTime());
			try {
				/*String backendToken = TokenGenerator.generateAuthToken(T24CertificateConstants.BACKEND,
						ServerConfigurations.DBP_HOST_URL.getValue(),
						WealthUtils.getUserAttributeFromIdentity(request, "UserName"),
						WealthUtils.getUserAttributeFromIdentity(request, "customer_id"),
						T24CertificateConstants.ROLEID, T24CertificateConstants.TOKEN_VALIDITY, true);
				request.addRequestParam_(TemenosConstants.AUTHORIZATION, backendToken);*/
				request.addRequestParam_(TemenosConstants.CHANNELNAME, "INFINITY");
				String validate = inputMap.get(TemenosConstants.VALIDATEONLY) != null ? inputMap.get(TemenosConstants.VALIDATEONLY).toString() : "";
				if(validate.equals("")) {
					
				}
				else {
				//request.addRequestParam_(TemenosConstants.VALIDATEONLY, validate);
					inputMap.put(TemenosConstants.VALIDATEONLY,validate);
				}
				
				String order = inputMap.get(TemenosConstants.TRANSACTIONTYPE).toString();
				inputMap.put(TemenosConstants.TRANSACTIONTYPE, order);
				inputMap.put(TemenosConstants.ORDERTYPE, ordertype.substring(0, 4).toUpperCase());
				inputMap.put(TemenosConstants.ORDERDATE, currDate);
				inputMap.put(TemenosConstants.VALUEDATE, currDate);
				inputMap.put(TemenosConstants.DEALSTATUS, "TRANSMITTED");
				inputMap.put(TemenosConstants.CALCULATECHARGES, "YES");

				return true;
			} catch (Exception e) {
				alert.prepareError("Error in CreateMarketOrderStopLossPreProcessor" + e).log();
				return false;
			}
		} else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);

			return false;
		}
	}

}
