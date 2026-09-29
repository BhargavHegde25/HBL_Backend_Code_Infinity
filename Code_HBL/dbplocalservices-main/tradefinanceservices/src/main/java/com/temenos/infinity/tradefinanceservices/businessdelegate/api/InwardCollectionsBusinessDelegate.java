/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;

import java.util.List;

public interface InwardCollectionsBusinessDelegate extends BusinessDelegate {

    InwardCollectionsDTO createInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request);

    List<InwardCollectionsDTO> getInwardCollections(DataControllerRequest request);

    InwardCollectionsDTO getInwardCollectionById(String collectionSrmsId, DataControllerRequest request);

    InwardCollectionsDTO updateInwardCollection(InwardCollectionsDTO amendmentDetails, DataControllerRequest request);

    byte[] generateInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request);
}
