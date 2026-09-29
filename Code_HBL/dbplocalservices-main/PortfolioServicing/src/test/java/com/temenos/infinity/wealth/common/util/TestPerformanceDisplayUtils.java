package com.temenos.infinity.wealth.common.util;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TestPerformanceDisplayUtils does the following:-
 *   1) Test the getPerformanceForDashboard for YTD (Year To Date)
 *   2) Test the getPerformanceForDashboard for OneY (One Year)
 *   3) Test the getPerformanceForPortfolio for YTD (Year To Date)
 *   4) Test the getPerformanceForPortfolio for OneY (One Year)
 *   with sample request & response data
 *   
 * @author Rajesh Kappera
 */

public class TestPerformanceDisplayUtils {
	
	private static String graphDataForDashboard;
	private static String graphDataForPortfolio;
	
	private static String perfMetricYTD;
	private static String perfMetricOneY;
	private static int year;
	private static String currentDate;
	
	@BeforeClass
	public static void initializeData() {
		graphDataForDashboard = "{\"array\": [{\"PERIOD_DISPLAY\": \"JAN 2023\",\"seqno\": \"1\",\"PERIOD_INITIAL_DATE\": \"2022-12-31\",\"PERIOD_INITIAL_MKT_VAL\": \"-6000000\",\"DIFF_PERF\": \"1.6666667\",\"PERIOD_FINAL_MKT_VAL\": \"-6100000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-01-31\",\"PERIOD_GAIN_LOSS\": \"-100000\",\"PERIOD_RET_TWR\": \"1.6666667\"},{\"PERIOD_DISPLAY\": \"FEB 2023\",\"seqno\": \"2\",\"PERIOD_INITIAL_DATE\": \"2023-01-31\",\"PERIOD_INITIAL_MKT_VAL\": \"-6100000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-6100000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-02-28\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"MAR 2023\",\"seqno\": \"3\",\"PERIOD_INITIAL_DATE\": \"2023-02-28\",\"PERIOD_INITIAL_MKT_VAL\": \"-6100000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-2.51E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-03-31\",\"PERIOD_GAIN_LOSS\": \"-1.9E7\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"APR 2023\",\"seqno\": \"4\",\"PERIOD_INITIAL_DATE\": \"2023-03-31\",\"PERIOD_INITIAL_MKT_VAL\": \"-2.51E7\",\"DIFF_PERF\": \"4.252782851\",\"PERIOD_FINAL_MKT_VAL\": \"7.786060291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-04-30\",\"PERIOD_GAIN_LOSS\": \"561202.91\",\"PERIOD_RET_TWR\": \"4.252782851\"},{\"PERIOD_DISPLAY\": \"MAY 2023\",\"seqno\": \"5\",\"PERIOD_INITIAL_DATE\": \"2023-04-30\",\"PERIOD_INITIAL_MKT_VAL\": \"7.786060291E7\",\"DIFF_PERF\": \"-11.559119328\",\"PERIOD_FINAL_MKT_VAL\": \"6.886060291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-05-31\",\"PERIOD_GAIN_LOSS\": \"-9000000\",\"PERIOD_RET_TWR\": \"-11.559119328\"},{\"PERIOD_DISPLAY\": \"JUN 2023\",\"seqno\": \"6\",\"PERIOD_INITIAL_DATE\": \"2023-05-31\",\"PERIOD_INITIAL_MKT_VAL\": \"6.886060291E7\",\"DIFF_PERF\": \"-2.468755614\",\"PERIOD_FINAL_MKT_VAL\": \"6.716060291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-06-30\",\"PERIOD_GAIN_LOSS\": \"-1700000\",\"PERIOD_RET_TWR\": \"-2.468755614\"},{\"PERIOD_DISPLAY\": \"JUL 2023\",\"seqno\": \"7\",\"PERIOD_INITIAL_DATE\": \"2023-06-30\",\"PERIOD_INITIAL_MKT_VAL\": \"6.716060291E7\",\"DIFF_PERF\": \"-7.444840842\",\"PERIOD_FINAL_MKT_VAL\": \"6.216060291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-07-31\",\"PERIOD_GAIN_LOSS\": \"-5000000\",\"PERIOD_RET_TWR\": \"-7.444840842\"},{\"PERIOD_DISPLAY\": \"AUG 2023\",\"seqno\": \"8\",\"PERIOD_INITIAL_DATE\": \"2023-07-31\",\"PERIOD_INITIAL_MKT_VAL\": \"6.216060291E7\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"6.216060291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-08-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"SEP 2023\",\"seqno\": \"9\",\"PERIOD_INITIAL_DATE\": \"2023-08-31\",\"PERIOD_INITIAL_MKT_VAL\": \"6.216060291E7\",\"DIFF_PERF\": \"-30.267981865\",\"PERIOD_FINAL_MKT_VAL\": \"4.334584291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-09-30\",\"PERIOD_GAIN_LOSS\": \"-1.881476E7\",\"PERIOD_RET_TWR\": \"-30.267981865\"},{\"PERIOD_DISPLAY\": \"OCT 2023\",\"seqno\": \"10\",\"PERIOD_INITIAL_DATE\": \"2023-09-30\",\"PERIOD_INITIAL_MKT_VAL\": \"4.334584291E7\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"4.334584291E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-10-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"NOV 2023\",\"seqno\": \"11\",\"PERIOD_INITIAL_DATE\": \"2023-10-31\",\"PERIOD_INITIAL_MKT_VAL\": \"4.334584291E7\",\"DIFF_PERF\": \"-20.732629618\",\"PERIOD_FINAL_MKT_VAL\": \"5.588595355E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-11-28\",\"PERIOD_GAIN_LOSS\": \"-9559889.36\",\"PERIOD_RET_TWR\": \"-20.732629618\"},{\"PERIOD_DISPLAY\": \"2023\",\"seqno\": \"12\",\"PERIOD_INITIAL_DATE\": \"2022-12-31\",\"PERIOD_INITIAL_MKT_VAL\": \"-6000000\",\"DIFF_PERF\": \"-53.22752164\",\"PERIOD_FINAL_MKT_VAL\": \"5.588595355E7\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-11-28\",\"PERIOD_GAIN_LOSS\": \"-6.261344645E7\",\"PERIOD_RET_TWR\": \"-53.22752164\"}]}";
		graphDataForPortfolio = "{\"array\": [{\"PERIOD_DISPLAY\": \"JAN 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"1\",\"PERIOD_INITIAL_DATE\": \"2022-12-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-6000000\",\"DIFF_PERF\": \"1.6666667\",\"PERIOD_FINAL_MKT_VAL\": \"-6100000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-01-31\",\"PERIOD_GAIN_LOSS\": \"-100000\",\"PERIOD_RET_TWR\": \"1.6666667\"},{\"PERIOD_DISPLAY\": \"FEB 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"2\",\"PERIOD_INITIAL_DATE\": \"2023-01-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-6100000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-6100000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-02-28\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"MAR 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"3\",\"PERIOD_INITIAL_DATE\": \"2023-02-28\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-6100000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-6100000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-03-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"APR 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"4\",\"PERIOD_INITIAL_DATE\": \"2023-03-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-6100000\",\"DIFF_PERF\": \"-3.30033\",\"PERIOD_FINAL_MKT_VAL\": \"-5800000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-04-30\",\"PERIOD_GAIN_LOSS\": \"200000\",\"PERIOD_RET_TWR\": \"-3.30033\"},{\"PERIOD_DISPLAY\": \"MAY 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"5\",\"PERIOD_INITIAL_DATE\": \"2023-04-30\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5800000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-5800000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-05-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"JUN 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"6\",\"PERIOD_INITIAL_DATE\": \"2023-05-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5800000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-5800000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-06-30\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"JUL 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"7\",\"PERIOD_INITIAL_DATE\": \"2023-06-30\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5800000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-5800000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-07-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"AUG 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"8\",\"PERIOD_INITIAL_DATE\": \"2023-07-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5800000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-5800000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-08-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"SEP 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"9\",\"PERIOD_INITIAL_DATE\": \"2023-08-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5800000\",\"DIFF_PERF\": \"-4.3103448\",\"PERIOD_FINAL_MKT_VAL\": \"-5550000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-09-30\",\"PERIOD_GAIN_LOSS\": \"250000\",\"PERIOD_RET_TWR\": \"-4.3103448\"},{\"PERIOD_DISPLAY\": \"OCT 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"10\",\"PERIOD_INITIAL_DATE\": \"2023-09-30\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5550000\",\"DIFF_PERF\": \"0\",\"PERIOD_FINAL_MKT_VAL\": \"-5550000\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-10-31\",\"PERIOD_GAIN_LOSS\": \"0\",\"PERIOD_RET_TWR\": \"0\"},{\"PERIOD_DISPLAY\": \"NOV 2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"11\",\"PERIOD_INITIAL_DATE\": \"2023-10-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-5550000\",\"DIFF_PERF\": \"20.8989074\",\"PERIOD_FINAL_MKT_VAL\": \"-6709889.36\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-11-28\",\"PERIOD_GAIN_LOSS\": \"-1159889.36\",\"PERIOD_RET_TWR\": \"20.8989074\"},{\"PERIOD_DISPLAY\": \"2023\",\"PORTFOLIO_NAME\": \"My Investment Account 1\",\"seqno\": \"12\",\"PERIOD_INITIAL_DATE\": \"2022-12-31\",\"PORTFOLIO_CODE\": \"190345-3\",\"PERIOD_INITIAL_MKT_VAL\": \"-6000000\",\"DIFF_PERF\": \"13.734164731\",\"PERIOD_FINAL_MKT_VAL\": \"-6709889.36\",\"REF_CURRENCY\": \"USD\",\"PERIOD_FINAL_DATE\": \"2023-11-28\",\"PERIOD_GAIN_LOSS\": \"-809889.36\",\"PERIOD_RET_TWR\": \"13.734164731\"}]}";
		
		perfMetricYTD = "YTD";
		perfMetricOneY = "1Y";
		year = 2023;
		currentDate = "2023-11-28";
	}

