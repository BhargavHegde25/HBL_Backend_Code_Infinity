package com.kony.adminconsole.campaign.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.kony.adminconsole.campaign.SegmentListUpdateStratergy;
import com.kony.adminconsole.campaign.SingleSegmentUpdateStratergy;
import com.kony.adminconsole.campaign.UpdateUsersForSegment;
import com.kony.adminconsole.campaign.businessdelegate.api.UpdateUsersForSegmentsBusinessDelegate;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ACConstants;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class UpdateUsersForSegmentBusinessDelegateImpl implements UpdateUsersForSegmentsBusinessDelegate {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String HIT_ANALYTICS_RESPONSE = "Anlaytics response is";
    private static final String FETCH_SEGMENTS_RESPONSE = "Fetch segments Response";

    @Override
    public boolean updateActiveUsersForAllSegments(String[] additionalParams) throws ApplicationException {
        return processUpdateSegment(new SegmentListUpdateStratergy(), additionalParams);
    }

    @Override
    public boolean getAndupdateActiveUsersForSegment(Segment segment, String[] additionalParams)
            throws ApplicationException {
        return processUpdateSegment(new SingleSegmentUpdateStratergy(segment), additionalParams);
    }

    private boolean processUpdateSegment(UpdateUsersForSegment userSegmentProcessor, String[] additionalParams)
            throws ApplicationException {
        Result res = userSegmentProcessor.getSegments(ACConstants.CAMPAIGN_MS_SERVICE_NAME,
                ACConstants.GET_ALL_SEGMENTS, new HashMap<>(), additionalParams);
        logSegmentResultResponse(res, FETCH_SEGMENTS_RESPONSE);
        List<Segment> segmentList = userSegmentProcessor.processSegments(res);
        logSegmentList(segmentList);
        if (res.getParamValueByName(ACConstants.DBP_ERROR_MESSAGE) == null && segmentList != null
                && !segmentList.isEmpty()) {
            Result analyticsRes = userSegmentProcessor.invokeAnalyticsService(segmentList, additionalParams);
            logSegmentResultResponse(analyticsRes, HIT_ANALYTICS_RESPONSE);
            JsonArray segmentUserInput = userSegmentProcessor.processAnalyticsResponse(segmentList, analyticsRes);
            logUpdateProfileInput(segmentUserInput);
            return userSegmentProcessor.invokeUpdateUsersForSegments(segmentUserInput, additionalParams);
        }
        return false;
    }

    private static void logSegmentList(List<Segment> list) {
        if (diagnostic.isDebugEnabled()) {
            diagnostic.prepareDebug("SegmentList is " + list).log();
        }
    }

    private static void logUpdateProfileInput(JsonArray jsonarr) {
        if (diagnostic.isDebugEnabled()) {
            diagnostic.prepareDebug("input json array is" + new Gson().toJson(jsonarr)).log();
        }
    }

    private static void logSegmentResultResponse(Result res, String msg) {
        if (diagnostic.isDebugEnabled()) {
            if (res != null) {
                diagnostic.prepareDebug(msg + ResultToJSON.convert(res)).log();
            } else {
                diagnostic.prepareDebug(msg + "returned Null").log();
            }
        }
    }

}
