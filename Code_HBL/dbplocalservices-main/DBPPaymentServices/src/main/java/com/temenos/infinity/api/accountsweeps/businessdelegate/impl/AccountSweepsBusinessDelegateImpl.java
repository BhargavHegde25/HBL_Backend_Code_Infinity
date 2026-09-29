package com.temenos.infinity.api.accountsweeps.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.accountsweeps.backenddelegate.api.AccountSweepsBackendDelegate;
import com.temenos.infinity.api.accountsweeps.businessdelegate.api.AccountSweepsBusinessDelegate;
import com.temenos.infinity.api.accountsweeps.dto.AccountSweepsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.List;
import java.util.Set;

/**
 * @author naveen.yerra
 */
public class AccountSweepsBusinessDelegateImpl implements AccountSweepsBusinessDelegate {

    private final AccountSweepsBackendDelegate sweepsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountSweepsBackendDelegate.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    public AccountSweepsDTO getSweepByAccountId(String accountId) {
        return sweepsBackendDelegate.getSweepByAccountId(accountId);
    }
    public AccountSweepsDTO createSweep(AccountSweepsDTO accountSweepsDTO, DataControllerRequest request,String accountSweepBackend){
        AccountSweepsDTO sweepsDTO=null;
        try {
            sweepsDTO = sweepsBackendDelegate.createSweepAtBackEnd(accountSweepsDTO,request,accountSweepBackend);
        }
        catch (Exception e){
            alert.prepareError("Exception occured while creating record at backend",e).log();
        }
        return sweepsDTO;
    }

    @Override
    public List<AccountSweepsDTO> getAllAccountSweeps(Set<String> accounts) {
        return sweepsBackendDelegate.getAllSweepsFromBackend(accounts);
    }

    @Override
    public String getAllAccountSweepsFromT24(Set<String> accounts,DataControllerRequest request) {
        return sweepsBackendDelegate.getAllSweepsFromT24(accounts,request);
    }
    


    
    public AccountSweepsDTO deleteSweep(AccountSweepsDTO accountSweepsDTO, DataControllerRequest request,String accountSweepBackend) {
       return sweepsBackendDelegate.deleteSweepAtBackEnd(accountSweepsDTO,request,accountSweepBackend);

    }

    public AccountSweepsDTO editSweep(AccountSweepsDTO accountSweepsDTO, DataControllerRequest request,String accountSweepBackend) {
        return sweepsBackendDelegate.editSweep(accountSweepsDTO, request,accountSweepBackend);
    }

}
