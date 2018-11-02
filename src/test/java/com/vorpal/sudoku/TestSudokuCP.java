// TestSudokuCP.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the SudokuCP algorithm, which solves Sudoku boards using constraint programming via Choco-Solver.
 *
 * Test SudokuCP simple, easy, medium, hard, and unsolvable (modification of hard) tests using puzzles taken from:
 * http://www.sudoku-solutions.com
 *
 * Extreme puzzle taken from:
 * https://www.telegraph.co.uk/news/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html
 *
 * Multiple solution test taken from:
 * http://www.sudokudragon.com/unsolvable.htm
 */
class TestSudokuCP {
    private SudokuBoard make(final int[][] seed) {
        final var board = new SudokuBoard();
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y)
                board.set(x, y, seed[x][y]);
        return board;
    }

    private void check(final SudokuBoard board, final int[][] solution) {
        assertTrue(board.isSolved());
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y)
                assertEquals(board.get(x, y).intValue(), solution[x][y]);
    }

    @Test
    @DisplayName("Solve simple board")
    void solveSimpleBoardTest() {
        final int[][] seed = {
                {1, 0, 0, /**/ 0, 8, 9, /**/ 4, 5, 7},
                {7, 3, 8, /**/ 0, 0, 0, /**/ 0, 0, 0},
                {0, 4, 0, /**/ 0, 1, 0, /**/ 0, 0, 0},
                /*----------------------------------*/
                {0, 0, 4, /**/ 0, 5, 0, /**/ 9, 0, 6},
                {0, 0, 0, /**/ 0, 0, 0, /**/ 0, 0, 0},
                {0, 0, 0, /**/ 0, 0, 0, /**/ 7, 2, 8},
                /*----------------------------------*/
                {0, 8, 0, /**/ 0, 0, 1, /**/ 0, 0, 0},
                {0, 0, 7, /**/ 0, 0, 8, /**/ 0, 9, 5},
                {0, 6, 0, /**/ 0, 9, 0, /**/ 3, 0, 0}
        };

        final int[][] solution = {
                {1, 2, 6, /**/ 3, 8, 9, /**/ 4, 5, 7},
                {7, 3, 8, /**/ 4, 2, 5, /**/ 1, 6, 9},
                {5, 4, 9, /**/ 6, 1, 7, /**/ 8, 3, 2},
                /*----------------------------------*/
                {3, 7, 4, /**/ 8, 5, 2, /**/ 9, 1, 6},
                {8, 9, 2, /**/ 1, 7, 6, /**/ 5, 4, 3},
                {6, 5, 1, /**/ 9, 4, 3, /**/ 7, 2, 8},
                /*----------------------------------*/
                {9, 8, 3, /**/ 5, 6, 1, /**/ 2, 7, 4},
                {4, 1, 7, /**/ 2, 3, 8, /**/ 6, 9, 5},
                {2, 6, 5, /**/ 7, 9, 4, /**/ 3, 8, 1}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertTrue(solver.solve());

        check(board, solution);
    }

    @Test
    @DisplayName("Solve easy board")
    void solveEasyBoardTest() {
        final int[][] seed = {
                {0, 0, 4, /**/ 0, 0, 9, /**/ 0, 0, 3},
                {0, 6, 0, /**/ 0, 4, 0, /**/ 8, 2, 0},
                {7, 0, 0, /**/ 3, 5, 0, /**/ 0, 0, 4},
                /*----------------------------------*/
                {0, 0, 0, /**/ 9, 0, 2, /**/ 0, 6, 0},
                {2, 0, 7, /**/ 0, 0, 0, /**/ 0, 0, 0},
                {0, 0, 0, /**/ 0, 0, 3, /**/ 1, 0, 0},
                /*----------------------------------*/
                {0, 0, 0, /**/ 5, 2, 0, /**/ 0, 9, 0},
                {0, 0, 8, /**/ 0, 3, 0, /**/ 0, 0, 0},
                {0, 0, 0, /**/ 0, 0, 0, /**/ 0, 0, 7}
        };

        final int[][] solution = {
                {1, 5, 4, /**/ 2, 8, 9, /**/ 6, 7, 3},
                {3, 6, 9, /**/ 7, 4, 1, /**/ 8, 2, 5},
                {7, 8, 2, /**/ 3, 5, 6, /**/ 9, 1, 4},
                /*----------------------------------*/
                {5, 4, 3, /**/ 9, 1, 2, /**/ 7, 6, 8},
                {2, 1, 7, /**/ 8, 6, 5, /**/ 4, 3, 9},
                {8, 9, 6, /**/ 4, 7, 3, /**/ 1, 5, 2},
                /*----------------------------------*/
                {4, 7, 1, /**/ 5, 2, 8, /**/ 3, 9, 6},
                {9, 2, 8, /**/ 6, 3, 7, /**/ 5, 4, 1},
                {6, 3, 5, /**/ 1, 9, 4, /**/ 2, 8, 7}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertTrue(solver.solve());

        check(board, solution);
    }

    @Test
    @DisplayName("Solve medium board")
    void solveMediumBoardTest() {
        final int[][] seed = {
                {0, 0, 0, /**/ 0, 0, 0, /**/ 0, 0, 0},
                {1, 0, 0, /**/ 0, 0, 6, /**/ 0, 0, 2},
                {8, 3, 2, /**/ 0, 0, 0, /**/ 1, 0, 0},
                /*----------------------------------*/
                {9, 0, 0, /**/ 0, 0, 8, /**/ 0, 0, 0},
                {0, 0, 7, /**/ 0, 0, 0, /**/ 0, 4, 0},
                {0, 0, 3, /**/ 0, 4, 7, /**/ 8, 9, 1},
                /*----------------------------------*/
                {0, 0, 0, /**/ 9, 6, 0, /**/ 5, 0, 0},
                {0, 0, 4, /**/ 0, 0, 0, /**/ 0, 0, 6},
                {0, 0, 5, /**/ 0, 3, 0, /**/ 2, 0, 0}
        };

        final int[][] solution = {
                {4, 7, 6, /**/ 1, 2, 3, /**/ 9, 5, 8},
                {1, 5, 9, /**/ 8, 7, 6, /**/ 4, 3, 2},
                {8, 3, 2, /**/ 4, 9, 5, /**/ 1, 6, 7},
                /*----------------------------------*/
                {9, 4, 1, /**/ 6, 5, 8, /**/ 7, 2, 3},
                {2, 8, 7, /**/ 3, 1, 9, /**/ 6, 4, 5},
                {5, 6, 3, /**/ 2, 4, 7, /**/ 8, 9, 1},
                /*----------------------------------*/
                {3, 2, 8, /**/ 9, 6, 1, /**/ 5, 7, 4},
                {7, 9, 4, /**/ 5, 8, 2, /**/ 3, 1, 6},
                {6, 1, 5, /**/ 7, 3, 4, /**/ 2, 8, 9}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertTrue(solver.solve());

        check(board, solution);
    }

    @Test
    @DisplayName("Solve hard board")
    void solveHardBoardTest() {
        final int[][] seed = {
                {0, 1, 0, /**/ 9, 4, 0, /**/ 0, 7, 0},
                {0, 0, 0, /**/ 0, 0, 6, /**/ 0, 1, 0},
                {0, 8, 7, /**/ 0, 0, 1, /**/ 0, 9, 0},
                /*----------------------------------*/
                {0, 2, 0, /**/ 4, 0, 0, /**/ 7, 0, 0},
                {8, 0, 0, /**/ 0, 0, 0, /**/ 0, 0, 1},
                {0, 0, 4, /**/ 0, 0, 8, /**/ 0, 2, 0},
                /*----------------------------------*/
                {0, 6, 0, /**/ 8, 0, 0, /**/ 4, 5, 0},
                {0, 7, 0, /**/ 2, 0, 0, /**/ 0, 0, 0},
                {0, 9, 0, /**/ 0, 6, 7, /**/ 0, 8, 3}
        };

        final int[][] solution = {
                {5, 1, 2, /**/ 9, 4, 3, /**/ 8, 7, 6},
                {9, 4, 3, /**/ 7, 8, 6, /**/ 5, 1, 2},
                {6, 8, 7, /**/ 5, 2, 1, /**/ 3, 9, 4},
                /*----------------------------------*/
                {1, 2, 6, /**/ 4, 9, 5, /**/ 7, 3, 8},
                {8, 5, 9, /**/ 3, 7, 2, /**/ 6, 4, 1},
                {7, 3, 4, /**/ 6, 1, 8, /**/ 9, 2, 5},
                /*----------------------------------*/
                {2, 6, 1, /**/ 8, 3, 9, /**/ 4, 5, 7},
                {3, 7, 8, /**/ 2, 5, 4, /**/ 1, 6, 9},
                {4, 9, 5, /**/ 1, 6, 7, /**/ 2, 8, 3}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertTrue(solver.solve());

        check(board, solution);
    }

    @Test
    @DisplayName("Solve extreme board")
    void solveExtremeBoardTest() {
        final int[][] seed = {
                {8, 0, 0, /**/ 0, 0, 0, /**/ 0, 0, 0},
                {0, 0, 3, /**/ 6, 0, 0, /**/ 0, 0, 0},
                {0, 7, 0, /**/ 0, 9, 0, /**/ 2, 0, 0},
                /*----------------------------------*/
                {0, 5, 0, /**/ 0, 0, 7, /**/ 0, 0, 0},
                {0, 0, 0, /**/ 0, 4, 5, /**/ 7, 0, 0},
                {0, 0, 0, /**/ 1, 0, 0, /**/ 0, 3, 0},
                /*----------------------------------*/
                {0, 0, 1, /**/ 0, 0, 0, /**/ 0, 6, 8},
                {0, 0, 8, /**/ 5, 0, 0, /**/ 0, 1, 0},
                {0, 9, 0, /**/ 0, 0, 0, /**/ 4, 0, 0}
        };

        final int[][] solution = {
                {8, 1, 2, /**/ 7, 5, 3, /**/ 6, 4, 9},
                {9, 4, 3, /**/ 6, 8, 2, /**/ 1, 7, 5},
                {6, 7, 5, /**/ 4, 9, 1, /**/ 2, 8, 3},
                /*----------------------------------*/
                {1, 5, 4, /**/ 2, 3, 7, /**/ 8, 9, 6},
                {3, 6, 9, /**/ 8, 4, 5, /**/ 7, 2, 1},
                {2, 8, 7, /**/ 1, 6, 9, /**/ 5, 3, 4},
                /*----------------------------------*/
                {5, 2, 1, /**/ 9, 7, 4, /**/ 3, 6, 8},
                {4, 3, 8, /**/ 5, 2, 6, /**/ 9, 1, 7},
                {7, 9, 6, /**/ 3, 1, 8, /**/ 4, 5, 2}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertTrue(solver.solve());

        check(board, solution);
    }

    @Test
    @DisplayName("Fail on unsolvable board")
    void failUnsolvableBoardTest() {
        final int[][] seed = {
                {6, 1, 0, /**/ 9, 4, 0, /**/ 0, 7, 0},
                {0, 0, 0, /**/ 0, 0, 6, /**/ 0, 1, 0},
                {0, 8, 7, /**/ 0, 0, 1, /**/ 0, 9, 0},
                /*----------------------------------*/
                {0, 2, 0, /**/ 4, 0, 0, /**/ 7, 0, 0},
                {8, 0, 0, /**/ 0, 0, 0, /**/ 0, 0, 1},
                {0, 0, 4, /**/ 0, 0, 8, /**/ 0, 2, 0},
                /*----------------------------------*/
                {0, 6, 0, /**/ 8, 0, 0, /**/ 4, 5, 0},
                {0, 7, 0, /**/ 2, 0, 0, /**/ 0, 0, 0},
                {0, 9, 0, /**/ 0, 6, 7, /**/ 0, 8, 3}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertFalse(solver.solve());
    }

    @Test
    @DisplayName("Fail on board with multiple solutions")
    void failMultipleSolutionBoardTest() {
        final int[][] seed = {
                {0, 8, 0, /**/ 0, 0, 9, /**/ 7, 4, 3},
                {0, 5, 0, /**/ 0, 0, 8, /**/ 0, 1, 0},
                {0, 1, 0, /**/ 0, 0, 0, /**/ 0, 0, 0},
                /*----------------------------------*/
                {8, 0, 0, /**/ 0, 0, 5, /**/ 0, 0, 0},
                {0, 0, 0, /**/ 8, 0, 4, /**/ 0, 0, 0},
                {0, 0, 0, /**/ 3, 0, 0, /**/ 0, 0, 6},
                /*----------------------------------*/
                {0, 0, 0, /**/ 0, 0, 0, /**/ 0, 7, 0},
                {0, 3, 0, /**/ 5, 0, 0, /**/ 0, 8, 0},
                {9, 7, 2, /**/ 4, 0, 0, /**/ 0, 5, 0}
        };

        final var board = make(seed);

        final var solver = new SudokuCP(board);
        assertFalse(solver.solve());
    }
}
