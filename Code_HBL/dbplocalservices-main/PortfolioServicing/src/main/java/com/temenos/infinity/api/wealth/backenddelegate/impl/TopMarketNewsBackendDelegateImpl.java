package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.TopMarketNewsBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class TopMarketNewsBackendDelegateImpl implements TopMarketNewsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked" })
	@Override
	public Result getTopMarketNews(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object topicObj = inputParams.get(TemenosConstants.TOPIC);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		String topic = null, limitVal = null, offsetVal = null;
		String maxCount = "10";
		if (topicObj == null || topicObj.equals("")) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", "Invalid Input! Topic cannot be null or empty");
			return Utilities.constructResultFromJSONObject(result);
		} else {
			if (topicObj != null) {
				topic = inputParams.get(TemenosConstants.TOPIC).toString();
				inputMap.put(TemenosConstants.TOPIC, topic);
			}

			if (limitObj != null) {
				limitVal = inputParams.get(TemenosConstants.PAGESIZE).toString();
				inputMap.put(TemenosConstants.PAGESIZE, limitVal);
				maxCount = limitVal;
			}
			if (offsetObj != null) {
				offsetVal = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
				inputMap.put(TemenosConstants.PAGEOFFSET, offsetVal);
			}
			if (limitObj != null && offsetObj != null) {
				// Max value accepted by Refinitiv is 30 - Invalid input return with bigger values
				maxCount = "30";
			}
			inputMap.put(TemenosConstants.MAXCOUNT, maxCount);
			inputMap.put("ReturnPrivateNetworkURL", "false");
		}

		try {
			
			String createResponse = null;
			String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
			String operationName = OperationName.GETTOPMARKETNEWS;

			try {
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				

				JSONObject resultJSON = Utilities.convertStringToJSON(createResponse);
				JSONObject responseJSON = new JSONObject();
				responseJSON.put("topMarketNews", resultJSON);
				return Utilities.constructResultFromJSONObject(resultJSON);
				
			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - " + PortfolioWealthAPIServices.WEALTH_GETREFINITIVTOPMARKETNEWS.getOperationName()
				+ "  : " + e).log();
			}

			return null;
			
			
		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - "
					+ OperationName.GETTOPMARKETNEWS + "  : " + e).log();
			return null;

		}
	}

	@SuppressWarnings("unused")
	private JSONObject mockGetTopMarketNews(Map<String, Object> inputMap) {
		String topic = (String) inputMap.get(TemenosConstants.TOPIC);
		String maxCount = (String) inputMap.get(TemenosConstants.MAXCOUNT);
		JSONObject response = new JSONObject();
		JSONObject responseSTORYML = new JSONObject();
		JSONObject storyMLResponse = new JSONObject();
		JSONObject responseHL = new JSONObject();
		JSONArray arrayHL = new JSONArray();
		JSONObject arrayValues = new JSONObject();

		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		// final Random random = new Random();
		// LocalDate date = LocalDate.now();
		// LocalDate yesterday = date.minusDays(1);
		String[] dateValues = new String[30];

		LocalDateTime currDate = LocalDateTime.now();
		for (int j = 0; j < 20; j++) {
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

			dateValues[j] = currDate.minusHours(j).format(myFormatObj).concat("-00:00");
		}

		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201120:nRTROPT20201120015105KBN280065");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[1]);
		arrayValues.put("HT", "Most U.S. companies in China optimistic about Biden administration: business group");
		arrayValues.put("TE",
				"<Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119071149KBN27Z0PF</Origin> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_1_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"ViewRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_2_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"BaseRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_3_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"StoryTitle\\\">AstraZeneca COVID-19 vaccine shows promise in elderly, trial results by Christmas</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T071149+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T071149+0000</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin>\\nFILE PHOTO: A test tube labelled with the word Vaccine is seen in front of AstraZeneca logo in this illustration taken, September 9, 2020. REUTERS/Dado Ruvic/Illustration/File Photo");
		arrayHL.put(arrayValues);

		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119071149LYNXMPEGAI0DS");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[2]);
		arrayValues.put("HT",
				"FILE PHOTO: A test tube labelled with the word Vaccine is seen in front of AstraZeneca logo in this illustration");
		arrayValues.put("TE",
				"<Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119071149KBN27Z0PF</Origin> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_1_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"ViewRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_2_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"BaseRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_3_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"StoryTitle\\\">AstraZeneca COVID-19 vaccine shows promise in elderly, trial results by Christmas</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T071149+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T071149+0000</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin>\\nFILE PHOTO: A test tube labelled with the word Vaccine is seen in front of AstraZeneca logo in this illustration taken, September 9, 2020. REUTERS/Dado Ruvic/Illustration/File Photo");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119091433LYNXMPEGAI0JT");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[3]);
		arrayValues.put("HT", "FILE PHOTO: Yandex.Eats food delivery couriers are seen in the street in Moscow");
		arrayValues.put("TE",
				"<Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119090428KBN27Z10Z</Origin> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T091433Z_1_LYNXMPEGAI0JT_RTROPTP_1_RUSSIA-ECOMMERCE.JPG</Origin> <Origin Href=\\\"ViewRef\\\">2020-11-19T091433Z_1_LYNXMPEGAI0JT_RTROPTP_2_RUSSIA-ECOMMERCE.JPG</Origin> <Origin Href=\\\"BaseRef\\\">2020-11-19T091433Z_1_LYNXMPEGAI0JT_RTROPTP_3_RUSSIA-ECOMMERCE.JPG</Origin> <Origin Href=\\\"StoryTitle\\\">COVID-19 crisis: a shot in the arm for Russian e-commerce</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T090428+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T091433+0000</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin>\\nFILE PHOTO: Yandex.Eats food delivery couriers are seen in the street amid the coronavirus disease (COVID-19) outbreak in Moscow, Russia April 14, 2020. REUTERS/Maxim Shemetov/File Photo");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119060910LYNXMPEGAI0B3");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[4]);
		arrayValues.put("HT",
				"FILE PHOTO: People take part in an anti-racism protest in honour of Bruno Cande in Lisbon");
		arrayValues.put("TE",
				"<Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119060910KBN27Z0KA</Origin> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T060910Z_1_LYNXMPEGAI0B3_RTROPTP_1_HEALTH-CORONAVIRUS-EUROPE-DATA.JPG</Origin> <Origin Href=\\\"ViewRef\\\">2020-11-19T060910Z_1_LYNXMPEGAI0B3_RTROPTP_2_HEALTH-CORONAVIRUS-EUROPE-DATA.JPG</Origin> <Origin Href=\\\"BaseRef\\\">2020-11-19T060910Z_1_LYNXMPEGAI0B3_RTROPTP_3_HEALTH-CORONAVIRUS-EUROPE-DATA.JPG</Origin> <Origin Href=\\\"StoryTitle\\\">COVID toll turns spotlight on Europe&apos;s taboo of data by race</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T060910+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T060910+0000</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin>\\nFILE PHOTO: A protester holds a sign reading &quot;Say no to racism&quot; during an anti-racism protest in honour of Bruno Cande in Lisbon, Portugal, July 31, 2020. REUTERS/Catarina Demony/File Photo");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nOLUSTOPNEWS");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[5]);
		arrayValues.put("HT", "Reuters US Online Report Top News");
		arrayValues.put("TE",
				"<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201118:nRTROPT20201118175321KBN27Y2M1</Origin> U.S. COVID deaths top 250,000 as New York City schools halt in-person classes <p>NEW YORK (Reuters) - The U.S. death toll from COVID-19 surpassed a grim new milestone of 250,000 lives lost on Wednesday, as New York City&apos;s public school system, the nation&apos;s largest, called a halt to in-classroom instruction, citing a jump in coronavirus infection rates.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T003329Z_1_LYNXMPEGAI00V_RTROPTP_1_HEALTH-CORONAVIRUS-USA-NEW-YORK.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119003329LYNXMPEGAI00V</Origin> <Origin Href=\\\"ImageTitle\\\">Students ride on a scooter after exiting school, following the announcement to close New York City public schools, as the spread of coronavirus disease (COVID-19) continues to rise, in Brooklyn, New York</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201118T175321+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T003329+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201118:nRTROPT20201118215122KBN27Y2YS</Origin> Amid coronavirus spike, pressure grows on U.S. agency to approve Trump-to-Biden transition <p>WASHINGTON (Reuters) - A little-known agency that keeps the U.S. federal bureaucracy running is the biggest impediment to new efforts to fight the coronavirus outbreak, Democratic President-elect Joe Biden said on Wednesday. </p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T010741Z_2_LYNXMPEGAH1SN_RTROPTP_1_USA-ELECTION-TRANSITION-BIDEN.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201118:nRTROPT20201118215122LYNXMPEGAH1SN</Origin> <Origin Href=\\\"ImageTitle\\\">U.S. General Services Administration Administrator Emily W. Murphy sworn in Washington</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201118T215122+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201118T215122+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119051028KBN27Z0GN</Origin> Ahead of recount results, Georgia officials say Biden likely to remain the winner <p>WASHINGTON (Reuters) - A key battleground state in the 2020 presidential election is expected on Thursday to affirm Democrat Joe Biden&apos;s victory over President Donald Trump, which would deal yet another setback to Trump&apos;s scattershot efforts to hold on to power.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T051028Z_1_LYNXMPEGAI08O_RTROPTP_1_USA-ELECTION-BIDEN.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119051028LYNXMPEGAI08O</Origin> <Origin Href=\\\"ImageTitle\\\">President-elect Biden enters The Queen ahead of a virtual meeting with frontline healthcare workers in Wilmington</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T051028+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T051028+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119001021KBN27Z00V</Origin> Asia stocks ease from highs, bonds count on Fed support <p>SYDNEY (Reuters) - Asian shares drifted off all-time highs on Thursday as widening COVID-19 restrictions in the United states weighed on Wall Street, while bonds were underpinned by speculation the Federal Reserve would have to respond with yet more easing.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T001021Z_1_LYNXMPEGAI00E_RTROPTP_1_GLOBAL-MARKETS-ASIA.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119001021LYNXMPEGAI00E</Origin> <Origin Href=\\\"ImageTitle\\\">FILE PHOTO: A man wearing a protective face mask walks past a stock quotation board outside a brokerage, amid the coronavirus disease (COVID-19) outbreak, in Tokyo</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T001021+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T001021+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119023642KBN27Z093</Origin> U.S. airline CEOs renew request for more aid in letter to Congress <p>WASHINGTON/CHICAGO (Reuters) - The chief executives of the seven largest U.S. airlines made a fresh plea for more payroll relief before the end of the year and pointed to the challenges of distributing a COVID-19 vaccine in a letter to Congressional leaders on Wednesday.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T031354Z_2_LYNXMPEGAI03R_RTROPTP_1_HEALTH-CORONAVIRUS-USA.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119023642LYNXMPEGAI03R</Origin> <Origin Href=\\\"ImageTitle\\\">An airplane takes off from the Ronald Reagan National Airport as air traffic is affected by the spread of the coronavirus disease (COVID-19), in Washington</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T023642+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T023642+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119052558KBN27Z0HN</Origin> Analysis: As regulators prepare to weigh in on 737 MAX, FAA&apos;s global dominance fades <p>SYDNEY/MONTREAL (Reuters) - Global regulators have held off approving the Boeing 737 MAX despite a decision by the U.S. Federal Aviation Administration to end a 20-month grounding, highlighting changes in the global regulatory pecking order caused by two crashes of the jet.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T054006Z_3_LYNXMPEGAI09C_RTROPTP_1_BOEING-737MAX-REGULATORS.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119052558LYNXMPEGAI09C</Origin> <Origin Href=\\\"ImageTitle\\\">FILE PHOTO: Grounded Boeing 737 MAX aircraft are seen parked at Boeing facilities at Grant County International Airport in Moses Lake</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T052558+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T052558+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119072556KBN27Z0QN</Origin> Ethiopian government says rebels have committed &apos;atrocities&apos; during two-week Tigray conflict <p>ADDIS ABABA (Reuters) - Ethiopia&apos;s government on Thursday said rebels have committed &quot;serious crimes&quot; after conflict broke out this month in the northern Tigray region, as U.S. President-elect Joe Biden&apos;s foreign policy adviser called for greater protection of civilians.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T072556Z_1_LYNXMPEGAI0EB_RTROPTP_1_ETHIOPIA-CONFLICT-MILITARY-FACTBOX.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119072556LYNXMPEGAI0EB</Origin> <Origin Href=\\\"ImageTitle\\\">FILE PHOTO: Militia members from Ethiopia&apos;s Amhara region ride on their truck as they head to face the Tigray People&apos;s Liberation Front (TPLF), in Sanja, Amhara</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T072556+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T072556+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119063011KBN27Z0LG</Origin> Hong Kong court rules that police complaints system breaches Bill of Rights <p>HONG KONG (Reuters) - Hong Kong&apos;s High Court ruled on Thursday that the city government had failed to provide an independent mechanism to handle police complaints, breaching the Asian financial hub&apos;s Bill of Rights on torture and cruel treatment.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T063011Z_1_LYNXMPEGAI0CE_RTROPTP_1_HONGKONG-PROTESTS.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119063011LYNXMPEGAI0CE</Origin> <Origin Href=\\\"ImageTitle\\\">Anti-government demonstrators protest on Christmas Eve in Hong Kong</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T063011+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T063011+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201118:nRTROPT20201118204216KBN27Y2UO</Origin> Trump election campaign asks judge to declare him winner in Pennsylvania <p>(Reuters) - President Donald Trump&apos;s election campaign on Wednesday asked a judge to declare him the winner in Pennsylvania, saying the state&apos;s Republican-controlled legislature should select the electors that will cast votes in the U.S. Electoral College system.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T015200Z_7_LYNXMPEGAH1PZ_RTROPTP_1_USA-ELECTION-LAWSUITS-MERITS.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201118:nRTROPT20201118204216LYNXMPEGAH1PZ</Origin> <Origin Href=\\\"ImageTitle\\\">FILE PHOTO: U.S. 2020 presidential election in Pennsylvania</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201118T204216+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201118T204216+0000</Origin> </p>\\n<p><Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119071149KBN27Z0PF</Origin> AstraZeneca COVID-19 shot candidate shows promise among elderly in trials <p>LONDON (Reuters) - A potential COVID-19 vaccine developed by AstraZeneca Plc and Oxford University produced a strong immune response in older adults, giving hope it may protect some of those most vulnerable to the disease, data from mid-stage trials showed.</p> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T071149Z_1_LYNXMPEGAI0DS_RTROPTP_1_HEALTH-CORONAVIRUS-OXFORD-ASTRAZENECA-VACCINE.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119071149LYNXMPEGAI0DS</Origin> <Origin Href=\\\"ImageTitle\\\">FILE PHOTO: A test tube labelled with the word Vaccine is seen in front of AstraZeneca logo in this illustration</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T071149+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T071149+0000</Origin> </p>\\n");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119071939KBN27Z0PY");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[6]);
		arrayValues.put("HT",
				"China Sinopharm&apos;s coronavirus vaccine taken by about a million people in emergency use");
		arrayValues.put("TE",
				"<p>BEIJING (Reuters) - Nearly one million people have taken an experimental coronavirus vaccine developed by China National Pharmaceutical Group (Sinopharm) through the country&apos;s emergency use programme, the firm said late on Wednesday.</p>\\n<p>China launched the emergency use programme in July, which so far includes three vaccine candidates for essential workers and other limited groups of people even as clinical studies have yet to be completed to prove their safety and efficacy.</p>\\n<p>No serious adverse reaction has been reported from those who received the vaccine in emergency use, Sinopharm said in an article on social media WeChat, citing Chairman Liu Jingzhen from a recent media interview.</p>\\n<p>Two vaccine candidates developed by Sinopharm&apos;s subsidiary China National Biotec Group (CNBG) and third one developed by Sinovac Biotech &lt;SVA.O&gt; have been used for the emergency programme.</p>\\n<p>It&apos;s unclear which vaccine Liu referred to, and Sinopharm was not immediately available to comment.</p>\\n<p>Sinopharm&apos;s vaccines, which use inactivated virus unable to replicate in human cells to trigger immune responses, require two doses, clinical trial registration data showed. </p>\\n<p>The experimental vaccines are undergoing Phase 3 clinical trials overseas that have recruited nearly 60,000 people, and blood samples of more than 40,000 participants have been taken 14 days after they took the second dose, the article said citing Liu, without breaking down the numbers for each vaccine.</p>\\n<p>Among construction project employees, diplomats and students who went abroad after taking Sinopharm&apos;s vaccine, no one has been infected, it added.</p>\\n<p>But experts have cautioned against using data solely from emergency use programme, without comparable results from a clinical trial-standard control group, to determine a vaccine&apos;s effectiveness.</p>\\n<p></p>\\n<p> (Reporting by Roxanne Liu and Tony Munroe; Editing by Himani Sarkar)</p>\\n<Origin Href=\\\"ThumbnailRef\\\">2020-11-19T071939Z_1_LYNXMPEGAI0E2_RTROPTP_1_HEALTH-CORONAVIRUS-VACCINE-CHINA.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119071939LYNXMPEGAI0E2</Origin> <Origin Href=\\\"ImageTitle\\\">The 2020 China International Fair for Trade in Services (CIFTIS) in Beijing</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T071939+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T071939+0000</Origin>\\n");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119031407LYNXMPEGAI04U");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[7]);
		arrayValues.put("HT", "Robots, immune from virus, pack Thanksgiving food to spare elderly helpers");
		arrayValues.put("TE",
				"<p>By Roselle Chen</p>\\n<p>NEW YORK (Reuters) - &quot;Star Wars&quot; robot R2-D2 cannot deliver Thanksgiving dinner to your door, but his relatives are doing the packing.</p>\\n<p>Immune from the coronavirus, industrial robotic arms are assembling donated holiday food in a Boston suburb, allowing elderly volunteers to stay home in a pandemic that has doubled hungry households.</p>\\n<p>In Bedford, Massachusetts, the whirring arms studded with suction cups pick, sort and pack such Thanksgiving staples as potatoes and stuffing. Humans will ship them to two nonprofit groups to distribute to people in need.</p>\\n<p>&quot;Picking with Purpose&quot; is a partnership between artificial intelligence firm Berkshire Grey and Greater Boston Food Bank and City Harvest in New York, which feed some of the estimated 54 million people in the United States struggling to afford food during the pandemic.</p>\\n<p>&quot;There&apos;s like a double whammy,&quot; said Steve Johnson, Berkshire Grey&apos;s president and chief operating officer of robotics, citing the health risks and spike in hunger.</p>\\n<p>&quot;A lot of the people that help in food banks are typically retirees or volunteers in that age group,&quot; he said. Seniors are particularly vulnerable to the air-borne coronavirus that has killed more than 250,000 in the United States.</p>\\n<p>Berkshire Grey donated 40,000 pounds of food for the Thanksgiving project. About 3,000 boxes, with four meals each, go to City Harvest in New York and 1,000 boxes to the Boston food bank, feeding 16,000 people on the Nov. 26 holiday.</p>\\n<p>Many had never needed food handouts before.</p>\\n<p>Before the pandemic, the Greater Boston Food Bank, New England&apos;s largest hunger-relief organization, was feeding about 300,000 in a city with a population of 710,000, according to WorldPopulationReview.com.</p>\\n<p>&quot;Now these numbers are shy of 700,000 people&quot; in and around Boston who need help, said Carol Tienken, COO of the nonprofit group. &quot;This is the entire city of Boston that we&apos;re having to feed right now because people can&apos;t access food.&quot;</p>\\n<p>In New York, 2 million people do not know where their next meal is coming from, up from 1.5 million before COVID-19 gripped the city.</p>\\n<p>&quot;We&apos;re seeing a lot of new families, children, people that look like you and I, with the amount of people that are out of work,&quot; said Racine Droz, director of donor relations at City Harvest.</p>\\n<p></p>\\n<p> (Reporting by Roselle Chen; Writing by Barbara Goldberg; Editing by Richard Chang)</p>\\n<Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T230432+0000</Origin>\\n");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119023642KBN27Z093");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[8]);
		arrayValues.put("HT", "U.S. airline CEOs renew request for more aid in letter to Congress");
		arrayValues.put("TE",
				"<p>By David Shepardson and Tracy Rucinski</p>\\n<p>WASHINGTON/CHICAGO (Reuters) - The chief executives of the seven largest U.S. airlines made a fresh plea for more payroll relief before the end of the year and pointed to the challenges of distributing a COVID-19 vaccine in a letter to Congressional leaders on Wednesday.</p>\\n<p>The letter, seen by Reuters, was sent by the main industry lobby Airlines for America and signed by the heads of the top seven U.S. airlines. </p>\\n<p>&quot;As the nation looks forward and takes on the logistical challenges of distributing a vaccine, it will be important to ensure there are sufficient certified employees and planes in service necessary for adequate capacity to complete the task,&quot; they said.</p>\\n<p>U.S. airlines received $25 billion in federal aid to keep employees on payroll between March and September and have asked for a second round of support after cutting tens of thousands of jobs either through furloughs or early retirements in recent months.</p>\\n<p>They have argued that they need trained employees to help service an economic rebound, with the prospects of a vaccine in the coming months underscoring the urgency.</p>\\n<p>The number of travelers that passed through Transportation Security Administration checkpoints on Tuesday was down two-thirds from the same day in 2019, an improvement from the start of the pandemic but not enough to bring airlines out of their cash hole, particularly with further lockdowns looming as COVID-19 cases rise.</p>\\n<p>Still, the industry&apos;s aid request has received wide bipartisan support but has so far failed to pass as Congress remains deadlocked over a broader COVID-19 relief and stimulus plan.</p>\\n<p>They are now hoping that Congress can pass airline aid through some other vehicle such as a funding bill this year, people familiar with the matter have said.   </p>\\n<p>Congress is not expected to return until Nov. 30.</p>\\n<p>Southwest Airlines &lt;LUV.N&gt;, which has never laid off any employees in its 49-history, sent warnings of potential furloughs to about 400 employees on Wednesday.</p>\\n<p></p>\\n<p> (Reporting by Tracy Rucinski and David Shepardson; Editing by Jacqueline Wong and Stephen Coates)</p>\\n<Origin Href=\\\"ThumbnailRef\\\">2020-11-19T031354Z_2_LYNXMPEGAI03R_RTROPTP_1_HEALTH-CORONAVIRUS-USA.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119023642LYNXMPEGAI03R</Origin> <Origin Href=\\\"ImageTitle\\\">An airplane takes off from the Ronald Reagan National Airport as air traffic is affected by the spread of the coronavirus disease (COVID-19), in Washington</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T023642+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T023642+0000</Origin>\\n");
		arrayHL.put(arrayValues);

		arrayValues = new JSONObject();
		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119023642LYNXMPEGAI03R");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[9]);
		arrayValues.put("HT",
				"An airplane takes off from the Ronald Reagan National Airport as air traffic is affected by the spread of the coronavirus disease (COVID-19), in Washington");
		arrayValues.put("TE",
				"<Origin Href=\\\"StoryRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119023642KBN27Z093</Origin> <Origin Href=\\\"ThumbnailRef\\\">2020-11-19T031354Z_2_LYNXMPEGAI03R_RTROPTP_1_HEALTH-CORONAVIRUS-USA.JPG</Origin> <Origin Href=\\\"ViewRef\\\">2020-11-19T031354Z_2_LYNXMPEGAI03R_RTROPTP_2_HEALTH-CORONAVIRUS-USA.JPG</Origin> <Origin Href=\\\"BaseRef\\\">2020-11-19T031354Z_2_LYNXMPEGAI03R_RTROPTP_3_HEALTH-CORONAVIRUS-USA.JPG</Origin> <Origin Href=\\\"StoryTitle\\\">U.S. airline CEOs renew request for more aid in letter to Congress</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T023642+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T023642+0000</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin>\\nFILE PHOTO: An airplane takes off from the Ronald Reagan National Airport as air traffic is affected by the spread of the coronavirus disease (COVID-19), in Washington, U.S., March 18, 2020. REUTERS/Carlos Barria");
		arrayHL.put(arrayValues);

		arrayValues.put("ID", "urn:newsml:onlinereport.com:20201119:nRTROPT20201119205914KBN27Z31Z");
		arrayValues.put("PR", "onlinereport.com");
		arrayValues.put("LT", dateValues[10]);
		arrayValues.put("HT", "Delta to test touchless curbside check-in at Detroit airport");
		arrayValues.put("TE",
				"<p>CHICAGO (Reuters) - Delta Air Lines said on Thursday it will begin testing touchless curbside check-ins next month at Detroit Metropolitan Wayne County Airport, the latest example of how the coronavirus pandemic is changing the travel experience.</p>\\n<p>Atlanta-based Delta said it will expand the test in Detroit to include bag drop and boarding early next year and is exploring a curb-to-gate facial recognition option across its network.</p>\\n<p>Bill Lentsch, Delta&apos;s Chief Customer Experience Officer, said the COVID-19 pandemic has &quot;deepened the importance of providing a touchless experience.&quot; </p>\\n<p>The option will be available for U.S. domestic travelers who are TSA PreCheck or Global Entry members. </p>\\n<p></p>\\n<p> (Reporting by Tracy Rucinski; Editing by Nick Zieminski)</p>\\n<Origin Href=\\\"ThumbnailRef\\\">2020-11-19T205914Z_1_LYNXMPEGAI1SZ_RTROPTP_1_HEALTH-CORONAVIRUS-DELTA-AIR.JPG</Origin> <Origin Href=\\\"ImageRef\\\">urn:newsml:onlinereport.com:20201119:nRTROPT20201119205914LYNXMPEGAI1SZ</Origin> <Origin Href=\\\"ImageTitle\\\">FILE PHOTO: Lone passenger leaves a Delta Airlines counter after checking in at Reagan National airport in Washington</Origin> <Origin Href=\\\"ChannelCode\\\">OLUSTOPNEWS</Origin> <Origin Href=\\\"LinkTitle\\\">Reuters US Online Report Top News</Origin> <Origin Href=\\\"StoryCreationDate\\\">20201119T205914+0000</Origin> <Origin Href=\\\"ImageCreationDate\\\">20201119T205914+0000</Origin>\\n");
		arrayHL.put(arrayValues);

		responseHL.put("HL", arrayHL);
		responseSTORYML.put("STORYML", responseHL);
		storyMLResponse.put("StoryMLResponse", responseSTORYML);
		response.put("GetSummaryByTopic_Response_1", storyMLResponse);

		return response;
	}

	private JSONObject pagination(JSONObject jsonResult, int limit, int offset) {
		String[] objectVal = new String[] { "GetSummaryByTopic_Response_1", "StoryMLResponse", "STORYML" };
		JSONObject objJson = jsonResult;
		for (int i = 0; i < objectVal.length; i++) {
			objJson = objJson.getJSONObject(objectVal[i]);
		}
		JSONArray jsonArray = objJson.getJSONArray("HL");
		JSONObject response = new JSONObject();
		JSONObject responseSTORYML = new JSONObject();
		JSONObject storyMLResponse = new JSONObject();
		JSONObject responseHL = new JSONObject();
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
		int totalcount = jsonArray.length();
		responseHL.put("HL", paginationJSON);
		responseSTORYML.put("STORYML", responseHL);
		storyMLResponse.put("StoryMLResponse", responseSTORYML);
		response.put("GetSummaryByTopic_Response_1", storyMLResponse);
		response.put("httpStatusCode", "200");
		response.put("totalcount", totalcount);
		return response;
	}

}
