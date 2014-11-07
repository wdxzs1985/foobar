CREATE TABLE `m_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(20) NOT NULL,
  `signature` varchar(80) DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(32) NOT NULL,
  `fingerprint` varchar(32) DEFAULT NULL,
  `role` char(1) NOT NULL DEFAULT '0',
  `include_red_flg` char(1) NOT NULL DEFAULT '0',
  `upd_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flg` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
