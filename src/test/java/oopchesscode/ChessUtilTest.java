package oopchesscode;

import oopchesscode.position.Square;
import oopchesscode.utils.ChessUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChessUtilTest {

    @Test
    void validSquareInputs() {
        assertTrue(ChessUtil.isValidSquareInput("e4"));
        assertTrue(ChessUtil.isValidSquareInput("A1"));
        assertTrue(ChessUtil.isValidSquareInput(" h8 "));
    }

    @Test
    void invalidSquareInputs() {
        assertFalse(ChessUtil.isValidSquareInput(null));
        assertFalse(ChessUtil.isValidSquareInput(""));
        assertFalse(ChessUtil.isValidSquareInput("e"));
        assertFalse(ChessUtil.isValidSquareInput("e9"));
        assertFalse(ChessUtil.isValidSquareInput("i4"));
    }

    @Test
    void parseSquareRoundTrip() {
        Square e4 = ChessUtil.parseSquare("e4");
        assertNotNull(e4);
        assertEquals(3, e4.getRankIndex());
        assertEquals(4, e4.getFileIndex());
    }

    @Test
    void parseSquareRejectsInvalid() {
        assertNull(ChessUtil.parseSquare("z1"));
        assertNull(ChessUtil.parseSquare(null));
    }
}
