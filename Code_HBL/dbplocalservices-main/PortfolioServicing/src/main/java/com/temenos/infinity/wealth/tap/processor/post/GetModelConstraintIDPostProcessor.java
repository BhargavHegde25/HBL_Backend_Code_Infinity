/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
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

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetModelConstraintIDPostProcessor TAP - Entered ").log();
		
		Map<String, Object> inputMap = new HashMap<>();
		Record messageRec = result.getRecordById("messages");
		String createMessage = messageRec.getParamValueByName("message");
		
		inputMap.put("portfolioCode", request.getParameter("portfolioCode").toString());
		String constraintId = ModelConstraintDBUtil.createModelConstraint(createMessage, "", request, result, inputMap);
		
		diagnostic.prepareDebug("==========> GetModelConstraintIDPostProcessor TAP - Created new model constraint ID in DB").log();
		request.addRequestParam_("modelConstraintId", constraintId);
		
		Result dbRes = new Result();
		dbRes.addOpstatusParam("0");
		dbRes.addHttpStatusCodeParam("200");
		dbRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetModelConstraintIDPostProcessor TAP - Exited ").log();
		return dbRes;
	}
}
