import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class TenTenSolver {

    public static final int[][][] PieceReservoir = new int[19][5][5];
    public static int[][] gameBoard = new int[10][10];
    public static int[][] copyOfGameBoard = new int[10][10];
    public static int[][][] thisRoundsPieces = new int[3][5][5];
    public static int[][] sumOfRowsAndColumnsInGameBoard = new int[2][10];
    public static int[][] copyOfSumOfRowsAndColumnsInGameBoard = new int[2][10];
    public static int[][] sumsOfRowsAndColumnsToBeSetToZero = new int[2][10];
    public static int[][] copyOfSumsOfRowsAndColumnsToBeSetToZero = new int[2][10];

    public static int score = 0;
    public static int copyScore;
    public static boolean gameOver;

    public static int[][] attemptedGameBoard = new int[10][10];
    public static int[][] attemptedSumOfRowsAndColumnsInGameBoard = new int[2][10];
    public static int[][] attemptedScore;
    public static int[][] attemptedGameOver;
    public static boolean attemptingRounds;
    public static int[][] pieceOrdering = new int[6][3];

    public static void main(String[] args) throws IOException {

        // population of the upper leftmost corner. call it cell (0,0).
        for (int i = 0; i <= 18; i++) {
            if (i != 5 && i != 12) {
                PieceReservoir[i][0][0] = 1;
            }
        }

        // population of the (1,0) cell. i.e. one to the right of the upper left
        // most cell.
        for (int i = 1; i <= 10; i++) {
            if (i != 2 && i != 4 && i != 6) {
                PieceReservoir[i][0][1] = 1;
                //
            }
        }

        for (int i = 14; i <= 18; i++) {
            if (i != 17) {
                PieceReservoir[i][0][1] = 1;
            }
        }

        // population of the (2,0) cell.
        PieceReservoir[3][0][2] = 1;

        for (int i = 10; i <= 18; i++) {
            if (i != 11 && i != 13 && i != 17) {
                PieceReservoir[i][0][2] = 1;
                //
            }
        }

        // population of the (3, 0) cell.
        PieceReservoir[10][0][3] = 1;
        PieceReservoir[16][0][3] = 1;

        // population of the (4,0) cell.
        PieceReservoir[16][0][4] = 1;

        // population of the (0,1) cell.
        for (int i = 0; i <= 18; i++) {
            if (i != 0 && i != 1 && i != 3 && i != 8 && i != 10 && i != 12 && i != 15 && i != 16) {
                PieceReservoir[i][1][0] = 1;
            }
        }

        // population of the (1, 1) cell.
        PieceReservoir[5][1][1] = 1;
        PieceReservoir[6][1][1] = 1;
        PieceReservoir[8][1][1] = 1;
        PieceReservoir[9][1][1] = 1;
        PieceReservoir[18][1][1] = 1;

        // population of the (2, 1) cell.
        for (int i = 12; i <= 18; i = i + 3) {
            PieceReservoir[i][1][2] = 1;
        }

        // population of the (0,2) cell.
        PieceReservoir[4][2][0] = 1;
        for (int i = 11; i <= 18; i++) {
            if (i != 15 && i != 16) {
                PieceReservoir[i][2][0] = 1;
            }
        }

        // population of the (1,2) cell.
        PieceReservoir[12][2][1] = 1;
        PieceReservoir[13][2][1] = 1;
        PieceReservoir[18][2][1] = 1;

        // population of the (2,2) cell.
        PieceReservoir[12][2][2] = 1;
        PieceReservoir[13][2][2] = 1;
        PieceReservoir[15][2][2] = 1;
        PieceReservoir[18][2][2] = 1;

        // population of the (0,3) cell.
        PieceReservoir[11][3][0] = 1;
        PieceReservoir[17][3][0] = 1;

        // population of the (0,4) cell.
        PieceReservoir[17][4][0] = 1;
        /////////////////////////////////////////////
        /////////////////////////////////////////////
        /////////////////////////////////////////////
        // population of the pieceOrder variable
        // pieceOrder[0][0] = 0;
        // pieceOrder[0][1] = ;
        // pieceOrder[0][2] = ;
        // pieceOrder[1][0] = ;
        // pieceOrder[1][1] = ;
        // pieceOrder[1][2] = ;
        // pieceOrder[2][0] = ;
        // pieceOrder[2][1] = ;
        // pieceOrder[2][2] = ;
        // pieceOrder[3][0] = ;
        // pieceOrder[3][1] = ;
        // pieceOrder[3][2] = ;
        // pieceOrder[4][0] = ;
        // pieceOrder[4][1] = ;
        // pieceOrder[4][2] = ;
        // pieceOrder[5][0] = ;
        // pieceOrder[5][1] = ;
        // pieceOrder[5][2] = ;

        // TODO Make Solver - I think part of this is putting the
        // "placeOneOfThisRoundsRemainingPieces" in a loop where i put pieces
        // down in all possible places, recording the results and keeping the
        // better results as they come in, I discard the previously recorded
        // result.
        // This would be, i think a bad tradeoff in order to keep things simple,
        // so I'm hoping to come up with a better solution.

        // An attemptedround is the state of the 'copy' variables before copying
        // them to the finalized variables and after all three pieces have been
        // placed or game is over, which ever comes first.

        // A round is the state of the finalized variables after being
        // copied into by the copy variables. A new round is created or reached
        // when the trial portion of the code is complete.

        // A simple score for some attemptedround AR, will be defined as
        // follows: at the end of a round, for all entries in the
        // sumOfAllRowsAndColumns matrix that are greater than zero, the simple
        // score of the round is the product of the those entries greater than
        // zero. attemptedrounds with lower scores are kept are copied into.

        // CALCULATION OF THE SIMPLE SCORE:
        // BUT FIRST A QUESTION: Can I think of a round where it would be worse
        // to have a fewer cells or lower simple score?
        // Off the top of my head, I don't think it could ever be worse to have
        // fewer used cells. Often times there will be no row or column deleted,
        // then it is other factors that go into what attempted round
        // should be accepted as the round.

        // OTHER FACTORS involved in calculating the score of an attemptedround:
        // xCloseness to completing a row(s) or column(s).
        // xConnectivity - opting to avoid connectivities lesser connectivities.
        // Connectivity for one cell is defined as the number of adjacent empty
        // cells. The minimum is 0 and the maximum is 4. The connectivity of the
        // gameboard would be the sum of the connectivities of each cell in the
        // gameboard. Not sure what to do about the double counting - two
        // adjacent cells count each other as an empty cell.
        // xEnsuring the space to place a piece of height or width 5 and space
        // enough for the 3x3.

        // The order of importance of these factors is:
        // Ensuring the space to fit the large pieces
        // number of used cells(= sum of sum of rows and columns)
        // preserving connectivity of 1 or greater for all cells.
        // closeness to completing a row
        // preserving connectivity of 2 or greater
        // Maybe connectivity of the gameboard?
        // preserving connectivity of 3 or greater.
        // Given an attemptedround, the simple score is...

        // So I think the score will be calculated in the following way:
        // I will present it as tie breaker rules in a tournament are presented.
        // Given two attempted rounds with equal ensuredness of

        // TODO Break large methods down into smaller methods. Make code more
        // English like
        // TODO Make GUI for the game
        // I fixed the "isGameOver" issue merely by checking if the game is over
        // a bit sooner - before the prompt to choose the next piece.

        // Will implement the consideration of each factor into a sequence of
        // strategies beginning with the simplest consideration

        int round = 1;
        int attemptedRound = 1;
        System.out.println("*************ROUND 1****************");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        // While loop 'defining' the life-span of the whole game from game start
        // to game over
        while (gameOver == false) {
            // for (int pieceOrder = 0; pieceOrder <= 5; pieceOrder++) {
            if (round > 1) {
                System.out.println("*************ROUND " + round + "****************");
            }

            // System.out.println("*******attemptedRound 1*******");
            // Declare three variables to hold three pieces.
            int[][] thisRoundsZerothPiece = new int[5][5];
            int[][] thisRoundsFirstPiece = new int[5][5];
            int[][] thisRoundsSecondPiece = new int[5][5];

            // Instantiate three random pieces
            TenTenSolver.deepClone(thisRoundsZerothPiece, TenTenSolver.chooseRandomPieceFromReserve());
            TenTenSolver.deepClone(thisRoundsFirstPiece, TenTenSolver.chooseRandomPieceFromReserve());
            TenTenSolver.deepClone(thisRoundsSecondPiece, TenTenSolver.chooseRandomPieceFromReserve());

            // Add them into "thisRoundsPieces" 3-D array
            thisRoundsPieces[0] = thisRoundsZerothPiece;
            thisRoundsPieces[1] = thisRoundsFirstPiece;
            thisRoundsPieces[2] = thisRoundsSecondPiece;

            TenTenSolver.printGameBoard();
            System.out.println("SCORE " + score);

            // While loop 'defining' the life-span of a round of three pieces.
            while (thisRoundsPieces[0][0][0] != 5 || thisRoundsPieces[1][0][0] != 5 || thisRoundsPieces[2][0][0] != 5) {
                System.out.println("SCORE " + score);
                TenTenSolver.printGameBoard();
                TenTenSolver.printThisRoundsRemainingPieces(thisRoundsPieces);

                TenTenSolver.isGameOver(thisRoundsPieces, gameBoard);
                // System.out.print("Choose piece 0, 1, or 2 to place on
                // GameBoard...");
                if (gameOver == false) {
                    // System.out.print("Watch computer play next piece");
                    // bufferedReader.readLine();
                } else {
                    break;
                }
                // int playersChoiceOfPieceToPlay =
                // Integer.parseInt(bufferedReader.readLine());
                // if (thisRoundsPieces[playersChoiceOfPieceToPlay][0][0] ==
                // 5)
                // {

                // System.out.print("Choose a Row to place the top left of
                // the
                // piece...");
                // int playersChoiceOfRow =
                // Integer.parseInt(bufferedReader.readLine());
                // System.out.print("Choose a Column to place the top left
                // of
                // the piece...");
                // int playersChoiceOfColumn =
                // Integer.parseInt(bufferedReader.readLine());

                // TenTen.placeOneOfThisRoundsRemainingPieces(thisRoundsPieces[playersChoiceOfPieceToPlay],
                // playersChoiceOfRow, playersChoiceOfColumn,
                // copyOfGameBoard);

                // TODO change how I choose random piece to play so that I no
                // longer choose already played piece, huge waste of time there.
                // Record what random seed chose EVERYTIME through the while
                // loop below, then when some piece is actually placed on the
                // board, pop that piece from the "thisRoundsPieces" 3D array,
                // but have to change while loop condition for a Round's
                // life-span.
                while (copyScore == score) {
                    Random randomSeedForThisRoundsPieceToPlay = new Random();
                    int randomIndexForThisRoundsPieceToPlay = randomSeedForThisRoundsPieceToPlay.nextInt(3);
                    if (thisRoundsPieces[randomIndexForThisRoundsPieceToPlay][0][0] == 5) {
                        System.out.println("///////////////////////////\nTHIS PIECE WAS ALREADY USED!!\nTRY AGAIN\n///////////////////////////");
                        continue;
                    }
                    Random randomSeedForGameBoardRow = new Random();
                    int randomIndexForGameBoardRow = randomSeedForGameBoardRow.nextInt(10);
                    Random randomSeedForGameBoardColumn = new Random();
                    int randomIndexForGameBoardColumn = randomSeedForGameBoardColumn.nextInt(10);
                    TenTenSolver.placeOneOfThisRoundsRemainingPieces(thisRoundsPieces[randomIndexForThisRoundsPieceToPlay], randomIndexForGameBoardRow, randomIndexForGameBoardColumn, copyOfGameBoard);
                }
                score = copyScore;

                TenTenSolver.deepClone(copyOfGameBoard, gameBoard);
            }
            round++;
            // }
        }

    };

    public static void Strategy(int i) {

    }

    public static void printPieceReserve() {
        for (int i = 0; i <= 18; i++) {
            for (int j = 0; j <= 4; j++) {
                for (int k = 0; k <= 4; k++) {
                    System.out.print(PieceReservoir[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static int[][] chooseRandomPieceFromReserve() {
        Random randomSeed = new Random();
        int randomIndex = randomSeed.nextInt(19);
        return PieceReservoir[randomIndex];
    }

    public static void printGameBoard() {
        System.out.println("   |0 1 2 3 4 5 6 7 8 9|\n   --------------------");
        for (int i = 0; i <= 9; i++) {
            System.out.print(i + "|  ");
            for (int j = 0; j <= 9; j++) {
                System.out.print(gameBoard[i][j] + " ");
                if (j == 9) {
                    System.out.print("  || " + sumOfRowsAndColumnsInGameBoard[0][i]);
                }
            }
            System.out.println();
            if (i == 9) {
                System.out.println("------------------------");
                System.out.print("||| ");
                for (int y = 0; y <= 9; y++) {
                    System.out.print(sumOfRowsAndColumnsInGameBoard[1][y] + " ");
                }
            }
        }
        System.out.println();
    }

    public static void printThisRoundsRemainingPieces(int[][][] thisRoundsPieces) {
        System.out.print("This round's remaining pieces are...\n");
        for (int i = 0; i <= 2; i++) {
            System.out.println("Piece " + i);
            if (thisRoundsPieces[i][0][0] != 5) {
                for (int j = 0; j <= 4; j++) {
                    for (int k = 0; k <= 4; k++) {
                        System.out.print(thisRoundsPieces[i][j][k]);
                    }
                    System.out.println();
                }
                System.out.println();
                System.out.println();
            }
        }
    }

    public static int[][] deepClone(int[][] copyOfGameBoard, int[][] gameBoard) {
        for (int i = 0; i <= copyOfGameBoard.length - 1; i++) {
            copyOfGameBoard[i] = gameBoard[i].clone();
        }
        return copyOfGameBoard;
    }

    public static void isGameOver(int[][][] thisRoundsPieces, int[][] gameBoard) {
        gameOver = true;
        // These three booleans are used to break out to some level of the
        // 5-nested for-loops. I'll give more details about each at some other
        // time.
        Boolean brokeOutOfPiecesColumnIteration = false;
        Boolean brokeOutOfGameBoardColumnIteration = false;
        Boolean brokeOutOfGameBoardRowIteration = false;
        // Definition: building block - Given a piece from the reservoir, the
        // building blocks of the piece are those cells(coordinates) populated
        // with a '1'.
        // for each piece and
        // for each row positioning of the piece and
        // for each column positioning of the piece and
        // for each row positioning of the building block and
        // for each column positioning of the building block
        for (int pieceNumber = 0; pieceNumber <= 2; pieceNumber++) {
            brokeOutOfGameBoardRowIteration = false;
            brokeOutOfGameBoardColumnIteration = false;
            brokeOutOfPiecesColumnIteration = false;
            for (int piecesRowPositionOnGameBoard = 0; piecesRowPositionOnGameBoard <= 9; piecesRowPositionOnGameBoard++) {
                if (brokeOutOfGameBoardRowIteration == true) {
                    brokeOutOfGameBoardRowIteration = false;
                    brokeOutOfGameBoardColumnIteration = false;
                    break;
                }
                if (brokeOutOfGameBoardColumnIteration == true) {
                    brokeOutOfGameBoardColumnIteration = false;
                }
                for (int piecesColumnPositionOnGameBoard = 0; piecesColumnPositionOnGameBoard <= 9; piecesColumnPositionOnGameBoard++) {
                    if (brokeOutOfGameBoardColumnIteration == true) {
                        brokeOutOfGameBoardColumnIteration = false;
                        break;
                    }
                    if (brokeOutOfGameBoardRowIteration == true) {
                        break;
                    }
                    for (int i = 0; i <= 4; i++) {
                        if (brokeOutOfPiecesColumnIteration == true || brokeOutOfGameBoardColumnIteration == true || brokeOutOfGameBoardRowIteration == true) {
                            brokeOutOfPiecesColumnIteration = false;
                            break;
                        }
                        for (int j = 0; j <= 4; j++) {
                            // Check if piece has already been used
                            if (thisRoundsPieces[pieceNumber][i][j] == 5) {
                                // continue to next piece number
                                brokeOutOfPiecesColumnIteration = true;
                                brokeOutOfGameBoardRowIteration = true;
                                break;
                            }
                            if (thisRoundsPieces[pieceNumber][i][j] == 1) {
                                // Check if piece's current building block is
                                // beyond the bottom of the gameboard.
                                if (!(piecesRowPositionOnGameBoard + i <= 9)) {
                                    brokeOutOfGameBoardRowIteration = true;
                                    break;
                                }

                                // Check if piece's current building block is
                                // beyond the right side of the gameboard.
                                if (!(piecesColumnPositionOnGameBoard + j <= 9)) {
                                    brokeOutOfGameBoardColumnIteration = true;
                                    break;
                                }
                            }
                            // Check if current piece's current building block's
                            // position is already occupied.
                            if (thisRoundsPieces[pieceNumber][i][j] == 1 && copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] == 1) {
                                brokeOutOfPiecesColumnIteration = true;
                                break;
                            }
                            // If all of the buildings blocks of one of the
                            // remaining pieces for this round all land inside
                            // the game board for some (row position, column
                            // position) on the game board,
                            // then this means the game is not over yet and we
                            // enter the below if-block and return back that the
                            // game is not over.
                            // Notice we only enter the below if-block if i and
                            // j both equal 4 - that is we've checked all of the
                            // possible building blocks of at least one of the
                            // remaining pieces.
                            // Otherwise we continue iterating through these
                            // four loops until we reach i = j = 4 for some
                            // (row position, column position) on the game
                            // board.
                            if (i == 4 && j == 4) {
                                gameOver = false;
                                return;
                            }
                        }
                    }
                }
            }
        }
        // If for all remaining pieces and for all positions on the game board
        // there exists some conflict of building blocks, then we never made it
        // inside of the if statement above returning "gameOver = false;"
        // And so this means none of the pieces fit and we end the game.
        System.out.println("*********\n*********\nGame Over\n*********\n*********\n");
    }

    public static void placeOneOfThisRoundsRemainingPieces(int[][] selectedPiece, int piecesRowPositionOnGameBoard, int piecesColumnPositionOnGameBoard, int[][] copyOfGameBoard) {
        Boolean breakOutOfOuterLoop = false;
        for (int i = 0; i <= 4; i++) {
            if (breakOutOfOuterLoop == true) {
                TenTenSolver.deepClone(copyOfGameBoard, gameBoard);
                copyScore = score;
                TenTenSolver.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                TenTenSolver.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                // copyOfGameBoard = gameBoard.clone();
                return;
            }
            for (int j = 0; j <= 4; j++) {
                if (selectedPiece[i][j] == 5) {
                    breakOutOfOuterLoop = true;
                    TenTenSolver.deepClone(copyOfGameBoard, gameBoard);
                    copyScore = score;
                    TenTenSolver.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                    TenTenSolver.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                    System.out.println("///////////////////////\nThis piece was played already\n//////////////////////");
                    return;
                }
                // If the current building block, (i,j), is out of bounds, then
                // reset the copyOfGameBoard, copyScore, and copies of sums of
                // rows and columns.
                if (selectedPiece[i][j] == 1 && !(piecesRowPositionOnGameBoard + i <= 9 && piecesColumnPositionOnGameBoard + j <= 9)) {
                    TenTenSolver.deepClone(copyOfGameBoard, gameBoard);
                    // copyOfGameBoard = gameBoard.clone();
                    breakOutOfOuterLoop = true;

                    copyScore = score;
                    TenTenSolver.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                    TenTenSolver.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                    System.out.println("//////////////////////\nOut of Bounds\n////////////////////////");
                    return;
                }
                // If selectedPiece's building block, (i,j), is placed on
                // already taken spot, then reset copy variables as above
                // if-statement
                if (selectedPiece[i][j] == 1 && copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] == 1) {
                    TenTenSolver.deepClone(copyOfGameBoard, gameBoard);
                    // copyOfGameBoard = gameBoard.clone();
                    breakOutOfOuterLoop = true;

                    copyScore = score;
                    TenTenSolver.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                    TenTenSolver.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                    System.out.println("/////////////////////\nSpot taken\n////////////////////////////");
                    return;
                }
                // If selected Piece's building block gets passed previous
                // if-statements and the building block exists(i.e. is equal to
                // 1), then place building block and add 1 to the
                // sumOfRowsAndColumns in the proper row and proper column and
                // increment the copyScore.
                if (selectedPiece[i][j] == 1 && copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] == 0) {
                    copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] = copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] + selectedPiece[i][j];
                    copyOfSumOfRowsAndColumnsInGameBoard[0][piecesRowPositionOnGameBoard + i] = copyOfSumOfRowsAndColumnsInGameBoard[0][piecesRowPositionOnGameBoard + i] + 1;
                    copyOfSumOfRowsAndColumnsInGameBoard[1][piecesColumnPositionOnGameBoard + j] = copyOfSumOfRowsAndColumnsInGameBoard[1][piecesColumnPositionOnGameBoard + j] + 1;
                    copyScore = copyScore + 1;
                    // If Sum of proper row equals 10, then indicate that row's
                    // sum to be set to zero in the
                    // copyOfSumsOfRowsAndColumnsToBeSetToZero variable by
                    // setting proper row to 1(i.e. 1 means to reset to 0).
                    if (copyOfSumOfRowsAndColumnsInGameBoard[0][piecesRowPositionOnGameBoard + i] == 10) {
                        copyOfSumsOfRowsAndColumnsToBeSetToZero[0][piecesRowPositionOnGameBoard + i] = 1;
                    }
                    // If Sum of proper column equals 10, then indicate that
                    // column's sum to be set to zero in the
                    // copyOfSumsOfRowsAndColumnsToBeSetToZero variable by
                    // setting proper column to 1(i.e. 1 means to reset to 0).
                    if (copyOfSumOfRowsAndColumnsInGameBoard[1][piecesColumnPositionOnGameBoard + j] == 10) {
                        copyOfSumsOfRowsAndColumnsToBeSetToZero[1][piecesColumnPositionOnGameBoard + j] = 1;
                    }

                }
            }
        }
        // Below is further logic for adding to the score and further logic for
        // resetting the sumOfRowsAndColumsInGameBoard's proper row and column
        // to 0.

        // TODO See if I can change this giant if-else block by removing the
        // if-else completely and using a return statement just before the
        // else-block. The reason I think this could be possible is because I
        // don't think we'll reach this code if the conditional is true anyway.
        // Maybe a better way to say it is - If we reach the below code then it
        // must have been the case that "breakOutOfOuterLoop" IS INDEED false.
        // i.e. we don't need to check if it's false if it's always false when
        // we arrive here.
        if (breakOutOfOuterLoop == false) {
            // Set indicate the piece to be unplayabe as the round continues
            // since it has been placed on the gameboard already.
            selectedPiece[0][0] = 5;
            // update gameBoard with the latest building blocks added.
            TenTenSolver.deepClone(gameBoard, copyOfGameBoard);
            // gameBoard = copyOfGameBoard.clone();

            // For all rows, if the row is now filled with building blocks, then
            // reset every entry in that row to zero i.e. delete the building
            // blocks in that row.
            // For all Columns, if the Columns is now filled with building
            // blocks, then reset every entry in that column to zero i.e. delete
            // the building blocks in that columns.
            // Further comments inside.
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 9; j++) {
                    if (copyOfSumOfRowsAndColumnsInGameBoard[i][j] == 10) {
                        // TODO What I should do is merely record which rows and
                        // columns sum to 10 and then after I know all of them,
                        // delete them all together.

                        // If we're deleting a row that has been filled, then we
                        // hold the row constant and iterate through all the
                        // columns resetting back to zero as we go along.
                        if (i == 0) {
                            for (int columnIndex = 0; columnIndex <= 9; columnIndex++) {
                                gameBoard[j][columnIndex] = 0;
                            }
                            // Otherwise, if we're deleting a column, then we
                            // hold the column constant and iterate through the
                            // rows resetting the entries in the column back to
                            // zero.
                        } else {
                            for (int rowIndex = 0; rowIndex <= 9; rowIndex++) {
                                gameBoard[rowIndex][j] = 0;
                            }
                        }
                    }
                }
            }
            // Logic for adding
            int numberOfRowsDeleted = 0;
            int numberOfColumnsDeleted = 0;
            for (int rowsAndColumns = 0; rowsAndColumns <= 1; rowsAndColumns++) {
                for (int rowCommaColumn = 0; rowCommaColumn <= 9; rowCommaColumn++) {
                    if (copyOfSumsOfRowsAndColumnsToBeSetToZero[rowsAndColumns][rowCommaColumn] == 1) {
                        if (rowsAndColumns == 0) {
                            numberOfRowsDeleted++;
                        }
                        if (rowsAndColumns == 1) {
                            numberOfColumnsDeleted++;
                        }
                        copyOfSumOfRowsAndColumnsInGameBoard[rowsAndColumns][rowCommaColumn] = 0;
                    }
                }
            }
            // Add the triangle number to the score. 10*Ti, where i is the
            // number of rows and columns deleted.
            if (numberOfRowsDeleted + numberOfColumnsDeleted > 0) {
                int totalRowsAndColumnsDeleted = numberOfRowsDeleted + numberOfColumnsDeleted;
                for (int i = totalRowsAndColumnsDeleted; i >= 1; i--) {
                    copyScore += 10 * i;
                    System.out.println("+ " + 10 * i);
                }
            }
            // TODO the "if" below needs to also check "OR numberOfRowsDeleted >
            // 0", perhaps in same for loop and if conditional or separate, idk
            // yet.
            // Account for subtracting 1 building block from EVERY row when a
            // column was deleted or
            // any number of column deletions. i.e. n column deletions implies
            // to subtract n from the total number of building blocks in EVERY
            // row.
            if (numberOfColumnsDeleted > 0) {
                for (int row = 0; row <= 9; row++) {
                    for (int subtractionIteration = 1; subtractionIteration <= numberOfColumnsDeleted; subtractionIteration++) {
                        if (copyOfSumOfRowsAndColumnsInGameBoard[0][row] != 0 && numberOfColumnsDeleted > 0) {
                            copyOfSumOfRowsAndColumnsInGameBoard[0][row] = copyOfSumOfRowsAndColumnsInGameBoard[0][row] - 1;
                        }
                    }
                }
            }
            // Account for subtracting 1 building block from EVERY column when a
            // row was deleted or
            // any number of row deletions. i.e. n row deletions implies
            // to subtract n from the total number of building blocks in EVERY
            // column.
            if (numberOfRowsDeleted > 0) {
                for (int column = 0; column <= 9; column++) {
                    for (int subtractionIteration = 1; subtractionIteration <= numberOfRowsDeleted; subtractionIteration++) {
                        if (copyOfSumOfRowsAndColumnsInGameBoard[1][column] != 0 && numberOfRowsDeleted > 0) {
                            copyOfSumOfRowsAndColumnsInGameBoard[1][column] = copyOfSumOfRowsAndColumnsInGameBoard[1][column] - 1;
                        }
                    }
                }
            }
            // Copy the attempted piece's placement resulting scores and other
            // variables to be the most recent successful change.
            numberOfColumnsDeleted = 0;
            numberOfRowsDeleted = 0;
            TenTenSolver.deepClone(sumOfRowsAndColumnsInGameBoard, copyOfSumOfRowsAndColumnsInGameBoard);
            TenTenSolver.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
            // If we did not successfully place a piece onto the game board then
            // abort this attempt and reset all temporary variables to the last
            // successful changes.
        } else {
            TenTenSolver.deepClone(copyOfGameBoard, gameBoard);
            TenTenSolver.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
            TenTenSolver.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
            copyScore = score;
        }
    }

    public static int assurePlayingPieceFitsWithinGameBoardBounds(int[][] piece, int rowLoop, int columnLoop, int rowPosition, int columnPosition) {
        if (piece[rowLoop][columnLoop] == 1 && !(rowPosition + rowLoop <= 9 && columnPosition + columnLoop <= 9)) {
            rowLoop = 6;
            return rowLoop;
        }
        return rowLoop;
    }

    public static void assurePlayingPieceDoesNotConflictWithPreviouslyPlacedPieces() {

    }
}
