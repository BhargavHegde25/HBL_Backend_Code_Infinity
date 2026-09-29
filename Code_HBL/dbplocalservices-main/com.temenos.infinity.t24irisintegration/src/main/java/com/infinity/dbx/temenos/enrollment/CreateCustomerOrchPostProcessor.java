/**
 * 
 */
package com.infinity.dbx.temenos.enrollment;

import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.utils.ConvertJsonToResult;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public class CreateCustomerOrchPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	/* (non-Javadoc)
	 * @see com.konylabs.middleware.common.DataPostProcessor2#execute(com.konylabs.middleware.dataobject.Result, com.konylabs.middleware.controller.DataControllerRequest, com.konylabs.middleware.controller.DataControllerResponse)
	 */
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		// TODO Auto-generated method stub
		String id = result.getParamByName(Constants.PARAM_ID) != null
				? result.getParamByName(Constants.PARAM_ID).getValue()
				: null;
		if (StringUtils.isNotBlank(id)) {
			enrollCustomerInBackendIdentifiers(id, request);
		}
		return result;
	}
	
	public void enrollCustomerInBackendIdentifiers(String cust_id, DataControllerRequest dcRequest) {
		try {
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			String customerResultfromSession = (String)temenosUtils.retreiveFromSession(EnrollmentConstants.CONSTANT_CUSTOMER, dcRequest);
			Result enrollResult = ConvertJsonToResult.convert(customerResultfromSession);
			String backend_cust_id = "";
			if (enrollResult != null) {
				Dataset bodyDataset = enrollResult.getDatasetById(EnrollmentConstants.PARAM_BODY_DS);
				Record customerRecord = bodyDataset.getRecord(0);
				backend_cust_id = customerRecord.getParamByName(EnrollmentConstants.PARAM_CUSTOMER_ID) != null
						? customerRecord.getParamByName(EnrollmentConstants.PARAM_CUSTOMER_ID).getValue()
						: "";
			}
			HashMap<String, Object> inputmap = new HashMap<>();
			dcRequest.addRequestParam_(Constants.PARAM_ID, UUID.randomUUID().toString());
			dcRequest.addRequestParam_(EnrollmentConstants.PARAM_CUSTOMER_ID_PASCAL_CASE, cust_id);
			dcRequest.addRequestParam_(EnrollmentConstants.PARAM_BACKEND_ID, backend_cust_id);
			dcRequest.addRequestParam_(EnrollmentConstants.PARAM_BACKEND_TYPE, EnrollmentConstants.CONSTANT_T24);
			dcRequest.addRequestParam_(EnrollmentConstants.PARAM_SEQUENCE_NUMBER, Constants.YES);
			dcRequest.addRequestParam_(EnrollmentConstants.PARAM_IDENTIFIER_NAME, EnrollmentConstants.PARAM_CUSTOMER_ID);
			
			Result response = (Result) CommonUtils.callInternalService(EnrollmentConstants.SERVICE_DBP_RB_LOCAL_SERVICE_DB,
					EnrollmentConstants.OPERATION_DBXDB_BACKENDIDENTIFIER_CREATE, inputmap, null, dcRequest, 1, true);
			alert.prepareError(ResultToJSON.convert(response)).log();
		} catch (Exception e) {
			
			alert.prepareError(e.toString()).log();
			
		}
	}
}
