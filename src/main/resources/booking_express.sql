use booking_express;

-- Users Table
CREATE TABLE Users (
    username VARCHAR(50) PRIMARY KEY,
    id INT AUTO_INCREMENT,
    password VARCHAR(255) NOT NULL,
    UNIQUE KEY (id)
);

-- Trains Table
CREATE TABLE Trains (
    trainNo VARCHAR(20) PRIMARY KEY,
    id INT AUTO_INCREMENT,
    bogeys INT NOT NULL,
    max_capacity INT NOT NULL,
    available_seats INT NOT NULL,
    route VARCHAR(100) NOT NULL,
    train_status VARCHAR(20) NOT NULL,
    UNIQUE KEY (id)
);

-- Tickets Table
CREATE TABLE Tickets (
    pnr VARCHAR(20) PRIMARY KEY,
    id INT AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    trainNo VARCHAR(20) NOT NULL,
    ticket_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (username) REFERENCES Users(username),
    FOREIGN KEY (trainNo) REFERENCES Trains(trainNo),
    UNIQUE KEY (id)
);

-- Transactions Table
CREATE TABLE Transactions (
    transactionId VARCHAR(50) PRIMARY KEY,
    id INT AUTO_INCREMENT,
    pnr VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL,
    date_time DATETIME NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pnr) REFERENCES Tickets(pnr),
    FOREIGN KEY (username) REFERENCES Users(username),
    UNIQUE KEY (id)
);


ALTER table Users
add role varchar(20);

alter table users
drop column roll;

select * from users;


update users
set role = "admin"
where username = "AthSat";

select * from Trains;

alter table Users
drop column mobileNo;

alter table tickets
add userMobile varchar(30);

ALTER TABLE tickets
ADD passenger1 VARCHAR(255),
ADD passenger2 VARCHAR(255),
ADD passenger3 VARCHAR(255),
ADD passenger4 VARCHAR(255);

alter table trains
add costPerSeat INT;

select * from tickets;

delete from tickets where trainNo = 10001;

alter table tickets
add dateOfDeparture VARCHAR(100);

select * from transactions;
delete from tickets;

delete from transactions;
delete from tickets;
delete from trains;
delete from users;

select * from users;
select * from trains;
select * from tickets;
select * from transactions;

INSERT INTO Users (username, password, role) value ("Admin", "Admin", "admin");

update trains
set available_seats = 800
where id = 6;