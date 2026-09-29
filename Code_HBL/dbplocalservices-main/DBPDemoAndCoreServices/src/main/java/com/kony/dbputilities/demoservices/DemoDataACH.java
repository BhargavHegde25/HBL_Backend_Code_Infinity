package com.kony.dbputilities.demoservices;

import java.util.HashMap;

import com.kony.dbputilities.util.HelperMethods;

public class DemoDataACH {

	static String ACHTemplate1Records = "["
            + "{\"Record_Name\":\"John Brown\",\"ToAccountNumber\":\"9867503739107120\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"134666668\",\"Detail_id\":\"1565 \",\"Amount\":\"19000\"},"
            + "{\"Record_Name\":\"Henry Black\",\"ToAccountNumber\":\"4876986750373910\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"758999992\",\"Detail_id\":\"3485\",\"Amount\":\"15000\"},"
            + "{\"Record_Name\":\"Linda Gray\",\"ToAccountNumber\":\"6654487698675030\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"576777776\",\"Detail_id\":\"4532\",\"Amount\":\"7000\"},"
            + "{\"Record_Name\":\"Mary Green\",\"ToAccountNumber\":\"5345665448769860\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"285888884\",\"Detail_id\":\"8694\",\"Amount\":\"12500\"},"
            + "{\"Record_Name\":\"Rick White\",\"ToAccountNumber\":\"4789534566544870\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"375555550\",\"Detail_id\":\"2356\",\"Amount\":\"10000\"}"
            + "]";
    static String ACHTemplate1 = "{\n" + "	\"MaxAmount\": \"100000\",\n" + "	\"TemplateType_id\": \"1\",\n"
            + "	\"TemplateRequestType_id\": \"2\",\n"
            + "	\"TemplateRequestTypeValue\": \"PPD (Prearranged Payment and Deposit)\",\n"
            + "	\"TransactionType_id\": \"2\",\n" + "	\"TransactionTypeValue\": \"Payment\",\n"
            + "	\"Status\":\"6\",\n" + "	\"TemplateName\": \"Monthly Pay\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE_TEMPLATE\",\n"
            + "	\"TemplateDescription\": \"Weekly Employee Payroll\",\n" + "	\"TotalAmount\": 63500,\n"
            + " \"Records\": "+ACHTemplate1Records+"}";
    
    static String ACHTemplate2Records =
            "[{\"EIN\":\"285484846\",\"ToAccountNumber\":\"4876986750376580\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"81203790\",\"IsZeroTaxDue\":\"0\",\"TaxType_id\":\"9\",\"SubRecords\":[{\"Amount\":\"200\",\"TaxSubCategory_id\":\"20\"}]},"
                    + "{\"EIN\":\"285484846\",\"ToAccountNumber\":\"4876986750376580\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"81203790\",\"IsZeroTaxDue\":\"0\",\"TaxType_id\":\"9\",\"SubRecords\":[{\"Amount\":\"100\",\"TaxSubCategory_id\":\"21\"}]},"
                    + "{\"EIN\":\"285484846\",\"ToAccountNumber\":\"4876986750376580\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"81203790\",\"IsZeroTaxDue\":\"0\",\"TaxType_id\":\"9\",\"SubRecords\":[{\"Amount\":\"450\",\"TaxSubCategory_id\":\"22\"}]}]";
    static String ACHTemplate2 = "{\"MaxAmount\": \"1000\",\n" + "	\"TemplateType_id\": \"1\",\n"
            + "	\"TemplateRequestType_id\": \"8\",\n" + "	\"TemplateRequestTypeValue\": \"Federal Tax\",\n"
            + "	\"TransactionType_id\": \"2\",\n" + "	\"TransactionTypeValue\": \"Payment\",\n"
            + "	\"TemplateName\": \"Quarterly Tax Return\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE_TEMPLATE\",\n"
            + "	\"TemplateDescription\": \"Quarterly Federal Tax Payment\",\n" + "	\"TotalAmount\": 750,\n"
    		+ " \"Records\": "+ACHTemplate2Records+"}";

    static String ACHTemplate3Records =
            "[{\"Record_Name\":\"Brown Industries Ltd\",\"ToAccountNumber\":\"9867503739107450\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"134896661\",\"Detail_id\":\"1565\",\"Amount\":\"2576\",\"AdditionalInfo\":\"51*U*00401*00000001*0*P*~\\\\GS*RA*111111111*222222222*55555555*1800*00000001*X*0040\"},"
                    + "{\"Record_Name\":\"BlueBird Solutions\",\"ToAccountNumber\":\"4876986750373270\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"758999978\",\"Detail_id\":\"3485\",\"Amount\":\"3500\",\"AdditionalInfo\":\"510ST*800\\\\ST*800*1\\\\BPR*C*1*C*ACH*CTX*01*111111111*DA*2112*9ACHFILE992**01*7701100\"}]";
    static String ACHTemplate3 = "			{\"MaxAmount\": \"7000\",\n" + "			\"TemplateType_id\": \"1\",\n"
            + "			\"TemplateRequestType_id\": \"6\",\n"
            + "			\"TemplateRequestTypeValue\": \"CTX (Corporate Trade Exchange)\",\n"
            + "			\"TransactionType_id\": \"2\",\n" + "			\"TransactionTypeValue\": \"Payment\",\n"
            + "			\"TemplateName\": \"Corporate Trading\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE_TEMPLATE\",\n"
            + "			\"TemplateDescription\": \"Large Trade Payments\",\n" + "			\"TotalAmount\": 6076,\n"
            + " \"Records\": "+ACHTemplate3Records+"}";

