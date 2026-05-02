package bookapp.model;

import java.time.LocalDate;

public class BorrowRecord {

    private Book book;
    private User user;
    private LocalDate dueDate;
    private double totalAmount;
    private boolean returned = false;

    public BorrowRecord(Book book, User user, LocalDate dueDate, double totalAmount) {
        this.book = book;
        this.user = user;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean r) {
        returned = r;
    }

    public void setDueDate(LocalDate d) {
        dueDate = d;
    }

    public void setTotalAmount(double amt) {
        totalAmount = amt;
    }
}