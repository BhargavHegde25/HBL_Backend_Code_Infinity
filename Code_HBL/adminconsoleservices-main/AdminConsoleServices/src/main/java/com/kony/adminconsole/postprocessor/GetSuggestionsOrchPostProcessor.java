package com.kony.adminconsole.postprocessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.utilities.EnvironmentParamRead;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetSuggestionsOrchPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		Result resultNew = new Result();
		Dataset dataset = new Dataset();
		dataset.setId("servicedefinition");
		resultNew.addDataset(dataset);
		
		dataset = new Dataset();
		dataset.setId("serviceType");
		resultNew.addDataset(dataset);
		
		dataset = new Dataset();
		dataset.setId("customers");
		resultNew.addDataset(dataset);
		
		try {
			
			int limit = EnvironmentParamRead.getSuggestionRecordLimit(request);
			if(limit == 0) {
				return resultNew;
			}
			
			Dataset dsSD = result.getDatasetById("servicedefinition");
			int dsSDSize = 0;
			if(null != dsSD) {
				dsSDSize = dsSD.getAllRecords().size();
			}
			
			Dataset dsGRP = result.getDatasetById("serviceType");
			int dsGRPSize = 0;
			if(null != dsGRP) {
				dsGRPSize = dsGRP.getAllRecords().size();
			}
			
			if(dsSDSize != 0 || dsGRPSize !=0) {
				Set<String> service = new HashSet<>();
				Set<String> serviceType = new HashSet<>();
				findServiceAndTypeAgainstRole( request , service, serviceType);
				if(service.size() != 0) {
					
					if(dsSDSize !=0) {
						dsSD = removefromDataSet(dsSD, service);
						dsSDSize = dsSD.getAllRecords().size();
					}
					if(dsGRPSize !=0) {
						dsGRP = removefromDataSet(dsGRP, serviceType);
						dsGRPSize = dsGRP.getAllRecords().size();
					}
				}
				
			}
			
			Dataset dsCustomer = result.getDatasetById("customers");
			int dsCustomerSize = 0;
			if(null != dsCustomer) {
				dsCustomerSize = dsCustomer.getAllRecords().size();
			}
			
			int currCount = 0;
			int i;
			for(i=0 ; i<limit; i++) {
				
				if(currCount == dsSDSize) {
					
					break;
				}
				resultNew.getDatasetById("servicedefinition").addRecord(dsSD.getRecord(i));
				currCount++;
			}
			limit = limit-i;
			currCount = 0;
			
			for(i=0 ; i<limit; i++) {
				
				if(currCount == dsGRPSize) {
					
					break;
				}
				resultNew.getDatasetById("serviceType").addRecord(dsGRP.getRecord(i));
				currCount++;
			}
			limit = limit-i;
			currCount = 0;
			
			for(i=0 ; i<limit; i++) {
				if(currCount == dsCustomerSize) {
					
					break;
				}
				resultNew.getDatasetById("customers").addRecord(dsCustomer.getRecord(i));
				currCount++;
			}
		
		}catch(Exception exp) {
			alert.prepareError("Exception occured in GetSuggestionsOrchPostProcessor : ", exp).log();
			resultNew.addParam("Exp trace", exp.toString());
		}
		return resultNew;
	}
	
	public void findServiceAndTypeAgainstRole(DataControllerRequest request , Set<String> service, Set<String> serviceType) {
		
		try {
			
			String roleIds = StringUtils.EMPTY;
			List<String> roleList = null;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
			if(null != userDetailsBeanInstance) {
				roleIds = userDetailsBeanInstance.getRoleId();
			}
			
			if(StringUtils.isNotBlank(roleIds)) {
				roleList = Arrays.asList(roleIds.split(","));
			}
			Map<String, String> postParametersMap = null;
			if((null != roleList) && (roleList.size() > 0)) {
				
				for(String role : roleList ) {
					
					postParametersMap = new HashMap<String, String>();
					postParametersMap.put(ODataQueryConstants.FILTER, "InternalRole_id eq '" + role + "'");

		            String readEndpointResponse = Executor.invokeService(
		                    ServiceURLEnum.INTERNAL_ROLE_TO_SERVICEDEFINITION_MAPPING_VIEW_READ, postParametersMap, null,
		                    request);
		            JSONObject readResponse = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
		            if (null != readResponse && readResponse.has("internal_role_to_servicedefinition_mapping_view") 
		            		&& readResponse.getJSONArray("internal_role_to_servicedefinition_mapping_view").length() > 0){
		            	
		            	JSONArray respJsonArray = readResponse.getJSONArray("internal_role_to_servicedefinition_mapping_view");
		            	
		            	Iterator iterator = respJsonArray.iterator();
		            	while (iterator.hasNext()){
		            		
		            		JSONObject item = (JSONObject) iterator.next();
		            		String serviceId = item.get("ServiceDefinition_id").toString();
		            		service.add(serviceId);
		            		String type = item.get("ServiceDefinition_Type_id").toString();
		            		serviceType.add(type);
		            	}	
		            }
		            		
				}
			}
			
		} catch (Exception exp) {
			alert.prepareError("Exception occured in GetSuggestionsOrchPostProcessor while processing service and type for Roles : ", exp).log();
			//result.addParam("Exp trace", exp.toString());
		}
	}
	
	public Dataset removefromDataSet(Dataset ds, Set<String> ids) {
		
		Dataset dsNew = new Dataset();
		List<Record> recordList = ds.getAllRecords();
		for(int i=0; i< recordList.size();i ++){
			
			String id = recordList.get(i).getParamValueByName("id");
			if( ids.contains(id)) {
				dsNew.addRecord(recordList.get(i));
			}
		}
		
		return dsNew;
	}

}
