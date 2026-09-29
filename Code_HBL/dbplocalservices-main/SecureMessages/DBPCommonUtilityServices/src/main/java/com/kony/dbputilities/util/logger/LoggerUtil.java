package com.kony.dbputilities.util.logger;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLConstants;

public class LoggerUtil {

    private boolean isDebugModeEnabled = false;

    private Alert alert;

    public LoggerUtil(Class<?> className) {
        isDebugModeEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getValue(URLConstants.IS_DEBUG_MODE_ENABLED));
	    Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	    Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    }
    public void debug(String message) {
        isDebugModeEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getValue(URLConstants.IS_DEBUG_MODE_ENABLED));
        if(isDebugModeEnabled) {
            alert.prepareError("Class: "+ log.getClass()+"\t Line no "+ new Exception().getStackTrace()[1].getLineNumber()+" \t "+message).log();
        }
    }

    public void error(String message) {
        alert.prepareError("Class: "+ log.getClass()+"\t Line no "+ new Exception().getStackTrace()[1].getLineNumber()+" \t "+message).log();
    }

    public void error(String string, Exception e) {
        alert.prepareError("Class: "+ log.getClass()+"\t Line no "+ new Exception().getStackTrace()[1].getLineNumber()+" \t "+string,e).log();
    }

    /**
     * @return the isDebugModeEnabled
     */
    public boolean isDebugModeEnabled() {
        return isDebugModeEnabled;
    }

}
