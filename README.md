აპლიკაციის გასაშვებად გამოიყენეთ GlassFish სერვერი

mysql database-ის url, user და password მოცემულია DBConnection კლასში, შესაბამისად მოგიწევთ დააფდეითება

დათაბეიზის შექმნის query:
CREATE DATABASE MovieDB;

თეიბლის შექმნის query:
CREATE TABLE Movie (
    MovieID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255),
    Director VARCHAR(255),
    ReleaseDate DATE,
    Rating FLOAT,
    Description TEXT
);
