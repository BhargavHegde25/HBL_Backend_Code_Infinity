package com.temenos.dbx.eum.product.contract.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractAccountBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class ContractCoreCustomerBackendDelegateImpl implements ContractCoreCustomerBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final String CONTRACT_ID = "contractId";
    private static final String TAXID = "taxId";
    private static final String ADDRESSLINE1 = "addressLine1";
    private static final String ADDRESSLINE2 = "addressLine2";
    private static final String CITYNAME = "cityName";
    private static final String COUNTRY = "country";
    private static final String ZIPCODE = "zipCode";
    private static final String STATE = "state";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String INDUSTRY = "industry";
    private static final String CORECUSTOMERID = "coreCustomerId";
    private static final String ISPRIMARY = "isPrimary";
    private static final String COMPANYLEGALUNIT = "companyLegalUnit";
    private static final String LegalEntityId = "legalEntityId";

    @Override
    public ContractCoreCustomersDTO createContractCustomer(ContractCoreCustomersDTO inputCustomerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomersDTO resultCustomerDTO = null;
        if (inputCustomerDTO == null || StringUtils.isBlank(inputCustomerDTO.getId())
                || StringUtils.isBlank(inputCustomerDTO.getContractId())) {
            return resultCustomerDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputCustomerDTO, false);
            JsonObject customerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTCORECUSTOMER_CREATE);
            if (JSONUtil.isJsonNotNull(customerJson)
                    && JSONUtil.hasKey(customerJson, DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)
                    && customerJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).isJsonArray()) {
                JsonArray customersArray =
                        customerJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).getAsJsonArray();
                JsonObject object =
                        customersArray.size() > 0 ? customersArray.get(0).getAsJsonObject() : new JsonObject();
                resultCustomerDTO =
                        (ContractCoreCustomersDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ContractCoreCustomersDTO.class, false);
                if (resultCustomerDTO == null || StringUtils.isBlank(resultCustomerDTO.getId())) {
                	diagnostic.prepareDebug("contractcorecustomer has not returned id").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10356);
                }

            } else {
            	diagnostic.prepareDebug("incorrect response from contractcorecustomer").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10356);
            }

        } catch (Exception e) {
        	alert.prepareError("exception while inserting into contractcorecustomer", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10356);
        }
        return resultCustomerDTO;
    }

    private void processAccountsData(List<ContractCoreCustomersDTO> dtoList,

			JsonObject contractCoreCustomerandaccountsJson) throws IOException {

		if (null == dtoList || null == contractCoreCustomerandaccountsJson)

			return;

		if (JSONUtil.hasKey(contractCoreCustomerandaccountsJson, DBPDatasetConstants.DATASET_RECORDS1)

				&& contractCoreCustomerandaccountsJson.get(DBPDatasetConstants.DATASET_RECORDS1).isJsonArray()) {

			JsonArray array = contractCoreCustomerandaccountsJson.get(DBPDatasetConstants.DATASET_RECORDS1)

					.getAsJsonArray();

			if (array == null || array.isJsonNull())

				return;

			Map<String, JsonArray> corecusAccountsmapping = new HashMap<>();

			for (JsonElement customeraccount : array) {

				JsonObject customeraccountjsonobj = customeraccount.getAsJsonObject();

				if (customeraccountjsonobj.has(DBPUtilitiesConstants.CORECUSTOMERID)) {

					String coreCustomerid = customeraccountjsonobj.get(DBPUtilitiesConstants.CORECUSTOMERID)

							.getAsString();

					if (corecusAccountsmapping.containsKey(coreCustomerid)) {

						corecusAccountsmapping.get(coreCustomerid).add(customeraccount);

					} else {

						JsonArray custaccountsarray = new JsonArray();

						custaccountsarray.add(customeraccount);

						corecusAccountsmapping.put(coreCustomerid, custaccountsarray);

					}

				}

			}

			for (ContractCoreCustomersDTO corecustDTO : dtoList) {

				if (corecusAccountsmapping.containsKey(corecustDTO.getCoreCustomerId())) {

					List<ContractAccountsDTO> customerAccounts = JSONUtils.parseAsList(

							corecusAccountsmapping.get(corecustDTO.getCoreCustomerId()).toString(),

							ContractAccountsDTO.class);

					for (ContractAccountsDTO accountDTO : customerAccounts) {

						accountDTO.setAccountType((HelperMethods.getAccountsNames().get(accountDTO.getTypeId())));

					}

					corecustDTO.setAccounts(customerAccounts);

				}

			}

		}

	}

    @Override
    public List<ContractCoreCustomersDTO> getContractCoreCustomers(String contractId, boolean getAccountDetails,

			boolean getCompleteDetails, Map<String, Object> headersMap) throws ApplicationException {

		List<ContractCoreCustomersDTO> dtoList = null;

		try {

			Map<String, Object> inputParams = new HashMap<>();

			inputParams.put("_contractId", contractId);

			JsonObject contractCoreCustomerandaccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams,

					headersMap, URLConstants.CONTRACT_CORECUSTOMER_ACCOUNTS_PROC);

			if (null != contractCoreCustomerandaccountsJson

					&& JSONUtil.hasKey(contractCoreCustomerandaccountsJson, DBPDatasetConstants.DATASET_RECORDS)

					&& contractCoreCustomerandaccountsJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {

				JsonArray array = contractCoreCustomerandaccountsJson.get(DBPDatasetConstants.DATASET_RECORDS)

						.getAsJsonArray();

				dtoList = JSONUtils.parseAsList(array.toString(), ContractCoreCustomersDTO.class);

				if (null != dtoList) {

					if (getAccountDetails) {
						processAccountsData(dtoList, contractCoreCustomerandaccountsJson);

					}

					if (getCompleteDetails) {
						
						getAndProcessBackendData(dtoList, headersMap);

					}

				}

			}

		} catch (ApplicationException e) {

			throw new ApplicationException(e.getErrorCodeEnum());

		} catch (Exception e) {

			throw new ApplicationException(ErrorCodeEnum.ERR_10366);

		}

		return dtoList;

	}

    private void getAndProcessBackendData(List<ContractCoreCustomersDTO> contractCoreCustomersDTOs,

			Map<String, Object> headersMap) throws ApplicationException {

		List<MembershipDTO> memberShipDTOs = new ArrayList<>();

		CoreCustomerBackendDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl

				.getBackendDelegate(CoreCustomerBackendDelegate.class);

		for (ContractCoreCustomersDTO contractCoreCustomersDTO : contractCoreCustomersDTOs) {

			MembershipDTO member = new MembershipDTO();

			member.setId(contractCoreCustomersDTO.getCoreCustomerId());
			member.setCompanyLegalUnit(contractCoreCustomersDTO.getCompanyLegalUnit());;
			memberShipDTOs.add(member);

		}

		try {

			DBXResult customerResult = coreCustomerBD.searchCoreCustomers(memberShipDTOs, headersMap);

			Map<String, JsonObject> custDataMap = new HashMap<>();

			if (customerResult != null && StringUtils.isBlank(customerResult.getDbpErrCode())

					&& StringUtils.isBlank(customerResult.getDbpErrMsg()) && customerResult.getResponse() != null) {

				JsonArray customerArray = (JsonArray) customerResult.getResponse();

				for (JsonElement customerelem : customerArray) {

					JsonObject customer = customerelem.getAsJsonObject();

					if (customer.has("id")) {

						custDataMap.put(customer.get("id").getAsString(), customer);

					}

				}

				for (ContractCoreCustomersDTO contractCoreCustomersDTO : contractCoreCustomersDTOs) {

					if (custDataMap.containsKey(contractCoreCustomersDTO.getCoreCustomerId())) {

						fillcontractCoreCustomersDTOData(contractCoreCustomersDTO,

								custDataMap.get(contractCoreCustomersDTO.getCoreCustomerId()));

					}

				}

			}

		} catch (ApplicationException e) {

			throw new ApplicationException(e.getErrorCodeEnum());

		} catch (Exception e) {

			throw new ApplicationException(ErrorCodeEnum.ERR_10366);

		}

    }

	private void fillcontractCoreCustomersDTOData(ContractCoreCustomersDTO contractCoreCustomersDTO,

			JsonObject customerResponse) {

		String taxId = customerResponse.has(TAXID) ? customerResponse.get(TAXID).getAsString() : "";

		String addressLine1 = customerResponse.has(ADDRESSLINE1) ? customerResponse.get(ADDRESSLINE1).getAsString()

				: "";

		String addressLine2 = customerResponse.has(ADDRESSLINE2) ? customerResponse.get(ADDRESSLINE2).getAsString()

				: "";

		String cityName = customerResponse.has(CITYNAME) ? customerResponse.get(CITYNAME).getAsString() : "";

		String country = customerResponse.has(COUNTRY) ? customerResponse.get(COUNTRY).getAsString() : "";

		String zipcode = customerResponse.has(ZIPCODE) ? customerResponse.get(ZIPCODE).getAsString() : "";

		String state = customerResponse.has(STATE) ? customerResponse.get(STATE).getAsString() : "";

		String phone = customerResponse.has(PHONE) ? customerResponse.get(PHONE).getAsString() : "";

		String email = customerResponse.has(EMAIL) ? customerResponse.get(EMAIL).getAsString() : "";

		String industry = customerResponse.has(INDUSTRY) ? customerResponse.get(INDUSTRY).getAsString() : "";
		String legalEntityId = customerResponse.has(COMPANYLEGALUNIT) ? customerResponse.get(COMPANYLEGALUNIT).getAsString() : "";
		contractCoreCustomersDTO.setTaxId(taxId);

		contractCoreCustomersDTO.setAddressLine1(addressLine1);

		contractCoreCustomersDTO.setAddressLine2(addressLine2);

		contractCoreCustomersDTO.setCityName(cityName);

		contractCoreCustomersDTO.setCountry(country);

		contractCoreCustomersDTO.setZipCode(zipcode);

		contractCoreCustomersDTO.setState(state);

		contractCoreCustomersDTO.setPhone(phone);

		contractCoreCustomersDTO.setEmail(email);

		contractCoreCustomersDTO.setIndustry(industry);
		contractCoreCustomersDTO.setCompanyLegalUnit(legalEntityId);

	}


	@Override
    public Set<String> getValidCoreContractCustomers(Set<String> customersList,
    		 String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException {
        Set<String> validCustomers = new HashSet<String>();
        try {
            if (customersList == null || customersList.isEmpty()) {
                return validCustomers;
            }
            StringBuilder customersListCSV = new StringBuilder();
            for (String customer : customersList) {
                if (StringUtils.isBlank(customersListCSV)) {
                    customersListCSV.append(customer);
                } else {
                    customersListCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(customer);
                }

            }

            Map<String, Object> inputParams = new HashMap<>();
            String valisCustomersCSV;
            inputParams.put("_coreCustomersCSV", customersListCSV.toString());
            inputParams.put("companyLegalUnit", legalEntityId);
            JsonObject validcustomersListJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.GET_VALID_CORECUSTOMERS_LIST_PROC);
            if (null != validcustomersListJson
                    && JSONUtil.hasKey(validcustomersListJson, DBPDatasetConstants.DATASET_RECORDS)
                    && validcustomersListJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray array = validcustomersListJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (null != object && JSONUtil.hasKey(object, "validCustomers")) {
                        valisCustomersCSV = object.get("validCustomers").getAsString();
                        validCustomers =
                                HelperMethods.splitString(valisCustomersCSV, DBPUtilitiesConstants.COMMA_SEPERATOR);
                    }
                }

            }
        } catch (Exception e) {
        	alert.prepareError("exception while validating corecustomers", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10367);
        }

        return validCustomers;
    }

    @Override
    public void deleteContractCoreCustomers(Set<String> contractCoreCustomers, String contractId,
            Map<String, Object> headersMap) throws ApplicationException {
        StringBuilder customersListCSV = new StringBuilder();
        try {
            for (String customer : contractCoreCustomers) {
                if (StringUtils.isBlank(customersListCSV)) {
                    customersListCSV.append(customer);
                } else {
                    customersListCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(customer);
                }

            }

            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("_contractCustomersList", customersListCSV.toString());
            inputParams.put("_contractId", contractId);
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACT_CORECUSTOMERS_DELETE_PROC);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10370);
        }

    }

    @Override
    public Map<String, Set<String>> getCoreCustomerAccountsFeaturesActions(String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        final String DATASET_RECORDS1 = "records1";
        final String DATASET_RECORDS2 = "records2";

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Set<String>> coreCustomerDetailsMap = new HashMap<>();
        inputParams.put("_coreCustomerId", coreCustomerId);
        inputParams.put("_contractId", contractId);
        JsonObject coreCustomerDetails = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CONTRACT_CORECUSTOMER_DETAILS_GET_PROC);
        if (null != coreCustomerDetails
                && JSONUtil.hasKey(coreCustomerDetails, DBPDatasetConstants.DATASET_RECORDS)
                && coreCustomerDetails.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                && JSONUtil.hasKey(coreCustomerDetails, DATASET_RECORDS1)
                && coreCustomerDetails.get(DATASET_RECORDS1).isJsonArray()
                && JSONUtil.hasKey(coreCustomerDetails, DATASET_RECORDS2)
                && coreCustomerDetails.get(DATASET_RECORDS2).isJsonArray()) {
            JsonArray customerAccountsArray =
                    coreCustomerDetails.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
            JsonArray customerFeaturesArray = coreCustomerDetails.get(DATASET_RECORDS1).getAsJsonArray();
            JsonArray customerActionsArray = coreCustomerDetails.get(DATASET_RECORDS2).getAsJsonArray();
            JsonElement accountElement =
                    customerAccountsArray.size() > 0 ? customerAccountsArray.get(0) : new JsonObject();
            JsonElement featuresElement =
                    customerFeaturesArray.size() > 0 ? customerFeaturesArray.get(0) : new JsonObject();
            JsonElement actionsElement =
                    customerActionsArray.size() > 0 ? customerActionsArray.get(0) : new JsonObject();
            if (accountElement.isJsonObject()) {
                JsonObject object = accountElement.getAsJsonObject();
                if (null != object && JSONUtil.hasKey(object, "coreCustomerAccounts")) {

                    coreCustomerDetailsMap.put("accounts",
                            HelperMethods.splitString(object.get("coreCustomerAccounts").getAsString(),
                                    DBPUtilitiesConstants.COMMA_SEPERATOR));
                } else {
                    coreCustomerDetailsMap.put("accounts", new HashSet<String>());
                }
            }
            if (featuresElement.isJsonObject()) {
                JsonObject object = featuresElement.getAsJsonObject();
                if (null != object && JSONUtil.hasKey(object, "coreCustomerFeatures")) {

                    coreCustomerDetailsMap.put("features",
                            HelperMethods.splitString(object.get("coreCustomerFeatures").getAsString(),
                                    DBPUtilitiesConstants.COMMA_SEPERATOR));
                } else {
                    coreCustomerDetailsMap.put("features", new HashSet<String>());
                }
            }
            if (actionsElement.isJsonObject()) {
                JsonObject object = actionsElement.getAsJsonObject();
                if (null != object && JSONUtil.hasKey(object, "coreCustomerActions")) {

                    coreCustomerDetailsMap.put("actions",
                            HelperMethods.splitString(object.get("coreCustomerActions").getAsString(),
                                    DBPUtilitiesConstants.COMMA_SEPERATOR));
                } else {
                    coreCustomerDetailsMap.put("actions", new HashSet<String>());
                }
            }

        }
        return coreCustomerDetailsMap;
    }

    @Override
    public void deleteCoreCustomerAccounts(Set<String> accounts, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        StringBuilder accountsCSV = new StringBuilder();
        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

        try {
            if (null != accounts && !accounts.isEmpty()) {
                for (String accountId : accounts) {
                    if (StringUtils.isBlank(accountsCSV)) {
                        accountsCSV.append(accountId);
                    } else {
                        accountsCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(accountId);
                    }

                }

                Map<String, Object> inputParams = new HashMap<>();
                inputParams.put("_contractId", contractId);
                inputParams.put("_coreCustomerId", coreCustomerId);
                inputParams.put("_accountsCSV", accountsCSV.toString());
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.CONTRACT_CORECUSTOMER_ACCOUNTS_DELETE_PROC);

                approvalmatrixDelegate.deleteApprovalMatrixForAccounts(contractId, coreCustomerId, accounts);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10371);
        }

    }

    @Override
    public void deleteCoreCustomerFeatures(Set<String> features, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        StringBuilder featuresCSV = new StringBuilder();
        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
        try {
            if (null != features && !features.isEmpty()) {
                for (String featureId : features) {
                    if (StringUtils.isBlank(featuresCSV)) {
                        featuresCSV.append(featureId);
                    } else {
                        featuresCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(featureId);
                    }
                }
                Map<String, Object> inputParams = new HashMap<>();
                inputParams.put("_contractId", contractId);
                inputParams.put("_coreCustomerId", coreCustomerId);
                inputParams.put("_featuresCSV", featuresCSV.toString());
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.CONTRACT_CORECUSTOMER_FEATURES_DELETE_PROC);
                approvalmatrixDelegate.deleteApprovalMatrixFeatureSet(contractId, coreCustomerId, features);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10372);
        }

    }

    @Override
    public void deleteCoreCustomerActions(Set<String> actions, String contractId, String coreCustomerId,String accountId,
            Map<String, Object> headersMap) throws ApplicationException {
        StringBuilder actionsCSV = new StringBuilder();
        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
        try {
            if (null != actions && !actions.isEmpty()) {
                for (String actionId : actions) {
                    if (StringUtils.isBlank(actionsCSV)) {
                        actionsCSV.append(actionId);
                    } else {
                        actionsCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(actionId);
                    }
                }
                Map<String, Object> inputParams = new HashMap<>();
                inputParams.put("_contractId", contractId);
                inputParams.put("_coreCustomerId", coreCustomerId);
                inputParams.put("_accountId", accountId);
                inputParams.put("_actionsCSV", actionsCSV.toString());
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.CONTRACT_CORECUSTOMER_ACTIONS_DELETE_PROC);
                approvalmatrixDelegate.deleteApprovalMatrixFeatureForActions(contractId, coreCustomerId, actions);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10373);
        }

    }

    @Override
    public ContractCoreCustomersDTO getContractCoreCustomers(ContractCoreCustomersDTO contractCoreCustomersDTO,
            Map<String, Object> headersMap) throws ApplicationException {
    	if (StringUtils.isBlank(contractCoreCustomersDTO.getCoreCustomerId()) && StringUtils.isBlank(contractCoreCustomersDTO.getCompanyLegalUnit())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        Map<String, Object> inputParams = new HashMap<>();
        ContractCoreCustomersDTO dto = null;
        String filter = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + contractCoreCustomersDTO.getCoreCustomerId()
       + DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + contractCoreCustomersDTO.getCompanyLegalUnit();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CONTRACTCORECUSTOMER_GET);

        if (null != contractJson && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)
                && contractJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).isJsonArray()) {
            JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).getAsJsonArray();
            JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
            dto = (ContractCoreCustomersDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                    ContractCoreCustomersDTO.class, false);
            if (null == dto || StringUtils.isBlank(dto.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10765);
            }
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10765);
        }
        return dto;
    }

    @Override
    public ContractCoreCustomersDTO updateContractCustomer(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomersDTO resultCustomerDTO = null;
        if (customerDTO == null || StringUtils.isBlank(customerDTO.getId())) {
            return resultCustomerDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(customerDTO, false);
            JsonObject customerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTCORECUSTOMER_UPDATE);
            if (JSONUtil.isJsonNotNull(customerJson)
                    && JSONUtil.hasKey(customerJson, DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)
                    && customerJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).isJsonArray()) {
                JsonArray customersArray =
                        customerJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).getAsJsonArray();
                JsonObject object =
                        customersArray.size() > 0 ? customersArray.get(0).getAsJsonObject() : new JsonObject();
                resultCustomerDTO =
                        (ContractCoreCustomersDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ContractCoreCustomersDTO.class, false);
                if (resultCustomerDTO == null || StringUtils.isBlank(resultCustomerDTO.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10399);
                }

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10399);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10399);
        }
        return resultCustomerDTO;
    }

    @Override
    public List<ContractCoreCustomersDTO> getContractCoreCustomerDetails(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        List<ContractCoreCustomersDTO> dtoList = null;

        if (null == customerDTO || StringUtils.isBlank(customerDTO.getContractId())) {
            return dtoList;
        }

        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = CONTRACT_ID + DBPUtilitiesConstants.EQUAL + customerDTO.getContractId();
            if (StringUtils.isNotBlank(customerDTO.getCoreCustomerId())) {
                filter = filter + DBPUtilitiesConstants.AND + CORECUSTOMERID + DBPUtilitiesConstants.EQUAL
                        + customerDTO.getCoreCustomerId();
            }
            if (StringUtils.isNotBlank(customerDTO.getIsPrimary())) {
                filter = filter + DBPUtilitiesConstants.AND + ISPRIMARY + DBPUtilitiesConstants.EQUAL
                        + customerDTO.getIsPrimary();
            }
            if(StringUtils.isNotBlank(customerDTO.getCompanyLegalUnit())) {
            	filter = filter + DBPUtilitiesConstants.AND + COMPANYLEGALUNIT + DBPUtilitiesConstants.EQUAL
                        + customerDTO.getCompanyLegalUnit();
            }
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject contractCoreCustomerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTCORECUSTOMER_GET);
            if (null != contractCoreCustomerJson
                    && JSONUtil.hasKey(contractCoreCustomerJson, DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)
                    && contractCoreCustomerJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).isJsonArray()) {
                JsonArray array = contractCoreCustomerJson.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)
                        .getAsJsonArray();
                dtoList = JSONUtils.parseAsList(array.toString(), ContractCoreCustomersDTO.class);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10398);
        }
        return dtoList;
    }
}