package com.temenos.dbx.product.payeeservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayeesFilterDTO implements DBPDTO{

	private static final long serialVersionUID = -209633969550095413L;
	
	private String order;
	private String searchString;
	private String sortBy;
	private String limit;
	private String offset;
	private String payeeId;
	private String customerId;
	private String companyId;
	
	public PayeesFilterDTO() {
		super();
	}
	
	public PayeesFilterDTO(String order, String searchString, String sortBy, String limit, String offset,
			String payeeId, String customerId, String companyId) {
		super();
		this.order = order;
		this.searchString = searchString;
		this.sortBy = sortBy;
		this.limit = limit;
		this.offset = offset;
		this.payeeId = payeeId;
		this.customerId = customerId;
		this.companyId = companyId;
	}
	
	public String getOrder() {
		return order;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getSearchString() {
		return searchString;
	}
	
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	public String getSortBy() {
		return sortBy;
	}
	
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	public String getLimit() {
		return limit;
	}
	
	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public String getOffset() {
		return offset;
	}
	
	public void setOffset(String offset) {
		this.offset = offset;
	}
	
	public String getPayeeId() {
		return payeeId;
	}
	
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + ((offset == null) ? 0 : offset.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((searchString == null) ? 0 : searchString.hashCode());
		result = prime * result + ((sortBy == null) ? 0 : sortBy.hashCode());
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
		PayeesFilterDTO other = (PayeesFilterDTO) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (limit == null) {
			if (other.limit != null)
				return false;
		} else if (!limit.equals(other.limit))
			return false;
		if (offset == null) {
			if (other.offset != null)
				return false;
		} else if (!offset.equals(other.offset))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (payeeId == null) {
			if (other.payeeId != null)
				return false;
		} else if (!payeeId.equals(other.payeeId))
			return false;
		if (searchString == null) {
			if (other.searchString != null)
				return false;
		} else if (!searchString.equals(other.searchString))
			return false;
		if (sortBy == null) {
			if (other.sortBy != null)
				return false;
		} else if (!sortBy.equals(other.sortBy))
			return false;
		return true;
	}

}