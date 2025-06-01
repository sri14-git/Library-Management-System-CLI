package org.lms.dao;

import org.lms.enums.Status;
import org.lms.model.Book;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class BookDAO {

    private final EntityManager em;

    public BookDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Book book) {
        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
    }

    public Book findById(int id) {
        try {
            return em.find(Book.class, id);
        }
        catch (Exception e){
            return null;
        }
    }

    public List<Book> getAvailableBooks(){
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.copiesAvailable > 0 AND b.status = :status ORDER BY b.bookId  "
                                                ,Book.class);
        query.setParameter("status",Status.AVAILABLE);
        return query.getResultList();

    }

    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b ORDER BY b.bookId", Book.class);
        return query.getResultList();
    }

    public void delete(int id) {
        Book book = findById(id);
        if (book != null) {
            em.getTransaction().begin();
            em.remove(book);
            em.getTransaction().commit();
        }
    }

    public void update(Book book) {
        em.getTransaction().begin();
        em.merge(book);
        em.getTransaction().commit();
    }
}
