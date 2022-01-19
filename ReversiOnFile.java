/**
 * ReversiOnFile is a subclass of Reversi, adding File I/O capabilities
 * for loading and saving game.
 *
 * I declare that the work here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: Sanjana Mahmood
 * Student ID  : 1155135437
 * Date        : 29.11.2020
 * 
 */


package reversi;

import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.io.*;

public class ReversiOnFile extends Reversi {
    
    public static final char UNICODE_BLACK_CIRCLE = '\u25CF';
    public static final char UNICODE_WHITE_CIRCLE = '\u25CB';
    public static final char UNICODE_WHITE_SMALL_SQUARE = '\u25AB';
    
    // constructor to give a new look to new subclass game
    public ReversiOnFile()
    {
        window.setTitle("ReversiOnFile");
        gameBoard.setBoardColor(Color.BLUE);
    }


    // method to load the states of the cells of the board and current player from text file to the java program
    public void loadBoard(String filename)
    {
        //prepare an empty board       
        for (int i=1;i<=8;i++){
            for (int j=1;j<=8;j++){
            pieces[i][j]=EMPTY;
            }
        }        
        //load board and current player from file in UTF-8 Charset encoding
        char[][] token=new char[10][10];
        try{   
            Scanner scanner=new Scanner(new File(filename));
            
            int i=1;
            while (scanner.hasNextLine()){
                String line=scanner.nextLine();
                Scanner sc=new Scanner(line);
                sc.useDelimiter("");                

                int j=1;
                while (sc.hasNext()){
                    token[i][j]=sc.findInLine(".").charAt(0);
                    j++;
                }
                i++;
            }
            
            
            if (token[9][1]==UNICODE_WHITE_CIRCLE)
                currentPlayer=WHITE;
            else
                currentPlayer=BLACK;
            
            for (int row=1;row<=8;row++){
                for (int col=1;col<=8;col++){
                    
                    if (token[row][col]==UNICODE_BLACK_CIRCLE)
                        pieces[row][col]=BLACK;
                    if (token[row][col]==UNICODE_WHITE_CIRCLE)
                        pieces[row][col]=WHITE;
                    if (token[row][col]==UNICODE_WHITE_SMALL_SQUARE)
                        pieces[row][col]=EMPTY;
                }   
            }
            
            gameBoard.addText("Loaded board from " + filename);
            System.out.println("Loaded board from " + filename);
            gameBoard.updateStatus(pieces, currentPlayer);
        }
        catch (FileNotFoundException | NullPointerException | ArrayIndexOutOfBoundsException e ){
            gameBoard.addText("Cannot load board from " + filename);
            System.out.println("Cannot load board from " + filename);
            setupBoardDefaultGame();
        }
    }
    
    //method called when invalid filename input by user, then sets the default board for playing
    public void setupBoardDefaultGame(){
        
        for(int i=1;i<=8;i++){
            for (int j=1;j<=8;j++){
            pieces[i][j]=EMPTY;
            }
        }
        
        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][4] = BLACK;
        pieces[5][5] = WHITE;
        
        currentPlayer=BLACK;  
        gameBoard.updateStatus(pieces, currentPlayer);
    
    }
 
    /*method called when user closes the gameboard window, which then asks the user to input 
      filename. If user enters a filename, the state of the gameboard cells and current player 
      is stored in the text file, else game is not saved. */
    public void saveBoard(String filename)
    {
        // 1) open/overwrite a file for writing text in UTF-8 Charset encoding
        PrintStream myNewFile=null;
        try{
            
            myNewFile=new PrintStream(filename,"UTF-8");
            for (int i=1;i<=8;i++){
                for (int j=1;j<=8;j++){
                
                    if (pieces[i][j]==BLACK)
                        myNewFile.print(UNICODE_BLACK_CIRCLE);
                    else if (pieces[i][j]==WHITE)
                        myNewFile.print(UNICODE_WHITE_CIRCLE);
                    else
                        myNewFile.print(UNICODE_WHITE_SMALL_SQUARE);
                }
                myNewFile.println();
            }
            
            if (currentPlayer==BLACK)
                myNewFile.print(UNICODE_BLACK_CIRCLE);
            else 
                myNewFile.print(UNICODE_WHITE_CIRCLE);
            
            gameBoard.addText("Saved board to " + filename);
            System.out.println("Saved board to " + filename);
            myNewFile.close();
        }
        
        catch(Exception ee){
            gameBoard.addText("Cannot save board to " + filename);
            System.out.println("Cannot save board to " + filename);
            
        }
    }
   
    /* method called when closing the gameboard window. It says "Goodbye!" on System.out.
    Lastly, before program termination, it displays saveBoard dialogue box for user to enter 
    the filename to save the states of the cells of the gameboard and currentPlayer in a text file. */
    @Override
    protected void sayGoodbye()
    {
        System.out.println("Goodbye!");
        String filename = JOptionPane.showInputDialog("Save board filename");
        saveBoard(filename);
    }
     
    // main() method, starting point of subclass ReversiOnFile
    public static void main(String[] args) {
        ReversiOnFile game = new ReversiOnFile();
         
       // comment or remove the following statements for real game play
       // game.setupDebugBoardEndGame();
       // game.saveBoard("game4.txt");
       // game.setupDebugBoardMidGame();
       // game.saveBoard("game8.txt");
       // end of sample/ debugging
        

       if (game instanceof Reversi)
           System.out.println("INSTANCE OF REVERSI");
       if (game instanceof ReversiOnFile)
                      System.out.print("FILEEE");


       
        //load board filename
        String filename = JOptionPane.showInputDialog("Load board filename");
        game.loadBoard(filename);
       
        
        //if the loaded board is in a state of double forced pass/end game, the following codes are executed.
        game.legalMoves=game.calculateAllPossibleValidMoves(game.currentPlayer, game.pieces);
        if (game.forcedPass(game.legalMoves)){
            
            game.gameBoard.addText("Forced Pass");
            game.currentPlayer=game.FLIP*game.currentPlayer;
            game.gameBoard.updateStatus(game.pieces,game.currentPlayer);
            
            game.legalMoves=game.calculateAllPossibleValidMoves(game.currentPlayer,game.pieces);

                if (game.forcedPass(game.legalMoves)){
                  game.gameBoard.addText("Double Forced Pass");
                  game.gameBoard.addText("End game!");
                  game.entered=true;
                }       
        }    
    }
}
