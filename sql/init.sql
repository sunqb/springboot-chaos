/*
 SpringBoot-Chaos 数据库初始化脚本

 Database: chaos
 MySQL Version: 5.7+ / 8.0+

 Date: 2025-11-27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 创建数据库
-- ----------------------------
CREATE DATABASE IF NOT EXISTS `chaos` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `chaos`;

-- ----------------------------
-- 系统用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(512) DEFAULT NULL COMMENT '头像',
  `sex` tinyint(1) DEFAULT '0' COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- 初始化管理员账号 (密码: admin123)
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `sex`, `status`, `remark`, `create_by`, `create_time`) VALUES
(1, 'admin', '$2a$10$VQBMBs4Xv0QvPxGYqX3Y3.xYJWq8q7q8ZbQJm7QJ6q8ZbQJm7QJ6q', '超级管理员', '13800138000', 'admin@chaos.com', 1, 1, '系统内置管理员', 'system', NOW());

-- ----------------------------
-- 系统角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- 初始化角色数据
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 1, 1, 'system', NOW()),
(2, '普通用户', 'USER', '普通用户角色', 2, 1, 'system', NOW());

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 初始化用户角色关联
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`) VALUES
(1, 1, NOW());

-- ----------------------------
-- 系统菜单表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID（一级菜单为0）',
  `menu_name` varchar(64) NOT NULL COMMENT '菜单名称',
  `menu_type` tinyint(1) DEFAULT '1' COMMENT '菜单类型：0-目录，1-菜单，2-按钮',
  `permission` varchar(128) DEFAULT NULL COMMENT '权限标识',
  `path` varchar(256) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(256) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `visible` tinyint(1) DEFAULT '1' COMMENT '是否可见：0-隐藏，1-显示',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- ----------------------------
-- 初始化菜单数据
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `permission`, `path`, `component`, `icon`, `sort`, `visible`, `status`, `create_by`, `create_time`) VALUES
(1, 0, '系统管理', 0, NULL, '/system', NULL, 'setting', 1, 1, 1, 'system', NOW()),
(2, 1, '用户管理', 1, 'system:user:list', '/system/user', 'system/user/index', 'user', 1, 1, 1, 'system', NOW()),
(3, 1, '角色管理', 1, 'system:role:list', '/system/role', 'system/role/index', 'team', 2, 1, 1, 'system', NOW()),
(4, 1, '菜单管理', 1, 'system:menu:list', '/system/menu', 'system/menu/index', 'menu', 3, 1, 1, 'system', NOW()),
(5, 2, '用户新增', 2, 'system:user:add', NULL, NULL, NULL, 1, 1, 1, 'system', NOW()),
(6, 2, '用户修改', 2, 'system:user:edit', NULL, NULL, NULL, 2, 1, 1, 'system', NOW()),
(7, 2, '用户删除', 2, 'system:user:delete', NULL, NULL, NULL, 3, 1, 1, 'system', NOW());

-- ----------------------------
-- 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 初始化角色菜单关联（超级管理员拥有所有菜单）
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`) VALUES
(1, 1, NOW()),
(1, 2, NOW()),
(1, 3, NOW()),
(1, 4, NOW()),
(1, 5, NOW()),
(1, 6, NOW()),
(1, 7, NOW());

-- ----------------------------
-- 系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_name` varchar(128) NOT NULL COMMENT '配置名称',
  `config_key` varchar(128) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` tinyint(1) DEFAULT '0' COMMENT '配置类型：0-系统内置，1-自定义',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_config_key` (`config_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- 初始化系统配置
-- ----------------------------
INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_by`, `create_time`) VALUES
('系统名称', 'sys.name', 'SpringBoot-Chaos', 0, '系统名称配置', 'system', NOW()),
('文件上传路径', 'sys.upload.path', '/data/upload', 0, '文件上传存储路径', 'system', NOW()),
('文件上传类型', 'sys.upload.types', 'jpg,jpeg,png,gif,doc,docx,xls,xlsx,pdf,zip', 0, '允许上传的文件类型', 'system', NOW()),
('文件上传大小', 'sys.upload.maxSize', '50', 0, '文件上传最大大小（MB）', 'system', NOW());

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(64) DEFAULT NULL COMMENT '模块标题',
  `business_type` tinyint(1) DEFAULT '0' COMMENT '业务类型：0-其他，1-新增，2-修改，3-删除',
  `method` varchar(256) DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(16) DEFAULT NULL COMMENT '请求方式',
  `operator_type` tinyint(1) DEFAULT '0' COMMENT '操作类别：0-其他，1-后台用户，2-手机端用户',
  `oper_name` varchar(64) DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(512) DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(64) DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(256) DEFAULT NULL COMMENT '操作地点',
  `oper_param` text COMMENT '请求参数',
  `json_result` text COMMENT '返回参数',
  `status` tinyint(1) DEFAULT '1' COMMENT '操作状态：0-失败，1-成功',
  `error_msg` text COMMENT '错误信息',
  `oper_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `cost_time` bigint(20) DEFAULT '0' COMMENT '消耗时间（毫秒）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_oper_time` (`oper_time`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- 登录日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `login_ip` varchar(64) DEFAULT NULL COMMENT '登录IP',
  `login_location` varchar(256) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(64) DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
  `status` tinyint(1) DEFAULT '1' COMMENT '登录状态：0-失败，1-成功',
  `msg` varchar(256) DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_username` (`username`) USING BTREE,
  KEY `idx_login_time` (`login_time`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

SET FOREIGN_KEY_CHECKS = 1;
