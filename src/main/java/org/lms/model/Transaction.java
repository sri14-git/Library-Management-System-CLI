package org.lms.model;

import org.lms.enums.Status;
import org.lms.enums.TransactionType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column
    private LocalDateTime expectedReturnDate;

    @Column
    private LocalDateTime actualReturnDate;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    public Transaction() {}

    public Transaction(Member member, Book book, LocalDateTime date,
                       TransactionType type, LocalDateTime expectedReturnDate, LocalDateTime actualReturnDate, Status status) {
        this.member = member;
        this.book = book;
        this.quantity = 1;
        this.date = date;
        this.type = type;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }


    public void setMember(Member member) {
        this.member = member;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("| %-14d | %-20s | %-10d |%-20s | %-8d | %-12s | %-10s | %-18s | %-18s | %-10s |"
                , transactionId,(member != null) ? member.getName() : "N/A"
                ,book.getBookId(),(book != null) ? book.getTitle() : "N/A",quantity,(date != null) ? date.toString() : "N/A",
                type,(expectedReturnDate != null) ? expectedReturnDate.toString() : "N/A",
                (actualReturnDate != null) ? actualReturnDate.toString() : "N/A"
        ,status);
    }
}
