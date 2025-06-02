package org.lms.dao;

import org.lms.enums.Status;
import org.lms.enums.TransactionType;
import org.lms.model.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TransactionDAO {

    private final EntityManager em;

    public TransactionDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Transaction transaction) {
        em.getTransaction().begin();
        em.persist(transaction);
        em.getTransaction().commit();
    }

    public Transaction findById(int id) {

        try{
            return em.find(Transaction.class,id);
        }
        catch (Exception e){
            return null;
        }
    }


    public List<Transaction> getByType(TransactionType type){
        TypedQuery<Transaction> query = em.createQuery("Select t from Transaction t where t.type = :type "
                                                        , Transaction.class);
        query.setParameter("type",type);
        return query.getResultList();
    }

    public void update(Transaction member) {
        em.getTransaction().begin();
        em.merge(member);
        em.getTransaction().commit();
    }


    public List<Transaction> findAll() {
        TypedQuery<Transaction> query =em.createQuery("SELECT t FROM Transaction t ORDER BY t.transactionId ",Transaction.class);
        return query.getResultList();
    }
    public Transaction findOpenBorrow(int transactionID, int memberId) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.transactionId = :transactionId " +
                        "AND t.member.memberId = :memberId " +
                        "AND t.type = 'BORROW' " +
                        "AND t.actualReturnDate IS NULL AND t.status = :status", Transaction.class);

        query.setParameter("transactionId", transactionID);
        query.setParameter("memberId", memberId);
        query.setParameter("status", Status.ACTIVE);
        return query.getResultStream().findFirst().orElse(null);
    }

//
//
    public List<Transaction> getMembersTransactionsByType(int memberId, TransactionType type,Status status) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE " +
                                            "t.member.memberId = :memberId AND t.type = :type AND t.status = :status ORDER BY t.transactionId", Transaction.class);
        query.setParameter("memberId", memberId);
        query.setParameter("type", type);
        query.setParameter("status", status);
        return query.getResultList();

    }

    public List<Transaction> getbyBookId(int bookId) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.book.bookId = :bookId ORDER BY t.transactionId "
                                                        ,Transaction.class);
        query.setParameter("bookId",bookId);
        return query.getResultList();
    }

    public List<Transaction> findByMemberId(int memberId) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.member.memberId = :memberId ORDER BY t.transactionId "
                                                        ,Transaction.class);
        query.setParameter("memberId",memberId);
        return query.getResultList();
    }
}
