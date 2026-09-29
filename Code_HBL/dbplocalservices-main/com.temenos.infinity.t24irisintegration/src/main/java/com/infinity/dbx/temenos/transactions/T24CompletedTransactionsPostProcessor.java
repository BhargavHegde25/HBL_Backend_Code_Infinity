package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class T24CompletedTransactionsPostProcessor extends BasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
        	
        	diagnostic.prepareDebug("START CompletedTransactionsPostProcessor").log();
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            alert.prepareError("************ T24CompletedTransactionsPostProcessor Result ******* "+com.konylabs.middleware.dataobject.ResultToJSON.convert(result)).log();
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
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
                        record.addParam(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_SUCCESSFUL);
                        String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
                        if (transactionType != StringUtils.EMPTY) {
                            transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                            if (transactionType == null) {
                                transactionType = TRANS_TYPE_OTHERS;
                            }
                        }
                        record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
                        
//                    	String narrative = record.getParamValueByName("notes");
//                    	Param descriptionParam = record.getParam("description");
//                    	diagnostic.prepareDebug("***********narrative:"+narrative).log();
//                    	if(narrative!=null && !"".equals(narrative)) {
//                    		String displayName = descriptionParam.getValue();
//                    		if(displayName==null) displayName="";
//                    		descriptionParam.setValue(displayName+" "+narrative);
//                    		diagnostic.prepareDebug("***********descriptionParam:"+descriptionParam.getValue()).log();
//                    	}
                        
                        /*
                         * HBL changes
                         * Before dislayName + narrativve
                         * Now narrative + notes if both are null showing only display name
                         */
                        String notes = record.getParamValueByName("notes");
                        String narrative = record.getParamValueByName("narrative");
                        String chequeNumber = record.getParamValueByName("documentId");
                        String displayName = record.getParamValueByName("displayName");
                        alert.prepareError("***********chequeNumber:"+chequeNumber+" narrative "+narrative+" notes "+notes).log();
                        
                        // description may not exist in the response -> create it
                        Param descriptionParam = record.getParam("description");
                        if (descriptionParam == null) {
                            record.addParam("description", "");
                            descriptionParam = record.getParam("description");
                        }

                        // treat null / "null" / blank as empty
                        notes = (notes == null || "null".equalsIgnoreCase(notes.trim())) ? "" : notes.trim();
                        narrative = (narrative == null || "null".equalsIgnoreCase(narrative.trim())) ? "" : narrative.trim();
                        chequeNumber = (chequeNumber == null || "null".equalsIgnoreCase(chequeNumber.trim())) ? "" : chequeNumber.trim();
                        displayName = (displayName == null || "null".equalsIgnoreCase(displayName.trim())) ? "" : displayName.trim();

                        // displayName, otherwise the incoming description
                        if (displayName.isEmpty()) {
                            String current = descriptionParam.getValue();
                            displayName = (current == null || "null".equalsIgnoreCase(current.trim())) ? "" : current.trim();
                        }

                        alert.prepareError("***********chequeNumber:" + chequeNumber + " narrative " + narrative
                                + " notes " + notes + " displayName " + displayName).log();

                        // same text in narrative and notes -> use it once
                        if (!notes.isEmpty() && notes.equalsIgnoreCase(narrative)) {
                            notes = "";
                        }

                        boolean hasNarrative = !narrative.isEmpty();
                        boolean hasNotes = !notes.isEmpty();
                        boolean hasChequeNumber = !chequeNumber.isEmpty();

                        String description;
                        if (hasNarrative && hasNotes) {
                            description = narrative + " " + notes;        // both
                        } else if (hasNarrative) {
                            description = displayName + " " + narrative;  // narrative only
                        } else if (hasNotes) {
                            description = displayName + " " + notes;      // notes only
                        } else {
                            description = displayName;                    // neither
                        }

                        if (hasChequeNumber) {
                            description = description + " - " + chequeNumber;
                        }

                        // HBL changes - comment the next line to keep duplicate text
                        description = removeDuplicateText(description);

                        descriptionParam.setValue(description);
                        
                        descriptionParam.setValue(description);

                        alert.prepareError(
                            "***********descriptionParam: " + descriptionParam.getValue()
                        ).log();
                        
                    	record.addParam(descriptionParam);
        			}  
                 }   
            }
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            CommonUtils.setErrMsg(result, e.toString());
        }
        return result;
    }
    
    /*
     * HBL changes - removes duplicated text from the built description.
     * 1) side-by-side repeats:                        "MED LOT-89 MED LOT-89" -> "MED LOT-89"
     * 2) side-by-side repeats ending in punctuation:  "QR-07016VIJH9Y-" twice -> once
     * 3) a phrase of 2+ words repeated later in the text:
     *    "Credit Card Pmt 4404500000007933 Credit Card Pmt" -> "Credit Card Pmt 4404500000007933"
     * Single repeated words are never removed.
     */
    private String removeDuplicateText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        String value = text.replaceAll("(?i)\\b(.+?)(?:\\s+\\1)+\\b", "$1");
        value = value.replaceAll("(?i)(?<!\\S)(\\S.*?)(?:\\s+\\1)+(?!\\S)", "$1");
        value = value.replaceAll("\\s+", " ").trim();

        java.util.List<String> words = new java.util.ArrayList<String>(
                java.util.Arrays.asList(value.split(" ")));
        boolean removed = true;
        while (removed) {
            removed = false;
            outer:
            for (int len = words.size() / 2; len >= 2 && !removed; len--) {
                for (int i = 0; i + len <= words.size() - len; i++) {
                    for (int j = i + len; j + len <= words.size(); j++) {
                        if (samePhrase(words, i, j, len)) {
                            for (int k = 0; k < len; k++) {
                                words.remove(j);
                            }
                            removed = true;
                            break outer;
                        }
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(w);
        }
        return sb.toString().trim();
    }

    private boolean samePhrase(java.util.List<String> words, int i, int j, int len) {
        for (int k = 0; k < len; k++) {
            String left = dupKey(words.get(i + k));
            if (left.isEmpty() || !left.equals(dupKey(words.get(j + k)))) {
                return false;
            }
        }
        return true;
    }

    /* compare ignoring case and leading / trailing punctuation */
    private String dupKey(String word) {
        return word.toLowerCase().replaceAll("^[^a-z0-9]+", "").replaceAll("[^a-z0-9]+$", "");
    }
}
