DROP TABLE IF EXISTS `price`;

CREATE TABLE `price` (
  `id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `price` decimal(12, 2) NOT NULL,
  `cur_code` varchar(3) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;