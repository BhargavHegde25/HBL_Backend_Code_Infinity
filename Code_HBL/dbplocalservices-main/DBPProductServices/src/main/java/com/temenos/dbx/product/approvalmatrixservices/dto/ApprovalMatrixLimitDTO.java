package com.temenos.dbx.product.approvalmatrixservices.dto;
import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;

/**
 * 
 * @author KH9450
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
public class ApprovalMatrixLimitDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5292040825088009861L;
	private String upperlimit;
	private String lowerlimit;
	private String currency;
	private String approvalRuleId;
	private String approvalRuleName;
	private int numberOfApprovals;
	private boolean invalid;
	private List<ApprovalMatrixApproverDTO> approvers = new ArrayList<>();
	private String groupRule;
	private String groupList;
	public ApprovalMatrixLimitDTO(){
	}
	public ApprovalMatrixLimitDTO(String upperlimit,String lowerlimit,String currency, String approvalRuleId,String approvalRuleName,int numberOfApprovals, boolean invalid, List<ApprovalMatrixApproverDTO> approvers, String groupList, String groupRule)
	{
		this.upperlimit = upperlimit;
		this.lowerlimit = lowerlimit;
		this.currency = currency;
		this.approvalRuleId = approvalRuleId;
		this.approvalRuleName = approvalRuleName;
		this.numberOfApprovals = numberOfApprovals;
		this.invalid = invalid;
		this.approvers = approvers;
		this.groupList = groupList;
		this.groupRule = groupRule;
	}
	public void add (ApprovalMatrixApproverDTO approver)
	{
		this.approvers.add(approver);
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
	public boolean isInvalid() {
		return invalid;
	}
	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	public String getUpperlimit() 
	{
		return upperlimit;
	}
	public void setUpperlimit(String d)
	{
		this.upperlimit = d;
	}
	public String getLowerlimit() 
	{
		return lowerlimit;
	}
	public void setLowerlimit(String d) 
	{
		this.lowerlimit = d;
	}
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	public String getCurrency(){return currency;}
	public String getApprovalRuleId()
	{
		return approvalRuleId;
	}
	public void setApprovalRuleId(String approvalRuleId)
	{
		this.approvalRuleId = approvalRuleId;
	}
	public String getApprovalRuleName()
	{
		return approvalRuleName;
	}
	public void setApprovalRuleName(String approvalRuleName) 
	{
		this.approvalRuleName = approvalRuleName;
	}
	public int getNumberOfApprovals() {
		return numberOfApprovals;
	}
	public void setNumberOfApprovals(int numberOfApprovals)
	{
		this.numberOfApprovals = numberOfApprovals;
	}
	public List<ApprovalMatrixApproverDTO> getApprovers() 
	{
		return approvers;
	}
	public void setApprovers(List<ApprovalMatrixApproverDTO> approvers) 
	{
		this.approvers = approvers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approvalRuleId == null) ? 0 : approvalRuleId.hashCode());
		result = prime * result + ((approvalRuleName == null) ? 0 : approvalRuleName.hashCode());
		result = prime * result + ((approvers == null) ? 0 : approvers.hashCode());
		result = prime * result + (invalid ? 1231 : 1237);
		result = prime * result + ((lowerlimit == null) ? 0 : lowerlimit.hashCode());
		result = prime * result + numberOfApprovals;
		result = prime * result + ((upperlimit == null) ? 0 : upperlimit.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
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
		ApprovalMatrixLimitDTO other = (ApprovalMatrixLimitDTO) obj;
		if (approvalRuleId == null) {
			if (other.approvalRuleId != null)
				return false;
		} else if (!approvalRuleId.equals(other.approvalRuleId))
			return false;
		if (approvalRuleName == null) {
			if (other.approvalRuleName != null)
				return false;
		} else if (!approvalRuleName.equals(other.approvalRuleName))
			return false;
		if (approvers == null) {
			if (other.approvers != null)
				return false;
		} else if (!approvers.equals(other.approvers))
			return false;
		if (invalid != other.invalid)
			return false;
		if (lowerlimit == null) {
			if (other.lowerlimit != null)
				return false;
		} else if (!lowerlimit.equals(other.lowerlimit))
			return false;
		if (numberOfApprovals != other.numberOfApprovals)
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
		return true;
	}
	
	public static boolean sameLimits(List<ApprovalMatrixLimitDTO> limits1, List<ApprovalMatrixLimitDTO> limits2) {
		if( (limits1 == null) || (limits2 == null) || (limits1.size() != limits2.size()) ) {
			return false;
		}
		boolean result = true;
		for( int i = 0; i < limits1.size(); i++ ) {
			result = limits1.get(i).equals(limits2.get(i));
			
			if( result == false ) {
				return result;
			}
		}
		
		return result;
	}
	
}