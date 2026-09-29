package com.infinity.dbx.temenos.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class HBLGetUserPostProcessor extends BasePostProcessor implements TemenosConstants, UserConstants, Constants {
	LoggerUtil logger = new LoggerUtil(HBLGetUserPostProcessor.class);
	 @Override
	    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
	            throws Exception {

	        Result userGet = getDbxUser(request);
	        JSONObject responseObj= new JSONObject(ResultToJSON.convert(result));
	        logger.debug("HBL::HBLGetUserPostProcessor::responseObj:"+responseObj);
	        JSONArray contactDetailsArray= null;
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
	        boolean isphoneRecordSet = false;
	        boolean isEmailRecordSet = false;
	        Dataset contactDetails = userRecord.getDatasetById("contactDetails");
	    	contactDetailsArray=responseObj.has("contactDetails1")?responseObj.getJSONArray("contactDetails1"):new JSONArray();
	        logger.debug("HBL::HBLGetUserPostProcessor::isPhoneSet:"+isPhoneSet);
	        logger.debug("HBL::HBLGetUserPostProcessor::contactDetailsArray:"+contactDetailsArray);
	        if (!isPhoneSet && contactDetailsArray != null && contactDetailsArray.length()>0) {
	            	phone=contactDetailsArray.getJSONObject(0).has("contactType") && contactDetailsArray.getJSONObject(0).getString("contactType").equalsIgnoreCase("MOBILE")?contactDetailsArray.getJSONObject(0).getString("contactData"):"";
	            	 logger.debug("HBL::HBLGetUserPostProcessor::phone:"+phone);
	            	if(contactDetailsArray.getJSONObject(0).has("iddPrefixPhone"))
		             phoneRecord.addParam("phoneCountryCode", contactDetailsArray.getJSONObject(0).getString("iddPrefixPhone"));
	            	}
	        if (StringUtils.isNotBlank(phone)) {
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
	                phoneDs.addRecord(phoneRecord);
	                isPhoneSet = true;
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
	        if (!isEmailSet && contactDetailsArray != null && contactDetailsArray.length()>0) {
	            	email=contactDetailsArray.getJSONObject(1).has("contactType") && contactDetailsArray.getJSONObject(1).getString("contactType").equalsIgnoreCase("EMAIL")?contactDetailsArray.getJSONObject(1).getString("contactData"):"";
	            	logger.debug("HBL::HBLGetUserPostProcessor::email:"+email);
	            	if(StringUtils.isNotBlank(email))
	     	        emailRecord.addParam("Value", email);
	            }
	            emailRecord.addParam("isPrimary", "true");
	            emailRecord.addParam("Type_id", "COMM_TYPE_EMAIL");
	            emailRecord.addParam("id", "1");
	            EmailIds.addRecord(emailRecord);
	            isEmailSet = true;
	        logger.debug("HBL::HBLGetUserPostProcessor::emailRecord:"+emailRecord);
	        logger.debug("HBL::HBLGetUserPostProcessor::phoneRecord:"+phoneRecord);
	        
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
	        logger.debug("HBL::HBLGetUserPostProcessor::userGet:"+ResultToJSON.convert(userGet));
	        if (userGet != null && userGet.getDatasetById("customer") != null && userRecord != null) {

	            Record customerGetRecord = userGet.getDatasetById("customer").getRecord(0);
	            
	            Dataset ContactNumbers = customerGetRecord.getDatasetById("ContactNumbers") != null
	                    ? customerGetRecord.getDatasetById("ContactNumbers")
	                    : new Dataset();

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
	            }

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

	        if (!isphoneRecordSet && isPhoneSet && StringUtils.isNotBlank(phone)) {
	            phoneDs.addRecord(phoneRecord);
	        }

	        if (!isEmailRecordSet && isEmailSet && StringUtils.isNotBlank(email)) {
	            EmailIds.addRecord(emailRecord);
	        }

	        userRecord.addDataset(EmailIds);
	        userRecord.addDataset(phoneDs);
	        userRecord.addDataset(addressDS);

	        return result;
	    }
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
	 public static JSONObject convertRecordToJSONObject(Record record) {

			JSONObject jsonObj = new JSONObject();

			List<Param> arList = record.getAllParams();

			Iterator<Param> it = arList.iterator();
			while (it.hasNext()) {
				Param p = it.next();
				String key = p.getName();
				jsonObj.put(key, p.getValue());
			}
			return jsonObj;
		}
	 public static JSONArray convertDatasetToJSONArray(Dataset dataset) {
	        JSONArray array = new JSONArray();
	        List<Record> records = new ArrayList<>();

	        if (dataset != null && dataset.getAllRecords().size() != 0) {
	            records = dataset.getAllRecords();
	        }

	        for (int i = 0; i < records.size(); i++) {
	            array.put(convertRecordToJSONObject(records.get(i)));
	        }
	        return array;
	    }
}
