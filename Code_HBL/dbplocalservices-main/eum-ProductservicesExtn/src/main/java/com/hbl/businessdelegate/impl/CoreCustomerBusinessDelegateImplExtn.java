package com.hbl.businessdelegate.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.impl.CoreCustomerBusinessDelegateImpl;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;

public class CoreCustomerBusinessDelegateImplExtn extends CoreCustomerBusinessDelegateImpl{
	LoggerUtil logger = new LoggerUtil(CoreCustomerBusinessDelegateImplExtn.class);

    private static final String ID = "id";
    private static final String ACCOUNTS = "accounts";
    private static final String CORE_CUSTOMER_ACCOUNTS = "coreCustomerAccounts";
    private static final String CORE_CUSTOMER_ID = "coreCustomerId";
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
    private static final String ISBUSINESS = "isBusiness";
    private static final String NAME = "name";
    private static final String SECTORID = "sectorId";
    private static final String IDTYPE_ID = "legalDocumentName";
    private static final String IDVALUE = "legalId";
    private static final String IDISSUEDATE = "legalIssueDate";
    private static final String IDEXPIRYDATE = "legalExpiredDate";
    private static final String PARTYID = "partyId";
	
	
    public List<MembershipDTO> getHBLMembershipDetails(String customerFullName, String customerAccountNumber,String dateofbirth, String legalEntityId, String email, String phone,
            Map<String, Object> headerMap) throws ApplicationException {
        CoreCustomerBackendDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CoreCustomerBackendDelegate.class);

        MembershipDTO membershipDTO = new MembershipDTO();
        List<MembershipDTO> resultList = new ArrayList<>();
        MembershipDTO resultMembershipDTO;
        membershipDTO.setDateOfBirth(dateofbirth);
        membershipDTO.setName(customerFullName);
        membershipDTO.setTaxId(customerAccountNumber); //mapping accountNumber to SSN as we don't have account number field in MembershipDTO 
        membershipDTO.setPhone(phone);
        membershipDTO.setEmail(email);
        membershipDTO.setCompanyLegalUnit(legalEntityId);
        DBXResult customerResult = null;
        try {
        customerResult =
                coreCustomerBD.searchCoreCustomers(membershipDTO, headerMap);

        }catch (ApplicationException e) {
            logger.error("ApplicationException Occured while searching a customer",e);
            throw new ApplicationException(e.getErrorCodeEnum());
        }

        if (customerResult != null && StringUtils.isBlank(customerResult.getDbpErrCode())
                && StringUtils.isBlank(customerResult.getDbpErrMsg())
                && customerResult.getResponse() != null) {
            JsonArray customerArray = (JsonArray) customerResult.getResponse();
            for (JsonElement element : customerArray) {
                resultMembershipDTO = new MembershipDTO();
                String integration = EnvironmentConfigurationsHandler.getServerProperty("INTEGRATION_NAME");
                JsonObject customerResponse = element.getAsJsonObject();
                String id = "";
				if (integration.equalsIgnoreCase("party")) {
					id = customerResponse.has(PARTYID) ? customerResponse.get(PARTYID).getAsString() : "";
				} else {
					id = customerResponse.has(ID) ? customerResponse.get(ID).getAsString() : "";

				}
         
                String taxId =
                        customerResponse.has(TAXID) ? customerResponse.get(TAXID).getAsString()
                                : "";
                String addressLine1 =
                        customerResponse.has(ADDRESSLINE1)
                                ? customerResponse.get(ADDRESSLINE1).getAsString()
                                : "";
                String addressLine2 =
                        customerResponse.has(ADDRESSLINE2)
                                ? customerResponse.get(ADDRESSLINE2).getAsString()
                                : "";
                String cityName =
                        customerResponse.has(CITYNAME)
                                ? customerResponse.get(CITYNAME).getAsString()
                                : "";
                String country =
                        customerResponse.has(COUNTRY) ? customerResponse.get(COUNTRY).getAsString()
                                : "";
                String zipcode =
                        customerResponse.has(ZIPCODE) ? customerResponse.get(ZIPCODE).getAsString()
                                : "";
                String state =
                        customerResponse.has(STATE) ? customerResponse.get(STATE).getAsString()
                                : "";

                String industry =
                        customerResponse.has(INDUSTRY) ? customerResponse.get(INDUSTRY).getAsString()
                                : "";
                String isBusiness =
                        customerResponse.has(ISBUSINESS) ? customerResponse.get(ISBUSINESS).getAsString()
                                : "";
                String name =
                        customerResponse.has(NAME) ? customerResponse.get(NAME).getAsString()
                                : "";

                String sectorId =
                        customerResponse.has(SECTORID) ? customerResponse.get(SECTORID).getAsString()
                                : "";

                String phoneFromSearch =
                        customerResponse.has(PHONE) ? customerResponse.get(PHONE).getAsString()
                                : "";

                if (StringUtils.isBlank(isBusiness) && StringUtils.isNotBlank(sectorId)) {
                    if (Integer.parseInt(sectorId) >= 2000) {
                        isBusiness = DBPUtilitiesConstants.BOOLEAN_STRING_TRUE;
                    } else {
                        isBusiness = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
                    }
                }
                resultMembershipDTO.setId(id);
                resultMembershipDTO.setName(name);
                resultMembershipDTO.setTaxId(taxId);
                resultMembershipDTO.setAddressLine1(addressLine1);
                resultMembershipDTO.setAddressLine2(addressLine2);
                resultMembershipDTO.setCityName(cityName);
                resultMembershipDTO.setCountry(country);
                resultMembershipDTO.setZipCode(zipcode);
                resultMembershipDTO.setState(state);
                resultMembershipDTO.setPhone(phone);
                resultMembershipDTO.setEmail(email);
                resultMembershipDTO.setIndustry(industry);
                resultMembershipDTO.setIsBusiness(isBusiness);
                resultMembershipDTO.setSectorId(sectorId);
                /*
                 * mapping channelAccess to Status as we don't have channelAccess field in MembershipDTO 
                 */
                String channelAccess = customerResponse.has("channelAccess") ? customerResponse.get("channelAccess").getAsString(): "";
                resultMembershipDTO.setStatus(channelAccess);

                if (phoneFromSearch.contains(phone) || phone.contains(phoneFromSearch)) {
            
                    resultList.add(resultMembershipDTO);
                }
                

            }
        }

        return resultList;
    }

}
