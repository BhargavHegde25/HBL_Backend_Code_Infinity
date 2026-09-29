/**
 * 
 */
package com.temenos.dbx.product.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.infinity.dbx.dbp.jwt.auth.Authentication;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * @author srirama.sadineni
 *
 */
public class GenerateJWTToken implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();

		dcRequest.addRequestParam_("flowType", AuthConstants.PRE_LOGIN_FLOW);
		String authToken = TokenUtils.getT24AuthToken(dcRequest);
		result.addParam("T24Token", authToken);
		result.addParam("AMSToken", generateAMSToken());
		
		return result;

	}
	
	private String generateAMSToken(){
		try {
			Map<String, String> inputMap = new HashMap<>();
            return TokenUtils.getAMSAuthToken(inputMap);
        } catch (CertificateNotRegisteredException e) {
            alert.prepareError(e.toString()).log();
        } catch (Exception e) {
        	 alert.prepareError(e.toString()).log();
        }
		
		return null;
	}
	
	public static void main(String[] args) {
		
//		String str = "{\"partyAssessments\":[],\"partyIdentifiers\":[],\"personPositions\":[],\"otherRiskIndicators\":[],\"partyLanguages\":[],\"contactPoints\":[{\"phoneAddress\":{},\"electronicAddress\":{\"electronicAddressType\":\"Email\",\"electronicAddress\":\"srirama.sadineni2@kony.com\",\"electronicCommunicationType\":\"Primary\"},\"contactPointType\":\"Electronic Address\",\"partyId\":\"2011253499\",\"startDate\":\"2020-04-21\",\"extensionData\":{}},{\"phoneAddress\":{\"phoneAddressType\":\"Mobile Number\",\"iddPrefixPhone\":\"+91\",\"nationalPhoneNumber\":\"8143589876\"},\"electronicAddress\":{},\"contactPointType\":\"Phone Address\",\"partyId\":\"2011253499\",\"startDate\":\"2020-04-21\",\"extensionData\":{}}],\"partyNamess\":[{\"firstName\":\"sriram\",\"lastName\":\"Sadineni\",\"partyId\":\"2011253499\",\"startDate\":\"2020-04-21\",\"extensionData\":{}}],\"contactAddresss\":[],\"dateOfBirth\":\"1992-03-31\",\"employments\":[],\"partyType\":\"Individual\",\"occupations\":[],\"extensionData\":{},\"partyStatus\":\"Prospect\",\"taxDetailss\":[],\"citizenships\":[],\"nationalitys\":[],\"partyId\":\"2011253499\",\"residences\":[],\"vulnerabilitys\":[],\"partyLifeCycles\":[]}";
//		
//		
//		JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
//		
//		Result result = JSONToResult.convert(jsonObject.toString());
//		
//		System.out.println(ResultToJSON.convert(result));
		
		Map<String, String> input = new HashMap();
		
		input.put("flowType", AuthConstants.PRE_LOGIN_FLOW);
		
		Map<String, Object> header = new HashMap();
		
		header.put(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS,  "{\"os\":\"80\",\"dm\":\"\",\"did\":\"EF9C431B-205B-41A9-A7FD-65CBA12359BE\",\"ua\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36\",\"aid\":\"KonyOLB\",\"aname\":\"KonyOLB\",\"chnl\":\"desktop\",\"plat\":\"web\",\"aver\":\"4.2.6\",\"atype\":\"spa\",\"stype\":\"b2c\",\"kuid\":\"dbpolbuser\",\"mfaid\":\"bc7e5253-ee34-454d-8045-7d7420fa6d56\",\"mfbaseid\":\"5ca0d383-4f49-4155-8d16-5c795fdf3eda\",\"mfaname\":\"DbpLocalServices\",\"sdkversion\":\"9.0.0\",\"sdktype\":\"js\",\"fid\":\"frmCardManagement\",\"sessiontype\":\"I\",\"rsid\":\"1586239557164-295c-bd70-1cd5\",\"svcid\":\"Cards\"}");
		
		System.out.println(TokenUtils.getT24AuthToken(input, header));
		
		
	}
}