CREATE DATABASE moodle;
CREATE USER 'moodle'@'%' IDENTIFIED BY 'moodle123';
CREATE USER 'moodle'@'localhost' IDENTIFIED BY 'moddle123';
-- GRANT PROCESS, REPLICATION CLIENT ON *.* TO 'exporter'@'dbmon';
GRANT ALL PRIVILEGES ON *.* TO 'moodle'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'moodle'@'localhost';
flush privileges;
-- GRANT SELECT ON performance_schema.* TO 'exporter'@'dbmon';
