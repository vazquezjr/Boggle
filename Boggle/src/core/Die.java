/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author RV
 */
public class Die {
    private final int NUMBER_OF_SIDES = 6; // Constant, number of sides on each individual die
    
    private final ArrayList diceSides = new ArrayList(); // Stores the letters of each die
    String currentLetter; // The letter that is currently facing up on the die
    
    public void randomLetter() { // Method to find out the current letter of the die
        Random random = new Random(); // Creates a new instance of class Random
        currentLetter = diceSides.get(random.nextInt(NUMBER_OF_SIDES)).toString(); /* Retrieves a random letter from diceSides
                                                                                      and stores it in currentLetter */
    }

    public String getLetter() { // Method that returns the current letter of the die
        this.randomLetter(); // Invokes the method randomLetter
        return currentLetter; // // returns the letter that is facing up on the die
    }
    
    public void addLetter(String face) { // Method that adds a letter to each side of the die
        diceSides.add(face);
    }
    
    public void displayAllLetters() { // Method that displays all of the sides of a die
        int i;
        
        for (i = 0; i < NUMBER_OF_SIDES; i++) {
            System.out.printf(diceSides.get(i).toString() + " "); /* Loop that prints the elements in diceSides, a.k.a the
                                                                     letters on each side of the die */
        }
    }
}
