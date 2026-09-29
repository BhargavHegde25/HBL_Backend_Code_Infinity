package com.temenos.dbx.product.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.AccountActionApproverListDTO;
import com.temenos.dbx.product.dto.GroupActionsDTO;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for ApproverBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface ApproverBusinessDelegate extends BusinessDelegate {

    /**
     * method to get the approvers having access to account and action {@link GroupActionsDTO}
     */
    List<AccountActionApproverListDTO> getAccountActionApproverList(
            AccountActionApproverListDTO accountActionApproverListInputDTO,
            Map<String, Object> headersMap,
            String url);
}
