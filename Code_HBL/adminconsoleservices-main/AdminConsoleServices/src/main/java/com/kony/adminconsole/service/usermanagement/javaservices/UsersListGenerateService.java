package com.kony.adminconsole.service.usermanagement.javaservices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UsersListGenerateService implements JavaService2{

	    @Override
	    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
	            DataControllerResponse responseInstance) throws Exception{
		Log4j2Configurator.getInstance();
	    	Result result =  new Result();
	    	try {
		 String searchText = requestInstance.getParameter("searchText");
		 String role = requestInstance.getParameter("role");
		 String status = requestInstance.getParameter("status");
		 String createdStartDate = requestInstance.containsKeyInRequest("createdStartDate")?requestInstance.getParameter("createdStartDate"):null;
		 String createdEndDate = requestInstance.containsKeyInRequest("createdEndDate")?requestInstance.getParameter("createdEndDate"):null;
		 String updatedStartDate = requestInstance.containsKeyInRequest("updatedStartDate")?requestInstance.getParameter("updatedStartDate"):null;
		 String updatedEndDate = requestInstance.containsKeyInRequest("updatedEndDate")?requestInstance.getParameter("updatedEndDate"):null;
		 boolean isKeyCloakEnabled = Boolean
					.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance));
		 if (isKeyCloakEnabled) {
				String readInternalUsersViewResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_USERS,
						new HashMap<String, String>(), null, requestInstance);
				if (readInternalUsersViewResponse == null) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20000);
				}
				JSONObject readInternalUsersViewResponseJSON = CommonUtilities
						.getStringAsJSONObject(readInternalUsersViewResponse);
				JSONArray internalUsers = new JSONArray();
				if (readInternalUsersViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readInternalUsersViewResponseJSON.getJSONArray("internalusers_view") != null) {
					 internalUsers = readInternalUsersViewResponseJSON.getJSONArray("internalusers_view");
				}
				 String fileId = Integer.toString(CommonUtilities.generateRandomWithRange(100000, 999999));
		            result.addParam("fileId", fileId);
		            MemoryManager.saveIntoCache(fileId, internalUsers.toString());
		            MemoryManager.saveIntoCache("searchText", searchText);
		 }else {
			 Map<String, String> internalUsersViewMap = new HashMap<String, String>();
				internalUsersViewMap.put(ODataQueryConstants.SELECT,
						"FirstName, LastName, Username, Email, Role_Name, Permission_Count, Status_Desc");

				StringBuilder filterString = new StringBuilder();

				if (!StringUtils.isNotBlank(searchText) && !StringUtils.isNotBlank(status)) {
					filterString.append("(Status_Desc eq 'Active' or Status_Desc eq 'Suspended')");
				} else {
					String[] statuses = status.split("_");
					filterString.append("(");
					for (int i = 0; i < statuses.length - 1; ++i) {
						if (statuses[i].equals("Disabled")) {
							filterString.append("Status_Desc eq 'Inactive'");
						} else {
							filterString.append("Status_Desc eq '" + statuses[i] + "'");
						}
						filterString.append(" or ");
					}
					if (statuses[statuses.length - 1].equals("Disabled")) {
						filterString.append("Status_Desc eq 'Inactive')");
					} else {
						filterString.append("Status_Desc eq '" + statuses[statuses.length - 1] + "')");
					}
				}

				if (StringUtils.isNotBlank(role)) {
					if (filterString != null) {
						filterString.append(" and ");
					}
					String[] roles = role.split("_");
					filterString.append("(");
					for (int i = 0; i < roles.length - 1; ++i) {
						filterString.append("Role_Name eq '" + roles[i] + "'");
						filterString.append(" or ");
					}
					filterString.append("Role_Name eq '" + roles[roles.length - 1] + "')");
				}
				if (StringUtils.isNotBlank(createdStartDate) && StringUtils.isNotBlank(createdEndDate)) {
					if (filterString != null) {
						filterString.append(" and ");
					}
					filterString.append("createdts ge '" + getStartDateInOAuthFormat(createdStartDate)
							+ "' and createdts le '" + getEndDateInOAuthFormat(createdEndDate) + "'");
				}
				if (StringUtils.isNotBlank(updatedStartDate) && StringUtils.isNotBlank(updatedEndDate)) {
					if (filterString != null) {
						filterString.append(" and ");
					}
					filterString.append("lastmodifiedts ge '" + getStartDateInOAuthFormat(updatedStartDate)
							+ "' and lastmodifiedts le '" + getEndDateInOAuthFormat(updatedEndDate) + "'");
				}

				if (filterString != null) {
					internalUsersViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
				}
				String readInternalUsersViewResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERS_VIEW_READ,
						internalUsersViewMap, null, requestInstance);
				if (readInternalUsersViewResponse == null) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20000);
				}
				JSONObject readInternalUsersViewResponseJSON = CommonUtilities
						.getStringAsJSONObject(readInternalUsersViewResponse);
				JSONArray internalUsers = new JSONArray();
				if (readInternalUsersViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readInternalUsersViewResponseJSON.getJSONArray("internalusers_view") != null) {
					internalUsers  = readInternalUsersViewResponseJSON.getJSONArray("internalusers_view");
				}
				String fileId = Integer.toString(CommonUtilities.generateRandomWithRange(100000, 999999));
	            result.addParam("fileId", fileId);
	            MemoryManager.saveIntoCache(fileId, internalUsers.toString(),60);
	            MemoryManager.saveIntoCache("searchText", searchText);
		 }
	    	}catch(Exception e) {
				 
				ErrorCodeEnum.ERR_20687.setErrorCode(result);
		        String errorMessage = "Failed to download locations list. Please contact administrator.";
		        CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
			}
		 return result;
		 
	 }
	 public static String getStartDateInOAuthFormat(String inputDateString) throws ParseException {
			DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat oauthFormat = new SimpleDateFormat("yyyy-MM-dd");

			return oauthFormat.format(originalFormat.parse(inputDateString)) + "T00:00:00";
		}

		public static String getEndDateInOAuthFormat(String inputDateString) throws ParseException {
			DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
			DateFormat oauthFormat = new SimpleDateFormat("yyyy-MM-dd");

			return oauthFormat.format(originalFormat.parse(inputDateString)) + "T23:59:59";
		}
}
