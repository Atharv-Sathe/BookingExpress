# Booking Express

**Booking Express** is a desktop application built with Java Swing for managing railway bookings. It provides features for both users and admins to handle train tickets and transactions efficiently. The application ensures role-based access and offers a modular, maintainable structure.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Directory Structure](#directory-structure)
- [Database Schemas](#database-schemas)
- [Installation](#installation)

---

## Overview

Booking Express facilitates:
- **Users:** Booking train tickets, viewing and canceling tickets, and tracking transactions.
- **Admins:** Managing trains and users, including adding, removing, and editing details.

**GitHub Repository:** [Booking Express](https://github.com/Atharv-Sathe/BookingExpress)

---

## Features

### User Features:
- **Authentication:** Login and Registration.
- **Book Tickets:** Reserve train seats for up to four passengers.
- **Manage Tickets:** View, cancel booked tickets.
- **Transaction History:** Track all transactions made.

### Admin Features:
- **Train Management:** Add, edit, and remove train details.
- **User Management:** Manage registered users (future updates).
- **Search Trains:** Locate train details efficiently.

### Additional:
- **Role-based Access Control:** Separate dashboards for users and admins.
- **Real-Time Updates:** Automatically update seat availability and transaction logs.

---

## Directory Structure

```bash
src/
└── com.bookingexpress/
    ├── dao/           # Data Access Objects
    │   ├── TicketDAO.java
    │   ├── TrainDAO.java
    │   ├── TransactionDAO.java
    │   └── UserDAO.java
    ├── models/        # Entity Models
    │   ├── Ticket.java
    │   ├── Train.java
    │   ├── Transaction.java
    │   └── User.java
    ├── ui/            # User Interface
    │   ├── AdminDashboard.java
    │   ├── BookTicketPanel.java
    │   ├── LoginRegistrationFrame.java
    │   └── UserDashboard.java
    ├── utils/         # Utility Classes
    │   ├── DatabaseUtil.java
    │   └── RouteManager.java
    └── Main.java      # Application Entry Point

```
---

## Database Schemas

### 1. Users
| Column Name | Data Type   | Description              |
|-------------|-------------|--------------------------|
| id          | INT         | Primary Key              |
| username    | VARCHAR(50) | User's unique username   |
| password    | VARCHAR(50) | User's password (hashed) |
| role        | VARCHAR(10) | User's role (user/admin) |

### 2. Trains
| Column Name     | Data Type   | Description                    |
|------------------|-------------|--------------------------------|
| id              | INT         | Primary Key                   |
| trainNo         | VARCHAR(10) | Train's unique number          |
| bogeys          | INT         | Number of train bogeys         |
| max_capacity    | INT         | Maximum seat capacity          |
| available_seats | INT         | Available seats for booking    |
| route           | VARCHAR(50) | Train route                    |
| train_status    | VARCHAR(20) | Current status (e.g., transit) |
| costPerSeat     | DECIMAL(10) | Cost per seat                  |

### 3. Tickets
| Column Name     | Data Type   | Description                       |
|------------------|-------------|-----------------------------------|
| pnr             | VARCHAR(10) | Unique ticket ID                 |
| username        | VARCHAR(50) | Associated username              |
| trainNo         | VARCHAR(10) | Train number                     |
| ticket_status   | VARCHAR(10) | Current ticket status (e.g., active) |
| userMobile      | VARCHAR(15) | Contact number                   |
| passenger1-4    | VARCHAR(50) | Passenger details                |
| dateOfDeparture | DATE        | Departure date                   |

### 4. Transactions
| Column Name     | Data Type   | Description                      |
|------------------|-------------|----------------------------------|
| transactionId   | VARCHAR(10) | Unique transaction ID           |
| pnr             | VARCHAR(10) | Associated ticket PNR           |
| username        | VARCHAR(50) | User initiating the transaction |
| date_time       | TIMESTAMP   | Date and time of transaction    |
| amount          | DECIMAL(10) | Transaction amount              |

---
## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Atharv-Sathe/BookingExpress.git
   cd BookingExpress
   ```
2. Set Up MySQL tables:
   - Import provided database schema from `src/main/resources/booking_express.sql`.
   - Update `database.properties` and `DatabaseUtil.java`.
3. Build the project using Maven:
   ```bash
   nvm clean install
   ```
4. Run the application
   - Execute `Main.java` from your IDE.
---
## Contact

- Email me at: [@AthSat](mailto:atharvsathe28704@gmail.com)!
- X Handle : [X](https://X.com/@AthSat7) !
- If you like my projects, follow me on [GitHub](https://github.com/Atharv-Sathe) !
- Star [this](https://github.com/Atharv-Sathe/BookingExpress.git) repository !