	@Test
	public void testGetPerformanceForDashboardYTD() {
		
		JSONObject actualResultDashboard = PerformanceDisplayUtils.getPerformanceForDashboard(new JSONObject(graphDataForDashboard).getJSONArray("array"), perfMetricYTD, year);
		
		assertNotNull(actualResultDashboard);
		
		if (!(perfMetricYTD.equalsIgnoreCase(actualResultDashboard.getString("performance")) || perfMetricOneY.equalsIgnoreCase(actualResultDashboard.getString("performance")))) {
			fail();
		}
		
		assertNotNull(actualResultDashboard.get("performanceValue"));
		assertNotEquals("", actualResultDashboard.get("performanceValue"));
	}
	
	@Test
	public void testGetPerformanceForDashboardOneY() {
		
		JSONObject actualResultDashboard = PerformanceDisplayUtils.getPerformanceForDashboard(new JSONObject(graphDataForDashboard).getJSONArray("array"), perfMetricOneY, year);
		
		assertNotNull(actualResultDashboard);
		
		if (!(perfMetricYTD.equalsIgnoreCase(actualResultDashboard.getString("performance")) || perfMetricOneY.equalsIgnoreCase(actualResultDashboard.getString("performance")))) {
			fail();
		}
		
		assertNotNull(actualResultDashboard.get("performanceValue"));
		assertNotEquals("", actualResultDashboard.get("performanceValue"));
	}

