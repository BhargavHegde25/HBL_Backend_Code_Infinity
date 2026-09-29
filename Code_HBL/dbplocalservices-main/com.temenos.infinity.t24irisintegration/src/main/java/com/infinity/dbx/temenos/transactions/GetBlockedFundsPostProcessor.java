package com.infinity.dbx.temenos.transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */

public class GetBlockedFundsPostProcessor extends TemenosBasePostProcessor implements TransactionConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        Dataset transactionsDS = result.getDatasetById(TRANSACTION);
        String ErrMsg = result.getParamValueByName(TransactionConstants.PARAM_ERROR_MESSAGE) != ""
                ? result.getParamValueByName(TransactionConstants.PARAM_ERROR_MESSAGE) : "";

        List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
        if ((transactionRecords == null || transactionRecords.isEmpty()) && StringUtils.isEmpty(ErrMsg)) {
            diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
            Result transactionResult = TemenosUtils.getEmptyResult(TRANSACTION);
            if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGESTART))) {
            	transactionResult.addParam(TransactionConstants.PARAM_PAGESTART, result.getParamValueByName(TransactionConstants.PARAM_PAGESTART));
            }
            if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGESIZE))) {
            	transactionResult.addParam(TransactionConstants.PARAM_PAGE_SIZE_PENDING, result.getParamValueByName(TransactionConstants.PARAM_PAGESIZE));
            }
            if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTALSIZE))) {
            	transactionResult.addParam(TransactionConstants.PARAM_TOTAL_SIZE_PENDING, result.getParamValueByName(TransactionConstants.PARAM_TOTALSIZE));
            }
            return transactionResult;
        }
        
        if ((transactionRecords != null && !transactionRecords.isEmpty())){
            for (Record transaction : transactionRecords) {
                String fromDate = CommonUtils.getParamValue(transaction, TransactionConstants.PARAM_FROM_DATE);
                String toDate = CommonUtils.getParamValue(transaction, TransactionConstants.PARAM_TO_DATE);
                if(StringUtils.isNotBlank(fromDate)){
                    //Convert to standard date format
                    transaction.addParam(TransactionConstants.PARAM_FROM_DATE, convertDate(fromDate));
                }
                if(StringUtils.isNotBlank(toDate)){
                    //Convert to standard date format
                    transaction.addParam(TransactionConstants.PARAM_TO_DATE, convertDate(toDate));
                }
            }
        }
        Dataset dataset = new Dataset();
		Record record = new Record();
		dataset.setId("Meta");
		if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTALSIZE)))
			record.addParam(new Param(TransactionConstants.PARAM_TOTALSIZE, result.getParamValueByName(TransactionConstants.PARAM_TOTALSIZE)));
		if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGESIZE)))
			record.addParam(new Param(TransactionConstants.PARAM_PAGESIZE, result.getParamValueByName(TransactionConstants.PARAM_PAGESIZE)));
		if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGESTART)))
			record.addParam(new Param(TransactionConstants.PARAM_PAGESTART, result.getParamValueByName(TransactionConstants.PARAM_PAGESTART)));
		dataset.addRecord(record);
		result.addDataset(dataset);
		return result;
    }
    
    public String convertDate(String Date){
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = (Date) formatter.parse(Date);
            } catch (ParseException e) {
                alert.prepareError("Unable to parse Date" + e).log();
            }
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String processedDate = DATE_FORMAT.format(date);
        return processedDate;
    }
}