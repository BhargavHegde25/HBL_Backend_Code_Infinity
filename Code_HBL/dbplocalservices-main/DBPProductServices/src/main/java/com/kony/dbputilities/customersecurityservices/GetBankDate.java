package com.kony.dbputilities.customersecurityservices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class GetBankDate implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Record record = new Record();
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    Date dateobj = new Date();
		    
		    record.addParam(new Param("currentWorkingDate", df.format(dateobj), "String"));	
		    record.addParam(new Param("nextWorkingDate", df.format(dateobj), "String"));
		    record.addParam(new Param("lastWorkingDate", df.format(dateobj), "String"));
		    record.addParam(new Param("companyId", "", "String"));
			Dataset ds = new Dataset("body");
			ds.addRecord(record);
			result.addDataset(ds);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke : "+e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}
}
