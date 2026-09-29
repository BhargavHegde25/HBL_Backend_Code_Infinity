package com.hbl.resource.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.FeatureConfiguration;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.ContractBackendDelegateImpl;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.ContractDTOExtn;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;

public class ContractBackendDelegateImplExtn extends ContractBackendDelegateImpl{
	LoggerUtil logger = new LoggerUtil(ContractBackendDelegateImplExtn.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	 @Override
	    public boolean updateContractStatus(String contractId, String statusId, String companyLegalUnit, Map<String, Object> headerMap)
	            throws ApplicationException {
	        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(statusId)) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10380);
	        }
	        try {
	            Map<String, Object> inputParams = new HashMap<>();

	            inputParams.put("id", contractId);
	            inputParams.put("statusId", statusId);
	            if(statusId.equals(DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE)) {
	            inputParams.put("description", "SID_CONTRACT_AUTO_APPROVED");
	            }
	            inputParams.put("companyLegalUnit", companyLegalUnit);

	            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    URLConstants.CONTRACT_UPDATE);

	            if (JSONUtil.isJsonNotNull(contractJson)
	                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
	                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
	                JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
	                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
	                ContractDTO dto = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
	                        ContractDTO.class, false);
	                if (null == dto || StringUtils.isBlank(dto.getId())) {
	                    throw new ApplicationException(ErrorCodeEnum.ERR_10381);
	                }
	            } else {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10381);
	            }
	        } catch (ApplicationException e) {
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
	        }
	        return true;

	    }
	 
	 @Override
	    public List<ContractDTO> getListOfContractsByStatus(String statusId, String legalEntityId, Map<String, Object> headerMap)
	            throws ApplicationException {
	        List<ContractDTO> dtoList = new ArrayList<>();
	        try {
	            Map<String, Object> inputParams = new HashMap<>();
	            String filter = "statusId" + DBPUtilitiesConstants.EQUAL + statusId + DBPUtilitiesConstants.AND
	            				+ "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
	            if(statusId.equalsIgnoreCase("SID_CONTRACT_MANUAL_APPROVED")) {
	            	filter="statusId" + DBPUtilitiesConstants.EQUAL + DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE + DBPUtilitiesConstants.AND
            				+ "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId+DBPUtilitiesConstants.AND
            				+ "description" + DBPUtilitiesConstants.EQUAL + statusId;
	            	
	            }
	            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
	            logger.debug("HBL::ContractBackendDelegateImplExtn:getListOfContractsByStatus:inputParams:"+inputParams.toString());
	            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    URLConstants.CONTRACT_GET);

	            if (JSONUtil.isJsonNotNull(contractJson)
	                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
	                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
	                JsonArray contractArray = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT)
	                        .getAsJsonArray();

	                for (JsonElement element : contractArray) {
	                    if (element.isJsonObject()) {
	                        ContractDTO dto = (ContractDTO) DTOUtils
	                                .loadJsonObjectIntoObject(element.getAsJsonObject(), ContractDTO.class, false);
	                        if (null != dto) {
	                            updateServiceDefinitionName(dto, headerMap);
	                            dto=convertContarctIdToCoreCustomerId(dto,headerMap);
	                            dtoList.add(dto);
	                        }

	                    }

	                }
	            }
	        } catch (ApplicationException e) {
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10379);
	        }

	        return dtoList;
	    }
	 public List<ContractDTOExtn> getListOfContractsByStatusNew(String statusId, String legalEntityId, Map<String, Object> headerMap)
	            throws ApplicationException {
	        List<ContractDTOExtn> dtoList = new ArrayList<>();
	        try {
	            Map<String, Object> inputParams = new HashMap<>();
	            inputParams.put("_statusId", statusId);
	            inputParams.put("_companyLegalUnit", legalEntityId);
	            inputParams.put("_description", "");
	            if(statusId.equalsIgnoreCase(DBPUtilitiesConstants.CONTRACT_STATUS_PENDING)) {
	            	 inputParams.put("_description", "SID_CONTRACT_PENDING");
	            	 inputParams.put("_statusId", "");
	            }
	            if(statusId.equalsIgnoreCase("SID_CONTRACT_MANUAL_APPROVED")) {
	            	inputParams.put("_statusId", DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
	            	inputParams.put("_description", statusId);
	            }
	            logger.debug("HBL::ContractBackendDelegateImplExtn:getListOfContractsByStatus:inputParams:"+inputParams.toString());
	            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    "contract_status_proc.get");
	            logger.debug("HBL::ContractBackendDelegateImplExtn:getListOfContractsByStatus:Response:"+contractJson);
	            if (JSONUtil.isJsonNotNull(contractJson)
	                    && JSONUtil.hasKey(contractJson, "records")
	                    && contractJson.get("records").isJsonArray()) {
	                JsonArray contractArray = contractJson.get("records")
	                        .getAsJsonArray();

	                for (JsonElement element : contractArray) {
	                    if (element.isJsonObject()) {
	                        ContractDTOExtn dto = (ContractDTOExtn) DTOUtils
	                                .loadJsonObjectIntoObject(element.getAsJsonObject(), ContractDTOExtn.class, false);
	                        if (null != dto) {
	                        	String createdts = dto.getCreatedts();
	                        	logger.debug("HBL::dto value ###:"+createdts);
								if (statusId.equalsIgnoreCase(DBPUtilitiesConstants.CONTRACT_STATUS_PENDING)) {
									if (compareCreateDate(createdts))
										dtoList.add(dto);
								} else {
									dtoList.add(dto);
								}
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10379);
	        }

	        return dtoList;
	    }
	 
		public boolean compareCreateDate(String createddt) {
			// 1. Define formatter (matches "YYYY-MM-DD HH:mm:ss.S") 2025-11-30 15:19:23.0
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			LocalDateTime createdDate = LocalDateTime.parse(createddt, formatter);

			// 2. Calculate the difference in days
			long daysElapsed = ChronoUnit.DAYS.between(createdDate, LocalDateTime.now());

			// 3. Verify if it is within the last 365 days
			if (daysElapsed >= 0 && daysElapsed <= 365) {
				System.out.println("Valid: The date is within the last 365 days.");
				return true;

			} else {
				System.out.println("Invalid: The date exceeds 365 days.");
				return false;
			}
		}
		
	    public List<ContractDTOExtn> getListOfContractsByStatusNew_old(String statusId, String legalEntityId, Map<String, Object> headerMap)
	            throws ApplicationException {
	        List<ContractDTOExtn> dtoList = new ArrayList<>();
	        try {
	            Map<String, Object> inputParams = new HashMap<>();
	            String filter = "statusId" + DBPUtilitiesConstants.EQUAL + statusId + DBPUtilitiesConstants.AND
	            				+ "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
	            if(statusId.equalsIgnoreCase("SID_CONTRACT_MANUAL_APPROVED")) {
	            	filter="statusId" + DBPUtilitiesConstants.EQUAL + DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE + DBPUtilitiesConstants.AND
         				+ "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId+DBPUtilitiesConstants.AND
         				+ "description" + DBPUtilitiesConstants.EQUAL + statusId;
	            	
	            }
	            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
	            logger.debug("HBL::ContractBackendDelegateImplExtn:getListOfContractsByStatus:inputParams:"+inputParams.toString());
	            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    URLConstants.CONTRACT_GET);
	            logger.debug("HBL::ContractBackendDelegateImplExtn:getListOfContractsByStatus:Response:"+contractJson);
	            if (JSONUtil.isJsonNotNull(contractJson)
	                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
	                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
	                JsonArray contractArray = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT)
	                        .getAsJsonArray();

	                for (JsonElement element : contractArray) {
	                    if (element.isJsonObject()) {
	                        ContractDTOExtn dto = (ContractDTOExtn) DTOUtils
	                                .loadJsonObjectIntoObject(element.getAsJsonObject(), ContractDTOExtn.class, false);
	                        if (null != dto) {
	                            updateServiceDefinitionName(dto, headerMap);
	                            dto=convertContarctIdToCoreCustomerId(dto,headerMap);
	                            dtoList.add(dto);
	                        }

	                    }

	                }
	            }
	        } catch (ApplicationException e) {
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10379);
	        }

	        return dtoList;
	    }
	 public ContractDTOExtn convertContarctIdToCoreCustomerId( ContractDTOExtn dto, Map<String, Object> headerMap) {
		 BackendIdentifierDTO backendDto = new BackendIdentifierDTO();
		 backendDto.setContractId(dto.getId().toString());
		 backendDto.setBackendType("T24");
		 Map<String, String> backendIdentifier = getCoreCustomerId(backendDto, headerMap);
		 logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId result backendIdentifier object:"+backendIdentifier);
		if(backendIdentifier!=null &&  backendIdentifier.get("BackendId")!=null) {
			String coreCustomerId=backendIdentifier.get("BackendId");
		 logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId result coreCustomerId:"+coreCustomerId);
		 dto.setCreatedby(coreCustomerId);
	 }
		 
              
		return dto;
		 
	 }
	 private void updateServiceDefinitionName(ContractDTOExtn dto, Map<String, Object> headerMap)
	            throws ApplicationException {
	        if (null != dto && StringUtils.isNotBlank(dto.getServicedefinitionId())) {
	            ServiceDefinitionBusinessDelegate businessDelegate =
	                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
	            ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
	            serviceDefinitionDTO.setId(dto.getServicedefinitionId());
	            serviceDefinitionDTO = businessDelegate.getServiceDefinitionDetails(serviceDefinitionDTO, headerMap);
	            if (serviceDefinitionDTO == null || StringUtils.isBlank(serviceDefinitionDTO.getId())) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10379);
	            }
	            dto.setServicedefinitionName(serviceDefinitionDTO.getName());
	            dto.setServiceType(serviceDefinitionDTO.getServiceType());
	        }

	    }
	 public ContractDTO convertContarctIdToCoreCustomerId( ContractDTO dto, Map<String, Object> headerMap) {
		 BackendIdentifierDTO backendDto = new BackendIdentifierDTO();
		 backendDto.setContractId(dto.getId().toString());
		 backendDto.setBackendType("T24");
		 Map<String, String> backendIdentifier = getCoreCustomerId(backendDto, headerMap);
		 logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId result backendIdentifier object:"+backendIdentifier);
		if(backendIdentifier!=null &&  backendIdentifier.get("BackendId")!=null) {
			String coreCustomerId=backendIdentifier.get("BackendId");
		 logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId result coreCustomerId:"+coreCustomerId);
		 dto.setCreatedby(coreCustomerId);
	 }
		 
                 
		return dto;
		 
	 }
	 public Map<String, String> getCoreCustomerId(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap) {

		 Map<String, String> outputParams = null;
	        String filter = "";

	        if (StringUtils.isNotBlank((backendIdentifierDTO.getContractId()))) {
	            filter += "contractId" + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getContractId();
	        }

	        if (StringUtils.isNotBlank((backendIdentifierDTO.getBackendType()))) {
	            if (StringUtils.isNotBlank(filter)) {
	                filter += DBPUtilitiesConstants.AND;
	            }
	            filter += DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getBackendType();
	        }

	        if (StringUtils.isNotBlank((backendIdentifierDTO.getBackendId()))) {
	            if (StringUtils.isNotBlank(filter)) {
	                filter += DBPUtilitiesConstants.AND;
	            }
	            filter += DTOConstants.BACKENDID + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getBackendId();
	        }

	        if (StringUtils.isBlank(filter)) {
	            return outputParams;
	        }

	        Map<String, Object> inputParams = new HashMap<String, Object>();
	       
	        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
	        logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId from backend identifier table:inputParams:"+inputParams.toString());
	        String authToken = (String) headerMap.get(MWConstants.X_KONY_AUTHORIZATION_HEADER);
	        try {
	            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    URLConstants.BACKENDIDENTIFIER_GET, authToken);
	            logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId inputParams:"+inputParams.toString()+", result:"+result.toString());
	            if (result.has("backendidentifier") && !result.get("backendidentifier").isJsonNull()
	                    && result.get("backendidentifier").isJsonArray()
	                    && result.get("backendidentifier").getAsJsonArray().size() > 0) {
	                JsonArray jsonArray = result.get("backendidentifier").getAsJsonArray();
	                outputParams=jsonToMap(jsonArray.get(0).getAsJsonObject());
	                logger.debug("HBL::ContractBackendDelegateImplExtn:getCoreCustomerId outputParams:"+outputParams.toString());

	            }
	        } catch (Exception e) {
	            logger.error("Caught exception while getting BACKENDIDENTIFIER_GET: "+ e.toString());
	        }

	        return outputParams;
	    }
	 private void updateServiceDefinitionName(ContractDTO dto, Map<String, Object> headerMap)
	            throws ApplicationException {
	        if (null != dto && StringUtils.isNotBlank(dto.getServicedefinitionId())) {
	            ServiceDefinitionBusinessDelegate businessDelegate =
	                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
	            ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
	            serviceDefinitionDTO.setId(dto.getServicedefinitionId());
	            serviceDefinitionDTO = businessDelegate.getServiceDefinitionDetails(serviceDefinitionDTO, headerMap);
	            if (serviceDefinitionDTO == null || StringUtils.isBlank(serviceDefinitionDTO.getId())) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10379);
	            }
	            dto.setServicedefinitionName(serviceDefinitionDTO.getName());
	            dto.setServiceType(serviceDefinitionDTO.getServiceType());
	        }

	    }
	    private Map<String, String> jsonToMap(JsonObject jobject) {
	        Map<String, String> map = new HashMap<>();
	        for (Map.Entry<String, JsonElement> entry : jobject.entrySet()) {
	            if (HelperMethods.isJsonNotNull(entry.getValue())) {
	                map.put(entry.getKey(), entry.getValue().getAsString());
	            }
	        }
	        return map;
	    }
	    
	    @Override
	    public DBXResult getNormalizedContractFeatureActionLimits(
	            Map<String, List<ContractAccountsDTO>> coreCustomerAccounts, Map<String,Map<String, Set<String>>> productIdPermissions,
	            List<ContractCoreCustomersDTO> contractCustomers, FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO,
	            Map<String, FeatureActionLimitsDTO> contractFeatureActionDTOMap)
	            throws ApplicationException {
	        DBXResult response = new DBXResult();
	        JsonObject dbxResultJsonResponse = new JsonObject();
	        
	        
	        
	        /**
	         * Feature and actions information
	         */
	        Map<String, JsonObject> featureInfo = serviceDefinitionFeatureActionDTO.getFeatureInfo();
	        Map<String, JsonObject> actionsInfo = serviceDefinitionFeatureActionDTO.getActionsInfo();
	        Map<String, Map<String, Map<String, String>>> monetaryActionLimits = serviceDefinitionFeatureActionDTO
	                .getMonetaryActionLimits();
	        /**
	         * Global level permission array declarations
	         */

	        JsonArray globalPermissions = new JsonArray();
	        JsonArray accountLevelPermissions = new JsonArray();
	        JsonArray transactionlimits = new JsonArray();
	        
	        
	        Map<String, Set<String>> globalLevelPermissionsSD = serviceDefinitionFeatureActionDTO
	                .getGlobalLevelPermissions();
	        Map<String, Set<String>> accountLevelPermissionsSD = serviceDefinitionFeatureActionDTO
	                .getAccountLevelPermissions();
	        
	        /*This is to return all features in global level permissions for a contract */
	       // globalLevelPermissionsSD.putAll(accountLevelPermissionsSD);
			Iterator<String> it = accountLevelPermissionsSD.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();

				if (globalLevelPermissionsSD.containsKey(key)) {
					globalLevelPermissionsSD.get(key).addAll(accountLevelPermissionsSD.get(key));
				} else {
					globalLevelPermissionsSD.put(key, accountLevelPermissionsSD.get(key));
				}
			}

	        Map<String, Set<String>> transactionLimitsSD = serviceDefinitionFeatureActionDTO
	        		.getTransactionLimits();
	        
	        JsonObject globalCoreCustomerJsonObject = null;
	        JsonArray globalFeatureActionsJsonArray = null;
	        JsonArray actionsJsonArray = null;
	        JsonArray coreCustomerAccountLevelJsonArray = null;
	        JsonArray accountLevelFeatureActionsJsonArray = null;
	        JsonArray coreCustomerTransactionLimitsJsonArray = null;
	        JsonArray limits = null;
	        FeatureActionLimitsDTO contractFeatureActionDTO =null;
	        boolean isActionPresentAtProduct = true;
	        boolean isProductSpecificFeatueEnabled = 
	        		FeatureConfiguration.isProductSpecificFeatueEnabled();
	        /**
	         * looping each contractCustomers
	         */
	        for (ContractCoreCustomersDTO contractCustomer : contractCustomers) {

	            /**
	             * Forming global level permissions json object
	             */
	        	
	        	contractFeatureActionDTO = contractFeatureActionDTOMap.get(contractCustomer.getCoreCustomerId());

	            Map<String, Set<String>> coreCustomerGlobalPermissions = 
	            		contractFeatureActionDTO.getCoreCustomerGlobalLevelPermissions();
	            globalCoreCustomerJsonObject = addContractCustomerDetails(contractCustomer);
	            globalFeatureActionsJsonArray = new JsonArray();
	            for (Entry<String, Set<String>> featureActionEntry : globalLevelPermissionsSD.entrySet()) {
	                
	                actionsJsonArray = new JsonArray();
					for (String actionId : featureActionEntry.getValue()) {
						if (actionsInfo.get(actionId) != null) {
							JsonObject actionJsonObject = actionsInfo.get(actionId).deepCopy();

							boolean isEnabled = false;
							if (coreCustomerGlobalPermissions.containsKey(featureActionEntry.getKey())) {
								isEnabled = coreCustomerGlobalPermissions.get(featureActionEntry.getKey())
										.contains(actionId);
							}
							actionJsonObject.addProperty("isEnabled", String.valueOf(isEnabled));
							actionsJsonArray.add(actionJsonObject);
						}
					}
	                JsonObject featureJsonObject = featureInfo
	                		.get(featureActionEntry.getKey()).deepCopy();
	                featureJsonObject.add("permissions", actionsJsonArray);
	                globalFeatureActionsJsonArray.add(featureJsonObject);
	            }
	            globalCoreCustomerJsonObject.add("features", globalFeatureActionsJsonArray);
	            globalPermissions.add(globalCoreCustomerJsonObject);
	            logger.debug("global permissions done");

	            /**
	             * Forming account level permissions
	             */
	            Map<String, Map<String, Set<String>>> coreCustomerAccountLevelPermissions = 
	            		contractFeatureActionDTO.getAsscoiatedAccountActions();

	            JsonObject coreCustomerAccountLevel = addContractCustomerDetails(contractCustomer);
	            coreCustomerAccountLevelJsonArray = new JsonArray();
	            List<ContractAccountsDTO> accounts = coreCustomerAccounts.get(contractCustomer.getCoreCustomerId());
	            for (ContractAccountsDTO contractAccount : accounts) {
	                JsonObject accountDetails = addAccountDetails(contractAccount);
	                String productId = JSONUtil.getString(accountDetails, "productId");

	                logger.debug("product id" + productId);

	                accountLevelFeatureActionsJsonArray = new JsonArray();
	                for (Entry<String, Set<String>> featureActionEntry : accountLevelPermissionsSD.entrySet()) {
	                    JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey()).deepCopy();
	                    actionsJsonArray = new JsonArray();
	                    for (String actionId : featureActionEntry.getValue()) {
	                        JsonObject actionJsonObject = actionsInfo.get(actionId).deepCopy();
	                        boolean isEnabled = false;
	                        if (isProductSpecificFeatueEnabled) {
	                        	if(productIdPermissions.containsKey(productId)) {
	                        		isActionPresentAtProduct = (
	                            		productIdPermissions.get(productId).containsKey(featureActionEntry.getKey())
	                            		&& productIdPermissions.get(productId).get(featureActionEntry.getKey()).contains(actionId));
	                        		if(!isActionPresentAtProduct) {
	                                	continue;
	                                }
	                        	}
	                        }
							if (coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId()) != null
									&& coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId())
											.containsKey(featureActionEntry.getKey())) {
								isEnabled = coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId())
										.get(featureActionEntry.getKey()).contains(actionId);
								if(!coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId())
										.get(featureActionEntry.getKey()).contains(actionId)) {
									logger.debug("HBL::ContractBackendDelegateImplExtn:getNormalizedContractFeatureActionLimits::current actionId is not present in coreCustomerAccountLevelPermissions:so explicitly adding isEnabled= true; actionId:" + actionId);
									isEnabled=true;
								}
							}
							
							else if (coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId()) != null
									&& !coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId())
											.containsKey(featureActionEntry.getKey())) {
								logger.debug("HBL::ContractBackendDelegateImplExtn:getNormalizedContractFeatureActionLimits::current feature id is not present in coreCustomerAccountLevelPermissions:so explicitly adding isEnabled= true; feature id:" + featureActionEntry.getKey());
								isEnabled=true;

							}
							
							
							if(!isEnabled) {
								logger.debug("HBL::ContractBackendDelegateImplExtn:getNormalizedContractFeatureActionLimits::disabled: actionId:" + actionId);
							}
							
	                        actionJsonObject.addProperty("isEnabled", String.valueOf(isEnabled));
	                        actionsJsonArray.add(actionJsonObject);
	                    }
	                    featureJsonObject.add("permissions", actionsJsonArray);
	                    accountLevelFeatureActionsJsonArray.add(featureJsonObject);
	                }
	                accountDetails.add("featurePermissions", accountLevelFeatureActionsJsonArray);
	                coreCustomerAccountLevelJsonArray.add(accountDetails);
	            }
	            coreCustomerAccountLevel.add("accounts", coreCustomerAccountLevelJsonArray);
	            accountLevelPermissions.add(coreCustomerAccountLevel);

	            logger.debug("account level permissions done");

	            /**
	             * Forming transaction limits
	             */

	            Map<String, Map<String, Map<String, String>>> coreCustomerTransactionLimits = contractFeatureActionDTO
	                    .getCoreCustomerTransactionLimits();
	            JsonObject coreCustomerTransactionLimitsJsonObject = addContractCustomerDetails(contractCustomer);
	            coreCustomerTransactionLimitsJsonArray = new JsonArray();
	            for (Entry<String, Set<String>> featureActionEntry : transactionLimitsSD.entrySet()) {
	                JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey()).deepCopy();
	                
	                for (String actionId : featureActionEntry.getValue()) {
	                    for (Entry<String, JsonElement> entrySet : actionsInfo.get(actionId).entrySet()) {
	                        featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
	                    }
	                    boolean isEnabled = false;
	                    if (coreCustomerTransactionLimits.containsKey(featureActionEntry.getKey())) {
	                        isEnabled = coreCustomerTransactionLimits.get(featureActionEntry.getKey())
	                                .containsKey(actionId);
	                    }
	                    featureJsonObject.addProperty("isEnabled", String.valueOf(isEnabled));
	                    limits = new JsonArray();
						if (coreCustomerTransactionLimits.get(featureActionEntry.getKey()) != null
								&& coreCustomerTransactionLimits.get(featureActionEntry.getKey()).get(actionId) != null) {
							Map<String, String> limitsMap = coreCustomerTransactionLimits.get(featureActionEntry.getKey())
									.get(actionId);
							// Map<String, String> limitsMap =
							// monetaryActionLimits.get(featureActionEntry.getKey()).get(actionId);
							for (Entry<String, String> limitsEntry : limitsMap.entrySet()) {
								JsonObject limitsJson = new JsonObject();
								limitsJson.addProperty("id", limitsEntry.getKey());
								limitsJson.addProperty("value", limitsEntry.getValue());
								limits.add(limitsJson);
							}
							featureJsonObject.add("limits", limits);
						}
						else if (monetaryActionLimits.get(featureActionEntry.getKey()) != null
								&& monetaryActionLimits.get(featureActionEntry.getKey()).get(actionId) != null) {
							Map<String, String> limitsMap = monetaryActionLimits.get(featureActionEntry.getKey())
									.get(actionId);
							for (Entry<String, String> limitsEntry : limitsMap.entrySet()) {
								JsonObject limitsJson = new JsonObject();
								limitsJson.addProperty("id", limitsEntry.getKey());
								limitsJson.addProperty("value", limitsEntry.getValue());
								limits.add(limitsJson);
							}
							featureJsonObject.add("limits", limits);
						}
					}
	                coreCustomerTransactionLimitsJsonArray.add(featureJsonObject);
	            }
	            coreCustomerTransactionLimitsJsonObject.add("featurePermissions", coreCustomerTransactionLimitsJsonArray);
	            transactionlimits.add(coreCustomerTransactionLimitsJsonObject);
	            
	            logger.debug("transaction limits permissions done");

	        }
	        dbxResultJsonResponse.add("globalLevelPermissions", globalPermissions);
	        dbxResultJsonResponse.add("accountLevelPermissions", accountLevelPermissions);
	        dbxResultJsonResponse.add("transactionLimits", transactionlimits);
	        response.setResponse(dbxResultJsonResponse);
	        return response;
	    }
	    private JsonObject addAccountDetails(ContractAccountsDTO contractAccountDTO) {
	        JsonObject json = new JsonObject();
	        json.addProperty("accountId", contractAccountDTO.getAccountId());
	        json.addProperty("accountName", contractAccountDTO.getAccountName());
	        json.addProperty("productId", contractAccountDTO.getProductId()!=null?contractAccountDTO.getProductId():"");
	        json.addProperty("accountType", HelperMethods.getAccountsNames().get(contractAccountDTO.getTypeId()));
	        json.addProperty("ownerType", contractAccountDTO.getOwnerType());

	        /*
	         * json.addProperty("portfolioId", contractAccountDTO.getPortfolioId()); json.addProperty("portfolioName",
	         * contractAccountDTO.getPortfolioName()); json.addProperty("isPortfolio",
	         * contractAccountDTO.getIsPortfolioAccount());
	         */

	        return json;
	    }
	    public boolean updateContractChannelRequest(String contractId, String statusId, String companyLegalUnit, String ChannelRequestedFor, String submittedFrom, String isConsentProvided, Map<String, Object> headerMap)
	            throws ApplicationException {
	        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(statusId)) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10380);
	        }
	        try {
	            Map<String, Object> inputParams = new HashMap<>();
	            inputParams.put("id", contractId);
	            inputParams.put("description", statusId);
		        inputParams.put("statusId",statusId);
	            if(isConsentProvided.equalsIgnoreCase("true")) {
		            inputParams.put("description", statusId);
		            inputParams.put("statusId", DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
		         }
	            /*if(statusId.equals(DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE)) {
	            inputParams.put("description", statusId);
	            }
	            */
	            inputParams.put("companyLegalUnit", companyLegalUnit);
	            /* faxId is used for submitted from which channel */
	            inputParams.put("faxId", submittedFrom);
	            /* channelAccess is used for RequestedChannelAccess*/
	            inputParams.put("channelAccess", ChannelRequestedFor);
	            inputParams.put("isConsentProvided", isConsentProvided);

	            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
	                    URLConstants.CONTRACT_UPDATE);

	            if (JSONUtil.isJsonNotNull(contractJson)
	                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
	                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
	                JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
	                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
	                ContractDTO dto = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
	                        ContractDTO.class, false);
	                if (null == dto || StringUtils.isBlank(dto.getId())) {
	                    throw new ApplicationException(ErrorCodeEnum.ERR_10381);
	                }
	            } else {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10381);
	            }
	        } catch (ApplicationException e) {
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
	        }
	        return true;

	    }

}
