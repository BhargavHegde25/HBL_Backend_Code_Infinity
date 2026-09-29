package com.kony.mobilefabric.temenos;

import java.util.ArrayList;
import java.util.List;

import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * Test-only stand-in for the Fabric runtime logger. Fabric's JSONToResult and Result classes reference it, but the
 * class is only present inside a Fabric server, so it is missing from the unit-test classpath. This stub exposes
 * just the members those classes use and delegates to the real com.temenos.logger. It lives under src/test and is
 * never packaged.
 */
public class Logger {

    public static final List<Object> T = new ArrayList<>();

    private static final Alert ALERT = com.temenos.logger.Logger.forAlert().forModule("Infinity", "TEST");
    private static final Diagnostic DIAGNOSTIC = com.temenos.logger.Logger.forDiagnostic().forModule("Infinity",
            "TEST");

    public static Logger getLogger(Class<?> type) {
        return new Logger();
    }

    public Diagnostic D() {
        return DIAGNOSTIC;
    }

    public Alert W() {
        return ALERT;
    }
}
