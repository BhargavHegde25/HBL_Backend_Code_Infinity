package com.temenos.dbx.product.usermanagement.resource.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.fileutil.MimeTypeValidator;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerImageDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerImageBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerImageResource;

public class CustomerImageResourceImpl implements CustomerImageResource {
	private static Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getCustomerImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();

		CustomerImageDTO customerImageDTO = new CustomerImageDTO();

		String customerImage = "";
		String Customer_id = HelperMethods.getCustomerIdFromSession(dcRequest);
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
		customerImageDTO.setCustomer_id(Customer_id);
		customerImageDTO.setLegalEntityId(legalEntityId);
		try {
			CustomerImageBusinessDelegate customerImageBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CustomerImageBusinessDelegate.class);

			customerImage = customerImageBusinessDelegate.getCustomerImage(customerImageDTO,
					HelperMethods.getHeaders(dcRequest));

		} catch (Exception e) {
			alert.prepareError(
					"Exception while associating a business delegate call to CustomerImageBusinessDelegate to create customer image"
							+ e.getMessage()).log();
			ErrorCodeEnum.ERR_10195.setErrorCode(result);
			return result;
		}

		if (customerImage == null) {
			ErrorCodeEnum.ERR_10195.setErrorCode(result);
		} else if (StringUtils.isBlank(customerImage)) {
			result.addParam(DBPUtilitiesConstants.USER_IMAGE,
					HelperMethods.getFieldValue(result, DBPUtilitiesConstants.USER_IMAGE));
		} else {
			result.addParam(DBPUtilitiesConstants.USER_IMAGE, customerImage);
		}
		return result;
	}

	@Override
	public Result deleteCustomerImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		Result result = new Result();

		CustomerImageDTO customerImageDTO = new CustomerImageDTO();

		boolean customerImageDeleteStatus = false;
		try {
			CustomerImageBusinessDelegate customerImageBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CustomerImageBusinessDelegate.class);

			customerImageDeleteStatus = customerImageBusinessDelegate.deleteCustomerImage(customerImageDTO,
					HelperMethods.getHeaders(dcRequest));

		} catch (Exception e) {
			alert.prepareError(
					"Exception while associating a business delegate call to CustomerImageBusinessDelegate to delete customer image"
							+ e.getMessage()).log();
			ErrorCodeEnum.ERR_10196.setErrorCode(result);
			return result;
		}

		if (customerImageDeleteStatus) {
			result.addParam(new Param("success", "success"));
		} else {
			ErrorCodeEnum.ERR_10196.setErrorCode(result);
		}
		return result;

	}

	@Override
	public Result updateCustomerImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result getCustomerImageResult = getCustomerImage(methodID, inputArray, dcRequest, dcResponse);

		if (!(getCustomerImageResult.getNameOfAllParams().contains(DBPUtilitiesConstants.USER_IMAGE))) {
			ErrorCodeEnum.ERR_10194.setErrorCode(result);
			return result;
		}

		boolean updateCustomerImageStatus = false;

		if (inputParams.containsKey(DBPUtilitiesConstants.USER_IMAGE)
				&& StringUtils.isNotBlank(inputParams.get(DBPUtilitiesConstants.USER_IMAGE))) {

			String image = inputParams.get(DBPUtilitiesConstants.USER_IMAGE);

			try {
				byte[] decoded = Base64.getDecoder().decode(image);
				MimeTypeValidator mimeTypeValidator = new MimeTypeValidator(decoded);
				updateCustomerImageStatus = mimeTypeValidator.hasValidImageMimeType();
			} catch (Exception e) {
				alert.prepareError("Exception occured while validating the input user image" + e.getMessage()).log();
				ErrorCodeEnum.ERR_10194.setErrorCode(result);
				return result;
			}
		}
		if (!updateCustomerImageStatus) {
			ErrorCodeEnum.ERR_10194.setErrorCode(result);
			return result;
		}

		CustomerImageDTO customerImageDTO = new CustomerImageDTO();
		customerImageDTO.setUserImage(inputParams.get(DBPUtilitiesConstants.USER_IMAGE));
		if (StringUtils.isBlank(getCustomerImageResult.getParamValueByName(DBPUtilitiesConstants.USER_IMAGE))) {
			CustomerImageBusinessDelegate customerImageBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CustomerImageBusinessDelegate.class);
			updateCustomerImageStatus = customerImageBusinessDelegate.createCustomerImage(customerImageDTO,
					HelperMethods.getHeaders(dcRequest));
		} else {
			CustomerImageBusinessDelegate customerImageBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(CustomerImageBusinessDelegate.class);
			updateCustomerImageStatus = customerImageBusinessDelegate.updateCustomerImage(customerImageDTO,
					HelperMethods.getHeaders(dcRequest));
		}
		if (updateCustomerImageStatus) {
			result.addParam(new Param("success", "success"));
		} else {
			ErrorCodeEnum.ERR_10194.setErrorCode(result);
		}
		return result;
	}

}
