package oopchesscode.player;

import oopchesscode.board.ChessBoard;
import oopchesscode.pieces.*;
import oopchesscode.position.Square;

import java.util.ArrayList;
import java.util.List;

public class Participant {
private boolean lightSide;
private boolean hasForfeited = false;
private List<BasePiece> activePieces;

public Participant(boolean lightSide) {
this.lightSide = lightSide;
activePieces = new ArrayList<>();
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

public void setupPawns(boolean lightSide, ChessBoard chessBoard) {
int rankIndex = lightSide ? 6 : 1;
for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
Square pos = new Square(rankIndex, fileIndex);
PawnPiece pawn = new PawnPiece(lightSide, pos);
activePieces.add(pawn);
chessBoard.placePiece(pos, pawn);
}
}

private void addAndPlace(ChessBoard board, BasePiece p) {
    activePieces.add(p);
    board.placePiece(p.getSquare(), p);
}

public void arrangeBackRank(boolean isWhite, ChessBoard chessBoard) {
    int rankIndex = isWhite ? 7 : 0;
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
    for (BasePiece p : pieces) addAndPlace(chessBoard, p);
}


public BasePiece getKing() {
for (BasePiece p : activePieces) {
if (p instanceof KingPiece) return p;
}
return null;
}

public void initializeActivePieces(ChessBoard chessBoard) {
for (BasePiece p : new ArrayList<>(activePieces)) {
chessBoard.placePiece(p.getSquare(), p);
}
}

public Participant clone() {
Participant clone = new Participant(this.lightSide);
for (BasePiece p : this.activePieces) {
clone.activePieces.add(p);
}
return clone;
}

@Override
public String toString() {
return lightSide ? "White" : "Black";
}
}