    static String ACHTemplate4Records =
            "[{\"Record_Name\":\"Southern Concepts Inc\",\"ToAccountNumber\":\"9867503739107390\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"654666668\",\"Detail_id\":\"9032\",\"Amount\":\"190\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Custom Air Sys\",\"ToAccountNumber\":\"4876986750373280\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"348999992\",\"Detail_id\":\"1749\",\"Amount\":\"150\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Hilton\",\"ToAccountNumber\":\"6654487698675590\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"766777776\",\"Detail_id\":\" 3921\",\"Amount\":\"734\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Signs Now\",\"ToAccountNumber\":\"5345665448769840\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"785888884\",\"Detail_id\":\"47923\",\"Amount\":\"122\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Winslows Deli\",\"ToAccountNumber\":\"4789534566548370\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"895555550\",\"Detail_id\":\"389222\",\"Amount\":\"100\",\"AdditionalInfo\":\"\"}]";
    static String ACHTemplate4 = "	{\"MaxAmount\": \"2000\",\n" + "	\"TemplateType_id\": \"1\",\n"
            + "	\"TemplateRequestType_id\": \"3\",\n"
            + "	\"TemplateRequestTypeValue\": \"CCD (Cash Concentration and Disbursement)\",\n"
            + "	\"TransactionType_id\": \"1\",\n" + "	\"TransactionTypeValue\": \"Collection\",\n"
            + "	\"TemplateName\": \"Monthly Management Fees\",\n"
            + "	\"featureActionId\": \"ACH_COLLECTION_CREATE_TEMPLATE\",\n"
            + "	\"TemplateDescription\": \"Service Fees Collection\",\n" + "	\"TotalAmount\": 1296,\n"
            + " \"Records\": "+ACHTemplate4Records+"}";
    
    static String ACHTemplate5Records =
            "[{\"Record_Name\":\"Southern Concepts Inc\",\"ToAccountNumber\":\"9867503739107390\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"654666668\",\"Detail_id\":\"9032\",\"Amount\":\"190\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Custom Air Sys\",\"ToAccountNumber\":\"4876986750373280\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"348999992\",\"Detail_id\":\"1749\",\"Amount\":\"150\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Hilton\",\"ToAccountNumber\":\"6654487698675590\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"766777776\",\"Detail_id\":\"3921\",\"Amount\":\"734\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Signs Now\",\"ToAccountNumber\":\"5345665448769840\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"785888884\",\"Detail_id\":\"47923\",\"Amount\":\"122\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Winslows Deli\",\"ToAccountNumber\":\"4789534566548370\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"895555550\",\"Detail_id\":\"389222\",\"Amount\":\"100\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Alan Arts\",\"ToAccountNumber\":\"6654487698675550\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"86501578\",\"Detail_id\":\"5647\",\"Amount\":\"4724\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Armstrong Systems\",\"ToAccountNumber\":\"4789534566548340\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"63100277\",\"Detail_id\":\"5632\",\"Amount\":\"484\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Claudia Associates\",\"ToAccountNumber\":\"7584904767382020\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"104000029\",\"Detail_id\":\"5563\",\"Amount\":\"758\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"Douglas Paper Supplies\",\"ToAccountNumber\":\"6675830204855670\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"107000327\",\"Detail_id\":\"561356\",\"Amount\":\"573\",\"AdditionalInfo\":\"\"},"
                    + "{\"Record_Name\":\"George Tech Ltd\",\"ToAccountNumber\":\"4658492047647290\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"122187238\",\"Detail_id\":\"2358\",\"Amount\":\"383\",\"AdditionalInfo\":\"\"}]";
    static String ACHTemplate5 = "{\"MaxAmount\": \"9000\",\n" + "	\"TemplateType_id\": \"1\",\n"
            + "	\"TemplateRequestType_id\": \"3\",\n"
            + "	\"TemplateRequestTypeValue\": \"CCD (Cash Concentration and Disbursement)\",\n"
            + "	\"TransactionType_id\": \"1\",\n" + "	\"TransactionTypeValue\": \"Collection\",\n"
            + "	\"TemplateName\": \"Cash Concentration\",\n"
            + "	\"featureActionId\": \"ACH_COLLECTION_CREATE_TEMPLATE\",\n"
            + "	\"TemplateDescription\": \"Supplier Payment Collection\",\n" + "	\"TotalAmount\": 8218,\n"
            + " \"Records\": "+ACHTemplate5Records+"}";

