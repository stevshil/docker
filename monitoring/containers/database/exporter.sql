CREATE USER 'exporter'@'%' IDENTIFIED BY 'promexporter123';
CREATE USER 'exporter'@'localhost' IDENTIFIED BY 'promexporter123';
-- GRANT PROCESS, REPLICATION CLIENT ON *.* TO 'exporter'@'dbmon';
GRANT ALL PRIVILEGES ON *.* TO 'exporter'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'exporter'@'localhost';
flush privileges;
-- GRANT SELECT ON performance_schema.* TO 'exporter'@'dbmon';
