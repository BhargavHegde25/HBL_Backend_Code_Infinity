
package com.kony.adminconsole.service.alertmanagement;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kony.adminconsole.service.alertmanagement.pojo.AutoSubscribedAlertView;
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




public class SubscribeDefaultAlerts implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private boolean isT24Exception = false;
	private static String schemaname = null;
 
  public Object invoke(String methodID, Object[] maps, DataControllerRequest request,
      DataControllerResponse response) throws Exception {
	  
	  try
	  {
		  if(diagnostic.isDebugEnabled())
		  {
			  diagnostic.prepareDebug("Inside Invoke Method of customer alert config Service").log();
		  }
		  
		  if(schemaname == null)
		  {
				schemaname = getValue("DBX_SCHEMA_NAME");
		  }
		  
		  isT24Exception = false;
		  String infinity_customerid = null;
		  String auth = null;
		  
		  Map<String, String> inputParams = null;
		  
		  if (maps != null) {
			  if(diagnostic.isDebugEnabled())
			  {
				diagnostic.prepareDebug("inputParams : " + maps).log();
			  }

				if (maps.length > 1) {

					inputParams = (Map<String, String>) maps[1];

				}
			}
		  
		  if (inputParams != null) {
			  
			  if(diagnostic.isDebugEnabled())
			  {
				diagnostic.prepareDebug("inputParams : " + inputParams).log();
			  }

			  infinity_customerid = inputParams.get("userId");
			  auth = inputParams.get("X-Kony-Authorization");
			}
		  
		  if(diagnostic.isDebugEnabled())
		  {
			  diagnostic.prepareDebug("infinity_customerid : " + infinity_customerid).log();
			  diagnostic.prepareDebug("auth : " + auth).log();
		  }
		  
		  if (infinity_customerid == null || (infinity_customerid != null && infinity_customerid.length() == 0)) {
			  return returnResult(false, "Missing input parameters.","16011");
			}
		  
		  identifyAndExecuteChannelConfig(request, infinity_customerid,auth);
		  
	  }
	  catch(Exception e)
	  {
		  alert.prepareError("Error occurred: ", e).log();
		  alert.prepareError(e.toString()).log();
		  return returnResult(false, e.getMessage(),"16012");
	  }
	  
	  return returnResult(true, "Success");
  
  }

private void identifyAndExecuteChannelConfig(DataControllerRequest request,String infinity_userid, String auth) throws MiddlewareException, IOException, JsonParseException, JsonMappingException, Exception {
		
	    
		List<Object> custdata = getInfinityUseridDetails(infinity_userid,request,auth);
	  
	    Map<String,String> corecustomer_type_map = (Map<String,String>) custdata.get(0);
	    Map<String,String> corecustomer_isprimary_map = (Map<String,String>) custdata.get(1);
	    Map<String,Map<String,String>> corecust_account_accounttype_map = ( Map<String,Map<String,String>>) custdata.get(2);
	    
	    
	    Map<String,ArrayList<String>> validalertsubtypecustomertype_map = getValidalertsubtypecustomertypeCombinations(request);
	    if(diagnostic.isDebugEnabled())
		{
	    	diagnostic.prepareDebug("validalertsubtypecustomertype_map : " + validalertsubtypecustomertype_map).log();
	    }
	    String channelpref  = getCustomerviewalertconfigurationdata(request);
	    if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("channelpref : " + channelpref).log();
		}
	    
		Map<String, List<String>> channel_map = getChannelConfig(request,channelpref);
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("channel_map : " + channel_map).log();
		}
	    
	    ArrayList<AutoSubscribedAlertView> allAlertInfo_lst = getautosubscribealerts(request,channelpref);
	    if(diagnostic.isDebugEnabled())
		{
	    	diagnostic.prepareDebug("allAlertInfo_lst : " + allAlertInfo_lst).log();
		}
	    String extSystemCode = identifyExtSystem(allAlertInfo_lst);
	    
	    HashMap<String,ArrayList<AutoSubscribedAlertView>> corecustomer_alertsubtype_map = getCoreCustomerAlertSubTypeMap(corecustomer_type_map,validalertsubtypecustomertype_map,allAlertInfo_lst);
	    if(diagnostic.isDebugEnabled())
		{
	    	diagnostic.prepareDebug("corecustomer_alertsubtype_map : " + corecustomer_alertsubtype_map).log();
		}
	    Map<String,List<String>> valid_accounttype_alertsubtypes  = getValidAccountTypeAlertSubTypeCombinations(request);
	    if(diagnostic.isDebugEnabled())
		{
	    	diagnostic.prepareDebug("valid_accounttype_alertsubtypes : " + valid_accounttype_alertsubtypes).log();
		}
	    
	    HashMap<String, HashMap<String,AutoSubscribedAlertView>> corecustomer_nonaccountlevel_alertsubtype_map = getValidNonAccountLevelALertSubTypes(corecustomer_alertsubtype_map);
	    if(diagnostic.isDebugEnabled())
		{
	    	diagnostic.prepareDebug("corecustomer_nonaccountlevel_alertsubtype_map : " + corecustomer_nonaccountlevel_alertsubtype_map).log();
		}
	    
	    HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_accountlevel_alertsubtype_map = getValidAccountLevelALertSubTypes(corecustomer_alertsubtype_map,corecust_account_accounttype_map,valid_accounttype_alertsubtypes);
	    if(diagnostic.isDebugEnabled())
		{
	    	diagnostic.prepareDebug("corecustomer_accountlevel_alertsubtype_map : " + corecustomer_accountlevel_alertsubtype_map).log();
		}
		
		HashMap<String, ArrayList<String>> list_switch = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> list_channel = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> list_entit = new HashMap<String, ArrayList<String>>();
		String primaryId = null;
		
		primaryId = (String) custdata.get(3);
		
		if(diagnostic.isDebugEnabled())
		{
    		diagnostic.prepareDebug("primaryId : " + primaryId).log();
		}
		
	    for(String corecustomerid : corecustomer_type_map.keySet())
	    {
	    	if(diagnostic.isDebugEnabled())
			{
	    		diagnostic.prepareDebug("corecustomerid : " + corecustomerid).log();
			}
	    	HashMap<String,JSONObject> backend_alerts = null;
	    	
	    	if(corecustomer_isprimary_map.get(corecustomerid).equalsIgnoreCase("true") && extSystemCode.equalsIgnoreCase("T24"))
	    	{
	    		backend_alerts =  fetchBackendAlerts(corecustomerid,request);
	    		if(diagnostic.isDebugEnabled())
	    		{
	    			diagnostic.prepareDebug("backend_alerts : " + backend_alerts).log();
	    		}
	    	}
	    	
	    	configureAlerts(infinity_userid,corecustomerid,corecustomer_nonaccountlevel_alertsubtype_map,corecustomer_accountlevel_alertsubtype_map,backend_alerts,channelpref,channel_map,request,corecustomer_isprimary_map,corecust_account_accounttype_map,list_switch,list_channel,list_entit,primaryId,extSystemCode);
	    }
	    
}

