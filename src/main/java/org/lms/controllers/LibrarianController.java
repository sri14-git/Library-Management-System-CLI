package org.lms.controllers;

import org.lms.enums.Status;
import org.lms.enums.TransactionType;
import org.lms.model.Book;
import org.lms.model.Member;
import org.lms.model.Transaction;
import org.lms.service.AuthService;
import org.lms.service.BookService;
import org.lms.service.MemberService;
import org.lms.service.TransactionService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LibrarianController {
    Scanner sc;
    private final AuthService authService;
    private final BookService bookService;
    private final MemberService memberService;
    private final TransactionService transactionService;

    public LibrarianController(Scanner sc, EntityManager em) {
        this.sc = sc;
        this.authService = new AuthService(em);
        this.bookService = new BookService(em);
        this.memberService = new MemberService(em);
        this.transactionService = new TransactionService(em);
    }

    public void MainMenu() {
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();
        int flag = 0;
        try{
        if (authService.authenticate(username, password).getUsername().equals("admin")) {
            while (flag == 0) {
                System.out.println("********Login Successful*******");
                System.out.println("1.Manage Books");
                System.out.println("2.Manage Members");
                System.out.println("3.Manage Transactions");
                System.out.println("4.Exit");
                System.out.println("********************************");
                System.out.print("Enter your Choice: ");
                switch (sc.nextInt()) {
                    case 1:
                        manageBooks();
                        break;
                    case 2: {
                        manageMembers();
                        break;
                    }
                    case 3: {
                        manageTransactions();
                        break;
                    }
                    case 4: {
                        flag = 1;
                        break;
                    }


                }
            }
        }

    }catch(NoResultException e){
            System.out.println(" Failed");
        }
        catch (NullPointerException e){
            System.out.println("Failed");
            System.out.println("Kindly Make Sure,Username and Password are given Correctly and Try Again");
            sc.nextLine();
        }
        catch (InputMismatchException e) {
            System.out.println("Kindly Make Sure to Option are given Correctly and Try Again");
            sc.nextLine();
        }



    }
     public void manageBooks () {
        int flag = 0;
        while (flag == 0) {
            try{
                System.out.println("******** BOOKS MANAGEMENT *******");
            System.out.println("1.Add Book");
            System.out.println("2.Update Book stocks");
            System.out.println("3.Delete Book");
            System.out.println("4.List Books");
            System.out.println("5.List Available Books");
            System.out.println("6.Exit");
                System.out.println("********************************");
            System.out.print("Enter your Choice:");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: {
                    System.out.println("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.println("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.println("Enter Copies Available: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    if(quantity<=0){
                        System.out.println("Invalid Quantity");
                        break;
                    }
                    System.out.println("Enter the Genre");
                    String genre = sc.nextLine();
                    Book book = new Book(title, author, genre, quantity, Status.AVAILABLE);
                    bookService.save(book);
                    bookService.printBook(book);
                    System.out.println("Book Added Successfully");
                    break;
                }
                case 2: {
                    List<Book> books = bookService.findAll();
                    if (!books.isEmpty()) {
                        bookService.printBooks(books);
                    } else {
                        System.out.println("No Books Found");
                        break;
                    }
                    System.out.print("Enter The ID Of The Book To Update Stock: ");
                    int id = sc.nextInt();
                    Book book = bookService.findById(id);
                    if (book == null || book.getStatus().name().equals(Status.UNAVAILABLE.name())) {
                        System.out.println("Invalid ID");
                        break;
                    }
                    else {
                        System.out.print("Enter the No Of Copies To Be Added: ");
                        int copies = sc.nextInt();
                        sc.nextLine();
                        if(book.getCopiesAvailable()+copies<0){
                            System.out.println("Invalid Quantity Or " +
                                    "Make Sure All Copies are Returned Before Updating Stock");
                            break;
                        }
                        book.setCopiesAvailable(book.getCopiesAvailable() + copies);
                        book.setTotalCopies(book.getTotalCopies() + copies);
                        if(book.getStatus().name().equals(Status.UNAVAILABLE.name())){
                            book.setStatus(Status.AVAILABLE);
                            System.out.println("Book is now Available");
                        }
                        bookService.update(book);
                        System.out.println("Successfully Updated Stock");
                    }
                    break;
                }
                case 3: {
                    List<Book> books = bookService.getAvailableBooks();
                    if (!books.isEmpty()) {
                        bookService.printBooks(books);
                    } else {
                        System.out.println("No Books Found");
                        break;
                    }
                    System.out.print("Enter The ID Book To Delete (Cannot Delete Books That are Not Returned Yet) : ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    Book book = bookService.findById(id);
                    List<Transaction> transactions = transactionService.getTransactionsByType(TransactionType.BORROW).stream()
                            .filter(transaction -> transaction.getStatus().name().equals(Status.ACTIVE.name()))
                            .filter(transaction -> transaction.getBook().getBookId()==book.getBookId()).toList();
//                    transactionService.printTransactions(transactions);
                    try{
                    if(transactions.get(0)!=null || transactions.get(0).getStatus()== Status.ACTIVE){

                        System.out.println("Book is currently in use, kindly return the book first before deleting it");
                        break;
                    }}catch (NoSuchElementException | ArrayIndexOutOfBoundsException e){}
                    book.setStatus(Status.UNAVAILABLE);
                    book.setCopiesAvailable(0);
                    book.setTotalCopies(0);

                    try{
                    bookService.update(book);
                    System.out.println("Deleted Successfully");
                    break;
                    }
                    catch (NullPointerException e){
                        System.out.println("Book is not available for deletion");
                    }
                    catch (RollbackException e){
                        System.out.println("Book is Currently in use");
                    }

                }
                case 4: {
                    List<Book> books = bookService.findAll();
                    if (!books.isEmpty()) {
                        bookService.printBooks(books);
                    } else {
                        System.out.println("No Books Found");
                        break;
                    }
                    break;
                }
                case 5: {
                    List<Book> books = bookService.getAvailableBooks();
                    if (!books.isEmpty()) {
                        bookService.printBooks(books);
                    } else {
                        System.out.println("No Books Found");
                    }
                    break;
                }
                case 6: {
                    flag = 1;
                    break;
                }
                default:
                    System.out.println("Invalid Choice");
            }
        }catch(InputMismatchException e){
                System.out.println("Kindly Make Sure inputs are given Correctly and Try Again");
                sc.nextLine();
            }
        }
    }

        public void manageMembers () {
        int flag = 0;
        while (flag == 0) {
            try{
            System.out.println("******** MEMBERS MANAGEMENT *******");
            System.out.println("1. Add Members");
            System.out.println("2. Delete Members");
            System.out.println("3. View All Members");
            System.out.println("4. View All Active Members");
            System.out.println("5. Find By ID");
            System.out.println("6. Find By Username");
            System.out.println("7. Make User Active");
            System.out.println("8. Exit");
                System.out.println("******************************");
            System.out.print("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: {
                    System.out.println("Enter Name of the Member");
                    String name = sc.nextLine();
                    System.out.println("Enter Username of the Member");
                    String username = sc.nextLine();
                    Member member = memberService.findByUsername(username);
                    if (member != null) {
                        System.out.println("Username already exists");
                        if(!member.getName().toLowerCase().equals(name.toLowerCase())){
                            System.out.println("User is a existing member, kindly change the Username" +
                                    " and try again");
                            break;
                        }
                        System.out.println("Enter new password of the Member");
                        String password = sc.nextLine();
                        member.setPassword(password);
                        member.setStatus(Status.ACTIVE);
                        memberService.update(member);
                        System.out.println("Member Active Successfully with new Password");
                        break;
                    }
                    System.out.println("Enter password of the Member");
                    String password = sc.nextLine();
                    Member member1 = new Member(name, username, password,Status.ACTIVE);
                    memberService.save(member1);
                    memberService.printMember(member1);
                    System.out.println("Member Created Successfully");
                    break;
                }
                case 2: {
                    List<Member> members = memberService.findAll().stream()
                            .filter(member -> member.getStatus().name().equals(Status.ACTIVE.name())).toList();
                    if (!members.isEmpty()) {
                        memberService.printMembers(members);
                    } else {
                        System.out.println("No Members Found");
                        break;
                    }
                    System.out.println("Enter The Id of the Member that is to be Deleted");
                    int id = sc.nextInt();
                    sc.nextLine();
                    Member member = memberService.findById(id);
                    if (member == null || member.getStatus().name().equals(Status.INACTIVE.name())) {
                        System.out.println("Invalid ID");
                        break;
                    }
                    List<Transaction> transaction = transactionService.getMemberTransactions(id).stream()
                            .filter(transaction1 -> transaction1.getStatus().name().equals(Status.ACTIVE.name()))
                            .toList();
                    if(!transaction.isEmpty()){
                            System.out.println("Member is currently in use, kindly return the book first before deleting it");
                            break;
                    }
                    member.setStatus(Status.INACTIVE);
                    memberService.update(member);
                    System.out.println("Member is Successfully Deleted");
                    break;
                }
                case 3: {
                    List<Member> members = memberService.findAll();
                    if (!members.isEmpty()) {
                        memberService.printMembers(members);
                    } else {
                        System.out.println("No Members Found");
                        break;
                    }
                    break;
                }
                case 4: {
                    List<Member> members = memberService.findAll().stream()
                            .filter(member -> member.getStatus().name().equals(Status.ACTIVE.name())).toList();
                    if (!members.isEmpty()) {
                        System.out.println("printing");
                        memberService.printMembers(members);
                    } else {
                        System.out.println("No Active Members Found");
                        break;
                    }
                    break;
                }
                case 5: {
                    System.out.println("Enter the ID of the Member");
                    int id = sc.nextInt();
                    sc.nextLine();
                    if (memberService.findById(id) == null) {
                        System.out.println("Invalid ID");
                        break;
                    }
                    memberService.printMember(memberService.findById(id));
                    break;
                }
                case 6: {
                    System.out.println("Enter the Username of the Member");
                    String username = sc.nextLine();
                    if (memberService.findByUsername(username) == null) {
                        System.out.println("Invalid Username");
                        break;
                    }
                    memberService.printMember(memberService.findByUsername(username));
                    break;
                }

                case 7: {
                    List<Member> members = memberService.findAll().stream().
                            filter(member -> member.getStatus().name().equals(Status.INACTIVE.name()))
                            .toList();
                    if (!members.isEmpty()) {
                        memberService.printMembers(members);
                    } else {
                        System.out.println("No Inactive Members Found");
                        break;
                    }
                    System.out.println("Enter the ID of the Member to make Active");
                    int id = sc.nextInt();
                    sc.nextLine();
                    Member member = memberService.findById(id);
                    if (member == null || member.getStatus().name().equals(Status.ACTIVE.name())) {
                        System.out.println("Invalid ID");
                        break;
                    }
                    member.setStatus(Status.ACTIVE);
                    memberService.update(member);
                    System.out.println("Member is Successfully Activated");
                }
                break;
                case 8: {
                    flag = 1;
                    break;
                }
                default:
                    System.out.println("Invalid Choice");
            }
        }catch(InputMismatchException e){
                System.out.println("Kindly Make Sure,ID are given Correctly and Try Again");
                sc.nextLine();

            }


        }


    }

        public void manageTransactions () {
        int flag = 0;
        while (flag == 0) {
            try {
                System.out.println("******** TRANSACTIONS  MANAGEMENT *******");
                System.out.println("1. All Transactions");
                System.out.println("2. Transactions of A Member");
                System.out.println("3. Transactions of Type");
                System.out.println("4. Transactions by Status");
                System.out.println("5. Find By ID");
                System.out.println("6. Find By Book ID");
                System.out.println("7. Exit");
                System.out.println("******************************************");
                System.out.print("Enter your Choice: ");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1: {
                        List<Transaction> transactions = transactionService.findAllTransactions();
                        if (!transactions.isEmpty()) {
                            transactionService.printTransactions(transactions);
                        } else {
                            System.out.println("No Transactions Found");
                        }
                        break;
                    }
                    case 2: {
                        List<Member> members = memberService.findAll();
                        if (!members.isEmpty()) {
                            memberService.printMembers(members);
                        } else {
                            System.out.println("No Members Found");
                            break;
                        }
                        System.out.println("Enter the ID of the Member");
                        int id = sc.nextInt();
                        sc.nextLine();
                        List<Transaction> transactions = transactionService.getMemberTransactions(id);
                        if (!transactions.isEmpty()) {
                            transactionService.printTransactions(transactions);
                        } else {
                            System.out.println("No Transactions Found");
                        }
                        break;
                    }
                    case 3: {
                        TransactionType type= TransactionType.BORROW;
                        System.out.println("Choose Type Of Transaction");
                        System.out.println("1. Borrow");
                        System.out.println("2. Return");
                        System.out.print("Enter the Option: ");
                        int op = sc.nextInt();
                        sc.nextLine();
                        if (op==2){
                            type =  TransactionType.RETURN;
                        }
                        else if(op==1){
                             type =  TransactionType.BORROW;
                        }
                        else {
                            System.out.println("Invalid ID");
                            break;
                        }

                        List<Transaction> transactions = transactionService.getTransactionsByType(type);
                        if (!transactions.isEmpty()) {
                            transactionService.printTransactions(transactions);
                        } else {
                            System.out.println("No " + type + " Transactions Found");
                        }
                        break;
                    }
                    case 4: {
                        System.out.println("Enter the Status of the Transaction (Active/Completed))");
                        System.out.println("1. ACTIVE");
                        System.out.println("2. COMPLETED");
                        System.out.print("Enter the Option: ");
                        int op1 = sc.nextInt();
                        Status status;
                        if (op1 == 2) {
                            status = Status.COMPLETED;
                        } else if (op1 == 1) {
                            status = Status.ACTIVE;
                        } else {
                            status = Status.ACTIVE;
                            System.out.println("Invalid Option");
                            break;
                        }
                            TransactionType type = TransactionType.BORROW;
                            System.out.println("Choose Type Of Transaction");
                            System.out.println("1. Borrow");
                            System.out.println("2. Return");
                            System.out.print("Enter the Option: ");
                            int op = sc.nextInt();
                            sc.nextLine();
                            if (op == 2) {
                                type = TransactionType.RETURN;
                            } else if (op == 1) {
                                type = TransactionType.BORROW;
                            } else {
                                System.out.println("Invalid Option");
                                break;
                            }
                            List<Transaction> transactions = transactionService.getTransactionsByType(type).stream()
                                    .filter(transaction -> transaction.getStatus() == status).toList();
                            if (!transactions.isEmpty()) {
                                transactionService.printTransactions(transactions);
                            } else {
                                System.out.println("No " + status + " Transactions Found Of Type " + type);
                            }
                            break;

                        }

                    case 5: {
                        System.out.println("Enter the ID of the Transaction");
                        int id = sc.nextInt();
                        sc.nextLine();
                        Transaction transaction = transactionService.findById(id);
                        if (transaction == null) {
                            System.out.println("Invalid ID");
                            break;
                        }
                        transactionService.printTransactions(transactionService.findById(id));
                        break;
                    }
                    case 6: {
                        List<Book> books = bookService.findAll();
                        if (!books.isEmpty()) {
                            bookService.printBooks(books);
                        } else {
                            System.out.println("No Books Found");
                            break;
                        }
                        System.out.println("Enter the Book ID to get All the Transaction Consisting of that Book");
                        int bookid= sc.nextInt();
                        sc.nextLine();
                        List<Transaction> transactions = transactionService.getbyBookId(bookid);
                        if (!transactions.isEmpty()) {
                            transactionService.printTransactions(transactions);
                        }else{
                            System.out.println("No Transactions Found");
                        }
                        break;
                    }
                    case 7: {
                        flag = 1;
                        break;
                    }
                    default:
                        System.out.println("Invalid Choice");


                }

            }catch(InputMismatchException e){
                System.out.println("Kindly Make Sure,ID are given Correctly and Try Again");
                sc.nextLine();
            }
        }
    }


}



