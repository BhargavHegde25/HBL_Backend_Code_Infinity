package com.temenos.infinity.api.arrangements.postprocessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.infinity.api.arrangements.constants.ObjectServicesConstants;
import com.temenos.infinity.api.arrangements.utils.ObjectServiceHelperMethods;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.Constants;

public class CreatePayOffSimulationObjectPostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");


	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		Log4j2Configurator.getInstance();
	 try {

            PayloadHandler responsePayloadHandler = fabricResponseManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject customParams = new JsonObject();

            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
            		fabricRequestManager);
            String customerid = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
            String eventType = PARAM_ACCOUNT_ACTION;
            String eventSubType = PARAM_PAYOFF_SIMULATION;
            String producer = "Accounts/createAndGetPayOffSimulation";
            String statusId = PARAM_SID_EVENT_FAILURE;

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equals(PARAM_TRUE)) {
				/*
				 * if( responsePayload.has("errmsg")) {
				 * 
				 * String errmsg = responsePayload.getAsString("errmsg"); JsonArray
				 * accountsArray = responsePayload.getAsJsonArray("Accounts"); Integer
				 * noOfAccounts = accountsArray.size();
				 * responsePayload.addProperty("coreCustomerCount", noOfAccounts.toString());
				 * responsePayload.remove("Accounts"); }
				 */
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(fabricRequestManager, responsePayload,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods", e2).log();
                }
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured in GetAccountsPostLoginObjectServicePostProcessor ", ex).log();
        }

    }
}