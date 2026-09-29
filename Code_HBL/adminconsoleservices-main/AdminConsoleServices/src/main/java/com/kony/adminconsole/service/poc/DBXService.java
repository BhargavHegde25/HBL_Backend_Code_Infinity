package com.kony.adminconsole.service.poc;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * Super class of all DBX Service Classes
 * 
 * @author Aditya Mankal
 *
 */
public abstract class DBXService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {

        try {
            // Invoke Service Implementation
            Object serviceResult = invokeService(methodID, inputArray, requestInstance, responseInstance);

            // Return Service Result
            return serviceResult;
        } catch (ApplicationException e) {
            // DBX Application Exception
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            // Java Exception
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception. Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

    protected abstract Object invokeService(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception;
}
