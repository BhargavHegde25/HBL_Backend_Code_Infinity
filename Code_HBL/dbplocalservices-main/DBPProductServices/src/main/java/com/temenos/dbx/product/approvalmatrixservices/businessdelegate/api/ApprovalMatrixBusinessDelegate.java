package com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixStatusDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.SignatoryGroupMatrixDTO;
import com.temenos.dbx.product.dto.CustomerActionDTO;

/**
 * 
 * @author KH2387
 * @version 1.0 Interface for ApprovalMatrixBusinessDelegate extends
 *          {@link BusinessDelegate}
 *
 */
public interface ApprovalMatrixBusinessDelegate extends BusinessDelegate {

	/**
	 * method to create default Approval Matrix record
	 * 
	 * @param String
	 *            contractId
	 * @param String
	 *            accountIds
	 * @param String
	 *            Array actionIds
	 * @param String 
	 * 			 cif
	 * @return Boolean
	 */
	Boolean createDefaultApprovalMatrixEntry(String contractId, String accountIds, String[] actionIds, String cif, String legalEntityId);

	/**
	 * method to delete Approval Matrix record
	 * 
	 * @param String contractId
	 * @param String Set <accountIds or cifs or actionIds>
	 * @param String filterColumnName
	 * 
	 * @return Boolean
	 */
	Boolean deleteApprovalMatrixEntry(String contractId, String cif, Set<String> filterColumnIds, String filterColumnName);
	
	/**
	 * method to delete Approval Matrix record
	 * 
	 * @param String contractId
	 * @param String Set cifs
	 * @param DataControllerRequest dcRequest
	 * @return Boolean
	 */
	Boolean deleteApprovalMatrixForCifs(String contractId, Set<String> cifs, DataControllerRequest dcRequest );
	
	/**
	 * method to delete Approval Matrix record
	 * 
	 * @param String contractId
	 * @param String cif
	 * @param String Set accountIds
	 * 
	 * @return Boolean
	 */
	Boolean deleteApprovalMatrixForAccounts(String contractId, String cif, Set<String> accounts);
	
	/**
	 * method to delete Approval Matrix record
	 * 
	 * @param String contractId
	 * @param String cif
	 * @param String Set actionIds
	 * 
	 * @return Boolean
	 */
	Boolean deleteApprovalMatrixFeatureForActions(String contractId, String cif, Set<String> actions);

	/**
	 * method to delete Approval Matrix record
	 * 
	 * @param String contractId
	 * @param String cif
	 * @param String Set featureIds
	 * 
	 * @return Boolean
	 */
	Boolean deleteApprovalMatrixFeatureSet(String contractId, String cif, Set<String> features);
	
	/**
	 * method to update Approval Matrix record
	 *
	 * @param List          of {{@link ApprovalMatrixDTO}
	 * @param List          accountIds
	 * @param legalEntityId
	 * @return JSONObject
	 */
	JSONObject updateApprovalMatrixEntry(List<ApprovalMatrixDTO> approvalMatrixDTOList, List<String> accountIds, String legalEntityId);
	
	/**
	 * method to update Approval Matrix record
	 *
	 * @param List          of {{@link ApprovalMatrixDTO}
	 * @param List          accountIds
	 * @param legalEntityId
	 * @return JSONObject
	 */
	
	JSONObject updateApprovalSignatoryMatrixEntry(List<ApprovalMatrixDTO> approvalMatrixDTOList, List<String> accountIds, String legalEntityId);
	
	
	/**
	 * method to fetch ApprovalMatrix records
	 * @param contractId
	 * @param cif
	 * @param accountId
	 * @param actionId
	 * @param limitTypeId
	 * @return List<ApprovalMatrixDTO>
	 */
	List<ApprovalMatrixDTO> fetchApprovalMatrix(String contractId, String cif,String accountId,String actionId,String limitTypeId);
	
	/**
	 * method to fetch ApprovalMatrixRequest records
	 * @param contractId
	 * @param cif
	 * @param accountId
	 * @param limitTypeId
	 * @return List<ApprovalMatrixDTO>
	 */
	List<ApprovalMatrixDTO> fetchApprovalMatrixTemplate(String contractId, String cif,String actionId,String limitTypeId);
	
	
	/**
	 * method to fetch ApprovalMatrix records
	 * @param contractId
	 * @param cif
	 * @param accountId
	 * @param actionId
	 * @param limitTypeId
	 * @return List<ApprovalMatrixDTO>
	 */
	List<ApprovalMatrixDTO> fetchApprovalMatrixSignatory(String contractId, String cif,String accountId,String actionId,String limitTypeId);
	
	
	/**
	 * Fetches approval matrix entry from approvalmatrix table
	 * @param companyId
	 * @param accountId
	 * @param actionId
	 * @return {@link List<ApprovalMatrixDTO>}
	 */
	public List<ApprovalMatrixDTO> fetchApprovalMatrixEntry(String companyId,String accountId,String actionId);
	
