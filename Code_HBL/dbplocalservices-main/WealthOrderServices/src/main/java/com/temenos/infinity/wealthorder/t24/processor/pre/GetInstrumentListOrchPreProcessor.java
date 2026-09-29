/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentListOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  GetInstrumentDetailsOrchPreProcesor TAP - Entered").log();
		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========>  GetInstrumentDetailsOrchPreProcesor TAP - No user permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========>  GetInstrumentDetailsOrchPreProcesor TAP - User has permission").log();
				String portfolioId = null, sortBy = null, search = null;
				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					if (request.getParameter("isFavouriteSearch") == null
							|| !request.getParameter("isFavouriteSearch").toString().equalsIgnoreCase("true")) {
						return OrderServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
					}
				}
				Object sortByObj = inputMap.get(TemenosConstants.SORTBY);
				if (sortByObj != null) {
					sortBy = inputMap.get(TemenosConstants.SORTBY).toString();
					inputMap.put(TemenosConstants.SORTBY, sortBy);
				} else {
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.SORTBY);
				}

				if (inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null
						&& inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString().trim().length() > 0) {
					search = inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
					inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
					inputMap.put("instrumentName", search);
					inputMap.put("paramValue", ("%27" + search.trim() + "%27").replace(" ", "%20"));
				} else {
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.SEARCHBYINSTRUMENTNAME);
				}

				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
				if ((request.getParameter("isFavouriteSearch") != null
						&& request.getParameter("isFavouriteSearch").equalsIgnoreCase("true"))
						|| allportfoliosList.contains(portfolioId)) {
					diagnostic.prepareDebug("==========>  GetInstrumentDetailsOrchPreProcesor TAP - Entering integration").log();

				} else {
					alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
					alert.prepareError("Invalid request").log();
					result.addParam("status", "Failure");
					result.addParam("error", "Unauthorized Access");
					diagnostic.prepareDebug("==========>  GetInstrumentDetailsOrchPreProcesor TAP - Exiting with invalid portfolio").log();
					return false;

				}
				return true;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentDetailsOrchPreProcesor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
