package com.bct.postprocessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.CardConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class cardStmtEnqryPostprocessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(cardStmtEnqryPostprocessor.class);

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String nprCurrency = paramHelper.getServerProperty("CARD_TOPUP_NPR_CURRENCY_CONVERSION");
			String usdCurrency = paramHelper.getServerProperty("CARD_TOPUP_USD_CURRENCY_CONVERSION");
			
			String errmsg = result.getParamValueByName("respCode_out");
			logger.debug("errmsg :###" + errmsg);
			
			String regex = "^(?!0{2,3}$)\\d+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(errmsg);

			// Find and display matches
			if (matcher.find()) {
				result.addParam("errcode", result.getParamValueByName("respCode_out"));
				result.addParam("errmsg", CardConstants.StmtEnquiryErrMessage(errmsg));
			}else {

			logger.debug("STMT ENQUIRY Result:###" + ResultToJSON.convert(result));
			Dataset Cards = result.getDatasetById("transTypeTab_out");
			logger.debug("STMT ENQUIRY DS Result:###" + Cards.toString());
			List<Record> cardRecords = Cards != null ? Cards.getAllRecords() : null;
			logger.debug("STMT ENQUIRY size:" + cardRecords.size() + "");

			if (cardRecords.size() != 0) {
				for (Record record : cardRecords) {

					String tranCurr = record.getParamValueByName("tranCurr");
					String tranBillCurr = record.getParamValueByName("tranBillCurr");

					if (tranCurr.equalsIgnoreCase(nprCurrency)) {
						record.addParam("tranCurr", "NPR");
					} else if (tranCurr.equalsIgnoreCase(usdCurrency)) {
						record.addParam("tranCurr", "USD");
					}

					if (tranBillCurr.equalsIgnoreCase(nprCurrency)) {
						record.addParam("tranBillCurr", "NPR");
					} else if (tranBillCurr.equalsIgnoreCase(usdCurrency)) {
						record.addParam("tranBillCurr", "USD");
					}
				}
			 }
			}

		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

}
