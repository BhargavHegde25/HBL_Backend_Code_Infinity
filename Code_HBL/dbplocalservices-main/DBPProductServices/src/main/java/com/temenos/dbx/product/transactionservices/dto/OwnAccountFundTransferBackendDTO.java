package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class OwnAccountFundTransferBackendDTO extends TransferDTO implements DBPDTO{

	private static final long serialVersionUID = 7973996816061871938L;
	
	public OwnAccountFundTransferBackendDTO convert(OwnAccountFundTransferDTO dto) {
		super.convert(dto);
		return this;
		
	}
	
}