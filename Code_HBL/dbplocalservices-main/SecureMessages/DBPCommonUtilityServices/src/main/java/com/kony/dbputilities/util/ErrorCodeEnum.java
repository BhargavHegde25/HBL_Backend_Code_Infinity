package com.kony.dbputilities.util;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
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
    ERR_10050(10050, "Invalid input"),
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
    ERR_10207(10207, "Invalid input parameters"),

    ERR_10208(10208, "Party configuration is not available"),
    ERR_10209(10209, "Party identifier information is not available"),
    ERR_10210(10210, "Core configuration is not available"),
    ERR_10211(10211, "Core identifier information is not available"),
    ERR_10212(10212, "Party creation failed"),
    ERR_10213(10213, "Party update failed"),
    ERR_10214(10214, "dbxProspect creation failed"),
    ERR_10215(10215, "dbxProspect update failed"),
    ERR_10216(10216, "coreProspect creation failed"),
    ERR_10217(10217, "coreProspect update failed"),
    ERR_10218(10218, "dbxCustomer update failed"),

    ERR_10219(10219, "Invalid input parameters"),
    ERR_10220(10220, "Something went wrong please try again after somtime"),
    ERR_10221(10221, "Invalid Shared Token"),

    ERR_10222(10222, ErrorConstants.INVALID_DETAILS),
    ERR_10223(10223, "Backend failed to fetch customer communication"),
    ERR_10224(10224, "Backend failed to fetch customer address"),
    ERR_10225(10225, "Backend failed to fetch organisation details"),

    ERR_10226(10226, "Backend failed to fetch the features"),
    ERR_10227(10227, "Features are empty"),

    ERR_10228(10228, "Backend failed to fetch business types"),
    ERR_10229(10229, "Backend failed to fetch business role types"),

    ERR_10230(10230, "Backend failed to fetch authorized signatories"),
    ERR_10232(10232, ErrorConstants.INVALID_DETAILS),
    ERR_10231(10231, "could not find authorized signatories with the given details"),
    ERR_10233(10233,
            "Your registered phone number & email address were not found. Please contact the bank for assistance on enrolling your business."),

    ERR_10234(10234, ErrorConstants.INVALID_DETAILS),
    ERR_10235(10235, "Backend failed to create mfaservice record"),

    ERR_10236(10236, "Backend failed to fetch the account information"),
    ERR_10237(10237, ErrorConstants.INVALID_DETAILS),
    ERR_10239(10239, "Backend failed to fetch account information"),
    ERR_10240(10240, ErrorConstants.INVALID_DETAILS),
    ERR_10241(10241, "Account doesn't exists"),
    ERR_10238(10238, "Backend failed to fetch membership details"),

    ERR_10242(10242, "Backend failed to fetch the mfa service details"),
    ERR_10243(10243, "Mfa service details are not found"),
    ERR_10244(10244, "Backend failed to update the mfa service details"),
    ERR_10245(10245, "Backend failed to fetch the account information"),
    ERR_10246(10246, "Backend failed to fetch account information"),
    ERR_10247(10247, "Backend failed to fetch the account information"),

    ERR_10248(10248, "Not authorized to fetch the account information"),
    ERR_10249(10249, "Invalid Service Key"),
    ERR_10250(10250, ErrorConstants.INVALID_DETAILS),
    ERR_10251(10251, "No business type roles present"),
    ERR_10252(10252, "No business types present"),

    ERR_10253(10253, "Backend failed to fetch the account types"),
    ERR_10254(10254, "Failed to fetch the account types"),

    ERR_10255(10255, ErrorConstants.INVALID_DETAILS),
    ERR_10256(10256, "Backend failed to fetch valid accounts details"),

    ERR_10257(10257, ErrorConstants.INVALID_DETAILS),
    ERR_10258(10258, "No details found with given service key"),
    ERR_10259(10259, "please authenticate yourself before requesting information"),
    ERR_10260(10260, "Backend failed to fetch accounts information"),

    ERR_10261(10261, "Backend failed to fetch address information"),

    ERR_10262(10262, ErrorConstants.INVALID_DETAILS),

    ERR_10263(10263, "Backend failed to update organisation status"),
    ERR_10264(10264, "Backend failed to update organisation status"),
    ERR_10265(10265, ErrorConstants.INVALID_DETAILS),

    ERR_10266(10266, ErrorConstants.INVALID_DETAILS),
    ERR_10267(10267, "Backend failed to fetch the account information"),

    ERR_10268(10268, "Backend failed to fetch organization membership details"),
    ERR_10269(10269, "No membership details present"),

    ERR_10270(10270, "Backend failed to fetch membership owner details"),
    ERR_10271(10271, "Membership owner details are not present"),

    ERR_10272(10272, "Backend failed to fetch the account information"),

    ERR_10273(10273, "Backend failed to fetch the organisation information"),
    ERR_10274(10274, "Backend failed to fetch the organisation information"),

    ERR_10275(10275, "No accounts found for given customerId"),
    ERR_10276(10276, "Given master servicekey is invalid"),
    ERR_10277(10276, "Failed to update mfa service record"),

    ERR_10278(10278, "Backend failed to fetch customer details"),
    ERR_10279(10279, "Customer details not found"),
    ERR_10280(10280, "Backend failed to update customer details"),

    ERR_10281(10281, "Backend failed to create organization"),
    ERR_10282(10282, "Please provide required mandatory information for creating the organization"),
    ERR_10283(10283, "Backend failed to fetch business configuration"),
    ERR_10284(10284, "Backend failed to create organization"),
    ERR_10285(10285, "Please provide required mandatory information for creating the organization membership"),
    ERR_10286(10286, "Backend failed to create organization membership"),
    ERR_10287(10287, "Backend failed to create organization membership while creating organization"),
    ERR_10288(10288, "Backend failed to create organization features while creating organization"),
    ERR_10289(10289, "Backend failed to create organization actions while creating organization"),
    ERR_10290(10290, ErrorConstants.INVALID_DETAILS),
    ERR_10291(10291, "Please provide valid organization type"),
    ERR_10292(10292, "Given accounts list contains already used accounts by other organisation"),
    ERR_10293(10293, "Backend failed to fetch accounts details"),
    ERR_10294(10294, "Backend failed to create accounts details"),
    ERR_10295(10295, "Backend failed to update accounts details"),
    ERR_10296(10295, "Backend failed to create organization accounts while creating organization"),
    ERR_10297(10297, "Backend failed to update organization accounts while creating organization"),
    ERR_10298(10298, "Backend failed to fetch monetary actions while creating organization"),

    ERR_10299(10299, "Backend failed to create address"),
    ERR_10300(10300, "Backend failed to create address while creating organization"),
    ERR_10301(10301, "Backend failed to create organization addess while creating organization"),
    ERR_10302(10302, "Backend failed to create organization addess while creating organization"),
    ERR_10303(10303, "Backend failed to create organization communication while creating organization"),
    ERR_10304(10304, "Backend failed to create organization communication while creating organization"),

    ERR_10305(10305, "Backend failed to fetch organization accounts"),
    ERR_10306(10306, ErrorConstants.INVALID_DETAILS),
    ERR_10307(10307, "Backend failed to fetch the account holder details"),
    ERR_10308(10308, "Unauthorized access"),

    ERR_10309(10309, ErrorConstants.INVALID_DETAILS),
    ERR_10310(10310, "Backend failed to fetch organization details"),
    ERR_10311(10311, "Backend failed to fetch business signatory details"),
    ERR_10312(10312, "Backend failed to fetch signatory type details"),
    ERR_10313(10313, "Unauthorized access"),

    ERR_10314(10314, "Backend failed to create authorized signatory while creating organization"),
    ERR_10315(10315, "Backend failed to create customer communication"),
    ERR_10316(10316, "Backend failed to create customer communication while creating organization"),
    ERR_10317(10317, "Backend failed to create organization employee"),
    ERR_10318(10318, "Backend failed to create organization employee while creating organization"),
    ERR_10319(10319, "Backend failed to create customer accounts"),
    ERR_10320(10320, "Backend failed to create customer accounts while creating organization"),

    ERR_10321(10321, "Backend failed to create group"),

    ERR_10322(10322, "Backend failed to fetch organisationemployeeview details"),
    ERR_10323(10323, "Backend failed to create the credential checker entry"),

    ERR_10324(10324, "Please authenticate yourself before enrolling"),
    ERR_10325(10325, "Servicekey is expired"),

    ERR_10326(10326, "Please authenticate yourself before enrolling"),
    ERR_10327(10327, "Servicekey is expired"),

    ERR_10328(10328, "Invalid service key"),
    ERR_10329(10329, "Backend failed to create customer business type"),
    ERR_10330(10330, "Backend failed to create customer business type while creating organization"),
    ERR_10331(10331, "Backend failed to delete service key"),
    ERR_10332(10332, "Not allowed to update the status"),

    ERR_10333(10333, "Invalid input payload"),
    ERR_10334(10334, "Parameter _searchType cannot be empty"),
    ERR_10335(10335, "Failed to search for a customer"),
    ERR_10336(10336, "Error occured while encrypting the payload"),

    ERR_10337(10337, "An Organization with same name is existing in DBX, please choose another name"),

    ERR_10338(10338, ErrorConstants.INVALID_DETAILS),
    ERR_10339(10339, "Backend failed to get identity attributes"),

    ERR_10340(10340, ErrorConstants.INVALID_DETAILS),
    ERR_10341(10341, "Backend failed to get customer actions"),

    ERR_10342(10342, "Backend failed to generate captcha"),
    ERR_10343(10343, ErrorConstants.INVALID_DETAILS),
    ERR_10344(10344, "Invalid servicekey in Request"),
    ERR_10345(10345, "Invalid captcha"),

    ERR_10346(10346, "Backend failed to push event"),

    ERR_10347(10347, "Backend failed to verify captcha"),

    ERR_10348(10348, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10349(10349, "Invalid service definition"),
    ERR_10350(10350, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10351(10351, "Backend failed to create contract"),
    ERR_10352(10352, "Backend failed to create contract addess while creating contract"),
    ERR_10353(10353, "Backend failed to create contract addess while creating contract"),
    ERR_10354(10354, "Backend failed to create address while creating contract"),
    ERR_10355(10355, "Backend failed to create contract communication while creating contract"),
    ERR_10356(10356, "Backend failed to create contract customers while creating contract"),
    ERR_10357(10357, "Backend failed to create contract accounts while creating contract"),
    ERR_10358(10358, "Backend failed to create contract features while creating contract"),
    ERR_10359(10359, "Backend failed to create contract actionlimits while creating contract"),

    ERR_10360(10360, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10361(10361, "No records found for given contractId"),
    ERR_10362(10362, "Backend failed to get customer communication"),
    ERR_10363(10363, "Backend failed to get contract details"),

    ERR_10364(10364, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10365(10365, "Backend failed to update contract"),
    ERR_10366(10366, "Backend failed to get contract core customer details"),
    ERR_10367(10367, "Backend failed to fetch valid corecustomers list"),

    ERR_10368(10368, "Please provide valid customers"),
    ERR_10369(10369, "Please select a primary customer"),

    ERR_10370(10370, "Backend failed to delete contract customers"),
    ERR_10371(10371, "Backend failed to delete contract customer accounts"),
    ERR_10372(10372, "Backend failed to delete contract customer features"),
    ERR_10373(10373, "Backend failed to delete contract customer actions"),

    ERR_10374(10374, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10375(10375, "Backend failed to get contract users"),

    ERR_10376(10376, "Backend failed to get service definition details"),
    ERR_10377(10377, "Please provide valid servicedefinition ID"),
    ERR_10378(10378, "Could not find any contract in backend with the given id"),

    ERR_10379(10379, "Backend failed to get the list"),
    ERR_10380(10380, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),

    ERR_10381(10381, "Backend failed to update contract status"),
    ERR_10382(10382, ErrorConstants.INVALID_DETAILS),
    ERR_10383(10383, "Not allowed to update the status"),
    ERR_10384(10384, "Please provide a valid customerId"),

    ERR_10385(10385, ErrorConstants.INVALID_DETAILS),
    ERR_10386(10386, "Backend failed to create authorized signatory while enrolling a contract"),
    ERR_10387(10387, "Backend failed to create customer accounts"),
    ERR_10388(10388, "Backend failed to create customer actions"),
    ERR_10389(10389, ErrorConstants.INVALID_DETAILS),
    ERR_10390(10390, "Backend failed to enroll contract"),

    ERR_10391(10391, "Backend failed to fetch security attributes"),

    ERR_10392(10392, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10393(10393, "Backend failed to update contract"),
    ERR_10394(10394, "Backend failed to fetch actions with approvefeatureaction"),

    ERR_10395(10395, "Customer is in inactive state"),
    ERR_10396(10396, ErrorConstants.MANDATORY_INPUTFIELDS_ARE_EMPTY),
    ERR_10397(10397, "Backend failed to create backendidentifier table entry"),

    ERR_10398(10398, "Backend failed to get contractcorecustomers"),
    ERR_10399(10399, "Backend failed to update contractcorecustomers"),
    ERR_10400(10400, "Backend failed to update contract as there is no primary corecustomer before"),

    ERR_10401(10401, "A contract is already existing with given name, please choose another"),
    ERR_10402(10402, "Backend failed to create limitgroup limits"),
    ERR_10403(10343, "Backend failed to get contract actions"),

    ERR_10404(10404, "No details found for the given userId"),
    ERR_10405(10405, ErrorConstants.FAILED_TO_FETCH_USER_DETAILS),
    ERR_10406(10406, "Backend failed to update status"),

    ERR_10407(10407, "Backend failed to decrease limits at user"),

    ERR_10408(10408, "One or more customers does not contains any selected accounts"),
    ERR_10409(10409, "One or more accounts in the payload does not contains account specific details"),
    ERR_10410(10410, "Given payload is invalid"),
    ERR_10411(10411, "Backend failed while verifying the accounts"),
    ERR_10412(10412, "One or more accounts in the given payload are already associated with another contract"),

    ERR_10413(10413, "Backend failed to create a record in excluded contract accounts"),
    ERR_10414(10414, "Backend failed to get records from excluded contract accounts"),

    ERR_10415(10415, "Backend failed to update customer details"),
    
    ERR_10416(10416, "Max Limit exceeded."),
    ERR_10417(10417, "Limit is less than Min allowed value."),
    ERR_10418(10418, "Missing purchase limit value."),
    ERR_10419(10419, "Missing withdrawal limit value."),

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
    ERR_10525(10525, ErrorConstants.UNABLE_TO_DETERMINE_C360_CONFIGURATION),
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

    ERR_10630(10630, "Please provide valid Details."),

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
    ERR_10714(10714, "No group action limits"),
    ERR_10709(10709, "Failed to fetch the organization action limits"),
    ERR_10715(10715, "No organization action limits"),
    ERR_10716(10716, "No action limits"),

    ERR_10710(10710, "Invalid input parameters"),
    ERR_10711(10711, "Failed to fetch the customer approver list"),
    ERR_10712(10712, "No approvers present"),

    ERR_10713(10713, "Failed to fetch the customer action limits"),

    ERR_10717(10717, "Customer Information update failed"),

    ERR_10718(10718, "App Id cannot be empty"),
    ERR_10719(10719, "Failed while parsing environment configuration AC_APPID_TO_APP_MAPPING"),
    ERR_10720(10720, "Invalid app id found in the request"),
    ERR_10721(10721, "ActionId is a mandatory input"),
    ERR_10722(10722, "Invalid FeatureActionId"),
    ERR_10723(10723, "MFA is not applicable for this action"),
    ERR_10724(10724, "Failed to fetch feature"),
    ERR_10725(10725, "Feature is not active"),
    ERR_10726(10726, "Primary MFA type and secondary MFA type is invalid"),
    ERR_10727(10727, "Failed to fetch MFA configurations"),
    ERR_10728(10728, "Primary MFA type and secondary MFA type is invalid"),
    ERR_10729(10729, "Failed to fetch MFA Configuration and scenario"),
    ERR_10730(10730, "MFA Scenario is empty"),
    ERR_10731(10731, "Authorized Signatories in organization are limited"),

    ERR_10732(10732, "Failed to fetch group permssions"),
    ERR_10733(10733, "Failed to fetch the business user permissions"),
    ERR_10734(10734, "Failed to fetch the customer group"),
    ERR_10735(10735, "Failed to fetch the member group"),

    ERR_10736(10736, "Failed to fetch account holder information from core"),
    ERR_10737(10737, "Failed to fetch account holder information from core"),

    ERR_10738(10738, "Failed to enroll customer"),
    ERR_10739(10739, ErrorConstants.INVALID_DETAILS),
    ERR_10740(10740, "Failed to fetch communication details"),
    ERR_10741(10741, "Failed to validate activation code"),
    ERR_10742(10742, ErrorConstants.INVALID_DETAILS),
    ERR_10743(10743, ErrorConstants.FAILED_TO_GENRATE_SERVICE_KEY),
    ERR_10744(10744, "Unable to set the password"),
    ERR_10745(10745, "Failed to send the activation code"),
    ERR_10746(10746, "Failed to send the user name"),
    ERR_10747(10747, "Exceeded the maximum number of attempts"),
    ERR_10748(10748, "Customer is already enrolled"),
    ERR_10749(10749, "Activation code is expired."),
    ERR_10750(10750, ErrorConstants.INVALID_DETAILS),
    ERR_10751(10751, "Unable to get address types from server"),
    ERR_10752(10752, ErrorConstants.INVALID_DETAILS),
    ERR_10753(10753, ErrorConstants.INVALID_SERVICE_KEY),
    ERR_10754(10754, "Falied to validate service key"),

    ERR_10755(10755, ErrorConstants.INVALID_DETAILS),
    ERR_10756(10756, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),
    ERR_10757(10757, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),

    ERR_10758(10758, ErrorConstants.INVALID_DETAILS),
    ERR_10759(10759, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),
    ERR_10760(10760, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),

    ERR_10761(10761, ErrorConstants.INVALID_DETAILS),
    ERR_10762(10762, "Failed to fetch core customer accounts"),
    ERR_10785(10785, "Failed to fetch core customer accounts"),

    ERR_10763(10763, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),

    ERR_10764(10764, "Failed to search contracts"),
    ERR_10786(10786, "Failed to search contracts"),

    ERR_10765(10765, "Failed to fetch core contract customers"),
    ERR_10766(10766, "Failed to fetch the restrictive feature action limits"),
    ERR_10767(10767, "Failed to fetch the restrictive feature action limits"),
    ERR_10768(10768, ErrorConstants.INVALID_DETAILS),
    ERR_10769(10769, ErrorConstants.INVALID_DETAILS),
    ERR_10770(10770, "Failed to fetch the relative customers"),
    ERR_10771(10770, "Failed to fetch customers associated to accountId"),
    ERR_10772(10772, ErrorConstants.INVALID_DETAILS),
    ERR_10773(10773, ErrorConstants.FAILED_TO_FETCH_CUSTOMER_DETAILS),
    ERR_10774(10774, ErrorConstants.INVALID_DETAILS),
    ERR_10775(10775, "Failed to fetch contract features and action limits"),
    ERR_10776(10776, "Failed to fetch the Infinity user contract and contract customer details"),
    ERR_10777(10777, "Failed to fetch core customer group action limits"),
    ERR_10778(10778, "Failed to fetch core customer group action limits"),
    ERR_10779(10779, ErrorConstants.INVALID_DETAILS),
    ERR_10780(10780, ErrorConstants.INVALID_DETAILS),
    ERR_10781(10781, "Failed to fetch the core customer contract details"),
    ERR_10782(10782, "Failed to fetch the relative core customer contract details"),
    ERR_10783(10783, ErrorConstants.INVALID_DETAILS),
    ERR_10784(10784, "Failed to fetch the contract accounts"),

    ERR_10787(10787, "Failed to fetch the access policies"),

    ERR_10788(10788, "Failed to fetch user contract core customer actions"),

    ERR_10789(10789, "Failed to fetch the infinity user contract details"),
    ERR_10790(10790, "Failed to fetch the infinity user contract details"),
    ERR_10791(10791, ErrorConstants.INVALID_DETAILS),

    ERR_10792(10792, ErrorConstants.INVALID_DETAILS),
    ERR_10793(10793, "Failed to fetch the valid customer accounts"),
    ERR_10794(10794, "Failed to fetch the infinity user accounts"),

    ERR_10795(10795, "Failed to generate username and activation code"),
    ERR_10796(10796, "Failed to generate username and send email"),
    ERR_10797(10797, "Failed to generate activation code"),
    ERR_10798(10798, "Failed to validate details"),

    ERR_10799(10799, ErrorConstants.INVALID_DETAILS),
    ERR_10800(10800, "Failed to fetch user approval permissions"),

    ERR_10801(10801, ErrorConstants.INVALID_DETAILS),
    ERR_10802(10802, "User is already Enrolled"),
    ERR_10803(10803, "Please enroll retail user"),
    ERR_10804(10804, "Multiple customers found"),
    ERR_10805(10805, "Communication details are not found"),
    ERR_10806(10806, "Failed to validate enrollment details"),
    ERR_10812(10812, "Failed to enroll user"),

    ERR_10807(10807, "Backend failed to generate captcha"),
    ERR_10808(10808, ErrorConstants.INVALID_DETAILS),
    ERR_10809(10809, "Invalid servicekey in Request"),
    ERR_10810(10810, "Invalid captcha"),
    ERR_10811(10811, "Backend failed to verify captcha"),

    ERR_11001(11001, ErrorConstants.SECURITY_EXCEPTION),
    ERR_11002(11002, ErrorConstants.FAILED_TO_FETCH_ORGANIZATION_DETAILS),
    ERR_11003(11003, "Organization Update failed."), ERR_11004(11004, "Organization_id is empty"),
    ERR_11005(11005, "please provide valid list of accounts"),
    ERR_11006(11006, ErrorConstants.FAILED_TO_FETCH_ORGANIZATIONACCOUNTS_DETAILS),
    ERR_11007(11007, ErrorConstants.SECURITY_EXCEPTION),
    ERR_11008(11008, "A company is already enrolled in this name. Please check & try again"),
    ERR_11009(11009, "Company Enrollment is Failed"),
    ERR_11010(11010, "A company is already enrolled with MembershipId and Taxid. Please check & try again"),
    ERR_11012(11012, ErrorConstants.FAILED_TO_FETCH_ORGANIZATIONEMPLOYES_DETAILS),
    ERR_11013(11013, ErrorConstants.USER_NOT_EXISTS_IN_DBX), ERR_11014(11014, ErrorConstants.INVALID_DETAILS),
    ERR_11015(11015, ErrorConstants.FAILED_TO_FETCH_TIN_DETAILS), ERR_11017(11017, "MemeberShip Creation Failed"),
    ERR_11018(11018, "Applicant Creation Failed"), ERR_11019(11019, "Membership creation failed at DBP"),
    ERR_11020(11020, ErrorConstants.SECURITY_EXCEPTION), ERR_11021(11021, ErrorConstants.INVALID_DETAILS),
    ERR_11022(11022, ErrorConstants.FAILED_TO_FETCH_ORGANIZATIONACCOUNTS_DETAILS),
    ERR_11023(11023, ErrorConstants.ACCOUNTS_NOT_EXISTS_IN_DBX), ERR_11024(11024, ErrorConstants.INVALID_DETAILS),
    ERR_11025(11025, ErrorConstants.FAILED_TO_FETCH_ACCOUNTS_DETAILS),
    ERR_11026(11026, ErrorConstants.RECORD_NOT_FOUND_IN_DBX),
    ERR_11027(11027, ErrorConstants.FAILED_TO_FETCH_ORGANIZATION_DETAILS),
    ERR_11028(11028, ErrorConstants.RECORD_NOT_FOUND_IN_DBX),
    ERR_11029(11029, ErrorConstants.BACKGROUNDVERIFICATION_FAILED),
    ERR_11030(11030, ErrorConstants.GIVEN_ACCOUNT_DOES_NOT_BELONG_TO_CUSTOMER),

    ERR_12000(12000, ErrorConstants.INTERNAL_SERVICE_ERROR), ERR_12001(12001, ErrorConstants.USER_UNAUTHORIZED),
    ERR_12002(12002, ErrorConstants.INVALID_ACCOUNT_NUMBER), ERR_12003(12003, ErrorConstants.NO_MATCHING_RECORDS_FOUND),
    ERR_12004(12004, ErrorConstants.RECORD_DOESNT_BELONG_TO_THE_SAME_COMPANY),
    ERR_12005(12005, ErrorConstants.NO_PERMISSION_TO_ADD_DOMESTIC_RECIPIENTS),
    ERR_12006(12006, ErrorConstants.NO_PERMISSION_TO_ADD_INTERNATIONAL_RECIPIENTS),
    ERR_12007(12007, ErrorConstants.USER_UNAUTHORIZED),
    ERR_12021(12021, ErrorConstants.MISSING_EFFECTIVE_DATE), ERR_12022(12022, ErrorConstants.MISSING_DEBIT_ACCOUNT),
    ERR_12025(12025, ErrorConstants.MISSING_TEMPLATE_NAME),
    ERR_12026(12026, ErrorConstants.MISSING_TRANSACTION_TYPE_ID),
    ERR_12027(12027, ErrorConstants.MISSING_TEMPLATE_REQUEST_ID), ERR_12028(12028, ErrorConstants.MISSING_RECORDS),
    ERR_12029(12029, ErrorConstants.MISSING_MAX_AMOUNT), ERR_12030(12030, ErrorConstants.MISSING_TRANSACTION_DATE),
    ERR_12031(12031, ErrorConstants.MISSING_AMOUNT), ERR_12032(12032, ErrorConstants.MISSING_PAYEE),
    ERR_12033(12033, ErrorConstants.MISSING_DEBIT_CREDET_ACCOUNT),
    ERR_12034(12034, ErrorConstants.MISSING_BBGENERAL_TRANSACTION_TYPE_ID),
    ERR_12035(12035, ErrorConstants.MISSING_REQUEST_ID), ERR_12036(12036, ErrorConstants.MISSING_ACTION),
    ERR_12037(12037, ErrorConstants.MISSING_TEMPLATE_ID), ERR_12038(12038, ErrorConstants.MISSING_TEMPLATE_RECORD_ID),
    ERR_12039(12039, ErrorConstants.MISSING_TRANSACTIONENTRY),
    ERR_12040(12040, ErrorConstants.MISSING_FEATURE_ACTION_ID), ERR_12041(12041, ErrorConstants.MISSING_ACH_FILE_ID),
    ERR_12042(12042, ErrorConstants.MISSING_TRANSACTION_ID),
    ERR_12043(12043, ErrorConstants.MISSING_TRANSACTION_RECORD_ID),
    ERR_12044(12044, ErrorConstants.ACH_TRANSACTION_RECORD_FETCH_FAILED),
    ERR_12045(12045, ErrorConstants.ACH_TRANSACTION_SUBRECORD_FETCH_FAILED),
    ERR_12046(12046, ErrorConstants.UNBALANCED_ACH_FILE),
    ERR_12047(12047, ErrorConstants.ACH_FILE_PARSE_ERROR),
    ERR_12048(12048, ErrorConstants.FETCH_ACHFILERECORD_FAILED),
    ERR_12049(12049, ErrorConstants.FETCH_ACHFILESUBRECORD_FAILED),
    ERR_12050(12050, ErrorConstants.ACH_FILE_VALIDATION_ERROR),
    ERR_12051(12051, ErrorConstants.OFFSET_AMOUNT_IS_NOT_BALANCED),
    ERR_12052(12052, ErrorConstants.MISSING_CUSTOMER_ID),

    ERR_12053(12053, ErrorConstants.PAYEE_CREATION_FAILED),
    ERR_12054(12054, ErrorConstants.MISSING_PAYEE_ID),
    ERR_12055(12055, ErrorConstants.PAYEE_EDIT_FAILED),
    ERR_12056(12056, ErrorConstants.PAYEE_DELETE_FAILED),
    ERR_12057(12057, ErrorConstants.PAYEE_FETCH_FAILED),
    ERR_12058(12058, ErrorConstants.PAYEE_BACKEND_FETCH_FAILED),
    ERR_12059(12059, ErrorConstants.OFFSET_ACCOUNT_UNAUTHORIZED),
    ERR_12060(12060, ErrorConstants.EMPTY_CIF),
    ERR_12061(12061, ErrorConstants.ALL_THE_ACCOUNTS_DOESNOT_BELONG_TO_SAME_CIF),
    ERR_12062(12062, ErrorConstants.DUPLICATE_PAYEE),
    ERR_12063(12063, ErrorConstants.INVALID_BENEFICIARY_ACCOUNT),
    ERR_12064(12064, ErrorConstants.INVALID_IBAN_OR_SWIFT),

    ERR_12101(12101, ErrorConstants.ACH_TEMPLATE_CREATION_FAILED),
    ERR_12102(12102, ErrorConstants.ACH_TEMPLATE_RECORD_CREATION_FAILED),
    ERR_12103(12103, ErrorConstants.ACH_TEMPLATE_SUB_RECORD_CREATION_FAILED),
    ERR_12104(12104, ErrorConstants.ACH_TRANSACTION_CREATION_FAILED),
    ERR_12105(12105, ErrorConstants.ACH_TRANSACTION_RECORD_CREATION_FAILED),
    ERR_12106(12106, ErrorConstants.ACH_TRANSACTION_SUB_RECORD_CREATION_FAILED),
    ERR_12107(12107, ErrorConstants.APPROVE_FAILED), ERR_12108(12108, ErrorConstants.APPROVE_FAILED),
    ERR_12109(12109, ErrorConstants.APPROVE_FAILED),
    ERR_12110(12110, ErrorConstants.ACH_USER_UNAUTHORIZED),
    ERR_12111(12111, ErrorConstants.ACH_INVALID_FILENAME),
    ERR_12112(12112, ErrorConstants.ACH_NO_DEBIT_CREDIT_ERROR),
    ERR_12113(12113, ErrorConstants.SELF_APPROVAL_NOT_ALLOWED), // Added as part of ADP-2810
    ERR_12114(12114, ErrorConstants.FAILED_TO_FETCH_APPLICATION_RECORD), // Added as part of ADP-2810
    ERR_12115(12115, ErrorConstants.TRANSACTION_EXECUTION_FAILED),
    ERR_12116(12116, ErrorConstants.CHEQUE_MANAGEMENT_APPROVE_FAILED),
    ERR_12117(12117, ErrorConstants.CHEQUE_MANAGEMENT_WITHDRAW_FAILED), // Added as part of ADP-2810
    ERR_12118(12118, ErrorConstants.NOT_ASSOCIATED_TO_SIGNATORY_GROUP),

    ERR_12301(12301, ErrorConstants.TOTAL_AMOUNT_CANNOT_BE_ZERO),
    ERR_12302(12302, ErrorConstants.TOTAL_AMOUNT_GREATER_THAN_MAX_AMOUNT),
    ERR_12303(12303, ErrorConstants.FILE_TOO_LARGE),
    ERR_12304(12304, ErrorConstants.FILE_FORMATS_NOT_AVAILABLE),
    ERR_12305(12305, ErrorConstants.UNSUPPORTED_FILE_TYPE),
    ERR_12306(12306, ErrorConstants.TRANSACTION_TYPE_NOT_FOUND),
    ERR_12307(12307, ErrorConstants.FROMACCOUNTNUMBER_SAME_AS_TOACCOUNTNUMBER),
    ERR_12308(12308, ErrorConstants.INVALID_FILE),
    ERR_12309(12309, ErrorConstants.INVALID_EFFECTIVE_DATE),
    ERR_12310(12310, ErrorConstants.INVALID_REQUEST_TYPE),

    ERR_12401(12401, ErrorConstants.SECURITY_ERROR),
    ERR_12402(12402, ErrorConstants.INVALID_REQUEST_PASS_USERNAME),
    ERR_12403(12403, ErrorConstants.UNAUTHORIZED_ACCESS),
    ERR_12404(12404, ErrorConstants.USER_NOT_EXISTS_IN_DBX),
    ERR_12405(12405, ErrorConstants.USER_CREATION_FAILED_IN_CORE),
    ERR_12406(12406, ErrorConstants.USER_DOESNT_BELONG_TO_ORG), ERR_12407(12407, ErrorConstants.SECURITY_ERROR),
    ERR_12408(12408, ErrorConstants.UNAUTHORIZED_ACCESS),
    ERR_12409(12409, ErrorConstants.INVALID_REQUEST_PASS_USERNAME),
    ERR_12410(12410, ErrorConstants.ACCOUNTS_MISSING_IN_REQUEST),
    ERR_12411(12411, ErrorConstants.CUSTOMER_ACCOUNT_CREATE_FAILED),
    ERR_12412(12412, ErrorConstants.CUSTOMER_ACCOUNT_DELETE_FAILED),
    ERR_12413(12413, ErrorConstants.CUSTOMER_ROLE_NOT_AVAILABLE),
    ERR_12414(12414, ErrorConstants.TRANSACTION_LIMITS_NOT_CREATED),
    ERR_12415(12415, ErrorConstants.TRANSACTION_LIMITS_NOT_UPDATED),
    ERR_12416(12416, ErrorConstants.INVALID_REQUEST_PASS_USERNAME),
    ERR_12417(12417, "Please provide a valid username."), ERR_12418(12418, ErrorConstants.SECURITY_ERROR),
    ERR_12419(12419, ErrorConstants.UNAUTHORIZED_ACCESS), ERR_12420(12420, "User has no email address to communicate."),
    ERR_12421(12421, "Please Provide a valid email of the user."),
    ERR_12422(12422, "User has no primary communication email."), ERR_12423(12423, "Account is already activated."),
    ERR_12424(12424, "Error occured while creating an activation token."), ERR_12425(12425, "Failed to send Email."),
    ERR_12426(12426, "You need to be more than 18 years to apply."),
    ERR_12427(12427, "Could not activate your account. Please contact the bank for more information"),
    ERR_12428(12428, ErrorConstants.PROVIDE_VALID_PASSWORD),
    ERR_12429(12429, "Invalid Activation Link. Please contact the bank for more information"),
    ERR_12430(12430, "Incorrect User Name. Please re-try."), ERR_12431(12431, "There is no user with this Username"),
    ERR_12432(12432, "Your account is already activated"), ERR_12433(12433, "Your password has been set sucessfully"),
    ERR_12434(12434, "Owner Creation failed."), ERR_12435(12435, "No Accounts are mapped for organization."),
    ERR_12436(12436, "Error ocurred while creating accounts."), ERR_12437(12437, ErrorConstants.SECURITY_ERROR),
    ERR_12438(12438, "Edit operation not permitted for this Transaction type"),
    ERR_12450(12450, "Failed to fetch campaign specifications"),
    ERR_12451(12451, "'chnl' mandatory reporting parameter not available"),
    ERR_12452(12452, "'chnl' mandatory reporting parameter is invalid"),
    ERR_12455(12455, "'UserName' mandatory input parameter not available"),
    ERR_12456(12456, "'screenName' mandatory input parameter not available"),
    ERR_12457(12457, "'scale' mandatory input parameter not available"),
    ERR_12458(12458, "Customer ID is a mandatory field"), ERR_12459(12459, "Campaign ID is a mandatory field"),
    ERR_12460(12460, "Failed to ignore the campaign"),

    ERR_12462(12462, ErrorConstants.INVALID_ATTACHMENT_SIZE),
    ERR_12463(12463, ErrorConstants.INVALID_ATTACHMENT_NAME),
    ERR_12464(12464, ErrorConstants.INVALID_ATTACHMENT_TYPE),

    ERR_12501(12501, ErrorConstants.TRANSACTION_DENIED_DUE_TO_AUTODENIAL_MAX_LIMIT),
    ERR_12502(12502, ErrorConstants.TRANSACTION_DENIED_DUE_TO_AUTO_DENIAL_DAILY_LIMIT),
    ERR_12503(12503, ErrorConstants.TRANSACTION_DENIED_DUE_TO_AUTO_DENIAL_WEEKLY_LIMIT),
    ERR_12504(12504, ErrorConstants.TRANSACTION_DENIED_DUE_TO_MAX_LIMIT),
    ERR_12505(12505, ErrorConstants.TRANSACTION_DENIED_DUE_TO_DAILY_LIMIT),
    ERR_12506(12506, ErrorConstants.TRANSACTION_DENIED_DUE_TO_WEEKLY_LIMIT),
    ERR_12507(12507, ErrorConstants.TRANSACTION_DENIED_DUE_TO_INVALID_APPROVAL_MATRIX_LIMIT),
    ERR_12508(12508, ErrorConstants.FAILED_TO_FETCH_CONTRACT_CUSTOMER_LIMITS),
    ERR_12509(12509, ErrorConstants.ERROR_WHILE_FETCHING_USER_ROLE),
    ERR_12510(12510, ErrorConstants.ERROR_WHILE_FETCHING_ROLE_LIMITS),
    ERR_12511(12511, ErrorConstants.ERROR_WHILE_FETCHING_USER_LIMITS),
    ERR_12512(12512, ErrorConstants.TRANSACTION_DENIED_DUE_TO_MIN_LIMIT),

    ERR_12513(12513, ErrorConstants.FAILED_TO_CREATE_CUSTOM_VIEW),
    ERR_12514(12514, ErrorConstants.FAILED_TO_DELETE_CUSTOM_VIEW),
    ERR_12515(12515, ErrorConstants.FAILED_TO_GET_CUSTOM_VIEW),
    ERR_12516(12516, ErrorConstants.FAILED_TO_EDIT_CUSTOM_VIEW),
    ERR_12517(12517, ErrorConstants.BULK_PAYMENT_LIMIT_EXHAUSTED),
    ERR_12518(12517, ErrorConstants.ERROR_WHILE_FETCHING_LIMITGROUP_LIMITS),
    ERR_12519(12518, ErrorConstants.ERROR_WHILE_FETCHING_LIMITGROUPID),

    ERR_12520(12520, ErrorConstants.NO_APPROVALMATRIX_ENTRY_FOUND),
    ERR_12521(12521, ErrorConstants.APPROVALMATRIX_NOT_SET),
    ERR_12522(12522, ErrorConstants.INVALID_APPROVALMATRIX_FOR_MULTIPLE_UPTO_RANGES),
    ERR_12523(12523, ErrorConstants.INVALID_APPROVALMATRIX_FOR_MULTIPLE_ABOVE_RANGES),
    ERR_12524(12524, ErrorConstants.FAILED_TO_FETCH_APPROVER_IDS),
    ERR_12525(12525, ErrorConstants.FETCHED_APPROVER_IDS_NOT_VALID),
    ERR_12526(12526, ErrorConstants.NO_APPROVALMATRIX_ID_FOUND_FOR_REQUEST),
    ERR_12527(12527, ErrorConstants.NO_LIMITRANGE_FOR_FEATUREACTIONID),
    ERR_12528(12528, ErrorConstants.MIXED_APPROVALMATRIX_FOUND),

    ERR_12600(12600, ErrorConstants.BACKEND_INVOCATION_FAILED),
    ERR_12601(12601, ErrorConstants.TRANSACTION_CREATION_FAILED_AT_BACKEND),
    ERR_12602(12602, ErrorConstants.TRANSACTION_EDIT_FAILED_AT_BACKEND),
    ERR_12603(12603, ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND),
    ERR_12604(12604, ErrorConstants.TRANSACTION_CANCEL_OCCURRENCE_FAILED_AT_BACKEND),

    ERR_12605(12605, ErrorConstants.INVALID_DETAILS), ERR_12606(12606, ErrorConstants.RECORDS_NOT_EXISTS),

    ERR_12607(12607, ErrorConstants.INVALID_DETAILS), ERR_12608(12608, ErrorConstants.INVALID_DETAILS),
    ERR_12609(12609, "Given accounts list contains already used accounts by other organisations"),
    ERR_12610(12610, "Given accounts list contains invalid accounts records"),

    ERR_12611(12611, ErrorConstants.TRANSACTIONS_RDC_CREATE_ERROR),
    ERR_00000(15001, "Error occured at backend"), // This Error Message will be overwritten by Core's Error Message

    ERR_13000(13000, ErrorConstants.INVALID_REQUEST), ERR_13001(13001, ErrorConstants.INVALID_REQUEST),
    ERR_13002(13002, ErrorConstants.INVALID_REQUEST), ERR_13003(13003, ErrorConstants.SECURITY_EXCEPTION),
    ERR_13004(13004, ErrorConstants.SECURITY_EXCEPTION), ERR_13005(13005, ErrorConstants.SECURITY_EXCEPTION),
    ERR_13006(13006, ErrorConstants.SECURITY_EXCEPTION), ERR_13007(13007, ErrorConstants.INVALID_REQUEST),
    ERR_13500(13500, ErrorConstants.ACH_PREPROCESSOR_COMPANY_VALIDATION),
    ERR_13501(13501, ErrorConstants.ACH_PREPROCESSOR_ACCOUNT_VALIDATION),
    ERR_13502(13502, ErrorConstants.ACH_PREPROCESSOR_TEMPLATE_FETCH_FAILED),
    ERR_13503(13503, "Failed to fetch customer actions"), ERR_13504(13504, "Customer id is a mandatory parameter"),
    ERR_13505(13505, "Action id is a mandatory parameter"), ERR_13506(13506, "Failed to read customer actions proc"),
    ERR_13507(13507, "Failed to read action limits table"), ERR_13508(13508, "Failed to read customer information"),
    ERR_13509(13509, "Failed to read features from identity scope"),
    ERR_13510(13510, "Failed to read feature_details_view "),
    ERR_13511(13511, "Failed to read organisation actions table"),
    ERR_13512(13512, "Failed to read customer actions table"),
    ERR_13513(13513, "Failed to associate Business user Action Limits"),
    ERR_13514(13514, "group id is a missing or invalid"), ERR_13515(13515, "actions are mandatory input"),
    ERR_13516(13516, "accounts are mandatory input"), ERR_13517(13517, "Organisation id is a mandatory input"),
    ERR_13518(13518, "Failed to read group actions table"), ERR_13519(13519, "Failed to create customer action limit"),
    ERR_13520(13520, "Failed to get from member group table"), ERR_13521(13521, "Failed to update customer action"),
    ERR_13522(13522, "Failed to delete customer action"), ERR_13523(13523, "Features is a mandatory parameter"),
    ERR_13525(13525, "Something went wrong while generating report"),
    ERR_13526(13526, "Dont have permissions for any account to fetch limits"),
    ERR_13527(13527, "Failed to read group actions table"),
    ERR_13528(13528, "Failed to read customer group table"),
    ERR_13529(13529, "account id is a mandatory parameter"),

    ERR_14001(14001, ErrorConstants.MISSING_BULKWIRE_FILE_ID),
    ERR_14002(14002, ErrorConstants.INSUFFICIENT_PERMISSIONS_BULKWIRE_FILE_ID),
    ERR_14003(14003, ErrorConstants.MISSING_BULKWIREFILE_EXECUTION_ID),
    ERR_14004(14004, ErrorConstants.INCORRECT_FILE_FORMAT),
    ERR_14005(14005, ErrorConstants.INCORRECT_FILE_DATA),
    ERR_14006(14006, ErrorConstants.FILE_TRANSACTION_LIMIT_EXCEED),
    ERR_14007(14007, ErrorConstants.MISSING_BULKWIRE_FILE_EXECUTION_ID),
    ERR_14008(14008, ErrorConstants.DB_FETCH_ERROR),
    ERR_14009(14009, ErrorConstants.MISSING_BULKWIRE_TEMPLATE_ID),
    ERR_14010(14010, ErrorConstants.INSUFFICIENT_PERMISSIONS_BULKWIRE_TEMPLATE_ID),
    ERR_14011(14011, ErrorConstants.INAPPROPRIATE_REQUEST_PAYLOAD_BULKWIRETYPE),
    ERR_14012(14012, ErrorConstants.INAPPROPRIATE_REQUEST_PAYLOAD),
    ERR_14013(14013, ErrorConstants.INVALID_ACCOUNTNUMBER_BULKWIRETRANSFER),
    ERR_14014(14014, ErrorConstants.MISSING_BULKWIRE_TEMPLATE_EXECUTION_ID),
    ERR_14015(14014, ErrorConstants.USER_ACCOUNT_UNAUTHORIZED),

    ERR_14016(14016, "Error while uploading attachment"),
    ERR_14017(14017, ErrorConstants.MISSING_FILE_ID),
    ERR_14018(14018, ErrorConstants.ERROR_GENERATING_FILE_ID),
    ERR_14019(14019, ErrorConstants.EMPTY_BULKWIRE_FILE),

    ERR_20001(20001, "Internal Error"), ERR_20612(20612, "Username cannot be empty"),
    ERR_20613(20613, "Unable to find the user"),
    ERR_20688(20688, "Customer ID is a mandatory field"),
    ERR_20539(20539, "Customer not found"),
    ERR_20689(20689, "Failed to fetch customer basic information"),
    ERR_20881(20881, "Failed to read customer address"),
    ERR_20538(20538, "Failed to unlock the user"),
    ERR_20302(20302, "Failed to fetch card request summary"),
    ERR_20691(20691, "Unexpected error has occurred at customer requests"),
    ERR_20692(20692, "Parameter status_id cannot be empty"), ERR_20693(20693, "Parameter channel_id cannot be empty"),
    ERR_20694(20694, "Parameter DeviceName cannot be empty"), ERR_20695(20695, "Parameter LastUsedIp cannot be empty"),
    ERR_20696(20696, "Parameter OperatingSystem cannot be empty"),
    ERR_20697(20697, "Parameter LastLoginTime cannot be empty"),
    ERR_20698(20698, ErrorConstants.BILLPAY_UPDATE_CONFIRMATION_NUMBER_VALIDATION),
    ERR_20699(20699, ErrorConstants.BILLPAY_UPDATE_STATUS_VALIDATION),
    ERR_20700(20700, ErrorConstants.P2P_MISSING_PERSON_ID),
    ERR_20865(20865, "Unrecognised customer username"),

    ERR_21000(21000, ErrorConstants.FETCH_APPROVERS_FAILED),
    ERR_21001(21001, ErrorConstants.APPROVALMATRIX_UPDATE_FAILED), ERR_21002(21002, ErrorConstants.INVALID_LIMITS),
    ERR_21003(21003, ErrorConstants.INVALID_APPROVERS), ERR_21004(21004, "Error while fetching company ID"),
    ERR_21005(21005, "Error Invoking CustomerBusinessDelegate"),
    ERR_21006(21006, "Organization is a required parameter for C360 users"),
    ERR_21007(21007, "No approval matrix records found for these params"),
    ERR_21008(21008, "Aunthetication to fetch approval matrix records failed"),
    ERR_21009(21009, ErrorConstants.FAILED_TO_FETCH_CONTRACT_CUSTOMER_LIMITS),
    ERR_21010(21010, ErrorConstants.USER_ALREADY_APPROVED),
    ERR_21011(21011, ErrorConstants.CANNOT_WITHDRAW),
    ERR_21012(21012, ErrorConstants.CANNOT_REJECT),
    ERR_21013(21013, ErrorConstants.ACH_TEMPLATE_DELETION_FAILED),
    ERR_21014(21014, ErrorConstants.ACH_TEMPLATE_EDIT_FAILED),
    ERR_21016(21016, "Incorrect CVV code Entered"),
    ERR_21015(21015, "Card is Already in Active State"),
    ERR_21017(21017, "User doesnt have permission to access data related to the given coreCustomerId"),
    ERR_21018(21018, "User doesnt have permission to add one or more signatories mentioned"),
    ERR_21019(21019,
            "The signatories you are trying to add do not have permission to access data related to the given coreCustomerId"),
    ERR_21020(21020, "Another Signatory group exists with the same name"),
    ERR_21021(21021, "Signatory you are trying to add, is already added in another group of the user"),
    ERR_21022(21022, ErrorConstants.SIGNATORY_GROUP_NAME_MANDATORY),
    ERR_21023(21023, ErrorConstants.SIGNATORY_GROUP_NAME_SPECIAL_CHARS),
    ERR_21024(21024, ErrorConstants.CONTRACT_ID_MANDATORY),
    ERR_21025(21025, ErrorConstants.CORECUSTOMER_ID_MANDATORY),
    ERR_21026(21026, ErrorConstants.SIGNATORIES_MANDATORY),
    ERR_21027(21027, ErrorConstants.SIGNATORY_ID_MISSING),
    ERR_21028(21028, ErrorConstants.FAILED_TO_FETCH_SIGNATORY_GROUPS),
    ERR_21029(21029, ErrorConstants.FAILED_TO_FETCH_CORECUSTOMERS),
    ERR_21030(21030, ErrorConstants.DENIED_ACCESS_TO_CIF_AND_CONTRACT),
    ERR_21031(21031, ErrorConstants.NO_SIGNATORYGROUPS_FOUND),
    ERR_21032(21032, ErrorConstants.DENIED_ACCESS_TO_SIGNATORYGROUP),
    ERR_21033(21033, ErrorConstants.FAILED_TO_FETCH_CUSTOMERSIGNATORY),
    ERR_21034(21034, ErrorConstants.UPDATE_SIGNATORYGROUP_FAILED),
    ERR_21035(21035, ErrorConstants.APPROVALMATRIXTEMPLATE_UPDATE_FAILED),
    ERR_21036(21036, ErrorConstants.ERROR_FETCHING_ACTIONIDS),
    ERR_21037(21037, ErrorConstants.APPROVALMATRIXTEMPLATE_ASSIGN_FAILED),
    ERR_21038(21038, ErrorConstants.APPROVALMATRIX_FETCHUSINGCONTRACTID_FAILED),
    ERR_21100(21100, ErrorConstants.INVALID_RECIPIENT_CATEGORY),
    ERR_21101(21101, ErrorConstants.TEMPLATE_EMPTY_FIELDS),
    ERR_21102(21102, ErrorConstants.DUPLICATE_TEMPLATENAME),
    ERR_21104(21104, ErrorConstants.MISSING_CUSTOMROLE_NAME),
    ERR_21105(21105, ErrorConstants.MISSING_PARENTROLE_ID),
    ERR_21106(21106, ErrorConstants.MISSING_CUSTOMROLE_ID),
    ERR_21107(21107, ErrorConstants.INVALID_CUSTOM_ROLE_NAME_OR_DESCRIPTION),
    ERR_21108(21108, ErrorConstants.INVALID_TRANSACTION_LIMITS),
    ERR_21109(21109, ErrorConstants.UNIQUE_CUSTOM_ROLE_NAME),
    ERR_21110(21110, ErrorConstants.INVALID_ORG_ACCOUNTS),
    ERR_21111(21111, ErrorConstants.PAYEEID_MANDATORY),
    ERR_21112(21112, ErrorConstants.APPROVERS_MANDATORY),
    ERR_21113(21113, ErrorConstants.EMPTY_ACCOUNT_LIST),
    ERR_21114(21114, ErrorConstants.PARTY_FETCH_FAILED),
    ERR_21115(21115, ErrorConstants.USER_ALREADY_LINKED),
    ERR_21116(21116, ErrorConstants.USER_NOT_LINKED),
    ERR_21117(21117, ErrorConstants.CARD_FETCH_FAILED),
    ERR_21118(21118, ErrorConstants.CARD_EDIT_FAILED),
    ERR_21119(21119, ErrorConstants.USERDELINKING_BACKEND_FAILED),
    ERR_21120(21120, ErrorConstants.USERLINKING_BACKEND_FAILED),
    ERR_21121(21121, ErrorConstants.COMMUNICATION_DETAILS_FETCH_FAILED),
    ERR_21122(21122, ErrorConstants.COMMUNICATION_DETAILS_EDIT_FAILED),
    ERR_21123(21123, ErrorConstants.USER_DETAILS_MISMATCH),
    ERR_21124(21124, ErrorConstants.USER_NOT_SIGNATORY),
    ERR_21125(21125, ErrorConstants.COMBINED_ALERT_SERVICES_FAILED),
    ERR_21126(21126, ErrorConstants.SENDING_ALERT_FAILED),
    ERR_21127(21127, ErrorConstants.FAILED_TO_FETCH_ORGANISATIONEMPLOYEE),
    ERR_21128(21128, ErrorConstants.FAILED_TO_UPDATE_ORGANISATIONEMPLOYEE),
    ERR_21129(21129, ErrorConstants.FAILED_TO_FETCH_BACKENDIDENTIFIER),
    ERR_21130(21130, ErrorConstants.FAILED_TO_UPDATE_BACKENDIDENTIFIER),
    ERR_21131(21131, ErrorConstants.SENDING_ACTIVATION_LINK_FAILED),
    ERR_21132(21132, ErrorConstants.USER_UNAUTHORIZED_TO_VIEW_PAYEE),
    ERR_21133(21133,
            "Invalid Debit Account Id for the current user. Debit and Credit Account id's should be of the same user"),
    ERR_21134(21134,
            "Invalid Credit Account Id for the current user. Debit and Credit Account id's should be of the same user"),
    ERR_21135(21135, "Parameter contractId cannot be empty"),
    ERR_21136(21136, "Parameter cif cannot be empty"),

    ERR_21200(21200, ErrorConstants.BULK_PAYMENTS_FILE_PARSE_ERROR),
    ERR_21201(21201, ErrorConstants.BULK_PAYMENTS_FILE_FETCH_INPUT_ERROR),
    ERR_21202(21202, ErrorConstants.BULK_PAYMENTS_FILE_CREATE_ERROR),
    ERR_21204(21204, ErrorConstants.EMPTY_BULK_PAYMENTS_FILE),
    ERR_21206(21206, ErrorConstants.EMPTY_BULK_PAYMENTS_FILE_DESCRIPTION),
    ERR_21207(21207, ErrorConstants.FAILED_TO_STORE_BULK_PAYMENTS_FILE),
    ERR_21208(21208, ErrorConstants.BULK_PAYMENTS_FILE_DELETE_ERROR),

    ERR_21209(21209, ErrorConstants.BULK_PAYMENTS_PO_CREATE_ERROR),
    ERR_21210(21210, ErrorConstants.BULK_PAYMENTS_PO_FETCH_ERROR),
    ERR_21211(21211, ErrorConstants.BULK_PAYMENTS_PO_DELETE_ERROR),
    ERR_21212(21212, ErrorConstants.BULK_PAYMENTS_PO_EDIT_ERROR),

    ERR_21240(21240, ErrorConstants.BULK_PAYMENTS_FILE_UPDATE_ERROR),

    ERR_21213(21213, ErrorConstants.BULK_PAYMENTS_RECORD_CREATE_ERROR),
    ERR_21214(21214, ErrorConstants.BULK_PAYMENTS_RECORD_FETCH_ERROR),
    ERR_21215(21215, ErrorConstants.BULK_PAYMENTS_RECORD_DELETE_ERROR),
    ERR_21216(21216, ErrorConstants.BULK_PAYMENTS_RECORD_EDIT_ERROR),

    ERR_21217(21217, ErrorConstants.FILTER_RESPONSE_FAILED_ERROR),

    ERR_21218(21218, ErrorConstants.FETCH_SAMPLE_FILES_FAILED_ERROR),
    ERR_21219(21219, ErrorConstants.INVALID_BULKPAYMENTS_FILE_NAME),
    ERR_21220(21220, ErrorConstants.FETCH_UPLOADED_FILES_FAILED_ERROR),
    ERR_21222(21222, ErrorConstants.UNAUTHORISED_USER_BULK_PAYMENT_REVIEW),
    ERR_21223(21223, ErrorConstants.FETCH_ONGOING_PAYMENTS_ERROR),
    ERR_21224(21224, ErrorConstants.FAILED_TO_STORE_BULK_PAYMENTS_FILE_BACKEND),
    ERR_21225(21225, ErrorConstants.UPLOAD_FAILED_ERROR),
    ERR_21226(21226, ErrorConstants.UPLOAD_FAILED_ERROR_BACKEND),
    ERR_21227(21227, ErrorConstants.INVALID_BULKPAYMENTS_RECORDID),
    ERR_21228(21228, ErrorConstants.UNAUTHORISED_USER_BULK_PAYMENT_UPLOAD),
    ERR_21229(21229, ErrorConstants.BULKPAYMENTS_FAILED_ERROR_BACKEND),
    ERR_21235(21235, ErrorConstants.CANCEL_BULKPAYMENTS_RECORD_ERROR),
    ERR_21236(21236, ErrorConstants.UNAUTHORISED_USER_CANCEL_BULKPAYMENT_RECORD),
    ERR_21237(21237, ErrorConstants.UPDATE_BULKPAYMENTS_RECORD_ERROR),

    ERR_21230(21230, ErrorConstants.FAILED_TO_FETCH_BULK_PAYMENT_RECORD_DETAILS_BACKEND),
    ERR_21231(21231, ErrorConstants.BULK_PAYMENTS_REVIEW_ERROR),
    ERR_21232(21232, ErrorConstants.INVALID_TOTAL_AMOUNT),
    ERR_21233(21233, ErrorConstants.INVALID_BULKPAYMENTS_RECIPIENTNAME),
    ERR_21234(21234, ErrorConstants.INVALID_BULKPAYMENTS_AMOUNT),
    ERR_21238(21238, ErrorConstants.INVALID_BULKPAYMENTS_PAYMENTORDERID),
    ERR_21239(21239, ErrorConstants.INVALID_BULKPAYMENTS_CURRENCY),
    ERR_21242(21242, ErrorConstants.MANDATORY_BULKPAYMENTS_FROMACCOUNT_DESCRIPTION),
    ERR_21241(21241, ErrorConstants.INVALID_BULKPAYMENTS_DESCRIPTION),

    ERR_21246(21246, ErrorConstants.FAILED_TO_FETCH_BULKPAYMENTS_RECORDS),
    ERR_21247(21247, ErrorConstants.FAILED_TO_FETCH_RECORDS_REVIEWD_BY_ME_AND_IN_APPROVALQUEUE),
    ERR_21248(21248, ErrorConstants.FAILED_TO_FETCH_RECORDS_WAITING_FOR_MY_APPROVAL),
    ERR_21249(21249, ErrorConstants.FAILED_TO_FETCH_RECORD_HISTORY_REVIEWED_BY_ME),
    ERR_21250(21250, ErrorConstants.FAILED_TO_FETCH_RECORD_HISTORY_ACTED_BY_ME),
    ERR_21251(21251, ErrorConstants.MULTIPLE_PAYMENT_BLOCKS_ERROR),
    ERR_21252(21252, ErrorConstants.BULK_PAYMENTS_DOWNLOAD_FAILED),
    ERR_21253(21253, ErrorConstants.INITIATE_BULK_PAYMENTS_DOWNLOAD_FAILED),
    ERR_21254(21254, ErrorConstants.FILE_ID_GENERATION_FAILED),
    ERR_21255(21255, ErrorConstants.BULK_PAYMENTS_FILE_GENERATION_FAILED),
    ERR_21256(21256, ErrorConstants.EMPTY_BULK_PAYMENTS_BATCHMODE),
    ERR_21257(21257, ErrorConstants.INVALID_BULK_PAYMENTS_FILE_BATCHMODE),
    ERR_21258(21258, ErrorConstants.EMPTY_BULK_PAYMENTS_FILE_BATCHMODE),
    ERR_21259(21259, ErrorConstants.INVALID_SWIFT_BIC),
    ERR_21260(21260, ErrorConstants.FAILED_TO_FETCH_BANK_DETAILS),
    ERR_21261(21261, ErrorConstants.FAILED_TO_FETCH_BENEFICIARY_NAME),
    ERR_21262(21262, ErrorConstants.INVALID_ACCOUNT_ID),
    ERR_21263(21263, ErrorConstants.INVALID_IBAN),

    ERR_20040(20040, "Internal Service Error"),
    ERR_20041(20041, "Error from Microservice"),
    ERR_20042(20042, "No records found"),
    ERR_20043(20043, ErrorConstants.FUNDING_ACCOUNT_ACCESS_DENIED),

    ERR_20044(20044, ErrorConstants.SAVINGSPOT_ACCESS_DENIED),
    ERR_20045(20045, ErrorConstants.INSUFFICIENT_BALANCE_IN_FUNDING_ACCOUNT),
    ERR_20046(20046, ErrorConstants.INSUFFICIENT_BALANCE_IN_SAVINGSPOT),
    ERR_20047(20047, ErrorConstants.SAVINGSPOT_CLOSED),
    ERR_20048(20048, ErrorConstants.FUNDING_ACCOUNT_IS_BUSINESS_ACCOUNT),

    ERR_25001(25001, "DMS Backend Failed"),
    ERR_25003(25003, "DMS-No records were found that matched the selection criteria"),
    ERR_25004(25004, "DMS Login Failed"),
    ERR_25005(25005, "DMS Username and Password are empty"),

    ERR_26001(26001, ErrorConstants.FAILED_TO_FETCH_APPROVERS),
    ERR_26002(26002, ErrorConstants.INVALID_DATE),
    ERR_26003(26003, ErrorConstants.INVALID_BULKPAYMENTS_FILE_SIZE),

    // Setting Error code and constants related to OMS cheque management
    ERR_26004(26004, ErrorConstants.FAILED_TO_FETCH_CHEQUE_BOOKS),
    ERR_26005(26005, ErrorConstants.INVALID_PAYLOAD),
    ERR_26006(26006, ErrorConstants.NO_RECORDS_FROM_TRANSACT),
    ERR_26007(26007, ErrorConstants.ACCOUNT_NOT_FOUND),
    ERR_26008(26008, ErrorConstants.ACCOUNT_NUMBER_NOT_RELATED_TO_CUSTOMER),
    ERR_26009(26009, ErrorConstants.AUTHORIZATION_FAILED),
    ERR_26010(26010, ErrorConstants.FAILED_TO_CREATE_SERVICE_REQUEST),
    ERR_26011(26011, ErrorConstants.FAILED_TO_CREATE_CHEQUE_BOOK_REQUEST),
    ERR_26012(26012, ErrorConstants.FAILED_TO_CREATE_STOP_PAYMENT_REQUEST),
    ERR_26013(26013, ErrorConstants.FAILED_TO_FETCH_STOP_PAYMENTS),
    ERR_26014(26014, ErrorConstants.FAILED_TO_GET_CUSTOMER),
    ERR_26015(26015, ErrorConstants.FAILED_TO_VALIDATE_CHEQUE_BOOK_REQUEST),
    ERR_26016(26016, ErrorConstants.CHEQUE_VALIDATION_FAILED),
    ERR_26017(26017, ErrorConstants.FAILED_TO_VALIDATE_STOP_PAYMENT_REQUEST),
    ERR_26018(26018, ErrorConstants.STOP_PAYMENT_VALIDATION_FAILED),
    ERR_26019(26019, "Stop Payment Validation Failed, %s"),
    ERR_26020(26020, "Cheque Book Validation Failed, %s"),
    ERR_26021(26021, ErrorConstants.BACKEND_FAILED),
    ERR_26022(26022, ErrorConstants.FAILED_IN_POSTPROCESSOR),
    ERR_26023(26023, ErrorConstants.FAILED_TO_DELETE_REQUESTID),
    // ERR_26022(26024, "Error while validating for approval."),

    ERR_27001(27001, "Failed to fetch base currency"),
    ERR_27002(27002, "Error occured while fetching base currency"),
    ERR_27003(27002, "Error occured while fetching dashboard currency list"),
    ERR_27004(27004, "Base currency code and quote currency code required to fetch currency rates"),
    ERR_27005(27005, "Error occured while fetching currency rates"),
    ERR_27006(27006, "Base currency code required to fetch dashboard currency rates"),
    ERR_27007(27007, "Error occured while fetching dashboard currency rates"),
    ERR_27008(27008, "Quote currency code is required to update recent currencies of the user"),
    ERR_27009(27009, "Failed to fetch base currency from backend"),
    ERR_27010(27010, "Failed to fetch base currency rates from backend"),
    ERR_27011(27011, "Failed to fetch currency list from backend"),
    ERR_27012(27012, "Failed to fetch dashboard currency list"),
    ERR_27013(27013, "Base currency code required to fetch dashboard currency list"),
    ERR_27014(27014, "Failed to fetch currency rates"),
    ERR_27015(27015, "Failed to fetch dashboard currency rates"),
    ERR_27016(27016, ErrorConstants.FAILED_TO_FETCH_CONVERTED_AMOUNT),
    ERR_27017(27017, ErrorConstants.INVALID_AMOUNT_VALUE),

    ERR_28000(28000, ErrorConstants.ERROR_IN_INVOKING_CREATEBULKPAYEMENTTEMPLATE),
    ERR_28001(28001, ErrorConstants.INVALID_TEMPLATENAME),
    ERR_28002(28002, ErrorConstants.INVALID_CURRENCY),
    ERR_28003(28003, ErrorConstants.INVALID_FROMACCOUNT),
    ERR_28004(28004, ErrorConstants.INVALID_PROCESSINGMODE),
    ERR_28005(28005, ErrorConstants.UNAUTHORISED_USER_BULKPAYMENT_TEMPLATE_CREATE),
    ERR_28006(28006, ErrorConstants.UNABLE_TO_STORE_TEMPLATE_AT_BACKEND),
    ERR_28007(28007, ErrorConstants.ERROR_IN_INVOKING_FETCHBULKPAYEMENTTEMPLATE),
    ERR_28008(28008, ErrorConstants.UNAUTHORISED_USER_BULKPAYMENT_TEMPLATE_FETCH),
    ERR_28009(28009, ErrorConstants.BULK_PAYMENTS_TEMPLATES_FETCH_ERROR),
    ERR_28010(28010, ErrorConstants.DOWNLOAD_ERROR),
    ERR_28011(28011, ErrorConstants.BULKPAYMENT_TEMPLATE_EDIT_FAILED),
    ERR_28012(28012, ErrorConstants.UNAUTHORISED_USER_BULKPAYMENT_TEMPLATE_DELETE),
    ERR_28013(28013, ErrorConstants.BULKPAYMENT_TEMPLATE_DELETION_FAILED),
    ERR_28014(28014, ErrorConstants.BULKPAYMENT_EXPIRED_ERROR),
    ERR_28015(28015, ErrorConstants.PAYMENT_DATE_ERROR),
    ERR_28016(28016, ErrorConstants.INVALID_TEMPLATEID),
    ERR_28017(28017, ErrorConstants.ERROR_IN_CREATING_RECORDS),
    ERR_28018(28018, ErrorConstants.ERROR_IN_INVOKING_FETCHBULKPAYEMENTTEMPLATEBYID),
    ERR_28019(28019, ErrorConstants.ERROR_IN_INVOKING_FETCHTEMPLATEPOSBYID),
    ERR_28020(28020, ErrorConstants.UNAUTHORISED_USER_BULKREQUEST_CREATE),
    ERR_28021(28021, ErrorConstants.ERROR_IN_PARSING_INPUT_RECORDS),

    ERR_29000(29000, ErrorConstants.ERROR_IN_UPDATE_RECORDS),

    ERR_28022(28022, ErrorConstants.FAILED_TO_FETCH_DBP_HOST_URL),
    ERR_28023(28023, ErrorConstants.FAILED_BASE64_FETCH_MOCK),
    ERR_28024(28024, ErrorConstants.FAILED_DELETE_FROM_MOCK),
    ERR_28025(28025, ErrorConstants.NULL_FEATURE_ACTION_ERROR),

    ERR_28027(28027, ErrorConstants.FAILED_TO_GENERATE_FILE),
    ERR_28028(28028, ErrorConstants.FAILED_TO_UPDATE_ACCOUNT_STATEMENTS),
    ERR_28029(28029, ErrorConstants.FAILED_TO_CREATE_ACCOUNT_STATEMENTS),
    ERR_28030(28030, ErrorConstants.UNKNOWN_ERROR),
    ERR_28031(28031, ErrorConstants.FAILED_TO_FETCH_ACCOUNT_STATEMENTS),
    ERR_28032(28032, ErrorConstants.FAILED_TO_UPDATE_SERVICEDEFINTIION_LIMITSANDPERMISSIONS),
    ERR_28033(28033, ErrorConstants.FAILED_TO_UPDATE_CUSTOMERROLE_LIMITSANDPERMISSIONS),
    ERR_28034(28034, ErrorConstants.ERROR_IN_PO_STATUS),
    ERR_28026(28026, ErrorConstants.CIF_VALIDATION),

    ERR_29001(29001, ErrorConstants.MISSING_CONTRACTID),
    ERR_29002(29002, ErrorConstants.EMPTY_CIFLIST),
    ERR_29003(29003, ErrorConstants.MISSING_DISABLEFLAG),
    ERR_29004(29004, ErrorConstants.INVALID_DISABLEFLAG),
    ERR_29005(29005, ErrorConstants.ERROR_IN_APPROVALMATRIX_STATUS_UPDATE),
    ERR_29006(29006, ErrorConstants.NO_CONTRACT_CIF_ASSOCIATION),
    ERR_29007(29007, ErrorConstants.ERROR_FETCHING_STATUS),
    ERR_29008(29008, ErrorConstants.LANGUAGECODE_IS_EMPTY),
    ERR_29009(29009, ErrorConstants.ACC_PERMISSION_CHECK_EXCEP_MSG),
    ERR_29010(29010, ErrorConstants.INVALID_ACCNUM),
    ERR_29011(29011, ErrorConstants.INVALID_PERMISSION),
    ERR_29012(29012, ErrorConstants.FAILED_VALIDATION_PERMISSION),
    ERR_29013(29013, ErrorConstants.FAILED_LIMITS_VALIDATION),
    ERR_29014(29014, ErrorConstants.FAILED_UPDATING_BACKENDID),
    ERR_29015(29015, ErrorConstants.FAILED_FETCHING_TRANSACTION),
    ERR_29016(29016, ErrorConstants.FAILED_CREATING_DBXENTRY),
    ERR_29017(29017, ErrorConstants.BACKEND_PENDING_TRANSACTION_INVOCATION_FAILED),
    ERR_29018(29018, ErrorConstants.FAILED_VALIDATION_FOR_APPROVALS),
    ERR_29019(29019, ErrorConstants.ERROR_UPDATING_BACKENDID),
    ERR_29020(29020, ErrorConstants.ERROR_APPROVING_TRANSACTION_AT_BACKEND),
    ERR_29021(29021, ErrorConstants.VALID_SERVICE_NAME),
    ERR_29022(29022, "Backend failed to check core user exists"),
    ERR_29023(29023, ErrorConstants.INVALID_ORDERTYPE_ACCESS),
    ERR_29024(29024, ErrorConstants.ERROR_IN_COUNT_SERVICE),
    ERR_29025(29025, ErrorConstants.SALESFORCE_ERROR),
    ERR_29026(29026, "Maximum addition of data limit already reached"),
    ERR_29027(29027, ErrorConstants.TRANSACTION_ID_MISSING),
    ERR_29028(29028, ErrorConstants.FEATURE_ACTION_ID_MISSING),
    ERR_29029(29029, ErrorConstants.APPROVER_USERID_MISSING),
    ERR_29030(29030, ErrorConstants.FEATUREACTION_IS_INCORRECT),
    ERR_29031(29031, ErrorConstants.SIGNATORY_GROUP_NOT_ELIGIBLE),
    ERR_29032(29032, ErrorConstants.BLOCK_APPROVED_APPLICANT),
    ERR_29033(29033, ErrorConstants.APPROVALMATRIX_NOT_CONFIGURED),
    ERR_29034(29034, ErrorConstants.ERROR_WHILE_GENERATING_JWT_TOKEN);

    public static final String ERROR_CODE_KEY = DBPConstants.DBP_ERROR_CODE_KEY;
    public static final String ERROR_MESSAGE_KEY = DBPConstants.DBP_ERROR_MESSAGE_KEY;
    public static final String OPSTATUS_CODE = DBPConstants.FABRIC_OPSTATUS_KEY;
    public static final String HTTPSTATUS_CODE = DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY;
    public static final String ERROR_DETAILS = "errorDetails";
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

    public String getMessage(String... params) {
        return String.format(this.message, params);
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
        if(StringUtils.isBlank(errorMessage)) {
            errorMessage = this.message;
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
    
    public Result setErrorCodewithErrorDetails(Result result, String errorMessage, String errorDetails) {
        if (result == null) {
            result = new Result();
        }

        result.addParam(new Param(ERROR_CODE_KEY, this.getErrorCodeAsString(), MWConstants.INT));
        result.addParam(new Param(ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(ERROR_DETAILS, errorDetails, MWConstants.STRING));
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
    
    public Result updateResultObject(Result result) {
		if (result == null) {
			return constructResultObject();
		} else {
			return addAttributesToResultObject(result);
		}
	}

	public Result constructResultObject() {
		Result result = new Result();
		return addAttributesToResultObject(result);
	}

	private Result addAttributesToResultObject(Result result) {
		result.addParam(new Param(ErrorCodes.ERRCODE, this.getErrorCodeAsString()));
		result.addParam(new Param(ErrorCodes.ERRMSG, this.message));
		return result;
	}

}