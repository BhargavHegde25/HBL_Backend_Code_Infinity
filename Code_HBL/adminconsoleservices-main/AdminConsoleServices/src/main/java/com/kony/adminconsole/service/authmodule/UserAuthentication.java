package com.kony.adminconsole.service.authmodule;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.crypto.BCrypt;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.commons.utils.WriteToLog;
import com.kony.adminconsole.dto.AuthenticationResult;
import com.kony.adminconsole.dto.InternalUser;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.InternalUserHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.AuthenticationResults;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.StatusEnum;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Class to perform User Authentication
 * 
 * @author Aditya Mankal
 *
 */
public class UserAuthentication {

    public static final int PASSWORD_HASH_ROUNDS = 11;

    public static final int MAX_ALLOWED_LOGIN_ATTEMPTS = 5;
    public static final int PASSWORD_VALIDITY_TIME_DEFAULT_DURATION_MINS = 45 * 24 * 60;// 45 Days * 24 hours * 60
                                                                                        // minutes

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    /**
     * Method to perform User Authentication
     * 
     * @param username
     * @param password
     * @param requestInstance
     * @return AuthenticationResult
     */
    public static AuthenticationResult authenticateUser(String username, String password,
            DataControllerRequest requestInstance) {

        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.setAuthenticationSuccessful(false);

        try {

            // Validate Input
            if (StringUtils.isAnyBlank(username, password)) {
                // Return Invalid Credentials Response
                diagnostic.prepareDebug("Returning Invalid-Credentials Result...").log();
                authenticationResult.setCode(AuthenticationResults.INVALID_CREDENTIALS.getCode());
                authenticationResult.setMessage(AuthenticationResults.INVALID_CREDENTIALS.getMessage());
                AuditHandler.auditAdminActivity(requestInstance, username, null, ModuleNameEnum.LOGIN, EventEnum.LOGIN,
                        ActivityStatusEnum.FAILED, "Invalid input details");
                return authenticationResult;
            }

            /* Fetch Internal User Profile */
            InternalUser internalUserProfile = InternalUserHandler.getUserProfile(username, requestInstance);
            authenticationResult.setUserProfile(internalUserProfile);

            /* Check for Account Existence */
            if (internalUserProfile == null) {
                // Unrecognised Account
                diagnostic.prepareDebug("Unrecognised Account.Returning Unrecognised-Account Result...").log();
                authenticationResult.setCode(AuthenticationResults.UNRECOGNIZED_ACCOUNT.getCode());
                authenticationResult.setMessage(AuthenticationResults.UNRECOGNIZED_ACCOUNT.getMessage());
                AuditHandler.auditAdminActivity(requestInstance, username, null, ModuleNameEnum.LOGIN, EventEnum.LOGIN,
                        ActivityStatusEnum.FAILED, "Invalid input details");
                return authenticationResult;
            }

            /* Validate Credentials */
            boolean isCorrectPassword = isCorrectPassword(password, internalUserProfile.getHashedPassword());
//            diagnostic.prepareDebug("Password validation result:" + isCorrectPassword).log();
	    	try {
			ThreadExecutor.execute(
					()-> WriteToLog.debug("Password validation result:" + isCorrectPassword)
					);
			} catch (InterruptedException e) {
				
				alert.prepareError("Error occurred: ", e).log();
			}
            
            if (isCorrectPassword == false) {

                // Increment failed login attempt count
                diagnostic.prepareDebug("Incrementing Failed Login Attempt Count..").log();
                internalUserProfile.setFailedLoginAttemptCount(internalUserProfile.getFailedLoginAttemptCount() + 1);
                InternalUserHandler.setFailedLoginAttempCount(internalUserProfile.getId(),
                        internalUserProfile.getFailedLoginAttemptCount(), requestInstance);
                diagnostic.prepareDebug("Incremented Failed Login Attempt Count").log();

                // Check if the failed sequential login attempt count has exceeded the max
                // allowed attempt count
                if (internalUserProfile.getFailedLoginAttemptCount() >= MAX_ALLOWED_LOGIN_ATTEMPTS) {

                    // Suspend Account
                    diagnostic.prepareDebug("Exceeded maximum allowed password attempts. Suspending account..").log();
                    InternalUserHandler.suspendUserAccount(internalUserProfile.getId(), requestInstance);

                    // Return suspended account response
                    diagnostic.prepareDebug("Account suspended.Returning Account-Suspended Result...").log();
                    authenticationResult.setCode(AuthenticationResults.ACCOUNT_SUSPENDED.getCode());
                    authenticationResult.setMessage(AuthenticationResults.ACCOUNT_SUSPENDED.getMessage());
                    AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
                            ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED,
                            "Invalid credentials. Account suspended");
                    return authenticationResult;
                }

                // Return Invalid Credentials Response
                diagnostic.prepareDebug("Returning Invalid-Credentials Result...").log();
                authenticationResult.setCode(AuthenticationResults.INVALID_CREDENTIALS.getCode());
                authenticationResult.setMessage(AuthenticationResults.INVALID_CREDENTIALS.getMessage());
                AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
                        ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED,
                        "Invalid credentials. Incorrect password attempt count: "
                                + internalUserProfile.getFailedLoginAttemptCount());
                return authenticationResult;
            }

