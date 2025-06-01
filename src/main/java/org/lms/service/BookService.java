package org.lms.service;

import org.lms.dao.BookDAO;
import org.lms.model.Book;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hibernate.internal.util.StringHelper.truncate;

public class BookService {

    private final BookDAO bookDAO;
    public BookService(EntityManager em) {
        this.bookDAO = new BookDAO(em);
    }

    public void save(Book book){
        bookDAO.save(book);
    }
    public Book findById(int id){
        return bookDAO.findById(id);
    }
    public void update(Book book){
        bookDAO.update(book);
    }
    public List<Book> getAvailableBooks(){
        return bookDAO.getAvailableBooks();
    }
    public List<Book> findAll(){
        return bookDAO.findAll();
    }
    public void delete(int id){
        bookDAO.delete(id);
    }
    public  void printBooks(List<Book> books) {
        String format = "| %-8s | %-25s | %-20s | %-15s | %-12s | %-13s | %-14s |\n";
        System.out.println("+----------+---------------------------+----------------------+-----------------+--------------+---------------+----------------+");
        System.out.printf(format, "Book ID", "Title", "Author", "Genre", "Total Copies", "Available", "Times Borrowed");
        System.out.println("+----------+---------------------------+----------------------+-----------------+--------------+---------------+----------------+");

        for (Book b : books) {
            System.out.printf(format,
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getGenre(),
                    b.getTotalCopies(),
                    b.getCopiesAvailable(),
                    b.getTimesBorrowed());
        }

        System.out.println("+----------+---------------------------+----------------------+-----------------+--------------+---------------+----------------+");
    }
    public  void printBook(Book b) {
        String format = "| %-8s | %-25s | %-20s | %-15s | %-12s | %-13s | %-14s |\n";


        System.out.println("+----------+---------------------------+----------------------+-----------------+--------------+---------------+----------------+");
        System.out.printf(format, "Book ID", "Title", "Author", "Genre", "Total Copies", "Available", "Times Borrowed");
        System.out.println("+----------+---------------------------+----------------------+-----------------+--------------+---------------+----------------+");

        System.out.printf(format,
                b.getBookId(),
                truncate(b.getTitle(), 25),
                truncate(b.getAuthor(), 20),
                b.getGenre(),
                b.getTotalCopies(),
                b.getCopiesAvailable(),
                b.getTimesBorrowed());

        System.out.println("+----------+---------------------------+----------------------+-----------------+--------------+---------------+----------------+");
    }



}