    static String ACHTransaction1 = "{" + "\"BBGeneralTransactionType_id\": \"6\",\n"
            + "	\"TemplateType_id\": \"1\",\n" + "	\"TemplateTypeValue\": \"ACH\",\n" + "	\"Status\": \"Pending\",\n"
            + "	\"TemplateRequestType_id\": \"2\",\n" + "	\"softDelete\": \"0\",\n"
            + "	\"TransactionType_id\": \"2\",\n" + "	\"TransactionTypeValue\": \"Payment\",\n"
            + "	\"StatusValue\": \"Pending\",\n" + "	\"RequestType\": \"PPD Payment\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE\",\n"
            + "	\"DebitAccount\": \"190218142950916\",\n" + "	\"AccountName\": \"Rewards Savings\",\n"
            + "	\"TemplateDescription\": \"Weekly Employee Payroll\",\n" + "	\"TemplateName\": \"Monthly Pay\",\n"
            + "	\"MaxAmount\": \"100000\",\n" + "	\"CreatedOn\": \"02/21/2019\",\n" + "	\"Template_id\": \"42\",\n"
            + "	\"EffectiveDate\": \"2019-02-25\",\n" + "	\"TotalAmount\": 63500,\n"
            + "	\"LastUsed\": \"1919-01-31T18:30:00.000Z\"" + "}";

    static String ACHTransaction1Records =
            "[{\"Record_Name\":\"John Brown\",\"ToAccountNumber\":\"9867503739107120\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"134666668\",\"Detail_id\":\"1565 \",\"Amount\":\"19000\"},"
                    + "{\"Record_Name\":\"Henry Black\",\"ToAccountNumber\":\"4876986750373910\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"758999992\",\"Detail_id\":\"3485\",\"Amount\":\"15000\"},"
                    + "{\"Record_Name\":\"Linda Gray\",\"ToAccountNumber\":\"6654487698675030\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"576777776\",\"Detail_id\":\"4532\",\"Amount\":\"7000\"},"
                    + "{\"Record_Name\":\"Mary Green\",\"ToAccountNumber\":\"5345665448769860\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"285888884\",\"Detail_id\":\"8694\",\"Amount\":\"12500\"},"
                    + "{\"Record_Name\":\"Rick White\",\"ToAccountNumber\":\"4789534566544870\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"375555550\",\"Detail_id\":\"2356\",\"Amount\":\"10000\"}]";

    static String ACHTransaction2 = "{" + "\"BBGeneralTransactionType_id\": \"7\",\n"
            + "	\"TemplateType_id\": \"1\",\n" + "	\"TemplateTypeValue\": \"ACH\",\n" + "	\"Status\": \"Executed\",\n"
            + "	\"TemplateRequestType_id\": \"3\",\n" + "	\"TransactionType_id\": \"1\",\n"
            + "	\"featureActionId\": \"ACH_COLLECTION_CREATE\",\n"
            + "	\"TransactionTypeValue\": \"Collection\",\n" + "	\"StatusValue\": \"Executed\",\n"
            + "	\"RequestType\": \"CCD Collection\",\n" + "	\"DebitAccount\": \"190218142950916\",\n"
            + "	\"AccountName\": \"Rewards Savings\",\n" + "	\"TemplateDescription\": \"Service Fees Collection\",\n"
            + "	\"TemplateName\": \"Monthly Management Fees\",\n" + "	\"MaxAmount\": \"2000\",\n"
            + "	\"CreatedOn\": \"02/21/2019\",\n" + "	\"Template_id\": \"45\",\n"
            + "	\"EffectiveDate\": \"2019-02-25\",\n" + "	\"TotalAmount\": 1296,\n"
            + "	\"LastUsed\": \"1919-01-31T18:30:00.000Z\"}";

    static String ACHTransaction2Records =
            "[{\"Record_Name\":\"Southern Concepts Inc\",\"ToAccountNumber\":\"9867503739107390\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"654666668\",\"Detail_id\":\"9032\",\"Amount\":\"190\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Custom Air Sys\",\"ToAccountNumber\":\"4876986750373280\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"348999992\",\"Detail_id\":\"1749\",\"Amount\":\"150\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Hilton\",\"ToAccountNumber\":\"6654487698675590\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"766777776\",\"Detail_id\":\" 3921\",\"Amount\":\"734\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Signs Now\",\"ToAccountNumber\":\"5345665448769840\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"785888884\",\"Detail_id\":\"47923\",\"Amount\":\"122\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Winslows Deli\",\"ToAccountNumber\":\"4789534566548370\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"895555550\",\"Detail_id\":\"389222\",\"Amount\":\"100\",\"AdditionalInfo\":null}]";

