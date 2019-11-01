/**
 VisibleField class
 This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
 user can see about the minefield), Client can call getStatus(row, col) for any square.
 It actually has data about the whole current state of the game, including
 the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
 It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
 and changes the game state accordingly.

 It, along with the MineField (accessible in mineField instance variable), forms
 the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
 It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from
 outside this class via the getMineField accessor.
 */
public class VisibleField {
    // ----------------------------------------------------------
    // The following public constants (plus numbers mentioned in comments below) are the possible states of one
    // location (a "square") in the visible field (all are values that can be returned by public method
    // getStatus(row, col)).

    // Covered states (all negative values):
    public static final int COVERED = -1;   // initial value of all squares
    public static final int MINE_GUESS = -2;
    public static final int QUESTION = -3;

    // Uncovered states (all non-negative values):

    // values in the range [0,8] corresponds to number of mines adjacent to this square

    public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
    public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
    public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
    // ----------------------------------------------------------

    // <put instance variables here>
    // the MineField object which has all information of the minefield
    private MineField mineField;
    // the 2D int array to represent the status of every location in the minefield
    private int[][] status;



    /**
     Create a visible field that has the given underlying mineField.
     The initial state will have all the mines covered up, no mines guessed, and the game
     not over.
     @param mineField  the minefield to use for for this VisibleField
     */
    public VisibleField(MineField mineField) {
        // Initializes the VisibleField object with given mineField

        this.mineField=mineField;
        int rows=this.mineField.numRows();
        int cols=this.mineField.numCols();
        status=new int[rows][cols];
        resetGameDisplay();
    }


    /**
     Reset the object to its initial state (see constructor comments), using the same underlying
     MineField.
     */
    public void resetGameDisplay() {
        // Resets the status of each location, which means each square is covered

        for (int i=0;i<status.length;i++){
            for (int j=0;j<status[0].length;j++){
                status[i][j]=COVERED;
            }
        }
    }


    /**
     Returns a reference to the mineField that this VisibleField "covers"
     @return the minefield
     */
    public MineField getMineField() {
        return mineField;       // DUMMY CODE so skeleton compiles
    }


    /**
     Returns the visible status of the square indicated.
     @param row  row of the square
     @param col  col of the square
     @return the status of the square at location (row, col).  See the public constants at the beginning of the class
     for the possible values that may be returned, and their meanings.
     PRE: getMineField().inRange(row, col)
     */
    public int getStatus(int row, int col) {
        return status[row][col];
    }


    /**
     Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
     or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value can
     be negative, if they have guessed more than the number of mines in the minefield.
     @return the number of mines left to guess.
     */
    public int numMinesLeft() {
        // calculate the number of mines remaining to be guessed

        int guessSum=0;
        for (int i=0;i<status.length;i++){
            for (int j=0;j<status[0].length;j++){
                if (status[i][j]==MINE_GUESS){
                    guessSum++;
                }
            }
        }
        return mineField.numMines()-guessSum;
    }


    /**
     Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
     changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
     changes it to COVERED again; call on an uncovered square has no effect.
     @param row  row of the square
     @param col  col of the square
     PRE: getMineField().inRange(row, col)
     */
    public void cycleGuess(int row, int col) {
        // Changes the status of the square according to the following sequence:
        // COVERED --> MINE_GUESS --> QUESTION --> COVERED

        if (getStatus(row,col)==COVERED){
            status[row][col]=MINE_GUESS;
        } else if (getStatus(row,col)==MINE_GUESS){
            status[row][col]=QUESTION;
        } else if (getStatus(row,col)==QUESTION){
            status[row][col]=COVERED;
        }
    }


