package com.temenos.infinity.api.loanspayoff.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.loanspayoff.dto.BillDetails;
import com.temenos.infinity.api.loanspayoff.dto.LoanDTO;

public interface LoansPayoffAPIBackendDelegate extends BackendDelegate {

    LoanDTO createSimulationT24(String arrangementId, String activityId, String productId, String effectiveDate,
            String backendToken) throws ApplicationException;

    BillDetails getBillDetailsT24(String arrangementId, String billType, String paymentDate, String backendToken)
            throws ApplicationException;

}
