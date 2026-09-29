package com.temenos.infinity.api.usermanagement.javaservice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.infinity.api.usermanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.resource.api.UserManagementResource;


/**
 * 
 * @author sailendra
 * @version Java Service to update primary Address of a customer with document Proof
 * 
 */

public class UpdatePrimaryAddressWithDocsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
        DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            HashMap < String, Object > dataMap = new HashMap < String, Object > ();
            Result result = new Result();
            HashMap < String, Object > headerMap = new HashMap < String, Object > ();
            Map<String, Object> userAttributes = CustomerSessionsUtil.getLoggedInUserAttributesMap(request);
            String username = userAttributes.get("UserName").toString();
            JSONObject requestPayload = getJSONFromRequest(request);
            if (!((username.equals(request.getParameter("userName")) && username.equals(request.getParameter("modifiedByName"))))) {
                return ErrorCodeEnum.ERR_200533.setErrorCode(new Result());
            }
            dataMap.put("Documents", requestPayload.get("Documents"));
            JSONObject uploadDocResponse = new JSONObject();
            CustomerDetailsDTO customerDetailsDTO = constructPayLoad(request);
            if (requestPayload.has("Documents")) {
                 boolean isValid=true;
                JSONArray documents = new JSONArray(requestPayload.get("Documents").toString());
                for (int iterator = 0; iterator < documents.length(); iterator++) {
                    JSONObject document = documents.getJSONObject(iterator);
                    String fileName = document.getString("fileName");
                    isValid =validFileName(fileName.substring(0,fileName.lastIndexOf("."))); 
                    if(!isValid)
                        break;
                    }
               UserManagementResource updateCustomerDetailsResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
                if(isValid){
                String DocResponse = updateCustomerDetailsResource.uploadDocsForChangeRequest(request);
                if (StringUtils.isNotBlank(DocResponse)) {
                    alert.prepareError("Document Response" + DocResponse).log();
                    uploadDocResponse = new JSONObject(DocResponse);
                    if (uploadDocResponse.has("supportingDocumentData")) {
                        customerDetailsDTO.setSupportingDocumentIds(uploadDocResponse.get("supportingDocumentData").toString());
                      } else {
                        return ErrorCodeEnum.ERR_200532.setErrorCode(new Result());
                      }
                }}
                else{
                  Result errorResult =new Result();
                    errorResult.addErrMsgParam("Filename  should be in Alphanumeric.");
                    return errorResult; 

        } 
                headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
                headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

                result = updateCustomerDetailsResource.updateCustomerDetails(customerDetailsDTO, request, headerMap);
            }
            return result;
        } catch (Exception e) {
            alert.prepareError("Unable to create order : " + e).log();
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
        }
    }
    public static JSONObject getJSONFromRequest(DataControllerRequest request) {
        JSONObject json = new JSONObject();
        request.getParameterNames().forEachRemaining(name -> {
            json.put(name, request.getParameter(name));
        });
        return json;
    }
     public boolean validFileName(String name) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
       
    }
    public static CustomerDetailsDTO constructPayLoad(DataControllerRequest request) {

        CustomerDetailsDTO customerDetailsDTO = new CustomerDetailsDTO();
        if (request.containsKeyInRequest("UpdatePrimaryAddress")) {

            JsonArray addresses = new JsonParser().parse(request.getParameter("UpdatePrimaryAddress")).getAsJsonArray();
            if (addresses.toString().contains("Addr_id")) {
                String Addr_id = addresses.get(0).getAsJsonObject().get("Addr_id").getAsString() != null ?
                    addresses.get(0).getAsJsonObject().get("Addr_id").getAsString() :
                    "";
                customerDetailsDTO.setAddr_id(Addr_id);
            }

            String Addr_type = addresses.get(0).getAsJsonObject().get("Addr_type").getAsString() != null ?
                addresses.get(0).getAsJsonObject().get("Addr_type").getAsString() :
                "";
            String isPrimary = addresses.get(0).getAsJsonObject().get("isPrimary").getAsString() != null ?
                addresses.get(0).getAsJsonObject().get("isPrimary").getAsString() :
                "";
            String addrLine1 = addresses.get(0).getAsJsonObject().get("addrLine1").getAsString() != null ?
                addresses.get(0).getAsJsonObject().get("addrLine1").getAsString() :
                "";
            String addrLine2 = addresses.get(0).getAsJsonObject().get("addrLine2").getAsString() != null ?
                addresses.get(0).getAsJsonObject().get("addrLine2").getAsString() :
                "";
            String customerID = addresses.get(0).getAsJsonObject().get("customerID").getAsString() != null ?
                    addresses.get(0).getAsJsonObject().get("customerID").getAsString() :
                    "";
            String customerName = addresses.get(0).getAsJsonObject().get("customerName").getAsString() != null ?
                    addresses.get(0).getAsJsonObject().get("customerName").getAsString() :
                    "";
            String City_id = addresses.get(0).getAsJsonObject().get("citySelected").getAsString() != null ?
                      addresses.get(0).getAsJsonObject().get("citySelected").getAsString() :
                      "";
            String ZipCode = addresses.get(0).getAsJsonObject().get("zipcode").getAsString() != null ?
                      addresses.get(0).getAsJsonObject().get("zipcode").getAsString() :
                      "";
            if (addresses.toString().contains("Region_id")) {
                String Region_id = addresses.get(0).getAsJsonObject().get("Region_id").getAsString() != null ?
                       addresses.get(0).getAsJsonObject().get("Region_id").getAsString() :
                       "";
                       customerDetailsDTO.setRegion_id(Region_id);
                    }

            String countryCode = addresses.get(0).getAsJsonObject().get("countrySelected").getAsString() != null ?
                      addresses.get(0).getAsJsonObject().get("countrySelected").getAsString() :
                      "";
            addresses.remove(0);
            String requestDetails = addresses.toString();
            String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
            String modifiedByName = request.getParameter("modifiedByName") != null ?
                request.getParameter("modifiedByName") :
                "";
            customerDetailsDTO.setAddressType(Addr_type);
            customerDetailsDTO.setPrimaryFlag(isPrimary);
            customerDetailsDTO.setAddrLine1(addrLine1);
            customerDetailsDTO.setAddrLine2(addrLine2);
            customerDetailsDTO.setCity_id(City_id);
            customerDetailsDTO.setZipCode(ZipCode);
            customerDetailsDTO.setCountryCode(countryCode);
            customerDetailsDTO.setUserName(userName);
            customerDetailsDTO.setModifiedByName(modifiedByName);
            customerDetailsDTO.setDetailToBeUpdated("UpdatePrimaryAddress");
            customerDetailsDTO.setRequestDetails(requestDetails);
            customerDetailsDTO.setCustomerId(customerID);
            customerDetailsDTO.setCustomerName(customerName);
            customerDetailsDTO.setAddressType(Addr_type);
            if (StringUtils.isNotBlank(customerDetailsDTO.getAddr_id())) {
                customerDetailsDTO.setOperation("UpdateAddress");
            } else {
                customerDetailsDTO.setOperation("createAddress");
            }

        }

        return customerDetailsDTO;
    }

}
