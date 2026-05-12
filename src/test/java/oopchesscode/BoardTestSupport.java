package oopchesscode;

import oopchesscode.board.ChessBoard;
import oopchesscode.pieces.BasePiece;
import oopchesscode.player.Participant;
import oopchesscode.position.Square;
import oopchesscode.utils.ChessUtil;

/**
 * Helpers for building minimal positions in tests.
 */
public final class BoardTestSupport {

    private BoardTestSupport() {
    }

    public static Square sq(String algebraic) {
        Square s = ChessUtil.parseSquare(algebraic);
        if (s == null) {
            throw new IllegalArgumentException("Bad square: " + algebraic);
        }
        return s;
    }

    public static void place(ChessBoard board, Participant owner, BasePiece piece) {
        owner.getActivePieces().add(piece);
        board.placePiece(piece.getSquare(), piece);
    }

    public static void clearBoard(ChessBoard board, Participant white, Participant black) {
        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                board.placePiece(new Square(r, f), null);
            }
        }
        white.getActivePieces().clear();
        black.getActivePieces().clear();
    }
}
