package com.bct.javaservices;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;



public class GetNPSBillersIntegrationURLTest {
	
	public static void main1(String[] args) {
    	JoseUtils jose = new JoseUtils();
    	try {
    		JSONObject payload = new JSONObject();
    		JSONObject certDetails = new JSONObject();
    		certDetails.put("senderCertPath", "src/main/resources/certificates/NPIBILLER0701_SENDER_PRIVATE_KEY.pfx");
    		certDetails.put("senderCertPass", "123");
    		certDetails.put("receiverCertPath", "src/main/resources/certificates/NPIBILLER_RECEIVER_PUBLIC_KEY.cer");
    		certDetails.put("receiverCertPass", "");
    		
    		JSONObject decryptCert= new JSONObject();
    		decryptCert.put("senderCertPath", "src/main/resources/certificates/NPIBILLER_RECEIVER_PUBLIC_KEY.cer");
    		decryptCert.put("senderCertPass", "");
    		decryptCert.put("receiverCertPath", "src/main/resources/certificates/NPIBILLER0701_SENDER_PRIVATE_KEY.pfx");
    		decryptCert.put("receiverCertPass", "123");
    		
    		String Stringpayload="eyJraWQiOiJMd3R0TWo2aVBaSWlYNEJ2R3g1UUtkNnJmQnExWkR5Q0hTa0NjcFVQY1kwXFxcXFxcPSIsImN0eSI6IkpXVCIsImVuYyI6IkEyNTZHQ00iLCJhbGciOiJSU0EtT0FFUC0yNTYifQ.r5362XwueGdM-JOBRlN1GxM4xZlcNr_UI-ICf102jJJvc-BgNEK5SSg-FHJrnx5fO74fnKw_WcXd6AE0osmSBywXWQDuYw55669UXGksu2lVCEN3tdwijokapvy5QOcjmptN-o03_q2-78rf8gZUiz2eLLCEA_mZXybFD4dxy4oniHPU2YRn0zr3e3Wsrxfw_R9q4uX2htFDGKHbXkqmkwr-Nz-m7FC_b2R2ZKFUb0M5TYEpPB2TDWse5QhCkKUt5hzMWlmWDWTF_oWu9NGZ9aIjZEodtrXuKVDt0ba9ZpcIHwvKRzHUskB6auoAz2es2-jbqiyGX-xp-kgWiew58A.kyYwGcREPajbUYOP.yHDoNu082bXA4RDsIexvvTHdYIc4tSv2vKm2WBlZ_2bOlSkt3m0kYE8ILjDohLlt-I2p2hYlo3YwPMwWBo6O04FX1gBUhDXA5uoJycMwDbJ-gHf_SY_3WvLFBPEbca2PbqHu-qHPuq17AWIK2lEkhIQBTgrR0eC-ZcfYMkRqJK5JxaOuPHOtbTvLiQD8u-GeNpIFu3OjtQKynnoSn0ZxviORKF1zY5CYENKG8rj6WHfOxGWJhNc1T5dZDJmjSxbWc6gZR8vDluxkPZGWyhttx5mTYNH-BC-4mgtYfDjp8y0XDdXeKBHhIFN9mj9xprq6IxxX8PVMoKkrNg8ganYGFB_reOXNkrfFMwUfvLCOB-CbKfA3_WdIdz9SGxExMHbdJgKXQMm37-huM7iHGK57rRzGKqb1tTCqKpvhFR7j5jD6CQbT6Zpw3i-nJ3FuLdIbH3gMo7VQlH7SEFYrDJpMxJ68OIjI9d_htcY8SOQSFrpI1TZ8ixm2I-tn94D_0baTk6mUfr2VIB9P9pMnQzmmGSkBIwVv9XmAMU7a5oGl37f9g0ceAAWthB_7zH4Ib7dxwQQhu8Jtuj9Gy0FwWiDlChDrfo3Msd7Wb5k4UQAXrXUFNbCWL8gROJCSSRc7lDzSockUbvME8gZkitLWzF7uV8uZ5yvODboUIFgMxsNv8RX9GlrEJwtT3O3tcH4GkunZJzbByIsrL_tNO_3n2NK-HrhFoOw9e9ECtPrp1AE5AI8oLB7xr67RG2FhLGKrnw052MOANPNFSQ1A9VF1A47yGC1xuUVBhxfBhWQilmJuIRLkM7J8Mj7UwYmEX7AkcyxJahvf-fSWw2TvGWbCjSKFs_NkRITrWLlp41HOvIVtRNT-yfZ7FHE5iF32mqb3afSccbwr6t1slMlYye9SqLIVd2o-YPzkzD3ZX-a-NXPJc3xDsU25UzHZqYnUi0OUu2-t-ocDgjHlpilGHjPs1FFWW-93ECoEnZ8La3GWyEb7E0K7ZVtk8XmrTaSqIpZqoOaY7Mvs_lhSsKVpUi9qof0OLLsbbIQVwcY3PfYjtS5axXdUzzkXrzX8s5Tfq1HzyT5ST_rHrMaWZXDXXu4tVtJhRYOaPCT5Ex6fPy_d6r7oe7vZbHzv374MLHL9iqwjLBTtrsLoHSQtHE3kZl_OKkRj-lzP9jTzUfkOUqngXnFfzV27_6mmeoZOKZGXnJVfGM_hOfdC593W_YmnKa2pJaDaPHyk9cijfNWH4IJ_1mSlRla0kpwab6zSyaNOcgCYSqW21b3UnUykLh2n-JGglR-IHV7_KdJgRAL_7ZIZjfH5hGePiCafDbipVyHbZ9_iixbU6-6j5RIT-gZSY5iR4AKIY1SGDeZaeMuT70D54zs-XtRDM2oQa65tserIY2K8maU5w7cSDU3lGDrDEA_OgUQSnxKsbcK-NzIUCfaT0O6DwA2DHIKRG3tnk9_UViB-nIO2B1jzDJrj9ewL95olGug7PhqwXCc6Mbck6Ny3d0tNrHz-73-X00FY4a9AHVUrYWGwkiR7mkjFHQ8lvZV3xTx9mibvviIZlLVl0-02E8AaV0m43KHjTy6XGSo62yRsLe4hXIhtwBeO1QZgwvb1TrxPMfyJjlJSccLzpVzL9r6UHTqIKoxYLmzdqOvXgjFknjFtOa23lPfHEmae3ZZjtHkYG5RmcLMzx9uMbOzZ6g9872VNatonKRvQ6AOpJw-TUCJdyo3I1TyGwZ1GKb5HE09O440CxmUqIxq1cmrse_taYQZSokhc68SE0H4KabtBWXhehotI8NWWnYLhjMdJZUC3jrjKjiSiXtnK05PSNjtfRyHQmtq9cxMfGLQMExxpRexGTXHiGRDRUN07beRvzVS_hOo3QJ9FXsif8J3Lbr4HoYSHuUIft64rgSv44ArhTwX6hoIBjkfP2j1YzhLCpDM8Lbw4MumtL1pD_tqT2er4BtBBcWj-0gbKNQQ28SwUMqLMfjJ_Ek-GMAB-UmMwXcG4EU2JquJD4nZI6dmxN5tNYp5JOK_F8Yrd5FX38DAp_hQM0NFyI2KWXDST2JXJuWOhG-BEggtehdo6KRooTv-4hS1ixxY5x1XauJaziZnkC5uCTWx-vRJn6MyQFHJgpyTwXuOfDjCE2A4gyj6fsQra4OEu3svErpgJW9KFstf8_I9nptYLvLSqfLePVj9gdzqcBJT1x5aCkQd8d_TfDXAQAuFUmXAUaXXjlQlhTWJYkDYw7IYtN5w1NQJsogNLAddvz2QUkgzL53EFRfzfEmprYK4HkFmPpbjxyxwnKnMr2Rbq5szcL889xBNfbd3SIwML9X2HHFj-rZQaCgGoTaPmIKpbwHQo89EV_o9gMpPYQ-6wuGBZs8_ibMl73-5bPc0mhI0kg0SOBXUruzW8WS3yNQZX-Fa7IS_-woFkMRtc4oJOU0Uj9xlZTQYe39Z0otilRFWX4-K7C0ms9BCJb1ojydsguFOYMJAXD5YJ0nlFgXcG_iWvSoQ6uto466KAdlNPde8Ok_aqzG6eesnMVLPGpi2BX8d3k24VjUeH29vBy2deYJNFtMdEuooTmfDvYYNDUT8jSRCtiCap0Cq5cpX3iuoHg1OdDUq-KCPUsCjG5TWoPzOufHKjN8Oig_qNDV_aX5pX5tL9M-2L0ZtZ544o1IDlNdXZFFRxhxOgv0_jZTKvUY-n0jeMbUzRez4Rtmz7EdlXpPjGNOyhHnsT5i3idCey18VDNgD_1b-Tf8CRvOmh8bqi2PKw9CkX1DymA2D5rRHK3BT7pAy8s969ZQ2f1PWYSDWrUFEoqfGGPQ6CRYYoqtU5B8QAysjAkKtLbmWUeB3WoZOG0l7XQT5cgXnU2suvLrdq6XlmJC2ot2PlEnC5RG9-PyJzkCHQq5ZIP-w5b8b5Ee90uO4v5ofvcdQSU333Sc_NbU2Zac54PLBd8qiyiS38l_0a8pZkGvla7WGor0AL2kfLgomnJLolWcXYo-yl8pGKdjAfRRvn_OD-EWf8izQw5Clj1Zbo3JnnkbKV3u8bAlXPDMcza4rfpPXhAJfq7sLK77GkT8_6C-yH6HwxiAmPdxe-le1SpoJn8x4oxy2ianbhpEvF0o3xYAVHLEdpl7lfGou2UJkBWW4IfHDS4wvIteeQCqBaIewxK7K-rUpIOc3p2OTjNQFQ--FOA0EQyu5JZfZqtdgagV1KxoF8OcbXWVyAbgkO9m4LMI5XOX3Fg8vGnP1_r_3-KPvvycZh_NhG7nAgoYM0yeCpicH_2jBz1BNB3fO9HnTVJ46d11j-vjnHlvoce9ZRzqbySXiyctpIJgUVEbG1vgbgemiGp-e16ypfqGfPdb07TTqxtOtu8ktDURcZJayDxCB-LQ0ss04dq8yOrDlswboDvrL73P6f7yZS1I0p3HeF3pXcIQ9542ksnqmgAYQ6gsdrUFQpvsTuoNYngnXTZPLRcCh5J5Rv4NksGC0HN8rwkwRngv-5fr_q3azWxPpjl5kx-bYCgP5OMBaZn5HSjKRzgMJhe8YBOa18MpjYFDuth8QVQWTGl-70ch_Sfnno7qSxKePOvL7HEJMO05745i_14RyULeOgSUYx98AYlWJDVysHObnYRpNYU5Jd3kO5fIE1411GWw3Qe4Zv6e1EzJ4qUwDpE2rwcALSRw-DobbmSdN6cJoqx04m7y-5KFrZ9FXGeg8CYWdkwSKvKiPZVbc6iSyMyo74kTsDKDueF1J4LlU0J2E_ibkLM51Y6nMT7v8GCXGR8SpB57QtPs3FN85Xcnvo2pk.44uA0giDpbyr_GJp1_NJNA";
    		
    		String sessionId=jose.generateSessionId();
    		payload.put("participant_code", "himalayan");
    		payload.put("session_id", sessionId);
    		
			//String encrypetedToken=signAndEncryptPayload(payload.toString(), certDetails);
			String decryptedToken=JoseUtils.decryptAndVerifyResponse(Stringpayload, decryptCert);
			JSONObject NPIObj= new JSONObject(decryptedToken);
			//System.out.println("session_id:"+sessionId);
			//System.out.println("encrypetedToken:"+encrypetedToken);
			removeEmptyAndNullFields(NPIObj);
			System.out.println("NPIObj:"+NPIObj.toString());
			//HashMap inputMap = (HashMap) NPIObj.toMap();
			//HelperMethods.removeNullValues(inputMap);
		}  catch (Exception e) {
			System.out.println("Exception occured::"+e.getMessage());
		}
    	
		
	}
    public String generateSessionId(){
		String sessionId = "himalayan" + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return sessionId;
	}
    public static void removeEmptyAndNullFields(Object object) throws Exception {
        if (object instanceof JSONArray) {
            JSONArray array = (JSONArray) object;
            for (int i = 0; i < array.length(); ++i) 
              removeEmptyAndNullFields(array.get(i));
        } else if (object instanceof JSONObject) {
            JSONObject json = (JSONObject) object;
            JSONArray names = json.names();
            if (names == null) return;
            for (int i = 0; i < names.length(); ++i) {
                String key = names.getString(i);
                
                if (json.isNull(key) || json.get(key) =="") {
                    json.remove(key);
                }
                else {
                    removeEmptyAndNullFields(json.get(key));
                }
            }
        }
    }
    public JSONObject postProcessArrayResponse(JSONObject errorResponse) {
		//LOG.debug("BCT::ManualMerchantFormFieldsPostprocessor::postProcessResponse:jsonObj:" + errorResponse);
		String JsonString="{\"ConfirmBillPaidResult\":{\"RETURN_CONFIRMPAID_NATIVE\":[{\"CODE\":0,\"MESSAGE\":\"AmountPaidSuccessfully\",\"TRACEID\":57155188,\"PARTNERTXNID\":171396471176541,\"SYSTEMTXNID\":97337,\"SCNO\":\"042.23.058KA4\",\"CUSTOMERNAME\":\"Mr.BALARAMSHRESTHA\",\"DUE_BILL_OF\":\"PAIDUPTO2080-12\",\"PAYABLE_AMOUNT\":0,\"CONSUMER_ID\":100539305,\"OFF_CODE\":205,\"OFFICE\":\"KULESWOREDC\",\"BILL_DATE\":\"\",\"NO_OF_DAYS\":0,\"BILL_AMT\":0,\"FINE_RATE\":0,\"REBATE\":\"\",\"PAID_AMT\":0,\"AMOUNT_DUE_LEFT\":1,\"PAID_DATE\":\"4/24/202412:00:00AM\"},{\"CODE\":0,\"MESSAGE\":\"AmountPaidSuccessfully\",\"TRACEID\":57155188,\"PARTNERTXNID\":171396471176541,\"SYSTEMTXNID\":97337,\"SCNO\":\"042.23.058KA4\",\"CUSTOMERNAME\":\"Mr.BALARAMSHRESTHA\",\"DUE_BILL_OF\":\"ADVANCE\",\"PAYABLE_AMOUNT\":0,\"CONSUMER_ID\":100539305,\"OFF_CODE\":205,\"OFFICE\":\"KULESWOREDC\",\"BILL_DATE\":\"24-APR-24\",\"NO_OF_DAYS\":0,\"BILL_AMT\":-35.59,\"FINE_RATE\":0,\"REBATE\":\"\",\"PAID_AMT\":0,\"AMOUNT_DUE_LEFT\":1,\"PAID_DATE\":\"4/24/202412:00:00AM\"}]}}";
		JSONObject staticResult= new JSONObject(JsonString) ;
		//LOG.debug("BCT::ManualMerchantFormFieldsPostprocessor::postProcessResponse:jsonObj:" + staticResult);
       // Result result = JSONToResult.convert(staticResult.toString());
        return staticResult;
	}
	public static void main(String[] args) {
		GetNPSBillersIntegrationURLTest extn = new GetNPSBillersIntegrationURLTest();
		System.out.println(extn.postProcessArrayResponse(null));
		JSONObject responseObj= extn.postProcessArrayResponse(null);
		 responseObj= responseObj.has("ConfirmBillPaidResult")? responseObj.getJSONObject("ConfirmBillPaidResult"):new JSONObject();
		 JSONArray mockResponse= responseObj.has("RETURN_CONFIRMPAID_NATIVE")? responseObj.getJSONArray("RETURN_CONFIRMPAID_NATIVE"): new JSONArray();
		 String transactionId =mockResponse.getJSONObject(0).has("PARTNERTXNID")?String.valueOf(mockResponse.getJSONObject(0).get("PARTNERTXNID")):"";
		 System.out.println(transactionId);
		 
	}

}
