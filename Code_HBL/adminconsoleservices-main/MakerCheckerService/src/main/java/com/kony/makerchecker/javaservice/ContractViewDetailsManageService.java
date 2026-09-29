package com.kony.makerchecker.javaservice;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class ContractViewDetailsManageService implements JavaService2 {

	private static final String CREATE_CONTRACT_VIEW_DETAILS = "CreateContractViewDetails";
	private static final String EDIT_CONTRACT_VIEW_DETAILS = "EditContractViewDetails";

	private static final String CREATE_SIGNATORY_GROUP_VIEW_DETAILS = "CreateSignatoryGroupViewDetail";
	private static final String DELETE_SIGNATORY_GROUP_VIEW_DETAILS = "DeleteSignatoryGroupViewDetail";
	private static final String EDIT_SIGNATORY_GROUP_VIEW_DETAILS   = "EditSignatoryGroupViewDetails";
	private static final String CREATE_APPROVAL_RULE_SIGNATORY_GROUP_VIEW_DETAILS = "CreateApprovalRuleBySignatoryGroupViewDetails";

	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {

			MakerCheckerResource makerCheckerResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(MakerCheckerResource.class);
			if (StringUtils.equalsIgnoreCase(methodId, CREATE_CONTRACT_VIEW_DETAILS)) {
				result = makerCheckerResource.createContractViewDetails(methodId, inputArray, request, response);
			} else if (StringUtils.equalsIgnoreCase(methodId, EDIT_CONTRACT_VIEW_DETAILS)) {
				result = makerCheckerResource.editContractViewDetails(methodId, inputArray, request, response);
			} else if (StringUtils.equalsIgnoreCase(methodId, CREATE_SIGNATORY_GROUP_VIEW_DETAILS)) {
				result = makerCheckerResource.createSignatoryGroupViewDetails(methodId, inputArray, request, response);
			} else if (StringUtils.equalsIgnoreCase(methodId, DELETE_SIGNATORY_GROUP_VIEW_DETAILS)) {
				result = makerCheckerResource.deleteSignatoryGroupViewDetails(methodId, inputArray, request, response);
			} else if (StringUtils.equalsIgnoreCase(methodId, EDIT_SIGNATORY_GROUP_VIEW_DETAILS)) {
				result = makerCheckerResource.editSignatoryGroupViewDetails(methodId, inputArray, request, response);
			} else if (StringUtils.equalsIgnoreCase(methodId, CREATE_APPROVAL_RULE_SIGNATORY_GROUP_VIEW_DETAILS)) {
				result = makerCheckerResource.createApprovalRuleBySignatoryGroupViewDetails(methodId, inputArray, request, response);
			}
			return result;
		} catch (Exception e) {
			alert.prepareError("Exception occurred in invoking Contract View Details Manage Service: " + e).log();
			return ErrorCodesEnum.ERR_10005.setErrorCode(new Result());
		}
	}
}
