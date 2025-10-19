package oopchesscode.board;


import oopchesscode.pieces.BasePiece;
import oopchesscode.position.Square;


import java.util.HashSet;
import java.util.Set;


public class ChessBoard {
private BasePiece[][] chessBoard;
private Set<Square> threatenedSquares;


public ChessBoard() {
chessBoard = new BasePiece[8][8];
threatenedSquares = new HashSet<>();
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


if (captured != null) {
opponent.getActivePieces().remove(captured);
}


placePiece(target, movingPiece);
placePiece(origin, null);
movingPiece.setSquare(target);
}


public void render() {
int i = 7;
while (i >= 0) {
System.out.print((i + 1) + " ");
int j = 0;
while (j < 8) {
BasePiece piece = chessBoard[i][j];
System.out.print((piece != null ? piece.toString() : "-") + " ");
j++;
}
System.out.println();
i--;
}
System.out.println(" A B C D E F G H");
}


public boolean onBoard(Square pos) {
return pos.getRankIndex() >= 0 && pos.getRankIndex() < 8 && pos.getFileIndex() >= 0 && pos.getFileIndex() < 8;
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
