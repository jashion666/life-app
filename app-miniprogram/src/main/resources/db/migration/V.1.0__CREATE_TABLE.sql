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
  `post_id` varchar(50) NOT NULL COMMENT '快递单号',
  `type` varchar(50) DEFAULT NULL COMMENT '物流公司',
  `trajectory` varchar(15000) DEFAULT NULL COMMENT '物流轨迹',
  `last_update_time` datetime DEFAULT NULL COMMENT '物流最后更新时间',
  `complete_flag` int(1) DEFAULT NULL COMMENT '订物流完成flag（0：未完成，1：完成）',
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `insert_id` int(11) NOT NULL COMMENT '插入者的id',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_id` int(11) NOT NULL COMMENT '更新的id',
  PRIMARY KEY (`post_id`,`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递记录';

