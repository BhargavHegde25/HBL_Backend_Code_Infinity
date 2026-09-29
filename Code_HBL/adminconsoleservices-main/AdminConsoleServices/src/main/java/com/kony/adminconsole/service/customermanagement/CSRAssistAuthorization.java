package com.kony.adminconsole.service.customermanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.resource.ApplicationResource;
import com.kony.adminconsole.commons.crypto.EncryptionUtils;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CSRAssistHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * CSRAssistAuthorization service is used to authorize for CSR assist
 * 
 * @author Alahari Prudhvi Akhil (KH2346)
 * 
 */
public class CSRAssistAuthorization implements JavaService2 {

    public static final String CSRAssistPermissionID = "PID45";
    //private static final String ONLINE_BANKING_DASHBOARD_URL = "/apps/OnlineBanking/#_frmAccountsLanding";
    private static final String LOANS_APPLY_PERSONAL_LOAN_URL = "/apps/ConsumerLending/#_frmYourLoanNewKA";
    private static final String LOANS_APPLY_CREDIT_LOAN_URL = "/apps/ConsumerLending/#_frmCashRewardsCreditCardsKA";
    private static final String LOANS_APPLY_VEHICLE_LOAN_URL = "/apps/ConsumerLending/#_frmYourVehicleLoanKA";
    private static final String LOANS_LEARN_CREDIT_LOAN_URL = "/apps/ConsumerLending/#_frmCashRewardsCreditCardsKA";
    private static final String LOANS_LEARN_PERSONAL_LOAN_URL = "/apps/ConsumerLending/#_frmPersonalLoanPostloginKA";
    private static final String LOANS_LEARN_VEHICLE_LOAN_URL = "/apps/ConsumerLending/#_frmVehicleLoanTypeKA";
    private static final String LOANS_RESUME_CREDIT_LOAN_URL = "/apps/ConsumerLending/#_frmVerifyCreditCard";
    private static final String LOANS_CREATE_APPLICANT_URL =
            "/apps/ConsumerLending/#_frmLogin2KA?new_customer=true&loantype=";
    private static final String LOANS_DASHBOARD_URL = "/apps/ConsumerLending/#_frmLogin2KA";
//    private static final String APP_ID_KONY_OLB = "OnlineBanking";
    private static final String APP_ID_KONY_CL = "ConsumerLending";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            Result processedResult = new Result();
            String customerId = requestInstance.getParameter("customerid");
            String accountId = StringUtils.EMPTY;
            String customerUsername = requestInstance.getParameter("customer_username");
            String loanId = requestInstance.getParameter("Loan_id");
            String loanType = requestInstance.getParameter("Loan_Type");
            String applicationId = requestInstance.getParameter("ApplicationId");
            String phoneNumber = requestInstance.getParameter("PhoneNumber");
            String HostURL = EnvironmentConfiguration.AC_CSR_ASSIST_CL_HOST_URL.getValue(requestInstance);
            String applicationType = requestInstance.getParameter("ApplicationType");

            if (methodID.equalsIgnoreCase("CSRAssistAuthorizationCreateApplicant")) {
                if (StringUtils.isBlank(loanType)) {
                    ErrorCodeEnum.ERR_20713.setErrorCode(processedResult);
                    return processedResult;
                }

                processedResult.addParam(
                        new Param("BankingURL", HostURL + LOANS_CREATE_APPLICANT_URL + loanType));
                return processedResult;

            }
            String currentActionLogs = "", resolvedURL = "", appId = "";

            // Check mandatory fields
            if (StringUtils.isBlank(customerId) && StringUtils.isBlank(customerUsername)) {
	            ErrorCodeEnum.ERR_20688.setErrorCode(processedResult);
	            return processedResult; 	
            }
            if (methodID.equalsIgnoreCase("CSRAssistCustomerOnboardingResumeApp") || methodID.equalsIgnoreCase("CSRAssistProspectOnboardingResumeApp")) {
	            if (StringUtils.isBlank(applicationId)) {
		            ErrorCodeEnum.ERR_20149.setErrorCode(processedResult);
		            return processedResult; 	
	            }
            }
            if (methodID.equalsIgnoreCase("CSRAssistProspectOnboardingResumeApp")) {
	            if (StringUtils.isBlank(phoneNumber)) {
		            ErrorCodeEnum.ERR_20151.setErrorCode(processedResult);
		            return processedResult; 	
	            }
            }

