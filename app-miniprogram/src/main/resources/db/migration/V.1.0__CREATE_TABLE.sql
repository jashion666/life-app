DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(500) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `t_express`;
CREATE TABLE `t_express` (
  `u_id` int(11) NOT NULL COMMENT '用户id',
  `code_no` varchar(50) NOT NULL COMMENT '快递单号',
  `type` varchar(50) DEFAULT NULL COMMENT '物流公司',
  `trajectory` varchar(2000) DEFAULT NULL COMMENT '物流轨迹',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `inser_id` int(11) NOT NULL COMMENT '插入者的id',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_id` int(11) NOT NULL COMMENT '更新的id',
  PRIMARY KEY (`code_no`,`u_id`),
  UNIQUE KEY `t_express_u_id_uindex` (`u_id`),
  UNIQUE KEY `t_express_code_no_uindex` (`code_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递记录';

