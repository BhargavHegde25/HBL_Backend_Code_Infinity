package com.kony.dbputilities.demoservices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTemplateBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.OwnAccountFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

public class DemoDataACHServices {

    HashMap<String, String> createdTemplates = new HashMap<>();

    public void init(DataControllerRequest dcRequest, String orgId, String createdUserId, DemoDataSB helper) {

        CreateTemplateDemo(dcRequest, DemoDataACH.ACHTemplate1, helper.getAccountNum("Pro Business Checking"),
                orgId, getFormattedTimeStampForRecords("2018-05-15"), createdUserId);

        CreateTemplateDemo(dcRequest, DemoDataACH.ACHTemplate2, helper.getAccountNum("Progress Business Checking"), orgId,
                getFormattedTimeStampForRecords("2018-06-06"), createdUserId);

        CreateTemplateDemo(dcRequest, DemoDataACH.ACHTemplate3, helper.getAccountNum("Pro Business Checking"), 
                orgId, getFormattedTimeStampForRecords("2018-03-24"), createdUserId);

        CreateTemplateDemo(dcRequest, DemoDataACH.ACHTemplate4, helper.getAccountNum("Pro Business Checking"), 
                orgId, getFormattedTimeStampForRecords("2018-04-10"), createdUserId);

        CreateTemplateDemo(dcRequest, DemoDataACH.ACHTemplate5, helper.getAccountNum("Progress Business Checking"), orgId,
                getFormattedTimeStampForRecords("2018-05-05"), createdUserId);

        CreateTransactionDemoWithRequest(dcRequest, DemoDataACH.ACHTransaction1, DemoDataACH.ACHTransaction1Records,
                helper.getAccountNum("Pro Business Checking"), orgId, getFormattedTimeStampForRecords("2018-06-08"),
                createdUserId, "Pending");
        
        CreateTransactionDemoWithRequest(dcRequest, DemoDataACH.ACHTransaction2, DemoDataACH.ACHTransaction2Records,
                helper.getAccountNum("Pro Business Checking"), orgId, getFormattedTimeStampForRecords("2019-07-08"),
                createdUserId, "Pending");
        
        CreateTransactionDemoWithRequest(dcRequest, DemoDataACH.ACHTransaction3, DemoDataACH.ACHTransaction3Records,
                helper.getAccountNum("Progress Business Checking"), orgId, getFormattedTimeStampForRecords("2019-03-08"),
                createdUserId, "Pending");
        
        CreateTransactionDemoWithRequest(dcRequest, DemoDataACH.ACHTransaction4, DemoDataACH.ACHTransaction4Records,
                helper.getAccountNum("Pro Business Checking"), orgId, getFormattedTimeStampForRecords("2019-03-28"),
                createdUserId, "Pending");
        
        CreateTransactionDemoWithRequest(dcRequest, DemoDataACH.ACHTransaction5, DemoDataACH.ACHTransaction5Records,
                helper.getAccountNum("Progress Business Checking"), orgId, getFormattedTimeStampForRecords("2019-06-18"),
                createdUserId, "Pending");

        CreateTransactionDemo(dcRequest, DemoDataACH.ACHTransaction2, DemoDataACH.ACHTransaction2Records,
                helper.getAccountNum("Pro Business Checking"), orgId, getFormattedTimeStampForRecords("2018-06-08"),
                createdUserId, "Executed");

        CreateTransactionDemo(dcRequest, DemoDataACH.ACHTransaction3, DemoDataACH.ACHTransaction3Records,
                helper.getAccountNum("Progress Business Checking"), orgId,
                getFormattedTimeStampForRecords("2018-07-13"), createdUserId, "Executed");

        CreateTransactionDemo(dcRequest, DemoDataACH.ACHTransaction4, DemoDataACH.ACHTransaction4Records,
                helper.getAccountNum("Pro Business Checking"), orgId, getFormattedTimeStampForRecords("2018-05-10"),
                createdUserId, "Executed");

        CreateTransactionDemo(dcRequest, DemoDataACH.ACHTransaction5, DemoDataACH.ACHTransaction5Records,
                helper.getAccountNum("Progress Business Checking"), orgId,
                getFormattedTimeStampForRecords("2018-03-22"), createdUserId, "Executed");

        CreateTransactionDemo(dcRequest, DemoDataACH.ACHTransaction6, DemoDataACH.ACHTransaction6Records,
                helper.getAccountNum("Progress Business Checking"), orgId,
                getFormattedTimeStampForRecords("2018-03-27"), createdUserId, "3");

        //createACHFile1WithRequest(dcRequest, orgId, getFormattedTimeStampForRecords("2018-06-10"), "10", "Pending");

        createACHFile2(dcRequest, orgId, getFormattedTimeStampForRecords("2018-05-27"), createdUserId, "Executed");

        createACHFile3(dcRequest, orgId, getFormattedTimeStampForRecords("2018-05-16"), createdUserId, "Executed");

        createGeneralTransactionWithRequest(dcRequest, FeatureAction.BILL_PAY_CREATE,
        		DemoDataACH.BillPayTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);

        createGeneralTransactionWithRequest(dcRequest, FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE,
        		DemoDataACH.InterBankTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
        createGeneralTransactionWithRequest(dcRequest, FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE,
        		DemoDataACH.IntraBankTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
        createGeneralTransactionWithRequest(dcRequest, FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE,
        		DemoDataACH.InternationalAccountTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
        createGeneralTransactionWithRequest(dcRequest, FeatureAction.P2P_CREATE,
        		DemoDataACH.P2PTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
        createGeneralTransactionWithRequest(dcRequest, FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE,
        		DemoDataACH.OwnAccountTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
        createGeneralTransactionWithRequest(dcRequest, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE,
        		DemoDataACH.InternationalWireTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
        createGeneralTransactionWithRequest(dcRequest, FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE,
        		DemoDataACH.DomesticWireTransactionData, orgId, getFormattedTimeStampForRecords("2018-07-13"), createdUserId, helper);
        
    }

    public void CreateTemplateDemo(DataControllerRequest dcRequest, String template, String debitAccount,
    		String companyId, String date, String createdUserId) {
        HashMap<String, String> map = convertJsonToMap(template);
        String createdTemplateId = createTemplateHelper(dcRequest, map, debitAccount, companyId, date, createdUserId);
        createdTemplates.put(map.get("TemplateName"), createdTemplateId);
    }

    public void CreateTransactionDemo(DataControllerRequest dcRequest, String transaction, String transactionRecords,
            String debitAccount, String companyId, String date, String createdBy, String status) {
        HashMap<String, String> map = convertJsonToMap(transaction);
        String createdTransactionId = createTransactionHelper(dcRequest, map, debitAccount, companyId, date, createdBy,
                status);
        if (StringUtils.isNotBlank(createdTransactionId)) {
            createTransactionRecordHelper(dcRequest, createdTransactionId, map.get("TemplateRequestType_id"),
                    transactionRecords);
        }

    }

    public void CreateTransactionDemoWithRequest(DataControllerRequest dcRequest, String transaction,
            String transactionRecords, String debitAccount, String companyId, String date, String createdBy,
            String status) {
        try {
            HashMap<String, String> map = convertJsonToMap(transaction);
            String featureActionId = map.get("featureActionId");
            String createdTransactionId = createTransactionHelper(dcRequest, map, debitAccount, companyId, date,
                    createdBy, status);
            if (StringUtils.isNotBlank(createdTransactionId)) {
                createTransactionRecordHelper(dcRequest, createdTransactionId, map.get("TemplateRequestType_id"),
                        transactionRecords);
                createRequestHelper(dcRequest, debitAccount, companyId, date, createdBy,
                        "Pending", createdTransactionId, featureActionId);
            }
            

        } catch (Exception e) {

        }

    }

    public String createTemplateHelper(DataControllerRequest dcRequest, HashMap<String, String> template,
            String debitAccount, String companyId, String date, String createdUserId) {
        try {
            template.putAll(addToTemplate(debitAccount, companyId, date, createdUserId));
            
            ObjectMapper mapper = new ObjectMapper();
            BBTemplateDTO achtemplateDTO = null;
            BBTemplateDTO responseDTO = null;
            ACHTemplateBusinessDelegate achtemplateDelegate = DBPAPIAbstractFactoryImpl.getInstance()
        			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTemplateBusinessDelegate.class);

            achtemplateDTO = mapper.convertValue(template, BBTemplateDTO.class);
            
        	responseDTO = achtemplateDelegate.createTemplate(achtemplateDTO);
			
            String template_id = String.valueOf(responseDTO.getTemplateId());

            return template_id;
        } catch (Exception e) {
            return "";
        }

    }
    
    private String createTransactionHelper(DataControllerRequest dcRequest, HashMap<String, String> map,
            String debitAccount, String companyId, String date, String createdBy, String status) {
        try {
            if (map.containsKey("TemplateName")) {
                map.put("Template_id", createdTemplates.get(map.get("TemplateName")));
            }
            map.putAll(addToTransaction(debitAccount, companyId, date, createdBy, status));
            
            ObjectMapper mapper = new ObjectMapper();
            ACHTransactionDTO achTransactionDTO = null;
            ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
    				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class); 
            
            achTransactionDTO = mapper.convertValue(map, ACHTransactionDTO.class);
            
        	achTransactionDTO = achTransactionBusinessDelegate.createTransactionAtDBX(achTransactionDTO);
        	String transaction_id = achTransactionDTO.getTransaction_id();
        	
            return transaction_id;
        } catch (Exception e) {
            return "";
        }
    }

    private void createTransactionRecordHelper(DataControllerRequest dcRequest, String createdTransactionId,
            String TemplateRequestType_id, String transactionRecords) {
        try {
        	
        	List<ACHTransactionRecordDTO> queryData = null;
			queryData = getTransactionRecordDTOForCreation(transactionRecords, createdTransactionId+"", TemplateRequestType_id+"");            
            
			if(queryData != null) {
				ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	    				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);   
				
				achTransactionBusinessDelegate.createTransactionRecordAndSubRecords(queryData);
			}
        } catch (Exception e) {

        }

    }
    
    private List<ACHTransactionRecordDTO> getTransactionRecordDTOForCreation(String recordsData, String transactionId, String templateRequestType_id) {
		
		List<ACHTransactionRecordDTO> recordsDTOList = null;
		
		try {
			
			JSONArray records = new JSONArray(recordsData);
			int numberOfRecords = records.length();
			
			for(int i=0; i < numberOfRecords; i++) {
				JSONObject record = records.getJSONObject(i);
				record.put("transaction_id", transactionId);
				record.put("templateRequestType_id", templateRequestType_id);
			}
			recordsDTOList = JSONUtils.parseAsList(records.toString(), ACHTransactionRecordDTO.class);
			
		} catch (IOException e) {
			return null;
		}
		
		return recordsDTOList;
	}

    private void createRequestHelper(DataControllerRequest dcRequest, String debitAccount,
            String company_id, String date, String createdBy, String status, String transactionID, String featureActionId)
            throws HttpCallException {

    	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
				
    	ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
    			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

    	try {
	    	List<String> matrixIds = new ArrayList<String>();
	    	List<ApprovalMatrixDTO> approvalMatrixListDTO  =  approvalMatrixBusinessDelegate.fetchApprovalMatrixEntry(company_id,debitAccount,featureActionId);
			
	    	for(int i=0; i < approvalMatrixListDTO.size() ; i++) {
				matrixIds.add(approvalMatrixListDTO.get(i).getId());
			}
	    	
			BBRequestDTO bbRequestDTO = new BBRequestDTO(null, transactionID, matrixIds, featureActionId,
					company_id, debitAccount, "Pending", createdBy, 0, 0, "ALL");
			
			String requestId = approvalQueueDelegate.addTransactionToApprovalQueue(bbRequestDTO);
			
			switch(featureActionId) {
			case FeatureAction.ACH_PAYMENT_CREATE:
			case FeatureAction.ACH_COLLECTION_CREATE: 
				ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class); 
				achTransactionBusinessDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.BILL_PAY_CREATE:
				BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
				billpayTransactionDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
				InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				interbanktransferDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
				IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
				intrabankTransactionDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
				OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				ownaccountTransactionDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.P2P_CREATE:
				P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
				p2pTransactionDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
				InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				internationalFundTransferDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
				InternationalWireTransactionBusinessDelegate intWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
				intWireTransactionDelegate.updateRequestId(transactionID, requestId);
				break;
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
				DomesticWireTransactionBusinessDelegate domWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
				domWireTransactionDelegate.updateRequestId(transactionID, requestId);
				break;
			default:
				break;
			}
			
    	}
		catch(Exception e) {
		}
    }

    public HashMap<String, String> addToTemplate(String debitAccount, String company_id, String d, String createdUserId) {
        HashMap<String, String> addToTemplate = new HashMap<>();

        addToTemplate.put("DebitAccount", debitAccount);
        addToTemplate.put("companyId", company_id);
        addToTemplate.put("createdby", createdUserId);
        addToTemplate.put("Status", "Executed");
        if (d == null) {
            addToTemplate.put("createdts", HelperMethods.getCurrentTimeStamp());
            addToTemplate.put("EffectiveDate", HelperMethods.getCurrentTimeStamp());
        } else {
            addToTemplate.put("createdts", d);
            addToTemplate.put("EffectiveDate", d);
        }

        return addToTemplate;
    }

    public HashMap<String, String> addToTemplateRecord(String templateId, String TemplateTypeId) {
        HashMap<String, String> addToTemplateRecord = new HashMap<>();
        addToTemplateRecord.put("Template_id", templateId);
        addToTemplateRecord.put("TemplateRequestType_id", TemplateTypeId);

        return addToTemplateRecord;
    }

    public HashMap<String, String> addToRequest(String debitAccount, String company_id, String date, String createdBy,
            String status) {
        HashMap<String, String> addToRequest = new HashMap<>();
        addToRequest.put("companyId", company_id);
        addToRequest.put("createdby", createdBy);
        addToRequest.put("Status", status);
        addToRequest.put("accountId", debitAccount);

        if (date == null) {
            addToRequest.put("createdts", HelperMethods.getCurrentTimeStamp());
        } else {
            addToRequest.put("createdts", date);
        }

        return addToRequest;
    }

    public HashMap<String, String> requestTemplate(String requestTypeId, String status) {
        HashMap<String, String> inputForRequests = new HashMap<>();
        inputForRequests.put("RequestType_id", requestTypeId);
        inputForRequests.put("Status", status);

        return inputForRequests;
    }

    public HashMap<String, String> addToTransaction(String debitAccount, String company_id, String d, String createdBy,
            String Status) {
        HashMap<String, String> addToTransaction = new HashMap<>();

        addToTransaction.put("companyId", company_id);
        addToTransaction.put("createdby", createdBy);
        addToTransaction.put("Status", Status);
        addToTransaction.put("DebitAccount", debitAccount);

        if (d == null) {
            addToTransaction.put("createdts", HelperMethods.getCurrentTimeStamp());
        } else {
            addToTransaction.put("createdts", d);
        }

        return addToTransaction;
    }

    public void createACHFile1WithRequest(DataControllerRequest dcRequest, String companyId, String date,
            String createdBy, String status) {
        HashMap<String, String> map = DemoDataACH.getACHFile1(companyId, createdBy, date, status);
        try {
            Result result = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                    URLConstants.DMS_BB_REQUEST_CREATE);
            map.put("Request_id", HelperMethods.getFieldValue(result, "Request_id"));

            createACHFileHelper(map);
            
        } catch (HttpCallException e) {
        }
    }

    public void createACHFile2(DataControllerRequest dcRequest, String companyId, String date, String createdBy,
            String status) {
        HashMap<String, String> map = DemoDataACH.getACHFile2(companyId, createdBy, date, status);
        createACHFileHelper(map);
    }

    public void createACHFile3(DataControllerRequest dcRequest, String companyId, String date, String createdBy,
            String status) {
        HashMap<String, String> map = DemoDataACH.getACHFile3(companyId, createdBy, date, status);
        createACHFileHelper(map);
    }

    public void createACHFileHelper(HashMap<String, String> map) {
        ObjectMapper mapper = new ObjectMapper();
        ACHFileDTO achFileDTO = null;
        ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);
        achFileDTO = mapper.convertValue(map, ACHFileDTO.class);
        
        try {
        	achFileBusinessDelegate.uploadACHFileAtDBX(achFileDTO);
        } catch (Exception e) {
        }
    }
    
