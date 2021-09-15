CREATE TABLE `user_info` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户自增id',
  `user_name` varchar(100) NOT NULL COMMENT '用户名称，登录用的',
  `password` varchar(512) NOT NULL COMMENT '密码',
  `email` varchar(100) NOT NULL COMMENT '邮箱，忘记密码使用',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '用户状态，1正常 0不可用',
  `create_time` datetime NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`user_id`)
) ;

CREATE TABLE `user_settings` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `watermark_logo_enable` bit(1) NOT NULL COMMENT '启用logo水印，默认否',
  `watermark_logo_repeat` bit(1) DEFAULT NULL COMMENT 'logo水印是否重复',
  `watermark_logo_gradient` tinyint DEFAULT NULL COMMENT 'logo水印倾斜度',
  `watermark_logo_alpha` tinyint DEFAULT NULL COMMENT '水印透明度（0，1）默认0.7',
  `watermark_text_enable` bit(1) NOT NULL COMMENT '启用文字水印，默认否',
  `watermark_text_content` varchar(255) DEFAULT NULL COMMENT '文字水印内容',
  `watermark_text_alpha` tinyint DEFAULT NULL COMMENT '水印透明度（0，1）默认0.7',
  `compress_scale` tinyint NOT NULL COMMENT '压缩比率，（0，1）默认0.8',
  `response_return_type` varchar(255) NOT NULL COMMENT '返回类型，默认md格式',
  PRIMARY KEY (`user_id`)
) ;