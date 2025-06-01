package org.lms.controllers;

import org.lms.enums.Status;
import org.lms.enums.TransactionType;
import org.lms.model.Book;
import org.lms.model.Member;
import org.lms.model.Transaction;
import org.lms.service.AuthService;
import org.lms.service.BookService;
import org.lms.service.TransactionService;

import javax.persistence.EntityManager;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MembersController {
    Scanner sc;
    private final AuthService authService;
    private final BookService bookService;
    private final TransactionService transactionService;

    public MembersController(Scanner sc, EntityManager em) {
        this.sc = sc;
        this.authService = new AuthService(em);
        this.bookService = new BookService(em);
        this.transactionService = new TransactionService(em);
    }

    public void MainMenu() {
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();
        int flag = 0;
        try {
            Member member = authService.authenticate(username, password);
            if (member == null) {
                System.out.println("Login Failed");
                return;
            }
            if (member.getPassword().equals(password)) {
                while (flag == 0) {
                    System.out.println("********Login Successful*******");
                    System.out.println("1.Borrow Books");
                    System.out.println("2.Return Books");
                    System.out.println("3.View My Transactions"); ///need to add available books
                    System.out.println("4.Exit");
                    System.out.println("********************************");
                    System.out.print("Enter your Choice: ");
                    switch (sc.nextInt()) {
                        case 1: { /// need to update times borrowed
                            List<Book> books = bookService.getAvailableBooks();
                            if (!books.isEmpty()) {
                                bookService.printBooks(books);
                            } else {
                                System.out.println("No Books Found");
                            }
                            System.out.print("Enter The ID Of The Book To Borrow: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            transactionService.borrowBook(member.getMemberId(), id);

                            break;
                        }
                        case 2: {
                            List<Transaction> transactions = transactionService.getMemberTransactionByType(member.getMemberId(), TransactionType.BORROW, Status.ACTIVE);
                            if (!transactions.isEmpty()) {
                                transactionService.printTransactions(transactions);
                            } else {
                                System.out.println("No Transactions Found");
                            }
                            System.out.print("Enter The ID Of The Transaction To Return: ");
                            int tid = sc.nextInt();
                            sc.nextLine();
                            transactionService.returnBook(member.getMemberId(), tid);
                            System.out.println("Thank you for returning");
                            break;
                        }
                        case 3: {
                            List<Transaction> transactions = transactionService.getMemberTransactions(member.getMemberId());
                            if (!transactions.isEmpty()) {
                                transactionService.printTransactions(transactions);
                            } else {
                                System.out.println("No Transactions Found");
                            }
                            break;
                        }
                        case 4: {
                            flag = 1;
                            break;
                        }
                        default: {
                            System.out.println("Invalid Choice");
                            break;
                        }
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Failed");
            System.out.println("Kindly Make Sure,ID are given Correctly and Try Again");
            sc.nextLine();
        }
        catch (Exception e) {
            System.out.println("Failed");
            System.out.println("Kindly Make Sure,ID are given Correctly and Try Again");
        }
    }


}
