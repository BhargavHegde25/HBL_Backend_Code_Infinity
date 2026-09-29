package com.kony.adminconsole.service.approvalmatrix.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.approvalmatrix.resource.api.ApprovalMatrixResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ApprovalMatrixManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    
    private static final String GET_APPROVAL_MATRIX_OPERATION_NAME = "getApprovalMatrix";
    private static final String CREATE_APPROVAL_RULE_SG_LEVEL_OPERATION_NAME = "createApprovalRuleSGLevel";
    private static final String CREATE_APPROVAL_RULE_USER_LEVEL_OPERATION_NAME = "createApprovalRuleUserLevel";
    
    private static final String GET_APPROVAL_RULES_OPERATION_NAME = "getApprovalRules";
    private static final String UPDATE_APPROVAL_RULE_USER_LEVEL_OPERATION_NAME = "updateApprovalRuleUserLevel";
   
    private static final String UPDATE_APPROVAL_RULE_SG_LEVEL_OPERATION_NAME = "updateApprovalRuleSGLevel";
    private static final String GET_APPROVERS_IN_SG_OPERATION_NAME = "getApproversInSignatoryGroup";
    
    private static final String GET_APPROVAL_MATRIX_BY_CONTRACT_ID_OPERATION_NAME = "getApprovalMatrixByContractId";
    private static final String GET_ACC_ACTION_CUSTOMER_APPROVER_LIST_OPERATION_NAME = "getAccountActionCustomerApproverList";
    private static final String UPDATE_APPROVALMATRIX_STATUS = "updateApprovalMatrixStatus";
    private static final String IS_APPROVALMATRIX_DISABLED = "isApprovalMatrixDisabled";
    
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
    	
        try {

            if (methodID.equalsIgnoreCase(GET_APPROVAL_MATRIX_OPERATION_NAME)) {
                return getApprovalMatrix(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(CREATE_APPROVAL_RULE_SG_LEVEL_OPERATION_NAME)) {
                return createApprovalRuleSGLevel(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(CREATE_APPROVAL_RULE_USER_LEVEL_OPERATION_NAME)) {
                return createApprovalRuleUserLevel(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_APPROVAL_RULES_OPERATION_NAME)) {
                return getApprovalRules(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_APPROVAL_RULE_USER_LEVEL_OPERATION_NAME)) {
                return updateApprovalRuleUserLevel(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_APPROVAL_RULE_SG_LEVEL_OPERATION_NAME)) {
                return updateApprovalRuleSGLevel(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_APPROVERS_IN_SG_OPERATION_NAME)) {
            	return getApproversInSignatoryGroup(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_APPROVAL_MATRIX_BY_CONTRACT_ID_OPERATION_NAME)) {
            	return getApprovalMatrixByContractId(methodID, inputArray, requestInstance, responseInstance);
            }else if(methodID.equalsIgnoreCase(GET_ACC_ACTION_CUSTOMER_APPROVER_LIST_OPERATION_NAME)) {
            	return getAccountActionCustomerApproverList(methodID, inputArray, requestInstance, responseInstance);
            }else if(methodID.equalsIgnoreCase(UPDATE_APPROVALMATRIX_STATUS)) {
            	return updateApprovalMatrixStatus(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(IS_APPROVALMATRIX_DISABLED)) {
                return isApprovalMatrixDisabled(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
        
    }

    private Object getApprovalMatrix(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixManageService = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixManageService.getApprovalMatrix(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getApprovalMatrix: ", e).log();
            return ErrorCodeEnum.ERR_22101.setErrorCode(new Result());
        }
        return result;
    }

    private Object createApprovalRuleSGLevel(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.createApprovalRuleSGLevel(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createApprovalRuleSGLevel: ", e).log();
            return ErrorCodeEnum.ERR_22102.setErrorCode(new Result());
        }
        return result;
    }

    private Object createApprovalRuleUserLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.createApprovalRuleUserLevel(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createApprovalRuleUserLevel: ", e).log();
            return ErrorCodeEnum.ERR_22103.setErrorCode(new Result());
        }
        return result;
    }

    private Object getApprovalRules(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.getApprovalRules(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getApprovalRules: ", e).log();
            return ErrorCodeEnum.ERR_22104.setErrorCode(new Result());
        }
        return result;
    }

    private Object updateApprovalRuleUserLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.updateApprovalRuleUserLevel(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateApprovalRuleUserLevel: ", e).log();
            return ErrorCodeEnum.ERR_22105.setErrorCode(new Result());
        }
        return result;
    }

    private Object updateApprovalRuleSGLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.updateApprovalRuleSGLevel(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateApprovalRuleSGLevel: ", e).log();
            return ErrorCodeEnum.ERR_22106.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getApproversInSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.getApproversInSignatoryGroup(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getApproversInSignatoryGroup: ", e).log();
            return ErrorCodeEnum.ERR_22107.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getApprovalMatrixByContractId(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.getApprovalMatrixByContractId(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getApprovalMatrixByContractId: ", e).log();
            return ErrorCodeEnum.ERR_22118.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getAccountActionCustomerApproverList(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.getAccountActionCustomerApproverList(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getAccountActionCustomerApproverList: ", e).log();
            return ErrorCodeEnum.ERR_22119.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object updateApprovalMatrixStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.updateApprovalMatrixStatus(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateApprovalMatrixStatus: ", e).log();
            return ErrorCodeEnum.ERR_20513.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object isApprovalMatrixDisabled(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
        	ApprovalMatrixResource approvalmatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
            result = approvalmatrixResource.isApprovalMatrixDisabled(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of isApprovalMatrixDisabled: ", e).log();
            return ErrorCodeEnum.ERR_20514.setErrorCode(new Result());
        }
        return result;
    }

}