    static String ACHTransaction3 = "{" + "\"MaxAmount\": \"1500\",\n" + "	\"TemplateType_id\": \"1\",\n"
            + "	\"TemplateRequestType_id\": \"2\",\n"+ "	\"Status\": \"Executed\",\n"
            + "	\"TemplateRequestTypeValue\": \"PPD (Prearranged Payment and Deposit)\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE\",\n"
            + "	\"TransactionType_id\": \"2\",\n" + "	\"BBGeneralTransactionType_id\": \"6\",\n"
            + "	\"TransactionTypeValue\": \"Payment\",\n" + "	\"DebitAccount\": \"190218142950916\",\n"
            + "	\"DebitAccountName\": \"Rewards Savings -  XXXXXXXXXXX0916\",\n"
            + "	\"EffectiveDate\": \"2019-02-27\",\n" + "	\"TotalAmount\": 1500" + "}";

    static String ACHTransaction3Records =
            "[{\"Record_Name\":\"Jack Black\",\"ToAccountNumber\":\"9867503739107870\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"135666668\",\"Detail_id\":\"1543\",\"Amount\":350},"
                    + "{\"Record_Name\":\"Carley Corbell\",\"ToAccountNumber\":\"4876986750373780\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"754599992\",\"Detail_id\":\"5463\",\"Amount\":350},"
                    + "{\"Record_Name\":\"Tania Teasley\",\"ToAccountNumber\":\"6654487698675670\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"578977776\",\"Detail_id\":\"9673\",\"Amount\":350},"
                    + "{\"Record_Name\":\"Nora Phillips\",\"ToAccountNumber\":\"5345665448769240\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"284588884\",\"Detail_id\":\"6956\",\"Amount\":\"450\"}]";

    static String ACHTransaction4 = "{" + "\"BBGeneralTransactionType_id\": \"6\",\n"
            + "	\"TemplateType_id\": \"1\",\n" + "	\"TemplateTypeValue\": \"ACH\",\n" + "	\"Status\": \"Executed\",\n"
            + "	\"TemplateRequestType_id\": \"6\",\n" + "	\"TransactionType_id\": \"2\",\n"
            + "	\"TransactionTypeValue\": \"Payment\",\n" + "	\"StatusValue\": \"Executed\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE\",\n"
            + "	\"RequestType\": \"CTX Payment\",\n" + "	\"DebitAccount\": \"190218142950916\",\n"
            + "	\"AccountName\": \"Rewards Savings\",\n" + "	\"TemplateDescription\": \"Large Trade Payments\",\n"
            + "	\"TemplateName\": \"Corporate Trading\",\n" + "	\"MaxAmount\": \"7000\",\n"
            + "	\"CreatedOn\": \"02/21/2019\",\n" + "	\"Template_id\": \"44\",\n"
            + "	\"EffectiveDate\": \"2019-02-26\",\n" + "	\"Approver\": \"N/A\",\n" + "	\"TotalAmount\": 6076,\n"
            + "	\"LastUsed\": \"1919-01-31T18:30:00.000Z\"" + "}";

    static String ACHTransaction4Records =
            "[{\"Record_Name\":\"Brown Industries Ltd\",\"ToAccountNumber\":\"9867503739107450\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"134896661\",\"Detail_id\":\"1565\",\"Amount\":\"2576\",\"AdditionalInfo\":\"51*U*00401*00000001*0*P*~\\\\GS*RA*111111111*222222222*55555555*1800*00000001*X*0040\"},"
                    + "{\"Record_Name\":\"BlueBird Solutions\",\"ToAccountNumber\":\"4876986750373270\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"758999978\",\"Detail_id\":\"3485\",\"Amount\":\"3500\",\"AdditionalInfo\":\"510ST*800\\\\ST*800*1\\\\BPR*C*1*C*ACH*CTX*01*111111111*DA*2112*9ACHFILE992**01*7701100\"}]";

    static String ACHTransaction5 = "{" + "\"BBGeneralTransactionType_id\": \"7\",\n"
            + "	\"TemplateType_id\": \"1\",\n" + "	\"TemplateTypeValue\": \"ACH\",\n" + "	\"Status\": \"Executed\",\n"
            + "	\"TemplateRequestType_id\": \"3\",\n" + "	\"TransactionType_id\": \"1\",\n"
            + "	\"TransactionTypeValue\": \"Collection\",\n" + "	\"StatusValue\": \"Executed\",\n"
            + "	\"RequestType\": \"CCD Collection\",\n" + "	\"DebitAccount\": \"190218142950916\",\n"
            + "	\"AccountName\": \"Rewards Savings\",\n"
            + "	\"TemplateDescription\": \"Supplier Payment Collection\",\n"
            + "	\"featureActionId\": \"ACH_COLLECTION_CREATE\",\n"
            + "	\"TemplateName\": \"Cash Concentration\",\n" + "	\"MaxAmount\": \"9000\",\n"
            + "	\"CreatedOn\": \"02/21/2019\",\n" + "	\"Template_id\": \"46\",\n"
            + "	\"EffectiveDate\": \"2019-02-26\",\n" + "	\"TotalAmount\": 8218,\n"
            + "	\"LastUsed\": \"1919-01-31T18:30:00.000Z\"" + "}";

