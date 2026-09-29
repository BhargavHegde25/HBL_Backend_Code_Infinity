package com.temenos.dbx.transaction.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.BlockedFundDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface BlockedFundsBusinessDelegate extends BusinessDelegate {

    public List<BlockedFundDTO> getBlockedFunds(BlockedFundDTO inputDTO);
}
