/**
 * 
 */
package com.kony.adminconsole.service.usermanagement.backenddelegate.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.service.usermanagement.backenddelegate.api.InternalUserManagementBackendDelegate;

/**
 * @author rishi.gupta
 *
 */
public class InternalUserManagementBackendDelegateImpl implements InternalUserManagementBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

}
