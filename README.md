CREATE DATABASE MovieDB;


CREATE TABLE Movie (
MovieID INT AUTO_INCREMENT PRIMARY KEY,
Title VARCHAR(255),
Director VARCHAR(255),
ReleaseDate DATE,
Rating FLOAT,
Description TEXT
);

docker exec -it java-mysql mysql -u root -p