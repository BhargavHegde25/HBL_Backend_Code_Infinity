package com.temenos.dbx.product.approvalservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 
 * @author KH2174
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalRuleDTO implements DBPDTO {

	private static final long serialVersionUID = 6597925078705187784L;
	
	private String id;
	private String name;
	private int numberOfApprovals;
	
	public ApprovalRuleDTO() {
		super();
	}
	
	public ApprovalRuleDTO(String id, String name, int numberOfApprovals) {
		super();
		this.id = id;
		this.name = name;
		this.numberOfApprovals = numberOfApprovals;
	}
	public int getNumberOfApprovals() {
		return numberOfApprovals;
	}
	public void setNumberOfApprovals(int numberOfApprovals) {
		this.numberOfApprovals = numberOfApprovals;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfApprovals;
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
		ApprovalRuleDTO other = (ApprovalRuleDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfApprovals != other.numberOfApprovals)
			return false;
		return true;
	}
	
	
}
