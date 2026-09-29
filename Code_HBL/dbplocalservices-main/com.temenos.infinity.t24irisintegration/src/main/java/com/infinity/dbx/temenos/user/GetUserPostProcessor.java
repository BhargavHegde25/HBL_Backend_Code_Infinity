package com.infinity.dbx.temenos.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class GetUserPostProcessor extends BasePostProcessor implements TemenosConstants, UserConstants, Constants {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        Result userGet = getDbxUser(request);

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        // Read customer table
        Record userRecord = result.getDatasetById(DATASET_USER) != null
                ? result.getDatasetById(DATASET_USER).getRecord(0)
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
        Record emailRecord = new Record();
        boolean isphoneRecordSet = false;
        boolean isEmailRecordSet = false;
        boolean isEmailSet = false;
        //Dataset contactDetails = userRecord.getDatasetById("contactDetails");
        /***
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
        }else {
        	diagnostic.debug("In HBL Else No phone");
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
        }else {
        	diagnostic.debug("In HBL Else No Email");
        }

***/
        
        Result res = getContactInfo(request);
    	diagnostic.debug("Contact result ##:"+ ResultToJSON.convert(res));
    	Dataset ds = res.getDatasetById("Contacts");
    	Record PhoneRec = ds.getAllRecords().get(0);
    	Record EmailRec = ds.getAllRecords().get(1);
    	String ph = "";
    	String phPrefix = "";
    	String email = "";
    	//{"contactData":"8124616448","iddPrefixPhone":"+91","contactType":"MOBILE"},
    	//{"contactData":"muragaiah.ojili@bahwancybertek.com","iddPrefixPhone":"","contactType":"EMAIL"}
    	if(PhoneRec.getParamValueByName("contactType").equalsIgnoreCase("MOBILE")) {
    		ph = PhoneRec.getParamValueByName("contactData");//phone number
    		phPrefix = PhoneRec.getParamValueByName("iddPrefixPhone");//phone number prefix
    	}else if(PhoneRec.getParamValueByName("contactType").equalsIgnoreCase("EMAIL")) {
    		email = PhoneRec.getParamValueByName("contactData");//Email
    	}
    	
    	if(EmailRec.getParamValueByName("contactType").equalsIgnoreCase("MOBILE")) {
    		ph = EmailRec.getParamValueByName("contactData");//phone number
    		phPrefix = EmailRec.getParamValueByName("iddPrefixPhone");//phone number prefix
    	}else if(EmailRec.getParamValueByName("contactType").equalsIgnoreCase("EMAIL")) {
    		email = EmailRec.getParamValueByName("contactData");//Email
    	}
    	
    	diagnostic.debug("Customer Phone nubmer: ##"+ phPrefix + "-" + ph);
    	diagnostic.debug("Customer Email: ##"+ email);
        
    	
    	   phoneRecord.addParam("phoneCountryCode", phPrefix);
    	   phoneRecord.addParam("Value", ph);
           phoneRecord.addParam("isPrimary", "true");
           phoneRecord.addParam("Extension", "Mobile");
           phoneRecord.addParam("Type_id", "COMM_TYPE_PHONE");
           phoneRecord.addParam("id", "1");
           isPhoneSet = true;
           
           emailRecord.addParam("Value", email);
           emailRecord.addParam("isPrimary", "true");
           emailRecord.addParam("Type_id", "COMM_TYPE_EMAIL");
           emailRecord.addParam("id", "1");

           isEmailSet = true;
           
           
        Dataset addressDS = userRecord.getDatasetById(DATASET_ADDRESS);
        if (addressDS == null) {
            addressDS = new Dataset(DATASET_ADDRESS);
            userRecord.addDataset(addressDS);
            userRecord.removeParamByName(DATASET_ADDRESS);
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
          
        addressRecord.addStringParam(ADDR_TYPE, ADDRESS_TYPE_HOME);
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
        if(StringUtils.isBlank(addressRecord.getParamValueByName(ADDR_TYPE))) {
            addressRecord.addStringParam(ADDR_TYPE, ADDRESS_TYPE_HOME);
        }

        if(StringUtils.isNotBlank(CommonUtils.getParamValue(addressRecord, "country"))) {
            String country = CommonUtils.getParamValue(addressRecord, "country");
            inputParams.put(PARAM_DOLLAR_FILTER, "Name eq '" + country + "'");
            Result countryGet = CommonUtils.callIntegrationService(request, inputParams, null,
                    TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_COUNTRY_GET, false);
            if (countryGet.getDatasetById(DS_COUNTRY) != null
                    && !countryGet.getDatasetById(DS_COUNTRY).getAllRecords().isEmpty()) {
                String countryCode = countryGet.getDatasetById(DS_COUNTRY).getRecord(0).getParamValueByName("Code");
                addressRecord.addStringParam("CountryCode", countryCode);
            } else {
                addressRecord.addStringParam("CountryCode", country);
            }
        }
        addressRecord.addStringParam("isPrimary", "true");
        addressRecord.addStringParam("Address_id", "1");
   
        if(isAddressNew) {
            addressDS.addRecord(addressRecord);
        }

        if (userGet != null && userGet.getDatasetById("customer") != null && userRecord != null) {

            Record customerGetRecord = userGet.getDatasetById("customer").getRecord(0);

            Dataset ContactNumbers = customerGetRecord.getDatasetById("ContactNumbers") != null
                    ? customerGetRecord.getDatasetById("ContactNumbers")
                    : new Dataset();
            /**
            customerGetRecord.removeDatasetById("ContactNumbers");
            for (Record dbRec : ContactNumbers.getAllRecords()) {
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
                String phoneNumber = dbRec.getParamValueByName("Value");

                if (StringUtils.isNotBlank(phoneNumber)) {
                    if (StringUtils.isNotBlank(email) && email.equals(phoneNumber)) {
                        isEmailRecordSet = true;
                    }

                    EmailIds.addRecord(dbRec);
                }
            } **/

            ContactNumbers = customerGetRecord.getDatasetById("Addresses") != null
                    ? customerGetRecord.getDatasetById("Addresses")
                    : new Dataset();
            customerGetRecord.removeDatasetById("Addresses");

            for (Record dbRec : ContactNumbers.getAllRecords()) {
            	if(StringUtils.isBlank(dbRec.getParamValueByName(ADDR_TYPE))) {
            		dbRec.addStringParam(ADDR_TYPE, ADDRESS_TYPE_HOME);
                }
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
                userRecord.addStringParam(FIRSTNAME, customerName.substring(0, customerName.indexOf(' ')));
                userRecord.addStringParam(LASTNAME, customerName.substring(customerName.indexOf(' ') + 1));
            } else {
                userRecord.addStringParam(FIRSTNAME, customerGetRecord.getParamValueByName(FIRSTNAME));
                userRecord.addStringParam(LASTNAME, customerGetRecord.getParamValueByName(LASTNAME));
            }

            String bankName = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
                    TemenosConstants.PROP_PREFIX_TEMENOS, PROP_GENERAL, PROP_BANKNAME);
            userRecord.addStringParam(UserConstants.BANK_NAME, bankName);
        }

       // if (!isphoneRecordSet && isPhoneSet && StringUtils.isNotBlank(phone)) {
            phoneDs.addRecord(phoneRecord);
       // }

       // if (!isEmailRecordSet && isEmailSet && StringUtils.isNotBlank(email)) {
            EmailIds.addRecord(emailRecord);
       // }
        
        diagnostic.debug("phoneDs: ##"+ phoneDs.toString());

        userRecord.addDataset(EmailIds);
        userRecord.addDataset(phoneDs);
        userRecord.addDataset(addressDS);

        return result;
    }
    
    private Result getContactInfo(DataControllerRequest dcRequest) throws Exception {
    	Result contactInfoResult = new Result();
    	String serviceName = "ArrangementT24ISAccountsCustom";
    	String operationName = "getContactDetailsCustom";
    	HashMap<String, Object> inputParams = new HashMap<String, Object>();
    	String backendId = getCoreBackendId(dcRequest);
    	diagnostic.prepareDebug("backendIdentifier is ##" + backendId).log();
    	inputParams.put("customerId", backendId);
    	dcRequest.addRequestParam_("customerId", backendId);
    	HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
    	contactInfoResult = CommonUtils.callIntegrationService(dcRequest, inputParams, serviceHeaders, serviceName,
				operationName, true);
    	
    	return contactInfoResult;
    }
    
    public String getCoreBackendId(DataControllerRequest dcreq)  {		
    	String backendId = null;
		try {
			
			if (dcreq.getServicesManager().getIdentityHandler() != null) {
				Map<String, Object> userAttributesMap = dcreq.getServicesManager().getIdentityHandler().getUserAttributes();               
				String backendIdentifier = (String)userAttributesMap.get("backendIdentifiers");
				if(diagnostic.isDebugEnabled()){
					diagnostic.prepareDebug("backendIdentifier is" + backendIdentifier).log();
				}
				if(StringUtils.isNotBlank(backendIdentifier)) { 				
					backendId = getCoreIDFromJson(backendIdentifier);				
				}
			}
			else
			{
				alert.prepareError("NULL IDENTITYHANDLER").log();
			}
			
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();	
			
		}
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("backendId is" + backendId).log();
		}
		return backendId;

	}
    protected static String getCoreIDFromJson(String backendIdentifier) {
		String backendId = null;
		JsonObject backendIdentifiersJSON = new JsonParser().parse(backendIdentifier).getAsJsonObject();
		if(backendIdentifiersJSON.entrySet().size() == 1) {
			for ( Entry<String, JsonElement> entry : backendIdentifiersJSON.entrySet()) {
				backendId = getBackendIdFromCoreType(backendIdentifiersJSON, entry.getKey());
			}
			if(diagnostic.isDebugEnabled()){
				diagnostic.prepareDebug("backendId is" + backendId).log();
			}
		}else {
			String coreType = null;
			try {
				coreType = EnvironmentConfigurationsHandler.getServerAppProperty("ALERTS_CORETYPE");
			} catch (Exception e) {
				alert.prepareError("ALERTS_CORETYPE is not available" ,e).log();
				
			}
			if(coreType == null)
			{
				alert.prepareError("ALERTS_CORETYPE is not available").log();
			}
			else
			{
				if(StringUtils.isNotEmpty(coreType) && backendIdentifiersJSON.has(coreType)){					
					backendId = getBackendIdFromCoreType(backendIdentifiersJSON,coreType);
				}
				if(diagnostic.isDebugEnabled()){
					diagnostic.prepareDebug("backendId is" + backendId).log();
				}
			}
			
		}
		return backendId;
	}

	protected static String getBackendIdFromCoreType(JsonObject backendIdentifiersJSON, String key) {
		JsonArray backendTypeObj = backendIdentifiersJSON.get(key).getAsJsonArray();
		String backendId = null;
		if(backendTypeObj.size() > 0) {
			backendId  =	backendTypeObj.get(0).getAsJsonObject().get("BackendId").getAsString();
		}
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("backendId is" + backendId).log();
		}
		return backendId;
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
    private Result getDbxUser(DataControllerRequest request) throws Exception {

        if (Boolean.parseBoolean(request.getParameter("isSuperAdmin"))
                && !Boolean.parseBoolean(request.getParameter("isCustomerPresent"))) {
            return new Result();
        }

        String getUserServiceName = KONY_DBX_SERVICE_JAVA;
        String getUserOperationName = KONY_DBX_OP_JAVA_GETUSERDETAILS_CONCURRENT;
        
        request.addRequestParam_("isCallingFromT24", "true");
        
        Result result = CommonUtils.callIntegrationService(request, null, request.getHeaderMap(), getUserServiceName,
                getUserOperationName, true);

        return result;
    }
}
