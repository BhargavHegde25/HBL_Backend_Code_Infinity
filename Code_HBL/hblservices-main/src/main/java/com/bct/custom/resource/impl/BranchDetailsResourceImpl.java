package com.bct.custom.resource.impl;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.BranchDetailsBusinessDelegate;
import com.bct.custom.dto.BranchDetails;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.resource.api.BranchDetailsResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.KMSBusinessDelegate;
import com.kony.dbp.exception.ApplicationException;

public class BranchDetailsResourceImpl implements BranchDetailsResource{
	private static final Logger LOG = LogManager.getLogger(BranchDetailsResourceImpl.class);
	
	@Override
	public Result branchDetailsCRUDOperations(String methodId, Object[] inputArray, DataControllerRequest dcRequest)
			throws ApplicationException {
		Result result = new Result();
		LOG.debug("BCT::BranchDetailsResourceImpl:createBranchDetails:inputArray:"+inputArray.toString());
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		BranchDetailsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(BranchDetailsBusinessDelegate.class);
		Map<String, Object> inputsArray = new HashMap<String, Object>();
		LOG.debug("BCT::BranchDetailsResourceImpl:methodId:"+methodId);
		try {
			if(methodId.equals("getBranchManagerEmail")) {
			String input = inputParams.get("id");
			LOG.debug("BCT::BranchDetailsResourceImpl::getBranchMngrEmail: " + inputParams.toString());
			JSONArray response = businessDelegate.getBranchDetails(input, inputsArray, dcRequest);
			LOG.debug("BCT::BranchDetailsResourceImpl:getBranchMngrEmail:response: " + response.toString());
			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray(response);
			ds.setId("emails");
			result.addDataset(ds);
			LOG.debug("Result branchdetails:" + ds);
			result.addParam(new Param("opstatus", "0"));
			result.addParam(new Param("httpStatusCode", "200"));
			result.addParam(new Param("success", "true"));
			String firstName = "Janakiram";
			String lastName="Damerla";
			String emailTemplate = "HBL_HIMAL_REMIT_FD_REQUEST";
			//String email="janakiramulu.d@bahwancybertek.com";

			JSONArray array = new JSONArray();
			for(int i=0;i<result.getDatasetById("emails").getAllRecords().size(); i++) {
				String emails= result.getDatasetById("emails").getRecord(i).getParamValueByName("branch_mge_email").toString();
				LOG.debug("Response emails:"+emails);
				JSONObject JsonObject = new JSONObject();
				if(StringUtils.isNotEmpty(emails))
				JsonObject.put("email", emails);
				array.put(JsonObject);
				
			}
			LOG.debug("Response emails Array:"+array);
			Map<String, Object> fdRequest = new HashMap<String, Object>();
			fdRequest.put("firstName", firstName);
			fdRequest.put("lastName", lastName);
			fdRequest.put("templateName", emailTemplate);
			fdRequest.put("refNumber", String.valueOf(generateRandomID()));
			fdRequest.put("accountNumber", String.valueOf(generateRandomID()));
			fdRequest.put("email", array);
			sendGeneratedUsernameToEmail(fdRequest, dcRequest.getHeaderMap());
		} 	else if(methodId.equals("createBranchDetails")) {
			// Map<String, Object> inputCreateParams = (HashMap<String, Object>) inputArray[1];
			LOG.debug("BCT::BranchDetailsResourceImpl:createBranchDetails:inputArray:"+inputArray.toString());
			 Map<String, String> inputCreateParams = HelperMethods.getInputParamMap(inputArray);
				String request = inputCreateParams.get("branchDetails").toString();
				LOG.debug("BCT::BranchDetailsResourceImpl:createBranchDetails:request:"+request);
				JSONArray requestArray = new JSONArray(request);
			
			 ArrayList<BranchDetails> branchDetailsArray = validateCreateBranchDetails(requestArray, dcRequest);
			if(branchDetailsArray.size()>0) {
			 result = businessDelegate.createBranchDetails(branchDetailsArray, inputParams, dcRequest);
			}
			LOG.debug("BCT::BranchDetailsResourceImpl::validateCreateBranchDetails: response:" + result.toString());
			if (result.getParamByName("dbpErrCode") == null) {
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
				result.setParam(new Param("success", "true"));
			}
		}
		else if(methodId.equals("getOtherBankDetails")) {
				String input = inputParams.get("id");
				LOG.debug("BCT::BranchDetailsResourceImpl::getOtherBankDetails: " + inputParams.toString());
				JSONArray response = businessDelegate.getBranchDetails(input, inputsArray, dcRequest);
				LOG.debug("BCT::BranchDetailsResourceImpl:getOtherBankDetails:response: " + response.toString());
				Dataset ds = new Dataset();
				if(response!=null) {
					JSONObject bankDetails = new JSONObject();
					bankDetails.put("bankDetails", response);
	                result = JSONToResult.convert(bankDetails.toString());
	                result.setParam(new Param("opstatus", "0"));
	                result.setParam(new Param("httpStatusCode", "200"));
	                result.addParam(new Param("success", "true"));
				}
			}
			
		} catch (IOException e) {
			LOG.error("BCT::BranchDetailsResourceImpl::exception: " + e.toString());
			result.addParam(new Param("dbpErrCode","1001"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}catch (Exception e) {
			LOG.error("Exception occured at: BCT::BranchDetailsResourceImpl::exception: " + e.toString());
			result.addParam(new Param("dbpErrCode","1002"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}
	 private int generateRandomID() {
	        SecureRandom rand = new SecureRandom();
	        return (int) (100000 + (rand.nextFloat() * 900000));
	    }
	private void sendGeneratedUsernameToEmail( Map<String, Object> info, Map<String, Object> headersMap) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> input = new HashMap<>();
			input.put("FirstName", info.get("firstName"));
			input.put("LastName", info.get("lastName"));
			input.put("EmailType", info.get("templateName"));
			input.put("Email",info.get("email"));
			JSONObject addContext = new JSONObject();
			addContext.put("refNumber",info.get("refNumber"));
			addContext.put("accountNumber",info.get("accountNumber"));
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			LOG.debug("sendKMSEmail:input:"+input.toString());
			kmsBusinessDelegate.sendKMSEmail(input, headersMap);
		} catch (Exception e) {
			LOG.error("Exception occured while sending email " + e.getMessage());
			//throw new ApplicationException(ErrorCodeEnum.ERR_10796);
		}
	}
	
	public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = new Record();
			Record resRecord = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			if(resRecord.getParamValueByName("branch_mge_email")!=null) {
			record.addParam("branch_mge_email", resRecord.getParamValueByName("branch_mge_email"));
			}
			dataset.addRecord(record);
		}
		return dataset;
	}
	public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}

