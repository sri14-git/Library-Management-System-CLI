package org.lms.service;

import org.lms.dao.BookDAO;
import org.lms.model.Book;

import javax.persistence.EntityManager;
import java.util.List;

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

}
