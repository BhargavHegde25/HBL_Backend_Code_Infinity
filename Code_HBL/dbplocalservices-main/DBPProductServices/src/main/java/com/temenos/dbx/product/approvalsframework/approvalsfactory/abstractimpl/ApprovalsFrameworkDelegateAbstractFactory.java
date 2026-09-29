package com.temenos.dbx.product.approvalsframework.approvalsfactory.abstractimpl;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.approvalsframework.approvalsfactory.api.ApprovalsFrameworkDelegateFactory;
import com.temenos.dbx.product.constants.FeatureAction;

public class ApprovalsFrameworkDelegateAbstractFactory implements ApprovalsFrameworkDelegateFactory {

    ApprovalsFrameworkDelegateFactory delegateFactory = null;

    public ApprovalsFrameworkDelegateAbstractFactory(String action){
        switch (action){
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
            case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
            case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
            case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
            case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
            case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
            case FeatureAction.BILL_PAY_CREATE:
            case FeatureAction.P2P_CREATE:
            case FeatureAction.ACH_FILE_UPLOAD:
            case FeatureAction.ACH_COLLECTION_CREATE:
            case FeatureAction.ACH_PAYMENT_CREATE:
            case FeatureAction.BULK_PAYMENT_REQUEST_SUBMIT:
            case FeatureAction.CHEQUE_BOOK_REQUEST_CREATE:
            case FeatureAction.IMPORT_LC_CREATE:
            default: this.delegateFactory = null;
        }
    }
    @Override
    public void executeOperationBeforeApproval() throws ApplicationException {
        if(this.delegateFactory != null){
            this.delegateFactory.executeOperationBeforeApproval();
        }
    }

    @Override
    public void executeOperationAfterApproval() throws ApplicationException {
        if(this.delegateFactory != null){
            this.delegateFactory.executeOperationAfterApproval();
        }
    }

    @Override
    public void fetchFeatureDataWithApprovalInfo() throws ApplicationException{
        if(this.delegateFactory != null){
            this.delegateFactory.fetchFeatureDataWithApprovalInfo();
        }
    }

    @Override
    public void approveOperation() throws ApplicationException {
        if(this.delegateFactory != null){
            this.delegateFactory.approveOperation();
        }
    }

    @Override
    public void rejectOperation() throws ApplicationException {
        if(this.delegateFactory != null){
            this.delegateFactory.rejectOperation();
        }
    }

    @Override
    public void withdrawOperation() throws ApplicationException {
        if(this.delegateFactory != null){
            this.delegateFactory.withdrawOperation();
        }
    }
}
