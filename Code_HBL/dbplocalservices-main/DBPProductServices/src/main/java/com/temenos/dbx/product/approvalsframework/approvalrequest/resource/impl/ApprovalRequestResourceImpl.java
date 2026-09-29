package com.temenos.dbx.product.approvalsframework.approvalrequest.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBActedRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.api.ApprovalRequestBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalrequest.resource.api.ApprovalRequestResource;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.PendingRequestApproversDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.RequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.RequestHistoryDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.resource.api.BulkPaymentRecordResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.payeeservices.resource.api.InterBankPayeeResource;
import com.temenos.dbx.product.payeeservices.resource.api.InternationalPayeeResource;
import com.temenos.dbx.product.payeeservices.resource.api.IntraBankPayeeResource;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.OwnAccountFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.commons.backenddelegate.api.UserBackendDelegate;
import com.temenos.dbx.product.payeeservices.utils.PayeeUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class ApprovalRequestResourceImpl implements ApprovalRequestResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    ApprovalRequestBusinessDelegate approvalRequestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalRequestBusinessDelegate.class);
    TransactionLimitsBusinessDelegate transactionLimitsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
    FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
    AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
    ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
    CustomerBusinessDelegate custDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	BulkPaymentRecordResource bulkPaymentRecordResource = DBPAPIAbstractFactoryImpl.getResource(BulkPaymentRecordResource.class);
	ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileBusinessDelegate.class);
	ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
	BillPayTransactionBusinessDelegate billPayTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
	P2PTransactionBusinessDelegate p2PTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
	InterBankFundTransferBusinessDelegate interBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	IntraBankFundTransferBusinessDelegate intraBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
	DomesticWireTransactionBusinessDelegate domesticWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
	InternationalWireTransactionBusinessDelegate internationalWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
	InternationalFundTransferBusinessDelegate internationalFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
	OwnAccountFundTransferBusinessDelegate ownAccountFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	BulkPaymentRecordBusinessDelegate bulkPaymentRecordBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentRecordBusinessDelegate.class);
	BulkPaymentRecordBackendDelegate bulkPaymentRecordBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
	GeneralTransactionsBusinessDelegate generalTransactionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
	FeatureActionBusinessDelegate FeatureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
	ACHCommonsBusinessDelegate aCHCommonsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
	SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
	UserBackendDelegate userBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(UserBackendDelegate.class);

    @Override
    public Result validateForApprovals(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception {

        Result result = new Result();
        String confirmationNumber = requestMap.containsKey("confirmationNumber") ? (String) requestMap.get("confirmationNumber") : null;
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        String companyId = requestMap.containsKey("companyId") ? (String) requestMap.get("companyId") : null;
        String accountId = requestMap.containsKey("accountId") ? (String) requestMap.get("accountId") : null;
        String statusStr = requestMap.containsKey("status") ? (String) requestMap.get("status") : null;
        String featureActionId = requestMap.containsKey("featureActionID") ? (String) requestMap.get("featureActionID") : null;
        String amountStr = requestMap.containsKey("amount") ? (String) requestMap.get("amount") : null;
        String date = requestMap.containsKey("date") ? (String) requestMap.get("date") : "";
        String transactionCurrency = requestMap.containsKey("transactionCurrency") ? (String) requestMap.get("transactionCurrency") : null;
        String prevRequestId = requestMap.containsKey("requestId") ? (String) requestMap.get("requestId") : null;
        String offsetDetails = requestMap.containsKey("offsetDetails") ? (String) requestMap.get("offsetDetails") : null;
        String serviceCharge = requestMap.containsKey("serviceCharge") ? (String) requestMap.get("serviceCharge") : null;
        String contractCifMapStr = requestMap.containsKey("contractCifMap") ? (String) requestMap.get("contractCifMap") : null;
        String additionalMetaInfoStr = requestMap.containsKey("additionalMetaInfo") ? (String) requestMap.get("additionalMetaInfo") : null;
        TransactionStatusEnum status = null;
        /**
         * contractCifMap is the unified map which will take care of request creation of multiple contracts and cifs
         * Structure: <contractId, <coreCustomerId, <"companyId", companyId>>>
         */
        Map<String, Map<String, Map<String, String>>> contractCifMap = new HashMap<>();

        if (featureActionId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87103);
        }
        if (confirmationNumber == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87202);
        }
        if (statusStr == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87206);
        }
        try {
            JSONObject statusJSON = new JSONObject(statusStr);
            String tempStatus = statusJSON.optString("status", null);
            if (tempStatus == null) {
                throw new ApplicationException(ErrorCodeEnum.ERR_87206);
            }
            switch (tempStatus) {
                case "New":
                    status = TransactionStatusEnum.NEW;
                    break;
                case "Pending":
                    status = TransactionStatusEnum.PENDING;
                    break;
                case "Approved":
                    status = TransactionStatusEnum.APPROVED;
                    break;
                case "Self Approved":
                    status = TransactionStatusEnum.SELF_APPROVED;
                    break;
                case "Invalid":
                    status = TransactionStatusEnum.INVALID;
                    break;
                case "Rejected":
                    status = TransactionStatusEnum.REJECTED;
                    break;
                case "Sent":
                    status = TransactionStatusEnum.SENT;
                    break;
                case "Failed":
                    status = TransactionStatusEnum.FAILED;
                    break;
                case "Executed":
                    status = TransactionStatusEnum.EXECUTED;
                    break;
                case "Withdrawn":
                    status = TransactionStatusEnum.WITHDRAWN;
                    break;
                case "Cancelled":
                    status = TransactionStatusEnum.CANCELLED;
                    break;
                case "Discarded":
                    status = TransactionStatusEnum.DISCARDED;
                    break;
                case "Edited":
                    status = TransactionStatusEnum.EDITED;
                    break;
                case "Ready":
                    status = TransactionStatusEnum.READY;
                    break;
                case "Created":
                    status = TransactionStatusEnum.CREATED;
                    break;
                case "Uploaded":
                    status = TransactionStatusEnum.UPLOADED;
                    break;
                case "Ready for Execution":
                    status = TransactionStatusEnum.READY_FOR_EXECUTION;
                    break;
                case "Newly Added":
                    status = TransactionStatusEnum.NEWLY_ADDED;
                    break;
                case "Modified":
                    status = TransactionStatusEnum.MODIFIED;
                    break;
                case "OldTransaction":
                    status = TransactionStatusEnum.OLD_TRANSACTION;
                    break;
                default:
                    status = TransactionStatusEnum.NEW;
            }
        } catch (JSONException je) {
            alert.prepareError("Error parsing status for ValidateForApprovals call: " + je).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_87206);
        }
        FeatureActionDTO featureActionDTO = featureActionBusinessDelegate.getFeatureActionById(featureActionId);
        if (featureActionDTO == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87104);
        }

        Map<String, Object> customerSessionMap = CustomerSession.getCustomerMap(dcRequest);
        if (!CustomerSession.IsAPIUser(customerSessionMap)) {
            // ADMIN user is always allowed to perform operations on managing approval matrix
            customerId = CustomerSession.getCustomerId(customerSessionMap);
        }

        Set<String> customerPermittedActionIds = CustomerSession.getPermittedActionIdsSet(dcRequest);
        if (customerPermittedActionIds == null) {
            return ErrorCodeEnum.ERR_28025.setErrorCode(result);
        }
        if (!customerPermittedActionIds.contains(featureActionDTO.getFeatureActionId())) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
        Double amount = null;
        JSONArray contractCifMapJSON = null;
        /**
         * decision-making - if feature is at an account-level, then proceed for account-level data fields check
         */
        boolean isAccountLevel = featureActionDTO.getIsAccountLevel().equalsIgnoreCase("true") ? true : false;
        Set<String> limitTypeIdsToCheck = new HashSet<>();
        Map<String, Double> exhaustedAmounts = new HashMap<>();
        if (isAccountLevel) {
            if (StringUtils.isEmpty(accountId)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_87207);
            }
            /**
             * amount and transaction currency are mandatory fields for an account-level monetary action
             */
            if (featureActionDTO.getTypeId().equals(Constants.MONETARY_ACTIONTYPE)) {
                if (StringUtils.isEmpty(amountStr)) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_87204);
                }
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException nme) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_87205);
                }
                if (StringUtils.isEmpty(transactionCurrency)) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_87211);
                }
            }

            /**
             * @note for an account-level action, there is only one contract and core customer involved in the approval process, since an account can be associated to one and only one cif and contract
             */
            CustomerAccountsDTO accountsDTO = accountBusinessDelegate.getAccountDetails(customerId, accountId);
            contractCifMap.put(accountsDTO.getContractId(), new HashMap<>());
            contractCifMap.get(accountsDTO.getContractId()).put(accountsDTO.getCoreCustomerId(), new HashMap<>());
            contractCifMap.get(accountsDTO.getContractId()).get(accountsDTO.getCoreCustomerId()).put("companyId", StringUtils.isEmpty(companyId) ? (accountsDTO.getContractId() + "_" + accountsDTO.getCoreCustomerId()) : companyId);

            if (featureActionDTO.getTypeId().equals(Constants.MONETARY_ACTIONTYPE)) {
                TransactionStatusDTO limitsValidationStatusDTO = transactionLimitsBusinessDelegate.validateLimitsForMonetaryAction(customerId, accountsDTO.getContractId(), accountsDTO.getCoreCustomerId(), featureActionId, accountId, amount, serviceCharge, date, transactionCurrency, status, dcRequest);

                // Perform check on pre-approve limits
                Double userPAMaxLimit = limitsValidationStatusDTO.getUserAccountLevelLimits().get(Constants.PRE_APPROVED_TRANSACTION_LIMIT);
                Double userPADailyLimit = limitsValidationStatusDTO.getUserAccountLevelLimits().get(Constants.PRE_APPROVED_DAILY_LIMIT);
                Double userPAWeeklyLimit = limitsValidationStatusDTO.getUserAccountLevelLimits().get(Constants.PRE_APPROVED_WEEKLY_LIMIT);

                Double userExhaustedDaily = amount + limitsValidationStatusDTO.getUserExhaustedLimits().getDailyLimit();
                Double userExhaustedWeekly = amount + limitsValidationStatusDTO.getUserExhaustedLimits().getWeeklyLimit();
                exhaustedAmounts.put(Constants.MAX_TRANSACTION_LIMIT, amount);
                exhaustedAmounts.put(Constants.DAILY_LIMIT, userExhaustedDaily);
                exhaustedAmounts.put(Constants.WEEKLY_LIMIT, userExhaustedWeekly);

                if (amount > userPAMaxLimit) {
                    // needs approval
                    limitTypeIdsToCheck.add(Constants.MAX_TRANSACTION_LIMIT);
                }
                if (userExhaustedDaily > userPADailyLimit) {
                    // needs approval
                    limitTypeIdsToCheck.add(Constants.DAILY_LIMIT);
                }
                if (userExhaustedWeekly > userPAWeeklyLimit) {
                    // needs approval
                    limitTypeIdsToCheck.add(Constants.WEEKLY_LIMIT);
                }
                if (limitTypeIdsToCheck.size() == 0) {
                    result.addParam("status", TransactionStatusEnum.SENT.getStatus());
                    result.addParam("transactionId", confirmationNumber);
                    return result;
                }
            }
        } else {
            /**
             * @note for a customer-level action, there could be more than one contract(s) and core customer(s) involved in the approval process
             */
            if (contractCifMapStr == null) {
                throw new ApplicationException(ErrorCodeEnum.ERR_87208);
            }
            try {
                contractCifMapJSON = requestMap.containsKey("contractCifMap") ? new JSONArray((String) requestMap.get("contractCifMap")) : null;
            } catch (JSONException je) {
                throw new ApplicationException(ErrorCodeEnum.ERR_87209);
            }
            contractCifMap = ApprovalUtilities.fetchContractCifMapFromJSON(contractCifMapJSON);
        }

        JSONObject additionalMeta;

        try{
            additionalMeta = new JSONObject(additionalMetaInfoStr);
        } catch (JSONException je){
            additionalMeta = new JSONObject();
        } catch (NullPointerException npe){
            additionalMeta = new JSONObject();
        }

        Map<String, Map<String, Set<String>>> contractCifTalliedMatrixIds = approvalRequestBusinessDelegate.validateForApprovalRules(customerId, contractCifMap, featureActionDTO, featureActionDTO.getTypeId(), isAccountLevel, accountId, status, limitTypeIdsToCheck, exhaustedAmounts);

        List<RequestDTO> requests = approvalRequestBusinessDelegate.createNewBBRequestForTalliedMatrixIds(customerId, contractCifTalliedMatrixIds, confirmationNumber, featureActionId, accountId, amount != null ? amount.toString() : null, serviceCharge, additionalMeta, Constants.DEFAULT_COMMENT_REQUEST_CREATED);


        /**
         * @note after request creation, check for SelfApproval application config. If SelfApproval is enabled, then proceed for approval of the given requests created
         */
        boolean isSelfApprovalPerformed = false;
        if (!requests.isEmpty()) {
            boolean isSelfApprovalEnabled = applicationBusinessDelegate.getIsSelfApprovalEnabledFromCache();

            if (isSelfApprovalEnabled) {
                try {
                    requests = approvalRequestBusinessDelegate.approveCompositeRequests(customerId, customerPermittedActionIds, requests, Constants.DEFAULT_COMMENT_SELFAPPROVED);
                    isSelfApprovalPerformed = true;
                } catch (ApplicationException ae) {
                    // self-approval failed as user does not have approval permissions for the requests the user created
                    isSelfApprovalPerformed = false;
                }
            }
        }

        for (RequestDTO request : requests) {
            // Fill in request data in contractCifMap, based on the contract and cif for which the request was created
            // This is done to keep track of all the requests created (or not) for the specified cifs
            contractCifMap.get(request.getContractId()).get(request.getCoreCustomerId()).put(Constants.REQUESTID, request.getRequestId());
            contractCifMap.get(request.getContractId()).get(request.getCoreCustomerId()).put(Constants.ASSOCREQUESTID, request.getAssocRequestId());
            contractCifMap.get(request.getContractId()).get(request.getCoreCustomerId()).put(Constants.STATUS, request.getStatus());
            contractCifMap.get(request.getContractId()).get(request.getCoreCustomerId()).put(Constants.TRANSACTIONID, request.getTransactionId());
            contractCifMap.get(request.getContractId()).get(request.getCoreCustomerId()).put(Constants.CONFIRMATION_NUMBER, request.getTransactionId());
        }

        /**
         * @note for the generated contractCifMap, compare with the generated list of requests to determine whether requests were raised for those cifs,
         * and, if so, send the request ids and their respective status generated for that cif
         * Response expectation (for multiple requests generated):
         * "Requests": [
         *      {
         *          "requestId": "",
         *          "assocRequestId: "",
         *          "transactionId": "",
         *          "confirmationNumber": "",
         *          "contractId": "",
         *          "coreCustomerId": "",
         *          "status": ""
         *      },...
         * ]
         */
        Dataset requestSet = new Dataset("Requests");
        Record requestDataRecord;
        String assocRequestId = null;
        String assocStatus = null;
        for (Map.Entry<String, Map<String, Map<String, String>>> contractEntry : contractCifMap.entrySet()) {
            String contractId = contractEntry.getKey();
            for (Map.Entry<String, Map<String, String>> cifEntry : contractEntry.getValue().entrySet()) {
                String cifId = cifEntry.getKey();
                Map<String, String> requestData = cifEntry.getValue();
                requestDataRecord = new Record();
                requestDataRecord.addParam(Constants.CONTRACTID, contractId);
                requestDataRecord.addParam(Constants.CORECUSTOMERID, cifId);
                if (requestData.containsKey(Constants.REQUESTID)) {
                    requestDataRecord.addParam(Constants.REQUESTID, requestData.get(Constants.REQUESTID));
                    assocRequestId = requestData.get(Constants.ASSOCREQUESTID);
                    requestDataRecord.addParam(Constants.ASSOCREQUESTID, requestData.get(Constants.ASSOCREQUESTID));
                    requestDataRecord.addParam(Constants.STATUS, requestData.get(Constants.STATUS));
                    if (assocStatus == null) {
                        assocStatus = requestData.get(Constants.STATUS);
                    } else {
                        if (requestData.get(Constants.STATUS).equalsIgnoreCase("PENDING") && assocStatus.equalsIgnoreCase("APPROVED")) {
                            assocStatus = requestData.get(Constants.STATUS);
                        }
                    }
                    requestDataRecord.addParam(Constants.TRANSACTIONID, requestData.get(Constants.TRANSACTIONID));
                    requestDataRecord.addParam(Constants.CONFIRMATION_NUMBER, requestData.get(Constants.CONFIRMATION_NUMBER));
                } else {
                    requestDataRecord.addParam(Constants.STATUS, TransactionStatusEnum.SENT.getStatus());
                }
                requestSet.addRecord(requestDataRecord);
            }
        }
        result.addDataset(requestSet);
        result.addParam("transactionId", confirmationNumber);
        if (assocRequestId == null) {
            result.addParam("status", TransactionStatusEnum.SENT.getStatus());
        } else {
            result.addParam("status", assocStatus);
            result.addParam("requestId", assocRequestId);
            result.addParam("isSelfApproved", String.valueOf(isSelfApprovalPerformed));
        }
        return result;
    }

    @Override
    public Result approveRequest(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception {
        // Request ID could be comma-separated requests as well
        Result result = new Result();
        String assocRequestIdsStr = requestMap.containsKey("requestId") ? (String) requestMap.get("requestId") : null;
        String comments = requestMap.containsKey("comments") ? requestMap.get("comments") != null ? (String) requestMap.get("comments") : Constants.DEFAULT_COMMENT_APPROVED : Constants.DEFAULT_COMMENT_APPROVED;
        if (assocRequestIdsStr == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87402);
        }
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        Map<String, Object> customerSessionMap = CustomerSession.getCustomerMap(dcRequest);
        if (!CustomerSession.IsAPIUser(customerSessionMap)) {
            // ADMIN user is always allowed to perform operations on approving a request
            customerId = CustomerSession.getCustomerId(customerSessionMap);
        }
        Set<String> assocRequestIds = new HashSet<>(Arrays.asList(assocRequestIdsStr.split(",")));
        if (assocRequestIds.size() > 1) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87403);
        }

        Map<String, Map<String, Map<String, String>>> contractCifMap = approvalRequestBusinessDelegate.fetchContractCifMapForCustomer(customerId);

        // Fetch existing request details for the given set of requestIds and the contract cif association of the customer
        List<RequestDTO> requests = approvalRequestBusinessDelegate.fetchRequestsWithApprovalMatrixInfo(assocRequestIds, true, contractCifMap, false);
        if (requests.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87400);
        }
        Set<String> customerPermittedActionIds = CustomerSession.getPermittedActionIdsSet(dcRequest);

        if (customerPermittedActionIds == null) {
            return ErrorCodeEnum.ERR_28025.setErrorCode(result);
        }

        // pass these fields to the Approve-function business delegate logic
        List<RequestDTO> approvalResponse = approvalRequestBusinessDelegate.approveCompositeRequests(customerId, customerPermittedActionIds, requests, comments);

        /**
         * @note baseline assumption is that for given set of request ID's being approved, all have a common assocRequestId, transactionId, and additionalMeta information
         */
        Dataset requestSet = new Dataset("Requests");
        String assocRequestId = null;
        String assocStatus = null;
        String transactionId = null;
        String featureActionId = null;
        String additionalMetaStr = null;
        String assocCoreCustomerId = null;
        for (RequestDTO request : approvalResponse) {
            requestSet.addRecord(request.getAsRecord());
            if (assocRequestId == null) {
                assocRequestId = request.getAssocRequestId();
            }
            if (assocStatus == null) {
                assocStatus = request.getStatus();
            } else {
                if (request.getStatus().equalsIgnoreCase("PENDING") && assocStatus.equalsIgnoreCase("APPROVED")) {
                    assocStatus = request.getStatus();
                }
            }
            if (transactionId == null) {
                transactionId = request.getTransactionId();
            }
            if (featureActionId == null) {
                featureActionId = request.getFeatureActionId();
            }
            if (additionalMetaStr == null) {
                additionalMetaStr = request.getAdditionalMeta();
            }
            if (assocCoreCustomerId == null) {
            	assocCoreCustomerId = request.getCoreCustomerId();
            }
        }

        JSONObject additionalMeta = null;
        try {
            additionalMeta = new JSONObject(additionalMetaStr);
        } catch (JSONException je) {
            alert.prepareError("Error parsing additionalMeta for the given request ID: " + assocRequestId).log();
        }
        if (assocStatus.equalsIgnoreCase("APPROVED")) {
            // TODO: call the create/edit beneficiary after approval integration service handler
            InternationalPayeeResource internationalPayeeResource = DBPAPIAbstractFactoryImpl.getResource(InternationalPayeeResource.class);
            InterBankPayeeResource interBankPayeeResource = DBPAPIAbstractFactoryImpl.getResource(InterBankPayeeResource.class);
            IntraBankPayeeResource intraBankPayeeResource = DBPAPIAbstractFactoryImpl.getResource(IntraBankPayeeResource.class);
            switch (featureActionId) {
                case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT:
                    internationalPayeeResource.createPayeeAfterApproval(dcRequest, additionalMeta,false);
                    break;
                case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT:
                    internationalPayeeResource.deletePayeeAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE:
                    internationalPayeeResource.editPayeeLinkageAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL:
                    internationalPayeeResource.editPayeeOptionalFieldsAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT:
                    interBankPayeeResource.createPayeeAfterApproval(dcRequest, additionalMeta,false);
                    break;
                case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT:
                    interBankPayeeResource.deletePayeeAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE:
                    interBankPayeeResource.editPayeeLinkageAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL:
                    interBankPayeeResource.editPayeeOptionalFieldsAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT:
                    intraBankPayeeResource.createPayeeAfterApproval(dcRequest, additionalMeta,false);
                    break;
                case FeatureAction.INTRA_BANK_FUND_TRANSFER_DELETE_RECEPIENT:
                    intraBankPayeeResource.deletePayeeAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE:
                    intraBankPayeeResource.editPayeeLinkageAfterApproval(dcRequest, additionalMeta);
                    break;
                case FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL:
                    intraBankPayeeResource.editPayeeOptionalFieldsAfterApproval(dcRequest, additionalMeta);
                default: break;
            }
            result.addParam("featureActionId", featureActionId);
            formatAndPushEvent(result, dcRequest, "Approved", assocCoreCustomerId, null, null); 
        }

        result.addDataset(requestSet);
        result.addParam("requestId", assocRequestId);
        result.addParam("status", assocStatus);
        result.addParam("transactionId", transactionId);
        
        return result;
    }

    @Override
    public Result rejectRequest(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception {
        // Request ID could be comma-separated requests as well
        Result result = new Result();
        String assocRequestIdsStr = requestMap.containsKey("requestId") ? (String) requestMap.get("requestId") : null;
        String comments = requestMap.containsKey("comments") ? requestMap.get("comments") != null ? (String) requestMap.get("comments") : Constants.DEFAULT_COMMENT_REJECTED : Constants.DEFAULT_COMMENT_REJECTED;
        if (assocRequestIdsStr == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87402);
        }
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        Map<String, Object> customerSessionMap = CustomerSession.getCustomerMap(dcRequest);
        String approverUsername = CustomerSession.getCustomerCompleteName(customerSessionMap);
        if (!CustomerSession.IsAPIUser(customerSessionMap)) {
            // ADMIN user is always allowed to perform operations on rejecting a request
            customerId = CustomerSession.getCustomerId(customerSessionMap);
        }
        Set<String> assocRequestIds = new HashSet<>(Arrays.asList(assocRequestIdsStr.split(",")));
        if (assocRequestIds.size() > 1) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87403);
        }

        // For Rejection - Fetch all the associated existing request details
        List<RequestDTO> requests = approvalRequestBusinessDelegate.fetchRequestsWithApprovalMatrixInfo(assocRequestIds, true, null, false);
        if (requests.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87400);
        }
        Set<String> customerPermittedActionIds = CustomerSession.getPermittedActionIdsSet(dcRequest);
        if (customerPermittedActionIds == null) {
            return ErrorCodeEnum.ERR_28025.setErrorCode(result);
        }

        // pass these fields to the Approve-function business delegate logic
        List<RequestDTO> approvalResponse = approvalRequestBusinessDelegate.rejectCompositeRequests(customerId, customerPermittedActionIds, requests, comments);

        /**
         * @note baseline assumption is that for given set of request ID's being approved, all have a common assocRequestId, transactionId, and additionalMeta information
         */
        Dataset requestSet = new Dataset("Requests");
        String assocRequestId = null;
        String assocStatus = null;
        String transactionId = null;
        String featureActionId = null;
        String initiatorCustomerId = null;
        for (RequestDTO request : approvalResponse) {
            requestSet.addRecord(request.getAsRecord());
            if (assocRequestId == null) {
                assocRequestId = request.getAssocRequestId();
            }
            if (assocStatus == null) {
                assocStatus = request.getStatus();
            } else {
                if (request.getStatus().equalsIgnoreCase("PENDING") && assocStatus.equalsIgnoreCase("APPROVED")) {
                    assocStatus = request.getStatus();
                }
            }
            if (transactionId == null) {
                transactionId = request.getTransactionId();
            }
            if (featureActionId == null) {
                featureActionId = request.getFeatureActionId();
            }
            if (initiatorCustomerId == null) {
            	initiatorCustomerId = request.getCreatedBy();
            }
        }
        result.addDataset(requestSet);
        result.addParam("requestId", assocRequestId);
        result.addParam("status", assocStatus);
        result.addParam("transactionId", transactionId);
        result.addParam("featureActionId", featureActionId);
        formatAndPushEvent(result, dcRequest, "Rejected", initiatorCustomerId, null, approverUsername);
        return result;
    }

    @Override
    public Result withdrawRequest(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception {
        Result result = new Result();
        String assocRequestIdsStr = requestMap.containsKey("requestId") ? (String) requestMap.get("requestId") : null;
        String comments = Constants.DEFAULT_COMMENT_WITHDRAWN;

        try {
            if (requestMap.containsKey("comments")) {
                if (requestMap.get("comments") != null) {
                    comments = (String) requestMap.get("comments");
                }
            }
        } catch (Exception e) {
            alert.prepareError("withdrawRequest : Approvals Framework 2.0 : unable to get comments").log();
        }

        if (assocRequestIdsStr == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87402);
        }
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        Map<String, Object> customerSessionMap = CustomerSession.getCustomerMap(dcRequest);
        if (!CustomerSession.IsAPIUser(customerSessionMap)) {
            // ADMIN user is always allowed to perform operations on managing approval matrix
            customerId = CustomerSession.getCustomerId(customerSessionMap);
        }
        Set<String> assocRequestIds = new HashSet<>(Arrays.asList(assocRequestIdsStr.split(",")));
        if (assocRequestIds.size() > 1) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87403);
        }

        List<RequestDTO> requests = approvalRequestBusinessDelegate.fetchRequestsWithApprovalMatrixInfo(assocRequestIds, true, null, false);
        if (requests.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87400);
        }
        Set<String> customerPermittedActionIds = CustomerSession.getPermittedActionIdsSet(dcRequest);
        if (customerPermittedActionIds == null) {
            return ErrorCodeEnum.ERR_28025.setErrorCode(result);
        }
        // pass these fields to the Approve-function business delegate logic
        List<RequestDTO> approvalResponse = approvalRequestBusinessDelegate.withdrawCompositeRequests(customerId, customerPermittedActionIds, requests, comments);

        /**
         * @note baseline assumption is that for given set of request ID's being approved, all have a common assocRequestId, transactionId, and additionalMeta information
         */
        Dataset requestSet = new Dataset("Requests");
        String assocRequestId = null;
        String assocStatus = null;
        String transactionId = null;
        String featureActionId = null;
        for (RequestDTO request : approvalResponse) {
            requestSet.addRecord(request.getAsRecord());
            if (assocRequestId == null) {
                assocRequestId = request.getAssocRequestId();
            }
            if (assocStatus == null) {
                assocStatus = request.getStatus();
            } else {
                if (request.getStatus().equalsIgnoreCase("PENDING") && assocStatus.equalsIgnoreCase("APPROVED")) {
                    assocStatus = request.getStatus();
                }
            }
            if (transactionId == null) {
                transactionId = request.getTransactionId();
            }
            if (featureActionId == null) {
                featureActionId = request.getFeatureActionId();
            }
        }
        result.addDataset(requestSet);
        result.addParam("requestId", assocRequestId);
        result.addParam("status", assocStatus);
        result.addParam("transactionId", transactionId);
        result.addParam("featureActionId", featureActionId);
        formatAndPushEvent(result, dcRequest, "Withdrawn", customerId, comments, null);
        return result;
    }

    @Override
    public Result fetchAllPendingRequests(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {

        Result result = new Result();
        FilterDTO newFilterDTO = new FilterDTO();
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        
        List<String> requiredActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_VIEW,
				FeatureAction.BILL_PAY_VIEW_PAYMENTS,
				FeatureAction.P2P_VIEW,
				FeatureAction.ACH_FILE_VIEW,
				FeatureAction.ACH_COLLECTION_VIEW,
				FeatureAction.ACH_PAYMENT_VIEW,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW);
		List<String> requiredActionIdsToFetch = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE,
				FeatureAction.P2P_APPROVE,
				FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE,
				FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
		
		if(customerId == null) {
			alert.prepareError("fetchAllPendingRequests customer Id is null").log();
	        Map<String, Object> customerMap = CustomerSession.getCustomerMap(dcRequest);
	        if (!CustomerSession.IsAPIUser(customerMap)) {
	            // fetch the customerId from session map, if not a Spotlight user
	            customerId = CustomerSession.getCustomerId(customerMap);
	        }
		}
        String permittedFeatureActionIds = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);
        
        if (StringUtils.isEmpty(permittedFeatureActionIds)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}

        
        
        try {
			String removeByValue = requestMap.get("removeByValue") != null ? requestMap.get("removeByValue").toString() : null;
			
			if(StringUtils.isNotEmpty(removeByValue)) {
				requestMap.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(requestMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
        
        
        List<ApprovalRequestDTO> mainRequests = approvalRequestBusinessDelegate.fetchAllPendingRequests(customerId, new HashSet<>(requiredActionIdsToFetch));

        if(mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21250.setErrorCode(new Result());
		}

		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_filterByParam("assocStatus,amICreator");
		filterDTO.set_filterByValue("Pending,true");

		List<ApprovalRequestDTO> subRequests = filterDTO.filter(mainRequests);
		List<BBRequestDTO> subBBRequests = ApprovalUtilities.convertApprovalRequestDTOToBBRequestDTO(subRequests);
		List<ApprovalRequestDTO> records = fetchAllRequests(subBBRequests, dcRequest);
		records = newFilterDTO.filter(records);

		try {
			JSONArray resultRecords = new JSONArray(records);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.RECORDS, resultRecords);
			result = JSONToResult.convert(resultObject.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the JSON to result", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(result);
		}

		return result;
    }

    @Override
    public Result fetchAllPendingApprovals(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {

        Result result = new Result();
        FilterDTO newFilterDTO = new FilterDTO();
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        
        List<String> requiredActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE,
				FeatureAction.P2P_APPROVE,
				FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE,
				FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
        
        Map<String, Object> customerMap = CustomerSession.getCustomerMap(dcRequest);
        String permittedFeatureActionIds = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);

        if (StringUtils.isEmpty(permittedFeatureActionIds)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}

		try {
			String removeByValue = requestMap.get("removeByValue") != null ? requestMap.get("removeByValue").toString() : null;

			if(StringUtils.isNotEmpty(removeByValue)) {
				requestMap.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(requestMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
        
        if (!CustomerSession.IsAPIUser(customerMap)) {
            // fetch the customerId from session map, if not a Spotlight user
            customerId = CustomerSession.getCustomerId(customerMap);
        }

        List<ApprovalRequestDTO> mainRequests = approvalRequestBusinessDelegate.fetchAllPendingApprovals(customerId, new HashSet<>(requiredActionIds));
        
        if(mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21248.setErrorCode(new Result());
		}

		ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_filterByParam("amIApprover,assocStatus,actedByMeAlready");
		filterDTO.set_filterByValue("true,Pending,false");

		if (applicationDTO != null && applicationDTO.isSelfApprovalEnabled()) {
			filterDTO.set_removeByParam("amICreator");
			filterDTO.set_removeByValue(new HashSet<String>(Arrays.asList("true")));
		}
		List<ApprovalRequestDTO> subRequests = filterDTO.filter(mainRequests);
		List<BBRequestDTO> subBBRequests = ApprovalUtilities.convertApprovalRequestDTOToBBRequestDTO(subRequests);

		List<ApprovalRequestDTO> records = fetchAllRequests(subBBRequests, dcRequest);


		records = newFilterDTO.filter(records);
		try {
			JSONArray resultRecords = new JSONArray(records);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.RECORDS, resultRecords);
			result = JSONToResult.convert(resultObject.toString());

		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the JSON to result", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(result);
		}

		return result;
    }

    @Override
    public Result fetchAllRequestHistory(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        
    	Result result = new Result();
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        FilterDTO newFilterDTO = new FilterDTO();
        Map<String, Object> customerMap = CustomerSession.getCustomerMap(dcRequest);
        
        List<String> requiredActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_VIEW,
				FeatureAction.BILL_PAY_VIEW_PAYMENTS,
				FeatureAction.P2P_VIEW,
				FeatureAction.ACH_FILE_VIEW,
				FeatureAction.ACH_COLLECTION_VIEW,
				FeatureAction.ACH_PAYMENT_VIEW,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW,
				FeatureAction.CHEQUE_BOOK_REQUEST_VIEW,
				FeatureAction.IMPORT_LC_VIEW);
        
        List<String> requiredIdsToFetch = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE,
				FeatureAction.P2P_APPROVE,
				FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE,
				FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
        
        String permittedFeatureActionIds = CustomerSession.getPermittedActionIds(dcRequest,requiredActionIds);

        if (!CustomerSession.IsAPIUser(customerMap)) {
            // fetch the customerId from session map, if not a Spotlight user
            customerId = CustomerSession.getCustomerId(customerMap);
        }
        
        if (StringUtils.isEmpty(permittedFeatureActionIds)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}
        
        try {
			String removeByValue = requestMap.get("removeByValue") != null ? requestMap.get("removeByValue").toString() : null;

			if(StringUtils.isNotEmpty(removeByValue)) {
				requestMap.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(requestMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
        
        List<ApprovalRequestDTO> mainRequests = approvalRequestBusinessDelegate.fetchAllRequestHistory(customerId, new HashSet<String>(requiredIdsToFetch));
        
        if(mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21249.setErrorCode(new Result());
		}

		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_removeByParam("assocStatus");
		filterDTO.set_removeByValue(new HashSet<>(Arrays.asList(TransactionStatusEnum.PENDING.getStatus())));

		filterDTO.set_filterByParam("amICreator");
		filterDTO.set_filterByValue("true");

		List<ApprovalRequestDTO> subRequests = filterDTO.filter(mainRequests);

		List<BBRequestDTO> subBBRequests = ApprovalUtilities.convertApprovalRequestDTOToBBRequestDTO(subRequests);
		List<ApprovalRequestDTO> records = fetchAllRequests(subBBRequests, dcRequest);

		Set<String> recordSet = new HashSet<String>();

		for(ApprovalRequestDTO record:records) {
			recordSet.add(record.getRequestId());
		}

		List<BBActedRequestDTO> history = approvalQueueBusinessDelegate.fetchRequestHistory(null, recordSet);
		records = (new FilterDTO()).merge(records, history, "requestId=requestId","actionts");

		records = newFilterDTO.filter(records);
		try {
			JSONArray resultRecords = new JSONArray(records);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.RECORDS, resultRecords);
			result = JSONToResult.convert(resultObject.toString());

		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the JSON to Result", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(result);
		}

		return result;
    }

    @Override
    public Result fetchAllApprovalHistory(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {

        Result result = new Result();
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;
        FilterDTO newFilterDTO = new FilterDTO();
        
        List<String> requiredActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE,
				FeatureAction.P2P_APPROVE,
				FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE,
				FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);

        Map<String, Object> customerMap = CustomerSession.getCustomerMap(dcRequest);
        String permittedFeatureActionIds = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);

        if (StringUtils.isEmpty(permittedFeatureActionIds)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}
        
        if (!CustomerSession.IsAPIUser(customerMap)) {
            // fetch the customerId from session map, if not a Spotlight user
            customerId = CustomerSession.getCustomerId(customerMap);
        }
        
        try {
			String removeByValue = requestMap.get("removeByValue") != null ? requestMap.get("removeByValue").toString() : null;

			if(StringUtils.isNotEmpty(removeByValue)) {
				requestMap.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(requestMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

        List<ApprovalRequestDTO> mainRequests = approvalRequestBusinessDelegate.fetchAllApprovalHistory(customerId, new HashSet<>(requiredActionIds));
        
        if(mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21247.setErrorCode(new Result());
		}

		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_removeByParam("assocStatus");
		filterDTO.set_removeByValue(new HashSet<>(Arrays.asList(TransactionStatusEnum.WITHDRAWN.getStatus())));
		filterDTO.set_filterByParam("actedByMeAlready");
		filterDTO.set_filterByValue("true");
		List<ApprovalRequestDTO> subRequests = filterDTO.filter(mainRequests);
		List<BBRequestDTO> subBBRequests = ApprovalUtilities.convertApprovalRequestDTOToBBRequestDTO(subRequests);
		List<ApprovalRequestDTO> records = fetchAllRequests(subBBRequests, dcRequest);

		List<BBActedRequestDTO> history = approvalQueueBusinessDelegate.fetchRequestHistory(customerId, new HashSet<String>());
		records = (new FilterDTO()).merge(records, history, "requestId=requestId","approvalDate");

		records = newFilterDTO.filter(records);

		try {
			JSONArray resultRecords = new JSONArray(records);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.RECORDS, resultRecords);
			result = JSONToResult.convert(resultObject.toString());

		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the JSONObject to result", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(result);
		}

		return result;
    }

    @Override
    public Result fetchApprovalHistoryInformation(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String requestId = requestMap.containsKey("requestId") ? (String) requestMap.get("requestId") : null;
        String isAssociationIdStr = requestMap.containsKey("isAssociationId") ? (String) requestMap.get("isAssociationId") : null;
        boolean isAssociationId = true;
        if (isAssociationIdStr == null) {
            isAssociationId = false;
        } else {
            if (isAssociationIdStr.equalsIgnoreCase("1")) {
                isAssociationId = true;
            } else {
                isAssociationId = false;
            }
        }
        diagnostic.prepareDebug("fetchApprovalHistoryInformation _fetch_request_historyinfo_proc isAssociationId: " + isAssociationId).log();
        diagnostic.prepareDebug("fetchApprovalHistoryInformation _fetch_request_historyinfo_proc requestId: " + requestId).log();
        List<RequestHistoryDTO> requestHistoryDTOList = approvalRequestBusinessDelegate.fetchRequestHistoryInfo(requestId, isAssociationId);
        Dataset requestHistoryDataset = new Dataset("RequestHistory");
        for(RequestHistoryDTO requestHistoryDTO: requestHistoryDTOList){
            requestHistoryDataset.addRecord(requestHistoryDTO.getAsRecord());
        }
        result.addDataset(requestHistoryDataset);
        diagnostic.prepareDebug("fetchApprovalHistoryInformation _fetch_pendingapprovers_for_request_proc isAssociationId: " + isAssociationId).log();
        diagnostic.prepareDebug("fetchApprovalHistoryInformation _fetch_pendingapprovers_for_request_proc requestId: " + requestId).log();
        List<PendingRequestApproversDTO> pendingRequestApproversDTOList = approvalRequestBusinessDelegate.fetchPendingApproversInfoForRequest(requestId, isAssociationId);
        Dataset pendingApproversDataset = new Dataset("CompositePendingApprovers");
        for(PendingRequestApproversDTO pendingRequestApproversDTO : pendingRequestApproversDTOList){
            pendingApproversDataset.addRecord(pendingRequestApproversDTO.getAsRecord());
        }
        result.addDataset(pendingApproversDataset);
        return result;
    }

    @Override
    public Result fetchApprovalRequestsCounts(Map<String, Object> requestMap, DataControllerRequest dcRequest) {

        Result result = new Result();
        String customerId = requestMap.containsKey("customerId") ? (String) requestMap.get("customerId") : null;

        Map<String, Object> customerMap = CustomerSession.getCustomerMap(dcRequest);
        diagnostic.prepareDebug("Inside fetchApprovalRequestsCounts "+"customerId: " + customerId).log();

        if (!CustomerSession.IsAPIUser(customerMap)) {
            // fetch the customerId from session map, if not a Spotlight user
            customerId = CustomerSession.getCustomerId(customerMap);
        }

        if (customerId == null) {
            // TODO: throw error: customer ID is a mandatory field
        }
        
        List<String> requiredActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE,
				FeatureAction.P2P_APPROVE,
				FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE,
				FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE);
        
        String featureActionlist = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);

        diagnostic.prepareDebug("Inside fetchApprovalRequestsCounts "+"getPermittedActionIds: " + customerId).log();
		if (StringUtils.isEmpty(featureActionlist)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}
        

        Map<String, Object> countsMap = approvalRequestBusinessDelegate.fetchAllApprovalRequestsCounts(customerId, new HashSet<>(requiredActionIds));

        Dataset countsDataset = new Dataset("Counts");

        for (Map.Entry<String, Object> countsMapEntry : countsMap.entrySet()) {
            Record limitGroupRecord = new Record();
            limitGroupRecord.addParam("limitgroupId", countsMapEntry.getKey());
            Map<String, Object> limitGroupCountsData = (Map<String, Object>) countsMapEntry.getValue();
            Dataset featureActionCountsDataset = new Dataset("featureActions");
            for (Map.Entry<String, Object> limitGroupCountsDataEntry : limitGroupCountsData.entrySet()) {
                String key = limitGroupCountsDataEntry.getKey();
                if (key.equalsIgnoreCase("limitGroupName")) {
                    limitGroupRecord.addParam("limitgroupName", (String) limitGroupCountsDataEntry.getValue());
                } else {
                    // key is the feature action id being checked
                    Map<String, Object> featureActionCountsMap = (Map<String, Object>) limitGroupCountsDataEntry.getValue();
                    Record featureActionCountRecord = new Record();
                    featureActionCountRecord.addParam("featureActionId", key);
                    featureActionCountRecord.addParam("featureActionName", (String) featureActionCountsMap.get("featureActionName"));
                    featureActionCountRecord.addParam("featureName", (String) featureActionCountsMap.get("featureName"));
                    featureActionCountRecord.addParam("actionType", (String) featureActionCountsMap.get("actionType"));
                    featureActionCountRecord.addIntParam("myApprovalsPending", (Integer) featureActionCountsMap.get("myApprovalsPending"));
                    featureActionCountRecord.addIntParam("myApprovalsHistory", (Integer) featureActionCountsMap.get("myApprovalsHistory"));
                    featureActionCountRecord.addIntParam("myRequestsPending", (Integer) featureActionCountsMap.get("myRequestsPending"));
                    featureActionCountRecord.addIntParam("myRequestHistory", (Integer) featureActionCountsMap.get("myRequestHistory"));
                    featureActionCountsDataset.addRecord(featureActionCountRecord);
                }
            }
            limitGroupRecord.addDataset(featureActionCountsDataset);
            countsDataset.addRecord(limitGroupRecord);
        }

        result.addDataset(countsDataset);
        return result;
    }

    @Override
    public Result fetchRequestsWithApprovalInfo(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        String requestIds = requestMap.containsKey("requestIds") ? (String) requestMap.get("requestIds") : null;
        String isAssociationIdStr = requestMap.containsKey("isAssociationId") ? (String) requestMap.get("isAssociationId") : null;
        boolean isAssociationId = true;
        if (isAssociationIdStr == null) {
            isAssociationId = false;
        } else {
            if (isAssociationIdStr.equalsIgnoreCase("1")) {
                isAssociationId = true;
            } else {
                isAssociationId = false;
            }
        }
        Result result = new Result();
        if (requestIds == null) {
            result.addParam("dbpErrMsg", "No RequestIds passed!");
            return result;
        }
        List<PendingRequestApproversDTO> pendingRequestApproversDTOList = approvalRequestBusinessDelegate.fetchPendingApproversInfoForRequest(requestIds, isAssociationId);
        List<RequestDTO> requests = approvalRequestBusinessDelegate.fetchRequestsWithApprovalMatrixInfo(new HashSet<>(Arrays.asList(requestIds.split(","))), isAssociationId, null, false);
        if (requests == null) {
            result.addParam("dbpErrMsg", "Error fetching requests!");
            return result;
        }
        Dataset requestSet = new Dataset("Requests");
        for (RequestDTO request : requests) {
            requestSet.addRecord(request.getAsRecord());
        }
        result.addDataset(requestSet);
        return result;
    }

    @Override
    public Result fetchRequestHistory(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        return null;
    }
    
    @Override
    public List<ApprovalRequestDTO> fetchAllRequests(List<BBRequestDTO> requests, DataControllerRequest dcr) {

		Map<String, Object> customer = CustomerSession.getCustomerMap(dcr);
		String customerId = CustomerSession.getCustomerId(customer);
		List<BBRequestDTO> requestsWithNoMetaData = new ArrayList<>();
		List<BBRequestDTO> requestsWithMetaData = new ArrayList<>();

		// ADP-7058 - since bbRequest object would contain the additional t24 data, extract from the bbRequest DTO object additionalMeta
		String additionalMeta;
		List<ApprovalRequestDTO> records = new ArrayList<>();
		ApprovalRequestDTO tempApprovalRequestDTO;
		//ADP-7058 - populate requestsWithNoMetaData with requests having no meta-data
		for(BBRequestDTO bbRequest : requests){
			additionalMeta = bbRequest.getAdditionalMeta();
			if(StringUtils.isEmpty(additionalMeta) || additionalMeta == null){
				requestsWithNoMetaData.add(bbRequest);
			}
			else{
				requestsWithMetaData.add(bbRequest);
			}
		}
		try{
			for(BBRequestDTO bbRequest: requestsWithMetaData) {
				additionalMeta = bbRequest.getAdditionalMeta();
				try{
					tempApprovalRequestDTO = JSONUtils.parse(additionalMeta, ApprovalRequestDTO.class);
					tempApprovalRequestDTO.setRequestId(bbRequest.getRequestId());
					tempApprovalRequestDTO.setAssocRequestId(bbRequest.getAssocRequestId());
					tempApprovalRequestDTO.setCompanyLegalUnit(bbRequest.getCompanyLegalUnit());
					tempApprovalRequestDTO.setTransactionId(bbRequest.getTransactionId());
					tempApprovalRequestDTO.setStatus(bbRequest.getStatus());
					tempApprovalRequestDTO.setFeatureActionId(bbRequest.getFeatureActionId());
					tempApprovalRequestDTO.setIsGroupMatrix(bbRequest.getIsGroupMatrix());
					tempApprovalRequestDTO.setAmICreator(bbRequest.getAmICreator());
					tempApprovalRequestDTO.setAmIApprover(bbRequest.getAmIApprover());
					tempApprovalRequestDTO.setActedByMeAlready(bbRequest.getActedByMeAlready());
					tempApprovalRequestDTO.setReceivedApprovals(bbRequest.getReceivedApprovals());
					tempApprovalRequestDTO.setRequiredApprovals(bbRequest.getRequiredApprovals());
					records.add(tempApprovalRequestDTO);
				} catch (Exception e){
					alert.prepareError("Invalid meta data stored in the db for requestId: " + bbRequest.getRequestId() +" metadata will be refreshed.").log();
					requestsWithNoMetaData.add(bbRequest);
				}
			}
		}catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		//ADP-7058 - now for each of the requests in requestsWithNoMetaData list, fetch the additionalMeta, and store in DBXDB
		if(requestsWithNoMetaData.size() > 0){
			List<BBRequestDTO> bulkPaymentsRequests = new ArrayList<>();

			// DBB-12763 - enabling single bulk, and multiple bulk
			List<BBRequestDTO> bulkPaymentsSingleRequests = new ArrayList<>();
			List<BBRequestDTO> bulkPaymentsMultipleRequests = new ArrayList<>();

			List<BBRequestDTO> billPayRequests = new ArrayList<>();
			List<BBRequestDTO> p2pRequests = new ArrayList<>();
			List<BBRequestDTO> achFileRequests = new ArrayList<>();
			List<BBRequestDTO> achTransactionRequests = new ArrayList<>();
			List<BBRequestDTO> interBankRequests = new ArrayList<>();
			List<BBRequestDTO> intraBankRequests = new ArrayList<>();
			List<BBRequestDTO> domesticWireTransfers = new ArrayList<>();
			List<BBRequestDTO> internationalWireTransfers = new ArrayList<>();
			List<BBRequestDTO> internationalAccountsFundsTransferRequests = new ArrayList<>();
			List<BBRequestDTO> ownAccountFundsTransferRequests = new ArrayList<>();
			List<BBRequestDTO> chequeRequests = new ArrayList<>();
			List<BBRequestDTO> letterOfCredits = new ArrayList<>();
			for (BBRequestDTO dto: requestsWithNoMetaData) {
				switch (dto.getFeatureActionId()) {
					case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
					case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
						internationalAccountsFundsTransferRequests.add(dto);
						break;
					case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
					case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
						interBankRequests.add(dto);
						break;
					case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
					case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
						intraBankRequests.add(dto);
						break;
					case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
					case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
						ownAccountFundsTransferRequests.add(dto);
						break;
					case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
						internationalWireTransfers.add(dto);
						break;
					case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
						domesticWireTransfers.add(dto);
						break;
					case FeatureAction.BILL_PAY_CREATE:
						billPayRequests.add(dto);
						break;
					case FeatureAction.P2P_CREATE:
						p2pRequests.add(dto);
						break;
					case FeatureAction.ACH_FILE_UPLOAD:
						achFileRequests.add(dto);
						break;
					case FeatureAction.ACH_COLLECTION_CREATE:
					case FeatureAction.ACH_PAYMENT_CREATE:
						achTransactionRequests.add(dto);
						break;
					case FeatureAction.BULK_PAYMENT_REQUEST_SUBMIT:
						bulkPaymentsRequests.add(dto);
						break;
					case FeatureAction.BULK_PAYMENT_SINGLE_SUBMIT:
						bulkPaymentsSingleRequests.add(dto);
						break;
					case FeatureAction.BULK_PAYMENT_MULTIPLE_SUBMIT:
						bulkPaymentsMultipleRequests.add(dto);
						break;
					case FeatureAction.CHEQUE_BOOK_REQUEST_CREATE:
						chequeRequests.add(dto);
						break;
					case FeatureAction.IMPORT_LC_CREATE:
						letterOfCredits.add(dto);
						break;
				}
			}
			List<ApprovalRequestDTO> metaFetchRecords = new ArrayList<>();
			if(!CollectionUtils.isEmpty(bulkPaymentsRequests)) {
				try {
					metaFetchRecords.addAll(JSONUtils.parseAsList(new JSONArray(bulkPaymentRecordBusinessDelegate.fetchAllRecordsFromBackendWithApprovalInfo(bulkPaymentsRequests, dcr)).toString(), ApprovalRequestDTO.class));
				} catch (IOException e) {
					alert.prepareError("Error occured while getting bulk payment requests", e).log();
				}
			}

			// DBB-12763 enable single bulk, and multiple bulk
			if(!CollectionUtils.isEmpty(bulkPaymentsSingleRequests)) {
				try {
					metaFetchRecords.addAll(JSONUtils.parseAsList(new JSONArray(bulkPaymentRecordBusinessDelegate.fetchAllRecordsFromBackendWithApprovalInfo(bulkPaymentsSingleRequests, dcr)).toString(), ApprovalRequestDTO.class));
				} catch (IOException e) {
					alert.prepareError("Error occured while getting single bulk payment requests", e).log();
				}
			}
			if(!CollectionUtils.isEmpty(bulkPaymentsMultipleRequests)) {
				try {
					metaFetchRecords.addAll(JSONUtils.parseAsList(new JSONArray(bulkPaymentRecordBusinessDelegate.fetchAllRecordsFromBackendWithApprovalInfo(bulkPaymentsMultipleRequests, dcr)).toString(), ApprovalRequestDTO.class));
				} catch (IOException e) {
					alert.prepareError("Error occured while getting multiple bulk payment requests", e).log();
				}
			}

			if(!CollectionUtils.isEmpty(chequeRequests))
				metaFetchRecords.addAll(approvalQueueBusinessDelegate.fetchChequeBookInfo(chequeRequests, dcr));
			if(!CollectionUtils.isEmpty(achFileRequests))
				metaFetchRecords.addAll(achFileBusinessDelegate.fetchACHFilesWithApprovalInfo(achFileRequests, dcr));
			if(!CollectionUtils.isEmpty(achTransactionRequests))
				metaFetchRecords.addAll(achTransactionBusinessDelegate.fetchACHTransactionsWithApprovalInfo(achTransactionRequests, dcr));
			if(!CollectionUtils.isEmpty(billPayRequests))
				metaFetchRecords.addAll(billPayTransactionBusinessDelegate.fetchBillPayTransactionsWithApprovalInfo(billPayRequests, dcr));
			if(!CollectionUtils.isEmpty(p2pRequests))
				metaFetchRecords.addAll(p2PTransactionBusinessDelegate.fetchP2PTransactionsWithApprovalInfo(p2pRequests, dcr));
			if(!CollectionUtils.isEmpty(interBankRequests))
				metaFetchRecords.addAll(interBankFundTransferBusinessDelegate.fetchInterBankTransactionsWithApprovalInfo(interBankRequests, dcr));
			if(!CollectionUtils.isEmpty(intraBankRequests))
				metaFetchRecords.addAll(intraBankFundTransferBusinessDelegate.fetchIntraBankTransactionsWithApprovalInfo(intraBankRequests, dcr));
			if(!CollectionUtils.isEmpty(domesticWireTransfers))
				metaFetchRecords.addAll(domesticWireTransactionBusinessDelegate.fetchWireTransactionsWithApprovalInfo(domesticWireTransfers, dcr));
			if(!CollectionUtils.isEmpty(internationalWireTransfers))
				metaFetchRecords.addAll(internationalWireTransactionBusinessDelegate.fetchWireTransactionsWithApprovalInfo(internationalWireTransfers, dcr));
			if(!CollectionUtils.isEmpty(internationalAccountsFundsTransferRequests))
				metaFetchRecords.addAll(internationalFundTransferBusinessDelegate.fetchInternationalTransactionsWithApprovalInfo(internationalAccountsFundsTransferRequests, dcr));
			if(!CollectionUtils.isEmpty(ownAccountFundsTransferRequests))
				metaFetchRecords.addAll(ownAccountFundTransferBusinessDelegate.fetchOwnAccountFundTransactionsWithApprovalInfo(ownAccountFundsTransferRequests, dcr));
			if(!CollectionUtils.isEmpty(letterOfCredits))
				metaFetchRecords.addAll(approvalQueueBusinessDelegate.fetchLetterOfCreditDetails(letterOfCredits, dcr));

			// update all the requests additionalMeta in DBXDB
			for(ApprovalRequestDTO approvalReqDTO : metaFetchRecords){
				approvalQueueBusinessDelegate.updateAdditionalMetaForApprovalRequestInDB(approvalReqDTO);
			}
			records.addAll(metaFetchRecords);
		}


		Set<String> accounts = new HashSet<String>();
		Set<String> sentByIds = new HashSet<String>();
		try{
			for(ApprovalRequestDTO dto : records) {
				accounts.add(dto.getAccountId());
				sentByIds.add(dto.getSentBy());
			}
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		try{
			FeatureActionBusinessDelegate limitGroupDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
			List<FeatureActionDTO> featureactions = limitGroupDelegate.fetchFeatureActionsWithLimitGroupDetails();
			records = (new FilterDTO()).merge(records, featureactions, "featureActionId=featureActionId", "featureName,featureActionName,limitGroupName");
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		try{
			AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
			List<CustomerAccountsDTO> customerAccountsDTOs = accountBusinessDelegate.getAccountDetails(customerId, accounts);
			records = (new FilterDTO()).merge(records, customerAccountsDTOs, "accountId=accountId", "");
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		try{
			CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
			List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(sentByIds);
			records = (new FilterDTO()).merge(records, customerDTOs, "sentBy=id","fullName","sentBy=fullName");
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}

		return records;
	}
    
	public void formatAndPushEvent(Result result, DataControllerRequest dcRequest, String transactionStatus,
			String customerId, String comment, String approverUsername) {
		String featureActionId = result.getParamValueByName("featureActionId");
		DataControllerResponse dcResponse = null;
		String username = "";
		try {
			if (!StringUtils.isEmpty(featureActionId)) {
				String action = "", actionType = "";
				if (Constants.CREATE_BENEFICIARIES_FEATURE_ACTION_LIST.contains(featureActionId)) {
					action = "Creation";
					actionType = "CREATE";
				} else if (Constants.EDIT_BENEFICIARIES_FEATURE_ACTION_OPTIONAL_LIST.contains(featureActionId)) {
					action = "Updation";
					actionType = "EDIT_OPTIONAL";
				} else if (Constants.EDIT_BENEFICIARIES_FEATURE_ACTION_LINKAGE_LIST.contains(featureActionId)) {
					action = "Updation";
					actionType = "EDIT_LINKAGE";
				} else if (Constants.DELETE_BENEFICIARIES_FEATURE_ACTION_LIST.contains(featureActionId)) {
					action = "Deletion";
					actionType = "DELETE";
				}
				Map<String, String> eventPayload = new HashMap<>();
				if ("Withdrawn".equalsIgnoreCase(transactionStatus)) {
					eventPayload.put("comment", comment);
				} else if ("Rejected".equalsIgnoreCase(transactionStatus)) {
					// fetching the Initiatior username
					username = userBackendDelegate.fetchUsernameFromCustomerId(customerId, null);
					username = username.replace("\"", "");
					eventPayload.put("approverUsername", approverUsername);
					eventPayload.put("userName", username);
				} else {
					//transactionStatus is Approved & fetching the username of associated user of the request
					String coreCustomerId = customerId;
					Map<String, String> custIdUserNameMap = PayeeUtils.fetchUserNameByCoreCustomerId(coreCustomerId,
							dcRequest);
					eventPayload.put("userName", custIdUserNameMap.get(coreCustomerId));
				}
				eventPayload.put("transactionStatus", transactionStatus);
				eventPayload.put("initiatorCustId", customerId);
				eventPayload.put("action", action);
				eventPayload.put("actionType", actionType);
				eventPayload.put("userType", "Initiatior");
				eventPayload.put("className", ApprovalRequestResourceImpl.class.getName());
				PayeeUtils.AsyncPushEvent(result, dcRequest, dcResponse, eventPayload);
			}
		} catch (Exception e) {
			alert.prepareError("Exception in ApprovalRequestResourceImpl:formatAndPushEvent" + e).log();
		}
	}
}
