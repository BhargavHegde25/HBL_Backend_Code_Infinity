package com.kony.adminconsole.postprocessor;

import static com.kony.adminconsole.utilities.ACConstants.CREATE_CONTRACT;
import static com.kony.adminconsole.utilities.ACConstants.CREATE_CUSTOMER_ROLE;
import static com.kony.adminconsole.utilities.ACConstants.CREATE_INFINITY_USER;
import static com.kony.adminconsole.utilities.ACConstants.CREATE_SERVICE_DEFINITION;
import static com.kony.adminconsole.utilities.ACConstants.CUSTOMER_ROLE_ASSIGN;
import static com.kony.adminconsole.utilities.ACConstants.CUSTOMER_ROLE_STATUS;
import static com.kony.adminconsole.utilities.ACConstants.DELETE_SERVICE_DEFINITION;
import static com.kony.adminconsole.utilities.ACConstants.EDIT_CONTRACT;
import static com.kony.adminconsole.utilities.ACConstants.EDIT_CUSTOMER_BASIC_INFO;
import static com.kony.adminconsole.utilities.ACConstants.EDIT_CUSTOMER_CONTACT;
import static com.kony.adminconsole.utilities.ACConstants.EDIT_CUSTOMER_ROLE;
import static com.kony.adminconsole.utilities.ACConstants.EDIT_INFINITY_USER;
import static com.kony.adminconsole.utilities.ACConstants.EDIT_SERVICE_DEFINITION;
import static com.kony.adminconsole.utilities.ACConstants.MANAGE_SERVICE_DEFINITION;
import static com.kony.adminconsole.utilities.ACConstants.UPDATE_DBP_USER_STATUS;
import static com.kony.adminconsole.utilities.ACConstants.UPDATE_DEFAULT_ROLE;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ServiceNameToEventMapper;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

/**
 * Postprocessor for logging events
 * 
 * @author Sri kavya Pitchika
 *
 */

public class AuditLogsPostProcessor implements ObjectServicePostProcessor {
	private static final Logger LOG = LogManager.getLogger(AuditLogsPostProcessor.class);
	private String merchant="";
	private String cardCategory="";
	private String cardType="";
	@Override
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
			throws Exception {
		Log4j2Configurator.getInstance();

		JsonObject response = (JsonObject) fabricResManager.getPayloadHandler().getPayloadAsJson();
		JsonObject reqPayload = (JsonObject) fabricReqManager.getPayloadHandler().getPayloadAsJson();
		String operationId = fabricReqManager.getServicesManager().getOperationData().getOperationId();
		LOG.debug("HBL:AuditLogsPostProcessor :operationId:"+operationId);
		LOG.debug("HBL:AuditLogsPostProcessor :reqPayload:"+reqPayload);
		ServiceNameToEventMapper sm = findParamsForCurrentOperation(operationId,reqPayload);
		ActivityStatusEnum status = ActivityStatusEnum.SUCCESSFUL;
		String statusText= ACConstants.SUCCESSFULLY;
		// If response has error code then status is unsuccessful otherwise successful
		if (response != null && !response.entrySet().isEmpty()) {
			Set<Map.Entry<String, JsonElement>> entries = response.entrySet();
			for (Map.Entry<String, JsonElement> entry : entries) {
				String key = entry.getKey();
				if (key.equals("dbpErrCode") && null!=entry.getValue() 
						&& StringUtils.isNotBlank(entry.getValue().toString())) {
					status = ActivityStatusEnum.FAILED;
					statusText = ACConstants.FAILED.toLowerCase();
					break;
				}
			}
		}
		UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler
				.getUserDetails(fabricReqManager.getServicesManager());
		if (sm != null) {
			String description;
			if(status==ActivityStatusEnum.SUCCESSFUL) {
				String eventText="";
				if(sm.getEventType()==EventEnum.CREATE) eventText=ACConstants.CREATED;
				if(sm.getEventType()==EventEnum.UPDATE) eventText=ACConstants.UPDATED;
				if(sm.getEventType()==EventEnum.DELETE) eventText=ACConstants.DELETED;
				if(sm.getEventType()==EventEnum.APPROVEREQUEST) eventText="request approved";
				if(sm.getEventType()==EventEnum.REJECTREQUEST) eventText="request rejected";
				if(sm.getEventType()==EventEnum.SETTLED) eventText="request settled";
				if(sm.getEventType()==EventEnum.APPROVEENROLL) eventText="approved";
				if(sm.getEventType()==EventEnum.REJECTENROLL) eventText="rejected";
				if(sm.getEventType()==EventEnum.MERCHANT_ACTIVATE) eventText="merchant id #"+merchant+" activated";
				if(sm.getEventType()==EventEnum.MERCHANT_DEACTIVATE) eventText="merchant id #"+merchant+" deactivated";
				if(sm.getEventType()==EventEnum.MERCHANT_UPDATE) eventText=merchant+" merchant updated";
				if(sm.getEventType()==EventEnum.MERCHANT_CREATE) eventText=merchant+" merchant created";
				if(sm.getEventType()==EventEnum.MERCHANT_FEE_DELETE) eventText="merchant id #"+merchant+" fee deleted";
				if(sm.getEventType()==EventEnum.CREATE_CARD_LIMITS) eventText=cardCategory+" of "+cardType+" limits created";
				if(sm.getEventType()==EventEnum.UPDATE_CARD_LIMITS) eventText=cardCategory+" of "+cardType+" limits updated";
				if(sm.getEventType()==EventEnum.DELETE_CARD_LIMITS) eventText="card id #"+cardCategory+" limits deleted";
				if(sm.getEventType()==EventEnum.DISABLE_THIRDPARTY_AUTH) eventText=merchant+" user third party authentication disabled";
				description = sm.getEvent().getModuleNameAlias() + " "+ eventText+" "
								+ statusText;
				
				if(sm.getEventType()==EventEnum.MERCHANT_ACTIVATE || sm.getEventType()==EventEnum.MERCHANT_DEACTIVATE || sm.getEventType()==EventEnum.MERCHANT_UPDATE ||
					sm.getEventType()==EventEnum.MERCHANT_CREATE || sm.getEventType()==EventEnum.MERCHANT_FEE_DELETE || sm.getEventType()==EventEnum.CREATE_CARD_LIMITS ||
					sm.getEventType()==EventEnum.UPDATE_CARD_LIMITS || sm.getEventType()==EventEnum.DELETE_CARD_LIMITS || sm.getEventType()==EventEnum.DISABLE_THIRDPARTY_AUTH) {
				description = eventText+" "+ statusText;
				}
			}
			else {
				description = sm.getEvent().getModuleNameAlias() + " "+ sm.getEventType().getEventNameAlias().toLowerCase()+" "
						+ statusText;
			}
			AuditHandler.auditAdminActivity(fabricReqManager, fabricResManager, userDetailsBeanInstance.getUserName(),
					userDetailsBeanInstance.getRoleName(), sm.getEvent(), sm.getEventType(), status,
					description);
		}
	}

