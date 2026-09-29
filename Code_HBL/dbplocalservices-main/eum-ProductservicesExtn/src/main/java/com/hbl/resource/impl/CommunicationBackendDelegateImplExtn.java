package com.hbl.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hbl.resource.constants.HBLConstants;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.CommunicationBackendDelegateImpl;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CommunicationBackendDelegateImplExtn extends CommunicationBackendDelegateImpl{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	 @Override
	    public DBXResult getPrimaryMFACommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
	            Map<String, Object> headerMap) {
	        DBXResult dbxResult = new DBXResult();
	        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
	            return dbxResult;
	        }
	        Map<String, Object> inputParams = new HashMap<>();
	        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
	        try {
	            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
	                DBXResult response = getMFACommunicationDetailsForT24(customerCommunicationDTO, headerMap);
	                if (response != null && response.getResponse() != null)
	                    return response;
	            }
	            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id()
	                    + DBPUtilitiesConstants.AND +
	                    "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";
	            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
	            JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    URLConstants.CUSTOMER_COMMUNICATION_GET);
	            dbxResult.setResponse(communicationJson);
	        } catch (Exception e) {

	        }
	        return dbxResult;
	    }
	 private DBXResult getMFACommunicationDetailsForT24(CustomerCommunicationDTO customerCommunicationDTO,
	            Map<String, Object> headersMap) {
	        DBXResult response = new DBXResult();
	        try {
	            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
	            backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
	            if (StringUtils.isNotBlank(customerCommunicationDTO.getCompanyLegalUnit())) {
					backendIdentifierDTO.setCompanyLegalUnit(customerCommunicationDTO.getCompanyLegalUnit());
				}
	            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
	                    .get(backendIdentifierDTO, headersMap);
	            BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
	            if (identifierDTO == null)
	                return null;
	            Map<String, Object> inputParams = new HashMap<>();
	            inputParams.put("customerId", identifierDTO.getBackendId());
	            HelperMethods.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
	            /*This BRANCH_ID_REFERENCE usage is defined as a fall back*/
	            if(StringUtils.isBlank(identifierDTO.getCompanyLegalUnit())) {
	            headersMap.put("companyId",
	                    EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
	            }
	            else{
	                headersMap.put("companyId",
	                        identifierDTO.getCompanyLegalUnit());
	            }
	            /*JsonObject t24Response = ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISUSER_INTEGRATION_SERVICE,
	                    null, OperationName.CORE_CUSTOMER_SEARCH,
	                    inputParams, headersMap);
	                    */
	            JsonObject t24Response = ServiceCallHelper.invokeServiceAndGetJson(HBLConstants.HBL_T24ISUSER_INTEGRATION_SERVICE,null, OperationName.CORE_CUSTOMER_SEARCH,inputParams, headersMap);

	            JsonObject customerResponse = new JsonObject();
	            if (!JSONUtil.hasKey(t24Response, DBPDatasetConstants.DATASET_CUSTOMERS)
	                    || t24Response.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray().size() < 0) {
	                alert.prepareError("Exception occured while fetching the customer communication details").log();
	            }
	            customerResponse =
	                    t24Response.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray().get(0).getAsJsonObject();
	            JsonArray communication = new JsonArray();
	            JsonObject obj = new JsonObject();
	            obj.addProperty("Type_id", DBPUtilitiesConstants.COMM_TYPE_EMAIL);
	            obj.addProperty("Value", JSONUtil.getString(customerResponse, "email"));
	            communication.add(obj);
	            obj = new JsonObject();
	            obj.addProperty("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
	            obj.addProperty("Value", JSONUtil.getString(customerResponse, "phone"));
	            communication.add(obj);
	            obj = new JsonObject();
	            obj.addProperty("Type_id", InfinityConstants.ssn);
	            obj.addProperty("Value", JSONUtil.getString(customerResponse,InfinityConstants.ssn));
	            communication.add(obj);
	            obj = new JsonObject();
	            obj.addProperty("Type_id", InfinityConstants.dob);
	            obj.addProperty("Value", JSONUtil.getString(customerResponse, InfinityConstants.dateOfBirth));
	            communication.add(obj);
	            JsonObject jsonObject = new JsonObject();
	            jsonObject.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, communication);
	            response.setResponse(jsonObject);
	        } catch (Exception e) {

	        }
	        return response;
	    }

}