    /**
     Uncovers this square and returns false iff you uncover a mine here.
     If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in
     the neighboring area that are also not next to any mines, possibly uncovering a large region.
     Any mine-adjacent squares you reach will also be uncovered, and form
     (possibly along with parts of the edge of the whole field) the boundary of this region.
     Does not uncover, or keep searching through, squares that have the status MINE_GUESS.
     Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
     or a loss (opened a mine).
     @param row  of the square
     @param col  of the square
     @return false   iff you uncover a mine at (row, col)
     PRE: getMineField().inRange(row, col)
     */
    public boolean uncover(int row, int col) {
        // When we uncover one square, there are two possible results:
        // 1. If there is a mine, updates the status of this square which represents the failure
        // of the minesweeper.
        // 2. If there is no mine, updates the status of squares, and the minesweeper does not finish
        // until the game is over.

        if (mineField.hasMine(row,col)){
            status[row][col]=EXPLODED_MINE;
            return false;
        } else{
            update(row,col);
            return true;
        }
    }


    /**
     Returns whether the game is over.
     (Note: This is not a mutator.)
     @return whether game over
     */
    public boolean isGameOver() {
        // win: true --> win the game
        // lose: true --> lose the game

        boolean win=false;
        boolean lose=false;

        // numOfUncov counts the total number of uncovered square whose status is from 0 to 8

        int numOfUncov=0;

        // If there is a square whose status is EXPLODED_MINE which means we have uncovered
        // a mine, we lose; otherwise, if the status of the square is uncovered, numOfUncov
        // plus 1.
        for (int i=0;i<mineField.numRows();i++){
            for (int j=0;j<mineField.numCols();j++){
                if (status[i][j]==EXPLODED_MINE){
                    lose=true;
                    break;
                }
                if (status[i][j]>=0 && status[i][j]<=8){
                    numOfUncov++;
                }
            }
        }

        // If we have uncovered all the squares which have no mines, we win.
        if (!lose){
            if (numOfUncov==mineField.numRows() * mineField.numCols() - mineField.numMines()){
                win=true;
            }
        }

        // If game is over, updates all the final status.
        if (lose){
            updateLose();
        }

        if (win){
            updateWin();
        }
        return lose || win;
    }


    /**
     Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states,
     vs. any one of the covered states).
     @param row of the square
     @param col of the square
     @return whether the square is uncovered
     PRE: getMineField().inRange(row, col)
     */
    public boolean isUncovered(int row, int col) {
        // Judges whether this square has been uncovered

        if (getStatus(row,col)>=0 && getStatus(row,col)<=EXPLODED_MINE){
            return true;
        } else{
            return false;
        }
    }


    // <put private methods here>
    //There are four cases we need to keep searching through or stop searching:
    // 1. The location is invalid --> inRange(row, col) is false
    // 2. The status of the square is MINE_GUESS
    // 3. The square is uncovered
    // 4. This square has adjacent mines

    private void update(int row,int col){
        if (!mineField.inRange(row,col) || isUncovered(row,col) || status[row][col]==MINE_GUESS){
            return;
        }
        if (mineField.numAdjacentMines(row,col)!=0){
            status[row][col]=mineField.numAdjacentMines(row,col);
            return;
        } else{
            status[row][col]=mineField.numAdjacentMines(row,col);
            update(row-1,col-1);
            update(row-1,col);
            update(row-1,col+1);
            update(row,col-1);
            update(row,col+1);
            update(row+1,col-1);
            update(row+1,col);
            update(row+1,col+1);
        }
    }

    //update the final status if we lose

    private void updateLose(){
        for (int i=0;i<mineField.numRows();i++){
            for (int j=0;j<mineField.numCols();j++){
                if (mineField.hasMine(i,j) && status[i][j]!=MINE_GUESS && status[i][j]!=EXPLODED_MINE){
                    status[i][j]=MINE;
                }
                if (!mineField.hasMine(i,j) && status[i][j]==MINE_GUESS){
                    status[i][j]=INCORRECT_GUESS;
                }
            }
        }
    }

    //update the final status if we win

    private void updateWin(){
        for (int i=0;i<mineField.numRows();i++){
            for (int j=0;j<mineField.numCols();j++){
                if (status[i][j]==COVERED || status[i][j]==QUESTION){
                    status[i][j]=MINE_GUESS;
                }
            }
        }
    }
}
