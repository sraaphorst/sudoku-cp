// SudokuCP.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.sudoku;

/**
 * Takes a Sudoku problem, represents it as a CP, and then allows solving.
 */
@SuppressWarnings("WeakerAccess")
public final class SudokuCP extends GenSudokuCP<Integer> {
    @SuppressWarnings("WeakerAccess")
    public SudokuCP(SudokuBoard sudokuBoard) {
        super(sudokuBoard);
    }
}