private String identifyExtSystem(ArrayList<AutoSubscribedAlertView> allAlertInfo_lst) {
	
	String  extSystem = null;
	for(AutoSubscribedAlertView autoSubscribedAlertView : allAlertInfo_lst)
	  {
		  if(autoSubscribedAlertView.getExternalSystem() == 1)
		  {
			  extSystem = "T24";
			  break;
		  }
	  }
	if(extSystem == null)
	{
		extSystem = "INTERNAL";
	}
	
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("extSystem : " + extSystem).log();
	}
	
	return extSystem;
}

private void configureAlerts(String infinityuser_id, String corecustomerid, HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_nonaccountlevel_alertsubtype_map,
		HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_accountlevel_alertsubtype_map,
		HashMap<String, JSONObject> backend_alerts, String channelpref, Map<String, List<String>> channel_map, DataControllerRequest request, Map<String, String> corecustomer_isprimary_map, Map<String, Map<String, String>> corecust_account_accounttype_map,
		HashMap<String, ArrayList<String>> list_switch,HashMap<String, ArrayList<String>> list_channel,HashMap<String, ArrayList<String>> list_entit,String primaryId,String extSystemCode) throws Exception {
	
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("corecustomerid : "+ corecustomerid).log();
	}
	
	if(corecustomer_isprimary_map.get(corecustomerid).equalsIgnoreCase("true"))
	{
		executePrimaryCustomerConfig(infinityuser_id, corecustomerid, corecustomer_nonaccountlevel_alertsubtype_map,
				corecustomer_accountlevel_alertsubtype_map, backend_alerts, channelpref, channel_map, request,
				corecust_account_accounttype_map, list_switch,list_channel,list_entit,primaryId);
	}
	else
	{
		executeNonPrimaryCustomerConfig(infinityuser_id, corecustomerid, corecustomer_nonaccountlevel_alertsubtype_map,
				corecustomer_accountlevel_alertsubtype_map, channelpref, channel_map, request,
				corecust_account_accounttype_map, list_switch,list_channel,list_entit,primaryId);
	}
	
	if(extSystemCode.equalsIgnoreCase("T24"))
	{
		configureAdditionalT24AlertsinDBX(infinityuser_id, corecustomerid, corecustomer_accountlevel_alertsubtype_map,
				backend_alerts, channelpref, channel_map, request, corecust_account_accounttype_map, list_switch,list_channel,list_entit,primaryId);
	}
	
	
	
}

private void configureAdditionalT24AlertsinDBX(String infinityuser_id, String corecustomerid,
		HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_accountlevel_alertsubtype_map,
		HashMap<String, JSONObject> backend_alerts, String channelpref, Map<String, List<String>> channel_map,
		DataControllerRequest request, Map<String, Map<String, String>> corecust_account_accounttype_map,
		HashMap<String, ArrayList<String>> list_switch,HashMap<String, ArrayList<String>> list_channel,HashMap<String, ArrayList<String>> list_entit,String primaryId) throws Exception {
	
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("Remaining Alerts : " + backend_alerts).log();
	}
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("valid Alerts : " + corecustomer_accountlevel_alertsubtype_map).log();
	}
	
	if(backend_alerts != null)
	{
		for(String key : backend_alerts.keySet())
		{
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("key : " + key).log();
			}
			if(key.contains(","))
			{
				if(corecustomer_accountlevel_alertsubtype_map.containsKey(corecustomerid))
				{
					if(corecustomer_accountlevel_alertsubtype_map.get(corecustomerid).containsKey(key))	
					{
						String acno = key.split(",")[0];
						String alertsubtype = key.split(",")[1];
						AutoSubscribedAlertView allAlertInfo = corecustomer_accountlevel_alertsubtype_map.get(corecustomerid).get(key);
						if(diagnostic.isDebugEnabled())
						{
							diagnostic.prepareDebug("allAlertInfo : " + allAlertInfo).log();
						}

						String value = null;
						if(backend_alerts.get(key).getJSONArray("fields") != null && backend_alerts.get(key).getJSONArray("fields").length() > 0)
						{
							value = backend_alerts.get(key).getJSONArray("fields").getJSONObject(0).getString("value");
						}
						String alertRequestId = backend_alerts.get(key).getString("alertRequestId");
						
						String status = updateT24(infinityuser_id,corecustomerid,acno,alertsubtype,request,alertRequestId,value,primaryId);
						
						if(status!= null && status.equalsIgnoreCase("success"))
						{
							populateDbxCustomerAlertEntitlement(infinityuser_id, acno, allAlertInfo,request,value,alertRequestId,"*",list_entit);
							populateCustomerAlertChannel(infinityuser_id, acno, allAlertInfo, channel_map , channelpref,request,"*",list_channel);
							populateCustomeralertswitch(infinityuser_id, acno, allAlertInfo,request,"*",list_switch);
						}
						else
						{
							if(diagnostic.isDebugEnabled())
							{
								diagnostic.prepareDebug("eror in updating transact").log();
							}
						}
					
					}
				}

			}
		}
	}
}

