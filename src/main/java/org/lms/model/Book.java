package org.lms.model;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(nullable = false)
    private String title;

    private String author;
    private String genre;

    @Column(nullable = false)
    private int totalCopies;

    @Column(nullable = false)
    private int copiesAvailable;


    private int timesBorrowed;


    public Book() {}


    public Book(String title, String author, String genre, int totalCopies) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.copiesAvailable = totalCopies;
        this.timesBorrowed = 0;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public int getTimesBorrowed() {
        return timesBorrowed;
    }

    public void setTimesBorrowed(int timesBorrowed) {
        this.timesBorrowed = timesBorrowed;
    }

    @Override
    public String toString() {
        return String.format("| %-10d | %-20s | %-20s | %-15s | %-12d | %-15d | %-14d |", bookId, title, author,
                genre,totalCopies,copiesAvailable,timesBorrowed);
    }
}
