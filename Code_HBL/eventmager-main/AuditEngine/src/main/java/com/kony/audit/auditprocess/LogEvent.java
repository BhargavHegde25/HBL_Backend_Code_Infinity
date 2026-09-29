package com.kony.audit.auditprocess;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.audit.Audit;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.audit.auditutils.AuditConstants;
import com.kony.audit.auditutils.AuditDBConstants;
import com.kony.audit.auditutils.Event;
import com.kony.audit.auditutils.AuditUtils;
import com.kony.audit.auditutils.PropertiesCache;

public class LogEvent {
	private LogEvent() {
	}

	private static final String MONEYMOVEMENTJSONFEILDS = "isScheduled,Customer_id,ExpenseCategory_id,Payee_id,Bill_id,Type_id,Reference_id,fromAccountNumber,fromAccountBalance,toAccountNumber,toAccountBalance,amount,convertedAmount,transactionCurrency,baseCurrency,Status_id,statusDesc,notes,checkNumber,imageURL1,imageURL2,hasDepositImage,description,scheduledDate,transactionDate,createdDate,transactionComments,toExternalAccountNumber,Person_Id,frequencyType,numberOfRecurrences,frequencyStartDate,frequencyEndDate,checkImage,checkImageBack,cashlessOTPValidDate,cashlessOTP,cashlessPhone,cashlessEmail,cashlessPersonName,cashlessMode,cashlessSecurityCode,cashWithdrawalTransactionStatus,cashlessPin,category,billCategory,recurrenceDesc,deliverBy,p2pContact,p2pRequiredDate,requestCreatedDate,penaltyFlag,payoffFlag,viewReportLink,isPaypersonDeleted,fee,feeCurrency,feePaidByReceipent,frontImage1,frontImage2,backImage1,backImage2,checkDesc,checkNumber1,checkNumber2,bankName1,bankName2,withdrawlAmount1,withdrawlAmount2,cashAmount,payeeCurrency,billid,isDisputed,disputeDescription,disputeReason,disputeStatus,disputeDate,payeeName,checkDateOfIssue,checkReason,isPayeeDeleted,amountRecieved,requestValidity,statementReference,transCreditDebitIndicator,bookingDateTime,valueDateTime,transactionInformation,addressLine,transactionAmount,chargeAmount,chargeCurrency,sourceCurrency,targetCurrency,unitCurrency,exchangeRate,contractIdentification,quotationDate,instructedAmount,instructedCurrency,transactionCode,transactionSubCode,proprietaryTransactionCode,proprietaryTransactionIssuer,balanceCreditDebitIndicator,balanceType,balanceAmount,balanceCurrency,merchantName,merchantCategoryCode,creditorAgentSchemeName,creditorAgentIdentification,creditorAgentName,creditorAgentaddressType,creditorAgentDepartment,creditorAgentSubDepartment,creditorAgentStreetName,creditorAgentBuildingNumber,creditorAgentPostCode,creditorAgentTownName,creditorAgentCountrySubDivision,creditorAgentCountry,creditorAgentAddressLine,creditorAccountSchemeName,creditorAccountIdentification,creditorAccountName,creditorAccountSeconIdentification,debtorAgentSchemeName,debtorAgentIdentification,debtorAgentName,debtorAgentAddressType,debtorAgentDepartment,debtorAgentSubDepartment,debtorAgentStreetName,debtorAgentBuildingNumber,dedtorAgentPostCode,debtorAgentTownName,debtorAgentCountrySubDivision,debtorAgentCountry,debtorAgentAddressLine,debtorAccountSchemeName,debtorAccountIdentification,debtorAccountName,debtorAccountSeconIdentification,cardInstrumentSchemeName,cardInstrumentAuthorisationType,cardInstrumentName,cardInstrumentIdentification,IBAN,sortCode,FirstPaymentDateTime,NextPaymentDateTime,FinalPaymentDateTime,StandingOrderStatusCode,FP_Amount,FP_Currency,NP_Amount,NP_Currency,FPA_Amount,FPA_Currency,ConsentId,Initiation_InstructionIdentification,Initiation_EndToEndIdentification,RI_Reference,RI_Unstructured,RiskPaymentContextCode,MerchantCustomerIdentification,beneficiaryName,bankName,swiftCode,DomesticPaymentId,linkSelf,StatusUpdateDateTime,dataStatus,serviceName,payPersonName";
	private static final String AUDITACTIVITYJSONFEILDS = "EventId,EventType,EventSubType,Status_Id,sessionId,AppId,UserName,Customer_Id,partyid,corecustomerid,isCSRAssist,appSessionId,payeeNickName,relationshipNumber,AdminUserName,AdminUserRole,Producer,MoneyMovementRefId,creditcardnumber,mfa_State,mfa_ServiceKey,mfa_Type,nonSearchable,phoneNumber,email,deviceModel,operatingSystem,browser,deviceId,channel,appVersion,platform,ipAddress,eventts,createdby,createdts,softdeleteflag";
	private static String[] moneymovementarr = MONEYMOVEMENTJSONFEILDS.split(",");
	private static String[] auditactivityarr = AUDITACTIVITYJSONFEILDS.split(",");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	private static final Audit audit = Logger.forAudit().forModule("Infinity", "CUSTOMERACTIVITY");

