package com.infinity.dbx.temenos.bulkpaymentservices.backenddelegate.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl.BulkPaymentFileBackendDelegateImpl;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;

public class BulkPaymentFileBackendDelegateImplExtn extends BulkPaymentFileBackendDelegateImpl {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String UPLOAD_FILE_ERROR_MESSAGE = "errmsg_uploadFile";
    private static final String STORE_FILE_ERROR_MESSAGE = "errmsg_storeBulkFile";
    private static final String ERROR_MESSAGE = "errmsg";

	@Override
	public BulkPaymentFileDTO uploadBulkPaymentFile(BulkPaymentFileDTO bulkPaymentFileDTO, DataControllerRequest dcr) {

		String serviceName = TemenosConstants.SERVICE_T24OS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_UPLOAD_BULK_FILE;
		Map<String, Object> requestParameters;
		BulkPaymentFileDTO bulkPaymentDTO = new BulkPaymentFileDTO();

		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkPaymentFileDTO).toString(), String.class,
					Object.class);
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params", e).log();
			return null;
		}

		String uploadResponse = null;
		try {
			uploadResponse = DBPServiceExecutorBuilder.
					builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			
			JSONObject response = new JSONObject(uploadResponse);

			if (response.has(UPLOAD_FILE_ERROR_MESSAGE) && StringUtils.isNotEmpty(response.getString(UPLOAD_FILE_ERROR_MESSAGE))) {	
				response.remove(ERROR_MESSAGE);
				response.put(ERROR_MESSAGE, response.getString(UPLOAD_FILE_ERROR_MESSAGE));
			}
			if (response.has(STORE_FILE_ERROR_MESSAGE) && StringUtils.isNotEmpty(response.getString(STORE_FILE_ERROR_MESSAGE))) {
				response.remove(ERROR_MESSAGE);
				response.put(ERROR_MESSAGE, response.getString(STORE_FILE_ERROR_MESSAGE));
			}
			bulkPaymentDTO = JSONUtils.parse(response.toString(), BulkPaymentFileDTO.class);

		} catch (JSONException e) {
			alert.prepareError("Unable to Upload Bulk Payment File", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Unable to store the file at Infinity", e).log();
			return null;
		}

		return bulkPaymentDTO;
	}
	
	@Override
	public List<BulkPaymentFileDTO> fetchBulkPaymentUploadedFiles(String fromDate, String toDate, DataControllerRequest dcr) {
		
		String serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		String operationName = TemenosConstants.OP_FETCH_UPLOADED_BULK_FILES;
		String response;
		List<BulkPaymentFileDTO> files;
		String lastUpdateDate = null;
		Map<String, Object> headers = dcr.getHeaderMap();
		
		if(StringUtils.isNotEmpty(fromDate) && StringUtils.isNotEmpty(toDate)) 
		{	
			lastUpdateDate = String.join(" ", fromDate, toDate);
			headers.put("lastUpdateDate", lastUpdateDate);
		}
		else
		{
			headers.remove(lastUpdateDate);
		}	
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(null).
					withRequestHeaders(headers).
					withDataControllerRequest(dcr).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = TemenosUtils.getFirstOccuringArray(responseObj);
            files = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentFileDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Error in Downloading Bulk Payment uploaded Files", e).log();			
			return null;
		} 
		catch (Exception e) {
			alert.prepareError("Error occured in fetchBulkPaymentUploadedFiles", e).log();			
			return null;
		}
		
		return files;
	}

}