	/**
	 * updates isDisabled flag to true in manageapprovalmatrix table
	 * @param companyId
	 * @param accountId
	 * @param actionId
	 * @return boolean - true if sucessful else false
	 */
	public boolean disableApprovalMatrix(String contractId, List<String> cifList);
	
	/**
	 * updates isDisabled flag to true in manageapprovalmatrix table
	 * @param companyId
	 * @param accountId
	 * @param actionId
	 * @return {@link List<String>}
	 */
	public List<String> getCifList(String contractId);
	
	/**
	 * updates isDisabled flag to false in manageapprovalmatrix table
	 * @param companyId
	 * @param accountId
	 * @param actionId
	 * @return boolean - true if sucessful else false
	 */
	public boolean enableApprovalMatrix(String contractId, List<String> cifList);
	
	/**
	 * checks if approval matrix is disabled for the given contractId and cif
	 * @param companyId
	 * @param accountId
	 * @param actionId
	 * @return {@link List<ApprovalMatrixStatusDTO>}
	 */
	public List<ApprovalMatrixStatusDTO> fetchApprovalMatrixStatus(String contractId, List<String> cifList);
	
	/**
	 * Fetches list of customerIds from customerApprovalMatrix table
	 * @param matrixId
	 * @return {@link List<String>}
	 */
	public List<String> fetchApproverIds(String matrixId);
	
	/**
	 * Fetches list of customerIds from customerApprovalMatrixTemplate table
	 * @param matrixId
	 * @param request 
	 * @return {@link List<String>}
	 */
	public List<String> fetchApproverIdsFromTemplate(String matrixId, DataControllerRequest request);

	/**
	 * Used to call CreateApprovalMatrix StoredProc
	 * @param approvalmatrixValues
	 * @param approvalIds
	 * @return Boolean
	 */
	Boolean callCreateApprovalMatrixStoredProc(String approvalmatrixValues, String approvalIds);

	Boolean callCreateSignatoryApprovalMatrixStoredProc(String approvalmatrixValues, String approvalIds);
	
	SignatoryGroupMatrixDTO fetchSignatoryGroupMatrix(String matrixId);
	
	SignatoryGroupMatrixDTO fetchSignatoryGroupMatrixFromTemplate(String matrixId);

	List<String> fetchUserOfGroupList(String groupList);
	
	/**
	 * method to call ApprovalMatrixTemplateCleanup StoredProc
	 * @param String contractId
	 * @param String Array actionIds
	 * @param String cif
	 * @param String limitTypeIds
	 * @return Boolean
	 */
	public Boolean callApprovalMatrixTemplateCleanupProc(String contractId, String[] actionIds, String cif, String limitTypeIds);
	
	/**
	 * method to call createNoApprovalTemplateEntry StoredProc
	 * @param String contractId
	 * @param String Array actionIds
	 * @param String cif
	 * @param int approvalMode
	 * @return Boolean
	 */
	public Boolean createApprovalMatrixTemplateDefaultEntry(String contractId, String[] actionIds, String cif, int approvalMode, String legalEntityId);
	
		
	public JSONObject updateApprovalSignatoryMatrixTemplateEntry(List<ApprovalMatrixDTO> approvalMatrixTemplateList, int approvalMode);
	
	public JSONObject updateApprovalMatrixTemplateEntry(List<ApprovalMatrixDTO> approvalMatrixTemplateList, List<String> accountIds, int approvalMode);
	
	public Boolean callCreateApprovalMatrixTemplateStoredProc(String approvalmatrixTemplateValues, String approvalIds, int approvalMode);
	
	public List<ApprovalMatrixDTO> fetchApprovalMatrixTemplateRecords(String cifId) ;
	
	public Set<String> getActionWithApproveFeatureAction(Set<String> actionsSet, Map<String, Object> headersMap);

	public ApprovalMatrixDTO fetchApprovalMatrixById(String approvalMatrixId);
	
	public Map<String, LimitsDTO> fetchLimits(String contractId, String coreCustomerId, List<String> actionIds);
	
	public List<ApprovalMatrixDTO> fetchAccountDetails(String contractId, String cif, String accountIds);
	
	public List<CustomerActionDTO> fetchCustomerActionsByUserID(String cifId, String contractId, String userId);

	public List<CustomerActionDTO> fetchCustomerActionsByLoggedUserID(String userId);

	public Map<String, JSONObject> fetchFeatureActiondetails(Set<String> customerActionsSet);

	public Map<String, JSONObject> fetchFeatureActionsEligibleForApproval();

	public List<CustomerActionDTO> fetchCustomerActions(String cifId, String contractId, Set<String> setEligibleForApprovals);

	@Deprecated
	public String fetchUserApprovalCurrency(String cifId, String contractId, String accountId);
}
