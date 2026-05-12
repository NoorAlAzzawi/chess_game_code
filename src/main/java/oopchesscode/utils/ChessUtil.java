package oopchesscode.utils;

import oopchesscode.position.Square;

public final class ChessUtil {

    private ChessUtil() {
    }

    public static boolean isValidSquareInput(String input) {
        if (input == null) {
            return false;
        }
        input = input.trim();
        if (input.length() != 2) {
            return false;
        }
        char file = Character.toUpperCase(input.charAt(0));
        char rank = input.charAt(1);
        return file >= 'A' && file <= 'H' && rank >= '1' && rank <= '8';
    }

    /**
     * Parses algebraic notation (e.g. "E2"). Caller should use {@link #isValidSquareInput} first.
     */
    public static Square parseSquare(String input) {
        if (!isValidSquareInput(input)) {
            return null;
        }
        char fileChar = Character.toUpperCase(input.charAt(0));
        char rankChar = input.charAt(1);
        int rankIndex = Character.getNumericValue(rankChar) - 1;
        int fileIndex = fileChar - 'A';
        return new Square(rankIndex, fileIndex);
    }
}
