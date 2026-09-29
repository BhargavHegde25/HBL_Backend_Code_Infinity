package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;
import com.temenos.infinity.api.arrangements.resource.api.UserManagementResource;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
/**
 * 
 * @author harishs
 * @version Java Service to update Account Details in Profile Settings in order
 *          management micro services
 * 
 */

public class UpdateUserAccountSettingsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			UserManagementResource updateUserAccountResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
			UserAccountSettingsDTO userAccount = constructPayLoad(request);
			String companyId = CommonUtils.getCompanyId(request);
			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
			headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));
			headerMap.put("companyid", companyId);
			request.addRequestParam_("companyid", companyId);
			Result result = updateUserAccountResource.updateAccountDetails(userAccount, request,headerMap);
			return result;
		} catch (Exception e) {
			alert.prepareError("Unable to create order : " + e).log();
			return ErrorCodeEnum.ERR_20058.setErrorCode(new Result());
		}
	}
	
	public static UserAccountSettingsDTO constructPayLoad(DataControllerRequest request) {
		UserAccountSettingsDTO userAccount = new UserAccountSettingsDTO();
		String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
		String nickName = request.getParameter("nickName") != null ? request.getParameter("nickName") : "";
		String favouriteStatus = request.getParameter("favouriteStatus") != null
				? request.getParameter("favouriteStatus")
				: "";
		String eStatementEnable = request.getParameter("eStatementEnable") != null
				? request.getParameter("eStatementEnable")
				: "";
		String email = request.getParameter("email") != null ? request.getParameter("email") : "";

		userAccount.setEmail(email);
		userAccount.seteStatementEnable(eStatementEnable);
		userAccount.setFavouriteStatus(favouriteStatus);
		userAccount.setNickName(nickName);
		userAccount.setAccountID(accountId);
		return userAccount;
	}
}
