package com.bct.preprocessor;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ConnectIPSOtherbankTransferTestPreProcessor {

	public static void main(String[] args) {
		String transactionDetailsString = "[{\'cipsBatchDetail\':{\'batchCrncy\':\'NPR\',\'debtorAccount\':\'\',\'debtorBranch\':\'\',\'debtorAddress\':null,\'debtorAgent\':\'\',\'batchId\':\'bilbatch-2650\',\'batchFreeText4\':\'\',\'batchCount\':1,\'batchFreeText3\':\'\',\'corporateId\':null,\'initBranchId\':null,\'id\':2650,\'rcreUserId\':null,\'channelId\':null,\'rcreTime\':null,\'debtorEmail\':null,\'debtorName\':\'\',\'debitAcid\':\'\',\'batchAmount\':10,\'recDate\':null,\'debtorMobile\':null,\'categoryPurpose\':\'ECPG\',\'batchFreeText2\':\'\',\'batchFreeText1\':\'\',\'batchChargeAmount\':null},\'cipsTransactionDetail\':{\'responseResult\':null,\'creditAcid\':null,\'data\':null,\'appTxnId\':\'12729775\',\'endToEndId\':\'9841432993\',\'freeText1\':\'\',\'mode\':1,\'freeText2\':\'\',\'freeText3\':\'\',\'creditBankLogo\':null,\'freeText4\':\'\',\'freeText5\':\'\',\'merchantId\':120,\'freeText6\':\'\',\'appGroup\':null,\'freeText7\':\'\',\'beneficiaryName\':\'\',\'appId\':\'NIMB-NTC-APP-1\',\'id\':163,\'rcreTime\':\'2025-01-10\',\'accountValidationMsg\':null,\'addenda1\':null,\'addenda2\':null,\'addenda3\':\'\',\'addenda4\':\'\',\'recDate\':\'2025-01-10T12:07:47.228+00:00\',\'creditorAddress\':null,\'system\':null,\'freeNum2\':null,\'creditorIdValue\':\'\',\'freeNum3\':null,\'freeNum4\':null,\'instructionId\':\'RT-2645\',\'freeNum1\':null,\'creditorIdType\':\'\',\'beneficiaryId\':\'\',\'freeString\':null,\'status\':null,\'freeCode2\':\'\',\'freeCode1\':\'\',\'creditorEmail\':null,\'favTxnFlg\':null,\'orignBranchId\':null,\'batchId\':0,\'txnResponse\':null,\'chargeAmount\':0,\'particulars\':\'\',\'rcreUserId\':\'NPIBILLER4501@999\',\'creditorMobile\':null,\'billValidatonTraceId\':null,\'amount\':10,\'chargeLiability\':\'CG\',\'debitBankLogo\':null,\'token\':null,\'refId\':\'9841432993\',\'remarks\':\'Nepal Telecom - PrePaid\'},\'debitInformation\':{\'debtorName\':\'JANAKI22\',\'debtorAccount\':\'15483407\',\'fromAccountCurrency\':\'NPR\',\'debtorAgent\':\'4501\',\'debtorBranch\':\'1\'}}]";
		//transactionDetailsString=transactionDetailsString.replaceAll("'","\"");
		 if(!validatePasswordRegex("")){
			 System.out.println("Password does not meet the validation criteria");
	        }
		System.out.println("transactionDetailsString:"+transactionDetailsString);
		JSONArray array= new JSONArray(transactionDetailsString);
		System.out.println("array:"+array);
		ConnectIPSOtherbankTransferTestPreProcessor obj= new ConnectIPSOtherbankTransferTestPreProcessor();
		HashMap payload = obj.preparePayload();
		String batchString=obj.generateBatchString(payload);
		String txString=obj.generateTransactionString(payload);
		String userId="HBL@999";
		String token=obj.generateTokenString(batchString, txString, userId);
		System.out.println("token before signing:"+token);
		String contentToEncode = token;//"HBL -531,0701,1,01900099870014,1,NPR,HBL-531,2101,1,0830122854100011,1,HBL@999";  
		try {
			String encodeContent = obj.HBLTxnToken(contentToEncode);
			System.out.println("token after signing:"+encodeContent);
		} catch (Exception e) {
			System.out.println("Exception occured:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	public HashMap  preparePayload() {
		HashMap inputMap = new HashMap();
		String randomid=String.valueOf(generateBatchId());
		//randomid="HBL-"+randomid;
		inputMap.put("batchId", randomid);
		inputMap.put("batchAmount", 100);
		inputMap.put("batchCount", 1);
		inputMap.put("batchCrncy", "NPR");
		inputMap.put("categoryPurpose", "DNRQ");
		inputMap.put("debtorAgent", "0701");
		//inputMap.put("debtorBranch", "NP0010018");
		inputMap.put("debtorBranch", "1");
		inputMap.put("debtorName", "NABIN PRAJAPATI");
		inputMap.put("debtorAccount", "01908433710016");
		
		inputMap.put("instructionId", randomid+"-1");
		inputMap.put("endToEndId", randomid);
		inputMap.put("amount", 100);
		inputMap.put("creditorAgent", "4501");
		inputMap.put("creditorBranch", "1");
		inputMap.put("creditorName", "NIMESH SHRESTHA");
		inputMap.put("creditorAccount", "001011160000270");
		
		
		/* OPTIONAL FIELDS
		inputMap.put("debtorIdType", "0001");
		inputMap.put("debtorIdValue", "123456");
		inputMap.put("debtorAddress", "Kathmandu Nepal");
		inputMap.put("debtorPhone", "+977-01-4255306");
		inputMap.put("debtorMobile", "+977-9812345678");
		inputMap.put("debtorEmail", "test@test.com");
		*/
		
		return inputMap;
		
	}
	public String generateBatchString(HashMap payload){
		String batchId= payload.get("batchId").toString();
		String batchAmount= payload.get("batchAmount").toString();
		String batchCount= payload.get("batchCount").toString();
		String batchCrncy= payload.get("batchCrncy").toString();
		String debtorAgent= payload.get("debtorAgent").toString();
		String debtorBranch= payload.get("debtorBranch").toString();
		String debtorName=payload.get("debtorName").toString();
		String debtorAccount= payload.get("debtorAccount").toString();

		String batchString=batchId+","+debtorAgent+","+debtorBranch+","+debtorAccount+","+batchAmount+","+batchCrncy;
		
		System.out.println("batchId:"+batchId);
		System.out.println("batchAmount:"+batchAmount);
		System.out.println("batchCount:"+payload.get("batchCount").toString());
		System.out.println("batchCrncy:"+batchCrncy);
		System.out.println("categoryPurpose:"+payload.get("categoryPurpose").toString());
		System.out.println("debtorAgent:"+debtorAgent);
		System.out.println("debtorBranch:"+debtorBranch);
		System.out.println("debtorName:"+debtorName);
		System.out.println("debtorAccount:"+debtorAccount);
		
		
		
		return batchString;
		
	}
	public String generateTransactionString(HashMap payload){
		String instructionId= payload.get("instructionId").toString();
		String endToEndId= payload.get("endToEndId").toString();
		String amount= payload.get("amount").toString();
		String creditorAgent= payload.get("creditorAgent").toString();
		String creditorBranch= payload.get("creditorBranch").toString();
		String creditorName= payload.get("creditorName").toString();
		String creditorAccount= payload.get("creditorAccount").toString();
		
		String TxString=instructionId+","+creditorAgent+","+creditorBranch+","+creditorAccount+","+amount;
		//String TxString=instructionId;
		System.out.println("instructionId:"+instructionId);
		System.out.println("endToEndId:"+endToEndId);
		System.out.println("amount:"+amount);
		System.out.println("creditorAgent:"+creditorAgent);
		System.out.println("creditorBranch:"+creditorBranch);
		System.out.println("creditorName:"+creditorName);
		System.out.println("creditorAccount:"+creditorAccount);
		return TxString;
		
	}
public String generateTokenString(String batchString,String txString, String userId){
	
		
		String token=batchString+","+txString+","+userId;
		
		return token;
		
	}
	public String HBLTxnToken(String contentToEncode) throws Exception {
		  String cips_cert_path="src/main/resources/certificates/NPI.pfx";
	      String cips_cert_password = "123"; 
        byte[] certStore = Files.readAllBytes(Paths.get(cips_cert_path));
        
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
       
		keyStore.load(new ByteArrayInputStream(certStore), cips_cert_password.toCharArray());
        
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());
        
        byte[] utf8Data = contentToEncode.getBytes("UTF-8");
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(utf8Data);
        
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }
	
	
	
	public String generateBatchId(){
		String batchId = "HBL" + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return batchId;
	}
	private static boolean validatePasswordRegex(String password) {

        Map<String, String> passwordRulesAndPolicyInputMap = new HashMap<>();
        passwordRulesAndPolicyInputMap.put("ruleForCustomer", "true");
        passwordRulesAndPolicyInputMap.put("policyForCustomer", "true");

        Result result = null;
        Result passwordRulesAndPolicyResult = new Result();
        try {
                JSONObject passwordRules = new JSONObject();

                if (passwordRules.has("charRepeatCount")) {
                    String repeatedCharRegex = "(.)\\1{" + passwordRules.getString("charRepeatCount") + ",}";
                    Pattern repeatedCharPattern = Pattern.compile(repeatedCharRegex);
                    Matcher repeatedCharMatcher = repeatedCharPattern.matcher(password);
                    if (repeatedCharMatcher.find()) {
                        return false;
                    }
                }

                String passwordRegex = "";

                if (passwordRules.has("atleastOneSymbol") && passwordRules.getString("atleastOneSymbol").equals("true")) {
                    if (passwordRules.has("atleastOneLowerCase")
                            && passwordRules.getString("atleastOneLowerCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[a-z])";
                    }
                    if (passwordRules.has("atleastOneUpperCase")
                            && passwordRules.getString("atleastOneUpperCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[A-Z])";
                    }
                    if (passwordRules.has("atleastOneNumber")
                            && passwordRules.getString("atleastOneNumber").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*\\d)";
                    }
                    if (passwordRules.has("supportedSymbols") && passwordRules.getString("supportedSymbols").length() != 0) {
                        String supportedSymbols = "";
                        if(passwordRules.getString("supportedSymbols").indexOf("-") > -1) {
                            supportedSymbols = passwordRules.getString("supportedSymbols").replace("-", "\\-");
                            passwordRules.put("supportedSymbols", supportedSymbols);
                        }
                        passwordRegex = passwordRegex + "(?=(.*[" + passwordRules.getString("supportedSymbols") + "]))";
                        if(passwordRules.getString("supportedSymbols").indexOf(",") > -1) {
                            supportedSymbols = passwordRules.getString("supportedSymbols").replaceAll(",", "");
                            passwordRules.put("supportedSymbols", supportedSymbols);
                        }
                    }
                    passwordRegex = passwordRegex + "[A-Za-z0-9" + passwordRules.getString("supportedSymbols")
                            + "]{" + passwordRules.getString("minLength") + ","
                            + passwordRules.getString("maxLength") + "}$";
                } else {
                    passwordRegex = "^";
                    if (passwordRules.has("atleastOneLowerCase")
                            && passwordRules.getString("atleastOneLowerCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[a-z])";
                    }
                    if (passwordRules.has("atleastOneUpperCase")
                            && passwordRules.getString("atleastOneUpperCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[A-Z])";
                    }
                    if (passwordRules.has("atleastOneNumber")
                            && passwordRules.getString("atleastOneNumber").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*\\d)";
                    }
                    passwordRegex = passwordRegex + "[^\\\\W]{" + passwordRules.getString("minLength") + ","
                            + passwordRules.getString("maxLength") + "}$";
                }

                Pattern passwordRegexPattern = Pattern.compile(passwordRegex);
                Matcher passwordRegexMatcher = passwordRegexPattern.matcher(password);

                if (!passwordRegexMatcher.matches()) {
                    return false;
                }
        } catch (Exception e) {
        	System.out.println("Caught exception : "+ e);
        }

        return true;
    }

}
