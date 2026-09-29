INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'KeyCloakObjService', 'KeycloakUsers', 'getKeycloakUsers', 'ALLOW,API_ACCESS');

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES (uuid() ,'BulkWireObjects','BulkWireFile','initiateDownloadBulkWireSampleFile','ALLOW');