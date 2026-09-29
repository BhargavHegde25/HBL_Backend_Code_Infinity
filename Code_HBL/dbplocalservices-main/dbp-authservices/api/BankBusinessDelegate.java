package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for ApproverBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface BankBusinessDelegate extends BusinessDelegate {

	DBXResult getBankName(String bankID, Map<String, Object> headers);
}