private void executeNonPrimaryCustomerConfig(String infinityuser_id, String corecustomerid,
		HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_nonaccountlevel_alertsubtype_map,
		HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_accountlevel_alertsubtype_map, String channelpref,
		Map<String, List<String>> channel_map, DataControllerRequest request,
		Map<String, Map<String, String>> corecust_account_accounttype_map, HashMap<String, ArrayList<String>> list_switch,HashMap<String, ArrayList<String>> list_channel,HashMap<String, ArrayList<String>> list_entit,String primaryId)
		throws Exception {
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug(" Not a Primary Core Customer ").log();
	}
	
	for(String key : corecustomer_accountlevel_alertsubtype_map.get(corecustomerid).keySet())
	{
		String acno = key.split(",")[0];
		AutoSubscribedAlertView allAlertInfo = corecustomer_accountlevel_alertsubtype_map.get(corecustomerid).get(key);
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("allAlertInfo : "+ allAlertInfo).log();
		}
		
		if(allAlertInfo.getIsGlobal() == 0 && allAlertInfo.isAutoSubscribeEnabled() && allAlertInfo.getExternalSystem() == 1)
		{
			if(!isT24Exception)
			{
				if(diagnostic.isDebugEnabled())
				{
					diagnostic.prepareDebug("creating both in dbxdb and transact").log();
				}
					String alertRequestId = populateT24(corecustomerid,allAlertInfo,acno,infinityuser_id,request,primaryId);
					
					if(alertRequestId != null)
					{
						 populateDbxCustomerAlertEntitlement(infinityuser_id, acno, allAlertInfo,request,allAlertInfo.getValue1(),alertRequestId,"*",list_entit);
						 populateCustomerAlertChannel(infinityuser_id, acno, allAlertInfo, channel_map , channelpref,request,"*",list_channel);
						 populateCustomeralertswitch(infinityuser_id, acno, allAlertInfo,request,"*",list_switch);
					}
					else
					{
						if(diagnostic.isDebugEnabled())
						{
							diagnostic.prepareDebug("error in creating in transact").log();
						}
					}	
				
			}
			else
			{
				alert.prepareError("skipping t24").log();
			}
		}
		else if(allAlertInfo.getIsGlobal() == 0 && allAlertInfo.isAutoSubscribeEnabled() )
		{
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("Configuring only in dbxdb").log();
			}
			
		    populateDbxCustomerAlertEntitlement(infinityuser_id, acno, allAlertInfo,request,allAlertInfo.getValue1(),null,"*",list_entit);
		    populateCustomerAlertChannel(infinityuser_id, acno, allAlertInfo, channel_map , channelpref,request,"*",list_channel);
		    populateCustomeralertswitch(infinityuser_id, acno, allAlertInfo,request,"*",list_switch);
		}
		else
		{
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("No Need to configure").log();
			}
		}
	}
	
	for(String key : corecustomer_nonaccountlevel_alertsubtype_map.get(corecustomerid).keySet())
	{
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("Configuring only in dbxdb").log();
		}
		AutoSubscribedAlertView allAlertInfo = corecustomer_nonaccountlevel_alertsubtype_map.get(corecustomerid).get(key);
		
		if(allAlertInfo.getIsGlobal() == 0 && allAlertInfo.isAutoSubscribeEnabled()  )
		{
			populateDbxCustomerAlertEntitlement(infinityuser_id, "*", allAlertInfo,request,allAlertInfo.getValue1(),null,"*",list_entit);
			populateCustomerAlertChannel(infinityuser_id, "*", allAlertInfo, channel_map , channelpref,request,"*",list_channel);
			populateCustomeralertswitch(infinityuser_id, "*", allAlertInfo,request,"*",list_switch);
		}
	}
}

private void executePrimaryCustomerConfig(String infinityuser_id, String corecustomerid,
		HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_nonaccountlevel_alertsubtype_map,
		HashMap<String, HashMap<String, AutoSubscribedAlertView>> corecustomer_accountlevel_alertsubtype_map,
		HashMap<String, JSONObject> backend_alerts, String channelpref, Map<String, List<String>> channel_map,
		DataControllerRequest request, Map<String, Map<String, String>> corecust_account_accounttype_map,
		HashMap<String, ArrayList<String>> list_switch,HashMap<String, ArrayList<String>> list_channel,HashMap<String, ArrayList<String>> list_entit,String primaryId) throws Exception {
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("Primary Customer ").log();
	}
	for(String key : corecustomer_accountlevel_alertsubtype_map.get(corecustomerid).keySet())
	{
		String acno = key.split(",")[0];
		String alertsubtype = key.split(",")[1];
		AutoSubscribedAlertView allAlertInfo = corecustomer_accountlevel_alertsubtype_map.get(corecustomerid).get(key);
		
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("allAlertInfo : "+ allAlertInfo).log();
		}
		
		if(allAlertInfo.getIsGlobal() == 0 && allAlertInfo.isAutoSubscribeEnabled()  && allAlertInfo.getExternalSystem() == 1)
		{
			if(!isT24Exception)
			{
				if(backend_alerts.containsKey(key))
				{
					if(diagnostic.isDebugEnabled())
					{
						diagnostic.prepareDebug("creating  in dbxdb and updating in transact").log();
					}
					String value = null;
					if(backend_alerts.get(key).getJSONArray("fields") != null && backend_alerts.get(key).getJSONArray("fields").length() > 0)
					{
						value = backend_alerts.get(key).getJSONArray("fields").getJSONObject(0).getString("value");
					}
					String alertRequestId = backend_alerts.get(key).getString("alertRequestId");
					String status = updateT24(infinityuser_id,corecustomerid,acno,alertsubtype,request,alertRequestId,value,primaryId);
					
					if(status!= null && status.equalsIgnoreCase("success"))
					{
						populateDbxCustomerAlertEntitlement(infinityuser_id, acno, allAlertInfo,request,value,alertRequestId,"*",list_entit);
						populateCustomerAlertChannel(infinityuser_id, acno, allAlertInfo, channel_map , channelpref,request,"*",list_channel);
						populateCustomeralertswitch(infinityuser_id, acno, allAlertInfo,request,"*",list_switch);
					}
					else
					{
						if(diagnostic.isDebugEnabled())
						{
							diagnostic.prepareDebug("eror in updating transact").log();
						}
					}
					backend_alerts.remove(key);
				}
				else
				{
					if(diagnostic.isDebugEnabled())
					{
						diagnostic.prepareDebug("creating both in dbxdb and transact").log();
					}
					String alertRequestId = populateT24(corecustomerid,allAlertInfo,acno,infinityuser_id,request,primaryId);
					
					if(alertRequestId != null)
					{
						 populateDbxCustomerAlertEntitlement(infinityuser_id, acno, allAlertInfo,request,allAlertInfo.getValue1(),alertRequestId,"*",list_entit);
						 populateCustomerAlertChannel(infinityuser_id, acno, allAlertInfo, channel_map , channelpref,request,"*",list_channel);
						 populateCustomeralertswitch(infinityuser_id, acno, allAlertInfo,request,"*",list_switch);
					}
					else
					{
						if(diagnostic.isDebugEnabled())
						{
							diagnostic.prepareDebug("error in creating in transact").log();
						}
					}
				}
			}
			else
			{
				alert.prepareError("Skipping t24").log();
			}
		}
		else if(allAlertInfo.getIsGlobal() == 0 && allAlertInfo.isAutoSubscribeEnabled() )
		{
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("Configuring only in dbxdb").log();
			}
			
		    populateDbxCustomerAlertEntitlement(infinityuser_id, acno, allAlertInfo,request,allAlertInfo.getValue1(),null,"*",list_entit);
		    populateCustomerAlertChannel(infinityuser_id, acno, allAlertInfo, channel_map , channelpref,request,"*",list_channel);
		    populateCustomeralertswitch(infinityuser_id, acno, allAlertInfo,request,"*",list_switch);
		}
		else
		{
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("No Need to configure").log();
			}
		}
	}
	for(String key : corecustomer_nonaccountlevel_alertsubtype_map.get(corecustomerid).keySet())
	{
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("Configuring only in dbxdb").log();
		}
		AutoSubscribedAlertView allAlertInfo = corecustomer_nonaccountlevel_alertsubtype_map.get(corecustomerid).get(key);
		
		if(allAlertInfo.getIsGlobal() == 0 && allAlertInfo.isAutoSubscribeEnabled()  )
		{
			populateDbxCustomerAlertEntitlement(infinityuser_id, "*", allAlertInfo,request,allAlertInfo.getValue1(),null,"*",list_entit);
			populateCustomerAlertChannel(infinityuser_id, "*", allAlertInfo, channel_map , channelpref,request,"*",list_channel);
			populateCustomeralertswitch(infinityuser_id, "*", allAlertInfo,request,"*",list_switch);
		}
	
	}
}


