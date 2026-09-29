package com.temenos.infinity.api.chequemanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.resource.api.GetCheuqeBookResource;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeBookRequestsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            GetCheuqeBookResource chequeBookResource = DBPAPIAbstractFactoryImpl
                    .getResource(GetCheuqeBookResource.class);
            ChequeBook chequeBook = new ChequeBook();
            String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
            String chequeIssueId = request.getParameter("chequeIssueId") != null ? request.getParameter("chequeIssueId") : "";
            chequeBook.setAccountID(accountId);
            chequeBook.setChequeIssueId(chequeIssueId);
            
            Result result = chequeBookResource.getChequeBookRequests(chequeBook, request);
            return result;
        } catch (Exception e) { 
            alert.prepareError("Unable to get ChequeBook Requests from OMS: "+e).log();
            return ErrorCodeEnum.ERR_26004.setErrorCode(new Result()); 
        }

    }
}
