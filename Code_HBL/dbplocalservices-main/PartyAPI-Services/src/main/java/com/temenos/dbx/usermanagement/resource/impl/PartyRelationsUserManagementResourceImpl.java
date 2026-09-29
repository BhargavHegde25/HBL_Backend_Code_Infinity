package com.temenos.dbx.usermanagement.resource.impl;

import java.util.Map;

import com.kony.dbputilities.util.LegalEntityUtil;
import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyRelationsUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;
import com.temenos.dbx.usermanagement.resource.api.PartyRelationsUserManagementResource;

public class PartyRelationsUserManagementResourceImpl implements PartyRelationsUserManagementResource {


	//partyRelationscreate Function
	@Override
	public Result partyRelationCreate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) 
	{
		Result result = new Result();
	        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

	        JsonArray partyRelations = null;
	        if (StringUtils.isNotBlank(inputMap.get(PartyConstants.partyRelations))) {
	            try {
					LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
	            	partyRelations =
	                        new JsonParser().parse(inputMap.get(PartyConstants.partyRelations)).getAsJsonArray();
	            } catch (Exception e) {
	                // TODO: handle exception
	            }
	        }
	        
	        String partyId = inputMap.get(PartyConstants.partyId);
	        JsonObject partyRelationsObj = new JsonObject();
	        partyRelationsObj.add(PartyConstants.partyRelations, partyRelations);
	        partyRelationsObj.addProperty(PartyConstants.partyId,partyId);
	        
	        if(StringUtils.isBlank(partyId) && partyRelations.size()< 0) 
	        {
	            result.addParam("message", "Invalid InputParams, Mandatory params partyId or partyRelations are empty");
	            result.addParam("status", "fail");
	            return result;
	        }
	        
	        PartyRelationsUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class)
	                .getBusinessDelegate(PartyRelationsUserManagementBusinessDelegate.class);

	        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);

	        
	        if (StringUtils.isNotBlank(partyIP)) 
	        {
	            Map<String, Object> headers = dcRequest.getHeaderMap();
	            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
	            DBXResult response = managementBusinessDelegate.create(partyRelationsObj, headers);

	            if (response.getResponse() != null) 
	            {
	                String id = (String) response.getResponse();
	                result.addParam("status", "success");
	                result.addParam("partyId", id);		                
	                return result;
	            }
	            result.addParam("code", response.getDbpErrCode());
	            result.addParam("message", response.getDbpErrMsg());
	            result.addParam("status", "fail");
	            return result;
	        }
	        else 
	        {
	            result.addParam("message", "PartyRelationsHostURL not found");
	        }
	        result.addParam("status", "fail");
	        return result;
	    }
//partyRelationsGet Function	
	 @Override
	    public Result partyRelationsGet(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) {
	        Result result = new Result();
	        
	        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

	        JsonObject party = new JsonObject();

		 	LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
	       String partyId = inputMap.get(PartyConstants.partyId);
	        String relationType = inputMap.get(PartyConstants.relationType);
	        String hierarchyType = inputMap.get(PartyConstants.hierarchyType);
	        JsonObject partyRelationsobj = new JsonObject();     
	        partyRelationsobj.addProperty(PartyConstants.relationType,relationType);
	        partyRelationsobj.addProperty(PartyConstants.hierarchyType,hierarchyType);
	        
	        //PartyDTO partyDTO = new PartyDTO();
	        if(StringUtils.isBlank(partyId) || StringUtils.isBlank(relationType) || StringUtils.isBlank(hierarchyType))
	        {
	            result.addParam("message", "Invalid InputParams, Mandatory params partyId or relationType or hierarchyType are empty");
	            result.addParam("status", "fail");
	            return result;
	        }
	        
	        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);

	        if (StringUtils.isNotBlank(partyIP)) {
	            Map<String, Object> headers = dcRequest.getHeaderMap();
	            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);
	      
	            String id = inputMap.get("id");
	            PartyRelationsUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(BusinessDelegateFactory.class)
	                    .getBusinessDelegate(PartyRelationsUserManagementBusinessDelegate.class);
	            DBXResult response = new DBXResult();
	            JsonArray partyJsonArray = new JsonArray();
	            if (StringUtils.isNotBlank(partyId)) {
	                BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
	                BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
	                backendIdentifierDTO.setCustomer_id(id);
	                backendIdentifierDTO
	                        .setBackendType(PartyConstants.PARTY);

	                DBXResult dbxResult = backendDelegateimpl.get(backendIdentifierDTO, dcRequest.getHeaderMap());
	                if (dbxResult.getResponse() != null) {
	                    backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
	                    partyId = backendIdentifierDTO.getBackendId();
	                }
	            }
	            if (StringUtils.isNotBlank(partyId)) {
	            	 partyRelationsobj.addProperty(PartyConstants.partyId, partyId);
	                response = managementBusinessDelegate.get(partyRelationsobj, headers);
	                if (response.getResponse() != null) {
	                    JsonObject jsonObject = (JsonObject) response.getResponse();
	                    partyJsonArray.add(jsonObject);
	                    party.add(PartyConstants.parties, partyJsonArray);
	                }
	            } 
	            
	            if (partyJsonArray.size() > 0) {
	                
	               return JSONToResult.convert(party.toString());
	            }

	            result.addParam("code", response.getDbpErrCode());
	            result.addParam("message", response.getDbpErrMsg());
	            return result;
	        } else {
	            result.addParam("message", "PartyHostURL not found");
	        }

	        return result;
	    }
	 
	//partyRelationsUpdate Function	
	
	 public Result partyRelationsUpdate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) 
	 {
	        Result result = new Result();
	        Map<String, String> inputMap = HelperMethods.getInputParamMap(inputArray);

	        String partyId = inputMap.get(PartyConstants.partyId);
	        String relationType = inputMap.get(PartyConstants.relationType);
	        JsonObject partyRelationsObj = new JsonObject();
	        partyRelationsObj.addProperty(PartyConstants.relationType, relationType);
	        partyRelationsObj.addProperty(PartyConstants.partyId,partyId);
	        PartyRelationsUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class)
	                .getBusinessDelegate(PartyRelationsUserManagementBusinessDelegate.class);

	        String partyIP = URLFinder.getPathUrl(URLConstants.PARTY_HOST_URL, dcRequest);
		 	LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
	        if (StringUtils.isNotBlank(partyIP)) 
	        {
	            Map<String, Object> headers = dcRequest.getHeaderMap();
	            headers = PartyUtils.addJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);

	            DBXResult response = managementBusinessDelegate.update(partyRelationsObj, headers);

	            if (response.getResponse() == null) {
	                result.addParam("code", response.getDbpErrCode());
	                result.addParam("message", response.getDbpErrMsg());
	                return result;
	            }

	            JsonObject jsonObject = (JsonObject) response.getResponse();
	            if(StringUtils.isBlank(partyId) || StringUtils.isBlank(relationType)) 
	            {
	                result.addParam("message", "Invalid InputParams, Mandatory params partyId or relationType are empty");
	                result.addParam("status", "fail");
	                return result;
	            }
	            
	            response = managementBusinessDelegate.update(partyRelationsObj, headers);

	            if (response.getResponse() != null) {
	                String id = (String) response.getResponse();
	                result.addParam("status", "success");
	                return result;
	            }

	            result.addParam("code", response.getDbpErrCode());
	            result.addParam("message", response.getDbpErrMsg());
	            result.addParam("status", "fail");
	            return result;
	        } else {
	            result.addParam("message", "PartyHostURL not found");
	        }

	        return result;
	    }

}
