package com.temenos.msArrangement.utils;

import com.temenos.msArrangement.utils.ErrorConstants;
import com.temenos.msArrangement.utils.MWConstants;
import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public enum ErrorCodeEnum {

    ERR_10000(10000, ErrorConstants.CUSTOMER_360_API_LOGIN_FAILURE),
    ERR_10001(10001, ErrorConstants.INVALID_DETAILS),
    ERR_10002(10002, "Customer not found"),
    ERR_10003(10003, ErrorConstants.FAILED_TO_FETCH_USERNAME),
    ERR_10004(10004, ErrorConstants.BACKEND_IDENTIFIER_NOT_FOUND),
    ERR_10005(10005, ErrorConstants.INVALID_DETAILS),
    ERR_10006(10006, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_10007(10007, ErrorConstants.FAILED_TO_FETCH_USERNAME),
    ERR_10008(10008, ErrorConstants.FAILED_TO_FETCH_USERNAME),
    ERR_10009(10009, ErrorConstants.PROVIDE_USERNAME_AND_PASSWORD),
    ERR_10010(10010, ErrorConstants.INVALID_PASSWORD_RESETLINK),
    ERR_10011(10011, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_10012(10012, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10013(10013, ErrorConstants.PASSWORD_CANNOT_RESET),
    ERR_10014(10014, ErrorConstants.SECURITY_EXCEPTION),
    ERR_10015(10015, ErrorConstants.INVALID_PASSWORD_RESETLINK),
    ERR_10016(10016, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10017(10017, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10018(10018, ErrorConstants.RECORDS_NOT_EXISTS),
    ERR_10019(10019, ErrorConstants.SECURITY_EXCEPTION),
    ERR_10020(10020, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),
    ERR_10022(10022, "OTP request limit is excceeded than allowed number."),
    ERR_10023(10023, ErrorConstants.INVALID_DETAILS),
    ERR_10024(10024, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10025(10025, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_10026(10026, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10027(10027, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_10028(10028, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10029(10029, ErrorConstants.PROVIDE_USERNAME),
    ERR_10030(10030, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10031(10031, ErrorConstants.ERROR_IN_CREATE_PROSPECT),
    ERR_10032(10032, ErrorConstants.PROVIDE_USERNAME),
    ERR_10033(10033, ErrorConstants.BACKEND_IDENTIFIER_NOT_FOUND),
    ERR_10034(10034, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10035(10035, ErrorConstants.SECURITY_EXCEPTION),
    ERR_10036(10036, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10037(10037, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),
    ERR_10038(10038, ErrorConstants.INVALID_DETAILS),
    ERR_10039(10039, ErrorConstants.ERROR_IN_CREATE),
    ERR_10040(10040, ErrorConstants.ERROR_IN_CREATE),
    ERR_10041(10041, ErrorConstants.USERNAME_ALREADY_EXISTS),
    ERR_10042(10042, ErrorConstants.PROVIDE_USERNAME),
    ERR_10043(10043, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10044(10044, ErrorConstants.ERROR_IN_CREATE),
    ERR_10045(10045, "Customer creation failed."),
    ERR_10046(10046,
            "This Organization ID has already been enrolled for micro-business. Please contact the bank for more information"),
    ERR_10047(10047, "This Organization ID cannot be enrolled for micro-business with the given user information."),
    ERR_10048(10048,
            "This Membership ID cannot be enrolled for micro-business. Please contact the bank for more information"),
    ERR_10049(10049, ErrorConstants.USERNAME_ALREADY_EXISTS),
    ERR_10050(10050, "Invalid Org ID"),
    ERR_10051(10051, ErrorConstants.SECURITY_ERROR),
    ERR_10052(10052, ErrorConstants.USER_ALREADY_EXISTS),
    ERR_10053(10053, ErrorConstants.PROVIDE_USERNAME),
    ERR_10054(10054, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10055(10055, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10056(10056, ErrorConstants.ERROR_IN_CREATE),
    ERR_10057(10057, ErrorConstants.ERROR_IN_CREATE),
    ERR_10058(10058, "Please provide phone number."),
    ERR_10059(10059, "SMS not sent for OTP."),
    ERR_10060(10060, "Both Sms and email sent successfully"),
    ERR_10061(10061, "Only email sent successfully."),
    ERR_10062(10062, "Only Sms sent successfully."),
    ERR_10063(10063, "Sms and/or email not sent."),
    ERR_10064(10064, ErrorConstants.SECURITY_ERROR),
    ERR_10065(10065, "Invalid Org Id"),
    ERR_10066(10066, ErrorConstants.PROVIDE_USERNAME),
    ERR_10067(10067, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10068(10068, ErrorConstants.ERROR_IN_CREATE_CORPORATE),
    ERR_10069(10069, ErrorConstants.PROVIDE_USERNAME),
    ERR_10070(10070, ErrorConstants.BACKEND_IDENTIFIER_NOT_FOUND),
    ERR_10071(10071, ErrorConstants.FAILED_TO_FETCH_EMPLOYEE_DETAILS),
    ERR_10072(10072, "Exceeded maximum number of attempts"),
    ERR_10073(10073, "Secure Access Code has expired. Request a resend"),
    ERR_10074(10074, "Secure Access Code didn't match. Recheck"),
    ERR_10075(10075, "Invalid security key or service key."),
    ERR_10076(10076, ErrorConstants.USERNAME_ALREADY_EXISTS),
    ERR_10077(10077, ErrorConstants.USER_ALREADY_EXISTS),
    ERR_10078(10078, "Organization details not found."),
    ERR_10079(10079, ErrorConstants.PROVIDE_USERNAME),
    ERR_10080(10080, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_10081(10081, ErrorConstants.FAILED_TO_FETCH_OWNER_DETAILS),
    ERR_10082(10082, ErrorConstants.ERROR_IN_CREATE),
    ERR_10083(10083, ErrorConstants.INCORRECT_DETAILS),
    ERR_10084(10084, ErrorConstants.SESSIONTOKEN_EXPIRED),
    ERR_10085(10085, ErrorConstants.LOGIN_SUCCESS),
    ERR_10086(10086, ErrorConstants.INVALID_TOKEN),
    ERR_10087(10087, ErrorConstants.TRIAL_EXPIRED),
    ERR_10088(10088, ErrorConstants.PROFILE_LOCKED),
    ERR_10089(10089, ErrorConstants.PROFILE_SUSPENDED),
    ERR_10090(10090, ErrorConstants.ACTIVATION_BEFORE_LOGIN),
    ERR_10091(10091, ErrorConstants.SUBMIT_AGREEMENT),
    ERR_10092(10092, ErrorConstants.INACTIVE_USER),
    ERR_10093(10093, ErrorConstants.SUBMIT_AGREEMENT),
    ERR_10094(10094, ErrorConstants.AUTHENTICATE_WITH_CB),
    ERR_10095(10095, ErrorConstants.INCORRECT_USERNAME_OR_PASSWORD),
    ERR_10097(10097, ErrorConstants.NOT_AUTHORISED_TO_USE),
    ERR_10098(10098, ErrorConstants.REPORTING_PARAMS_MISSING),
    ERR_10099(10099, ErrorConstants.CSR_ASSIST_TOKEN),

    ERR_10116(10116, ErrorConstants.ERROR_IN_CREATE),
    ERR_10117(10117, ErrorConstants.REGISTER_YOUR_DEVICE),
    ERR_10118(10118, ErrorConstants.INVALID_PIN),
    ERR_10119(10119, ErrorConstants.INVALID_USERNAME),
    ERR_10120(10120, "Both username and password cannot be empty"),
    ERR_10121(10121, "please enter username"),
    ERR_10122(10122, " Dynamic message "),
    ERR_10123(10123, "Atleast one special character should be included in your username"),
    ERR_10124(10124, "Dynamic message"),
    ERR_10125(10125, "please enter password"),
    ERR_10126(10126, "Dynamic message"),
    ERR_10127(10127, "Atleast one lower case character should be included in your password"),
    ERR_10128(10128, "Atleast one upper case character should be included in your password"),
    ERR_10129(10129, "Atleast one number should be included in your password"),
    ERR_10130(10130, "Atleast one special character should be included in your password"),
    ERR_10131(10131, "Dynamic message"),
    ERR_10132(10132, "Dynamic message"),
    ERR_10133(10133, "OTP Requests are exceeded more than the limit allowed for a day"),
    ERR_10134(10134, "This Password is used before"),
    ERR_10135(10135, "Your password has expired. Please reset it."),
    ERR_10136(10136, "Applicant not found."),
    ERR_10137(10137, "Employee not found."),
    ERR_10138(10138, "Please provide phone number and Email."),
    ERR_10139(10139, ErrorConstants.NOT_AUTHORISED_TO_USE),
    ERR_10140(10140, ErrorConstants.ERROR_UPDATING_PASSWORD),
    ERR_10141(10141, ErrorConstants.INVALID_PASSWORD),
    ERR_10142(10142, "User doesn't exist in DBX."),
    ERR_10143(10143, "Error in creation"),
    ERR_10145(10145, "Password is already present in history"),
    ERR_10146(10146, "User exists in DBX."),
    ERR_10147(10147, "SMS request limit is excceeded than allowed number."),
    ERR_10148(10148, "Email request limit is excceeded than allowed number."),
    ERR_10149(10149, ErrorConstants.UNAUTHORIZED_ACCESS),
    ERR_10150(10150, "Error in updating details"),
    ERR_10151(10151, "Error in updating details"),
    ERR_10152(10152, "Not a valid user"),
    ERR_10153(10153, "Special Characters should not be included in your Username."),
    ERR_10155(10155, ErrorConstants.ERR_IN_CREATING_RECORD),
    ERR_10156(10156, ErrorConstants.ERR_IN_CREATING_RECORD),
    ERR_10157(10157, ErrorConstants.ERR_IN_CREATING_RECORD),
    ERR_10158(10158, ErrorConstants.ERR_IN_CREATING_RECORD),
    ERR_10159(10159, ErrorConstants.SECURITY_ERROR),
    ERR_10160(10160, ErrorConstants.EXTERNAL_BANK_ERROR),
    ERR_10161(10161, ErrorConstants.DBX_BANK_ERROR),
    ERR_10162(10162, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10163(10163, "Device details are empty"),
    ERR_10164(10164, ErrorConstants.ADMIN_CALL_FAILED),
    ERR_10165(10165, "Please provide valid DateOfBirth."),
    ERR_10166(10166, "Please provide valid Details."),
    ERR_10167(10167, "Service is down, please try again after sometime."),
    ERR_10168(10168, "Service is down, please try again after sometime."),
    ERR_10169(10169, "Service is down, please try again after sometime."),
    ERR_10170(10170, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10171(10171, ErrorConstants.ADMIN_CALL_FAILED),
    ERR_10172(10172, ErrorConstants.PROVIDE_USERNAME_AND_PASSWORD),
    ERR_10173(10173, ErrorConstants.INVALID_PASSWORD_RESETLINK),
    ERR_10174(10174, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_10175(10175, "This Password is used before"),
    ERR_10176(10176, "Password cannot be reset"),
    ERR_10177(10177, ErrorConstants.SECURITY_EXCEPTION),
    ERR_10178(10178, "Please provied UserID."),
    ERR_10179(10179, "Please provied UserID."),
    ERR_10180(10180, "P2P is not Activated."),
    ERR_10181(10181, ErrorConstants.USERNAME_ALREADY_EXISTS),
    ERR_10182(10182, "Internal Communication Error"),
    ERR_10183(10183, "Please provide valid Details."),
    ERR_10184(10184, ErrorConstants.INVALID_DETAILS),
    ERR_10185(10185, "Unable to determine terms and conditions."),
    ERR_10186(10186, "Already signed."),
    ERR_10187(10187, "Unable to determine terms and conditions."),
    ERR_10188(10188, "Unable to determine terms and conditions."),
    ERR_10189(10189, ErrorConstants.INVALID_DETAILS),
    ERR_10190(10190, "Unable to determine terms and conditions."),
    ERR_10191(10191, "Logged in user is not Admin"),
    ERR_10192(10192, "User not found with given userName"),
    ERR_10193(10193, "communication is not present for the given userName"),

    ERR_10500(10500, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10501(10501, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10502(10502, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10503(10503, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10504(10504, ErrorConstants.FAILED_TO_GENRATE_SERVICE_KEY),
    ERR_10505(10505, ErrorConstants.SECURE_ACCESS_CODE_EXPIRED),
    ERR_10506(10506, ErrorConstants.INVALID_SECURITY_QUESTIONS),
    ERR_10507(10507, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10508(10508, "Please provide phone number."),
    ERR_10509(10509, "OTP Requests are exceeded more than the limit allowed for a day"),
    ERR_10510(10510, "OTP request limit is excceeded than allowed number."),
    ERR_10511(10511, "SMS not sent for OTP."),
    ERR_10512(10512, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10513(10513, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10514(10514, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10515(10515, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10516(10516, ErrorConstants.SECURE_ACCESS_CODE_EXPIRED),
    ERR_10517(10517, "Secure Access Code didn't match. Recheck"),
    ERR_10518(10518, "Invalid security key or service key."),
    ERR_10519(10519, "Exceeded maximum number of attempts"),
    ERR_10520(10520, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10521(10521, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10522(10522, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10523(10523, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10524(10524, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10525(10525, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10526(10526, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10527(10527, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10528(10528, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10529(10529, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10530(10530, "Unable to determine the customer"),
    ERR_10531(10531, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10532(10532, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10533(10533, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10534(10534, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10535(10535, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10536(10536, "Unable to determine the customer"),
    ERR_10537(10537, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10538(10538, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10539(10539, "Internal error occured while processing the request"),
    ERR_10540(10540, "Internal error occured while processing the request"),
    ERR_10541(10541, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10542(10542, "Please provide phone number."),
    ERR_10543(10543, "OTP Requests are exceeded more than the limit allowed for a day"),
    ERR_10544(10544, "OTP request limit is excceeded than allowed number."),
    ERR_10545(10545, "SMS not sent for OTP."),
    ERR_10546(10546, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10547(10547, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10548(10548, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10549(10549, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10550(10550, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10551(10551, ErrorConstants.SECURE_ACCESS_CODE_EXPIRED),
    ERR_10552(10552, "Secure Access Code didn't match. Recheck"),
    ERR_10553(10553, "Invalid security key or service key."),
    ERR_10554(10554, "Exceeded maximum number of attempts"),
    ERR_10555(10555, "Please provide phone number."),
    ERR_10556(10556, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10557(10557, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10558(10558, "OTP Requests are exceeded more than the limit allowed for a day"),
    ERR_10559(10559, "OTP request limit is excceeded than allowed number."),
    ERR_10560(10560, "SMS not sent for OTP."),
    ERR_10561(10561, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10562(10562, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10563(10563, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10564(10564, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10565(10565, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10566(10566, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10567(10567, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10568(10568, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10569(10569, "Unable to determine the Organization"),
    ERR_10570(10570, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10571(10571, "Please provide phone number."),
    ERR_10572(10572, "OTP Requests are exceeded more than the limit allowed for a day"),
    ERR_10573(10573, "OTP request limit is excceeded than allowed number."),
    ERR_10574(10574, "SMS not sent for OTP."),
    ERR_10575(10575, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10576(10576, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10577(10577, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10578(10578, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10579(10579, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10580(10580, ErrorConstants.SECURE_ACCESS_CODE_EXPIRED),
    ERR_10581(10581, "Secure Access Code didn't match. Recheck"),
    ERR_10582(10582, "Invalid security key or service key."),
    ERR_10583(10583, "Exceeded maximum number of attempts"),
    ERR_10584(10584, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10585(10585, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10586(10586, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10587(10587, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10588(10588, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10589(10589, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10590(10590, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10591(10591, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10592(10592, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10593(10593, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10594(10594, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10595(10595, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10596(10596, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10597(10597, ErrorConstants.SECURE_ACCESS_CODE_EXPIRED),
    ERR_10598(10598, "Secure Access Code didn't match. Recheck"),
    ERR_10599(10599, "Invalid security key or service key."),

    ERR_10600(10600, "Exceeded maximum number of attempts"),
    ERR_10601(10601, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10602(10602, "Please provide phone number."),
    ERR_10603(10603, "OTP Requests are exceeded more than the limit allowed for a day"),
    ERR_10604(10604, "OTP request limit is excceeded than allowed number."),
    ERR_10605(10605, "SMS not sent for OTP."),
    ERR_10606(10606, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10607(10607, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10608(10608, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10609(10609, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10610(10610, ErrorConstants.FAILED_TO_GENRATE_SERVICE_KEY),
    ERR_10611(10611, "Sms and/or email not sent."),
    ERR_10612(10612, "Please provide phone number."),
    ERR_10613(10613, "Invalid security key or service key."),
    ERR_10614(10614, ErrorConstants.INVALID_SECURITY_QUESTIONS),
    ERR_10615(10615, "Exceeded maximum number of attempts"),
    ERR_10616(10616, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10617(10617, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10618(10618, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10619(10619, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10620(10620, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10621(10621, ErrorConstants.UNABLE_TO_DETERMINE),
    ERR_10622(10622, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10623(10623, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10624(10624, ErrorConstants.INVALID_REQUEST_PAYLOAD),
    ERR_10625(10625, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10626(10626, ErrorConstants.INVALID_SERVICE_KEY),

    ERR_10627(10627, "Not a valid user"),
    ERR_10628(10628, "Invalid request input"),
    ERR_10629(10629, "Failed to update the customer type"),

    ERR_10688(10688, "Customer ID is a mandatory field"),
    ERR_10689(10689, "Failed to fetch Customer Address"),
    ERR_10690(10690, "Failed to fetch Customer Contact Details"),
    ERR_10691(10691, "Failed to fetch Customer Security Questions"),
    ERR_10692(10692, "Failed to fetch Customer Entitlements"),

    ERR_10700(10700, "Internal Error"),
    ERR_10701(10701, ErrorConstants.FAILED_TO_GENRATE_SERVICE_KEY),

    ERR_10702(10702, "Failed to fetch group(s)"),

    ERR_10703(10703, "Invalid input parameters"),
    ERR_10704(10704, "Failed to fetch the organization group related features and actions"),

    ERR_10705(10705, "Invalid input parameters"),
    ERR_10706(10706, "Failed to fetch the organization group related features and actions"),

    ERR_10707(10707, "Failed to fetch the session attributes"),
    ERR_10708(10708, "Failed to fetch the group action limits"),
    ERR_10709(10709, "Failed to fetch the organization action limits"),

    ERR_10710(10710, "Invalid input parameters"),
    ERR_10711(10711, "Failed to fetch the customer approver list"),
    ERR_10712(10711, "No approvers present"),

    ERR_10194(10194, "Error in updating user image "),
    ERR_10195(10195, "Error in fetcing user image"),
    ERR_10196(10196, "Error in deleting user image"),
    ERR_10197(10197, "Invalid input parameters"),
    ERR_10198(10198, "Invalid input parameters"),
    ERR_10199(10199, "Invalid input parameters"),

    ERR_10201(10201, "Invalid inputs"),
    ERR_10202(10202, "Internal error occured"),
    ERR_10203(10203, "Invalid Organisation type"),
    ERR_10204(10204, "Mandatory fields are missing"),
    ERR_10205(10205, "Invalid input parameters"),
    ERR_10206(10206, "Invalid input parameters"),

    ERR_11001(11001, ErrorConstants.SECURITY_EXCEPTION),
    ERR_11002(11002, ErrorConstants.FAILED_TO_FETCH_ORGANIZATION_DETAILS),
    ERR_11003(11003, "Organization Update failed."),
    ERR_11004(11004, "Organization_id is empty"),
    ERR_11005(11005, "Organization_account_id empty"),
    ERR_11006(11006, ErrorConstants.FAILED_TO_FETCH_ORGANIZATIONACCOUNTS_DETAILS),
    ERR_11007(11007, ErrorConstants.SECURITY_EXCEPTION),
    ERR_11008(11008, "A company is already enrolled in this name. Please check & try again"),
    ERR_11009(11009, "Company Enrollment is Failed"),
    ERR_11010(11010, "A company is already enrolled with MembershipId and Taxid. Please check & try again"),
    ERR_11012(11012, ErrorConstants.FAILED_TO_FETCH_ORGANIZATIONEMPLOYES_DETAILS),
    ERR_11013(11013, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_11014(11014, ErrorConstants.INVALID_DETAILS),
    ERR_11015(11015, ErrorConstants.FAILED_TO_FETCH_TIN_DETAILS),
    ERR_11017(11017, "MemeberShip Creation Failed"),
    ERR_11018(11018, "Applicant Creation Failed"),
    ERR_11019(11019, "Membership creation failed at DBP"),
    ERR_11020(11020, ErrorConstants.SECURITY_EXCEPTION),
    ERR_11021(11021, ErrorConstants.INVALID_DETAILS),
    ERR_11022(11022, ErrorConstants.FAILED_TO_FETCH_ORGANIZATIONACCOUNTS_DETAILS),
    ERR_11023(11023, ErrorConstants.ACCOUNTS_NOT_EXISTS_IN_DBX),
    ERR_11024(11024, ErrorConstants.INVALID_DETAILS),
    ERR_11025(11025, ErrorConstants.FAILED_TO_FETCH_ACCOUNTS_DETAILS),
    ERR_11026(11026, ErrorConstants.RECORD_NOT_FOUND_IN_DBX),
    ERR_11027(11027, ErrorConstants.FAILED_TO_FETCH_ORGANIZATION_DETAILS),
    ERR_11028(11028, ErrorConstants.RECORD_NOT_FOUND_IN_DBX),
    ERR_11029(11029, ErrorConstants.BACKGROUNDVERIFICATION_FAILED),
    ERR_11030(11030, ErrorConstants.GIVEN_ACCOUNT_DOES_NOT_BELONG_TO_CUSTOMER),

    ERR_12000(12000, ErrorConstants.INTERNAL_SERVICE_ERROR),
    ERR_12001(12001, ErrorConstants.USER_UNAUTHORIZED),
    ERR_12002(12002, ErrorConstants.INVALID_ACCOUNT_NUMBER),
    ERR_12003(12003, ErrorConstants.NO_MATCHING_RECORDS_FOUND),
    ERR_12004(12004, ErrorConstants.RECORD_DOESNT_BELONG_TO_THE_SAME_COMPANY),
    ERR_12021(12021, ErrorConstants.MISSING_EFFECTIVE_DATE),
    ERR_12022(12022, ErrorConstants.MISSING_DEBIT_ACCOUNT),
    ERR_12025(12025, ErrorConstants.MISSING_TEMPLATE_NAME),
    ERR_12026(12026, ErrorConstants.MISSING_TRANSACTION_ID),
    ERR_12027(12027, ErrorConstants.MISSING_TEMPLATE_REQUEST_ID),
    ERR_12028(12028, ErrorConstants.MISSING_RECORDS),
    ERR_12029(12029, ErrorConstants.MISSING_MAX_AMOUNT),
    ERR_12030(12030, ErrorConstants.MISSING_TRANSACTION_DATE),
    ERR_12031(12031, ErrorConstants.MISSING_AMOUNT),
    ERR_12032(12032, ErrorConstants.MISSING_PAYEE),
    ERR_12033(12033, ErrorConstants.MISSING_DEBIT_CREDET_ACCOUNT),
    ERR_12034(12034, ErrorConstants.MISSING_BBGENERAL_TRANSACTION_TYPE_ID),
    ERR_12035(12035, ErrorConstants.MISSING_REQUEST_ID),
    ERR_12036(12036, ErrorConstants.MISSING_ACTION),
    ERR_12037(12037, ErrorConstants.MISSING_TEMPLATE_ID),
    ERR_12038(12038, ErrorConstants.MISSING_TEMPLATE_RECORD_ID),
    ERR_12039(12039, ErrorConstants.MISSING_TRANSACTIONENTRY),

    ERR_12101(12101, ErrorConstants.ACH_TEMPLATE_CREATION_FAILED),
    ERR_12102(12102, ErrorConstants.ACH_TEMPLATE_RECORD_CREATION_FAILED),
    ERR_12103(12103, ErrorConstants.ACH_TEMPLATE_SUB_RECORD_CREATION_FAILED),
    ERR_12104(12104, ErrorConstants.ACH_TRANSACTION_CREATION_FAILED),
    ERR_12105(12105, ErrorConstants.ACH_TRANSACTION_RECORD_CREATION_FAILED),
    ERR_12106(12106, ErrorConstants.ACH_TRANSACTION_SUB_RECORD_CREATION_FAILED),
    ERR_12107(12107, ErrorConstants.APPROVE_FAILED),

    ERR_12301(12301, ErrorConstants.TOTAL_AMOUNT_CANNOT_BE_ZERO),
    ERR_12302(12302, ErrorConstants.TOTAL_AMOUNT_GREATER_THAN_MAX_AMOUNT),
    ERR_12303(12303, ErrorConstants.FILE_TOO_LARGE),
    ERR_12304(12304, ErrorConstants.FILE_FORMATS_NOT_AVAILABLE),
    ERR_12305(12305, ErrorConstants.UNSUPPORTED_FILE_TYPE),

    ERR_12401(12401, ErrorConstants.SECURITY_ERROR),
    ERR_12402(12402, ErrorConstants.INVALID_REQUEST_PASS_USERNAME),
    ERR_12403(12403, ErrorConstants.UNAUTHORIZED_ACCESS),
    ERR_12404(12404, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_12405(12405, ErrorConstants.USER_CREATION_FAILED_IN_CORE),
    ERR_12406(12406, ErrorConstants.USER_DOESNT_BELONG_TO_ORG),
    ERR_12407(12407, ErrorConstants.SECURITY_ERROR),
    ERR_12408(12408, ErrorConstants.UNAUTHORIZED_ACCESS),
    ERR_12409(12409, ErrorConstants.INVALID_REQUEST_PASS_USERNAME),
    ERR_12410(12410, ErrorConstants.ACCOUNTS_MISSING_IN_REQUEST),
    ERR_12411(12411, ErrorConstants.CUSTOMER_ACCOUNT_CREATE_FAILED),
    ERR_12412(12412, ErrorConstants.CUSTOMER_ACCOUNT_DELETE_FAILED),
    ERR_12413(12413, ErrorConstants.CUSTOMER_ROLE_NOT_AVAILABLE),
    ERR_12414(12414, ErrorConstants.TRANSACTION_LIMITS_NOT_CREATED),
    ERR_12415(12415, ErrorConstants.TRANSACTION_LIMITS_NOT_UPDATED),
    ERR_12416(12416, ErrorConstants.INVALID_REQUEST_PASS_USERNAME),
    ERR_12417(12417, "Please provide a valid username."),
    ERR_12418(12418, ErrorConstants.SECURITY_ERROR),
    ERR_12419(12419, ErrorConstants.UNAUTHORIZED_ACCESS),
    ERR_12420(12420, "User has no email address to communicate."),
    ERR_12421(12421, "Please Provide a valid email of the user."),
    ERR_12422(12422, "User has no primary communication email."),
    ERR_12423(12423, "Account is already activated."),
    ERR_12424(12424, "Error occured while creating an activation token."),
    ERR_12425(12425, "Failed to send Email."),
    ERR_12426(12426, "You need to be more than 18 years to apply."),
    ERR_12427(12427, "Could not activate your account. Please contact the bank for more information"),
    ERR_12428(12428, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_12429(12429, "Invalid Activation Link. Please contact the bank for more information"),
    ERR_12430(12430, "Incorrect User Name. Please re-try."),
    ERR_12431(12431, "There is no user with this Username"),
    ERR_12432(12432, "Your account is already activated"),
    ERR_12433(12433, "Your password has been set sucessfully"),
    ERR_12434(12434, "Owner Creation failed."),
    ERR_12435(12435, "No Accounts are mapped for organization."),
    ERR_12436(12436, "Error ocurred while creating accounts."),
    ERR_12437(12437, ErrorConstants.SECURITY_ERROR),
    ERR_12438(12438, "Edit operation not permitted for this Transaction type"),
    ERR_12450(12450, "Failed to fetch campaign specifications"),
    ERR_12451(12451, "'chnl' mandatory reporting parameter not available"),
    ERR_12452(12452, "'chnl' mandatory reporting parameter is invalid"),
    ERR_12455(12455, "'UserName' mandatory input parameter not available"),
    ERR_12456(12456, "'screenName' mandatory input parameter not available"),
    ERR_12457(12457, "'scale' mandatory input parameter not available"),
    ERR_12458(12458, "Customer ID is a mandatory field"),
    ERR_12459(12459, "Campaign ID is a mandatory field"),
    ERR_12460(12460, "Failed to ignore the campaign"),

    ERR_13000(13000, ErrorConstants.INVALID_REQUEST),
    ERR_13001(13001, ErrorConstants.INVALID_REQUEST),
    ERR_13002(13002, ErrorConstants.INVALID_REQUEST),
    ERR_13003(13003, ErrorConstants.SECURITY_EXCEPTION),
    ERR_13004(13004, ErrorConstants.SECURITY_EXCEPTION),
    ERR_13005(13005, ErrorConstants.SECURITY_EXCEPTION),
    ERR_13006(13006, ErrorConstants.SECURITY_EXCEPTION),
    ERR_13007(13007, ErrorConstants.INVALID_REQUEST),
    ERR_13500(13500, ErrorConstants.ACH_PREPROCESSOR_COMPANY_VALIDATION),
    ERR_13501(13501, ErrorConstants.ACH_PREPROCESSOR_ACCOUNT_VALIDATION),
    ERR_13502(13502, ErrorConstants.ACH_PREPROCESSOR_TEMPLATE_FETCH_FAILED),
    ERR_13503(13503, "Failed to fetch customer actions"),
    ERR_13504(13504, "Customer id is a mandatory parameter"),
    ERR_13505(13505, "Action id is a mandatory parameter"),
    ERR_13506(13506, "Failed to read customer actions proc"),
    ERR_13507(13507, "Failed to read action limits table"),
    ERR_13508(13508, "Failed to read customer information"),
    ERR_13509(13509, "Failed to read features from identity scope"),
    ERR_13510(13510, "Failed to read feature_details_view "),
    ERR_13511(13511, "Failed to read organisation actions table"),
    ERR_13512(13512, "Failed to read customer actions table"),
    ERR_13513(13513, "Failed to associate Business user Action Limits"),
    ERR_13514(13514, "group id is a mandatory input"),
    ERR_13515(13515, "actions are mandatory input"),
    ERR_13516(13516, "accounts are mandatory input"),
    ERR_13517(13517, "Organisation id is a mandatory input"),
    ERR_13518(13518, "Failed to read group actions table"),
    ERR_13519(13519, "Failed to create customer action limit"),
    ERR_13520(13520, "Failed to get from member group table"),
    ERR_13521(13521, "Failed to update customer action"),
    ERR_13522(13522, "Failed to delete customer action"),
    ERR_13523(13523, "Features is a mandatory parameter"),

    ERR_20001(20001, "Internal Error"),
    ERR_20612(20612, "Username cannot be empty"),
    ERR_20613(20613, "Unable to find the user"),

    ERR_20691(20691, "Unexpected error has occurred at customer requests"),
    ERR_20692(20692, "Parameter status_id cannot be empty"),
    ERR_20693(20693, "Parameter channel_id cannot be empty"),
    ERR_20694(20694, "Parameter DeviceName cannot be empty"),
    ERR_20695(20695, "Parameter LastUsedIp cannot be empty"),
    ERR_20696(20696, "Parameter OperatingSystem cannot be empty"),
    ERR_20697(20697, "Parameter LastLoginTime cannot be empty"),
    ERR_20698(20698, ErrorConstants.BILLPAY_UPDATE_CONFIRMATION_NUMBER_VALIDATION),
    ERR_20699(20699, ErrorConstants.BILLPAY_UPDATE_STATUS_VALIDATION);

    public static final String ERROR_CODE_KEY = DBPConstants.DBP_ERROR_CODE_KEY;
    public static final String ERROR_MESSAGE_KEY = DBPConstants.DBP_ERROR_MESSAGE_KEY;
    public static final String OPSTATUS_CODE = DBPConstants.FABRIC_OPSTATUS_KEY;
    public static final String HTTPSTATUS_CODE = DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY;

    private int errorCode;
    private String message;

    private ErrorCodeEnum(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCodeAsString() {
        return String.valueOf(errorCode);
    }

    public Integer getErrorCodeAsInt() {
        return Integer.valueOf(errorCode);
    }

    public Result setErrorCode(Result result) {
        if (result == null) {
            result = new Result();
        }

        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "0", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));

        return result;
    }

    public Record setErrorCode(Record record) {
        if (record == null) {
            record = new Record();
        }

        record.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        record.addParam(new Param(ERROR_MESSAGE_KEY, this.getMessage(), MWConstants.STRING));
        record.addParam(new Param(OPSTATUS_CODE, "0", MWConstants.INT));
        record.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));

        return record;
    }

    public Result setErrorCode(Result result, String errorMessage) {
        if (result == null) {
            result = new Result();
        }

        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "0", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));

        return result;
    }

    public Record setErrorCode(Record record, String errorMessage) {
        if (record == null) {
            record = new Record();
        }

        record.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        record.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        record.addParam(new Param(OPSTATUS_CODE, "0", MWConstants.INT));
        record.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));

        return record;
    }

    public Result setErrorCode(Result result, String errorCode, String errorMessage) {
        if (result == null) {
            result = new Result();
        }

        result.addParam(new Param(ERROR_CODE_KEY, errorCode, MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(OPSTATUS_CODE, "0", MWConstants.INT));
        result.addParam(new Param(HTTPSTATUS_CODE, "0", MWConstants.INT));

        return result;
    }

    public JsonObject setErrorCode(JsonObject result) {
        if (result == null) {
            result = new JsonObject();
        }

        result.addProperty(ERROR_CODE_KEY, this.getErrorCodeAsInt());
        result.addProperty(ERROR_MESSAGE_KEY, this.getMessage());
        result.addProperty(OPSTATUS_CODE, Integer.parseInt("0"));
        result.addProperty(HTTPSTATUS_CODE, Integer.parseInt("0"));

        return result;
    }

    public JsonObject setErrorCode(JsonObject result, String message) {
        if (result == null) {
            result = new JsonObject();
        }

        result.addProperty(ERROR_CODE_KEY, this.getErrorCodeAsInt());
        result.addProperty(ERROR_MESSAGE_KEY, message);
        result.addProperty(OPSTATUS_CODE, Integer.parseInt("0"));
        result.addProperty(HTTPSTATUS_CODE, Integer.parseInt("0"));

        return result;
    }

}
