
package oopchesscode.utils;

import oopchesscode.position.Square;

public class ChessUtil {

    public static boolean isValidSquareInput(String input) 
    {
        if (input == null) return false;
        input = input.trim();           
        if (input.length() != 2) return false;
        char file = Character.toUpperCase(input.charAt(0));
        char rank = input.charAt(1);
        return file >= 'A' && file <= 'H' && rank >= '1' && rank <= '8';
    }
    


    public static Square parseSquare(String input)
     {
        char fileIndex = Character.toUpperCase(input.charAt(0));
        char rankIndex = input.charAt(1);
        int x = Character.getNumericValue(rankIndex) - 1;
        int y = fileIndex - 'A';
        return new Square(x, y);
    }
}
