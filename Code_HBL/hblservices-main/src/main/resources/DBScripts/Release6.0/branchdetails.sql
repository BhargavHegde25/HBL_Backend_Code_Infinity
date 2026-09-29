CREATE TABLE `branchdetails` (
  `id` int NOT NULL,
  `bank_cd` varchar(45) DEFAULT NULL,
  `bank_name` varchar(45) DEFAULT NULL,
  `bank_sc` varchar(45) DEFAULT NULL,
  `branch_cd` varchar(45) DEFAULT NULL,
  `branch_name` varchar(45) DEFAULT NULL,
  `bank_swift` varchar(45) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT '1',
  `branch_mge_email` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci