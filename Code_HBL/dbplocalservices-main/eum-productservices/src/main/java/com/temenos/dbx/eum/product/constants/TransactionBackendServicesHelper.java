package com.temenos.dbx.eum.product.constants;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.constants.TransactionBackendServicesHelper;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class TransactionBackendServicesHelper {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static Map<String, String> backendServices = new HashMap<>();
    private static String PAYMENT_BACKEND_MOCK = "MOCK";

    static {
        try (InputStream inputStream = TransactionBackendServicesHelper.class.getClassLoader()
                .getResourceAsStream("TransactionBackendServices.yml");) {
            Yaml yaml = new Yaml();
            backendServices = yaml.load(inputStream);
            System.out.println(backendServices);
        } catch (Exception e) {
            alert.prepareError("Unable to load properties : "+e).log();
        }
    }

    /**
     * method to get serviceId and operation for given service key
     *
     */
    public static final String getBackendURL(String serviceId, String backend) {
        Gson gson = new Gson(); 
        String services = gson.toJson(backendServices);
        return new JSONObject(services).getJSONObject("Backend").getJSONObject(backend).get(serviceId).toString();
    }
    
    public static Result fetchBackendResponse(String serviceId, DataControllerRequest request, HashMap<String, Object> serviceHeaders,HashMap<String, Object> params){
        String Payment_Backend = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
        if(StringUtils.isBlank(Payment_Backend))
            Payment_Backend = PAYMENT_BACKEND_MOCK;
        String serviceDetails = TransactionBackendServicesHelper.getBackendURL(serviceId, Payment_Backend);
        String serviceName = serviceDetails.split("\\.")[0];
        String operationName = serviceDetails.split("\\.")[1];
        Result transactionResult = new Result();
        try {
            transactionResult = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
                    operationName, true);
        } catch (Exception e) {
            alert.prepareError("Exceptiom occurred while invoking backend service : "+ serviceDetails +" "+e).log();
            return null;
        }
        return transactionResult;
    }
}