            // Check the access control for this customer for current logged-in internal
            // user
           if (!methodID.equalsIgnoreCase("CSRAssistProspectOnboardingResumeApp") && !methodID.equalsIgnoreCase("CSRAssistAuthorizationForOrig")) {
		        CustomerHandler.doesCurrentLoggedinUserHasAccessToCustomer(customerUsername, customerId, requestInstance,
		                processedResult);
		        if (processedResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
		            return processedResult;
		        }
            }
            // End of access check

            // Getting customer and prospect more details
            CustomerHandler customerHandler = new CustomerHandler();
            JSONObject customerDetails = null;
            customerDetails = customerHandler.getCustomerDetails(customerUsername, customerId,
                    requestInstance);
            customerId = customerDetails.getString("id");
            customerUsername = customerDetails.getString("UserName");
        

            // Check for member consent
            if (customerDetails.getString("IsAssistConsented").equalsIgnoreCase("false")
                    || customerDetails.getString("IsAssistConsented").equalsIgnoreCase("0")) {
                ErrorCodeEnum.ERR_20714.setErrorCode(processedResult);
                return processedResult;
            }
        
            if (methodID.equalsIgnoreCase("CSRAssistAuthorizationCreateApplicant")) {

                processedResult.addParam(
                        new Param("BankingURL", HostURL + LOANS_CREATE_APPLICANT_URL + loanType));
                return processedResult;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorization")) {
                // Check for member status. If status is new then CSR assist is not allowed
                if (customerDetails.getString("Status_id").equalsIgnoreCase("SID_CUS_NEW")) {
                    ErrorCodeEnum.ERR_20715.setErrorCode(processedResult);
                    return processedResult;
                }
                resolvedURL = EnvironmentConfiguration.AC_CSR_ASSIST_OLB_HOST_URL.getValue(requestInstance);
                       
                currentActionLogs = "OLB application";
                appId = EnvironmentConfiguration.AC_APP_ID_OLB.getValue(requestInstance);

            }else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationViewSpecAccount")) {
                // Check for member status. If status is new then CSR assist is not allowed
                if (customerDetails.getString("Status_id").equalsIgnoreCase("SID_CUS_NEW")) {
                    ErrorCodeEnum.ERR_20715.setErrorCode(processedResult);
                    return processedResult;
                }
                
                if (StringUtils.isBlank(requestInstance.getParameter("accountId"))) {
                    ErrorCodeEnum.ERR_20552.setErrorCode(processedResult);
                    return processedResult;
                }
                
                accountId = requestInstance.getParameter("accountId");
                       
                currentActionLogs = "OLB application";
                appId = EnvironmentConfiguration.AC_APP_ID_OLB.getValue(requestInstance);
                
