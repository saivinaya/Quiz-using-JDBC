/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  This class has readFile() method, which is used to read the questions saved in a csv text file and put it into a ArrayList
 * @author VinayaSaiD
 */
public class ReadFromFile {
    public static ArrayList<String> readFile(String name) throws Exception
    {
            InputStream s = ReadFromFile.class.getResourceAsStream("/Database/"+name);
            // Create a File instance
            Scanner read = new Scanner(s);
            read.useDelimiter(",");
            
            ArrayList<String> lines = new ArrayList<String>();
            String names;
            // Read data from a file
            while (read.hasNext()) {
              names = read.nextLine();
              lines.add(names);
            }
            // Close the file
            read.close();
            return lines;
    }
}
