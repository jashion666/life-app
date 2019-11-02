DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar (200) NOT NULL COMMENT '微信用户唯一标识' ,
  `session_key` varchar (200) NOT NULL COMMENT '会话密钥' ,
  `username` varchar(500) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `insert_time` datetime NOT NULL COMMENT '插入时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
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

DROP TABLE IF EXISTS `role`;
create table if not exists role
(
  u_id int not null comment '用户关联id'
    primary key,
  role_id int not null comment '角色id',
  role_name varchar(20) not null comment '角色名',
  locked tinyint(1) null comment '用户是否被锁定'
)
comment '用户权限表';

DROP TABLE IF EXISTS `authority`;
create table if not exists authority
(
  role_id int not null
    primary key,
  menu_id int null,
  menu_name varchar(20) null
)
comment '权限表';