            /* Check Password Validity */
            if (internalUserProfile.hasSuperAdminPrivilages() == false) {
            	
//                diagnostic.prepareDebug("Verifying Password validity..").log();
                try {
					ThreadExecutor.execute(
							()-> WriteToLog.debug("Verifying Password validity..")
							);
				} catch (InterruptedException e) {
					
					alert.prepareError("Error occurred: ", e).log();
				}
                boolean hasPasswordExpired = hasPasswordExpired(internalUserProfile,
                        PASSWORD_VALIDITY_TIME_DEFAULT_DURATION_MINS);
//                diagnostic.prepareDebug("Password expiry result:" + hasPasswordExpired).log();
                try {
					ThreadExecutor.execute(
							()-> WriteToLog.debug("Password expiry result:" + hasPasswordExpired)
							);
				} catch (InterruptedException e) {
					
					alert.prepareError("Error occurred: ", e).log();
				}
                if (hasPasswordExpired == true) {
                    // Return Expired Password Response
                    diagnostic.prepareDebug("Returning Password-Expired Result...").log();
                    authenticationResult.setCode(AuthenticationResults.PASSWORD_EXPIRED.getCode());
                    authenticationResult.setMessage(AuthenticationResults.PASSWORD_EXPIRED.getMessage());
                    AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
                            ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED, "Password expired");
                    return authenticationResult;
                }
            }

            /* Check Account Status */
            boolean isAccountActive = StringUtils.equals(internalUserProfile.getStatus(), StatusEnum.SID_ACTIVE.name());
            if (isAccountActive == false) {
                if (StringUtils.equals(internalUserProfile.getStatus(), StatusEnum.SID_SUSPENDED.name())) {
                    // Return Suspended Account Response
                    diagnostic.prepareDebug("Suspended-Account.Returning Account-Suspended Result...").log();
                    authenticationResult.setCode(AuthenticationResults.ACCOUNT_SUSPENDED.getCode());
                    authenticationResult.setMessage(AuthenticationResults.ACCOUNT_SUSPENDED.getMessage());
                    AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
                            ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED, "User account suspended");
                } else {
                    // Return Inactive Account Response
                    diagnostic.prepareDebug("Inactive-Account.Returning Inactive-Account Result...").log();
                    authenticationResult.setCode(AuthenticationResults.ACCOUNT_INACTIVE.getCode());
                    authenticationResult.setMessage(AuthenticationResults.ACCOUNT_INACTIVE.getMessage());
                    AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
                            ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED, "User account inactive");
                }
                return authenticationResult;
            }

            /* Verify Role Status */
            boolean isRoleActive = StringUtils.equals(internalUserProfile.getRoleStatusId(),
                    StatusEnum.SID_ACTIVE.name());
            if (isRoleActive == false) {
                // Return Inactive Role Response
                diagnostic.prepareDebug("Inactive-Role.Returning Inactive-Role Result...").log();
                authenticationResult.setCode(AuthenticationResults.ROLE_INACTIVE.getCode());
                authenticationResult.setMessage(AuthenticationResults.ROLE_INACTIVE.getMessage());
                AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
                        ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED, "User role inactive");
                return authenticationResult;
            }

            /* Authentication Success */
