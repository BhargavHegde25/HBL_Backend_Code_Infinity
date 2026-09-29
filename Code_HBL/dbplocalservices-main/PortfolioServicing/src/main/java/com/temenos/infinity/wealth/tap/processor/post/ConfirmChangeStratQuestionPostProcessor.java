package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("==========> ConfirmChangeStratQuestionPostProcessor TAP - Entered ").log();
			String statusMessage = TemenosConstants.SUCCESS;
			Record headerRec = result.getRecordById("header");

			if (headerRec != null) {
				Dataset messagesRec = headerRec.getDatasetById("messages");

				diagnostic.prepareDebug("==========> ConfirmChangeStratQuestionPostProcessor TAP - Message header available").log();
				if (messagesRec != null && messagesRec.getRecord(0) != null) {
					diagnostic.prepareDebug("==========> ConfirmChangeStratQuestionPostProcessor TAP - Begin:Message header manipulation").log();
					String message = messagesRec.getRecord(0).getParamValueByName("level");
					if (message.equalsIgnoreCase("info")) {
						statusMessage = TemenosConstants.SUCCESS;
					} else {
						statusMessage = TemenosConstants.FAILURE;
					}
					diagnostic.prepareDebug("==========> ConfirmChangeStratQuestionPostProcessor TAP - End:Message header manipulation").log();
				}
			} else {

				diagnostic.prepareDebug("==========> ConfirmChangeStratQuestionPostProcessor TAP - Message header unavailable").log();
				statusMessage = TemenosConstants.FAILURE;
			}

			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, statusMessage);

		} catch (Exception e) {
			alert.prepareError("==========> ConfirmChangeStratQuestionPostProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}
}
