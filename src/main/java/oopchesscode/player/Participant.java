package oopchesscode.player;

import oopchesscode.board.ChessBoard;
import oopchesscode.pieces.BasePiece;
import oopchesscode.pieces.BishopPiece;
import oopchesscode.pieces.KingPiece;
import oopchesscode.pieces.KnightPiece;
import oopchesscode.pieces.PawnPiece;
import oopchesscode.pieces.QueenPiece;
import oopchesscode.pieces.RookPiece;
import oopchesscode.position.Square;

import java.util.ArrayList;
import java.util.List;

public class Participant {

    private final boolean lightSide;
    private boolean hasForfeited;
    private final List<BasePiece> activePieces;

    public Participant(boolean lightSide) {
        this.lightSide = lightSide;
        this.activePieces = new ArrayList<>();
        this.hasForfeited = false;
    }

    public boolean isLight() {
        return lightSide;
    }

    public List<BasePiece> getActivePieces() {
        return activePieces;
    }

    public boolean getIsForfeit() {
        return hasForfeited;
    }

    public void setIsForfeit() {
        hasForfeited = true;
    }

    public void setupPawns(boolean lightSide, ChessBoard board) {
        // White on ranks 1–2 (indices 0–1), Black on ranks 7–8 (indices 6–7).
        int rankIndex = lightSide ? 1 : 6;
        for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
            Square pos = new Square(rankIndex, fileIndex);
            PawnPiece pawn = new PawnPiece(lightSide, pos);
            activePieces.add(pawn);
            board.placePiece(pos, pawn);
        }
    }

    private void addAndPlace(ChessBoard board, BasePiece p) {
        activePieces.add(p);
        board.placePiece(p.getSquare(), p);
    }

    public void arrangeBackRank(boolean isWhite, ChessBoard board) {
        int rankIndex = isWhite ? 0 : 7;
        BasePiece[] pieces = {
                new RookPiece(isWhite, new Square(rankIndex, 0)),
                new KnightPiece(isWhite, new Square(rankIndex, 1)),
                new BishopPiece(isWhite, new Square(rankIndex, 2)),
                new QueenPiece(isWhite, new Square(rankIndex, 3)),
                new KingPiece(isWhite, new Square(rankIndex, 4)),
                new BishopPiece(isWhite, new Square(rankIndex, 5)),
                new KnightPiece(isWhite, new Square(rankIndex, 6)),
                new RookPiece(isWhite, new Square(rankIndex, 7))
        };
        for (BasePiece p : pieces) {
            addAndPlace(board, p);
        }
    }

    public BasePiece getKing() {
        for (BasePiece p : activePieces) {
            if (p instanceof KingPiece) {
                return p;
            }
        }
        return null;
    }

    /**
     * Ensures every active piece is placed on the board (idempotent if already placed).
     */
    public void initializeActivePieces(ChessBoard board) {
        for (BasePiece p : new ArrayList<>(activePieces)) {
            board.placePiece(p.getSquare(), p);
        }
    }

    /**
     * Shallow copy: same piece instances; used so simulated captures do not mutate the real list.
     */
    public Participant clone() {
        Participant copy = new Participant(this.lightSide);
        copy.activePieces.addAll(this.activePieces);
        return copy;
    }

    @Override
    public String toString() {
        return lightSide ? "White" : "Black";
    }
}
