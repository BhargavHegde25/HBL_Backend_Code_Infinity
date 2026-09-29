/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.util.ModelConstraintDBUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetModelConstraintIDPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Map<String, Object> inputMap = new HashMap<>();
		Record messageRec = result.getRecordById("messages");
		String createMessage = messageRec.getParamValueByName("message");
		if(request.getParameter("isCustomized").toString().equalsIgnoreCase("true")) {
			inputMap.put("portfolioCode",request.getParameter("portfolioCode").toString());
			String constraintId = ModelConstraintDBUtil.getModelConstraint(inputMap, request);
			constraintId = ModelConstraintDBUtil.updateModelConstraint(constraintId,request,result,inputMap);
			request.addRequestParam_("modelConstraintId", constraintId);
		}
		else if (messageRec.hasParamByName("level") && messageRec.getParamValueByName("level").equalsIgnoreCase("ERROR")) {
			String filter = null;
			JSONObject resultJson = new JSONObject();
			filter = "portfolioCode" + DBPUtilitiesConstants.EQUAL + request.getParameter("portfolioCode").toString();
			Map<String, String> input = new HashMap<>();
			input.put(DBPUtilitiesConstants.FILTER, filter);
			Result getResult = HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(request), URLConstants.INF_WLTH_MODEL_CONSTRAINT_GET);
			if (HelperMethods.hasRecords(getResult)) {
				List<Dataset> dataset = getResult.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
				String constraintId = resultJson.get("constraintId").toString();
				if (constraintId != null && constraintId.length() > 0) {
					inputMap.put("modelConstraintId", constraintId);
				}
			}
		} else {
			inputMap.put("portfolioCode",request.getParameter("portfolioCode").toString());
			String constraintId = ModelConstraintDBUtil.getModelConstraint(inputMap, request);
			if (constraintId != null) {
				constraintId= ModelConstraintDBUtil.updateModelConstraint(constraintId,request,result,inputMap);
				request.addRequestParam_("modelConstraintId", constraintId);
			} else {
				constraintId = ModelConstraintDBUtil.createModelConstraint(createMessage,constraintId, request, result, inputMap);
				request.addRequestParam_("modelConstraintId", constraintId);
			}
		}
		Result dbRes = new Result();
		dbRes.addOpstatusParam("0");
		dbRes.addHttpStatusCodeParam("200");
		dbRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return dbRes;
	}
}
