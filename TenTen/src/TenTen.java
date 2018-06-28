import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class TenTen {

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

        // TODO Implement what i think is easy strategy of counting total spaces
        // used to begin building the attempted round logic.

        // TODO Game Over method doesn't seem to be ending game prematurely
        // anymore. and now the game plays itself fully till completion
        // seemingly without error.

        // TODO check if I check for GameOver before I make more space from a
        // deletion of a row. My hunch is that I am ending the game prematurely.
        // Ihad a scenario where the gameboard was pretty full-almost gameover-
        // and only one piece could fit at that moment, but the piece that was
        // placed on the board actually delted a column that allowed a second
        // piece for that round to fit, but the game ended as if I check for
        // game over before updating the board from a deletion of a row or
        // column.

        // TODO sumOfRowsAndColumnsToBeSetToZero does not have an associated
        // copy for variable swapping. not sure what bad implications could
        // arise. This actually causes huge problems.

        // TODO Make the functionality that allows the game to play on its own.

        // TODO Make Solver - I think part of this is putting the
        // "placeOneOfThisRoundsRemainingPieces" in a loop where i put pieces
        // down in all possible places, recording the results and keeping and as
        // better results come in, I discard the previously recorded result.
        // This would be, i think a bad tradeoff in order to keep things simple,
        // so I'm hoping to come up with a better solution.

        // An attemptedround is the state of the 'copy' variables before copying
        // them to the finalized variables and after all three pieces have been
        // placed or game is over, which ever comes first.

        // A round is the state of the finalized variables after being
        // copiedinto by the copy variables. A new round is created or reached
        // when the trial portion of the code is complete.

        // A simple score for some attemptedround AR, will be defined as
        // follows: at the end of a round, for all entries in the
        // sumOfAllRowsAndColumns matrix that are greater than zero, the simple
        // score of the round is the product of the those entries greater than
        // zero. attemptedrounds with lower scores are kept are copied into.

        // CALCULATION OF THE SIMPLE SCORE:
        // BUT FIRST A QUESTION: Can I think of a round where it would be worse
        // to have a fewer cells or lower simple score...shit i haven't defined
        // simple score, hmmm.
        // I don't think it could ever be worse to have fewer used cells.
        // However, most of the time the number of cells for all attemptedRounds
        // is equal. Then it is other factors that go into what attempted round
        // should accepted as the round.

        // OTHER FACTORS involved in calculating the score of an attemptedround
        // in order of importance are:
        // xCloseness to completing a row(s) or column(s).
        // xConnectivity - opting to avoid connectivities lesser connectivities.
        // xEnsuring the space to place a piece of height or width 5 and space
        // enough for the 3x3.
        // So the order of importance is:
        // Ensuring the space to fit the large pieces
        // number of used cells(= sum of sum of rows and columns)
        // preserving connectivity of 1 or greater.
        // closeness to completing a row
        // preserving connectivity of 2 or greater
        // total connectivity?
        // preserving connectivity of 3 or greater.
        // Given an attemptedround, the simple score is...

        // So I think the score will be calculated in the following way:
        // I will present it as tie breaker rules in a tournament are presented.
        // Given two attempted rounds with equal ensuredness of

        // TODO All unit tests pass at least initial scenarios. Time to do
        // exhaustive testing.
        // TODO Break large methods down into smaller methods. Make code more
        // English
        // TODO Make GUI for the game
        // I fixed the "isGameOver" issue merely by checking if the game is over
        // a bit sooner - before the prompt to choose the next piece.

        // Will implement the consideration of each factor into a sequence of
        // strategies beginning with the simplest consideration

        int round = 1;
        System.out.println("*************ROUND 1****************");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (gameOver == false) {
            if (round > 1) {
                System.out.println("*************ROUND " + round + "****************");
            }

            int[][] thisRoundsZerothPiece = new int[5][5];
            int[][] thisRoundsFirstPiece = new int[5][5];
            int[][] thisRoundsSecondPiece = new int[5][5];

            /////////////////////////////////////////////////////////////////////////

            /////////////////////////////////////////////////////////////////////////
            // BufferedReader reservoirBufferedReader = new BufferedReader(new
            // InputStreamReader(System.in));
            //
            // System.out.print("Choose zeroth piece from Reservoir...");
            // int zerothPiece =
            // Integer.parseInt(reservoirBufferedReader.readLine());
            // TenTen.deepClone(thisRoundsZerothPiece,
            // PieceReservoir[zerothPiece]);
            //
            // System.out.print("Choose 1st piece from Reservoir...");
            // int firstPiece =
            // Integer.parseInt(reservoirBufferedReader.readLine());
            // TenTen.deepClone(thisRoundsFirstPiece,
            // PieceReservoir[firstPiece]);
            //
            // System.out.print("Choose 3rd piece from Reservoir...");
            // int secondPiece =
            // Integer.parseInt(reservoirBufferedReader.readLine());
            // TenTen.deepClone(thisRoundsSecondPiece,
            // PieceReservoir[secondPiece]);
            //////////////////////////////////////////////////////////////////////////
            // Randomly choosing pieces
            TenTen.deepClone(thisRoundsZerothPiece, TenTen.chooseRandomPieceFromReserve());
            TenTen.deepClone(thisRoundsFirstPiece, TenTen.chooseRandomPieceFromReserve());
            TenTen.deepClone(thisRoundsSecondPiece, TenTen.chooseRandomPieceFromReserve());

            thisRoundsPieces[0] = thisRoundsZerothPiece;
            thisRoundsPieces[1] = thisRoundsFirstPiece;
            thisRoundsPieces[2] = thisRoundsSecondPiece;

            TenTen.printGameBoard();
            System.out.println("SCORE " + score);

            while (thisRoundsPieces[0][0][0] != 5 || thisRoundsPieces[1][0][0] != 5 || thisRoundsPieces[2][0][0] != 5) {
                System.out.println("SCORE " + score);
                TenTen.printGameBoard();
                TenTen.printThisRoundsRemainingPieces(thisRoundsPieces);

                TenTen.isGameOver(thisRoundsPieces, gameBoard);
                System.out.print("Choose piece 0, 1, or 2 to place on GameBoard...");
                int playersChoiceOfPieceToPlay = Integer.parseInt(bufferedReader.readLine());
                System.out.print("Choose a Row to place the top left of the piece...");
                if (thisRoundsPieces[playersChoiceOfPieceToPlay][0][0] == 5) {
                    System.out.println("///////////////////////////\nTHIS PIECE WAS ALREADY USED!!\nTRY AGAIN\n///////////////////////////");
                    continue;
                }

                int playersChoiceOfRow = Integer.parseInt(bufferedReader.readLine());
                System.out.print("Choose a Column to place the top left of the piece...");
                int playersChoiceOfColumn = Integer.parseInt(bufferedReader.readLine());

                TenTen.placeOneOfThisRoundsRemainingPieces(thisRoundsPieces[playersChoiceOfPieceToPlay], playersChoiceOfRow, playersChoiceOfColumn, copyOfGameBoard);
                score = copyScore;

                TenTen.deepClone(copyOfGameBoard, gameBoard);
            }
            round++;

        }

    };

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
        Boolean brokeOutOfPiecesColumnIteration = false;
        Boolean brokeOutOfGameBoardColumnIteration = false;
        Boolean brokeOutOfGameBoardRowIteration = false;
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
                            if (thisRoundsPieces[pieceNumber][i][j] == 5) {
                                // continue to next piece number
                                brokeOutOfPiecesColumnIteration = true;
                                brokeOutOfGameBoardRowIteration = true;
                                break;
                            }
                            if (thisRoundsPieces[pieceNumber][i][j] == 1) {
                                if (!(piecesRowPositionOnGameBoard + i <= 9)) {
                                    brokeOutOfGameBoardRowIteration = true;
                                    break;
                                }
                                if (!(piecesColumnPositionOnGameBoard + j <= 9)) {
                                    brokeOutOfGameBoardColumnIteration = true;
                                    break;
                                }
                            }
                            if (thisRoundsPieces[pieceNumber][i][j] == 1 && copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] == 1) {
                                brokeOutOfPiecesColumnIteration = true;
                                break;
                            }
                            if (i == 4 && j == 4) {
                                gameOver = false;
                                return;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("*********\n*********\nGame Over\n*********\n*********\n");
    }

    public static void placeOneOfThisRoundsRemainingPieces(int[][] selectedPiece, int piecesRowPositionOnGameBoard, int piecesColumnPositionOnGameBoard, int[][] copyOfGameBoard) {
        Boolean breakOutOfOuterLoop = false;
        for (int i = 0; i <= 4; i++) {
            if (breakOutOfOuterLoop == true) {
                TenTen.deepClone(copyOfGameBoard, gameBoard);
                copyScore = score;
                TenTen.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                TenTen.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                // copyOfGameBoard = gameBoard.clone();
                return;
            }
            for (int j = 0; j <= 4; j++) {
                if (selectedPiece[i][j] == 5) {
                    breakOutOfOuterLoop = true;
                    TenTen.deepClone(copyOfGameBoard, gameBoard);
                    copyScore = score;
                    TenTen.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                    TenTen.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                    System.out.println("///////////////////////\nThis piece was played already\n//////////////////////");
                    return;
                }

                if (selectedPiece[i][j] == 1 && !(piecesRowPositionOnGameBoard + i <= 9 && piecesColumnPositionOnGameBoard + j <= 9)) {
                    TenTen.deepClone(copyOfGameBoard, gameBoard);
                    // copyOfGameBoard = gameBoard.clone();
                    breakOutOfOuterLoop = true;

                    copyScore = score;
                    TenTen.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                    TenTen.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                    System.out.println("//////////////////////\nOut of Bounds\n////////////////////////");
                    return;
                }

                if (selectedPiece[i][j] == 1 && copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] == 1) {
                    TenTen.deepClone(copyOfGameBoard, gameBoard);
                    // copyOfGameBoard = gameBoard.clone();
                    breakOutOfOuterLoop = true;

                    copyScore = score;
                    TenTen.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
                    TenTen.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
                    System.out.println("/////////////////////\nSpot taken\n////////////////////////////");
                    return;
                }

                if (selectedPiece[i][j] == 1 && copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] == 0) {
                    copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] = copyOfGameBoard[piecesRowPositionOnGameBoard + i][piecesColumnPositionOnGameBoard + j] + selectedPiece[i][j];
                    copyOfSumOfRowsAndColumnsInGameBoard[0][piecesRowPositionOnGameBoard + i] = copyOfSumOfRowsAndColumnsInGameBoard[0][piecesRowPositionOnGameBoard + i] + 1;
                    copyOfSumOfRowsAndColumnsInGameBoard[1][piecesColumnPositionOnGameBoard + j] = copyOfSumOfRowsAndColumnsInGameBoard[1][piecesColumnPositionOnGameBoard + j] + 1;
                    copyScore = copyScore + 1;
                    if (copyOfSumOfRowsAndColumnsInGameBoard[0][piecesRowPositionOnGameBoard + i] == 10) {
                        copyOfSumsOfRowsAndColumnsToBeSetToZero[0][piecesRowPositionOnGameBoard + i] = 1;
                    }
                    if (copyOfSumOfRowsAndColumnsInGameBoard[1][piecesColumnPositionOnGameBoard + j] == 10) {
                        copyOfSumsOfRowsAndColumnsToBeSetToZero[1][piecesColumnPositionOnGameBoard + j] = 1;
                    }

                }
            }
        }
        if (breakOutOfOuterLoop == false) {
            selectedPiece[0][0] = 5;
            TenTen.deepClone(gameBoard, copyOfGameBoard);
            // gameBoard = copyOfGameBoard.clone();
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 9; j++) {
                    if (copyOfSumOfRowsAndColumnsInGameBoard[i][j] == 10) {
                        // What I should do is merely record which rows and
                        // columns sum to 10 and then after I know all of them,
                        // delete them all together.
                        if (i == 0) {
                            for (int columnIndex = 0; columnIndex <= 9; columnIndex++) {
                                gameBoard[j][columnIndex] = 0;
                            }
                        } else {
                            for (int rowIndex = 0; rowIndex <= 9; rowIndex++) {
                                gameBoard[rowIndex][j] = 0;
                            }
                        }
                    }
                }
            }
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
            // Account for subtracting 1 from a row when a column was deleted or
            // any number of column deletions
            if (numberOfColumnsDeleted > 0) {
                for (int row = 0; row <= 9; row++) {
                    for (int subtractionIteration = 1; subtractionIteration <= numberOfColumnsDeleted; subtractionIteration++) {
                        if (copyOfSumOfRowsAndColumnsInGameBoard[0][row] != 0 && numberOfColumnsDeleted > 0) {
                            copyOfSumOfRowsAndColumnsInGameBoard[0][row] = copyOfSumOfRowsAndColumnsInGameBoard[0][row] - 1;
                        }
                    }
                }
            }
            if (numberOfRowsDeleted > 0) {
                for (int column = 0; column <= 9; column++) {
                    for (int subtractionIteration = 1; subtractionIteration <= numberOfRowsDeleted; subtractionIteration++) {
                        if (copyOfSumOfRowsAndColumnsInGameBoard[1][column] != 0 && numberOfRowsDeleted > 0) {
                            copyOfSumOfRowsAndColumnsInGameBoard[1][column] = copyOfSumOfRowsAndColumnsInGameBoard[1][column] - 1;
                        }
                    }
                }
            }
            numberOfColumnsDeleted = 0;
            numberOfRowsDeleted = 0;
            TenTen.deepClone(sumOfRowsAndColumnsInGameBoard, copyOfSumOfRowsAndColumnsInGameBoard);
            TenTen.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
        } else {
            TenTen.deepClone(copyOfGameBoard, gameBoard);
            TenTen.deepClone(copyOfSumOfRowsAndColumnsInGameBoard, sumOfRowsAndColumnsInGameBoard);
            TenTen.deepClone(copyOfSumsOfRowsAndColumnsToBeSetToZero, sumsOfRowsAndColumnsToBeSetToZero);
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
