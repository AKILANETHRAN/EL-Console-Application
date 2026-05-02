package bookapp.main;

import bookapp.model.*;
import bookapp.service.LibraryService;
import java.util.*;

public class AdvancedBookApp {

    static Scanner sc = LibraryService.sc;

    public static void main(String[] args) {

        LibraryService.users = bookapp.service.FileHandler.loadUsers();
        LibraryService.books = bookapp.service.FileHandler.loadBooks(LibraryService.users);
        LibraryService.records = bookapp.service.FileHandler.loadRecords(LibraryService.users,
                LibraryService.books);
        System.out.println("Data loaded. Users: " + LibraryService.users.size()
                + "  Books: " + LibraryService.books.size());

        while (true) {

            List<String> mainMenu = Arrays.asList("Register", "Login", "Exit");
            for (int i = 0; i < mainMenu.size(); i++) {
                System.out.print((i + 1) + "." + mainMenu.get(i) + (i == mainMenu.size() - 1 ? "" : " "));
            }
            System.out.println();
            int ch = Integer.parseInt(sc.nextLine());

            if (ch == 1) {
                System.out.print("Name:");
                String n = sc.nextLine();
                System.out.print("Email:");
                String e = sc.nextLine();
                System.out.print("Password:");
                String p = sc.nextLine();
                LibraryService.register(new User(n, e, p));
            } else if (ch == 2) {

                System.out.print("Email:");
                String e = sc.nextLine();
                System.out.print("Password:");
                String p = sc.nextLine();

                User u = LibraryService.login(e, p);

                if (u != null)
                    userMenu(u);
                else
                    System.out.println("Invalid");
            } else
                break;
        }
    }

    public static void userMenu(User user) {

        while (true) {

            List<String> uMenu = Arrays.asList("Add", "Search Title", "Search Publisher", "Search City", "Borrow",
                    "Check Overdue", "Logout");
            for (int i = 0; i < uMenu.size(); i++) {
                System.out.print((i + 1) + "." + uMenu.get(i) + (i == uMenu.size() - 1 ? "" : " "));
            }
            System.out.println();
            int ch = Integer.parseInt(sc.nextLine());

            if (ch == 1) {

                System.out.print("Title:");
                String t = sc.nextLine();

                System.out.print("Publisher:");
                String p = sc.nextLine();

                System.out.print("City:");
                String c = sc.nextLine();

                List<Genre> genres = Arrays.asList(Genre.values());
                for (int i = 0; i < genres.size(); i++) {
                    System.out.print((i + 1) + "." + genres.get(i).name() + (i == genres.size() - 1 ? "" : " "));
                }
                System.out.println();
                Genre g = genres.get(Integer.parseInt(sc.nextLine()) - 1);

                List<Availability> availabilities = Arrays.asList(Availability.values());
                for (int i = 0; i < availabilities.size(); i++) {
                    System.out.print(
                            (i + 1) + "." + availabilities.get(i).name() + (i == availabilities.size() - 1 ? "" : " "));
                }
                System.out.println();
                Availability a = availabilities.get(Integer.parseInt(sc.nextLine()) - 1);

                double price = 0;

                if (a == Availability.LEND) {
                    System.out.print("Monthly Rent:");
                    price = Double.parseDouble(sc.nextLine());
                } else if (a == Availability.SALE) {
                    System.out.print("Book Price:");
                    price = Double.parseDouble(sc.nextLine());
                }

                LibraryService.addBook(new Book(t, p, c, g, a, price, user));
            }

            else if (ch == 2) {
                System.out.print("Enter Title:");
                LibraryService.searchByTitle(sc.nextLine());
            } else if (ch == 3) {
                System.out.print("Enter Publisher:");
                LibraryService.searchByPublisher(sc.nextLine());
            } else if (ch == 4) {
                System.out.print("Enter City:");
                LibraryService.searchByCity(sc.nextLine());
            }

            else if (ch == 5) {
                System.out.print("Book ID:");
                String id = sc.nextLine();
                LibraryService.borrowBook(user, id);
            }

            else if (ch == 6)
                LibraryService.checkOverdue(user);
            else
                break;
        }
    }
}