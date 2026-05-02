package bookapp.service;

import bookapp.model.*;
import java.io.*;
import java.util.*;

public class LibraryService {

    public static List<User> users = new ArrayList<>();
    public static List<Book> books = new ArrayList<>();
    public static List<BorrowRecord> records = new ArrayList<>();
    public static final Scanner sc = new Scanner(System.in);

    private static java.time.LocalDate safeDateInput() {
        while (true) {
            try {
                String dateStr = sc.nextLine();
                String[] parts = dateStr.split("-");
                return java.time.LocalDate.of(
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[0]));
            } catch (Exception e) {
                System.out.print("Invalid format! Use dd-mm-yyyy (e.g. 05-03-2026): ");
            }
        }
    }

    public static void register(User u) {
        users.add(u);
        FileHandler.saveUsers(users);
    }

    public static User login(String email, String pass) {
        for (User u : users)
            if (u.getEmail().equals(email) && u.getPassword().equals(pass))
                return u;
        return null;
    }

    public static void addBook(Book b) {
        books.add(b);
        FileHandler.saveBooks(books);
        System.out.println("Book Added Successfully!");
    }

    public static void searchByTitle(String title) {
        for (Book b : books)
            if (b.getTitle().equalsIgnoreCase(title) && !b.isBorrowed())
                System.out.println(b.getBookId() + " " + b.getTitle() + " " + b.getAvailability());
    }

    public static void searchByPublisher(String pub) {
        for (Book b : books)
            if (b.getPublisher().equalsIgnoreCase(pub) && !b.isBorrowed())
                System.out.println(b.getBookId() + " " + b.getTitle() + " " + b.getAvailability());
    }

    public static void searchByCity(String city) {
        for (Book b : books)
            if (b.getCity().equalsIgnoreCase(city) && !b.isBorrowed())
                System.out.println(b.getBookId() + " " + b.getTitle() + " " + b.getAvailability());
    }

    public static void borrowBook(User user, String id) {
        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(id) && !b.isBorrowed()) {
                if (b.getAvailability() == Availability.LEND) {
                    System.out.print("Enter number of months needed: ");
                    int months = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Today's Date (dd-mm-yyyy): ");
                    java.time.LocalDate startDate = safeDateInput();
                    java.time.LocalDate dueDate = startDate.plusMonths(months);

                    double total = months * b.getPrice();

                    records.add(new BorrowRecord(b, user, dueDate, total));
                    b.setBorrowed(true);
                    FileHandler.saveBooks(books);
                    FileHandler.saveRecords(records);

                    System.out.println("Book Borrowed Successfully");
                    System.out.println("Total Lend Amount = Rs." + total);
                    System.out.println("Due Date = " + dueDate);
                    return;
                }

                if (b.getAvailability() == Availability.SALE) {
                    b.setBorrowed(true);
                    FileHandler.saveBooks(books);
                    System.out.println("Book Purchased for Rs." + b.getPrice());
                    return;
                }

                if (b.getAvailability() == Availability.GIVEAWAY) {
                    b.setBorrowed(true);
                    FileHandler.saveBooks(books);
                    System.out.println("Book Received for FREE");
                    return;
                }
            }
        }
        System.out.println("Book not available");
    }

    public static void checkOverdue(User user) {
        boolean found = false;
        for (BorrowRecord r : records) {
            if (!r.isReturned() && r.getUser().getEmail().equals(user.getEmail())) {
                found = true;
                System.out.println("\nChecking record for: " + r.getBook().getTitle());
                System.out.print("Enter Current Date (dd-mm-yyyy): ");
                java.time.LocalDate current = safeDateInput();
                java.time.LocalDate due = r.getDueDate();

                if (current.isBefore(due)) {
                    System.out.println("Status: On Time (Due: " + due + ")");
                    System.out.print("Do you want to return the book now? (yes/no): ");
                    if (sc.nextLine().equalsIgnoreCase("yes")) {
                        performReturn(r);
                    }
                } else {
                    System.out.println("⚠ Due Date Finished!");
                    long lateDays = java.time.temporal.ChronoUnit.DAYS.between(due, current);
                    double fine = lateDays * 15;
                    System.out.println("Fine accumulated: Rs." + fine);

                    System.out.print("Do you want to (1) Extend or (2) Return now?: ");
                    int choice = sc.nextInt();
                    sc.nextLine();

                    if (choice == 1) {
                        System.out.print("Enter extra days: ");
                        int extra = sc.nextInt();
                        sc.nextLine();
                        r.setDueDate(due.plusDays(extra));
                        double extraRent = (r.getBook().getPrice() / 30) * extra;
                        r.setTotalAmount(r.getTotalAmount() + extraRent + fine);
                        FileHandler.saveRecords(records);
                        System.out.println("Extended! New due date: " + r.getDueDate());
                    } else {
                        r.setTotalAmount(r.getTotalAmount() + fine);
                        performReturn(r);
                    }
                }
            }
        }
        if (!found)
            System.out.println("No borrowed books found");
    }

    private static void performReturn(BorrowRecord r) {
        r.setReturned(true);
        r.getBook().setBorrowed(false);
        FileHandler.saveBooks(books);
        FileHandler.saveRecords(records);
        System.out.println("Book Returned Successfully! Total Amount: Rs." + r.getTotalAmount());
    }
}