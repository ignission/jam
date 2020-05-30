create table accounts (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  display_name VARCHAR(100),
  email VARCHAR(255) NOT NULL UNIQUE KEY,
  password CHAR(60) NOT NULL
);