package oopchesscode.board;

import oopchesscode.pieces.BasePiece;
import oopchesscode.position.Square;

import java.util.HashSet;
import java.util.Set;

public class ChessBoard {
    private final BasePiece[][] chessBoard;
    private final Set<Square> threatenedSquares;

    public ChessBoard() {
        this.chessBoard = new BasePiece[8][8];
        this.threatenedSquares = new HashSet<>();
    }

    public void placePiece(Square pos, BasePiece piece) {
        if (pos != null && onBoard(pos)) {
            chessBoard[pos.getRankIndex()][pos.getFileIndex()] = piece;
        }
    }

    public BasePiece pieceAt(Square pos) {
        if (pos != null && onBoard(pos)) {
            return chessBoard[pos.getRankIndex()][pos.getFileIndex()];
        }
        return null;
    }

    public boolean isEmpty(Square pos) {
        return pieceAt(pos) == null;
    }

    public void relocate(Square origin, Square target, oopchesscode.player.Participant opponent) {
        BasePiece movingPiece = pieceAt(origin);
        BasePiece captured = pieceAt(target);

        if (captured != null && opponent != null) {
            opponent.getActivePieces().remove(captured);
        }

        placePiece(target, movingPiece);
        placePiece(origin, null);
        if (movingPiece != null) {
            movingPiece.setSquare(target);
        }
    }

    public void render() {
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                BasePiece piece = chessBoard[i][j];
                System.out.print((piece != null ? piece.toString() : "-") + " ");
            }
            System.out.println();
        }
        System.out.println(" A B C D E F G H");
    }

    public boolean onBoard(Square pos) {
        if (pos == null) {
            return false;
        }
        int r = pos.getRankIndex();
        int f = pos.getFileIndex();
        return r >= 0 && r < 8 && f >= 0 && f < 8;
    }

    public void updateThreatenedSquares(oopchesscode.player.Participant opponent) {
        threatenedSquares.clear();
        for (BasePiece p : opponent.getActivePieces()) {
            threatenedSquares.addAll(p.possibleMoves(this));
        }
    }

    public Set<Square> getThreatenedSquares() {
        return threatenedSquares;
    }
}
