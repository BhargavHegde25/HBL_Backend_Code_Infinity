package com.infinity.dbx.temenos.accounts;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class getMortgageFacilitiesPostProcessor extends BasePostProcessor {

	@Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		StringBuilder address = new StringBuilder();
		 if(StringUtils.isNotBlank((result.getParamValueByName("propertyAddress")))) {
			 address.append(result.getParamByName("propertyAddress").getValue());
		 }
		 if(StringUtils.isNotBlank(result.getParamValueByName("propertyCity"))) {
			 address.append(", ");
			 address.append(result.getParamByName("propertyCity").getValue());
		 }
		 
		 if(StringUtils.isNotBlank(result.getParamValueByName("propertyDistrict"))) {
			 address.append(", ");
			 address.append(result.getParamByName("propertyDistrict").getValue());
		 }
		 if(StringUtils.isNotBlank(result.getParamValueByName("propertyCountry"))) {
			 address.append(", ");
			 address.append(result.getParamByName("propertyCountry").getValue());
		 }
		 if(StringUtils.isNotBlank(result.getParamValueByName("propertyZipCode"))) {
			 address.append(", ");
			 address.append(result.getParamByName("propertyZipCode").getValue());
		 }
		 if(StringUtils.isNotBlank(address)) {
		 result.addParam(new Param("propertyAddress", address.toString()));
		 }
		 
		 if(StringUtils.isNotBlank((result.getParamValueByName("commitmentTerm")))) {
			 String commitmentTerm = result.getParamByName("commitmentTerm").getValue();
			 if(StringUtils.contains(commitmentTerm, "Y")) {
				 commitmentTerm = commitmentTerm.replace("Y", " years");
			 }
			 else if(StringUtils.contains(commitmentTerm, "M")) {
				 commitmentTerm = commitmentTerm.replace("M", " months");
			 }
			 else if(StringUtils.contains(commitmentTerm, "W")) {
				 commitmentTerm = commitmentTerm.replace("W", " weeks");
			 }
			 else if(StringUtils.contains(commitmentTerm, "D")) {
				 commitmentTerm = commitmentTerm.replace("D", " days");
			 }
			 result.addParam(new Param("commitmentTerm", commitmentTerm));
		 }
			return result;
	}
}
