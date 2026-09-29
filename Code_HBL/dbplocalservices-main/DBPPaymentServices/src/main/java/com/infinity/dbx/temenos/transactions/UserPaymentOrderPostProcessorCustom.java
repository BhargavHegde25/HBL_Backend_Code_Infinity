package com.infinity.dbx.temenos.transactions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * <p>
 * Post-Processor class for User Payment Orders
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class UserPaymentOrderPostProcessorCustom extends BasePostProcessor {
	

	private static final String TRANSACTIONS_ARRAY_KEY = "Transactions";
	private static final String CONFIGURATIONS = "configurations";

	private static Map<String, String> PAYMENT_ORDER_PRODUCT_MAP = new HashMap<>();
	private static Map<String, String> PAYMENT_ORDER_SERVICE_NAME_MAP = new HashMap<>();
	private static Map<String, String> CHARGE_BEARER_MAP = new HashMap<>();
	
	
	
	static {
		CHARGE_BEARER_MAP.put(TransactionConstants.SHA_PAID_BY, TransactionConstants.PAID_BY_BOTH);
		CHARGE_BEARER_MAP.put(TransactionConstants.BEN_PAID_BY, TransactionConstants.PAID_BY_BENEFICIARY);
		CHARGE_BEARER_MAP.put(TransactionConstants.OUR_PAID_BY, TransactionConstants.PAID_BY_SELF);
		
		PAYMENT_ORDER_PRODUCT_MAP.put(TransactionConstants.INATIONAL_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		PAYMENT_ORDER_SERVICE_NAME_MAP.put(TransactionConstants.INATIONAL_PRODUCT_ID,
				TransactionConstants.INATIONAL_PRODUCT_SERVICE_NAME);
		
		PAYMENT_ORDER_PRODUCT_MAP.put(TransactionConstants.DOMESTIC_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		PAYMENT_ORDER_PRODUCT_MAP.put(TransactionConstants.DOMESTIC_PRODUCT_ID_ACOTHER,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		PAYMENT_ORDER_SERVICE_NAME_MAP.put(TransactionConstants.DOMESTIC_PRODUCT_ID,
				TransactionConstants.DOMESTIC_PRODUCT_SERVICE_NAME);
		PAYMENT_ORDER_SERVICE_NAME_MAP.put(TransactionConstants.DOMESTIC_PRODUCT_ID_ACOTHER,
				TransactionConstants.ACOTHER_PRODUCT_SERVICE_NAME);
		
		PAYMENT_ORDER_PRODUCT_MAP.put(TransactionConstants.INSTA_PAY_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		PAYMENT_ORDER_SERVICE_NAME_MAP.put(TransactionConstants.INSTA_PAY_PRODUCT_ID,
				TransactionConstants.INSTA_PAY_PRODUCT_SERVICE_NAME);
		PAYMENT_ORDER_PRODUCT_MAP.put(TransactionConstants.SEPA_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		PAYMENT_ORDER_SERVICE_NAME_MAP.put(TransactionConstants.SEPA_PRODUCT_ID,
				TransactionConstants.INSTA_PAY_PRODUCT_SERVICE_NAME);
		
		PAYMENT_ORDER_PRODUCT_MAP.put(TransactionConstants.ACTRF_PRODUCT_ID,
				TransactionConstants.INTERNAL_TRANSFER_PRODUCT_ID);
		PAYMENT_ORDER_SERVICE_NAME_MAP.put(TransactionConstants.ACTRF_PRODUCT_ID,
				TransactionConstants.ACTRF_PRODUCT_SERVICE_NAME);
		
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("In " + UserPaymentOrderPostProcessorCustom.class.getName()).log();

			// Convert Result to JSON
            // Load Payment Status from C360 Bundle Configurations
            JSONObject bundleConfig = TemenosUtils.getBundleConfigurations(TransactionConstants.DBP_BUNDLE,
                    TransactionConstants.DBP_CONFIG_KEY, request);
            String paymentStatusOptions = "";
            if (bundleConfig != null) {
                JSONArray configurations = bundleConfig.optJSONArray(CONFIGURATIONS);
                if (configurations != null && configurations.length() > 0) {
                    JSONObject paymentStatus = configurations.optJSONObject(0);
                    if (paymentStatus.has(TransactionConstants.DBP_CONFIG_TABLE_VALUE)) {
                        paymentStatusOptions = paymentStatus.getString(TransactionConstants.DBP_CONFIG_TABLE_VALUE);
                    }
                }
            }
            if (StringUtils.isBlank(paymentStatusOptions)) {
                alert.prepareError("Unable to get payment status configurations").log();
            }
            JSONObject PAYMENT_STATUS_MAP = new JSONObject(paymentStatusOptions);
			String serviceResponse = ResultToJSON.convert(result);
			JSONObject serviceResponseJSON = new JSONObject(serviceResponse);

			String currentpaymentOrderProductId;
			JSONObject currentPaymentOrderJSON, currentDebitJSON, currentCreditJSON, currentBeneficiaryJSON,
					currentNarrativesJSON;
			JSONArray currentDebitsArray, currentCreditsArray, currentBeneficiariesArray, currentNarrativesArray;
			JSONArray paymentOrdersArray = serviceResponseJSON.optJSONArray(TRANSACTIONS_ARRAY_KEY);

			List<String> narratives = new ArrayList<>();
			JSONObject additionalInfo=null;
			Map<String, ArrayList<JSONObject>> paymentOrdersMap = new HashMap<>(); 
			// Traverse Payment Orders
			if (paymentOrdersArray == null) {
				return TemenosUtils.getEmptyResult(TRANSACTIONS_ARRAY_KEY);
			}
			for (Object currObject : paymentOrdersArray) {
				if (currObject instanceof JSONObject) {
					currentPaymentOrderJSON = (JSONObject) currObject;
					String paymentType=currentPaymentOrderJSON.optString("structuredIssuer");
					 additionalInfo=getAdditionalInformation(currentPaymentOrderJSON);
					if(StringUtils.isNotBlank(paymentType)) {
						if(paymentType.equalsIgnoreCase("INTRA_BANK_TRANSFER")) {
							paymentType="INTRA_BANK";
						}
						else if(paymentType.equalsIgnoreCase("OWN_ACCOUNT_TRANSFER")) {
							paymentType="OWN_ACCOUNT";
						}
					 currentPaymentOrderJSON.put("paymentType", paymentType);
					}
					// Handle Debits
					currentDebitsArray = currentPaymentOrderJSON
							.optJSONArray(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_KEY);
					if (currentDebitsArray != null && currentDebitsArray.length() > 0) {
						currentDebitJSON = currentDebitsArray.optJSONObject(0);
						if (currentDebitJSON != null) {
							currentPaymentOrderJSON.put(TransactionConstants.FROM_ACCOUNT_IBAN_KEY, currentDebitJSON
									.optString(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_ACCOUNT_IBAN_KEY));
							currentPaymentOrderJSON.put(TransactionConstants.FROM_ACCOUNT_CURRENCY_KEY, currentDebitJSON
									.optString(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_CURRENCY_KEY));
							currentPaymentOrderJSON.put(TransactionConstants.FROM_ACCOUNT_NUMBER_KEY, currentDebitJSON
									.optString(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_ACCOUNT_ID_KEY));
							currentPaymentOrderJSON.put(TransactionConstants.FROM_ACCOUNT_NAME_KEY, currentDebitJSON
									.optString(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_ACCOUNT_NAME_KEY));
							
							String totalDebitAmountVal=currentDebitJSON.optString("totalDebitAmount");
							if(StringUtils.isNotBlank(totalDebitAmountVal)) {
					        BigDecimal totalDebitAmount = new BigDecimal(totalDebitAmountVal).setScale(2);
					        alert.prepareError("HBL:UserPaymentOrderPostProcessorCustom:totalDebitAmount:"+totalDebitAmount).log();
							currentPaymentOrderJSON.put(TransactionConstants.PAYMENT_ORDER_TOTAL_DEBIT_KEY, totalDebitAmount.toString());
							}else {
								String amountVal=currentPaymentOrderJSON.optString("amount");
								BigDecimal amount = new BigDecimal(amountVal).setScale(2);
								 alert.prepareError("HBL:UserPaymentOrderPostProcessorCustom:amount:"+amount).log();
								currentPaymentOrderJSON.put(TransactionConstants.PAYMENT_ORDER_TOTAL_DEBIT_KEY, amount.toString());
							}
							
							currentPaymentOrderJSON.put(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_CURRENCY_KEY, currentDebitJSON
                                    .optString(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_CURRENCY_KEY));
							if(currentDebitJSON.optString(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_CURRENCY_KEY).equalsIgnoreCase("USD") && currentPaymentOrderJSON.optString(TransactionConstants.CURRENCY_ID).equalsIgnoreCase("NPR")) {
								currentPaymentOrderJSON.put(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_CURRENCY_KEY, currentPaymentOrderJSON.optString(TransactionConstants.CURRENCY_ID));
								}
						}
						currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_DEBITS_ARRAY_KEY);
					}

					// Handle Credits
					currentCreditsArray = currentPaymentOrderJSON
							.optJSONArray(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_KEY);
					if (currentCreditsArray != null && currentCreditsArray.length() > 0) {
						currentCreditJSON = currentCreditsArray.optJSONObject(0);
						if (currentCreditJSON != null) {
							currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_IBAN_KEY, currentCreditJSON
									.optString(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_IBAN_KEY));
							currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY, currentCreditJSON
									.optString(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_ID_KEY));
							
							String toAccountNumber = currentCreditJSON
									.optString(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_ID_KEY);
							alert.prepareError("toAccountNumber ###"+ toAccountNumber).log();
							String eSewaPaybleAccount = EnvironmentConfigurationsHandler.getServerProperty("ESEWA_TOPUP_PAYABLE_ACCOUNT");
							alert.prepareError("eSewaPaybleAccount ###"+ eSewaPaybleAccount).log();
							String creditCardPaybleAccount = EnvironmentConfigurationsHandler.getServerProperty("S2M_CARD_PAYMENT_PAYABLE_ACCNOUNT_NO");
							alert.prepareError("creditCardPaybleAccount ###"+ creditCardPaybleAccount).log();
							
							if (toAccountNumber.equalsIgnoreCase(eSewaPaybleAccount)) {
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NAME_KEY, "eSewa Load");
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY, "");
							} else if (toAccountNumber.equalsIgnoreCase(creditCardPaybleAccount)) {
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NAME_KEY, "Card Payment");
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY, "");
							}else {
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY, currentCreditJSON
										.optString(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_ID_KEY));
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NAME_KEY, currentCreditJSON
										.optString(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_ACCOUNT_NAME_KEY));
							}
							
							currentPaymentOrderJSON.put(TransactionConstants.CREDIT_VALUE_DATE, currentCreditJSON
                                    .optString(TransactionConstants.CREDIT_VALUE_DATE)); 
							
							if (StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("CIPS")) {
								if (additionalInfo != null) {
									currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY,
											additionalInfo.optString("creditorAccount"));
									currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NAME_KEY,
											additionalInfo.optString("creditorName"));
								}
							}
							
						}
						currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_KEY);
					}

					// Handle Beneficiaries
					currentBeneficiariesArray = currentPaymentOrderJSON
							.optJSONArray(TransactionConstants.PAYMENT_ORDER_BENEFICIARIES_ARRAY_KEY);
					if (currentBeneficiariesArray != null && currentBeneficiariesArray.length() > 0) {
						currentBeneficiaryJSON = currentBeneficiariesArray.optJSONObject(0);
						if (currentBeneficiaryJSON != null) {
							if (currentBeneficiaryJSON.has(TransactionConstants.BENEFICIARY_BIC_KEY)) {
								currentPaymentOrderJSON.put(TransactionConstants.ACCOUNT_SWIFTCODE_KEY,
										currentBeneficiaryJSON.optString(TransactionConstants.BENEFICIARY_BIC_KEY));
							}
							if (currentBeneficiaryJSON.has(TransactionConstants.BENEFICIARY_ACCOUNT_ID_KEY)) {
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY,
										currentBeneficiaryJSON
												.optString(TransactionConstants.BENEFICIARY_ACCOUNT_ID_KEY));
								currentPaymentOrderJSON.put(TransactionConstants.EXTERNAL_ACCOUNT_NUMBER_KEY,
										currentBeneficiaryJSON
												.optString(TransactionConstants.BENEFICIARY_ACCOUNT_ID_KEY));
							}
							if (currentBeneficiaryJSON.has(TransactionConstants.BENEFICIARY_IBAN_KEY)) {
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NUMBER_KEY,
											currentBeneficiaryJSON
													.optString(TransactionConstants.BENEFICIARY_IBAN_KEY));
								
								
								currentPaymentOrderJSON.put(TransactionConstants.EXTERNAL_ACCOUNT_NUMBER_KEY,
										currentBeneficiaryJSON
												.optString(TransactionConstants.BENEFICIARY_ACCOUNT_ID_KEY));
							}
							if (currentBeneficiaryJSON.has(TransactionConstants.BENEFICIARY_NAME_KEY)) {
								currentPaymentOrderJSON.put(TransactionConstants.TO_ACCOUNT_NAME_KEY,
										currentBeneficiaryJSON.optString(TransactionConstants.BENEFICIARY_NAME_KEY));
							}
						}
						currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_BENEFICIARIES_ARRAY_KEY);
					}

					// Handle Narratives
					/*currentNarrativesArray = currentPaymentOrderJSON
							.optJSONArray(TransactionConstants.PAYMENT_ORDER_NARRATIVES_ARRAY_KEY);
					if (currentNarrativesArray != null && currentNarrativesArray.length() > 0) {
						narratives.clear();
						for (Object currNarrativeObject : currentNarrativesArray) {
							if (currNarrativeObject instanceof JSONObject) {
								currentNarrativesJSON = (JSONObject) currNarrativeObject;
								narratives.add(currentNarrativesJSON.optString(TransactionConstants.NARRATIVE_KEY));
							}
						}
						currentPaymentOrderJSON.put(TransactionConstants.DESCRIPTION_KEY, String.join(" ", narratives));
						currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_NARRATIVES_ARRAY_KEY);
					}*/ 
					
					// Handle RemittanceInformations - TransactionNotes or Description
                    JSONArray currentRemittanceArray = currentPaymentOrderJSON
                            .optJSONArray(TransactionConstants.PAYMENT_ORDER_REMITTANCE_ARRAY_KEY);
                    if (currentRemittanceArray != null && currentRemittanceArray.length() > 0) {
                        List<String> remittance = new ArrayList<>();
                        remittance.clear();
                        for (Object currRemittanceObject : currentRemittanceArray) {
                            if (currRemittanceObject instanceof JSONObject) {
                                JSONObject currentRemittanceJSON = (JSONObject) currRemittanceObject;
                                remittance.add(currentRemittanceJSON.optString(TransactionConstants.REMITTANCE_INFO_KEY));
                            }
                        }
                        currentPaymentOrderJSON.put(TransactionConstants.DESCRIPTION_KEY, String.join(" ", remittance));
                        currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_REMITTANCE_ARRAY_KEY);
                    }

					// Set Frequency Type
					currentPaymentOrderJSON.put(TransactionConstants.FREQUENCY_TYPE_KEY,
							TransactionConstants.FREQUENCY_TYPE_DEFAULT_VALUE);

					// Setting transaction Currency
					if (currentPaymentOrderJSON.has(TransactionConstants.CURRENCY_ID)) {
						currentPaymentOrderJSON.put(TransactionConstants.TRANSACTION_CURRECNY,
								currentPaymentOrderJSON.optString(TransactionConstants.CURRENCY_ID));
					}

					// Set Frequency End Date
					if (currentPaymentOrderJSON.has(TransactionConstants.EXECUTION_DATE_KEY)) {
						currentPaymentOrderJSON.put(TransactionConstants.SCHEDULED_DATE_KEY,
								currentPaymentOrderJSON.optString(TransactionConstants.EXECUTION_DATE_KEY));
						
						SimpleDateFormat dateFormatter = new SimpleDateFormat(TransactionConstants.TRANSACTION_DATE_FORMAT);
						Date executionDate = dateFormatter.parse(currentPaymentOrderJSON.optString(TransactionConstants.EXECUTION_DATE_KEY));
				        Date today = new Date();
				        if (today.compareTo(executionDate) > 0) {
				            currentPaymentOrderJSON.put(TransactionConstants.PARAM_IS_SCHEDULED,TransactionConstants.FALSE_SMALL);
				        }
				        else {
				        	currentPaymentOrderJSON.put(TransactionConstants.PARAM_IS_SCHEDULED,TransactionConstants.TRUE);
				        }
						currentPaymentOrderJSON.remove(TransactionConstants.EXECUTION_DATE_KEY);
					} 
					
					// Set Transaction ID
					if (currentPaymentOrderJSON.has(TransactionConstants.PAYMENT_ORDER_ID_KEY)) {
						currentPaymentOrderJSON.put(TransactionConstants.TRANSACTION_ID_KEY,
								currentPaymentOrderJSON.optString(TransactionConstants.PAYMENT_ORDER_ID_KEY));
						currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_ID_KEY);
					}
					
					if(currentPaymentOrderJSON.has(TransactionConstants.ACCOUNT_WITH_BANK_BIC)){
						currentPaymentOrderJSON.put(TransactionConstants.BIC_ID_KEY,
								currentPaymentOrderJSON.optString(TransactionConstants.ACCOUNT_WITH_BANK_BIC));
						currentPaymentOrderJSON.remove(TransactionConstants.ACCOUNT_WITH_BANK_BIC);
					}

					// Set Transaction Type
					if (currentPaymentOrderJSON.has(TransactionConstants.PAYMENT_ORDER_PRODUCT_ID_KEY)) {
						currentpaymentOrderProductId = currentPaymentOrderJSON
								.optString(TransactionConstants.PAYMENT_ORDER_PRODUCT_ID_KEY);
						
						if (PAYMENT_ORDER_PRODUCT_MAP.containsKey(currentpaymentOrderProductId) || PAYMENT_ORDER_SERVICE_NAME_MAP.containsKey(currentpaymentOrderProductId)) {
							currentPaymentOrderJSON.put(TransactionConstants.TRANSACTION_TYPE_KEY,
									PAYMENT_ORDER_PRODUCT_MAP.get(currentpaymentOrderProductId));
							currentPaymentOrderJSON.put(TransactionConstants.SERVICE_NAME,
									PAYMENT_ORDER_SERVICE_NAME_MAP.get(currentpaymentOrderProductId));
							if(currentpaymentOrderProductId.equalsIgnoreCase(TransactionConstants.INSTA_PAY_PRODUCT_ID)) {
								currentPaymentOrderJSON.put(TransactionConstants.PAYMENT_TYPE,
										TransactionConstants.INSTA_PAY_PRODUCT_ID);
							}
							else if(currentpaymentOrderProductId.equalsIgnoreCase(TransactionConstants.SEPA_PRODUCT_ID)) {
								currentPaymentOrderJSON.put(TransactionConstants.PAYMENT_TYPE,
										TransactionConstants.SEPA_PRODUCT_ID);
							}
							currentPaymentOrderJSON.remove(TransactionConstants.PAYMENT_ORDER_PRODUCT_ID_KEY);
						}
					}
					// Set CIPS Transaction Type
					if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("CIPS")){
						currentPaymentOrderJSON.put(TransactionConstants.TRANSACTION_TYPE_KEY,
								PAYMENT_ORDER_PRODUCT_MAP.get("DOMESTIC"));
						currentPaymentOrderJSON.put(TransactionConstants.SERVICE_NAME,
								PAYMENT_ORDER_SERVICE_NAME_MAP.get("DOMESTIC"));
					}
					
					if(currentPaymentOrderJSON.has(TransactionConstants.CHARGE_BEARER)) {
						currentPaymentOrderJSON.put(TransactionConstants.PAID_BY,
								CHARGE_BEARER_MAP.get(currentPaymentOrderJSON
										.optString(TransactionConstants.CHARGE_BEARER)));
						
					}
					JSONObject paymentStatus = new JSONObject(PAYMENT_STATUS_MAP.get(TransactionConstants.PAYMENT_STATUS).toString());
					JSONObject currentStatus = new JSONObject(PAYMENT_STATUS_MAP.get(TransactionConstants.CURRENT_STATUS).toString());
					if(currentPaymentOrderJSON.has(TransactionConstants.PAYMENT_STATUS)
							&& paymentStatus != null && paymentStatus.has(currentPaymentOrderJSON.optString(TransactionConstants.PAYMENT_STATUS))) {
								currentPaymentOrderJSON.put(TransactionConstants.STATUS_DESCRIPTION,
		                        		paymentStatus.get(currentPaymentOrderJSON
		                                        .optString(TransactionConstants.PAYMENT_STATUS)));
					}else if(currentPaymentOrderJSON.has(TransactionConstants.CURRENT_STATUS)
							&& currentStatus != null
							&& currentStatus.has(currentPaymentOrderJSON.optString(TransactionConstants.CURRENT_STATUS))){
								currentPaymentOrderJSON.put(TransactionConstants.STATUS_DESCRIPTION,
										currentStatus.get(currentPaymentOrderJSON
												.optString(TransactionConstants.CURRENT_STATUS)));
                    }else {
                    	currentPaymentOrderJSON.put(TransactionConstants.STATUS_DESCRIPTION,
								PAYMENT_STATUS_MAP.get(TransactionConstants.DEFAULT));
                    }
					if (!paymentOrdersMap.containsKey(currentPaymentOrderJSON.optString(TransactionConstants.TRANSACTION_ID_KEY)))  {
						paymentOrdersMap.put(currentPaymentOrderJSON.optString(TransactionConstants.TRANSACTION_ID_KEY), new ArrayList<JSONObject>());
					}	
					paymentOrdersMap.get(currentPaymentOrderJSON.optString(TransactionConstants.TRANSACTION_ID_KEY)).add(currentPaymentOrderJSON);
				}
			}
			serviceResponseJSON.remove(TRANSACTIONS_ARRAY_KEY);
			JSONArray mergedPaymentOrders = this.removeDuplicateAndGetPaymentOrdersList(paymentOrdersMap, additionalInfo);
			
			serviceResponseJSON.put(TRANSACTIONS_ARRAY_KEY, mergedPaymentOrders);

			// Convert JSON to Result
			result = JSONToResult.convert(serviceResponseJSON.toString());
			diagnostic.prepareDebug("Processed User Payment Order Final Response:"+serviceResponseJSON).log();

		} catch (Exception e) {
			alert.prepareError("Exception in " + UserPaymentOrderPostProcessorCustom.class.getName(), e).log();
			return TemenosUtils.getEmptyResult(TRANSACTIONS_ARRAY_KEY);
		}
		return result;
	}

	private JSONArray removeDuplicateAndGetPaymentOrdersList(Map<String, ArrayList<JSONObject>> paymentOrdersMap2, JSONObject additionalInfo) {
		JSONArray mergedPaymentOrders = new JSONArray();
		for (Map.Entry<String, ArrayList<JSONObject>> entry : paymentOrdersMap2.entrySet()) {
			ArrayList<JSONObject> entryValue = entry.getValue();
			if (entryValue.size() > 1 ) {
				mergedPaymentOrders.put(this.getMergedRecord(entryValue,additionalInfo));
			}
			else {
				JSONObject record = entryValue.get(0);
				String recordStatus = record.optString(TransactionConstants.RECORD_STATUS);
				String paymentType = record.optString("paymentType");
				if (recordStatus == null) {
					record.put(TransactionConstants.PENDING_APPROVAL, false);
				}
				else {
					record.put(TransactionConstants.PENDING_APPROVAL,recordStatus.equals(TransactionConstants.INAU) || recordStatus.equals(TransactionConstants.RNAU));
				}
				if(additionalInfo!=null && !additionalInfo.isEmpty()) {
                    	for(String key: additionalInfo.keySet()) {
                    		if(!key.equalsIgnoreCase("amount"))
                    		 record.put(key, additionalInfo.getString(key));
                    	}
				}
				if(!paymentType.equalsIgnoreCase("BILL_PAYMENT")) {
					mergedPaymentOrders.put(record);
				 }
				
			}
		}
		return mergedPaymentOrders;		
	}
	
	private JSONObject getMergedRecord(ArrayList<JSONObject> entryValue, JSONObject additionalInfo) {
		JSONObject mergedRecord = null;
		String recordStatus = "";
		for (JSONObject entry: entryValue) {
			String currentRecordStatus = entry.optString(TransactionConstants.RECORD_STATUS);
			if (currentRecordStatus == "" || currentRecordStatus == null) {
				mergedRecord = entry;
			}
			else  {
				recordStatus = currentRecordStatus;
			}
		}
		/*if(additionalInfo!=null && !additionalInfo.isEmpty()) {
        	for(String key: additionalInfo.keySet()) {
        		if(mergedRecord!=null)
        		mergedRecord.put(key, additionalInfo.getString(key));
        	}
		} 
		*/
		/*if(additionalInfo!=null) {
		mergedRecord.put("additionalInformation",  additionalInfo);
		mergedRecord.remove("additionalInformations");
		}
		*/
		mergedRecord.put(TransactionConstants.PENDING_APPROVAL, recordStatus.equals(TransactionConstants.INAU) || recordStatus.equals(TransactionConstants.RNAU));
		return mergedRecord;
	}
	public JSONObject getAdditionalInformation(JSONObject currentPaymentOrderJSON) {
		//alert.prepareError("GetTransfersOperation: GetTransfers: getAdditionalInformation:responseObj:" + currentPaymentOrderJSON).log();
		JSONObject jsonObj=null;
		try {
				if (currentPaymentOrderJSON.optJSONArray("additionalInformations") != null) {
				JSONArray additionalInformations = currentPaymentOrderJSON.optJSONArray("additionalInformations");
				//transRecord.removeDatasetById("additionalInformations");
				alert.prepareError("GetTransfersOperation: GetTransfers: additionalInformations:ary:" + additionalInformations).log();
				jsonObj=new JSONObject();
				for (int j = 0; j < additionalInformations.length(); j++) {
					JSONObject record = additionalInformations.optJSONObject(j);
					String additionalInformation = record.optString("additionalInformation");
					if (StringUtils.isNotBlank(additionalInformation)) {
						String[] keyValue = additionalInformation.split(":");
						String recordKey = "additionalInformation";
						String recordValue = additionalInformation;
						if (keyValue.length > 1) {
							recordKey = keyValue[0];
							recordValue = keyValue[1];
							jsonObj.put(recordKey, recordValue);
						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while invoking getAdditionalInformation: " + e.getLocalizedMessage()).log();;
		}
		alert.prepareError("GetTransfersOperation: GetTransfers: getAdditionalInformation:jsonObj:"+ jsonObj).log();
	return jsonObj;
	}
	
	public static JSONObject convertRecordToJSONObject(Record record) {
		JSONObject jsonObj=null;
		if(record.getAllParams().size()>0) {
			jsonObj = new JSONObject();
		List<Param> arList = record.getAllParams();

		Iterator<Param> it = arList.iterator();
		while (it.hasNext()) {
			Param p = it.next();
			String key = p.getName();
			jsonObj.put(key, p.getValue());
		}
		}
		return jsonObj;
	}
}