private HashMap<String, HashMap<String,AutoSubscribedAlertView>> getValidNonAccountLevelALertSubTypes(
		HashMap<String, ArrayList<AutoSubscribedAlertView>> corecustomer_alert_map) {
	
	HashMap<String, HashMap<String,AutoSubscribedAlertView>> obj = new HashMap<String, HashMap<String,AutoSubscribedAlertView>>();
	
	for(String cust : corecustomer_alert_map.keySet() )
	{
		HashMap<String,AutoSubscribedAlertView> obj1 = new HashMap<String,AutoSubscribedAlertView>();
		for(AutoSubscribedAlertView allAlertInfo : corecustomer_alert_map.get(cust))
		{
			if(allAlertInfo.getIsAccountLevel() == 0)
			{
				
				obj1.put(allAlertInfo.getAlertsubtypeid(), allAlertInfo);
				
			}
		}
		obj.put(cust, obj1);
	}
	
	return obj;
}

private ArrayList<AutoSubscribedAlertView> getautosubscribealerts(DataControllerRequest request,String channelpref) throws Exception {

	Result all_alert_view = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_autosubscribedalertsview_get", schemaname),null,null,request);
	  
	
	  JSONArray all_alert_view_json = ResultToJSON.convertDataset( all_alert_view.getDatasetById("autosubscribedalertsview"));
	  
	  if(diagnostic.isDebugEnabled())
	  {
		  diagnostic.prepareDebug("all_alert_view_json : "+all_alert_view_json ).log();
	  }
	  ArrayList<AutoSubscribedAlertView> allAlertInfo_lst = new Gson().fromJson(all_alert_view_json.toString(), new TypeToken<List<AutoSubscribedAlertView>>(){}.getType());
	
	  if( ! channelpref.equalsIgnoreCase("ALERT"))
	  {
		  List<String> data = new ArrayList<String>();
		  for(AutoSubscribedAlertView autoSubscribedAlertView : allAlertInfo_lst)
		  {
			  if(autoSubscribedAlertView.isAutoSubscribeEnabled())
			  {
				  data.add(autoSubscribedAlertView.getGroupid());
			  }
		  }
		  for(AutoSubscribedAlertView autoSubscribedAlertView : allAlertInfo_lst)
		  {
			  if(data.contains(autoSubscribedAlertView.getGroupid()))
			  {
				  autoSubscribedAlertView.setAutoSubscribeEnabled(true);
			  }
		  }
	  }
	  
	  return allAlertInfo_lst;

}

private HashMap<String, HashMap<String, AutoSubscribedAlertView>> getValidAccountLevelALertSubTypes(
		HashMap<String, ArrayList<AutoSubscribedAlertView>> customer_alert_map,
		Map<String, Map<String, String>> corecust_account_accounttype_map,
		Map<String, List<String>> valid_accounttype_alerttypes) {

	
	HashMap<String, HashMap<String, AutoSubscribedAlertView>> obj = new HashMap<String, HashMap<String, AutoSubscribedAlertView>>();
	
	for(String cust : corecust_account_accounttype_map.keySet() )
	{
		HashMap<String, AutoSubscribedAlertView> obj1 = new HashMap<String, AutoSubscribedAlertView>();
		for (String acnt : corecust_account_accounttype_map.get(cust).keySet()) {
			String acnt_type = corecust_account_accounttype_map.get(cust).get(acnt);

			if (valid_accounttype_alerttypes.containsKey(acnt_type)) {

				List<String> validalertsubtypestypes = valid_accounttype_alerttypes.get(acnt_type);

				for (AutoSubscribedAlertView allAlertInfo : customer_alert_map.get(cust)) {
					if (allAlertInfo.getIsAccountLevel() == 1) {
						if (validalertsubtypestypes.contains(allAlertInfo.getAlertsubtypeid())) {
							String key = acnt + "," + allAlertInfo.getAlertsubtypeid();
							obj1.put(key, allAlertInfo);
						}

					}

				}

			}

		}

		obj.put(cust, obj1);

	}
		
		
	
	return obj;

}

