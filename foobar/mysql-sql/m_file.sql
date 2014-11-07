CREATE TABLE `m_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `extension` varchar(20) DEFAULT NULL,
  `content_type` varchar(20) DEFAULT NULL,
  `order_no` int(11) DEFAULT '0',
  `upd_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flg` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
