package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SpotlightUserDetailsFromTokenPreProcessor implements DataPreProcessor2 {

	@Override
	public boolean execute(HashMap inputArray, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		
		/**
		 * Additional attributes (if needed with different name) should be added via
		 * code.
		 */

		Map<String, Object> userAttributes = request.getServicesManager().getIdentityHandler().getUserAttributes();
		String username = (String) userAttributes.get("username");
		String user_id = (String) userAttributes.get("user_id");
		String customerTypeId = (String) userAttributes.get("customerTypeId");
		String UserRole = (String) userAttributes.get("UserRole");
		String roleId = (String) userAttributes.get("roleId");
		String FirstName = (String) userAttributes.get("FirstName");
		String LastName = (String) userAttributes.get("LastName");
		String Email = (String) userAttributes.get("Email");
		
		result.addParam(new Param("username", username, FabricConstants.STRING));
		result.addParam(new Param("user_id", user_id, FabricConstants.STRING));
		result.addParam(new Param("customerTypeId", customerTypeId, FabricConstants.STRING));
		result.addParam(new Param("UserRole", UserRole, FabricConstants.STRING));
		result.addParam(new Param("roleId", roleId, FabricConstants.STRING));
		result.addParam(new Param("FirstName", FirstName, FabricConstants.STRING));
		result.addParam(new Param("LastName", LastName, FabricConstants.STRING));
		result.addParam(new Param("Email", Email, FabricConstants.STRING));

		return false;
		
	}

}