private HashMap<String, ArrayList<AutoSubscribedAlertView>> getCoreCustomerAlertSubTypeMap(Map<String, String> corecustomer_type_map, Map<String, ArrayList<String>> validalertsubtypecustomertype_map,ArrayList<AutoSubscribedAlertView> allAlertInfo_lst) {
		
	HashMap<String, ArrayList<AutoSubscribedAlertView>> corecustalertsubtype = new HashMap<String, ArrayList<AutoSubscribedAlertView>>();
	for(String corecustomer : corecustomer_type_map.keySet())
	{
		String corecusttype = corecustomer_type_map.get(corecustomer);
		ArrayList<String>  valid_alertsubtypes = validalertsubtypecustomertype_map.get(corecusttype);
		
		for(AutoSubscribedAlertView allAlertInfo : allAlertInfo_lst)
		{
			//if(allAlertInfo.getIsAutoSubscribeEnabled() == 1)
			{
				String alertsubtypeid = allAlertInfo.getAlertsubtypeid();
				
				if(valid_alertsubtypes.contains(alertsubtypeid))
				{
					if(corecustalertsubtype.containsKey(corecustomer))
					{
						corecustalertsubtype.get(corecustomer).add(allAlertInfo);
					}
					else
					{
						ArrayList<AutoSubscribedAlertView>  AllAlertInfo_lst = new ArrayList<AutoSubscribedAlertView>();
						AllAlertInfo_lst.add(allAlertInfo);
						corecustalertsubtype.put(corecustomer, AllAlertInfo_lst);
					}
				}
				else
				{
					if(diagnostic.isDebugEnabled())
					{
						diagnostic.prepareDebug("NOT VALID").log();
					}
					
				}
			}
			
		}
	}
	return corecustalertsubtype;
}

private Map<String, ArrayList<String>> getValidalertsubtypecustomertypeCombinations(DataControllerRequest request) throws Exception {


	
	Result alertsubtypecustomertype_res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_alertsubtypecustomertype_get", schemaname),null,null,request);
	
	
	Map<String, ArrayList<String>> data = alertsubtypecustomertype_res.getDatasetById("alertsubtypecustomertype").getAllRecords().stream()
		    .collect(Collectors.groupingBy(
		    rec -> rec.getParamValueByName("customerTypeId"),
		        Collectors.mapping(rec -> rec.getParamValueByName("alertSubTypeId"),
		            Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))));
	
	return data;
	

}

private List<Object> getInfinityUseridDetails(String infinity_customerid, DataControllerRequest request, String auth) throws Exception {
	List<JSONArray> cust_data = invokeInfinityUrl(infinity_customerid,request,auth);
	
	JSONArray companyList = cust_data.get(0);
	JSONArray userDetails = cust_data.get(1);
	
    HashMap<String,String> corecustomer_type_map = new HashMap<String,String>();
    HashMap<String,String> corecustomer_isprimary_map = new HashMap<String,String>();
    HashMap<String,HashMap<String,String>> corecust_account_accounttype_map = new HashMap<String,HashMap<String,String>>();
    
    
    for(int i=0;i<companyList.length();i++)
    {
   	 JSONObject company = companyList.getJSONObject(i);
   	 String corecustomerid = company.getString("cif");
   	 String corecustomertype = company.getString("contractType");
   	 String isPrimary = company.getString("isPrimary");
   	 
   	 corecustomer_type_map.put(corecustomerid, corecustomertype);
   	 corecustomer_isprimary_map.put(corecustomerid, isPrimary);
   	 
   	 JSONArray accounts = company.getJSONArray("accounts");
   	 HashMap<String,String> acmap = new HashMap<String,String>();
   	 for(int j=0;j<accounts.length();j++)
        {
   		 JSONObject account = accounts.getJSONObject(j);
   		 String accountid = account.getString("accountId");
   		 String accounttype = account.getString("accountType");
   		 
   		 
   		 acmap.put(accountid, accounttype);
        }
   	 
   	 corecust_account_accounttype_map.put(corecustomerid, acmap);
    }
    
    if(diagnostic.isDebugEnabled())
    {
   	 diagnostic.prepareDebug("corecustomer_type_map : " + corecustomer_type_map).log();
        diagnostic.prepareDebug("corecustomer_isprimary_map : " + corecustomer_isprimary_map).log();
        diagnostic.prepareDebug("corecust_account_accounttype_map : " + corecust_account_accounttype_map).log();
    }
    
	
    List<Object> data = new ArrayList<Object>();
    data.add(corecustomer_type_map);
    data.add(corecustomer_isprimary_map);
    data.add(corecust_account_accounttype_map);
    
    String primaryId = userDetails.getJSONObject(0).getString("coreCustomerId");
    data.add(primaryId);
    
	return data;
}

private List<JSONArray> invokeInfinityUrl(String infinity_customerid, DataControllerRequest request, String auth) throws Exception {
	Map<String, Object> inputmap = new HashMap<>();

	inputmap.put("id", infinity_customerid);

	Map<String, Object> headermap = new HashMap<>();

	if (diagnostic.isDebugEnabled()) {
		diagnostic.prepareDebug("auth key : " + request.getParameter("X-Kony-Authorization")).log();
	}

	//headermap.put("X-Kony-Authorization", request.getParameter("X-Kony-Authorization"));
	headermap.put("X-Kony-Authorization", request.getParameter("auth"));

	//inputmap.put("X-Kony-Authorization", request.getParameter("X-Kony-Authorization"));
	inputmap.put("X-Kony-Authorization", request.getParameter("auth"));

	Result result = callObjectService("CustomerManagementObjService", "InfinityUser", "getInfinityUser", inputmap,
			headermap, request);

	if (diagnostic.isDebugEnabled()) {
		diagnostic.prepareDebug("Result from getInfinityUser : " + result).log();
	}

	JSONArray companyList_json = ResultToJSON.convertDataset(result.getDatasetById("companyList"));

	if (diagnostic.isDebugEnabled()) {
		diagnostic.prepareDebug("companyList_json : " + companyList_json).log();
	}
	
	JSONArray userDetails_json = ResultToJSON.convertDataset(result.getDatasetById("userDetails"));
	
	if (diagnostic.isDebugEnabled()) {
		diagnostic.prepareDebug("userDetails_json : " + userDetails_json).log();
	}
	
	List<JSONArray> obj = new ArrayList<JSONArray>();
	obj.add(companyList_json);
	obj.add(userDetails_json);

	return obj;

}

private Map<String, List<String>> getChannelConfig(DataControllerRequest request, String channelpref) throws JsonParseException, JsonMappingException, MiddlewareException, IOException, Exception {
	
	Map<String, List<String>> channel_map  = null;
	
	if(channelpref.equalsIgnoreCase("GROUP"))
	  {
		  channel_map = getGroupChannelConfig(request);
	  }
	  else  if(channelpref.equalsIgnoreCase("CATEGORY"))
	  {
		  channel_map = getCategoryChannelConfig(request);
	  }
	  else  if(channelpref.equalsIgnoreCase("ALERT"))
	  {
		  channel_map =  getAlertSubtypeChannelConfig(request);
	  }
	
	return channel_map;
}

