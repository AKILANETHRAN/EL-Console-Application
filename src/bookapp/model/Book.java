package bookapp.model;

public class Book {

    private String bookId;
    private String title;
    private String publisher;
    private String city;
    private Genre genre;
    private Availability availability;
    private double price;
    private User owner;
    private boolean borrowed = false;

    public Book(String title, String publisher, String city,
            Genre genre, Availability availability, double price, User owner) {

        this.title = title;
        this.publisher = publisher;
        this.city = city;
        this.genre = genre;
        this.availability = availability;
        this.price = price;
        this.owner = owner;
        this.bookId = generateId();
    }

    private String generateId() {
        return genre.toString().substring(0, 2) + (int) (Math.random() * 900 + 100);
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCity() {
        return city;
    }

    public Genre getGenre() {
        return genre;
    }

    public Availability getAvailability() {
        return availability;
    }

    public double getPrice() {
        return price;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean b) {
        borrowed = b;
    }
}