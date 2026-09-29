package com.temenos.infinity.api.cards.utils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class CardHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private CardHelper() {
    }

    public static void reloadCardsIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null, URLConstants.CARDS_OS_GET);
        } catch (Exception e) {
            alert.prepareError("Error while reloading cards:", e).log();
        }
    }
}
