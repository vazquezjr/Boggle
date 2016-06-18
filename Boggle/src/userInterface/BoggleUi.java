/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import core.Board;
import core.Die;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants.CharacterConstants;

/**
 *
 * @author RV
 */
public class BoggleUi {
    JFrame frame; // Declares all of the components needed to build the user interface
    JMenuBar menuBar;
    JMenu boggle;
    JMenuItem newGame;
    JMenuItem exit;
    
    JPanel diceBoard;
    JPanel contentPane1;
    JPanel contentPane2;
    
    JTextArea userWords;
    JTextPane userVsComp;
    JScrollPane wordScroll;
    JViewport userWordsView; // Viewport that contains the words found by the user
    JViewport userVsCompView; // Viewport that contains the words found by the computer
    
    JLabel time;
    JLabel currentWord;
    JLabel currentScore;
    
    JButton shake;
    JButton submitWord;
    
    ArrayList dictionary; // An ArrayList that contains the temporary dictionary for the valid words
    ArrayList wordsFound; // Stores the words that the user has found
    ArrayList<JButton> dice; // An ArrayList that contains 16 buttons
    ArrayList<Die> die; // An ArrayList that contains 16 Die objects
    ArrayList<String> computerWords; // Stores the words found by the computer
    ArrayList<Integer> wordsPrinted; // Keeps track of which words have been printed to the userVsComp JTextPane
    
    int stopThread = 0; // Determines whether the timer should stop running
    int[] diceUsed = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Keeps track which dice have been used on the board
    int score = 0; // Stores the current score
    String word = ""; // Stores the current word
    
    public BoggleUi (Board board, ArrayList arrayList)
    {
        die = board.shakeDice(); // Stores the ArrayList of Die objects in ArrayList die
        dictionary = arrayList; // Stores the temporary dictionary in the ArrayList dictionary
        computerWords = new ArrayList<>();
        wordsPrinted = new ArrayList<>();
        
        initComponents(); // Sets up all of the components of the interface
        
        frame.setVisible(true); // Sets the window to display when the code is run
    }
    
    private void initComponents ()
    {
        frame = new JFrame(); // Block of code that sets up the frame
        frame.setLayout(new BorderLayout());
        frame.setTitle("Boggle");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(660, 660));
        frame.setMinimumSize(new Dimension(660, 500));
        frame.setMaximumSize(new Dimension(700, 700));
        
        setMenuBar(); // This block of code calls methods to set up different portions of the user interface
        setDiceBoard();
        setContentPane();
        setWordPane();
        
        wordsFound = new ArrayList();
        
