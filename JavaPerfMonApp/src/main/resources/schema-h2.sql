CREATE TABLE IF NOT EXISTS endpoint (
  id int(11) NOT NULL AUTO_INCREMENT,
  site VARCHAR(45) DEFAULT NULL,
  enabled tinyint(1) NOT NULL DEFAULT '0',
  today_low_count int(11) NOT NULL DEFAULT '0',
  last_low_ping TIMESTAMP NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
);