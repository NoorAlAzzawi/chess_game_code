package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;
import oopchesscode.player.Participant;

import java.util.ArrayList;
import java.util.List;

public class PawnPiece extends BasePiece {

    public PawnPiece(boolean isWhite, Square square) {
        super(isWhite, square);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Square target, Participant player) {
        if (target == null || !board.onBoard(target)) return false;

        final int sr = square.getRankIndex(), sf = square.getFileIndex();
        final int tr = target.getRankIndex(), tf = target.getFileIndex();
        final int dr = tr - sr, df = tf - sf;

        // White advances toward rank 8 (increasing index); Black toward rank 1 (decreasing).
        final int dir = isWhite ? 1 : -1;
        final int startRank = isWhite ? 1 : 6;

        if (dr == 0 && df == 0) return false;
        if (df == 0 && dr == dir && board.isEmpty(target)) return true;

        if (df == 0 && dr == 2 * dir && sr == startRank) {
            Square middle = new Square(sr + dir, sf);
            return board.isEmpty(middle) && board.isEmpty(target);
        }

        if (Math.abs(df) == 1 && dr == dir) {
            BasePiece tp = board.pieceAt(target);
            return tp != null && tp.isLight() != this.isWhite;
        }

        return false;
    }

    @Override
    public List<Square> possibleMoves(ChessBoard board) {
        final List<Square> moves = new ArrayList<>(4);
        final int r0 = square.getRankIndex();
        final int f0 = square.getFileIndex();
        // White advances toward rank 8 (increasing index); Black toward rank 1 (decreasing).
        final int dir = isWhite ? 1 : -1;
        final int startRank = isWhite ? 1 : 6;

        final Square oneAhead = new Square(r0 + dir, f0);
        if (board.onBoard(oneAhead) && board.isEmpty(oneAhead)) {
            moves.add(oneAhead);

            if (r0 == startRank) {
                final Square twoAhead = new Square(r0 + 2 * dir, f0);
                if (board.onBoard(twoAhead) && board.isEmpty(twoAhead)) {
                    moves.add(twoAhead);
                }
            }
        }

        for (int df : new int[]{-1, 1}) {
            final Square diag = new Square(r0 + dir, f0 + df);
            if (board.onBoard(diag)) {
                final BasePiece p = board.pieceAt(diag);
                if (p != null && p.isLight() != this.isWhite) {
                    moves.add(diag);
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return isWhite ? "p" : "P";
    }
}
