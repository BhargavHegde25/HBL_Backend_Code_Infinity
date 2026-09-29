package com.temenos.dbx.transaction.businessdelegate.impl;

import java.util.ArrayList;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.temenos.dbx.transaction.businessdelegate.api.BlockedFundsBusinessDelegate;
import com.temenos.dbx.transaction.dto.BlockedFundDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class BlockedFundsBusinessDelegateImpl implements BlockedFundsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    public List<BlockedFundDTO> getBlockedFunds(BlockedFundDTO inputDTO){
        String accountID = inputDTO.getAccountID();
        List<BlockedFundDTO>  blockedFundsList = new ArrayList<>();
        
        //Implement Dbxdb code 
        String accountId = inputDTO.getAccountID();
        BlockedFundDTO record1 = new BlockedFundDTO();
        record1.setAccountID(accountId);
        record1.setFromDate("2020-04-16T08:52:35Z");
        record1.setToDate("2020-04-30T08:52:35Z");
        record1.setLockedEventId("ACLK2010813942"); 
        record1.setLockedAmount("550.00");
        record1.setTransactionReference("ACLK2010813942");
        record1.setLockReason("Locked By Bank");
        blockedFundsList.add(record1);
        
        BlockedFundDTO record2 = new BlockedFundDTO();
        record2.setAccountID(accountId);
        record2.setFromDate("2020-04-11T08:52:35Z");
        record2.setToDate("2020-04-17T08:52:35Z");
        record2.setLockedEventId("ACLK2010813456");
        record2.setLockedAmount("1500.00");
        record2.setTransactionReference("ACLK2010813456");
        record2.setLockReason("Locked By Bank"); 
        blockedFundsList.add(record2);
        
        return blockedFundsList;
    }

}
