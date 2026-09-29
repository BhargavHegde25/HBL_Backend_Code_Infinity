package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

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
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.vo.AssetVO;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetPersonalizedStrategyPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	
	/**
 * @author vinayranjan.sharma
 *
 */
 
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
		JSONArray finalResult = new JSONArray();
		
		try {
			HashMap<String, ArrayList<AssetVO>>  assetMap = new HashMap<String, ArrayList<AssetVO>> ();
			ArrayList<AssetVO> assetList = new ArrayList<AssetVO>();
			Session session= request.getSession(false);
			assetMap =  (HashMap<String, ArrayList<AssetVO>>) session.getAttribute(portfolioId);
			if (null != assetMap && !assetMap.isEmpty())
			{
				assetList = assetMap.get(portfolioId);
				JSONArray array1 = new JSONArray(assetList);
				int len =array1.length()-1;
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
									Double recommendedWeightSec = Double.parseDouble(segObjF.get("recommendedValue").toString());
									Double targetWeightSec = Double.parseDouble(segObjF.get("targetValue").toString()) ;
									secResult.put(TemenosConstants.ID, segObjF.get("sectorId").toString());
									secResult.put(TemenosConstants.NAME, segObjF.get("sectorName"));
									secResult.put("targetWeight", String.format("%.2f", targetWeightSec));
									secResult.put("recommendedWeight",  String.format("%.2f", recommendedWeightSec));
									secResult.put("level", "3");
									secResult.put("parentId", regObj1.get("regionId").toString());
									secResult.put("marketSegmentId", segObjF.get("sectorId").toString());
									secResult.put("isCustomized", "false");
									finalResult.put(secResult);
								}	
							}
							Double recommendedWeightReg = Double.parseDouble(regObj1.get("recommendedValue").toString());
							Double targetWeightReg = Double.parseDouble(regObj1.get("targetValue").toString()) ;
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
					Double targetWeightSeg = Double.parseDouble(segObj.get("targetValue").toString()) ;
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
				result.appendResult(final_result);
				return result;
			}

			if (portfolioId.equalsIgnoreCase("100777-4")) {
				JSONObject returnObj = new JSONObject();
				returnObj.put("personalizedStrategy", assets(portfolioId));
//				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);
				returnObj.put("wealthCoreFlag", "true");
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);

			} else if (portfolioId.equalsIgnoreCase("100777-5")) {
				JSONObject returnObj = new JSONObject();
				returnObj.put("personalizedStrategy", assets(portfolioId));
//				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);
				returnObj.put("wealthCoreFlag", "true");
				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);
			}
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking GetPersonalizedStrategyPostProcessor MOCK - "
					+ PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOHEALTHRECOMMENDEDINSTRUMENTS.getOperationName() + "  : " + e).log();
		}
		return result;
	}

	private JSONArray assets(String portfolioId) {
		String sectorCWeight[] = new String[]{"25.00","25.00","25.00","25.00","15.00","5.00","5.00","20.00", "4.00",
				"1.00","25.00","1.50","2.50","1.50","2.75","1.25","1.50","2.00","1.00","1.00","5.00","5.00","10.00","5.00",
				"5.00","2.00","1.00","1.00","1.00","25.00"};
		String sectorDWeight[] = new String[] {"25.00","25.00","25.00","25.00","15.00","5.00","5.00","20.00", "4.00",
				"1.00","25.00","1.50","2.50","1.50","2.75","1.25","1.50","2.00","1.00","1.00","5.00","5.00","10.00","5.00",
				"5.00","2.00","1.00","1.00","1.00","25.00"};
		String sectorNames[] = new String[]  {"Stocks","Bonds","Funds","Cash","US","EU","Asia","US","EU","Asia","US",
				 "Aerospace & Defense","Auto & Truck Manufacturers","Beverages (Nonalcoholic)","Biotechnology & Drugs",
				 "Communications Equipment", "Computer Services", "Consumer Financial Services", "Regional Banks", 
				 "Retail" ,"Retail", "Regional Banks","Consumer Financial Services", "Regional Banks","Retail", "Aerospace & Defense",
				 "Auto & Truck Manufacturers", "Beverages (Nonalcoholic)", "Regional Banks", "Exchange Traded Funds"};
		String level[] = new String[] {"1","1","1","1","2","2","2","2","2","2","2","3","3","3","3","3","3","3","3","3","3",
				 "3","3","3","3","3","3","3","3","3" };
		String parentid[] = new String[] { "0","0","0","0","1","1","1","2","2","2","3","5","5","5","5","5","5","5","5","5",
				 "6","7","8","8","8","9","9","9","10","11"};
		String id[] = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19",
				 "20","21","22","23","24","25","26","27","28","29","30" };
		String isCustomized = "false";
		if (portfolioId.equalsIgnoreCase("100777-4")) {
			 sectorCWeight = new String[] {"40.00","30.00","20.00","10.00","20.00","10.00","10.00","15.00", "12.00", "3.00",
						"20.00","2.00","2.00","2.50","2.50","2.00","2.00","3.00","2.00","2.00","10.00","10.00","5.00","5.00"
						,"5.00","4.00" ,"4.00" ,"4.00","3.00" ,"20.00"};
			 sectorDWeight = new String[] {"40.00","30.00","20.00","10.00","20.00","10.00","10.00","15.00", "12.00", "3.00",
						"20.00","2.00","2.00","2.50","2.50","2.00","2.00","3.00","2.00","2.00","10.00","10.00","5.00","5.00"
						,"5.00","4.00" ,"4.00" ,"4.00","3.00" ,"20.00"};
			 sectorNames = new String[] {"Stocks","Bonds","Funds","Cash","US","EU","Asia","US","EU","Asia","US",
					 "Aerospace & Defense","Auto & Truck Manufacturers","Beverages (Nonalcoholic)","Biotechnology & Drugs",
					 "Communications Equipment", "Computer Services", "Consumer Financial Services", "Regional Banks", 
					 "Retail" ,"Retail", "Regional Banks","Consumer Financial Services", "Regional Banks","Retail", "Aerospace & Defense",
					 "Auto & Truck Manufacturers", "Beverages (Nonalcoholic)", "Regional Banks", "Exchange Traded Funds"};
			 level = new String[] {"1","1","1","1","2","2","2","2","2","2","2","3","3","3","3","3","3","3","3","3","3",
					 "3","3","3","3","3","3","3","3","3" };
			 parentid = new String[] { "0","0","0","0","1","1","1","2","2","2","3","5","5","5","5","5","5","5","5","5",
					 "6","7","8","8","8","9","9","9","10","11"};
			 id = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19",
					 "20","21","22","23","24","25","26","27","28","29","30" };
			 isCustomized ="false";
		}
		else if (portfolioId.equalsIgnoreCase("100777-5")) {	
		}
		JSONArray al1 = new JSONArray();
		for (int i = 0; i < sectorNames.length; i++) {
			HashMap<String, String> assets_hm = new HashMap<String, String>();
			assets_hm.put("recommendedWeight", sectorCWeight[i]);
			assets_hm.put("targetWeight", sectorDWeight[i]);
			assets_hm.put("Name", sectorNames[i]);
			assets_hm.put("level", level[i]);
			assets_hm.put("parentId", parentid[i]);
			assets_hm.put("ID", id[i]);
			assets_hm.put("marketSegmentId", id[i]);
			assets_hm.put("isCustomized", isCustomized);
			al1.put(assets_hm);
		}
		return al1;
	}
}
