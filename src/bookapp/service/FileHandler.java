package bookapp.service;

import bookapp.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class FileHandler {

    private static final String USERS_FILE   = "data/users.txt";
    private static final String BOOKS_FILE   = "data/books.txt";
    private static final String RECORDS_FILE = "data/records.txt";

    // ─── SAVE USERS ──────────────────────────────────────────
    // Format: name|email|password

    public static void saveUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                bw.write(u.getName() + "|" + u.getEmail() + "|" + u.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // ─── SAVE BOOKS ──────────────────────────────────────────
    // Format:
    // bookId|title|publisher|city|genre|availability|price|ownerEmail|borrowed

    public static void saveBooks(List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : books) {
                bw.write(b.getBookId() + "|"
                        + b.getTitle() + "|"
                        + b.getPublisher() + "|"
                        + b.getCity() + "|"
                        + b.getGenre() + "|"
                        + b.getAvailability() + "|"
                        + b.getPrice() + "|"
                        + b.getOwner().getEmail() + "|"
                        + b.isBorrowed());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // ─── SAVE RECORDS ────────────────────────────────────────
    // Format: bookId|userEmail|dueDate|totalAmount|returned

    public static void saveRecords(List<BorrowRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RECORDS_FILE))) {
            for (BorrowRecord r : records) {
                bw.write(r.getBook().getBookId() + "|"
                        + r.getUser().getEmail() + "|"
                        + r.getDueDate() + "|"
                        + r.getTotalAmount() + "|"
                        + r.isReturned());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    // ─── LOAD USERS ──────────────────────────────────────────

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists())
            return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    // ─── LOAD BOOKS ──────────────────────────────────────────

    public static List<Book> loadBooks(List<User> users) {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        if (!file.exists())
            return books;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 9) {
                    String bookId = parts[0];
                    String title = parts[1];
                    String publisher = parts[2];
                    String city = parts[3];
                    Genre genre = Genre.valueOf(parts[4]);
                    Availability avail = Availability.valueOf(parts[5]);
                    double price = Double.parseDouble(parts[6]);
                    String ownerEmail = parts[7];
                    boolean borrowed = Boolean.parseBoolean(parts[8]);

                    // Find the owner user by email
                    User owner = findUserByEmail(users, ownerEmail);
                    if (owner != null) {
                        Book b = new Book(title, publisher, city, genre, avail, price, owner);
                        b.setBookId(bookId); // restore original ID
                        b.setBorrowed(borrowed);
                        books.add(b);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    // ─── LOAD RECORDS ────────────────────────────────────────

    public static List<BorrowRecord> loadRecords(List<User> users, List<Book> books) {
        List<BorrowRecord> records = new ArrayList<>();
        File file = new File(RECORDS_FILE);
        if (!file.exists())
            return records;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String bookId = parts[0];
                    String userEmail = parts[1];
                    LocalDate dueDate = LocalDate.parse(parts[2]);
                    double totalAmount = Double.parseDouble(parts[3]);
                    boolean returned = Boolean.parseBoolean(parts[4]);

                    Book book = findBookById(books, bookId);
                    User user = findUserByEmail(users, userEmail);

                    if (book != null && user != null) {
                        BorrowRecord r = new BorrowRecord(book, user, dueDate, totalAmount);
                        r.setReturned(returned);
                        records.add(r);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading records: " + e.getMessage());
        }
        return records;
    }

    // ─── HELPER METHODS ──────────────────────────────────────

    private static User findUserByEmail(List<User> users, String email) {
        for (User u : users) {
            if (u.getEmail().equals(email))
                return u;
        }
        return null;
    }

    private static Book findBookById(List<Book> books, String id) {
        for (Book b : books) {
            if (b.getBookId().equals(id))
                return b;
        }
        return null;
    }
}
