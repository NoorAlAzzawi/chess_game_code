package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;
import oopchesscode.player.Participant;

import java.util.ArrayList;
import java.util.List;

public class KnightPiece extends BasePiece {

    private static final int[][] KNIGHT_OFFSETS = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
    };

    public KnightPiece(boolean isWhite, Square square) {
        super(isWhite, square);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Square target, Participant player) {
        if (target == null || !board.onBoard(target)) {
            return false;
        }
        if (target.equals(square)) {
            return false;
        }
        int dr = Math.abs(target.getRankIndex() - square.getRankIndex());
        int dc = Math.abs(target.getFileIndex() - square.getFileIndex());
        if (dr * dc != 2) {
            return false;
        }
        BasePiece dest = board.pieceAt(target);
        return dest == null || dest.isLight() != this.isWhite;
    }

    @Override
    public List<Square> possibleMoves(ChessBoard board) {
        final int r0 = square.getRankIndex();
        final int f0 = square.getFileIndex();
        final List<Square> moves = new ArrayList<>(8);

        for (int[] d : KNIGHT_OFFSETS) {
            final int r = r0 + d[0];
            final int f = f0 + d[1];
            if (r < 0 || r >= 8 || f < 0 || f >= 8) {
                continue;
            }
            final Square pos = new Square(r, f);
            final BasePiece other = board.pieceAt(pos);
            if (other == null || other.isLight() != this.isWhite) {
                moves.add(pos);
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return isWhite ? "n" : "N";
    }
}
