package com.temenos.infinity.wealth.mock.processor.post;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetPastProposalPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		JSONObject final_response = new JSONObject();
		try {
			diagnostic.prepareDebug("==========> GetPastProposalPostProcessor Mock - Entered ").log();
			ArrayList<HashMap<String, String>> pastProposalList = new ArrayList<HashMap<String, String>>();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if ((portfolioId.equalsIgnoreCase("100777-4") || portfolioId.equalsIgnoreCase("100777-5")) && userPermissions.contains(TemenosConstants.INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW) ) {
				
				/*String filter = request.getParameterValues(TemenosConstants.FILTER)[0];
                
				String description[] = new String[] {
						"Q4 Investment review and proposal for Digital Advisory portfolio.",
						"Q3 Final Investment review and proposal for Digital Advisory portfolio.",
						"Q2 Investment review and proposal for Digital Advisory portfolio..." };
				String[] dateTime = new String[] { "25/12/2021,6:00 AM", "28/01/2022,6:00 AM", "24/05/2022,6:00 AM" };
				String[] header = new String[] { "Released for Trading", "Released for Trading",
						"Released for Trading" };
				String[] pastProposalId = new String[] { "1", "2", "3" };*/
				String filter = request.getParameterValues(TemenosConstants.FILTER)[0];

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                
				String[] tradeDateArr = new String[3];

				Calendar cal = Calendar.getInstance();

				for (int i = 0; i < 3; i++) {
				if (i > 0) {
                cal.add(Calendar.YEAR, +1);
				cal.add(Calendar.MONTH, -1);

				cal.add(Calendar.DATE, 1);

				} else {

				cal.add(Calendar.YEAR, -2);

				cal.add(Calendar.DATE, 1);

				}
				String da = sdf.format(cal.getTime()).toString();
				String[] resultd = da.split(" ");
				SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
	            SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm:ss");
	            String string = sdf4.format(sdf3.parse(resultd[1]));
	            String[] ampm = string.split(":");
	            int ij=Integer.parseInt(ampm[0]);  
	            if(ij >12) {
	            	ij=ij-12;
	            	da = resultd[0] +", " +ij + ":" + ampm[1]+ " PM";
	            }else {
	            	da = resultd[0] +", " +ij + ":" + ampm[1]+ " AM";
	            }
				//tradeDateArr[i] = sdf.format(cal.getTime()).toString();
	            tradeDateArr[i] = da;

				}
                
				String description[] = new String[] {

				"Q4 Investment review and proposal for Digital Advisory portfolio.",

				"Q3 Final Investment review and proposal for Digital Advisory portfolio.",

				"Q2 Investment review and proposal for Digital Advisory portfolio..." };

				String[] dateTime = tradeDateArr;

				String[] header = new String[] { "Released for Trading", "Released for Trading",

				"Released for Trading" };

				String[] pastProposalId = new String[] { "1", "2", "3" };

				IntStream.range(0, description.length).forEach(index -> {
					HashMap<String, String> hMap = new HashMap<String, String>();
					hMap.put("description", description[index]);
					hMap.put("dateTime", dateTime[index]);
					hMap.put("header", header[index]);
					hMap.put("pastProposalId", pastProposalId[index]);
					pastProposalList.add(hMap);
				});
				ArrayList<HashMap<String, String>> finalList = pastProposalList;
				if (filter != null && filter.equalsIgnoreCase("")) {
					final_response.put("pastProposal", finalList);
				} else if (filter != null
						&& (filter.equalsIgnoreCase("previousYear") || filter.equalsIgnoreCase("currentYear"))) {
					try {
						String startDate = "", endDate = "", datefilter_key = "dateTime", dateFormat = "dd/MM/yyyy";
						LocalDate localDate = LocalDate.now();
						if (filter.equalsIgnoreCase("previousYear")) {
							startDate = localDate.minusYears(1).withDayOfYear(1)
									.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							endDate = localDate.minusYears(1).withMonth(12).withDayOfMonth(31)
									.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						} else if (filter.equalsIgnoreCase("currentYear")) {
							startDate = localDate.withDayOfYear(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							endDate = localDate.withMonth(12).withDayOfMonth(31)
									.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						}
						PortfolioWealthUtils obj = new PortfolioWealthUtils();
						finalList = obj.filterDate(finalList, startDate, endDate, datefilter_key, dateFormat);
						diagnostic.prepareDebug("==========> GetPastProposalPostProcessor Mock -  No. of records returned after date filter: "+finalList.size()).log();

					} catch (Exception e) {
						e.getMessage();
					}
					final_response.put("pastProposal", finalList);
				} else if (filter != null
						&& !(filter.equalsIgnoreCase("previousYear") || filter.equalsIgnoreCase("currentYear"))) {
					final_response.put("pastProposal", new ArrayList<String>());
				} else {
					final_response.put("pastProposal", finalList);
				}
			} else {
				final_response.put("pastProposal", new ArrayList<String>());
			}
			final_response.put("portfolioID", portfolioId);
			if (portfolioId.equalsIgnoreCase("100777-4"))
			{
				final_response.put("isNewProp", "false");
				final_response.put("funcResultCode", "");
			}else if (portfolioId.equalsIgnoreCase("100777-5") && userPermissions.contains(TemenosConstants.INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW))
			{
				final_response.put("isNewProp", "true");
				final_response.put("funcResultCode", "");
			}else if (portfolioId.equalsIgnoreCase("100777-5") && !userPermissions.contains(TemenosConstants.INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW))
			{
				final_response.put("isNewProp", "false");
				final_response.put("funcResultCode", "");
			}
			
			Result final_result = Utilities.constructResultFromJSONObject(final_response);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			result.appendResult(final_result);
			diagnostic.prepareDebug("==========> GetPastProposalPostProcessor Mock - Exited").log();

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetPastProposalPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		return result;
}
}
