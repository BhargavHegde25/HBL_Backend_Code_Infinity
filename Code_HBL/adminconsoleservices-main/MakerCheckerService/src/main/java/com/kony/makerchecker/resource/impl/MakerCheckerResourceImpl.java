package com.kony.makerchecker.resource.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.makerchecker.businessdelegate.api.MakerCheckerBusinessDelegate;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class MakerCheckerResourceImpl implements MakerCheckerResource {

	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	@Override
	public Result isMakerCheckerEnabled(String method, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();

		String expApiOperationName = request.getParameter(UtilConstants.EXPAPIOPERATIONNAME);
		String legalEntityId = request.getParameter(UtilConstants.LEGAL_ENTITY_ID);

		if (StringUtils.isBlank(expApiOperationName)) {
			diagnostic.prepareDebug("expApiOperationName is a manadatory field!").log();
			return ErrorCodesEnum.ERR_10001.setErrorCode(result);
		}
//		if (StringUtils.isBlank(legalEntityId)) {
//			diagnostic.prepareDebug("legal entity cannot be null!").log();
//			return ErrorCodesEnum.ERR_10004.setErrorCode(result);
//		}
		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);

		try {
			Map<String,String> obj = new HashMap<>();
			obj = businessDelegate.isMakerCheckerEnabled(request, expApiOperationName, legalEntityId);
			if(obj!=null&&obj.size()>0) {
				String isApprovalRequired = obj.get(UtilConstants.IS_APPROVAL_REQ);
				result.addParam(UtilConstants.IS_APPROVAL_REQ,isApprovalRequired);
				
				Result resultStorePayloadForRequest = new Result();
				if (isApprovalRequired.equalsIgnoreCase(UtilConstants.TRUE)||isApprovalRequired.equals("1")) {
					resultStorePayloadForRequest = storePayloadForRequest(method, inputArray, request,
							dataControllerResponse);
					result.addParam(UtilConstants.APPROVAL_REQ_ID, resultStorePayloadForRequest.getParamValueByName(UtilConstants.REQUEST_ID));
				}

			} else {
				return ErrorCodesEnum.ERR_10003.setErrorCode(result);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while fetching data!").log();
			result.addParam("errorMsg", "Error occured while fetching result from db");
			return ErrorCodesEnum.ERR_10002.setErrorCode(result);
		}
		return result;
	}

	@Override
	public Result storePayloadForRequest(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) {
		Result result = new Result();

		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		try {
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);

			String reqId = "";
			JSONObject jsonObj = businessDelegate.storePayloadForRequest(dataControllerRequest, inputParams);

			if (jsonObj != null) {
				reqId = jsonObj.getString(UtilConstants.REQUEST_ID);
				result.addParam(UtilConstants.REQUEST_ID, reqId);
			} else {
				return ErrorCodesEnum.ERR_10006.setErrorCode(new Result());
			}

		} catch (Exception e) {
			alert.prepareError("Exception while creating data!").log();
			result.addParam("errorMsg", "Error occured while fetching result from db");
		}

		return result;
	}

	public Result getDashboardCounts(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();

		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		try {
			Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

			JSONObject jsonObj = businessDelegate.getDashboardCounts(dataControllerRequest, inputParams);

			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while fetching dashboard counts for makerchecker!").log();
			return ErrorCodesEnum.ERR_10007.setErrorCode(result);
		}
	}
	
	@Override
	public Result getMakerCheckerPendingRequests(String method, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();
		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		if (StringUtils.isEmpty((String) inputParams.get("module"))
				|| StringUtils.isEmpty((String) inputParams.get("action"))
				|| StringUtils.isEmpty((String) inputParams.get("recordId"))
				|| StringUtils.isEmpty((String) inputParams.get("companyLegalUnit"))) {
			diagnostic.prepareDebug("module, action, recordId, companyLegalUnit Params are manadatory fields!").log();
			return ErrorCodesEnum.ERR_10015.setErrorCode(result);
		}
		try {
			JSONObject jsonObj = businessDelegate.getMakerCheckerPendingRequests(request, inputParams);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Exception while fetching data!").log();
			result.addParam("errorMsg", "Error occured while fetching result");
			return ErrorCodesEnum.ERR_10013.setErrorCode(result);
		}
	}
	
	@Override
	public Result approvalRequestViewDetails(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();

		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		
		if (StringUtils.isEmpty((String) inputParams.get("module"))
				|| StringUtils.isEmpty((String) inputParams.get("action"))
				|| StringUtils.isEmpty((String) inputParams.get("requestId"))) {
			diagnostic.prepareDebug("module, action, requestId Params are manadatory fields!").log();
			return ErrorCodesEnum.ERR_10017.setErrorCode(result);
		}
		
		try {
			JSONObject jsonObj = businessDelegate.approvalRequestViewDetails(dataControllerRequest, inputParams);

			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while fetching Approval Request details!").log();
			return ErrorCodesEnum.ERR_10016.setErrorCode(result);
		}
	}
	

	public Result approveRejectRequest(String method, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dataControllerResponse) {
		
		Result result = new Result();
		try {

			String requestId = dcRequest.getParameter(UtilConstants.REQUEST_ID);
			String action = dcRequest.getParameter(UtilConstants.ACTION);
			String comments = dcRequest.getParameter(UtilConstants.COMMENTS);
			
			HashMap<String,String> approvalRequestDetails = MakerCheckerUtils.getApprovalRequestDetails(dcRequest, requestId);
	
			String[] reqPermissions = { approvalRequestDetails.get(UtilConstants.PERMISSION_NAME) };
			dcRequest.addRequestParam_("addedlegalEntityIdValue", approvalRequestDetails.get(UtilConstants.COMPANY_LEGAL_UNIT));
			
			if(approvalRequestDetails.isEmpty()) {
				diagnostic.prepareDebug("Invalid RequestId").log();
				return ErrorCodesEnum.ERR_10027.setErrorCode(result);
			}
			
			if (!LoggedInUserHandler.hasAccessToLegalEntity(dcRequest, reqPermissions)) {		
				result.addParam(new Param("Status", "Approve/Reject Request failed", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(result);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return result;
			}
			
			if (StringUtils.isBlank(action)) {
				diagnostic.prepareDebug("Action is a mandatory param").log();
				return ErrorCodesEnum.ERR_10018.setErrorCode(result);
			}
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug("RequestId is a mandatory param").log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			if (!action.equalsIgnoreCase(UtilConstants.SID_APPROVED) && !action.equalsIgnoreCase(UtilConstants.SID_REJECTED)) {
				diagnostic.prepareDebug("Action can be either APPROVED or REJECTED").log();
				return ErrorCodesEnum.ERR_10021.setErrorCode(result);
			}

			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);

			JSONObject jsonObj = businessDelegate.approveRejectRequest(dcRequest, requestId, action, comments, approvalRequestDetails);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}

			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while approving/rejecting the approval request!").log();
			return ErrorCodesEnum.ERR_10020.setErrorCode(result);
		}

	}

	@Override
	public Result requestsHistory(String method, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();
		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		if ((inputParams.get("companyLegalUnit") == null
				|| StringUtils.isEmpty(inputParams.get("companyLegalUnit").toString()))
				|| (inputParams.get("userType") == null
						|| StringUtils.isEmpty(inputParams.get("userType").toString()))) {
			diagnostic.prepareDebug(ErrorCodesEnum.ERR_10035.getMessage()).log();
			return ErrorCodesEnum.ERR_10035.setErrorCode(result);
		}
		try {
			JSONObject jsonObj = businessDelegate.getRequestsHistory(request, inputParams);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Exception while fetching data!").log();
			result.addParam("errorMsg", "Error occured while fetching result");
			return ErrorCodesEnum.ERR_10036.setErrorCode(result);
		}
	}

	@Override
	public Result getMakerCheckerConfig(String method, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		String legalEntityId = request.getParameter(UtilConstants.LEGAL_ENTITY_ID);
		String languageCode = request.getParameter(UtilConstants.LANGUAGE_CODE);

		if (StringUtils.isBlank(legalEntityId)) {
			diagnostic.prepareDebug("legal entity cannot be null!").log();
			return ErrorCodesEnum.ERR_10004.setErrorCode(result);
		}
		if (StringUtils.isBlank(languageCode)) {
			languageCode = UtilConstants.DEFAULT_LANGUAGE_CODE;
		}
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("legalEntityId", legalEntityId);
		inputParams.put("languageCode", languageCode);

		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		try {
			JSONObject responseObj = businessDelegate.getMakerCheckerConfig(request, inputParams);
			if (responseObj != null && responseObj.has(UtilConstants.MAKER_CHECKER_CONFIG_VIEW)) {
				JSONObject makerCheckerConfigJson = new JSONObject();
				JSONArray makerCheckerConfigData = responseObj.optJSONArray(UtilConstants.MAKER_CHECKER_CONFIG_VIEW);
				makerCheckerConfigData=removeEnrolMakerCheckerConfig(makerCheckerConfigData);
				makerCheckerConfigJson.put("MakerCheckerConfigData",makerCheckerConfigData);
				result = JSONToResult.convert(makerCheckerConfigJson.toString());
			} else {
				result = JSONToResult.convert(responseObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Failed to fetch data from makercheckerconfig view.").log();
			return ErrorCodesEnum.ERR_10024.setErrorCode(result);
		}
	}
	@Override
	public Result updateMakerCheckerConfig(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		String makerCheckerConfig = request.getParameter("makerCheckerConfig");
		JSONArray configArray = new JSONArray(makerCheckerConfig);
		
		Iterator<Object> iterator = configArray.iterator();
        while (iterator.hasNext()) {
            JSONObject item = (JSONObject) iterator.next();
            String id = item.optString("id");
            String companyLegalUnit = item.optString("companyLegalUnit");
            if (StringUtils.isAnyBlank(id, companyLegalUnit)) {
            	result.addParam(new Param(UtilConstants.STATUS, UtilConstants.FAILURE, FabricConstants.STRING));
            	return ErrorCodesEnum.ERR_10041.setErrorCode(result);
            }
        }

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", configArray);

		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		try {
			JSONObject responseObj = businessDelegate.updateMakerCheckerConfig(request, inputParams);
			if (responseObj == null) {
				result.addParam(new Param(UtilConstants.STATUS, UtilConstants.FAILURE, FabricConstants.STRING));
				return ErrorCodesEnum.ERR_10040.setErrorCode(result);
			}

			if (responseObj != null && responseObj.has(UtilConstants.OPSTATUS)
					&& Integer.valueOf(responseObj.optString(UtilConstants.OPSTATUS)) == 0) {
				result.addParam(new Param(UtilConstants.STATUS, UtilConstants.SUCCESS, FabricConstants.STRING));
			} else {
				result = JSONToResult.convert(responseObj.toString());
				result.addParam(new Param(UtilConstants.STATUS, UtilConstants.FAILURE, FabricConstants.STRING));
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception occured while trying to get data from dbxdb_update_makercheckerconfig_proc.").log();
			result.addParam(new Param(UtilConstants.STATUS, UtilConstants.FAILURE, FabricConstants.STRING));
			return ErrorCodesEnum.ERR_10040.setErrorCode(result);
		}
	}

	@Override
	public Result parseApprovalRequestDetails(String method, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();
		List<String> excludedParamsList = null;
		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		String reqId = dcRequest.getParameter("requestId");
		if (StringUtils.isBlank(reqId)) {
			diagnostic.prepareDebug("requestId param is manadatory field!").log();
			return ErrorCodesEnum.ERR_10019.setErrorCode(result);
		}

		String excludedParams = dcRequest.getParameter("excludedParams");

		if (StringUtils.isNotBlank(excludedParams)) {
			excludedParamsList = Stream.of(excludedParams.trim().split(",")).collect(Collectors.toList());
		}
		
		try {
			String res = businessDelegate.getApprovalRequests(dcRequest, reqId, excludedParamsList);
			
			if (StringUtils.isNotBlank(res)) {
				result.addParam("requestDetails", res);
			}

			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while parsing Approval Request details!").log();
			return ErrorCodesEnum.ERR_10028.setErrorCode(result);
		}
	}

	@Override
	public Result getCheckerApprovalRequests(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();

		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		if (StringUtils.isEmpty((String) inputParams.get(UtilConstants.LEGAL_ENTITY_ID))) {
			diagnostic.prepareDebug("legalEntityId is manadatory !").log();
			return ErrorCodesEnum.ERR_10030.setErrorCode(result);
		}

		try {
			JSONObject jsonObj = businessDelegate.getCheckerApprovalRequests(dataControllerRequest, inputParams);

			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}

			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while fetching Approvar Pending Request details!").log();
			return ErrorCodesEnum.ERR_10033.setErrorCode(result);
		}
	}
	
	@Override
	public Result getAllMakerPendingRequests(String method, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dataControllerResponse) {
		Result result = new Result();
		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		if (StringUtils.isEmpty((String) inputParams.get("legalEntityId"))) {
			diagnostic.prepareDebug("legalEntityId Param is manadatory field!").log();
			return ErrorCodesEnum.ERR_10004.setErrorCode(result);
		}
		
		try {
			JSONObject jsonObj = businessDelegate.getAllMakerPendingRequests(request, inputParams);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Exception while fetching data!").log();
			result.addParam("errorMsg", "Error occured while fetching result");
			return ErrorCodesEnum.ERR_10013.setErrorCode(result);
		}
	}
	
	@Override
	public Result getMCModuleActionOperation(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) {
		Result result = new Result();

		MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		if (StringUtils.isEmpty((String) inputParams.get(UtilConstants.LANGUAGECODE))) {
			diagnostic.prepareDebug("languagecode is manadatory !").log();
			return ErrorCodesEnum.ERR_10037.setErrorCode(result);
		}

		try {
			JSONObject jsonObj = businessDelegate.getMCModuleActionOperation(dataControllerRequest, inputParams);

			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}

			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while fetching Module Request Type Names!").log();
			return ErrorCodesEnum.ERR_10038.setErrorCode(result);
		}
	}

	@Override
	public Result withdrawRequest(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			String action = request.getParameter(UtilConstants.ACTION);

			if (StringUtils.isBlank(action)) {
				diagnostic.prepareDebug("Action is a mandatory param").log();
				return ErrorCodesEnum.ERR_10018.setErrorCode(result);
			}
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug("RequestId is a mandatory param").log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			if (!action.equalsIgnoreCase(UtilConstants.SID_WITHDRAWN)) {
				diagnostic.prepareDebug(action + "-Action not allowed").log();
				return ErrorCodesEnum.ERR_10046.setErrorCode(result);
			}
			HashMap<String, String> approvalRequestDetails = MakerCheckerUtils.getApprovalRequestDetails(request,
					requestId);
			if (approvalRequestDetails.isEmpty()) {
				diagnostic.prepareDebug("Invalid RequestId").log();
				return ErrorCodesEnum.ERR_10027.setErrorCode(result);
			}
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("requestId", requestId);
			inputParams.put("action", action);
			inputParams.put("approvalRequestDetails", approvalRequestDetails);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.withdrawRequest(request, inputParams);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while withdrawing the request!").log();
			return ErrorCodesEnum.ERR_10045.setErrorCode(result);
		}
	}

	@Override
	public Result viewCustomerDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug(ErrorCodesEnum.ERR_10019.getMessage()).log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(UtilConstants.REQUEST_ID, requestId);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.viewCustomerDetails(request, response, inputParams);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo(ErrorCodesEnum.ERR_10048.getMessage()).log();
			return ErrorCodesEnum.ERR_10048.setErrorCode(result);
		}
	}

	public Result enrollCustomerViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		String requestId = request.getParameter("requestId");
		if (StringUtils.isBlank(requestId)) {
			diagnostic.prepareDebug("Request id is a manadatory field!").log();
			return ErrorCodesEnum.ERR_10019.setErrorCode(result);
		}
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", requestId);

		try {
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject response = businessDelegate.enrollCustomerViewDetails(request, inputParams);
			if (response != null) {
				result = JSONToResult.convert(response.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception while fetching enroll Customer View Details!").log();
			return ErrorCodesEnum.ERR_10049.setErrorCode(result);
		}
	}

	@Override
	public Result createContractViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		String requestId = request.getParameter("requestId");
		if (StringUtils.isBlank(requestId)) {
			diagnostic.prepareDebug("Request id is a manadatory field!").log();
			return ErrorCodesEnum.ERR_10019.setErrorCode(result);
		}
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", requestId);

		try {
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject response = businessDelegate.createContractViewDetails(request, inputParams);
			if (response != null) {
				result = JSONToResult.convert(response.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo("Error occurred while fetching data for create contract view details").log();
			return ErrorCodesEnum.ERR_10056.setErrorCode(result);
		}
	}
	
	@Override
	public Result editContractViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug(ErrorCodesEnum.ERR_10019.getMessage()).log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(UtilConstants.REQUEST_ID, requestId);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.viewEditContractDetails(request, response, requestId);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo(ErrorCodesEnum.ERR_10053.getMessage()).log();
			return ErrorCodesEnum.ERR_10053.setErrorCode(result);
		}
	}
	
	@Override
	public Result createSignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug(ErrorCodesEnum.ERR_10019.getMessage()).log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(UtilConstants.REQUEST_ID, requestId);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.createSignatoryGroupViewDetails(request, response, requestId);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo(ErrorCodesEnum.ERR_10053.getMessage()).log();
			return ErrorCodesEnum.ERR_10053.setErrorCode(result);
		}
	}
	
	@Override
	public Result deleteSignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug(ErrorCodesEnum.ERR_10019.getMessage()).log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(UtilConstants.REQUEST_ID, requestId);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.deleteSignatoryGroupViewDetails(request, response, requestId);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo(ErrorCodesEnum.ERR_10053.getMessage()).log();
			return ErrorCodesEnum.ERR_10053.setErrorCode(result);
		}
	}
	
	@Override
	public Result editSignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug(ErrorCodesEnum.ERR_10019.getMessage()).log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(UtilConstants.REQUEST_ID, requestId);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.editSignatoryGroupViewDetails(request, response, requestId);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo(ErrorCodesEnum.ERR_10053.getMessage()).log();
			return ErrorCodesEnum.ERR_10053.setErrorCode(result);
		}
	}
	
	@Override
	public Result createApprovalRuleBySignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter(UtilConstants.REQUEST_ID);
			
			if (StringUtils.isBlank(requestId)) {
				diagnostic.prepareDebug(ErrorCodesEnum.ERR_10019.getMessage()).log();
				return ErrorCodesEnum.ERR_10019.setErrorCode(result);
			}
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put(UtilConstants.REQUEST_ID, requestId);
			
			MakerCheckerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(MakerCheckerBusinessDelegate.class);
			JSONObject jsonObj = businessDelegate.createApprovalRuleBySignatoryGroupViewDetails(request, response, requestId);
			if (jsonObj != null) {
				result = JSONToResult.convert(jsonObj.toString());
			}
			return result;
		} catch (Exception e) {
			diagnostic.prepareInfo(ErrorCodesEnum.ERR_10053.getMessage()).log();
			return ErrorCodesEnum.ERR_10053.setErrorCode(result);
		}
	}
	public JSONArray removeEnrolMakerCheckerConfig(JSONArray array){
		if(array!=null && array.length()>0) {
			for(int i=0;i<array.length();i++) {
				JSONObject jsonObj= array.getJSONObject(i);
				String actionName= jsonObj.optString("actionName");
				if(actionName.equalsIgnoreCase("Enroll Customer")) {
					array.remove(i);
				}
					
			}
		}
		return array;
	}
}
