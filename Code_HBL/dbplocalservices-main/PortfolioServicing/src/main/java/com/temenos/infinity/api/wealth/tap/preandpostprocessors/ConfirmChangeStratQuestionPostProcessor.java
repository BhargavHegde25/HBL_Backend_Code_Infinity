package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * Confirm change Strategy Question post processor - When message is info then
 * consider it as success.
 * 
 * @author r.lakshminarayanan
 *
 */
public class ConfirmChangeStratQuestionPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {

			String statusMessage = TemenosConstants.SUCCESS;
			Record headerRec = result.getRecordById("header");

			if (headerRec != null) {
				Dataset messagesRec = headerRec.getDatasetById("messages");

				if (messagesRec != null && messagesRec.getRecord(0) != null) {
					String message = messagesRec.getRecord(0).getParamValueByName("level");
					if (message.equalsIgnoreCase("info")) {
						statusMessage = TemenosConstants.SUCCESS;
					} else {
						statusMessage = TemenosConstants.FAILURE;
					}
				}
			} else {
				statusMessage = TemenosConstants.FAILURE;
			}

			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, statusMessage);

		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
}