	private static void processAuditRecord(Event eventobj, Map<String, String> eventdata, Map<String, String> otherdata,
			Map<String, String> sessiondata, String moneymonitoruuid) {
		Map<String, String> eventglobaldata = new HashMap<>();
		new JsonObjectProcessor().fetchAlertContentFieldsFromAJson(eventobj.getJsonElement().getAsJsonObject(),
				eventglobaldata);
		JsonObject js = generateAuditJsonFromInput(eventobj, eventobj.getJsonElement(), eventdata, otherdata,
				sessiondata, eventglobaldata);
		Map<String, Object> reqinput = AuditUtils.convertJsonToMap(js);
		reqinput.put("createdts", new Date());
		reqinput.put("Id", UUID.randomUUID().toString());
		reqinput.put("softdeleteflag", "0");
		reqinput.put(AuditConstants.MONEYMOVEMENTREFID, moneymonitoruuid);
		diagnostic.prepareDebug("reqinput" + reqinput).log();
		audit.prepareInfo(new JSONObject(reqinput).toString()).tag("level", "INFO").log();
		AuditUtils.callInternalService(reqinput, AuditDBConstants.EVENTDBDBSERVICE,
				AuditUtils.replaceSchemaName(AuditDBConstants.AUDIT_INSERT, AuditUtils.getLogSchemaname()), null);

	}

	private static void processMoneyMovementRecord(Event eventobj, Map<String, String> eventdata,
			Map<String, String> otherdata, Map<String, String> sessiondata, List<String> currencycodes,
			String moneymonitoruuid) {
		JsonObject js = null;

		js = generateMoneyMovementJsonFromInput(eventobj, eventdata, otherdata, sessiondata, currencycodes);
		Map<String, Object> reqinput = AuditUtils.convertJsonToMap(js);
		reqinput.put("Id", moneymonitoruuid);
		reqinput.put("createdts", new Date());
		reqinput.put("softdeleteflag", "0");
		audit.prepareInfo(new JSONObject(reqinput).toString()).tag("level", "INFO").log();
		AuditUtils.callInternalService(reqinput, AuditDBConstants.EVENTDBDBSERVICE,
				AuditUtils.replaceSchemaName(AuditDBConstants.MONEYMOVEMENT_INSERT, AuditUtils.getLogSchemaname()),
				null);

	}

	public static void processEvent(Event eventobj, Map<String, String> eventdata, Map<String, String> otherdata,
			Map<String, String> sessiondata, List<String> currencycodes) {
		try {
			String moneymonitoruuid = UUID.randomUUID().toString();
			if ((eventdata.containsKey(AuditConstants.AMOUNT) && eventdata.containsKey(AuditConstants.REFERENCEID))
					|| (eventdata.containsKey(AuditConstants.AMOUNT)
							&& eventdata.containsKey(AuditConstants.TRANSACTIONID))) {
				processMoneyMovementRecord(eventobj, eventdata, otherdata, sessiondata, currencycodes,
						moneymonitoruuid);
			}
			processAuditRecord(eventobj, eventdata, otherdata, sessiondata, moneymonitoruuid);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}

	}

	private static JsonObject generateMoneyMovementJsonFromInput(Event eventobj, Map<String, String> eventdata,
			Map<String, String> otherdata, Map<String, String> sessiondata, List<String> currencycodes) {
		JsonObject mainObj = new JsonObject();

		for (int item = 0; item < moneymovementarr.length; item++) {
			String propValue = PropertiesCache.getInstance().getProperty(moneymovementarr[item]);
			if (propValue == null)
				propValue = moneymovementarr[item].toLowerCase();
			if(propValue!=null)
				propValue= propValue.toLowerCase();
			if (moneymovementarr[item].equals(AuditConstants.TRANSACTIONCURRENCY) && currencycodes != null
					&& !currencycodes.isEmpty()) {
				mainObj.addProperty(moneymovementarr[item], currencycodes.get(0));
			} else {
				addFromPayload(eventdata, otherdata, sessiondata, propValue, mainObj, moneymovementarr[item]);
			}
		}

		if (!mainObj.has(AuditConstants.REFERENCE_ID)) {
			mainObj.addProperty(AuditConstants.REFERENCE_ID, eventdata.get(AuditConstants.TRANSACTIONID));
		}
		if (!mainObj.has(AuditConstants.CUSTOMER_ID)) {
			mainObj.addProperty(AuditConstants.CUSTOMER_CAM, eventobj.getCustomerId());
		}
		return mainObj;
	}

