/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Boggle;

// Import classes that are necessary for this file.
import java.util.ArrayList;
import inputOutput.*;
import core.*;
import userInterface.BoggleUi;
/**
 *
 * @author RV
 */
public class Boggle {

    private static ArrayList dataFileIn = new ArrayList(); // Stores all the letters for the dice
    private static ArrayList tempDiction = new ArrayList(); // Stores the valid words for the Boggle game
    private static final String fileName = "BoggleData.txt"; // Stores the file name for the Boggle dice
    private static final String dictionary = "TemporaryDictionary.txt"; // Stores the file name for the dictionary file
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ReadDataFile readData = new ReadDataFile(fileName); // Creates an instance of ReadDataFile and passes the file name to it
        readData.populateData(); // Calls method from ReadDataFile to scan fileName
        dataFileIn = readData.getData(); // Stores data from fileName and stores it in dataFileIn
        
        ReadDataFile readDiction = new ReadDataFile(dictionary); // Creates an instance of ReadDataFile and passes the dictionary fille name to it
        readDiction.populateData(); // Calls method from ReadDataFile to scan dictionary
        tempDiction = readDiction.getData(); // Stores the words from dictionary and stores it in tempDiction
     
        Board boggleBoard = new Board(dataFileIn); // Creates an instance of Board and passes the letter data to it
        
        BoggleUi uI = new BoggleUi(boggleBoard, tempDiction); // Creates an instance of BooggleUi and passes the board and dictionary to it
    }
    
}
