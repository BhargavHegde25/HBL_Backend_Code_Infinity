package com.infinity.dbx.temenos.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.AESEncyptor;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbx.objects.Customer;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;


public class GetUser implements JavaService2, AccountsConstants, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
        Iterator<String> iterator = request.getParameterNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if ((!params.containsKey(key) || StringUtils.isBlank((String) params.get(key)))
                    && StringUtils.isNotBlank(request.getParameter(key))) {
                params.put(key, request.getParameter(key));
            }
        }

        String loggedInUserId = "";
        String legalEntityId = null;
        boolean isUnenrolled = false;
		boolean isSuperAdmin = Boolean.valueOf(request.getParameter("isSuperAdmin"));
		alert.prepareError("isSuperAdmin:"+isSuperAdmin);
		boolean isEncryptEnabled = Boolean
				.valueOf(com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler
						.getClientAppProperty("E2E_ENCRYPTION_ENABLED"));
        String integration = StringUtils.upperCase(EnvironmentConfigurationsHandler.getValue("INTEGRATION_NAME", request));
        if (Boolean.parseBoolean(request.getParameter("isSuperAdmin"))) {
            loggedInUserId = (String) params.get("Customer_id");
            legalEntityId = (String) params.get("legalEntityId");
        } else {
            loggedInUserId = (String) params.get("loginUserId");
            String integrationType = EnvironmentConfigurationsHandler
                    .getValue("ARRANGEMENTS_BACKEND", request);
            if ("MOCK".equalsIgnoreCase(integrationType)) {
            	legalEntityId = LegalEntityUtil.getLegalEntityIdFromSession(request);
            }
            else
            	legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
        }
        alert.prepareError("legalEntityId:"+legalEntityId);
        if(StringUtils.isBlank(legalEntityId)) {
        	return ErrorCodeEnum.ERR_29040.setErrorCode(result);
        }
        boolean isCustomerPresent = false;
        String schema = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME", request);
        BackendIdentifierDTO backenddto = new BackendIdentifierDTO();
        backenddto.setBackendType(integration);
        backenddto.setCompanyLegalUnit(legalEntityId);
        BackendIdentifierBusinessDelegate backendidentifierBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
        //String backendType = backendidentifierBD.getBackendType(backenddto,request.getHeaderMap());
        String backendType = StringUtils.upperCase(integration);

        String partyId="";
        String filter = "id eq " + loggedInUserId;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.PARAM_DOLLAR_FILTER, filter);
        result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                UserConstants.DB_SERVICE,
                schema + UserConstants.OP_GET_USER);
        Dataset customerGetDs = result != null ? result.getDatasetById("customer") : null;
        if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
            isCustomerPresent = true;
            filter = "BackendType eq " + backendType + " and Customer_id eq " + loggedInUserId  + " and companyLegalUnit eq "+ legalEntityId;
            map = new HashMap<String, Object>();
            map.put(Constants.PARAM_DOLLAR_FILTER, filter);
            result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                    UserConstants.DB_SERVICE,
                    schema + UserConstants.OP_GET_BACKENDIDENTIFIER);
            customerGetDs = result != null ? result.getDatasetById("backendidentifier") : null;
            if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
                request.addRequestParam_("Customer_id",
                        customerGetDs.getAllRecords().get(0).getParamValueByName("BackendId"));
                params.put("Customer_id", customerGetDs.getAllRecords().get(0).getParamValueByName("BackendId"));
                partyId = customerGetDs.getAllRecords().get(0).getParamValueByName("BackendId");
                request.getHeaderMap().put("companyId",
                        customerGetDs.getAllRecords().get(0).getParamValueByName("companyLegalUnit"));
            } else {
            	Result virtualUserResult = getDbxUser(params, request);
            	if (!isSuperAdmin && isEncryptEnabled) {
        			@SuppressWarnings("deprecation")
        			String resultJson = ResultToJSON.convert(virtualUserResult);
        			virtualUserResult = AESEncyptor.encryptJsonStringToResult(resultJson);
        		}
                return virtualUserResult;
            }
        }
        else {
            filter = "BackendType eq " + backendType + " and BackendId eq " + loggedInUserId  + " and companyLegalUnit eq "+ legalEntityId;
            map = new HashMap<String, Object>();
            map.put(Constants.PARAM_DOLLAR_FILTER, filter);
            result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                    UserConstants.DB_SERVICE,
                    schema + UserConstants.OP_GET_BACKENDIDENTIFIER);
            customerGetDs = result != null ? result.getDatasetById("backendidentifier") : null;
            if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
                request.getHeaderMap().put("companyId",
                        customerGetDs.getAllRecords().get(0).getParamValueByName("companyLegalUnit"));
                loggedInUserId = customerGetDs.getAllRecords().get(0).getParamValueByName("Customer_id");
                partyId = customerGetDs.getAllRecords().get(0).getParamValueByName("BackendId");
                isCustomerPresent = true;
            }
        }
        
        if(!isCustomerPresent){
            filter = "id eq " + loggedInUserId;
            map = new HashMap<String, Object>();
            map.put(Constants.PARAM_DOLLAR_FILTER, filter);
            result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                    UserConstants.DB_SERVICE,
                    schema + UserConstants.OP_GET_USER);
            customerGetDs = result != null ? result.getDatasetById("customer") : null;
            if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
                isCustomerPresent = true;
            }
        }
        alert.prepareError("loggedInUserId6566",loggedInUserId ).log();
        request.addRequestParam_("isCustomerPresent", ""+isCustomerPresent);
        Record userRecord = new Record();
        if(integration.equalsIgnoreCase("T24")) {
         if (Boolean.parseBoolean(request.getParameter("isSuperAdmin"))) {
            result = CommonUtils.callIntegrationService(request, params, request.getHeaderMap(),
                    UserConstants.SERVICE_ID_USER,
                    UserConstants.OP_GET_USER_FOR_ADMIN, true);
            Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
            if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                userRecord = userDS.getRecord(0);
            }
         } else {
            result = CommonUtils.callIntegrationService(request, params, null, UserConstants.SERVICE_ID_USER,
                    UserConstants.OP_GET_USER_DETAILS, true);
            Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
            if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                userRecord = userDS.getRecord(0);
                Result associatedCustomers = getAssociatedCustomers(params, request);
                if (associatedCustomers != null && !associatedCustomers.getAllDatasets().isEmpty() &&
                        associatedCustomers.getDatasetById("customers") != null
                        && !associatedCustomers.getDatasetById("customers").getAllRecords().isEmpty()) {
                    userRecord.addDataset(associatedCustomers.getDatasetById("customers"));
                }
            }
         }
        } else if(integration.equalsIgnoreCase("party")){
        	if (Boolean.parseBoolean(request.getParameter("isSuperAdmin"))) {
        	if(!isCustomerPresent){
        	isUnenrolled = true;
			result = getPartyDetailsForUnenrolled(params, request, legalEntityId);
            Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
            if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                userRecord = userDS.getRecord(0);
        	}	
        }	
		else {
			result = getPartyDetailsForEenrolled(params,partyId, legalEntityId, request);
            Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
            if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                userRecord = userDS.getRecord(0);
                Result associatedCustomers = getAssociatedCustomers(params, request);
                if (associatedCustomers != null && !associatedCustomers.getAllDatasets().isEmpty() &&
                        associatedCustomers.getDatasetById("customers") != null
                        && !associatedCustomers.getDatasetById("customers").getAllRecords().isEmpty()) {
                    userRecord.addDataset(associatedCustomers.getDatasetById("customers"));
                }
            }
		  }
         }else
         {
         	if(!isCustomerPresent){
 			result = getPartyDetailsForUnenrolled(params, request, legalEntityId);
             Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
             if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                 userRecord = userDS.getRecord(0);
         	}	
         }	
 		else {
 			result = getPartyDetailsForEenrolled(params,partyId, legalEntityId, request);
             Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
             if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                 userRecord = userDS.getRecord(0);
                 Result associatedCustomers = getAssociatedCustomers(params, request);
                 if (associatedCustomers != null && !associatedCustomers.getAllDatasets().isEmpty() &&
                         associatedCustomers.getDatasetById("customers") != null
                         && !associatedCustomers.getDatasetById("customers").getAllRecords().isEmpty()) {
                     userRecord.addDataset(associatedCustomers.getDatasetById("customers"));
                 }
             }
 		  }
         	 Dataset userDS = result.getDatasetById(UserConstants.DATASET_USER);
             if (userDS != null && !userDS.getAllRecords().isEmpty()) {
                 userRecord = userDS.getRecord(0);
                 Result associatedCustomers = getAssociatedCustomers(params, request);
                 if (associatedCustomers != null && !associatedCustomers.getAllDatasets().isEmpty() &&
                         associatedCustomers.getDatasetById("customers") != null
                         && !associatedCustomers.getDatasetById("customers").getAllRecords().isEmpty()) {
                     userRecord.addDataset(associatedCustomers.getDatasetById("customers"));
                 }
             }
          }
        	 request.addRequestParam_("legalEntityId", legalEntityId);        	     	 
        	 if(!isUnenrolled) {
        		 Result userGetOrch = getDbxUser(params, request);
        		 mergeData(userGetOrch,result,request);
        	 }
        	 
       }

		if (!isSuperAdmin && isEncryptEnabled) {
			@SuppressWarnings("deprecation")
			String resultJson = ResultToJSON.convert(result);
			result = AESEncyptor.encryptJsonStringToResult(resultJson);
		}
        
       
        Customer cust = TemenosUtils.createCustomer(userRecord);
        // What customer id attribute should we use?
        Gson gson = new Gson();
        String gsonCustomer = gson.toJson(cust);
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.insertIntoSession(Constants.SESSION_ATTRIB_CUSTOMER, gsonCustomer, request);
        return result;
        
    }
    private void mergeData(Result userGet, Result result, DataControllerRequest request) throws Exception {
    	
    	
    	Record userRecord = result.getDatasetById(UserConstants.DATASET_USER) != null
                ? result.getDatasetById(UserConstants.DATASET_USER).getRecord(0)
                : new Record();
    	Dataset phoneDs = userRecord.getDatasetById("ContactNumbers");
        if (phoneDs == null) {
            phoneDs = new Dataset();
            phoneDs.setId("ContactNumbers");
        }

        Dataset EmailIds = userRecord.getDatasetById("EmailIds");
        if (EmailIds == null) {
            EmailIds = new Dataset();
            EmailIds.setId("EmailIds");
        }

        boolean isPhoneSet = false;
        if (phoneDs != null) {
            for (Record rec : phoneDs.getAllRecords()) {
                if ("true".equalsIgnoreCase(CommonUtils.getParamValue(rec, TemenosConstants.PARAM_IS_PRIMARY))) {
                    isPhoneSet = true;
                    break;
                }
            }
        }

        String phone = "";
        Record phoneRecord = new Record();
        boolean isphoneRecordSet = false;
        boolean isEmailRecordSet = false;
        Dataset contactDetails = userRecord.getDatasetById("contactDetails");
        if (!isPhoneSet && contactDetails != null) {
            Record rec = contactDetails.getAllRecords().get(0);
            phone = rec.getParamValueByName("phone");

            if (phone != null) {
                if (phone.contains("-")) {
                    phoneRecord.addParam("phoneCountryCode", phone.split("-")[0]);
                    phoneRecord.addParam("Value", phone.split("-")[1]);
                } else {
                    phoneRecord.addParam("Value", phone);
                }
                phoneRecord.addParam("isPrimary", "true");
                phoneRecord.addParam("Extension", "Mobile");
                phoneRecord.addParam("Type_id", "COMM_TYPE_PHONE");
                phoneRecord.addParam("id", "1");
                isPhoneSet = true;
            }
        }
        boolean isEmailSet = false;
        if (EmailIds != null) {
            for (Record rec : EmailIds.getAllRecords()) {
                if ("true".equalsIgnoreCase(CommonUtils.getParamValue(rec, TemenosConstants.PARAM_IS_PRIMARY))) {
                    isEmailSet = true;
                    break;
                }
            }
        }
        String email = "";
        Record emailRecord = new Record();
        if (!isEmailSet && contactDetails != null) {
            Record rec = contactDetails.getAllRecords().get(0);
            email = rec.getParamValueByName("email");
            emailRecord.addParam("Value", email);
            emailRecord.addParam("isPrimary", "true");
            emailRecord.addParam("Type_id", "COMM_TYPE_EMAIL");
            emailRecord.addParam("id", "1");

            isEmailSet = true;
        }

        
        Dataset addressDS = userRecord.getDatasetById(UserConstants.DATASET_ADDRESS);
        if (addressDS == null) {
            addressDS = new Dataset(UserConstants.DATASET_ADDRESS);
            userRecord.addDataset(addressDS);
            userRecord.removeParamByName(UserConstants.DATASET_ADDRESS);
        }

        Record addressRecord = new Record();
        boolean isAddressNew = true;
        if (addressDS != null && addressDS.getAllRecords() != null && !addressDS.getAllRecords().isEmpty()) {
            for(Record record : addressDS.getAllRecords()) {
                if(Boolean.parseBoolean(record.getParamValueByName("isPrimary"))){
                    addressRecord = record;
                    isAddressNew = false;
                }
                else if(!record.hasParamByName("isPrimary")) {
                    addressRecord = record;
                    isAddressNew = false;
                }
            }
        }
          
        addressRecord.addStringParam(UserConstants.ADDR_TYPE, UserConstants.ADDRESS_TYPE_HOME);
        if(StringUtils.isBlank(addressRecord.getParamValueByName("country"))) {
            addressRecord.addStringParam("country", userRecord.getParamValueByName("country"));
        }
        if(StringUtils.isBlank(addressRecord.getParamValueByName("AddressLine1"))) {
            addressRecord.addStringParam("AddressLine1", userRecord.getParamValueByName("AddressLine1"));
        }
        if(StringUtils.isBlank(addressRecord.getParamValueByName("AddressLine2"))) {
            addressRecord.addStringParam("AddressLine2", userRecord.getParamValueByName("AddressLine2"));
        }
        if(StringUtils.isBlank(addressRecord.getParamValueByName("ZipCode"))) {
            addressRecord.addStringParam("ZipCode", userRecord.getParamValueByName("ZipCode"));
        }
        if(StringUtils.isBlank(addressRecord.getParamValueByName("City_id"))) {
            addressRecord.addStringParam("City_id", userRecord.getParamValueByName("City_id"));
        }
        if(StringUtils.isBlank(addressRecord.getParamValueByName("CityName"))) {
            addressRecord.addStringParam("CityName", userRecord.getParamValueByName("CityName"));
        }
        if(StringUtils.isBlank(addressRecord.getParamValueByName(UserConstants.ADDR_TYPE))) {
            addressRecord.addStringParam(UserConstants.ADDR_TYPE, UserConstants.ADDRESS_TYPE_HOME);
        }

        if(StringUtils.isNotBlank(CommonUtils.getParamValue(addressRecord, "country"))) {
            String country = CommonUtils.getParamValue(addressRecord, "country");
            HashMap<String, Object> inputParams = new HashMap<String, Object>();
            inputParams.put(Constants.PARAM_DOLLAR_FILTER, "Name eq '" + country + "'");
            Result countryGet = CommonUtils.callIntegrationService(request, inputParams, null,
                    TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_COUNTRY_GET, false);
            if (countryGet.getDatasetById(DS_COUNTRY) != null
                    && !countryGet.getDatasetById(DS_COUNTRY).getAllRecords().isEmpty()) {
                String countryCode = countryGet.getDatasetById(DS_COUNTRY).getRecord(0).getParamValueByName("Code");
                addressRecord.addStringParam("Country_id", countryCode);
            } else {
                addressRecord.addStringParam("Country_id", country);
            }
        }
        addressRecord.addStringParam("isPrimary", "true");
        addressRecord.addStringParam("Address_id", "1");
   
        if(isAddressNew) {
            addressDS.addRecord(addressRecord);
        }
        
        if (userGet != null && userGet.getDatasetById("user") != null && userRecord != null) {

            Record customerGetRecord = userGet.getDatasetById("user").getRecord(0);

            Dataset ContactNumbers = customerGetRecord.getDatasetById("ContactNumbers") != null
                    ? customerGetRecord.getDatasetById("ContactNumbers")
                    : new Dataset();

            customerGetRecord.removeDatasetById("ContactNumbers");
            for (Record dbRec : ContactNumbers.getAllRecords()) {
            	String isPrimary = dbRec.getParamValueByName("isPrimary");
            	if (StringUtils.equalsIgnoreCase("true", isPrimary)) 
            		continue;
            	String phoneNumber = dbRec.getParamValueByName("Value");
				if (StringUtils.isNotBlank(phone) && phone.contains("-")) {
					String phoneCountryCode = (dbRec.getParamValueByName("phoneCountryCode")) + "-";
					String phoneNo = phoneCountryCode + phoneNumber;
					if (StringUtils.isNotBlank(phoneNo)) {
						if (StringUtils.isNotBlank(phone) && phone.equals(phoneNo)) {
							isphoneRecordSet = true;
						}
						phoneDs.addRecord(dbRec);
					}
				} else {
					if (StringUtils.isNotBlank(phoneNumber)) {
						if (StringUtils.isNotBlank(phone) && phone.equals(phoneNumber)) {
							isphoneRecordSet = true;
						}
						phoneDs.addRecord(dbRec);
					}
				}
			}

            ContactNumbers = customerGetRecord.getDatasetById("EmailIds") != null
                    ? customerGetRecord.getDatasetById("EmailIds")
                    : new Dataset();
            customerGetRecord.removeDatasetById("EmailIds");
            for (Record dbRec : ContactNumbers.getAllRecords()) {
            	String isPrimary = dbRec.getParamValueByName("isPrimary");
            	if (StringUtils.equalsIgnoreCase("true", isPrimary)) 
            		continue;
                String phoneNumber = dbRec.getParamValueByName("Value");

                if (StringUtils.isNotBlank(phoneNumber)) {
                    if (StringUtils.isNotBlank(email) && email.equals(phoneNumber)) {
                        isEmailRecordSet = true;
                    }

                    EmailIds.addRecord(dbRec);
                }
            }

            ContactNumbers = customerGetRecord.getDatasetById("Addresses") != null
                    ? customerGetRecord.getDatasetById("Addresses")
                    : new Dataset();

            customerGetRecord.removeDatasetById("Addresses");

            for (Record dbRec : ContactNumbers.getAllRecords()) {
            	if(StringUtils.isBlank(dbRec.getParamValueByName(UserConstants.ADDR_TYPE))) {
            		dbRec.addStringParam(UserConstants.ADDR_TYPE, UserConstants.ADDRESS_TYPE_HOME);
                }
            	String isPrimary = dbRec.getParamValueByName("isPrimary");
            	if (StringUtils.equalsIgnoreCase("true", isPrimary)) 
            		continue;
            	addressDS.addRecord(dbRec);
            }

            for (Param param : customerGetRecord.getAllParams()) {
                userRecord.addParam(param);
            }
            for (Record record : customerGetRecord.getAllRecords()) {
                userRecord.addRecord(record);
            }
            for (Dataset dataset : customerGetRecord.getAllDatasets()) {
                userRecord.addDataset(dataset);
            }

            String customerName = userRecord.getParamValueByName(CUSTOMER_NAME);
            if (StringUtils.isNotBlank(customerName) && customerName.contains(" ")) {
                userRecord.addStringParam(UserConstants.FIRSTNAME, customerName.substring(0, customerName.indexOf(' ')));
                userRecord.addStringParam(UserConstants.LASTNAME, customerName.substring(customerName.indexOf(' ') + 1));
            } else {
                userRecord.addStringParam(UserConstants.FIRSTNAME, customerGetRecord.getParamValueByName(UserConstants.FIRSTNAME));
                userRecord.addStringParam(UserConstants.LASTNAME, customerGetRecord.getParamValueByName(UserConstants.LASTNAME));
            }

            String bankName = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
                    TemenosConstants.PROP_PREFIX_TEMENOS, UserConstants.PROP_GENERAL, UserConstants.PROP_BANKNAME);
            userRecord.addStringParam(UserConstants.BANK_NAME, bankName);
        }

        if (!isphoneRecordSet && isPhoneSet && StringUtils.isNotBlank(phone)) {
            phoneDs.addRecord(phoneRecord);
        }

        if (!isEmailRecordSet && isEmailSet && StringUtils.isNotBlank(email)) {
            EmailIds.addRecord(emailRecord);
        }

        userRecord.addDataset(EmailIds);
        userRecord.addDataset(phoneDs);
        userRecord.addDataset(addressDS);
		
	}
    
	private void mergeAddresses(Result userGetOrch, Result result) {

		Record userRecord = result.getDatasetById(UserConstants.DATASET_USER) != null
				? result.getDatasetById(UserConstants.DATASET_USER).getRecord(0)
				: new Record();
		Dataset addressDS = userRecord.getDatasetById(UserConstants.DATASET_ADDRESS);
		if (addressDS == null) {
			addressDS = new Dataset(UserConstants.DATASET_ADDRESS);
			userRecord.addDataset(addressDS);
			userRecord.removeParamByName(UserConstants.DATASET_ADDRESS);
		}
		Record customerGetRecord = userGetOrch.getDatasetById("user").getRecord(0);
		Dataset addresses = customerGetRecord.getDatasetById("Addresses") != null
				? customerGetRecord.getDatasetById("Addresses")
				: new Dataset();
		customerGetRecord.removeDatasetById("Addresses");

		for (Record dbRec : addresses.getAllRecords()) {
			if (StringUtils.isBlank(dbRec.getParamValueByName(UserConstants.ADDR_TYPE))) {
				dbRec.addStringParam(UserConstants.ADDR_TYPE, UserConstants.ADDRESS_TYPE_HOME);
			}
			addressDS.addRecord(dbRec);
		}
	}

    /**
     * Call the dbpRbLocalServicesJava.getUserDetails() operation to retrieve the user's details for downstream
     * processing.
     * 
     * @param HashMap
     *            The hashmap containing the input parameters
     * @param DataControllerRequest
     *            Request object
     * @return Result The result object from calling the service
     **/
    private Result getDbxUser(HashMap<String, Object> params, DataControllerRequest request) throws Exception {

        String getUserServiceName = KONY_DBX_SERVICE_JAVA;
        String getUserOperationName = KONY_DBX_OP_JAVA_GETUSERDETAILS_CONCURRENT;

        // get user details from session start
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(X_KONY_AUTHORIZATION, request.getHeader(X_KONY_AUTHORIZATION));
        Result result = CommonUtils.callIntegrationService(request, null, headerMap, getUserServiceName,
                getUserOperationName, true);
        if(result.getDatasetById("customer") != null) {
            result.getDatasetById("customer").setId("user");
        }

        return result;
    }

    private Result getAssociatedCustomers(HashMap<String, Object> params, DataControllerRequest request)
            throws Exception {
        String getUserServiceName = KONY_DBX_EUM_SERVICE_JAVA;
        String getUserOperationName = OP_GET_ASSOCIATED_CUSTOMERS;
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(X_KONY_AUTHORIZATION, request.getHeader(X_KONY_AUTHORIZATION));
        Result result = CommonUtils.callIntegrationService(request, null, headerMap, getUserServiceName,
                getUserOperationName, true);
        return result;
    }
    
    private Result getPartyDetailsForUnenrolled(HashMap<String, Object> params
    		,DataControllerRequest request, String legalEntityId) {
    	Result result = new Result();
    	DBXResult result1 = new DBXResult();
    	Map<String, Object> headerMap = HelperMethods.addJWTAuthHeaderParty(request.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
		JsonObject contactDetails = new JsonObject();
		JsonObject address = new JsonObject();
		JsonObject email = new JsonObject();
		JsonArray contactDetailsarray = new JsonArray();
		JsonArray addressarray = new JsonArray();
		JsonArray emailarray = new JsonArray();
		JsonArray partyJsonArray = new JsonArray();
		String customerid = (String) params.get("Customer_id");
		String companyId = legalEntityId;
		String queryParams = "";
		String id = "";
		id = companyId + "-" + customerid;

		queryParams += "alternateIdentifierNumber=" + id;
		queryParams += "&";
		queryParams += "alternateIdentifierType=BackOfficeIdentifier";
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + "/api/v5.0.0/party/parties?"
				+ queryParams;
		DBXResult dbxResult1 = new DBXResult();
		dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null,
				headerMap);
		if (dbxResult1.getResponse() != null) {
			JsonElement partyResponse = new JsonParser().parse((String) dbxResult1.getResponse());
			if (partyResponse.isJsonObject()) {
				JsonObject partyResponseObject = partyResponse.getAsJsonObject();
				if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
						&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
					partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
				}
			}
		}

		JsonArray userdetails = new JsonArray();
		if (partyJsonArray.size() > 0) {
			JsonArray customerDetails = new JsonArray();
			for (JsonElement jsonelement : partyJsonArray) {
				JsonObject jsonObject = new JsonObject();
				
				jsonObject.addProperty("DateOfBirth",
				JSONUtil.getString(jsonelement.getAsJsonObject(), "dateOfBirth"));
				jsonObject.addProperty("customerName",
						(jsonelement.getAsJsonObject().has("firstName")
								&& !jsonelement.getAsJsonObject().get("firstName").isJsonNull()
								&& StringUtils
										.isNotBlank(jsonelement.getAsJsonObject().get("firstName").getAsString())
												? jsonelement.getAsJsonObject().get("firstName").getAsString()
												: "")
								+ " "
								+ (jsonelement.getAsJsonObject().has("lastName")
										&& !jsonelement.getAsJsonObject().get("lastName").isJsonNull()
										&& StringUtils.isNotBlank(
												jsonelement.getAsJsonObject().get("lastName").getAsString())
														? jsonelement.getAsJsonObject().get("lastName")
																.getAsString()
														: ""));
				jsonObject.addProperty("LastName", JSONUtil.getString(jsonelement.getAsJsonObject(), "lastName"));
    
				
				if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYADDRESS)
						&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).isJsonArray()
						&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
					customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray();
					for (JsonElement customerInfo : customerDetails) {
							
						if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
								&& "Electronic".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(),
										"communicationNature"))) {
							String electronicAddress=JSONUtil.getString(customerInfo.getAsJsonObject(),
									"electronicAddress");
							jsonObject.addProperty("email", electronicAddress);
							email.addProperty("isPrimary", JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"));
							email.addProperty("Extension", JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationType"));
							email.addProperty("Value", electronicAddress);
						}
						if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
								&& "Phone".equalsIgnoreCase(
										JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))) {
							 String phonePrefix = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"iddPrefixPhone");
								String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo");
								String phoneCountryCode = "";
								String phone = "";
								if (phoneNo.contains("-")) {
									phoneCountryCode = phoneNo.substring(0, phoneNo.indexOf("-"));
									phone = phoneNo.substring(phoneNo.indexOf("-") + 1);
									jsonObject.addProperty("phone", phoneCountryCode + "" + phone);
									contactDetails.addProperty("phoneCountryCode", phoneCountryCode);
									contactDetails.addProperty("Value", phone);
								} else {
									jsonObject.addProperty("phone", phonePrefix + "" + phoneNo);
									contactDetails.addProperty("phoneCountryCode", phonePrefix);
									contactDetails.addProperty("Value", phoneNo);
								}

							contactDetails.addProperty("isPrimary", JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"));
							contactDetails.addProperty("Extension", JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationType"));
						}

					   if ("Physical".equalsIgnoreCase(
							JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))
							&& "true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))) {

						   String streetName = JSONUtil.getString(customerInfo.getAsJsonObject(),"streetName");
						   String town = JSONUtil.getString(customerInfo.getAsJsonObject(), "town");
						   String postalOrZipCode = JSONUtil.getString(customerInfo.getAsJsonObject(),"postalOrZipCode");
						   String country = JSONUtil.getString(customerInfo.getAsJsonObject(), "countryCode");
						   String FlatNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "flatNumber"); 
						   String BuildingNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "buildingNumber"); 
						   String BuildingName = JSONUtil.getString(customerInfo.getAsJsonObject(), "buildingName"); 
						   String Floor = JSONUtil.getString(customerInfo.getAsJsonObject(), "floor"); 
						   String postBoxNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "postBoxNumber"); 
						   String countrySubdivision = JSONUtil.getString(customerInfo.getAsJsonObject(), "countrySubdivision"); 
						  
						   jsonObject.addProperty("city", town);
				 		   jsonObject.addProperty("zipcode", postalOrZipCode);
						   address.addProperty("addressCity", town);
		                   address.addProperty("City_id", town);
		                   address.addProperty("CityName", town);
		                   address.addProperty("Region_id", countrySubdivision);
							if (country != "") {
								address.addProperty("Country_id", country);
							} else if (countrySubdivision != "") {
								address.addProperty("Country_id", countrySubdivision.split("-")[0]);
							}
		                   address.addProperty("addressType",JSONUtil.getString(customerInfo.getAsJsonObject(), "addressType"));
						   address.addProperty("zipcode", postalOrZipCode);
						   address.addProperty("Zipcode", postalOrZipCode);	
						   address.addProperty("isPrimary", JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"));
						   if((FlatNumber=="") && (BuildingNumber=="") &&( BuildingName=="")&&
		                    		 (Floor=="")&&(postBoxNumber=="")) {
		                     if(customerInfo.getAsJsonObject().get("addressFreeFormat").getAsJsonArray().size() > 0) {
		                    	 JsonArray addressfreeformat = customerInfo.getAsJsonObject().get("addressFreeFormat").getAsJsonArray(); 
									JsonObject addressLine2 = (JsonObject) addressfreeformat.get(0);
									String addressLine = addressLine2.get("addressLine").getAsString();
									jsonObject.addProperty("addressLine1", streetName);
									address.addProperty("AddressLine1", streetName);
									address.addProperty("addressLine1", streetName);
									jsonObject.addProperty("addressLine2", addressLine);
									address.addProperty("addressLine2", addressLine);
									address.addProperty("AddressLine2", addressLine);
								}
							} else if (StringUtils.isNotBlank(BuildingName)) {
								jsonObject.addProperty("addressLine1", BuildingName);
								address.addProperty("AddressLine1", BuildingName);
								address.addProperty("addressLine1", BuildingName);
								jsonObject.addProperty("addressLine2", streetName);
								address.addProperty("addressLine2", streetName);
								address.addProperty("AddressLine2", streetName);
							}

						}

					}
				}
				
				contactDetailsarray.add(contactDetails);
				emailarray.add(email);
				addressarray.add(address);

				jsonObject.add("ContactNumbers", contactDetailsarray);
				jsonObject.add("EmailIds", emailarray);
				jsonObject.add("Addresses", addressarray);
				userdetails.add(jsonObject);
				JsonObject jsonobject = new JsonObject();
				jsonobject.add("user", userdetails);	
				result1.setResponse(jsonobject);
				JsonObject jsonObject1 = (JsonObject) result1.getResponse();
				result = ConvertJsonToResult.convert(jsonObject1);
				
			}
			
		}
		return result;
	}
    
    private Result getPartyDetailsForEenrolled(HashMap<String, Object> params
    		,String partyId, String legalEntityId, DataControllerRequest request) {
    	Result result = new Result();
    	DBXResult result1 = new DBXResult();
		String id="";
		JsonObject contactDetails = new JsonObject();
		JsonObject address = new JsonObject();
		JsonObject email = new JsonObject();
		JsonArray contactDetailsarray = new JsonArray();
		JsonArray addressarray = new JsonArray();
		JsonArray emailarray = new JsonArray();
		JsonArray userdetails = new JsonArray();
		Map<String, Object> headerMap = HelperMethods.addJWTAuthHeaderParty(request.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ "/api/v5.0.0/party/parties/"+ partyId;
		DBXResult dbxResult1 = new DBXResult();
		dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
		 JsonObject customerJson = JSONUtil.parseAsJsonObject((String) dbxResult1.getResponse());
		 JsonObject jsonObject = new JsonObject();
			
			jsonObject.addProperty("DateOfBirth",
			JSONUtil.getString(customerJson, "dateOfBirth"));
			jsonObject.addProperty("customerName",
					(customerJson.has("firstName")
							&& !customerJson.get("firstName").isJsonNull()
							&& StringUtils
									.isNotBlank(customerJson.get("firstName").getAsString())
											? customerJson.get("firstName").getAsString()
											: "")
							+ " "
							+ (customerJson.has("lastName")
									&& !customerJson.get("lastName").isJsonNull()
									&& StringUtils.isNotBlank(
											customerJson.get("lastName").getAsString())
													? customerJson.get("lastName")
															.getAsString()
													: ""));
			jsonObject.addProperty("LastName", JSONUtil.getString(customerJson, "lastName"));

			if (JSONUtil.hasKey(customerJson, DTOConstants.PARTYADDRESS)
					&& customerJson.get(DTOConstants.PARTYADDRESS).isJsonArray()
					&& customerJson.get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
				JsonArray customerDetails = customerJson.get(DTOConstants.PARTYADDRESS).getAsJsonArray();
				for (JsonElement customerInfo : customerDetails) {
					
					if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
							&& "Electronic".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(),
									"communicationNature"))) {
						String electronicAddress=JSONUtil.getString(customerInfo.getAsJsonObject(),
								"electronicAddress");
						jsonObject.addProperty("email", electronicAddress);
						email.addProperty("isPrimary",JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"));
						email.addProperty("Extension", JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationType"));
						email.addProperty("Value", electronicAddress);
					}
					if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
							&& "Phone".equalsIgnoreCase(
									JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))) {
						 String phonePrefix = JSONUtil.getString(customerInfo.getAsJsonObject(),
									"iddPrefixPhone");
								String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo");
								String phoneCountryCode = "";
								String phone = "";
								if (phoneNo.contains("-")) {
									phoneCountryCode = phoneNo.substring(0, phoneNo.indexOf("-"));
									phone = phoneNo.substring(phoneNo.indexOf("-") + 1);
									jsonObject.addProperty("phone", phoneCountryCode + "" + phone);
									contactDetails.addProperty("phoneCountryCode", phoneCountryCode);
									contactDetails.addProperty("Value", phone);
								} else {
									jsonObject.addProperty("phone", phonePrefix + "" + phoneNo);
									contactDetails.addProperty("phoneCountryCode", phonePrefix);
									contactDetails.addProperty("Value", phoneNo);
								}

							contactDetails.addProperty("isPrimary", JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"));
							contactDetails.addProperty("Extension", JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationType"));
						}

				   if ("Physical".equalsIgnoreCase(
						JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))
						&& "true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))) {
					   String streetName = JSONUtil.getString(customerInfo.getAsJsonObject(),"streetName");
					   String town = JSONUtil.getString(customerInfo.getAsJsonObject(), "town");
					   String postalOrZipCode = JSONUtil.getString(customerInfo.getAsJsonObject(),"postalOrZipCode");
					   String country = JSONUtil.getString(customerInfo.getAsJsonObject(), "countryCode");
					   String FlatNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "flatNumber"); 
					   String BuildingNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "buildingNumber"); 
					   String BuildingName = JSONUtil.getString(customerInfo.getAsJsonObject(), "buildingName"); 
					   String Floor = JSONUtil.getString(customerInfo.getAsJsonObject(), "floor"); 
					   String postBoxNumber = JSONUtil.getString(customerInfo.getAsJsonObject(), "postBoxNumber"); 
					   String countrySubdivision = JSONUtil.getString(customerInfo.getAsJsonObject(), "countrySubdivision"); 
				   
					   jsonObject.addProperty("city", town);
					   jsonObject.addProperty("zipcode", postalOrZipCode);
					   address.addProperty("addressCity", town);
	                   address.addProperty("City_id", town);
	                   address.addProperty("CityName", town);
	                   address.addProperty("Region_id", countrySubdivision);
	                   if (country != "") {
							address.addProperty("Country_id", country);
						} else if (countrySubdivision != "") {
							address.addProperty("Country_id", countrySubdivision.split("-")[0]);
						}
	                   address.addProperty("AddressType",JSONUtil.getString(customerInfo.getAsJsonObject(), "addressType"));			   
					   address.addProperty("zipcode", postalOrZipCode);
					   address.addProperty("Zipcode", postalOrZipCode);	
					   address.addProperty("isPrimary", JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"));
					   if((FlatNumber=="") && (BuildingNumber=="") &&( BuildingName=="")&&
	                    		 (Floor=="")&&(postBoxNumber=="")) {
	                     if(customerInfo.getAsJsonObject().get("addressFreeFormat").getAsJsonArray().size() > 0) {
	                    	 JsonArray addressfreeformat = customerInfo.getAsJsonObject().get("addressFreeFormat").getAsJsonArray(); 
	                    	 JsonObject addressLine2 = (JsonObject) addressfreeformat.get(0);
	                    	 String addressLine = addressLine2.get("addressLine").getAsString();
	                    	 jsonObject.addProperty("addressLine1", streetName);
	                    	 address.addProperty("AddressLine1", streetName);
	  					   	 address.addProperty("addressLine1", streetName);
	                    	 jsonObject.addProperty("addressLine2", addressLine);
	                    	 address.addProperty("addressLine2", addressLine);
	                    	 address.addProperty("AddressLine2", addressLine);
							}
						} else if (StringUtils.isNotBlank(BuildingName)) {
							jsonObject.addProperty("addressLine1", BuildingName);
							address.addProperty("AddressLine1", BuildingName);
							address.addProperty("addressLine1", BuildingName);
							jsonObject.addProperty("addressLine2", streetName);
							address.addProperty("addressLine2", streetName);
							address.addProperty("AddressLine2", streetName);
						}
					}
				}
			}
			contactDetailsarray.add(contactDetails);
			emailarray.add(email);
			addressarray.add(address);

			if (JSONUtil.hasKey(customerJson, "alternateIdentities")
					&& customerJson.get("alternateIdentities").isJsonArray()
					&& customerJson.get("alternateIdentities").getAsJsonArray().size() > 0) {
				JsonArray customerDetails = customerJson.get("alternateIdentities").getAsJsonArray();
				for (JsonElement customerInfos : customerDetails) {
					String identityNumber = JSONUtil.getString(customerInfos.getAsJsonObject(),
							"identityNumber");
					String[] identityNumberArray = identityNumber.split("-");
					if (identityNumberArray != null && identityNumberArray.length == 2) {
						id = identityNumberArray[1];
						jsonObject.addProperty("customerId", id);

					}
				}
			}
			jsonObject.add("ContactNumbers", contactDetailsarray);
			jsonObject.add("EmailIds", emailarray);
			jsonObject.add("Addresses", addressarray);
			userdetails.add(jsonObject);
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("user", userdetails);	
			result1.setResponse(jsonobject);
			JsonObject jsonObject1 = (JsonObject) result1.getResponse();
			result = ConvertJsonToResult.convert(jsonObject1);
             
	 return result;
    }
    	
}