private String unsubscribeT24(String infinityuser_id,  DataControllerRequest request,String alertRequestId) throws Exception {
	
	Map<String, Object> inputmap = new HashMap<>();
	inputmap.put("alertRequestId", alertRequestId);	
	inputmap.put("externalUserId", infinityuser_id);
	inputmap.put("subscribe", "NO");
	String status = null;
	Map<String, Object> headerMap = new HashMap<>();
	try
	{
		Result result = callOtherService("T24AlertSubscription","update",inputmap,headerMap,request);
		
		
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug(result.toString()).log();
		}
	   
		Record header = result.getRecordById("header");
		status =  header.getParamValueByName("status");
		
		 if(!status.equalsIgnoreCase("success"))
		 {
			 alert.prepareError("error msg : " + result.getParamValueByName("errorDetailsMessage")).log();
			 alert.prepareError("error code : " + result.getParamValueByName("errorDetailsCode")).log();
		 }
	}
	catch(Exception e)
	{
		alert.prepareError("error in unsubscribe , configuring only internal").log();
		isT24Exception = true;
	}
	
   
   return status;
}


private String updateT24(String infinityuser_id, String corecustomerid, String account, String alertsubtype, DataControllerRequest request,String alertRequestId, String value,String primaryId) throws Exception {
	
	
	String status = unsubscribeT24(infinityuser_id,request,alertRequestId);
	
	if(status != null && status.equalsIgnoreCase("success"))
	{
		status = null;
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("UNSUBSCRIBE successfull").log();
		}
		
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put("alertRequestId", alertRequestId);
		
		inputmap.put("eventId", alertsubtype);
		inputmap.put("externalCustomerId", primaryId);
		inputmap.put("customerId", corecustomerid);
		inputmap.put("externalUserId", infinityuser_id);
		inputmap.put("contractReference", account);
		if(value != null)
		{
			inputmap.put("value", value);
		}
		inputmap.put("subscribe", "YES");
		Map<String, Object> headerMap = new HashMap<>();
		try
		{
			Result result = callOtherService("T24AlertSubscription","update",inputmap,headerMap,request);
			
			
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug(result.toString()).log();
			}
		   
			Record header = result.getRecordById("header");
			status =  header.getParamValueByName("status");
			
			 if(!status.equalsIgnoreCase("success"))
			 {
				 alert.prepareError("error msg : " + result.getParamValueByName("errorDetailsMessage")).log();
				 alert.prepareError("error code : " + result.getParamValueByName("errorDetailsCode")).log();
			 }
		}
		catch(Exception e)
		{
			alert.prepareError("erorr in update , configuring only internal alerts").log();
			isT24Exception = true;
		}
		
	}
	else
	{
		alert.prepareError("UNSUBSCRIBE FAILED").log();
	}
   
   return status;
}

private String populateT24(String corecustomerid, AutoSubscribedAlertView alertsubtype, String account, String infinity_userid,DataControllerRequest request,String primaryId) throws Exception {
	
		
	Map<String, Object> inputmap = new HashMap<>();
	
	inputmap.put("eventId", alertsubtype.getAlertsubtypeid());
	inputmap.put("externalCustomerId", primaryId);
	inputmap.put("customerId", corecustomerid);
	inputmap.put("externalUserId", infinity_userid);
	inputmap.put("contractReference", account);
	inputmap.put("eventType", alertsubtype.getGroupid());
	if(alertsubtype.getValue1() != null)
	{
		inputmap.put("value", alertsubtype.getValue1());
	}
	
	Result result = null;
	Map<String, Object> headerMap = new HashMap<>();
	try
	{
		result = callOtherService("T24AlertSubscription","create",inputmap,headerMap,request);
	}
	catch(Exception e)
	{
		alert.prepareError("error in create, Configuring only internal alerts").log();
		isT24Exception = true;
		return null;
	}
	
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug(result.toString()).log();
	}
	
	Record header = result.getRecordById("header");
	String status =  header.getParamValueByName("status");
	String id = header.getParamValueByName("id");
 
	 if(status.equalsIgnoreCase("success"))
	 {
		 return id;
	 } 
	 else
	 {
		 alert.prepareError("error msg : " + result.getParamValueByName("errorDetailsMessage")).log();
		 alert.prepareError("error code : " + result.getParamValueByName("errorDetailsCode")).log();
		 return null;
	 }
		 
}


private HashMap<String,JSONObject> fetchBackendAlerts(String backendId, DataControllerRequest request) throws Exception {
	// TODO Auto-generated method stub
	
	 HashMap<String,JSONObject> backend_alerts = new HashMap<String,JSONObject>();
	
	Map<String, Object> headerMap = new HashMap<>();
	
	headerMap.put("customerId", backendId);
	
	try
	{
		Result result = callOtherService("T24AlertSubscription","get",null,headerMap,request);
		
		JSONArray array = ResultToJSON
				.convertDataset(result.getDatasetById("body")); 
	
     
     for(int i = 0; i < array.length(); i++)
     {
    	 JSONObject jsonobj = array.getJSONObject(i);
    	 
    	 String key =  null;
    	 if(jsonobj.get("contractReference") != null)
    	 {
    		 key = jsonobj.getString("contractReference")+","+jsonobj.getString("eventId");
    	 }
    	 else
    	 {
    		 key = jsonobj.getString("eventId");
    	 }	 
    		 
      backend_alerts.put(key, jsonobj);	  
    	
     }
	}
	catch(Exception e)
	{
		alert.prepareError("Excpetion in get , Configuring only internal alerts").log();
		isT24Exception = true;
	}
	
	
     return backend_alerts;
}


private Map<String, List<String>> getValidAccountTypeAlertSubTypeCombinations(DataControllerRequest request) throws Exception {
	
	Result alertsubtypeaccounttype_res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_alertsubtypeaccounttype_view_get", schemaname),null,null,request);
	
	 
	 Map<String, List<String>> data = alertsubtypeaccounttype_res.getDatasetById("alertsubtypeaccounttype_view").getAllRecords().stream()
	            .collect(Collectors.groupingBy(
	            rec -> rec.getParamValueByName("accountTypeId"),
	                Collectors.mapping(rec -> rec.getParamValueByName("alertSubTypeId"),
	                    Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))));
	
	 return data;
}