		return response;
	}
	
		public ArrayList<BranchDetails> validateCreateBranchDetails(JSONArray array, DataControllerRequest dcRequest)
				throws IOException, ApplicationException {
			String request="";
			BranchDetails dto;
			ArrayList<BranchDetails> branchDetailsList = new ArrayList<BranchDetails>();
			for(int i=0;i<array.length();i++) {
				request=array.getJSONObject(i).toString();
				dto = JSONUtils.parse(request, BranchDetails.class);
				LOG.debug("BCT::BranchDetailsResourceImpl::validateCreateBranchDetails: MerchantDTO:" + dto.toString());
				if(validateCreateBranchDetails(dto, dcRequest)) {
					branchDetailsList.add(dto);
				}else {
					throw new ApplicationException(ErrorCodeEnum.ERR_10755);//ApplicationException(ErrorCodeEnum.ERR_10755);
					}
			}
			if(branchDetailsList.size()==0) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755);
			}
			
			return branchDetailsList;
		}
		public boolean validateCreateBranchDetails(BranchDetails dto, DataControllerRequest dcRequest) {
			boolean isValid=true;
			if (dto.getId()==null || StringUtils.isBlank(dto.getBank_cd())
					|| StringUtils.isBlank(dto.getBank_name())
					|| StringUtils.isBlank(dto.getBranch_mge_email()) ) {
				isValid= false;
			}
			LOG.debug("BCT::BranchDetailsResourceImpl::validateCreateBranchDetails: isValid:" + isValid);
			return isValid;
		}

}
