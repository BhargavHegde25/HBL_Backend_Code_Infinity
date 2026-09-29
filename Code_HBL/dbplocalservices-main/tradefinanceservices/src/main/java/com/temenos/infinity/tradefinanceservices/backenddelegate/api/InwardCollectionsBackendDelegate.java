/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.List;

public interface InwardCollectionsBackendDelegate extends BackendDelegate {

    InwardCollectionsDTO createInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request);

    List<InwardCollectionsDTO> getInwardCollections(DataControllerRequest request);

    InwardCollectionsDTO getInwardCollectionById(String collectionSrmsId, DataControllerRequest request);

    InwardCollectionsDTO updateInwardCollection(InwardCollectionsDTO amendmentDetails, DataControllerRequest request);

}
