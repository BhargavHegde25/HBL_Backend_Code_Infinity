package com.temenos.infinity.wealth.mock.processor.post;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetTopMarketNewsMockPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetTopMarketNewsMockPostProcessor Mock - Entered ").log();
			String limitVal = request.getParameter(TemenosConstants.PAGESIZE).toString();
			String offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET);
			String topic = request.getParameter(TemenosConstants.TOPIC);
			String maxCount = request.getParameter(TemenosConstants.MAXCOUNT);
			JSONObject responseObj = new JSONObject();
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

			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210429:nRTROPT20210429021504KBN2CG05V");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[1]);
			arrayValues.put("HT", "Asia shares rise on supportive Fed as Biden unveils new stimulus");
			arrayValues.put("TE",
					"<p>By Kane Wu</p><p>HONG KONG (Reuters) -     Asian shares rose in early trade on Thursday after the U.S. Federal Reserve said it was too early to consider rolling back emergency support for the economy, and as U.S. President Joe Biden unveiled plans for a $1.8 trillion stimulus package.</p><p>Fed Chair Jerome Powell said on Wednesday that &quot;it is not time yet&quot; to begin discussing any change in policy after the U.S. central bank left interest rates and its bond-buying programme unchanged, despite taking a more optimistic view of the country&apos;s economic recovery.</p><p>Powell&apos;s comments came before Biden&apos;s unveiling of a sweeping package for families and education in his first speech to Congress.</p><p>Excerpts of Biden&apos;s speech released in advance by the White House &quot;hit the high points - big infra(structure) spend, talking climate action and vaccines,&quot; said John Milroy, investment adviser at Ord Minnett. &quot;The Fed remains dovish, all very supportive.&quot;</p><p>Early in the Asian trading day, MSCI&apos;s broadest index of Asia-Pacific shares outside Japan was up 0.1%.</p><p>Australia&apos;s S&amp;P/ASX 200 edged up 0.31%, as strong oil prices lifted energy stocks.</p><p>China&apos;s blue-chip CSI300 index was 0.65% higher in early trade. Hong Kong&apos;s Hang Seng index opened up 0.7%, Seoul&apos;s KOSPI added 0.37% and Taiwan shares rose 0.48%.</p><p>Markets in Japan were closed for a holiday but Nikkei futures edged 0.05% higher to 28,970.</p><p>Tech shares got a boost after Apple Inc on Wednesday posted sales and profits ahead of Wall Street expectations, though it warned a global chip shortage could dent iPad and Mac sales by several billion dollars.</p><p>Nasdaq futures were 0.79% higher and S&amp;P e-mini futures added 0.48% after Wall Street ended lower on Wednesday. The Dow Jones Industrial Average fell 0.48% to end at 33,820.38 points, while the S&amp;P 500 lost 0.08% to 4,183.18.</p><p>The dollar dropped 0.15% against the yen to 108.43 and the euro gained 0.2% to 1.2147 following the Fed&apos;s decision to maintain supportive policies.</p><p>Oil prices extended gains on Thursday after rising 1% in the previous session as bullish forecasts for a demand recovery this summer offset concerns of rising COVID-19 cases in India, Japan and Brazil.</p><p>Brent crude for June rose 0.37%, to $67.52 a barrel while U.S. West Texas Intermediate crude for June was at $64.12 a barrel, up 0.41%.</p><p>Spot gold added 0.42% to $1,788.72 an ounce.</p><p></p><p> (Reporting by Kane Wu in Hong Kong; additional reporting by Andrew Galbraith in Shanghai; Editing by Jacqueline Wong)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428113408KBN2CF1I9");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[2]);
			arrayValues.put("HT", "Dow, Nasdaq weighed down by Microsoft, Amgen; all eyes on Fed");
			arrayValues.put("TE",
					"<p>By Shreyashi Sanyal and Shivani Kumaresan</p><p>(Reuters) -     The Dow and Nasdaq indexes fell on Wednesday as Amgen and Microsoft weighed, while investors focused on a Federal Reserve meeting for updates on monetary policy and waited for another batch of earnings from big technology firms.</p><p>The U.S. central bank&apos;s policy statement is expected to largely follow the mold established in December, when the Fed said it would not change monetary policy until there was &quot;substantial further progress&quot; in meeting its maximum employment and 2% inflation goals. The statement is due at 2 p.m. EDT.</p><p>&quot;Investors are obviously looking for a continuation of the policy, which is very accommodative,&quot; said Andre Bakhos,  managing director at New Vines Capital LLC, New Jersey.</p><p>&quot;They don&apos;t want to hear, &apos;We are going to raise rates&apos;. If something goes wrong, investors want to know the Fed will be there.&quot;</p><p>The S&amp;P 500 touched an all-time high earlier in the session, powered by shares of Google parent Alphabet Inc, which jumped 3.7% after reporting a record profit for the second consecutive quarter and announcing a $50 billion share buyback.</p><p>Both Alphabet and the S&amp;P 500 communication services sector, which houses the company, also hit record highs.</p><p>Market participants are now prepping for results of Apple Inc and Facebook Inc after markets close. </p><p>Facebook Inc is expected to report a rise in first-quarter revenue, while Apple Inc is expected to post a more than 32% jump in second-quarter revenue. Shares of Facebook rose 1.4%, while Apple dipped 0.2%. </p><p>At 12:08 p.m. ET the Dow Jones Industrial Average was down 117.71 points, or 0.35%, at 33,867.22, the S&amp;P 500 was up 1.79 points, or 0.04%, at 4,188.51 and the Nasdaq Composite was down 32.39 points, or 0.23%, at 14,057.83.    </p><p>The tech-heavy Nasdaq on Monday completed a full recovery from its 11% correction that began in February, largely supported by a rise in mega-cap stocks and ebbing inflation fears.</p><p>Microsoft Corp met quarterly sales expectations and beat profit estimates, but its shares fell 3.3% and pressured the Nasdaq due to skepticism about one-off benefits included in the results and high hopes after a year-long rally.</p><p>Biotech Amgen Inc&apos;s 7.5% decline weighed on the Dow  after it said its first-quarter sales and profit fell due to a 7% drop in its net drug prices and a hit from the COVID-19 pandemic.        </p><p>Boeing Co fell 3.4% after posting a wider-than-expected quarterly loss and pausing 737 MAX deliveries over an electrical issue that has partly re-grounded the fleet.</p><p>Overall earnings for S&amp;P 500 companies in the first quarter are expected to jump 39.2% from a year earlier, according to Refinitiv IBES data.</p><p>U.S. President Joe Biden is expected to unveil a sweeping $1.8 trillion package for families and education in his first joint speech to Congress, senior White House officials say.  </p><p>Advancing issues outnumbered decliners by a 1.24-to-1 ratio on the NYSE. Declining issues outnumbered advancers for a 1.06-to-1 ratio on the Nasdaq.    </p><p>The S&amp;P index recorded 71 new 52-week highs and no new low, while the Nasdaq recorded 87 new highs and 18 new lows.</p><p></p><p> (Reporting by Shivani Kumaresan and Shreyashi Sanyal in Bengaluru; Editing by Anil D&apos;Silva)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428155506KBN2CF265");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[3]);
			arrayValues.put("HT", "CP shareholders say bidding war for Kansas City Southern unnecessary");
			arrayValues.put("TE",
					"<p>By Maiya Keidan and Fergal Smith</p><p>TORONTO (Reuters) -     As Kansas City Southern&apos;s share price rises above Canadian Pacific Railway&apos;s offer on prospects of a bidding war, CP shareholders and analysts say the company should stick to its initial proposal, noting Canadian National Railway&apos;s rival offer may struggle to secure regulatory approval.</p><p>CN Railway, Canada&apos;s biggest railroad, last week made an unsolicited $30 billion offer for KCS, trumping CP&apos;s agreed $25 billion bid. Since then, KCS shares have traded about 10% over CP&apos;s offer, reflecting the market&apos;s optimism that CP may have to bump up its bid.</p><p>CP has, so far, declined to raise its offer and investors say it should stay put given the regulatory hurdles facing CN&apos;s proposal.</p><p>&quot;I think they&apos;ve played their hand well by not responding with an increased offer,&quot; said one Canadian fund manager who is invested in both CP and CN. </p><p>CP has &quot;a much better likelihood&quot; of getting regulatory approval, the fund manager said, pointing to the overlap in tracks CN has with KCS.</p><p>CN, with a market value $76 billion runs rail networks parallel to KCS for about 100 kilometres (62 miles) in Louisiana, while CP, with a market capitalisation of $50 billion, has no overlapping rail networks. CN was &quot;confident&quot; in its ability to achieve all necessary regulatory approval, the company told analysts on Monday.</p><p>PRIZED ASSET</p><p>Canada&apos;s two largest railroads are vying for the prized U.S. railroad asset, with the combination set to create the first direct network connecting Canada, the United States, and Mexico.</p><p>It would be first major North American railroad M&amp;A in more than 20 years and is expected to benefit from the new trade pact between the three countries that came into force last July.</p><p>For the U.S. freight rail regulator, the Surface Transportation Board (STB), the size of the combined group is also likely a factor when considering whether the proposed tie-up is in the public interest, said investors.</p><p>Last week, STB said that a waiver of rules governing mergers pre-2001 that was granted to KCS would be applicable to a merger of the company and CP. KCS had been granted the waiver that applies less strict rules based on its small size.</p><p>Meanwhile, CN has applied to the regulator to proceed under STB&apos;s existing merger rules. </p><p>The combination of CP and KCS would make it the smallest Class 1 railway even post-merger while the combination with CN would result in the third-largest North American Class 1, CIBC analysts said in a note.</p><p>&quot;It does feel like as much as anything that CN is just trying to up the price that CP will have to pay,&quot; said Greg Taylor, portfolio manager at Purpose Investments, which owns shares in CP.</p><p>Meanwhile, shippers who are likely to have a sway over the deal outcome are pulling roughly even with CP and CN in their stated level of support.</p><p>Still, the regulatory risk could be too high for CN to overcome.</p><p>&quot;CP upping their bid right now would really be bidding against themselves if the other deal is not that likely to be approved,&quot; Taylor said.</p><p></p><p> (Reporting by Maiya Keidan and Fergal Smith; Editing by Denny Thomas and Marguerita Choy)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428153406KBN2CF24K");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[4]);
			arrayValues.put("HT", "Toyota to invest $803 million in Indiana to build new SUVs");
			arrayValues.put("TE",
					"<p>WASHINGTON (Reuters) -     Toyota Motor Corp said Wednesday it will invest $803 million and add 1,400 jobs at an Indiana assembly plant to build two new larger sport utility vehicles (SUVS).</p><p>The Japanese automaker declined to say if the eight-passenger vehicles would be electric, hybrid or traditional gasoline-powered vehicles, but said one will be a Lexus model. Both will also include a semi-automated driving system allowing for periods of hands-free driving, the company said. Toyota currently builds Sienna minivans and Highlander SUVs in Indiana and employs 7,000 people at its Princeton, Indiana plant.</p><p>The company said in February it will unveil two new electric vehicles that will go on sale next year in the United States. Toyota said one the new EVs in 2022 will be an SUV.</p><p></p><p> (Reporting by David Shepardson; Editing by Chizu Nomiyama)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428142355KBN2CF1XP");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[5]);
			arrayValues.put("HT", "Exclusive-Aon&apos;s $30 billion Willis deal set to win EU approval - sources");
			arrayValues.put("TE",
					"<p>By Foo Yun Chee</p><p>BRUSSELS (Reuters) -Aon is set to gain conditional EU antitrust approval for its $30 billion bid for Willis Towers Watson, people familiar with the matter said, clearing a key hurdle to becoming the world&apos;s No. 1 insurance broker.</p><p>The sector&apos;s biggest-ever deal comes as insurers struggle with rising claims and new challenges brought on by the COVID-19 pandemic and climate change.    London-headquartered Aon, which clinched the deal a year ago to create the world&apos;s largest insurance broker ahead of Marsh &amp; McLennan Companies Inc, offered concessions to the European Commission earlier this month.    Following feedback from rivals and customers last week, the EU competition enforcer has asked for some tweaks but is unlikely to ask for more concessions, the people said.    Aon could have faced a charge sheet called a statement of objections which sets out EU concerns if market feedback had been negative and if it had then refused to offer more concessions. This is not the case now, the people said.</p><p>Aon&apos;s shares reversed losses and gained as much as 1.2% after the Reuters story while Willis also trimmed losses and rose 2.9%.    The concessions package includes selling a swathe of Willis assets, including its reinsurance arm and its German retirement benefits and consulting business, people with direct knowledge of the matter have told Reuters.</p><p>The concessions also include selling Willis&apos; insurance broking activities in France, including French unit Gras Savoye, as well as in Germany, Spain and the Netherlands. </p><p>Willis&apos; entire property and casualty business portfolio servicing large multinationals in those four countries and other European assets to service these clients, as well as its financial and professional lines, will also be sold.</p><p>The Commission, which is scheduled to decide on the deal by July 27, and Aon declined to comment.   </p><p> (Reporting by Foo Yun Chee. Editing by Jane Merriman, Mark Potter and David Evans)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428141931KBN2CF1Y8");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[6]);
			arrayValues.put("HT", "U.S. goods trade deficit vaults to record high in March");
			arrayValues.put("TE",
					"<p>By Lucia Mutikani</p><p>WASHINGTON (Reuters) -     The U.S. trade deficit in goods jumped to a record high in March, suggesting trade was a drag on economic growth in the first quarter, but that was likely offset by robust domestic demand amid massive government aid.</p><p>Economic activity in the United States has rebounded more quickly compared to its global rivals. The pent-up demand is drawing in imports, eclipsing a recovery in exports and keeping the overall trade deficit elevated. The report from the Commerce Department on Wednesday also showed inventories at retailers were drawn down in March, underscoring the strong domestic demand.</p><p>&quot;The widening in the goods deficit suggests that trade will be a drag on first-quarter GDP,&quot; said Ryan Sweet, a senior economist at Moody&apos;s Analytics in West Chester, Pennsylvania. &quot;This won&apos;t be a big issue, as other parts of the economy are still doing well, such as business investment in equipment and consumer spending.&quot;</p><p>The goods trade deficit surged 4.0% to $90.6 billion last month, the highest in the history of the series. Exports of goods accelerated 8.7% to $142.0 billion. They were boosted by shipments of motor vehicles, industrial supplies, consumer and capital goods, and food. </p><p>The jump in exports was offset by a 6.8% advance in imports to $232.6 billion. Imports rose broadly. There were large gains in imports of motor vehicles, industrial supplies, consumer goods and food. Capital goods imports also rose solidly.</p><p>Demand during the pandemic shifted to goods from services, with Americans cooped up at home. Consumption has been boosted by the very generous fiscal stimulus, including the White House&apos;s $1.9 trillion COVID-19 pandemic rescue package, which dispatched one-time $1,400 checks to qualified households and extended a $300 unemployment subsidy through early September.</p><p>Economists expect the goods trade deficit will remain large at least until year-end, with demand reverting back to services like air travel and dining out following the expansion of the COVID-19 vaccination program to all adult Americans.    </p><p>&quot;The goods deficit will start to shrink by the end of 2021 and into 2022,&quot; said Bill Adams, senior economist at PNC Financial in Pittsburgh, Pennsylvania. &quot;As the pandemic comes under control in the United States, American consumers will spend less on imported goods, shrinking imports, and foreigners will buy more U.S. exports as their economies recover further.&quot;</p><p>Stocks on Wall Street were mixed. The dollar rose against a basket of currencies. U.S. Treasury prices were mixed.</p><p>STRONG GDP GROWTH ANTICIPATED</p><p>The report was published ahead of Thursday&apos;s advance first-quarter gross domestic product data, which is expected to show the economy grew at a robust 6.1% annualized rate in the first three months of the year after expanding at a 4.3% pace in the fourth quarter, according to a Reuters survey of economists.</p><p>That would be the second-fastest growth pace since the third quarter of 2003. Strong consumer spending and business investment as well as the housing market are expected to boost growth.</p><p>Some of the goods imported in March ended up in warehouses at wholesalers, which could blunt the drag on GDP growth from trade. The Commerce Department reported wholesale inventories shot up 1.4% last month after rising 0.9% in February.</p><p>But stocks at retailers tumbled 1.4% after gaining 0.1% in February. Retail inventories excluding autos, which go into the calculation of GDP, rose 0.6% after advancing 1.4% in February.</p><p>Economists at Goldman Sachs lifted their first-quarter GDP growth estimate by three tenths of a percentage point to a 7.7% rate, noting that the drawdown in retail inventories was not as large as they had previously assumed.</p><p>Though trade flows continue to recover after being severely disrupted early in the pandemic, bottlenecks in the global supply chain remain a challenge.</p><p>&quot;Supply chain bottlenecks will probably remain a constraining factor in the near term that could weigh on trade,&quot; said Rubeela Farooqi, chief U.S. economist at High Frequency Economics in White Plains, New York. &quot;However, flows will likely bounce once restrictions on all activity are lifted globally.&quot;</p><p></p><p> (Reporting by Lucia Mutikani; Editing by Paul Simao and Chizu Nomiyama)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428140414KBN2CF1WE");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[7]);
			arrayValues.put("HT", "Nestle says 573 UK jobs at risk amid confectionery revamp");
			arrayValues.put("TE",
					"<p>ZURICH (Reuters) -     Nestle is restructuring its confectionery operations in Britain in a move that could cost nearly 600 jobs in the north east of the England, the world&apos;s biggest packed food company said on Wednesday.</p><p>&quot;We are proposing changes to adapt our confectionery manufacturing for the future with a 29.4 million pound ($40.8 million) investment at our factories in York and Halifax and the proposed closure of our Fawdon site towards the end of 2023. Regrettably, these proposals put 573 roles at risk, subject to consultation,&quot; its UK arm said in a statement.</p><p></p><p> (Reporting by Michael Shields, editing by John Revill)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428133637KBN2CF1TI");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[8]);
			arrayValues.put("HT", "U.S. auto sales to more than double in April: J.D. Power, LMC Automotive");
			arrayValues.put("TE",
					"<p>(Reuters) -     U.S. auto retail sales for April are expected to be the highest ever recorded for the month, helped by strong consumer demand and tighter inventories at dealerships, industry consultants J.D. Power and LMC Automotive said on Wednesday.</p><p>Retail sales for new vehicles in April are forecast to reach 1.3 million units, up 110.6% compared with last year, said the statement.</p><p>Total auto sales for April, including retail and non-retail, are projected to reach 1.5 million units, a 107.1% increase compared to the same period in 2020.</p><p>&quot;With the sales pace exceeding the rate at which vehicles are being produced, compounded by significant production disruption due to microchip shortages, there is a growing risk to the industry&apos;s ability to sustain the current sales pace in the coming months,&quot; said Thomas King, president of the data and analytics division at J.D. Power.</p><p>Average transaction prices are expected to rise 6.8% to $37,572, the highest ever for April, while the average incentive spending per unit is expected to fall to $3,191 from $4,953 last year.</p><p>King said low inventories &quot;have enabled manufacturers and retailers to reduce discounts&quot;, and consumers were willing to buy vehicles closer to the manufacturer&apos;s suggested retail price (MSRP) and more expensive vehicles.</p><p>The total seasonally adjusted annualized rate for the month will be 18.1 million vehicles, while it was 16.4 million units in 2019, the report said.</p><p></p><p> (Reporting by Shreyasee Raj in Bengaluru; Editing by Ramakrishnan M.)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210428:nRTROPT20210428120752KBN2CF1MN");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[9]);
			arrayValues.put("HT", "General Dynamics revenue up 7% on aerospace");
			arrayValues.put("TE",
					"<p>By Shreyasee Raj and Mike Stone</p><p>(Reuters) -     Defense contractor General Dynamics Corp on Wednesday posted a 7% rise in first-quarter revenue as its aerospace unit&apos;s sales picked up, fueled by hopes of economic recovery following mass COVID-19 vaccinations.</p><p>Shares rose in pre-market trading to a 14-month high.</p><p>The company&apos;s Gulfstream business jet deliveries rose to 28 units from 23 a year earlier, but fell from 40 in the prior quarter, amid increased coronavirus inoculations and easing travel restrictions. </p><p>While General Dynamics&apos; aerospace segment saw a slump during the global health crisis last year as its supply chain struggled, the company&apos;s bottom-line was buoyed by its robust defense unit. In 2020, 69% of its consolidated revenue was from the U.S. government.</p><p>In the quarter, sales across all four of its business segments were up compared with the same period last year. </p><p>Sales in the company&apos;s aerospace unit rose to $1.89 billion for the first quarter from $1.69 billion a year earlier, while overall revenue rose to $9.39 billion from $8.75 billion. </p><p>Marine Systems revenue was up 10.6% compared with a year ago, after a $1.9 billion award from the U.S. Navy for the construction of a tenth Virginia-class submarine.</p><p>Net earnings rose marginally to $708 million, or $2.48 per share, from $706 million, or $2.43 per share, a year earlier.</p><p></p><p> (Reporting by Shreyasee Raj in Bengaluru and Mike Stone in Washington; Editing by Ramakrishnan M. And Steve Orlofsky)</p>");
			arrayHL.put(arrayValues);

			arrayValues = new JSONObject();
			arrayValues.put("ID", "urn:newsml:onlinereport.com:20210429:nRTROPT20210429040647KBN2CG09Q");
			arrayValues.put("PR", "onlinereport.com");
			arrayValues.put("LT", dateValues[10]);
			arrayValues.put("HT", "Government money seen powering U.S. economy in first quarter");
			arrayValues.put("TE",
					"<p>By Lucia Mutikani</p><p>WASHINGTON (Reuters) -     U.S. economic growth likely accelerated in the first quarter, fueled by massive government aid to households and businesses, charting the course for what is expected to be the strongest performance this year in nearly four decades.</p><p>The United States&apos; economy is rebounding more quickly compared to its global rivals, thanks to two additional rounds of COVID-19 relief money from Washington as well as easing anxiety over the pandemic, which has boosted domestic demand and allowed services businesses like restaurants and bars to reopen.</p><p>Though the anticipated pick-up in gross domestic product last quarter would leave output just below its level at the end of 2019, the economy remains at least a couple of years away from fully recovering from the pandemic recession, which started in February 2020.</p><p>The Commerce Department will publish its snapshot of first-quarter GDP growth on Thursday at 8:30 a.m EDT (1230 GMT).</p><p>&quot;It will be a solid GDP number,&quot; said Ryan Sweet, a senior economist at Moody&apos;s Analytics in West Chester, Pennsylvania. &quot;It&apos;s one small milestone in many that we have to hit before we can say we have fully recovered from the recession.&quot;</p><p>The economy likely grew at a 6.1% annualized rate in the first three months of the year, according to a Reuters survey of economists. That would be the second-fastest GDP growth pace since the third quarter of 2003 and would follow a 4.3% rate in the fourth quarter.</p><p>The survey was, however, conducted before this week&apos;s March durable goods orders, goods trade deficit as well as wholesale and retail inventories data. Economists at Goldman Sachs initially trimmed their GDP growth estimate by one-tenth of a percentage point to a 7.4% rate after the durable goods data. </p><p>They subsequently raised the estimate to a 7.7% pace after the goods trade deficit and inventory data.</p><p>Former President Donald Trump&apos;s government provided nearly $3 trillion in relief money early in the pandemic, triggering record GDP growth in the third quarter of 2020. That was followed by nearly $900 billion in additional stimulus in late December. President Joe Biden&apos;s administration offered another $1.9 trillion rescue package in March, which sent one-time $1,400 checks to qualified households and extended a $300 unemployment subsidy through early September.</p><p>The Federal Reserve on Wednesday acknowledged the burgeoning domestic activity, but the U.S. central bank gave no sign it was ready to reduce its extraordinary support for the recovery. </p><p>PENT-UP DEMAND</p><p>The rapidly accelerating economy could dampen enthusiasm among some moderate Democrats for Biden&apos;s ambitious economic agenda. Biden on Wednesday unveiled a sweeping $1.8 trillion package for families and education in his first joint speech to Congress. Republicans oppose more stimulus, now worried about swelling debt. The new package and an earlier infrastructure and jobs plan total around $4 trillion, rivaling the annual federal budget. </p><p>There are concerns among some economists that the massive government funding could ignite inflation. Many economists, including Fed Chair Jerome Powell, expect higher inflation will be transitory, arguing that the labor market remains 8.4 million jobs below its peak in February 2020.</p><p>A separate report from the Labor Department on Thursday is expected to show 549,000 people filed for state unemployment benefits last week, according to a Reuters survey. Though claims have dropped from a record 6.149 million in early April 2020, they remain well above the range of 200,000 to 250,000 that is viewed as consistent with a healthy labor market. About 17.4 million Americans were receiving unemployment benefits in early April. </p><p>The economy continued to power ahead early in the second quarter, with consumer confidence vaulting to a 14-month high in April, thanks to the fiscal stimulus and the expansion of the COVID-19 vaccination program to all American adults. That is helping to unleash pent-up demand. </p><p>Americans have accumulated at least $2 trillion in excess savings. Many economists expect the economy will fully recover from the recession in late 2023. They expect growth this year could top 7%, which would be the fastest since 1984. The economy contracted 3.5% in 2020, its worst performance in 74 years. </p><p>&quot;Assuming vaccines remain effective against new variants of the virus, the economy should experience significant growth for the rest of the year,&quot; said Kevin Cummins, chief U.S. economist at NatWest Markets in Stamford, Connecticut.</p><p>&quot;The combination of an extraordinary amount of fiscal stimulus, highly accommodative monetary policy, an extremely positive supply shock as the economy re-opens and a pile of excess savings to support consumption make us extremely optimistic about GDP growth in 2021 and 2022.&quot; </p><p>Growth in the first quarter was likely driven by consumer spending, which is expected to have accelerated after almost braking in the final three months of 2020. Another quarter of double-digit growth is anticipated for business spending on equipment, as well as a rebound in investment in nonresidential structures such as mining exploration, shafts and wells.</p><p>Residential investment likely contributed to GDP growth for a third straight quarter. But trade was likely a drag for the third consecutive quarter as some of the robust domestic demand was satiated with imports. Strong consumption meant fewer unsold goods in warehouses, which likely resulted in inventory accumulation subtracting from GDP growth.</p><p></p><p> (Reporting by Lucia Mutikani; Editing by Andrea Ricci and Paul Simao)</p>");
			arrayHL.put(arrayValues);

			responseHL.put("HL", arrayHL);
			responseSTORYML.put("STORYML", responseHL);
			storyMLResponse.put("StoryMLResponse", responseSTORYML);
			//responseObj.put("GetSummaryByTopic_Response_1", storyMLResponse);
			//String str = storyMLResponse.toString();
			int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
			int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
			if (limit > 0 && offset >= 0) {
				JSONObject jsonPagination = pagination(storyMLResponse, limit, offset);
				String str = jsonPagination.toString();
				Result final_result= Utilities.constructResultFromJSONObject(jsonPagination);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				final_result.addParam("GetSummaryByTopic_Response_1", str);
				result.appendResult(final_result);
			} else {
				Result final_result= Utilities.constructResultFromJSONObject(storyMLResponse);
				String str = storyMLResponse.toString();
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				final_result.addParam("GetSummaryByTopic_Response_1", str);
				result.appendResult(final_result);
			}
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetTopMarketNewsMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> GetTopMarketNewsMockPostProcessor Mock - Exited ").log();
		return result;
	}
	private JSONObject pagination(JSONObject jsonResult, int limit, int offset) {
		diagnostic.prepareDebug("==========> GetTopMarketNewsMockPostProcessor Mock - pagination Entered ").log();
		String[] objectVal = new String[] {"StoryMLResponse", "STORYML" };
		JSONObject objJson = jsonResult;
		for (int i = 0; i < objectVal.length; i++) {
			objJson = objJson.getJSONObject(objectVal[i]);
		}
		JSONArray jsonArray = objJson.getJSONArray("HL");
		JSONObject response = new JSONObject();
		JSONObject responseSTORYML = new JSONObject();
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
		response.put("StoryMLResponse", responseSTORYML);
		//response.put("GetSummaryByTopic_Response_1", storyMLResponse);
		response.put("totalCount", totalcount);
		diagnostic.prepareDebug("==========> GetTopMarketNewsMockPostProcessor Mock - pagination Entered ").log();
		return response;
	}
}

