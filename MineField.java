import java.util.Random;

public class MineField {

    // <put instance variables here>

    // the 2D boolean array as underlying minefield
    private boolean[][] mineData;
    // the number of rows in the field
    private int numRows;
    // the number of columns in the field
    private int numCols;
    // the number of mines in the minefield
    private int numMines;


    /**
     Create a minefield with same dimensions as the given array, and populate it with the mines in the array
     such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
     this minefield will corresponds to the number of 'true' values in mineData.
     * @param mineData  the data for the mines; must have at least one row and one col.
     */
    public MineField(boolean[][] mineData) {
        // Initialize the MineField object based on the given 2D boolean array
        numRows=mineData.length;
        numCols=mineData[0].length;
        numMines=0;

        this.mineData=new boolean[numRows][numCols];
        for (int i=0;i<numRows;i++){
            for (int j=0;j<numCols;j++){
                if (mineData[i][j]==true){
                    this.mineData[i][j]=true;
                    numMines++;
                }
            }
        }

    }


    /**
     Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once
     populateMineField is called on this object).  Until populateMineField is called on such a MineField,
     numMines() will not correspond to the number of mines currently in the MineField.
     @param numRows  number of rows this minefield will have, must be positive
     @param numCols  number of columns this minefield will have, must be positive
     @param numMines   number of mines this minefield will have,  once we populate it.
     PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations).
     */
    public MineField(int numRows, int numCols, int numMines) {
        // Initializes the MineField object with given 3 parameters
        mineData=new boolean[numRows][numCols];
        this.numRows=numRows;
        this.numCols=numCols;
        this.numMines=numMines;


    }


    /**
     Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
     ensuring that no mine is placed at (row, col).
     @param row the row of the location to avoid placing a mine
     @param col the column of the location to avoid placing a mine
     PRE: inRange(row, col)
     */
    public void populateMineField(int row, int col) {

        Random rand=new Random();
        // Removes any current mines on the minefield
        resetEmpty();
        // Generates the row and column randomly
        for (int i=0;i<numMines;i++){
            int x=rand.nextInt(numRows);
            int y=rand.nextInt(numCols);
            while (invalid(x,y,row,col,mineData)){
                x=rand.nextInt(numRows);
                y=rand.nextInt(numCols);
            }

        }
    }


    /**
     Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
     Thus, after this call, the actual number of mines in the minefield does not match numMines().
     Note: This is the state the minefield is in at the beginning of a game.
     */
    public void resetEmpty() {
        // Resets the minefield to all empty squares, which means there is no mine in the field.
        for (int i=0;i<numRows;i++){
            for (int j=0;j<numCols;j++){
                if (mineData[i][j]==true){
                    mineData[i][j]=false;
                }
            }
        }
    }


    /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
     */
    public int numAdjacentMines(int row, int col) {
        // calculate the total number of mines in the 8 squares adjacent to (row, col)
        int num=0;
        for (int i=-1;i<2;i++){
            for (int j=-1;j<2;j++){
                if (inRange(row+i,col+j) && hasMine(row+i,col+j) && (i!=0 || j!=0)){
                    num++;
                }
            }
        }
        return num;

    }


    /**
     Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
     start from 0.
     @param row  row of the location to consider
     @param col  column of the location to consider
     @return whether (row, col) is a valid field location
     */
    public boolean inRange(int row, int col) {
        // judge whether (row, col) is a valid field location
        if ((row<0 || row>=numRows) || (col<0 || col>=numCols)){
            return false;
        }
        return true;       // DUMMY CODE so skeleton compiles
    }


    /**
     Returns the number of rows in the field.
     @return number of rows in the field
     */
    public int numRows() {
        return numRows;       // DUMMY CODE so skeleton compiles
    }


    /**
     Returns the number of columns in the field.
     @return number of columns in the field
     */
    public int numCols() {
        return numCols;       // DUMMY CODE so skeleton compiles
    }


    /**
     Returns whether there is a mine in this square
     @param row  row of the location to check
     @param col  column of the location to check
     @return whether there is a mine in this square
     PRE: inRange(row, col)
     */
    public boolean hasMine(int row, int col) {

        return mineData[row][col];    // DUMMY CODE so skeleton compiles
    }


    /**
     Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
     some of the time this value does not match the actual number of mines currently on the field.  See doc for that
     constructor, resetEmpty, and populateMineField for more details.
     * @return
     */
    public int numMines() {
        return numMines;
    }


    // <put private methods here>

    // There are two cases we cannot place the mine:
    // 1. We cannot place the mine at location with the parameters (row, col).
    // 2. This square already has a mine.
    private boolean invalid(int x,int y,int row,int col,boolean[][] mineData){
        if ((x==row && y==col) || hasMine(x,y)){
            return true;
        }
        mineData[x][y]=true;
        return false;
    }

}

