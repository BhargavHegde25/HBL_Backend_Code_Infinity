package com.kony.dbputilities.util;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class Timer {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    Long start;
    String url = "";

    public Timer(String url) {
        this.url = url;
        start = System.currentTimeMillis();
    }

    public void printEndTime() {
        alert.prepareError(
                "Execution took -->" + (System.currentTimeMillis() - start) + " milliseconds time for service " + url).log();
    }

}
