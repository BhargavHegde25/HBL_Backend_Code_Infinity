package com.temenos.infinity.api.stoppayments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeSupplementsPostProcessor extends BasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    String sortKey = null;

    @Override 
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        
        Object body = result.getParamByName(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENTS_RESULT) != null ? result.getParamByName(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENTS_RESULT).getObjectValue() : null;
        String ErrMsg = result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) != ""
                ? result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) : "";

        if (body == null && StringUtils.isEmpty(ErrMsg)) {
            alert.prepareError("Cheque Supplements empty return result " + result.getAllParams()).log();
            return TemenosUtils.getEmptyResult(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENTS_RESULT);
        }

        if (body != null) {
            JSONArray chequeSupplements = new JSONArray(body.toString());
            List<Record> chequeSupplementsFinals = new ArrayList<Record>();

            for (int i = 0; i < chequeSupplements.length(); i++) {
                Record product = new Record();
                JSONObject cheque = new JSONObject();
                // Set Amount field
                Double amount = 0.0;
                cheque = (JSONObject) chequeSupplements.get(i);
                if (cheque.has(StopPaymentConstants.PARAM_CHEQUE_AMOUNT)) {
                    Object chequeAmount = cheque.get(StopPaymentConstants.PARAM_CHEQUE_AMOUNT);
                    if (chequeAmount != null) {
                        amount = StopCheckPaymentUtils.convertAmountToDouble(chequeAmount);
                    }
                } else {
                    if (cheque.has(StopPaymentConstants.PARAM_CHEQUE_AMOUNT_FROM)) {
                        Object chequeAmountFrom = cheque.get(StopPaymentConstants.PARAM_CHEQUE_AMOUNT_FROM);
                        if (chequeAmountFrom != null) {
                            amount = StopCheckPaymentUtils.convertAmountToDouble(chequeAmountFrom);
                        }
                    }
                }
                product.addStringParam(StopPaymentConstants.PARAM_CHEQUE_AMOUNT, Double.toString(amount));
                // Set Cheque Number
                if (cheque.has(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_NUMBER)) {
                    product.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_NUMBER,
                            cheque.getString(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_CHEQUE_NUMBER));
                }
                if (cheque.has(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_TYPE)) {
                    product.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_TYPE,
                            cheque.getString(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_TYPE));
                }
                if (cheque.has(StopPaymentConstants.PARAM_REFERENCE_NUMBER)) {
                    product.addStringParam(StopPaymentConstants.PARAM_REFERENCE_NUMBER,
                            cheque.getString(StopPaymentConstants.PARAM_REFERENCE_NUMBER));
                }
                if (cheque.has(StopPaymentConstants.PARAM_DATE)) {
                    product.addStringParam(StopPaymentConstants.PARAM_DATE,
                            cheque.getString(StopPaymentConstants.PARAM_DATE));
                }
                if (cheque.has(StopPaymentConstants.PARAM_CHEQUE_STATUS)) {
                    product.addStringParam(StopPaymentConstants.PARAM_CHEQUE_STATUS,
                            cheque.getString(StopPaymentConstants.PARAM_CHEQUE_STATUS));
                }
                if (cheque.has(StopPaymentConstants.PARAM_REMARKS)) {
                    JSONArray remarks = (JSONArray) cheque.get(StopPaymentConstants.PARAM_REMARKS);
                    if (remarks.getJSONObject(0).has(StopPaymentConstants.PARAM_REMARK)) {
                        String remark = remarks.getJSONObject(0).getString(StopPaymentConstants.PARAM_REMARK);
                        product.addStringParam(StopPaymentConstants.PARAM_REMARK, remark);
                    }
                }
                // Set Payee Names
                Record payee = new Record();
                Dataset payeeNamesDataset = new Dataset();
                String payeeName = StringUtils.EMPTY;
                payeeNamesDataset.setId(StopPaymentConstants.PARAM_PAYEE_NAMES);

                if (cheque.has(StopPaymentConstants.PARAM_BENEFICIARIES)) {
                    JSONArray beneficiaries = (JSONArray) cheque.get(StopPaymentConstants.PARAM_BENEFICIARIES);
                    if (beneficiaries.getJSONObject(0).has(StopPaymentConstants.PARAM_BENEFICIARY_ID)) {
                        payeeName = beneficiaries.getJSONObject(0).getString(StopPaymentConstants.PARAM_BENEFICIARY_ID);

                    }
                } else {
                    if (cheque.has(StopPaymentConstants.PARAM_PAYEE_NAMES)) {
                        JSONArray payeeNames = (JSONArray) cheque.get(StopPaymentConstants.PARAM_PAYEE_NAMES);
                        if (payeeNames.getJSONObject(0).has(StopPaymentConstants.PARAM_PAYEE_NAME)) {
                            payeeName = payeeNames.getJSONObject(0).getString(StopPaymentConstants.PARAM_PAYEE_NAME);

                        }
                    }
                }
                if (StringUtils.isNotEmpty(payeeName)) {
                    payee.addStringParam(StopPaymentConstants.PARAM_PAYEE_NAME, payeeName);
                    payeeNamesDataset.addRecord(payee);
                }
                product.addDataset(payeeNamesDataset);
                // Filter with amount query
                String amountSearch = request.getParameter(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_AMOUNT);
                String amountRange = request.getParameter(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_AMOUNT_RANGE);
                String payeeNameQuery = request.getParameter(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_PAYEE_NAME);
                if (StringUtils.isNotBlank(amountSearch)) {
                    if (amount == Double.parseDouble(amountSearch)) {
                        if (StringUtils.isNotBlank(amountRange)) {
                            Double amountStart = Double.parseDouble(amountRange.split("-")[0]);
                            Double amountEnd = Double.parseDouble(amountRange.split("-")[1]);

                            if (amount >= amountStart && amount <= amountEnd) {
                                if (StringUtils.isNotBlank(payeeNameQuery)) {
                                    if (payeeName.contains(payeeNameQuery)) {
                                        chequeSupplementsFinals.add(product);
                                    }
                                } else {
                                    chequeSupplementsFinals.add(product);
                                }
                            }
                        } else {
                            if (StringUtils.isNotBlank(payeeNameQuery)) {
                                if (payeeName.contains(payeeNameQuery)) {
                                    chequeSupplementsFinals.add(product);
                                }
                            } else {
                                chequeSupplementsFinals.add(product);
                            }
                        }
                    }
                } else if (StringUtils.isNotBlank(amountRange)) {
                    Double amountStart = Double.parseDouble(amountRange.split("-")[0]);
                    Double amountEnd = Double.parseDouble(amountRange.split("-")[1]);

                    if (amount >= amountStart && amount <= amountEnd) {
                        if (StringUtils.isNotBlank(payeeNameQuery)) {
                            if (payeeName.contains(payeeNameQuery)) {
                                chequeSupplementsFinals.add(product);
                            }
                        } else {
                            chequeSupplementsFinals.add(product);
                        }
                    }
                } else if (StringUtils.isNotBlank(payeeNameQuery)) {
                    if (payeeName.contains(payeeNameQuery)) {
                        chequeSupplementsFinals.add(product);
                    }
                } else {
                    chequeSupplementsFinals.add(product);
                }

            }
            Result FinalResult = new Result();
            Dataset dataset = new Dataset();
            dataset.setId(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENTS_RESULT);
            dataset.addAllRecords(chequeSupplementsFinals);
            FinalResult.addDataset(dataset);
            result = FinalResult;
        }
        return result;
    }
}