    public void createGeneralTransactionWithRequest(DataControllerRequest dcRequest, String featureActionId,
            String transactionData, String companyId, String date, String createdBy, DemoDataSB helper) {
	    try {
	        HashMap<String, String> inputParams = new HashMap<>();
	        inputParams = convertJsonToMap(transactionData);
	        inputParams.put("featureActionId", featureActionId);
			inputParams.put("companyId", companyId);
			inputParams.put("createdby", createdBy);
			inputParams.put("status", "Pending");
			String transactionId = null;
			String fromAccount = null;
			
			switch(featureActionId) {
			case FeatureAction.BILL_PAY_CREATE:
				BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
				BillPayTransactionDTO billpayDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Pro Business Checking"));
				inputParams.put("toAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				inputParams.put("payeeId", helper.getPayee("payee4"));
				inputParams.put("payeeName", "AT&T Mobile");
				billpayDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), BillPayTransactionDTO.class);
				billpayDTO = billpayTransactionDelegate.createTransactionAtDBX(billpayDTO);
				transactionId = billpayDTO.getTransactionId();
				fromAccount = billpayDTO.getFromAccountNumber();
				break;
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
				InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
				InterBankFundTransferDTO interbankDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				inputParams.put("toAccountNumber", helper.getExternalAccountNum("Alex Sion"));
				inputParams.put("ExternalAccountNumber", helper.getExternalAccountNum("Alex Sion"));
				interbankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InterBankFundTransferDTO.class);
				interbankDTO = interbanktransferDelegate.createTransactionAtDBX(interbankDTO);
				transactionId = interbankDTO.getTransactionId();
				fromAccount = interbankDTO.getFromAccountNumber();
				break;
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
				IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
				IntraBankFundTransferDTO intrabankDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Progress Business Checking"));
				inputParams.put("toAccountNumber", helper.getAccountNum("Pro Business Checking"));
				intrabankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IntraBankFundTransferDTO.class);
				intrabankDTO = intrabankTransactionDelegate.createTransactionAtDBX(intrabankDTO);
				transactionId = intrabankDTO.getTransactionId();
				fromAccount = intrabankDTO.getFromAccountNumber();
				break;
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
				OwnAccountFundTransferBusinessDelegate ownaccountTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
				OwnAccountFundTransferDTO ownaccountDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				inputParams.put("toAccountNumber", helper.getAccountNum("Pro Business Checking"));
				ownaccountDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), OwnAccountFundTransferDTO.class);
				ownaccountDTO = ownaccountTransactionDelegate.createTransactionAtDBX(ownaccountDTO);
				transactionId = ownaccountDTO.getTransactionId();
				fromAccount = ownaccountDTO.getFromAccountNumber();
				break;
			case FeatureAction.P2P_CREATE:
				P2PTransactionBusinessDelegate p2pTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PTransactionBusinessDelegate.class);
				P2PTransactionDTO p2pDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				inputParams.put("personId", helper.getPerson("person1"));
				inputParams.put("payPersonName", "Judy Blume");
				p2pDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PTransactionDTO.class);
				p2pDTO = p2pTransactionDelegate.createTransactionAtDBX(p2pDTO);
				transactionId = p2pDTO.getTransactionId();
				fromAccount = p2pDTO.getFromAccountNumber();
				break;
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
				InternationalFundTransferBusinessDelegate internationalFundTransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
				InternationalFundTransferDTO internationalDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				inputParams.put("ExternalAccountNumber", helper.getExternalAccountNum("Henry James"));
				inputParams.put("toAccountNumber", helper.getExternalAccountNum("Henry James"));
				internationalDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalFundTransferDTO.class);
				internationalDTO = internationalFundTransferDelegate.createTransactionAtDBX(internationalDTO);
				transactionId = internationalDTO.getTransactionId();
				fromAccount = internationalDTO.getFromAccountNumber();
				break;
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
				InternationalWireTransactionBusinessDelegate internationalWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
				WireTransactionDTO intWireDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				intWireDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), WireTransactionDTO.class);
				intWireDTO = internationalWireTransactionDelegate.createTransactionAtDBX(intWireDTO);
				transactionId = intWireDTO.getTransactionId();
				fromAccount = intWireDTO.getFromAccountNumber();
				break;
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
				DomesticWireTransactionBusinessDelegate domesticWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
				WireTransactionDTO domWireDTO = null;
				inputParams.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
				domWireDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), WireTransactionDTO.class);
				domWireDTO = domesticWireTransactionDelegate.createTransactionAtDBX(domWireDTO);
				transactionId = domWireDTO.getTransactionId();
				fromAccount = domWireDTO.getFromAccountNumber();
				break;
			default:
				break;
			}
			
			if(transactionId != null && !"".equals(transactionId) && fromAccount != null && !"".equals(fromAccount))
				createRequestHelper(dcRequest, fromAccount, companyId, date, createdBy, "Pending", transactionId, featureActionId);
	        
	    } catch (Exception e) {
	
	    }

    }


    public HashMap<String, String> convertJsonToMap(JSONObject json) {
        HashMap<String, String> params = new HashMap<>();

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = json.get(key).toString();
            params.put(key, value);
        }

        return params;
    }

    public HashMap<String, String> convertJsonToMap(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        return convertJsonToMap(json);
    }

    public String getFormattedTimeStampForRecords(String dt) {

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat returnFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        expectedFormat.setLenient(false);
        try {
            Date d = expectedFormat.parse(dt);
            return returnFormat.format(d).toString();
        } catch (Exception e) {
            return HelperMethods.getCurrentTimeStamp();
        }
    }

    public String getFormattedDateRecords(String dt) {

        SimpleDateFormat returnFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        expectedFormat.setLenient(false);
        try {
            Date d = expectedFormat.parse(dt);
            return returnFormat.format(d).toString();
        } catch (Exception e) {
            return HelperMethods.getCurrentTimeStamp();
        }
    }

}
