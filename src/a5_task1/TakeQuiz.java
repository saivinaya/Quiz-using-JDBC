/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task1;
import static a5_task1.A5_Task1.conn;
import static a5_task1.A5_Task1.s;
import static a5_task1.EmbeddedDerby.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is where the actual quiz happens,
 * startTest() is the method where a random question is picked from the database,
 * printQuestion() is used to print the question selected and take the answer from the user and check if it is correct or not,
 * randBetween() is used to select a random question number.
 * @author VinayaSaiD
 */
public class TakeQuiz {
    public static ArrayList<Integer> startTest()
    {   ArrayList<ResultSet> resultSetArray = new ArrayList<ResultSet>();
        // First result set to know the number of questions in the database, the rest of the 3 are for 3 questions
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        //printTable();
        // to maintain all the previous questions selected not to repeat any questions in the same quiz
        ArrayList<Integer> selectedQuestions = new ArrayList<Integer>();
        // to maintain the answers user gave to the questions
        ArrayList<Integer> answersGiven = new ArrayList<Integer>();
        try
        {   int count = 0;
            conn.setAutoCommit(false);
            String query = "SELECT COUNT(*) FROM Quiz";
            rs = s.executeQuery(query);
            resultSetArray.add(rs);
            while (rs.next()) 
            {   count = rs.getInt(1);
            }
            //System.out.println(count);
            int randomQuestionNumber = 0;
            int randomQuestionNumber1 = 0;
            int randomQuestionNumber2 = 0;
            
            // selecting the first question and printing it and taking the answer
            randomQuestionNumber = randBetween(1,count);
            String query1 = "SELECT * FROM Quiz WHERE questionNumber = " + randomQuestionNumber ;
            rs1 = s.executeQuery(query1);
            resultSetArray.add(rs1);
            int result = printQuestion(rs1);
            answersGiven.add(result);
            selectedQuestions.add(randomQuestionNumber);
            
            // selecting the next questions and printing them and taking the answers from the user and seing that the questions do not repeat again
            boolean inSelected = true;
            while (inSelected == true)
            {   randomQuestionNumber1 = randBetween(1,count);
                inSelected = false;
                for(int km =0; km < selectedQuestions.size(); km++)
                {   if (randomQuestionNumber1 == selectedQuestions.get(km))
                    {   inSelected = true; 
                    }
                }
            }
            selectedQuestions.add(randomQuestionNumber1);
            String query2 = "SELECT * FROM Quiz WHERE questionNumber = " + randomQuestionNumber1 ;
            rs2 = s.executeQuery(query2);
            resultSetArray.add(rs2);
            result = printQuestion(rs2);
            answersGiven.add(result);
            
            // selecting the next questions and printing them and taking the answers from the user and seing that the questions do not repeat again
            boolean inSelected1 = true;
            while (inSelected1 == true)
            {   randomQuestionNumber2 = randBetween(1,count);
                inSelected1 = false;
                for(int km =0; km < selectedQuestions.size(); km++)
                {   if (randomQuestionNumber2 == selectedQuestions.get(km))
                    {   inSelected1 = true; 
                    }
                }
            }
            String query3 = "SELECT * FROM Quiz WHERE questionNumber = " + randomQuestionNumber2 ;
            rs3 = runQuery(query3);
            resultSetArray.add(rs3);
            result = printQuestion(rs3);
            answersGiven.add(result);
            selectedQuestions.add(randomQuestionNumber2);
            return answersGiven;
        }
        catch (SQLException sqle)
        {   printSQLException(sqle);
        } 
        finally 
        {   //deleting all the result sets opened
            int i = 0;
            while (!resultSetArray.isEmpty()) 
            {   try 
                {
                    ResultSet r = resultSetArray.remove(i);
                    if (r != null) 
                    {   r.close();
                        r = null;
                    }
                } 
                catch (SQLException sqle) 
                {   printSQLException(sqle);
                }
            }
        }
        return answersGiven;
    }
    
    public static int randBetween(int start, int end) 
    {   return start + (int)Math.round(Math.random() * (end - start));
    }
    
    public static int printQuestion(ResultSet res)
    {   String optionChoosen = "";
        Scanner input = new Scanner(System.in); 
        int correctAnswer = 0;
        try
        {   
            while (res.next()) 
            {   // extract the question and choice from the result set and printing the question on the console
                String question = res.getString("description");
                String choice1 = res.getString("choice1");
                String choice2 = res.getString("choice2");
                String choice3 = res.getString("choice3");
                String choice4 = res.getString("choice4");
                String answer = res.getString("Answer");
                System.out.println("\n\nQuestion");
                System.out.println(question);
                System.out.println("a. "+ choice1);
                System.out.println("b. "+ choice2);
                System.out.println("c. "+ choice3);
                System.out.println("d. "+ choice4);
               
            // taking the answer from the user and seeing to it that it is valid input
            boolean wronginput = true;
            while(wronginput == true)
            {   
                System.out.println("Please enter your Choice: ");
                optionChoosen = input.nextLine();
                optionChoosen = optionChoosen.toLowerCase();
                if ((optionChoosen.equals("a") || optionChoosen.equals("b") || optionChoosen.equals("c") || optionChoosen.equals("d")))
                {   wronginput = false;
                }
                else
                {   System.out.println("\nWrong input entered. Please enter a valid option (a, b, c or d). \n");
                }
            }
            // checking if the answer is correct or not
            if (optionChoosen.equals(answer))
            {
                correctAnswer = 1;
            }
            }
        }
        catch (SQLException sqle) 
        {   printSQLException(sqle);
        }
        return correctAnswer;
    }
    
    //Can be used to print the entire database set which can be used for testing
    /*
    public static void printTable()
    {
        try
        { 
        ResultSet res=null;
        res = s.executeQuery("SELECT * FROM Quiz");
        while (res.next()) 
            {   // extract the question and choice from the result set and printing the question on the console
                int questionNo = res.getInt("questionNumber");
                String question = res.getString("description");
                String choice1 = res.getString("choice1");
                String choice2 = res.getString("choice2");
                String choice3 = res.getString("choice3");
                String choice4 = res.getString("choice4");
                String answer = res.getString("Answer");
                System.out.println("\n\nQuestion " + questionNo);
                System.out.println(question);
                System.out.println("a. "+ choice1);
                System.out.println("b. "+ choice2);
                System.out.println("c. "+ choice3);
                System.out.println("d. "+ choice4);
                System.out.println("Answer. "+ answer);
            }
        }
        catch (SQLException sqle) 
        {   printSQLException(sqle);
        }
    }*/
}
