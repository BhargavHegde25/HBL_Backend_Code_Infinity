package com.temenos.infinity.api.wealth.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class GetPortfolioListMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
//		Object customerIdobj = inputParams.get(TemenosConstants.CUSTOMERID);
		
		String customerId = request.getParameter(TemenosConstants.CUSTOMERID).toString();
		if (customerId != null) {
			//customerId = inputParams.get(TemenosConstants.CUSTOMERID).toString();
			inputMap.put(TemenosConstants.CUSTOMERID, customerId);
			PortfolioWealthUtils.saveCoreCustomerIdIntoSession(customerId);
		}
		try {
			StringBuilder sb=new StringBuilder();
			JSONObject portfolioListmock = mockUtil.mockPortfolioList(inputMap);
			JSONObject PortfolioList = (JSONObject) portfolioListmock.get("PortfolioList");
			JSONArray portfolioList = (JSONArray) PortfolioList.get("portfolioList");
			for (int i = 0; i < portfolioList.length(); i++) {
				JSONObject assetObj = portfolioList.getJSONObject(i);
				sb.append(assetObj.get(TemenosConstants.PORTFOLIOID).toString());
				sb.append(",");
			}
			String allPortfolios=sb.deleteCharAt(sb.length() - 1).toString();
			
			PortfolioWealthUtils.savePortfoliosIntoSession(allPortfolios, customerId);
			portfolioListmock.put("opstatus", "0");
			portfolioListmock.put("httpStatusCode", "200");
			//portfolioListmock.put("allPortfolios", allPortfolios);
			
			return Utilities.constructResultFromJSONObject(portfolioListmock);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of GetAssetAllocationMock: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
