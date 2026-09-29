package com.temenos.infinity.api.usermanagement.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.usermanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.resource.api.UserManagementResource;

/**
 * 
 * @author venkat
 * @version Java Service to update customer Details in Profile Settings in order
 *          management micro services
 * 
 */

public class UpdateCustomerDetailsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			UserManagementResource updateCustomerDetailsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(UserManagementResource.class);
			CustomerDetailsDTO customerDetailsDTO = constructPayLoad(request);

			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
			headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

			Result result = updateCustomerDetailsResource.updateCustomerDetails(customerDetailsDTO, request, headerMap);
			return result;
		} catch (Exception e) {
			alert.prepareError("Unable to create order : " + e).log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
	}

	public static CustomerDetailsDTO constructPayLoad(DataControllerRequest request) {

		CustomerDetailsDTO customerDetailsDTO = new CustomerDetailsDTO();

		if (request.containsKeyInRequest("phoneNumbers")) {

			JsonArray phoneNumbers = new JsonParser().parse(request.getParameter("phoneNumbers")).getAsJsonArray();
			if (phoneNumbers.toString().contains("id")) {
				String id = phoneNumbers.get(0).getAsJsonObject().get("id").getAsString() != null
						? phoneNumbers.get(0).getAsJsonObject().get("id").getAsString()
						: "";
				customerDetailsDTO.setId(id);
			}
			if (phoneNumbers.toString().contains("isTypeBusiness")) {
				String isTypeBusiness = phoneNumbers.get(0).getAsJsonObject().get("isTypeBusiness")
						.getAsString() != null
								? phoneNumbers.get(0).getAsJsonObject().get("isTypeBusiness").getAsString()
								: "";
				customerDetailsDTO.setIsTypeBusiness(isTypeBusiness);
			}
			String Extension = phoneNumbers.get(0).getAsJsonObject().get("Extension").getAsString() != null
					? phoneNumbers.get(0).getAsJsonObject().get("Extension").getAsString()
					: "";
			String isPrimary = phoneNumbers.get(0).getAsJsonObject().get("isPrimary").getAsString() != null
					? phoneNumbers.get(0).getAsJsonObject().get("isPrimary").getAsString()
					: "";
			if (phoneNumbers.toString().contains("isAlertsRequired")) {
			String isAlertsRequired = phoneNumbers.get(0).getAsJsonObject().get("isAlertsRequired")
					.getAsString() != null ? phoneNumbers.get(0).getAsJsonObject().get("isAlertsRequired").getAsString()
							: "";
					customerDetailsDTO.setIsAlertsRequired(isAlertsRequired);
			}
			String phoneNumber = phoneNumbers.get(0).getAsJsonObject().get("phoneNumber").getAsString() != null
					? phoneNumbers.get(0).getAsJsonObject().get("phoneNumber").getAsString()
					: "";
			String phoneCountryCode = phoneNumbers.get(0).getAsJsonObject().get("phoneCountryCode")
					.getAsString() != null ? phoneNumbers.get(0).getAsJsonObject().get("phoneCountryCode").getAsString()
							: "";

			String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
			String modifiedByName = request.getParameter("modifiedByName") != null
					? request.getParameter("modifiedByName")
					: "";

			customerDetailsDTO.setExtension(Extension);
			customerDetailsDTO.setIsPrimary(isPrimary);			
			customerDetailsDTO.setPhoneNumber(phoneNumber);
			customerDetailsDTO.setPhoneCountryCode(phoneCountryCode);

			customerDetailsDTO.setUserName(userName);
			customerDetailsDTO.setModifiedByName(modifiedByName);

			customerDetailsDTO.setDetailToBeUpdated("phoneNumbers");

			if (StringUtils.isNotBlank(customerDetailsDTO.getId())) {
				customerDetailsDTO.setOperation("Update");
			} else {
				customerDetailsDTO.setOperation("Create");
			}

		}

		else if (request.containsKeyInRequest("EmailIds")) {

			JsonArray emailIds = new JsonParser().parse(request.getParameter("EmailIds")).getAsJsonArray();

			if (emailIds.toString().contains("id")) {
				String id = emailIds.get(0).getAsJsonObject().get("id").getAsString() != null
						? emailIds.get(0).getAsJsonObject().get("id").getAsString()
						: "";
				customerDetailsDTO.setId(id);
			}
			if (emailIds.toString().contains("isTypeBusiness")) {
				String isTypeBusiness = emailIds.get(0).getAsJsonObject().get("isTypeBusiness").getAsString() != null
						? emailIds.get(0).getAsJsonObject().get("isTypeBusiness").getAsString()
						: "";
				customerDetailsDTO.setIsTypeBusiness(isTypeBusiness);
			}
			if (emailIds.toString().contains("Extension")) {
				String Extension = emailIds.get(0).getAsJsonObject().get("Extension").getAsString() != null
						? emailIds.get(0).getAsJsonObject().get("Extension").getAsString()
						: "";
				customerDetailsDTO.setExtension(Extension);
			}

			String isPrimary = emailIds.get(0).getAsJsonObject().get("isPrimary").getAsString() != null
					? emailIds.get(0).getAsJsonObject().get("isPrimary").getAsString()
					: "";
			if (emailIds.toString().contains("isAlertsRequired")) {		
			String isAlertsRequired = emailIds.get(0).getAsJsonObject().get("isAlertsRequired").getAsString() != null
					? emailIds.get(0).getAsJsonObject().get("isAlertsRequired").getAsString()
					: "";
					customerDetailsDTO.setIsAlertsRequired(isAlertsRequired);
			}
			String value = emailIds.get(0).getAsJsonObject().get("value").getAsString() != null
					? emailIds.get(0).getAsJsonObject().get("value").getAsString()
					: "";

			String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
			String modifiedByName = request.getParameter("modifiedByName") != null
					? request.getParameter("modifiedByName")
					: "";

			customerDetailsDTO.setIsPrimary(isPrimary);			
			customerDetailsDTO.setValue(value);
			customerDetailsDTO.setUserName(userName);
			customerDetailsDTO.setModifiedByName(modifiedByName);

			customerDetailsDTO.setDetailToBeUpdated("EmailIds");

			if (StringUtils.isNotBlank(customerDetailsDTO.getId())) {
				customerDetailsDTO.setOperation("Update");
			} else {
				customerDetailsDTO.setOperation("Create");
			}

		}

		else if (request.containsKeyInRequest("Addresses")) {

			JsonArray addresses = new JsonParser().parse(request.getParameter("Addresses")).getAsJsonArray();

			if (addresses.toString().contains("Addr_id")) {
				String Addr_id = addresses.get(0).getAsJsonObject().get("Addr_id").getAsString() != null
						? addresses.get(0).getAsJsonObject().get("Addr_id").getAsString()
						: "";
				customerDetailsDTO.setAddr_id(Addr_id);
			}

			String Addr_type = addresses.get(0).getAsJsonObject().get("Addr_type").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("Addr_type").getAsString()
					: "";
			String isPrimary = addresses.get(0).getAsJsonObject().get("isPrimary").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("isPrimary").getAsString()
					: "";
			String addrLine1 = addresses.get(0).getAsJsonObject().get("addrLine1").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("addrLine1").getAsString()
					: "";
			String addrLine2 = addresses.get(0).getAsJsonObject().get("addrLine2").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("addrLine2").getAsString()
					: "";
			String City_id = addresses.get(0).getAsJsonObject().get("City_id").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("City_id").getAsString()
					: "";
			String ZipCode = addresses.get(0).getAsJsonObject().get("ZipCode").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("ZipCode").getAsString()
					: "";
					
			if (addresses.toString().contains("Region_id")) {
				String Region_id = addresses.get(0).getAsJsonObject().get("Region_id").getAsString() != null
						? addresses.get(0).getAsJsonObject().get("Region_id").getAsString()
						: "";
				customerDetailsDTO.setRegion_id(Region_id);
			}
			
			String countryCode = addresses.get(0).getAsJsonObject().get("countryCode").getAsString() != null
					? addresses.get(0).getAsJsonObject().get("countryCode").getAsString()
					: "";

			String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
			String modifiedByName = request.getParameter("modifiedByName") != null
					? request.getParameter("modifiedByName")
					: "";

			customerDetailsDTO.setAddr_type(Addr_type);
			customerDetailsDTO.setIsPrimary(isPrimary);
			customerDetailsDTO.setAddrLine1(addrLine1);
			customerDetailsDTO.setAddrLine2(addrLine2);
			customerDetailsDTO.setCity_id(City_id);
			customerDetailsDTO.setZipCode(ZipCode);			
			customerDetailsDTO.setCountryCode(countryCode);
			customerDetailsDTO.setUserName(userName);
			customerDetailsDTO.setModifiedByName(modifiedByName);

			customerDetailsDTO.setDetailToBeUpdated("Addresses");

			if (StringUtils.isNotBlank(customerDetailsDTO.getAddr_id())) {
				customerDetailsDTO.setOperation("UpdateAddress");
			} else {
				customerDetailsDTO.setOperation("CreateAddress");
			}

		}

		else if (request.containsKeyInRequest("deleteCommunicationID")) {
			String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
			String deleteCommunicationID = request.getParameter("deleteCommunicationID") != null
					? request.getParameter("deleteCommunicationID")
					: "";
			if (request.containsKeyInRequest("communicationType")) {
				String communicationType = StringUtils.isNotBlank(request.getParameter("communicationType"))
						? request.getParameter("communicationType")
						: "";
				if (communicationType.contentEquals("phoneNumbers")) {
					customerDetailsDTO.setDetailToBeUpdated("phoneNumbers");
				} else if (communicationType.contentEquals("EmailIds")) {
					customerDetailsDTO.setDetailToBeUpdated("EmailIds");
				}
			}					

			customerDetailsDTO.setUserName(userName);
			customerDetailsDTO.setDeleteCommunicationID(deleteCommunicationID);
			customerDetailsDTO.setOperation("Delete");			

		}

		else if (request.containsKeyInRequest("deleteAddressID")) {
			String userName = request.getParameter("userName") != null ? request.getParameter("userName") : "";
			String deleteAddressID = request.getParameter("deleteAddressID") != null
					? request.getParameter("deleteAddressID")
					: "";

			customerDetailsDTO.setUserName(userName);
			customerDetailsDTO.setDeleteAddressID(deleteAddressID);
			customerDetailsDTO.setOperation("DeleteAddress");

		}
		customerDetailsDTO.setLegalEntityId(LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
		return customerDetailsDTO;

	}
}
