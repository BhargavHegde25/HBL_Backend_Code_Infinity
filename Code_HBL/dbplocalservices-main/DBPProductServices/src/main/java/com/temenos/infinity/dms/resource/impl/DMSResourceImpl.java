package com.temenos.infinity.dms.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPErrorCodeSetter;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.dms.businessdelegate.api.DMSBusinessDelegate;
import com.temenos.infinity.dms.resource.api.DMSResource;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;

import org.json.JSONObject;

public class DMSResourceImpl implements DMSResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result loginAndSearchFiles(DataControllerRequest dcRequest, String page, String accountNnumber, String customerNumber, String year,
			String subType, String authToken) throws ApplicationException{

		Result result = new Result();
		if (StringUtils.isBlank(page) || StringUtils.isBlank(year) || StringUtils.isBlank(subType)) {
			diagnostic.prepareDebug("Missing Params").log();
			return ErrorCodeEnum.ERR_10204.setErrorCode(new Result());
		}
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
			String userId = CustomerSession.getCustomerId(customer);
			
			if(!userId.equals(customerNumber)){
				alert.prepareError("Not Authorized user").log();
				return ErrorCodeEnum.ERR_11001.setErrorCode(new Result());
			}
			
			CustomerAccountsBusinessDelegate customerAccountsBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerAccountsBusinessDelegate.class);
            boolean validAccount = customerAccountsBusinessDelegate.checkIfCustomerHasThisAccount(accountNnumber, customerNumber, dcRequest.getHeaderMap());
            if(!validAccount) {
            	alert.prepareError("Not Authorized Account").log();
				return ErrorCodeEnum.ERR_11001.setErrorCode(new Result());
            }
            
			DMSBusinessDelegate dmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(DMSBusinessDelegate.class);
			ArrayList<JSONObject> documents = dmsBusinessDelegate.search(dcRequest,page, accountNnumber,
					customerNumber, year, subType, authToken);

			HashMap<String, ArrayList<String>> monthlyFileMap = new HashMap<String, ArrayList<String>>();
			if (documents != null && !documents.isEmpty()) {
				for (int i = 0; i < documents.size(); i++) {
					
					String fileYear="";
					String fileDate="";
					if (documents.get(i).getString("documentDate") != null
							&& documents.get(i).getString("documentDate") != "") {
						String[] documentDate = documents.get(i).getString("documentDate").split("-");
						if (documentDate.length >= 3) {
							fileYear = documentDate[0];
							fileDate = documentDate[2];
						}
					}
					String monthName = documents.get(i).getString("month");
					if (monthlyFileMap.containsKey(monthName)) {
						ArrayList<String> existingFilesList = monthlyFileMap.get(monthName);
						String newFileDetails = documents.get(i).getString("subType")+"_"+fileDate+"-"+monthName+"-"+fileYear+"."+documents.get(i).getString("fileExtension")+"/"+ documents.get(i).getString("documentId");
						existingFilesList.add(newFileDetails);
						monthlyFileMap.put(monthName, existingFilesList);
					} else {
						ArrayList<String> newFilesList = new ArrayList<String>();
						String newFileDetails = documents.get(i).getString("subType")+"_"+fileDate+"-"+monthName+"-"+fileYear+"."+documents.get(i).getString("fileExtension")+"/"+ documents.get(i).getString("documentId");
						newFilesList.add(newFileDetails);
						monthlyFileMap.put(monthName, newFilesList);
					}
				}
			}
			Record monthlyData;
			ArrayList<Dataset> totalFilesInaYear = new ArrayList<Dataset>();
			ArrayList<Param> filesInaMonth;
			for (Entry<String, ArrayList<String>> entry : monthlyFileMap.entrySet()) {
				Dataset totalfilesInMonth = new Dataset(entry.getKey());
				ArrayList<String> currentmonthEntries = entry.getValue();
				monthlyData = new Record();
				filesInaMonth = new ArrayList<Param>();
				Param fileDetails;
				for (int i = 0; i < currentmonthEntries.size(); i++) {
					fileDetails = new Param("file" + " " + i, currentmonthEntries.get(i));
					filesInaMonth.add(fileDetails);
				}
				monthlyData.addAllParams(filesInaMonth);
				totalfilesInMonth.addRecord(monthlyData);
				totalFilesInaYear.add(totalfilesInMonth);
			}
			result.addAllDatasets(totalFilesInaYear);
			return result;
		}
		catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        }
		catch (Exception e) {
			alert.prepareError("Document Retrieve Failed--"+e).log();
			return ErrorCodeEnum.ERR_25001.setErrorCode(new Result());
		}
	}
	
	@Override
	public Result loginAndDownload(String documentId, String revision, String operation, String authToken) {

		Result result = new Result();
		// If no AuthToken is passed to input for download(Public) throw Invalid Input Exception
		if (operation.equalsIgnoreCase("download") && StringUtils.isBlank(authToken)) {
			diagnostic.prepareDebug("Download Operation performed without Auth Token").log();
			return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
		}
		try {
			DMSBusinessDelegate dmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(DMSBusinessDelegate.class);
			// Download Operation
			if (documentId != null && revision != null) {
				String DownloadResult = dmsBusinessDelegate.download(documentId, revision,authToken);
				result.addParam("base64", DownloadResult);
				diagnostic.prepareDebug("Document Retrieved Successfully").log();
			} else {
				diagnostic.prepareDebug("DMS-documentId revision number are empty").log();
				return ErrorCodeEnum.ERR_10204.setErrorCode(new Result());
			}

		} catch (Exception e) {
			alert.prepareError("Document Retrieve Failed--"+e).log();
			return ErrorCodeEnum.ERR_25003.setErrorCode(new Result());
		}
		return result;
	}

}