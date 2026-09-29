INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID524','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','RequestOverviewViewRiskAssessment','To view FCM results in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID525','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','EntityOverviewViewFCMResults','To view FCM results in Entity Overview','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_OPS', 'PID524', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_OPS_SUPERVISIOR', 'PID524', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM', 'PID524', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM_SUPERVISIOR', 'PID524', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_SYSTEM_ADMIN', 'PID524', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_UW', 'PID524', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_OPS', 'PID525', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_OPS_SUPERVISIOR', 'PID525', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM', 'PID525', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM_SUPERVISIOR', 'PID525', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_SYSTEM_ADMIN', 'PID525', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_UW', 'PID525', '0');

UPDATE application SET deploymentGeography = 'EUROPE' where id = '2'; 