1.文件夹下的  chromedriver.exe 可以放到机器上任意的位置
2.使用时将MyWebDriver 类中的CHROME_DRIVER_PATH  对应的驱动地址改为自己本机上到的驱动对应的地址  例如：D:\\chromedriver.exe
3. 建表语句：
CREATE TABLE `goods_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` varchar(255) DEFAULT NULL COMMENT '商品ID',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `img_url` varchar(255) DEFAULT NULL COMMENT '商品图片地址',
  `goods_price` varchar(255) DEFAULT NULL COMMENT '商品标价',
  `merchant_url` varchar(255) DEFAULT '' COMMENT '商家url 地址',
  `merchant_name` varchar(255) DEFAULT '' COMMENT '商家名称',
  `evaluations_num` varchar(255) DEFAULT '' COMMENT '评价数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44425 DEFAULT CHARSET=utf8 COMMENT='商品信息表';
