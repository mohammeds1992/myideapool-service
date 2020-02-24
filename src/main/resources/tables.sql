CREATE SCHEMA `myideapool` ;


CREATE TABLE myideapool.`user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` bigint DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_refresh_token
ON myideapool.`user` (refresh_token);

CREATE TABLE myideapool.`idea` (
  `user_id` int NOT NULL,
  `idea_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `impact` int NOT NULL,
  `ease` int NOT NULL,
  `confidence` int NOT NULL,
  `id` int NOT NULL,
  `created_at` bigint DEFAULT NULL,
  PRIMARY KEY (`idea_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;