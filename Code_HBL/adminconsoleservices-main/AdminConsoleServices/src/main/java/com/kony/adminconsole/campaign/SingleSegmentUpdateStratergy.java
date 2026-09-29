package com.kony.adminconsole.campaign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;

public class SingleSegmentUpdateStratergy extends SegmentListUpdateStratergy {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
    private Segment segment;

    public SingleSegmentUpdateStratergy(Segment segment) {
        super();
        this.segment = segment;
    }

    @Override
    public Result getSegments(String serviceName, String operationName, @SuppressWarnings("rawtypes") Map inputMap,
            String[] additonalParams) throws ApplicationException {
        return new Result();
    }

    @Override
    public List<Segment> processSegments(Result segmentResult) throws ApplicationException {
        List<Segment> segList = new ArrayList<>();
        segList.add(segment);
        return segList;
    }

    @Override
    public boolean invokeUpdateUsersForSegments(JsonArray segmentUpdateObj, String[] additonalParams)
            throws ApplicationException {
        Callable<Boolean> addCustomersToCampaignGroupsCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return Boolean.valueOf(CampaignUtil.invokeUpdateUsersForSegment(segmentUpdateObj));
            }
        };
        try {
            ThreadExecutor.execute(addCustomersToCampaignGroupsCallable);
        } catch (InterruptedException e) {
            alert.prepareError("invokeUpdateUsersForSegments throw error ", e).log();
            Thread.currentThread().interrupt();
        }
        return true;
    }

}