//            diagnostic.prepareDebug("Authentication pass").log();
            try {
				ThreadExecutor.execute(
						()-> WriteToLog.debug("Authentication pass")
						);
			} catch (InterruptedException e) {
				
				alert.prepareError("Error occurred: ", e).log();
			}
            
            // Reset Failed Login Attempt count
            InternalUserHandler.setFailedLoginAttempCount(internalUserProfile.getId(), 0, requestInstance);
//            diagnostic.prepareDebug("Successfully Reset failed login attempt count").log();
            try {
				ThreadExecutor.execute(
						()-> WriteToLog.debug("Successfully Reset failed login attempt count")
						);
			} catch (InterruptedException e) {
				
				alert.prepareError("Error occurred: ", e).log();
			}
            // Set Last-Login Date Time
            final Calendar calendar  = Calendar.getInstance();
			final int      utcOffset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
			final long     tempDate  = new Date().getTime();
			
            InternalUserHandler.setLastDateLoginTime(internalUserProfile.getId(), new Date(tempDate - utcOffset), requestInstance);
//            diagnostic.prepareDebug("Successfully Set last-login date time").log();
            try {
				ThreadExecutor.execute(
						()-> WriteToLog.debug("Successfully Set last-login date time")
						);
			} catch (InterruptedException e) {
				
				alert.prepareError("Error occurred: ", e).log();
			}

//            diagnostic.prepareDebug("Returning Authentication sucess result").log();
            try {
				ThreadExecutor.execute(
						()-> WriteToLog.debug("Returning Authentication sucess result")
						);
			} catch (InterruptedException e) {
				
				alert.prepareError("Error occurred: ", e).log();
			}
            authenticationResult.setAuthenticationSuccessful(true);
            authenticationResult.setCode(AuthenticationResults.AUTHENTICATION_SUCCESSFUL.getCode());
            authenticationResult.setMessage(AuthenticationResults.AUTHENTICATION_SUCCESSFUL.getMessage());
//            AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
//                    ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.SUCCESSFUL, "Login successful");
          
		AuditHandler.auditAdminActivity(requestInstance, username, internalUserProfile.getRoleName(),
			                    ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.SUCCESSFUL, "Login successful");

        } catch (ApplicationException e) {
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            authenticationResult.setCode(AuthenticationResults.APPLICATION_EXCEPTION.getCode());
            String exceptionMessage = AuthenticationResults.APPLICATION_EXCEPTION.getMessage() + ".Exception Code:"
                    + e.getErrorCodeEnum().getErrorCodeAsString() + ". Exception Message:"
                    + e.getErrorCodeEnum().getMessage();
            authenticationResult.setMessage(exceptionMessage);
        } catch (Exception e) {
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            authenticationResult.setCode(AuthenticationResults.UNEXPECTED_EXCEPTION.getCode());
            authenticationResult.setMessage(AuthenticationResults.UNEXPECTED_EXCEPTION.getMessage());
        }

        /* Return Authentication Result */
        return authenticationResult;
    }

    /**
     * Method to determine if the password has expired
     * 
     * @param internalUserProfile
     * @param passwordValiditiyDurationInMinutes
     * @return true if the password has expired <br>
     *         false otherwise
     */
    private static boolean hasPasswordExpired(InternalUser internalUserProfile,
            int passwordValiditiyDurationInMinutes) {

        // Default to profile creation Date Time if the last password change time is not
        // present
        Date lastPasswordChangeDateTime = internalUserProfile.getLastPasswordChangedTime() == null
                ? internalUserProfile.getCreatedTime()
                : internalUserProfile.getLastPasswordChangedTime();

        if (lastPasswordChangeDateTime == null) {
            return true;// Password was never set
        }

        // Compute time difference between current system time and last password change
        // time
        Date currentDate = Calendar.getInstance().getTime();
        long timeDifference = currentDate.getTime() - lastPasswordChangeDateTime.getTime();

        // Convert time from milliseconds to minutes
        timeDifference = TimeUnit.MILLISECONDS.toMinutes(timeDifference);

        // Return Password Expiry status
        return timeDifference > passwordValiditiyDurationInMinutes;
    }

    /**
     * Method to compare the plain text password with the hashed password
     * 
     * @param plainTextPassword
     * @param hashedPassword
     * @return true if the password is correct <br>
     *         false otherwise
     */
    private static boolean isCorrectPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            // Supressed exception
            return false;
        }
    }

}
