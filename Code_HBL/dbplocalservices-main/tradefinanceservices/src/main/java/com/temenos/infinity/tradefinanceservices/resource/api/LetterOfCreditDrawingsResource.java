/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;

public interface LetterOfCreditDrawingsResource  extends Resource {
	Result getImportDrawingDetailsById(Object[] inputArray,DataControllerRequest request);
	Result generateImportDrawingPdf(DataControllerRequest request);
	Result getImportDrawings(Object[] inputArray,DrawingsDTO drawingsDTO, DataControllerRequest request);
	Result createLetterOfCreditDrawings(DrawingsDTO drawings, DataControllerRequest request);
	Result submitLetterOfCreditDrawings(DrawingsDTO drawings, DataControllerRequest request) throws ApplicationException;
	Result updateImportLCDrawingByBank(DrawingsDTO drawings, DataControllerRequest request);
}
