package com.kony.eum.AdminConsole.BLProcessor;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetGroups implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = null;
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Map<String, String> userDetails = HelperMethods.getCustomerFromIdentityService(dcRequest);
		String typeId = inputParams.get("typeId");
		StringBuilder filterQuery = new StringBuilder();
		String businessType = userDetails.get("organizationType");

		try {
			if (StringUtils.isNotBlank(typeId)) {

				if (StringUtils.isNotBlank(businessType)) {
					filterQuery.append("BusinessType_id").append(DBPUtilitiesConstants.EQUAL).append(businessType);
					result = HelperMethods.callGetApi(dcRequest, filterQuery.toString(),
							HelperMethods.getHeaders(dcRequest), URLConstants.GROUPBUSINESSTYPE_GET);
					filterQuery = new StringBuilder();
					List<Record> groups = result.getDatasetById("groupbusinesstype").getAllRecords();
					filterQuery.append("Type_id").append(DBPUtilitiesConstants.EQUAL).append(typeId)
							.append(DBPUtilitiesConstants.AND).append("Status_id").append(DBPUtilitiesConstants.EQUAL)
							.append("SID_ACTIVE");
					boolean isStart = true;
					for (int i = 0; i < groups.size(); i++) {
						if (isStart) {
							filterQuery.append(DBPUtilitiesConstants.AND).append("(");
							isStart = false;
						}
						filterQuery.append("id").append(DBPUtilitiesConstants.EQUAL)
								.append(HelperMethods.getFieldValue(groups.get(i), "Group_id"));
						if (i + 1 != groups.size())
							filterQuery.append(DBPUtilitiesConstants.OR);
					}
					if (!isStart) {
						filterQuery.append(")");
					}
				} else {
					filterQuery.append("Type_id").append(DBPUtilitiesConstants.EQUAL).append(typeId)
							.append(DBPUtilitiesConstants.AND).append("Status_id").append(DBPUtilitiesConstants.EQUAL)
							.append("SID_ACTIVE");
				}
				result = HelperMethods.callGetApi(dcRequest, filterQuery.toString(),
						HelperMethods.getHeaders(dcRequest), URLConstants.MEMBERGROUP_READ);
			} else {

				result = HelperMethods.callGetApi(dcRequest, filterQuery.toString(),
						HelperMethods.getHeaders(dcRequest), URLConstants.GROUPSVIEW_READ);

			}
			if (HelperMethods.hasRecords(result)) {
				result.getAllDatasets().get(0).setId("GroupRecords");
				return result;
			}
			alert.prepareError("Fetch from both the tables(groups_view & membergroup failed").log();
			ErrorCodeEnum.ERR_10702.setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Exception occured GroupsGetService : " + e.getMessage()).log();
		}
		return result;
	}
}
