ALTER TABLE tasktracker.task ENGINE = InnoDB;
ALTER TABLE tasktracker.task_list ENGINE = InnoDB;
ALTER TABLE tasktracker.user ENGINE = InnoDB;
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE tasktracker.task ADD CONSTRAINT task_list_id FOREIGN KEY (task_list_id) REFERENCES tasktracker.task_list(id) ON DELETE CASCADE;
ALTER TABLE tasktracker.task_list ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES tasktracker.user(id);
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE tasktracker.task_list ADD CONSTRAINT title_unique UNIQUE(title);
ALTER TABLE tasktracker.user ADD CONSTRAINT mail_unique UNIQUE(mail);