package com.kony.adminconsole.service.customermanagement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.common.collect.Multiset.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.dto.MemberSearchBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.CacheUtil;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * CustomerSearch service will serach for a customer based on several search
 * parameters
 * 
 * @author Alahari Prudhvi Akhil (KH2346)
 * 
 */
public class CustomerSearch implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	static final String APPLICANT_SEARCH = "APPLICANT_SEARCH";
	static final String APPLICANT_SEARCH_TOTAL_COUNT = "APPLICANT_SEARCH_TOTAL_COUNT";
	static final String GROUP_SEARCH = "GROUP_SEARCH";
	static final String GROUP_SEARCH_TOTAL_COUNT = "GROUP_SEARCH_TOTAL_COUNT";
	static final String CUSTOMER_SEARCH = "CUSTOMER_SEARCH";
	static final String CUSTOMER_SEARCH_TOTAL_COUNT = "CUSTOMER_SEARCH_TOTAL_COUNT";

	public static final String STATUS_SUCCESS = "Success";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		try {
			
			// Sort validation
			if (StringUtils.isNotBlank(requestInstance.getParameter("_sortVariable"))
					&& requestInstance.getParameter("_sortVariable").contains(" ")) {
				ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
				return processedResult;
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter("_sortDirection"))
					&& (requestInstance.getParameter("_sortDirection").contains(" ")
							|| (!requestInstance.getParameter("_sortDirection").equalsIgnoreCase("ASC")
									&& !requestInstance.getParameter("_sortDirection").equalsIgnoreCase("DESC")))) {
				ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
				return processedResult;
			}
			
			String id = requestInstance.getParameter("_id");
			if (StringUtils.isBlank(id) ||  id.equalsIgnoreCase("null") ) {
				id ="";
			}
			
			String name = requestInstance.getParameter("_name");
			if (StringUtils.isBlank(name) ||  name.equalsIgnoreCase("null") ) {
				name="";
			}
			String SSN = requestInstance.getParameter("_SSN");
			if (StringUtils.isBlank(SSN) ||  SSN.equalsIgnoreCase("null") ) {
				SSN="";
			}
			String username = requestInstance.getParameter("_username");
			if (StringUtils.isBlank(username) ||  username.equalsIgnoreCase("null") ) {
				username="";
			}
			String phone = requestInstance.getParameter("_phone");
			if (StringUtils.isBlank(phone) ||  phone.equalsIgnoreCase("null") ) {
				phone="";
			}
			String email = requestInstance.getParameter("_email");
			if (StringUtils.isBlank(email) ||  email.equalsIgnoreCase("null") ) {
				email="";
			}
			String accNo = requestInstance.getParameter("_cardorAccountnumber");
			if (StringUtils.isBlank(accNo) ||  accNo.equalsIgnoreCase("null") ) {
				accNo="";
			}
			String Tin = requestInstance.getParameter("_TIN");
			if (StringUtils.isBlank(Tin) ||  Tin.equalsIgnoreCase("null") ) {
				Tin="";
			}
			String IDType = requestInstance.getParameter("_IDType");
			if (StringUtils.isBlank(IDType) ||  IDType.equalsIgnoreCase("null") ) {
				IDType="";
			}
			String IDValue = requestInstance.getParameter("_IDValue");
			if (StringUtils.isBlank(IDValue) ||  IDValue.equalsIgnoreCase("null") ) {
				IDValue="";
			}
			String CompanyId = requestInstance.getParameter("_companyId");
			if (StringUtils.isBlank(CompanyId) ||  CompanyId.equalsIgnoreCase("null") ) {
				CompanyId="";
			}
			String searchType = requestInstance.getParameter("_searchType");
			if (StringUtils.isBlank(searchType) || searchType.equalsIgnoreCase("null") ) {
				searchType="";
			}
			String dateOfBirth = requestInstance.getParameter("_dateOfBirth");
			if (StringUtils.isBlank(dateOfBirth) || dateOfBirth.equalsIgnoreCase("null") ) {
				dateOfBirth="";
			}
			String customerId = requestInstance.getParameter("_customerId");
			if (StringUtils.isBlank(customerId) || customerId.equalsIgnoreCase("null") ) {
				customerId="";
			}
			String isStaffMember = requestInstance.getParameter("_IsStaffMember");
			if (StringUtils.isBlank(isStaffMember) || isStaffMember.equalsIgnoreCase("null") ) {
				isStaffMember="";
			}
			String  group= requestInstance.getParameter("_group");
			if (StringUtils.isBlank(group) || group.equalsIgnoreCase("null") ) {
				group="";
			}
			String  requestID= requestInstance.getParameter("_requestID");
			if (StringUtils.isBlank(requestID) || requestID.equalsIgnoreCase("null") ) {
				requestID="";
			}
			String  branchIDS= requestInstance.getParameter("_branchIDS");
			if (StringUtils.isBlank(branchIDS) || branchIDS.equalsIgnoreCase("null") ) {
				branchIDS="";
			}
			String  productIDS= requestInstance.getParameter("_productIDS");
			if (StringUtils.isBlank(productIDS) || productIDS.equalsIgnoreCase("null") ) {
				productIDS="";
			}
			String cityIDS = requestInstance.getParameter("_cityIDS");
			if (StringUtils.isBlank(cityIDS) || cityIDS.equalsIgnoreCase("null") ) {
				cityIDS="";
			}
			String  entitlementIDS= requestInstance.getParameter("_entitlementIDS");
			if (StringUtils.isBlank(entitlementIDS) || entitlementIDS.equalsIgnoreCase("null") ) {
				entitlementIDS="";
			}
			String  groupIDS= requestInstance.getParameter("_groupIDS");
			if (StringUtils.isBlank(groupIDS) || groupIDS.equalsIgnoreCase("null") ) {
				groupIDS="";
			}
			String  custStatus= requestInstance.getParameter("_customerStatus");
			if (StringUtils.isBlank(custStatus) || custStatus.equalsIgnoreCase("null") ) {
				custStatus="";
			}
			String  before= requestInstance.getParameter("_before");
			if (StringUtils.isBlank(before) || before.equalsIgnoreCase("null") ) {
				before="";
			}
			String  after= requestInstance.getParameter("_after");
			if (StringUtils.isBlank(after) || after.equalsIgnoreCase("null") ) {
				after="";
			}
			
			String companyLegalUnit = requestInstance.getParameter("_legalEntityId");
			if (StringUtils.isBlank(companyLegalUnit) ||  companyLegalUnit.equalsIgnoreCase("null") ) {
				companyLegalUnit="";
			}
			
			if (StringUtils.isBlank(companyLegalUnit)) {
				ErrorCodeEnum.ERR_22230.setErrorCode(processedResult);
				return processedResult;
				
			}
			
			String[] reqPermissions = {PermissionName.VIEW_CUSTOMER_ACTIVITY_LOGS, PermissionName.UPDATE_CUSTOMER_GROUP,
					PermissionName.ASSIGN_CUSTOMER_GROUP, PermissionName.UPDATE_GROUP,
					PermissionName.VIEW_CUSTOMER, PermissionName.CREATE_GROUP};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
			{
				processedResult.addParam(new Param("Status", "Customer search operation failed", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return processedResult;
				
			}

			MemberSearchBean memberSearchBean = new MemberSearchBean();
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			memberSearchBean.setSearchType(searchType);
			memberSearchBean.setMemberId(id);
			memberSearchBean.setCustomerName(name);
			memberSearchBean.setSsn(SSN);
			memberSearchBean.setCustomerUsername(username);
			memberSearchBean.setCustomerPhone(phone);
			memberSearchBean.setCustomerEmail(email);
			memberSearchBean.setIsStaffMember(isStaffMember);
			memberSearchBean.setCardorAccountnumber(accNo);
			memberSearchBean.setTin(Tin);
			memberSearchBean.setCustomerGroup(group);
			memberSearchBean.setCustomerIDType(IDType);
			memberSearchBean.setCustomerIDValue(IDValue);
			memberSearchBean.setCustomerCompanyId(CompanyId);
			memberSearchBean.setCustomerRequest(requestID);
			memberSearchBean.setBranchIDS(branchIDS);
			memberSearchBean.setProductIDS(productIDS);
			memberSearchBean.setCityIDS(cityIDS);
			memberSearchBean.setEntitlementIDS(entitlementIDS);
			memberSearchBean.setGroupIDS(groupIDS);
			memberSearchBean.setCustomerStatus(custStatus);
			memberSearchBean.setBeforeDate(before);
			memberSearchBean.setAfterDate(after);
			memberSearchBean.setDateOfBirth(dateOfBirth);
			memberSearchBean.setCustomerId(customerId);
			memberSearchBean.setCompanyLegalUnit(companyLegalUnit);
			memberSearchBean.setSortVariable(requestInstance.getParameter("_sortVariable"));
			memberSearchBean.setSortDirection(requestInstance.getParameter("_sortDirection"));
			memberSearchBean.setPageOffset(requestInstance.getParameter("_pageOffset"));
			memberSearchBean.setPageSize(requestInstance.getParameter("_pageSize"));

			if (StringUtils.isBlank(memberSearchBean.getSearchType())) {
				ErrorCodeEnum.ERR_20690.setErrorCode(processedResult);
				return processedResult;

			}
		
			processedResult
					.addParam(new Param("SortVariable", memberSearchBean.getSortVariable(), FabricConstants.STRING));
			processedResult
					.addParam(new Param("SortDirection", memberSearchBean.getSortDirection(), FabricConstants.STRING));
			processedResult.addParam(
					new Param("PageOffset", String.valueOf(memberSearchBean.getPageOffset()), FabricConstants.INT));
			processedResult.addParam(
					new Param("PageSize", String.valueOf(memberSearchBean.getPageSize()), FabricConstants.INT));

			/*if (StringUtils.isBlank(id)
			&& StringUtils.isBlank(customerId)
			&& StringUtils.isBlank(dateOfBirth)
			&& StringUtils.isBlank(name)
			&& StringUtils.isBlank(SSN)
			&& StringUtils.isBlank(username)
			&& StringUtils.isBlank(phone)
			&& StringUtils.isBlank(email)
			&& StringUtils.isBlank(accNo)
			&& StringUtils.isBlank(Tin)
			&& StringUtils.isBlank(IDType)
			&& StringUtils.isBlank(IDValue)
			&& StringUtils.isBlank(CompanyId)
			&& (searchType.equalsIgnoreCase("CUSTOMER_SEARCH"))) {
		processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
		Dataset recordsDS = new Dataset();
		recordsDS.setId("records");
		processedResult.addDataset(recordsDS);
		return processedResult;
	}
	}*/
	if((!(StringUtils.isBlank(companyLegalUnit) || companyLegalUnit.equalsIgnoreCase("null")))
        	&& (StringUtils.isBlank(id) || id.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(name) || name.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(dateOfBirth) || dateOfBirth.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(SSN) || SSN.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(username) || username.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(phone) || phone.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(email) || email.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(accNo) || accNo.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(Tin) || Tin.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(IDType) || IDType.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(IDValue) || IDValue.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(CompanyId) || CompanyId.equalsIgnoreCase("null"))
            && (StringUtils.isBlank(customerId) || customerId.equalsIgnoreCase("null"))
            && (searchType.equalsIgnoreCase("CUSTOMER_SEARCH"))){
		    ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
            return processedResult;
        	
        	
        }
	
	

			if (memberSearchBean.getSearchType().equalsIgnoreCase(APPLICANT_SEARCH)) {
				processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
			}

			JSONObject customers = null;
			if (StringUtils.isNotBlank(accNo)) {
				if (StringUtils.isNotBlank(id)
						|| StringUtils.isNotBlank(customerId)
						|| StringUtils.isNotBlank(name)
						|| StringUtils.isNotBlank(username)
						|| StringUtils.isNotBlank(phone)
						|| StringUtils.isNotBlank(email)
						|| StringUtils.isNotBlank(Tin)
						|| StringUtils.isNotBlank(IDType)
						|| StringUtils.isNotBlank(IDValue)
						|| StringUtils.isNotBlank(CompanyId) ) {

					processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
					Dataset recordsDS = new Dataset();
					recordsDS.setId("records");
					processedResult.addDataset(recordsDS);
					return processedResult;
				}

				customers = DBPServices.getCustomerWithAccountNumber(authToken, accNo, requestInstance);

			} else {
				customers = DBPServices.searchCustomers(authToken, memberSearchBean.getSearchType(), memberSearchBean,
						requestInstance);
			}

			if(customers.has("ErasureStatusRecordsFound")) {
				throw new Exception(ErrorCodeEnum.ERR_20721.getMessage());
			}
			
			if (customers.has("TotalResultsFound")) {
				processedResult.addParam(new Param("TotalResultsFound", customers.get("TotalResultsFound").toString(),
						FabricConstants.STRING));
			}
			if (customers.has("customerbasicinfo_view")) {
				JSONObject customerViewJson = customers.getJSONObject("customerbasicinfo_view");
//				String customerStatus = customerViewJson.getString("CustomerStatus_id");
//				if (StringUtils.isNotBlank(customerStatus) && customerStatus.equalsIgnoreCase("SID_CUS_INACTIVE")) {
//					String combinedUserId = customerViewJson.getString("combinedUserId");
//					if (StringUtils.isNotBlank(combinedUserId)) {
//						return searchCombinedUser(combinedUserId, authToken, requestInstance);
//					}
//				}
				
				

				SSN = customerViewJson.optString("SSN");
//				String CustomerTypeId = customerViewJson.optString("CustomerType_id");
//                if(!customerViewJson.has("CustomerType_Name")&&CustomerTypeId.equalsIgnoreCase("TYPE_ID_PROSPECT")){
//					customerViewJson.put("CustomerType_id", "TYPE_ID_RETAIL");
//				}
				
				String isEnrolledFromSpotlight = customerViewJson.optString("isEnrolledFromSpotlight");
				String isProfileExist = customerViewJson.optString("isProfileExist");
				String isCustomerEnrolled = customerViewJson.optString("isCustomerEnrolled");
				String userId = customerViewJson.optString("id");
				
				if(StringUtils.isNotBlank(userId) && 
						!(( null!=isEnrolledFromSpotlight && isEnrolledFromSpotlight.equalsIgnoreCase("1"))&&
						( null!=isProfileExist && isProfileExist.equalsIgnoreCase("false"))&&
						(null!=isCustomerEnrolled && isCustomerEnrolled.equalsIgnoreCase("false")))) {
					
					boolean hasAccess = CustomerHandler.doesCurrentLoggedinUserHasAccessToGivenCustomer("", userId,
							requestInstance, new Result());
				
					if(hasAccess) {
						customerViewJson.put("isCustomerAccessiable", true);
					} else {
						customerViewJson.put("isCustomerAccessiable", false);
					}
				}
				
				customerViewJson.put("SSN", CustomerHandler.maskSSN(SSN));
				Record customerbasicinfo_view = CommonUtilities.constructRecordFromJSONObject(customerViewJson);
				customerbasicinfo_view.setId("customerbasicinfo_view");
				processedResult.addRecord(customerbasicinfo_view);
				Record configuration = new Record();
				configuration.setId("Configuration");
				if (customerViewJson.has("accountLockoutTime")) {
					configuration.addParam(new Param("value", customerViewJson.getString("accountLockoutTime")));
				}
				processedResult.addRecord(configuration);
			}
			if (customers.has("records")) {

				JSONArray recordsArray = customers.getJSONArray("records");
				filterSearchedCustomerLegalEntities(requestInstance, recordsArray);
				Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(recordsArray);
				recordsDataset.setId("records");
				Param recordsStatus = new Param("Status", "Records returned: " + recordsArray.length(),
						FabricConstants.STRING);
				processedResult.addDataset(recordsDataset);
				processedResult.addParam(recordsStatus);
			} else {
//				ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
				Dataset recordsDataset = new Dataset();
				recordsDataset.setId("records");
				processedResult.addDataset(recordsDataset);
				if(!processedResult.hasParamByName("TotalResultsFound")) {
					processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
				}
				if(!processedResult.hasParamByName("Status")) {
					processedResult.addParam(new Param("Status", "Records returned: 0", FabricConstants.STRING));
				}
				return processedResult;

			}
			
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.SUCCESSFUL,
                    "Successfully fetched customer detail. customerId: " + memberSearchBean.toString());

			return processedResult;
		} catch (Exception e) {
			alert.prepareError("Unexpected error", e).log();
			
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED,
                    "Failed to search customer detail: " + e.getMessage());
			
			processedResult.addParam(new Param("FailureReason", e.getMessage()));
			if(e.getMessage().toString().equals(ErrorCodeEnum.ERR_20721.getMessage())){
				ErrorCodeEnum.ERR_20721.setErrorCode(processedResult);
			} else {
				ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
			}
			return processedResult;
		}
	}



	private void filterSearchedCustomerLegalEntities(DataControllerRequest requestInstance, JSONArray recordsArray) {

		Set<String> spotlightUserEntities = CacheUtil.getLoggedInUserLegalEntities(requestInstance);
		MultiEntityResource companyLegalUnitResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(MultiEntityResource.class);

		Result companyLegalUnits = companyLegalUnitResource.getAllCompanyLegalUnits(requestInstance);
		JsonElement legalEntitiesElement = new JsonParser().parse(ResultToJSON.convert(companyLegalUnits));
		JsonArray legalEntitiesJsonArray = legalEntitiesElement.getAsJsonObject().get("companyLegalUnits")
				.getAsJsonArray();
		Map<String, String> legalentityInfo = new HashMap<>();
		for (int i = 0; i < legalEntitiesJsonArray.size(); i++) {
			String legalentityid = legalEntitiesJsonArray.get(i).getAsJsonObject().get("id").getAsString();
			String companyName = legalEntitiesJsonArray.get(i).getAsJsonObject().get("companyName").getAsString();
			legalentityInfo.put(legalentityid, companyName);
		}
		if (recordsArray != null && !recordsArray.isEmpty()) {
			for (int i = 0; i < recordsArray.length(); i++) {
				
				JSONObject customerJson = recordsArray.getJSONObject(i);
				if (customerJson.has("legalEntities")) {
					JSONArray legalEntitiesArray = customerJson.getJSONArray("legalEntities");

					JSONArray newLegalEntitiesArray = new JSONArray();
					if (legalEntitiesArray != null && !legalEntitiesArray.isEmpty()) {
						for (int p = 0; p < legalEntitiesArray.length(); p++) {
							JSONObject customerLegalEntityJsonObj = legalEntitiesArray.getJSONObject(p);
							String LE1 = customerLegalEntityJsonObj.getString("legalEntity");
							if (spotlightUserEntities.contains(LE1)) {
								customerLegalEntityJsonObj.put("description", legalentityInfo.get(LE1));
								newLegalEntitiesArray.put(customerLegalEntityJsonObj);

							}
						}

						customerJson.put("legalEntities", newLegalEntitiesArray);
					}
				}
			}

		}
	}

