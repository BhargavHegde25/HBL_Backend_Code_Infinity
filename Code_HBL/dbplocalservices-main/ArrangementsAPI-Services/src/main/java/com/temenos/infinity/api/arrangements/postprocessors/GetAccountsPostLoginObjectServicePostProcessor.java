package com.temenos.infinity.api.arrangements.postprocessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.infinity.api.arrangements.constants.ObjectServicesConstants;
import com.temenos.infinity.api.arrangements.utils.ObjectServiceHelperMethods;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.Constants;

public class GetAccountsPostLoginObjectServicePostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants, ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private static final int CACHE_IN_SECONDS = (1 * 30 * 60); // 30 mins

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        JsonObject responsePayloadJson = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        JsonObject requestPayloadJson = fabricRequestManager.getPayloadHandler().getPayloadAsJson() != null ? fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject() : new JsonObject();
        JsonObject cacheJson = new JsonObject();
        JsonArray accountsCacheArray = new JsonArray();
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(fabricRequestManager);
        String loginUserId = null;
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            loginUserId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
        } else {
            loginUserId = requestPayloadJson.has(InfinityConstants.id) && 
                    !requestPayloadJson.get(InfinityConstants.id).isJsonArray() ?
                            requestPayloadJson.get(InfinityConstants.id).getAsString() : null;
        }
        if(StringUtils.isNotBlank(loginUserId)) {
            addAccountLevelPermissionsandUpdateCacheArray(loginUserId, responsePayloadJson, fabricRequestManager,
                    accountsCacheArray);
            
            JsonObject request = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            Boolean muteActions = Boolean.FALSE;
            if (request!=null && request.has("Membership_id") && StringUtils.isNotBlank(request.get("Membership_id").getAsString()))
            	muteActions = Boolean.TRUE;
            
            if(!muteActions) {
            JsonArray accountsArray = JSONUtil.hasKey(responsePayloadJson, "Accounts") ? responsePayloadJson.get("Accounts").getAsJsonArray() : new JsonArray();
            for (JsonElement accountObject : accountsArray) {	
        		JsonObject account = accountObject.isJsonObject() ? accountObject.getAsJsonObject() : new JsonObject();
        		if (account.get("accountType").getAsString().equals("mortgageFacility")) {
        			accountsCacheArray.add(accountObject);
        		}
        		}
            cacheJson.add("Accounts", accountsCacheArray);   
            if (JSONUtil.isJsonNotNull(cacheJson)) {
                MemoryManager.saveIntoCache(DBPUtilitiesConstants.ACCOUNTS_POSTLOGIN_CACHE_KEY + loginUserId,
                        cacheJson.toString(), CACHE_IN_SECONDS);
            }
            }
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responsePayloadJson);
        }
        try {
            execute(fabricRequestManager, fabricResponseManager);
        } catch (Exception e) {
		Log4j2Configurator.getInstance();
            alert.prepareError("Exception while caching accounts in session", e).log();
        }
        return true;
    }

    private void addAccountLevelPermissionsandUpdateCacheArray(String customerId, JsonObject responsePayloadJson,
            FabricRequestManager fabricRequestManager, JsonArray accountsCacheArray) throws Exception {
        Map<String, Set<String>> accountLevelActions = new HashMap<>();
		Map<String, String> serviceDefinitions = new HashMap<>();
		Map<String, String> contracts = new HashMap<>();
		Map<String, String> customerGroups = new HashMap<>();
		Set<String> coreCustomers = getCoreCustomersList(responsePayloadJson, customerId, fabricRequestManager,
				serviceDefinitions, customerGroups, contracts);
		alert.prepareError("coreCustomers >>"+coreCustomers).log();
		Map<String, Map<String, String>> accountsDetails = getCoreCustomerAccountsDetails(coreCustomers, customerId,
				fabricRequestManager);
        Set<String> usedCoreCustomers = new HashSet<>();
      //setting coreCutomerId's for Mortgage Accounts
        //getting User list
        Map<String, Object> inputParamsOne = new HashMap<>();
        inputParamsOne.put("_customerId", customerId);
        JsonObject resultObjectNew =
        		com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager, inputParamsOne,
        				com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
        				URLConstants.USER_CUSTOMERS_PROC);
        Set<String>  coreCustomersMortgage = new HashSet<String>();
        coreCustomersMortgage.addAll(coreCustomers);
         
        JsonArray accountsArray = JSONUtil.hasKey(responsePayloadJson, "Accounts") ? responsePayloadJson.get("Accounts").getAsJsonArray() : new JsonArray();
        String coreCustomerIdTwo= null;
        String mockMortgageResponse = ServerConfigurations.MOCK_MORTGAGE_RESPONSE.getValue();
        if(mockMortgageResponse != null && mockMortgageResponse.equalsIgnoreCase("Yes")) {
		
			  for (JsonElement accountObject : accountsArray) { JsonObject account =
			  accountObject.isJsonObject() ? accountObject.getAsJsonObject() : new
			  JsonObject(); if
			  (account.get("accountType").getAsString().equals("mortgageFacility")) {
			  alert.prepareError("mortgageFacility accountType	 "+account.get("accountType").
			  getAsString().equals("mortgageFacility")).log(); Iterator<String> it =
			  coreCustomersMortgage.iterator(); for(int i=0;
			  i<coreCustomersMortgage.size();i++) { String value = it.next();
			  account.addProperty("Membership_id", value);
			  account.addProperty("coreCustomerId", value);
			  coreCustomersMortgage.remove(value); } if(!account.has("Membership_id")) {
			  account.addProperty("Membership_id", coreCustomerIdTwo);
			  account.addProperty("coreCustomerId", coreCustomerIdTwo); } JsonArray
			  actionsArray = JSONUtil.getJsonArrary(resultObjectNew,
			  DBPDatasetConstants.DATASET_RECORDS); for (JsonElement element :
			  actionsArray) { JsonObject actionRecord = element.isJsonObject() ?
			  element.getAsJsonObject() : new JsonObject(); String isBusiness =
			  actionRecord.has("isBusiness") ? actionRecord.get("isBusiness").getAsString()
			  : ""; coreCustomerIdTwo = actionRecord.has("coreCustomerId") ?
			  actionRecord.get("coreCustomerId").getAsString() : ""; String
			  coreCustomerIdOne = account.get("Membership_id").getAsString();
			  if(StringUtils.isNotBlank(coreCustomerIdOne) &&
			  coreCustomerIdOne.equals(coreCustomerIdTwo)) {
			  account.addProperty("isBusinessAccount", isBusiness); } } } }
			 
        	
        }
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("_userId", customerId);
        JsonObject request = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        Boolean muteActions = Boolean.FALSE;
        if (request!=null && request.has("actions") && "false".equals(request.get("actions").getAsString()))
        	muteActions = Boolean.TRUE;
        
        
        if(!muteActions) {      	
        for (String coreCustomerId : coreCustomers) {
            if (!usedCoreCustomers.contains(coreCustomerId)) {
                inputParams.put("_coreCustomerId", coreCustomerId);
                JsonObject resultObject =
                        com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager, inputParams,
                                com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
                                URLConstants.USER_ACCOUNTACTIONS_GET_PROC);

                if (JSONUtil.isJsonNotNull(resultObject)
                        && JSONUtil.hasKey(resultObject, DBPDatasetConstants.DATASET_RECORDS) &&
                        resultObject.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                    usedCoreCustomers.add(coreCustomerId);
                    JsonArray actionsArray = JSONUtil.getJsonArrary(resultObject, DBPDatasetConstants.DATASET_RECORDS);
                    for (JsonElement element : actionsArray) {
                        JsonObject actionRecord = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
                        String accountId =
                                actionRecord.has("Account_id") ? actionRecord.get("Account_id").getAsString() : "";
                        String userId =
                                actionRecord.has("Customer_id") ? actionRecord.get("Customer_id").getAsString() : "";
                        String actionId =
                                actionRecord.has("Action_id") ? actionRecord.get("Action_id").getAsString() : "";

                        if (customerId.equalsIgnoreCase(userId) && StringUtils.isNotBlank(accountId)
                                && StringUtils.isNotBlank(actionId) && StringUtils.isNotBlank(userId)) {
                            if (accountLevelActions.containsKey(accountId)) {
                                accountLevelActions.get(accountId).add(actionId);
                            } else {
                                Set<String> actions = new HashSet<>();
                                actions.add(actionId);
                                accountLevelActions.put(accountId, actions);
                            }

                        }
                    }

                }
            }
			getAccountLevelNewActions(customerId, contracts.get(coreCustomerId), coreCustomerId,
					customerGroups.get(coreCustomerId), serviceDefinitions.get(coreCustomerId), fabricRequestManager,
					accountLevelActions, accountsDetails);
        }
        }

        addPermissionsToAccountRecords(responsePayloadJson, accountLevelActions, accountsDetails, accountsCacheArray);
    }

	private void getAccountLevelNewActions(String customerId, String contractId, String coreCustomerId, String groupId,
			String serviceDefinitionId, FabricRequestManager fabricRequestManager,
			Map<String, Set<String>> accountLevelActions, Map<String, Map<String, String>> accountsDetails) {
		Map<String, Map<String, Map<String, Boolean>>> globalActions = new HashMap<String, Map<String, Map<String, Boolean>>>();
		Map<String, Map<String, Map<String, Map<String, Boolean>>>> accountActions = new HashMap<String, Map<String, Map<String, Map<String, Boolean>>>>();
		try {
			ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ContractBackendDelegate.class);
			String legalEntityId = null;
        	try {
                IdentityHandler identityHandler = fabricRequestManager.getServicesManager().getIdentityHandler();
                Map<String, Object> userAttributes = identityHandler.getUserAttributes();
               
                if(userAttributes != null && userAttributes.size() >0) {
                	legalEntityId = (String)userAttributes.get("legalEntityId");
				} 
					  else { legalEntityId = (String)userAttributes.get("companyId"); }
					 
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                alert.prepareError(e.toString()).log();
            }
			/*
			 * FeatureActionLimitsDTO actionLimitsDTO =
			 * backendDelegate.getRestrictiveFeatureActionLimits( serviceDefinitionId, "",
			 * groupId, coreCustomerId, "",
			 * com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager.
			 * getHeadersHandler()), false, "");
			 */
			
			FeatureActionLimitsDTO actionLimitsDTO = backendDelegate.getRestrictiveFeatureActionLimits(
					serviceDefinitionId, "", groupId, coreCustomerId, "",
					com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager.getHeadersHandler()),
					false, "",legalEntityId);
			
			
			String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
					+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
					+ contractId + DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId
					+ DBPUtilitiesConstants.EQUAL + coreCustomerId;
			Map<String, String> input = new HashMap<String, String>();
			input.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject response = new JsonObject();
			
			try {
				response = com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager,
						input, com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
						URLConstants.EXCLUDED_CUSTOMER_ACTION_LIMITS_GET);
			} catch (HttpCallException e) {
				
				alert.prepareError(e.toString()).log();
			}
			JsonArray jsonArray = new JsonArray();
			if (response.has(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION)) {
				JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					jsonArray = jsonElement.getAsJsonArray();
				}
			}

			accountActions.put(coreCustomerId,new HashMap<String, Map<String, Map<String, Boolean>>>());
			
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();

				String featureId = jsonObject2.has(InfinityConstants.featureId)
						&& !jsonObject2.get(InfinityConstants.featureId).isJsonNull()
								? jsonObject2.get(InfinityConstants.featureId).getAsString().trim()
								: null;
				String actionId = jsonObject2.has(InfinityConstants.Action_id)
						&& !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
								? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
								: null;
				if (StringUtils.isBlank(actionId)) {
					actionId = jsonObject2.has(InfinityConstants.action_id)
							&& !jsonObject2.get(InfinityConstants.action_id).isJsonNull()
									? jsonObject2.get(InfinityConstants.action_id).getAsString().trim()
									: null;
				}
				String accountId = jsonObject2.has(InfinityConstants.Account_id)
						&& !jsonObject2.get(InfinityConstants.Account_id).isJsonNull()
								? jsonObject2.get(InfinityConstants.Account_id).getAsString()
								: null;
				if (StringUtils.isBlank(accountId)) {
					accountId = jsonObject2.has(InfinityConstants.account_id)
							&& !jsonObject2.get(InfinityConstants.account_id).isJsonNull()
									? jsonObject2.get(InfinityConstants.account_id).getAsString()
									: null;
				}

				if (actionLimitsDTO.getGlobalLevelActions().contains(actionId)) {
					if (!globalActions.containsKey(coreCustomerId)) {
						globalActions.put(coreCustomerId, new HashMap<String, Map<String, Boolean>>());
					}
					if (!actionLimitsDTO.getFeatureaction().containsKey(featureId)) {
						continue;
					}
					if (!globalActions.get(coreCustomerId).containsKey(featureId)) {
						globalActions.get(coreCustomerId).put(featureId, new HashMap<String, Boolean>());
					}
					if (!actionLimitsDTO.getFeatureaction().get(featureId).contains(actionId)) {
						continue;
					}
					globalActions.get(coreCustomerId).get(featureId).put(actionId, true);
				} else {
					if (actionLimitsDTO.getAccountLevelActions().contains(actionId)) {
						if (!actionLimitsDTO.getFeatureaction().containsKey(featureId)
							|| !actionLimitsDTO.getFeatureaction().get(featureId).contains(actionId)
							|| !actionLimitsDTO.getAccountLevelActions().contains(actionId)) {
							continue;
						}
						if (!accountActions.get(coreCustomerId).containsKey(accountId)) {
							accountActions.get(coreCustomerId).put(accountId,
									new HashMap<String, Map<String, Boolean>>());
						}
						
						if (!accountActions.get(coreCustomerId).get(accountId).containsKey(featureId)) {
							accountActions.get(coreCustomerId).get(accountId).put(featureId,
									new HashMap<String, Boolean>());
						}
						accountActions.get(coreCustomerId).get(accountId).get(featureId).put(actionId, true);
					}
				}
			}
			
			if (actionLimitsDTO.getNewFeatureAction() != null) {
				Map<String, Set<String>> featureActions = actionLimitsDTO.getNewFeatureAction();
				for (String feature : featureActions.keySet()) {
					for (String action : featureActions.get(feature)) {
						if (actionLimitsDTO.getMonetaryActions().contains(action)
								|| actionLimitsDTO.getAccountLevelActions().contains(action)) {
							for (String accountId : accountActions.get(coreCustomerId).keySet()) {
								if(!accountActions.get(coreCustomerId).get(accountId).containsKey(feature)
										|| !accountActions.get(coreCustomerId).get(accountId).get(feature).containsKey(action)) {
									accountLevelActions.get(accountId).add(action);
								}
							}
						}
					}
				}
			}

			Callable<Result> callable = new Callable<Result>() {
				public Result call() {
					try {
						createCustomerActionEntries(customerId, contractId, coreCustomerId, serviceDefinitionId,
								groupId, fabricRequestManager, actionLimitsDTO, globalActions, accountActions);
					} catch (Exception e) {
					}
					return new Result();
				}

				private void createCustomerActionEntries(String customerId, String contractId, String coreCustomerId,
						String serviceDefinitionId, String groupId, FabricRequestManager fabricRequestManager,
						FeatureActionLimitsDTO featureActionDTO, Map<String, Map<String, Map<String, Boolean>>> globalActions, Map<String, Map<String, Map<String, Map<String, Boolean>>>> accountActions) {
					
					Map<String, String> input = new HashMap<String, String>();
					input.put(DBPUtilitiesConstants.FILTER, filter);
					JsonObject response = new JsonObject();
					try {
						response = com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager,
								input, com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
								URLConstants.CUSTOMER_ACTION_LIMITS_GET);
					} catch (HttpCallException e) {
						
						alert.prepareError(e.toString()).log();
					}
					JsonArray jsonArray = new JsonArray();
					if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
						JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
						if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
							jsonArray = jsonElement.getAsJsonArray();
						}
					}

					for (int i = 0; i < jsonArray.size(); i++) {
						JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();

						String featureId = jsonObject2.has(InfinityConstants.featureId)
								&& !jsonObject2.get(InfinityConstants.featureId).isJsonNull()
										? jsonObject2.get(InfinityConstants.featureId).getAsString().trim()
										: null;
						String actionId = jsonObject2.has(InfinityConstants.Action_id)
								&& !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
										? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
										: null;
						if (StringUtils.isBlank(actionId)) {
							actionId = jsonObject2.has(InfinityConstants.action_id)
									&& !jsonObject2.get(InfinityConstants.action_id).isJsonNull()
											? jsonObject2.get(InfinityConstants.action_id).getAsString().trim()
											: null;
						}
						String accountId = jsonObject2.has(InfinityConstants.Account_id)
								&& !jsonObject2.get(InfinityConstants.Account_id).isJsonNull()
										? jsonObject2.get(InfinityConstants.Account_id).getAsString()
										: null;
						if (StringUtils.isBlank(accountId)) {
							accountId = jsonObject2.has(InfinityConstants.account_id)
									&& !jsonObject2.get(InfinityConstants.account_id).isJsonNull()
											? jsonObject2.get(InfinityConstants.account_id).getAsString()
											: null;
						}

						if (featureActionDTO == null) {
							continue;
						}
						
						if (!featureActionDTO.getFeatureaction().containsKey(featureId)
								|| !featureActionDTO.getFeatureaction().get(featureId).contains(actionId)) {
								continue;
							}

						if (featureActionDTO.getGlobalLevelActions().contains(actionId)) {
							if (!globalActions.containsKey(coreCustomerId)) {
								globalActions.put(coreCustomerId, new HashMap<String, Map<String, Boolean>>());
							}
							if (!globalActions.get(coreCustomerId).containsKey(featureId)) {
								globalActions.get(coreCustomerId).put(featureId, new HashMap<String, Boolean>());
							}
							globalActions.get(coreCustomerId).get(featureId).put(actionId, true);
						} else {
							if (featureActionDTO.getAccountLevelActions().contains(actionId)) {
								
								if (!accountActions.containsKey(coreCustomerId)) {
									accountActions.put(coreCustomerId,
											new HashMap<String, Map<String, Map<String, Boolean>>>());
								}
								if (!accountActions.get(coreCustomerId).containsKey(accountId)) {
									accountActions.get(coreCustomerId).put(accountId,
											new HashMap<String, Map<String, Boolean>>());
								}
								if (!accountActions.get(coreCustomerId).get(accountId).containsKey(featureId)) {
									accountActions.get(coreCustomerId).get(accountId).put(featureId,
											new HashMap<String, Boolean>());
								}
								accountActions.get(coreCustomerId).get(accountId).get(featureId).put(actionId, true);
							}
						}
					}
					

					input = new HashMap<String, String>();
					String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
							+ DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL
							+ customerId + DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
							+ contractId ;
					input.put(DBPUtilitiesConstants.FILTER, filter);
					try {
						response = com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager,
								input, com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
								URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_GET);
					} catch (HttpCallException e) {
						
						alert.prepareError(e.toString()).log();
					} 
					Map<String, Map<String, Double>> limitsMap = new HashMap<String, Map<String,Double>>();
					Map<String, Map<String, String>> limitsIds = new HashMap<String, Map<String,String>>();
					if (response.has(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS)) {
						JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS);
						if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
							for (JsonElement element : jsonElement.getAsJsonArray()) {
								String limitTypeId = element.getAsJsonObject().get(InfinityConstants.LimitType_id)
										.getAsString();
								String limitGroupId = element.getAsJsonObject().get(InfinityConstants.limitGroupId)
										.getAsString();
								String value = element.getAsJsonObject().get(InfinityConstants.value).getAsString();
								
								String id = element.getAsJsonObject().get(InfinityConstants.id).getAsString();
								
								if(StringUtils.isNotBlank(limitGroupId)) {
									if (!limitsMap.containsKey(limitGroupId)) {
										limitsMap.put(limitGroupId, new HashMap<String, Double>());
									}
									if (!limitsIds.containsKey(limitGroupId)) {
										limitsIds.put(limitGroupId, new HashMap<String, String>());
									}
									
									limitsIds.get(limitGroupId).put(limitTypeId, id);
									
									try{
										limitsMap.get(limitGroupId).put(limitTypeId, Double.parseDouble(value));
									}catch (Exception e) {
									}
								}
							}
						}
					}

					if (featureActionDTO.getNewFeatureAction() != null) {
						Map<String, Set<String>> featureActions = featureActionDTO.getNewFeatureAction();
						ActionLimitsDTO dto = new ActionLimitsDTO();
						for (String feature : featureActions.keySet()) {
							for (String action : featureActions.get(feature)) {
								dto = new ActionLimitsDTO();
								dto.setContractId(contractId);
								dto.setCustomerId(customerId);
								dto.setCoreCustomerId(coreCustomerId);
								dto.setFeatureId(feature);
								dto.setActionId(action);
								dto.setRoleId(groupId);
								dto.setAccountLevel(featureActionDTO.getAccountLevelActions().contains(action));
								dto.setMonetory(featureActionDTO.getMonetaryActions().contains(action));

								if (featureActionDTO.getGlobalLevelActions().contains(action)) {
									if (globalActions.containsKey(coreCustomerId) 
											&& globalActions.get(coreCustomerId).containsKey(feature)
											&& globalActions.get(coreCustomerId).get(feature).containsKey(action)) {
										continue;
									}
									
									addActions(dto, com.kony.dbputilities.util.HelperMethods
											.getHeaders(fabricRequestManager.getHeadersHandler()));
								} else if (featureActionDTO.getAccountLevelActions().contains(action)) {
									for (String accountId : accountLevelActions.keySet()) {
										dto.setAccountId(accountId);
										
										if(!coreCustomerId.equals(accountsDetails.get(accountId).get(InfinityConstants.coreCustomerId))) {
											continue;
										}
										if (accountActions.containsKey(coreCustomerId) && 
												accountActions.get(coreCustomerId).containsKey(accountId)
												&& accountActions.get(coreCustomerId).get(accountId).containsKey(feature)
												&& accountActions.get(coreCustomerId).get(accountId).get(feature)
												.containsKey(action)) {
											continue;
										}

										if(!featureActionDTO.getMonetaryActions().contains(action)) {
											addActions(dto, com.kony.dbputilities.util.HelperMethods
												.getHeaders(fabricRequestManager.getHeadersHandler()));
										}
									
										if(featureActionDTO.getMonetaryActions().contains(action) && featureActionDTO.getNewMonetaryActionLimits().containsKey(feature)
												&& featureActionDTO.getNewMonetaryActionLimits().get(feature).containsKey(action)) {
	
											Map<String, String> limitMap = featureActionDTO.getNewMonetaryActionLimits()
													.get(feature).get(action);
											
											String limitGroupId = featureActionDTO.getActionsInfo().get(action)
													.get(InfinityConstants.limitGroupId).getAsString();
											dto.setLimitGroupId(limitGroupId);
											for (String limitTypeId : limitMap.keySet()) {
												double value = Double.parseDouble((limitMap.containsKey(limitTypeId)
														&& StringUtils.isNotBlank(limitMap.get(limitTypeId)))
																? limitMap.get(limitTypeId)
																: "0.0");
												if(!limitsMap.containsKey(limitGroupId)) {
													limitsMap.put(limitGroupId, new HashMap<String, Double>());
												}
												if(!limitsMap.get(limitGroupId).containsKey(limitTypeId)) {
													limitsMap.get(limitGroupId).put(limitTypeId, Double.valueOf("0.0"));
												}
												
//												if (limitTypeId.equals(InfinityConstants.DAILY_LIMIT)) {
//													dto.setDailyLimitValue(value);
//													limitsMap.get(limitGroupId).put(limitTypeId, limitsMap.get(limitGroupId).get(limitTypeId)+value);
//												} else if (limitTypeId.equals(InfinityConstants.WEEKLY_LIMIT)) {
//													dto.setWeeklyLimitValue(value);
//													limitsMap.get(limitGroupId).put(limitTypeId, limitsMap.get(limitGroupId).get(limitTypeId)+value);
//												} else if (limitTypeId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
//													dto.setMaxTransactionLimitValue(value);
//													limitsMap.get(limitGroupId).put(limitTypeId, Math.max(limitsMap.get(limitGroupId).get(limitTypeId), value));
//												}
											}
											addActions(dto, com.kony.dbputilities.util.HelperMethods
													.getHeaders(fabricRequestManager.getHeadersHandler()));
										}
									}
								}
							}
						}
					}
					
					updateLimitGroups(fabricRequestManager, customerId, coreCustomerId, contractId, limitsMap, limitsIds);
					
					
					
					
				}

				private void updateLimitGroups(FabricRequestManager fabricRequestManager, String customerId,
						String coreCustomerId, String contractId, Map<String, Map<String, Double>> limitsMap,
						Map<String, Map<String, String>> limitsIds) {
					
					
					Map<String, String> input = new HashMap<String, String>();
					
					for(String limitGroupId : limitsMap.keySet()) {
						
						for(String limitTypeId : limitsMap.get(limitGroupId).keySet()) {
							input = new HashMap<String, String>();
							input.put(InfinityConstants.Customer_id, customerId);
							input.put(InfinityConstants.contractId, contractId);
							input.put(InfinityConstants.coreCustomerId, coreCustomerId);
							input.put(InfinityConstants.limitGroupId, limitGroupId);
							input.put(InfinityConstants.LimitType_id, limitTypeId);
							input.put(InfinityConstants.value, limitsMap.get(limitGroupId).get(limitTypeId)+"");
							
							if(!limitsIds.containsKey(limitGroupId) || !limitsIds.get(limitGroupId).containsKey(limitTypeId)) {
								input.put(InfinityConstants.id, limitsIds.get(limitGroupId).get(limitTypeId));
								try {
									com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager,
											input, com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
											URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_CREATE);
								} catch ( Exception e) {
									alert.prepareError("Error occured",e).log();
								} 
							}
							else {
								input.put(InfinityConstants.id, com.kony.dbputilities.util.HelperMethods.getNewId());
								try {
									/*com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager,
											input, com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
											URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_UPDATE);*/
								} catch ( Exception e) {
									alert.prepareError("Error occured",e).log();
								} 
							}
						}
					}
				}

				private void addActions(ActionLimitsDTO dto, Map<String, Object> headerMap) {
					LimitsAndPermissionsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
							.getBackendDelegate(LimitsAndPermissionsBackendDelegate.class);
					backendDelegate.addActionsToCustomer(dto, headerMap);
				}
			};
			try {
				ThreadExecutor.getExecutor().execute(callable);
			} catch (Exception e) {
				alert.prepareError("ThreadExecutor : Exception occured while adding new featureActions ", e).log();
			}
		} catch (ApplicationException e) {
			
			alert.prepareError(e.toString()).log();
		}
		
	
    }

    private Map<String, Map<String, String>> getCoreCustomerAccountsDetails(Set<String> coreCustomers,
            String customerId, FabricRequestManager fabricRequestManager) throws HttpCallException {

        Map<String, Map<String, String>> accountDetailsMap = new HashMap<>();
        if (!coreCustomers.isEmpty()) {

            StringBuilder coreCustomersListCSV = new StringBuilder();
            Map<String, Object> input = new HashMap<String, Object>();

            for (String corecustomerId : coreCustomers) {
                if (StringUtils.isBlank(coreCustomersListCSV)) {
                    coreCustomersListCSV.append(corecustomerId);
                } else {
                    coreCustomersListCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(corecustomerId);
                }

            }
            input.put("_coreCustomerIdList", coreCustomersListCSV.toString());
            input.put("_customerId", customerId);

            JsonObject response =
                    com.kony.dbputilities.util.HelperMethods.callApiJson(fabricRequestManager, input,
                            com.kony.dbputilities.util.HelperMethods.getHeaders(fabricRequestManager),
                            URLConstants.CORECUSTOMER_ACCOUNTS_DETAILS_GET_PROC);

            if (JSONUtil.isJsonNotNull(response)
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS) &&
                    response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {

                JsonArray accountDetails = JSONUtil.getJsonArrary(response, DBPDatasetConstants.DATASET_RECORDS);
                for (JsonElement element : accountDetails) {
                    JsonObject accountRecord = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
                    String accountId = JSONUtil.getString(accountRecord, InfinityConstants.accountId);
                    String coreCustomerId = JSONUtil.getString(accountRecord, InfinityConstants.coreCustomerId);
                    String coreCustomerName = JSONUtil.getString(accountRecord, InfinityConstants.coreCustomerName);
                    String isBusinessAccount = JSONUtil.getString(accountRecord, InfinityConstants.isBusinessAccount);
                    String eStatementEnable = JSONUtil.getString(accountRecord, InfinityConstants.eStatementEnable);
                    String vpaId = JSONUtil.getString(accountRecord, "vpaId");
                    String consentStatus = JSONUtil.getString(accountRecord, "consentStatus");
                    String favouriteStatus = JSONUtil.getString(accountRecord, InfinityConstants.favouriteStatus);
                    //String isSweepCreated = JSONUtil.getString(accountRecord, InfinityConstants.isSweepCreated);
                    
                    String isSweepCreated = JSONUtil.getString(accountRecord, InfinityConstants.isSweepCreated); 
            
                    String accountStatus = JSONUtil.getString(accountRecord, InfinityConstants.accountStatus);
                    String email = JSONUtil.getString(accountRecord, InfinityConstants.email);
                    String nickName = JSONUtil.getString(accountRecord, Constants.nickName);
                    Map<String, String> detailsMap = new HashMap<String, String>();
                    detailsMap.put(InfinityConstants.coreCustomerId, coreCustomerId);
                    detailsMap.put(InfinityConstants.coreCustomerName, coreCustomerName);
                    detailsMap.put(InfinityConstants.isBusinessAccount, isBusinessAccount);
                    detailsMap.put(InfinityConstants.eStatementEnable, eStatementEnable);
                    detailsMap.put("vpaId", vpaId);
                    detailsMap.put("consentStatus", consentStatus);
                    detailsMap.put(InfinityConstants.favouriteStatus, favouriteStatus);
                    detailsMap.put(InfinityConstants.isSweepCreated, isSweepCreated);
                    detailsMap.put(InfinityConstants.accountStatus, accountStatus);
                    detailsMap.put(InfinityConstants.email, email);
                    if(!nickName.isEmpty()) {detailsMap.put(Constants.nickName, nickName);}
                    accountDetailsMap.put(accountId, detailsMap);

                }
            }
        }
        return accountDetailsMap;
    }

    private void addPermissionsToAccountRecords(JsonObject accountsJson,
            Map<String, Set<String>> accountLevelActions, Map<String, Map<String, String>> accountsDetails,
            JsonArray accountsCacheArray) {
        JsonArray accountsArray = JSONUtil.hasKey(accountsJson, "Accounts")
                ? accountsJson.get("Accounts").getAsJsonArray()
                : new JsonArray();
        String mockAccountClosureList = ServerConfigurations.MOCK_ACCOUNT_CLOSURE_LIST.getValueIfExists();
        for (JsonElement accountObject : accountsArray) {
            JsonObject account = accountObject.isJsonObject() ? accountObject.getAsJsonObject() : new JsonObject();
            String accountId = account.has("Account_id") ? account.get("Account_id").getAsString()
                    : account.get("account_id").getAsString();

            if (StringUtils.isNotBlank(accountId)) {

                account.addProperty("accountID", accountId);
                account.addProperty("Account_id", accountId);
                account.addProperty("account_id", accountId);

                if (!account.has("nickName") || account.get("nickName").isJsonNull()) {
                    account.add("nickName", account.get("displayName"));
                }

                if (accountLevelActions.containsKey(accountId)) {
                    String actionsString = convertHasetToJsonArrayString(accountLevelActions.get(accountId));
                    account.addProperty("actions", actionsString);
                    updateAccountsCache(accountsCacheArray, accountId, actionsString);
                } else if (!JSONUtil.hasKey(account, "actions")) {
                    account.addProperty("actions", "[]");
                }

                if (account.has("creditCardNumber")
                        && StringUtils.isNotBlank(account.get("creditCardNumber").getAsString())) {
                    String creditcardNumber = getMaskedValue(account.get("creditCardNumber").getAsString());
                    account.addProperty("creditCardNumber", creditcardNumber);
                }

                if (accountsDetails != null && accountsDetails.containsKey(accountId)) {
                    Map<String, String> details = accountsDetails.get(accountId);

                    String membershipId = details.get(InfinityConstants.coreCustomerId);
                    String membershipName = details.get(InfinityConstants.coreCustomerName);
                    String isBusinessAccount = details.get(InfinityConstants.isBusinessAccount);
                    String favouriteStatus = details.get(InfinityConstants.favouriteStatus);
                    String email = details.get(InfinityConstants.email);
                    String eStatementEnable = details.get(InfinityConstants.eStatementEnable);
                    String vpaId = details.get("vpaId");
                    String consentStatus = details.get("consentStatus");
                    
                    if (StringUtils.isNotBlank(vpaId)) {
                        account.addProperty("vpaId", vpaId);
                    }
                    
                    if (StringUtils.isNotBlank(consentStatus)) {
                        account.addProperty("consentStatus", consentStatus);
                    }
                    
                    
                    String accountStatus = details.get(InfinityConstants.accountStatus);
                    Boolean isSweepCreated = StringUtils.isNotBlank(details.get(InfinityConstants.isSweepCreated))?
                            Boolean.parseBoolean(details.get(InfinityConstants.isSweepCreated)):false;
                    String nickName = details.get(Constants.nickName);

                    String eStatementFromIntegration = account.has(InfinityConstants.eStatementEnable)
                            && StringUtils.isNotBlank(account.get(InfinityConstants.eStatementEnable).getAsString())
                                    ? account.get(InfinityConstants.eStatementEnable).getAsString()
                                    : "";

                    if (StringUtils.isNotBlank(membershipId)) {
                        account.addProperty("Membership_id", membershipId);
                        account.addProperty(InfinityConstants.coreCustomerId, membershipId);
                    }
           
                    if (StringUtils.isNotBlank(membershipName)) {
                        account.addProperty("MembershipName", membershipName);
                        account.addProperty(InfinityConstants.coreCustomerName, membershipName);
                    }
                    if (StringUtils.isNotBlank(isBusinessAccount)) {
                        account.addProperty(InfinityConstants.isBusinessAccount, isBusinessAccount);
                    }
                    if (StringUtils.isNotBlank(favouriteStatus)) {
                        account.addProperty(InfinityConstants.favouriteStatus, favouriteStatus);
                    }
                    if (StringUtils.isNotBlank(nickName)) {
                        account.addProperty("nickName", nickName);
                        account.addProperty("displayName", nickName);
                    }
                   
                    	account.addProperty(InfinityConstants.isSweepCreated, isSweepCreated);
                    
                   if (StringUtils.isNotBlank(accountStatus)) {
                        account.addProperty(InfinityConstants.accountStatus, accountStatus);
                    }
                    
                    
                    if (StringUtils.isNotBlank(eStatementFromIntegration)) {
                        if (StringUtils.isNotBlank(email)
                                && "true".equalsIgnoreCase(eStatementFromIntegration)) {
                            account.addProperty(InfinityConstants.email, email);
                            account.addProperty(InfinityConstants.eStatementEnable, eStatementFromIntegration);
                        } else {
                            if (account.has(InfinityConstants.email)) {
                                account.remove(InfinityConstants.email);
                            }
                            account.addProperty(InfinityConstants.eStatementEnable,
                                    DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
                        }
                    } else if (StringUtils.isNotBlank(email) && "true".equalsIgnoreCase(eStatementEnable)) {
                        account.addProperty(InfinityConstants.email, email);
                        account.addProperty(InfinityConstants.eStatementEnable, eStatementEnable);
                    } else {
                        if (account.has(InfinityConstants.email)) {
                            account.remove(InfinityConstants.email);
                        }
                        account.addProperty(InfinityConstants.eStatementEnable,
                                DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);

                    }

                }

            }

        }

    }

    private void updateAccountsCache(JsonArray accountsCacheArray, String accountId, String actionsString) {
        if (StringUtils.isNotBlank(accountId) && null != accountsCacheArray) {
            JsonObject object = new JsonObject();
            object.addProperty("Account_id", accountId);
            object.addProperty("accountID", accountId);
            object.addProperty("actions", actionsString);
            accountsCacheArray.add(object);
        }

    }

    private Set<String> getCoreCustomersList(JsonObject accountsJson, String customerId, FabricRequestManager request,
			Map<String, String> serviceDefinitions, Map<String, String> customerGroups, Map<String, String> contracts)
            throws HttpCallException,Exception {
        Set<String> coreCustomers = new HashSet<>();
		Map<String, String> serviceDefinition = new HashMap<String, String>();
        JsonArray accountsArray = JSONUtil.hasKey(accountsJson, "Accounts")
                ? accountsJson.get("Accounts").getAsJsonArray()
                : new JsonArray();
        for (JsonElement accountObject : accountsArray) {
            JsonObject account = accountObject.isJsonObject() ? accountObject.getAsJsonObject() : new JsonObject();
            String mockMortgageResponse = ServerConfigurations.MOCK_MORTGAGE_RESPONSE.getValue();
            if(mockMortgageResponse != null && mockMortgageResponse.equalsIgnoreCase("Yes")) {
            	if(!account.get("accountType").getAsString().equalsIgnoreCase("mortgageFacility")) {
            		if (JSONUtil.hasKey(account, "Account_id") && JSONUtil.hasKey(account, "Membership_id")) {
            			String coreCustomerId = account.get("Membership_id").getAsString();

            			if (!coreCustomers.contains(coreCustomerId)) {
            				coreCustomers.add(coreCustomerId);
            			}
            		}
            	}
            }else {
            	if (JSONUtil.hasKey(account, "Account_id") && JSONUtil.hasKey(account, "Membership_id")) {
            		String coreCustomerId = account.get("Membership_id").getAsString();

            		if (!coreCustomers.contains(coreCustomerId)) {
            			coreCustomers.add(coreCustomerId);
            		}
            	}
            }
        }

        if (coreCustomers.isEmpty()) {

            String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId;
            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject response =
                    com.kony.dbputilities.util.HelperMethods.callApiJson(request, input,
                            com.kony.dbputilities.util.HelperMethods.getHeaders(request),
                            URLConstants.CONTRACT_CUSTOMERS_GET);

            if (JSONUtil.isJsonNotNull(response)
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS) &&
                    response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).isJsonArray()) {

                JsonArray customers = JSONUtil.getJsonArrary(response, DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
                for (JsonElement element : customers) {
                    JsonObject actionRecord = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
                    String coreCustomerId = JSONUtil.getString(actionRecord, InfinityConstants.coreCustomerId);
					String contractId = JSONUtil.getString(actionRecord, InfinityConstants.contractId);
                    if (StringUtils.isNotBlank(coreCustomerId)) {
                        coreCustomers.add(coreCustomerId);
						if (StringUtils.isNotBlank(contractId) && !serviceDefinition.containsKey(contractId)) {
							contracts.put(coreCustomerId, contractId);
							filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId;
							input = new HashMap<String, Object>();
							input.put(DBPUtilitiesConstants.FILTER, filter);
							JsonObject contractResponse = com.kony.dbputilities.util.HelperMethods.callApiJson(request,
									input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
									URLConstants.CONTRACT_GET);
							if (JSONUtil.isJsonNotNull(contractResponse)
									&& JSONUtil.hasKey(contractResponse, DBPDatasetConstants.DATASET_CONTRACT)
									&& contractResponse.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
								JsonArray contractArray = contractResponse.get(DBPDatasetConstants.DATASET_CONTRACT)
										.getAsJsonArray();
								for (JsonElement contractelement : contractArray) {
									String serviceDefinitionId = JSONUtil.getString(contractelement.getAsJsonObject(),
											"servicedefinitionId");
									serviceDefinition.put(contractId, serviceDefinitionId);
                    }
                }
            }
						serviceDefinitions.put(coreCustomerId, serviceDefinition.get(contractId));
					}
				}
			}
		}

		for (String coreCustomerId : coreCustomers) {
			if (!serviceDefinitions.containsKey(coreCustomerId)) {
				String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId;
				Map<String, Object> input = new HashMap<String, Object>();
				input.put(DBPUtilitiesConstants.FILTER, filter);

				JsonObject response = com.kony.dbputilities.util.HelperMethods.callApiJson(request, input,
						com.kony.dbputilities.util.HelperMethods.getHeaders(request),
						URLConstants.CONTRACTCORECUSTOMER_GET);

				if (JSONUtil.isJsonNotNull(response)
						&& JSONUtil.hasKey(response, DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS)
						&& response.get(DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS).isJsonArray()) {

					JsonArray customers = JSONUtil.getJsonArrary(response, DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS);
					for (JsonElement element : customers) {
						JsonObject contractCoreCustomer = element.getAsJsonObject();
						String contractId = contractCoreCustomer.has(InfinityConstants.contractId)
								&& !contractCoreCustomer.get(InfinityConstants.contractId).isJsonNull()
										? contractCoreCustomer.get(InfinityConstants.contractId).getAsString()
										: null;
						if (StringUtils.isNotBlank(contractId) && !serviceDefinition.containsKey(contractId)) {
							contracts.put(coreCustomerId, contractId);
							filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId;
							input = new HashMap<String, Object>();
							input.put(DBPUtilitiesConstants.FILTER, filter);
							JsonObject contractResponse = com.kony.dbputilities.util.HelperMethods.callApiJson(request,
									input, com.kony.dbputilities.util.HelperMethods.getHeaders(request),
									URLConstants.CONTRACT_GET);
							if (JSONUtil.isJsonNotNull(contractResponse)
									&& JSONUtil.hasKey(contractResponse, DBPDatasetConstants.DATASET_CONTRACT)
									&& contractResponse.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
								JsonArray contractArray = contractResponse.get(DBPDatasetConstants.DATASET_CONTRACT)
										.getAsJsonArray();
								for (JsonElement contractelement : contractArray) {
									String serviceDefinitionId = JSONUtil.getString(contractelement.getAsJsonObject(),
											"servicedefinitionId");
									serviceDefinition.put(contractId, serviceDefinitionId);
								}
							}
						}
						serviceDefinitions.put(coreCustomerId, serviceDefinition.get(contractId));
					}
				}
			}

			if (!customerGroups.containsKey(coreCustomerId)) {
				String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
						+ DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL
						+ customerId;
				Map<String, Object> input = new HashMap<String, Object>();
				input.put(DBPUtilitiesConstants.FILTER, filter);
				JsonObject jsonResponse = com.kony.dbputilities.util.HelperMethods.callApiJson(request, input,
						com.kony.dbputilities.util.HelperMethods.getHeaders(request), URLConstants.CUSTOMER_GROUP_GET);
				if (jsonResponse.has(DBPDatasetConstants.DATASET_CUSTOMERGROUP)) {
					JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP);
					if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
						JsonArray jsonArray = jsonElement.getAsJsonArray();
						JsonObject contract = jsonArray.get(0).getAsJsonObject();
						String groupId = contract.has(InfinityConstants.Group_id)
								&& !contract.get(InfinityConstants.Group_id).isJsonNull()
										? contract.get(InfinityConstants.Group_id).getAsString()
										: null;
						customerGroups.put(coreCustomerId, groupId);
					}
				}
			}
		}
        return coreCustomers;

    }

    private String convertHasetToJsonArrayString(Set<String> addedActions) {
        JsonArray actionsList = new JsonArray();
        for (String action : addedActions) {
            actionsList.add(action);
        }
        return actionsList.toString();
    }

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {

            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject customParams = new JsonObject();

            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_ACCOUNT_ACTION;
            String eventSubType = PARAM_INTERNAL_ACCOUNTS_FETCH;
            String producer = "Accounts/getAccountsPostLogin";
            String statusId = PARAM_SID_EVENT_FAILURE;

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equals(PARAM_TRUE)&& responsePayload.has("Accounts")) {
                JsonArray accountsArray = responsePayload.getAsJsonArray("Accounts");
                Integer noOfAccounts = accountsArray.size();
                responsePayload.addProperty("noOfAccounts", noOfAccounts.toString());
                responsePayload.remove("Accounts");
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responsePayload,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods", e2).log();
                }
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured in GetAccountsPostLoginObjectServicePostProcessor ", ex).log();
        }

    }

    private String getMaskedValue(String creditcardNumber) {
        String lastFourDigits;
        if (StringUtils.isNotBlank(creditcardNumber)) {
            if (creditcardNumber.length() > 4) {
                lastFourDigits = creditcardNumber.substring(creditcardNumber.length() - 4);
                creditcardNumber = "XXXXX" + lastFourDigits;
            } else {
                creditcardNumber = "XXXXX" + creditcardNumber;
            }
        }

        return creditcardNumber;
    }

}