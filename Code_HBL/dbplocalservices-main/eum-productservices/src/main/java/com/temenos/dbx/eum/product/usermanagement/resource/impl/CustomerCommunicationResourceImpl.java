package com.temenos.dbx.eum.product.usermanagement.resource.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.util.LegalEntityUtil;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CommunicationBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerCommunicationResource;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerCommunicationResourceImpl implements CustomerCommunicationResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Result getCustomerCommunication(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String customerId = inputParams.get("Customer_id");
		if (StringUtils.isNotBlank(customerId)) {

			CommunicationBusinessDelegate customerCommunicationBD = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CommunicationBusinessDelegate.class);
			LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
			CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
			communicationDTO.setCustomer_id(customerId);
			DBXResult dbxResult  =
					customerCommunicationBD.get(communicationDTO, dcRequest.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				List<CustomerCommunicationDTO> dtoList = (List<CustomerCommunicationDTO>) dbxResult.getResponse();
				String communicationString = new JsonArray().toString();
				try {
					communicationString = JSONUtils.stringifyCollectionWithTypeInfo(dtoList, CustomerCommunicationDTO.class);
				} catch (IOException e) {
					alert.prepareError(e.toString()).log();
				}
				JSONArray communicationArray = new JSONArray(communicationString);
				JSONObject communicationJson = new JSONObject();
				communicationJson.put(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, communicationArray);
				Result communication = ConvertJsonToResult.convert(communicationJson.toString());
				result = processCommuniction(communication);

			}

		}
		return result;
	}

	private Result processCommuniction(Result communication) {
		Result returnResult = new Result();
		if (HelperMethods.hasRecords(communication)) {

			List<Record> list = communication.getAllDatasets().get(0).getAllRecords();
			Dataset phoneRecords = new Dataset();
			Dataset emailRecords = new Dataset();

			phoneRecords.setId("ContactNumbers");
			emailRecords.setId("EmailIds");

			for (Record record : list) {
				if (record.getNameOfAllParams().contains(DBPUtilitiesConstants.TYPE_ID)
						&& record.getParamValueByName(DBPUtilitiesConstants.TYPE_ID)
						.equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
					emailRecords.addRecord(record);
				} else if (record.getNameOfAllParams().contains(DBPUtilitiesConstants.TYPE_ID)
						&& record.getParamValueByName(DBPUtilitiesConstants.TYPE_ID)
						.equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
					processMobileNumber(record);
					phoneRecords.addRecord(record);
				}
			}

			returnResult.addDataset(phoneRecords);	
			returnResult.addDataset(emailRecords);
		}

		return returnResult;
	}

	private void processMobileNumber(Record record) {
		String phone = record.getParamValueByName("Value");
		if (StringUtils.isNotBlank(phone)) {
			String[] phoneArray = StringUtils.split(phone, "-");
			if (phoneArray != null && phoneArray.length >= 2) {
				String countryCode = phoneArray[0];
				record.addParam("phoneCountryCode", countryCode, DBPUtilitiesConstants.STRING_TYPE);
				String phoneNumber = phoneArray[1];
				record.addParam("phoneNumber", phoneNumber, DBPUtilitiesConstants.STRING_TYPE);
				record.addParam("Value", phoneNumber, DBPUtilitiesConstants.STRING_TYPE);
				if (phoneArray.length == 3) {
					String extension = phoneArray[2];
					record.addParam("phoneExtension", extension, DBPUtilitiesConstants.STRING_TYPE);
				}
			} else {
				record.addParam("phoneNumber", phone, DBPUtilitiesConstants.STRING_TYPE);
			}
		}
	}

	@Override
	public Object getCustomerCommunicationRecords(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = inputParams.get("Customer_id");
		String legalEntityId = StringUtils.isNotBlank(inputParams.get("legalEntityId")) ? inputParams.get("legalEntityId")
				: dcRequest.getParameter("legalEntityId");
		if(StringUtils.isBlank(legalEntityId)) {
			legalEntityId = LegalEntityUtil.getCurrentLegalEntityIdFromCache(dcRequest);
		}
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(id);
		customerCommunicationDTO.setCompanyLegalUnit(legalEntityId);

		CommunicationBusinessDelegate impl = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CommunicationBusinessDelegate.class);
		Map<String, Object> headerMap = dcRequest.getHeaderMap();
		headerMap.put("companyid", legalEntityId);
		DBXResult  dbxResult = impl.get(customerCommunicationDTO, headerMap);

		if (dbxResult.getResponse() != null) {
			List<CustomerCommunicationDTO> dtoList = (List<CustomerCommunicationDTO>) dbxResult.getResponse();
			Dataset phoneRecords = new Dataset();
            Dataset emailRecords = new Dataset();
            phoneRecords.setId("ContactNumbers");
            emailRecords.setId("EmailIds");
            
            for (CustomerCommunicationDTO communication : dtoList) {
            	if(StringUtils.isNotBlank(communication.getType_id()) && communication.getType_id().equals(HelperMethods.getCommunicationTypes().get("Email"))) {
            		Record record = DTOUtils.getRecordFromDTO(communication, true);
            		emailRecords.addRecord(record);
            	}else if (StringUtils.isNotBlank(communication.getType_id()) && communication.getType_id().equals(HelperMethods.getCommunicationTypes().get("Phone"))) {
            		Record record = DTOUtils.getRecordFromDTO(communication, true);
            		processMobileNumber(record);
                    phoneRecords.addRecord(record);
            	}
            }
            result.addDataset(phoneRecords);
            result.addDataset(emailRecords);
		}
		return result;
	}
	
	
	@Override
	public Object getPrimaryCommunicationForLogin(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = inputParams.get("id");
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(id);

		CommunicationBusinessDelegate impl = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CommunicationBusinessDelegate.class);

		DBXResult  dbxResult = impl.getPrimaryCommunicationForLogin(customerCommunicationDTO, dcRequest.getHeaderMap());

		if (dbxResult.getResponse() != null) {
			JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
			
			result = JSONToResult.convert(jsonObject.toString());
			
		}
		return result;
	}

	@Override
	public Object getPrimaryCommunication(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = inputParams.get("id");
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(id);
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		CommunicationBusinessDelegate impl = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CommunicationBusinessDelegate.class);

		DBXResult  dbxResult = impl.getPrimaryCommunication(customerCommunicationDTO, dcRequest.getHeaderMap());

		if (dbxResult.getResponse() != null) {
			JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
			
			result = JSONToResult.convert(jsonObject.toString());
			
		}
		return result;
	}

}
