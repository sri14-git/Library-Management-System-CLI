package org.lms.controllers;

import org.lms.enums.TransactionType;
import org.lms.model.Book;
import org.lms.model.Member;
import org.lms.model.Transaction;
import org.lms.service.AuthService;
import org.lms.service.BookService;
import org.lms.service.MemberService;
import org.lms.service.TransactionService;

import javax.persistence.EntityManager;
import java.util.List;
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
        if (authService.authenticate(username, password).getUsername().equals("admin")) {
            while (flag == 0) {
                System.out.println("********Login Successful*******");
                System.out.println("1.Manage Books");
                System.out.println("2.Manage Members");
                System.out.println("3. Manage Transactions");
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
        } else {
            System.out.println("Login Failed");
        }

    }

    public void manageBooks() {
        int flag = 0;
        while (flag == 0) {
            System.out.println("1.Add Book");
            System.out.println("2.Update Book stocks");
            System.out.println("3.Delete Book");
            System.out.println("4.List Books");
            System.out.println("5.Exit");
            switch (sc.nextInt()) {
                case 1: {
                    System.out.println("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.println("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.println("Enter Publisher: ");
                    String publisher = sc.nextLine();
                    System.out.println("Enter Copies Available: ");
                    int quantity = sc.nextInt();
                    System.out.println("Enter the Genre");
                    String genre = sc.nextLine();
                    bookService.save(new Book(title,author,genre,quantity));
                    System.out.println("Book Added Successfully");
                    break;
                }
                case 2: {
                    List<Book> books = bookService.findAll();
                    System.out.println("Books");
                    for (Book book : books) {
                        System.out.println(book.toString());
                    }
                    System.out.print("Enter The ID Of The Book To Update Stock: ");
                    int id = sc.nextInt();
                    Book book = bookService.findById(id);
                    if (book == null) {
                        System.out.println("Invalid ID");
                    } else {
                        System.out.print("Enter the No Of Copies To Be Added: ");
                        int copies = sc.nextInt();
                        book.setCopiesAvailable(book.getCopiesAvailable() + copies);
                        book.setTotalCopies(book.getTotalCopies() + copies);
                        bookService.update(book);
                    }
                    break;
                }
                case 3: {
                    List<Book> books = bookService.findAll();
                    System.out.println("Books");
                    for (Book book : books) {
                        System.out.println(book.toString());
                    }
                    System.out.print("Enter The ID Book To Delete: ");
                    int id2 = sc.nextInt();
                    bookService.delete(id2);
                    System.out.println("Deleted Successfully");
                    break;
                }
                case 4: {
                    List<Book> books = bookService.findAll();
                    System.out.println("Books");
                    for (Book book : books) {
                        System.out.println(book.toString());
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
        }
    }

    public void manageMembers(){
        int flag=0;
        while(flag==0) {
        System.out.println("******** MEMBERS MANAGEMENT *******");
        System.out.println("1. Add Members");
        System.out.println("2. Delete Members");
        System.out.println("3. View All Members");
        System.out.println("4. Find By ID");
        System.out.println("5. Find By Username");
        System.out.println("6. Exit");
        System.out.print("Enter your Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
            switch (choice) {
                case 1:{
                    System.out.println("Enter Name of the Member");
                    String name = sc.nextLine();
                    System.out.println("Enter Username of the Member");
                    String username = sc.nextLine();
                    System.out.println("Enter password of the Member");
                    String password = sc.nextLine();
                    Member member = new Member(name,username,password);
                    memberService.save(member);
                    System.out.println("Member Created Successfully");
                    break;
                }
                case 2: {
                    List<Member> members = memberService.findAll();
                    for(Member member: members){
                        System.out.println(member.toString());
                    }
                    System.out.println("Enter The Id of the Member that is to be Deleted");
                    int id = sc.nextInt();
                    memberService.remove(id);
                    System.out.println("Member is Successfully Deleted");
                    break;
                }
                case 3: {
                    List<Member> members = memberService.findAll();
                    for(Member member: members){
                        System.out.println(member.toString());
                    }
                    break;
                }
                case 4: {
                    System.out.println("Enter the ID of the Member");
                    int id = sc.nextInt();
                    System.out.println(memberService.findById(id).toString());
                    break;
                }
                case 5: {
                    System.out.println("Enter the Username of the Member");
                    String username = sc.nextLine();
                    System.out.println(memberService.findByUsername(username).toString());
                    break;
                }

                case 6: {
                    flag = 1;
                    break;
                }
                default:
                    System.out.println("Invalid Choice");


            }


        }


    }

    public void manageTransactions(){
        int flag=0;
        while(flag==0) {
            System.out.println("******** TRANSACTIONS  MANAGEMENT *******");
            System.out.println("1. All Transactions");
            System.out.println("2. Transactions of A Member");
            System.out.println("3. Transactions of Type");
            System.out.println("4. Transactions by Status");
            System.out.println("5. Find By ID");
            System.out.println("6. Exit");
            System.out.println("******************************************");
            System.out.print("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:{
                    List<Transaction> transactions = transactionService.findAllTransactions();
                    System.out.println("| TransactionID | MemberName | BookID | title | quantity | Date | Type | ExpectedReturnDate | ActualReturnDate | Status |");
                    for(Transaction transaction: transactions){
                        System.out.println(transaction.toString());
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter the ID of the Member");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("| TransactionID | MemberName | BookID | title | quantity | Date | Type | ExpectedReturnDate | ActualReturnDate | Status |");
                    List<Transaction> transactions = transactionService.getMemberTransactions(id);
                    for(Transaction transaction: transactions){
                        System.out.println(transaction.toString());
                    }
                    break;
                }
                case 3: {

                    System.out.println("Enter the Type of the Transaction");
                    TransactionType type = sc.nextLine().toUpperCase().equals("BORROW")?TransactionType.BORROW:TransactionType.RETURN;
                    List<Transaction> transactions = transactionService.getTransactionsByType(type);
                    if (!transactions.isEmpty()) {
                        System.out.println("| TransactionID | MemberName | BookID | title | quantity | Date | Type | ExpectedReturnDate | ActualReturnDate | Status |");

                        for (Transaction transaction : transactions) {
                            System.out.println(transaction.toString());
                        }
                    }
                    else{
                        System.out.println("No"+type+"Transactions Found");
                    }
                    break;
                }
                case 4: {
                    System.out.println("Enter the Status of the Transaction (Active/Completed))");
                    String status = sc.nextLine().toUpperCase();
                    System.out.println("Enter the Type of the Transaction (Borrow/Return)");
                    TransactionType type = sc.nextLine().toUpperCase().equals("BORROW")?TransactionType.BORROW:TransactionType.RETURN;
                    List<Transaction> transactions = transactionService.getTransactionsByType(type).stream()
                            .filter(transaction -> transaction.getStatus().name().equals(status)).toList();
                    if (!transactions.isEmpty()) {
                        System.out.println("| TransactionID | MemberName | BookID | title | quantity | Date | Type | ExpectedReturnDate | ActualReturnDate | Status |");
                        for (Transaction transaction : transactions) {
                            System.out.println(transaction.toString());
                        }
                    }
                    else{
                        System.out.println("No "+status+"Transactions Found Of Type "+type);
                    }
                    break;

                }
                case 5: {
                    System.out.println("Enter the ID of the Transaction");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("| TransactionID | MemberName | BookID | title | quantity | Date | Type | ExpectedReturnDate | ActualReturnDate | Status |");
                    System.out.println(transactionService.findById(id).toString());
                    break;
                }
                case 6: {
                    flag = 1;
                    break;
                }
                default:
                    System.out.println("Invalid Choice");


            }

    }
}


}



