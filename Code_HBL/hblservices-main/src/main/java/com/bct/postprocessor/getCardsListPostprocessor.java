package com.bct.postprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.CardConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class getCardsListPostprocessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(getCardsListPostprocessor.class);

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String HBL_IMAGES_APP_URL = paramHelper.getServerProperty("HBL_IMAGES_APP_URL");
			logger.debug("Result:###" + ResultToJSON.convert(result));
			Dataset Cards = result.getDatasetById("cardDataInfo_out");
			logger.debug("Transactions DS Result:###" + Cards.toString());
			List<Record> cardRecords = Cards != null ? Cards.getAllRecords() : null;
			logger.debug("transactionRecords size:" + cardRecords.size() + "");
			String errmsg = result.getParamValueByName("respCode_out");
			logger.debug("CardList Errmsg:" + errmsg);

			String Card_Label = "";
			String Card_Type = "";
			String Card_Category = "";
			String cardimage = "";
			String cardImageUrl = "";

			if (StringUtils.isNotBlank(errmsg)) {
				String regex = "^(?!0{2,3}$)\\d+$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(errmsg);

				// Find and display matches
				while (matcher.find()) {
					result.addParam("errcode", result.getParamValueByName("respCode_out"));
					result.addParam("errmsg", CardConstants.GetCardsErrMessage(errmsg));
				}
			}
			
			result.removeParamByName("errcode");
			result.removeParamByName("errmsg");
			
			if (cardRecords.size() != 0) {
				for (Record record : cardRecords) {
					String cardProgLabel = record.getParamValueByName("cardProgLabel");
					if (StringUtils.isNotBlank(cardProgLabel)) {

						String accCurr = record.getParamValueByName("accCurr");
						String currencyCode = getCurrencyCode(accCurr);

						record.addParam("currCode", currencyCode);

						JSONObject cardInfo = getCardAdditionalDetails(request, cardProgLabel);
						logger.debug("cardInfo: data ###" + cardInfo.toString());

						if (cardInfo.length() == 0) {
							if (cardInfo != null) {
								if (Card_Label != null)
									record.addParam("Card_Label", "HBL VISA DEBIT 1 YEAR");

								if (Card_Type != null)
									record.addParam("Card_Type", "VISA");

								if (Card_Category != null)
									record.addParam("Card_Category", "Debit Domestic");

								if (cardimage != null)
									record.addParam("cardimage",
											HBL_IMAGES_APP_URL + "/cardImages/" + "visadebit" + ".png");
							}

						} else {
							Card_Label = cardInfo.getString("Card_Label");
							logger.debug("Card_Label:###" + Card_Label);
							Card_Type = cardInfo.getString("Card_Type");
							logger.debug("Card_Type:###" + Card_Type);
							Card_Category = cardInfo.getString("Card_Category");
							logger.debug("Card_Category:###" + Card_Category);
							cardimage = cardInfo.getString("cardimage");
							cardImageUrl = HBL_IMAGES_APP_URL + "/cardImages/" + cardimage + ".png";
							logger.debug("cardimage:###" + cardimage);
							if (cardInfo != null) {
								if (Card_Label != null)
									record.addParam("Card_Label", Card_Label);

								if (Card_Type != null)
									record.addParam("Card_Type", Card_Type);

								if (Card_Category != null)
									record.addParam("Card_Category", Card_Category);

								if (cardimage != null)
									record.addParam("cardimage", cardImageUrl);
							}
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

	private JSONObject getCardAdditionalDetails(DataControllerRequest request, String Symbol) {

		JSONObject cardInfo = new JSONObject();
		try {

			String filter = CommonUtils.buildOdataCondition("Symbol", Constants.EQUAL, Symbol);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, "dbxdb_cardtypes_get", false);
			Dataset cardDataset = result.getDatasetById("cardtypes");

			if (null != cardDataset) {
				// Converting dataset to json array
				JSONArray array = ResultToJSON.convertDataset(cardDataset);
				cardInfo = array.getJSONObject(0);
			}
			logger.debug("getCardAdditionalDetails customerInfo:" + cardInfo);
		} catch (Exception e) {
			logger.error("Error while retrieving getCardAdditionalDetails for Customer " + Symbol);
		}
		return cardInfo;

	}

	public String getCurrencyCode(String crrency) {
		switch (crrency) {
		case "840":
			return "USD";
		case "524":
			return "NPR";
		default:
			// Unknown Error message ID
			return "NPR";
		}
	}
}
