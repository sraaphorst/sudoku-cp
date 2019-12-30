# Sudoku Constraint Programming with Choco-Solver

**Status:** Complete, verson 1.0.0.


This is an implementation, in Java 10, of a Sudoku solver using constraint programming.
The intention of this project was to play around with constraint programming using
Choco-Solver (http://www.choco-solver.org).

The program supports fully generic Sudoku boards (i.e. `n^2 x n^2` boards over `n^2`
arbitrary symbols), but has an easy-to-use implementation of the standard `9x9`
Sudoku board over the digits `1` through `9`.

The main classes as are follows:

* [`GenSudokuBoard`](src/main/java/com/vorpal/sudoku/GenSudokuBoard.java): The generic
implementation of an arbitrary Sudoku board.

* [`GenSudokuCP`](src/main/java/com/vorpal/sudoku/GenSudokuCP.java): The solver,
which takes a `GenSudokuBoard`, creates a constraint program from it, and then
determines if there is a unique solution and, if so, what that solution is.

* [`SudokuBoard`](src/main/java/com/vorpal/sudoku/SudokuBoard.java): Represents a
standard `9x9` Sudoku board: it is a subclass of `GenSudokuBoard` but handles most
of the details so that the interface is simple to use.

* [`SudokuCP`](src/main/java/com/vorpal/sudoku/SudokuCP.java): The solver for
`SudokuBoard`, and a subclass of `GenSudokuCP`.


Examples of how to use these classes can be found in the test cases, which are
designed specifically for `SudokuBoard` and `SudokuCP`:

* [`TestSudokuBoard`](src/test/java/com/vorpal/sudoku/TestSudokuBoard.java):
Tests for `SudokuBoard`, which provide examples as to how to use the functionality
of `SudokuBoard`.

* [`TestSudokuCP`](src/test/java/com/vorpal/sudoku/TestSudokuCP.java):
Tests for `SudokuCP`, which show how to invoke the constraint programming solving
algorithm on a board.
