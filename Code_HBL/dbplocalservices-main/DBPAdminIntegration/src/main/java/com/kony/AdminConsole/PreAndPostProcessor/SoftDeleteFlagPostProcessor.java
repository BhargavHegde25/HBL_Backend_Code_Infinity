package com.kony.AdminConsole.PreAndPostProcessor;

import java.util.List;
 
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
 


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class SoftDeleteFlagPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	    
	    @Override
	    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) {
	        
	    	Set<String> booleanFlagSet = new HashSet<>();
			booleanFlagSet.addAll(Arrays.asList(new String[] {"softdeleteflag","hasSuperAdminPrivilages",
					"softdelete","isPrimary","featureIsPrimary","isAccountLevel","isMFAApplicable","Feature_isPrimary",
					"isCombinedUser","IsEnrolledForOlb","IsStaffMember","isEnrolled","IsAssistConsented","IsOlbAllowed",
					"isEagreementSigned","implicitAccountAccess","isBusiness","isFeaturePrimary","isPreLoginConfiguration",
					"isBusinessType","isCustomerCentric","isLeadSupported","alertcategory_accountLevel","alertcategorytext_softdeleteflag",
					"alertcategory_softdeleteflag","alertsubtype_softdeleteflag","alertsubtype_isAutoSubscribeEnabled",
					"isAutoSubscribeEnabled","alerttypetext_softdeleteflag","alerttype_softdeleteflag",
					"isPreferredContactMethod","isGroupLevel","isOwner","autoSyncAccounts","isAdmin","isAuthSignatory",
					"passwordExpiryWarningRequired","isDisabled","isGroupMatrix","isVerified","isInternationalAccount",
					"isSameBankAccount","softDelete","isBillPaySupported","isBillPayActivated","isDefaultGroup", 
					"isWireTransferEligible","isWireTransferActivated","isP2PSupported","isP2PActivated","invalid"}));

			
	        List<Dataset> dsList = result.getAllDatasets();
	        
	 
	        for (Dataset ds : dsList) {
	        	
	        	List<Record> recordList = ds.getAllRecords();
	        
	        	for(Record rec : recordList) {
	        		List<Param> paramList = rec.getAllParams();
	        		
	        		for (Param param : paramList) {
	        					
	        			if(booleanFlagSet.contains(param.getName())) {
	        				
	        				if(param.getValue().equalsIgnoreCase("0"))  {
	        					param.setValue("false");
	        				}
	        				else if (param.getValue().equalsIgnoreCase("1")) {
	        					param.setValue("true");
	        				}

	        			}
	        		}
	        	}
	        }
	      
	        return result;
	        
	    }
	}

