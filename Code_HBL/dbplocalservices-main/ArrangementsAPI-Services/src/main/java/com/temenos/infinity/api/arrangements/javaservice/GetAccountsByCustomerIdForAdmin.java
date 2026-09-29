package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetAccountsByCustomerIdForAdmin implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        String authToken="";
        
        // Initializing of Accounts through Abstract factory method
        ArrangementsResource AccountsResource = DBPAPIAbstractFactoryImpl.getResource(ArrangementsResource.class);

        String customerId = request.getParameter(TemenosConstants.CUSTOMER_ID) != null
                ? request.getParameter(TemenosConstants.CUSTOMER_ID).toString()
                : "";

        // Throw error if customer id is not sent in request param
        if (customerId.equals("")) {
            alert.prepareError("Input CustomerId not found! ").log();
            return ErrorCodeEnum.ERR_20052.setErrorCode(new Result());
        }

     // Read dbxdb customer table and fetch the details
        Map<String, String> typeUserNameMap = getCustomerTypeAndUserName(request, customerId);

        // Fetch the customer type and back end(T24 customer) id
        String customerType_id = typeUserNameMap.get(TemenosConstants.CUSTOMER_TYPE_ID);
        Map<String, String> backendDetails = ArrangementsUtils.getBackendId(request, customerId, TemenosConstants.CONSTANT_TEMPLATE_NAME,
                TemenosConstants.PARAM_CUSTOMER_ID, "1");
        String backendId = backendDetails.get("backendId");
        String companyId = backendDetails.get("companyId");
        if (null == backendId || backendId.equals("")) {
            alert.prepareError("Unable to fetch the backend Id for the provided user name! ").log();
            return ErrorCodeEnum.ERR_20053.setErrorCode(new Result());
        }

        // If product line is null then set default to ACCOUNT
        String productLineId = request.getParameter("productLineId");
        if (StringUtils.isBlank(productLineId)) {
            productLineId = "ACCOUNTS";
        }

        // Set Admin Privileges in request
        request.addRequestParam_(TemenosConstants.TRANSACTION_PERMISSION, TemenosConstants.ADMIN);
        request.addRequestParam_(TemenosConstants.PARAM_USERNAME, typeUserNameMap.get(TemenosConstants.PARAM_USERNAME));
        request.addRequestParam_("customerId", customerId);

        String AccountId = request.getParameter("accountID");
        try {
        	Map<String, String> inputMap = new HashMap<>();
			inputMap.put("customerId",ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"));
			authToken = TokenUtils.getAMSAuthToken(inputMap);
        	CommonUtils.setCompanyIdToRequest(request);
		} catch (CertificateNotRegisteredException e) {
			alert.prepareError("Certificate Not Registered" + e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured during generation of authToken " + e).log();
		}
        try {
            result = AccountsResource.getArrangementAccounts(backendId, customerType_id, customerId, productLineId,
                    AccountId, companyId, request, authToken); 

        } catch (Exception e) {
            alert.prepareError("Unable to fetch records from Backend" + e).log();
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
        }
        return result;

    }

    // Get CustomerId and back end Id
    public Map<String, String> getCustomerTypeAndUserName(DataControllerRequest request, String customerId) {
        String customerType_id = "";
        String userName = "";
        Map<String, String> map = new HashMap<String, String>();
        try {

            String filter = ArrangementsUtils.buildOdataCondition(TemenosConstants.PARAM_ID, TemenosConstants.EQUAL,
                    customerId);
            HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
            HashMap<String, Object> svcParams = new HashMap<String, Object>();

            svcParams.put(TemenosConstants.PARAM_DOLLAR_FILTER, filter);
            String DBXCustomerDetails = "";
            try {
                DBXCustomerDetails = DBPServiceExecutorBuilder.builder()
                        .withServiceId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERDETAILS.getServiceName())

                        .withOperationId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERDETAILS.getOperationName())

                        .withRequestParameters(svcParams).withRequestHeaders(svcHeaders)
                        .withDataControllerRequest(request).build().getResponse();
            } catch (Exception e) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20046);
            }
            Result dbxResult = new Result();
            if (StringUtils.isNotBlank(DBXCustomerDetails)) {
                dbxResult = JSONToResult.convert(new JSONObject(DBXCustomerDetails).toString());
            }
            Dataset customerDataset = dbxResult.getDatasetById(TemenosConstants.DS_CUSTOMER);
            if (null != customerDataset) {

                customerType_id = customerDataset.getRecord(0).getParamValueByName(TemenosConstants.CUSTOMER_TYPE_ID);
                userName = customerDataset.getRecord(0).getParamValueByName(TemenosConstants.PARAM_USERNAME);
            }

        } catch (Exception e) {

            alert.prepareError("Error while retrieving CustomerType_id for Customer " + customerId).log();
        }

        map.put(TemenosConstants.CUSTOMER_TYPE_ID, customerType_id);
        map.put(TemenosConstants.PARAM_USERNAME, userName);

        return map;
    }

}
