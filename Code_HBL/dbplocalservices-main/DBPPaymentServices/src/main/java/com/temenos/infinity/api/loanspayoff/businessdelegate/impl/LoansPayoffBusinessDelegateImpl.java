package com.temenos.infinity.api.loanspayoff.businessdelegate.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.loanspayoff.backenddelegate.api.LoansPayoffAPIBackendDelegate;
import com.temenos.infinity.api.loanspayoff.businessdelegate.api.LoansPayoffBusinessDelegate;
import com.temenos.infinity.api.loanspayoff.constants.ErrorCodeEnum;
import com.temenos.infinity.api.loanspayoff.dto.BillDetails;
import com.temenos.infinity.api.loanspayoff.dto.LoanDTO;

public class LoansPayoffBusinessDelegateImpl implements LoansPayoffBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public LoanDTO createSimulation(String arrangementId, String activityId, String productId, String effectiveDate,
            String backendToken) throws ApplicationException {

        try {
            LoansPayoffAPIBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(LoansPayoffAPIBackendDelegate.class);
            LoanDTO output = backendDelegate.createSimulationT24(arrangementId, activityId, productId, effectiveDate,
                    backendToken);
            return output;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }

    }

    @Override
    public BillDetails getBillDetails(String arrangementId, String billType, String paymentDate, String backendToken)
            throws ApplicationException {
        try {
            LoansPayoffAPIBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(LoansPayoffAPIBackendDelegate.class);
            BillDetails output = backendDelegate.getBillDetailsT24(arrangementId, billType, paymentDate, backendToken);
            return output;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }
}