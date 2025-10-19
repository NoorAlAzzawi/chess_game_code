package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;
import oopchesscode.player.Participant;

import java.util.ArrayList;
import java.util.List;

public class QueenPiece extends BasePiece {

    public QueenPiece(boolean isWhite, Square square) 
    {
        super(isWhite, square);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Square target, Participant player) 
    {
        if (target == null || !board.onBoard(target)) return false;
        if (target.equals(square)) return false;

        final int sr = square.getRankIndex(), sf = square.getFileIndex();
        final int tr = target.getRankIndex(), tf = target.getFileIndex();
        final int dr = tr - sr, df = tf - sf;

        if (dr != 0 && df != 0 && Math.abs(dr) != Math.abs(df)) return false;

        final int rStep = Integer.compare(dr, 0);
        final int fStep = Integer.compare(df, 0);

        for (int r = sr + rStep, f = sf + fStep; r != tr || f != tf; r += rStep, f += fStep) {
            if (!board.isEmpty(new Square(r, f))) return false;
        }

        BasePiece dest = board.pieceAt(target);
        return dest == null || dest.isLight() != this.isWhite;
    }

    private static final int[][] DIRS = {
        {-1,-1}, {-1,0}, {-1,1},
        { 0,-1},         { 0,1},
        { 1,-1}, { 1,0}, { 1,1}
    };

    @Override
    public List<Square> possibleMoves(ChessBoard board) {
        final List<Square> moves = new ArrayList<>(27);
        final int r0 = square.getRankIndex();
        final int f0 = square.getFileIndex();

        for (int[] d : DIRS) {
            int r = r0 + d[0], c = f0 + d[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                final Square pos = new Square(r, c);
                final BasePiece other = board.pieceAt(pos);
                if (other == null) {
                    moves.add(pos);
                } else {
                    if (other.isLight() != this.isWhite) moves.add(pos);
                    break;
                }
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return isWhite ? "Q" : "q";
    }
}
