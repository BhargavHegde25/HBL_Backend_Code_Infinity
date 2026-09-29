package com.kony.adminconsole.postprocessor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.RoleHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public class FetchKeycloakUserRolePostProcessor  implements DataPostProcessor2 {

	private static final String ROLES_PARAM = "roles";
	private static final String ACCESS_TOKEN_PARAM = "access_token";
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		JSONArray roles = new JSONArray();
		if(result.hasParamByName(ROLES_PARAM)) {
			String resp = result.getParamByName(ROLES_PARAM).getObjectValue().toString();
			roles = new JSONArray(resp);
		}
 
		StringJoiner names=new StringJoiner(",");
		if(null != roles && roles.length() > 0) {
			for(int i=0 ; i<roles.length() ; i++) {
				JSONObject jsonObject= roles.getJSONObject(i);
				String name = jsonObject.getString("name");
				if(StringUtils.isNotBlank(name)) {
					names.add(name);
				}
			}
		}
        Map<String,String> roleIdMap = getRoleIdMap(names.toString(), request);
		
        StringJoiner roleName = new StringJoiner(",");
        StringJoiner roleIds = new StringJoiner(",");
        
        for(Map.Entry<String, String> entry : roleIdMap.entrySet() ) {
        	
        	roleName.add(entry.getKey());
        	roleIds.add(entry.getValue());
        }
		
        if(result.hasParamByName(ROLES_PARAM)) {
        	result.removeParamByName(ROLES_PARAM);
        }
		
		if(result.hasParamByName(ACCESS_TOKEN_PARAM)) {
			result.removeParamByName(ACCESS_TOKEN_PARAM);
		}		
		
		result.addParam("roleNames", roleName.toString());
		result.addParam("roleIds", roleIds.toString());
		
		return result;
	}
	
	private Map<String,String> getRoleIdMap(String roles, DataControllerRequest requestInstance)
			throws ApplicationException, IOException {
		Map<String,String> roleIdMap = new LinkedHashMap<>();
		if (StringUtils.isNotBlank(roles)) {
			String[] roleNames = roles.split(",");
			
			for (String name : roleNames) {
				String roleId = RoleHandler.getRoleIdFromName(name, requestInstance);
				if (roleId != null)
					roleIdMap.put(name, roleId);
			}
		}
		return roleIdMap;
	}

}