    static String ACHTransaction5Records =
            "[{\"Record_Name\":\"Southern Concepts Inc\",\"ToAccountNumber\":\"9867503739107390\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"654666668\",\"Detail_id\":\"9032\",\"Amount\":\"190\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Custom Air Sys\",\"ToAccountNumber\":\"4876986750373280\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"348999992\",\"Detail_id\":\"1749\",\"Amount\":\"150\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Hilton\",\"ToAccountNumber\":\"6654487698675590\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"766777776\",\"Detail_id\":\"3921\",\"Amount\":\"734\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Signs Now\",\"ToAccountNumber\":\"5345665448769840\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"785888884\",\"Detail_id\":\"47923\",\"Amount\":\"122\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Winslows Deli\",\"ToAccountNumber\":\"4789534566548370\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"895555550\",\"Detail_id\":\"389222\",\"Amount\":\"100\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Alan Arts\",\"ToAccountNumber\":\"6654487698675550\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"86501578\",\"Detail_id\":\"5647\",\"Amount\":\"4724\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Armstrong Systems\",\"ToAccountNumber\":\"4789534566548340\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"63100277\",\"Detail_id\":\"5632\",\"Amount\":\"484\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Claudia Associates\",\"ToAccountNumber\":\"7584904767382020\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"104000029\",\"Detail_id\":\"5563\",\"Amount\":\"758\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"Douglas Paper Supplies\",\"ToAccountNumber\":\"6675830204855670\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"107000327\",\"Detail_id\":\"561356\",\"Amount\":\"573\",\"AdditionalInfo\":null},"
                    + "{\"Record_Name\":\"George Tech Ltd\",\"ToAccountNumber\":\"4658492047647290\",\"ToAccountType\":\"1\",\"ABATRCNumber\":\"122187238\",\"Detail_id\":\"2358\",\"Amount\":\"383\",\"AdditionalInfo\":null}]";

    static String ACHTransaction6 = "{" + "\"BBGeneralTransactionType_id\": \"6\",\n"
            + "	\"CreatedBy\": \"kmanthinasb1\",\n" + "	\"TemplateType_id\": \"1\",\n"
            + "	\"CompanyName\": \"SB Davis Furniture Inc._kony.dbx50SB\",\n" + "	\"TemplateTypeValue\": \"ACH\",\n"
            + "	\"Status\": \"6\",\n" + "	\"TemplateRequestType_id\": \"8\",\n" + "	\"softDelete\": \"0\",\n"
            + "	\"TransactionType_id\": \"2\",\n" + "	\"Company_id\": \"24\",\n"
            + "	\"TransactionTypeValue\": \"Payment\",\n" + "	\"StatusValue\": \"New\",\n"
            + "	\"RequestType\": \"Federal Tax\",\n" + "	\"DebitAccount\": \"190218142950916\",\n"
            + "	\"AccountName\": \"Rewards Savings\",\n" + "	\"LastUsed\": \"1919-01-31T18:30:00.000Z\",\n"
            + "	\"TemplateName\": \"Quarterly Tax Return\",\n" + "	\"MaxAmount\": \"1000\",\n"
            + "	\"CreatedOn\": \"02/21/2019\",\n" + "	\"Template_id\": \"43\",\n"
            + "	\"EffectiveDate\": \"2019-02-26\",\n" + "	\"Approver\": \"N/A\",\n"
            + "	\"featureActionId\": \"ACH_PAYMENT_CREATE\",\n"
            + "	\"userName\": \"kmanthinasb1\",\n" + "	\"TotalAmount\": 750,\n"
            + "	\"TemplateDescription\": \"Quarterly Federal Tax Payment\"" + "}";

