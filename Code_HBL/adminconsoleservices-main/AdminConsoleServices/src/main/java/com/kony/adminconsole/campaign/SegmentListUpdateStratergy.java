package com.kony.adminconsole.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.dto.campaign.DataContext;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;

public class SegmentListUpdateStratergy implements UpdateUsersForSegment {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Result getSegments(String serviceName, String operationName, @SuppressWarnings("rawtypes") Map inputMap,
            String[] additonalParams) throws ApplicationException {
        try {
            return CampaignUtil.invokeService(ACConstants.CAMPAIGN_MS_SERVICE_NAME, ACConstants.GET_ALL_SEGMENTS, null);
        } catch (MiddlewareException e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_21805, e);
        }
    }

    @Override
    public List<Segment> processSegments(Result segmentResult) throws ApplicationException {
        List<Segment> segmentList = new ArrayList<>();
        try {
            String errmsg = chkAndGetErrorMsgForGetAllSegments(segmentResult);
            if (StringUtils.isBlank(errmsg)
                    && segmentResult.getDatasetById(ACConstants.GET_ALL_SEGEMENT_DATASET) != null) {
                segmentResult.getDatasetById(ACConstants.GET_ALL_SEGEMENT_DATASET).getAllRecords()
                        .forEach(getSegmentList(segmentList));

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_21799, new Throwable(
                        "getSegments returned empty response,AuthPreProcessor might have returned false"));
            }
        } catch (Exception e) {
            alert.prepareError(ErrorCodeEnum.ERR_21799.getMessage(), e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21799, e);
        }
        return segmentList;
    }

    @Override
    public Result invokeAnalyticsService(List<Segment> segmentList, String[] additonalParams)
            throws ApplicationException {
        Result res = null;
        try {
            List<String> endpointURLlist = new ArrayList<>();
            List<String> filterList = new ArrayList<>();
            List<String> campaignIdList = new ArrayList<>();
            List<String> dataContextIdList = new ArrayList<>();
            prepareLoopParams(segmentList, endpointURLlist, filterList, campaignIdList, dataContextIdList);
            res = invokeAnalyticsService(endpointURLlist, filterList, campaignIdList, dataContextIdList);
        } catch (AppRegistryException e) {
            alert.prepareError("Not able to find the Analytics app ", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21800, e);
        } catch (Exception e) {
            alert.prepareError(ErrorCodeEnum.ERR_21800.getMessage(), e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21800, e);
        }
        return res;
    }

    @Override
    public JsonArray processAnalyticsResponse(List<Segment> segmentList, Result analyticSegmentResult)
            throws ApplicationException {
        JsonArray profJsonArr = new JsonArray();
        try {
            @SuppressWarnings("rawtypes")
            Map<Object, Optional<List>> segmentUsersMap = analyticSegmentResult.getDatasetById(ACConstants.LOOP_DATASET)
                    .getAllRecords().stream().filter(r1 -> r1.getParamByName("Customers") != null).collect(
                            Collectors.groupingBy(r2 -> r2.getParamByName(ACConstants.LOOP_PARAM_SEGMENT_ID).getValue(),
                                    Collectors.mapping(funcgetActiveCustomersAsList,
                                            Collectors.reducing(ListUtils::intersection))));

            for (Segment segment : segmentList) {
                if (segmentUsersMap.containsKey(segment.getSegmentId())
                        && segmentUsersMap.get(segment.getSegmentId()).isPresent()) {
                    JsonObject profileObj = new JsonObject();
                    profileObj.addProperty(ACConstants.PROFILE_ID, segment.getSegmentId());
                    int noOfUsersForSegment = segmentUsersMap.get(segment.getSegmentId()).get().size();
                    segment.setNumberOfUsers(noOfUsersForSegment);
                    profileObj.addProperty(ACConstants.NUMBER_OF_USERS, noOfUsersForSegment);
                    profJsonArr.add(profileObj);
                } else {
                    alert.prepareError("Activeusers not found for " + segment.getSegmentId()).log();
                }
            }
        } catch (Exception e) {
            alert.prepareError(ErrorCodeEnum.ERR_21800.getMessage(), e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21800, e);
        }
        return profJsonArr;
    }

    @Override
    public boolean invokeUpdateUsersForSegments(JsonArray segmentUpdateObj, String[] additonalParams)
            throws ApplicationException {
        boolean success = false;
        try {
            Result res = CampaignUtil.invokeServiceUpdateUserForSegments(segmentUpdateObj);
            if (Integer.valueOf(res.getParamByName(ACConstants.OPSTATUS).getValue()) == 0) {
                success = true;
            } else {
                alert.prepareError("UpdateUsers failed " + ErrorCodeEnum.ERR_21801.getMessage()
                        + res.getParamValueByName(ACConstants.ERRMSG) + res.getParamValueByName(ACConstants.ERRCODE)).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_21801,
                        new Throwable(res.getParamValueByName(ACConstants.ERRMSG)));
            }
        } catch (MiddlewareException e) {
            alert.prepareError(ErrorCodeEnum.ERR_21801.getMessage(), e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21801, e);
        }
        return success;
    }

    public Result invokeServiceUpdateUserForSegments(JsonArray segmentUpdateObj) throws MiddlewareException {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put(ACConstants.PROFILES, segmentUpdateObj);
        return CampaignUtil.invokeService(ACConstants.CAMPAIGN_MS_SERVICE_NAME, ACConstants.UPDATE_USERS_FOR_SEGMENT,
                inputMap);
    }

    public static final Function<Record, List<String>> funcgetActiveCustomersAsList = (Record rec) -> Stream
            .of(rec.getParamByName("Customers").getValue().split(",", -1)).collect(Collectors.toList());

    private void prepareLoopParams(List<Segment> segmentList, List<String> endpointURLlist, List<String> filterList,
            List<String> segmentIdList, List<String> dataContextIdList) {
        for (Segment segment : segmentList) {
            for (DataContext dc : segment.getDcList()) {
                endpointURLlist.add(dc.getEndPointURL());
                filterList.add(dc.getConditionExpression());
                segmentIdList.add(segment.getSegmentId());
                dataContextIdList.add(dc.getDataContextId());
            }
        }
    }

    private Result invokeAnalyticsService(List<String> endpointURLlist, List<String> filterList,
            List<String> campaignNameList, List<String> dataContextIdList) throws MiddlewareException {
        Map<String, Object> inputMapdc = new HashMap<>();
        inputMapdc.put(ACConstants.LOOP_COUNT, endpointURLlist.size());
        inputMapdc.put(ACConstants.LOOP_SEPERATOR, ",");
        inputMapdc.put(ACConstants.LOOP_PARAM_ENDPOINT_URL, endpointURLlist.stream().collect(Collectors.joining(",")));
        inputMapdc.put(ACConstants.LOOP_PARAM_FILTER, filterList.stream().collect(Collectors.joining(",")));
        inputMapdc.put(ACConstants.LOOP_PARAM_SEGMENT_ID,
                campaignNameList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        inputMapdc.put(ACConstants.LOOP_PARAM_DATACONTEXT_ID,
                dataContextIdList.stream().collect(Collectors.joining(",")));
        return CampaignUtil.invokeService(ACConstants.LOOPING_SERVICE_NAME, ACConstants.LOOPING_OPERATION_NAME,
                inputMapdc);
    }

    private Consumer<? super Record> getSegmentList(List<Segment> segmentList) {
        return record -> {
            Segment segment = new Segment(record.getParamByName(ACConstants.PROFILE_ID).getValue());
            Dataset profileConditionsds = record.getDatasetById(ACConstants.PROFILE_CONDITIONS);
            List<DataContext> dclist = new ArrayList<>();
            if(profileConditionsds != null) {
            	profileConditionsds.getAllRecords().forEach(dcrecord -> {
            		DataContext dc = new DataContext(dcrecord.getParamByName(ACConstants.PROFILE_CONDITION_ID).getValue(),
            				dcrecord.getParamByName(ACConstants.DATA_CONTEXT_ID).getValue(),
            				dcrecord.getParamByName(ACConstants.DATA_CONTEXT_ENDPOINTS).getValue(),
            				dcrecord.getParamByName(ACConstants.CONDITION_EXPRESSION).getValue());
            		dclist.add(dc);
            	});
            }
            segment.setDcList(dclist);
            segmentList.add(segment);
        };
    }

    private String chkAndGetErrorMsgForGetAllSegments(Result segmentResult) {
        StringBuilder s = new StringBuilder();
        getErrmsgForGetAllSegments(segmentResult, s);
        if (StringUtils.isNotBlank(s.toString())) {
            alert.prepareError(s.toString()).log();
        }
        return s.toString();
    }

    private void getErrmsgForGetAllSegments(Result segmentResult, StringBuilder s) {
        if (segmentResult.getParamValueByName(ACConstants.ERRCODE) != null) {
            alert.prepareError("getProfiles service has returned errcode "
                    + segmentResult.getParamValueByName(ACConstants.ERRCODE)).log();

            if (segmentResult.getParamValueByName(ACConstants.ERRMSG) != null) {
                s.append(ACConstants.GET_ALL_SEGMENTS + " failed with error "
                        + segmentResult.getParamValueByName(ACConstants.ERRMSG));
                alert.prepareError(ACConstants.GET_ALL_SEGMENTS + " service has returned errmsg "
                        + segmentResult.getParamValueByName(ACConstants.ERRMSG)).log();
            }

        }
    }

}