                resolvedURL = EnvironmentConfiguration.AC_CSR_ASSIST_OLB_ACC_URL.getValue(requestInstance);

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationApplyPersonalLoan")) {
                resolvedURL = HostURL + LOANS_APPLY_PERSONAL_LOAN_URL;
                currentActionLogs = "apply Personal loan";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationApplyCreditLoan")) {
                resolvedURL = HostURL + LOANS_APPLY_CREDIT_LOAN_URL;
                currentActionLogs = "apply Credit Card loan";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationApplyVehicleLoan")) {
                resolvedURL = HostURL + LOANS_APPLY_VEHICLE_LOAN_URL;
                currentActionLogs = "apply Vehicle loan";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationLearnCreditLoan")) {
                resolvedURL = HostURL + LOANS_LEARN_CREDIT_LOAN_URL;
                currentActionLogs = "learn about Credit Card loan";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationLearnVehicleLoan")) {
                resolvedURL = HostURL + LOANS_LEARN_VEHICLE_LOAN_URL;
                currentActionLogs = "learn about Vehicle loan";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationLearnPersonalLoan")) {
                resolvedURL = HostURL + LOANS_LEARN_PERSONAL_LOAN_URL;
                currentActionLogs = "learn about Personal loan";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationResumeLoan")) {
                resolvedURL = HostURL;
                if (loanType.equalsIgnoreCase("PERSONAL_APPLICATION")) {
                    resolvedURL += LOANS_APPLY_PERSONAL_LOAN_URL;
                } else if (loanType.equalsIgnoreCase("VEHICLE_APPLICATION")) {
                    resolvedURL += LOANS_APPLY_VEHICLE_LOAN_URL;
                } else if (loanType.equalsIgnoreCase("CREDIT_CARD_APPLICATION")) {
                    resolvedURL += LOANS_RESUME_CREDIT_LOAN_URL;
                }
                currentActionLogs = "resume a loan application with id: " + loanId + " ";
                appId = APP_ID_KONY_CL;

            } else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationNativeApplication")) {
                requestInstance.setAttribute("isServiceBeingAccessedByOLB", true);
                resolvedURL = HostURL + LOANS_DASHBOARD_URL;
                appId = APP_ID_KONY_CL;

            }
            else if (methodID.equalsIgnoreCase("CSRAssistCustomerOnboardingResumeApp")) {
            	currentActionLogs = "KonyOnboarding resume application for customer";
            	appId = EnvironmentConfiguration.AC_APP_ID_ONBOARDING.getValue(requestInstance);
            	resolvedURL = EnvironmentConfiguration.AC_CSR_ASSIST_CO_HOST_URL.getValue(requestInstance) + 
            			"/apps/" +  appId + "/#_frmLanding";
            	
            }
            else if (methodID.equalsIgnoreCase("CSRAssistCustomerOnboardingNewApp")) {
            	currentActionLogs = "KonyOnboarding new application for customer";
            	appId = EnvironmentConfiguration.AC_APP_ID_ONBOARDING.getValue(requestInstance);
            	resolvedURL = EnvironmentConfiguration.AC_CSR_ASSIST_CO_HOST_URL.getValue(requestInstance) + 
            			"/apps/" +  appId + "/#_frmLanding";
            	
            }
            else if (methodID.equalsIgnoreCase("CSRAssistProspectOnboardingResumeApp")) {
            	currentActionLogs = "KonyOnboarding resume application for prospect";
            	appId = EnvironmentConfiguration.AC_APP_ID_ONBOARDING.getValue(requestInstance);
            	resolvedURL = EnvironmentConfiguration.AC_CSR_ASSIST_CO_HOST_URL.getValue(requestInstance) + 
            			"/apps/" +  appId + "/#_frmLanding";
               
            }
            else if (methodID.equalsIgnoreCase("CSRAssistAuthorizationForOrig")) {
            	currentActionLogs = "KonyOnboarding resume application for prospect";
            	appId = EnvironmentConfiguration.AC_APP_ID_ONBOARDING.getValue(requestInstance);
            	resolvedURL = EnvironmentConfiguration.AC_CSR_ASSIST_CO_HOST_URL.getValue(requestInstance) + 
            			"/apps/" +  appId + "/#_frmLanding";
               
            }
            resolvedURL += "?session_token=";
            
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            CSRAssistHandler csrAssistHandler = new CSRAssistHandler();

            String csrAssistGrantToken;
            try {
            	String customerTypeId = new String();
            	if(customerDetails.has("CustomerType_id"))
            		customerTypeId = customerDetails.getString("CustomerType_id");
		         csrAssistGrantToken = csrAssistHandler.generateCSRAssistGrantToken(customerId,
		        		 customerTypeId, accountId, userDetailsBeanInstance, appId, requestInstance);
            } catch (Exception e) {
                ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.LOGIN,
                        ActivityStatusEnum.FAILED, "CSR Assist for " + currentActionLogs
                                + ": Session token generation Failed. username: " + customerUsername);
                return processedResult;
            }

            if (StringUtils.isBlank(csrAssistGrantToken)) {
                ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.LOGIN,
                        ActivityStatusEnum.FAILED, "CSR Assist for " + currentActionLogs
                                + ": Session token generation Failed. username: " + customerUsername);
                return processedResult;
            }            

            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.LOGIN,
                    ActivityStatusEnum.SUCCESSFUL, "CSR Assist for " + currentActionLogs
                            + ": Session token generation successful. username: " + customerUsername);
            // Return session_token
            resolvedURL += csrAssistGrantToken;
            
            // Encrypting query params
            if(applicationId != null && !applicationId.isEmpty()) {
            	applicationId = EncryptionUtils.encrypt(applicationId, EnvironmentConfiguration.AC_ENCRYPTION_KEY.getValue(requestInstance));
            }
            if(phoneNumber != null && !phoneNumber.isEmpty()) {
            	phoneNumber = EncryptionUtils.encrypt(phoneNumber, EnvironmentConfiguration.AC_ENCRYPTION_KEY.getValue(requestInstance));
            }
            
            if (methodID.equalsIgnoreCase("CSRAssistAuthorizationResumeLoan")) {
                resolvedURL += "&Loan_id=" + loanId + "&isResume=true";
            }
            if (methodID.equalsIgnoreCase("CSRAssistCustomerOnboardingResumeApp")) {
                resolvedURL += "&ApplicationId=" + applicationId + "&Identifier=c360" + "&IsCustomer=true";
            }
            if (methodID.equalsIgnoreCase("CSRAssistCustomerOnboardingNewApp")) {
                resolvedURL += "&Identifier=c360" + "&IsCustomer=true";
            }
            if (methodID.equalsIgnoreCase("CSRAssistProspectOnboardingResumeApp")) {
                resolvedURL += "&ApplicationId=" + applicationId + "&Phone=" + phoneNumber + "&Identifier=c360" + "&IsCustomer=false";
            }
            if (methodID.equalsIgnoreCase("CSRAssistAuthorizationForOrig")) {
                resolvedURL += "&ApplicationId=" + applicationId + "&Identifier=c360";
                if(customerDetails.has("CustomerType_id")) {
                	if(customerDetails.getString("CustomerType_id").equalsIgnoreCase("TYPE_ID_PROSPECT")){
                		resolvedURL += "&IsCustomer=false";
                	}
                	else if(customerDetails.getString("CustomerType_id").equalsIgnoreCase("TYPE_ID_RETAIL")) {
                		resolvedURL += "&IsCustomer=true";
                	}                	
                }
            }
			if (StringUtils.equalsAny(methodID, "CSRAssistProspectOnboardingResumeApp",
					"CSRAssistCustomerOnboardingResumeApp" , "CSRAssistAuthorizationForOrig")) {
				ApplicationResource applicationResource = DBPAPIAbstractFactoryImpl
						.getResource(ApplicationResource.class);
				String bankRef = applicationResource.getApplicationBankReference(methodID, inputArray, requestInstance,
						responseInstance);
				if (!StringUtils.isBlank(bankRef))
					resolvedURL += "&BankRef=" + bankRef;
				
				if (StringUtils.isNotBlank(applicationType))
					resolvedURL += "&FormCode=" + (applicationType.equalsIgnoreCase("SME")
							? EnvironmentConfiguration.SME_ONBOARDING_ENTITY_DEFINTION.getValue(requestInstance)
							: EnvironmentConfiguration.ONBOARDING_ENTITY_DEFINTION.getValue(requestInstance));

			}
            processedResult.addParam(new Param("BankingURL", resolvedURL, FabricConstants.STRING));

            return processedResult;
        } catch (ApplicationException ae) {
            alert.prepareError("Runtime ApplicationException Trace: " + ae.getStackTrace()[0].toString()).log();
            Result errorResult = new Result();
            ae.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } 
        catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

}
