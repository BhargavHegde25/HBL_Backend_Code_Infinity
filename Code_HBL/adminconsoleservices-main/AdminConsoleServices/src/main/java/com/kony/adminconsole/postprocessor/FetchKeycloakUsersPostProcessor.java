package com.kony.adminconsole.postprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Postprocessor for the key cloak users get operation. <br>
 * This adds computed params and removes unnecessary ones
 * 
 * @author Sri kavya Pitchika
 *
 */
public class FetchKeycloakUsersPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		//Remove this from output to avoid token leakage
		if(result.hasParamByName("access_token"))
          	 result.removeParamByName("access_token");
		
		List<Dataset> dsList = result.getAllDatasets();
		Dataset ds = dsList.get(0);
		List<Record> dsRecsList = ds.getAllRecords();
		for (Record rec : dsRecsList) {
			// To avoid null pointers in existing services that make use of users
			if(rec.getParam("LastName")==null)
				rec.addParam("LastName","");
			if(rec.getParam("FirstName")==null)
				rec.addParam("FirstName","");
			if(rec.getParam("Email")==null)
				rec.addParam("Email","");
			String lastName = rec.getParamValueByName("LastName");
			String firstName = rec.getParamValueByName("FirstName");
			String enabled = rec.getParamValueByName("enabled");
			String name = "";
			if (StringUtils.isNotBlank(firstName))
				name = firstName + " ";
			if (StringUtils.isNotBlank(lastName))
				name = name + lastName;
			if (StringUtils.isNotBlank(enabled)) {
				if(enabled.equals("true"))
					rec.addParam("Status_id","SID_ACTIVE");
				else
					rec.addParam("Status_id","SID_INACTIVE");
				rec.removeParamByName("enabled");
			}				
			rec.addParam("Name", name);
		}
		return result;
	}

}
