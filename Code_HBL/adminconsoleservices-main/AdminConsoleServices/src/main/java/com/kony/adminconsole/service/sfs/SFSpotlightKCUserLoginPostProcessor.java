package com.kony.adminconsole.service.sfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.dto.Permission;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.adminconsole.handler.RoleHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import net.minidev.json.JSONArray;

public class SFSpotlightKCUserLoginPostProcessor implements DataPostProcessor2 {

	private static final String ROLES_PARAM = "roles";
	public static final String ROLE_ID_KEY = "roleId";
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		Record user_attributes = result.getRecordById("user_attributes");
		String roleIds = "";
		if(user_attributes.hasParamByName(ROLES_PARAM)){
            JSONArray roles = (JSONArray) (user_attributes.getParamByName(ROLES_PARAM).getObjectValue());
            String roleNames=StringUtils.join(roles.toArray(), ',');
            
            roleIds = getRoleIds(roleNames, request);
            result.getRecordById("user_attributes").addParam(new Param(ROLE_ID_KEY, roleIds, FabricConstants.STRING));
            result.getRecordById("user_attributes").removeParamByName(ROLES_PARAM);
		}

		Record security_attributes = new Record();
		security_attributes.setId("security_attributes");
	    security_attributes.addParam("access_token", request.getParameter("access_token"));
	    security_attributes.addParam("session_token", request.getParameter("access_token"));
	    security_attributes.addParam("refresh_token", request.getParameter("refresh_token"));
	    
	    if (StringUtils.isNotBlank(roleIds)) {
			List<Permission> permissionsList = PermissionHandler.getRolesGrantedPermissions(roleIds, request);
			org.json.JSONArray permissionsArray = new org.json.JSONArray();
			if (permissionsList != null && !permissionsList.isEmpty()) {
				for (Permission permission : permissionsList) {
					permissionsArray.put(permission.getName());
				}
			}
			security_attributes.addParam(new Param(DBPConstants.PERMISSIONS_IDENTITY_KEY,
					permissionsArray.toString(), FabricConstants.STRING));							
		}
	    
	    result.addRecord(security_attributes);
	    return result;
	}

	private String getRoleIds(String roles, DataControllerRequest requestInstance)
			throws ApplicationException, IOException {
		String roleIds = null;
		if (StringUtils.isNotBlank(roles)) {
			String[] roleNames = roles.split(",");
			List<String> ids = new ArrayList<String>();
			for (String name : roleNames) {
				String roleId = RoleHandler.getRoleIdFromName(name, requestInstance);
				if (roleId != null)
					ids.add(roleId);
			}
			if (ids.size() > 0) {
				roleIds = StringUtils.join(ids.toArray(), ',');
			}
		}
		return roleIds;
	}
}
