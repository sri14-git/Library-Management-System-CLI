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
        if (book == null || book.getStatus() != Status.AVAILABLE){
            System.out.println("Book not found");
            return;
        }
        if (member == null || member.getStatus() != Status.ACTIVE){
            System.out.println("Member not found");
            return;
        }
        if(book.getCopiesAvailable() < 0){
            System.out.println("Not enough copies available");
        }
        else{
//            int totalBorrowed = book.getTimesBorrowed();
            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            book.setTimesBorrowed(book.getTimesBorrowed() + 1);
            bookDAO.update(book);
            Transaction transaction = new Transaction(member,book,date, TransactionType.BORROW,expectedReturnDate,null, Status.ACTIVE);
            transactionDAO.save(transaction);
        }
        System.out.println("Book borrowed successfully,Kindly return on or before (7 days) "+expectedReturnDate.toLocalDate() );
    }

    public void returnBook(int memberId, int transactionId){
        Transaction transaction = transactionDAO.findById(transactionId);
        if (transaction == null)
        {
            System.out.println("Invalid transaction id");
            return;
        }
        Book book = bookDAO.findById(transaction.getBook().getBookId());
        Member member = memberDAO.findById(transaction.getMember().getMemberId());
        Transaction borrow = transactionDAO.findOpenBorrow(transactionId,memberId);
        LocalDateTime date = LocalDateTime.now();
        if (book == null || book.getStatus() != Status.AVAILABLE){
            System.out.println("Book not found");
            return;
        }
        if (member == null|| member.getStatus() != Status.ACTIVE){
            System.out.println("Member not found");
            return;
        }
        if (borrow == null || borrow.getStatus() != Status.ACTIVE){
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

        Transaction newtransaction = new Transaction(member,book,date,TransactionType.RETURN,null,date,Status.COMPLETED);
        borrow.setQuantity(borrowedQty - 1);
        borrow.setStatus(Status.COMPLETED);
        transactionDAO.save(newtransaction);
        borrow.setActualReturnDate(date);
        transactionDAO.update(borrow);

    }
    public List<Transaction> getMemberTransactionByType(int memberId, TransactionType type,Status status){
        return transactionDAO.getMembersTransactionsByType(memberId,type,status);
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
    public List<Transaction> getbyBookId(int bookid){
        return transactionDAO.getbyBookId(bookid);
    }

    /// **********Print**********
    public void printTransactions(List<Transaction> transactions){

        String format = "| %-13s | %-10s | %-6s | %-25s | %-8s | %-16s | %-6s | %-18s | %-18s | %-9s |\n";
        String sept = "+---------------+------------+--------+---------------------------" +
                "+----------+------------------+--------+--------------------+--------------------+-----------+";
        System.out.println(sept);
        System.out.printf(format, "TransactionID", "MemberName", "BookID", "Title", "Quantity", "Date", "Type", "ExpectedReturn", "ActualReturn", "Status");
        System.out.println(sept);
        for (Transaction t : transactions) {
            String memberName = t.getMember().getName();
            String title = t.getBook().getTitle();
            int bookId = t.getBook().getBookId();
            int txnId = t.getTransactionId();
            int qty = t.getQuantity();
            String date = t.getDate().toLocalDate().toString();
            String expected = t.getExpectedReturnDate() != null ? t.getExpectedReturnDate().toLocalDate().toString() : "--";
            String actual = t.getActualReturnDate() != null ? t.getActualReturnDate().toLocalDate().toString() : "--";
            String status = t.getStatus().name();
            System.out.printf(format, txnId, memberName, bookId, title, qty, date, t.getType(), expected, actual, status);
        }
        System.out.println(sept);
    }
    public void printTransactions(Transaction t){

        String format = "| %-13s | %-10s | %-6s | %-25s | %-8s | %-16s | %-6s | %-18s | %-18s | %-9s |\n";
        String separator = "+---------------+------------+--------+-------------------------" +
                "--+----------+------------------+--------+--------------------+--------------------+-----------+";

        System.out.println(separator);
        System.out.printf(format, "TransactionID", "MemberName", "BookID", "Title", "Quantity", "Date", "Type", "ExpectedReturn", "ActualReturn", "Status");
        System.out.println(separator);

            String memberName = t.getMember().getName();
            String title = t.getBook().getTitle();
            int bookId = t.getBook().getBookId();
            int txnId = t.getTransactionId();
            int qty = t.getQuantity();
            String date = t.getDate().toLocalDate().toString();
            String expected = t.getExpectedReturnDate() != null ? t.getExpectedReturnDate().toLocalDate().toString() : "--";
            String actual = t.getActualReturnDate() != null ? t.getActualReturnDate().toLocalDate().toString() : "--";
            String status = t.getStatus().name();

            System.out.printf(format, txnId, memberName, bookId, title, qty, date, t.getType(), expected, actual, status);

        System.out.println(separator);
    }


}