	private static void addFromPayload(Map<String, String> eventdata, Map<String, String> otherdata,
			Map<String, String> sessiondata, String propvalue, JsonObject mainobj, String itemname) {
		if (otherdata.containsKey(propvalue) && StringUtils.isNotBlank(otherdata.get(propvalue))) {
			mainobj.addProperty(itemname, otherdata.get(propvalue));
		} else if (eventdata.containsKey(propvalue) && StringUtils.isNotBlank(eventdata.get(propvalue))) {
			mainobj.addProperty(itemname, eventdata.get(propvalue));
		} else if (sessiondata.containsKey(propvalue) && StringUtils.isNotBlank(sessiondata.get(propvalue))) {
			mainobj.addProperty(itemname, sessiondata.get(propvalue));
		}
	}

	private static void addEventDataToJson(JsonObject mainobj, JsonElement event) {
		JsonObject tempjson = null;
		try {
			tempjson = AuditUtils.getJsonObjects(AuditConstants.EVENTDATA, event.getAsJsonObject(), false);
		} catch (Exception e) {
			diagnostic.prepareDebug("parameter fetch error", e).log();
		}
		if (tempjson != null)
			mainobj.addProperty(AuditConstants.EVENTDATA_CAM, tempjson.toString());
	}

	private static JsonObject generateAuditJsonFromInput(Event eventobj, JsonElement event,
			Map<String, String> eventdata, Map<String, String> otherdata, Map<String, String> sessiondata,
			Map<String, String> eventglobaldata) {
		JsonObject mainObj = new JsonObject();
		try {
			for (int item = 0; item < auditactivityarr.length; item++) {
				String propValue = PropertiesCache.getInstance().getProperty(auditactivityarr[item]);
				if (propValue == null)
					propValue = auditactivityarr[item].toLowerCase();
				if(propValue!=null)
					propValue= propValue.toLowerCase();
				if (auditactivityarr[item].equals(AuditConstants.STATUS_ID)) {
					mainObj.addProperty(auditactivityarr[item], eventobj.getstatus());
				} else if (auditactivityarr[item].equals(AuditConstants.CREATEDBY_LOWER)) {
					mainObj.addProperty(auditactivityarr[item],
							PropertiesCache.getInstance().getProperty("C_AUDIT_TRANSACTION_CUST_USERCONST"));
				} else if (eventglobaldata.containsKey(propValue)) {
					mainObj.addProperty(auditactivityarr[item], eventglobaldata.get(propValue));
				} else if (auditactivityarr[item].equals(AuditConstants.IS_SCHEDULING_ENGINE_RECORD)
						&& eventdata.containsKey(propValue)) {
					mainObj.addProperty(AuditConstants.IS_SCHEDULING_ENGINE_RECORD, true);
				} else {
					addFromPayload(eventdata, otherdata, sessiondata, propValue, mainObj, auditactivityarr[item]);
				}
			}

			diagnostic.prepareDebug(mainObj.toString()).log();
			if (mainObj.has(AuditConstants.ADMINUSERNAME) && mainObj.get(AuditConstants.ADMINUSERNAME) != null
					&& !mainObj.get(AuditConstants.ADMINUSERNAME).getAsString().equals("")
					&& !mainObj.get(AuditConstants.ADMINUSERNAME).getAsString().equals("\"\"")) {
				mainObj.addProperty(AuditConstants.ISCSRASSIST, AuditConstants.TRUE);

			}
			diagnostic.prepareDebug(mainObj.toString()).log();
			if (!mainObj.has(AuditConstants.USERNAME_CAM)) {
				mainObj.addProperty(AuditConstants.USERNAME_CAM, eventobj.getUserName());
			}
			if (!mainObj.has(AuditConstants.CUSTOMER_CAM)) {
				mainObj.addProperty(AuditConstants.CUSTOMER_CAM, eventobj.getCustomerId());
			}
			if (eventobj.getappId() != null)
				mainObj.addProperty(AuditConstants.APPID, eventobj.getappId());
			addEventDataToJson(mainObj, event);
			addNonSearchableToJson(mainObj, eventdata, otherdata, sessiondata, eventglobaldata);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return mainObj;
	}

	private static void addNonSearchableToJson(JsonObject mainobj, Map<String, String> eventdata,
			Map<String, String> otherdata, Map<String, String> sessiondata, Map<String, String> eventglobaldata) {

		JsonObject nonsearchable = new JsonObject();
		String jsonparams = PropertiesCache.getInstance().getProperty("C_NON_SEARCHABLES");
		String[] nonsearchables = null;
		if (jsonparams != null)
			nonsearchables = jsonparams.split(",");
		if (nonsearchables == null)
			return;
		for (int i = 0; i < nonsearchables.length; i++) {
			String jsonfield = nonsearchables[i];
			String[] fileds = jsonfield.split(":");
			String key = null;
			String value = null;
			if (fileds.length <= 1)
				continue;
			key = fileds[0];
			value = fileds[1];
			if (value != null)
				value = value.toLowerCase();
			if (key != null && value != null) {
				if (eventglobaldata.containsKey(value)) {
					nonsearchable.addProperty(key, eventglobaldata.get(value));
				} else {
					addFromPayload(eventdata, otherdata, sessiondata, value, nonsearchable, key);
				}
			}
		}
		if (!nonsearchable.entrySet().isEmpty())
			mainobj.addProperty(AuditConstants.NONSEARCHABLE, nonsearchable.toString());
	}
}
