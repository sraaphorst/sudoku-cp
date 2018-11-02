// TestSudokuCP.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test SudokuCP using puzzles taken from:
 * http://www.sudoku-solutions.com
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
}
