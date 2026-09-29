package com.kony.adminconsole.keycloakspotlighteventhandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONObject;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;

import com.kony.adminconsole.utilities.DBXResult;
import com.kony.adminconsole.utilities.HTTPOperationsForKeycloak;
import com.kony.adminconsole.utilities.SessionHandler;

public class KeycloakEventListenerProvider implements EventListenerProvider{
 
	private static final Properties PROPS = SessionHandler.loadProps();
	
	private static final String DELETE_SESSION_EXP_API = "delete_session_exp_api";
	private static final String CLOS_USER_CREATE = "clos_user_create_exp_api";
	private static final String CLOS_ROLE_MAPPING = "clos_role_mapping_exp_api";
	private static final String OPERATION_TYPE = "operationType";
    private static final String REALM_ID = "realmId";
    private static final String CLIENT_ID = "clientId";
    private static final String USER_ID = "userId";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String RESOURCE_PATH= "resourcePath";
    private static final String GET_REPRESENTATION = "getRepresentation";
    private static final String GET_RESOURCE_TYPE_AS_STRING = "getResourceTypeAsString";
    private static final String TYPE = "type";
    private static final String ERROR = "error";
	
	@Override
	public void onEvent(Event event) {
		
	}

	@Override
	public void onEvent(AdminEvent event, boolean includeRepresentation) {
		
		String userId= "";
		boolean isLogoutSuccessful = false;
		
		String eventOperationType = event.getOperationType().toString();
		
		if(eventOperationType.equals(OperationType.CREATE.toString()) 
				|| eventOperationType.equals(OperationType.DELETE.toString()) 
				|| eventOperationType.equals(OperationType.UPDATE.toString())
				|| eventOperationType.equals(OperationType.ACTION.toString())){
			if(event.getResourcePath()!=null && event.getResourcePath().startsWith("users/")) {
				if(event.getResourcePath().contains("/role-mappings/") ) {
					System.out.println("Role modification happened for the user: "+ userId );
					userId= event.getResourcePath().toString().split("/")[1];					
					createCLOSRole(event,userId);
					isLogoutSuccessful = logoutUser(userId);
				}
				
				if(eventOperationType.equals(OperationType.UPDATE.toString())) {
					userId = event.getResourcePath().toString().split("/")[1];
					System.out.println("User details modified for the user: "+ userId);
					
					isLogoutSuccessful = logoutUser(userId);
				}
				
				if(eventOperationType.equals(OperationType.DELETE.toString())) {
					userId = event.getResourcePath().toString().split("/")[1];
					System.out.println("User deleted : "+ userId);
					
					isLogoutSuccessful = logoutUser(userId);
				}
				
				if(eventOperationType.equals(OperationType.ACTION.toString()) && event.getResourcePath().contains("/logout")) {
					userId = event.getResourcePath().toString().split("/")[1];
					System.out.println("Session killed for the user:"+ userId);
					
					isLogoutSuccessful = logoutUser(userId);
				}
				if (event.getOperationType().toString().equals(OperationType.CREATE.toString()) && event.getResourceTypeAsString().equals("USER")){
					System.out.println("Clos-User Creation: " + event);
					createCLOSUser(event); 
				}
          
			}
			if(eventOperationType.equals(OperationType.DELETE.toString()) && 
					event.getResourcePath().contains("sessions/") && event.getResourcePath()!=null) {
				userId = event.getResourcePath().toString().split("/")[1];
				System.out.println("Session killed for the user:"+ userId);
				
				isLogoutSuccessful = logoutUser(userId);
			}
			
			
		}
		System.out.println("Response for logout operation: " +isLogoutSuccessful );
	}

	private boolean logoutUser(String userId) {
		
		try {

		String apiToken = SessionHandler.getSpotlightAPILoginToken();
		
		DBXResult result = new DBXResult();
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("x-kony-authorization", apiToken);
		String url = PROPS.getProperty(DELETE_SESSION_EXP_API);
		
		JSONObject inputPayload = new JSONObject();
		inputPayload.put("userId", userId);
		
		result = HTTPOperationsForKeycloak.sendHttpRequest(HTTPOperationsForKeycloak.operations.POST,url, inputPayload.toString(), headerMap);
		
		JSONObject resultJsonObject = new JSONObject(result.getResponse().toString());
		
		return resultJsonObject.getBoolean("success");
		}
		catch(Exception e)
		{
			return false;
		}
	}


