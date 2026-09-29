package com.temenos.infinity.wealth.tap.processor.post;
import java.util.List;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.CustomerUtils;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author sarah
 *
 */
public class GetCustomersTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		    Dataset bodyDataset = result.getDatasetById("body");
		    JSONObject resJSON = new JSONObject();
			if(bodyDataset!=null)
			{
				JSONArray bodyArr = ResultToJSON.convertDataset(bodyDataset);
				resJSON = CustomerUtils.getCustomerDetailsFromTAP(bodyArr);
			}
			Result finalresult = Utilities.constructResultFromJSONObject(resJSON);
			finalresult.addOpstatusParam("0");
			finalresult.addHttpStatusCodeParam("200");
			finalresult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetCustomersT24PostProcessor T24 - Executed ").log();
			return finalresult;
	}

}


