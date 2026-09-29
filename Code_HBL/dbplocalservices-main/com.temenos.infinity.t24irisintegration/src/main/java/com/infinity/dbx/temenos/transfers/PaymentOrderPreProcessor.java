package com.infinity.dbx.temenos.transfers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.utils.MultiValueUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class PaymentOrderPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String PARKING_ACCOUNT_TRANSFER = "PARKING_ACCOUNT_TRANSFER";

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			super.execute(params, request, response, result);
			String serviceName = CommonUtils.getParamValue(params, TransferConstants.SERVICE_NAME);
			String paymentType=CommonUtils.getParamValue(params, "paymentType");
			alert.prepareError("paymentType in PaymentOrderPreProcessor:"+paymentType).log();
			if(StringUtils.isBlank(paymentType)) {
			if (serviceName.equalsIgnoreCase("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE"))
				paymentType="OWN_ACCOUNT_TRANSFER";
			if(serviceName.equalsIgnoreCase("INTRA_BANK_FUND_TRANSFER_CREATE"))
				paymentType="INTRA_BANK_TRANSFER";
			if(serviceName.equalsIgnoreCase("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE"))
				paymentType="INTER_BANK_TRANSFER";
			}
			params.put("paymentType",paymentType);
			String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
			if (StringUtils.isNotBlank(transactionType)
					&& !Constants.TRANSCTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)
					&& !Constants.TRANSCTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)
					&& !PARKING_ACCOUNT_TRANSFER.equalsIgnoreCase(transactionType)) {
				CommonUtils.setOpStatusOk(result);
				alert.prepareError("Please provide valid transfer type").log();
				return false;
			}
			TransferUtils.setPaymentDetails(params, request);
			String ScheduledDate = CommonUtils.getParamValue(params, Constants.PARAM_SCHEDULED_DATE);
			if (StringUtils.isNotBlank(ScheduledDate)) {
				params.put(TransferConstants.PARAM_EXECUTION_DATE, CommonUtils.convertDateToYYYYMMDD(ScheduledDate));
			}
			String paymentContainer = EnvironmentConfigurationsHandler.getValue(T24_PAYMENT_CONTAINER, request);
			if (StringUtils.isNotBlank(paymentContainer) && Constants.TRUE.equalsIgnoreCase(paymentContainer)) {
				params.put(TransferConstants.PARAM_PAYMENT_CONTAINER, paymentContainer);
			}

			String transactionNotes = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_NOTES);
			if (StringUtils.isNotBlank(transactionNotes)) {
				if (transactionNotes.length() < Constants.PARAM_INST_ID_REFERENCE_LEN)
					params.put(Constants.PARAM_INST_ID_REFERENCE, transactionNotes);
				else
					params.put(Constants.PARAM_INST_ID_REFERENCE,
							transactionNotes.substring(0, Constants.PARAM_INST_ID_REFERENCE_LEN));

			}

			String creditValueDate = CommonUtils.getParamValue(params, Constants.CREDIT_VALUE_DATE);
            if (StringUtils.isNotBlank(creditValueDate)) {
                params.put(Constants.CREDIT_VALUE_DATE, CommonUtils.convertDateToYYYYMMDD(creditValueDate));
            }
            
            String streetName = CommonUtils.getParamValue(params, TransferConstants.PARAM_STREET_NAME);
            if (StringUtils.isNotBlank(streetName)) {
            	String swiftAddr = MultiValueUtil.splitMultiValue(TransferConstants.PARAM_PO_SWIFT_ADDRESS,
                        TransferConstants.PARAM_PO_SWIFT_ADDRESS_LENGTH, streetName);
                params.remove(TransferConstants.PARAM_STREET_NAME);
                params.put(TransferConstants.PARAM_STREET_NAME, swiftAddr);
                alert.prepareError("streetName : " + swiftAddr).log(); 
            }
            String beneficiaryAddrLine1 = CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_ADDR_LINE1);
            String beneficiaryAddrLine2 = CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_ADDR_LINE2);
            String beneficiaryCity = CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_CITY);
            String beneficiaryZipCode = CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_ZIP_CODE);
            String beneficiaryCountry = CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_COUNTRY);
            String beneficiaryAddress = "";
            JsonArray beneficiaryAddrArr = new JsonArray();
            String property = "beneficiaryAddress";
            if (StringUtils.isNotBlank(beneficiaryAddrLine1)) {
            	JsonObject beneficiaryAddrObj = new JsonObject();
            	beneficiaryAddrObj.addProperty(property, beneficiaryAddrLine1);
            	beneficiaryAddrArr.add(beneficiaryAddrObj);
            }
            if(StringUtils.isNotBlank(beneficiaryAddrLine2)) {
            	JsonObject beneficiaryAddrObj = new JsonObject();
            	beneficiaryAddrObj.addProperty(property, beneficiaryAddrLine2);
            	beneficiaryAddrArr.add(beneficiaryAddrObj);
            }
            if(StringUtils.isNotBlank(beneficiaryCity) || StringUtils.isNotBlank(beneficiaryZipCode)) {
            	JsonObject beneficiaryAddrObj = new JsonObject();
            	if(StringUtils.isNotBlank(beneficiaryCity) && StringUtils.isBlank(beneficiaryZipCode))
            		beneficiaryAddrObj.addProperty(property, beneficiaryCity);
            	if(StringUtils.isBlank(beneficiaryCity) && StringUtils.isNotBlank(beneficiaryZipCode))
            		beneficiaryAddrObj.addProperty(property, beneficiaryZipCode);
            	if(StringUtils.isNotBlank(beneficiaryCity) && StringUtils.isNotBlank(beneficiaryZipCode)) {
            		beneficiaryCity = beneficiaryCity.concat(",").concat(beneficiaryZipCode);
            		beneficiaryAddrObj.addProperty(property, beneficiaryCity);
            	}
            	beneficiaryAddrArr.add(beneficiaryAddrObj);
            }
            if(StringUtils.isNotBlank(beneficiaryCountry)) {
            	JsonObject beneficiaryAddrObj = new JsonObject();
            	beneficiaryAddrObj.addProperty(property, beneficiaryCountry);
            	beneficiaryAddrArr.add(beneficiaryAddrObj);
            }
            if(!beneficiaryAddrArr.isEmpty())
            	beneficiaryAddress = beneficiaryAddrArr.toString();
            if(StringUtils.isNotBlank(beneficiaryAddress)) {
            	params.remove(TransferConstants.PARAM_BENEFICIARY_ADDR_LINE1);
            	params.put(TransferConstants.PARAM_BENEFICIARY_ADDR_LINE1, beneficiaryAddress);
            }
            alert.prepareError("beneficiaryAddress " + beneficiaryAddress).log();
			// Process Charges
			String charges = CommonUtils.getParamValue(params, TransferConstants.PARAM_CHARGES);
			alert.prepareError("Charges Array + " + charges).log();
			if (StringUtils.isNotBlank(charges)) {
				JSONArray finalChargesArray = new JSONArray();

				try {
					JSONArray chargesArray = new JSONArray(charges);
					if (chargesArray.length() > 0) {
						for (Object item : chargesArray) {
							if ((item instanceof JSONObject)) {
								JSONObject chargeObject = new JSONObject(item.toString());
								if (chargeObject.has(TransferConstants.PARAM_CHARGE_ACC_CCY_ID)
										&& chargeObject.has(TransferConstants.PARAM_CHARGE_ACC_CCY_AMOUNT)) {
									continue;
								}
								if (chargeObject.has(TransferConstants.PARAM_CHARGE_ACC_CCY_ID)) {
									chargeObject.remove(TransferConstants.PARAM_CHARGE_ACC_CCY_ID);
								}
								if (chargeObject.has(TransferConstants.PARAM_CHARGE_ACC_CCY_AMOUNT)) {
									chargeObject.remove(TransferConstants.PARAM_CHARGE_ACC_CCY_AMOUNT);
								}
								finalChargesArray.put(chargeObject);
							}
						}
					}
				} catch (Exception e) {
					alert.prepareError("Error while processing charges : " + charges + e).log();
				}

				params.remove(TransferConstants.PARAM_CHARGES);
				params.put(TransferConstants.PARAM_CHARGES, finalChargesArray.toString());
			}
			params.remove(TemenosConstants.USER_ID);
			request.getHeaderMap().put("companyId", "");

		} catch (Exception e) {
			alert.prepareError("Exception occurred in One Time Transfer PreProcesor:" + e).log();
			return false;
		}

		return Boolean.TRUE;
	}

}
