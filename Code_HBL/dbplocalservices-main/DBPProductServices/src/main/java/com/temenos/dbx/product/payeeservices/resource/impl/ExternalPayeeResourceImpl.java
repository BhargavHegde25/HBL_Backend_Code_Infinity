package com.temenos.dbx.product.payeeservices.resource.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sun.mail.imap.protocol.Item;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.payeeservices.constants.PayeeConstants;
import com.temenos.dbx.product.payeeservices.constants.PayeeVerificationBackendServicesHelper;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.ExternalPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.ExternalPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.resource.api.ExternalPayeeResource;
import com.temenos.dbx.product.payeeservices.resource.api.InterBankPayeeResource;
import com.temenos.dbx.product.payeeservices.resource.api.InternationalPayeeResource;
import com.temenos.dbx.product.payeeservices.resource.api.IntraBankPayeeResource;

public class ExternalPayeeResourceImpl implements ExternalPayeeResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String otherBankLogoUrl="https://www.connectips.com/cdn/CIPS/Retail/banklogo/";
	private static final String sameBankLogoUrl="https://www.connectips.com/cdn/CIPS/Retail/creditors/new/hbl_creditcard.png";
	
	ExternalPayeeBusinessDelegate externalPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ExternalPayeeBusinessDelegate.class);
	
	ExternalPayeeBackendDelegate externalPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(ExternalPayeeBackendDelegate.class);

	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	
	@Override
	public Result fetchAllMyPayees(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String userId = CustomerSession.getCustomerId(customer);
		
		List<ExternalPayeeBackendDTO> externalPayeeBackendDTOs = externalPayeeDelegate.fetchPayeesFromDBXOrch(request.getHeaderMap(), request);
		if(externalPayeeBackendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from dbx ").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		if(externalPayeeBackendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.EXTERNALACCOUNT, new JSONArray()).toString());
        }			

		if(externalPayeeBackendDTOs.get(0).getDbpErrMsg() != null && !(externalPayeeBackendDTOs.get(0).getDbpErrMsg()).isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, externalPayeeBackendDTOs.get(0).getDbpErrCode(), externalPayeeBackendDTOs.get(0).getDbpErrMsg());
		}

		//externalPayeeBackendDTOs.stream().filter(c->c.getIsApproved().equalsIgnoreCase("1")).collect(Collectors.toList());
		//Applying filters like offset,limit,sort etc
		FilterDTO filterDTO = new FilterDTO();
		try {
			filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
		}
		externalPayeeBackendDTOs = filterDTO.filter(externalPayeeBackendDTOs);
				
		try {
	        JSONArray records = new JSONArray(externalPayeeBackendDTOs);
	        records=addBankLogo(records);
	        JSONObject resultObject = new JSONObject();
	        resultObject.put(Constants.EXTERNALACCOUNT,records);
	        result = JSONToResult.convert(resultObject.toString());
		}
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_12048.setErrorCode(new Result());
        }
		return result;
	}
	public JSONArray addBankLogo(JSONArray records) {
		for(int i=0;i<records.length();i++) {
			JSONObject record= records.getJSONObject(i);
			String bankId=record.optString("iban");
			if(record.has("isSameBankAccount") && record.optString("isSameBankAccount").equalsIgnoreCase("true")) {
				record.put("bankName", "Himalayan Bank Limited");
				record.put("logoUrl", sameBankLogoUrl);
			}else {
				record.put("logoUrl", otherBankLogoUrl+bankId+".png");
			}
			records.put(i, record);
		}
		return records;
		
	}

	@Override
    public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		String isInternationalAccount = (String) inputParams.get("isInternationalAccount");
		String isSameBankAccount = (String) inputParams.get("isSameBankAccount");
		String verifyPayee = (String) inputParams.get("payeeVerification");
		String payeeVerificationStatus = (String) inputParams.get("payeeVerificationStatus");
		String payeeVerification = "";
		String payeeVerificationErrMsg = "";
		String payeeVerificationName="";
		if ("Success".equalsIgnoreCase(payeeVerificationStatus) || "Skipped".equalsIgnoreCase(payeeVerificationStatus)) {
			payeeVerification = payeeVerificationStatus;
		}
		if ("".equalsIgnoreCase(payeeVerification) && "true".equalsIgnoreCase(verifyPayee)) {
			payeeVerification = "Success";
			payeeVerificationStatus = "Success"; 
			Result payeeVerificationResult = PayeeVerificationBackendServicesHelper.fetchVerifyPayeeResponse(request, inputParams);
			if (payeeVerificationResult == null) {
				alert.prepareError("Error occured while invoking payee verification services: ").log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else {
				payeeVerification = payeeVerificationResult.getParamValueByName("payeeVerification");
				payeeVerificationStatus = payeeVerificationResult.getParamValueByName("payeeVerificationStatus");
				payeeVerificationErrMsg = payeeVerificationResult.getParamValueByName("payeeVerificationErrMsg");
				payeeVerificationName = payeeVerificationResult.getParamValueByName("payeeVerificationName");
			}
		} else if ("".equalsIgnoreCase(payeeVerification) && "false".equalsIgnoreCase(verifyPayee)) {
			payeeVerification = "Skipped";
			payeeVerificationStatus = "Skipped";
		}
		if("".equals(payeeVerification) || "Success".equalsIgnoreCase(payeeVerification) || "Skipped".equalsIgnoreCase(payeeVerification)) {
			inputParams.put("payeeVerification", payeeVerification);
		}else {
			inputParams.put("payeeVerification", "");
		}
		inputArray[1] = (HashMap<String, Object>) inputParams;
		if ("".equals(payeeVerificationErrMsg)) {
			if ("false".equals(isInternationalAccount) && "false".equals(isSameBankAccount)) {
				InterBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(InterBankPayeeResource.class);
				result = payeeResource.createPayee(methodId, inputArray, request, response);
			} else if ("true".equals(isInternationalAccount) && "false".equals(isSameBankAccount)) {
				InternationalPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(InternationalPayeeResource.class);
				result = payeeResource.createPayee(methodId, inputArray, request, response);

			} else if ("true".equals(isSameBankAccount) && "false".equals(isInternationalAccount)) {
				IntraBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(IntraBankPayeeResource.class);
				result = payeeResource.createPayee(methodId, inputArray, request, response);
			} else {
				alert.prepareError("Payee Type is not valid.").log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		}
		if (!"".equals(payeeVerificationStatus)) {
			result.addParam(new Param("payeeVerificationStatus", payeeVerificationStatus));
		}
		if (!"".equals(payeeVerificationErrMsg))
			result.addParam(new Param("payeeVerificationErrMsg", payeeVerificationErrMsg));
		    result.addParam(new Param("payeeVerificationName", payeeVerificationName));
		return result;
    }

    @Override
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		String isInternationalAccount = (String) inputParams.get("isInternationalAccount");
		String isSameBankAccount = (String) inputParams.get("isSameBankAccount");
		
		if("false".equals(isInternationalAccount) && "false".equals(isSameBankAccount)) {
			InterBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InterBankPayeeResource.class);
            result  = payeeResource.editPayee(methodId, inputArray, request, response);
		}
		else if("true".equals(isInternationalAccount) && "false".equalsIgnoreCase(isSameBankAccount)) {
			InternationalPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InternationalPayeeResource.class);
			result  = payeeResource.editPayee(methodId, inputArray, request, response);
            
		}
		else if("true".equals(isSameBankAccount) && "false".equals(isInternationalAccount)) {
        	IntraBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(IntraBankPayeeResource.class);
            result  = payeeResource.editPayee(methodId, inputArray, request, response);
		}
		else {
			alert.prepareError("Payee Type is not valid.").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
    }

    @Override
    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		String isInternationalAccount = (String) inputParams.get("isInternationalAccount");
		String isSameBankAccount = (String) inputParams.get("isSameBankAccount");
		
		if("false".equals(isInternationalAccount) && "false".equals(isSameBankAccount)) {
			InterBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InterBankPayeeResource.class);
            result  = payeeResource.deletePayee(methodId, inputArray, request, response);
		}
		else if("true".equals(isInternationalAccount) && "false".equals(isSameBankAccount)) {
			InternationalPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InternationalPayeeResource.class);
			result  = payeeResource.deletePayee(methodId, inputArray, request, response);
            
		}
		else if("true".equals(isSameBankAccount) && "false".equals(isInternationalAccount)) {
        	IntraBankPayeeResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(IntraBankPayeeResource.class);
            result  = payeeResource.deletePayee(methodId, inputArray, request, response);
		}
		else {
			alert.prepareError("Payee Type is not valid.").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
    }

}
