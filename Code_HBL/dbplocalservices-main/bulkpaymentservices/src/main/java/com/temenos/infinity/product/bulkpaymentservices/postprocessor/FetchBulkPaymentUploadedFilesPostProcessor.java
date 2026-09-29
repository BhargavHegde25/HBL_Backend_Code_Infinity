package com.temenos.infinity.product.bulkpaymentservices.postprocessor;

import java.util.List;
import java.util.ListIterator;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FetchBulkPaymentUploadedFilesPostProcessor extends BasePostProcessor implements AccountsConstants  {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			List<Dataset> recordList = result.getAllDatasets();
            if(recordList !=null 
            	&& recordList.size()>0 
            	&& recordList.get(0)!=null 
            	&& recordList.get(0).getAllRecords()!=null 
            	&& recordList.get(0).getAllRecords().size() > 0)
            {      
			ListIterator<Record> recordItr = result.getAllDatasets().get(0).getAllRecords().listIterator();
			
			 while(recordItr.hasNext()) { 
				 Record record = recordItr.next();
				 
				 if(record.getParam("status") == null || String.valueOf(record.getParam("status")).equalsIgnoreCase("RECEIVED") )
				 {
					 
					record.addParam(new Param("status", "Uploaded"));
				 }
			 }}

		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking post processor for fetchBulkPaymentUploadedFiles: ", e).log();
			return null;
		}
		return result;
	}
}
