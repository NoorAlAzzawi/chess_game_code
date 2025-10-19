package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;
import oopchesscode.player.Participant;

import java.util.ArrayList;
import java.util.List;

public class RookPiece extends BasePiece {

    public RookPiece(boolean isWhite, Square square)
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

        if (sr != tr && sf != tf) return false;

        final int rStep = Integer.compare(tr, sr);
        final int fStep = Integer.compare(tf, sf);

        for (int r = sr + rStep, f = sf + fStep; r != tr || f != tf; r += rStep, f += fStep) {
            if (!board.isEmpty(new Square(r, f))) return false;
        }

        BasePiece dest = board.pieceAt(target);
        return dest == null || dest.isLight() != this.isWhite;
    }

    private static final int[][] ROOK_DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    @Override
    public List<Square> possibleMoves(ChessBoard board) {
        final List<Square> moves = new ArrayList<>(14);
        final int r0 = square.getRankIndex(), f0 = square.getFileIndex();

        for (int[] d : ROOK_DIRS) {
            int r = r0 + d[0], f = f0 + d[1];
            while (r >= 0 && r < 8 && f >= 0 && f < 8) {
                final Square pos = new Square(r, f);
                final BasePiece other = board.pieceAt(pos);
                if (other == null) {
                    moves.add(pos);
                } else {
                    if (other.isLight() != this.isWhite) moves.add(pos);
                    break;
                }
                r += d[0];
                f += d[1];
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return isWhite ? "R" : "r";
    }
}

