/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task1;

import java.sql.*;
import java.util.ArrayList;
import static a5_task1.A5_Task1.*;
import java.util.Arrays;
import java.util.regex.Pattern;
/**
 * This class is where all the database operations happen, used to connect to the Derby using an embedded mode execute the query and return the result set,
 * derbyConnectAndCreate() is used to create the Quiz Table and add the data from the csv text file,
 * printSQLException() is used to print details of an SQLException chain, Details included are SQL State, Error code, Exception message,
 * runQuery() is used to run the query on the Quiz table, 
 * closeTheResources() is used to close the statement and the connection to the Derby database.
 * @author VinayaSaiD
 */
public class EmbeddedDerby
{
    public static String framework = "embedded";
    public static String protocol = "jdbc:derby:";

    public void derbyConnectAndCreate(ArrayList<String> textFileList)
    {
        System.out.println("SimpleApp starting in " + framework + " mode");

        String dbName = "ExamDB"; // the name of the database
        String url = protocol + dbName + ";create=true";
        String username = "admin12";
        String password = "admin1234";
        try
        {
            conn = DriverManager.getConnection (url, username, password);
            s = conn.createStatement ();
            System.out.println("Connected to database " + dbName);
            conn.setAutoCommit(false);
            
            DatabaseMetaData dbm = conn.getMetaData();
            // check if "Quiz" table is there or not, if not there create the table if it is there do not recreate it again.
            ResultSet tables = dbm.getTables(null, null, "QUIZ", null);
            if (!tables.next()) 
            {
                s.executeUpdate("create table Quiz(questionNumber int,description varchar(500),choice1 varchar(500),choice2 varchar(500),choice3 varchar(500),choice4 varchar(500),Answer varchar(40))");
                System.out.println("Created table Quiz");
                 // add rows to the created table using a PreparedStatement 
                PreparedStatement psInsert = conn.prepareStatement("insert into Quiz values (?, ?, ?, ?, ?, ?, ?)");
                String eachline0;
                String[] tempArray;
                
                for (int i=0;i< (textFileList.size());i++)
                {
                    eachline0 = textFileList.get(i);
                    ArrayList<String> questionBits = new ArrayList<String>();
                    String kept = eachline0.substring( 0, eachline0.indexOf(","));
                    String eachline = eachline0.substring(eachline0.indexOf(",")+1);
                    // This to to get the question
                    tempArray = eachline.split(Pattern.quote(",a."));
                    questionBits.add(tempArray[0]);
                    // This to to get the choice 1
                    tempArray = tempArray[1].split(Pattern.quote(",b."));
                    questionBits.add(tempArray[0]);
                    // This to to get the choice 2
                    tempArray = tempArray[1].split(Pattern.quote(",c."));
                    questionBits.add(tempArray[0]);
                    // This to to get the choice 3
                    tempArray = tempArray[1].split(Pattern.quote(",d."));
                    questionBits.add(tempArray[0]);
                    // This to to get the choice 4
                    int leftStringLength = tempArray[1].length();
                    String lastOption = tempArray[1].substring(0, (leftStringLength-2));
                    // This to to get the correct answer
                    questionBits.add(lastOption);
                    String answer = tempArray[1].substring((leftStringLength-1), (leftStringLength));
                    questionBits.add(answer);
                    // This is to get the question number from the text file.
                    // And then inserting the values into the prepared statement
                    psInsert.setInt(1, Integer.parseInt(kept));
                    psInsert.setString(2, questionBits.get(0));
                    psInsert.setString(3, questionBits.get(1));
                    psInsert.setString(4, questionBits.get(2));
                    psInsert.setString(5, questionBits.get(3));
                    psInsert.setString(6, questionBits.get(4));
                    psInsert.setString(7, questionBits.get(5));
                    // executing the query to write to the table
                    psInsert.executeUpdate();
                }
                System.out.println("Inserted all Questions from Text File into the Quiz Table.\n ");
                // Commiting the changes into the database.
                conn.commit();
                System.out.println("Committed the transaction");
                // this is to close the preared statement that was created
                try 
                {   if (psInsert != null) {
                        psInsert.close();
                        psInsert = null;
                    }
                } 
                catch (SQLException sqle) 
                {   printSQLException(sqle);
                }
                }
            else
            {   System.out.println("Table already exists");
            }
        }
        catch (SQLException sqle)
        {   printSQLException(sqle);
        } 
    }

    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }
    
    public static ResultSet runQuery(String query)
    {   ResultSet rs = null;
        try
        {   // running the query that is sent on the Quiz Table
            rs = s.executeQuery(query);
            return rs;
        }
        catch (SQLException sqle)
        {   printSQLException(sqle);
        } 
        return rs;
    }
    public void closeTheResources()
    {
        try
        {   DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (SQLException se)
        {   if (( (se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState()) ))) 
            {   // we got the expected exception
                System.out.println("Derby shut down normally");
            } 
            else 
            {   // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                printSQLException(se);
            }
        }
        // This is to close the statement created at the start
        try 
        {   if (s != null) {
                s.close();
                s = null;
            }
        } 
        catch (SQLException sqle) 
        {   printSQLException(sqle);
        }
    }
}