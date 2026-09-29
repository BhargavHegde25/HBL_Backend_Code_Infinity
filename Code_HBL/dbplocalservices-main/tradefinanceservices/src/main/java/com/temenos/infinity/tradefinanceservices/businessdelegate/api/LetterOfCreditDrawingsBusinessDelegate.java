/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;

public interface LetterOfCreditDrawingsBusinessDelegate extends BusinessDelegate {
	DrawingsDTO getImportDrawingDetailsById(DataControllerRequest request, String srmsRequestOrderId)
			throws ApplicationException, com.kony.dbp.exception.ApplicationException;

	byte[] generateImportDrawingPdf(DrawingsDTO drawingDetails, DataControllerRequest request);
	/**
     * method to getImportDrawings
     * @param request
	 * @throws com.kony.dbp.exception.ApplicationException 
     */
    List<DrawingsDTO> getImportDrawings(DrawingsDTO drawingsDTO, DataControllerRequest request)
            throws ApplicationException, com.kony.dbp.exception.ApplicationException; 
			
	DrawingsDTO createDrawings(DrawingsDTO drawings, DataControllerRequest request) throws ApplicationException, com.kony.dbp.exception.ApplicationException;

	DrawingsDTO updateDrawings(DrawingsDTO drawings, DataControllerRequest request) throws ApplicationException, com.kony.dbp.exception.ApplicationException;

			
	
}