	private ServiceNameToEventMapper findParamsForCurrentOperation(String operationId, JsonObject reqPayload) {
		String status=null;
		String request=reqPayload.has("merchantDetails")?reqPayload.getAsJsonArray("merchantDetails").toString():null;
		JSONObject merchantObj=null;
		JSONArray requestArray1 =null;
		try {
		if(request!=null) {
		 requestArray1 = new JSONArray(request);
		 merchantObj = requestArray1.getJSONObject(0);
		}
		if (operationId.equals(CREATE_SERVICE_DEFINITION)) {
			return ServiceNameToEventMapper.CREATE_SERVICE_DEFINITION;
		} else if (operationId.equals(UPDATE_DEFAULT_ROLE) || operationId.equals(MANAGE_SERVICE_DEFINITION)
				|| operationId.equals(EDIT_SERVICE_DEFINITION)) {
			return ServiceNameToEventMapper.UPDATE_SERVICE_DEFINITION;
		} else if (operationId.equals(DELETE_SERVICE_DEFINITION)) {
			return ServiceNameToEventMapper.DELETE_SERVICE_DEFINITION;
		} else if (operationId.equals(CREATE_CONTRACT)) {
			return ServiceNameToEventMapper.CREATE_CONTRACT;
		} else if (operationId.equals(EDIT_CONTRACT)) {
			return ServiceNameToEventMapper.EDIT_CONTRACT;
		} else if (operationId.equals(CREATE_CUSTOMER_ROLE)) {
			return ServiceNameToEventMapper.CREATE_CUSTOMER_ROLE;
		} else if (operationId.equals(EDIT_CUSTOMER_ROLE) || operationId.contentEquals(CUSTOMER_ROLE_ASSIGN)
				|| operationId.equals(CUSTOMER_ROLE_STATUS)) {
			return ServiceNameToEventMapper.EDIT_CUSTOMER_ROLE;
		} else if (operationId.equals(CREATE_INFINITY_USER)) {
			return ServiceNameToEventMapper.CREATE_INFINITY_USER;
		} else if (operationId.equals(EDIT_INFINITY_USER) || operationId.contentEquals(EDIT_CUSTOMER_CONTACT)
				|| operationId.equals(UPDATE_DBP_USER_STATUS) || operationId.equals(EDIT_CUSTOMER_BASIC_INFO)) {
			return ServiceNameToEventMapper.EDIT_INFINITY_USER;
		}
		else if (operationId.equals("updateContractStatus")) {
			status=reqPayload.has("statusId")?reqPayload.get("statusId").getAsString():null;
	    	if(status.equalsIgnoreCase("SID_CONTRACT_ACTIVE")) {
	    		return ServiceNameToEventMapper.APPROVE_ENROLL_REQ;
	    	}else if(status.equalsIgnoreCase("SID_CONTRACT_REJECTED")) {
	    		return ServiceNameToEventMapper.REJECT_ENROLL_REQ;
	    	}
		}
		else if (operationId.equals("TransPINReqApproveReject")) {
			status=reqPayload.has("status")?reqPayload.get("status").getAsString():null;
			if(status.equalsIgnoreCase("APPROVED"))
			return ServiceNameToEventMapper.APPROVE_RESET_TX_PIN;
			else if(status.equalsIgnoreCase("REJECTED"))
			return ServiceNameToEventMapper.REJECT_RESET_TX_PIN;
		}
		else if (operationId.equals("updateDisputeTransaction")) {
			status=reqPayload.has("disputeStatus")?reqPayload.get("disputeStatus").getAsString():null;
			if(status.equalsIgnoreCase("Settled"))
			return ServiceNameToEventMapper.DISPUTE_TRANS;
		}
		else if (operationId.equals("updateMerchantDetails")) {
			boolean updateStatusOnly=false;
			if(!merchantObj.has("limitsAndCharges")) {
				updateStatusOnly=true;
			}
			if(updateStatusOnly) {
				merchant=merchantObj.optString("code");
				status=merchantObj.has("isActive")?merchantObj.optString("isActive"):"";
				if(status.equalsIgnoreCase("false"))
					return ServiceNameToEventMapper.DEACTIVATE_MERCHANT;
				else if(status.equalsIgnoreCase("true"))
					return ServiceNameToEventMapper.ACTIVATE_MERCHANT;
			}
			else {
				merchant=merchantObj.optString("labelText");
				return ServiceNameToEventMapper.UPDATE_MERCHANT;
			}
		}
		else if (operationId.equals("createMerchantDetails")) {
			merchant=merchantObj.optString("labelText");
			return ServiceNameToEventMapper.CREATE_MERCHANT;
		}
		else if (operationId.equals("deleteMerchantCharges")) {
			request=reqPayload.has("merchantPaymentCharges")?reqPayload.getAsJsonArray("merchantPaymentCharges").toString():null;
			requestArray1 = new JSONArray(request);
			merchantObj = requestArray1.getJSONObject(0);
			merchant=merchantObj.optString("merchantCode");
			return ServiceNameToEventMapper.MERCHANT_FEE_DELETE;
		}
		else if (operationId.equals("createCardLimits")) {
			cardCategory=reqPayload.has("cardCategory")?reqPayload.get("cardCategory").getAsString():"";
			cardType=reqPayload.has("cardType")?reqPayload.get("cardType").getAsString():null;
			status= cardCategory+" of "+cardType+"";
			return ServiceNameToEventMapper.CREATE_CARD_LIMITS;
		}
		else if (operationId.equals("updateCardLimits")) {
			cardCategory=reqPayload.has("cardCategory")?reqPayload.get("cardCategory").getAsString():"";
			cardType=reqPayload.has("cardType")?reqPayload.get("cardType").getAsString():null;
			return ServiceNameToEventMapper.UPDATE_CARD_LIMITS;
		}
		else if (operationId.equals("deleteCardLimits")) {
			cardCategory=reqPayload.has("id")?reqPayload.get("id").getAsString():"";
			return ServiceNameToEventMapper.DELETE_CARD_LIMITS;
		}
		else if (operationId.equals("DisableThirdPartyAuth")) {
			merchant=reqPayload.has("userName")?reqPayload.get("userName").getAsString():"";
			return ServiceNameToEventMapper.DISABLE_THIRDPARTY_AUTH;
		}
		}catch (Exception e) {
			LOG.error("Exception occured in HBL:AuditLogsPostProcessor:",e);
		}
		return null;
	}
}
