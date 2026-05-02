# EL Console Application (Advanced Book Sharing App)

A Java-based console application designed to manage a library or book-sharing system. This application allows users to register, add books, borrow/return books, and persists all data using file handling.

## Features

- **User Management**: Register and manage users.
- **Book Management**: Add new books with details like Title, Author, and Genre.
- **Borrowing System**: Users can borrow and return books. Tracks book availability automatically.
- **Data Persistence**: Uses local text files (`books.txt`, `users.txt`, `records.txt`) to save application state across sessions, ensuring no data is lost when the application closes.
- **Console Interface**: Interactive, menu-driven CLI for easy navigation and usage.

## Project Structure

- `src/bookapp/model/`: Contains data models like `Book`, `User`, `BorrowRecord`, `Genre`, and `Availability`.
- `src/bookapp/service/`: Contains the business logic (`LibraryService`) and data persistence logic (`FileHandler`).
- `src/bookapp/main/`: Contains the main entry point (`AdvancedBookApp`) which handles the console UI.
- `data/`: Directory where the text files are stored for data persistence.

## Technologies Used

- **Java**: Core application logic.
- **File I/O**: For storing data in text files.
- **Collections Framework**: For managing in-memory data structures.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/AKILANETHRAN/EL-Console-Application.git
   ```
2. Navigate to the project directory and compile the Java files.
3. Run the `AdvancedBookApp` main class to start the console interface.

## Usage
Upon running the application, follow the on-screen menu prompts to:
1. Register a new user.
2. Add a new book to the library.
3. Borrow a book.
4. Return a book.
5. View available books and user records.
