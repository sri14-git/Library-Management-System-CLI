package org.lms.service;

import org.lms.dao.BookDAO;
import org.lms.dao.MemberDAO;
import org.lms.dao.TransactionDAO;
import org.lms.enums.Status;
import org.lms.enums.TransactionType;
import org.lms.model.Book;
import org.lms.model.Member;
import org.lms.model.Transaction;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {

    private final EntityManager em;
    private final TransactionDAO transactionDAO;
    private final MemberDAO memberDAO;
    private final BookDAO bookDAO;
    public TransactionService(EntityManager em) {
        this.em = em;
        this.transactionDAO = new TransactionDAO(em);
        this.memberDAO = new MemberDAO(em);
        this.bookDAO = new BookDAO(em);
    }
///**************MEMBERS***************************
    public void borrowBook(int memberId, int bookId){
        Book book = bookDAO.findById(bookId);
        Member member = memberDAO.findById(memberId);
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime expectedReturnDate = date.plusDays(7);
        if (book == null){
            System.out.println("Book not found");
            return;
        }
        if (member == null){
            System.out.println("Member not found");
            return;
        }
        if(book.getCopiesAvailable() < 0){
            System.out.println("Not enough copies available");
        }
        else{
            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            bookDAO.update(book);
            Transaction transaction = new Transaction(member,book,date, TransactionType.BORROW,expectedReturnDate,null, Status.ACTIVE);
            transactionDAO.save(transaction);
        }
        System.out.println("Book borrowed successfully,Kindly return on or before"+expectedReturnDate);
    }

    public void returnBook(int memberId, int bookId){
        Book book = bookDAO.findById(bookId);
        Member member = memberDAO.findById(memberId);
        Transaction borrow = transactionDAO.findOpenBorrow(bookId,memberId);
        LocalDateTime date = LocalDateTime.now();
        if (book == null){
            System.out.println("Book not found");
            return;
        }
        if (member == null){
            System.out.println("Member not found");
            return;
        }
        if (borrow == null){
            System.out.println("No active borrow found");
            return;
        }
        if (borrow.getExpectedReturnDate().isBefore(date) && borrow.getExpectedReturnDate()!=null){
            System.out.println("Late return detected, Expected Return Date: "+borrow.getExpectedReturnDate().toLocalDate()+"Actual Return Date: "+date); //check to Print
            System.out.println("Extra Charges May apply");
        }
        int borrowedQty = borrow.getQuantity();
        if (borrowedQty <= 0) {
            System.out.println("All borrowed copies already returned.");
            return;
        }
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);

        Transaction transaction = new Transaction(member,book,date,TransactionType.RETURN,null,date,Status.COMPLETED);
        borrow.setStatus(Status.COMPLETED);
        borrow.setQuantity(borrowedQty - 1);
        transactionDAO.save(transaction);
        borrow.setActualReturnDate(date);
        transactionDAO.update(borrow); ///updatind borrowed Status



    }
    public List<Transaction> getMemberTransactionByType(int memberId, TransactionType type){
        return transactionDAO.getMembersTransactionsByType(memberId,type);
    }
    ///*************LIBRARIAN*****************
    public List<Transaction> getMemberTransactions(int memberId){
        return transactionDAO.findByMemberId(memberId);
    }
    public List<Transaction> getTransactionsByType(TransactionType type){
        return transactionDAO.getByType(type);
    }
    public List<Transaction> findAllTransactions(){
        return transactionDAO.findAll();
    }
    public Transaction findById(int id){
        return transactionDAO.findById(id);
    }


}
