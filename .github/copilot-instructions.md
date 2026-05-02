# AdvancedBookSharingApp - AI Coding Agent Guidelines

## Architecture Overview

**Three-tier design:**
- **UI Layer** (`main/AdvancedBookApp.java`): Menu-driven CLI with `Scanner` input, routes user actions
- **Service Layer** (`service/LibraryService.java`): Business logic; static collections hold all in-memory data (`users`, `books`, `records`)
- **Data Layer** (`service/FileHandler.java`): Persistence using pipe-delimited (`|`) text files: `users.txt`, `books.txt`, `records.txt`
- **Models** (`model/*.java`): `User`, `Book`, `BorrowRecord` + enums `Genre`, `Availability`

## Critical Data Flow & Reconciliation

**On Startup:**
1. `FileHandler.loadUsers()` → `FileHandler.loadBooks(users)` → `FileHandler.loadRecords(users, books)`
2. Key pattern: Object references must be reconstructed from files (e.g., `Book.owner` is a `User` reference, need user email matching)
3. **Never** deserialize objects directly; always rebuild relationships via lookups (see `loadBooks()` and `loadRecords()` in [src/bookapp/service/FileHandler.java](src/bookapp/service/FileHandler.java))

**Mutation Pattern:**
- Any edit to books/users/records MUST call corresponding `FileHandler.save*()` immediately after (e.g., `FileHandler.saveBooks(books)` after `Library.addBook()`)
- Missing save calls = silent data loss on next restart

## Domain Model Details

**Book States & Pricing:**
- `Availability.LEND`: Monthly rental (`price` = monthly rent amount)
- `Availability.SALE`: One-time purchase (`price` = sale price)
- `Availability.GIVEAWAY`: Free (price irrelevant)
- Book ID generated as: 2-letter genre prefix + 3-digit random (e.g., `CO234`)

**BorrowRecord Lifecycle:**
- Created when book borrowed; tracks user, due date, total amount
- Overdue handling: Rs. 15/day fine; users can extend or pay fine + return
- Extension: Due date extended + extra rent (pro-rata daily rate) + accumulated fine added to total

**Genre Enum:**
`CODING, ACTION, THRILLER, SCIFI, NONSCIFI, ADVENTURE, TRAUMA`

## File Persistence Format

All files are pipe-delimited with headers implied by order:

- **users.txt**: `name|email|password`
- **books.txt**: `bookId|title|publisher|city|genre|availability|price|ownerEmail|borrowed`
- **records.txt**: `bookId|userEmail|dueDate|totalAmount|returned`

**Important:** Date format in records is `java.time.LocalDate.toString()` (ISO-8601: YYYY-MM-DD)  
**Important:** SafeDate input from CLI expects user format: `dd-mm-yyyy` (see `safeDateInput()` in [LibraryService.java](src/bookapp/service/LibraryService.java#L14))

## Common Patterns to Follow

1. **Search workflow:** Filter collections with `.equalsIgnoreCase()` for case-insensitive text matching; check `!b.isBorrowed()` before returning results (see `searchByTitle()`)
2. **Date calculations:** Use `java.time.LocalDate.plusMonths()`, `plusDays()`, and `ChronoUnit.DAYS.between()` for fine calculations
3. **Menu rendering:** Build `List<String>` of options, loop with index to print `i+1.optionName`
4. **User context:** After login, all operations are scoped to logged-in `User` (passed as parameter to service methods)

## Development Workflow

**Build & Run:**
```bash
javac -d out/production/AdvancedBookSharingApp src/bookapp/**/*.java
cd out/production/AdvancedBookSharingApp
java bookapp.main.AdvancedBookApp
```

**Test Data Locations:** `users.txt`, `books.txt`, `records.txt` in project root (created on first run)

## Key Extension Points

- **New search type:** Add method to `LibraryService` following `searchByTitle()` pattern + add menu option to `AdvancedBookApp.userMenu()`
- **New book model field:** Update `Book` class, then update save format in [FileHandler.saveBooks()](src/bookapp/service/FileHandler.java#L28), then update load logic in `loadBooks()`
- **New Availability type:** Add to `Availability` enum, then add handling branch in `LibraryService.borrowBook()` (see LEND/SALE/GIVEAWAY pattern)
