package com.kony.adminconsole.service.business;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CompanyApprovalMatrixGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result result = new Result();
		long startTime = System.currentTimeMillis();
		long svcEndTime = 0;
		long svcStartTime = 0;
		try {
			if (requestInstance.getParameter("Organization_id") == null) {
				ErrorCodeEnum.ERR_21011.setErrorCode(result);
				return result;
			} else {
				String Organization_id = requestInstance.getParameter("Organization_id");
				svcStartTime = System.currentTimeMillis();
				JSONObject getCompanyApprovalMatrixresponse = DBPServices.getCompanyApprovalMatrix(Organization_id,
						requestInstance);
				svcEndTime = System.currentTimeMillis();
				if (getCompanyApprovalMatrixresponse == null
						|| !getCompanyApprovalMatrixresponse.has(FabricConstants.OPSTATUS)
						|| getCompanyApprovalMatrixresponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_21016.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}else if (getCompanyApprovalMatrixresponse.has("dbpErrMsg")) {
					result.addParam(new Param("errMsg", getCompanyApprovalMatrixresponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;

				}else {
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
					result.addParam(new Param("opstatus", getCompanyApprovalMatrixresponse.get("opstatus").toString(),
							FabricConstants.STRING));

					// Creating Dataset and adding to result
					JSONArray readResponseJSONArray = getCompanyApprovalMatrixresponse.optJSONArray("accounts");
					Dataset dataSet = new Dataset();
					dataSet.setId("accounts");
					for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
						JSONObject currJSONObject = readResponseJSONArray.optJSONObject(indexVar);
						Record currRecord = new Record();
						if (currJSONObject.length() != 0) {
							currRecord.addParam(new Param("accountId", currJSONObject.optString("accountId"),
									FabricConstants.STRING));
							currRecord.addParam(new Param("accountName", currJSONObject.optString("accountName"),
									FabricConstants.STRING));
							JSONArray limitTypes = currJSONObject.optJSONArray("limitTypes");
							Dataset limitTypesDataset = new Dataset("limitTypes");
							for (int l = 0; l < limitTypes.length(); l++) {
								if (limitTypes != null && limitTypes.length() > 0) {
									JSONObject limitType = limitTypes.optJSONObject(l);
									Record limitTypeRecord = new Record();
									if (limitType.length() != 0) {
										limitTypeRecord.addParam(new Param("limitTypeId",
												limitType.optString("limitTypeId"), FabricConstants.STRING));
										JSONArray actionsArray = limitType.optJSONArray("actions");
										Dataset actions = new Dataset("actions");
										for (int a = 0; a < actionsArray.length(); a++) {
											if (actionsArray != null && actionsArray.length() > 0) {
												JSONObject actionObject = actionsArray.optJSONObject(a);
												Record actionRecord = new Record();
												if (actionObject.length() != 0) {
													actionRecord.addParam(
															new Param("actionId", actionObject.optString("actionId"),
																	FabricConstants.STRING));
													actionRecord.addParam(new Param("actionDescription",
															actionObject.optString("actionDescription"),
															FabricConstants.STRING));
													actionRecord.addParam(
															new Param("featureId", actionObject.optString("featureId"),
																	FabricConstants.STRING));
													actionRecord.addParam(new Param("featureName",
															actionObject.optString("featureName"),
															FabricConstants.STRING));
													actionRecord.addParam(new Param("featureStatus",
															actionObject.optString("featureStatus"),
															FabricConstants.STRING));
													JSONArray limits = actionObject.optJSONArray("limits");
													if (limits != null && limits.length() > 0) {
														Dataset actionLimits = new Dataset("limits");
														for (int k = 0; k < limits.length(); k++) {
															JSONObject limit = limits.optJSONObject(k);
															Record limitRecord = new Record();
															if (limit.length() != 0) {
																limitRecord.addParam(new Param("numberOfApprovals",
																		limit.optString("numberOfApprovals"),
																		FabricConstants.STRING));
																limitRecord.addParam(new Param("approvalRuleId",
																		limit.optString("approvalRuleId"),
																		FabricConstants.STRING));
																limitRecord.addParam(new Param("approvalRuleName",
																		limit.optString("approvalRuleName"),
																		FabricConstants.STRING));
																limitRecord.addParam(new Param("lowerlimit",
																		limit.optString("lowerlimit"),
																		FabricConstants.STRING));
																limitRecord.addParam(new Param("upperlimit",
																		limit.optString("upperlimit"),
																		FabricConstants.STRING));
																JSONArray approversArray = limit
																		.optJSONArray("approvers");
																if (approversArray != null
																		&& approversArray.length() > 0) {
																	Dataset approvers = new Dataset("approvers");
																	for (int p = 0; p < approversArray.length(); p++) {
																		JSONObject approver = approversArray
																				.getJSONObject(p);
																		Record approverRecord = new Record();
																		if (approver.length() != 0) {
																			approverRecord
																					.addParam(new Param("approverId",
																							approver.optString(
																									"approverId"),
																							FabricConstants.STRING));
																			approverRecord
																					.addParam(new Param("approverName",
																							approver.optString(
																									"approverName"),
																							FabricConstants.STRING));
																			approvers.addRecord(approverRecord);
																		}
																	}
																	limitRecord.addDataset(approvers);
																}
																actionLimits.addRecord(limitRecord);
															}

														}
														actionRecord.addDataset(actionLimits);
													}
													actions.addRecord(actionRecord);
												}

											}
											limitTypeRecord.addDataset(actions);
										}

										limitTypesDataset.addRecord(limitTypeRecord);
									}

								}
								currRecord.addDataset(limitTypesDataset);
							}
							dataSet.addRecord(currRecord);
						}

					}
					result.addDataset(dataSet);

				}
			}

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in get Company approval matrix", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		long endTime = System.currentTimeMillis();
		diagnostic.prepareDebug("MF Time get approval matrix send rsp:" + (endTime - startTime) + "service time"
				+ (svcEndTime - svcStartTime)).log();

		return result;
	}
}