private Map<String,List<String>> getAlertSubtypeChannelConfig(DataControllerRequest request)
		throws MiddlewareException, IOException, JsonParseException, JsonMappingException, Exception {
	
	Result alertsubtypechannel_res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_alertsubtypechannel_get", schemaname),null,null,request);

	List<Record> alertsubtypechannel = alertsubtypechannel_res.getDatasetById("alertsubtypechannel").getAllRecords();
	  
	  
	  Map<String, List<String>> data = alertsubtypechannel.stream()
	            .collect(Collectors.groupingBy(
	            rec -> rec.getParamValueByName("alertSubTypeId"),
	                Collectors.mapping(rec -> rec.getParamValueByName("channelId"),
	                    Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))));
	  
	  return data;
}

private Map<String,List<String>> getCategoryChannelConfig(DataControllerRequest request)
		throws MiddlewareException, IOException, JsonParseException, JsonMappingException, Exception {
	  
	Result alertcategorychannel_res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_alertcategorychannel_get", schemaname),null,null,request);
	
	List<Record> alertcategorychannel = alertcategorychannel_res.getDatasetById("alertcategorychannel").getAllRecords();
	 
	  
	  Map<String, List<String>> data = alertcategorychannel.stream()
	            .collect(Collectors.groupingBy(
	            rec -> rec.getParamValueByName("AlertCategoryId"),
	                Collectors.mapping(rec -> rec.getParamValueByName("ChannelID"),
	                    Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))));
	  
	  return data;
}

private Map<String,List<String>> getGroupChannelConfig(DataControllerRequest request)
		throws MiddlewareException, IOException, JsonParseException, JsonMappingException, Exception {
	
	Result alerttypechannel_res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_alerttypechannel_get", schemaname),null,null,request);
	  
	List<Record> alerttypechannel = alerttypechannel_res.getDatasetById("alerttypechannel").getAllRecords();
	  
	  Map<String, List<String>> data = alerttypechannel.stream()
	            .collect(Collectors.groupingBy(
	            rec -> rec.getParamValueByName("alertTypeId"),
	                Collectors.mapping(rec -> rec.getParamValueByName("channelId"),
	                    Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))));
	  
	  return data;
}

private String getCustomerviewalertconfigurationdata(DataControllerRequest request)
		throws Exception {
	
	  Result customerviewalertconfiguration_res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_customerviewalertconfiguration_get", schemaname),null,null,request);

	return  customerviewalertconfiguration_res.getDatasetById("customerviewalertconfiguration").getAllRecords().get(0).getParamValueByName("alertPreferenceView");
}


private void populateCustomeralertswitch(String infinity_customerid, String account_number,
		AutoSubscribedAlertView obj_AllAlertInfo, DataControllerRequest request,String actype, HashMap<String, ArrayList<String>> list) throws Exception {
	
	
	if(list.containsKey(account_number) && list.get(account_number).contains(obj_AllAlertInfo.getCategoryid()))
		return ;
	if(list.containsKey(account_number))
	{
		list.get(account_number).add(obj_AllAlertInfo.getCategoryid());
	}
	else
	{
		ArrayList<String> dt = new ArrayList<String>();
		dt.add(obj_AllAlertInfo.getCategoryid());
		list.put(account_number, dt);
	}

	  Map<String, Object> inputparams = new HashMap<>();
	     inputparams.put("Customer_id", infinity_customerid);
	     inputparams.put("AccountID", account_number);
	     inputparams.put("AlertCategoryId", obj_AllAlertInfo.getCategoryid());
	     inputparams.put("AccountType", actype);
	     inputparams.put("Status_id","SID_SUBSCRIBED");
	     inputparams.put("createdts", new Timestamp(System.currentTimeMillis()));
	     inputparams.put("softdeleteflag", 0);
	  
	Result res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_customeralertswitch_create", schemaname),inputparams,null,request);
	
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("result :"+ res).log();
	}
	
}

private void populateCustomerAlertChannel(String infinity_customerid, String account_number,
		AutoSubscribedAlertView obj_AllAlertInfo, Map<String, List<String>> channel_map, String channelpref, DataControllerRequest request,String actype,HashMap<String, ArrayList<String>> list) throws Exception {
	
	List<String> channels = null;
	if(channelpref.equalsIgnoreCase("GROUP"))
	{
		 if(list.containsKey(account_number) && list.get(account_number).contains(obj_AllAlertInfo.getGroupid()))
				return ;
			if(list.containsKey(account_number))
			{
				list.get(account_number).add(obj_AllAlertInfo.getGroupid());
			}
			else
			{
				ArrayList<String> dt = new ArrayList<String>();
				dt.add(obj_AllAlertInfo.getGroupid());
				list.put(account_number, dt);
			}
			
		 channels = channel_map.get(obj_AllAlertInfo.getGroupid());
		 
		 for(String channel : channels)
			{

				Map<String, Object> inputparams = new HashMap<>();
				     inputparams.put("customerId",infinity_customerid);
				     inputparams.put("alertCategoryId",obj_AllAlertInfo.getCategoryid());
				     inputparams.put("alertTypeId", obj_AllAlertInfo.getGroupid());
				     inputparams.put("alertSubTypeId", "*");
				     inputparams.put("channelId", channel);
				     inputparams.put("accountId", account_number);
				     inputparams.put("accountType", actype);
				  
				Result res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_customeralertchannel_create", schemaname),inputparams,null,request);
				
				if(diagnostic.isDebugEnabled())
				{
					diagnostic.prepareDebug("result :"+ res).log();
				}

				
			}
		 
		
	}
	else if(channelpref.equalsIgnoreCase("CATEGORY"))
	{
		if(list.containsKey(account_number) && list.get(account_number).contains(obj_AllAlertInfo.getCategoryid()))
			return ;
		if(list.containsKey(account_number))
		{
			list.get(account_number).add(obj_AllAlertInfo.getCategoryid());
		}
		else
		{
			ArrayList<String> dt = new ArrayList<String>();
			dt.add(obj_AllAlertInfo.getCategoryid());
			list.put(account_number, dt);
		}

		channels = channel_map.get(obj_AllAlertInfo.getCategoryid());
		
		for(String channel : channels)
		{

			Map<String, Object> inputparams = new HashMap<>();
			     inputparams.put("customerId",infinity_customerid);
			     inputparams.put("alertCategoryId",obj_AllAlertInfo.getCategoryid());
			     inputparams.put("alertTypeId", "*");
			     inputparams.put("alertSubTypeId", "*");
			     inputparams.put("channelId", channel);
			     inputparams.put("accountId", account_number);
			     inputparams.put("accountType", actype);
			  
			Result res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_customeralertchannel_create", schemaname),inputparams,null,request);
			
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("result :"+ res).log();
			}

		}
	}
	else if(channelpref.equalsIgnoreCase("ALERT"))
	{
		if(list.containsKey(account_number) && list.get(account_number).contains(obj_AllAlertInfo.getAlertsubtypeid()))
			return ;
		if(list.containsKey(account_number))
		{
			list.get(account_number).add(obj_AllAlertInfo.getAlertsubtypeid());
		}
		else
		{
			ArrayList<String> dt = new ArrayList<String>();
			dt.add(obj_AllAlertInfo.getAlertsubtypeid());
			list.put(account_number, dt);
		}
		channels = channel_map.get(obj_AllAlertInfo.getAlertsubtypeid());
		
		for(String channel : channels)
		{

			Map<String, Object> inputparams = new HashMap<>();
			     inputparams.put("customerId",infinity_customerid);
			     inputparams.put("alertCategoryId",obj_AllAlertInfo.getCategoryid());
			     inputparams.put("alertTypeId", obj_AllAlertInfo.getGroupid());
			     inputparams.put("alertSubTypeId", obj_AllAlertInfo.getAlertsubtypeid());
			     inputparams.put("channelId", channel);
			     inputparams.put("accountId", account_number);
			     inputparams.put("accountType", actype);
			  
			Result res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_customeralertchannel_create", schemaname),inputparams,null,request);
			
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("result :"+ res).log();
			}

		}
	}
	
	
}

