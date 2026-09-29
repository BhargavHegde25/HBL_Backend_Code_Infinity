package com.kony.adminconsole.commons.utils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class WriteToLog {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	public static Object debug(String logStr) {
		
		diagnostic.prepareDebug(logStr).log();
		
		return new Object();
	}
	
	public static Object info(String logStr) {
		
		diagnostic.prepareInfo(logStr).log();
		
		return new Object();
	}
	
	public static Object error(String logStr) {
		
		alert.prepareError(logStr).log();
		
		return new Object();
	}

}
