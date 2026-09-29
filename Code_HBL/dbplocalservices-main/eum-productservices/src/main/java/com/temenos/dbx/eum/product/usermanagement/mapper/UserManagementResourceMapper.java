package com.temenos.dbx.eum.product.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.ProfileManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.ProfileManagementResourceImpl;

public class UserManagementResourceMapper implements DBPAPIMapper<Resource> {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(ProfileManagementResource.class, ProfileManagementResourceImpl.class);
		return map;
	}
}
