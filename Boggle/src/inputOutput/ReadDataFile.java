/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author RV
 */
public class ReadDataFile {
    private Scanner input; // Reads data from a file
    private final String fileName; // Stores the file name
    private final ArrayList fileData = new ArrayList(); // Stores the data scanned from the file
    
    public ReadDataFile(String file) { /* Constructor that creates an object ReadDataFiles and stores the file name into the 
                                          String file */
        this.fileName = file;
    }
    
    public void populateData() { // Method that scans the data from the file and stores it in the ArrayList
        try { // Code that may throw an exception
            URL fileAddress = getClass().getResource(fileName);
            // Creates an instance of class URL with the location of the file passed to it
            
            File data = new File(fileAddress.toURI());
            // Creates an instance of class File with the actual file passed to it
            
            input = new Scanner(data); // Instantiates the Scanner class
            
            while(input.hasNext()) // Determines whether the file still has data in it to be scanned
                fileData.add(input.next()); // Scans the data into the the ArrayList
            
        } catch (FileNotFoundException | URISyntaxException ex) { // Catches two exceptions
            Logger.getLogger(ReadDataFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList getData() { // Method that returns the data in the ArrayList
        return this.fileData;
    }
}
       
