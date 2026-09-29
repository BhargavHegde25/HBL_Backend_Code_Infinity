package com.temenos.dbx.product.approvalservices.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalQueueResource;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;


public class ApprovalQueueUpdationPostProcessor implements ObjectServicePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	    
		@Override
		public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
				throws Exception {
		Log4j2Configurator.getInstance();
			// TODO Auto-generated method stub
			try {
				//Initializing of ApprovalQueueResource through Abstract factory method
				ApprovalQueueResource approvalQueueResource = DBPAPIAbstractFactoryImpl.getResource(ApprovalQueueResource.class);
				
				approvalQueueResource.autoRejectPendingTransactionsInApprovalQueue(fabricRequestManager, fabricResponseManager);
			}
			catch(Exception e) {
				alert.prepareError("iFailed to update the approval queue after users permission change: ").log();
			}
			
		}
}