    static String ACHTransaction6Records =
            "[{\"EIN\":\"285484846\",\"ToAccountNumber\":\"4876986750376580\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"81203790\",\"IsZeroTaxDue\":\"0\",\"TaxType_id\":\"9\",\"SubRecords\":[{\"Amount\":\"200\",\"TaxSubCategory_id\":\"20\"}]},"
                    + "{\"EIN\":\"285484846\",\"ToAccountNumber\":\"4876986750376580\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"81203790\",\"IsZeroTaxDue\":\"0\",\"TaxType_id\":\"9\",\"SubRecords\":[{\"Amount\":\"100\",\"TaxSubCategory_id\":\"21\"}]},"
                    + "{\"EIN\":\"285484846\",\"ToAccountNumber\":\"4876986750376580\",\"ToAccountType\":\"3\",\"ABATRCNumber\":\"81203790\",\"IsZeroTaxDue\":\"0\",\"TaxType_id\":\"9\",\"SubRecords\":[{\"Amount\":\"450\",\"TaxSubCategory_id\":\"22\"}]}]";

    static String ACHFile1 =
            "101 234567890 1234567891310081642D094101ImmDestName            ImmOriginName                  \n"
                    + "5220Rise            8665801226 Credit   1261338450CCDADVANCE         1310080001123456780000001\n"
                    + "622122238682890123456789     000000060025877722       Integration, 93e5d115-  0123456780000000\n"
                    + "622122238682890123456789     000000060025877723       Integration, 93e5d115-  0123456780000001\n"
                    + "622122238682890123456789     000000060025877724       Integration, 93e5d115-  0123456780000002\n"
                    + "622122238682890123456789     000000060025877725       Integration, 93e5d115-  0123456780000003\n"
                    + "622122238682890123456789     000000060025877726       Integration, 93e5d115-  0123456780000004\n"
                    + "822000000901895474280000000000000000006579941204177339                         123456780000001\n"
                    + "5225Rise            8665801226 Debit    1204177339PPDPAYMENT         1310080001123456780000002\n"
                    + "622122238682890123456789     000000100025877675       Integration, 972ff172-S 0123456780000000\n"
                    + "622122238682890123456789     000000100025877676       Integration, d275a016-S 0123456780000001\n"
                    + "822500001000710010400000001406600000000000001204177339                         123456780000002        \n"
                    + "9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";

    static String ACHFile2 =
            "101 234567890 1234567891310081642D094101ImmDestName            ImmOriginName                  \n"
                    + "5220Rise            8665801226 Credit   1261338450CCDADVANCE         1310080001123456780000001\n"
                    + "622122238682890123456789     000000050025877722       Integration, 93e5d115-  0123456780000000\n"
                    + "6223210702275678901234       000000050025877720       Integration, 993128d4-  0123456780000001\n"
                    + "822000000901895474280000000000000000006579941204177339                         123456780000001\n"
                    + "9000005000017000001572972184060000000000000000008411862                                       \n"
                    + "9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";

    static String ACHFile3 =
            "101 234567890 1234567891310081642D094101ImmDestName            ImmOriginName                  \n"
                    + "5220Rise            8665801226 Debits   1261338450CCDPAYMENT         1310080001123456780000001\n"
                    + "622122238682890123456789     000000230025877722       Integration, 93e5d115-  0123456780000000\n"
                    + "6223210702275678901234       000000230025877720       Integration, 993128d4-  0123456780000001\n"
                    + "622122238682890123456789     000000100025877721       Integration, 845edb91-  0123456780000002\n"
                    + "822000000901895474280000000000000000006579941204177339                         123456780000001\n"
                    + "9000005000017000001572972184060000000000000000008411862                                       \n"
                    + "9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";

    static String BBGeneralTransaction1 =
            "{\"TransactionDate\": \"2019-02-28\", \"Payee\": \"Business Advantage Savings\", \"Amount\": \"6000.00\", \"Frequency\": \"Once\", \"Reccurence\": \"0\", \"DebitOrCreditAccount\": \"190128223244113\", \"BBGeneralTransactionType_id\": \"9\"}";
    static String BBGeneralTransaction1Entry = "{\"TransactionEntry\": " + "{\n"
            + "	\"transactionType\": \"InternalTransfer\",\n" + "	\"frequencyEndDate\": \"2019-02-28\",\n"
            + "	\"fromAccountNumber\": \"190128223244113\",\n" + "	\"amount\": \"6000.00\",\n"
            + "	\"transactionsNotes\": \"Development Fund\",\n" + "	\"toAccountNumber\": \"190128223246815\",\n"
            + "	\"frequencyType\": \"Once\",\n" + "	\"scheduledDate\": \"2019-02-28\",\n"
            + "	\"isScheduled\": \"0\"\n" + "}" + "}";

    static String BillPayTransactionData =
            "{\"amount\":\"12.00\",\"fromAccountNumber\":\"200418061428368\",\"transactionsNotes\":\"test\",\"scheduledDate\":\"2020-05-23T08:25:39.024Z\",\"transactionType\":\"BillPay\",\"deliverBy\":\"2020-05-23T08:25:39.024Z\",\"toAccountNumber\":\"211121\",\"zipCode\":\"12345\",\"billerId\":\"4\"}";
    
