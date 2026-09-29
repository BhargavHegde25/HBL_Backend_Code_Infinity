package com.temenos.dbx.product.approvalservices.resource.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.dbx.product.approvalsframework.approvalmatrix.backenddelegate.api.ApprovalMatrixBackendDelegate;
import com.temenos.dbx.product.approvalsframework.approvalrequest.resource.api.ApprovalRequestResource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.constants.EventSubType;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionResource;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBActedRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.RequestHistoryDTO;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalQueueResource;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;
import com.temenos.dbx.product.bulkpaymentservices.resource.api.BulkPaymentRecordResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.FeatureDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.CustomerSignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupRequestMatrixDTO;
import com.temenos.dbx.product.signatorygroupservices.resource.api.SignatoryGroupResource;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.OwnAccountFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.RequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.api.ApprovalRequestBusinessDelegate;

public class ApprovalQueueResourceImpl implements ApprovalQueueResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	CustomerBusinessDelegate custDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(CustomerBusinessDelegate.class);
	BulkPaymentRecordResource bulkPaymentRecordResource = DBPAPIAbstractFactoryImpl
			.getResource(BulkPaymentRecordResource.class);
	SignatoryGroupResource signatoryGroupResource = DBPAPIAbstractFactoryImpl.getResource(SignatoryGroupResource.class);
	ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ACHFileBusinessDelegate.class);
	ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
	BillPayTransactionBusinessDelegate billPayTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
	P2PTransactionBusinessDelegate p2PTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
	InterBankFundTransferBusinessDelegate interBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	IntraBankFundTransferBusinessDelegate intraBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
	DomesticWireTransactionBusinessDelegate domesticWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
	InternationalWireTransactionBusinessDelegate internationalWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
	InternationalFundTransferBusinessDelegate internationalFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
	OwnAccountFundTransferBusinessDelegate ownAccountFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	BulkPaymentRecordBusinessDelegate bulkPaymentRecordBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(BulkPaymentRecordBusinessDelegate.class);
	BulkPaymentRecordBackendDelegate bulkPaymentRecordBackendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
	GeneralTransactionsBusinessDelegate generalTransactionsBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
	FeatureActionBusinessDelegate FeatureActionBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(FeatureActionBusinessDelegate.class);
	ACHCommonsBusinessDelegate aCHCommonsBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
	SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
	ApprovalRequestResource approvalRequestResource = DBPAPIAbstractFactoryImpl
			.getResource(ApprovalRequestResource.class);
	ApprovalRequestBusinessDelegate approvalRequestBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApprovalRequestBusinessDelegate.class);
	ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

	@Override
	public Result fetchRequestHistory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		try {

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);

			@SuppressWarnings("unchecked")
			Map<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
			String requestId = (String) requestMap.get(Constants.REQUESTID);

			ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW,
					FeatureAction.ACH_PAYMENT_VIEW, FeatureAction.ACH_FILE_VIEW, FeatureAction.BILL_PAY_VIEW_PAYMENTS,
					FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW, FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW,
					FeatureAction.P2P_VIEW, FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW, FeatureAction.CHEQUE_BOOK_REQUEST_VIEW);

			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);

			if (featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			JSONObject resObj = new JSONObject();

			List<RequestHistoryDTO> history = approvalQueueDelegate.fetchRequestHistory(requestId, userId);

			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);

			if (requestDTO == null) {
				alert.prepareError("Error while fetching BBRequest details").log();
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}

			// ADP-7058 - update the T24 meta-data while viewing a request
			try {
				approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(requestDTO, request);
			} catch (Exception e) {
				alert.prepareError("Failed to update T24 data for the request: " + e).log();
//				result.addErrMsgParam(e + "");
//				return result;
			}

			boolean isGroupMatrix = Boolean.parseBoolean(requestDTO.getIsGroupMatrix());

			if (requestDTO.getStatus().equalsIgnoreCase(TransactionStatusEnum.PENDING.getStatus())) {
				if (!isGroupMatrix) {
					JSONArray jsonArray = approvalQueueDelegate.fetchApprovers(requestId, userId);
					Set<String> approverIds = new HashSet<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);
						approverIds.add((String) object.get("customerId"));
					}
					CustomerBusinessDelegate custDelegate = DBPAPIAbstractFactoryImpl
							.getBusinessDelegate(CustomerBusinessDelegate.class);
					List<CustomerDTO> approvers = custDelegate.getCustomerInfo(approverIds);
					Set<String> actedUser = new HashSet<String>();
					for (RequestHistoryDTO requestHistoryDTO : history) {
						if (!requestHistoryDTO.getStatus().equals(TransactionStatusEnum.PENDING.getStatus())
								&& requestHistoryDTO.getSoftdeleteflag().equalsIgnoreCase(Constants.FALSE))
							actedUser.add(requestHistoryDTO.getCreatedby());
					}
					for (CustomerDTO customerDTO : approvers) {
						if (!actedUser.contains(customerDTO.getId())) {
							RequestHistoryDTO pendingApprover = new RequestHistoryDTO();
							pendingApprover.setUserName(customerDTO.getFullName());
							pendingApprover.setRequestId(requestId);
							pendingApprover.setStatus(Constants.APPROVAL_PENDING);
							pendingApprover.setAction(Constants.APPROVAL_PENDING);
							history.add(pendingApprover);
						}
					}
				} else {
					AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
							.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

					String approveFeatureActionId = FeatureActionBusinessDelegate
							.getApproveFeatureAction(requestDTO.getFeatureActionId());
					List<SignatoryGroupDTO> approversList = JSONUtils.parseAsList(
							approvalQueueDelegate.fetchApprovers(requestId, userId).toString(),
							SignatoryGroupDTO.class);

					List<RequestHistoryDTO> pendingApproverList = new ArrayList<RequestHistoryDTO>();
					for (SignatoryGroupDTO approver : approversList) {
						RequestHistoryDTO pendingApprover = new RequestHistoryDTO();
						pendingApprover.setGroupName(approver.getSignatoryGroupName());
						pendingApprover.setRequestId(requestId);
						pendingApprover.setStatus(Constants.APPROVAL_PENDING);
						pendingApprover.setAction(Constants.APPROVAL_PENDING);

						Set<String> actedUser = new HashSet<String>();
						for (RequestHistoryDTO requestHistoryDTO : history) {
							if (!requestHistoryDTO.getStatus().equals(TransactionStatusEnum.PENDING.getStatus())
									&& requestHistoryDTO.getSoftdeleteflag().equalsIgnoreCase(Constants.FALSE))
								actedUser.add(requestHistoryDTO.getCreatedby());
						}

						JSONArray pendingSignatoryUsers = new JSONArray();
						List<SignatoryGroupDTO> sigCustDTOs = signatoryGroupBusinessDelegate
								.fetchSignatoryGroupCustomers(approver.getSignatoryGroupId());
						if (sigCustDTOs != null && !sigCustDTOs.isEmpty()) {
							for (SignatoryGroupDTO sigCustDTO : sigCustDTOs) {
								if (!actedUser.contains(sigCustDTO.getUserId()) && authorizationChecksBusinessDelegate
										.isUserAuthorizedForFeatureActionForAccount(sigCustDTO.getUserId(),
												approveFeatureActionId, requestDTO.getAccountId(),
												Boolean.parseBoolean(sigCustDTO.getIsCombinedUser()))) {
									JSONObject userDetail = new JSONObject();
									userDetail.put("userId", sigCustDTO.getUserId());
									userDetail.put("userName", sigCustDTO.getUserName());
									userDetail.put("fullName",
											sigCustDTO.getFirstName() + " " + sigCustDTO.getLastName());
									userDetail.put("role", sigCustDTO.getRole());
									userDetail.put("userImage", sigCustDTO.getUserImage());
									pendingSignatoryUsers.put(userDetail);
								}
							}
						}
						pendingApprover.setPendingApprovers(pendingSignatoryUsers.toString());
						pendingApprover.setGroupId(approver.getSignatoryGroupId());
						pendingApproverList.add(pendingApprover);
					}
					history.addAll(pendingApproverList);

					ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl
							.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
					List<SignatoryGroupRequestMatrixDTO> sigGrpReqMatList = signatoryGroupBusinessDelegate
							.fetchPendingSignatoryGroupRequestMatrix(requestId);
					for (SignatoryGroupRequestMatrixDTO sigGrpReq : sigGrpReqMatList) {
						ApprovalMatrixDTO matrixDTO = approvalMatrixBusinessDelegate
								.fetchApprovalMatrixById(sigGrpReq.getApprovalMatrixId());
						if (matrixDTO != null) {
							sigGrpReq.setLimitTypeId(matrixDTO.getLimitTypeId());
						}
					}

					if (sigGrpReqMatList == null || sigGrpReqMatList.isEmpty()) {
						alert.prepareError("Error while fetching signatory group details").log();
						return ErrorCodeEnum.ERR_14008.setErrorCode(result);
					}
					String pendingListString = JSONUtils.stringifyCollectionWithTypeInfo(sigGrpReqMatList,
							SignatoryGroupRequestMatrixDTO.class);
					JSONArray pendingArray = new JSONArray(pendingListString);
					resObj.put("pendingGroupRules", pendingArray);
				}
			}
			if (history != null) {
				String historyString = JSONUtils.stringifyCollectionWithTypeInfo(history, RequestHistoryDTO.class);
				JSONArray historyRes = new JSONArray(historyString);
				resObj.put(Constants.RECORDS, historyRes);
				result = JSONToResult.convert(resObj.toString());
			} else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		} catch (Exception exp) {
			alert.prepareError("Exception occurred while defining resources for fetching actions", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

	@Override
	public Result rejectACHTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// Initialization of business Delegate Class
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		ACHTransactionBusinessDelegate aCHTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ACHTransactionBusinessDelegate.class);

		try {
			List<String> requiredApproveActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_APPROVE,
					FeatureAction.ACH_PAYMENT_APPROVE);

			String approveActionList = CustomerSession.getPermittedActionIds(request, requiredApproveActionIds);

			String requestId = request.getParameter("Request_id");
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);

			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
			contracts.add(CustomerSession.getCompanyId(customer));

			BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate
					.authorizationCheckForRejectAndWithdrawl(requestId, String.join(",", contracts), approveActionList);

			if (bbrequestObject == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}

			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Rejecting ACH Transaction Request";
			}

			String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
			if (status == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
				return ErrorCodeEnum.ERR_21012.setErrorCode(new Result());
			}

			status = TransactionStatusEnum.REJECTED.getStatus();

			BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);

			if (bBRequestDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			aCHTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), ""); // confirmationNumber
																														// not
																														// needed

			approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
					bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

			result.addStringParam("Transaction_id", bBRequestDTO.getTransactionId());
			result.addStringParam("FeatureAction_id", bBRequestDTO.getFeatureActionId());
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.REJECTED.getStatus());
			loggingForACHTransactionsAndTemplates(request, response, bBRequestDTO, result);
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at rejectACHRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public Result withdrawACHTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// Initialization of business Delegate Class
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		ACHTransactionBusinessDelegate aCHTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ACHTransactionBusinessDelegate.class);

		try {
			List<String> requiredCreateActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_CREATE,
					FeatureAction.ACH_PAYMENT_CREATE);

			String createActionList = CustomerSession.getPermittedActionIds(request, requiredCreateActionIds);

			String requestId = request.getParameter("Request_id");
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);

			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
			contracts.add(CustomerSession.getCompanyId(customer));

			BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate
					.authorizationCheckForRejectAndWithdrawl(requestId, String.join(",", contracts), createActionList);

			if (bbrequestObject != null && !((CustomerSession.IsCombinedUser(customer)
					&& custDelegate.getCombinedUserIds(customerId).contains(bbrequestObject.getCreatedby()))
					|| (customerId.equals(bbrequestObject.getCreatedby())))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}

			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Withdrawing ACH Transaction Request";
			}

			String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
			if (status == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
				return ErrorCodeEnum.ERR_21011.setErrorCode(new Result());
			}

			status = TransactionStatusEnum.WITHDRAWN.getStatus();

			BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);

			if (bBRequestDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			aCHTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), ""); // confirmationNumber
																														// not
																														// needed

			approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
					bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

			result.addStringParam("Transaction_id", bBRequestDTO.getTransactionId());
			result.addStringParam("FeatureAction_id", bBRequestDTO.getFeatureActionId());
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.WITHDRAWN.getStatus());

			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at withdrawACHTransactionRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public Result approveACHTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {

			ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);

			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			String requestId = request.getParameter("Request_id");
			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Approving ACH Transaction Request";
			}

			Boolean flag = approvalQueueBusinessDelegate.checkIfUserAlreadyApproved(requestId, customerId);
			if (flag == true) {
				return ErrorCodeEnum.ERR_21010.setErrorCode(result);
			}
			/**
			 * Start: Added as part of ADP-2810
			 */
			ApplicationDTO applicationDTO = new ApplicationDTO();
			ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApplicationBusinessDelegate.class);
			applicationDTO = applicationBusinessDelegate.properties();
			String createdby = null;

			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);

			if (requestDTO != null && StringUtils.isNotEmpty(requestDTO.getCreatedby())) {
				diagnostic.prepareInfo("Fetch BBRequest details success").log();
				createdby = requestDTO.getCreatedby();
			} else {
				alert.prepareError("Error while fetching BBRequest details").log();
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}
			if (applicationDTO == null) {
				diagnostic.prepareInfo("Error while fetching Application record details").log();
				return ErrorCodeEnum.ERR_12114.setErrorCode(result);
			} else if (!applicationDTO.isSelfApprovalEnabled() && createdby != null
					&& createdby.equalsIgnoreCase(customerId)) {
				return ErrorCodeEnum.ERR_12113.setErrorCode(result);
			}
			/**
			 * End: Added as part of ADP-2810
			 */
			Object res = approvalQueueBusinessDelegate.approveACHTransaction(requestId, customerId, comments,
					requestDTO.getCompanyId(), request);

			if (res == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			} else if (res instanceof ACHTransactionDTO) {
				result.addStringParam("Transaction_id", ((ACHTransactionDTO) res).getTransaction_id() + "");
				result.addStringParam("FeatureAction_id", ((ACHTransactionDTO) res).getFeatureActionId());
				ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
				achTransactionBusinessDelegate.executeTransactionAfterApproval(
						String.valueOf(((ACHTransactionDTO) res).getTransaction_id()),
						((ACHTransactionDTO) res).getFeatureActionId(), request);
			} else if (res instanceof BBRequestDTO) {
				result.addStringParam("Transaction_id", ((BBRequestDTO) res).getTransactionId());
				result.addStringParam("FeatureAction_id", ((BBRequestDTO) res).getFeatureActionId());
			}
			result.addStringParam("Success", "Successful");
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.APPROVED.getStatus());
			if (res instanceof ACHTransactionDTO) {
				loggingForACHTransactionsAndTemplates(request, response, res, result);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception at approveACHRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

	@Override
	public Result approveACHFileRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {

			ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);

			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			String requestId = request.getParameter("Request_id");
			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Approving ACH File Request";
			}

			/**
			 * Start: Added as part of ADP-2810
			 */
			ApplicationDTO applicationDTO = new ApplicationDTO();
			ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApplicationBusinessDelegate.class);
			applicationDTO = applicationBusinessDelegate.properties();
			String createdby = null;
			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);
			if (requestDTO != null && StringUtils.isNotEmpty(requestDTO.getCreatedby())) {
				diagnostic.prepareInfo("Fetch BBRequest details success").log();
				createdby = requestDTO.getCreatedby();
			} else {
				alert.prepareError("Error while fetching BBRequest details").log();
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}
			if (applicationDTO == null) {
				diagnostic.prepareInfo("Error while fetching Application record details").log();
				return ErrorCodeEnum.ERR_12114.setErrorCode(result);
			} else if (!applicationDTO.isSelfApprovalEnabled() && createdby != null
					&& createdby.equalsIgnoreCase(customerId)) {
				return ErrorCodeEnum.ERR_12113.setErrorCode(result);
			}
			/**
			 * End: Added as part of ADP-2810
			 */

			Boolean flag = approvalQueueBusinessDelegate.checkIfUserAlreadyApproved(requestId, customerId);
			if (flag == true) {
				return ErrorCodeEnum.ERR_21010.setErrorCode(result);
			}

			Object res = approvalQueueBusinessDelegate.approveACHFile(requestId, customerId, comments,
					requestDTO.getCompanyId(), request);

			if (res == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			} else if (res instanceof ACHFileDTO) {
				result.addStringParam("ACHFile_id", ((ACHFileDTO) res).getAchFile_id() + "");
				result.addStringParam("FeatureAction_id", ((ACHFileDTO) res).getFeatureActionId());
				ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ACHFileBusinessDelegate.class);
				achFileBusinessDelegate.executeACHFileAfterApproval(((ACHFileDTO) res).getAchFile_id(),
						((ACHFileDTO) res).getFeatureActionId(), request);

				_logACHFile(request, response, result, requestId, "Approved");
			} else if (res instanceof BBRequestDTO) {
				result.addStringParam("ACHFile_id", ((BBRequestDTO) res).getTransactionId());
				result.addStringParam("FeatureAction_id", ((BBRequestDTO) res).getFeatureActionId());
			}
			result.addStringParam("Success", "Successful");
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.APPROVED.getStatus());

		} catch (Exception e) {
			alert.prepareError("Caught exception at approveACHFileRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

	@Override
	public Result rejectACHFileRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

		ACHFileBusinessDelegate aCHFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);

		try {
			List<String> requiredApproveActionIds = Arrays.asList(FeatureAction.ACH_FILE_APPROVE);

			String approveActionList = CustomerSession.getPermittedActionIds(request, requiredApproveActionIds);

			String requestId = request.getParameter("Request_id");
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
			contracts.add(CustomerSession.getCompanyId(customer));

			BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate
					.authorizationCheckForRejectAndWithdrawl(requestId, String.join(",", contracts), approveActionList);

			if (bbrequestObject == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Rejecting ACH File Request";
			}

			String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
			if (status == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
				return ErrorCodeEnum.ERR_21012.setErrorCode(new Result());
			}

			status = TransactionStatusEnum.REJECTED.getStatus();

			BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);

			if (bBRequestDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			aCHFileBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), ""); // confirmationNumber
																													// not
																													// needed

			approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
					bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

			result.addStringParam("ACHFile_id", bBRequestDTO.getTransactionId());
			result.addStringParam("FeatureAction_id", bBRequestDTO.getFeatureActionId());
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.REJECTED.getStatus());

			_logACHFile(request, response, result, requestId, "Rejected");

			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at rejectACHFileRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public Result withdrawACHFileRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		ACHFileBusinessDelegate aCHFileBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ACHFileBusinessDelegate.class);

		try {
			List<String> requiredCreateActionIds = Arrays.asList(FeatureAction.ACH_FILE_UPLOAD);

			String createActionList = CustomerSession.getPermittedActionIds(request, requiredCreateActionIds);

			String requestId = request.getParameter("Request_id");
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
			contracts.add(CustomerSession.getCompanyId(customer));

			BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate
					.authorizationCheckForRejectAndWithdrawl(requestId, String.join(",", contracts), createActionList);

			if (bbrequestObject != null && !((CustomerSession.IsCombinedUser(customer)
					&& custDelegate.getCombinedUserIds(customerId).contains(bbrequestObject.getCreatedby()))
					|| (customerId.equals(bbrequestObject.getCreatedby())))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}

			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Withdraw ACH File Request";
			}

			String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
			if (status == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
				return ErrorCodeEnum.ERR_21011.setErrorCode(new Result());
			}

			status = TransactionStatusEnum.WITHDRAWN.getStatus();

			BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);

			if (bBRequestDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			aCHFileBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), ""); // confirmationNumber
																													// not
																													// needed

			approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
					bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

			result.addStringParam("ACHFile_id", bBRequestDTO.getTransactionId());
			result.addStringParam("FeatureAction_id", bBRequestDTO.getFeatureActionId());
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.WITHDRAWN.getStatus());

			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at withdrawACHFileRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public Result approveGeneralTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {

			ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);

			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			String requestId = request.getParameter("Request_id");
			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Approving General Transaction Request";
			}
			/**
			 * Start: Added as part of ADP-2810
			 */
			ApplicationDTO applicationDTO = new ApplicationDTO();
			ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApplicationBusinessDelegate.class);
			applicationDTO = applicationBusinessDelegate.properties();
			String createdby = null;

			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);

			if (requestDTO != null && StringUtils.isNotEmpty(requestDTO.getCreatedby())) {
				diagnostic.prepareInfo("Fetch Request details success").log();
				createdby = requestDTO.getCreatedby();
			} else {
				alert.prepareError("Error while fetching bbResponse").log();
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}
			if (applicationDTO == null) {
				diagnostic.prepareInfo("Error while fetching Application record details").log();
				return ErrorCodeEnum.ERR_12114.setErrorCode(result);
			} else if (!applicationDTO.isSelfApprovalEnabled() && createdby != null
					&& createdby.equalsIgnoreCase(customerId)) {
				return ErrorCodeEnum.ERR_12113.setErrorCode(result);
			}
			/**
			 * End: Added as part of ADP-2810
			 */
			Boolean flag = approvalQueueBusinessDelegate.checkIfUserAlreadyApproved(requestId, customerId);
			if (flag == true) {
				return ErrorCodeEnum.ERR_21010.setErrorCode(result);
			}

			Object res = approvalQueueBusinessDelegate.approveGeneralTransaction(requestId, customerId, comments,
					requestDTO.getCompanyId(), request);

			if (res == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			} else if (res instanceof JSONObject) {
				String transactionId = ((JSONObject) res).getString("Transaction_id");
				String featureActionId = ((JSONObject) res).getString("FeatureAction_id");
				String createdBy = ((JSONObject) res).getString("createdBy");
				if (createdBy.isEmpty())
					createdBy = "";

				result.addStringParam("Transaction_id", transactionId);
				result.addStringParam("FeatureAction_id", featureActionId);
				GeneralTransactionsBusinessDelegate generalTransactionsBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
				generalTransactionsBusinessDelegate.executeTransactionAfterApproval(transactionId, featureActionId,
						request);
				_logGeneralTransaction(request, response, result, createdBy);
			} else if (res instanceof BBRequestDTO) {
				result.addStringParam("Transaction_id", ((BBRequestDTO) res).getTransactionId());
				result.addStringParam("FeatureAction_id", ((BBRequestDTO) res).getFeatureActionId());
			}
			result.addStringParam("Success", "Successful");
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.APPROVED.getStatus());

		} catch (Exception e) {
			alert.prepareError("Caught exception at approveGeneralTransactionRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

	@Override
	public Result rejectGeneralTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		// Initialization of business Delegate Class
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);

		try {
			List<String> requiredApproveActionIds = Arrays.asList(FeatureAction.BILL_PAY_APPROVE,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE, FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
					FeatureAction.P2P_APPROVE, FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
					FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE);

			String approveActionList = CustomerSession.getPermittedActionIds(request, requiredApproveActionIds);

			String requestId = request.getParameter("Request_id");
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
			contracts.add(CustomerSession.getCompanyId(customer));

			BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate
					.authorizationCheckForRejectAndWithdrawl(requestId, String.join(",", contracts), approveActionList);

			if (bbrequestObject == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}

			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Rejecting General Transaction Request";
			}

			String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
			if (status == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
				return ErrorCodeEnum.ERR_21012.setErrorCode(new Result());
			}

			status = TransactionStatusEnum.REJECTED.getStatus();

			BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);

			if (bBRequestDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			generalTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
					bBRequestDTO.getFeatureActionId(), bBRequestDTO.getStatus());

			approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
					bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

			String createdBy = bBRequestDTO.getCreatedby();
			if (createdBy.isEmpty())
				createdBy = "";

			result.addStringParam("Transaction_id", bBRequestDTO.getTransactionId());
			result.addStringParam("FeatureAction_id", bBRequestDTO.getFeatureActionId());
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.REJECTED.getStatus());

			_logGeneralTransaction(request, response, result, createdBy);

			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at rejectGeneralTransactionRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public Result withdrawGeneralTransactionRequest(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		// Initialization of business Delegate Class
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);

		try {
			List<String> requiredCreateActionIds = Arrays.asList(FeatureAction.BILL_PAY_CREATE,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE, FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE,
					FeatureAction.P2P_CREATE, FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE,
					FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE);
			String createActionList = CustomerSession.getPermittedActionIds(request, requiredCreateActionIds);

			String requestId = request.getParameter("Request_id");
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);

			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(customerId);

			BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate
					.authorizationCheckForRejectAndWithdrawl(requestId, String.join(",", contracts), createActionList);

			if (bbrequestObject != null && !((CustomerSession.IsCombinedUser(customer)
					&& custDelegate.getCombinedUserIds(customerId).contains(bbrequestObject.getCreatedby()))
					|| (customerId.equals(bbrequestObject.getCreatedby())))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}

			String comments = request.getParameter("Comments");
			if (comments == null || comments == "") {
				comments = "Withdrawing General Transaction Request";
			}

			String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
			if (status == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
				return ErrorCodeEnum.ERR_21011.setErrorCode(new Result());
			}

			status = TransactionStatusEnum.WITHDRAWN.getStatus();

			BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);

			if (bBRequestDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			generalTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
					bBRequestDTO.getFeatureActionId(), bBRequestDTO.getStatus());

			approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
					bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

			result.addStringParam("Transaction_id", bBRequestDTO.getTransactionId());
			result.addStringParam("FeatureAction_id", bBRequestDTO.getFeatureActionId());
			result.addStringParam("ActedBy", customerId);
			result.addStringParam("Status", TransactionStatusEnum.WITHDRAWN.getStatus());

			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at withdrawGeneralTransactionRequest method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public void autoRejectPendingTransactionsInApprovalQueue(FabricRequestManager fabricRequestManager,
			FabricResponseManager fabricResponseManager) {

		PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();
		JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

		JsonElement mfaElement = requestpayload.get("id");
		String customerId = null;

		if (mfaElement != null) {
			customerId = mfaElement.getAsString();
		} else {
			alert.prepareError("id field is missing in the request payload: ").log();
			// responsePayloadHandler.updatePayloadAsJson(ErrorCodeEnum.ERR_10500.setErrorCode(new
			// JsonObject()));
		}

		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);

		approvalQueueDelegate.autoRejectInvalidPendingTransactionsWhenApproverPermissionIsRevoked(customerId);
	}

	/**
	 * Logs ACHfile status is auditactivity
	 * 
	 * @param requestManager
	 * @param responseManager
	 * @param result
	 * @param requestId
	 * @param transactionType
	 */
	public void _logACHFile(DataControllerRequest request, DataControllerResponse response, Result result,
			String requestId, String transactionType) {

		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE))
			return;

		ACHFileBusinessDelegate aCHFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);

		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApproversBusinessDelegate.class);
		String achFileId = result.getParamValueByName("ACHFile_id");

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);

		try {
			ACHFileDTO achFileDTO = aCHFileBusinessDelegate.fetchACHFileEntry(achFileId);

			JSONObject achFileObject = new JSONObject(achFileDTO);
			JsonObject customParams = new JsonParser().parse(achFileObject.toString()).getAsJsonObject();

			String eventType = Constants.MAKE_TRANSFER;
			String eventSubType = Constants.ACH_FILE_INITIATE;
			String producer = "Transactions/POST(uploadACHFile)";
			String statusID = Constants.SID_EVENT_SUCCESS;
			String userName = CustomerSession.getCustomerName(customer);

			customParams.addProperty("uploadedBy", customParams.get("createdby").getAsString());

			List<String> approvers = approversBusinessDelegate.getRequestApproversList(requestId);
			List<String> approvedBy = approversBusinessDelegate.getRequestActedApproversList(requestId, "Approved");
			if (approvers == null) {
				customParams.addProperty(Constants.APPROVERS, "");
			} else {
				customParams.addProperty(Constants.APPROVERS, approvers.toString());
			}
			if (approvedBy == null) {
				customParams.addProperty("approvedBy", "");
			} else {
				customParams.addProperty("approvedBy", approvedBy.toString());
			}
			List<String> rejectedBy = approversBusinessDelegate.getRequestActedApproversList(requestId, "Rejected");
			if (rejectedBy == null) {
				customParams.addProperty("rejectedBy", "N/A");
			} else {
				customParams.addProperty("rejectedBy", rejectedBy.toString());
			}
			if (transactionType == "Rejected") {
				eventSubType = Constants.REJECTED_ + eventSubType;
				customParams.addProperty(Constants.STATUS, TransactionStatusEnum.REJECTED.getStatus());
			} else {
				customParams.addProperty(Constants.STATUS, TransactionStatusEnum.APPROVED.getStatus());
			}
			customParams.addProperty("createdOn", achFileDTO.getCreatedts());
			customParams.remove(Constants.CREATEDTS);
			Double amount = achFileDTO.getDebitAmount() + achFileDTO.getCreditAmount();
			customParams.addProperty(Constants.AMOUNT, amount);
			customParams.addProperty(Constants.REFERENCEID, achFileDTO.getAchFile_id());

			AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);

			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null, userName,
					customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine.").log();
		}
	}

	/**
	 * Logs ACH transactions rejections and approvals auditactivity
	 * 
	 * @param request
	 * @param response
	 * @param res
	 * @param result
	 */
	public void loggingForACHTransactionsAndTemplates(DataControllerRequest request, DataControllerResponse response,
			Object res, Result result) {

		try {
			String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
			if (enableEvents == null || enableEvents.equalsIgnoreCase("false"))
				return;

			ACHTransactionResource achTransactionResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(ACHTransactionResource.class);
			ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApproversBusinessDelegate.class);

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String transactionId = null;
			String eventsubtype = null;
			List<String> approvers, approvedby;
			String status = ACHConstants.SID_EVENT_SUCCESS;
			JsonObject resultObject = new JsonParser().parse(ResultToJSON.convert(result)).getAsJsonObject();
			JsonObject customparams = new JsonObject();
			if (res instanceof BBRequestDTO) {
				transactionId = ((BBRequestDTO) res).getTransactionId();
			} else if (res instanceof ACHTransactionDTO) {
				transactionId = ((ACHTransactionDTO) res).getTransaction_id();
			}
			JSONObject transaction = achTransactionResource
					.fetchTranscationEntryWithRecordsAndSubRecords(transactionId);
			if (transaction == null) {
				alert.prepareError("error while logging the rejection/approval of ACHrequest").log();
				return;
			}
			JsonObject transactionDetails = new JsonParser().parse(transaction.toString()).getAsJsonObject();
			customparams.addProperty("referenceId", transaction.getString("transaction_id"));
			customparams.addProperty("amount", transaction.getString("totalAmount"));
			customparams.addProperty("fromAccountNumber", transaction.getString("fromAccount"));
			customparams.add("transactionDetails", transactionDetails);
			customparams.add("Response", resultObject);
			approvers = approversBusinessDelegate.getRequestApproversList(request.getParameter("Request_id"));
			customparams.addProperty("approvers", approvers.toString());
			String dtFormat = "yyyy-MM-dd'T'hh:mm:ss";
			SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
			Date currdate = new Date();
			String date = formatter.format(currdate);
			customparams.addProperty("executedOn", date);
			if (transaction.getString("templateName").equals(ACHConstants.NO_TEMPLATE_USED)) {
				if (transaction.getString("featureActionId").equals(FeatureAction.ACH_COLLECTION_CREATE))
					eventsubtype = ACHConstants.ACH_COLLECTION_ONE_TIME_TRANSFER;
				else if (transaction.getString("featureActionId").equals(FeatureAction.ACH_PAYMENT_CREATE))
					eventsubtype = ACHConstants.ACH_PAYMENT_ONE_TIME_TRANSFER;
			} else {
				if (transaction.getString("featureActionId").equals(FeatureAction.ACH_COLLECTION_CREATE))
					eventsubtype = ACHConstants.ACH_COLLECTION_TEMPLATE_TRANSFER;
				else if (transaction.getString("featureActionId").equals(FeatureAction.ACH_PAYMENT_CREATE))
					eventsubtype = ACHConstants.ACH_PAYMENT_TEMPLATE_TRANSFER;
			}
			if (res instanceof BBRequestDTO) {
				eventsubtype = Constants.REJECTED_ + eventsubtype;
			} else if (res instanceof ACHTransactionDTO) {
				approvedby = approversBusinessDelegate.getRequestActedApproversList(request.getParameter("Request_id"),
						"Approved");
				customparams.addProperty("approvedby", approvedby.toString());
			}
			List<String> rejectedBy = approversBusinessDelegate
					.getRequestActedApproversList(request.getParameter("Request_id"), "Rejected");
			if (rejectedBy == null) {
				customparams.addProperty("rejectedBy", "N/A");
			} else {
				customparams.addProperty("rejectedBy", rejectedBy.toString());
			}

			AdminUtil.addAdminUserNameRoleIfAvailable(customparams, request);

			EventsDispatcher.dispatch(request, response, "MAKE_TRANSFER", eventsubtype,
					"ACHTransactions/createACHTransaction", status, null, CustomerSession.getCustomerName(customer),
					customparams);
		} catch (Exception exp) {
			alert.prepareError("error while logging the rejection/approval of ACHrequest").log();
			return;
		}
	}

	/**
	 * Logs BBGeneralTransaction status is auditactivity
	 * 
	 * @param request
	 * @param response
	 * @param result
	 * @param createdBy
	 */
	void _logGeneralTransaction(DataControllerRequest request, DataControllerResponse response, Result result,
			String createdBy) {

		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE))
			return;

		GeneralTransactionsBusinessDelegate generalTransactionsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);

		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApproversBusinessDelegate.class);

		AuditLog auditLog = new AuditLog();

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);

		try {

			String transactionId = result.getParamValueByName("Transaction_id");
			String featureActionId = result.getParamValueByName("FeatureAction_id");

			if (StringUtils.isEmpty(createdBy))
				createdBy = "";
			JsonObject customParams = generalTransactionsBusinessDelegate.fetchTransactionEntry(transactionId,
					featureActionId, createdBy);

			String eventType = Constants.MAKE_TRANSFER;
			String eventSubType = "";
			String producer = "Transactions/POST(createTransfer)";
			String statusID = Constants.SID_EVENT_SUCCESS;
			String userName = CustomerSession.getCustomerName(customer);
			String requestId = request.getParameter("Request_id");
			String action = request.getParameter("Action");

			String fromAccountNumber = "";
			String toAccountNumber = "";

			if (customParams.has("fromAccountNumber")) {
				fromAccountNumber = customParams.get("fromAccountNumber").getAsString();
			}
			if (customParams.has("toAccountNumber")) {
				toAccountNumber = customParams.get("toAccountNumber").getAsString();
			}
			;

			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);

			eventSubType = deriveSubType(featureActionId, customParams);

			List<String> approvers = approversBusinessDelegate.getRequestApproversList(requestId);
			List<String> approvedBy = approversBusinessDelegate.getRequestActedApproversList(requestId, "Approved");
			if (approvers == null) {
				customParams.addProperty(Constants.APPROVERS, "");
			} else {
				customParams.addProperty(Constants.APPROVERS, approvers.toString());
			}
			if (approvedBy == null) {
				customParams.addProperty("approvedBy", "");
			} else {
				customParams.addProperty("approvedBy", approvedBy.toString());
			}
			List<String> rejectedBy = approversBusinessDelegate.getRequestActedApproversList(requestId, "Rejected");
			if (rejectedBy == null) {
				customParams.addProperty("rejectedBy", "N/A");
			} else {
				customParams.addProperty("rejectedBy", rejectedBy.toString());
			}
			if (action.equalsIgnoreCase("Rejected")) {
				eventSubType = Constants.REJECTED_ + eventSubType;
				customParams.addProperty(Constants.STATUS, TransactionStatusEnum.REJECTED.getStatus());
			} else {
				customParams.addProperty(Constants.STATUS, TransactionStatusEnum.APPROVED.getStatus());
			}
			if (customParams.has("transactionType")) {
				customParams.addProperty("transferType", customParams.get("transactionType").getAsString());
				customParams.remove("transactionType");
			}
			customParams.remove(Constants.CREATEDTS);
			String[] dates = { "deliverBy", "lastmodifiedts", "frequencystartdate", "frequencyenddate", "scheduledDate",
					"processingDate", "frequencyStartDate", "frequencyEndDate", "synctimestamp" };
			for (String date : dates) {
				if (customParams.has(date)) {
					customParams.get(date);
					if (date != null) {
						String dateValue = customParams.get(date).getAsString();
						dateValue = HelperMethods.convertDateFormat(dateValue, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						customParams.remove(date);
						customParams.addProperty(date, dateValue);
					}
				}
			}

			AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);

			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null, userName,
					customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine.").log();
		}
	}

	// common function for Event SubType
	private String deriveSubType(String featureActionId, JsonObject customParams) {

		AuditLog auditLog = new AuditLog();
		String eventSubType = "";

		String isScheduled = "";
		if (customParams.has(Constants.ISSCHEDULED)) {
			isScheduled = customParams.get(Constants.ISSCHEDULED).getAsString();
		}
		String frequencyType = Constants.ONCE;
		if (customParams.has(Constants.FREQUENCYTYPE)) {
			JsonElement frequencyTypeJson = customParams.get(Constants.FREQUENCYTYPE);
			if (frequencyTypeJson != null)
				frequencyType = customParams.get(Constants.FREQUENCYTYPE).getAsString();
		}
		Boolean payeeId = customParams.has("payeeId");
		String wireAccountType = "";
		String serviceName = customParams.get("featureActionId").getAsString();

		switch (featureActionId) {
		case FeatureAction.BILL_PAY_CREATE:
			eventSubType = auditLog.deriveSubTypeForBillPayment(payeeId);
			break;

		case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
		case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
			eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType, serviceName);
			break;

		case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
		case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
			eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType, serviceName);
			break;

		case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
		case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
			eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType, serviceName);
			break;

		case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
		case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
			eventSubType = auditLog.deriveSubTypeForInternalTransfer(isScheduled, frequencyType);
			break;

		case FeatureAction.P2P_CREATE:
			eventSubType = auditLog.deriveSubTypeForP2PTransfer(isScheduled, frequencyType, payeeId);
			break;

		case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
		case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
			if (customParams.has("wireAccountType")) {
				wireAccountType = customParams.get("wireAccountType").getAsString();
			}
			eventSubType = auditLog.deriveSubTypeForWireTransfer(payeeId, serviceName, wireAccountType);
			break;

		default:
			break;
		}
		return eventSubType;
	}

	@Override
	public Result fetchApprovers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String requestId = inputParams.get("requestId") != null ? inputParams.get("requestId").toString() : null;

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);

		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		JSONArray approvers = approvalQueueDelegate.fetchRequestApproverIds(requestId);
		if (approvers == null) {
			alert.prepareError("Error while fetching Approvers list.").log();
			return ErrorCodeEnum.ERR_26001.setErrorCode(result);
		}

		JSONObject obj = new JSONObject();
		obj.put("customers", approvers);

		result = JSONToResult.convert(obj.toString());
		return result;
	}

	public Result fetchRecordsPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {

		Result result = new Result();
		FilterDTO newFilterDTO = new FilterDTO();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		// String customerId = ApprovalUtilities.getCustomerId(request);
		String customerId = CustomerSession.getCustomerId(customer);

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE, FeatureAction.P2P_APPROVE, FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE, FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE, FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE, FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE, FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE, FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE,
				FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);

		String featureActionlist = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (StringUtils.isEmpty(featureActionlist)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}

		try {
			String removeByValue = inputParams.get("removeByValue") != null
					? inputParams.get("removeByValue").toString()
					: null;

			if (StringUtils.isNotEmpty(removeByValue)) {
				inputParams.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ", e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests(customerId, "", "",
				featureActionlist);

		if (mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21248.setErrorCode(new Result());
		}

		ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_filterByParam("amIApprover,status,actedByMeAlready");
		filterDTO.set_filterByValue("true,Pending,false");

		if (applicationDTO != null && applicationDTO.isSelfApprovalEnabled()) {
			filterDTO.set_removeByParam("amICreator");
			filterDTO.set_removeByValue(new HashSet<String>(Arrays.asList("true")));
		}
		List<BBRequestDTO> subRequests = filterDTO.filter(mainRequests);

		List<ApprovalRequestDTO> records = fetchAllRequests(subRequests, request);

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
	public Result fetchMyApprovalHistory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {

		Result result = new Result();
		FilterDTO newFilterDTO = new FilterDTO();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		// String customerId = ApprovalUtilities.getCustomerId(request);
		String customerId = CustomerSession.getCustomerId(customer);

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE, FeatureAction.P2P_APPROVE, FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE, FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE, FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE, FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE, FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE, FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE,
				FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);

		String featureActionlist = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (StringUtils.isEmpty(featureActionlist)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}

		try {
			String removeByValue = inputParams.get("removeByValue") != null
					? inputParams.get("removeByValue").toString()
					: null;

			if (StringUtils.isNotEmpty(removeByValue)) {
				inputParams.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ", e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests(customerId, "", "",
				featureActionlist);

		if (mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21247.setErrorCode(new Result());
		}

		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_removeByParam("status");
		filterDTO.set_removeByValue(new HashSet<>(Arrays.asList(TransactionStatusEnum.WITHDRAWN.getStatus())));
		filterDTO.set_filterByParam("actedByMeAlready");
		filterDTO.set_filterByValue("true");
		List<BBRequestDTO> subRequests = filterDTO.filter(mainRequests);

		List<ApprovalRequestDTO> records = fetchAllRequests(subRequests, request);

		List<BBActedRequestDTO> history = approvalQueueBusinessDelegate.fetchRequestHistory(customerId,
				new HashSet<String>());
		records = (new FilterDTO()).merge(records, history, "requestId=requestId", "approvalDate");

		records = newFilterDTO.filter(records);

		try {
			// ADP-7058 Fetch the 50 most recent records, as configured in fabric
//			JSONObject filterObj = new JSONObject(inputParams);
//			if(filterObj.has("searchString") && StringUtils.isEmpty(filterObj.getString("searchString"))){
//				int maxLen = 50;
//				try{
//					maxLen = Integer.parseInt(EnvironmentConfigurationsHandler.getValue("APPROVAL_HISTORY_MAX_RECORDS", request));
//				} catch(Exception e){
//					alert.prepareError("Exception while parsing APPROVAL_HISTORY_MAX_RECORDS server property! " + e).log();
//				}
//				if(records.size() > maxLen){
//					records = records.subList(0, maxLen);
//				}
//			}
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

	@SuppressWarnings("unchecked")
	@Override
	public Result approve(String methodID, Object[] inputArray, DataControllerRequest dcr,
			DataControllerResponse response) {

		Result result = new Result();
		try {
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcr);
			// String customerId = ApprovalUtilities.getCustomerId(dcr);
			String customerId = CustomerSession.getCustomerId(customer);
			String requestId = dcr.getParameter("requestId");
			String comments = "Approved";

			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);

			if (requestDTO == null) {
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}

			String featureActionId = requestDTO.getFeatureActionId();

			FeatureActionDTO featureActionDTO = FeatureActionBusinessDelegate.getFeatureActionById(featureActionId);

			boolean isAccountLevel = featureActionDTO.getIsAccountLevel().equalsIgnoreCase("true") ? true : false;

			/*
			 * allowedFeatureActionIds contains all the create action id's which the new
			 * approvals framework supports
			 */
			// Set<String> allowedFeatureActionIds = new HashSet<>();
			Set<String> signatoryGroupFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.SIGNATORY_GROUP_CREATE_EDIT, FeatureAction.SIGNATORY_GROUP_DELETE));
			Set<String> approvalMatrixFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.APPROVAL_MATRIX_MANAGE));

			// Temporary Channeling Mechanism for ValidateForApprovals V2.0 - invocation
			// point for customer-level feature actions
			// if (!isAccountLevel || allowedFeatureActionIds.contains(featureActionId)) {

			if (!isAccountLevel && !signatoryGroupFeatureActionIds.contains(featureActionId)
					&& !approvalMatrixFeatureActionIds.contains(featureActionId)) {
				Map<String, Object> inputMap = (Map<String, Object>) inputArray[1];
				inputMap.put("requestId", requestDTO.getAssocRequestId());
				result = approvalRequestResource.approveRequest(inputMap, dcr);
			} else {
				if (StringUtils.isNotEmpty(featureActionId)) {
					featureActionId = FeatureActionBusinessDelegate.getApproveFeatureAction(featureActionId);
				}
				if (StringUtils.isEmpty(featureActionId)) {
					return ErrorCodeEnum.ERR_28025.setErrorCode(result);
				}
				List<String> requiredApproveActionIds = Arrays.asList(featureActionId);
				featureActionId = CustomerSession.getPermittedActionIds(dcr, requiredApproveActionIds);
				if (featureActionId == null) {
					return ErrorCodeEnum.ERR_12001.setErrorCode(result);
				}

				Boolean flag = approvalQueueBusinessDelegate.checkIfUserAlreadyApproved(requestId, customerId);
				if (flag == true) {
					return ErrorCodeEnum.ERR_21010.setErrorCode(result);
				}

				ApplicationDTO applicationDTO = new ApplicationDTO();
				applicationDTO = applicationBusinessDelegate.properties();
				String createdby = null;

				if (requestDTO != null && StringUtils.isNotEmpty(requestDTO.getCreatedby())) {
					diagnostic.prepareInfo("Fetch BBRequest details success").log();
					createdby = requestDTO.getCreatedby();
				} else {
					alert.prepareError("Error while fetching BBRequest details").log();
					return ErrorCodeEnum.ERR_14008.setErrorCode(result);
				}
				if (applicationDTO == null) {
					diagnostic.prepareInfo("Error while fetching Application record details").log();
					return ErrorCodeEnum.ERR_12114.setErrorCode(result);
				} else if (!applicationDTO.isSelfApprovalEnabled() && createdby != null
						&& createdby.equalsIgnoreCase(customerId)) {
					return ErrorCodeEnum.ERR_12113.setErrorCode(result);
				}

				// TSR-289435
				String status = requestDTO.getStatus();
				switch (status.toUpperCase()) {
				case "APPROVED":
					return ErrorCodeEnum.ERR_21039.setErrorCode(new Result());
				case "EXECUTED":
					return ErrorCodeEnum.ERR_21039.setErrorCode(new Result());
				case "WITHDRAWN":
					return ErrorCodeEnum.ERR_21040.setErrorCode(new Result());
				case "FAILED":
					return ErrorCodeEnum.ERR_21040.setErrorCode(new Result());
				case "REJECTED":
					return ErrorCodeEnum.ERR_21040.setErrorCode(new Result());
				default:
					break;
				}

				if (StringUtils.isNotBlank(createdby) && createdby.equalsIgnoreCase(customerId)) {
					comments = "Self Approved";
				}
				/*
				 * ApprovalRequestDTO request =
				 * _fetchRequestDetails(requestId,featureActionId,dcr); if(request == null) {
				 * alert.prepareError("No request found with given requestId").log(); return
				 * ErrorCodeEnum.ERR_12000.setErrorCode(result); }
				 */
				SimpleDateFormat fileFormat = new SimpleDateFormat(Constants.SHORT_DATE_FORMAT);
				fileFormat.setLenient(false);
//			Date paymentDate;
//			Date currentDate;
//			try {
//				if(request.getProcessingDate() == null) {
//					alert.prepareError("Request do not have processing Date").log();
//					//return ErrorCodeEnum.ERR_28015.setErrorCode(result);
//				}
//				else {
//					paymentDate = fileFormat.parse(request.getProcessingDate());
//					currentDate = fileFormat.parse(applicationBusinessDelegate.getServerTimeStamp());
//					if(currentDate!=null && currentDate.after(paymentDate)) {
//						alert.prepareError("The execution date has expired and this cannot be processed further").log();
//						return ErrorCodeEnum.ERR_28014.setErrorCode(result);
//					}
//				}
//			} catch (ParseException e) {
//				alert.prepareError("Caught exception at approve method: " + e).log();
//				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
//			}

				Object res = null;
				switch (featureActionId) {
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
				case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
				case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
				case FeatureAction.BILL_PAY_APPROVE:
				case FeatureAction.P2P_APPROVE:
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
					res = approvalQueueBusinessDelegate.approveGeneralTransaction(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				case FeatureAction.ACH_FILE_APPROVE:
					res = approvalQueueBusinessDelegate.approveACHFile(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				case FeatureAction.ACH_COLLECTION_APPROVE:
				case FeatureAction.ACH_PAYMENT_APPROVE:
					res = approvalQueueBusinessDelegate.approveACHTransaction(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				case FeatureAction.BULK_PAYMENT_REQUEST_APPROVE:
					res = bulkPaymentRecordBusinessDelegate.approveBulkPaymentRecord(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				case FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE:
					res = approvalQueueBusinessDelegate.approveChequeBookrequest(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				case FeatureAction.IMPORT_LC_APPROVE:
					res = approvalQueueBusinessDelegate.approveLetterOfCredit(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				case FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE:
				case FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE:
				case FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL:
					res = approvalQueueBusinessDelegate.approveNewActionTypes(requestId, customerId, comments,
							requestDTO.getCompanyId(), dcr);
					break;
				}

				if (res == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(result);
				} else if (res instanceof BBRequestDTO) {
					result.addStringParam("transactionId", ((BBRequestDTO) res).getTransactionId());
					result.addStringParam("featureActionId", ((BBRequestDTO) res).getFeatureActionId());
					try {
						LogEvents.pushAlertsForApprovalActions(dcr, response, res, featureActionId,
								CustomerSession.getCustomerName(customer), false, "APPROVE", customer);
					} catch (Exception e) {
						alert.prepareError("Failed at LogEvents.pushAlertsForApprovalActions").log();
					}
				} else {
					try {
						LogEvents.pushAlertsForApprovalActions(dcr, response, res, featureActionId,
								CustomerSession.getCustomerName(customer), true, "APPROVE", customer);
					} catch (Exception e) {
						alert.prepareError("Failed at LogEvents.pushAlertsForApprovalActions").log();
					}
					switch (featureActionId) {
					case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
					case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
					case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
					case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
					case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
					case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
					case FeatureAction.BILL_PAY_APPROVE:
					case FeatureAction.P2P_APPROVE:
					case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
					case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
					case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
					case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
						result.addStringParam("transactionId", ((JSONObject) res).getString("Transaction_id"));
						result.addStringParam("featureActionId", ((JSONObject) res).getString("FeatureAction_id"));
						generalTransactionsBusinessDelegate.executeTransactionAfterApproval(
								((JSONObject) res).getString("Transaction_id"), featureActionId, dcr);
						break;
					case FeatureAction.ACH_FILE_APPROVE:
						result.addStringParam("transactionId", ((ACHFileDTO) res).getAchFile_id());
						result.addStringParam("featureActionId", ((ACHFileDTO) res).getFeatureActionId());
						achFileBusinessDelegate.executeACHFileAfterApproval(((ACHFileDTO) res).getAchFile_id(),
								featureActionId, dcr);
						break;
					case FeatureAction.ACH_COLLECTION_APPROVE:
					case FeatureAction.ACH_PAYMENT_APPROVE:
						result.addStringParam("transactionId", ((ACHTransactionDTO) res).getTransaction_id());
						result.addStringParam("featureActionId", ((ACHTransactionDTO) res).getFeatureActionId());
						achTransactionBusinessDelegate.executeTransactionAfterApproval(
								((ACHTransactionDTO) res).getTransaction_id(), featureActionId, dcr);
						break;
					case FeatureAction.BULK_PAYMENT_REQUEST_APPROVE:
						result.addStringParam("transactionId", ((BulkPaymentRecordDTO) res).getRecordId());
						result.addStringParam("featureActionId", ((BulkPaymentRecordDTO) res).getFeatureActionId());
						BulkPaymentRecordDTO recordDTO = bulkPaymentRecordBusinessDelegate
								.fetchBulkPaymentRecordByRequestId(requestId, dcr);
						((BulkPaymentRecordDTO) res).setConfirmationNumber(recordDTO.getRecordId());
						((BulkPaymentRecordDTO) res).setRequestId(requestId);
						((BulkPaymentRecordDTO) res).setTotalAmount(recordDTO.getTotalAmount());
						((BulkPaymentRecordDTO) res).setFromAccount(recordDTO.getFromAccount());
						((BulkPaymentRecordDTO) res).setPaymentDate(recordDTO.getPaymentDate());
						bulkPaymentRecordBusinessDelegate.executeRecordAfterApproval(
								((BulkPaymentRecordDTO) res).getRecordId(), dcr, response, result);
						break;
					case FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE:
						approvalQueueBusinessDelegate.executeChequeBookRequestAfterApproval(requestId,
								((JSONObject) res).getString("Transaction_id"), featureActionId, dcr);
						break;
					case FeatureAction.IMPORT_LC_APPROVE:
						approvalQueueBusinessDelegate.executeLetterOfCreditAfterApproval(requestId,
								((JSONObject) res).getString("Transaction_id"), featureActionId, dcr);
						;
						break;
					case FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE:
						JSONObject metaDatatoCreateSignatoryGroup = new JSONObject(requestDTO.getAdditionalMeta());
						boolean isCreateFlow = metaDatatoCreateSignatoryGroup.getBoolean("isNewFlow");
						if (isCreateFlow) {
							String signatoryGroupName = metaDatatoCreateSignatoryGroup.getString("signatoryGroupName");
							String signatoryGroupDescription = metaDatatoCreateSignatoryGroup
									.getString("signatoryGroupDescription");
							String coreCustomerId = metaDatatoCreateSignatoryGroup.getString("coreCustomerId");
							String contractId = metaDatatoCreateSignatoryGroup.getString("contractId");
							JSONArray signatories = new JSONArray(
									metaDatatoCreateSignatoryGroup.getString("newFlowData"));
							String signatoryCreatedby = metaDatatoCreateSignatoryGroup.getString("createdBy");

							JSONObject sigrResponse = signatoryGroupResource.createSignatoryGroupAfterApproval(
									signatoryGroupName, signatoryGroupDescription, coreCustomerId, contractId,
									signatories, signatoryCreatedby, isCreateFlow, dcr);
							if (sigrResponse == null) {
								alert.prepareError("Failed to approve create Signatory group request").log();
								return ErrorCodeEnum.ERR_12000.setErrorCode(result);
							}
						} else {
							String signatoryGroupValues = metaDatatoCreateSignatoryGroup
									.getString("signatoryGroupValues");
							String newSigValues = metaDatatoCreateSignatoryGroup.getString("newSigValues");
							String deleteSigValues = metaDatatoCreateSignatoryGroup.getString("deleteSigValues");
							try {
								JSONObject sigrResponse = signatoryGroupBusinessDelegate
										.updateSignatoryGroupAfterApproval(signatoryGroupValues, newSigValues,
												deleteSigValues);
								if (sigrResponse == null) {
									alert.prepareError("Failed to approve update Signatory group request").log();
									return ErrorCodeEnum.ERR_12000.setErrorCode(result);
								}
							} catch (Exception e1) {
								alert.prepareError(
										"Error while updating SignatoryGroup from SignatoryGroupBusinessDelegate : "
												+ e1)
										.log();
								return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
							}
						}
						break;
					case FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE:
						Map<String, Object> headersMap = new HashMap<String, Object>();
						JSONObject metaDatatoDeleteSignatoryGroup = new JSONObject(requestDTO.getAdditionalMeta());
						String signatoryGroupId = metaDatatoDeleteSignatoryGroup.getString("signatoryGroupId");
						boolean deleted;
						try {
							deleted = signatoryGroupBusinessDelegate.deleteSignatoryGroup(signatoryGroupId, headersMap);
						} catch (Exception e1) {
							alert.prepareError(
									"Error while deleting SignatoryGroup from SignatoryGroupBusinessDelegate : " + e1)
									.log();
							return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
						}
						if (!deleted) {
							alert.prepareError("Failed to approve create Signatory group request").log();
							return ErrorCodeEnum.ERR_12000.setErrorCode(result);
						}
						break;
					case FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL:
						JSONObject metaDataOfApprovalMatrix = new JSONObject(requestDTO.getAdditionalMeta());
						String contractId = metaDataOfApprovalMatrix.getString("contractId");
						String coreCustomerId = metaDataOfApprovalMatrix.getString("coreCustomerId");
						String approvalMatrixFeatureActionID = metaDataOfApprovalMatrix.getString("approvalMatrixFeatureActionID");
						String limitTypeId = metaDataOfApprovalMatrix.getString("limitTypeId");
						String accountIdsString = metaDataOfApprovalMatrix.getString("accountIds");
						Set<String> accountIdsList = new HashSet<>();
						if(!accountIdsString.equals("[]")) {
							accountIdsList =Arrays.stream(accountIdsString.split(",")).collect(Collectors.toSet());
						}
						String limits =  metaDataOfApprovalMatrix.getString("limits");
						JSONArray limitRulesJSONArray = new JSONArray(limits);
						List<ApprovalRuleDTO> limitRules = ApprovalUtilities.fetchRuleLimitsFromJSONArray(limitRulesJSONArray);
						boolean ruleUpdateStatus;
						try {
							com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegateFW2 = DBPAPIAbstractFactoryImpl.getBusinessDelegate(com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate.class);
							ruleUpdateStatus = approvalMatrixBusinessDelegateFW2.createOrUpdateApprovalRule(contractId, coreCustomerId, approvalMatrixFeatureActionID, accountIdsList, limitTypeId, limitRules);
						}catch (Exception e) {
							alert.prepareError("Caught exception at approve while calling approvalMatrixBusinessDelegateFW2.createOrUpdateApprovalRule method: " + e).log();
							return ErrorCodeEnum.ERR_87199.setErrorCode(result);
						}
						if (!ruleUpdateStatus) {
							alert.prepareError("Failed to update after calling approvalMatrixBusinessDelegateFW2.createOrUpdateApprovalRule method in approve").log();
							return ErrorCodeEnum.ERR_87199.setErrorCode(result);
						} else if (ruleUpdateStatus) {
							result.addParam("status", "Success");
							return result;
						}
						break;
					}
				}
				result.addStringParam("success", "Successful");
				result.addStringParam("actedBy", customerId);
				result.addStringParam("status", TransactionStatusEnum.APPROVED.getStatus());
				result.addStringParam("requestId", requestId);
				logTransaction(dcr, response, result, res, featureActionId);
			}
		} catch (ApplicationException ae) {
			return ae.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at approve method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	}

	private void logTransaction(DataControllerRequest dcr, DataControllerResponse response, Result result, Object res,
			String featureActionId) {
		try {
			switch (featureActionId) {
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
			case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
			case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
			case FeatureAction.BILL_PAY_APPROVE:
			case FeatureAction.P2P_APPROVE:
			case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
			case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
				_logGeneralTransaction(dcr, response, result,
						((ApprovalRequestDTO) JSONUtils.parse(res.toString(), ApprovalRequestDTO.class)).getSentBy());
				break;
			case FeatureAction.ACH_FILE_APPROVE:
				_logACHFile(dcr, response, result, dcr.getParameter(Constants.REQUESTID), "Approved");
				break;
			case FeatureAction.ACH_COLLECTION_APPROVE:
			case FeatureAction.ACH_PAYMENT_APPROVE:
				loggingForACHTransactionsAndTemplates(dcr, response, res, result);
				break;
			case FeatureAction.BULK_PAYMENT_REQUEST_APPROVE:
				bulkPaymentRecordResource.logApproveOrRejectTransaction(dcr, response, res, null, result);
				break;
			default:
				return;
			}
		} catch (IOException e) {
			alert.prepareError("error while logging the approval of transaction", e).log();
		}
	}

	@Override
	public Result reject(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		try {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = ApprovalUtilities.getCustomerId(request);
			String customerId = CustomerSession.getCustomerId(customer);
			String requestId = request.getParameter("requestId");
			String comments = request.getParameter("comments");

			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);
			if (requestDTO == null) {
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}

			String featureActionId = requestDTO.getFeatureActionId();

			FeatureActionDTO featureActionDTO = FeatureActionBusinessDelegate.getFeatureActionById(featureActionId);

			boolean isAccountLevel = featureActionDTO.getIsAccountLevel().equalsIgnoreCase("true") ? true : false;

			/*
			 * allowedFeatureActionIds contains all the create action id's which the new
			 * approvals framework supports
			 */
			Set<String> allowedFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.ACH_PAYMENT_CREATE, FeatureAction.BILL_PAY_CREATE));
			Set<String> signatoryGroupFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.SIGNATORY_GROUP_CREATE_EDIT, FeatureAction.SIGNATORY_GROUP_DELETE));
			Set<String> approvalMatrixFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.APPROVAL_MATRIX_MANAGE));

			// Temporary Channelling Mechanism for ValidateForApprovals V2.0 - invocation
			// point for customer-level feature actions
			if ((!isAccountLevel || allowedFeatureActionIds.contains(featureActionId))
					&& !signatoryGroupFeatureActionIds.contains(featureActionId)
					&& !approvalMatrixFeatureActionIds.contains(featureActionId)) {
				Map<String, Object> inputMap = (Map<String, Object>) inputArray[1];
				inputMap.put("requestId", requestDTO.getAssocRequestId());
				result = approvalRequestResource.rejectRequest(inputMap, request);
			} else {
				if (StringUtils.isNotEmpty(featureActionId)) {
					featureActionId = FeatureActionBusinessDelegate.getApproveFeatureAction(featureActionId);
				}
				if (StringUtils.isEmpty(featureActionId)) {
					return ErrorCodeEnum.ERR_28025.setErrorCode(result);
				}
				List<String> requiredApproveActionIds = Arrays.asList(featureActionId);
				String approveActionList = CustomerSession.getPermittedActionIds(request, requiredApproveActionIds);
				if (approveActionList == null) {
					return ErrorCodeEnum.ERR_12001.setErrorCode(result);
				}

				ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(ContractBusinessDelegate.class);
				List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
				contracts.add(CustomerSession.getCompanyId(customer));

				BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate.authorizationCheckForRejectAndWithdrawl(
						requestId, String.join(",", contracts), approveActionList);
				if (bbrequestObject == null) {
					return ErrorCodeEnum.ERR_12001.setErrorCode(result);
				}

				String groupName = "";
				if (requestDTO == null) {
					alert.prepareError("Error while fetching BBRequest details").log();
					return ErrorCodeEnum.ERR_14008.setErrorCode(result);
				}

				/*
				 * boolean isGroupMatrix = Boolean.parseBoolean(requestDTO.getIsGroupMatrix());
				 *
				 * SignatoryGroupBusinessDelegate sigGrpBusinessDelegate =
				 * DBPAPIAbstractFactoryImpl.getBusinessDelegate(SignatoryGroupBusinessDelegate.
				 * class); if(isGroupMatrix) { List<CustomerSignatoryGroupDTO> signatoryDTO =
				 * sigGrpBusinessDelegate.fetchCustomerSignatoryGroupsAssociatedWithRequest(
				 * customerId, requestId); if(signatoryDTO == null || signatoryDTO.isEmpty())
				 * return ErrorCodeEnum.ERR_12118.setErrorCode(new Result());
				 *
				 * List<String> groupNames =
				 * signatoryDTO.stream().map(CustomerSignatoryGroupDTO::getSignatoryGroupName).
				 * collect(Collectors.toList()); groupName = String.join(",", groupNames); }
				 */

				if (comments == null || comments == "") {
					comments = "Rejected";
				}

				String status = requestDTO.getStatus();
				if (status == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
				} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
					return ErrorCodeEnum.ERR_21012.setErrorCode(new Result());
				}
				status = TransactionStatusEnum.REJECTED.getStatus();
				BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status,
						request);
				if (bBRequestDTO == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
				}

				switch (featureActionId) {
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE:
				case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE:
				case FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE:
				case FeatureAction.BILL_PAY_APPROVE:
				case FeatureAction.P2P_APPROVE:
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE:
					generalTransactionsBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getFeatureActionId(), bBRequestDTO.getStatus());
					generalTransactionsBusinessDelegate.rejectGeneralTransaction(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getFeatureActionId(), request);
					_logGeneralTransaction(request, response, result, bBRequestDTO.getCreatedby());
					break;
				case FeatureAction.ACH_FILE_APPROVE:
					achFileBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), "");
					_logACHFile(request, response, result, requestId, "Rejected");
					break;
				case FeatureAction.ACH_COLLECTION_APPROVE:
				case FeatureAction.ACH_PAYMENT_APPROVE:
					achTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getStatus(), "");
					loggingForACHTransactionsAndTemplates(request, response, bBRequestDTO, result);
					break;
				case FeatureAction.BULK_PAYMENT_REQUEST_APPROVE:
					String rejectionreason = request.getParameter("rejectionreason");
					/*
					 * if (StringUtils.isEmpty(rejectionreason)) {
					 * alert.prepareError("rejectionreason is missing").log(); return
					 * ErrorCodeEnum.ERR_21227.setErrorCode(new Result()); }
					 */
					bulkPaymentRecordBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getStatus(), "", "");
					bulkPaymentRecordResource.logApproveOrRejectTransaction(request, response, bBRequestDTO,
							EventSubType.BULK_PAYMENT_REQUEST_REJECT, result);
					try {
						bulkPaymentRecordBackendDelegate.rejectBulkPaymentRecord(bBRequestDTO.getTransactionId(),
								comments, rejectionreason, request);
					} catch (Exception e) {
						alert.prepareError("Error while updating the status of Bulk Payment record", e).log();
					}
					break;
				case FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE:
					Boolean isRejected = approvalQueueBusinessDelegate
							.rejectChequeBookRequest(bBRequestDTO.getTransactionId(), comments, request);
					if (!isRejected) {
						status = TransactionStatusEnum.PENDING.getStatus();
						bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);
						return ErrorCodeEnum.ERR_12116.setErrorCode(new Result());
					}
					break;
				case FeatureAction.IMPORT_LC_APPROVE:
					Boolean isLetterOfCreditRejected = approvalQueueBusinessDelegate
							.rejectLetterOfCredit(bBRequestDTO.getTransactionId(), comments, request);
					if (!isLetterOfCreditRejected) {// Change the error code
						status = TransactionStatusEnum.PENDING.getStatus();
						bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);
						return ErrorCodeEnum.ERR_12116.setErrorCode(new Result());
					}
					break;
				case FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE:
				case FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE:
				case FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL:
					break;

				}

				approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
						bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus(), groupName);

				try {
					LogEvents.pushAlertsForApprovalActions(request, response, bBRequestDTO, featureActionId,
							CustomerSession.getCustomerName(customer), false, "REJECT", customer);
				} catch (Exception e) {
					alert.prepareError("Failed at LogEvents.pushAlertsForApprovalActions").log();
				}
				result.addStringParam("transactionId", bBRequestDTO.getTransactionId());
				result.addStringParam("featureActionId", bBRequestDTO.getFeatureActionId());
				result.addStringParam("actedBy", customerId);
				result.addStringParam("status", bBRequestDTO.getStatus());
				result.addStringParam("requestId", requestId);
			}
			return result;
		} catch (ApplicationException ae) {
			return ae.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at reject method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	@Override
	public Result withdraw(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			String requestId = request.getParameter("requestId");

			BBRequestDTO requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);
			if (requestDTO == null) {
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}

			String featureActionId = requestDTO.getFeatureActionId();
			FeatureActionDTO featureActionDTO = FeatureActionBusinessDelegate.getFeatureActionById(featureActionId);

			boolean isAccountLevel = featureActionDTO.getIsAccountLevel().equalsIgnoreCase("true") ? true : false;

			/*
			 * allowedFeatureActionIds contains all the create action id's which the new
			 * approvals framework supports
			 */
			Set<String> allowedFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.ACH_PAYMENT_CREATE, FeatureAction.BILL_PAY_CREATE));
			Set<String> signatoryGroupFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.SIGNATORY_GROUP_CREATE_EDIT, FeatureAction.SIGNATORY_GROUP_DELETE));
			Set<String> approvalMatrixFeatureActionIds = new HashSet<>(
					Arrays.asList(FeatureAction.APPROVAL_MATRIX_MANAGE));

			// Temporary Channelling Mechanism for ValidateForApprovals V2.0 - invocation
			// point for customer-level feature actions
			// if (!isAccountLevel || allowedFeatureActionIds.contains(featureActionId)) {
			if ((!isAccountLevel || allowedFeatureActionIds.contains(featureActionId))
					&& !signatoryGroupFeatureActionIds.contains(featureActionId)
					&& !approvalMatrixFeatureActionIds.contains(featureActionId)) {
				Map<String, Object> inputMap = (Map<String, Object>) inputArray[1];
				inputMap.put("requestId", requestDTO.getAssocRequestId());
				result = approvalRequestResource.withdrawRequest(inputMap, request);
			} else {
				Map<String, Object> customer = CustomerSession.getCustomerMap(request);
				// String customerId = ApprovalUtilities.getCustomerId(request);
				String customerId = CustomerSession.getCustomerId(customer);
				String createActionList = CustomerSession.getPermittedActionIds(request,
						Arrays.asList(featureActionId));
				if (createActionList == null) {
					return ErrorCodeEnum.ERR_12001.setErrorCode(result);
				}

				ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(ContractBusinessDelegate.class);
				List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
				contracts.add(CustomerSession.getCompanyId(customer));

				BBRequestDTO bbrequestObject = approvalQueueBusinessDelegate.authorizationCheckForRejectAndWithdrawl(
						requestId, String.join(",", contracts), createActionList);
				if (bbrequestObject != null && !((CustomerSession.IsCombinedUser(customer)
						&& custDelegate.getCombinedUserIds(customerId).contains(bbrequestObject.getCreatedby()))
						|| (customerId.equals(bbrequestObject.getCreatedby())))) {
					return ErrorCodeEnum.ERR_12001.setErrorCode(result);
				}

//				String comments = request.getParameter("comments");
//				if(comments == null || comments == "") {
//					comments = "Withdrawn";
//				}

				String comments = Constants.DEFAULT_COMMENT_WITHDRAWN;

				try {
					String comment = request.getParameter("comments");
					if (comment != null) {
						if (!comment.isEmpty()) {
							comments = (String) request.getParameter("comments");
						}
					}
				} catch (Exception e) {
					alert.prepareError("withdrawRequest : Approvals Framework 1.0 : unable to get comments" + e).log();
				}

				String status = approvalQueueBusinessDelegate.getRequestStatus(requestId);
				if (status == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
				} else if (!(status.equals(TransactionStatusEnum.PENDING.getStatus()))) {
					return ErrorCodeEnum.ERR_21011.setErrorCode(new Result());
				}
				status = TransactionStatusEnum.WITHDRAWN.getStatus();
				BBRequestDTO bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status,
						request);
				if (bBRequestDTO == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
				}

				switch (featureActionId) {
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
				case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
				case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
				case FeatureAction.BILL_PAY_CREATE:
				case FeatureAction.P2P_CREATE:
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
					generalTransactionsBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getFeatureActionId(), bBRequestDTO.getStatus());
					generalTransactionsBusinessDelegate.withdrawGeneralTransaction(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getFeatureActionId(), request);
					break;
				case FeatureAction.ACH_FILE_UPLOAD:
					achFileBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), "");
					break;
				case FeatureAction.ACH_COLLECTION_CREATE:
				case FeatureAction.ACH_PAYMENT_CREATE:
					achTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getStatus(), "");
					break;
				case FeatureAction.BULK_PAYMENT_SINGLE_SUBMIT:
					bulkPaymentRecordBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getStatus(), "", "");
					break;
				case FeatureAction.BULK_PAYMENT_MULTIPLE_SUBMIT:
					bulkPaymentRecordBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(),
							bBRequestDTO.getStatus(), "", "");
					break;
				case FeatureAction.CHEQUE_BOOK_REQUEST_CREATE:
					Boolean success = approvalQueueBusinessDelegate.withdrawRequest(bBRequestDTO.getTransactionId(),
							comments, request);
					if (!success) {
						status = TransactionStatusEnum.PENDING.getStatus();
						bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);
						return ErrorCodeEnum.ERR_12117.setErrorCode(new Result());
					}
					break;
				case FeatureAction.IMPORT_LC_CREATE:
					Boolean isSuccess = approvalQueueBusinessDelegate
							.withdrawLetterOfCredit(bBRequestDTO.getTransactionId(), comments, request);
					if (!isSuccess) {// Change the error code
						status = TransactionStatusEnum.PENDING.getStatus();
						bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId, status, request);
						return ErrorCodeEnum.ERR_12117.setErrorCode(new Result());
					}
					break;
				case FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE:
					// bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId,
					// status, request);
					break;
				case FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE:
					// bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId,
					// status, request);
					break;
				case FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL:
					// bBRequestDTO = approvalQueueBusinessDelegate.updateBBRequestStatus(requestId,
					// status, request);
					break;
				}

				approvalQueueBusinessDelegate.logActedRequest(bBRequestDTO.getRequestId(), bBRequestDTO.getCompanyId(),
						bBRequestDTO.getStatus(), comments, customerId, bBRequestDTO.getStatus());

				try {
					LogEvents.pushAlertsForApprovalActions(request, response, bBRequestDTO, featureActionId,
							CustomerSession.getCustomerName(customer), false, "WITHDRAW", customer);
				} catch (Exception e) {
					alert.prepareError("Failed at LogEvents.pushAlertsForApprovalActions").log();
				}
				result.addStringParam("transactionId", bBRequestDTO.getTransactionId());
				result.addStringParam("featureActionId", bBRequestDTO.getFeatureActionId());
				result.addStringParam("actedBy", customerId);
				result.addStringParam("status", bBRequestDTO.getStatus());
				result.addStringParam("requestId", requestId);
			}
			return result;
		} catch (ApplicationException ae) {
			return ae.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at withdraw method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	private ApprovalRequestDTO _fetchRequestDetails(String requestId, String featureActionId,
			DataControllerRequest dcr) {
		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests("", "", requestId,
				featureActionId);
		if (CollectionUtils.isEmpty(mainRequests)) {
			alert.prepareError("Error occurred while fetching bulk payment requests from Approval Queue").log();
			return null;
		}
		List<ApprovalRequestDTO> records = fetchAllRequests(mainRequests, dcr);
		if (CollectionUtils.isNotEmpty(records))
			return records.get(0);
		return null;
	}

	@Override
	public List<ApprovalRequestDTO> fetchAllRequests(List<BBRequestDTO> requests, DataControllerRequest dcr) {

		Map<String, Object> customer = CustomerSession.getCustomerMap(dcr);
		// String customerId = ApprovalUtilities.getCustomerId(dcr);
		String customerId = CustomerSession.getCustomerId(customer);
		List<BBRequestDTO> requestsWithNoMetaData = new ArrayList<>();
		List<BBRequestDTO> requestsWithMetaData = new ArrayList<>();

		// ADP-7058 - since bbRequest object would contain the additional t24 data,
		// extract from the bbRequest DTO object additionalMeta
		String additionalMeta;
		List<ApprovalRequestDTO> records = new ArrayList<>();
		ApprovalRequestDTO tempApprovalRequestDTO;
		// ADP-7058 - populate requestsWithNoMetaData with requests having no meta-data
		for (BBRequestDTO bbRequest : requests) {
			additionalMeta = bbRequest.getAdditionalMeta();
			if (StringUtils.isEmpty(additionalMeta) || additionalMeta == null) {
				requestsWithNoMetaData.add(bbRequest);
			} else {
				requestsWithMetaData.add(bbRequest);
			}
		}
		try {
			for (BBRequestDTO bbRequest : requestsWithMetaData) {
				additionalMeta = bbRequest.getAdditionalMeta();
				try {
					try {
						if (bbRequest.getFeatureActionId().contains("_EDIT")) {
							JSONObject ADMJsonObject = new JSONObject(additionalMeta);
							Object cifMapping = ADMJsonObject.get("cif");
							String cifMappingstr = cifMapping.toString();
							ADMJsonObject.remove("cif");
							ADMJsonObject.put("cif", cifMappingstr);
							additionalMeta = ADMJsonObject.toString();
						}
					} catch (Exception e) {
						alert.prepareError("Invalid meta data stored in the db for requestId: "
								+ bbRequest.getRequestId() + " metadata will be refreshed.").log();
					}
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
					tempApprovalRequestDTO.setSentByName(bbRequest.getSentByName());
					tempApprovalRequestDTO.setSentByUserName(bbRequest.getSentByUserName());
					tempApprovalRequestDTO.setCreatedts(bbRequest.getCreatedts());
					tempApprovalRequestDTO.setAdditionalMeta(additionalMeta);

					records.add(tempApprovalRequestDTO);
				} catch (Exception e) {
					alert.prepareError("Invalid meta data stored in the db for requestId: " + bbRequest.getRequestId()
							+ " metadata will be refreshed.").log();
					requestsWithNoMetaData.add(bbRequest);
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		// ADP-7058 - now for each of the requests in requestsWithNoMetaData list, fetch
		// the additionalMeta, and store in DBXDB
		if (requestsWithNoMetaData.size() > 0) {
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
			for (BBRequestDTO dto : requestsWithNoMetaData) {
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
			if (!CollectionUtils.isEmpty(bulkPaymentsRequests)) {
				try {
					metaFetchRecords.addAll(JSONUtils.parseAsList(
							new JSONArray(bulkPaymentRecordBusinessDelegate
									.fetchAllRecordsFromBackendWithApprovalInfo(bulkPaymentsRequests, dcr)).toString(),
							ApprovalRequestDTO.class));
				} catch (IOException e) {
					alert.prepareError("Error occured while getting bulk payment requests", e).log();
				}
			}

			// DBB-12763 enable single bulk, and multiple bulk
			if (!CollectionUtils.isEmpty(bulkPaymentsSingleRequests)) {
				try {
					metaFetchRecords.addAll(JSONUtils.parseAsList(new JSONArray(bulkPaymentRecordBusinessDelegate
							.fetchAllRecordsFromBackendWithApprovalInfo(bulkPaymentsSingleRequests, dcr)).toString(),
							ApprovalRequestDTO.class));
				} catch (IOException e) {
					alert.prepareError("Error occured while getting single bulk payment requests", e).log();
				}
			}
			if (!CollectionUtils.isEmpty(bulkPaymentsMultipleRequests)) {
				try {
					metaFetchRecords.addAll(JSONUtils.parseAsList(new JSONArray(bulkPaymentRecordBusinessDelegate
							.fetchAllRecordsFromBackendWithApprovalInfo(bulkPaymentsMultipleRequests, dcr)).toString(),
							ApprovalRequestDTO.class));
				} catch (IOException e) {
					alert.prepareError("Error occured while getting multiple bulk payment requests", e).log();
				}
			}

			if (!CollectionUtils.isEmpty(chequeRequests))
				metaFetchRecords.addAll(approvalQueueBusinessDelegate.fetchChequeBookInfo(chequeRequests, dcr));
			if (!CollectionUtils.isEmpty(achFileRequests))
				metaFetchRecords.addAll(achFileBusinessDelegate.fetchACHFilesWithApprovalInfo(achFileRequests, dcr));
			if (!CollectionUtils.isEmpty(achTransactionRequests))
				metaFetchRecords.addAll(achTransactionBusinessDelegate
						.fetchACHTransactionsWithApprovalInfo(achTransactionRequests, dcr));
			if (!CollectionUtils.isEmpty(billPayRequests))
				metaFetchRecords.addAll(billPayTransactionBusinessDelegate
						.fetchBillPayTransactionsWithApprovalInfo(billPayRequests, dcr));
			if (!CollectionUtils.isEmpty(p2pRequests))
				metaFetchRecords
						.addAll(p2PTransactionBusinessDelegate.fetchP2PTransactionsWithApprovalInfo(p2pRequests, dcr));
			if (!CollectionUtils.isEmpty(interBankRequests))
				metaFetchRecords.addAll(interBankFundTransferBusinessDelegate
						.fetchInterBankTransactionsWithApprovalInfo(interBankRequests, dcr));
			if (!CollectionUtils.isEmpty(intraBankRequests))
				metaFetchRecords.addAll(intraBankFundTransferBusinessDelegate
						.fetchIntraBankTransactionsWithApprovalInfo(intraBankRequests, dcr));
			if (!CollectionUtils.isEmpty(domesticWireTransfers))
				metaFetchRecords.addAll(domesticWireTransactionBusinessDelegate
						.fetchWireTransactionsWithApprovalInfo(domesticWireTransfers, dcr));
			if (!CollectionUtils.isEmpty(internationalWireTransfers))
				metaFetchRecords.addAll(internationalWireTransactionBusinessDelegate
						.fetchWireTransactionsWithApprovalInfo(internationalWireTransfers, dcr));
			if (!CollectionUtils.isEmpty(internationalAccountsFundsTransferRequests))
				metaFetchRecords.addAll(
						internationalFundTransferBusinessDelegate.fetchInternationalTransactionsWithApprovalInfo(
								internationalAccountsFundsTransferRequests, dcr));
			if (!CollectionUtils.isEmpty(ownAccountFundsTransferRequests))
				metaFetchRecords.addAll(ownAccountFundTransferBusinessDelegate
						.fetchOwnAccountFundTransactionsWithApprovalInfo(ownAccountFundsTransferRequests, dcr));
			if (!CollectionUtils.isEmpty(letterOfCredits))
				metaFetchRecords.addAll(approvalQueueBusinessDelegate.fetchLetterOfCreditDetails(letterOfCredits, dcr));

			// update all the requests additionalMeta in DBXDB
			for (ApprovalRequestDTO approvalReqDTO : metaFetchRecords) {
				approvalQueueBusinessDelegate.updateAdditionalMetaForApprovalRequestInDB(approvalReqDTO);
			}
			records.addAll(metaFetchRecords);
		}

		Set<String> accounts = new HashSet<String>();
		Set<String> sentByIds = new HashSet<String>();
		try {
			for (ApprovalRequestDTO dto : records) {
				accounts.add(dto.getAccountId());
				sentByIds.add(dto.getSentBy());
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		try {
			FeatureActionBusinessDelegate limitGroupDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(FeatureActionBusinessDelegate.class);
			List<FeatureActionDTO> featureactions = limitGroupDelegate.fetchFeatureActionsWithLimitGroupDetails();
			records = (new FilterDTO()).merge(records, featureactions, "featureActionId=featureActionId",
					"featureName,featureActionName,limitGroupName");
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		try {
			AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(AccountBusinessDelegate.class);
			List<CustomerAccountsDTO> customerAccountsDTOs = accountBusinessDelegate.getAccountDetails(customerId,
					accounts);
			records = (new FilterDTO()).merge(records, customerAccountsDTOs, "accountId=accountId", "");
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		try {
			CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(CustomerBusinessDelegate.class);
			List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(sentByIds);
			records = (new FilterDTO()).merge(records, customerDTOs, "sentBy=id", "fullName", "sentBy=fullName");
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}

		return records;
	}

	@Override
	public Result fetchAllMyPendingRequests(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {

		Result result = new Result();
		FilterDTO newFilterDTO = new FilterDTO();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		// String customerId = ApprovalUtilities.getCustomerId(request);
		String customerId = CustomerSession.getCustomerId(customer);

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_VIEW,
				FeatureAction.BILL_PAY_VIEW_PAYMENTS, FeatureAction.P2P_VIEW, FeatureAction.ACH_FILE_VIEW,
				FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW, FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW, FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW,
				FeatureAction.SIGNATORY_GROUP_VIEW,FeatureAction.APPROVAL_MATRIX_VIEW);

		List<String> requiredActionIdsToFetch = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE, FeatureAction.P2P_APPROVE, FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE, FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE, FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE, FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE, FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE, FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE,
				FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);

		String featureActionlist = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (StringUtils.isEmpty(featureActionlist)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}

		try {
			String removeByValue = inputParams.get("removeByValue") != null
					? inputParams.get("removeByValue").toString()
					: null;

			if (StringUtils.isNotEmpty(removeByValue)) {
				inputParams.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ", e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		alert.prepareError("customer id " + customerId + "abhiabhi").log();
		alert.prepareError("requiredActionIdsToFetch" + requiredActionIdsToFetch + "abhiabhi").log();
		diagnostic.prepareDebug("customer id " + customerId + "abhiabhi").log();
		diagnostic.prepareDebug("requiredActionIdsToFetch" + requiredActionIdsToFetch + "abhiabhi").log();
		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests(customerId, "", "",
				String.join(",", requiredActionIdsToFetch));

		if (mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21250.setErrorCode(new Result());
		}

		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_filterByParam("status,amICreator");
		filterDTO.set_filterByValue("Pending,true");

		List<BBRequestDTO> subRequests = filterDTO.filter(mainRequests);
		List<ApprovalRequestDTO> records = fetchAllRequests(subRequests, request);
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
	public Result fetchMyRequestHistory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {

		Result result = new Result();
		FilterDTO newFilterDTO = new FilterDTO();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		// String customerId = ApprovalUtilities.getCustomerId(request);
		String customerId = CustomerSession.getCustomerId(customer);

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_VIEW,
				FeatureAction.BILL_PAY_VIEW_PAYMENTS, FeatureAction.P2P_VIEW, FeatureAction.ACH_FILE_VIEW,
				FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW, FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW, FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW,
				FeatureAction.CHEQUE_BOOK_REQUEST_VIEW, FeatureAction.IMPORT_LC_VIEW,
				FeatureAction.SIGNATORY_GROUP_VIEW,FeatureAction.APPROVAL_MATRIX_VIEW);

		List<String> requiredIdsToFetch = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE, FeatureAction.P2P_APPROVE, FeatureAction.ACH_FILE_APPROVE,
				FeatureAction.ACH_COLLECTION_APPROVE, FeatureAction.ACH_PAYMENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE, FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE, FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE, FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE, FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE, FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE,
				FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);

		String featureActionlist = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (StringUtils.isEmpty(featureActionlist)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}

		try {
			String removeByValue = inputParams.get("removeByValue") != null
					? inputParams.get("removeByValue").toString()
					: null;

			if (StringUtils.isNotEmpty(removeByValue)) {
				inputParams.put("removeByValue", new HashSet<String>(Arrays.asList(removeByValue.split(","))));
			}
			newFilterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ", e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests(customerId, "", "",
				String.join(",", requiredIdsToFetch));

		if (mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests from Approval Queue").log();
			return ErrorCodeEnum.ERR_21249.setErrorCode(new Result());
		}

		FilterDTO filterDTO = new FilterDTO();
		filterDTO.set_removeByParam("status");
		filterDTO.set_removeByValue(new HashSet<>(Arrays.asList(TransactionStatusEnum.PENDING.getStatus())));

		filterDTO.set_filterByParam("amICreator");
		filterDTO.set_filterByValue("true");

		List<BBRequestDTO> subRequests = filterDTO.filter(mainRequests);

		List<ApprovalRequestDTO> records = fetchAllRequests(subRequests, request);

		Set<String> recordSet = new HashSet<String>();

		for (ApprovalRequestDTO record : records) {
			recordSet.add(record.getRequestId());
		}

		List<BBActedRequestDTO> history = approvalQueueBusinessDelegate.fetchRequestHistory(null, recordSet);
		records = (new FilterDTO()).merge(records, history, "requestId=requestId", "actionts");

		records = newFilterDTO.filter(records);

		try {
			// ADP-7058 Fetch the 50 most recent records, as configured in fabric
//			JSONObject filterObj = new JSONObject(inputParams);
//			if(filterObj.has("searchString") && StringUtils.isEmpty(filterObj.get("searchString"))){
//				int maxLen = 50;
//				try{
//					maxLen = Integer.parseInt(EnvironmentConfigurationsHandler.getValue("APPROVAL_HISTORY_MAX_RECORDS", request));
//				} catch(Exception e){
//					alert.prepareError("Exception while parsing APPROVAL_HISTORY_MAX_RECORDS server property! " + e).log();
//				}
//				if(records.size() > maxLen){
//					records = records.subList(0, maxLen);
//				}
//			}
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
	public Result validateForApprovals(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {
		TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);

		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String actionId = inputParams.containsKey("featureActionID") ? (String) inputParams.get("featureActionID")
				: null;

		FeatureActionDTO actionDTO = FeatureActionBusinessDelegate.getFeatureActionById(actionId);

		ApplicationDTO applicationDTO = new ApplicationDTO();
		ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApplicationBusinessDelegate.class);
		applicationDTO = applicationBusinessDelegate.properties();

		boolean isAccountLevel = actionDTO.getIsAccountLevel().equalsIgnoreCase("true") ? true : false;
		Set<String> signatoryGroupFeatureActionIds = new HashSet<>(
				Arrays.asList(FeatureAction.SIGNATORY_GROUP_CREATE_EDIT, FeatureAction.SIGNATORY_GROUP_DELETE));
		Set<String> approvalMatrixFeatureActionIds = new HashSet<>(Arrays.asList(FeatureAction.APPROVAL_MATRIX_MANAGE));
		Set<String> combinedNewActionsIds = new HashSet<String>();
		combinedNewActionsIds.addAll(signatoryGroupFeatureActionIds);
		combinedNewActionsIds.addAll(approvalMatrixFeatureActionIds);

		/*
		 * allowedFeatureActionIds contains all the create action id's which the new
		 * approvals framework supports Set<String> allowedFeatureActionIds = new
		 * HashSet<>(Arrays.asList(FeatureAction.ACH_PAYMENT_CREATE,
		 * FeatureAction.BILL_PAY_CREATE)); if (!isAccountLevel ||
		 * allowedFeatureActionIds.contains(actionId)) {
		 */

		// Temporary Channelling Mechanism for ValidateForApprovals V2.0 - invocation
		// point for customer-level feature actions
		if (!isAccountLevel && !signatoryGroupFeatureActionIds.contains(actionId)
				&& !approvalMatrixFeatureActionIds.contains(actionId)) {
			try {
				result = approvalRequestResource.validateForApprovals(inputParams, request);
			} catch (ApplicationException ae) {
				return ae.getErrorCodeEnum().setErrorCode(result);
			} catch (Exception e) {
				alert.prepareError("Error occurred while invoking new validateForApprovals!: " + e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		} else {
			String offsetDetails = inputParams.get("offsetDetails") == null ? null
					: inputParams.get("offsetDetails").toString();
			inputParams.remove("offsetDetails");
			
				diagnostic.prepareDebug("HBL:: TSR-773647 ApprovalQueueResourceImpl: validateForApprovals: inputParams:"+inputParams.toString()).log();
				if(StringUtils.isNotBlank(inputParams.get("status").toString()) && !inputParams.get("status").toString().equals("NEW")) { 
				JSONObject StatusObj = new JSONObject(inputParams.get("status").toString());
				String StatusVal = StatusObj.getString("status").toString().toUpperCase();
				if(StatusVal != null && StatusVal.equals("NEW")) {
					inputParams.put("status", "NEW");
				}
				}
			TransactionStatusDTO inputTransactionStatusDTO = null;
			try {
				inputTransactionStatusDTO = JSONUtils.parse(new JSONObject(inputParams).toString(),
						TransactionStatusDTO.class);
					diagnostic.prepareDebug("TSR-773647:: NEW input params after parse: " + inputParams).log();

			} catch (IOException e) {
				alert.prepareError("Error occured while fetching the input params: " + e).log();
				return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
			}

			String featureActionId = inputTransactionStatusDTO.getFeatureActionID();
			String featureActionlist = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureActionId));
			if (StringUtils.isEmpty(featureActionlist)) {
				alert.prepareError("feature List is missing").log();
				return ErrorCodeEnum.ERR_12040.setErrorCode(result);
			}

			String confirmationNumber = inputTransactionStatusDTO.getConfirmationNumber();
			String oldRequestId = inputTransactionStatusDTO.getRequestId();

			FeatureActionDTO featureActionDTO = FeatureActionBusinessDelegate.getFeatureActionById(featureActionId);
			if (StringUtils.isBlank(featureActionDTO.getApproveFeatureAction())) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.toString()));
				result.addParam(new Param("confirmationNumber", confirmationNumber));
				return result;
			}
			TransactionStatusDTO transactionStatusDTO = null;
			if (Constants.NON_MONETARY.equals(featureActionDTO.getTypeId())) {
				transactionStatusDTO = limitsDelegate.validateForLimitsForNonMonetaryActions(inputTransactionStatusDTO,request);

			} else if (StringUtils.isNotBlank(offsetDetails)) {
				Map<String, Double> offsetDetailsMap = JSONUtils.parseAsMap(offsetDetails, String.class, Double.class);
				inputTransactionStatusDTO.setOffsetDetails(offsetDetailsMap);
				transactionStatusDTO = limitsDelegate.validateOffsetLimitsForACHFile(null,
						inputTransactionStatusDTO.getCustomerId(), inputTransactionStatusDTO.getCompanyId(),
						inputTransactionStatusDTO.getOffsetDetails(), inputTransactionStatusDTO.getStatus(), request);

			} else {
				transactionStatusDTO = limitsDelegate.validateForLimits(inputTransactionStatusDTO.getCustomerId(),
						inputTransactionStatusDTO.getCompanyId(), inputTransactionStatusDTO.getAccountId(),
						inputTransactionStatusDTO.getFeatureActionID(), inputTransactionStatusDTO.getAmount(),
						inputTransactionStatusDTO.getStatus(), inputTransactionStatusDTO.getDate(),
						inputTransactionStatusDTO.getTransactionCurrency(),
						inputTransactionStatusDTO.getServiceCharge(), request);

			}

			if (transactionStatusDTO == null) {
				alert.prepareError("Failed to validate limits").log();
				return ErrorCodeEnum.ERR_29013.setErrorCode(result);
			}

			TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();

			if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				if (transactionStatus != null) {
					result.addParam(new Param("status", transactionStatus.toString()));
				}
				return result;
			}

			if (!Constants.NON_MONETARY.equals(featureActionDTO.getTypeId())) {
				try {
					String amountValue = "0.0";
					String serviceCharge = "0.0";
					String transactionAmount = "0.0";
					amountValue = transactionStatusDTO.getAmount() == null ? amountValue
							: transactionStatusDTO.getAmount().toString();
					serviceCharge = transactionStatusDTO.getServiceCharge() == null ? serviceCharge
							: transactionStatusDTO.getServiceCharge();
					transactionAmount = transactionStatusDTO.getTransactionAmount() == null ? transactionAmount
							: transactionStatusDTO.getTransactionAmount();
					result.addParam(new Param("amount", amountValue));
					result.addParam(new Param("transactionAmount", transactionAmount));
					result.addParam(new Param("serviceCharge", serviceCharge));
				} catch (Exception e) {
					alert.prepareError("Failed to parse amount").log();
					return ErrorCodeEnum.ERR_27017.setErrorCode(result);
				}
			}

			result.addParam(new Param("status", transactionStatus.toString()));
			result.addParam(new Param("confirmationNumber", confirmationNumber));
			if (transactionStatus == TransactionStatusEnum.PENDING) {

				ApprovalModeBusinessDelegate approvalModeBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(ApprovalModeBusinessDelegate.class);
				AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(AccountBusinessDelegate.class);
				String accountId = null;
				String coreCustomerId = null;
				String legalEntityId = null;
				boolean isGroupLevel = false;
				CustomerAccountsDTO account = null;

				if (StringUtils.isNotBlank(offsetDetails) && transactionStatusDTO.getAccountId() != null) {
					// For ACH File Transactions
					Set<String> cifs = new HashSet<String>();
					String[] accountIds = transactionStatusDTO.getAccountId().split(",");

					accountId = transactionStatusDTO.getAccountId().split(",")[0];
					account = accountBusinessDelegate.getAccountDetails(inputTransactionStatusDTO.getCustomerId(),
							accountId);
					coreCustomerId = account.getCoreCustomerId();
					isGroupLevel = approvalModeBusinessDelegate.isGroupLevel(coreCustomerId);

					cifs.add(coreCustomerId);

					// To verify if ach file batches contains different accounts which has different
					// types of approval matrix(user based & signatory group based)
					for (int i = 1; i < accountIds.length; i++) {
						account = accountBusinessDelegate.getAccountDetails(inputTransactionStatusDTO.getCustomerId(),
								accountIds[i]);
						coreCustomerId = account.getCoreCustomerId();
						if (!cifs.contains(coreCustomerId)) {
							if (isGroupLevel != approvalModeBusinessDelegate.isGroupLevel(coreCustomerId)) {
								alert.prepareError(
										"File contains multiple accounts which belongs to different type of approval matrices")
										.log();
								return ErrorCodeEnum.ERR_12528.setErrorCode(result);
							}
							cifs.add(coreCustomerId);
						}
					}

					accountId = transactionStatusDTO.getAccountId();
				} else {
					// For Other types of transactions
					if (combinedNewActionsIds.contains(actionId)) {
						accountId = null;
						coreCustomerId = inputTransactionStatusDTO.getCoreCustomerId();
					} else {
						accountId = inputTransactionStatusDTO.getAccountId();
						account = accountBusinessDelegate.getAccountDetails(inputTransactionStatusDTO.getCustomerId(),
								accountId);
						coreCustomerId = account.getCoreCustomerId();

					}
					isGroupLevel = approvalModeBusinessDelegate.isGroupLevel(coreCustomerId);
				}
				try {
					if (combinedNewActionsIds.contains(actionId)) {
						legalEntityId = LegalEntityUtil.getLegalEntityForCifAndContract(
								inputTransactionStatusDTO.getCoreCustomerId(),
								inputTransactionStatusDTO.getContractId());
					} else {
						legalEntityId = LegalEntityUtil.getLegalEntityForCifAndContract(account.getCoreCustomerId(),
								account.getContractId());
					}
				} catch (ApplicationException e) {
					return e.getErrorCodeEnum().setErrorCode(new Result());
				}
				List<String> matrixIds = transactionStatusDTO.getApprovalMatrixIds();
				BBRequestDTO bbRequestDTO;
				bbRequestDTO = new BBRequestDTO(null, confirmationNumber, matrixIds,
						inputTransactionStatusDTO.getFeatureActionID(), inputTransactionStatusDTO.getCompanyId(),
						accountId, transactionStatus.getStatus(), inputTransactionStatusDTO.getCustomerId(), 0, 0,
						legalEntityId);
				bbRequestDTO.setIsSelfApproved(String.valueOf(transactionStatusDTO.isSelfApproved()));
				bbRequestDTO.setSignatoryGroupMatrices(transactionStatusDTO.getSignatoryGroupMatrices());
				if (signatoryGroupFeatureActionIds.contains(actionId)) {
					JSONObject additionalMetaObj = new JSONObject();
					additionalMetaObj.put("signatoryGroupId", inputTransactionStatusDTO.getConfirmationNumber());
					additionalMetaObj.put("signatoryGroupName", inputTransactionStatusDTO.getSignatoryGroupName());
					additionalMetaObj.put("signatoryGroupDescription",
							inputTransactionStatusDTO.getSignatoryGroupDescription());
					if (inputTransactionStatusDTO.getIsNewFlow()) {
						JSONObject jsonNewSignatories = new JSONObject();
						jsonNewSignatories.put("signatories", inputTransactionStatusDTO.getNewSignatories());
						additionalMetaObj.put("newInfo", jsonNewSignatories.toString());
					} else {
						additionalMetaObj.put("newInfo", inputTransactionStatusDTO.getNewSignatories());
						additionalMetaObj.put("newSigValues", inputTransactionStatusDTO.getNewSigValues());
						additionalMetaObj.put("deleteSigValues", inputTransactionStatusDTO.getDeleteSigValues());
					}
					additionalMetaObj.put("newFlowData", inputTransactionStatusDTO.getNewSignatories());
					additionalMetaObj.put("oldInfo", inputTransactionStatusDTO.getOldSignatories());
					additionalMetaObj.put("isNewFlow", inputTransactionStatusDTO.getIsNewFlow());
					additionalMetaObj.put("createdBy", inputTransactionStatusDTO.getCustomerId());
					additionalMetaObj.put("contractId", inputTransactionStatusDTO.getContractId());
					additionalMetaObj.put("coreCustomerId", inputTransactionStatusDTO.getCoreCustomerId());
					additionalMetaObj.put("signatoryGroupValues", inputTransactionStatusDTO.getSignatoryGroupValues());
					bbRequestDTO.setAdditionalMeta(additionalMetaObj.toString());
				} else if (approvalMatrixFeatureActionIds.contains(actionId)) {
					JSONObject additionalMetaObj = new JSONObject();
					additionalMetaObj.put("createdBy", inputTransactionStatusDTO.getCustomerId());
					additionalMetaObj.put("contractId", inputTransactionStatusDTO.getContractId());
					additionalMetaObj.put("coreCustomerId", inputTransactionStatusDTO.getCoreCustomerId());
					additionalMetaObj.put("accountIds", inputTransactionStatusDTO.getAccountIds());

					additionalMetaObj.put("limitTypeId", inputTransactionStatusDTO.getLimitTypeId());
					additionalMetaObj.put("limits", inputTransactionStatusDTO.getLimits());
					additionalMetaObj.put("approvalMatrixFeatureActionID",
							inputTransactionStatusDTO.getApprovalMatrixFeatureActionID());

					JSONObject newApprovalInfo = new JSONObject();
					newApprovalInfo.put("limitTypeId", inputTransactionStatusDTO.getLimitTypeId());
					newApprovalInfo.put("limits", inputTransactionStatusDTO.getLimits());
					newApprovalInfo.put("approvalMatrixFeatureActionID",
							inputTransactionStatusDTO.getApprovalMatrixFeatureActionID());
					additionalMetaObj.put("newInfo", newApprovalInfo.toString());

					// TO-DO
					List<ApprovalMatrixDTO> listApprovalMatrixDTO=approvalMatrixBusinessDelegate.fetchApprovalMatrix(inputTransactionStatusDTO.getContractId(), inputTransactionStatusDTO.getCoreCustomerId(), inputTransactionStatusDTO.getAccountIds(), inputTransactionStatusDTO.getApprovalMatrixFeatureActionID(), inputTransactionStatusDTO.getLimitTypeId());
					//ApprovalMatrixDTO approvalMatrixDTO = listApprovalMatrixDTO.get(0);
					
					JSONObject oldApprovalInfo = new JSONObject();
					oldApprovalInfo.put("limitTypeId", inputTransactionStatusDTO.getLimitTypeId());
					//oldApprovalInfo.put("limits", approvalMatrixDTO.getLimits());
					oldApprovalInfo.put("approvalMatrixFeatureActionID",inputTransactionStatusDTO.getApprovalMatrixFeatureActionID());
					additionalMetaObj.put("oldInfo", oldApprovalInfo.toString());

					bbRequestDTO.setAdditionalMeta(additionalMetaObj.toString());
				}
				if (isGroupLevel) {
					bbRequestDTO.setIsGroupMatrix("true");
				}
				String requestId = null;
				if (StringUtils.isEmpty(oldRequestId))
					requestId = approvalQueueBusinessDelegate.addTransactionToApprovalQueue(bbRequestDTO);
				else {
					bbRequestDTO.setRequestId(oldRequestId);
					requestId = approvalQueueBusinessDelegate.updateTransactionInApprovalQueue(bbRequestDTO);
				}
				/*try {
					if (applicationDTO.isSelfApprovalEnabled()) {
						if (combinedNewActionsIds.contains(actionId)) {
							String coreCusId = inputTransactionStatusDTO.getCustomerId();
							boolean isSelfApprovalPerformed = false;
							BBRequestDTO bbrequestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);
							Set<String> customerPermittedActionIds = CustomerSession.getPermittedActionIdsSet(request);
							String assocRequestIdsStr = bbrequestDTO.getAssocRequestId();
							if (assocRequestIdsStr == null) {
								throw new ApplicationException(ErrorCodeEnum.ERR_87402);
							}
							Set<String> assocRequestIds = new HashSet<>(Arrays.asList(assocRequestIdsStr.split(",")));
							if (assocRequestIds.size() > 1) {
								throw new ApplicationException(ErrorCodeEnum.ERR_87403);
							}
							Map<String, Map<String, Map<String, String>>> contractCifMap = approvalRequestBusinessDelegate
									.fetchContractCifMapForCustomer(coreCusId);
							List<RequestDTO> requests = approvalRequestBusinessDelegate
									.fetchRequestsWithApprovalMatrixInfo(assocRequestIds, true, contractCifMap, false);
							if (!requests.isEmpty()) {
								boolean isSelfApprovalEnabled = applicationBusinessDelegate
										.getIsSelfApprovalEnabledFromCache();
								if (isSelfApprovalEnabled) {
									try {
										requests = approvalRequestBusinessDelegate.approveCompositeRequests(coreCusId,
												customerPermittedActionIds, requests,
												Constants.DEFAULT_COMMENT_SELFAPPROVED);
										isSelfApprovalPerformed = true;
									} catch (ApplicationException ae) {
										isSelfApprovalPerformed = false;
									}
								}
							}
							transactionStatusDTO.setSelfApproved(isSelfApprovalPerformed);
						}
					}
				} catch (Exception e) {
					alert.prepareError(
							"Failed to auto self approve--in Validate for Approvals operation for " + actionId + e)
							.log();
				}*/

				result.addParam(new Param("isSelfApproved", String.valueOf(transactionStatusDTO.isSelfApproved())));
				result.addParam(new Param("requestId", requestId));
			}
		}
		return result;
	}

	@Override
	public Result updateBackendIdInApprovalQueue(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {

		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String requestId = inputParams.get("requestId") == null ? null : inputParams.get("requestId").toString();
		String transactionId = inputParams.get("transactionId") == null ? null
				: inputParams.get("transactionId").toString();
		String isSelfApproved = inputParams.get("isSelfApproved") == null ? null
				: inputParams.get("isSelfApproved").toString();
		String featureActionId = inputParams.get("featureActionId") == null ? null
				: inputParams.get("featureActionId").toString();

		if (StringUtils.isBlank(requestId) || StringUtils.isBlank(transactionId)) {
			alert.prepareError("Mandatory inputs are missing").log();
			return ErrorCodeEnum.ERR_10396.setErrorCode(result);
		}

		boolean isUpdateSuccessful = approvalQueueBusinessDelegate.updateBBRequestTransactionId(requestId,
				transactionId);
		if (!isUpdateSuccessful) {
			alert.prepareError("Failed to update transactionId").log();
			return ErrorCodeEnum.ERR_29014.setErrorCode(result);
		}

		if (StringUtils.isNotBlank(isSelfApproved) && "true".equalsIgnoreCase(isSelfApproved)) {
			if (StringUtils.isBlank(featureActionId)) {
				alert.prepareError("Mandatory inputs are missing").log();
				return ErrorCodeEnum.ERR_10396.setErrorCode(result);
			}
			return _selfApprove(requestId, transactionId, featureActionId, request);
		}

		result.addParam(new Param("status", TransactionStatusEnum.PENDING.toString()));
		result.addParam(new Param("requestId", requestId));
		result.addParam(new Param("confirmationNumber", transactionId));
		return result;
	}

	/**
	 * Method to self approve a transaction
	 * 
	 * @param inputParams
	 * @param response
	 * @return {@link Result} - Contains status and referenceId of the transaction
	 */
	private Result _selfApprove(String requestId, String transactionId, String featureActionId,
			DataControllerRequest request) {
		Result result = new Result();

		BBRequestDTO requestDTO = approvalQueueBusinessDelegate.approve(requestId, "SelfApproved", featureActionId,
				request);
		if (requestDTO == null) {
			alert.prepareError("Transaction approve has failed").log();
			return ErrorCodeEnum.ERR_12107.setErrorCode(result);
		}

		requestDTO = approvalQueueBusinessDelegate.getBbRequest(requestId);
		if (requestDTO == null) {
			alert.prepareError("Error while fetching BBRequest details").log();
			return ErrorCodeEnum.ERR_14008.setErrorCode(result);
		}

		TransactionStatusEnum transactionStatus = TransactionStatusEnum.PENDING;
		String status = requestDTO.getStatus();
		if (StringUtils.isNotBlank(status)) {
			if (TransactionStatusEnum.APPROVED.getStatus().equals(status)) {
				transactionStatus = TransactionStatusEnum.APPROVED;
			} else if (!TransactionStatusEnum.PENDING.getStatus().equals(status)) {
				alert.prepareError("Transaction execution has failed").log();
				return ErrorCodeEnum.ERR_12115.setErrorCode(result);
			}
		} else {
			alert.prepareError("Error while fetching the transfer details").log();
			return ErrorCodeEnum.ERR_29020.setErrorCode(result);
		}

		result.addParam(new Param("requestId", requestId));
		result.addParam(new Param("status", transactionStatus.toString()));
		result.addParam(new Param("confirmationNumber", transactionId));
		return result;
	}

	public Result renotifyPendingApprovalRequest(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Result result = new Result();

		String transactionId = inputParams.get("TransactionId") == null ? null
				: inputParams.get("TransactionId").toString();
		if (StringUtils.isBlank(transactionId)) {
			alert.prepareError("Transaction ID is missing").log();
			result.addParam(new Param("Status", "Failed"));
			return ErrorCodeEnum.ERR_29027.setErrorCode(result);
		}

		String customerName = inputParams.get("approverUserId") == null ? null
				: inputParams.get("approverUserId").toString();
		if (StringUtils.isBlank(customerName)) {
			alert.prepareError("Approver UserID is missing").log();
			result.addParam(new Param("Status", "Failed"));
			return ErrorCodeEnum.ERR_29029.setErrorCode(result);
		}

		String featureActionId = inputParams.get("featureActionId") == null ? null
				: inputParams.get("featureActionId").toString();
		if (StringUtils.isBlank(featureActionId)) {
			alert.prepareError("Feature Action ID is missing").log();
			result.addParam(new Param("Status", "Failed"));
			return ErrorCodeEnum.ERR_29028.setErrorCode(result);
		}

		String requestId = inputParams.get("requestId") == null ? null : inputParams.get("requestId").toString();
		FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(FeatureActionBusinessDelegate.class);
		FeatureBusinessDelegate featureBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(FeatureBusinessDelegate.class);
		FeatureActionDTO featureActionDTO = featureActionBusinessDelegate.getFeatureActionById(featureActionId);
		if (featureActionDTO == null || StringUtils.isBlank(featureActionDTO.getFeatureId())) {
			alert.prepareError("Feature Action is incorrect").log();
			result.addParam(new Param("Status", "Failed"));
			return ErrorCodeEnum.ERR_29030.setErrorCode(result);
		}

		FeatureDTO featureDTO = featureBusinessDelegate.fetchFeatureById(featureActionDTO.getFeatureId());
		if (featureDTO == null || StringUtils.isBlank(featureDTO.getFeatureName())) {
			alert.prepareError("Feature Action is incorrect").log();
			result.addParam(new Param("Status", "Failed"));
			return ErrorCodeEnum.ERR_29030.setErrorCode(result);
		}

		inputParams.put("type", featureDTO.getFeatureName());
		LogEvents.pushAlertsForApprovalRequests(Constants.RENOTIFY_PENDING_APPROVAL_REQUEST, request, response,
				inputParams, null, transactionId, requestId, customerName, null);
		result.addParam(new Param("Status", "Success"));
		return result;
	}
}