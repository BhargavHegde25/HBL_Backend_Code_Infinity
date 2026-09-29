/**
 * 
 */
package com.temenos.infinity.wealth.t24.processor.post;

import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetAccountActivityT24PostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAccountActivityT24PostProcessor T24 - Entered ").log();
			JSONObject accountsActivityJSON = new JSONObject();
			JSONArray emptyArr = new JSONArray();
			Record headerRecord = result.getRecordById("header");
			String status = headerRecord.getParamValueByName("status");

			if (status != null && status.trim().equalsIgnoreCase("success")) {

				Dataset bodyDataset = result.getDatasetById("body");
				if (bodyDataset != null) {
					List<Record> records = bodyDataset.getAllRecords();
					JSONArray bodyArray = new JSONArray();
					for (int j = 0; j < records.size(); j++) {
						JSONObject transactionListJSONObject = new JSONObject();
						Record record = records.get(j);
						transactionListJSONObject = CommonUtils.convertRecordToJSONObject(record);
						bodyArray.put(transactionListJSONObject);
					}
					diagnostic.prepareDebug(
							"==========> GetAccountActivityT24PostProcessor T24 - No. of records returned initially: "
									+ bodyArray.length())
							.log();
					String[] responseFields = new String[] { TemenosConstants.ACCID, TemenosConstants.AMOUNT,
							TemenosConstants.QUANTITY, TemenosConstants.BALANCE, TemenosConstants.DISPLAYNAME,
							TemenosConstants.BOOKINGDATE, TemenosConstants.VALUEDATE, TemenosConstants.SHORTNAME,
							TemenosConstants.CURRENCYID, TemenosConstants.HOLDINGS_TYPE,
							TemenosConstants.TRANSACTIONREFERENCE, "ISIN" };

				JSONArray accountsActivityArr = new JSONArray();
				for (int i = 0; i < bodyArray.length(); i++) {
					JSONObject responseObject = bodyArray.getJSONObject(i);
					for (String field : responseFields) {
						if (responseObject.has(field)) {
							responseObject.put(field, responseObject.get(field));
						} else {
							responseObject.put(field, "");
						}

					}
					accountsActivityArr.put(responseObject);

				}
				diagnostic.prepareDebug("==========> GetAccountActivityT24PostProcessor T24 - No. of activities returned: " + accountsActivityArr.length() ).log();
				accountsActivityJSON.put("accountActivityList", accountsActivityArr);
				accountsActivityJSON.put("portfolioID", request.getParameter("portfolioId"));
				accountsActivityJSON.put("accountId", request.getParameter("accountId"));
				accountsActivityJSON.put(TemenosConstants.DATETO, request.getParameter("dateTo"));
				accountsActivityJSON.put(TemenosConstants.DATEFROM, request.getParameter("dateFrom"));

					Result accountsActivityResult = Utilities.constructResultFromJSONObject(accountsActivityJSON);
					accountsActivityResult.addOpstatusParam("0");
					accountsActivityResult.addHttpStatusCodeParam("200");
					accountsActivityResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					return accountsActivityResult;
				} else {
					accountsActivityJSON.put("accountActivityList", emptyArr);
					Result portfolioRes = Utilities.constructResultFromJSONObject(accountsActivityJSON);
					portfolioRes.addOpstatusParam("0");
					portfolioRes.addHttpStatusCodeParam("200");
					portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					return portfolioRes;
				}
			} else {
				Record errorRecord = result.getRecordById("error");
				alert.prepareError("==========> GetAccountActivityT24PostProcessor T24 - Error from API").log();
				String code = errorRecord.getParamValueByName("code");
				if (code.equalsIgnoreCase("TGVCP-007")) {			
					accountsActivityJSON.put("accountActivityList", emptyArr);
					Result portfolioRes = Utilities.constructResultFromJSONObject(accountsActivityJSON);
					portfolioRes.addOpstatusParam("0");
					portfolioRes.addHttpStatusCodeParam("200");
					portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					return portfolioRes;
				} else {
					accountsActivityJSON.put("errormessage", errorRecord.getParamValueByName("message"));
					accountsActivityJSON.put("errorcode", errorRecord.getParamValueByName("code"));
					Result errorResponse = Utilities.constructResultFromJSONObject(accountsActivityJSON);
					return errorResponse;
				}
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetAccountActivityT24PostProcessor T24 - Error: " + e.getMessage()).log();
		}
		return result;
	}

}
