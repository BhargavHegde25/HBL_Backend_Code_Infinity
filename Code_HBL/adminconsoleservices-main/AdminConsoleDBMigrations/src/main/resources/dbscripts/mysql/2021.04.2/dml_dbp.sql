
INSERT INTO `eventtype` (`id`, `Name`) VALUES ('DEVICE_MANAGEMENT', 'Device Management');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('SUSPEND_DEVICE_MFA', 'DEVICE_MANAGEMENT', 'Suspend Device Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('UNSUSPEND_DEVICE_MFA', 'DEVICE_MANAGEMENT', 'Unsuspend Device Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('REVOKE_DEVICE_MFA', 'DEVICE_MANAGEMENT', 'Revoke Device Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NEW_DEVICE_REGISTER_REQUEST_MFA', 'DEVICE_MANAGEMENT', 'New Device Regiser Request Mfa');
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Audit', 'pushAudit', 'DEVICE_MANAGEMENT');

INSERT INTO `eventtype` (`id`, `Name`) VALUES ('CARD_MANAGEMENT', 'Card Management');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('NEW_CARD_ACTIVATION_MFA', 'CARD_MANAGEMENT', 'New Card Activation Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('LOCK/UNLOCK_CARD_MFA', 'CARD_MANAGEMENT', 'Lock/Unlock Card Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('CHANGE_CARD_PIN_MFA', 'CARD_MANAGEMENT', 'Change Card Mfa'); 
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Audit', 'pushAudit', 'CARD_MANAGEMENT');

INSERT INTO `eventtype` (`id`, `Name`) VALUES ('CHECKBOOK_MANAGEMENT', 'Check Book Management');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('CREATE_CHECKBOOK_MFA', 'CHECKBOOK_MANAGEMENT', 'Create Check Book Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('APPROVE_CHECKBOOK_MFA', 'CHECKBOOK_MANAGEMENT', 'Approve Check Book Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('CANCEL_CHECKBOOK_MFA', 'CHECKBOOK_MANAGEMENT', 'Cancel Check Book Mfa'); 
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Audit', 'pushAudit', 'CHECKBOOK_MANAGEMENT');

INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('EMAIL_CHANGE_MFA', 'PROFILE_UPDATE', 'Email Change Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('PHONE_CHANGE_MFA', 'PROFILE_UPDATE', 'Phone Change  Mfa');

INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('PASSWORD_CREATE_MFA', 'CREDENTIAL_CHANGE', 'Password Create Mfa'); 
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`) VALUES ('PASSWORD_UPDATE_MFA', 'CREDENTIAL_CHANGE', 'Password Update Mfa'); 

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('3951e60a-fa51-4484-8aa1-88c8308f2296', 'RBObjects', 'DownloadTransactionPDF', 'get', 'ALLOW');