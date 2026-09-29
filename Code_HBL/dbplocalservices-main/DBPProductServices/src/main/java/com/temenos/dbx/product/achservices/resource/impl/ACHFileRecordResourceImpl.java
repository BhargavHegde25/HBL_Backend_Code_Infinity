package com.temenos.dbx.product.achservices.resource.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileRecordBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHFileRecordResource;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.commons.dto.FilterDTO;

public class ACHFileRecordResourceImpl implements ACHFileRecordResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	ACHFileRecordBusinessDelegate achFileRecordBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileRecordBusinessDelegate.class);
	ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileBusinessDelegate.class);
	
	@Override
	public Result fetchACHFileRecords(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dataControllerResponse) {
		
		Result result = new Result();
        try {
            @SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
            ACHFileRecordDTO requestDTO = JSONUtils.parse(new JSONObject(requestMap).toString(), ACHFileRecordDTO.class);
            
            String achFileId = requestDTO.getAchFileId();
            if(achFileId == null || achFileId.isEmpty()) {
                return ErrorCodeEnum.ERR_12041.setErrorCode(new Result());
            }

			try{
				Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
				String userId = CustomerSession.getCustomerId(customer);
				FilterDTO filters = new FilterDTO();
				// Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
				// filterParamsMap.get("achfile_id").toString();
				String achfile_id = achFileId;

				List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, achfile_id, filters, dcRequest);

				if(files == null) {
					diagnostic.prepareDebug("Security Issue : UserId " + userId + " achfile_id : " + achfile_id).log();
					return ErrorCodeEnum.ERR_10308.setErrorCode(new Result());
				}
				if(files.size()!=0) {
					diagnostic.prepareDebug("Security Issue : UserId " + userId + " achfile_id : " + achfile_id).log();
					return ErrorCodeEnum.ERR_10308.setErrorCode(new Result());
				}
			}catch (Exception e){
				alert.prepareError("Error occurred while fetching ach file by id at fetchACHFileRecords : \n" + String.valueOf(e)).log();
				return null;
			}

			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_FILE_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			ACHFileDTO achDTO = achFileBusinessDelegate.fetchTransactionById(achFileId, dcRequest);
			if(achDTO == null) {
				alert.prepareError("Record Doesn't Exist").log();
	            return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
			}
			
			achFileId = achDTO.getAchFile_id();
			
			List<ACHFileRecordDTO> responseDTO = achFileRecordBusinessDelegate.fetchACHFileRecords(achFileId);
            JSONArray records = new JSONArray(responseDTO);
            JSONObject resultObject = new JSONObject();
            resultObject.put(ACHConstants.ACHFILERECORDS,records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while fetching ACH File Records",exp).log();
            return ErrorCodeEnum.ERR_12048.setErrorCode(new Result());
        }
		
		return result;
	}
}
