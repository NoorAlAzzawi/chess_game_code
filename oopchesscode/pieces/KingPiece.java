package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;
import oopchesscode.player.Participant;

import java.util.ArrayList;
import java.util.List;

public class KingPiece extends BasePiece {
    private boolean isKingThreatened;

    public KingPiece(boolean isWhite, Square square) {
        super(isWhite, square);
    }

    public void setCheckFlag(boolean condition) {
        isKingThreatened = condition;
    }

    public boolean isThreatened() {
        return isKingThreatened;
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square target, Participant player) {
        int dr = Math.abs(target.getRankIndex() - square.getRankIndex());
        int dc = Math.abs(target.getFileIndex() - square.getFileIndex());
        if (dr == 0 && dc == 0) return false;
        if (dr > 1 || dc > 1) return false;
        BasePiece targetPiece = chessBoard.pieceAt(target);
        return targetPiece == null || targetPiece.isLight() != this.isWhite;
    }

    private static final int[][] KING_OFFSETS = {
        {-1,-1}, {-1,0}, {-1,1},
        { 0,-1}, { 0,1},
        { 1,-1}, { 1,0}, { 1,1}
    };

    @Override
    public List<Square> possibleMoves(ChessBoard board) {
        final List<Square> moves = new ArrayList<>(8);
        final int r0 = square.getRankIndex();
        final int f0 = square.getFileIndex();

        for (int[] d : KING_OFFSETS) {
            final Square tgt = new Square(r0 + d[0], f0 + d[1]);
            if (!board.onBoard(tgt)) continue;
            final BasePiece other = board.pieceAt(tgt);
            if (other == null || other.isLight() != this.isWhite) {
                moves.add(tgt);
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return isWhite ? "K" : "k";
    }
}


