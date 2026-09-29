package com.temenos.dbx.product.transactionservices.businessdelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.OwnAccountFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.GeneralTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

public class GeneralTransactionsBusinessDelegateImpl implements GeneralTransactionsBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<GeneralTransactionDTO> fetchGeneralTransactions(String customerId, String transactionId, String featureActionId, FilterDTO filters) {
		
		List<GeneralTransactionDTO> transactions = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_GENERAL_TRANSACTION_PROC;
		
		Map<String, Object> requestMap;
		String fetchResponse = null;
		
		try {
			requestMap = JSONUtils.parseAsMap(JSONUtils.stringify(filters), String.class, Object.class);
			requestMap.put(Constants._TRANSACTION_ID, transactionId);
			requestMap.put(Constants._CUSTOMER_ID, customerId);
			requestMap.put(Constants._FEATURE_ACTION_ID, featureActionId);
			
			fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestMap, null, "");
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray records = responseObj.getJSONArray(Constants.RECORDS);
			transactions = JSONUtils.parseAsList(records.toString(), GeneralTransactionDTO.class);
		}
		catch(Exception exp) {
			alert.prepareError("Error Occurred while fetching general transactions",exp).log();
			return null;
		}
		return transactions;
	}
	
	@Override
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request) {
		
		switch (featureActionId) {
			
			case FeatureAction.BILL_PAY_CREATE:
			case FeatureAction.BILL_PAY_APPROVE:
				BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
				billpayTransactionDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
				InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				interbanktransferDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
				IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class); 
				intrabankTransactionDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
				OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				ownaccountTransactionDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.P2P_CREATE:
			case FeatureAction.P2P_APPROVE:
				P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class); 
				p2pTransactionDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
				InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				internationalFundTransferDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
				DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class); 
				domesticWireTransactionDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
				InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class); 
				internationalWireTransactionDelegate.executeTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				InterBankFundTransferBusinessDelegate interDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				interDelegate.cancelTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
				IntraBankFundTransferBusinessDelegate intraDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class); 
				intraDelegate.cancelTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
				OwnAccountFundTransferBusinessDelegate ownTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				ownTransferDelegate.cancelTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				InternationalFundTransferBusinessDelegate internationalTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				internationalTransferDelegate.cancelTransactionAfterApproval(transactionId, featureActionId, request);
				break;
				
			default:
				break;
				
			}
	}

	@Override
	public Boolean updateStatus(String transactionId, String featureActionId, String status) {

		Object response = null;
		switch (featureActionId) {

		case FeatureAction.BILL_PAY_CREATE:
		case FeatureAction.BILL_PAY_APPROVE:
			BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
			response = billpayTransactionDelegate.updateStatus(transactionId, status, null);			
			break;
			
		case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
		case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
			InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
			response = interbanktransferDelegate.updateStatus(transactionId,status, null);
			break;
			
		case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
		case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
			IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
			response = intrabankTransactionDelegate.updateStatus(transactionId,status, null);
			break;
			
		case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
		case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
			OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl
					.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
			response = ownaccountTransactionDelegate.updateStatus(transactionId, status, null);
			break;
			
		case FeatureAction.P2P_CREATE:
		case FeatureAction.P2P_APPROVE:
			P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
			response = p2pTransactionDelegate.updateStatus(transactionId, status, null);
			break;
			
		case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
		case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
			InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl
					.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
			response = internationalFundTransferDelegate.updateStatus(transactionId, status, null);
			break;
			
		case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
		case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
			DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
			response = domesticWireTransactionDelegate.updateStatus(transactionId, status, null);
			break;
	
		case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
		case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
			InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
			response = internationalWireTransactionDelegate.updateStatus(transactionId, status, null);
			break;
		case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
		case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
		case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
		case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
		case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
		case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
		case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
		case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
			return true;
			
		default:
			return false;

		}
		if(response == null) {
			return false;
		}
		else {
			return true;
		}

	}
	
	@Override
	public JsonObject fetchTransactionEntry(String transactionId, String featureActionId, String createdBy) {
		
		JsonObject customParams = new JsonObject();
		
		switch (featureActionId) {
			
			case FeatureAction.BILL_PAY_CREATE:
			case FeatureAction.BILL_PAY_APPROVE:
				BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
				BillPayTransactionDTO billPayTransactionDTO = billpayTransactionDelegate.fetchTranscationEntry(transactionId);
				JSONObject billPayTransactionObject = new JSONObject(billPayTransactionDTO);			
				customParams = new JsonParser().parse(billPayTransactionObject.toString()).getAsJsonObject();
				break;
				
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				InterBankFundTransferDTO interBankFundTransferDTO  = interbanktransferDelegate.fetchTranscationEntry(transactionId);
				JSONObject interBankFundTransferObject = new JSONObject(interBankFundTransferDTO);			
				customParams = new JsonParser().parse(interBankFundTransferObject.toString()).getAsJsonObject();
				break;
				
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
				IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class); 
				IntraBankFundTransferDTO intraBankFundTransferDTO = intrabankTransactionDelegate.fetchTranscationEntry(transactionId);
				JSONObject intraBankFundTransferObject = new JSONObject(intraBankFundTransferDTO);			
				customParams = new JsonParser().parse(intraBankFundTransferObject.toString()).getAsJsonObject();
				break;
				
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
				OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				OwnAccountFundTransferDTO ownAccountFundTransferDTO = ownaccountTransactionDelegate.fetchTranscationEntry(transactionId);
				JSONObject ownAccountFundTransferObject = new JSONObject(ownAccountFundTransferDTO);			
				customParams = new JsonParser().parse(ownAccountFundTransferObject.toString()).getAsJsonObject();
				break;
				
			case FeatureAction.P2P_CREATE:
			case FeatureAction.P2P_APPROVE:
				P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class); 
				P2PTransactionDTO p2PTransactionDTO = p2pTransactionDelegate.fetchTranscationEntry(transactionId);
				JSONObject p2PTransactionObject = new JSONObject(p2PTransactionDTO);			
				customParams = new JsonParser().parse(p2PTransactionObject.toString()).getAsJsonObject();
				break;
				
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				InternationalFundTransferDTO internationalFundTransferDTO = internationalFundTransferDelegate.fetchTranscationEntry(transactionId);
				JSONObject internationalFundTransferObject = new JSONObject(internationalFundTransferDTO);			
				customParams = new JsonParser().parse(internationalFundTransferObject.toString()).getAsJsonObject();
				break;
				
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
				DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class); 
				WireTransactionDTO  domesticWireTransactionDTO = domesticWireTransactionDelegate.fetchTranscationById(transactionId, createdBy);
				JSONObject domesticWireTransactionObject = new JSONObject(domesticWireTransactionDTO);			
				customParams = new JsonParser().parse(domesticWireTransactionObject.toString()).getAsJsonObject();
				if(customParams.has("payeeId")) {
					JSONObject payeeObject = domesticWireTransactionObject.getJSONArray("payee").getJSONObject(0);
					customParams.addProperty("payeeNickName", payeeObject.get("nickName").toString());  
					customParams.addProperty("payeeName", payeeObject.get("name").toString());  
				}
				break;
				
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
				InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class); 
				WireTransactionDTO  internationalWireTransactionDTO = internationalWireTransactionDelegate.fetchTranscationById(transactionId, createdBy);
				JSONObject internationalWireTransactionObject = new JSONObject(internationalWireTransactionDTO);			
				customParams = new JsonParser().parse(internationalWireTransactionObject.toString()).getAsJsonObject();
				if(customParams.has("payeeId")) {
					JSONObject payeeObject = internationalWireTransactionObject.getJSONArray("payee").getJSONObject(0);
					customParams.addProperty("payeeNickName", payeeObject.get("nickName").toString());  
					customParams.addProperty("payeeName", payeeObject.get("name").toString());  
				}
				break;
				
			default:
				break;
				
			}
		return customParams;
	}
	
	@Override
	public GeneralTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, String featureActionId) {
		
		GeneralTransactionDTO transaction = null;
		JSONObject transObject = new JSONObject();
		
		switch (featureActionId) {
			
			case FeatureAction.BILL_PAY_CREATE:
			case FeatureAction.BILL_PAY_APPROVE:
				BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
				BillPayTransactionDTO billPayTransactionDTO = billpayTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber, null, null, null);
				transObject = new JSONObject(billPayTransactionDTO);			
				break;
				
				
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
				InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				InterBankFundTransferDTO interBankFundTransferDTO  = interbanktransferDelegate.fetchExecutedTranscationEntry(confirmationNumber, null, null, null);
				transObject = new JSONObject(interBankFundTransferDTO);			
				break;
			
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
				IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class); 
				IntraBankFundTransferDTO intraBankFundTransferDTO = intrabankTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber, null, null ,null);
				transObject = new JSONObject(intraBankFundTransferDTO);			
				break;
			
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
				OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				OwnAccountFundTransferDTO ownAccountFundTransferDTO = ownaccountTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber, null, null,null);
				transObject = new JSONObject(ownAccountFundTransferDTO);			
				break;
				
			case FeatureAction.P2P_CREATE:
			case FeatureAction.P2P_APPROVE:
				P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class); 
				P2PTransactionDTO p2PTransactionDTO = p2pTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber, null, null, null);
				transObject = new JSONObject(p2PTransactionDTO);			
				break;
			
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
				InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				InternationalFundTransferDTO internationalFundTransferDTO = internationalFundTransferDelegate.fetchExecutedTranscationEntry(confirmationNumber, null, null,null);
				transObject = new JSONObject(internationalFundTransferDTO);			
				break;
				
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
				DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class); 
				WireTransactionDTO  domesticWireTransactionDTO = domesticWireTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber);
				transObject = new JSONObject(domesticWireTransactionDTO);
				break;
				
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
				InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class); 
				WireTransactionDTO internationalWireTransactionDTO = internationalWireTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber);
				transObject = new JSONObject(internationalWireTransactionDTO);			
				break;
				
			default:
				break;
				
			}
		
		try {
			transaction = JSONUtils.parse(transObject.toString(), GeneralTransactionDTO.class);
		} catch (IOException e) {
			alert.prepareError("Failed to convert the transaction to GeneralTransactionDTO object", e).log();
		}
		return transaction;
	}
	
	@Override
	public List<GeneralTransactionDTO> fetchTransactionById(String transactionId, String featureActionId, DataControllerRequest request) {
		
		List<GeneralTransactionDTO> responseList = new ArrayList<GeneralTransactionDTO>();
		GeneralTransactionDTO transaction = null;
		JSONObject transObject = new JSONObject();
		
		switch (featureActionId) {
			
			case FeatureAction.BILL_PAY_CREATE:
			case FeatureAction.BILL_PAY_APPROVE:
				BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
				BillPayTransactionDTO billPayTransactionDTO = billpayTransactionDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(billPayTransactionDTO);			
				break;
			
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
				InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				InterBankFundTransferDTO interBankFundTransferDTO  = interbanktransferDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(interBankFundTransferDTO);			
				break;
			
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
				IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class); 
				IntraBankFundTransferDTO intraBankFundTransferDTO = intrabankTransactionDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(intraBankFundTransferDTO);			
				break;
			
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
				OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				OwnAccountFundTransferDTO ownAccountFundTransferDTO = ownaccountTransactionDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(ownAccountFundTransferDTO);			
				break;
				
			case FeatureAction.P2P_CREATE:
			case FeatureAction.P2P_APPROVE:
				P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class); 
				P2PTransactionDTO p2PTransactionDTO = p2pTransactionDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(p2PTransactionDTO);			
				break;
			
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
				InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				InternationalFundTransferDTO internationalFundTransferDTO = internationalFundTransferDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(internationalFundTransferDTO);			
				break;
				
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
				DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class); 
				WireTransactionDTO  domesticWireTransactionDTO = domesticWireTransactionDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(domesticWireTransactionDTO);
				break;
				
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
				InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class); 
				WireTransactionDTO internationalWireTransactionDTO = internationalWireTransactionDelegate.fetchTransactionById(transactionId, request);
				transObject = new JSONObject(internationalWireTransactionDTO);			
				break;
				
			default:
				break;
				
			}
		
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);			
		String customerId = CustomerSession.getCustomerId(customer);
		Set<String> createdByList = new HashSet<String>();
	
		
		try {
			transaction = JSONUtils.parse(transObject.toString(), GeneralTransactionDTO.class);
			List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests(customerId, "", transObject.getString("requestId"), featureActionId);
			transaction = new FilterDTO().merge(Arrays.asList(transaction), mainRequests, "requestId=requestId", "status,transactionId").get(0);
			createdByList.add(transaction.getCreatedby());
			List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(createdByList);
			transaction = (new FilterDTO()).merge(Arrays.asList(transaction), customerDTOs, "createdby=id","").get(0);
			responseList.add(transaction);
		} catch (IOException e) {
			alert.prepareError("Failed to convert the transaction to GeneralTransactionDTO object", e).log();
		}
		return responseList;
	}
	
	@Override
    public void rejectGeneralTransaction(String transactionId, String featureActionId, DataControllerRequest request) {
       
        switch (featureActionId) {
           
            case FeatureAction.BILL_PAY_CREATE:
            case FeatureAction.BILL_PAY_APPROVE:
                BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
                billpayTransactionDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
                InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
                interbanktransferDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
                IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
                intrabankTransactionDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
                OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
                ownaccountTransactionDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.P2P_CREATE:
            case FeatureAction.P2P_APPROVE:
                P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
                p2pTransactionDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
                InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
                internationalFundTransferDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
            case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
                DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
                domesticWireTransactionDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
            case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
                InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
                internationalWireTransactionDelegate.rejectTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
                InterBankFundTransferBusinessDelegate interDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
                interDelegate.rejectDeletion(transactionId, request);
                break;
               
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
                IntraBankFundTransferBusinessDelegate intraDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
                intraDelegate.rejectDeletion(transactionId, request);
                break;
               
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
                OwnAccountFundTransferBusinessDelegate ownTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
                ownTransferDelegate.rejectDeletion(transactionId, request);
                break;
               
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
                InternationalFundTransferBusinessDelegate internationalTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
                internationalTransferDelegate.rejectDeletion(transactionId, request);
                break;
               
            default:
                break;
               
            }
    }

	@Override
    public void withdrawGeneralTransaction(String transactionId, String featureActionId, DataControllerRequest request) {
       
        switch (featureActionId) {
           
            case FeatureAction.BILL_PAY_CREATE:
            case FeatureAction.BILL_PAY_APPROVE:
                BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
                billpayTransactionDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
                InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
                interbanktransferDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
                IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
                intrabankTransactionDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
                OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
                ownaccountTransactionDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.P2P_CREATE:
            case FeatureAction.P2P_APPROVE:
                P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
                p2pTransactionDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
                InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
                internationalFundTransferDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
            case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
                DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
                domesticWireTransactionDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
            case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
                InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
                internationalWireTransactionDelegate.withdrawTransaction(transactionId, featureActionId, request);
                break;
               
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
                InterBankFundTransferBusinessDelegate interDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
                interDelegate.withdrawDeletion(transactionId, request);
                break;
               
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
                IntraBankFundTransferBusinessDelegate intraDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
                intraDelegate.withdrawDeletion(transactionId, request);
                break;
               
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
                OwnAccountFundTransferBusinessDelegate ownTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
                ownTransferDelegate.withdrawDeletion(transactionId, request);
                break;
               
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
                InternationalFundTransferBusinessDelegate internationalTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
                internationalTransferDelegate.withdrawDeletion(transactionId, request);
                break;
               
            default:
                break;
               
            }
    }

	@Override
	public Result getPurposeCodesById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		// TODO Auto-generated method stub
		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String serviceName = "T24ISPaymentsView";
		String operationName = "getPurposeCodes";
		try {
		result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
					true);
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at invoke getPurposeCodes : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
	
	@Override
	public Result getExternalCodes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String serviceName = TemenosConstants.T24_IS_PAYMENTS_VIEW;
		String operationName = "getExternalCodes";
		try {
		result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
					true);
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at invoke getExternalCodes : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
}
