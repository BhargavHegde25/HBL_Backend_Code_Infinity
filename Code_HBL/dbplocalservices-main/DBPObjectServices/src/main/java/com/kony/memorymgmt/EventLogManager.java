package com.kony.memorymgmt;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class EventLogManager {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private FabricRequestManager fabricRequestManager = null;
    private String customerId = "";//null;
    final String CACHE_KEY_AUDIT_PROPERTIES = "AuditLogProperties";

    public EventLogManager(FabricRequestManager fabricRequestManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = "";//HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    // Store Audit properties in session
    public void saveAuditPropertiesIntoSession(SessionMap AuditLogMap) {
        if (null != AuditLogMap) {
            MemoryManager.save(this.fabricRequestManager, CACHE_KEY_AUDIT_PROPERTIES + this.customerId, AuditLogMap);
            diagnostic.prepareInfo("Properties stored in cache").log();
        }
    }

    // Retrieve Audit properties from session
    public SessionMap getAuditPropertiesFromSession(String customerId) {
        SessionMap AuditStore = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                CACHE_KEY_AUDIT_PROPERTIES + this.customerId);
        if (null == AuditStore || AuditStore.isEmpty()) {
            AuditStore = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                    CACHE_KEY_AUDIT_PROPERTIES + this.customerId);
        }
        return AuditStore;
    }
}
