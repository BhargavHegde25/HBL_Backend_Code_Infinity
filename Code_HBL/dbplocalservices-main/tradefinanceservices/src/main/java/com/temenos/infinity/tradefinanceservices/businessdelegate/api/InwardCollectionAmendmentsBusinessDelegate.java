/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.List;

public interface InwardCollectionAmendmentsBusinessDelegate extends BusinessDelegate, ExcelBusinessDelegate {

    InwardCollectionAmendmentsDTO createInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDTO, DataControllerRequest request);

    List<InwardCollectionAmendmentsDTO> getInwardCollectionAmendments(DataControllerRequest request);

    InwardCollectionAmendmentsDTO getInwardCollectionAmendmentById(String amendmentSrmsId, DataControllerRequest request);

    InwardCollectionAmendmentsDTO updateInwardCollectionAmendment(InwardCollectionAmendmentsDTO amendmentDetails, DataControllerRequest request);

    byte[] generateInwardCollectionAmendment(InwardCollectionAmendmentsDTO amendmentDTO, InwardCollectionsDTO collectionDTO, DataControllerRequest request);
}
