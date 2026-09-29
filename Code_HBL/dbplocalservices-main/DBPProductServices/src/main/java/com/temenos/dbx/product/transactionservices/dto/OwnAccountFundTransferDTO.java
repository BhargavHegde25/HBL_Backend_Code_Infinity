package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class OwnAccountFundTransferDTO extends TransferDTO  implements DBPDTO{

	private static final long serialVersionUID = -6809020923323456022L;

    public OwnAccountFundTransferDTO updateValues(OwnAccountFundTransferDTO dto) {
    	super.updateValues(dto);
    	return dto;
    }
	


}