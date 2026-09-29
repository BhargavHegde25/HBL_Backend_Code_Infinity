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
public class ApprovalMatrixLimitTypeDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161965550436408149L;
	private String limitTypeId;
	private List<ApprovalMatrixActionDTO> actions = new ArrayList<>();
	public ApprovalMatrixLimitTypeDTO(){		
	}
	public ApprovalMatrixLimitTypeDTO(String limitTypeId,List<ApprovalMatrixActionDTO> actions)
	{
		this.limitTypeId = limitTypeId;
		this.actions = actions;
	}
	public List<ApprovalMatrixActionDTO> getActions() {
		return actions;
	}
	public void setActions(List<ApprovalMatrixActionDTO> actions) {
		this.actions = actions;
	}
	public String getLimitTypeId() {
		return limitTypeId;
	}
	public void setLimitTypeId(String limitTypeId) {
		this.limitTypeId = limitTypeId;
	}
	public void add(ApprovalMatrixActionDTO action) {
		this.actions.add(action);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
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
		ApprovalMatrixLimitTypeDTO other = (ApprovalMatrixLimitTypeDTO) obj;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		if (limitTypeId == null) {
			if (other.limitTypeId != null)
				return false;
		} else if (!limitTypeId.equals(other.limitTypeId))
			return false;
		return true;
	}
}
