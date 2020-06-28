-- SQL Script to build DB for simple PHP app
CREATE DATABASE userapp;

use userapp;

CREATE TABLE users (
	id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	firstname VARCHAR(30) NOT NULL,
	lastname VARCHAR(30) NOT NULL,
	email VARCHAR(50) NOT NULL,
	age INT(3),
	location VARCHAR(50),
	date TIMESTAMP
);

-- Populate table
INSERT INTO users VALUES(1,"Steve","Shilling","bob@builder.com",23,"UK",default);
INSERT INTO users VALUES(2,"Marcus","Chinwag","bob@builder.com",23,"UK",default);
INSERT INTO users VALUES(3,"Paul","Filmer","bob@builder.com",23,"UK",default);