	@Test
	public void testGetPerformanceForPortfolioYTD() {

		JSONObject actualResultPortfolio = PerformanceDisplayUtils.getPerformanceForPortfolio(new JSONObject(graphDataForPortfolio).getJSONArray("array"), perfMetricYTD, year, currentDate);
		
		assertNotNull(actualResultPortfolio);
		
		if (!(perfMetricYTD.equalsIgnoreCase(actualResultPortfolio.getString("performance")) || perfMetricOneY.equalsIgnoreCase(actualResultPortfolio.getString("performance")))) {
			fail();
		}
		
		assertNotNull(actualResultPortfolio.get("performanceValue"));
		assertNotEquals("", actualResultPortfolio.get("performanceValue"));
	}
	
	@Test
	public void testGetPerformanceForPortfolioOneY() {

		JSONObject actualResultPortfolio = PerformanceDisplayUtils.getPerformanceForPortfolio(new JSONObject(graphDataForPortfolio).getJSONArray("array"), perfMetricOneY, year, currentDate);
		
		assertNotNull(actualResultPortfolio);
		
		if (!(perfMetricYTD.equalsIgnoreCase(actualResultPortfolio.getString("performance")) || perfMetricOneY.equalsIgnoreCase(actualResultPortfolio.getString("performance")))) {
			fail();
		}
		
		assertNotNull(actualResultPortfolio.get("performanceValue"));
		assertNotEquals("", actualResultPortfolio.get("performanceValue"));
	}
}
