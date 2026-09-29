package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.util.ArrayList;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.util.StrategyUtil;
import com.temenos.infinity.api.wealth.vo.AssetVO;
import com.temenos.infinity.api.wealth.vo.ComputeVO;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.exception.InfWlthException;

/**
 * @author vinayranjan.sharma
 *
 */

public class ComputeStrategyPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];

		JSONArray finalResult = new JSONArray();

		try {
			HashMap<String, ArrayList<AssetVO>> assetMap = new HashMap<String, ArrayList<AssetVO>>();
			ArrayList<AssetVO> assetList = new ArrayList<AssetVO>();
			assetMap.put(portfolioId, assetList);
			Session session = request.getSession(false);
			if (null != session) {
				assetMap = (HashMap<String, ArrayList<AssetVO>>) session.getAttribute(portfolioId);
			}

			ComputeVO computeVO = new ComputeVO();
			int UpdateCount = Integer.parseInt(request.getParameterValues("UpdateCount")[0]);
			int ID = Integer.parseInt(request.getParameterValues(TemenosConstants.ID)[0]);
			Double recommendedWeight = Double.parseDouble(request.getParameterValues("recommendedWeight")[0]);
			Double targetWeight = Double.parseDouble(request.getParameterValues("targetWeight")[0]);
			computeVO.setId(ID);
			computeVO.setPortfolioId(portfolioId);
			computeVO.setUpdateCount(UpdateCount);
			computeVO.setName(request.getParameterValues(TemenosConstants.NAME)[0]);
			computeVO.setRecommendedValue(recommendedWeight);
			computeVO.setTargetValue(targetWeight);
			assetMap = StrategyUtil.computeAssetData(assetMap, computeVO);
			session.setAttribute(portfolioId, assetMap);
			assetList = assetMap.get(computeVO.getPortfolioId());

			JSONArray array1 = new JSONArray(assetList);
			int len = array1.length() - 1;
			JSONObject assetObj = array1.getJSONObject(len);
			String updateCount = assetObj.get("updateCount").toString();
			String assetOb = assetObj.get("segmentList").toString();
			JSONArray array2 = new JSONArray(assetOb);
			for (int i = 0; i < array2.length(); i++) {
				JSONObject segObj = array2.getJSONObject(i);
				JSONObject segResult = new JSONObject();
				if (segObj.has("regionList")) {
					JSONArray regObj = segObj.getJSONArray("regionList");
					for (int k = 0; k < regObj.length(); k++) {
						JSONObject regResult = new JSONObject();
						JSONObject regObj1 = regObj.getJSONObject(k);
						if (regObj1.has("sectorList")) {
							JSONArray secObj = regObj1.getJSONArray("sectorList");
							for (int j = 0; j < secObj.length(); j++) {
								JSONObject segObjF = secObj.getJSONObject(j);
								JSONObject secResult = new JSONObject();
								Double recommendedWeightSec = Double
										.parseDouble(segObjF.get("recommendedValue").toString());
								Double targetWeightSec = Double.parseDouble(segObjF.get("targetValue").toString());
								secResult.put(TemenosConstants.ID, segObjF.get("sectorId").toString());
								secResult.put(TemenosConstants.NAME, segObjF.get("sectorName"));
								secResult.put("targetWeight", String.format("%.2f", targetWeightSec));
								secResult.put("recommendedWeight", String.format("%.2f", recommendedWeightSec));
								secResult.put("level", "3");
								secResult.put("parentId", regObj1.get("regionId").toString());
								secResult.put("marketSegmentId", segObjF.get("sectorId").toString());
								secResult.put("isCustomized", "false");
								finalResult.put(secResult);
							}
						}
						Double recommendedWeightReg = Double.parseDouble(regObj1.get("recommendedValue").toString());
						Double targetWeightReg = Double.parseDouble(regObj1.get("targetValue").toString());
						regResult.put(TemenosConstants.ID, regObj1.get("regionId").toString());
						regResult.put(TemenosConstants.NAME, regObj1.get("regionName"));
						regResult.put("targetWeight", String.format("%.2f", targetWeightReg));
						regResult.put("recommendedWeight", String.format("%.2f", recommendedWeightReg));
						regResult.put("level", "2");
						regResult.put("parentId", segObj.get("segmentId").toString());
						regResult.put("marketSegmentId", regObj1.get("regionId").toString());
						regResult.put("isCustomized", "false");
						finalResult.put(regResult);
					}
				}
				Double recommendedWeightSeg = Double.parseDouble(segObj.get("recommendedValue").toString());
				Double targetWeightSeg = Double.parseDouble(segObj.get("targetValue").toString());
				segResult.put(TemenosConstants.ID, segObj.get("segmentId").toString());
				segResult.put(TemenosConstants.NAME, segObj.get("segmentName"));
				segResult.put("targetWeight", String.format("%.2f", targetWeightSeg));
				segResult.put("recommendedWeight", String.format("%.2f", recommendedWeightSeg));
				segResult.put("isCustomized", segObj.get("customized"));
				segResult.put("level", "1");
				segResult.put("parentId", "0");
				segResult.put("marketSegmentId", segObj.get("segmentId").toString());
				finalResult.put(segResult);
			}
			JSONObject computedResJSON = new JSONObject();
			computedResJSON.put("personalizedStrategy", finalResult);
			Result final_result = Utilities.constructResultFromJSONObject(computedResJSON);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			final_result.addParam("updateCount", updateCount);
			result.appendResult(final_result);
		} catch (InfWlthException iwEx) {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam("dbpErrCode", "84001");
			result.addParam("dbpErrMsg", "Percentage allocation is not equal to 100%.");
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking ComputeStrategyPostProcessor MOCK - " + e).log();
			throw new Exception("Error while invoking ComputeStrategyPostProcessor MOCK ");
		}
		return result;
	}

}
