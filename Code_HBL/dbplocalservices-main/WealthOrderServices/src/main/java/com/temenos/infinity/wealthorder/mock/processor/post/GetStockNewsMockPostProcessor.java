/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;

/**
 * @author himaja.sridhar
 *
 */
public class GetStockNewsMockPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
				diagnostic.prepareDebug("==========> GetStockNewsMockPostProcessor Mock - Entered ").log();
		try {
			String isinCode = request.getParameter("instrumentCode");
			String limitVal = request.getParameter("pageSize");
			String offsetVal = request.getParameter("pageOffset");

			LinkedHashMap<String, String> hm1 = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> hm2 = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> hm3 = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> hm4 = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> hm5 = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> hm6 = new LinkedHashMap<String, String>();
			String[] dateValues = new String[30];

			JSONArray stockNews = new JSONArray();
			LocalDateTime currDate = LocalDateTime.now();
			for (int j = 0; j < 20; j++) {
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

				dateValues[j] = currDate.minusHours(j).format(myFormatObj).concat("-00:00");
			}

			if (isinCode.trim().equalsIgnoreCase("AMZN.O") || isinCode.trim().equalsIgnoreCase("AMZN.OQ")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201117:nBw8fgMlna");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "Amazon Named “Low Price Leader” in New Study");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201117:nL1N2I308K");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "NXP, Amazon partner to connect cars to cloud computing services");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:onlinereport.com:20201116:nRTROPT20201116231350KBN27W315");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Does vaccine promise put U.S. consumers in a shopping mood? Retailers may have clues");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:onlinereport.com:20201116:nRTROPT20201116231350KBN27W315-OCABS");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "Does vaccine promise put U.S. consumers in a shopping mood? Retailers may have clues");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				hm5.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ1P2");
				hm5.put("RT", dateValues[5]);
				hm5.put("PR", "reuters.com");
				hm5.put("HT", "UPDATE 1-Loeb&apos;s Third Point funds gain in November, up double-digits YTD");
				hm5.put("LT", dateValues[5]);
				hm5.put("CT", dateValues[5]);

				hm6.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ1TX");
				hm6.put("RT", dateValues[6]);
				hm6.put("PR", "reuters.com");
				hm6.put("HT",
						"UPDATE 1-More than 400 lawmakers from 34 countries back &apos;Make Amazon Pay&apos; campaign");
				hm6.put("LT", dateValues[6]);
				hm6.put("CT", dateValues[6]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);
				al.add(hm5);
				al.add(hm6);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("GOOGL.O") || isinCode.trim().equalsIgnoreCase("GOOGL.OQ")
					|| isinCode.trim().equalsIgnoreCase("GOOGL.N")) {

				hm1.put("ID", "urn:newsml:onlinereport.com:20201117:nOLINTECH");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "Reuters India Online Report Technology 	");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201116:nL1N2I221H");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT",
						"WRAPUP 2-Brazil launches &apos;Pix&apos; instant payments system, Whatsapp to enter &apos;soon&apos;");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201116:nPremltmRa");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Hedge Funds Are Betting Big On This $130 Trillion Trend");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:onlinereport.com:20201116:nRTROPT20201116173458KBN27W2DC");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT",
						"Brazil launches &apos;Pix&apos; instant payments system, Whatsapp to enter &apos;soon&apos;");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AAPL.O") || isinCode.trim().equalsIgnoreCase("AAPL.OQ")
					|| isinCode.trim().equalsIgnoreCase("AAPL.N")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201116:nPn8n5GmYa");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT",
						"WPI-MANA Team Creates Thermoelectric Device Combined with Wavelength-selective Thermal Emitter That Generates Continuous Power, Day and Night");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201109:nCNWs6qtka");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "Montage Gold Inc. Issues Stock Options");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201030:nGNX7nSvLd");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Montage Gold Corp. Announces Full Exercise of Underwriters’ Over-Allotment Option");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201029:nL4N2HK569");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "UPDATE 5-Facebook anticipates tougher 2021 even as pandemic boosts ad revenue");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("LVMH.PA")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201116:nPRrGB7ADa");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "REG-BlackRock Grtr Eur: Portfolio Update");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201109:nCNWs6qtka");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "Hennessy Announces Second Phase of Unfinished Business Funding");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201113:nBw88YMtTa");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT",
						"$422.9 Billion Worldwide Winter Wear Industry to 2027 - Impact of COVID-19 on the Market - ResearchAndMarkets.com");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201112:nPnbc3Fs4a");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT",
						"Narvar Simplifies Returns for Consumers and Retailers, Launching Digital, Boxless Returns with Cole Haan");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AMAG.OQ")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201116:nPNA4qXWga");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "Covis Group Completes Acquisition of AMAG Pharmaceuticals");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201116:nPre6CRv7a");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "Covis Group Completes Acquisition of AMAG Pharmaceuticals");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201116:nPn2rN98Ta");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Covis Group Completes Acquisition of AMAG Pharmaceuticals");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201116:nCNWxYhf4a");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "Covis Group Completes Acquisition of AMAG Pharmaceuticals");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AMAL.O") || isinCode.trim().equalsIgnoreCase("AMAL.OQ")) {

				hm1.put("ID", "urn:newsml:onlinereport.com:20201117:nRTROPT20201117123049KBN27X1IB-OCABS");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "TSX futures down as weaker oil, rising virus cases weigh");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:onlinereport.com:20201117:nRTROPT20201117123049KBN27X1IB-OCATP");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "TSX futures down as weaker oil, rising virus cases weigh");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201117:nL4N2I32EG");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "CANADA STOCKS-TSX futures down as weaker oil, rising virus cases weigh");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201116:nZaw3q4PRR");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "SNG: 24 women appointed to Saudi Shoura committees");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AMRN.O")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201116:nCNWyxhx6a");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT",
						"VASCEPA® (Icosapent Ethyl) Found to Significantly Reduce Ischemic Events in Patients with Prior Coronary Artery Bypass Grafting Procedures");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201113:nGNX1CLGsL");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT",
						"VASCEPA® (Icosapent Ethyl) Found to Significantly Reduce Ischemic Events in Patients with Prior Coronary Artery Bypass Grafting (CABG) Procedures in Post Hoc Subgroup Analyses of Landmark REDUCE-IT® Study Presented at American Heart Association’s Virtual Scientific Sessions 2020");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201111:nGNX9nq9Wk");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT",
						"Amarin to Present at the Stifel 2020 Virtual Healthcare and  Jefferies Virtual London Healthcare Conferences");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201109:nFWN2HV13A");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "BRIEF-ADF Foods Ltd Sept-Quarter Consol PAT Rises");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AMBA.O")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201117:nGNE6CFqqv");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "COVID-19 is a ‘wake-up command’ to address Africa’s challenges - Tony Blair");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201117:nGNX366Dj2");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "COVID-19 is a ‘wake-up command’ to address Africa’s challenges - Tony Blair");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201114:nGNE669KWF");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Moody’s Investor Service affirms African Development Bank’s AAA credit rating");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201114:nGNXc4gF4N");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "Moody’s Investor Service affirms African Development Bank’s AAA credit rating");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AMCX.O")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201102:nASA01BFN");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "BRIEF-AMC Networks Inc Reports Third Quarter EPS $1.17");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201102:nGNX2kTy77");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "AMC Networks Inc. Reports Third Quarter 2020 Results");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201021:nGNX2Vqnkk");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "AMC Networks to Report Third Quarter 2020 Results");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201021:nFWN2HB15K");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "BRIEF-Amc Networks Announces Final Results Of Modified Dutch Auction Tender Offer");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("MSFT.O")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ26V");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "UPDATE 3-Justice Department accuses Facebook of discriminating against U.S. workers");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201203:nB8N2FR00N");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "BRIEF-Microsoft Unveils A New Data Governance Solution, Azure Purview");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ1I8");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Microsoft aims to help businesses get handle on data with new tool");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201203:nL2N2GK17L");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "FOCUS-North American farmers profit as consumers pressure food business to go green");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("TSLA.O") || isinCode.trim().equalsIgnoreCase("TSLA.OQ")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201204:nL1N2IK01K");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "UPDATE 1-LG Chem, SK Innovation spar over EV recalls in trade dispute");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ323");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "GLOBAL MARKETS-Asia stocks set for small gains as U.S. advances fiscal stimulus");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ2BL");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "LG Chem, SK Innovation spar over EV recalls in trade dispute");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ2OS");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "US STOCKS-Nasdaq hits record high, S&amp;P 500 ends lower");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AMRN.O")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201201:nGNX7J4hN");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "Amarin Files Patent Infringement Lawsuit Against Hikma");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201124:nGNX6QdSS9");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "Amarin to Present at Piper Sandler’s 32nd Annual Healthcare Conference (Virtual)");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201120:nFWN2I519C");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT",
						"BRIEF-Amarin Shares Topline Data From Partner’s Phase 3 Study Of Vascepa In Mainland China");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201119:nGNX9pW2Dl");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT",
						"Amarin Shares Topline Data from Partner’s Pivotal Phase 3 Study of VASCEPA® (Icosapent Ethyl) in Mainland China");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("WMT.N")) {

				hm1.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ26V");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "UPDATE 3-Justice Department accuses Facebook of discriminating against U.S. workers");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20201203:nL1N2IJ0BY");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "UPDATE 4-UK food retailers hand back $2.4 bln in property tax relief");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20201203:nL4N2IJ3GN");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Walmart to spend more than $700 mln on new round of employee bonuses");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20201203:nFWN2IJ0UZ");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT",
						"BRIEF-Walmart Announces More Than $700 Mln In Additional Associate Bonuses, Tops $2.8 Bln In Total Cash Bonuses To Associates In 2020");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("F.N") || isinCode.trim().equalsIgnoreCase("2YTD")
					|| isinCode.trim().equalsIgnoreCase("5YTD")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210517:nL2N2N41RT");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "US STOCKS-Wall St weighed down by falling tech stocks");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20210517:nPn2m2kqsa");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "First National Bank Expands in Charleston with Branch at Freshfields Village");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20210517:nL3N2N41OT");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Chinese automaker Changan aims to list EV unit on STAR Market -sources");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20210516:nL1N2N10NH");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "European climate group says EU needs far tougher van CO2 targets");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				hm5.put("ID", "urn:newsml:reuters.com:20210514:nFWN2N10RL");
				hm5.put("RT", dateValues[5]);
				hm5.put("PR", "reuters.com");
				hm5.put("HT", "BRIEF-F.N.B. Corp Files For Potential Mixed Shelf Offering Size Not Disclosed");
				hm5.put("LT", dateValues[5]);
				hm5.put("CT", dateValues[5]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);
				al.add(hm5);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("JPM.N")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210518:nD5N2K201R");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "Emirates NBD hires banks for AT1 dollar bonds - document");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20210517:nFWN2N41H0");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "BRIEF-JPMorgan Chase Declares Common Stock Dividend");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20210517:nFWN2N40SA");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT",
						"BRIEF-Jpmorgan Chase Says Credit Card Charge-Off Rate 1.97% In April Versus 2.03% In March");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("BLK")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210518:nRSR9606Ya");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "REG - iShares III BLK E £  - Net Asset Value(s)");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20210518:nRSR9607Ya");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "REG - iShares III BLK MAM£  - Net Asset Value(s)");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20210518:nRSR9608Ya");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "REG - iShares III BLK MAG£  - Net Asset Value(s)");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20210518:nL5N2N43A7");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "Italy - Factors to watch on May 18");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("FIDELIT.LG")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210315:nFWN2LD0VX");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT",
						"BRIEF-Fidelity Bank Lists 41.21 Bln Naira 8.5% Fixed Rate Unsecured Subordinated Bonds Due 2031");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("BA.N") || isinCode.trim().equalsIgnoreCase("BA.NQ")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210517:nL2N2N41GJ");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "Biden administration approved $735 million arms sale to Israel - sources");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20210517:nL2N2N40YK");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "UPDATE 2-Emirates could swap Boeing 777X jets for smaller Dreamliners, chairman says");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20210517:nD5N2MD001");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "Emirates could swap Boeing 777x for smaller Dreamliners, chairman says");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20210517:nL2N2N40A4");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "UPDATE 1-Ryanair &apos;upset&apos; with Boeing, fears no MAX deliveries before summer");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("AXP.N") || isinCode.trim().equalsIgnoreCase("FEURUSD")
					|| isinCode.trim().equalsIgnoreCase("FGBPUSD")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210517:nFWN2N40WN");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "BRIEF-American Express Reports Card Member Loan Stats for April");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20210423:nL1N2MG2F8");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT", "US STOCKS-Wall Street rallies on strong economic data; tech in focus");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20210423:nL1N2MG22Q");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT", "US STOCKS-Wall Street rallies on strong economic data; tech in focus");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20210423:nL4N2MG3KY");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "US STOCKS-Wall Street rallies on strong economic data; tech in focus");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			} else if (isinCode.trim().equalsIgnoreCase("CSCO.O")  || isinCode.trim().equalsIgnoreCase("HSB.N") || isinCode.trim().equalsIgnoreCase("HI.N")) {

				hm1.put("ID", "urn:newsml:reuters.com:20210514:nFWN2N10X4");
				hm1.put("RT", dateValues[1]);
				hm1.put("PR", "reuters.com");
				hm1.put("HT", "BRIEF-Cisco Says On May 13 Entered Second Amended And Restated Credit Agreement");
				hm1.put("LT", dateValues[1]);
				hm1.put("CT", dateValues[1]);

				hm2.put("ID", "urn:newsml:reuters.com:20210514:nASA026SG");
				hm2.put("RT", dateValues[2]);
				hm2.put("PR", "reuters.com");
				hm2.put("HT",
						"BRIEF-Cisco Announces Intent To Acquire Kenna Security To Deliver Industry Leading Vulnerability Management");
				hm2.put("LT", dateValues[2]);
				hm2.put("CT", dateValues[2]);

				hm3.put("ID", "urn:newsml:reuters.com:20210512:nASA0262A");
				hm3.put("RT", dateValues[3]);
				hm3.put("PR", "reuters.com");
				hm3.put("HT",
						"BRIEF-Cisco Announces Intent To Acquire Socio Labs To Power The Future Of Hybrid Events");
				hm3.put("LT", dateValues[3]);
				hm3.put("CT", dateValues[3]);

				hm4.put("ID", "urn:newsml:reuters.com:20210511:nL1N2MX2MK");
				hm4.put("RT", dateValues[4]);
				hm4.put("PR", "reuters.com");
				hm4.put("HT", "Tech giants join call for funding U.S. chip production");
				hm4.put("LT", dateValues[4]);
				hm4.put("CT", dateValues[4]);

				ArrayList<LinkedHashMap> al = new ArrayList<LinkedHashMap>();
				al.add(hm1);
				al.add(hm2);
				al.add(hm3);
				al.add(hm4);

				stockNews = new JSONArray(al);

			}
			// JSONArray stockNews = wealthmMockUtil.getStockNews(ricCode);
			JSONArray sortedJSON = new JSONArray();
			int totalCount = 0;

			if (stockNews != null && stockNews.length() > 0) {
				totalCount = stockNews.length();
				List<JSONObject> stockList = new ArrayList<JSONObject>();
				for (int i = 0; i < stockNews.length(); i++)
					stockList.add(stockNews.getJSONObject(i));

				Collections.sort(stockList, new Comparator<JSONObject>() {
					private final String KEY_NAME = "CT";

					@Override
					public int compare(JSONObject a, JSONObject b) {
						Date d1 = new Date();
						Date d2 = new Date();
						try {
							String str = a.has(KEY_NAME) ? a.get(KEY_NAME).toString() : "";
							if (str != "") {
								d1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
										.parse(str.substring(0, str.length() - 6));
							}
							str = b.has(KEY_NAME) ? b.get(KEY_NAME).toString() : "";
							if (str != "") {
								d2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
										.parse(str.substring(0, str.length() - 6));
							}
						} catch (JSONException e) {
							e.getMessage();
						} catch (ParseException e) {
							e.getMessage();
						}
						return d1.compareTo(d2);
					}

				});

				for (int i = stockList.size() - 1; i >= 0; i--) {
					sortedJSON.put(stockList.get(i));
				}

				int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
				int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

				if (limit > 0 && offset >= 0) {
					sortedJSON = pagination(sortedJSON, limit, offset);
				}

			}
			JSONObject responseVal = new JSONObject();
			responseVal.put("stockNews", sortedJSON);
			responseVal.put("totalCount", totalCount);
			responseVal.put("httpStatusCode", "200");
			responseVal.put("opstatus", "0");
			diagnostic.prepareDebug("==========> GetStockNewsMockPostProcessor Mock - Exiting with success").log();
			return Utilities.constructResultFromJSONObject(responseVal);
		} catch (Exception e) {
			alert.prepareError("==========> GetStockNewsMockPostProcessor Mock - Error: " + e.getMessage()).log();
			return result;
		}
	}

	private JSONArray pagination(JSONArray jsonArray, int limit, int offset) {
		diagnostic.prepareDebug("==========> GetStockNewsMockPostProcessor Mock - pagination:: Begin").log();
		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < jsonArray.length(); i++) {
			if (j == limit) {
				break;
			} else {
				paginationJSON.put(jsonArray.get(i));
			}
			j++;
		}
		diagnostic.prepareDebug("==========> GetStockNewsMockPostProcessor Mock - pagination:: End").log();
		return paginationJSON;
	}

}
