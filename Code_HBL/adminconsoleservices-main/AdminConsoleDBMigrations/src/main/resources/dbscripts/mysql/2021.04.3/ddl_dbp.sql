DELIMITER $$
CREATE PROCEDURE `prospect_securityattributes_get_proc`(in _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

SET SESSION group_concat_max_len = 10000000;

SET @userAssociatedGroups =  (SELECT group_concat(distinct customergroup.Group_id SEPARATOR ",") FROM customergroup WHERE customergroup.Customer_id = _userId);

SET @actionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups));

                    
SET @activeFeaturesAtFI = (SELECT group_concat(distinct feature.id SEPARATOR ",") FROM feature WHERE Status_id = 'SID_FEATURE_ACTIVE'); 

SET @intersectedUserActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                featureaction.status = 'SID_ACTION_ACTIVE' AND
                                FIND_IN_SET(featureaction.id,@actionsAtGroups) AND
                                FIND_IN_SET(featureaction.Feature_id,@activeFeaturesAtFI));
                                
SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures = (SELECT group_concat(distinct featureaction.Feature_id SEPARATOR ",") FROM featureaction WHERE 
                                  FIND_IN_SET(featureaction.id,@intersectedUserActions));
                                  
SELECT @intersectedUserFeatures AS features;
                       
END$$
DELIMITER ;