    static String InterBankTransactionData =
    		"{\"amount\":\"2.00\",\"ExternalAccountNumber\":\"47834984272\",\"frequencyType\":\"Yearly\",\"fromAccountNumber\":\"190128223241502\",\"isScheduled\":\"1\",\"numberOfRecurrences\":\"2\",\"scheduledDate\":\"2020-01-28T09:49:18.205Z\",\"toAccountNumber\":\"47834984272\",\"transactionsNotes\":\"\",\"transactionType\":\"ExternalTransfer\",\"transactionCurrency\":\"USD\",\"fromAccountCurrency\":\"USD\"}";;
    
    static String IntraBankTransactionData =
            "{\"amount\":\"12.00\",\"frequencyEndDate\":\"2020-04-18T08:29:17.194Z\",\"frequencyType\":\"Once\",\"fromAccountNumber\":\"200418061428368\",\"isScheduled\":\"1\",\"scheduledDate\":\"2020-05-22T08:29:17.194Z\",\"toAccountNumber\":\"47834984272\",\"transactionsNotes\":\"\",\"transactionType\":\"ExternalTransfer\",\"transactionCurrency\":\"USD\",\"fromAccountCurrency\":\"USD\",\"toAccountCurrency\":null,\"numberOfRecurrences\":null,\"ExternalAccountNumber\":\"47834984272\"}";
    
    static String InternationalAccountTransactionData =
            "{\"amount\":\"2.00\",\"ExternalAccountNumber\":\"47834984272\",\"frequencyType\":\"Yearly\",\"fromAccountNumber\":\"190128223241502\",\"isScheduled\":\"1\",\"numberOfRecurrences\":\"2\",\"scheduledDate\":\"2020-06-28T09:49:18.205Z\",\"toAccountNumber\":\"47834984272\",\"transactionsNotes\":\"\",\"transactionType\":\"ExternalTransfer\",\"transactionCurrency\":\"USD\",\"fromAccountCurrency\":\"USD\"}";
    
    static String P2PTransactionData =
    		"{\"amount\":\"2.00\",\"frequencyEndDate\":\"2020-01-27T07:35:41.349Z\",\"frequencyType\":\"Once\",\"fromAccountNumber\":\"190128223241502\",\"isScheduled\":\"0\",\"p2pContact\":\"8790146356\",\"personId\":\"1467\",\"scheduledDate\":\"2020-01-27T07:35:41.349Z\",\"transactionsNotes\":\"test\",\"transactionType\":\"P2P\",\"transactionCurrency\":\"USD\",\"fromAccountCurrency\":\"USD\"}";
    
    static String OwnAccountTransactionData =
            "{\"amount\":\"12.00\",\"frequencyEndDate\":\"2020-04-18T08:20:09.739Z\",\"frequencyType\":\"Once\",\"fromAccountNumber\":\"200418061428368\",\"isScheduled\":\"1\",\"scheduledDate\":\"2020-06-25T08:20:09.739Z\",\"toAccountNumber\":\"200418061428459\",\"transactionsNotes\":\"\",\"transactionType\":\"InternalTransfer\",\"transactionCurrency\":\"USD\",\"fromAccountCurrency\":\"USD\",\"toAccountCurrency\":\"USD\",\"numberOfRecurrences\":null,\"ExternalAccountNumber\":null}";
    
    static String InternationalWireTransactionData =
    		"{\"amount\":\"10.00\",\"fromAccountNumber\":\"200418153056775\",\"payeeAccountNumber\":\"123\",\"payeeAddressLine1\":\"test\",\"payeeName\":\"test\",\"payeeNickName\":\"test\",\"transactionsNotes\":\"test\",\"transactionType\":\"Wire\",\"payeeCurrency\":\"Dollar\",\"swiftCode\":\"123\",\"wireAccountType\":\"International\",\"country\":\"UnitedStatesofAmerica\",\"bankName\":\"test\",\"internationalRoutingCode\":\"123\",\"zipCode\":\"123\",\"cityName\":\"test\",\"state\":\"Telangana\",\"bankAddressLine1\":\"test\",\"bankAddressLine2\":\"test\",\"bankCity\":\"test\",\"bankState\":\"Telangana\",\"bankZip\":\"123\",\"payeeType\":\"Business\",\"payeeAddressLine2\":\"test\"}";
    
    static String DomesticWireTransactionData =
            "{\"amount\":\"20.00\",\"fromAccountNumber\":\"190128223241502\",\"payeeAccountNumber\":\"123\",\"payeeAddressLine1\":\"test\",\"payeeName\":\"test\",\"payeeNickName\":\"test\",\"transactionsNotes\":\"test\",\"transactionType\":\"Wire\",\"payeeCurrency\":\"Dollar\",\"wireAccountType\":\"Domestic\",\"bankName\":\"test\",\"routingNumber\":\"123\",\"zipCode\":\"123\",\"cityName\":\"test\",\"state\":\"Telangana\",\"bankAddressLine1\":\"test\",\"bankAddressLine2\":\"test\",\"bankCity\":\"test\",\"bankState\":\"Telangana\",\"bankZip\":\"123\",\"payeeType\":\"Business\",\"payeeAddressLine2\":\"test\"}";
    