        frame.setJMenuBar(menuBar); // Block of code that adds the menubar, dice board, and both content panes to the Frame
        frame.add(diceBoard, BorderLayout.LINE_START);
        frame.add(contentPane1, BorderLayout.CENTER);
        frame.add(contentPane2, BorderLayout.PAGE_END);
    }
    
    private void setMenuBar()
    {
        menuBar = new JMenuBar(); // Creates an instance of JMenuBar
        boggle = new JMenu("Boggle"); // Creates an instance of JMenu
        
        newGame = new JMenuItem("New Game"); // Creates a new JMenuItem "New Game"
        newGame.addActionListener(new NewGameListener()); // Adds an ActionListener to the "New Game" JMenuItem
        
        exit = new JMenuItem("Exit"); // Creates a new JMenuItem "Exit"
        exit.addActionListener(new ExitListener()); // Adds an ActionListener to the "Exit" JMenuItem
        
        boggle.add(newGame); // Block of code that adds the menu items to the menu
        boggle.add(exit);
        
        menuBar.add(boggle); // Adds the menu to the menubar
    }
    
    private void setDiceBoard()
    {
        diceBoard = new JPanel(new GridLayout(4, 4)); // Sets up the board where the dice are displayed
        diceBoard.setBorder(new TitledBorder("Boggle Board"));
        
        dice = new ArrayList<>(); // Initializes the ArrayList containing the JButtons
        
        for (int i = 0; i < 16; i++)
        {
            dice.add(new JButton(die.get(i).getLetter())); // Adds the letter to the button
            dice.get(i).setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 50)); // Sets the font of the JButton contents
            dice.get(i).setPreferredSize(new Dimension(100, 100)); // Sets the size of the JButton
            dice.get(i).addActionListener(new DiceListener()); // Adds an ActionListener to the JButton
            dice.get(i).setEnabled(false); // Disables the JButton
            diceBoard.add(dice.get(i)); // Adds the dice to the JPanel
        }
    }
    
    private void setContentPane() 
    {
        contentPane1 = new JPanel();                                              // Sets up the JPanel containing a 
        contentPane1.setLayout(new BoxLayout(contentPane1, BoxLayout.PAGE_AXIS)); // text area to type words, a timer, and                                                                 // a button to shake the board.
                                                                                  // a button to shake the dice on the board
        
        userWords = new JTextArea();  // Block of code that sets up the JTextArea
        userWords.setWrapStyleWord(true);
        userWords.setLineWrap(true);
        userWords.setEditable(false);
        
        userVsComp = new JTextPane();  // Block of code that sets up the JTextPane
        userVsComp.setDocument(userVsComp.getStyledDocument());
        userVsComp.setEditable(false);
        
        userWordsView = new JViewport();  // Block of code that sets up the viewports that will be set on the JScrollPane
        userWordsView.setView(userWords);
        userVsCompView = new JViewport();
        userVsCompView.setView(userVsComp);
        
        wordScroll = new JScrollPane(wordScroll); // Block of code that sets up the JScrollPane
        wordScroll.setViewport(userWordsView);
        wordScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        wordScroll.setMaximumSize(new Dimension(405, 300));
        
        time = new JLabel("3:00"); // Block of code that sets up the timer
        time.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 50));
        time.setHorizontalAlignment(JLabel.CENTER);
        time.setMaximumSize(new Dimension(300, 100));
        time.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7), BorderFactory.createTitledBorder("Time Left")));
        time.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        time.addPropertyChangeListener(new TimeListener());
        
        shake = new JButton(); // Block of code that sets up the shake button
        shake.setMaximumSize(new Dimension(100, 50));
        shake.setAlignmentX(JButton.LEFT_ALIGNMENT);
        shake.setText("Shake Dice");
        shake.addActionListener(new ShakeListener());
        
        contentPane1.setBorder(BorderFactory.createTitledBorder("Enter Words Found")); // Block of code that adds the text area, timer, and shake button to the content pane
        contentPane1.add(wordScroll);
        contentPane1.add(time);
        contentPane1.add(shake);
    }
    
    private void setWordPane()
    {
        contentPane2 = new JPanel(); // Block of code that sets up the JPanel containing the current word label, submit button, and current score label
        contentPane2.setLayout(new BoxLayout(contentPane2, BoxLayout.LINE_AXIS));
        contentPane2.setBorder(BorderFactory.createTitledBorder("Current Word"));
        contentPane2.setPreferredSize(new Dimension(400, 100));
        
        currentWord = new JLabel(); // Block of code that sets up the current word label
        currentWord.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentWord.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        currentWord.setHorizontalAlignment(JLabel.CENTER);
        currentWord.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 50));
        currentWord.setMaximumSize(new Dimension(400, 300));
        
        submitWord = new JButton("Submit Word"); // Block of code that sets up the submit word button 
        submitWord.setAlignmentX(JButton.CENTER_ALIGNMENT);
        submitWord.setMaximumSize(new Dimension(200, 50));
        submitWord.addActionListener(new SubmitListener());
        submitWord.setEnabled(false);
      
        currentScore = new JLabel("0"); // Block of code that sets up the current score label
        currentScore.setBorder(BorderFactory.createTitledBorder("Current Score"));
        currentScore.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        currentScore.setHorizontalAlignment(JLabel.CENTER);
        currentScore.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 50));
        currentScore.setMaximumSize(new Dimension(150, 300));
        
        contentPane2.add(currentWord); // Block of code that adds the current word label, submit word button, and current score label to the JPanel
        contentPane2.add(submitWord);
        contentPane2.add(currentScore);
    }
    
    private void compareWords() throws BadLocationException
    {
        Random random = new Random(); // Used to generate random integer values
        int computerWordsFound = random.nextInt(wordsFound.size() + 1); // Determines how many words were found by the computer   
        
        String userWordsArea[] = userWords.getText().split("\n"); // Stores all of the words found by the user in a String array
        
        SimpleAttributeSet strike = new SimpleAttributeSet(); // Stores information for the strikethrough attribute
        strike.addAttribute(CharacterConstants.StrikeThrough, TextAttribute.STRIKETHROUGH_ON); // Adds the strikethrough attribute to this instance
                                                                                               // of the SimpleAttributeSet class
        
        for (int i = 0; i < computerWordsFound; i++) // Loop that picks unique words found by the computer and stores it in an ArrayList
        {
            computerWords.add(wordsFound.get(random.nextInt(wordsFound.size())).toString());
            if (i > 0)
                for (int j = 0; j < i; j++)
                    if (computerWords.get(i).equals(computerWords.get(j)))
                    {
                        computerWords.remove(computerWords.get(i));
                        i--;
                        break;
                    }
        }
        
        // This block of code notifies the user that their words are being compared to the computer
        // and displays the words found by the computer
        userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), "The computer is now comparing its words to the player's words\n\n", null);
        userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), "These are the words that the computer found:\n", null);
        if (computerWordsFound == 0)
                userVsComp.getStyledDocument().insertString(userVsComp.getStyledDocument().getLength(), "none\n", null);
        for (int i = 0; i < computerWordsFound; i++)
                userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), computerWords.get(i) + "\n", null);
        userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), "\n", null);
        userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), "Only the following words without a strikethrough are counted towards the score:\n", null);
        
        wordsFound.stream().forEach((_item) -> { // Sets up the ArrayList that keeps track of the words printed to the userVsComp JTextPane
            wordsPrinted.add(0);
        });
        
        for (int i = 0; i < computerWordsFound; i++) // Loop that prints words with or without strikethroughs depending on whether the word was found
        {                                            // by both the user and the computer or by just the user, respectively
            for(int j = 0; j < wordsFound.size(); j++)
                if (computerWords.get(i).equals(userWordsArea[j]))
                {   // Prints words with strikethroughs
                    userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), computerWords.get(i), strike);
                    userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), "\n", null);
                    
                    for (int k = 0; k < wordsFound.size(); k++)
                        if (computerWords.get(i).equals(userWordsArea[k]))
                        {   
                            wordsPrinted.set(k, 1);
                            break;
                        }
                    break;
                }
        }
        
        for (int l = 0; l < wordsFound.size(); l++)
            if (wordsPrinted.get(l) == 0)
                {   // Prints words without strikethroughs
                    userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), userWordsArea[l], null);
                    userVsComp.getDocument().insertString(userVsComp.getStyledDocument().getLength(), "\n", null);
                }
        
        adjustScore(computerWords); // Invokes method which adjusts the score based on the words unique to the user        
                
        wordsPrinted.clear(); // Resets the ArrayList for a new game
        strike.removeAttribute(CharacterConstants.StrikeThrough); // Resets the instance of SimpleAttributeSet for a new game
        
        wordScroll.setViewport(userVsCompView); // Sets the JScrollPane to show the JTextPane showing the comparison of the user's words
                                                // versus the computer's words
    }
    
    private void adjustScore(ArrayList<String> comWords)
    {
        comWords.stream().forEach((comWord) -> { // Loop that subtracts points from the score based on the words found by the computer
            if ((comWord.length() == 3) || (comWord.length() == 4)) {
                score -= 1;
            } else if (comWord.length() == 5) {
                score -= 2;
            } else if (comWord.length() == 6) {
                score -= 3;
            } else if (comWord.length() == 7) {
                score -= 5;
            } else {
                score -= 11;
            }
        });
        currentScore.setText(score + ""); // Adjusts the score based on the words found by only the user
    }
    
    private class ExitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int response = JOptionPane.showConfirmDialog(null, "Confirm to exit Boggle?", // Asks the user if they want to exit Boggle
                    "Exit?", JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION)
                System.exit(0); // Exits the application
        }	
    }
    
    private class NewGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            wordScroll.setViewport(userWordsView); // Resets the viewport to the display the words entered by the user
            userWords.setText(""); // Clears the the words found area
            
            try { // Blaock of code that clears the JTextPane
                userVsComp.getStyledDocument().remove(0, userVsComp.getStyledDocument().getLength());
            } catch (BadLocationException ex) {
                Logger.getLogger(BoggleUi.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            wordsFound.clear(); // Block of code that clears the ArrayLists of words found by user and computer
            computerWords.clear();
            
            if (!(time.getText().equals("0:00"))) // Stops the timer
                stopThread = 1;
            
            time.setText("3:00"); // Resets the time
            
            shake.setEnabled(true); // Enables the shake button
            submitWord.setEnabled(false); // Disables the submit word button
            
            currentWord.setText(null); // Clears the currentWord label
            word = ""; // clears the current word
            
            currentScore.setText("0"); // Resets the score
            
            for (int i = 0; i < 16; i++) // Disables all of the Boggle dice
                dice.get(i).setEnabled(false);
        }
    }
    
    private class ShakeListener implements ActionListener
    {   
        @Override
        public void actionPerformed(ActionEvent e)
        {   
            Collections.shuffle(die); // Shuffles the Boggle Dice
            
            for (int i = 0; i < 16; i++) // Enables the JButtons and "shakes" the dice board
            {
                dice.get(i).setEnabled(true);
                dice.get(i).setText(die.get(i).getLetter());
            }
            
            currentScore.setText("0"); // Resets the score
            currentWord.setText(null); // Clears the current word label
            word = ""; // Clears the word
            time.setText("3:00"); // Resets the timer
            
            shake.setEnabled(false); // Disables the shake button
            submitWord.setEnabled(true); // Enables the submit word button
            score = 0; // Clears the score
            wordsFound.clear(); // Clears out the words that the user has found previously
            
            new Timer().start();
        }
    }
    
    private class DiceListener implements ActionListener
    {   
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int i;
            word = word + e.getActionCommand(); // Appends the letter to the end of the word
            currentWord.setText(word); // Updates the current word
      
            for (i = 0; i < 16; i++) // Determines which die was pressed
                if (e.getSource() == dice.get(i))
                {
                    diceUsed[i] = 1;
                    dice.get(i).setEnabled(false);
                    break;
                }
            
            //for (int i = 0; i < 16; i++)
            { // This block of code enables and disables certain dice depending on which button was pressed
                if (i == 0)
                {
                    if (diceUsed[i + 1] == 0)
                        dice.get(i + 1).setEnabled(true);
                    if (diceUsed[i + 4] == 0)
                        dice.get(i + 4).setEnabled(true);
                    if (diceUsed[i + 5] == 0)
                        dice.get(i + 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i + 1)) && (j != (i + 4)) && (j != (i + 5)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 3)
                {
                    if (diceUsed[i - 1] == 0)
                        dice.get(i - 1).setEnabled(true);
                    if (diceUsed[i + 3] == 0)
                        dice.get(i + 3).setEnabled(true);
                    if (diceUsed[i + 4] == 0)
                        dice.get(i + 4).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i - 1)) && (j != (i + 4)) && (j != (i + 3)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 12)
                {
                    if (diceUsed[i + 1] == 0)
                        dice.get(i + 1).setEnabled(true);
                    if (diceUsed[i - 4] == 0)
                        dice.get(i - 4).setEnabled(true);
                    if (diceUsed[i - 3] == 0)
                        dice.get(i - 3).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i + 1)) && (j != (i - 4)) && (j != (i - 3)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 15)
                {
                    if (diceUsed[i - 1] == 0)
                        dice.get(i - 1).setEnabled(true);
                    if (diceUsed[i - 4] == 0)
                        dice.get(i - 4).setEnabled(true);
                    if (diceUsed[i - 5] == 0)
                        dice.get(i - 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i - 1)) && (j != (i - 4)) && (j != (i - 5)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 4 || i == 8)
                {
                    if (diceUsed[i - 3] == 0)
                        dice.get(i - 3).setEnabled(true);
                    if (diceUsed[i - 4] == 0)
                        dice.get(i - 4).setEnabled(true);
                    if (diceUsed[i + 1] == 0)
                        dice.get(i + 1).setEnabled(true);
                    if (diceUsed[i + 4] == 0)
                        dice.get(i + 4).setEnabled(true);
                    if (diceUsed[i + 5] == 0)
                        dice.get(i + 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i - 3)) && (j != (i - 4)) && (j != (i + 1)) && (j != (i + 4)) && (j != (i + 5)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 7 || i == 11)
                {
                    if (diceUsed[i + 3] == 0)
                        dice.get(i + 3).setEnabled(true);
                    if (diceUsed[i - 4] == 0)
                        dice.get(i - 4).setEnabled(true);
                    if (diceUsed[i - 1] == 0)
                        dice.get(i - 1).setEnabled(true);
                    if (diceUsed[i + 4] == 0)
                        dice.get(i + 4).setEnabled(true);
                    if (diceUsed[i - 5] == 0)
                        dice.get(i - 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i + 3)) && (j != (i - 4)) && (j != (i - 1)) && (j != (i + 4)) && (j != (i - 5)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 1 || i == 2)
                {
                    if (diceUsed[i + 3] == 0)
                        dice.get(i + 3).setEnabled(true);
                    if (diceUsed[i - 1] == 0)
                        dice.get(i - 1).setEnabled(true);
                    if (diceUsed[i + 1] == 0)
                        dice.get(i + 1).setEnabled(true);
                    if (diceUsed[i + 4] == 0)
                        dice.get(i + 4).setEnabled(true);
                    if (diceUsed[i + 5] == 0)
                        dice.get(i + 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i + 3)) && (j != (i - 1)) && (j != (i + 1)) && (j != (i + 4)) && (j != (i + 5)))
                            dice.get(j).setEnabled(false);
                }
                
                else if (i == 13 || i == 14)
                {
                    if (diceUsed[i - 3] == 0)
                        dice.get(i - 3).setEnabled(true);
                    if (diceUsed[i - 1] == 0)
                        dice.get(i - 1).setEnabled(true);
                    if (diceUsed[i + 1] == 0)
                        dice.get(i + 1).setEnabled(true);
                    if (diceUsed[i - 4] == 0)
                        dice.get(i - 4).setEnabled(true);
                    if (diceUsed[i - 5] == 0)
                        dice.get(i - 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i - 3)) && (j != (i - 1)) && (j != (i + 1)) && (j != (i - 4)) && (j != (i - 5)))
                            dice.get(j).setEnabled(false);
                }
                
                else
                {
                    if (diceUsed[i - 5] == 0)
                        dice.get(i - 5).setEnabled(true);
                    if (diceUsed[i - 4] == 0)
                        dice.get(i - 4).setEnabled(true);
                    if (diceUsed[i - 3] == 0)
                        dice.get(i - 3).setEnabled(true);
                    if (diceUsed[i - 1] == 0)
                        dice.get(i - 1).setEnabled(true);
                    if (diceUsed[i + 1] == 0)
                        dice.get(i + 1).setEnabled(true);
                    if (diceUsed[i + 4] == 0)
                        dice.get(i + 4).setEnabled(true);
                    if (diceUsed[i + 3] == 0)
                        dice.get(i + 3).setEnabled(true);
                    if (diceUsed[i + 5] == 0)
                        dice.get(i + 5).setEnabled(true);
                    for (int j = 0; j < 16; j++)
                        if ((j != (i - 5)) && (j != (i - 4)) && (j != (i - 3)) && (j != (i - 1)) && (j != (i + 1)) && (j != (i + 3)) && (j != (i + 4)) && (j != (i + 5)))
                            dice.get(j).setEnabled(false);
                }
            }
                    
        }
    }
    
    private class SubmitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int uniqueWord = 1;
            
            for (Object wordsFound1 : wordsFound) { // Determines if the user found a new word (i.e. unique)
                if (currentWord.getText().toLowerCase().equals(wordsFound1.toString().toLowerCase())) {
                    JOptionPane.showMessageDialog(null, "You typed a duplicate word.", "Duplicate", JOptionPane.INFORMATION_MESSAGE); // Notifies the user that they have entered a duplicate word
                    uniqueWord = 0;
                    break;
                }
            }
            
            for (Object dictionary1 : dictionary) { // Determines if the word is valid and unique
                if ((currentWord.getText().toLowerCase().equals(dictionary1.toString().toLowerCase())) && uniqueWord == 1) {
                    if (currentWord.getText().length() >= 3) // Determines if the word contains three or more letters
                    { // These if statements increment the score label depending on the length of the word
                        if ((currentWord.getText().length() == 3) || (currentWord.getText().length() == 4)) 
                        {
                            userWords.append(word + "\n");
                            currentScore.setText((score += 1) + "");
                            wordsFound.add(word);
                            break;
                        }

                        else if (currentWord.getText().length() == 5)
                        {
                            userWords.append(word + "\n");
                            currentScore.setText((score += 2) + "");
                            wordsFound.add(word);
                            break;
                        }

                        else if (currentWord.getText().length() == 6)
                        {
                            userWords.append(word + "\n");
                            currentScore.setText((score += 3) + "");
                            wordsFound.add(word);
                            break;
                        }

                        else if (currentWord.getText().length() == 7)
                        {
                            userWords.append(word + "\n");
                            currentScore.setText((score += 5) + "");
                            wordsFound.add(word);
                            break;
                        }

                        else
                        {
                            userWords.append(word + "\n");
                            currentScore.setText((score += 11) + "");
                            wordsFound.add(word);
                            break;
                        }
                    }
                }
            }
                         
            for (int i = 0; i < 16; i++) // Reenables all of dice to start a new word
            {
                dice.get(i).setEnabled(true);
                diceUsed[i] = 0;
            }
            
            currentWord.setText(""); // Clears the current word label
            word = ""; // Clears the word
        }
    }
    
    private class TimeListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {   
            if (time.getText().equals("0:00")) // Determines when the user's time is finished
            {
                currentWord.setText("Game Over"); // Block of code that displays "Game Over" message and disables all of the dice and the submit word button
                for (int i = 0; i < 16; i++)
                    dice.get(i).setEnabled(false);
                submitWord.setEnabled(false);
                
                try {
                    compareWords(); // Invokes method that compares the user's words to the computer's words
                } catch (BadLocationException ex) {
                    Logger.getLogger(BoggleUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private class Timer extends Thread
    {
        @Override
        public void run()
        {
            int imin = 3, isec1 = 0, isec2 = 1; // Sets up the start of the timer

            while ((imin + isec1 + isec2) != 0) // Keeps the timer running until "0:00"
            {
                if (stopThread == 1) // Stops the timer if game is reset before timer hits "0:00"
                    break;
                
                else if (isec1 == 0 && isec2 == 0) // The remaining statements change each variable to simulate a countdown timer
                {
                    imin -= 1;
                    isec2 = 9;
                    isec1 = 5;
                }
                
                else if (isec2 == 0)
                {
                    isec1 -= 1;
                    isec2 = 9;
                }
                
                else if (isec1 == -1)
                    isec1 = 5;
                
                else
                    isec2 -= 1;
                
                time.setText(imin + ":" + isec1 + isec2); // Updates the timer
                
                try {
                    Thread.sleep(1000); // Ceases execution for approximately one second
                } catch (InterruptedException ex) {
                    Logger.getLogger(BoggleUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            stopThread = 0;
        }
    }
}
    