package org.lms;

import org.lms.controllers.LibrarianController;
import org.lms.controllers.MembersController;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int flag = 0;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LibraryPU");
        EntityManager em = emf.createEntityManager();
        LibrarianController librarianController = new LibrarianController(sc, em);
        MembersController MembersController = new MembersController(sc, em);

        while (flag == 0) {
            System.out.println("*****************************************");
            System.out.println("Welcome to Library Management System");
            System.out.println("1.Login As Librarian");
            System.out.println("2.Login As Member");
            System.out.println("3.Exit the System");
            System.out.println("*****************************************");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    librarianController.MainMenu();
                    break;
                case 2:
                    MembersController.MainMenu();
                    break;
                case 3:
                    flag = 1;
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}