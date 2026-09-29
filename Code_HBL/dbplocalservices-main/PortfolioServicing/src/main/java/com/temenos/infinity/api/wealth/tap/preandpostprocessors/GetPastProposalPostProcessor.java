package com.temenos.infinity.api.wealth.tap.preandpostprocessors;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
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
    	ArrayList<HashMap<String, String>> pastProposalList = new ArrayList<HashMap<String, String>>();
    	ArrayList<HashMap<String, String>> pastProposalListtwo = new ArrayList<HashMap<String, String>>();
    	String filter = request.getParameterValues(TemenosConstants.FILTER)[0];
    	JSONObject final_response = new JSONObject();
        Dataset bodySet = result.getDatasetById("body");
        boolean flagNewProp = false;
        String funcNewProp = "";
        if(bodySet != null) {
        JSONArray res = ResultToJSON.convertDataset(bodySet);
        ArrayList<String> ar = new ArrayList<String>();
        List <LocalDateTime> ldts = new ArrayList<>(ar.size());
        for (int i = 0; i < res.length(); i++) {
        	HashMap<String, String> hMap = new HashMap<String, String>();
        	HashMap<String, String> hMaptwo = new HashMap<String, String>();
			JSONObject bodyJSON = res.getJSONObject(i);
        	String extDate = bodyJSON.get("lastModifD").toString();
            String extName = bodyJSON.get("sessionDescriptionC").toString();
            String extId = bodyJSON.get("id").toString();
            String extStatus = bodyJSON.get("sessionStatusE").toString();
            String extfunc = bodyJSON.get("funcResultCode").toString();
            if(!(extStatus.equals("Working") || extStatus.equals("Sent for Validation"))) {
            hMap.put("pastProposalId", extId);
            hMap.put("description",extName);
            String[] resultd = extDate.split("T");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            String ds2 = sdf2.format(sdf1.parse(resultd[0]));
            SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String string = sdf.format(sdf3.parse(resultd[1]));
            String[] ampm = string.split(":");
            int ij=Integer.parseInt(ampm[0]);  
            if(ij >12) {
            	extDate = ds2 +"," +string + " PM";
            }else {
            	extDate = ds2 +"," +string + " AM";
            }
            hMap.put("dateTime", extDate);
            hMap.put("header",extStatus);
            if (filter != null && (!(filter.isEmpty()))) {
            pastProposalList.add(hMap);
            }
            }else if(extStatus.equals("Sent for Validation")) {
            	hMaptwo.put("funcresultcode",extfunc);
            	hMaptwo.put("dateTime", extDate);
            	flagNewProp = true;
            	if (filter != null && (!(filter.isEmpty()))) {
            	pastProposalListtwo.add(hMaptwo);
            	}
            	ldts.add(LocalDateTime.parse(extDate));
            }
		}
        if(ldts !=null) {
        ldts.sort(Comparator.naturalOrder());
        for(LocalDateTime dt : ldts) {
        	ar.add(dt.toString());
        }
        for(int j =0;j<pastProposalListtwo.size();j++) {
        	if(ar.get(ar.size()-1).equals(pastProposalListtwo.get(j).get("dateTime"))) {
        		funcNewProp = pastProposalListtwo.get(j).get("funcresultcode");
        	}
        }
        }
        }
        if(!pastProposalList.isEmpty()) {
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
        final_response.put("isNewProp", flagNewProp);
        final_response.put("funcResultCode", funcNewProp);
        Result final_result = Utilities.constructResultFromJSONObject(final_response);
        final_result.addOpstatusParam("0");
        final_result.addHttpStatusCodeParam("200");
        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
        return final_result;
        }
        final_response.put("pastProposal", pastProposalList);
        final_response.put("isNewProp", flagNewProp);
		final_response.put("funcResultCode", funcNewProp);
        Result final_result = Utilities.constructResultFromJSONObject(final_response);
        final_result.addOpstatusParam("0");
        final_result.addHttpStatusCodeParam("200");
        final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
        return final_result;
    }
}
 