    static HashMap<String, String> getACHFile1(String companyId, String createdBy, String date, String status) {
        HashMap<String, String> achFileTableMap = new HashMap<>();

        achFileTableMap.put("achFileName", "ACH_05112018.ach");
        achFileTableMap.put("debitAmount", "2000");
        achFileTableMap.put("numberOfCredits", "5");
        achFileTableMap.put("numberOfDebits", "2");
        achFileTableMap.put("numberOfPrenotes", "0");
        achFileTableMap.put("contents", ACHFile1);
        achFileTableMap.put("fileSize", "15717100000");
        achFileTableMap.put("creditAmount", "3000");
        achFileTableMap.put("numberOfRecords", "7");
        achFileTableMap.put("achFileFormatType_id", "2");
        achFileTableMap.put("requestType", "CCD,PPD");
        achFileTableMap.put("companyId", companyId);
        achFileTableMap.put("createdby", createdBy);
        achFileTableMap.put("featureActionId", "ACH_FILE_UPLOAD");
        if (date == null) {
            achFileTableMap.put("createdts", HelperMethods.getCurrentTimeStamp());
        } else {
            achFileTableMap.put("createdts", date);
        }
        achFileTableMap.put("status", status);
        achFileTableMap.put("BBGeneralTransactionType_id", "8");
        achFileTableMap.put("EffectiveDate", achFileTableMap.get("createdts"));
        achFileTableMap.put("RequestType_id", "8");
        achFileTableMap.put("accountId", "NA");

        return achFileTableMap;
    }

    static HashMap<String, String> getACHFile2(String companyId, String createdBy, String date, String status) {
        HashMap<String, String> achFileTableMap = new HashMap<>();

        achFileTableMap.put("achFileName", "ACH_06052018.ach");
        achFileTableMap.put("debitAmount", "0");
        achFileTableMap.put("numberOfCredits", "2");
        achFileTableMap.put("numberOfDebits", "0");
        achFileTableMap.put("numberOfPrenotes", "0");
        achFileTableMap.put("contents", ACHFile2);
        achFileTableMap.put("fileSize", "15717100000");
        achFileTableMap.put("creditAmount", "1000");
        achFileTableMap.put("numberOfRecords", "2");
        achFileTableMap.put("achFileFormatType_id", "2");
        achFileTableMap.put("requestType", "CCD");
        achFileTableMap.put("companyId", companyId);
        achFileTableMap.put("createdby", createdBy);
        achFileTableMap.put("featureActionId", "ACH_FILE_UPLOAD");
        if (date == null) {
            achFileTableMap.put("createdts", HelperMethods.getCurrentTimeStamp());
        } else {
            achFileTableMap.put("createdts", date);
        }
        achFileTableMap.put("status", status);
        achFileTableMap.put("BBGeneralTransactionType_id", "8");
        achFileTableMap.put("EffectiveDate", achFileTableMap.get("createdts"));
        achFileTableMap.put("RequestType_id", "8");
        achFileTableMap.put("accountId", "NA");

        return achFileTableMap;
    }

    static HashMap<String, String> getACHFile3(String companyId, String createdBy, String date, String status) {
        HashMap<String, String> achFileTableMap = new HashMap<>();

        achFileTableMap.put("achFileName", "ACH_07132018.ach");
        achFileTableMap.put("debitAmount", "0");
        achFileTableMap.put("numberOfCredits", "3");
        achFileTableMap.put("numberOfDebits", "0");
        achFileTableMap.put("numberOfPrenotes", "0");
        achFileTableMap.put("contents", ACHFile3);
        achFileTableMap.put("fileSize", "15717100000");
        achFileTableMap.put("creditAmount", "5600");
        achFileTableMap.put("numberOfRecords", "3");
        achFileTableMap.put("achFileFormatType_id", "2");
        achFileTableMap.put("requestType", "CCD");
        achFileTableMap.put("companyId", companyId);
        achFileTableMap.put("createdby", createdBy);
        achFileTableMap.put("featureActionId", "ACH_FILE_UPLOAD");
        if (date == null) {
            achFileTableMap.put("createdts", HelperMethods.getCurrentTimeStamp());
        } else {
            achFileTableMap.put("createdts", date);
        }
        achFileTableMap.put("status", status);
        achFileTableMap.put("BBGeneralTransactionType_id", "8");
        achFileTableMap.put("EffectiveDate", achFileTableMap.get("createdts"));
        achFileTableMap.put("RequestType_id", "8");
        achFileTableMap.put("accountId", "NA");

        return achFileTableMap;
    }

}
