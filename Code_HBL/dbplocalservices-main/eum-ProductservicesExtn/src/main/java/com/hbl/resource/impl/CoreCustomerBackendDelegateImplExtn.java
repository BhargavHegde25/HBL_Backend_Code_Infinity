package com.hbl.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.hbl.resource.constants.HBLConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.CoreCustomerBackendDelegateImpl;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;

public class CoreCustomerBackendDelegateImplExtn extends CoreCustomerBackendDelegateImpl{
	LoggerUtil logger = new LoggerUtil(CoreCustomerBackendDelegateImplExtn.class);
	
	public DBXResult searchHBLCoreCustomers(Map<String, Object> payload, Map<String, Object> headersMap)
			throws ApplicationException {
		logger.debug("HBL::CoreCustomerBackendDelegateImplExtn::searchCoreCustomers:" + payload.toString());
		DBXResult responseDTO = new DBXResult();
		Map<String, Object> procParams = new HashMap<>();
		procParams.put("_id", payload.get("id")!=null ? payload.get("id").toString() : "");
		procParams.put("_name", payload.get("name")!=null ? payload.get("name").toString() : "");
		procParams.put("_email", payload.get("email")!=null ?payload.get("email").toString() : "");
		procParams.put("_phone", payload.get("phone")!=null ?payload.get("phone").toString() : "");
		procParams.put("_dateOfBirth",
				payload.get("dateOfBirth")!=null ?payload.get("dateOfBirth").toString()  : "");
		procParams.put("_status", payload.get("status")!=null?payload.get("status").toString() : "");
		procParams.put("_city", payload.get("city")!=null ?payload.get("city").toString() : "");

		procParams.put("_country",
				payload.get("country")!=null ?payload.get("country").toString() : "");
		procParams.put("_zipCode",
				payload.get("zipCode")!=null ?payload.get("zipCode").toString() : "");
		if (payload.get("taxId")!=null) {
			procParams.put("_taxId", payload.get("taxId")!=null  ? payload.get("taxId").toString() : "");
		}
		if (payload.get("legalEntityId")!=null) {
			procParams.put("_legalEntityId",
					payload.get("legalEntityId")!=null ? payload.get("legalEntityId").toString()
							: "");
		}
		
		if (payload.get("accountNumber")!=null) {
			procParams.put("_accountNumber",
					payload.get("accountNumber")!=null ? payload.get("accountNumber").toString()
							: "");
		}
		if (payload.get("accountName")!=null) {
			procParams.put("_accountName",
					payload.get("accountName")!=null ? payload.get("accountName").toString()
							: "");
		}
		if (payload.get("mobileNumber")!=null) {
			procParams.put("_mobileNumber",
					payload.get("mobileNumber")!=null ? payload.get("mobileNumber").toString()
							: "");
		}
		logger.debug("HBL::CoreCustomerBackendDelegateImplExtn::searchCoreCustomers: procParams:" + procParams.toString());

		JsonObject response = new JsonObject();
		try {
			String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
				response = searchCoreCustomerT24(procParams, headersMap);
			} else {
				response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
						URLConstants.MEMBERSHIP_CUSTOMER_SEARCH_PROC);
			}
			logger.debug("partycust" + response);
			if (JSONUtil.hasKey(response, "records")) {
				if (response.get("records").getAsJsonArray().size() > 0) {
					responseDTO.setResponse(response.get("records").getAsJsonArray());
				}
			} else if (JSONUtil.hasKey(response, "error")) {
				if (response.get("error").getAsJsonArray().size() > 0) {
					JsonObject errorObj = response.get("error").getAsJsonArray().get(0).getAsJsonObject();
					logger.debug("HBL::CoreCustomerBackendDelegateImplExtn::searchCoreCustomers: errorObj:::"
							+ errorObj.toString());
					String errorCode = errorObj.has("code") ? errorObj.get("code").getAsString() : "";
					switch (errorCode) {
					case "E-141420":
						throw new ApplicationException(ErrorCodeEnum.ERR_141420);
					case "E-141421":
						throw new ApplicationException(ErrorCodeEnum.ERR_141421);
					case "E-141206":
						throw new ApplicationException(ErrorCodeEnum.ERR_141206);
					case "E-110608":
						throw new ApplicationException(ErrorCodeEnum.ERR_110608);
					default:
						logger.error("Unhandled error code from backend: " + errorCode);
						throw new ApplicationException(ErrorCodeEnum.ERR_10756);
					}
				}
			} else {
				logger.error("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response);
				throw new ApplicationException(ErrorCodeEnum.ERR_10756);
			}
		} catch (ApplicationException ae) {
			throw ae;
		}catch (Exception e) {
			logger.error("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
					+ e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10756);
		}
		return responseDTO;

	}
	 public JsonObject searchCoreCustomerT24(Map<String, Object> procParams, Map<String, Object> headersMap) {
	        addT24Headers(headersMap, (String) procParams.get("_id"), (String) procParams.get("_legalEntityId"));
	        if (procParams.containsKey("_combinedids")) {
				procParams.put("_id", procParams.get("_combinedids"));
			}
	        return ServiceCallHelper.invokeServiceAndGetJson(HBLConstants.HBL_T24ISUSER_INTEGRATION_SERVICE,
	                null, HBLConstants.SEARCH_CORE_CUSTOMER,
	                procParams, headersMap);
	        /*return ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISUSER_INTEGRATION_SERVICE,
		                null, OperationName.SEARCH_CORE_CUSTOMER,
		                procParams, headersMap);*/

	    }
	 
	 //Can't Sign In Flow
	 @Override
		public DBXResult searchCoreCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
				throws ApplicationException {
			
			DBXResult responseDTO = new DBXResult();
			Map<String, Object> procParams = new HashMap<>();
			procParams.put("_id", StringUtils.isNotBlank(membershipDTO.getId()) ? membershipDTO.getId() : "");
			//procParams.put("_name", StringUtils.isNotBlank(membershipDTO.getName()) ? membershipDTO.getName() : "");
			procParams.put("_email", StringUtils.isNotBlank(membershipDTO.getEmail()) ? membershipDTO.getEmail() : "");
			procParams.put("_phone", StringUtils.isNotBlank(membershipDTO.getPhone()) ? membershipDTO.getPhone() : "");
			procParams.put("_dateOfBirth",
					StringUtils.isNotBlank(membershipDTO.getDateOfBirth()) ? membershipDTO.getDateOfBirth() : "");
			procParams.put("_status", StringUtils.isNotBlank(membershipDTO.getStatus()) ? membershipDTO.getStatus() : "");
			procParams.put("_city",
					(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getCityName()))
							? membershipDTO.getAddress().getCityName()
							: "");
			procParams.put("_country",
					(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getCountry()))
							? membershipDTO.getAddress().getCountry()
							: "");
			procParams.put("_zipCode",
					(membershipDTO.getAddress() != null && StringUtils.isNotBlank(membershipDTO.getAddress().getZipCode()))
							? membershipDTO.getAddress().getZipCode()
							: "");
			/*if (StringUtils.isNotBlank(membershipDTO.getTaxId())) {
				procParams.put("_taxId", StringUtils.isNotBlank(membershipDTO.getTaxId()) ? membershipDTO.getTaxId() : "");
			}
			*/
			if (StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit())) {
				procParams.put("_legalEntityId",
						StringUtils.isNotBlank(membershipDTO.getCompanyLegalUnit()) ? membershipDTO.getCompanyLegalUnit()
								: "");
			}
			
			//HBL Can't Sign In Flow
			if (StringUtils.isNotBlank(membershipDTO.getName())) {
				procParams.put("_accountName",
						 StringUtils.isNotBlank(membershipDTO.getName()) ? membershipDTO.getName() : "");
			}
			if (StringUtils.isNotBlank(membershipDTO.getTaxId())) {
				procParams.put("_accountNumber",
						 StringUtils.isNotBlank(membershipDTO.getTaxId()) ? membershipDTO.getTaxId() : "");
			}
			procParams.put("_email", StringUtils.isNotBlank(membershipDTO.getEmail()) ? membershipDTO.getEmail() : "");
			procParams.put("_mobileNumber", StringUtils.isNotBlank(membershipDTO.getPhone()) ? membershipDTO.getPhone() : "");
			
			
			logger.debug("HBL::searchHBLCoreCustomers:searchCoreCustomers:Can't Sign in flow:procParams" + procParams.toString());
			JsonObject response = new JsonObject();
			try {
				String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
				if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
					response = searchCoreCustomerT24(procParams, headersMap);
				} else {
					response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
							URLConstants.MEMBERSHIP_CUSTOMER_SEARCH_PROC);
				}
				logger.debug("HBL::Can't Sign in flow: Response:" + response);
				if (JSONUtil.hasKey(response, "records")) {
					if (response.get("records").getAsJsonArray().size() > 0) {
						responseDTO.setResponse(response.get("records").getAsJsonArray());
					}
				} else if (JSONUtil.hasKey(response, "error")) {
					if (response.get("error").getAsJsonArray().size() > 0) {
						JsonObject errorObj = response.get("error").getAsJsonArray().get(0).getAsJsonObject();
						logger.debug("HBL::CoreCustomerBackendDelegateImplExtn::searchCoreCustomers: errorObj:::"
								+ errorObj.toString());
						String errorCode = errorObj.has("code") ? errorObj.get("code").getAsString() : "";
						switch (errorCode) {
						case "E-141420":
							throw new ApplicationException(ErrorCodeEnum.ERR_141420);
						case "E-141421":
							throw new ApplicationException(ErrorCodeEnum.ERR_141421);
						case "E-141206":
							throw new ApplicationException(ErrorCodeEnum.ERR_141206);
						case "E-110608":
							throw new ApplicationException(ErrorCodeEnum.ERR_110608);
						default:
							logger.error("Unhandled error code from backend: " + errorCode);
							throw new ApplicationException(ErrorCodeEnum.ERR_10756);
						}
					}
				} else {
					logger.error("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response);
					throw new ApplicationException(ErrorCodeEnum.ERR_10756);
				}
			} catch (ApplicationException ae) {
				throw ae;
			} catch (Exception e) {
				logger.error("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
						+ e.getMessage());
				throw new ApplicationException(ErrorCodeEnum.ERR_10756);
			}
			return responseDTO;

		}



}
