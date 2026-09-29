package com.temenos.dbx.transaction.businessdelegate.impl;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.kony.constants.OperationName;
import com.kony.constants.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.transaction.businessdelegate.api.BulkTransferBusinessDelegate;

public class BulkTransferBusinessDelegateImpl implements BulkTransferBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	
	@Override
	public JSONArray createBulkTransfer(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest) {
		
		JSONArray resArray = new JSONArray();
		String serviceName = ServiceId.BULK_TRANSFER_SERVICE;
		String operationName = OperationName.BULK_TRANSFER_OPERATION;

		String bulkTransferResponse = null;

		try {
			diagnostic.prepareDebug("In Business Delegate Method").log();
			bulkTransferResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					requestParameters, null, dataControllerRequest);
			JSONObject bulkTransferJSON = new JSONObject(bulkTransferResponse);
			resArray = bulkTransferJSON.getJSONArray("LoopDataset");
			return resArray;
		} catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while creating the bulktransfer: ", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured while creating the bulktransfer: ", exp).log();
			return null;
		}
	}

}
