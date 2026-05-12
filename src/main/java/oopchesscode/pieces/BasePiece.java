package oopchesscode.pieces;

import oopchesscode.board.ChessBoard;
import oopchesscode.position.Square;
import java.util.List;

public abstract class BasePiece {
    protected boolean isWhite;
    protected Square square;

    public BasePiece(boolean isWhite, Square square) {
        this.isWhite = isWhite;
        this.square = square;
    }

    public boolean isLight() {
        return isWhite;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public abstract boolean isValidMove(ChessBoard chessBoard, Square target, oopchesscode.player.Participant player);

    public abstract List<Square> possibleMoves(ChessBoard chessBoard);

    @Override
    public String toString() {
        return "?";
    }
}


