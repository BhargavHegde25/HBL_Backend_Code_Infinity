package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.MSCertificateConstants;
import com.temenos.infinity.api.arrangements.javaservice.GetAccounts;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;
import com.temenos.infinity.transact.tokenmanager.jwt.TokenGenerator;

public class GetAccountsOperation implements JavaService2 {

	private static final Logger LOG = LogManager.getLogger(GetAccountsOperation.class);

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        
    	
        Result result = new Result();
        String authToken = "";

        Map<String, String> params = HelperMethods.getInputParamMap(inputArray);

        // Initializing of Accounts through Abstract factory method
        ArrangementsResource AccountsResource = DBPAPIAbstractFactoryImpl.getResource(ArrangementsResource.class);
        ServicesManager servicesManager;
        String backendIdentifiers = null;
        String backendUserId = null;
        String customerType = null;
        String customerID = null;
        String companyId = null;
        String loginUserId = null;
        StringBuilder coreCustomerIdList = new StringBuilder();

        boolean isSuperAdmin = false;
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            loginUserId = HelperMethods.getCustomerIdFromSession(request);
        } else {
            loginUserId = params.containsKey(InfinityConstants.id) &&  StringUtils.isNotBlank(params.get(InfinityConstants.id)) ? params.get(InfinityConstants.id) : request.getParameter(InfinityConstants.id);
            companyId = getCompanyIdFromDB(loginUserId, request);
            isSuperAdmin = true;
        }

        if (!isSuperAdmin && StringUtils.isNotBlank(loginUserId)) {
            servicesManager = request.getServicesManager();
            // Get Customer id and type for business user
            customerType = (String) servicesManager.getIdentityHandler().getUserAttributes().get("CustomerType_id");
            customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
           // companyId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
            companyId=CommonUtils.getCompanyId(request);
            backendIdentifiers = (String) servicesManager.getIdentityHandler().getUserAttributes()
                    .get("backendIdentifiers");
            if (StringUtils.isNotBlank(backendIdentifiers)) {
                backendUserId = getBackendId(backendIdentifiers, "T24",companyId);
            }
        }
        
        if(StringUtils.isBlank(loginUserId) && isSuperAdmin) {
            backendUserId = params.containsKey(InfinityConstants.coreCustomerId) &&  StringUtils.isNotBlank(params.get(InfinityConstants.coreCustomerId)) ? params.get(InfinityConstants.coreCustomerId) : request.getParameter(InfinityConstants.coreCustomerId);
           // companyId=LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
            companyId=CommonUtils.getCompanyId(request);
        }
        
        if(StringUtils.isNotBlank(loginUserId)) {
            customerID = loginUserId;
        }
        
        // If product line is null then set default to ACCOUNT
        String productLineId = request.getParameter("productLineId");
        if (StringUtils.isBlank(productLineId)) {
            productLineId = "ACCOUNTS";
        }
        String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
		if (ARRANGEMENTS_BACKEND!= null) {
        if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
        	Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        	inputParams.put("userId", customerID);
        	inputArray[1] = inputParams;
    		GetAccounts mockAccounts = new GetAccounts();
    		return mockAccounts.invoke(methodID, inputArray, request, response);
    	}
        else if (ARRANGEMENTS_BACKEND.equals("t24")) {
        	try {
        		HashMap<String, Object> headerParams = new HashMap<String, Object>();
        		HashMap<String, Object> inputParams = new HashMap<String, Object>();
				String accounts = DBPServiceExecutorBuilder.builder()
                        .withServiceId("ArrangementT24ISAccountsCustom")
                        .withOperationId("getAccountsByCoreCustomerIdListCustom")
                        .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                        .withDataControllerRequest(request).build().getResponse();
				return JSONToResult.convert(accounts);
            } catch (Exception e) {
            	LOG.error(e);
            
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
    	}
		}
        
        try {
        	Map<String,Object> additionalParams = new HashMap<String,Object>();
        	additionalParams.put("productType", DBPUtilitiesConstants.PRODUCT_TYPE_T24);
            authToken = TokenGenerator.generateAuthToken(MSCertificateConstants.BACKEND,
                    MSCertificateConstants.USERNAME,
                    ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"),
                    ServerConfigurations.AMS_ROLE_ID.getValueIfExists(), MSCertificateConstants.TOKEN_VALIDITY, true,additionalParams);
            CommonUtils.setCompanyIdToRequest(request);
        } catch (CertificateNotRegisteredException e) {
            LOG.error("Certificate Not Registered" + e);
        } catch (Exception e) {
            LOG.error("Exception occured during generation of authToken " + e);
        }

        String AccountId = request.getParameter("accountID");
        try {
            result = AccountsResource.getArrangementAccounts(backendUserId, customerType, customerID, productLineId,
                    AccountId, companyId, request, authToken);
        } catch (Exception e) {
            LOG.error("Unable to fetch records from Backend" , e);
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
        }
        return result;
    }

    private String getCompanyIdFromDB(String loginUserId, DataControllerRequest request) {
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        backendIdentifierDTO.setCustomer_id(loginUserId);
        backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();
        return backendIdentifierDTO!= null ? backendIdentifierDTO.getCompanyId() : EnvironmentConfigurationsHandler.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
    }

    private static String getBackendId(String backendIdentifiers, String templateName,String companyId) {
        String BackendId = null;
        BackendId = getCoreId(backendIdentifiers, templateName, "customerId", "1",companyId);
        return BackendId;
    }

    private static String getCoreId(String backendIdentifiers, String BackendType, String IdentifierName,
            String SequenceNumber,String companyId) {
        String BackendId = null;

        if (StringUtils.isNotBlank(backendIdentifiers)) {
            JSONObject backendIdentifiersJSON = new JSONObject(backendIdentifiers);
            if (backendIdentifiersJSON.has(BackendType)) {
                JSONArray templateIdentifiers = backendIdentifiersJSON.getJSONArray(BackendType);

                for (int i = 0; i < templateIdentifiers.length(); i++) {
                    String identifier_name = templateIdentifiers.getJSONObject(i).getString("identifier_name");
                    String sequenceNumber = templateIdentifiers.getJSONObject(i).getString("sequence_number");
                    String companyIdFromLogin = templateIdentifiers.getJSONObject(i).getString("CompanyId");

                    if (BackendType.equals("T24")) {
                        if (IdentifierName.equals(identifier_name) && SequenceNumber.equals(sequenceNumber) ) {
                        
                        	if(companyIdFromLogin!=null && !"".equalsIgnoreCase(companyIdFromLogin)) {//TODO: This is a work around to support no company id in backend identifier
                        		if(companyId.equalsIgnoreCase(companyIdFromLogin)) { //TODO: Move this check to line 185 once comapnyId is coming in backendidentifier
                        			BackendId = templateIdentifiers.getJSONObject(i).getString("BackendId");
                        			break;
                        		}
                        		
                        	}else{
                        		BackendId = templateIdentifiers.getJSONObject(i).getString("BackendId");
                        		break;
                        	}
                      
                        	
                        }
                    } else {
                        if (IdentifierName.equals(identifier_name)) {
                            BackendId = templateIdentifiers.getJSONObject(0).getString("BackendId");
                        }
                    }
                }
            }
        }
        return BackendId;
    }

}
