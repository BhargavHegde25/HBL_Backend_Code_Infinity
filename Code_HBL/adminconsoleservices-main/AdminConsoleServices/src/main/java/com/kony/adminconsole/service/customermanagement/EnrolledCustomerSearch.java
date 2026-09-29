package com.kony.adminconsole.service.customermanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.dto.MemberSearchBean;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class EnrolledCustomerSearch implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		
		try {
			String id = requestInstance.getParameter("_id");
			if (StringUtils.isBlank(id)) {
				id ="";
			}
			
			String name = requestInstance.getParameter("_name");
			if (StringUtils.isBlank(name)) {
				name="";
			}
			String username = requestInstance.getParameter("_username");
			if (StringUtils.isBlank(username)) {
				username="";
			}
			
			if(StringUtils.isBlank(id) && StringUtils.isBlank(name) && StringUtils.isBlank(username)){
				return JSONToResult.convert(new JSONObject().put("customers", new JSONArray()).toString());
			}
			
			
			
			MemberSearchBean memberSearchBean = new MemberSearchBean();
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			memberSearchBean.setMemberId(id);
			memberSearchBean.setCustomerName(name);
			memberSearchBean.setCustomerUsername(username);
			memberSearchBean.setSortVariable(requestInstance.getParameter("_sortVariable"));
			memberSearchBean.setSortDirection(requestInstance.getParameter("_sortDirection"));
			memberSearchBean.setPageOffset(requestInstance.getParameter("_pageOffset"));
			memberSearchBean.setPageSize(requestInstance.getParameter("_pageSize"));
			
			result.addParam(new Param("SortVariable", memberSearchBean.getSortVariable(), FabricConstants.STRING));
			result.addParam(new Param("SortDirection", memberSearchBean.getSortDirection(), FabricConstants.STRING));
			result.addParam(new Param("PageOffset", String.valueOf(memberSearchBean.getPageOffset()), FabricConstants.INT));
			result.addParam(new Param("PageSize", String.valueOf(memberSearchBean.getPageSize()), FabricConstants.INT));
			
			JSONObject customers = null;
			customers = DBPServices.searchEnrolledCustomers(authToken, memberSearchBean.getSearchType(), memberSearchBean,
					requestInstance);
			if((!customers.has("customers")) || (StringUtils.isBlank(customers.getString("customers"))))
			{
				result=searchCustomerWithBackendId(id,customers,memberSearchBean,authToken,requestInstance,result);
				return result;
			}
			result.addParam(new Param("customers", customers.getString("customers"), FabricConstants.STRING));
			return result;
			
		}
		catch(Exception e) {
			alert.prepareError("Unexpected error", e).log();
			
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED,
                    "Failed to search customer detail: " + e.getMessage());
			
			result.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20716.setErrorCode(result);
			return result;
		}
	}
	public Result searchCustomerWithBackendId(String id,JSONObject customers,MemberSearchBean memberSearchBean,String authToken,
			DataControllerRequest requestInstance,Result result) throws DBPAuthenticationException
	{
		if(StringUtils.isBlank(id))
		{
			return JSONToResult.convert(new JSONObject().put("customers", new JSONArray()).toString());
		}
		JSONObject backendidentifier=DBPServices.searchCustomerWithBackendId(id);
		if(backendidentifier==null||!backendidentifier.has(FabricConstants.OPSTATUS)
                 || backendidentifier.getInt(FabricConstants.OPSTATUS) != 0 )
		{
		alert.prepareError("Failed to fetch backendidentifer data ").log();
		ErrorCodeEnum.ERR_22205.setErrorCode(result);
		return result;
		}
		if(backendidentifier.getJSONArray("backendidentifier").length()<1)
		{
			alert.prepareError("No record Found in BackendIdentifier Table").log();
			return JSONToResult.convert(new JSONObject().put("customers", new JSONArray()).toString());

		}
		id=backendidentifier.optJSONArray("backendidentifier").optJSONObject(0).optString("Customer_id");
		memberSearchBean.setMemberId(id);
		customers= DBPServices.searchEnrolledCustomers(authToken, memberSearchBean.getSearchType(), memberSearchBean,
				requestInstance);
		if((!customers.has("customers")) || (customers.getString("customers")== null))
		{
			alert.prepareError("Failed to fetch record in customer table with given backendId").log();
			return JSONToResult.convert(new JSONObject().put("customers", new JSONArray()).toString());
		}
		result.addParam(new Param("customers",customers.getString("customers"),FabricConstants.STRING));;
		return result;
		
	}

}
