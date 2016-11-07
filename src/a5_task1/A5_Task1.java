/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task1;

import java.sql.*;
import java.util.ArrayList;
import static a5_task1.ReadFromFile.*;
import java.util.Scanner;

/**
 * This has the main() method and calls the readFile() method which will read from a csv text file,
 * then calls derbyConnectAndCreate() to connect to Derby, create a table and add the lines from text file into the table and
 * finally used to take the input from the user whether he want to take the quiz or not.
 * @author VinayaSaiD
 */
public class A5_Task1 {

    static ArrayList<String> linesFromTextFile = new ArrayList<String>();
    public static Connection conn;
    public static Statement s;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {   // to read the lines from the text file
        linesFromTextFile = readFile("Questions.txt");
        EmbeddedDerby database = new EmbeddedDerby();
        // Connect to the database, and store the questions into the Quiz Table
        database.derbyConnectAndCreate(linesFromTextFile);
        printMenu();
    }
    public static void printMenu()
    {
        boolean wronginput = true;
        String optionChoosen = "";
        Scanner input = new Scanner(System.in); 
        // Take the input from the User whether he wants to take the Quiz or not. And check the validity of the data enetered.
        while(wronginput == true)
        {
            System.out.println("Do you want to take the Quiz? (Y or N)");
            optionChoosen = input.nextLine();
            if ((optionChoosen.equals("y") || optionChoosen.equals("Y") || optionChoosen.equals("N") || optionChoosen.equals("n")))
            {   wronginput = false;
            }
            else
            {   System.out.println("\nWrong input entered. Please enter a valid option. \n");
            }
        }
        // changing the choice entered by user into upper case
        String option = optionChoosen.toUpperCase();
        switch (option)
        {   case "Y" :  
            {   ArrayList<Integer> answersQuiz = new ArrayList<Integer>();
                // Send the control to startTest where actual questions are given to the user
                answersQuiz = TakeQuiz.startTest();
                int correctAnswers =0;
                int wrongAnswers = 0;
                // print the result of the quiz taken
                System.out.println("\n\n\nResult\n");
                for (int ap=0 ;ap < answersQuiz.size() ;ap++)
                {   if (answersQuiz.get(ap) == 1)
                    {   correctAnswers = correctAnswers + 1;
                        System.out.println("Question "+ (ap+1) + " is Correct");
                    }
                    else
                    {   wrongAnswers = wrongAnswers +1 ;
                        System.out.println("Question "+ (ap+1) + " is Wrong");
                    }
                }
                System.out.println("\n\nTotal Correct Answers are "+ correctAnswers);
                System.out.println("Total Wrong Answers are "+ wrongAnswers);
                System.out.println("\n\n\n");
                printMenu();
            } 
            case "N" :
            {   // close the connections to the database and the statement opened 
                System.out.println("Closing the Connection to the database.");
                EmbeddedDerby db = new EmbeddedDerby();
                db.closeTheResources();
                System.exit(0);
                break;
            }
            default:
            {   // close the connections to the database and the statement opened 
                System.out.println("Closing the Connection to the database.");
                EmbeddedDerby db = new EmbeddedDerby();
                db.closeTheResources();
                System.exit(0);
            }
        }
    }
}
