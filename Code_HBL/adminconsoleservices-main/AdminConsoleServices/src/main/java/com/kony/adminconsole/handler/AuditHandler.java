package com.kony.adminconsole.handler;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.commons.utils.http.HTTPOperations;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class AuditHandler {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    public static final String LOG_SERVICES_API_ACCESS_TOKEN_HEADER = "X-Kony-Log-Services-API-Access-Token";

    public static void auditAdminActivity(DataControllerRequest requestInstance, ModuleNameEnum moduleName,
            EventEnum event, ActivityStatusEnum status, String description) {
        try {
            if (requestInstance.getAttribute("isServiceBeingAccessedByOLB") != null
                    && Boolean.parseBoolean(requestInstance.getAttribute("isServiceBeingAccessedByOLB").toString()))
                return;

            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            ThreadExecutor.execute(
					()-> processInformation("AdminActivity",userDetailsBeanInstance.getUserName(),userDetailsBeanInstance.getRoleName(),
		            		moduleName.getModuleNameAlias(),event.getEventNameAlias(),
		            		status.getStatusAlias(),description,CommonUtilities.getISOFormattedLocalTimestamp(),null,requestInstance,null)
					);            
            
        }catch (InterruptedException e) {			
        	alert.prepareError("Error occurred: ", e).log();
		}catch (Exception e) {
            alert.prepareError("AdminConsole: Error occured while pushing log statement!", e).log();
            return;
        }

    }

    public static void auditAdminActivity(DataControllerRequest requestInstance, String moduleName,
                                          EventEnum event, ActivityStatusEnum status, String description) {
        try {
            if (requestInstance.getAttribute("isServiceBeingAccessedByOLB") != null
                    && Boolean.parseBoolean(requestInstance.getAttribute("isServiceBeingAccessedByOLB").toString()))
                return;

            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            ThreadExecutor.execute(
                    ()-> processInformation("AdminActivity",userDetailsBeanInstance.getUserName(),userDetailsBeanInstance.getRoleName(),
                            moduleName, event.getEventNameAlias(),
                            status.getStatusAlias(),description,CommonUtilities.getISOFormattedLocalTimestamp(),null,requestInstance,null)
            );

        }catch (InterruptedException e) {
        	alert.prepareError("Error occurred: ", e).log();
        }catch (Exception e) {
            alert.prepareError("AdminConsole: Error occured while pushing log statement!", e).log();
            return;
        }

    }
    
	public static void auditAdminActivity(DataControllerRequest requestInstance, String moduleName, String event,
			ActivityStatusEnum status, String description) {
		try {
			if (requestInstance.getAttribute("isServiceBeingAccessedByOLB") != null
					&& Boolean.parseBoolean(requestInstance.getAttribute("isServiceBeingAccessedByOLB").toString()))
				return;

			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			ThreadExecutor.execute(() -> processInformation("AdminActivity", userDetailsBeanInstance.getUserName(),
					userDetailsBeanInstance.getRoleName(), moduleName, event,
					status.getStatusAlias(), description, CommonUtilities.getISOFormattedLocalTimestamp(), null,
					requestInstance, null));

		} catch (InterruptedException e) {
			alert.prepareError("Error occurred: ", e).log();
		} catch (Exception e) {
			alert.prepareError("AdminConsole: Error occured while pushing log statement!", e).log();
			return;
		}

	}

    public static void auditAdminActivity(DataControllerRequest requestInstance, String username, String userRole,
            ModuleNameEnum moduleName, EventEnum event, ActivityStatusEnum status, String description) {

        try {
            if (requestInstance.getAttribute("isServiceBeingAccessedByOLB") != null
                    && Boolean.parseBoolean(requestInstance.getAttribute("isServiceBeingAccessedByOLB").toString()))
                return ;
            ThreadExecutor.execute(
					()-> processInformation("AdminActivity",username,userRole,moduleName.getModuleNameAlias(),event.getEventNameAlias(),
            		status.getStatusAlias(),description,CommonUtilities.getISOFormattedLocalTimestamp(),null,requestInstance,null)
					);
        }catch (InterruptedException e) {			
        	alert.prepareError("Error occurred: ", e).log();
		}catch (Exception e) {
            alert.prepareError("AdminConsole: Error occured while pushing log statement!", e).log();
            return ;
        }
    }
    
    public static void auditAdminActivity(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager, String user, String userRole,
            ModuleNameEnum moduleName, EventEnum event, ActivityStatusEnum status, String description) {

        try {
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(fabricReqManager.getServicesManager());
            ThreadExecutor.execute(
					()-> processInformation("AdminActivity",user==null ? userDetailsBeanInstance.getUserName() : user,userRole==null? userDetailsBeanInstance.getRoleName():userRole,moduleName.getModuleNameAlias(),event.getEventNameAlias(),
            		status.getStatusAlias(),description,CommonUtilities.getISOFormattedLocalTimestamp(),constructEventJsonDataLog(fabricReqManager,fabricResManager),null,fabricReqManager.getServicesManager())
					);
        }catch (InterruptedException e) {			
        	alert.prepareError("Error occurred: ", e).log();
		}catch (Exception e) {
            alert.prepareError("AdminConsole: Error occured while pushing log statement!", e).log();
            return;
        }
    }
    private static String processInformation(String logType,String username,String userRole,String moduleName,String event,
    		String status,String description,String eventts,JsonObject eventData,DataControllerRequest requestInstance,ServicesManager servicesManager) {
    	JSONObject auditInformation = new JSONObject();
        auditInformation.put("logType", logType);
        auditInformation.put("username", username);
        auditInformation.put("userRole", userRole);
        auditInformation.put("moduleName", moduleName);
        auditInformation.put("event", event);
        auditInformation.put("status", status);
        auditInformation.put("description", description);
        auditInformation.put("eventts", eventts);
        auditInformation.put("eventData", eventData);
        if(requestInstance==null)
        	return auditLog(servicesManager, auditInformation);
        else
        	return auditLog(requestInstance, auditInformation);
    }

    private static String auditLog(ServicesManager servicesManager, JSONObject auditInformation) {
        Map<String, String> headerParametersMap = new HashMap<String, String>();
        headerParametersMap.put(LOG_SERVICES_API_ACCESS_TOKEN_HEADER,
                EnvironmentConfiguration.AC_LOG_SERVICES_API_ACCESS_TOKEN.getValue(servicesManager));
        String auditActionResponse = HTTPOperations.hitPOSTServiceAndGetResponse(
                ServiceURLEnum.LOGMANAGEMENT_AUDITLOG.getServiceURL(servicesManager), auditInformation, null,
                headerParametersMap);

        return auditActionResponse;
    }
    
    private static String auditLog(DataControllerRequest requestInstance, JSONObject auditInformation) {
        Map<String, String> headerParametersMap = new HashMap<String, String>();
        headerParametersMap.put(LOG_SERVICES_API_ACCESS_TOKEN_HEADER,
                EnvironmentConfiguration.AC_LOG_SERVICES_API_ACCESS_TOKEN.getValue(requestInstance));
        String auditActionResponse = HTTPOperations.hitPOSTServiceAndGetResponse(
                ServiceURLEnum.LOGMANAGEMENT_AUDITLOG.getServiceURL(requestInstance), auditInformation, null,
                headerParametersMap);

        return auditActionResponse;
    }
    
    private static JsonObject constructEventJsonDataLog(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager) {

        JsonObject request = (JsonObject) fabricReqManager.getPayloadHandler().getPayloadAsJson();
        JsonObject response = (JsonObject) fabricResManager.getPayloadHandler().getPayloadAsJson();
        JsonObject eventLog = new JsonObject();
        eventLog.add("request", request);
        eventLog.add("response", response);
    	return eventLog;
    }
    
    public static void auditAdminActivity(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager, String user, String userRole,
            String moduleName, String event, ActivityStatusEnum status, String description) {

        try {
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(fabricReqManager.getServicesManager());
            ThreadExecutor.execute(
					()-> processInformation("AdminActivity",user==null ? userDetailsBeanInstance.getUserName() : user,userRole==null? userDetailsBeanInstance.getRoleName():userRole,moduleName,event,
            		status.getStatusAlias(),description,CommonUtilities.getISOFormattedLocalTimestamp(),constructEventJsonDataLog(fabricReqManager,fabricResManager),null,fabricReqManager.getServicesManager())
					);
        }catch (InterruptedException e) {			
        	alert.prepareError("Error occurred: ", e).log();
		}catch (Exception e) {
            alert.prepareError("AdminConsole: Error occured while pushing log statement!", e).log();
            return;
        }
    }

}
