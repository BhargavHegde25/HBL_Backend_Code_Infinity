package com.bct.postprocessor;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.ERR_EMPTY_RESPONSE;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_VALUE_SUCCESSFUL;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANS_TYPE_OTHERS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CompletedTransPostProcesCustom extends BasePostProcessor{
	Logger logger = LogManager.getLogger(CompletedTransPostProcesCustom.class);
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            JSONArray charges = new JSONArray();
            JSONObject additionalInfoObj = null;
            String resultString = ResultToJSON.convert(result);
            JSONObject resultObj=new JSONObject(resultString);
           JSONArray transactionsArray =resultObj.getJSONArray(TRANSACTION);
            logger.debug("Completed Transactions:###"+ ResultToJSON.convert(result));
            if(transactionsArray!=null && transactionsArray.isEmpty()) {
            	logger.debug(ERR_EMPTY_RESPONSE);
                Result transactionResult = TemenosUtils.getEmptyResult(TRANSACTION);
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_PAGE_START_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_COMPLETED));
                }
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED));
                }
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED));
                }
                return transactionResult;
            }
            else {
            	 if (transactionsArray.length() != 0) {
            		 for(int i=0;i<transactionsArray.length();i++) {
            			 JSONObject record= transactionsArray.getJSONObject(i);
            			 logger.debug("CompletedTransPostProcesCustom :record:" +record);
                     	 record.put("transferType", "COMPLETED");
                         record.put(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_SUCCESSFUL);
                         String transactionType = record.optString(PARAM_TRANSACTION_TYPE);
                         JSONArray chargesSet = record.optJSONArray("charges");
                         String paymentType =  record.optString("structuredIssuer");
                         JSONArray additionalInfoAry = record.optJSONArray("additionalInformations");
                         JSONArray creditsArray = record.optJSONArray("credits");
                         JSONArray debitsArray = record.optJSONArray("debits");
                         String totalDebitAmount="";
                         String debitCurrency="";
                         logger.debug("CompletedTransPostProcesCustom :additionalInfoAry:" +additionalInfoAry);
                         if (chargesSet != null && chargesSet.length() > 0) {
 							logger.debug("charges charges1 response Array: ##" + charges);

 							JSONObject rootObj =  chargesSet.getJSONObject(0);
 							String chargeCurrencyId = (String) rootObj.get("chargeCurrencyId");
 							String chargeType = (String) rootObj.get("chargeType");
 							String chargeAmount = (String) rootObj.get("chargeAmount");
 							String chargeName = (String) rootObj.get("chargeName");

 							if (StringUtils.isNotBlank(chargeCurrencyId)) {
 								logger.debug("chargeCurrencyId: ##" + chargeCurrencyId);
 								record.put("chargeCurrencyId", chargeCurrencyId);
 							}

 							if (StringUtils.isNotBlank(chargeType)) {
 								logger.debug("chargeType: ##" + chargeType);
 								record.put("chargeType", chargeType);
 							}

 							if (StringUtils.isNotBlank(chargeAmount)) {
 								logger.debug("chargeAmount: ##" + chargeAmount);
 								record.put("chargeAmount", chargeAmount);
 							}

 							if (StringUtils.isNotBlank(chargeName)) {
 								logger.debug("chargeName: ##" + chargeName);
 								record.put("chargeName", chargeName);
 							}
 						}
                         if (transactionType != StringUtils.EMPTY) {
                             transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                             if (transactionType == null) {
                                 transactionType = TRANS_TYPE_OTHERS;
                             }
                         }
                         record.put(PARAM_TRANSACTION_TYPE, transactionType);
                         if(additionalInfoAry!=null  && additionalInfoAry.length() > 0) {
                             logger.debug("CompletedTransPostProcesCustom :additionalInfoAry:" +additionalInfoAry);
                             additionalInfoObj = getAdditionalInformation(additionalInfoAry);
                            if (additionalInfoObj != null && !additionalInfoObj.isEmpty()) {
                            	for(String key: additionalInfoObj.keySet()) {
                            		 record.put(key, additionalInfoObj.getString(key));
                            	}
        					} 
                         }	
                        	 if (creditsArray != null && creditsArray.length() > 0) {
     							JSONObject creditsObj = creditsArray.getJSONObject(0);
     							 logger.debug("CompletedTransPostProcesCustom :additionalInfoObj:" +additionalInfoObj+",paymentType:"+paymentType);
     							if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("CIPS") && additionalInfoObj!=null) {
     								creditsObj.put("creditAccountName", additionalInfoObj.opt("creditorName"));
     								creditsObj.put("creditAccountId", additionalInfoObj.opt("creditorAccount"));
     							}
     							if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("BILL_PAYMENT") && additionalInfoObj!=null) {
     								creditsObj.put("creditAccountName", additionalInfoObj.opt("merchantName"));
     								creditsObj.put("creditAccountId", additionalInfoObj.opt("paymentAggregator"));
     								record.put(TransactionConstants.TRANSACTION_TYPE_KEY,"Bill Payment");
       								record.put(TransactionConstants.SERVICE_NAME,"BILL_PAY_CREATE");
       								record.remove(TransactionConstants.PAYMENT_ORDER_PRODUCT_ID_KEY);
     							}
     							creditsArray.put(0, creditsObj);
     			                record.put("credits",creditsArray);
                             }
                        	 if (debitsArray != null && debitsArray.length() > 0) {
      							JSONObject debitsObj = debitsArray.getJSONObject(0);
      							totalDebitAmount=debitsObj.optString("totalDebitAmount");
      							debitCurrency=debitsObj.optString("debitCurrency");
                              }
                        	 if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("CIPS")) {
                        		 String amount=record.optString("amount");
                        		 if(StringUtils.isNotBlank(totalDebitAmount)) {
                        		record.put("amount",new BigDecimal(totalDebitAmount).setScale(2));
                        		record.put("transactionCurrency",debitCurrency);
                        		 }
                        		record.put("totalDebitAmount",new BigDecimal(amount).setScale(2));
                        		record.put("debitCurrency", record.optString("paymentCurrencyId"));
                        		record.remove(TransactionConstants.CURRENCY_ID);
   								record.put(TransactionConstants.TRANSACTION_TYPE_KEY,"ExternalTransfer");
   								record.put(TransactionConstants.SERVICE_NAME,"INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
   								record.remove(TransactionConstants.PAYMENT_ORDER_PRODUCT_ID_KEY);
   								
   							}
                         transactionsArray.put(i,record);
            	 }
            		 resultObj.put(TRANSACTION, transactionsArray);
            		 result=JSONToResult.convert(resultObj.toString());
            }
            }
        } catch (Exception e) {
            logger.error(e);
            CommonUtils.setErrMsg(result, e.toString());
        }
        
        logger.debug("CompletedTransPostProcesCustom :final result ##" + ResultToJSON.convert(result));
        return result;
    }
    public Result execute_old(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            JSONArray charges = new JSONArray();
            JSONObject additionalInfoObj = null;
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);
            logger.debug("Completed Transactions:###"+ ResultToJSON.convert(result));
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                logger.debug(ERR_EMPTY_RESPONSE);
                Result transactionResult = TemenosUtils.getEmptyResult(TRANSACTION);
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_PAGE_START_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_COMPLETED));
                }
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED));
                }
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED));
                }
                return transactionResult;
            } else {
                if (transactionRecords.size() != 0) {
                    for (Record record : transactionRecords) {
                    	record.addParam("transferType", "COMPLETED");
                        record.addParam(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_SUCCESSFUL);
                        String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
                        Dataset chargesSet = record.getDatasetById("charges");
                        String paymentType =  record.getParamValueByName("structuredIssuer");
                        Dataset creditDS = record.getDatasetById("credits");
                        
						if (chargesSet != null && chargesSet.getAllRecords().size() > 0) {
							charges = convertDatasetToJSONArray(chargesSet);
							logger.debug("charges charges1 response: ##" + chargesSet.getAllRecords().toString());
							logger.debug("charges charges1 response Array: ##" + charges);

							JSONObject rootObj = (JSONObject) charges.get(0);
							String chargeCurrencyId = (String) rootObj.get("chargeCurrencyId");
							String chargeType = (String) rootObj.get("chargeType");
							String chargeAmount = (String) rootObj.get("chargeAmount");
							String chargeName = (String) rootObj.get("chargeName");

							if (StringUtils.isNotBlank(chargeCurrencyId)) {
								logger.debug("chargeCurrencyId: ##" + chargeCurrencyId);
								record.addParam("chargeCurrencyId", chargeCurrencyId);
							}

							if (StringUtils.isNotBlank(chargeType)) {
								logger.debug("chargeType: ##" + chargeType);
								record.addParam("chargeType", chargeType);
							}

							if (StringUtils.isNotBlank(chargeAmount)) {
								logger.debug("chargeAmount: ##" + chargeAmount);
								record.addParam("chargeAmount", chargeAmount);
							}

							if (StringUtils.isNotBlank(chargeName)) {
								logger.debug("chargeName: ##" + chargeName);
								record.addParam("chargeName", chargeName);
							}
						}
                        
                        if (transactionType != StringUtils.EMPTY) {
                            transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                            if (transactionType == null) {
                                transactionType = TRANS_TYPE_OTHERS;
                            }
                        }
                        record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
                        Dataset additionalInfoDS = record.getDatasetById("additionalInformations");
                        if(additionalInfoDS!=null  && additionalInfoDS.getAllRecords().size() > 0) {
                         JSONArray additionalInfoAry = convertDatasetToJSONArray(additionalInfoDS);
                         additionalInfoObj = getAdditionalInformation(additionalInfoAry);
                        if (additionalInfoObj != null && !additionalInfoObj.isEmpty()) {
                        	for(String key: additionalInfoObj.keySet()) {
                        		 record.addParam(key, additionalInfoObj.getString(key));
                        	}
    					} 
        			}
                        if (creditDS != null && creditDS.getAllRecords().size() > 0) {
                        	JSONArray creditArray= convertDatasetToJSONArray(creditDS);
							JSONObject creditsObj = creditArray.getJSONObject(0);
							if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("CIPS") && additionalInfoObj!=null) {
								creditsObj.put("creditAccountName", additionalInfoObj.getString("creditorName"));
								creditsObj.put("creditAccountId", additionalInfoObj.getString("creditorAccount"));
							}
							creditArray.put(0, creditsObj);
							Dataset dataset = constructDatasetFromJSONArray(creditArray);
			                dataset.setId("credits");
			                result.addDataset(dataset);
                        }
                        
                  }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            CommonUtils.setErrMsg(result, e.toString());
        }
        
        
        return result;
    }

    public static JSONArray convertDatasetToJSONArray(Dataset dataset) {
		JSONArray array = new JSONArray();
		List<Record> records = new ArrayList<>();

		if (dataset != null && dataset.getAllRecords().size() != 0) {
			records = dataset.getAllRecords();
		}

		for (int i = 0; i < records.size(); i++) {
			array.put(convertRecordToJSONObject(records.get(i)));
		}
		return array;
	}

	public static JSONObject convertRecordToJSONObject(Record record) {

		JSONObject jsonObj = new JSONObject();

		List<Param> arList = record.getAllParams();

		Iterator<Param> it = arList.iterator();
		while (it.hasNext()) {
			Param p = it.next();
			String key = p.getName();
			jsonObj.put(key, p.getValue());
		}
		return jsonObj;
	}
	public JSONObject getAdditionalInformation(JSONArray additionalInformations) {
		logger.debug("GetTransfersOperation: GetTransfers: getAdditionalInformation:responseObj:" + additionalInformations);
		JSONObject jsonObj=null;
		try {
			if (additionalInformations != null) {
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
			logger.debug("Exception occured while invoking getAdditionalInformation: " + e.getLocalizedMessage());
		}
		logger.debug("GetTransfersOperation: GetTransfers: getAdditionalInformation:jsonObj:"+ jsonObj);
	return jsonObj;
	}
	 public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
	        Dataset dataset = new Dataset();
	        for (int count = 0; count < JSONArray.length(); count++) {
	            Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
	            dataset.addRecord(record);
	        }
	        return dataset;
	    }

	    public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
	        Record response = new Record();
	        if (JSONObject == null || JSONObject.length() == 0) {
	            return response;
	        }
	        Iterator<String> keys = JSONObject.keys();

	        while (keys.hasNext()) {
	            String key = (String) keys.next();
	            if (JSONObject.get(key) instanceof Integer) {
	                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.INT);
	                response.addParam(param);

	            } else if (JSONObject.get(key) instanceof Boolean) {
	                Param param = new Param(key, JSONObject.get(key).toString(), FabricConstants.BOOLEAN);
	                response.addParam(param);

	            } else if (JSONObject.get(key) instanceof JSONArray) {
	                Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
	                dataset.setId(key);
	                response.addDataset(dataset);
	            } else if (JSONObject.get(key) instanceof JSONObject) {
	                Record record = constructRecordFromJSONObject(JSONObject.getJSONObject(key));
	                record.setId(key);
	                response.addRecord(record);
	            } else {
	                Param param = new Param(key, JSONObject.optString(key), FabricConstants.STRING);
	                response.addParam(param);
	            }
	        }

	        return response;
	    }

}
