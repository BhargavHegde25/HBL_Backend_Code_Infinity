/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class RevertStrategyPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> RevertStrategyPostProcessor TAP - Entered ").log();
		Result revertStrategy = new Result();
		if(result.getDatasetById("LoopDataset")==null) {
			diagnostic.prepareDebug("==========> RevertStrategyPostProcessor TAP - No data ").log();
			revertStrategy.addOpstatusParam("0");
			revertStrategy.addHttpStatusCodeParam("200");
			revertStrategy.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			 diagnostic.prepareDebug("==========> RevertStrategyPostProcessor TAP - Exited ").log();
			return revertStrategy;
		}
		else {
		JSONArray loopArr = ResultToJSON.convertDataset(result.getDatasetById("LoopDataset"));
		if(loopArr.getJSONObject(loopArr.length()-1).has("messages")) {
			revertStrategy.addParam("message","Delete succeeded");
			diagnostic.prepareDebug("==========> RevertStrategyPostProcessor TAP - Deleted ").log();
		}
		revertStrategy.addOpstatusParam("0");
		revertStrategy.addHttpStatusCodeParam("200");
		revertStrategy.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		 diagnostic.prepareDebug("==========> RevertStrategyPostProcessor TAP - Exited ").log();
		return revertStrategy;
		}
	}
	}

