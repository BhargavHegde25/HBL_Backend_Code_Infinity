/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorFundingRequestBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorFundingRequestResource;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.List;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.PARAM_STATUS_DRAFT;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.PARAM_STATUS_SUBMITTED;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.PARAM_STATUS_RESUBMITTED;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.PARAM_STATUS_DELETED;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceStatus.PARAM_STATUS_CANCELLED;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getCurrentDateTimeUTF;

/**
 * @author k.meiyazhagan
 */
public class AnchorFundingRequestResourceImpl implements AnchorFundingRequestResource {

    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result saveAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
        AnchorFundingRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorFundingRequestBusinessDelegate.class);
        inputDto.setUpdatedDate(getCurrentDateTimeUTF());
        

        AnchorFundingRequestDTO responseDTO;
        if (StringUtils.isBlank(inputDto.getFundingRequestId())) {
            inputDto.setCreatedDate(inputDto.getUpdatedDate());
            inputDto.setStatus(PARAM_STATUS_DRAFT);
            responseDTO = requestBusiness.createAnchorFundingRequest(inputDto, request);
        } else {
            responseDTO = requestBusiness.getAnchorFundingRequestById(inputDto.getFundingRequestId(), request);
            if (StringUtils.isBlank(responseDTO.getFundingRequestId())) {
                return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
            }
            inputDto.setCreatedDate(responseDTO.getCreatedDate());
            inputDto.setStatus(responseDTO.getStatus());
            responseDTO = requestBusiness.updateAnchorFundingRequest(inputDto, request);
        }

        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    @Override
    public Result submitAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
        AnchorFundingRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorFundingRequestBusinessDelegate.class);
        AnchorFundingRequestDTO responseDTO;
        if (StringUtils.isBlank(inputDto.getFundingRequestId())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        responseDTO = requestBusiness.getAnchorFundingRequestById(inputDto.getFundingRequestId(), request);
        if (StringUtils.isBlank(responseDTO.getFundingRequestId())) {
            return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
        }
        if (StringUtils.isBlank(responseDTO.getFundingRequestId()) ||
                StringUtils.isBlank(responseDTO.getFacilityId()) ||
                StringUtils.isBlank(responseDTO.getProductName()) ||
                StringUtils.isBlank(responseDTO.getProgramName()) ||
                StringUtils.isBlank(responseDTO.getFacilityAvailableLimit()) ||
                StringUtils.isBlank(responseDTO.getFacilityUtilisedLimit()) ||
                StringUtils.isBlank(responseDTO.getCurrency()) ||
                StringUtils.isBlank(responseDTO.getFacilityCurrency()) ||
                StringUtils.isBlank(responseDTO.getFundingRequestAmount()) ||
                StringUtils.isBlank(responseDTO.getInvoiceReferences())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }
        inputDto.setUpdatedDate(getCurrentDateTimeUTF());
        
        if (StringUtils.equals(responseDTO.getStatus(), PARAM_STATUS_DRAFT)) {
        	responseDTO.setStatus(PARAM_STATUS_SUBMITTED);
        } else {
        	responseDTO.setStatus(PARAM_STATUS_RESUBMITTED);
        }
        responseDTO = requestBusiness.updateAnchorFundingRequest(responseDTO, request);

        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
    }

    @Override
    public Result getAllFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
        AnchorFundingRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorFundingRequestBusinessDelegate.class);
        List<AnchorFundingRequestDTO> fundingRequests = requestBusiness.getAllFundingRequest(request);
        return JSONToResult.convert((new JSONObject()).put("FundingRequests", fundingRequests).toString());
    }

	@Override
	public Result cancelAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
		AnchorFundingRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorFundingRequestBusinessDelegate.class);
        AnchorFundingRequestDTO responseDTO;
        if (StringUtils.isBlank(inputDto.getFundingRequestId())) {
            return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
        }

        responseDTO = requestBusiness.getAnchorFundingRequestById(inputDto.getFundingRequestId(), request);
        if (StringUtils.isBlank(responseDTO.getFundingRequestId())) {
            return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
        }
        
        inputDto.setUpdatedDate(getCurrentDateTimeUTF());
        if (StringUtils.equals(responseDTO.getStatus(), PARAM_STATUS_DRAFT)) {
        	responseDTO.setStatus(PARAM_STATUS_DELETED);
        } else {
        	responseDTO.setStatus(PARAM_STATUS_CANCELLED);
        }
        responseDTO = requestBusiness.updateAnchorFundingRequest(responseDTO, request);

        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
	}

	@Override
	public Result saveAnchorFundingRequestMock(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
		AnchorFundingRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorFundingRequestBusinessDelegate.class);        
		inputDto.setUpdatedDate(getCurrentDateTimeUTF());
		
        AnchorFundingRequestDTO responseDTO;
        if (StringUtils.isBlank(inputDto.getFundingRequestId())) {
        	inputDto.setCreatedDate(inputDto.getUpdatedDate());
            responseDTO = requestBusiness.createAnchorFundingRequest(inputDto, request);
        } else {
            responseDTO = requestBusiness.getAnchorFundingRequestById(inputDto.getFundingRequestId(), request);
            if (StringUtils.isBlank(responseDTO.getFundingRequestId())) {
                return ErrorCodeEnum.ERR_30008.setErrorCode(new Result());
            }
            inputDto.setCreatedDate(responseDTO.getCreatedDate());
            responseDTO = requestBusiness.updateAnchorFundingRequest(inputDto, request);
        }

        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
	}

}