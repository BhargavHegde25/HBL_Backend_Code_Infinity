INSERT INTO `appmappingaid` (`id`,`Appid`, `Channel`, `aid`) VALUES ('7','INFINITYONBOARDING', 'desktop', 'InfinityOnboarding');
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Audit', 'pushAudit', 'ONBOARDING_APP');

INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`) VALUES ('ONBOARDING_APP', 'Onboarding Application', 'CUSTOMER');

INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_LANDING', 'ONBOARDING_APP', 'Landing module');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_UPDATE', 'ONBOARDING_APP', 'Update Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_CREATE', 'ONBOARDING_APP', 'Create Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_SUBMIT', 'ONBOARDING_APP', 'Submit Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_ISCOAPPICANT', 'ONBOARDING_APP', 'Is Co-Applicant');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('OTP_VERIFY', 'ONBOARDING_APP', 'OTP verification');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_RESUME', 'ONBOARDING_APP', 'Resume Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NOTIFY_APPROVED_APPLICATION', 'ONBOARDING_APP', 'Notify for Approved Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NOTIFY_UNDER_REVIEW_APPLICATION', 'ONBOARDING_APP', 'Notify for Under Review Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NOTIFY_DENIED_APPLICATION', 'ONBOARDING_APP', 'Notify for Denied Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NOTIFY_RESUME_APPLICATION', 'ONBOARDING_APP', 'Notify for Resume Application');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NOTIFICATION_EVENT', 'ONBOARDING_APP', 'Default Notification');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('MAX_OTP_VERIFY_ATTEMPT', 'ONBOARDING_APP', 'No.of OTP Verification attempts');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('OTP_RESEND_COUNT', 'ONBOARDING_APP', 'OTP resent count');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ADDRESSINFO_UPDATE', 'ONBOARDING_APP', 'Update Address Info');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('IDENTITYINFO_UPDATE', 'ONBOARDING_APP', 'Update IdentityInfo');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('PERSONALINFO_GET', 'ONBOARDING_APP', 'Get Personal Info');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ADDRESSINFO_GET', 'ONBOARDING_APP', 'Get Address Info');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('IDENTITYINFO_GET', 'ONBOARDING_APP', 'Get Identity Info');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('IDV_BACKGROUND_VERIFICATION', 'ONBOARDING_APP', 'Execute IDV');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('PERSONALINFO_UPDATE_PA_CONSENT', 'ONBOARDING_APP', 'Update Personal Info');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('IDV_VERIFY_RESPONSES', 'ONBOARDING_APP', 'Verify IDV Responses');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('EXISTINGCUSTOMER_PERSONALINFO', 'ONBOARDING_APP', 'Personal Info of Existing Customer');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('REQUEST_OTP', 'ONBOARDING_APP', 'OnRequest of OTP');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('ONBOARDING_NEWCUSTOMER_PA_CONSENT', 'ONBOARDING_APP', 'ONBoarding new customer creation');


