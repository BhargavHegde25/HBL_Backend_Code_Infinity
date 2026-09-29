package com.temenos.infinity.api.chequemanagement.businessdelegate.impl;
import java.util.List;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ChequeManagementBackendDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.ChequeManagementBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.dto.ApprovalRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class ChequeManagementBusinessDelegateImpl implements ChequeManagementBusinessDelegate{
	
	@Override
	public ChequeBookAction rejectChequeBook(DataControllerRequest request) throws ApplicationException {
		
		ChequeManagementBackendDelegate chequeManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ChequeManagementBackendDelegate.class);
		ChequeBookAction chequeBookAction = chequeManagementBackendDelegate.rejectChequeBook(request); 
		return chequeBookAction; 

	}
	@Override
	public ChequeBookAction withdrawCheque(DataControllerRequest request) throws ApplicationException {
		ChequeManagementBackendDelegate chequeManagementBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ChequeManagementBackendDelegate.class);
		ChequeBookAction chequeBookAction = chequeManagementBackendDelegate.withdrawCheque(request);
		return chequeBookAction;
	}
	@Override
	public List<ApprovalRequestDTO> fetchChequeDetails(DataControllerRequest request) throws ApplicationException {
		ChequeManagementBackendDelegate chequeManagementBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ChequeManagementBackendDelegate.class);
		List<ApprovalRequestDTO> chequeBooks = chequeManagementBackendDelegate.fetchChequeDetails(request);
		return chequeBooks;
	}
}
