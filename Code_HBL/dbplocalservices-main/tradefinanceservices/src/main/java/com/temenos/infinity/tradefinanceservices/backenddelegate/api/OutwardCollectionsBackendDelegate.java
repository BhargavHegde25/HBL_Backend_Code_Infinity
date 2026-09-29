/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public interface OutwardCollectionsBackendDelegate extends BackendDelegate {
    OutwardCollectionsDTO createCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request);

    List<OutwardCollectionsDTO> getCollections(DataControllerRequest request);

    OutwardCollectionsDTO getCollectionById(String collectionReference, DataControllerRequest request);

    OutwardCollectionsDTO updateCollection(OutwardCollectionsDTO collectionDto, DataControllerRequest request);
}