private void populateDbxCustomerAlertEntitlement(String corecustomerid,
		String account_number, AutoSubscribedAlertView obj_AllAlertInfo, DataControllerRequest request, String value, String alertRequestId,String actype,HashMap<String, ArrayList<String>> list) throws Exception {
	
	if(list.containsKey(account_number) && list.get(account_number).contains(obj_AllAlertInfo.getAlertsubtypeid()))
		return ;
	if(list.containsKey(account_number))
	{
		list.get(account_number).add(obj_AllAlertInfo.getAlertsubtypeid());
	}
	else
	{
		ArrayList<String> dt = new ArrayList<String>();
		dt.add(obj_AllAlertInfo.getAlertsubtypeid());
		list.put(account_number, dt);
	}
	
	  Map<String, Object> inputparams = new HashMap<>();
	
			  inputparams.put("Customer_id", corecustomerid);
			  inputparams.put("alertCategoryId", obj_AllAlertInfo.getCategoryid());
			  inputparams.put("AlertTypeId", obj_AllAlertInfo.getGroupid());
			  inputparams.put("alertSubTypeId",obj_AllAlertInfo.getAlertsubtypeid());
			  inputparams.put("AccountId", account_number); if(value != null)
			  inputparams.put("Value1", value); else inputparams.put("Value1",
			  obj_AllAlertInfo.getValue1());
			  
			  inputparams.put("Value2", obj_AllAlertInfo.getValue2());
			  inputparams.put("AccountType", actype); inputparams.put("createdts", new
			  Timestamp(System.currentTimeMillis())); inputparams.put("lastmodifiedts", new
			  Timestamp(System.currentTimeMillis())); inputparams.put("synctimestamp", new
			  Timestamp(System.currentTimeMillis())); inputparams.put("alertRequestId",
			  alertRequestId); inputparams.put("softdeleteflag", 0);
			 
	     
	  
	Result res = callOtherService("CRUDLayer",replaceSchemaName("{schema_name}_dbxcustomeralertentitlement_create", schemaname),inputparams,null,request);
	
	if(diagnostic.isDebugEnabled())
	{
		diagnostic.prepareDebug("result :"+ res).log();
	}
	
	 
}
  
  public static Result callOtherService(String serviceID, String operationID, Map<String, Object> inputmap,
			Map<String, Object> headermap, DataControllerRequest dcRequest) throws Exception {
		Result result = null;
		try
		{
			OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder().withServiceId(serviceID)
					.withOperationId(operationID).build();
			
			ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputmap).withHeaders(headermap).build();
			result = serviceRequest.invokeServiceAndGetResult();
		}
		catch(Exception e)
		{
			alert.prepareError("Error occurred: ", e).log();
			throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : " + operationID + " ; " + e.getMessage() );
		}
		return result;
	}
  public static Result callObjectService(String serviceID,String objid, String operationID, Map<String, Object> inputmap,
			Map<String, Object> headermap, DataControllerRequest dcRequest) throws Exception {
		Result result = null;
		try
		{
			OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder().withServiceId(serviceID).withObjectId(objid)
					.withOperationId(operationID).build();
			
			ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputmap).withHeaders(headermap).withAuthorizationToken(dcRequest.getParameter("X-Kony-Authorization")).build();
			result = serviceRequest.invokeServiceAndGetResult();
		}
		catch(Exception e)
		{
			alert.prepareError("Error occurred: ", e).log();
			throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : " + operationID + " objectid : " + objid +" ; "+ e.getMessage() );
		}
		return result;
	}
  
 

	public static Result returnResult(boolean flag, String msg) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, msg, Constants.STRING));
		}
		return res;
	}
	
	public static Result returnResult(boolean flag, String code,String msg) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(Constants.SUCCESS, Constants.TRUE, Constants.STRING));
		} else {
			res.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRMSG, msg, Constants.STRING));
			res.addParam(new Param(Constants.DBPERRCODE, code, Constants.STRING));
		}
		return res;
	}
	
	public static String getValue(String key){
	    return getValue(key, null, true);
	  }

	  public static String getValue(String key, String defaultValue) {
	    return getValue(key, defaultValue, false);
	  }
	
	private static String getValue(String key, String defaultValue, boolean required) {
	    String value = null;
	    try {
	    value = EnvironmentConfigurationsHandler.getServerAppProperty(key);
	    }catch (Exception e) {
	    	return defaultValue;
		}
	    if (value == null) {
	      if (required) {
	        throw new RuntimeException("Required configurable parameter " + key + " has not been set!");
	      }
	      return defaultValue;
	    } else {
	      return value;
	    }
	  }
	public static String replaceSchemaName(String operationid, String schemaname) {
	    if (operationid == null || schemaname == null)
	      return operationid; 
	    if (operationid.contains("{schema_name}"))
	      operationid = operationid.replace("{schema_name}", schemaname); 
	    return operationid;
	  }
}
