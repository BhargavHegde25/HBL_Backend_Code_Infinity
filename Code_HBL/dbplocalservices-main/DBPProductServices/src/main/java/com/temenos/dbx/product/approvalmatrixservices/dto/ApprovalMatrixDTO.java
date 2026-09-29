package com.temenos.dbx.product.approvalmatrixservices.dto;

import java.util.List;


import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author KH2387
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalMatrixDTO implements DBPDTO{

	private static final long serialVersionUID = 9174176582838690331L;
	
	private String id;
	private String companyId;
	@JsonAlias({"Account_id"})
	private String accountId;
	@JsonAlias({"AccountName"})
	private String accountName;
	private String limitTypeId;
	private String actionId;
	private String actionName;
	private String actionDescription;
	private String actionType;
	private String featureId;
	private String featureName;
	private String fifeaturestatus = "";
	private String orgfeaturestatus = "";
	private String approvalruleId;
	private int numberOfApprovals;
	private String approvalRuleName;
	private String customerId;
	private String firstName="";
	private String lastName="";
	private String lowerlimit;
	private String upperlimit;
	private String cifId;
	private String cifName;
	private String contractId;
	private boolean invalid;
	private String isGroupMatrix;
	private String groupRule;
	private String groupList;
	private String maxAmount;
	private String accountType;
	@JsonAlias({"ownerType"})
	private String ownershipType;
	private String isAccountLevel;
	private String currency;
	
	private List<CustomerApprovalMatrixDTO> customerApprovalMatrixDTO;
	private SignatoryGroupMatrixDTO signatoryGroupMatrixDTO;
	public ApprovalMatrixDTO(){		
	}
	public ApprovalMatrixDTO(String id, String companyId,String accountId,String accountName,String limitTypeId,String actionId,String actionName,String actionDescription,String featureName,String featureId,String fifeaturestatus,String orgfeaturestatus,String approvalRuleId,int numberOfApprovals,String approvalRuleName, String customerId, String firstName,String lastName,String lowerlimit,String upperlimit,String currency, String cifId, String cifName, String contractId, boolean invalid, String actionType, String isGroupMatrix, String groupList, String groupRule, String maxAmount,
	String accountType, String ownershipType,String isAccountLevel)
	{
		this.id = id;
		this.companyId = companyId;
		this.accountId = accountId;
		this.accountName = accountName; 
		this.limitTypeId = limitTypeId;
		this.actionId = actionId;
		this.actionName = actionName;
		this.actionDescription = actionDescription;
		this.featureId = featureId;
		this.featureName = featureName;
		this.fifeaturestatus = fifeaturestatus;
		this.orgfeaturestatus = orgfeaturestatus;
		this.approvalruleId = approvalRuleId;
		this.numberOfApprovals = numberOfApprovals;
		this.approvalRuleName = approvalRuleName;
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.lowerlimit = lowerlimit;
		this.upperlimit = upperlimit;
		this.currency = currency;
		this.cifId = cifId;
		this.cifName = cifName;
		this.contractId = contractId;
		this.invalid = invalid;
		this.actionType = actionType;
		this.isGroupMatrix=isGroupMatrix;
		this.groupList = groupList;
		this.groupRule = groupRule;
		this. maxAmount = maxAmount;
		this.accountType =  accountType;
		this.ownershipType = ownershipType;
		this.isAccountLevel = isAccountLevel;
	}
	
	ApprovalMatrixDTO(String companyId,String accountId,String limitTypeId,String actionId,String actionName,String actionDescription,String approvalRuleId,int numberOfApprovals,String approvalRuleName,String lowerlimit,String upperlimit,String currency, boolean invalid, List<CustomerApprovalMatrixDTO> customerApprovalMatrixDTO, SignatoryGroupMatrixDTO signatoryGroupMatrixDTO, String isGroupMatrix)
	{
		this.companyId = companyId;
		this.accountId = accountId;
		this.limitTypeId = limitTypeId;
		this.actionId = actionId;
		this.actionName = actionName;
		this.actionDescription = actionDescription;
		this.approvalruleId = approvalRuleId;
		this.numberOfApprovals = numberOfApprovals;
		this.approvalRuleName = approvalRuleName;
		this.lowerlimit = lowerlimit;
		this.upperlimit = upperlimit;
		this.currency = currency;
		this.invalid = invalid;
		this.customerApprovalMatrixDTO = customerApprovalMatrixDTO;		
		this.signatoryGroupMatrixDTO = signatoryGroupMatrixDTO;
		this.isGroupMatrix=isGroupMatrix;
	}
	public String getGroupRule() {
		return groupRule;
	}
	public void setGroupRule(String groupRule) {
		this.groupRule = groupRule;
	}
	public String getGroupList() {
		return groupList;
	}
	public void setGroupList(String groupList) {
		this.groupList = groupList;
	}
	public SignatoryGroupMatrixDTO getSignatoryGroupMatrixDTO() {
		return signatoryGroupMatrixDTO;
	}
	public void setSignatoryGroupMatrixDTO(SignatoryGroupMatrixDTO signatoryGroupMatrixDTO) {
		this.signatoryGroupMatrixDTO = signatoryGroupMatrixDTO;
	}
	public String getAccountId() {
		return accountId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getLimitTypeId() {
		return limitTypeId;
	}
	public void setLimitTypeId(String limitTypeId) {
		this.limitTypeId = limitTypeId;
	}
	public String getLowerlimit() {
		return lowerlimit;
	}
	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public String getApprovalruleId() {
		return approvalruleId;
	}

	public void setApprovalruleId(String approvalRuleId) {
		this.approvalruleId = approvalRuleId;
	}

	public int getNumberOfApprovals() {
		return numberOfApprovals;
	}

	public void setNumberOfApprovals(int numberOfApprovals) {
		this.numberOfApprovals = numberOfApprovals;
	}

	public String getApprovalRuleName() {
		return approvalRuleName;
	}

	public void setApprovalRuleName(String approvalRuleName) {
		this.approvalRuleName = approvalRuleName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setLowerlimit(String lowerlimit) {
		this.lowerlimit = lowerlimit;
	}
	public String getUpperlimit() {
		return upperlimit;
	}
	public void setUpperlimit(String upperlimit) {
		this.upperlimit = upperlimit;
	}
	public List<CustomerApprovalMatrixDTO> getCustomerApprovalMatrixDTO() {
		return customerApprovalMatrixDTO;
	}
	public void setCustomerApprovalMatrixDTO(List<CustomerApprovalMatrixDTO> customerApprovalMatrixDTO) {
		this.customerApprovalMatrixDTO = customerApprovalMatrixDTO;
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFifeaturestatus() {
		return fifeaturestatus;
	}
	public void setFifeaturestatus(String fifeaturestatus) {
		this.fifeaturestatus = fifeaturestatus;
	}
	public String getOrgfeaturestatus() {
		return orgfeaturestatus;
	}
	public void setOrgfeaturestatus(String orgfeaturestatus) {
		this.orgfeaturestatus = orgfeaturestatus;
	}	
	public String getCifId() {
		return cifId;
	}
	public void setCifId(String cifId) {
		this.cifId = cifId;
	}
	public String getCifName() {
		return cifName;
	}
	public void setCifName(String cifName) {
		this.cifName = cifName;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public boolean getInvalid() {
		return invalid;
	}
	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getIsGroupMatrix() {
		return isGroupMatrix;
	}
	public void setIsGroupMatrix(String isGroupMatrix) {
		this.isGroupMatrix = isGroupMatrix;
	}
	public String getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getOwnershipType() {
		return ownershipType;
	}
	public void setOwnershipType(String ownershipType) {
		this.ownershipType = ownershipType;
	}
	public String getIsAccountLevel() {
		return isAccountLevel;
	}
	public void setIsAccountLevel(String isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}
	public String getCurrency(){
		return this.currency;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 *  method to convert the list<ApprovalMatrixDTO> to ApprovalMatrixOutputDTO 
	 *  @param ({@link List<ApprovalMatrixDTO>})
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
  	public  ApprovalMatrixOutputDTO convertToApprovalMatrixOutputDTO(List<ApprovalMatrixDTO> approvalMatrixListDTO )
	{
		ApprovalMatrixOutputDTO approvalMatrixOutputDTO = new ApprovalMatrixOutputDTO();
		ApprovalMatrixDTO approvalMatrixDTO = approvalMatrixListDTO.get(0);
		String currCif = approvalMatrixDTO.getCifId();
		String currAccountID = approvalMatrixDTO.getAccountId();
		String currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
		String currActionID = approvalMatrixDTO.getActionId();
		String currLowerLimit = approvalMatrixDTO.getLowerlimit();
		String currUpperLimit = approvalMatrixDTO.getUpperlimit();
		approvalMatrixOutputDTO.setCompanyId(approvalMatrixDTO.getCompanyId());
		approvalMatrixOutputDTO.setContractId(approvalMatrixDTO.getContractId());
		ApprovalMatrixCifDTO cif = new ApprovalMatrixCifDTO();
		cif.setCifId(approvalMatrixDTO.getCifId());
		cif.setCifName(approvalMatrixDTO.getCifName());
		
		ApprovalMatrixAccountDTO account = new ApprovalMatrixAccountDTO();
		account.setAccountId(approvalMatrixDTO.getAccountId());
		account.setAccountName(approvalMatrixDTO.getAccountName());
		account.setAccountType(approvalMatrixDTO.getAccountType());
		account.setOwnershipType(approvalMatrixDTO.getOwnershipType());
		ApprovalMatrixLimitTypeDTO limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
		limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
		ApprovalMatrixActionDTO action = new ApprovalMatrixActionDTO();
		action.setActionId(approvalMatrixDTO.getActionId());
		action.setActionName(approvalMatrixDTO.getActionName());
		action.setActionDescription(approvalMatrixDTO.getActionDescription());
		action.setFeatureId(approvalMatrixDTO.getFeatureId());
		action.setFeatureName(approvalMatrixDTO.getFeatureName());
		action.setActionType(approvalMatrixDTO.getActionType());
		action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
		action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
		String featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
		action.setFeatureStatus(featureStatus);
		ApprovalMatrixLimitDTO limit = new ApprovalMatrixLimitDTO();
		limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
		limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
		limit.setCurrency(approvalMatrixDTO.getCurrency());
		limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
		limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
		limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
		limit.setInvalid(approvalMatrixDTO.getInvalid());
		ApprovalMatrixApproverDTO approver = new ApprovalMatrixApproverDTO();
		approver.setApproverId(approvalMatrixDTO.getCustomerId());
		String fullName= new String("");
		if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
			fullName="";
		}
		else {
			fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
		}
		approver.setApproverName(fullName);
		limit.add(approver);
		action.add(limit);
		limitTypeID.add(action);
		account.add(limitTypeID);
		cif.add(account);
		approvalMatrixOutputDTO.add(cif);
		
		int cifCount = 0;
		int accountCount = 0;
		int limitTypeCount = 0;
		int actionCount = 0;
		int limitCount = 0;
		int i,size = approvalMatrixListDTO.size();
		for (i=1;i<size;i++)
		{
			approvalMatrixDTO = approvalMatrixListDTO.get(i);
			if(currCif.equals(approvalMatrixDTO.getCifId())){
				if (currAccountID.equals(approvalMatrixDTO.getAccountId()))
				{
					if (currLimitTypeID.equals(approvalMatrixDTO.getLimitTypeId()))
					{
						if (currActionID.equals(approvalMatrixDTO.getActionId()))
						{
							if (currLowerLimit.equals(approvalMatrixDTO.getLowerlimit()) && currUpperLimit.equals(approvalMatrixDTO.getUpperlimit())) 
							{
								approver = new ApprovalMatrixApproverDTO();
								approver.setApproverId(approvalMatrixDTO.getCustomerId());
								if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
									fullName="";
								}
								else {
									fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
								}
								approver.setApproverName(fullName);
								approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).getLimitTypes().get(limitTypeCount).getActions().get(actionCount).getLimits().get(limitCount).add(approver);
							}
							else
							{
								
								limit = new ApprovalMatrixLimitDTO();
								limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
								limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
								limit.setCurrency(approvalMatrixDTO.getCurrency());
								limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
								limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
								limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
								limit.setInvalid(approvalMatrixDTO.getInvalid());
								approver = new ApprovalMatrixApproverDTO();
								approver.setApproverId(approvalMatrixDTO.getCustomerId());
								if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
									fullName="";
								}
								else {
									fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
								}
								approver.setApproverName(fullName);
								limit.add(approver);
								approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).getLimitTypes().get(limitTypeCount).getActions().get(actionCount).add(limit);

								currLowerLimit = approvalMatrixDTO.getLowerlimit();
								currUpperLimit = approvalMatrixDTO.getUpperlimit();	
								limitCount++;
						
							}
						}
						else
						{
							action = new ApprovalMatrixActionDTO();
							action.setActionId(approvalMatrixDTO.getActionId());
							action.setActionName(approvalMatrixDTO.getActionName());
							action.setActionDescription(approvalMatrixDTO.getActionDescription());
							action.setFeatureId(approvalMatrixDTO.getFeatureId());
							action.setFeatureName(approvalMatrixDTO.getFeatureName());
							action.setActionType(approvalMatrixDTO.getActionType());
							action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
							action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
							featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
							action.setFeatureStatus(featureStatus);
							limit = new ApprovalMatrixLimitDTO();
							limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
							limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
							limit.setCurrency(approvalMatrixDTO.getCurrency());
							limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
							limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
							limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
							limit.setInvalid(approvalMatrixDTO.getInvalid());
							approver = new ApprovalMatrixApproverDTO();
							approver.setApproverId(approvalMatrixDTO.getCustomerId());
							if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
								fullName="";
							}
							else {
								fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
							}
							approver.setApproverName(fullName);
							limit.add(approver);
							action.add(limit);
							approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).getLimitTypes().get(limitTypeCount).add(action);
							currLowerLimit = approvalMatrixDTO.getLowerlimit();
							currUpperLimit = approvalMatrixDTO.getUpperlimit();
							currActionID = approvalMatrixDTO.getActionId();
							limitCount=0;
							actionCount++;
						}
					}
					else
					{
						limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
						limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
						action = new ApprovalMatrixActionDTO();
						action.setActionId(approvalMatrixDTO.getActionId());
						action.setActionName(approvalMatrixDTO.getActionName());
						action.setActionDescription(approvalMatrixDTO.getActionDescription());
						action.setFeatureId(approvalMatrixDTO.getFeatureId());
						action.setFeatureName(approvalMatrixDTO.getFeatureName());
						action.setActionType(approvalMatrixDTO.getActionType());
						action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
						action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
						featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
						action.setFeatureStatus(featureStatus);
						limit = new ApprovalMatrixLimitDTO();
						limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
						limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
						limit.setCurrency(approvalMatrixDTO.getCurrency());
						limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
						limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
						limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
						limit.setInvalid(approvalMatrixDTO.getInvalid());
						approver = new ApprovalMatrixApproverDTO();
						approver.setApproverId(approvalMatrixDTO.getCustomerId());
						if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
							fullName="";
						}
						else {
							fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
						}
						approver.setApproverName(fullName);
						limit.add(approver);
						action.add(limit);
						limitTypeID.add(action);
						approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).add(limitTypeID);
						currLowerLimit = approvalMatrixDTO.getLowerlimit();
						currUpperLimit = approvalMatrixDTO.getUpperlimit();
						currActionID = approvalMatrixDTO.getActionId();
						currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
						limitTypeCount++;
						actionCount=0;
						limitCount=0;
					}
				}
				else
				{
					account = new ApprovalMatrixAccountDTO();
					account.setAccountId(approvalMatrixDTO.getAccountId());
					account.setAccountName(approvalMatrixDTO.getAccountName());
					account.setAccountType(approvalMatrixDTO.getAccountType());
					account.setOwnershipType(approvalMatrixDTO.getOwnershipType());
					limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
					limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
					action = new ApprovalMatrixActionDTO();
					action.setActionId(approvalMatrixDTO.getActionId());
					action.setActionName(approvalMatrixDTO.getActionName());
					action.setActionDescription(approvalMatrixDTO.getActionDescription());
					action.setFeatureId(approvalMatrixDTO.getFeatureId());
					action.setFeatureName(approvalMatrixDTO.getFeatureName());
					action.setActionType(approvalMatrixDTO.getActionType());
					action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
					action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
					featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
					action.setFeatureStatus(featureStatus);
					limit = new ApprovalMatrixLimitDTO();
					limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
					limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
					limit.setCurrency(approvalMatrixDTO.getCurrency());
					limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
					limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
					limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
					limit.setInvalid(approvalMatrixDTO.getInvalid());
					approver = new ApprovalMatrixApproverDTO();
					approver.setApproverId(approvalMatrixDTO.getCustomerId());
					if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
						fullName="";
					}
					else {
						fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
					}
					approver.setApproverName(fullName);
					limit.add(approver);
					action.add(limit);
					limitTypeID.add(action);
					account.add(limitTypeID);
					approvalMatrixOutputDTO.getCifs().get(cifCount).add(account);
					currLowerLimit = approvalMatrixDTO.getLowerlimit();
					currUpperLimit = approvalMatrixDTO.getUpperlimit();
					currActionID = approvalMatrixDTO.getActionId();
					currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
					currAccountID = approvalMatrixDTO.getAccountId();
					accountCount++;
					limitTypeCount=0;
					actionCount=0;
					limitCount=0;
					
				}				
			}
			else {
				cif = new ApprovalMatrixCifDTO();
				cif.setCifId(approvalMatrixDTO.getCifId());
				cif.setCifName(approvalMatrixDTO.getCifName());			
				account = new ApprovalMatrixAccountDTO();
				account.setAccountId(approvalMatrixDTO.getAccountId());
				account.setAccountName(approvalMatrixDTO.getAccountName());
				account.setAccountType(approvalMatrixDTO.getAccountType());
				account.setOwnershipType(approvalMatrixDTO.getOwnershipType());
				limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
				limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
				action = new ApprovalMatrixActionDTO();
				action.setActionId(approvalMatrixDTO.getActionId());
				action.setActionName(approvalMatrixDTO.getActionName());
				action.setActionDescription(approvalMatrixDTO.getActionDescription());
				action.setFeatureId(approvalMatrixDTO.getFeatureId());
				action.setFeatureName(approvalMatrixDTO.getFeatureName());
				action.setActionType(approvalMatrixDTO.getActionType());
				action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
				action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
				featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
				action.setFeatureStatus(featureStatus);
				limit = new ApprovalMatrixLimitDTO();
				limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
				limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
				limit.setCurrency(approvalMatrixDTO.getCurrency());
				limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
				limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
				limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
				limit.setInvalid(approvalMatrixDTO.getInvalid());
				approver = new ApprovalMatrixApproverDTO();
				approver.setApproverId(approvalMatrixDTO.getCustomerId());
				if(approvalMatrixDTO.getFirstName().isEmpty() && approvalMatrixDTO.getLastName().isEmpty()) {
					fullName="";
				}
				else {
					fullName=approvalMatrixDTO.getFirstName().concat(" "+approvalMatrixDTO.getLastName());
				}
				approver.setApproverName(fullName);
				limit.add(approver);
				action.add(limit);
				limitTypeID.add(action);
				account.add(limitTypeID);
				cif.add(account);
				approvalMatrixOutputDTO.add(cif);
				currLowerLimit = approvalMatrixDTO.getLowerlimit();
				currUpperLimit = approvalMatrixDTO.getUpperlimit();
				currActionID = approvalMatrixDTO.getActionId();
				currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
				currAccountID = approvalMatrixDTO.getAccountId();
				currCif = approvalMatrixDTO.getCifId();
				cifCount++;
				accountCount=0;
				limitTypeCount=0;
				actionCount=0;
				limitCount=0;				
			}

		}	
		return approvalMatrixOutputDTO;
	}
  	/**
	 *  method to convert the list<ApprovalMatrixDTO> to ApprovalMatrixOutputDTO 
	 *  @param ({@link List<ApprovalMatrixDTO>})
	 *  @return JSON object containing all approval matrix records
	 *  
	 */
  	public  ApprovalMatrixOutputDTO convertToApprovalMatrixSignatoryOutputDTO(List<ApprovalMatrixDTO> approvalMatrixListDTO )
	{
		ApprovalMatrixOutputDTO approvalMatrixOutputDTO = new ApprovalMatrixOutputDTO();
		ApprovalMatrixDTO approvalMatrixDTO = approvalMatrixListDTO.get(0);
		String currCif = approvalMatrixDTO.getCifId();
		String currAccountID = approvalMatrixDTO.getAccountId();
		String currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
		String currActionID = approvalMatrixDTO.getActionId();
		String currLowerLimit = approvalMatrixDTO.getLowerlimit();
		String currUpperLimit = approvalMatrixDTO.getUpperlimit();
		approvalMatrixOutputDTO.setCompanyId(approvalMatrixDTO.getCompanyId());
		approvalMatrixOutputDTO.setContractId(approvalMatrixDTO.getContractId());
		ApprovalMatrixCifDTO cif = new ApprovalMatrixCifDTO();
		cif.setCifId(approvalMatrixDTO.getCifId());
		cif.setCifName(approvalMatrixDTO.getCifName());
		
		ApprovalMatrixAccountDTO account = new ApprovalMatrixAccountDTO();
		account.setAccountId(approvalMatrixDTO.getAccountId());
		account.setAccountName(approvalMatrixDTO.getAccountName());
		account.setAccountType(approvalMatrixDTO.getAccountType());
		account.setOwnershipType(approvalMatrixDTO.getOwnershipType());
		ApprovalMatrixLimitTypeDTO limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
		limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
		ApprovalMatrixActionDTO action = new ApprovalMatrixActionDTO();
		action.setActionId(approvalMatrixDTO.getActionId());
		action.setActionName(approvalMatrixDTO.getActionName());
		action.setActionDescription(approvalMatrixDTO.getActionDescription());
		action.setFeatureId(approvalMatrixDTO.getFeatureId());
		action.setFeatureName(approvalMatrixDTO.getFeatureName());
		action.setActionType(approvalMatrixDTO.getActionType());
		action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
		action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
		String featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
		action.setFeatureStatus(featureStatus);
		ApprovalMatrixLimitDTO limit = new ApprovalMatrixLimitDTO();
		limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
		limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
		limit.setCurrency(approvalMatrixDTO.getCurrency());
		limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
		limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
		limit.setInvalid(approvalMatrixDTO.getInvalid());
		limit.setGroupList(approvalMatrixDTO.getGroupList());
		limit.setGroupRule(approvalMatrixDTO.getGroupRule());
		action.add(limit);
		limitTypeID.add(action);
		account.add(limitTypeID);
		cif.add(account);
		approvalMatrixOutputDTO.add(cif);
		
		int cifCount = 0;
		int accountCount = 0;
		int limitTypeCount = 0;
		int actionCount = 0;
		int limitCount = 0;
		int i,size = approvalMatrixListDTO.size();
		for (i=1;i<size;i++)
		{
			approvalMatrixDTO = approvalMatrixListDTO.get(i);
			if(currCif.equals(approvalMatrixDTO.getCifId())){
				if (currAccountID.equals(approvalMatrixDTO.getAccountId()))
				{
					if (currLimitTypeID.equals(approvalMatrixDTO.getLimitTypeId()))
					{
						if (currActionID.equals(approvalMatrixDTO.getActionId()))
						{
							if (currLowerLimit.equals(approvalMatrixDTO.getLowerlimit()) && currUpperLimit.equals(approvalMatrixDTO.getUpperlimit())) 
							{
								approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).getLimitTypes().get(limitTypeCount).getActions().get(actionCount).getLimits().get(limitCount);
							}
							else
							{
								
								limit = new ApprovalMatrixLimitDTO();
								limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
								limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
								limit.setCurrency(approvalMatrixDTO.getCurrency());
								limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
								limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
								limit.setInvalid(approvalMatrixDTO.getInvalid());
								limit.setGroupList(approvalMatrixDTO.getGroupList());
								limit.setGroupRule(approvalMatrixDTO.getGroupRule());
								approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).getLimitTypes().get(limitTypeCount).getActions().get(actionCount).add(limit);
								currLowerLimit = approvalMatrixDTO.getLowerlimit();
								currUpperLimit = approvalMatrixDTO.getUpperlimit();	
								limitCount++;
						
							}
						}
						else
						{
							action = new ApprovalMatrixActionDTO();
							action.setActionId(approvalMatrixDTO.getActionId());
							action.setActionName(approvalMatrixDTO.getActionName());
							action.setActionDescription(approvalMatrixDTO.getActionDescription());
							action.setFeatureId(approvalMatrixDTO.getFeatureId());
							action.setFeatureName(approvalMatrixDTO.getFeatureName());
							action.setActionType(approvalMatrixDTO.getActionType());
							action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
							action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
							featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
							action.setFeatureStatus(featureStatus);
							limit = new ApprovalMatrixLimitDTO();
							limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
							limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
							limit.setCurrency(approvalMatrixDTO.getCurrency());
							limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
							limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
							limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
							limit.setInvalid(approvalMatrixDTO.getInvalid());
							limit.setGroupList(approvalMatrixDTO.getGroupList());
							limit.setGroupRule(approvalMatrixDTO.getGroupRule());
							action.add(limit);
							approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).getLimitTypes().get(limitTypeCount).add(action);
							currLowerLimit = approvalMatrixDTO.getLowerlimit();
							currUpperLimit = approvalMatrixDTO.getUpperlimit();
							currActionID = approvalMatrixDTO.getActionId();
							limitCount=0;
							actionCount++;
						}
					}
					else
					{
						limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
						limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
						action = new ApprovalMatrixActionDTO();
						action.setActionId(approvalMatrixDTO.getActionId());
						action.setActionName(approvalMatrixDTO.getActionName());
						action.setActionDescription(approvalMatrixDTO.getActionDescription());
						action.setFeatureId(approvalMatrixDTO.getFeatureId());
						action.setFeatureName(approvalMatrixDTO.getFeatureName());
						action.setActionType(approvalMatrixDTO.getActionType());
						action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
						action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
						featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
						action.setFeatureStatus(featureStatus);
						limit = new ApprovalMatrixLimitDTO();
						limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
						limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
						limit.setCurrency(approvalMatrixDTO.getCurrency());
						limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
						limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
						limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
						limit.setInvalid(approvalMatrixDTO.getInvalid());
						limit.setGroupList(approvalMatrixDTO.getGroupList());
						limit.setGroupRule(approvalMatrixDTO.getGroupRule());
						action.add(limit);
						limitTypeID.add(action);
						approvalMatrixOutputDTO.getCifs().get(cifCount).getAccounts().get(accountCount).add(limitTypeID);
						currLowerLimit = approvalMatrixDTO.getLowerlimit();
						currUpperLimit = approvalMatrixDTO.getUpperlimit();
						currActionID = approvalMatrixDTO.getActionId();
						currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
						limitTypeCount++;
						actionCount=0;
						limitCount=0;
					}
				}
				else
				{
					account = new ApprovalMatrixAccountDTO();
					account.setAccountId(approvalMatrixDTO.getAccountId());
					account.setAccountName(approvalMatrixDTO.getAccountName());
					account.setAccountType(approvalMatrixDTO.getAccountType());
					account.setOwnershipType(approvalMatrixDTO.getOwnershipType());
					limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
					limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
					action = new ApprovalMatrixActionDTO();
					action.setActionId(approvalMatrixDTO.getActionId());
					action.setActionName(approvalMatrixDTO.getActionName());
					action.setActionDescription(approvalMatrixDTO.getActionDescription());
					action.setFeatureId(approvalMatrixDTO.getFeatureId());
					action.setFeatureName(approvalMatrixDTO.getFeatureName());
					action.setActionType(approvalMatrixDTO.getActionType());
					action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
					action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
					featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
					action.setFeatureStatus(featureStatus);
					limit = new ApprovalMatrixLimitDTO();
					limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
					limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
					limit.setCurrency(approvalMatrixDTO.getCurrency());
					limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
					limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
					limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
					limit.setInvalid(approvalMatrixDTO.getInvalid());
					limit.setGroupList(approvalMatrixDTO.getGroupList());
					limit.setGroupRule(approvalMatrixDTO.getGroupRule());
					action.add(limit);
					limitTypeID.add(action);
					account.add(limitTypeID);
					approvalMatrixOutputDTO.getCifs().get(cifCount).add(account);
					currLowerLimit = approvalMatrixDTO.getLowerlimit();
					currUpperLimit = approvalMatrixDTO.getUpperlimit();
					currActionID = approvalMatrixDTO.getActionId();
					currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
					currAccountID = approvalMatrixDTO.getAccountId();
					accountCount++;
					limitTypeCount=0;
					actionCount=0;
					limitCount=0;
					
				}				
			}
			else {
				cif = new ApprovalMatrixCifDTO();
				cif.setCifId(approvalMatrixDTO.getCifId());
				cif.setCifName(approvalMatrixDTO.getCifName());			
				account = new ApprovalMatrixAccountDTO();
				account.setAccountId(approvalMatrixDTO.getAccountId());
				account.setAccountName(approvalMatrixDTO.getAccountName());
				account.setAccountType(approvalMatrixDTO.getAccountType());
				account.setOwnershipType(approvalMatrixDTO.getOwnershipType());
				limitTypeID = new ApprovalMatrixLimitTypeDTO() ;
				limitTypeID.setLimitTypeId(approvalMatrixDTO.getLimitTypeId());		
				action = new ApprovalMatrixActionDTO();
				action.setActionId(approvalMatrixDTO.getActionId());
				action.setActionName(approvalMatrixDTO.getActionName());
				action.setActionDescription(approvalMatrixDTO.getActionDescription());
				action.setFeatureId(approvalMatrixDTO.getFeatureId());
				action.setFeatureName(approvalMatrixDTO.getFeatureName());
				action.setActionType(approvalMatrixDTO.getActionType());
				action.setMaxAmount(approvalMatrixDTO.getMaxAmount());
				action.setIsAccountLevel(approvalMatrixDTO.getIsAccountLevel());
				featureStatus = (approvalMatrixDTO.getOrgfeaturestatus()==null || approvalMatrixDTO.getOrgfeaturestatus().equals(""))?approvalMatrixDTO.getFifeaturestatus():approvalMatrixDTO.getOrgfeaturestatus();
				action.setFeatureStatus(featureStatus);
				limit = new ApprovalMatrixLimitDTO();
				limit.setLowerlimit(approvalMatrixDTO.getLowerlimit());
				limit.setUpperlimit(approvalMatrixDTO.getUpperlimit());
				limit.setCurrency(approvalMatrixDTO.getCurrency());
				limit.setApprovalRuleId(approvalMatrixDTO.getApprovalruleId());
				limit.setApprovalRuleName(approvalMatrixDTO.getApprovalRuleName());
				limit.setNumberOfApprovals(approvalMatrixDTO.getNumberOfApprovals());
				limit.setInvalid(approvalMatrixDTO.getInvalid());
				limit.setGroupList(approvalMatrixDTO.getGroupList());
				limit.setGroupRule(approvalMatrixDTO.getGroupRule());
				action.add(limit);
				limitTypeID.add(action);
				account.add(limitTypeID);
				cif.add(account);
				approvalMatrixOutputDTO.add(cif);
				currLowerLimit = approvalMatrixDTO.getLowerlimit();
				currUpperLimit = approvalMatrixDTO.getUpperlimit();
				currActionID = approvalMatrixDTO.getActionId();
				currLimitTypeID = approvalMatrixDTO.getLimitTypeId();
				currAccountID = approvalMatrixDTO.getAccountId();
				currCif = approvalMatrixDTO.getCifId();
				cifCount++;
				accountCount=0;
				limitTypeCount=0;
				actionCount=0;
				limitCount=0;				
			}

		}	
		return approvalMatrixOutputDTO;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((actionDescription == null) ? 0 : actionDescription.hashCode());
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((approvalRuleName == null) ? 0 : approvalRuleName.hashCode());
		result = prime * result + ((approvalruleId == null) ? 0 : approvalruleId.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((customerApprovalMatrixDTO == null) ? 0 : customerApprovalMatrixDTO.hashCode());
		result = prime * result + ((signatoryGroupMatrixDTO == null) ? 0 : signatoryGroupMatrixDTO.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((fifeaturestatus == null) ? 0 : fifeaturestatus.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((lowerlimit == null) ? 0 : lowerlimit.hashCode());
		result = prime * result + numberOfApprovals;
		result = prime * result + ((orgfeaturestatus == null) ? 0 : orgfeaturestatus.hashCode());
		result = prime * result + ((upperlimit == null) ? 0 : upperlimit.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((cifId == null) ? 0 : cifId.hashCode());
		result = prime * result + ((cifName == null) ? 0 : cifName.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + (invalid ? 1231 : 1237);
		result = prime * result + ((isGroupMatrix == null) ? 0 : isGroupMatrix.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((ownershipType == null) ? 0 : ownershipType.hashCode());
		result = prime * result + ((maxAmount == null) ? 0 : maxAmount.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApprovalMatrixDTO other = (ApprovalMatrixDTO) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (actionDescription == null) {
			if (other.actionDescription != null)
				return false;
		} else if (!actionDescription.equals(other.actionDescription))
			return false;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		if (approvalRuleName == null) {
			if (other.approvalRuleName != null)
				return false;
		} else if (!approvalRuleName.equals(other.approvalRuleName))
			return false;
		if (approvalruleId == null) {
			if (other.approvalruleId != null)
				return false;
		} else if (!approvalruleId.equals(other.approvalruleId))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (customerApprovalMatrixDTO == null) {
			if (other.customerApprovalMatrixDTO != null)
				return false;
		} else if (!customerApprovalMatrixDTO.equals(other.customerApprovalMatrixDTO))
			return false;
		if (signatoryGroupMatrixDTO == null) {
			if (other.signatoryGroupMatrixDTO != null)
				return false;
		} else if (!signatoryGroupMatrixDTO.equals(other.signatoryGroupMatrixDTO))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (featureName == null) {
			if (other.featureName != null)
				return false;
		} else if (!featureName.equals(other.featureName))
			return false;
		if (fifeaturestatus == null) {
			if (other.fifeaturestatus != null)
				return false;
		} else if (!fifeaturestatus.equals(other.fifeaturestatus))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (limitTypeId == null) {
			if (other.limitTypeId != null)
				return false;
		} else if (!limitTypeId.equals(other.limitTypeId))
			return false;
		if (lowerlimit == null) {
			if (other.lowerlimit != null)
				return false;
		} else if (!lowerlimit.equals(other.lowerlimit))
			return false;
		if (numberOfApprovals != other.numberOfApprovals)
			return false;
		if (orgfeaturestatus == null) {
			if (other.orgfeaturestatus != null)
				return false;
		} else if (!orgfeaturestatus.equals(other.orgfeaturestatus))
			return false;
		if (upperlimit == null) {
			if (other.upperlimit != null)
				return false;
		} else if (!upperlimit.equals(other.upperlimit))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (cifId == null) {
			if (other.cifId != null)
				return false;
		} else if (!cifId.equals(other.cifId))
			return false;
		if (cifName == null) {
			if (other.cifName != null)
				return false;
		} else if (!cifName.equals(other.cifName))
			return false;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (invalid != other.invalid)
			return false;
		if (isGroupMatrix == null) {
			if (other.isGroupMatrix != null)
				return false;
		} else if (!isGroupMatrix.equals(other.isGroupMatrix))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (ownershipType == null) {
			if (other.ownershipType != null)
				return false;
		} else if (!ownershipType.equals(other.ownershipType))
			return false;
		if (maxAmount == null) {
			if (other.maxAmount != null)
				return false;
		} else if (!maxAmount.equals(other.maxAmount))
			return false;
		if (isAccountLevel != other.isAccountLevel)
			return false;
		return true;
	}
}