package com.temenos.infinity.api.arrangements.backenddelegate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.arrangements.exception.DMSException;

public interface DocumentStorageBackendDelegate extends BackendDelegate {

	public Map<String, Object> downloadDocument(Map<String, Object> documentDetails, DataControllerRequest request) throws DMSException, Exception;
	
	//public Map<String, Object> deleteDocument(Map<String, Object> documentDetails, DataControllerRequest request) throws DMSException, Exception;
	
	public JSONArray searchDocument(Map<String, Object> documentDetails, DataControllerRequest request) throws DMSException, Exception;
	
}
