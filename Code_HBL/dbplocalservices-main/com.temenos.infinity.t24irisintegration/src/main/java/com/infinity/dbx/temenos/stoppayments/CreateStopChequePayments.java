package com.infinity.dbx.temenos.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.TemenosBaseService;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class CreateStopChequePayments extends TemenosBaseService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        try {
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];

            if (params == null) {
                CommonUtils.setErrMsg(result, "No input parameters provided");
                CommonUtils.setOpStatusError(result);
                return result;
            }

            String paymentModule = CommonUtils.getServerEnvironmentProperty(StopPaymentConstants.STOP_PAYMENT_MODULE,
                    request);
            String operationName = null;
            HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
            String serviceName = StopPaymentConstants.T24_SERVICE_NAME_STOP_PAYMENTS;
            if (StringUtils.isNotBlank(paymentModule)
                    && StopPaymentConstants.TRANSACTION_STOP_MODULE.equalsIgnoreCase(paymentModule)) {
                operationName = StopPaymentConstants.TRANSACTION_STOP;
                result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
                        true);
            } else {
                if (StringUtils.isNotBlank(paymentModule)
                        && StopPaymentConstants.PAYMENT_STOP_MODULE.equalsIgnoreCase(paymentModule)) {
                    operationName = StopPaymentConstants.PAYMENT_STOP;
                    result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
                            operationName, true);

                    // Calculate Fee for the cheque
					try {
						JsonParser jsonParser = new JsonParser();
						String stops = null;
						try {
							String resultString = ResultToJSON.convert(result);

							JsonObject resultObj = jsonParser.parse(resultString).getAsJsonObject();
                            if (resultObj.has(StopPaymentConstants.PARAM_STOPS)
                                    && resultObj.getAsJsonArray(StopPaymentConstants.PARAM_STOPS).size() > 0) {
                                JsonElement stopsElement = resultObj.getAsJsonArray(StopPaymentConstants.PARAM_STOPS)
                                        .get(0);
                                stops = stopsElement.toString();
                            }
						} catch (Exception e) {
	                        alert.prepareError("Could not parse stops object from response" + e +" "+ result.getParamValueByName(StopPaymentConstants.PARAM_STOPS)).log();
	                        Result errorResult = new Result();
	                        CommonUtils.setOpStatusError(result);
	                        CommonUtils.setErrMsg(errorResult, "Could not parse response from backend");
	                        return errorResult;
						}
						String status = result.getParamValueByName(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS);
						String errMessage = result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE);
						String errCode = result.getParamValueByName(StopPaymentConstants.PARAM_ERR_CODE);

						// Process Error Message
						if ((StringUtils.isNotEmpty(status)
								&& StopPaymentConstants.STATUS_FAILED.equalsIgnoreCase(status))
								|| StringUtils.isNotBlank(errMessage)) {
							alert.prepareError("T24 Error Message : " + errMessage).log();
							Result res = new Result();
                            CommonUtils.setErrCode(res, errCode);
                            CommonUtils.setErrMsg(res, errMessage); 
                            return res;
                        }

                        if (stops != null) {
                            JsonObject stopsObject = jsonParser.parse(stops).getAsJsonObject();
                            if (stopsObject.has(StopPaymentConstants.PARAM_CHARGES)) {
                                JsonArray chargeDetails = stopsObject.getAsJsonArray(StopPaymentConstants.PARAM_CHARGES);
                                Double Amount = 0.0;
                                for (int i = 0; i < chargeDetails.size(); i++) {
                                    JsonElement chargeElement = chargeDetails.get(i);
                                    JsonObject chargeObject = chargeElement.getAsJsonObject();
                                    if (chargeObject.has(StopPaymentConstants.PARAM_CHARGE_AMOUNT)) { 
                                        JsonElement chargeAmountElement = chargeObject.get(StopPaymentConstants.PARAM_CHARGE_AMOUNT);
                                        String chargeAmount = chargeAmountElement.getAsString();
                                        Amount = Amount + Double.parseDouble(chargeAmount);
                                    }
                                } 
                                result.addStringParam(StopPaymentConstants.PARAM_FEE, Double.toString(Amount));
                            }
                        }
                    } catch (Exception e) {
                        Result errorResult = new Result();
                        alert.prepareError("Exception Occured while processing the fee:" + e).log();
                        CommonUtils.setOpStatusError(result);
                        CommonUtils.setErrMsg(errorResult, "Parsing Response from Backend Failed");
                        return errorResult;
                    }
                }
            }
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while creating stop cheque payment:" + e).log();
            CommonUtils.setOpStatusError(result);
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }
}