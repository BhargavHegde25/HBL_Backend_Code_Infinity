package com.hbl.productservicesExtn.api;

import java.io.IOException;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalQueueResource;

public interface ApprovalQueueResourceExtn extends ApprovalQueueResource{
	public Result validateForApprovals(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException;
}
