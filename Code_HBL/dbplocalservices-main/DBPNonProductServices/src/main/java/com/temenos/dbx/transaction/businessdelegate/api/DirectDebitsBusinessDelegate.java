package com.temenos.dbx.transaction.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.DirectDebitDTO;

/**
 * @author sribarani.vasthan
 */
public interface DirectDebitsBusinessDelegate extends BusinessDelegate {
	public List<DirectDebitDTO> getDirectDebits(DirectDebitDTO inputDTO);

	public DirectDebitDTO cancelDirectDebit(String directDebitId, String directDebitStatus);

}
