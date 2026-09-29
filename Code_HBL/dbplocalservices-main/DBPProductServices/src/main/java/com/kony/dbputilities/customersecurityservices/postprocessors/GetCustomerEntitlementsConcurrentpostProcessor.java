package com.kony.dbputilities.customersecurityservices.postprocessors;

import java.util.HashMap;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerEntitlementsConcurrentpostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Log4j2Configurator.getInstance();

		Result returnResult = new Result();

		HashMap<String, Record> citiesList = new HashMap<>();
		HashMap<String, Record> regionsList = new HashMap<>();
		HashMap<String, Record> countriesList = new HashMap<>();

		Dataset services = result.getDatasetById("services");

		returnResult.addDataset(services);

		Dataset dataset = result.getDatasetById("city");

		if (dataset != null) {
			List<Record> cities = dataset.getAllRecords();
			for (Record record : cities) {
				citiesList.put(record.getParamValueByName("id"), record);
			}
		}

		dataset = result.getDatasetById("region");

		if (dataset != null) {
			List<Record> cities = dataset.getAllRecords();
			for (Record record : cities) {
				regionsList.put(record.getParamValueByName("id"), record);
			}
		}

		dataset = result.getDatasetById("country");

		if (dataset != null) {
			List<Record> cities = dataset.getAllRecords();
			for (Record record : cities) {
				countriesList.put(record.getParamValueByName("id"), record);
			}
		}

		Dataset addresses = result.getDatasetById("Addresses");

		if (addresses != null) {

			for (Record record : addresses.getAllRecords()) {
				if (StringUtils.isBlank(record.getParamValueByName("cityName"))
						&& StringUtils.isNotBlank(record.getParamValueByName("City_id"))) {
					Record city = citiesList.get(record.getParamValueByName("City_id"));
					record.addParam("cityName", city.getParamValueByName("Name"));
				}

				if (StringUtils.isBlank(record.getParamValueByName("state"))
						&& StringUtils.isNotBlank(record.getParamValueByName("Region_id"))) {
					Record region = regionsList.get(record.getParamValueByName("Region_id"));
					record.addParam("state", region.getParamValueByName("Name"));
					record.addParam("RegionCode", region.getParamValueByName("Code"));
				}

				if (StringUtils.isBlank(record.getParamValueByName("country"))
						&& StringUtils.isNotBlank(record.getParamValueByName("Country_id"))) {
					Record country = countriesList.get(record.getParamValueByName("Region_id"));
					record.addParam("CountryName", country.getParamValueByName("Name"));
					record.addParam("CountryCode", country.getParamValueByName("Code"));
				}

			}

		}

		returnResult.addDataset(addresses);

		Dataset emailIds = result.getDatasetById("EmailIds");

		returnResult.addDataset(emailIds);

		Dataset contactNumbers = result.getDatasetById("ContactNumbers");

		returnResult.addDataset(contactNumbers);

		Dataset securityQuestions = result.getDatasetById("customersecurityquestions");

		boolean status = false;

		if (securityQuestions != null && securityQuestions.getAllRecords().size() > 0) {
			status = true;
		}

		returnResult.addParam("isSecurityQuestionConfigured", status + "", MWConstants.STRING);

		return returnResult;
	}

}