//	public static Result searchCombinedUser(String customerId, String authToken, DataControllerRequest requestInstance)
//			throws DBPAuthenticationException {
//		MemberSearchBean combinedUserBean = new MemberSearchBean();
//		authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
//		combinedUserBean.setSearchType("CUSTOMER_SEARCH");
//		combinedUserBean.setMemberId(customerId);
//		combinedUserBean.setSortVariable(requestInstance.getParameter("_sortVariable"));
//		combinedUserBean.setSortDirection(requestInstance.getParameter("_sortDirection"));
//		combinedUserBean.setPageOffset(requestInstance.getParameter("_pageOffset"));
//		combinedUserBean.setPageSize(requestInstance.getParameter("_pageSize"));
//		Result combinedResult = new Result();
//		JSONObject customer = DBPServices.searchCustomers(authToken, combinedUserBean.getSearchType(), combinedUserBean,
//				requestInstance);
//		if (customer.has("TotalResultsFound")) {
//			combinedResult.addParam(new Param("TotalResultsFound", customer.get("TotalResultsFound").toString(),
//					FabricConstants.STRING));
//			if (customer.has("customerbasicinfo_view")) {
//				JSONObject combinedUserJson = customer.getJSONObject("customerbasicinfo_view");
//				String SSN = combinedUserJson.optString("SSN");
//				combinedUserJson.put("SSN", CustomerHandler.maskSSN(SSN));
//				Record customerbasicinfo_view = CommonUtilities.constructRecordFromJSONObject(combinedUserJson);
//				customerbasicinfo_view.setId("customerbasicinfo_view");
//				combinedResult.addRecord(customerbasicinfo_view);
//				Record configuration = new Record();
//				configuration.setId("Configuration");
//				configuration.addParam(new Param("value", combinedUserJson.getString("accountLockoutTime")));
//				combinedResult.addRecord(configuration);
//			}
//			if (customer.has("records")) {
//
//				JSONArray recordsArray = customer.getJSONArray("records");
//				Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(recordsArray);
//				recordsDataset.setId("records");
//				Param recordsStatus = new Param("Status", "Records returned: " + recordsArray.length(),
//						FabricConstants.STRING);
//				combinedResult.addDataset(recordsDataset);
//				combinedResult.addParam(recordsStatus);
//			} else {
//				ErrorCodeEnum.ERR_20716.setErrorCode(combinedResult);
//				return combinedResult;
//
//			}
//		}
//		return combinedResult;
//
//	}

	public static JSONObject searchCustomers(String authToken, String searchType, MemberSearchBean memberSearchBean,
			DataControllerRequest requestInstance) {

		Map<String, String> searchPostParameters = new HashMap<String, String>();
		searchPostParameters.put("_searchType", searchType);
		searchPostParameters.put("_id", memberSearchBean.getMemberId());
		searchPostParameters.put("_name", memberSearchBean.getCustomerName());
		searchPostParameters.put("_username", memberSearchBean.getCustomerUsername());
		searchPostParameters.put("_SSN", memberSearchBean.getSsn());
		searchPostParameters.put("_phone", memberSearchBean.getCustomerPhone());
		searchPostParameters.put("_email", memberSearchBean.getCustomerEmail());
		searchPostParameters.put("_IsStaffMember", memberSearchBean.getIsStaffMember());
		searchPostParameters.put("_cardorAccountnumber", memberSearchBean.getCardorAccountnumber());
		searchPostParameters.put("_TIN", memberSearchBean.getTin());
		searchPostParameters.put("_group", memberSearchBean.getCustomerGroup());
		searchPostParameters.put("_IDType", memberSearchBean.getCustomerIDType());
		searchPostParameters.put("_IDValue", memberSearchBean.getCustomerIDValue());
		searchPostParameters.put("_companyId", memberSearchBean.getCustomerCompanyId());
		searchPostParameters.put("_requestID", memberSearchBean.getCustomerRequest());
		searchPostParameters.put("_branchIDS", memberSearchBean.getBranchIDS());
		searchPostParameters.put("_productIDS", memberSearchBean.getProductIDS());
		searchPostParameters.put("_cityIDS", memberSearchBean.getCityIDS());
		searchPostParameters.put("_entitlementIDS", memberSearchBean.getEntitlementIDS());
		searchPostParameters.put("_groupIDS", memberSearchBean.getGroupIDS());
		searchPostParameters.put("_customerStatus", memberSearchBean.getCustomerStatus());
		searchPostParameters.put("_before", memberSearchBean.getBeforeDate());
		searchPostParameters.put("_after", memberSearchBean.getAfterDate());
		searchPostParameters.put("_sortVariable", memberSearchBean.getSortVariable());
		searchPostParameters.put("_sortDirection", memberSearchBean.getSortDirection());
		searchPostParameters.put("_pageOffset", String.valueOf(memberSearchBean.getPageOffset()));
		searchPostParameters.put("_pageSize", String.valueOf(memberSearchBean.getPageSize()));

		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_SEARCH_PROC_SERVICE,
				searchPostParameters, null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public static Result CustomerSearchByUserName(DataControllerRequest requestInstance) {
		Result processedResult = new Result();
		try {

			// Sort validation
			if (StringUtils.isNotBlank(requestInstance.getParameter("_sortVariable"))
					&& requestInstance.getParameter("_sortVariable").contains(" ")) {
				ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
				return processedResult;
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter("_sortDirection"))
					&& (requestInstance.getParameter("_sortDirection").contains(" ")
							|| (!requestInstance.getParameter("_sortDirection").equalsIgnoreCase("ASC")
									&& !requestInstance.getParameter("_sortDirection").equalsIgnoreCase("DESC")))) {
				ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
				return processedResult;
			}
			MemberSearchBean memberSearchBean = new MemberSearchBean();
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			memberSearchBean.setSearchType(requestInstance.getParameter("_searchType"));
			memberSearchBean.setMemberId(requestInstance.getParameter("_id"));
			memberSearchBean.setCustomerName(requestInstance.getParameter("_name"));
			memberSearchBean.setSsn(requestInstance.getParameter("_SSN"));
			memberSearchBean.setCustomerUsername(requestInstance.getParameter("_username"));
			memberSearchBean.setCustomerPhone(requestInstance.getParameter("_phone"));
			memberSearchBean.setCustomerEmail(requestInstance.getParameter("_email"));
			memberSearchBean.setIsStaffMember(requestInstance.getParameter("_IsStaffMember"));
			memberSearchBean.setCardorAccountnumber(requestInstance.getParameter("_cardorAccountnumber"));
			memberSearchBean.setTin(requestInstance.getParameter("_TIN"));
			memberSearchBean.setCustomerGroup(requestInstance.getParameter("_group"));
			memberSearchBean.setCustomerIDType(requestInstance.getParameter("_IDType"));
			memberSearchBean.setCustomerIDValue(requestInstance.getParameter("_IDValue"));
			memberSearchBean.setCustomerCompanyId(requestInstance.getParameter("_companyId"));
			memberSearchBean.setCustomerRequest(requestInstance.getParameter("_requestID"));
			memberSearchBean.setBranchIDS(requestInstance.getParameter("_branchIDS"));
			memberSearchBean.setProductIDS(requestInstance.getParameter("_productIDS"));
			memberSearchBean.setCityIDS(requestInstance.getParameter("_cityIDS"));
			memberSearchBean.setEntitlementIDS(requestInstance.getParameter("_entitlementIDS"));
			memberSearchBean.setGroupIDS(requestInstance.getParameter("_groupIDS"));
			memberSearchBean.setCustomerStatus(requestInstance.getParameter("_customerStatus"));
			memberSearchBean.setBeforeDate(requestInstance.getParameter("_before"));
			memberSearchBean.setAfterDate(requestInstance.getParameter("_after"));
			memberSearchBean.setSortVariable(requestInstance.getParameter("_sortVariable"));
			memberSearchBean.setSortDirection(requestInstance.getParameter("_sortDirection"));
			memberSearchBean.setPageOffset(requestInstance.getParameter("_pageOffset"));
			memberSearchBean.setPageSize(requestInstance.getParameter("_pageSize"));

			if (StringUtils.isBlank(memberSearchBean.getSearchType())) {
				ErrorCodeEnum.ERR_20690.setErrorCode(processedResult);
				return processedResult;

			}

			processedResult
					.addParam(new Param("SortVariable", memberSearchBean.getSortVariable(), FabricConstants.STRING));
			processedResult
					.addParam(new Param("SortDirection", memberSearchBean.getSortDirection(), FabricConstants.STRING));
			processedResult.addParam(
					new Param("PageOffset", String.valueOf(memberSearchBean.getPageOffset()), FabricConstants.INT));
			processedResult.addParam(
					new Param("PageSize", String.valueOf(memberSearchBean.getPageSize()), FabricConstants.INT));

			if (memberSearchBean.getSearchType().equalsIgnoreCase(APPLICANT_SEARCH)) {
				processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
			}
			if (memberSearchBean.getSearchType().equalsIgnoreCase(GROUP_SEARCH)) {
				JSONObject searchResults = searchCustomers(authToken, GROUP_SEARCH_TOTAL_COUNT, memberSearchBean,
						requestInstance);
				if (searchResults.has("records") && ((JSONArray) searchResults.get("records")).length() > 0) {
					if (searchResults.getJSONArray("records").getJSONObject(0).has("SearchMatchs")) {
						processedResult.addParam(new Param("TotalResultsFound",
								searchResults.getJSONArray("records").getJSONObject(0).getString("SearchMatchs"),
								FabricConstants.INT));
					} else {
						processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
					}
				}
			}
			if (memberSearchBean.getSearchType().equalsIgnoreCase(CUSTOMER_SEARCH)) {
				JSONObject searchResults = searchCustomers(authToken, CUSTOMER_SEARCH_TOTAL_COUNT, memberSearchBean,
						requestInstance);
				if (searchResults.has("records") && ((JSONArray) searchResults.get("records")).length() > 0) {
					if (searchResults.getJSONArray("records").getJSONObject(0).has("SearchMatchs")) {
						processedResult.addParam(new Param("TotalResultsFound",
								searchResults.getJSONArray("records").getJSONObject(0).getString("SearchMatchs"),
								FabricConstants.INT));
					} else {
						processedResult.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
					}
				}
			}
			JSONObject customers = searchCustomers(authToken, memberSearchBean.getSearchType(), memberSearchBean,
					requestInstance);
			if (customers.has("customerbasicinfo_view")) {
				JSONObject customerViewJson = customers.getJSONObject("customerbasicinfo_view");
				Record customerbasicinfo_view = CommonUtilities.constructRecordFromJSONObject(customerViewJson);
				customerbasicinfo_view.setId("customerbasicinfo_view");
				processedResult.addRecord(customerbasicinfo_view);
				Record configuration = new Record();
				configuration.setId("Configuration");
				configuration.addParam(new Param("value", customerViewJson.getString("accountLockoutTime")));
				processedResult.addRecord(configuration);
			}
			if (customers.has("records")) {

				JSONArray recordsArray = customers.getJSONArray("records");
				Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(recordsArray);
				recordsDataset.setId("records");
				Param recordsStatus = new Param("Status", "Records returned: " + recordsArray.length(),
						FabricConstants.STRING);
				processedResult.addDataset(recordsDataset);
				processedResult.addParam(recordsStatus);
			} else {
				ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
				return processedResult;

			}

			return processedResult;
		} catch (Exception e) {
			alert.prepareError("Unexpected error", e).log();
			processedResult.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
			return processedResult;
		}
	}
}