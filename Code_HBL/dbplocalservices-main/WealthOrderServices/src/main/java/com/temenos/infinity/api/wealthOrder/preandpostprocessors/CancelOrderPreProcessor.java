package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.HashMap;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
/**
 * (INFO) If status is set as a part of the request , the operation is exited 
 * 		  else operation is executed.
 * @author balaji.krishnan
 *
 */
public class CancelOrderPreProcessor implements DataPreProcessor2 {

	@Override
	public boolean execute(@SuppressWarnings("rawtypes") HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		try {
			/*String backendToken = TokenGenerator.generateAuthToken(T24CertificateConstants.BACKEND,
					ServerConfigurations.DBP_HOST_URL.getValue(),
					WealthUtils.getUserAttributeFromIdentity(request, "UserName"),
					WealthUtils.getUserAttributeFromIdentity(request, "customer_id"), T24CertificateConstants.ROLEID,
					T24CertificateConstants.TOKEN_VALIDITY, true);
			request.addRequestParam_("Authorization", backendToken);*/
			request.addRequestParam_("channelName", "INFINITY");
			request.addRequestParam_(TemenosConstants.ORDERSVIEW_TYPE, "OPEN");
			request.addRequestParam_("dealStatus", "CANCELLED");
			return true;
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}

}
