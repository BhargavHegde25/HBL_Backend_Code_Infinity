/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

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
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetAccountActivityTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAccountActivityTAPPostProcessor TAP - Entered ").log();
			JSONObject accountsActivityJSON = new JSONObject();
            String accId="";
			
			String[] responseFields = new String[] { TemenosConstants.ACCID, TemenosConstants.AMOUNT,
					TemenosConstants.QUANTITY, TemenosConstants.BALANCE, TemenosConstants.DISPLAYNAME,
					TemenosConstants.BOOKINGDATE, TemenosConstants.VALUEDATE, TemenosConstants.SHORTNAME,
					TemenosConstants.CURRENCYID,TemenosConstants.HOLDINGS_TYPE, TemenosConstants.TRANSACTIONREFERENCE, "ISIN" };

			JSONArray accountsActivityArr = new JSONArray();
			JSONArray sortedArray = new JSONArray();
		//	String status = headerRecord.getParamValueByName("status");

			//if (status != null && status.trim().equalsIgnoreCase("success")) {

				Dataset bodyDataset = result.getDatasetById("body");
				
				if(bodyDataset != null) {
				List<Record> records = bodyDataset.getAllRecords();
				diagnostic.prepareDebug("==========> GetAccountActivityTAPPostProcessor TAP - No. of records returned initially: "+ records.size()).log();
				JSONArray bodyArray = new JSONArray();
				for (int j = 0; j < records.size(); j++) {
					JSONObject transactionListJSONObject = new JSONObject();
					Record record = records.get(j);
					transactionListJSONObject = CommonUtils.convertRecordToJSONObject(record);
					bodyArray.put(transactionListJSONObject);
				}

				
				for (int i = 0; i < bodyArray.length(); i++) {
					JSONObject responseObject = bodyArray.getJSONObject(i);
					for (String field : responseFields) {
						if (responseObject.has(field)) {
							if(field.equalsIgnoreCase(TemenosConstants.BOOKINGDATE) || field.equalsIgnoreCase(TemenosConstants.VALUEDATE))
							{
								String date = responseObject.get(field).toString();
								date = date.substring(0, 10);
								responseObject.put(field, date);
							}
							else {
								responseObject.put(field, responseObject.get(field));
							}
						} else {
							responseObject.put(field, "");
						}

					}
					accountsActivityArr.put(responseObject);

				}
								
				if (request.getParameter(TemenosConstants.ACCID) != null
						&& request.getParameter(TemenosConstants.ACCID).length() > 0) {
					accId = request.getParameter(TemenosConstants.ACCID);
					sortedArray = PortfolioServiceUtils.returnAccountsBasedOnId(accountsActivityArr, accId);
					diagnostic.prepareDebug("==========> GetAccountActivityTAPPostProcessor TAP - No. of records returned for the account: "+ accountsActivityArr.length()).log();
				}
				}else {
					sortedArray = new JSONArray();
				}
				
				accountsActivityJSON.put("accountActivityList", sortedArray);
				accountsActivityJSON.put("portfolioID", request.getParameter("portfolioId"));
				accountsActivityJSON.put("accountId", request.getParameter("accountId"));
				accountsActivityJSON.put(TemenosConstants.DATETO, request.getParameter("dateTo"));
				accountsActivityJSON.put(TemenosConstants.DATEFROM, request.getParameter("dateFrom"));

				Result accountsActivityResult = Utilities.constructResultFromJSONObject(accountsActivityJSON);
				accountsActivityResult.addOpstatusParam("0");
				accountsActivityResult.addHttpStatusCodeParam("200");
				accountsActivityResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetAccountActivityTAPPostProcessor TAP - Exited ").log();
				return accountsActivityResult;
		} catch (Exception e) {
			alert.prepareError("==========> GetAccountActivityTAPPostProcessor TAP - Error: " + e.getMessage()).log();
		}
		return result;
	}

}
