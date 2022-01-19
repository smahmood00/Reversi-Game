/**
 * CSCI1130 Java Assignment
 * Reversi board game
 * 
 * Students shall complete this class to implement the game.
 * There are debugging, testing and demonstration code for your reference.
 * 
 * I declare that the assignment here submitted is original
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
 * Date        : 21.11.20
 * 
 * @author based on skeleton code provided by Michael FUNG
 */


package reversi;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Reversi {

    // pre-defined class constant fields used throughout this app
    public static final int BLACK = -1;
    public static final int WHITE = +1;
    public static final int EMPTY =  0;
    
    // a convenient constant field that can be used by students
    public final int FLIP  = -1;
    
    // GUI objects representing and displaying the game window and game board
    protected JFrame window;
    protected ReversiPanel gameBoard;
    protected Color boardColor = Color.GREEN;

    
    // a 2D array of pieces, each piece can be:
    //  0: EMPTY/ unoccupied/ out of bound
    // -1: BLACK
    // +1: WHITE
    protected int[][] pieces;
    
    
    // currentPlayer:
    // -1: BLACK
    // +1: WHITE
    protected int currentPlayer;
  
    /**
     * The only constructor for initializing a new board in this app
     */
    public Reversi() {
        window = new JFrame("Reversi");
        gameBoard = new ReversiPanel(this);
        window.add(gameBoard);
        window.setSize(850, 700);
        window.setLocation(100, 50);
        window.setVisible(true);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // use of implicitly extended inner class with overriden method, advanced stuff
        window.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    sayGoodbye();
                }
            }
        );


        // a 8x8 board of pieces[1-8][1-8] surrounded by an EMPTY boundary of 10x10 
        pieces = new int[10][10];
        
        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][4] = BLACK;
        pieces[5][5] = WHITE;

        currentPlayer = BLACK;  // black plays first
        
        gameBoard.updateStatus(pieces, currentPlayer);
        
    }
    
    /**
     * setupDebugBoard for testing END-game condition
     * students can freely make any changes to this method for testing purpose
     * TEMPORARY testing case
     */
    protected void setupDebugBoardEndGame()
    {
        gameBoard.addText("setupDebugBoardEndGame():");

        for (int row = 1; row <= 8; row++)
            for (int col = 1; col <= 8; col++)
                pieces[row][col] = BLACK;
        pieces[5][8] = WHITE;
        pieces[6][8] = EMPTY;
        pieces[7][8] = EMPTY;
        pieces[8][8] = EMPTY;

        currentPlayer = BLACK;  // BLACK plays first
        
        gameBoard.updateStatus(pieces, currentPlayer);
    }
 
    /**
     * setupDebugBoard for testing MID-game condition
     * students can freely make any changes to this method for testing purpose
     * TEMPORARY testing case
     */
    protected void setupDebugBoardMidGame()
    {
        gameBoard.addText("setupDebugBoardMidGame():");

        int row, col, distance;
        
        // make all pieces EMPTY
        for (row = 1; row <= 8; row++)
            for (col = 1; col <= 8; col++)
                pieces[row][col] = EMPTY;
        
        // STUDENTS' TEST and EXPERIMENT
        // setup a star pattern as a demonstration, you may try other setups
        // relax, we will NOT encounter array index out of bounds, see below!!
        row = 5;
        col = 3;
        distance = 3;
        
        // beware of hitting the boundary or ArrayIndexOutOfBoundsException
        for (int y_dir = -1; y_dir <= +1; y_dir++)
            for (int x_dir = -1; x_dir <= +1; x_dir++)
            {
                try {
                    int move;
                    // setup some opponents
                    for (move = 1; move <= distance; move++)
                        pieces[row + y_dir * move][col + x_dir * move] = BLACK;

                    // far-end friend piece
                    pieces[row+y_dir * move][col + x_dir*move] = WHITE;
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    // intentionally do nothing in this catch block
                    // this is simple and convenient in guarding array OOB
                }
            }
        // leave the center EMPTY for the player's enjoyment
        pieces[row][col] = EMPTY;
        
        // pieces[row][col] = 999;  // try an invalid piece

        
        // restore the fence of 10x10 EMPTY pieces around the 8x8 game board
        for (row = 1; row <= 8; row++)
            pieces[row][0] = pieces[row][9] = EMPTY;
        for (col = 1; col <= 8; col++)
            pieces[0][col] = pieces[9][col] = EMPTY;

        
        currentPlayer = WHITE;  // WHITE plays first
        // currentPlayer = 777;    // try an invalid player
        
        gameBoard.updateStatus(pieces, currentPlayer);
    }
    
   
    //method to calculate all possible valid moves in 8 directions 
    protected boolean nw,nn,ne,ww,ee,sw,ss,se=false; 
    protected int[][] calculateAllPossibleValidMoves(int currentPlayer,int[][]pieces){
        
        int arr [][]=new int[10][10];
        
        for (int r=1;r<=8;r++){
            for (int c=1;c<=8;c++){
              if (pieces[r][c]==EMPTY){ 
                nw=validMove(currentPlayer,-1,-1,r,c,pieces);
                nn=validMove(currentPlayer,-1,0,r,c,pieces);
                ne=validMove(currentPlayer,-1,1,r,c,pieces);

                ww=validMove(currentPlayer, 0,-1,r,c,pieces);
                ee=validMove(currentPlayer,0, 1,r,c,pieces);

                sw=validMove(currentPlayer,1,-1,r,c,pieces);
                ss=validMove(currentPlayer,1,0,r,c,pieces);
                se=validMove(currentPlayer,1,1,r,c,pieces);
                
                if (nw || nn || ne || ww || ee || sw || ss || se)
                    arr[r][c]=currentPlayer;
             }
            }
        }
            return arr;
    }
   
    /* Method to check if the position at r,c contains the opposite of currentPlayer on the board
     and if the line indicated by adding dr to r and dc to c can be turned to the currentPlayer colour
    */ 
    protected boolean validMove(int currentP, int dr,int dc,int r,int c,int [][] pcs){
        int otherPlayer;
        
        if (currentP == BLACK)
            otherPlayer=WHITE;
        else if (currentP==WHITE)
                otherPlayer=BLACK;
        else 
            return false;
        
        if ((r+dr<1) || (r+dr>8))
            return false;
        if ((c+dc<1) || (c+dc>8))
            return false;
        
        if (pcs[r+dr][c+dc]!=otherPlayer)
                return false;
        
        if ((r+dr+dr<1) || (r+dr+dr>8))
            return false;
        if ((c+dc+dc<1) || (c+dc+dc>8))
            return false; 
    
        return captureCheck (currentP,dr,dc,r+dr+dr,c+dc+dc,pcs);
    }
    
    /* Method to check if there is a colour of the currentPlayer on the same direction along 
       the line to bound the opponent's contiguous pieces in any of the 8 directions  starting at 
       (r,c) or anywhere further by adding dr and dc to (r,c)     
    */
    protected boolean captureCheck(int currentP,int dr,int dc,int r,int c,int[][] pieces){
        
        if (pieces[r][c]==currentP)
            return true;
        if (pieces[r][c]==EMPTY)
            return false;
        
        if ((r+dr<1) || (r+dr>8))
            return false;
        if ((c+dc<1) || (c+dc>8))
            return false;
        
        return captureCheck(currentP,dr,dc,r+dr,c+dc,pieces);
            
    }
    
    //Method that calls flipLine() method to check if cells can be flipped in any of 8 directions 
    protected void flipBoard(int currentP,int r,int c,int[][]pieces){
    
        flipLine(currentP,-1,-1,r,c,pieces);
        flipLine(currentP,-1,0,r,c,pieces);
        flipLine(currentP,-1,1,r,c,pieces);

        flipLine(currentP, 0,-1,r,c,pieces);
        flipLine(currentP,0, 1,r,c,pieces);

        flipLine(currentP,1,-1,r,c,pieces);
        flipLine(currentP,1,0,r,c,pieces);
        flipLine(currentP,1,1,r,c,pieces);
        
    }
    
    /* Method that flips the captured pieces from opponent's colour to the current player's colour
    if pieces are captured, and then returns true, otherwise if pieces are not captured, flipping does
    not take place and the method returns false */
    protected boolean flipLine (int currentP,int dr, int dc, int r, int c, int[][]pieces){
        
         if ((r+dr<1) || (r+dr>8))
            return false;
        if ((c+dc<1) || (c+dc>8))
            return false;
        
        if (pieces[r+dr][c+dc]==EMPTY)
                return false;
        
        if (pieces[r+dr][c+dc]==currentP)
                return true;
        else if (flipLine(currentP,dr,dc,r+dr,c+dc,pieces)){
            pieces[r+dr][c+dc]=currentPlayer;
            return true;
        }
        else
            return false;
 
    
    }
   
    /* Method that checks if forced pass is required as a result 
    of no valid moves possible for the current player */
    protected boolean forcedPass(int[][]legal){

       boolean legalEmpty=true;
       
       for (int i=1;i<=8;i++){
           for (int j=1;j<=8;j++){
               
               if (legal[i][j]!=EMPTY){
                   legalEmpty=false;                            
                   break;
               }       
           }
           
           if (legalEmpty==false)
             break;
       }
                
        return legalEmpty;     
    }        
    private boolean used=false; 
    private boolean fPass=false;
    private boolean fPass2=false;
   
   /**
     * STUDENTS' WORK HERE
     * 
     * As this is a GUI application, the gameBoard object (of class ReversiPanel)
     * actively listens to user's actionPerformed.  On user clicking of a
     * ReversiButton object, this callback method will be invoked to do some
     * game processing.
     * 
     * @param row is the row number of the clicked button
     * @param col is the col number of the clicked button
     */ 
    /* Method that takes the row and column of the user's click on the cells of the gameboard as parameters,
       calls methods to check whether it is a valid or invalid move,whether there are anymore valid moves left 
       or not for the current player. If no valid moves left, then forced pass is applied, and switched to another 
       player. If two consecutive forced pass takes place, the game ends. If there are valid moves left, it then calls 
       flipBoard() method to process whether it is possible to flip the contiguous oppenent's pieces bounded by the current
       player. It flips if the user has clicked a valid empty cell, otherwise, it displays invalid move. 
     */
    protected int [][]legalMoves;
    protected boolean entered=false;
    public void userClicked(int row, int col){
        
        int temp;
        
        while (true){
            legalMoves=calculateAllPossibleValidMoves(currentPlayer,pieces);
            fPass=forcedPass(legalMoves);
          
            // when there is no valid moves possible, a player is forced to pass
            if (fPass==true && used==false && entered==false){
                gameBoard.addText("Forced Pass");
                currentPlayer=FLIP*currentPlayer;
                gameBoard.updateStatus(pieces, currentPlayer);
                legalMoves=calculateAllPossibleValidMoves(currentPlayer,pieces);
                fPass2=forcedPass(legalMoves);
                
                /* if after first pass, there are no valid moves for the second player as well, 
                double forced pass occurs and the game ends */
                if (fPass2==true && used==false){
                  gameBoard.addText("Double Forced Pass");
                  gameBoard.addText("End game!");
                  used=true;
                  return;
                }
                else
                    return;
            } 
            
            
            temp = currentPlayer;
            
            //Following code is executed to capture opponent's pieces and flip them
            if (legalMoves[row][col]==temp && entered==false){
                pieces[row][col]=currentPlayer;
                flipBoard(currentPlayer,row,col,pieces);
                currentPlayer=FLIP*currentPlayer;
                gameBoard.updateStatus(pieces, currentPlayer);
                legalMoves=calculateAllPossibleValidMoves(currentPlayer,pieces);
                fPass=forcedPass(legalMoves);
                if (fPass==true && used==false)
                    continue;
                else
                    return;
                
            }
            else {
                gameBoard.addText("Invalid move"); 
                break;
            }
        }      
    } 
         
    // method to say "Goodbye!" on System.out, before program termination
    protected void sayGoodbye()
    {
        System.out.println("Goodbye!");
    }

    
    
    // main() method, starting point of basic Reversi game
    public static void main(String[] args) {
        Reversi game = new Reversi();
        
       // comment or remove the following statements for real game play
       // game.setupDebugBoardEndGame();
       // game.setupDebugBoardMidGame();
       // end of sample/ debugging code

        
        // *** STUDENTS need NOT write anything here ***
        
        // this application still runs in the background!!
        // the gameBoard object (of class ReversiPanel) listens and handles
        // user interactions as well as invoking method userClicked(row, col)
        
        // although this is end of main() method
        // THIS IS NOT THE END OF THE APP!
    }
}
