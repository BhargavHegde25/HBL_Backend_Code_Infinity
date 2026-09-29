package com.kony.adminconsole.campaign;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;

public interface UpdateUsersForSegment {

    Result getSegments(String serviceName, String operationName, @SuppressWarnings("rawtypes") Map inputMap,
            String[] additonalParams) throws ApplicationException;

    List<Segment> processSegments(Result segmentResult) throws ApplicationException;

    Result invokeAnalyticsService(List<Segment> segmentList, String[] additonalParams) throws ApplicationException;

    JsonArray processAnalyticsResponse(List<Segment> segmentList, Result segmentResult) throws ApplicationException;

    boolean invokeUpdateUsersForSegments(JsonArray segmentUpdateObj, String[] additonalParams)
            throws ApplicationException;

}
