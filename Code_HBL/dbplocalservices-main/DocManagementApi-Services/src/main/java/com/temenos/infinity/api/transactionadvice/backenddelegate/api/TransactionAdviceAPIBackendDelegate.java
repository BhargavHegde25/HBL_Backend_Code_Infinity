package com.temenos.infinity.api.transactionadvice.backenddelegate.api;

import java.util.ArrayList;
import java.util.HashMap;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import org.json.JSONObject;

public interface TransactionAdviceAPIBackendDelegate extends BackendDelegate {

	byte[] download(String documentId, String revision, String authToken);

	ArrayList<JSONObject> search(DataControllerRequest request,HashMap<String, Object> paramMap,String authToken) throws ApplicationException;

}
