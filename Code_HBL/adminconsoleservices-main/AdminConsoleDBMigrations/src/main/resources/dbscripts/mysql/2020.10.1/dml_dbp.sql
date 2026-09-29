INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e796b046-1218-11eb-adc1-0242ac120002', 'BulkWireObjects', 'BulkWireFile', 'uploadBWTemplateFile', 'DOMESTIC_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES,INTERNATIONAL_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('8ea9f514-1219-11eb-adc1-0242ac120002', 'BulkWireObjects', 'BulkWireFile', 'uploadBWFile', 'DOMESTIC_WIRE_TRANSFER_UPLOAD_BULK_FILES,INTERNATIONAL_WIRE_TRANSFER_UPLOAD_BULK_FILES');

UPDATE  `configurations` SET `config_key` = 'DISPUTE_TRANSACTION_TYPE_CONFIG' WHERE (`configuration_id` = '92');
UPDATE  `configurations` SET `config_key` = 'DISPUTE_REASON_CONFIG' WHERE (`configuration_id` = '94');
UPDATE  `configurations` SET `config_key` = 'DISPUTE_TRANSACTION_CONFIG' WHERE (`configuration_id` = '95');