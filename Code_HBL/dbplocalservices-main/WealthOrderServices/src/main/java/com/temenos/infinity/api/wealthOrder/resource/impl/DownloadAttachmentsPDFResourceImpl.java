/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.Arrays;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DownloadAttachmentsPDFBusinessDelegate;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthOrder.resource.api.DownloadAttachmentsPDFResource;

/**
 * @author himaja.sridhar
 *
 */
public class DownloadAttachmentsPDFResourceImpl implements DownloadAttachmentsPDFResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result generatePDF(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		String DownloadResult = "";
		Map<String, Object> headersMap = request.getHeaderMap();
		String[] navVal = { "Watchlist", "InstrumentTransactions" };
		String[] sortVal = { "Watchlist", "InstrumentTransactions" };
		if (!request.containsKeyInRequest(TemenosConstants.DOWNLOADFORMAT)
				|| (!request.getParameter(TemenosConstants.DOWNLOADFORMAT).equalsIgnoreCase("csv")
						&& !request.getParameter(TemenosConstants.DOWNLOADFORMAT).equalsIgnoreCase("xlsx")
						&& !request.getParameter(TemenosConstants.DOWNLOADFORMAT).equalsIgnoreCase("pdf"))) {
			return PortfolioWealthUtils.validateValue(TemenosConstants.DOWNLOADFORMAT);
		}
		if (request.getParameter(TemenosConstants.PORTFOLIOID) == null
				|| (request.getParameter(TemenosConstants.PORTFOLIOID).equals(""))) {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}
		if (!request.containsKeyInRequest(TemenosConstants.NAVPAGE)
				|| !(Arrays.stream(navVal).anyMatch(request.getParameter(TemenosConstants.NAVPAGE)::equals))
				|| request.getParameter(TemenosConstants.NAVPAGE) == null) {
			return PortfolioWealthUtils.validateValue(TemenosConstants.NAVPAGE);
		}
		if (!request.containsKeyInRequest(TemenosConstants.SORTBY)
				|| Arrays.stream(sortVal).anyMatch(request.getParameter(TemenosConstants.NAVPAGE)::equals)
						&& request.getParameter(TemenosConstants.SORTBY).toString() == null
				|| request.getParameter(TemenosConstants.SORTBY).toString().equals("")) {
			return PortfolioWealthUtils.validateParams(TemenosConstants.SORTBY);
		}
		if (request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("InstrumentTransactions")) {
			String dateformat = "YYYY-MM-dd";
			if (request.containsKeyInRequest(TemenosConstants.STARTDATE)
					&& (request.getParameter(TemenosConstants.STARTDATE).toString() != null
							|| !request.getParameter(TemenosConstants.STARTDATE).toString().equals(""))) {
				String startDate = request.getParameter(TemenosConstants.STARTDATE).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.STARTDATE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.STARTDATE);
			}
			if (request.containsKeyInRequest(TemenosConstants.ENDDATE)
					&& (request.getParameter(TemenosConstants.ENDDATE).toString() != null
							|| !request.getParameter(TemenosConstants.ENDDATE).toString().equals(""))) {
				String startDate = request.getParameter(TemenosConstants.ENDDATE).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.ENDDATE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ENDDATE);
			}
		} 
			try {
				DownloadAttachmentsPDFBusinessDelegate downloadAttachmentsPDFBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(DownloadAttachmentsPDFBusinessDelegate.class);
				DownloadResult = downloadAttachmentsPDFBusinessDelegate.generatePDF(methodID, inputArray, request,
						response, headersMap);
				if (DownloadResult.equalsIgnoreCase("UG9ydGZvbGlvIElEICBkb2VzIG5vdCBleGlzdCBmb3IgdGhlIEN1c3RvbWVy")) {
					return PortfolioWealthUtils.UnauthorizedAccess();
				} else {
					result.addParam("base64", DownloadResult);
					result.addParam("opstatus", "0");
					result.addParam("httpStatusCode", "200");
					diagnostic.prepareDebug("Document Retrieved Successfully").log();
					return result;
				}
			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
				diagnostic.prepareDebug("Document Retrieve Failed").log();
				return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
			}
	}
}
