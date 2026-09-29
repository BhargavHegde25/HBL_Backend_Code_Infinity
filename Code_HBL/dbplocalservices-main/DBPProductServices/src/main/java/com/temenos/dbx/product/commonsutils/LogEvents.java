package com.temenos.dbx.product.commonsutils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.BillPayPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeBackendDTO;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.GeneralTransactionDTO;

public class LogEvents {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	static CustomerBusinessDelegate custDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	static ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileBusinessDelegate.class);
	static ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
	static BillPayTransactionBusinessDelegate billPayTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
	static DomesticWireTransactionBusinessDelegate domesticWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
	static GeneralTransactionsBusinessDelegate generalTransactionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
	static ACHCommonsBusinessDelegate aCHCommonsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
	static ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

	public static void pushAlertsForApprovalActions(DataControllerRequest request, DataControllerResponse response, Object res, String featureActionId, String loggedInUser, boolean isInExecutionQueue,
			String action, Map<String, Object> customer) {

		try {
			diagnostic.prepareInfo("pushAlertsForApprovalActions Method started").log();
			ArrayList<String> usernames = new ArrayList<String>();
			String eventType = "", producer = "";
			JsonObject customParams = new JsonObject();
			JSONObject initiatorUserObj = new JSONObject();
			String eventSubTypeKey = "";
			String transactionId = "";
			String feature = "";
			String type = "";
			String account = null;

			if (res instanceof BBRequestDTO) {
				transactionId = ((BBRequestDTO) res).getTransactionId();
			} else if (res instanceof JSONObject) {
				transactionId = ((JSONObject) res).getString("Transaction_id");
			} else if (res instanceof ACHFileDTO) {
				transactionId = ((ACHFileDTO) res).getAchFile_id() + "";
			} else if (res instanceof ACHTransactionDTO) {
				transactionId = ((ACHTransactionDTO) res).getTransaction_id() + "";
			}

			switch (featureActionId) {
			case FeatureAction.ACH_FILE_APPROVE:
			case FeatureAction.ACH_FILE_UPLOAD: {
				diagnostic.prepareInfo("Alerts for ACH File").log();
				eventType = Constants.PARAM_EVENT_APPROVAL_ACH_FILE;
				eventSubTypeKey = "ACH_FILE_" + action + "_" + (isInExecutionQueue ? 1 : 0);

				ACHFileDTO aCHFileDTO = achFileBusinessDelegate.fetchTransactionById(transactionId, request);
				initiatorUserObj = custDelegate.getUserDetails(aCHFileDTO.getCreatedby());
				customParams.addProperty("FileName", aCHFileDTO.getAchFileName());
				customParams.addProperty("UploadDate", HelperMethods.convertDateFormat(aCHFileDTO.getCreatedts(), Constants.SHORT_DATE_FORMAT));
				customParams.addProperty("CreditAmount", getTwoDecimalString(aCHFileDTO.getCreditAmount()));
				customParams.addProperty("DebitAmount", getTwoDecimalString(aCHFileDTO.getDebitAmount()));
				customParams.addProperty("RefNO", aCHFileDTO.getAchFile_id());
				break;
			}
			case FeatureAction.ACH_COLLECTION_APPROVE:
			case FeatureAction.ACH_COLLECTION_CREATE:
			case FeatureAction.ACH_PAYMENT_APPROVE:
			case FeatureAction.ACH_PAYMENT_CREATE: {
				diagnostic.prepareInfo("Alerts for ACH Transaction").log();
				eventType = Constants.PARAM_EVENT_APPROVAL_ACH_TRANSACTION;
				eventSubTypeKey = "ACH_TRANSACTION_" + action + "_" + (isInExecutionQueue ? 1 : 0);

				ACHTransactionDTO aCHTransactionDTO = achTransactionBusinessDelegate.fetchTransactionById(transactionId, request);
				initiatorUserObj = custDelegate.getUserDetails(aCHTransactionDTO.getCreatedby());
				customParams.addProperty("TemplateName", aCHTransactionDTO.getTemplateName());
				customParams.addProperty("RequestType", aCHCommonsBusinessDelegate.getTransactionTypeById((int) aCHTransactionDTO.getTransactionType_id()));
				customParams.addProperty("amount", getTwoDecimalString(aCHTransactionDTO.getTotalAmount()));
				customParams.addProperty("Effectivedate", HelperMethods.convertDateFormat(aCHTransactionDTO.getEffectiveDate(), Constants.SHORT_DATE_FORMAT));
				customParams.addProperty("MaskedFromAccount", getMaskedValue(aCHTransactionDTO.getFromAccount()));
				customParams.addProperty("RefNO", aCHTransactionDTO.getTransaction_id());

				break;
			}
			case FeatureAction.BILL_PAY_APPROVE:
			case FeatureAction.BILL_PAY_CREATE: {
				diagnostic.prepareInfo("Alerts for BillPay").log();
				eventType = Constants.PARAM_EVENT_APPROVAL_BILLPAY;
				eventSubTypeKey = "BILLPAY_" + action + "_" + (isInExecutionQueue ? 1 : 0);

				BillPayTransactionDTO billPayTransactionDTO = billPayTransactionBusinessDelegate.fetchTransactionById(transactionId, request);
				initiatorUserObj = custDelegate.getUserDetails(billPayTransactionDTO.getCreatedby());
				customParams.addProperty("amount", getTwoDecimalString(billPayTransactionDTO.getAmount()));
				customParams.addProperty("Senddate", HelperMethods.convertDateFormat(billPayTransactionDTO.getScheduledDate(), Constants.SHORT_DATE_FORMAT));
				customParams.addProperty("MaskedFromAccount", getMaskedValue(billPayTransactionDTO.getFromAccountNumber()));
				customParams.addProperty("MaskedPayeeName", billPayTransactionDTO.getPayeeName());
				customParams.addProperty("transactionID", transactionId);
				break;
			}
			case FeatureAction.CHEQUE_BOOK_REQUEST_CREATE:
			case FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE: {
				eventType = Constants.PARAM_EVENT_APPROVAL_CHEQUE_BOOK;
				eventSubTypeKey = "CHEQUEBOOK_" + action + "_" + (isInExecutionQueue ? 1 : 0);

			//	ChequeBookDTO chequeBook = chequeBookBackendDelegate.getChequeDetails(request, transactionId);
				ApprovalRequestDTO chequebook = approvalQueueBusinessDelegate.fetchChequeBookDetails(transactionId, request);
				initiatorUserObj = custDelegate.getUserDetails(chequebook.getSentBy());
				customParams.addProperty("Fees", chequebook.getAmount());
				customParams.addProperty("MaskedFromAccount", getMaskedValue(chequebook.getAccountId()));
				customParams.addProperty("NoOfChequeBooks", chequebook.getNoOfBooks());
				customParams.addProperty("transactionID", transactionId);
				account = chequebook.getAccountId();
				break;
			}
				 
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE: {
				diagnostic.prepareInfo("Alerts for Wire Transfers").log();
				eventType = Constants.PARAM_EVENT_APPROVAL_WIRETRANSFER;
				if (featureActionId.contains("DOMESTIC")) {
					eventSubTypeKey = "DOMESTIC_";
				} else {
					eventSubTypeKey = "INTERNATIONAL_";
				}
				eventSubTypeKey += "WIRETRANSFER_" + action + "_" + (isInExecutionQueue ? 1 : 0);

				String filter = "transactionId" + DBPUtilitiesConstants.EQUAL + transactionId;
				ApprovalRequestDTO approvalRequestDTO = domesticWireTransactionBusinessDelegate.fetchWireTransactionsForApprovalInfo(filter, request).get(0);
				initiatorUserObj = custDelegate.getUserDetails(approvalRequestDTO.getSentBy());
				customParams.addProperty("amount", getTwoDecimalString(Double.parseDouble(approvalRequestDTO.getAmount())));
				customParams.addProperty("Senddate", HelperMethods.convertDateFormat(approvalRequestDTO.getSentDate(), Constants.SHORT_DATE_FORMAT));
				customParams.addProperty("MaskedFromAccount", getMaskedValue(approvalRequestDTO.getAccountId()));
				customParams.addProperty("MaskedPayeeName", approvalRequestDTO.getPayee());
				customParams.addProperty("transactionID", transactionId);
				break;
			}

			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
				eventSubTypeKey = "INTERNATIONAL_";
				feature = FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE;

			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
				if (eventSubTypeKey.length() == 0) {
					eventSubTypeKey = "INTER_";
					feature = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
				}
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
				if (eventSubTypeKey.length() == 0) {
					eventSubTypeKey = "INTRA_";
					feature = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
				}
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
				if (eventSubTypeKey.length() == 0) {
					eventSubTypeKey = "OWN_ACCOUNT_";
					feature = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE;
				}
			case FeatureAction.P2P_CREATE:
			case FeatureAction.P2P_APPROVE: {
				diagnostic.prepareInfo("Alerts for General Transactions").log();
				if (eventSubTypeKey.length() == 0) {
					eventSubTypeKey = "P2P_";
					feature = FeatureAction.P2P_CREATE;
				}
				eventType = Constants.PARAM_EVENT_APPROVAL_GENERALTRANSFER;

				GeneralTransactionDTO generalTransactionResponse = generalTransactionsBusinessDelegate.fetchTransactionById(transactionId, featureActionId, request).get(0);

				initiatorUserObj = custDelegate.getUserDetails(generalTransactionResponse.getCreatedby());
				customParams.addProperty("amount", getTwoDecimalString((generalTransactionResponse.getAmount())));
				customParams.addProperty("MaskedFromAccount", getMaskedValue(generalTransactionResponse.getFromAccountNumber()));
				customParams.addProperty("MaskedToAccount", getMaskedValue(generalTransactionResponse.getToAccountNumber()));
				customParams.addProperty("transactionID", transactionId);
				customParams.addProperty("scheduledDate", HelperMethods.convertDateFormat(generalTransactionResponse.getScheduledDate(), Constants.SHORT_DATE_FORMAT));
				
				//BeneficiaryName and SwiftCode For Euro flow
				customParams.addProperty("BeneficiaryName", (generalTransactionResponse.getPayeeName()));
				customParams.addProperty("SwiftCode", (generalTransactionResponse.getSwiftCode()));
				
				String frequencyTypeId = (generalTransactionResponse.getFrequencyTypeId());
				if (!frequencyTypeId.equals("") && !frequencyTypeId.equalsIgnoreCase("Once")) {
					eventSubTypeKey += "RECURRING_";
					customParams.addProperty("Frequency", (generalTransactionResponse.getFrequencyTypeId()));
				}
				eventSubTypeKey += action + "_" + (isInExecutionQueue ? 1 : 0);

				break;
			}
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
				type = "INTER_BANK_";
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
				type = type.length() == 0 ? "INTERNATIONAL_BANK_" : type;
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
				type = type.length() == 0 ? "INTRA_BANK_" : type;
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL: 
				type = type.length() == 0 ? "OWNACCOUNT_" : type;
				eventType = Constants.APPROVAL_GENERAL_TRANSACTION_CANCELLATION_REQUEST;
				eventSubTypeKey = action + "_CANCEL_";

				GeneralTransactionDTO generalTransactionResponse = generalTransactionsBusinessDelegate.fetchTransactionById(transactionId, featureActionId, request).get(0);
				
				initiatorUserObj = custDelegate.getUserDetails(generalTransactionResponse.getCreatedby());
				customParams.addProperty("amount", getTwoDecimalString(generalTransactionResponse.getAmount()));
				customParams.addProperty("executiondate", generalTransactionResponse.getScheduledDate());
				customParams.addProperty("MaskedFromAccount", getMaskedValue(generalTransactionResponse.getFromAccountNumber()));
				customParams.addProperty("MaskedToAccount", getMaskedValue(generalTransactionResponse.getToAccountNumber()));
				customParams.addProperty("transactionID", transactionId);
				customParams.addProperty("ReferenceID", transactionId);
				customParams.addProperty("InitiatorName", generalTransactionResponse.getCreatedby());

				String frequencyTypeId = generalTransactionResponse.getFrequencyTypeId();
				if (!frequencyTypeId.equals("") && !frequencyTypeId.equalsIgnoreCase("Once")) {
					eventSubTypeKey += "RECUR_";
					customParams.addProperty("Frequency", generalTransactionResponse.getFrequencyTypeId());
					customParams.addProperty("NoofRecurrences", generalTransactionResponse.getNumberOfRecurrences());
					customParams.addProperty("TransferDate", generalTransactionResponse.getScheduledDate());
					customParams.addProperty("EndBy", generalTransactionResponse.getFrequencyEndDate());
				}
				eventSubTypeKey += type + (isInExecutionQueue ? 1 : 0);
				break;
			}
			if(action.equalsIgnoreCase("APPROVE") && loggedInUser.equalsIgnoreCase(initiatorUserObj.getString("UserName"))){
				diagnostic.prepareInfo("No Alerts needed for Self Approval scenario").log();
				return;
			}
			
			
			addCommonCustomParams(customParams, initiatorUserObj, customer, loggedInUser, request);
			producer = getProducerAndAddUserNamesToList(usernames, action, isInExecutionQueue, initiatorUserObj, loggedInUser);
			
			String[] eventSubType = Constants.approvalAlertSubEvents.get(eventSubTypeKey);
			try {
				System.out.println("eventSubType array : "+ Arrays.toString(eventSubType));
				for (int i = 0; i < eventSubType.length; i++) {
					pushEvent(request, response, eventType, eventSubType[i], customParams, producer, usernames.get(i), account);
				}
			}catch (Exception e) {
				alert.prepareError("Exception occured at sending alerts", e).log();
			}
			diagnostic.prepareInfo("pushAlertsForApprovalActions Method exited").log();
		} catch (Exception e) {
			alert.prepareError("Exception occured at HandleAllAlerts method: " , e).log();
		}

	}
	
	public static void pushAlertsForApprovalRequests(String featureActionId,DataControllerRequest request,
			DataControllerResponse response,Map<String, Object> inputParam,ACHFileDTO achfileDTO, String referenceId,String requestId,
			String customerName,Map<String, Object> additionalParams) {
		try {
			JsonObject  customparams = new JsonObject();
			String producer = null;
			String eventType = null;
			String[] eventSubType = null;
			String subType = null ; 
			String account = null;
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String loggedInUserFullName = "";
			if(customer.get(Constants.FirstName) != null) {
			    loggedInUserFullName = customer.get(Constants.FirstName).toString() + " ";
			}if(customer.get(Constants.LastName) != null) {
			    loggedInUserFullName += customer.get(Constants.LastName).toString();
			}
			switch (featureActionId) {
				case FeatureAction.ACH_FILE_UPLOAD: {
					try {
						eventType = Constants.PARAM_EVENT_ACH_FILE_APPROVAL_REQUEST;
						eventSubType = Constants.approvalRequestsSubEvents.get(eventType);
						producer = Constants.ACH_FILE_UPLOAD;
						customparams.addProperty("FileName", achfileDTO.getAchFileName());
						customparams.addProperty("UplaodDate",achfileDTO.getCreatedts());
						customparams.addProperty("CreditAmount", getTwoDecimalString(achfileDTO.getCreditAmount()));
						customparams.addProperty("DebitAmount", getTwoDecimalString(achfileDTO.getDebitAmount()));
						customparams.addProperty("RefNumber",referenceId);
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);											
						}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS : Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case FeatureAction.ACH_PAYMENT_CREATE:
				case FeatureAction.ACH_COLLECTION_CREATE: {
					try {
						eventType = Constants.PARAM_EVENT_ACH_TRANSACTION_APPROVAL_REQUEST;
						eventSubType = Constants.approvalRequestsSubEvents.get(eventType);
						producer = Constants.ACH_TRANSACTION_CREATE;
						customparams.addProperty("TemplateName", inputParam.get("TemplateName").toString());
						customparams.addProperty("RequestType", additionalParams.get("transactionType").toString());
						customparams.addProperty("amount", getTwoDecimalString(Double.parseDouble(additionalParams.get("totalAmount").toString())));
						customparams.addProperty("Effectivedate",inputParam.get("EffectiveDate").toString());
						customparams.addProperty("MaskedFromAccount-NickName", getMaskedValue(inputParam.get("DebitAccount").toString()));
						customparams.addProperty("RefNo",referenceId);
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);						
					}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS : Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case FeatureAction.BILL_PAY_CREATE: {
					try {	
						producer = Constants.BILL_PAY_CREATE;
						eventType = Constants.PARAM_EVENT_BILLPAY_APPROVAL_REQUEST;
						eventSubType = Constants.approvalRequestsSubEvents.get(eventType);
						customparams.addProperty("Senddate", inputParam.get("scheduledDate").toString());
						CommonUtils cUtil = new CommonUtils();
						customparams.addProperty("MaskedFromAccount", cUtil.getMaskedValue(inputParam.get("fromAccountNumber").toString()));
						customparams.addProperty("amount", getTwoDecimalString(Double.parseDouble(inputParam.get("amount").toString())));						
						customparams.addProperty("transactionID",referenceId);
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);
						BillPayPayeeBackendDelegate billPayPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
								.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(BillPayPayeeBackendDelegate.class);
						Set<String> payeeIds = new HashSet<String>();
						payeeIds.add(inputParam.get("payeeId").toString());
						List<BillPayPayeeBackendDTO> billPayPayeeBackendDTOs = billPayPayeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
						BillPayPayeeBackendDTO billPayPayeeBackendDTO = billPayPayeeBackendDTOs.get(0);			
						customparams.addProperty("MaskedPayeeName",billPayPayeeBackendDTO.getPayeeName());
					}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS : Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				
				case FeatureAction.CHEQUE_BOOK_REQUEST_CREATE : {
					try {
						String isApprovalRequired = inputParam.get("isApprovalRequired").toString();
						if ("false".equalsIgnoreCase(isApprovalRequired)) {
							eventType = Constants.PARAM_EVENT_NOAPPROVAL_CHEQUEBOOK;
						} 
						else {
							eventType = Constants.PARAM_EVENT_APPROVAL_CHEQUE_BOOK;
						}
						eventSubType = Constants.approvalRequestsSubEvents.get(eventType);
						customparams.addProperty("Fees", getTwoDecimalString(Double.parseDouble(inputParam.get("fees").toString())));
						customparams.addProperty("MaskedFromAccount", getMaskedValue(inputParam.get("accountId").toString()));
						customparams.addProperty("NoOfChequeBooks", inputParam.get("NoOfChequeBooks").toString());
						customparams.addProperty("transactionID", inputParam.get("transactionId").toString());
						
						producer = Constants.CHEQUE_BOOK_REQUEST_CREATE;
	
						customparams.addProperty("InitiatorName",loggedInUserFullName);
						account = inputParam.get("accountId").toString();
					}
					catch(Exception e) {
						alert.prepareError("LOGEVENTS : Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE: {
					try {
						producer = Constants.DOMESTIC_WIRE_TRANSFER_CREATE;
						eventType = Constants.PARAM_EVENT_WIRETRANSFER_APPROVAL_REQUEST;
						eventSubType = Constants.approvalRequestsSubEvents.get(eventType);
						customparams.addProperty("MaskedFromAccount", getMaskedValue(inputParam.get("fromAccountNumber").toString()));
						customparams.addProperty("amount", getTwoDecimalString(Double.parseDouble(inputParam.get("amount").toString())));		
						SimpleDateFormat formatter = new SimpleDateFormat( Constants.SHORT_DATE_FORMAT);
						Date currdate = new Date();
						String date = formatter.format(currdate);				
					    customparams.addProperty("Senddate", date);
						customparams.addProperty("MaskedRecipientName",inputParam.get("payeeName").toString());	
						customparams.addProperty("transactionID",referenceId);					
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);
						
					}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS : Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE: {
					try {
						producer = Constants.INTERNATIONAL_WIRE_TRANSFER_CREATE;
						eventType = Constants.PARAM_EVENT_WIRETRANSFER_APPROVAL_REQUEST;
						eventSubType = Constants.approvalRequestsSubEvents.get(eventType);
						customparams.addProperty("MaskedFromAccount", getMaskedValue(inputParam.get("fromAccountNumber").toString()));
						customparams.addProperty("amount", getTwoDecimalString(Double.parseDouble(inputParam.get("amount").toString())));						
						SimpleDateFormat formatter = new SimpleDateFormat( Constants.SHORT_DATE_FORMAT);
						Date currdate = new Date();
						String date = formatter.format(currdate);						
					    customparams.addProperty("Senddate", date);
						customparams.addProperty("MaskedRecipientName",inputParam.get("payeeName").toString());	
						customparams.addProperty("transactionID",referenceId);		
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);
						
						
					}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS : Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE: {
					producer = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE;
					subType = FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE;
				}
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE: {
					if(producer == null)
					{
					producer = Constants.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
					subType = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
					}
				}
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE: {
					if(producer == null)
					{
					producer = Constants.INTRA_BANK_FUND_TRANSFER_CREATE;
					subType = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
					}				
				}
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:{
					if(producer == null)
					{
					producer = Constants.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE;					
					subType = FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE;
					}
					try {
						//JsonObject generalTransactionResponse = generalTransactionsBusinessDelegate.fetchTransactionEntry(referenceId, featureActionId, null);
						eventType = Constants.PARAM_EVENT_GENTRANSACTION_APPROVAL_REQUEST;
						GeneralTransactionDTO generalTransactionResponse = generalTransactionsBusinessDelegate.fetchTransactionById(referenceId, featureActionId, request).get(0);						
						if(inputParam.get("isScheduled").toString().equalsIgnoreCase("1"))
						{
							subType+="_REC";
							
						}
						eventSubType = Constants.approvalRequestsSubEvents.get(subType);
						customparams.addProperty("amount", getTwoDecimalString(Double.parseDouble(inputParam.get("amount").toString())));
						customparams.addProperty("executiondate", HelperMethods.convertDateFormat(inputParam.get("scheduledDate").toString(),Constants.SHORT_DATE_FORMAT));
						customparams.addProperty("MaskedFromAccount", getMaskedValue(inputParam.get("fromAccountNumber").toString()));
						customparams.addProperty("MaskedToAccount",getMaskedValue(inputParam.get("toAccountNumber").toString()));	
						customparams.addProperty("ReferenceID",referenceId);
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);
						//BeneficiaryName and SwiftCode For Euro flow
						customparams.addProperty("BeneficiaryName",(generalTransactionResponse.getPayeeName()));
						customparams.addProperty("SwiftCode",(generalTransactionResponse.getSwiftCode()));
					}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS :Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
				{
					try {
						producer = Constants.INTERNATIONAL_WIRE_TRANSFER_CREATE;
						eventType = Constants.APPROVAL_GENERAL_TRANSACTION_CANCELLATION_REQUEST;
					    subType = featureActionId;
						customparams.addProperty("amount", inputParam.get("amount").toString());
						customparams.addProperty("executiondate", inputParam.get("scheduledDate").toString());
						customparams.addProperty("MaskedFromAccount", getMaskedValue(inputParam.get("fromAccountNumber").toString()));
						customparams.addProperty("MaskedToAccount",getMaskedValue(inputParam.get("toAccountNumber").toString()));	
						customparams.addProperty("ReferenceID",referenceId);
						customparams.addProperty("requestId",requestId);
						customparams.addProperty("InitiatorName",loggedInUserFullName);
						customparams.addProperty("transactionID",referenceId);										
					    if(inputParam.get("isScheduled").toString().equalsIgnoreCase("1")){
							subType+="_REC";
							customparams.addProperty("Frequency", inputParam.get("frequency") != null ? inputParam.get("frequency").toString() : "");
							customparams.addProperty("NoofRecurrences", inputParam.get("NoofRecurrences") != null ? inputParam.get("NoofRecurrences").toString() : "");
							customparams.addProperty("TransferDate", inputParam.get("TransferDate") != null ? inputParam.get("TransferDate").toString() : "");
							customparams.addProperty("EndBy", inputParam.get("EndBy") != null ? inputParam.get("EndBy").toString() : "");
						}
						if(StringUtils.isNotEmpty(requestId)) {
							subType += "_1";
						}else {
							subType += "_0";
						}
						eventSubType = Constants.approvalRequestsSubEvents.get(subType);
					}
					catch (Exception e) {
						alert.prepareError("LOGEVENTS :Error pushing alert of "+featureActionId +" to initiator and approvers").log();
					}
					break;
				}
				case Constants.RENOTIFY_PENDING_APPROVAL_REQUEST : {
					eventType = Constants.REQUEST_AND_APPROVAL_ALERTS;
					eventSubType = new String[] {Constants.RENOTIFY_PENDING_APPROVAL_REQUEST};
					producer = Constants.RENOTIFY_PENDING_APPROVAL_REQUEST;
					customparams.addProperty("type", inputParam.get("type").toString());
					customparams.addProperty("transactionId", referenceId);
					break;
				}
			}
			try {
				System.out.println("eventSubType array : "+ Arrays.toString(eventSubType));
				for (int i = 0; i < eventSubType.length; i++) {
					pushEvent(request, response, eventType, eventSubType[i], customparams, producer,customerName,account);
				}
			}
			catch (Exception e) {
				alert.prepareError("LOGEVENTS :Error pushing alert of "+featureActionId +" to initiator and approvers").log();
			}
		} catch (Exception e) {
			alert.prepareError("LOGEVENTS :Failed at pushAlertsForApprovalRequests "+e).log();
		}
	}


	private static String getProducerAndAddUserNamesToList(ArrayList<String> usernames, String action, boolean isInExecutionQueue, JSONObject initiatorUserObj, String loggedInUser) {
		String producer = "";
		if (action.equals("APPROVE")) {
			producer = "ApprovalsAndRequests/operations/MyApprovals/Approve";
			if (isInExecutionQueue) {
				usernames.add(initiatorUserObj.getString("UserName"));
				usernames.add(loggedInUser);
			} else {
				usernames.add(initiatorUserObj.getString("UserName"));
			}
		} else if (action.equals("REJECT")) {
			producer = "ApprovalsAndRequests/operations/MyApprovals/Reject";
			usernames.add(initiatorUserObj.getString("UserName"));
			usernames.add(loggedInUser);
		} else if (action.equals("WITHDRAW")) {
			producer = "ApprovalsAndRequests/operations/MyRequests/Withdraw";
			usernames.add(loggedInUser);
			usernames.add(loggedInUser);
		}
		return producer;
		
	}

	private static Result pushEvent(DataControllerRequest requestManager, DataControllerResponse responseManager, String eventType, String eventSubType, JsonObject customParams, String producer,
			String username, String account) {
		try {
			String enableEvents = AuditHelperMethods.getConfigurableParameters(Constants.ENABLE_EVENTS, requestManager);

			if (enableEvents != null && enableEvents.equalsIgnoreCase("true")) {
				if (!eventSubType.equals("")) {
					try {
						AuditHelperMethods
								.execute(new AuditHelperMethods(requestManager, responseManager, eventType, eventSubType, producer, Constants.SID_EVENT_SUCCESS, account, username, customParams));

					} catch (Exception e2) {
						alert.prepareError("Exception Occured while invoking AuditHelperMethods").log();
					}
				}
			}

		} catch (Exception ex) {
			diagnostic.prepareDebug("exception occured in pushAlert", ex).log();
		}
		return new Result();
	}

	private static String getMaskedValue(String accountNumber) {
		String lastFourDigits;
		if (StringUtils.isNotBlank(accountNumber)) {
			if (accountNumber.length() > 4) {
				lastFourDigits = accountNumber.substring(accountNumber.length() - 4);
				accountNumber = "XXXX" + lastFourDigits;
			} else {
				accountNumber = "XXXX" + accountNumber;
			}
		}

		return accountNumber;
	}
	
	private static void addCommonCustomParams(JsonObject customParams, JSONObject initiatorUserObj, Map<String, Object> customer, String loggedInUser, DataControllerRequest request){
		String inititatorName = "";
		String loggedInUserFullName = "";
		if (initiatorUserObj.has(Constants.FirstName)) {
			inititatorName += initiatorUserObj.getString(Constants.FirstName) + " ";
		}
		if (initiatorUserObj.has(Constants.LastName)) {
			inititatorName += initiatorUserObj.getString(Constants.LastName);
		}

		
		if (customer.get(Constants.FirstName) != null) {
			loggedInUserFullName = customer.get(Constants.FirstName).toString() + " ";
		}
		if (customer.get(Constants.LastName) != null) {
			loggedInUserFullName += customer.get(Constants.LastName).toString();
		}

		if (inititatorName.length() == 0) {
			inititatorName = initiatorUserObj.getString("UserName");
		}
		if (loggedInUserFullName.length() == 0) {
			loggedInUserFullName = loggedInUser;
		}

		customParams.addProperty("InitiatorName", inititatorName);
		customParams.addProperty("ActedUserName", loggedInUserFullName);
		customParams.addProperty("requestId", request.getParameter("requestId"));
		diagnostic.prepareInfo("Added common Custom params").log();
	}
	
	private static String getTwoDecimalString(double amount) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(amount);
	}
}
