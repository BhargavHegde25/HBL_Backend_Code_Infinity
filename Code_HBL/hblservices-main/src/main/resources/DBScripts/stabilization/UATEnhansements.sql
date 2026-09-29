use dbxdb;
CREATE TABLE `hblBranchList` (
  `id` varchar(50) NOT NULL,
  `mnemonic` varchar(50) NOT NULL,
  `branchname` varchar(50) NOT NULL,
  `emailto` varchar(100) NOT NULL,
  `emailcc` varchar(100) DEFAULT NULL,
  `contactno` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastsynctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3


INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('1', 'NP0010018', 'Thamel', 'Hitidurbar@Himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('2', 'NP0010005', 'Maharajgunj
', 'Maharajgunj@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('3', 'NP0010003', 'Newroad
', 'Newroad@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('4', 'NP0010006', 'Bhaktapur
', 'bhaktapur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('5', 'NP0010002', 'Patan
', 'Patan@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('6', 'NP0010011', 'Tandi', 'tandi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('7', 'NP0010012', 'Bharatpur', 'bharatpur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('8', 'NP0010014', 'Birgunj
', 'birgunj@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('9', 'NP0010013', 'Hetauda', 'hetauda@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('10', 'NP0010009', 'Bhairahawa
', 'bhairahawa@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('11', 'NP0010015', 'Biratnagar', 'biratnagar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('12', 'NP0010007', 'Banepa
', 'banepa@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('13', 'NP0010016', 'Dhara', 'dharan@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('14', 'NP0010008', 'Pokhara', 'pokhara@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('15', 'NP0010010', 'Butwal', 'butwal@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('16', 'NP0010017', 'Teku', 'teku@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('17', 'NP0010019', 'Nepalgunj', 'nepalgunj@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('18', 'NP0010020', 'Itahari', 'itahari@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('19', 'NP0010021', 'Palpa', 'palpa@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('20', 'NP0010022', 'Chabahil', 'chabahil@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('21', 'NP0010023', 'Ghorahi', 'ghorahi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('22', 'NP0010024', 'Swoyambhu', 'swoyambhu@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('23', 'NP0010025', 'Trishuli', 'trishuli@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('24', 'NP0010026', 'Koteshwor', 'koteshwor@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('25', 'NP0010027', 'Damak', 'damak@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('26', 'NP0010028', 'Baglung', 'baglung@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('27', 'NP0010029', 'Parsa', 'parsa@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('28', 'NP0010030', 'Sorhakhutte', 'sorhakhutte@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('29', 'NP0010031', 'Dillibazar', 'dillibazar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('30', 'NP0010032', 'Dhangadi', 'dhangadi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('31', 'NP0010033', 'Gorkha', 'gorkha@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('32', 'NP0010034', 'Kalanki', 'kalanki@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('33', 'NP0010035', 'Satdobato', 'satdobato@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('34', 'NP0010036', 'Barahbise', 'barahabise@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('35', 'NP0010037', 'Kawasoti', 'kawasoti@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('36', 'NP0010038', 'Battisputali', 'battisputali@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('37', 'NP0010040', 'Dhading', 'dhading@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('38', 'NP0010041', 'Amarsingh', 'Rambazar.pokhara@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('39', 'NP0010042', 'kausaltar', 'kaushaltar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('40', 'NP0010043', 'Betrawati', 'betrawati@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('41', 'NP0010044', 'Birtamode', 'birtamod@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('42', 'NP0010045', 'Samakhushi', 'samakhushi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('43', 'NP0010046', 'Bardibas', 'bardibas@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('44', 'NP0010047', 'Hile', 'hile@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('45', 'NP0010048', 'Lamahi', 'lamahi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('46', 'NP0010049', 'Lamki', 'lamki@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('47', 'NP0010050', 'Salya', 'salyan@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('48', 'NP0010051', 'Raptisonari', 'raptisonari@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('49', 'NP0010052', 'Prasauni', 'prasauni@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('50', 'NP0010053', 'Baragadi', 'baragadi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('51', 'NP0010054', 'Subarna', 'subarna@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('52', 'NP0010055', 'Gaurisankar', 'Gaurishankar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('53', 'NP0010056', 'Myago', 'myagon@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('54', 'NP0010057', 'Tarkeshwor', 'Tarkeswor@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('55', 'NP0010059', 'Madi', 'madi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('56', 'NP0010058', 'Kumakha', 'Kumakha@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('57', 'NP0010060', 'Chhededaha', 'chhededaha@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('58', 'NP0010061', 'Janakpur', 'janakpurdham@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('59', 'NP0010063', 'Kohalpur', 'kohalpur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('60', 'NP0010064', 'Laha', 'lahan@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('61', 'NP0010065', 'Khurkot', 'khurkot@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('62', 'NP0010066', 'Nijgadh', 'nijgadh@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('63', 'NP0010067', 'Surkhet', 'surkhet@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('64', 'NP0010068', 'Bhaisepati', 'bhainsepati@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('65', 'NP0010069', 'Tulsipur', 'tulsipur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('66', 'NP0010070', 'Swamikartik', 'swamikartik@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('67', 'NP0010071', 'Jagannath', 'jagannath@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('68', 'NP0010072', 'Attariya', 'attariya@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('69', 'NP0010073', 'Charikot', 'charikot@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('70', 'NP0010074', 'Jhamsikhel', 'jhamsikhel@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('71', 'NP0010075', 'Manigram', 'manigram@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('72', 'NP0010076', 'Tikapur', 'tikapur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('73', 'NP0010077', 'Mahendranagar', 'mahendranagar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('74', 'NP0010270', 'ADARSHA BRANCH', 'Adarsha@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('75', 'NP0010232', 'ANAMNAGAR BRANCH', 'Anamnagar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('76', 'NP0010275', 'ARJUNDHARA BRANCH', 'ArjunDhara@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('77', 'NP0010211', 'ARUGHAT BRANCH', 'Arughat@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('78', 'NP0010310', 'ATTARKHEL BRANCH', 'Attarkhel@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('79', 'NP0010222', 'Banepa 2 Branch', 'Banepa2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('80', 'NP0010309', 'Bangemudha Branch', 'Bangemuda@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('81', 'NP0010271', 'Bannigadhi Branch', 'Bannigadhi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('82', 'NP0010202', 'Barhabise 2 Branch', 'Barhabishe2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('83', 'NP0010260', 'Besisahar Branch', 'Besisahar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('84', 'NP0010297', 'BHADRAPUR BRANCH', 'Bhadrapur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('85', 'NP0010209', 'BHAIRAHAWA 2 BRANCH', 'Bhairahawa2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('86', 'NP0010265', 'Bhairavi Branch', 'Bhairavi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('87', 'NP0010285', 'BHAJANI BRANCH', 'Bhajani@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('88', 'NP0010250', 'BHIMSENSTHAN BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('89', 'NP0010257', 'BHULBHULE BRANCH', 'Bhulbhule@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('90', 'NP0010216', 'BIJUWAR BRANCH', 'Bijuwar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('91', 'NP0010227', 'Biratchowk Branch', 'Biratchowck@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('92', 'NP0010205', 'BIRATNAGAR 2 BRANCH', 'Biratnagar2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('93', 'NP0010281', 'BIRENDRANAGAR BRANCH', 'Birendranagar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('94', 'NP0010204', 'BIRGUNJ 2 BRANCH', 'Birgunj2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('95', 'NP0010217', 'Birtamod 2 Branch', 'Birtamode2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('96', 'NP0010220', 'Bouddha Branch', 'Boudha@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('97', 'NP0010218', 'Butwal 2 Branch', 'Butwal2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('98', 'NP0010246', 'CHABAHIL 2 BRANCH', 'Chabahil2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('99', 'NP0010301', 'CHAPUR BRANCH', 'Chapur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('100', 'NP0010288', 'CHHINCHU BRANCH', 'Chhinchu@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('101', 'NP0010259', 'DAMAK 2 BRANCH', 'Damak2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('102', 'NP0010302', 'DAMAULI BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('103', 'NP0010290', 'DAUBAHAL BRANCH', 'Daubahal@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('104', 'NP0010203', 'Dhading 2 Branch', 'Dhading2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('105', 'NP0010278', 'DHALKEBAR BRANCH', 'Dhalkebar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('106', 'NP0010239', 'Dhangadhi 2 Branch', 'Dhangadhimai@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('107', 'NP0010286', 'DHANGADHIMAI BRANCH', 'Dhangadi2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('108', 'NP0010269', 'DHANKAUL BRANCH', 'Dhankaul@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('109', 'NP0010314', 'DHARAN BRANCH', 'Dharan2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('110', 'NP0010274', 'DHARCHE BRANCH', 'Dharche@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('111', 'NP0010234', 'DHULIKHEL BRANCH', 'Dhulikhel@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('112', 'NP0010280', 'DUDHE BRANCH', 'Dudhe@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('113', 'NP0010296', 'DUHABI BRANCH', 'Duhabi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('114', 'NP0010206', 'E-BANKING CENTER', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('115', 'NP0010320', 'FIKKAL EXT COUNTER', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('116', 'NP0010319', 'GACHHIYA EXT COUNTER', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('117', 'NP0010225', 'GAIGHAT BRANCH', 'Gaighat@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('118', 'NP0010291', 'Ganj Bhawanipur Branch', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('119', 'NP0010282', 'GARUDA BRANCH', 'Garuda@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('120', 'NP0010258', 'GAUDA BRANCH', 'Gauda@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('121', 'NP0010247', 'GHORAHI 2 BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('122', 'NP0010277', 'GOLBAZAR BRANCH', 'GolBazar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('123', 'NP0010221', 'GONGABU BRANCH', 'Gongabu@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('124', 'NP0010299', 'GOTHATAR BRANCH', 'Gothatar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('125', 'NP0010316', 'HARNAMADI EXT COUNTER', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('126', 'NP0010228', 'HETAUDA 2 BRANCH', 'Hetauda2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('127', 'NP0010263', 'HILIHANG BRANCH', 'Hilihang@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('128', 'NP0010245', 'ITAHARI 2 BRANCH', 'Itahari2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('129', 'NP0010273', 'JAJARKOT BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('130', 'NP0010264', 'JANAKI BRANCH', 'Janaki@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('131', 'NP0010238', 'JANAKPUR 2 BRANCH', 'Janakpurdham2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('132', 'NP0010242', 'JAWALAKHEL BRANCH', 'Jawalakhel@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('133', 'NP0010307', 'Kalaiya Branch', 'Kalaiya@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('134', 'NP0010272', 'KAMALADI BRANCH', 'Kamaladi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('135', 'NP0010241', 'KAPAN BRANCH', 'Kapan@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('136', 'NP0010312', 'KAPILVASTU BRANCH', 'Kapilvastu@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('137', 'NP0010251', 'KAWASOTI 2 BRANCH', 'Kawashowti2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('138', 'NP0010230', 'KHURKHURE BRANCH', 'Khurkhure@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('139', 'NP0010213', 'KIRTIPUR BRANCH', 'Kirtipur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('140', 'NP0010295', 'KOHALPUR 2 BRANCH', 'Kohalpur2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('141', 'NP0010233', 'Kuleshwor Branch', 'Kuleswore@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('142', 'NP0010248', 'KUMARIPATI BRANCH', 'Kumaripati@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('143', 'NP0010279', 'LAHAN 2 BRANCH', 'Lahan2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('144', 'NP0010284', 'LALMATIYA BRANCH', 'Lalmatiya@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('145', 'NP0010313', 'LAMACHAUR BRANCH', 'Lamachaur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('146', 'NP0010293', 'LAMAHI 2 BRANCH', 'Lamahi2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('147', 'NP0010283', 'LAMKICHUHA BRANCH', 'Lamki2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('148', 'NP0010223', 'LEKHNATH BRANCH', 'Lekhnath@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('149', 'NP0010318', 'Mahendranagar 2 Branch', 'Mahendranagar2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('150', 'NP0010262', 'MAIJOGMAI BRANCH', 'Maijogmai@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('151', 'NP0010201', 'MAIN BRANCH CTC', 'CTCBranch@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('152', 'NP0010308', 'MALANGWA BRANCH', 'Malangwa@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('153', 'NP0010256', 'MALING BRANCH', 'Maling@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('154', 'NP0010266', 'Mandavi Branch', 'Mandavi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('155', 'NP0010249', 'MANIGRAM 2 BRANCH', 'Manigram2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('156', 'NP0010207', 'MELAMCHI BRANCH', 'Melamchi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('157', 'NP0010214', 'MID BANESHWOR BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('158', 'NP0010236', 'MUSIKOT BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('159', 'NP0010304', 'NAKHIPOT BRANCH', 'Nakhipot@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('160', 'NP0010224', 'Narayangadh 2 Branch', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('161', 'NP0010219', 'Nepalgunj 2 Branch', 'Nepalgunj2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('162', 'NP0010244', 'New Baneshwor 2 Branch', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('163', 'NP0010210', 'New Road 2 Branch', 'NewRoad2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('164', 'NP0010300', 'NIJGADH 2 BRANCH', 'Nijgadh2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('165', 'NP0010237', 'OKHALDHUNGA BRANCH', 'Okhaldhunga@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('166', 'NP0010267', 'Pakaha Mainpur Branch', 'Pakahmainpur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('167', 'NP0010231', 'PARBATIPUR BRANCH', 'Parbatipur@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('168', 'NP0010215', 'PHIDIM BRANCH', 'Phidim@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('169', 'NP0010208', 'POKHARA 2 BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('170', 'NP0010298', 'Pushpalalchowk Branch', 'PushpalalChowk@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('171', 'NP0010253', 'PUTALISADAK BRANCH', 'Putalisadak@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('172', 'NP0010292', 'RABI BRANCH', 'Rabi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('173', 'NP0010255', 'RAINAS BRANCH', 'Rainas@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('174', 'NP0010321', 'REGIONAL OFFICE 1', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('175', 'NP0010322', 'REGIONAL OFFICE 2', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('176', 'NP0010323', 'REGIONAL OFFICE 3', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('177', 'NP0010324', 'REGIONAL OFFICE 4', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('178', 'NP0010325', 'REGIONAL OFFICE 5', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('179', 'NP0010326', 'REGIONAL OFFICE 6', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('180', 'NP0010327', 'REGIONAL OFFICE 7', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('181', 'NP0010235', 'SALYAN 2 BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('182', 'NP0010315', 'SANKHU BRANCH', 'Shivachowk@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('183', 'NP0010303', 'Shiva Chowk Branch', 'Shivalaya@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('184', 'NP0010305', 'SIMARA BRANCH', 'Simara@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('185', 'NP0010317', 'SINAM EXT COUNTER', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('186', 'NP0010226', 'SINDHULI BRANCH', 'Sindhuli@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('187', 'NP0010289', 'SITALABAZAR BRANCH', 'Sitalabazar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('188', 'NP0010268', 'Sunawarshi Branch', 'Sunavarshi@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('189', 'NP0010212', 'Sundhara Branch', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('190', 'NP0010306', 'SUNWAL BRANCH', 'Sunwal@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('191', 'NP0010276', 'SURUNGA BRANCH', 'Surunga@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('192', 'NP0010252', 'SURYABINAYAK BRANCH', 'Suryabinayak@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('193', 'NP0010240', 'Swayambhu Branch', 'Swoyambhu2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('194', 'NP0010229', 'TANDI 2 BRANCH', 'Tandi2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('195', 'NP0010287', 'THAKURDWARA BRANCH', "", NULL, NULL, '0');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('196', 'NP0010311', 'TOKHA BRANCH', 'Tokha@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('197', 'NP0010254', 'TRIPURESHWOR BRANCH', 'Tripureswar@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('198', 'NP0010243', 'TULSIPUR 2 BRANCH', 'Tulsipur2@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('199', 'NP0010294', 'URLABARI BRANCH', 'Urlabari@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('200', 'NP0010261', 'YANGWARAK BRANCH', 'Yangbarak@himalayanbank.com', NULL, NULL, '1');
INSERT INTO `dbxdb`.`hblBranchList` (`id`, `mnemonic`, `branchname`, `emailto`, `emailcc`, `contactno`, `status`) VALUES ('201', 'NP0010062', 'Sukedhara Branch', 'sukedhara@himalayanbank.com', NULL, NULL, '1');


-----

create all CRUD operations for table hblBranchList and do the refresh metadata


-------------ALTER requestnewcard with new column 'branchname, branchcode, branchtoemail, branchccemail'-------------------------------------


ALTER table dbxdb.requestnewcard ADD COLUMN branchname varchar(50) NULL AFTER status;
ALTER table dbxdb.requestnewcard ADD COLUMN branchcode varchar(50) NULL AFTER branchname;
ALTER table dbxdb.requestnewcard ADD COLUMN branchtoemail varchar(50) NULL AFTER branchcode;
ALTER table dbxdb.requestnewcard ADD COLUMN branchccemail varchar(50) NULL AFTER branchtoemail;


--- update card request email templates with collected card branch-----------

UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Dear %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Thank you for reaching out to HBL. We have received your request for a new %cardType%. Please find the details below:</span> </strong> </p> </td> <ul> <li> <td> <b>Application Reference No::</b> %referenceNumber% </td> </li> <li> <td> <b>Account Number:</b> %debitAccount% </td> </li> <li> <td> <b>Requested Card Type:</b> %cardType% </td> </li> <li> <td> <b>Expected Name on the Card:</b> %nameOnTheCard% </td> </li> <li> <td> <b>Date of Request:</b> %requestdate% </td> </li> <li> <td> <b>Preferred branch for card collection:</b> %branchname% </td> </li> </ul> </br> <l>Our team is currently processing your request, and we will notify you once your card has been issued and shipped. Please allow %estimatedTime% for delivery.</l> </br> </br> <l>If you have any questions or need further assistance in the meantime, feel free to contact us at %customerCareNumber%</l>undefined</br>undefined</br>undefined<l>Best Regards,</l>undefined</br>undefined<l>Himalayan Bank Limited,</l>undefined</br>undefined </tr>undefined </tbody>' WHERE (`id` = '220');



UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Dear %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Thank you for reaching out to HBL. We have received your request for a new %cardType%. Please find the details below:</span> </strong> </p> </td> <ul> <li> <td> <b>Application Reference No::</b> %referenceNumber% </td> </li> <li> <td> <b>Account Number:</b> %debitAccount% </td> </li> <li> <td> <b>Requested Card Type:</b> %cardType% </td> </li> <li> <td> <b>Card Fee:</b> %cardFee% </td> </li> <li> <td> <b>Top up Amount:</b> %topupAmount% </td> </li> <li> <td> <b>Total Debit Amount:</b> %totalDebitAmount% </td> </li> <li> <td> <b>Date of Request:</b> %requestdate% </td> </li> <li> <td> <b>Preferred branch for card collection:</b> %branchname% </td> </li> </ul> </br> <l>Our team is currently processing your request, and we will notify you once your card has been issued and shipped. Please allow %estimatedTime% for delivery.</l> </br> </br> <l>If you have any questions or need further assistance in the meantime, feel free to contact us at %customerCareNumber%</l>undefined</br>undefined</br>undefined<l>Best Regards,</l>undefined</br>undefined<l>Himalayan Bank Limited,</l>undefined</br>undefined </tr>undefined </tbody>' WHERE (`id` = '222');


UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Dear %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Thank you for reaching out to HBL. We have received your request for a new %cardType%. Please find the details below:</span> </strong> </p> </td> <ul> <li> <td> <b>Application Reference No::</b> %referenceNumber% </td> </li> <li> <td> <b>Requested Card Type:</b> %cardType% </td> </li> <li> <td> <b>Requested Card Name:</b> %cardName% </td> </li> <li> <td> <b>Expected Name on the Card:</b> %nameOnTheCard% </td> </li> <li> <td> <b>Date of Request:</b> %requestdate% </td> </li> <li> <td> <b>Preferred branch for card collection:</b> %branchname% </td> </li> </ul> </br> <l>Our team is currently processing your request, and we will notify you once your card has been issued and shipped. Please allow %estimatedTime% for delivery.</l> </br> </br> <l>If you have any questions or need further assistance in the meantime, feel free to contact us at %customerCareNumber%</l>undefined</br>undefined</br>undefined<l>Best Regards,</l>undefined</br>undefined<l>Himalayan Bank Limited,</l>undefined</br>undefined </tr>undefined </tbody>' WHERE (`id` = '231');


UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear Card Processing Team,</l> </br> </br> <l>This is to notify you of a new %cardType%  request for the following customer:</l> </br> <ul> <li> <td><b>Customer Name:</b> %coreIdentifier%</td> </li> <li> <td><b>Application Reference No:</b> %referenceNumber%</td> </li> <li> <td><b>Account Number:</b> %debitAccount%</td> </li> <li> <td><b>Requested Card Type:</b> %cardType%</td> </li> <li> <td><b>Expected Name on the Card:</b> %nameOnTheCard%</td> </li> <li> <td><b>Date of Request:</b> %requestdate%</td> </li> <li> <b>Preferred branch for card collection:</b> %branchname% </li> </ul> </br> <l>Please proceed with processing the %cardType% request and ensure that all necessary steps are followed.</l> </br> </br> <l>Best Regards,</l> </br> <l>%cardTeamName%</l> </br> </tr> </tbody>' WHERE (`id` = '221');


UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear Card Processing Team,</l> </br> </br> <l>This is to notify you of a new %cardType%  request for the following customer:</l> </br> <ul> <li> <td><b>Customer Name:</b> %coreIdentifier%</td> </li> <li> <td><b>Application Reference No:</b> %referenceNumber%</td> </li> <li> <td><b>Account Number:</b> %debitAccount%</td> </li> <li> <td><b>Requested Card Type:</b> %cardType%</td> </li> <li> <td><b>Card Fee:</b> %cardFee%</td> </li> <li> <td><b>Top up Amount:</b> %topupAmount%</td> </li> <li> <td><b>Total Debit Amount:</b> %totalDebitAmount%</td> </li> <li> <td><b>Date of Request:</b> %requestdate%</td> </li> <li> <b>Preferred branch for card collection:</b> %branchname% </li> </ul> </br> <l>Please proceed with processing the %cardType% request and ensure that all necessary steps are followed.</l> </br> </br> <l>Best Regards,</l> </br> <l>%cardTeamName%</l> </br> </tr> </tbody>' WHERE (`id` = '223');


UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear Card Processing Team,</l> </br> </br> <l>This is to notify you of a new %cardType%  request for the following customer:</l> </br> <ul> <li> <td><b>Customer Name:</b> %coreIdentifier%</td> </li> <li> <td><b>Application Reference No:</b> %referenceNumber%</td> </li> <li> <td><b>Requested Card Type:</b> %cardType%</td> </li> <li> <td><b>Requested Card Name:</b> %cardName%</td> </li> <li> <td><b>Expected Name on the Card:</b> %nameOnTheCard%</td> </li> <li> <td><b>Date of Request:</b> %requestdate%</td> </li> <li> <b>Preferred branch for card collection:</b> %branchname% </li> </ul> </br> <l>Please proceed with processing the %cardType% request and ensure that all necessary steps are followed.</l> </br> </br> <l>Best Regards,</l> </br> <l>%cardTeamName%</l> </br> </tr> </tbody>' WHERE (`id` = '232');


***************** aleter cardConfigLimit with new column cardImage and refresh metadata*******

ALTER table dbxdb.cardConfigLimit ADD COLUMN cardImage varchar(100) NULL AFTER cardDescription;
