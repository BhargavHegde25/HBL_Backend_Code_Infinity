package com.kony.adminconsole.campaign.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.kony.adminconsole.campaign.businessdelegate.api.UpdateUsersForSegmentsBusinessDelegate;
import com.kony.adminconsole.campaign.resource.UpdateUsersForSegmentsResource;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.dto.campaign.DataContext;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateUsersForSegmentsResourceImpl implements UpdateUsersForSegmentsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Result updateActiveCountForAllSegments(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        return performUpdateUsersForSegment(request, false, null);
    }

    private Result performUpdateUsersForSegment(DataControllerRequest dcrequest, boolean isSegmentGiven,
            Segment segment) {
        Result res = new Result();
        try {
            UpdateUsersForSegmentsBusinessDelegate segmentBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                            .getBusinessDelegate(UpdateUsersForSegmentsBusinessDelegate.class);
            boolean finalres = false;
            if (!isSegmentGiven) {
                finalres = segmentBusinessDelegate.updateActiveUsersForAllSegments(null);
            } else {
                finalres = segmentBusinessDelegate.getAndupdateActiveUsersForSegment(segment, null);
            }
            res.addParam(ACConstants.SUCCESS, String.valueOf(finalres));
        } catch (ApplicationException e) {
            res.addParam(ACConstants.SUCCESS, String.valueOf(false));
            alert.prepareError("Active count update forall segments failed with Error ", e).log();
            e.getErrorCodeEnum().setErrorCode(res);
        } catch (Exception e) {
            alert.prepareError("Active count update forall segments failed with Error ", e).log();
            ErrorCodeEnum.ERR_21802.setErrorCode(res);
            res.addParam(ACConstants.SUCCESS, String.valueOf(false));
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result getAndupdateActiveUsersForSegment(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
        Segment segment = null;
        if (StringUtils.isBlank(inputParams.get(ACConstants.SEGMENT))) {
            return ErrorCodeEnum.ERR_21803.setErrorCode(null);
        } else {
            try {
                JsonObject segmentJsonObject =
                        new JsonParser().parse(inputParams.get(ACConstants.SEGMENT)).getAsJsonObject();
                segment = new Segment(segmentJsonObject.get(ACConstants.PROFILE_ID).getAsString());
                JsonArray profileArr = segmentJsonObject.get(ACConstants.PROFILE_CONDITIONSS).getAsJsonArray();
                List<DataContext> dcList = new ArrayList<>();
				for (JsonElement profileConditionJE : profileArr) {
					JsonObject profCond = profileConditionJE.getAsJsonObject();
					String dataContextId = profCond.get(ACConstants.DATA_CONTEXT_ID).getAsString();
					String profileConditionId = profCond.get(ACConstants.PROFILE_CONDITION_ID).getAsString();
					String conditionExpression = profCond.get(ACConstants.CONDITION_EXPRESSION).getAsString();
					String endPointURL = profCond.get(ACConstants.DATA_CONTEXT_END_POINTS).getAsString();
					DataContext dc = new DataContext(profileConditionId, dataContextId, endPointURL,
							conditionExpression);
					dcList.add(dc);
				}
                if (!dcList.isEmpty()) {
                    segment.setDcList(dcList);
                }
                Result res = performUpdateUsersForSegment(request, true, segment);
                res.addParam(ACConstants.USERCOUNT, String.valueOf(segment.getNumberOfUsers()), Param.NUMBER_CONST);
                return res;
            } catch (JsonSyntaxException e) {
                alert.prepareError("Preparing input segment failed ", e).log();
                Result res = new Result();
                CampaignUtil.addDBPErrCodeAndmsg(res, ErrorCodeEnum.ERR_21803.getMessage() + e.getMessage(),
                        ErrorCodeEnum.ERR_21803.getErrorCode());

                return res;

            }

        }
    }

}
