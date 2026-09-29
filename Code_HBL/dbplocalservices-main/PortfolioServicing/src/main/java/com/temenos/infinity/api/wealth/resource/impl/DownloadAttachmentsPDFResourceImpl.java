/**
 * 
 */
package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Arrays;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.DownloadAttachmentsPDFBusinessDelegate;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.resource.api.DownloadAttachmentsPDFResource;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

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
		String[] navVal = { "Holdings", "Transactions", "Accounts Activity", "Performance", "Open Order",
				"History Order" };
		String[] sortVal = { "Holdings", "Transactions", "Accounts Activity", "Open Order", "History Order" };
		if (!request.containsKeyInRequest(TemenosConstants.DOWNLOADFORMAT) || (!request.getParameter(TemenosConstants.DOWNLOADFORMAT).equalsIgnoreCase("csv")
				&& !request.getParameter(TemenosConstants.DOWNLOADFORMAT).equalsIgnoreCase("xlsx")
				&& !request.getParameter(TemenosConstants.DOWNLOADFORMAT).equalsIgnoreCase("pdf"))
				) {
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
		
		if (request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("Holdings")) {
			String includeOrdersObj = request.getParameter(TemenosConstants.ISINCLUEORDERS);
			if (includeOrdersObj == null || includeOrdersObj.trim().equals("")) {
				request.addRequestParam_(TemenosConstants.ISINCLUEORDERS, "false");
				//return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ISINCLUEORDERS);
			} 
			else {
				if (!includeOrdersObj.equalsIgnoreCase("true")) {
					request.addRequestParam_(TemenosConstants.ISINCLUEORDERS, "false");
					//return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ISINCLUEORDERS);
				}
			}
		}
		
		if (request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("Transactions")) {
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
		if (request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("Accounts Activity")) {
			String dateformat = "YYYYMMdd";
			if (!request.containsKeyInRequest(TemenosConstants.ACCID)
					|| request.getParameter(TemenosConstants.ACCID).toString() == null
					|| request.getParameter(TemenosConstants.ACCID).toString().equals("")) {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ACCID);
			}
			if (request.containsKeyInRequest(TemenosConstants.DATEFROM)
					&& (request.getParameter(TemenosConstants.DATEFROM).toString() != null
							|| !request.getParameter(TemenosConstants.DATEFROM).toString().equals(""))) {
				String startDate = request.getParameter(TemenosConstants.DATEFROM).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATEFROM);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATEFROM);
			}
			if (request.containsKeyInRequest(TemenosConstants.DATETO)
					&& (request.getParameter(TemenosConstants.DATETO).toString() != null
							|| !request.getParameter(TemenosConstants.DATETO).toString().equals(""))) {
				String startDate = request.getParameter(TemenosConstants.DATETO).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATETO);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATETO);
			}
			if (!request.containsKeyInRequest(TemenosConstants.LISTTYPE)
					|| request.getParameter(TemenosConstants.LISTTYPE).toString() == null
					|| request.getParameter(TemenosConstants.LISTTYPE).toString().equals("")) {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LISTTYPE);
			}
		}
		if (request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("Performance")) {
			String dateformat = "YYYYMMdd";
			if (request.containsKeyInRequest(TemenosConstants.DATEFROM)
					&& (request.getParameter(TemenosConstants.DATEFROM).toString() != null
							|| !request.getParameter(TemenosConstants.DATEFROM).toString().equals(""))) {
				String startDate = request.getParameter(TemenosConstants.DATEFROM).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATEFROM);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATEFROM);
			}
			if (request.containsKeyInRequest(TemenosConstants.DATETO)
					&& (request.getParameter(TemenosConstants.DATETO).toString() != null
							|| !request.getParameter(TemenosConstants.DATETO).toString().equals(""))) {
				String startDate = request.getParameter(TemenosConstants.DATETO).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATETO);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATETO);
			}
			if (!request.containsKeyInRequest(TemenosConstants.DURATION)
					|| request.getParameter(TemenosConstants.DURATION).toString() == null
					|| request.getParameter(TemenosConstants.DURATION).toString().equals("")) {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DURATION);
			}
		}
		if (request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("History Order")
				|| request.getParameter(TemenosConstants.NAVPAGE).toString().equalsIgnoreCase("Open Order")) {
			if (!request.containsKeyInRequest(TemenosConstants.ORDERSVIEW_TYPE)
					|| request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).toString() == null
					|| request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).toString().equals("")) {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERSVIEW_TYPE);
			} else {
				if (request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).toString().trim().equalsIgnoreCase("OPEN")
						|| request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).toString().trim()
								.equalsIgnoreCase("HISTORY")) {
					String dateformat = "YYYY-MM-dd";
					if (request.containsKeyInRequest(TemenosConstants.STARTDATE)
							&& (request.getParameter(TemenosConstants.STARTDATE).toString() != null
									|| !request.getParameter(TemenosConstants.STARTDATE).toString().equals(""))) {
						String startDate = request.getParameter(TemenosConstants.STARTDATE).toString();
						boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
						if (!isTrue) {
							return PortfolioWealthUtils.validateFormat(TemenosConstants.STARTDATE);
						}
					} else  if (request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).toString().trim()
							.equalsIgnoreCase("HISTORY")) {
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
					} else if (request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).toString().trim()
							.equalsIgnoreCase("HISTORY")) {
						return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ENDDATE);
					}
				} else {
					return PortfolioWealthUtils.validateMandatoryFields("Type should be Open/History");
				}
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
