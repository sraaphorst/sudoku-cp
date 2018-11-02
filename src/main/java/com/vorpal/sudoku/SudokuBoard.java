// SudokuBoard.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.sudoku;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a simplified interface for GenSudokuBoard, in the standard 9x9 configuration over the numbers
 * 1 through 9, with 0 being an indicator that a position has not yet been filled.
 */
@SuppressWarnings("WeakerAccess")
public final class SudokuBoard extends GenSudokuBoard<Integer> {
    private static final Set<Integer> digitSet;
    static {
        final var tmpSet = new HashSet<Integer>(9);
        Collections.addAll(tmpSet, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        digitSet = Collections.unmodifiableSet(tmpSet);
    }

    @SuppressWarnings("WeakerAccess")
    public SudokuBoard() {
        super(3, 0, digitSet);
    }
}
