package com.temenos.infinity.api.loanspayoff.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.loanspayoff.dto.BillDetails;
import com.temenos.infinity.api.loanspayoff.dto.LoanDTO;

public interface LoansPayoffBusinessDelegate extends BusinessDelegate {

    LoanDTO createSimulation(String arrangementId, String activityId, String productId, String effectiveDate,
            String backendToken) throws ApplicationException;

    BillDetails getBillDetails(String arrangementId, String billType, String paymentDate, String backendToken)
            throws ApplicationException;

}
