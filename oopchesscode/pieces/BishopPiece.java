package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;

import java.util.ArrayList;
import java.util.List;

public class BishopPiece extends BasePiece {

    public BishopPiece(boolean isWhite, Square square) {
        super(isWhite, square);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Square target, oopchesscode.player.Participant player) {
        if (target == null || !board.onBoard(target)) return false;
        final int sr = square.getRankIndex(), sf = square.getFileIndex();
        final int tr = target.getRankIndex(), tf = target.getFileIndex();
        final int dr = tr - sr, df = tf - sf;

        if (dr == 0 || Math.abs(dr) != Math.abs(df)) return false;

        final int rStep = dr > 0 ? 1 : -1;
        final int fStep = df > 0 ? 1 : -1;

        for (int r = sr + rStep, f = sf + fStep; r != tr; r += rStep, f += fStep) {
            if (!board.isEmpty(new Square(r, f))) return false;
        }

        BasePiece dest = board.pieceAt(target);
        return dest == null || dest.isLight() != this.isWhite;
    }

    @Override
    public List<Square> possibleMoves(ChessBoard chessBoard) {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return isWhite ? "B" : "b";
    }
}
