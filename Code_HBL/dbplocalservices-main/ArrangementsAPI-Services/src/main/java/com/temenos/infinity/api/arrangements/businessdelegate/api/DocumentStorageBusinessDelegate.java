package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
//import com.temenos.infinity.api.arrangements.dto.DocumentStorageEvidence;
import com.temenos.infinity.api.arrangements.dto.DocumentStorage;
import com.temenos.infinity.api.arrangements.exception.DMSException;

public interface DocumentStorageBusinessDelegate extends BusinessDelegate {

	
	
	public DocumentStorage downloadDocument(DocumentStorage documentStorage, DataControllerRequest request) throws DMSException, Exception;
	
	
	
	public List<DocumentStorage> searchDocument(DocumentStorage documentStorage, DataControllerRequest request) throws DMSException, Exception;
	
}
