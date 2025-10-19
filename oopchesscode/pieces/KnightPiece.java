
package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;

import java.util.ArrayList;
import java.util.List;

public class KnightPiece extends BasePiece {
    public KnightPiece(boolean isWhite, Square square) {
        super(isWhite, square);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square target, oopchesscode.player.Participant player) {
        int dr = Math.abs(target.getRankIndex() - square.getRankIndex());
        int dc = Math.abs(target.getFileIndex() - square.getFileIndex());
        return dr * dc == 2;
    }

    private static final int[][] KNIGHT_OFFSETS = {
        { 2,  1}, { 1,  2}, {-1,  2}, {-2,  1},
        {-2, -1}, {-1, -2}, { 1, -2}, { 2, -1}
    };
    
    @Override
    public List<Square> possibleMoves(ChessBoard board) {
        final int r0 = square.getRankIndex();
        final int f0 = square.getFileIndex();
        final List<Square> moves = new ArrayList<>(8);
    
        int i = 0;
        while (i < KNIGHT_OFFSETS.length) {
            final int[] d = KNIGHT_OFFSETS[i];
            final int r = r0 + d[0];
            final int f = f0 + d[1];
            if (r >= 0 && r < 8 && f >= 0 && f < 8) {
                moves.add(new Square(r, f));
            }
            i++;
        }
        return moves;
    }
    

    @Override
    public String toString() {
        return isWhite ? "N" : "n";
    }
}
