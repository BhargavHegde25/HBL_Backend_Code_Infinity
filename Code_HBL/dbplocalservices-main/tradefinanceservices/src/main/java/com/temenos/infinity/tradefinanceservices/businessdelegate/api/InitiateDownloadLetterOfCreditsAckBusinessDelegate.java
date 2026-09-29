/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public interface InitiateDownloadLetterOfCreditsAckBusinessDelegate extends BusinessDelegate {

    byte[] getRecordPDFAsBytes(LetterOfCreditsDTO importLcDTO, String customerId, DataControllerRequest request);


}
