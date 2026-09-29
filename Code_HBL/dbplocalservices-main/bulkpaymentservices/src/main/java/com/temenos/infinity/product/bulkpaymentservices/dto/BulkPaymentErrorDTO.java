package com.temenos.infinity.product.bulkpaymentservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentErrorDTO implements DBPDTO {
	
	private static final long serialVersionUID = -7402234489127740356L;
	
	@JsonAlias({"errorDetail"})
	private String errorDescription;

	public BulkPaymentErrorDTO() {
		super();
	}
	
	public BulkPaymentErrorDTO(BulkPaymentErrorDTO dto) {
		super();
		this.errorDescription = dto.errorDescription;
	}
	
	public BulkPaymentErrorDTO(String errorDescription) {
		super();		
		this.errorDescription = errorDescription;
		
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result + ((errorDescription == null) ? 0 : errorDescription.hashCode());
		
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
		BulkPaymentErrorDTO other = (BulkPaymentErrorDTO) obj;
		if (errorDescription == null) {
			if (other.errorDescription != null)
				return false;
		} else if (!errorDescription.equals(other.errorDescription))
			return false;
		return true;
	}
}
