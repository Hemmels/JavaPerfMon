CREATE TABLE IF NOT EXISTS endpoint (
  id int(11) NOT NULL AUTO_INCREMENT,
  site VARCHAR(45) DEFAULT NULL,
  enabled tinyint(1) NOT NULL DEFAULT '0',
  today_low_count int(11) NOT NULL DEFAULT '0',
  last_low_ping TIMESTAMP NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
);

# TODO these are untested
CREATE TABLE IF NOT EXISTS down_log (
  site varchar(45) NOT NULL,
  downstamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (site),
  UNIQUE KEY site_UNIQUE (site),
  CONSTRAINT fk_down_log_name FOREIGN KEY (site) REFERENCES endpoint (site) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE VIEW BAD_SITES AS SELECT 
    endpoint.id AS id,
    endpoint.site AS site,
    endpoint.enabled AS enabled,
    endpoint.today_low_count AS today_low_count,
    endpoint.last_low_ping AS last_low_ping
FROM
    endpoint
WHERE
    (endpoint.today_low_count > 5);