	private void createCLOSUser(AdminEvent event) {
		try {
			String apiToken = SessionHandler.getSpotlightAPILoginToken();
			DBXResult result = new DBXResult();
			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("x-kony-authorization", apiToken);
			String url = PROPS.getProperty(CLOS_USER_CREATE);
			JSONObject inputPayload = new JSONObject();
			JSONObject userPayload = new JSONObject(event.getRepresentation());


			if (event.getResourcePath() != null && event.getResourcePath().toString().split("/")[1] != null) {
				inputPayload.put("userId", event.getResourcePath().toString().split("/")[1]);
				inputPayload.put("username", userPayload.getString("username"));
				inputPayload.put("firstName", userPayload.getString("firstName"));
				inputPayload.put("lastName", userPayload.getString("lastName"));
				inputPayload.put("email", userPayload.getString("email"));
				System.out.println("Clos user created:" + inputPayload);
			}

			result =
			HTTPOperationsForKeycloak.sendHttpRequest(HTTPOperationsForKeycloak.operations.POST,
			url,
			inputPayload.toString(), headerMap);
		} catch (Exception exception) {
		}
	}


	private void createCLOSRole(AdminEvent event, String userId) {
		try {
			
			
			String apiToken = SessionHandler.getSpotlightAPILoginToken();
			DBXResult result = new DBXResult();
			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("x-kony-authorization", apiToken);
			String url = PROPS.getProperty(CLOS_ROLE_MAPPING);
			JSONObject inputPayload = new JSONObject();
			JSONArray jsonArray = new JSONArray(event.getRepresentation());
            // Assuming there is only one element in the array
            JSONObject jsonObject = jsonArray.getJSONObject(0);
			if ((event.getResourcePath() != null) && (event.getResourcePath().toString().split("/")[1] != null)) {
				inputPayload.put("RoleId", jsonObject.getString("id"));
				inputPayload.put("RoleName", jsonObject.getString("name"));
				inputPayload.put("UserId", userId);
				System.out.println("Clos Role Mapping created:" + inputPayload);
			}
			result =
			HTTPOperationsForKeycloak.sendHttpRequest(HTTPOperationsForKeycloak.operations.POST,
			url,
			inputPayload.toString(), headerMap);
		} catch (Exception exception) {
		}
	}

	@Override
	public void close() {
		
	}
	
	private String toString(Event event) {

		StringBuilder sb = new StringBuilder();
		
		sb.append(TYPE+"=").append(event.getType());
		sb.append(", "+REALM_ID+"=").append(event.getRealmId());
		sb.append(", "+CLIENT_ID+"=").append(event.getClientId());
		sb.append(", "+USER_ID+"=").append(event.getUserId());
		sb.append(", "+IP_ADDRESS+"=").append(event.getIpAddress());

		if (event.getError() != null) {
			sb.append(", "+ERROR+"=");
			sb.append(event.getError());
		}

		if (event.getDetails() != null) {
			for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
				sb.append(", ");
				sb.append(e.getKey());

				if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {
					sb.append("=");
					sb.append(e.getValue());

				} else {
					sb.append("='");
					sb.append(e.getValue());
					sb.append("'");
				}
			}
		}
		sb.append("--------event.toString()--------").append(event.toString());
		return sb.toString();

	}


    private String toString(AdminEvent adminEvent) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(OPERATION_TYPE+"=").append(adminEvent.getOperationType());
        sb.append(", "+REALM_ID+"=").append(adminEvent.getAuthDetails().getRealmId());
        sb.append(", "+ CLIENT_ID +"=").append(adminEvent.getAuthDetails().getClientId());
        sb.append(", "+ USER_ID +"=").append(adminEvent.getAuthDetails().getUserId());
        sb.append(", "+IP_ADDRESS+"=").append(adminEvent.getAuthDetails().getIpAddress());
        sb.append(", "+RESOURCE_PATH+"=").append(adminEvent.getResourcePath());
        sb.append(", "+GET_REPRESENTATION+"=").append(adminEvent.getRepresentation());
        sb.append(", "+GET_RESOURCE_TYPE_AS_STRING+"=").append(adminEvent.getResourceTypeAsString());

        if (adminEvent.getError() != null) {
            sb.append(", "+ERROR+"=");
            sb.append(adminEvent.getError());
        }
        
        sb.append("--------adminEvent.toString()--------").append(adminEvent.toString());
        return sb.toString();
    }

}
