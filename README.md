# Bright List - BE
```
create database brightdb;

CREATE USER 'brightyadmin'@'%' IDENTIFIED BY 'brightypass';
GRANT ALL PRIVILEGES ON brightdb.* TO 'brightyadmin'@'%';

CREATE USER 'brightyuser'@'%' IDENTIFIED BY 'userpass';
GRANT SELECT, INSERT, DELETE, UPDATE ON brightdb.* TO 'brightyuser'@'%';
```

ALTER DATABASE brightdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;