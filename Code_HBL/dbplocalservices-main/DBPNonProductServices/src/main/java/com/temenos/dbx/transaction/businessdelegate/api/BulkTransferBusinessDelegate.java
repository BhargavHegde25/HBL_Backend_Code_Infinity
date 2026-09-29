package com.temenos.dbx.transaction.businessdelegate.api;
import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface BulkTransferBusinessDelegate extends BusinessDelegate {
	
	public JSONArray createBulkTransfer(Map<String, Object> bulkTransferparsedRequest, DataControllerRequest request);
}
