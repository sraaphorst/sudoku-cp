// GenSudokuCP.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.sudoku;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

/**
 * Takes a GenSudoku problem, represents it as a constraint program, and then allows solving.
 *
 * NOTE: Intermediate steps here may not be as expected, as entries in the board are represented as integers
 * through a map, thus being a permutation of the valid elements for the puzzle. We reverse the permutation when
 * the solution is confirmed to be achieved, so that writing the solution to the board provides the expected
 * final result.
 */
@SuppressWarnings("WeakerAccess")
public class GenSudokuCP<T> {
    // The board.
    private final GenSudokuBoard<T> sudokuBoard;

    // Side of the board, i.e. # rows and # columns.
    private final int side;

    // Valid entries, in order.
    private final List<T> validEntries;

    // The model of the board as a CP.
    private final Model model;

    // The variables.
    private final IntVar[][] vs;


    public GenSudokuCP(final GenSudokuBoard<T> sudokuBoard) {
        this.sudokuBoard = sudokuBoard;

        model = new Model();

        final var dimensionaity = sudokuBoard.getDimensionality();
        side = dimensionaity * dimensionaity;

        final var zero = sudokuBoard.getZero();
        validEntries = List.copyOf(sudokuBoard.getValidEntries());

        // We want a map between validEntries and integers, as we will model using integers.
        final var tmpMap = new HashMap<T, Integer>();
        tmpMap.put(zero, 0);
        for (int i = 0; i < validEntries.size(); ++i)
            tmpMap.put(validEntries.get(i), i+1);
        final var entryMap = Collections.unmodifiableMap(tmpMap);

        // Now we create one entry for every cell on the board, allowing the value to fall in [1, validEntries.size()].
        vs = model.intVarMatrix(side, side, 1, validEntries.size());

        // Iterate over the board, setting the fixed entries via constraints.
        for (var x = 0; x < side; ++x)
            for (var y = 0; y < side; ++y) {
                final var c = sudokuBoard.get(x, y);
                if (!c.equals(zero))
                    model.arithm(vs[x][y], "=", entryMap.get(c)).post();
            }

        // Add the row and column constraints.
        for (var x = 0; x < side; ++x) {
            final var row = new IntVar[side];
            final var column = new IntVar[side];
            for (var y = 0; y < side; ++y) {
                row[y] = vs[x][y];
                column[y] = vs[y][x];
            }
            model.allDifferent(row).post();
            model.allDifferent(column).post();
        }

        // Add the section constraints.
        for (var x = 0; x < dimensionaity; ++x) {
            for (var y = 0; y < dimensionaity; ++y) {
                final var section = new IntVar[side];
                int idx = 0;
                for (var xoff = 0; xoff < dimensionaity; ++xoff)
                    for (var yoff = 0; yoff < dimensionaity; ++yoff)
                        section[idx++] = vs[x * dimensionaity + xoff][y * dimensionaity + yoff];
                model.allDifferent(section).post();
            }
        }
    }

    public boolean solve() {
        // We want there to be exactly one solution and one solution only.
        // Thus calls to solve should return first true, then false.
        final var solved = model.getSolver().solve();
        if (!solved) return false;

        // Store the solution temporarily.
        // We convert it and copy it over to the board when we know the solution is unique.
        final var sol = new ArrayList<ArrayList<Integer>>(side);
        for (var x = 0; x < side; ++x) {
            final var row = new ArrayList<Integer>();
            for (var y = 0; y < side; ++y)
                // Remove 1 from value so we correspond directly to validEntries.
                row.add(vs[x][y].getValue() - 1);
            sol.add(row);
        }

        // Now make sure there are no more solutions.
        final var moreSolutions = model.getSolver().solve();
        if (moreSolutions)
            return false;

        // Translate and copy the solution to the board.
        for (var x = 0; x < side; ++x)
            for (var y = 0; y < side; ++y)
                sudokuBoard.set(x, y, validEntries.get(sol.get(x).get(y)));

        return true;
    }
}
