package com.kony.adminconsole.core.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.delegate.ServicePermissionsBusinessDelegate;
import com.dbp.core.dto.ServicePermissionsDTO;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;

/**
 * Class to register the Customer360 Service - Permission Mapping
 * 
 * @author Aditya Mankal
 *
 */
public class ServicePermissionMapRegister implements ServicePermissionsBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");



	@Override
	public List<ServicePermissionsDTO> getServicePermissionsMappings() {
		// TODO Auto-generated method stub
        try {

            List<ServicePermissionsDTO> fabricServicePermissions = new ArrayList<>();

            // Fetch Fabric Services
            String serviceResponse = Executor.invokeService(ServiceURLEnum.SERVICE_PERMISSION_MAPPER_READ,
                    new HashMap<String, String>(), new HashMap<String, String>(), StringUtils.EMPTY);

            // Parse Service Response
            JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
            if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                    || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                alert.prepareError("Failed to fetch Fabric Service Permission mappings. Service Response:" + serviceResponse).log();
                return null;
            }
            diagnostic.prepareDebug("Fetched Fabric Service Permission mappings").log();

            JSONArray servicePermissionsArray = serviceResponseJSON.optJSONArray("service_permission_mapper");
            if (servicePermissionsArray != null) {

                diagnostic.prepareDebug("Count of Records:" + servicePermissionsArray.length()).log();

                JSONObject currJSON = null;
                List<String> permissionsList = new ArrayList<>();
                ServicePermissionsDTO fabricServicePermission = null;
                String id, serviceName, objectName, operationName, permissions;

                // Traverse servicePermissionsArray and construct FabricServicePermissionsDTO
                // instances
                for (Object currObject : servicePermissionsArray) {
                    if (currObject instanceof JSONObject) {
                        currJSON = (JSONObject) currObject;

                        id = currJSON.optString("id");
                        serviceName = currJSON.optString("service_name");
                        objectName = currJSON.optString("object_name");
                        operationName = currJSON.optString("operation");
                        permissions = currJSON.optString("permissions");
                        if (StringUtils.isNotBlank(permissions)) {
                            permissionsList = Arrays.asList(permissions.split(","));
                        }
                        fabricServicePermission = new ServicePermissionsDTO(id, serviceName, objectName,
                                operationName, permissionsList);

                        // Add current mapping to list
                        fabricServicePermissions.add(fabricServicePermission);
                    }
                }
            }

            // Return Service Permission Mapping
            diagnostic.prepareDebug("Returning Service Permission map").log();
            return fabricServicePermissions;

        } catch (Exception e) {
            alert.prepareError("Exception in registering service-permission mapping. Exception:", e).log();
            return null;
        }
	}

}
