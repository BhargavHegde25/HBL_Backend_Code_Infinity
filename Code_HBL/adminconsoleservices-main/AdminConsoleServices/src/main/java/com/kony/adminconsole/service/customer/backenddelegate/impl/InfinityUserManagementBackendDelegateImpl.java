/**
 * 
 */
package com.kony.adminconsole.service.customer.backenddelegate.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.service.customer.backenddelegate.api.InfinityUserManagementBackendDelegate;

/**
 * @author rishi.gupta
 *
 */
public class InfinityUserManagementBackendDelegateImpl implements InfinityUserManagementBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

}
