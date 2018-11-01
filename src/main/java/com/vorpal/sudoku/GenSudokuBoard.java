/**
 * GenSudokuBoard.java
 *
 * By Sebastian Raaphorst, 2018.
 */

package com.vorpal.sudoku;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class GenSudokuBoard<T> {
    // This is truly horrible. Why doesn't Java have pairs or tuples?
    public class Pair {
        final int x, y;
        public Pair(int x, int y) {
            if (x < 0 || x >= numDigits || y < 0 || y >= numDigits)
                throw new IllegalArgumentException("Illegal coordinates: " + pairToString(x, y));
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return pairToString(x, y);
        }
    }

    // The default dimensionality of the board is 3, which indicates a normal 3*3 x 3*3 = 9 x 9 Sudoku board.
    public static int DEFAULT_DIMENSIONALITY = 3;

    // The dimensionality of the board, which is the square root of the number of digits.
    private final int dimensionality;
    private final int numDigits;
    private final ArrayList<ArrayList<T>> board;

    // The values of T which appear as valid entries on the board, minus the special zero element.
    private final Set<T> validEntries;

    // The special instance of T representing zero.
    private final T zero;

    /**
     * Create a generic dimensionality^2 x dimensionality^2 Sudoku board with dimensionality x dimensionality subgrids.
     * @param dimensionality The dimensionality of the board: should be the square root of the number of entries.
     * @param zero The placemarker used on the board to mark an unsolved position.
     * @param validEntries The valid entries that can appear in the board.
     *                     If this contains the zero element, it is removed.
     *                     The set must have size dimensionality^2.
     */
    GenSudokuBoard(final int dimensionality, final T zero, final Set<T> validEntries) {
        Objects.requireNonNull(validEntries);

        if (dimensionality <= 0)
            throw new IllegalArgumentException("dimensionality must be a positive integer");

        this.dimensionality = dimensionality;
        this.numDigits = dimensionality * dimensionality;
        this.zero = zero;
        this.validEntries = validEntries.stream()
                .filter(x -> !zero.equals(x))
                .collect(Collectors.toUnmodifiableSet());

        if (validEntries.size() != numDigits)
            throw new IllegalArgumentException("validEntries must contain " + numDigits + " digits");

        // There may be a nicer way to do this, as it's not ideal.
        board = new ArrayList<>(numDigits);
        var empty = Collections.nCopies(numDigits, zero);
        for (var i = 0; i < numDigits; ++i) {
            final var row = new ArrayList<T>(numDigits);
            row.addAll(empty);
            board.set(i, row);
        }
    }

    /**
     * Get the value at a position on the board.
     * @param x x coordinate
     * @param y y coordinate
     * @return the value at that position
     */
    public T get(int x, int y) {
        return get(new Pair(x, y));
    }

    /**
     * Get the value at a position on the board.
     * @param p the coordinate pair
     * @return the value at that position
     */
    public T get(final Pair p) {
        return board.get(p.x).get(p.y);
    }

    /**
     * Set the value at a position on the board.
     * Note that this method does not check if setting the position results in a legal configuration.
     * @param p the position
     * @param value the value to set
     * @return the old value
     */
    public T set(final Pair p, final T value) {
        if (!validEntries.contains(value))
            throw new IllegalArgumentException("Value not legal: " + value);
        final T old = board.get(p.x).get(p.y);
        board.get(p.x).set(p.y, value);
        return old;
    }

    /**
     * Check if a row is complete.
     * @param row the row
     * @return true if complete, and false otherwise
     */
    public boolean isRowComplete(final int row) {
        return checkRowProperty(row, this::isCompleteArea);
    }

    /**
     * Check if a column is complete.
     * @param column the column
     * @return true if complete, and false otherwise
     */
    public boolean isColumnComplete(final int column) {
        return checkColumnProperty(column, this::isCompleteArea);
    }

    /**
     * Check if a section is complete.
     * @param x the x coordinate of the section
     * @param y the y coordinate of the section
     * @return true if complete, and false otherwise
     */
    public boolean isSectionComplete(final int x, final int y) {
        return checkSectionProperty(x, y, this::isCompleteArea);
    }

    /**
     * Check if a row is valid.
     * @param row the row
     * @return true if valid, and false otherwise
     */
    public boolean isRowValid(final int row) {
        return checkRowProperty(row, this::isValidArea);
    }

    /**
     * Check if a column is valid.
     * @param column the column
     * @return true if valid, and false otherwise
     */
    public boolean isColumnValid(final int column) {
        return checkColumnProperty(column, this::isValidArea);
    }

    /**
     * Check if a section is valid.
     * @param x the x coordinate of the section
     * @param y the y coordinate of the section
     * @return true if valid, and false otherwise
     */
    public boolean isSectionValid(final int x, final int y) {
        return checkSectionProperty(x, y, this::isValidArea);
    }

    /**
     * Check if the board is complete, i.e. all elements are valid, and there are no zero elements.
     * @return true if complete, and false otherwise
     */
    public boolean isComplete() {
        return checkProperty(this::isCompleteArea);
    }

    /**
     * Check if the board is valid, i.e. there is no illegal placement of elements.
     * @return true is valid, and false otherwise
     */
    public boolean isValid() {
        return checkProperty(this::isValidArea);
    }

    /**
     * Check if the board has been solved, i.e. the board is both complete and valid.
     * @return true if solved, and false otherwise
     */
    public boolean isSolved() {
        return isComplete() && isValid();
    }

    /**
     * Check that a given property holds for every row, column, and section of the board.
     * @param propertyChecker the property to check
     * @return true if the property holds, and false otherwise
     */
    private boolean checkProperty(final Function<List<T>, Boolean> propertyChecker) {
        for (var i = 0; i < numDigits; ++i) {
            if (!checkRowProperty(i, propertyChecker))
                return false;
            if (!checkColumnProperty(i, propertyChecker))
                return false;
        }
        for (var x = 0; x < dimensionality; ++x)
            for (var y = 0; y < dimensionality; ++y)
                if (!checkSectionProperty(x, y, propertyChecker))
                    return false;
        return true;
    }

    private boolean checkRowProperty(final int row, final Function<List<T>, Boolean> propertyChecker) {
        return propertyChecker.apply(extract(rowIterator(row)));
    }

    private boolean checkColumnProperty(final int column, final Function<List<T>, Boolean> propertyChecker) {
        return propertyChecker.apply(extract(columnIterator(column)));
    }

    private boolean checkSectionProperty(final int x, final int y, final Function<List<T>, Boolean> propertyChecker) {
        return propertyChecker.apply(extract(sectionIterator(x, y)));
    }

    /**
     * Extract the values on a part of the board, as dictated by the coordinates of an Iterator.
     * @param sectionIndices the iterator of coordinates
     * @return a list of the values on the board at the specified coordinates
     */
    private List<T> extract(final Iterator<Pair> sectionIndices) {
        Objects.requireNonNull(sectionIndices);

        // We expect the initial capacity to be numDigits.
        final var values = new ArrayList<T>(numDigits);
        sectionIndices.forEachRemaining(p -> values.add(get(p)));
        return Collections.unmodifiableList(values);
    }

    /**
     * Provide an iterator to traverse a row of the board.
     * @param row the row
     * @return an Iterator over the coordinates of the row
     */
    private Iterator<Pair> rowIterator(final int row) {
        checkCoordinate(row);
        return new Iterator<>() {
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < numDigits;
            }

            @Override
            public Pair next() {
                return new Pair(row, pos++);
            }
        };
    }

    /**
     * Provide an iterator to traverse a column of the board.
     * @param col the column
     * @return an Iterator over the coordinates of the column
     */
    private Iterator<Pair> columnIterator(final int col) {
        checkCoordinate(col);
        return new Iterator<>() {
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < numDigits;
            }

            @Override
            public Pair next() {
                return new Pair(pos++, col);
            }
        };
    }

    /**
     * Provide an iterator to traverse a section (one of the subgrids) of the board.
     * @param x the x-coordinate of the section
     * @param y the y-coordinate of the section
     * @return an Iterator over the coordinates of the section
     */
    private Iterator<Pair> sectionIterator(final int x, final int y) {
        if (x < 0 || x >= dimensionality || y < 0 || y >= dimensionality)
            throw new IllegalArgumentException("Illegal quadrant: " + pairToString(x, y));

        return new Iterator<>() {
            int idx = 0;

            @Override
            public boolean hasNext() {
                return idx < numDigits;
            }

            @Override
            public Pair next() {
                int xpos = x * dimensionality + idx % dimensionality;
                int ypos = y * dimensionality + idx / dimensionality;
                ++idx;
                return new Pair(xpos, ypos);
            }
        };
    }

    /**
     * Check a list of elements to see if it is complete.
     * @param lst the list of elements
     * @return true if it is complete, and false otherwise
     */
    private boolean isCompleteArea(final List<T> lst) {
        return lst.containsAll(validEntries) && !lst.contains(zero);
    }

    /**
     * Check a list of elements to see if it is valid, i.e. there are no repeats other than possibly zero,
     * and every element is valid.
     * @param lst the list of elements
     * @return true if valid, and false otherwise
     */
    private boolean isValidArea(final List<T> lst) {
        // Count the zeros.
        final var numZeros = Collections.frequency(lst, zero);

        // Now find the unique list without the zeroes.
        final var unique = new HashSet<>(lst);
        unique.remove(zero);

        return unique.size() + numZeros == lst.size() && validEntries.containsAll(unique);

    }

    /**
     * Check if a coordinate is legal, i.e. it falls within the board.
     * Throw an IllegalArgumentException if it is not.
     * @param c the coordinate
     */
    private void checkCoordinate(final int c) {
        if (c < 0 || c >= numDigits)
            throw new IllegalArgumentException("Illegal coordinate: " + c);
    }

    /**
     * Quick formatting of a pair as a String.
     * @param x x element
     * @param y y element
     * @return a String representing the pair
     */
    private static String pairToString(int x, int y) {
        return String.format("(%d,%d)", x, y);
    }
}
