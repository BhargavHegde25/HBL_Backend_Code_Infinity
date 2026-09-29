package com.temenos.dbx.party.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for
 *          AuditActivityPartyCustomerInformationUpdateBusinessDelegate extends
 *          {@link BusinessDelegate}
 *
 */

public interface AuditActivityPartyCustomerInformationUpdateBusinessDelegate extends BusinessDelegate {

	boolean updateAuditLogsWithCustomerInformation(Map<String, Object> inputParams, Map<String, Object> headersMap,
			String url);

}
