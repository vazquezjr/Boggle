/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;

/**
 *
 * @author RV
 */
public class Board {
    
    private final int NUMBER_OF_DICE = 16; // Constant, number of dice used in the game
    private final int NUMBER_OF_SIDES = 6; // Constant, number of sides on each individual die
    private final int GRID = 4; // Constant, size of the board
    
    ArrayList diceData = new ArrayList(); // Stores the data from the scanned file
    ArrayList<Die> dice = new ArrayList(); // Stores individual Die objects
    
    public Board(ArrayList diceData) { // Constructor used to create an object of class Board
        this.diceData = diceData; // Stores the scanned data to the ArrayList
    }
    
    public void populateDice() { // Method that creates each of the 16 Die objects
        Die[] dieArray = new Die[NUMBER_OF_DICE]; // Creates an array of class Die
        
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            dieArray[i] = new Die(); // Creates a Die object
            for (int j = 0; j < NUMBER_OF_SIDES; j++) {
                dieArray[i].addLetter((diceData.get((6 * i) + j).toString())); /* This loop stores 6 letters in the ArrayList
                                                                                  of the Die object */
            }
            System.out.printf("Die " + (i + 1) + ": "); dieArray[i].displayAllLetters(); System.out.printf("\n");
            // Displays all the sides of the current Die object
            
            dice.add(dieArray[i]); // Stores the current Die object in an ArrayList containing Die objects
        }
        System.out.printf("\n");
    }
    
    public ArrayList shakeDice() { // Method that shakes all dice and shows each of their current letters on a 4 x 4 GRID
        populateDice(); // Calls method to create each die
        
        System.out.println("Boggle board");
        
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            System.out.printf(dice.get(i).getLetter() + " "); // Prints the current letter of the shaken die
            
            if ((i + 1) % GRID == 0) // Conditional statement used to print the letters in a 4 x 4 GRID
                System.out.printf("\n");
        }
        
        System.out.printf("\n");
        
        return dice; // Returns the ArrayList of Die objects
    }
}
        
        

    

