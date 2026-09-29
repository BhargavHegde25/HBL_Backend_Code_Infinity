package com.temenos.infinity.smartbanking.advisory.resource.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;
import com.temenos.infinity.smartbanking.advisory.resource.api.SmartBankingAdvisoryResource;
import com.temenos.infinity.smartbanking.advisory.utils.CommonUtils;
import com.temenos.dbx.usermanagement.resource.api.PartyUserManagementResource;

public class SmartBankingAdvisoryResourceImpl implements SmartBankingAdvisoryResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result getBusinessScoreAndDriversDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("subscriber"))) {
				CommonUtils.constructAndThrowValidationException("Subscriber is mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			
			Map<String, Object> scoreAndDriversResp = SBABusinessDelegate.getBusinessScoreAndDriversDetails(payloadMap);
			if (scoreAndDriversResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(scoreAndDriversResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Fetch Results successful");
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getBusinessScoreAndDriversDetails " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getBusinessScoreAndDriversDetails " + e.getMessage()).log();
		}
		return result;
	}
	
	
	@Override
	public Result getEnrollmentStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("subscriber"))) {
				CommonUtils.constructAndThrowValidationException("Subscriber is mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate sbaBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			
			Map<String, Object> enrollmentStatusResp = sbaBusinessDelegate.getEnrollmentStatus(payloadMap);
			if (enrollmentStatusResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(enrollmentStatusResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Successfully fetched enrollment status");
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getEnrollmentStatus " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getEnrollmentStatus " + e.getMessage()).log();
		}
		return result;
	}
	
	
	@Override
	public Result getAccountingData(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			SmartBankingAdvisoryBusinessDelegate sbaBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			
			Map<String, Object> accountingDataResp = sbaBusinessDelegate.getAccountingData(payloadMap);
			if (accountingDataResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(accountingDataResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Successfully fetched Accounting Details");
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getAccountingData " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getAccountingData " + e.getMessage()).log();
		}
		return result;
	}
	
	
	@Override
	public Result startProcess(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		Map<String, Object> partyUpdateStatusResp = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("targetPlatform")) && StringUtils.isEmpty((String) payloadMap.get("companyId")) 
					&& StringUtils.isEmpty((String) payloadMap.get("customerId")) && StringUtils.isEmpty((String) payloadMap.get("sbaEnrolmentStatus"))) {
				CommonUtils.constructAndThrowValidationException("Target Platform and Company Id and Customer Id are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate sbaBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			Map<String, Object> startProcessResp = sbaBusinessDelegate.startProcess(payloadMap);
			if (startProcessResp != null && startProcessResp.get("isEnrollmentSucess").toString().equalsIgnoreCase("true")) {
				if (!startProcessResp.containsKey("errmsg_postConnection")) {
					partyUpdateStatusResp = sbaBusinessDelegate.updateSBAStatus(payloadMap, request);
				}
				result = JSONToResult.convert(JSONUtils.stringify(startProcessResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Successfully Staretd Synchronization and "+partyUpdateStatusResp.get("message").toString());
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : startProcess " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : startProcess " + e.getMessage()).log();
		}
		return result;
	}
	
	
	@Override
	public Result get12MonthCashFlow(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("subscriber"))) {
				CommonUtils.constructAndThrowValidationException("Subscriber is mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			
			Map<String, Object> get12MonthCashFlowResp = SBABusinessDelegate.get12MonthCashFlow(payloadMap);
			if (get12MonthCashFlowResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(get12MonthCashFlowResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Sucessfully fetched the Response");
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : get12MonthCashFlow " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : get12MonthCashFlow " + e.getMessage()).log();
		}
		return result;
	}
	
	@Override
	public Result getSimulationDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("Subscriber")) && (StringUtils.isEmpty((String) payloadMap.get("income")) || 
					StringUtils.isEmpty((String) payloadMap.get("expense")))) {
				CommonUtils.constructAndThrowValidationException("Subscriber and Income Change and Expense Change are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			
			Map<String, Object> getSimulationDetailsResp = SBABusinessDelegate.getSimulationDetails(payloadMap);
			if (getSimulationDetailsResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(getSimulationDetailsResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Sucessfully fetched the Response");
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getSimulationDetails " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getSimulationDetails " + e.getMessage()).log();
		}
		return result;
	}
	
	
	@Override
	public Result getSBAEnrolmentStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("legalEntityId")) && (StringUtils.isEmpty((String) payloadMap.get("coreCustomerID")))) {
				CommonUtils.constructAndThrowValidationException("Legal Entity Id and Core Customer ID are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			Map<String, Object> enrollmentStatusResp = SBABusinessDelegate.getSBAEnrolmentStatus(payloadMap, request);
			if (enrollmentStatusResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(enrollmentStatusResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addStringParam("message", "Successfully fetched enrollment status");
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getSBASEnrolmentStatus " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getSBASEnrolmentStatus " + e.getMessage()).log();
		}
		return result;
	}
	
	@Override
	public Result updateSBAStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			payloadMap = (Map<String, Object>) inputArray[1];
			if(StringUtils.isEmpty((String) payloadMap.get("customerId")) && StringUtils.isEmpty((String) payloadMap.get("sbaEnrolmentStatus"))) {
				CommonUtils.constructAndThrowValidationException("Customer Id and sbaEnrolmentStatus are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			Map<String, Object> partyUpdateStatusResp = SBABusinessDelegate.updateSBAStatus(payloadMap, request);
			if (partyUpdateStatusResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(partyUpdateStatusResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : updateSBAStatus " + sbaException.getMessage()).log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : updateSBAStatus " + e.getMessage()).log();
		}
		return result;
	}
	
	@SuppressWarnings("null")
	@Override
	public Result getAccountsReceivable(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Map<String, Object> payloadMap = new HashMap<>();
		Result result = new Result();
		try {
			Dataset getAccountsReceivableResp = null;
			payloadMap = (Map<String, Object>) inputArray[1];
			if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("modelName"))) {
				CommonUtils.constructAndThrowValidationException("Query Param and Model Name are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);

			getAccountsReceivableResp = SBABusinessDelegate.getAccountsReceivable(payloadMap);
			if (getAccountsReceivableResp != null) {
				result.addDataset(getAccountsReceivableResp);
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getAccountsReceivable " + sbaException.getMessage())
					.log();
			result = sbaException.constructResultObject();
		} catch (NullPointerException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getAccountsReceivable" + e.getMessage()).log();
			result.addStringParam("dbpErrMsg", "Error from BackendResponse");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		}
		return result;
	}
	
	@SuppressWarnings("null")
	@Override
	public Result getAccountsPayable(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Map<String, Object> payloadMap = new HashMap<>();
		Result result = new Result();
		try {
			Dataset getAccountsPayableResp = null;
			payloadMap = (Map<String, Object>) inputArray[1];
			if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("modelName"))) {
				CommonUtils.constructAndThrowValidationException("Query Param and Model Name are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);

			getAccountsPayableResp = SBABusinessDelegate.getAccountsPayable(payloadMap);
			if (getAccountsPayableResp != null) {
				result.addDataset(getAccountsPayableResp);
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getAccountsPayable " + sbaException.getMessage())
					.log();
			result = sbaException.constructResultObject();
		} catch (NullPointerException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getAccountsPayable" + e.getMessage()).log();
			result.addStringParam("dbpErrMsg", "Error from BackendResponse");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		}
		return result;
	}
	
	@SuppressWarnings("null")
	@Override
	public Result getReceivablesDebtorDaysReq(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Map<String, Object> payloadMap = new HashMap<>();
		Result result = new Result();
		try {
			Dataset getReceivablesDebtorDaysReqResp = null;
			payloadMap = (Map<String, Object>) inputArray[1];
			if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("modelName"))
					|| StringUtils.isEmpty((String) payloadMap.get("topParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))) {
				CommonUtils.constructAndThrowValidationException("All Params are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);

			getReceivablesDebtorDaysReqResp = SBABusinessDelegate.getReceivablesDebtorDaysReq(payloadMap);
			if (getReceivablesDebtorDaysReqResp != null) {
				result.addDataset(getReceivablesDebtorDaysReqResp);
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getReceivablesDebtorDaysReq " + sbaException.getMessage())
					.log();
			result = sbaException.constructResultObject();
		} catch (NullPointerException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getReceivablesDebtorDaysReq" + e.getMessage()).log();
			result.addStringParam("dbpErrMsg", "Error from BackendResponse");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		}
		return result;
	}
	
	@SuppressWarnings("null")
	@Override
	public Result getPayablesDebtorDaysReq(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Map<String, Object> payloadMap = new HashMap<>();
		Result result = new Result();
		try {
			Dataset getPayablesDebtorDaysReqResp = null;
			payloadMap = (Map<String, Object>) inputArray[1];
			if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("modelName"))
					|| StringUtils.isEmpty((String) payloadMap.get("topParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))) {
				CommonUtils.constructAndThrowValidationException("All Params are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);

			getPayablesDebtorDaysReqResp = SBABusinessDelegate.getPayablesDebtorDaysReq(payloadMap);
			if (getPayablesDebtorDaysReqResp != null) {
				result.addDataset(getPayablesDebtorDaysReqResp);
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getPayablesDebtorDaysReq " + sbaException.getMessage())
					.log();
			result = sbaException.constructResultObject();
		} catch (NullPointerException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getPayablesDebtorDaysReq" + e.getMessage()).log();
			result.addStringParam("dbpErrMsg", "Error from BackendResponse");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		}
		return result;
	}	
	
	@SuppressWarnings({ "null", "unchecked" })
	@Override
	public Result getAccountsReceivableByCustomer(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Map<String, Object> payloadMap = new HashMap<>();
		Result result = new Result();
		try {
			Map<String, Object> getAccountsReceivableResp ;
			String getAccountsReceivable;
			payloadMap = (Map<String, Object>) inputArray[1];
			if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("modelName")) 
					|| StringUtils.isEmpty((String) payloadMap.get("Subscriber"))
					|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("topParam"))) {
				CommonUtils.constructAndThrowValidationException("All params are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			getAccountsReceivableResp = SBABusinessDelegate.getAccountsReceivableByCustomer(payloadMap);
			if (getAccountsReceivableResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(getAccountsReceivableResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getAccountsReceivableByCustomer " + sbaException.getMessage())
					.log();
			result = sbaException.constructResultObject();
		} catch (NullPointerException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getAccountsReceivableByCustomer" + e.getMessage()).log();
			result.addStringParam("dbpErrMsg", "Error from BackendResponse");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		}catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getAccountsReceivableByCustomer " + e.getMessage()).log();
		}
		return result;
	}
	
	@SuppressWarnings({ "null", "unchecked" })
	@Override
	public Result getAccountsPayableBySupplier(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Map<String, Object> payloadMap = new HashMap<>();
		Result result = new Result();
		try {
			Map<String, Object> getAccountsReceivableResp ;
			String getAccountsReceivable;
			payloadMap = (Map<String, Object>) inputArray[1];
			if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("modelName")) 
					|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))
					|| StringUtils.isEmpty((String) payloadMap.get("topParam"))) {
				CommonUtils.constructAndThrowValidationException("All params are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
			getAccountsReceivableResp = SBABusinessDelegate.getAccountsPayableBySupplier(payloadMap);
			if (getAccountsReceivableResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(getAccountsReceivableResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getAccountsPayableBySupplier " + sbaException.getMessage())
					.log();
			result = sbaException.constructResultObject();
		} catch (NullPointerException e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getAccountsPayableBySupplier" + e.getMessage()).log();
			result.addStringParam("dbpErrMsg", "Error from BackendResponse");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		}catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getAccountsPayableBySupplier " + e.getMessage()).log();
		}
		return result;
	}
	
	@Override
	public Result getSBAFeaturesActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> payloadMap = new HashMap<>();
		try {
			String userId = request.getServicesManager().getIdentityHandler().getUserAttributes().get("user_id")
					.toString();
			String legalEntityId = request.getServicesManager().getIdentityHandler().getUserAttributes()
					.get("homeLegalEntity").toString();
			payloadMap.put("userId", userId);
			payloadMap.put("legalEntityId", legalEntityId);
		} catch (MiddlewareException e) {
			alert.prepareError(e.getMessage()).log();
		}

		try {

			if (StringUtils.isEmpty((String) payloadMap.get("userId"))
					|| StringUtils.isEmpty((String) payloadMap.get("legalEntityId"))) {
				CommonUtils.constructAndThrowValidationException("Custoemr Id and Legal Entity Id are mandatory");
			}
			SmartBankingAdvisoryBusinessDelegate sbaBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);

			Map<String, Object> accountingDataResp = sbaBusinessDelegate.getSBAFeaturesActions(payloadMap);
			if (accountingDataResp != null) {
				result = JSONToResult.convert(JSONUtils.stringify(accountingDataResp));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
		} catch (SBAException sbaException) {
			result = sbaException.constructResultObject();
			alert.prepareError(
					"Error in SmartBankingAdvisoryResourceImpl : getSBAFeaturesActions " + sbaException.getMessage())
					.log();
		} catch (IOException e) {
			alert.prepareError("Error in SmartBankingAdvisoryResourceImpl : getSBAFeaturesActions " + e.getMessage())
					.log();
		}
		return result;
	}
}
