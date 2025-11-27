/*
 SpringBoot-Chaos 数据库初始化脚本
 基于 fh-matrix 表结构

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
-- 管理员表
-- ----------------------------
DROP TABLE IF EXISTS `p_admin`;
CREATE TABLE `p_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oid` varchar(64) NOT NULL COMMENT 'oid',
  `account_name` varchar(64) NOT NULL COMMENT '账户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `admin_name` varchar(64) DEFAULT NULL COMMENT '管理员姓名',
  `weixin_unionid` varchar(64) DEFAULT NULL COMMENT '微信unionid',
  `contact_number` varchar(16) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别：1-男，2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `picture` varchar(1024) DEFAULT NULL COMMENT '头像',
  `is_locked` tinyint(4) DEFAULT '0' COMMENT '是否锁定：0-否，1-是',
  `introduction` text COMMENT '介绍',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `organization_id` bigint(20) DEFAULT '0' COMMENT '所属组织ID',
  `is_activation` tinyint(4) DEFAULT '1' COMMENT '是否激活：0-否，1-是',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_account_name` (`account_name`) USING BTREE,
  UNIQUE KEY `uk_oid` (`oid`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE,
  KEY `idx_organization_id` (`organization_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ----------------------------
-- 初始化管理员账号 (密码: admin123, BCrypt加密)
-- ----------------------------
INSERT INTO `p_admin` (`id`, `oid`, `account_name`, `password`, `phone`, `admin_name`, `sex`, `is_locked`, `organization_id`, `is_activation`, `is_delete`, `create_by`, `create_time`) VALUES
(1, '3d1a367a3acb4e438e066ac47a0f9a06', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYUyPCTA8ywkE8YkqevHFAOXJRrm', '13800138000', '超级管理员', 1, 0, 1, 1, 0, 'system', NOW());

-- ----------------------------
-- 管理员角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `p_admin_role`;
CREATE TABLE `p_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `admin_oid` varchar(64) NOT NULL COMMENT '管理员oid',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `organization_id` bigint(20) DEFAULT '0' COMMENT '所属组织ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_admin_role` (`admin_oid`, `role_id`) USING BTREE,
  KEY `idx_admin_oid` (`admin_oid`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='管理员角色关联表';

-- ----------------------------
-- 初始化管理员角色关联
-- ----------------------------
INSERT INTO `p_admin_role` (`admin_oid`, `role_id`, `organization_id`, `create_by`, `create_time`) VALUES
('3d1a367a3acb4e438e066ac47a0f9a06', 1, 0, 'system', NOW());

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `p_role`;
CREATE TABLE `p_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(200) NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) DEFAULT NULL COMMENT '角色编码',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `is_locked` tinyint(1) DEFAULT '0' COMMENT '是否锁定：0-否，1-是',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- 初始化角色数据
-- ----------------------------
INSERT INTO `p_role` (`id`, `role_name`, `role_code`, `description`, `is_locked`, `sort`, `is_delete`, `create_by`, `create_time`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 0, 1, 0, 'system', NOW()),
(2, '普通管理员', 'ADMIN', '普通管理员角色', 0, 2, 0, 'system', NOW());

-- ----------------------------
-- 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `p_role_menu`;
CREATE TABLE `p_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联表id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS `p_menu`;
CREATE TABLE `p_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '菜单父id (一级菜单为0)',
  `menu_name` varchar(512) DEFAULT NULL COMMENT '菜单名称',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `state` tinyint(4) DEFAULT '1' COMMENT '菜单状态：1-公开，2-不公开',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单地址/路由',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `type` tinyint(4) DEFAULT '0' COMMENT '类型：0-目录，1-菜单，2-按钮',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- 初始化菜单数据
-- ----------------------------
INSERT INTO `p_menu` (`id`, `parent_id`, `menu_name`, `permission`, `state`, `url`, `component`, `type`, `sort`, `icon`, `is_delete`, `create_by`, `create_time`) VALUES
(1, 0, '系统管理', NULL, 1, '/system', NULL, 0, 1, 'setting', 0, 'system', NOW()),
(2, 1, '用户管理', 'system:user:list', 1, '/system/user', 'system/user/index', 1, 1, 'user', 0, 'system', NOW()),
(3, 1, '角色管理', 'system:role:list', 1, '/system/role', 'system/role/index', 1, 2, 'team', 0, 'system', NOW()),
(4, 1, '菜单管理', 'system:menu:list', 1, '/system/menu', 'system/menu/index', 1, 3, 'menu', 0, 'system', NOW()),
(5, 1, '组织管理', 'system:org:list', 1, '/system/org', 'system/org/index', 1, 4, 'apartment', 0, 'system', NOW()),
(6, 2, '用户新增', 'system:user:add', 1, NULL, NULL, 2, 1, NULL, 0, 'system', NOW()),
(7, 2, '用户修改', 'system:user:edit', 1, NULL, NULL, 2, 2, NULL, 0, 'system', NOW()),
(8, 2, '用户删除', 'system:user:delete', 1, NULL, NULL, 2, 3, NULL, 0, 'system', NOW());

-- ----------------------------
-- 初始化角色菜单关联（超级管理员拥有所有菜单）
-- ----------------------------
INSERT INTO `p_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`) VALUES
(1, 1, 'system', NOW()),
(1, 2, 'system', NOW()),
(1, 3, 'system', NOW()),
(1, 4, 'system', NOW()),
(1, 5, 'system', NOW()),
(1, 6, 'system', NOW()),
(1, 7, 'system', NOW()),
(1, 8, 'system', NOW());

-- ----------------------------
-- 组织机构表
-- ----------------------------
DROP TABLE IF EXISTS `p_organization`;
CREATE TABLE `p_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级ID',
  `superior_ids` varchar(1024) DEFAULT NULL COMMENT '上级组织机构ids（逗号分隔）',
  `name` varchar(64) DEFAULT NULL COMMENT '组织机构名称',
  `short_name` varchar(64) DEFAULT NULL COMMENT '组织机构简称',
  `org_code` varchar(64) DEFAULT NULL COMMENT '组织机构编码',
  `leader` varchar(64) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(16) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `state` tinyint(4) DEFAULT '1' COMMENT '状态：1-启用，2-禁用',
  `is_statistics` tinyint(4) DEFAULT '1' COMMENT '是否统计：1-统计，2-不统计',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `auth_start_time` datetime DEFAULT NULL COMMENT '生效开始时间',
  `auth_end_time` datetime DEFAULT NULL COMMENT '生效截止时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_org_code` (`org_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='组织机构表';

-- ----------------------------
-- 初始化组织机构
-- ----------------------------
INSERT INTO `p_organization` (`id`, `parent_id`, `superior_ids`, `name`, `short_name`, `org_code`, `state`, `is_statistics`, `sort`, `is_delete`, `create_by`, `create_time`, `auth_start_time`, `auth_end_time`) VALUES
(1, 0, ',0,', 'Chaos集团', 'Chaos', 'ORG001', 1, 1, 1, 0, 'system', NOW(), NOW(), '2099-12-31 23:59:59');

-- ----------------------------
-- 配置表
-- ----------------------------
DROP TABLE IF EXISTS `p_config`;
CREATE TABLE `p_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数配置id',
  `config_key` varchar(70) NOT NULL COMMENT '参数配置key',
  `config_value` text COMMENT '参数配置value',
  `config_description` varchar(1024) DEFAULT NULL COMMENT '参数配置说明',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_config_key` (`config_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='配置表';

-- ----------------------------
-- 初始化系统配置
-- ----------------------------
INSERT INTO `p_config` (`config_key`, `config_value`, `config_description`, `is_delete`, `create_by`, `create_time`) VALUES
('sys.name', 'SpringBoot-Chaos', '系统名称', 0, 'system', NOW()),
('sys.upload.path', '/data/upload', '文件上传存储路径', 0, 'system', NOW()),
('sys.upload.types', 'jpg,jpeg,png,gif,doc,docx,xls,xlsx,pdf,zip', '允许上传的文件类型', 0, 'system', NOW()),
('sys.upload.maxSize', '50', '文件上传最大大小（MB）', 0, 'system', NOW()),
('file.black.suffix', 'exe,bat,sh,php,jsp,asp,aspx,py,rb,dll,bin', '禁止上传的文件后缀', 0, 'system', NOW());

-- ----------------------------
-- 附件表
-- ----------------------------
DROP TABLE IF EXISTS `p_attachment`;
CREATE TABLE `p_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `oid` varchar(64) NOT NULL COMMENT '文件oid',
  `original_name` varchar(512) DEFAULT NULL COMMENT '原文件名',
  `new_name` varchar(512) DEFAULT NULL COMMENT '新文件名',
  `suffix` varchar(20) DEFAULT NULL COMMENT '后缀名',
  `size` bigint(20) DEFAULT '0' COMMENT '大小（字节）',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面',
  `origin_path` varchar(512) DEFAULT NULL COMMENT '原始文件路径',
  `view_path` varchar(512) DEFAULT NULL COMMENT '预览路径',
  `duration` bigint(20) DEFAULT NULL COMMENT '时长（毫秒）',
  `upload_state` tinyint(4) DEFAULT '2' COMMENT '上传状态：1-上传中，2-上传成功，3-上传失败',
  `state` tinyint(4) DEFAULT '3' COMMENT '状态：1-待处理，2-处理中，3-处理成功，4-处理失败',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `biz_type` varchar(64) DEFAULT NULL COMMENT '业务类型',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '业务ID',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_oid` (`oid`) USING BTREE,
  KEY `idx_biz` (`biz_type`, `biz_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='附件表';

-- ----------------------------
-- 用户表（C端用户）
-- ----------------------------
DROP TABLE IF EXISTS `p_user`;
CREATE TABLE `p_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oid` varchar(64) NOT NULL COMMENT 'oid',
  `account` varchar(64) DEFAULT NULL COMMENT '账号',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(512) DEFAULT NULL COMMENT '头像',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别：1-男，2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) DEFAULT NULL COMMENT '最后登录IP',
  `growth` int(11) DEFAULT '0' COMMENT '成长值',
  `score` int(11) DEFAULT '0' COMMENT '积分',
  `organization_id` bigint(20) DEFAULT '0' COMMENT '所属组织ID',
  `is_locked` tinyint(4) DEFAULT '0' COMMENT '是否锁定：0-否，1-是',
  `channel` tinyint(4) DEFAULT '1' COMMENT '渠道：1-web端，2-H5端，3-APP',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除：0-正常，1-删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_oid` (`oid`) USING BTREE,
  KEY `idx_account` (`account`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE,
  KEY `idx_organization_id` (`organization_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 初始化测试用户 (密码: 123456, BCrypt加密)
-- ----------------------------
INSERT INTO `p_user` (`id`, `oid`, `account`, `password`, `phone`, `real_name`, `nickname`, `sex`, `organization_id`, `is_locked`, `channel`, `is_delete`, `create_by`, `create_time`) VALUES
(1, 'u_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6', 'testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYUyPCTA8ywkE8YkqevHFAOXJRrm', '13900139000', '测试用户', '小测', 1, 1, 0, 1, 0, 'system', NOW());

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `p_user_role`;
CREATE TABLE `p_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_oid` varchar(64) NOT NULL COMMENT '用户oid',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_role` (`user_oid`, `role_id`) USING BTREE,
  KEY `idx_user_oid` (`user_oid`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `p_oper_log`;
CREATE TABLE `p_oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(64) DEFAULT NULL COMMENT '模块标题',
  `business_type` tinyint(4) DEFAULT '0' COMMENT '业务类型：0-其他，1-新增，2-修改，3-删除',
  `method` varchar(256) DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(16) DEFAULT NULL COMMENT '请求方式',
  `operator_type` tinyint(4) DEFAULT '0' COMMENT '操作类别：0-其他，1-后台用户，2-手机端用户',
  `oper_name` varchar(64) DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(512) DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(64) DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(256) DEFAULT NULL COMMENT '操作地点',
  `oper_param` text COMMENT '请求参数',
  `json_result` text COMMENT '返回参数',
  `status` tinyint(4) DEFAULT '1' COMMENT '操作状态：0-失败，1-成功',
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
DROP TABLE IF EXISTS `p_login_log`;
CREATE TABLE `p_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_oid` varchar(64) DEFAULT NULL COMMENT '用户oid',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `login_ip` varchar(64) DEFAULT NULL COMMENT '登录IP',
  `login_location` varchar(256) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(64) DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
  `status` tinyint(4) DEFAULT '1' COMMENT '登录状态：0-失败，1-成功',
  `msg` varchar(256) DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_oid` (`user_oid`) USING BTREE,
  KEY `idx_login_time` (`login_time`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

SET FOREIGN_KEY_CHECKS = 1;
