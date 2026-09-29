/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetDXMasterAssetT24PreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  GetDXMasterAssetT24PreProcessor TAP - Entered").log();
		try {

			if (((request.getParameter(TemenosConstants.APPLICATION) != null
					&& request.getParameter(TemenosConstants.APPLICATION).equalsIgnoreCase("DXMaster"))
					|| (request.getParameter("DXMaster") != null
							&& request.getParameter("DXMaster").equalsIgnoreCase("true"))
					|| request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null)
					|| request.getParameter("T24Favourite") != null) {
				if (request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null
						|| request.getParameter(TemenosConstants.OPSTATUS).equalsIgnoreCase("0")
						|| request.getParameter("T24Favourite") != null) {
					if (request.getParameter("T24Favourite") == null
							&& request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
						inputMap.put("param", "instrumentName");
						String paramValue = request.getParameter("paramValue");
						inputMap.put("instrumentId", paramValue);
					} else if (request.getParameter("T24Favourite") != null) {
						inputMap.put("param", "contractId");
						String paramValue = request.getParameter("T24Instrumentids");
						paramValue = paramValue.replaceAll(" ", "%20").trim();
						paramValue = paramValue.replaceAll("#", "%23").trim();
						inputMap.put("instrumentId", paramValue);
						request.addRequestParam_("T24DXMasterflag", "true");
					} else {
						inputMap.put("param", "contractId");
					}
					diagnostic.prepareDebug("==========>  GetDXMasterAssetT24PreProcessor T24 - Entering integration").log();
					return true;
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========>  GetDXMasterAssetT24PreProcessor  T24 - Exited integration").log();
					return false;
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetDXMasterAssetT24PreProcessor  T24 - Exited integration").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========>  GetDXMasterAssetT24PreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
