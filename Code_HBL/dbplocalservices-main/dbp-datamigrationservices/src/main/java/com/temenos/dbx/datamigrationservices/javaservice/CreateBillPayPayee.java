package com.temenos.dbx.datamigrationservices.javaservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.resource.api.MigratePayeesResource;

public class CreateBillPayPayee implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(CreateBillPayPayee.class);

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Result newResult = new Result();
		try {
			MigratePayeesResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(MigratePayeesResource.class);
			
			newResult = payeeResource.createBillPayPayee(methodID, inputArray, request, response);
			if(newResult.hasParamByName("id") && !newResult.hasParamByName("dbpErrCode")) {
				result.addParam(new Param("payeeId", newResult.getParamValueByName("id")));
			}else {
				result = newResult;
			}
		} catch (ApplicationException e) {
			logger.error("Exception occured while fetching user contract and contract customer details" + e.getMessage());
			e.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			logger.error("Caught exception at invoke of CreateBillPayPayee: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	
}