package com.temenos.infinity.api.arrangements.javaservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * This class returns cashPositions of a stipulated time period as per the duration which can be 'year','month','week'
 *
 * @author Khitish Rath
 *
 *
 */

public class FetchCashPositions implements JavaService2 {

	
    /* constants for keywords that will be used in creating */
    /* result objects. */
    private final String CREDIT = "credit";
    private final String DEBIT = "debit";
    private final String BALANCE = "total_balance";
    private final String SPAN = "span";
    private final String DURATION = "duration";

    /* constants for computing random numbers */
    private final int maxAmountMonth = 79999;
    private final int minAmountMonth = 10000;
    private final int minAmountWeek = 1000;
    private final int maxAmountWeek = 9999;
    private final int minAmountDay = 100;
    private final int maxAmountDay = 999;

    /* duration name month,week and day wise */

    private final String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
            "Dec" };
    private String[] weeks = { "First Week", "Second Week", "Third Week", "Fourth Week", "Fifth Week"};

    /* entry point of the program */
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
            DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);
        
		
        /* The duration can be 'month','year' or 'week' */
        String type = inputParams.get("Type");
        String startYear = inputParams.get("startYear");
        String endYear = inputParams.get("endYear");
        String month = inputParams.get("month");
        /* If the duration is not null */
        if (type == null ||type.isEmpty()) {
                return getResponse("Null or Empty value passed for Type", "8004");
            } else {
            	if(type.equalsIgnoreCase("yearly") && (startYear==null || endYear== null || startYear.isEmpty() ||endYear.isEmpty()))
            		return getResponse("Null or Empty value passed for Year", "8008");
            	else if (type.equalsIgnoreCase("yearly") && (Integer.parseInt(startYear)>Integer.parseInt(endYear)))
            		return getResponse("starYear has to be less than endYear", "8007");
            	else if (type.equalsIgnoreCase("monthly") && (endYear== null || endYear.isEmpty()))
            		return getResponse("Null or Empty value passed for Year", "8006");
            	else if (type.equalsIgnoreCase("weekly") && (endYear== null || month==null ||endYear.isEmpty() ||month.isEmpty()))
            		return getResponse("Null or Empty value passed for Year or Month", "8005");
            	else if (type.equalsIgnoreCase("daily") && (endYear== null || month==null||endYear.isEmpty() ||month.isEmpty()))
            		return getResponse("Null or Empty value passed for Year or Month", "8005");

                /* generate and return the dummy data */
                return generateDummyDataBy(type,startYear,endYear,month);

            }
       
    }

    /**
     * This method generates Result Object from dummy data computed on the go with random numbers
     *
     * @param type
     *            This takes 'yearly' or 'monthly' or 'weekly' or 'daily'
     * @param month 
     * 			  This takes month ex:'Jan' or 'Feb'
     * @param startYear 
     * 			  This takes start of year range ex: 2010
     * @param endYear 
     * 			  This takes end of year range ex: 2020
     * @return Result Object with all the credit,debit,total and duration as per duration.
     *
     *         e.g. for month is sends data in { span:"First Week", credit:2000, debit:1200, total:800 } ... ...
     *
     *         for year it sends data in { span:"January", credit:10000, debit:8000, total:2000 } ... ...
     *
     *         for week it sens data in { span:"January", credit:10000, debit:8000, total:2000 } ... ...
     */

    private Result generateDummyDataBy(String type, String startYear, String endYear, String month) {

        /* This gets the duration from calling method */
        String[] durations;
        /* This will have the random data generated for various durations */
        int[] cashPositions;
        
        Integer endYearInt= Integer.parseInt(endYear);
        Integer monthInt = getMonth(month);
        int day;
        	
        /* If duration is not null assign the appropriate value */
        if (type.equalsIgnoreCase("weekly")) {

            /* for weekly the span unit will be in weeks */
        	
            cashPositions = new Random().ints(minAmountWeek, maxAmountWeek).distinct().limit(18).toArray();
            Calendar calendar = Calendar.getInstance();
			if(calendar.get(Calendar.YEAR)==endYearInt && calendar.get(Calendar.MONTH)==monthInt)
            	day= calendar.get(Calendar.DAY_OF_MONTH);
            else {
            	calendar.set(endYearInt,monthInt,1);
            	day= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }	
            calendar.set(endYearInt,monthInt,day);
            int weekNumber = calendar.get(Calendar.WEEK_OF_MONTH);
            durations = Arrays.copyOfRange(weeks, 0, weekNumber);
            
        } else if (type.equalsIgnoreCase("monthly")) {

            /* for monthly the span unit will be in months */

            cashPositions = new Random().ints(minAmountMonth, maxAmountMonth).distinct().limit(18).toArray();
            Calendar calendar = Calendar.getInstance();
            if(calendar.get(Calendar.YEAR)==endYearInt ) {
            	monthInt= calendar.get(Calendar.MONTH);
            	day=calendar.get(Calendar.DAY_OF_MONTH);
            	calendar.set(endYearInt,monthInt,day);
                int monthNumber = calendar.get(Calendar.MONTH);
                durations = Arrays.copyOfRange(months, 0, monthNumber+1);
            }else { 
            	durations = months;
            }
           

        } else if (type.equalsIgnoreCase("yearly")) {

            /* for yearly the span unit will be in years */

            cashPositions = new Random().ints(minAmountMonth, maxAmountMonth).distinct().limit(18).toArray();
            int[] numbers = IntStream.rangeClosed(Integer.parseInt(startYear), endYearInt).toArray();
            String numStr=Arrays.toString(numbers);
            durations=numStr.substring(1,numStr.length()-1).split(", ");
            

        } else if (type.equalsIgnoreCase("daily")) {

            /* for daily the span unit will be in days */

            cashPositions = new Random().ints(minAmountDay, maxAmountDay).distinct().limit(18).toArray();
            Calendar calendar = Calendar.getInstance();
            if(calendar.get(Calendar.YEAR)==endYearInt && calendar.get(Calendar.MONTH)==monthInt)
            	day= (calendar.get(Calendar.DAY_OF_MONTH) - 1) ;
            else {
            	calendar.set(endYearInt,monthInt,1);
            	day= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            int[] numbers = IntStream.rangeClosed(1, 31).toArray();
            String numStr=Arrays.toString(numbers);
            String[] days =numStr.substring(1,numStr.length()-1).split(", ");
            durations = Arrays.copyOfRange(days, 0, day);
            
        } else {

            return getResponse("No data found for type : " + type, "0");
        }

        /*
         * create a random instance which will be used to create random numbers
         */
        Random rand = new Random();

        /*
         * create a result object which is to be returned to calling method this is equivalent to JSON parent object
         */
        Result result = new Result();

        /* create a data set which is equivalent to JSON Array */
        Dataset ds = new Dataset(DURATION);

        /* This will have all the records created below */
        ArrayList<Record> records = new ArrayList<>();

        for (String span : durations) {

            /* create a new record which translates to a one JSON object */
            Record record = new Record();

            /* create credit , debit from random number and compute balance */
            int credit = cashPositions[getRandomNumber(rand)];
            int debit = credit - ((credit * (new Random().nextInt(80))) / 100);
            int balance = credit - debit;

            /* add parameters to the JSON object equivalent created above */

            record.addParam(new Param(SPAN, span, "string"));
            record.addParam(new Param(CREDIT, String.valueOf(credit), "number"));
            record.addParam(new Param(DEBIT, String.valueOf(debit), "number"));
            record.addParam(new Param(BALANCE, String.valueOf(balance), "number"));

            /* add the JSON Object to the array */
            records.add(record);

        }
        /* add all the records to data set */
        ds.addAllRecords(records);

        /* add data set to result */
        result.addDataset(ds);

        /* a add operation status */
        result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "0", "string"));
        return result;

    }

    /**
     * This method returns a failure result object
     * 
     * @param message
     *            the message to be sent in response
     * @param statusCode
     *            the Infinity error code to be sent in response
     * @see <a href= "http://docs.kony.com/konylibrary/sync/kmf_sync_orm_api_guide/Content/Error%20Handling.htm">Kony
     *      Error Code</a>
     *
     * @return result
     */

    private Result getResponse(String message, String statusCode) {
        Result result = new Result();
        result.addParam(new Param("dbpErrCode", statusCode, "number"));
        result.addParam(new Param("dbpErrMsg", message, "string"));

        return result;
    }

    /**
     * Helper method which generates a random number
     * 
     * @param rand
     *            Random Object
     * @return random number
     */

    private int getRandomNumber(Random rand) {
        return rand.nextInt(18);
    }
    
    private int getMonth(String month) {
    	for (int i=0;i<months.length;i++) {
    		if(months[i].equalsIgnoreCase(month))
    			return i;
    	}
    	return 11;
    	
    }
}
