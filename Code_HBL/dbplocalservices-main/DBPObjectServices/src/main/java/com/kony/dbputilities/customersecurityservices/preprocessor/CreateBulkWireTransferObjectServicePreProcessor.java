package com.kony.dbputilities.customersecurityservices.preprocessor;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.mfa.preprocessors.BulkWireTransactionsMFAPreProcessor;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CreateBulkWireTransferObjectServicePreProcessor extends CreateTransferObjectServicePreProcessor{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
			throws Exception {
		boolean status = true;
		try {
			// Triggered Process Block
			Log4j2Configurator.getInstance();
			status = new BulkWireTransactionsMFAPreProcessor().process(requestManager, responseManager);
			alert.prepareError(Boolean.toString(status)).log();
			execute(requestManager, responseManager, null);
		} catch (Exception e) {
			alert.prepareError("exception occured in MFA Preprocessor", e).log();

		}
		return status;
	}
}
