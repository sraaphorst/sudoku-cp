// TestSudokuBoard.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.sudoku;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TestSudokuBoard {
    private final static SudokuBoard emptyBoard      = new SudokuBoard();
    private final static SudokuBoard invalidBoard    = new SudokuBoard();
    private final static SudokuBoard validBoard      = new SudokuBoard();
    private final static SudokuBoard incompleteBoard = new SudokuBoard();
    private final static SudokuBoard completeBoard   = new SudokuBoard();
    private final static SudokuBoard solvedBoard     = new SudokuBoard();

    // All the non-empty boards, for easy resetting.
    private final static List<SudokuBoard> boards = new ArrayList<>(5);
    static {
        Collections.addAll(boards, invalidBoard, validBoard, incompleteBoard, completeBoard, solvedBoard);
    }

    private final static int[][] boardArray = {
            {8, 2, 7, /**/ 1, 5, 4, /**/ 3, 9, 6},
            {9, 6, 5, /**/ 3, 2, 7, /**/ 1, 4, 8},
            {3, 4, 1, /**/ 6, 8, 9, /**/ 7, 5, 2},
            /*----------------------------------*/
            {5, 9, 3, /**/ 4, 6, 8, /**/ 2, 7, 1},
            {4, 7, 2, /**/ 5, 1, 3, /**/ 6, 8, 9},
            {6, 1, 8, /**/ 9, 7, 2, /**/ 4, 3, 5},
            /*----------------------------------*/
            {7, 8, 6, /**/ 2, 3, 5, /**/ 9, 1, 4},
            {1, 5, 4, /**/ 7, 9, 6, /**/ 8, 2, 3},
            {2, 3, 9, /**/ 8, 4, 1, /**/ 5, 6, 7}
    };

    @BeforeEach
    void setUp() {
        // Set the contents for each board. Expensive and unnecessary in most cases, but it ensures consistency.
        emptyBoard.clear();
        invalidBoard.clear();
        validBoard.clear();
        incompleteBoard.clear();
        completeBoard.clear();
        solvedBoard.clear();

        // Set the contents of all boards to a solved board.
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y)
                for (final var board: boards)
                    board.set(board.new Pair(x, y), boardArray[x][y]);

        // Now make additional changes so that each board has its desired characteristics.
        // invalidBoard is invalid and incomplete.
        invalidBoard.set(4, 3, 1);
        invalidBoard.set(1, 1, 0);

        // validBoard is valid but incomplete.
        validBoard.set(1, 1, 0);

        // incompleteBoard is invalid and incomplete.
        incompleteBoard.set(7, 6, 0);
        incompleteBoard.set(0, 8, 8);

        // completeBoard is invalid but complete.
        completeBoard.set(2, 5, 6);
    }

    @Test
    @DisplayName("SudokuBoard.get")
    void getTest() {
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y)
                assertEquals(solvedBoard.get(x, y).intValue(), boardArray[x][y]);
    }

    @Test
    @DisplayName("SudokuBoard.set")
    void setTest() {
        // Take the empty board and set it, coordinate by coordinate, to the solved board.
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y) {
                assertEquals(emptyBoard.get(x, y).intValue(), 0);
                assertNotEquals(emptyBoard.get(x, y), boardArray[x][y]);
                emptyBoard.set(x, y, boardArray[x][y]);
                assertEquals(emptyBoard.get(x, y).intValue(), boardArray[x][y]);
            }
    }

    @Test
    @DisplayName("SudokuBoard.clear")
    void clearTest() {
        // Take the complete board, clear it, and make sure every entry is zero.
        completeBoard.clear();
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y)
                assertEquals(completeBoard.get(x, y).intValue(), 0);
    }

    @Test
    @DisplayName("SudokuBoard.equals and SudokuBoard.copyFrom")
    void equalsCopyTest() {
        assertEquals(solvedBoard, solvedBoard);
        assertEquals(emptyBoard, emptyBoard);

        assertNotEquals(emptyBoard, solvedBoard);
        solvedBoard.clear();
        assertEquals(emptyBoard, solvedBoard);

        assertNotEquals(solvedBoard, incompleteBoard);
        solvedBoard.copyFrom(incompleteBoard);
        assertNotEquals(solvedBoard, emptyBoard);
        assertEquals(solvedBoard, incompleteBoard);

        assertNotEquals(emptyBoard, incompleteBoard);
        incompleteBoard.copyFrom(emptyBoard);
        assertEquals(emptyBoard, incompleteBoard);
        assertNotEquals(solvedBoard, incompleteBoard);
    }

    @Test
    @DisplayName("SudokuBoard.isValid methods")
    void validTest() {
        assertTrue(solvedBoard.isValid());
        assertTrue(emptyBoard.isValid());
        assertTrue(validBoard.isValid());
        assertFalse(invalidBoard.isValid());

        // The invalidBoard is only invalid in the middle section, at (4, 3).
        for (var x = 0; x < 9; ++x) {
            assertTrue(validBoard.isRowValid(x));
            assertTrue(validBoard.isColumnValid(x));

            if (x == 4)
                assertTrue(invalidBoard.isColumnValid(x));
            if (x == 3)
                assertTrue(invalidBoard.isRowValid(x));

        }
        assertFalse(invalidBoard.isRowValid(4));
        assertFalse(invalidBoard.isColumnValid(3));

        for (var x = 0; x < 3; ++x)
            for (var y = 0; y < 3; ++y) {
                assertTrue(validBoard.isSectionValid(x, y));
                if (x == 1 && y == 1) continue;
                assertTrue(invalidBoard.isSectionValid(x, y));
            }
        assertFalse(invalidBoard.isSectionValid(1, 1));
    }

    @Test
    @DisplayName("SudokuBoard.isComplete methods")
    void completeTest() {
        assertFalse(emptyBoard.isComplete());
        assertTrue(solvedBoard.isComplete());
        for (var x = 0; x < 9; ++x)
            for (var y = 0; y < 9; ++y)
                if (completeBoard.get(x, y) == 0)
                    System.out.println(completeBoard.get(x, y));
        assertTrue(completeBoard.isComplete());
        assertFalse(incompleteBoard.isComplete());

        // The incompleteBoard is only incomplete in the bottom right, at (7, 6).
        for (var x = 0; x < 9; ++x) {
            assertTrue(completeBoard.isRowComplete(x));
            assertTrue(completeBoard.isColumnComplete(x));

            if (x == 7)
                assertTrue(incompleteBoard.isColumnComplete(x));
            if (x == 6)
                assertTrue(incompleteBoard.isRowComplete(x));

        }
        assertFalse(incompleteBoard.isRowComplete(7));
        assertFalse(incompleteBoard.isColumnComplete(6));

        for (var x = 0; x < 3; ++x)
            for (var y = 0; y < 3; ++y) {
                assertTrue(completeBoard.isSectionComplete(x, y));
                if (x == 2 && y == 2) continue;
                assertTrue(incompleteBoard.isSectionComplete(x, y));
            }
        assertFalse(incompleteBoard.isSectionComplete(2, 2));
    }

    @Test
    @DisplayName("SudokuBoard.isSolved")
    void solvedTest() {
        assertFalse(emptyBoard.isSolved());
        assertFalse(invalidBoard.isSolved());
        assertFalse(validBoard.isSolved());
        assertFalse(incompleteBoard.isSolved());
        assertFalse(completeBoard.isSolved());
        assertTrue(solvedBoard.isSolved());
    }
}
