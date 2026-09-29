/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import java.io.IOException;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;

/**
 * @author k.meiyazhagan
 */
public interface OutwardCollectionsResource extends Resource {
    Result createCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request);

    Result getCollections(FilterDTO filterDto, DataControllerRequest request);

    Result getCollectionById(DataControllerRequest request);

	Result saveCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request);

	Result updateCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) throws IOException;
	
	Result updateCollectionByBank(OutwardCollectionsDTO inputDto, DataControllerRequest request);
	
	Result deleteCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request);
	
	Result requestCollectionStatus(OutwardCollectionsDTO inputDto, DataControllerRequest request);
}
