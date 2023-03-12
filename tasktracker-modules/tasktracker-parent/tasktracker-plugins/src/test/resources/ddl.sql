DROP TABLE IF EXISTS `task`;
DROP TABLE IF EXISTS `task_list`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `task` (
  `id` binary(16) NOT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `due_date` date DEFAULT NULL,
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reminder` date DEFAULT NULL,
  `done` tinyint NOT NULL DEFAULT '0',
  `task_list_id` binary(16) DEFAULT NULL,
  `task_id` binary(16) DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `completion_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  KEY `task_list_id` (`task_list_id`),
  CONSTRAINT `task_list_id` FOREIGN KEY (`task_list_id`) REFERENCES `task_list` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `task_list` (
  `id` binary(16) NOT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`,`title`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user` (
  `id` binary(16) NOT NULL,
  `mail` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;