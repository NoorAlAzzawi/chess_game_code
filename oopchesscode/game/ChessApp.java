package oopchesscode.game;


import java.util.Scanner;
import java.util.Locale;
import javax.swing.*;
import java.awt.*;


import oopchesscode.board.ChessBoard;
import oopchesscode.pieces.*;
import oopchesscode.player.Participant;
import oopchesscode.position.Square;
import oopchesscode.utils.ChessUtil;



public class ChessApp {
private ChessBoard chessBoard;
private Participant whitePlayer;
private Participant blackPlayer;
private boolean isLightTurn;
private boolean hasEnded;



private void setupSide(Participant p, boolean isWhite, ChessBoard board) {
p.arrangeBackRank(isWhite, board);
p.setupPawns(isWhite, board);
p.initializeActivePieces(board);
}


public void start() {
chessBoard = new ChessBoard();
whitePlayer = new Participant(true);
blackPlayer = new Participant(false);
setupSide(whitePlayer, true, chessBoard);
setupSide(blackPlayer, false, chessBoard);
isLightTurn = true;
hasEnded = false;
}



private enum Result { WHITE_FORFEIT, BLACK_FORFEIT, WHITE_CHECKMATED, BLACK_CHECKMATED }


private Result evaluateResult() {
if (whitePlayer.getIsForfeit()) return Result.WHITE_FORFEIT;
if (blackPlayer.getIsForfeit()) return Result.BLACK_FORFEIT;
return isCheckmated(whitePlayer, blackPlayer, chessBoard)
? Result.WHITE_CHECKMATED
: Result.BLACK_CHECKMATED;
}


public void end() {
String msg = switch (evaluateResult()) {
case WHITE_FORFEIT -> "White Participant has forfeited. Black Participant wins!";
case BLACK_FORFEIT -> "Black Participant has forfeited. White Participant wins!";
case WHITE_CHECKMATED -> "Checkmate! Black Participant wins!";
case BLACK_CHECKMATED -> "Checkmate! White Participant wins!";
};
System.out.println(msg);
}



public void play() {
Scanner inputScanner = new Scanner(System.in);
String input;
String[] inputParts;
Participant currentPlayer;
Participant waitingPlayer;
Square originPos;
Square targetPos;


while (!hasEnded) {
chessBoard.render();


currentPlayer = isLightTurn ? whitePlayer : blackPlayer;
waitingPlayer = isLightTurn ? blackPlayer : whitePlayer;


chessBoard.updateThreatenedSquares(waitingPlayer);



if (isCheckmated(currentPlayer, waitingPlayer, chessBoard)) break;


boolean inCheck = isThreatened(currentPlayer, waitingPlayer, chessBoard);
((KingPiece) currentPlayer.getKing()).setCheckFlag(inCheck);
if (inCheck) System.out.println("Check");



while (true) {
System.out.println(currentPlayer + " Enter Move: ");
input = inputScanner.nextLine().strip(); 
inputParts = input.split("\\s+"); 



if (inputParts.length == 1 && inputParts[0].equalsIgnoreCase("f")) {
currentPlayer.setIsForfeit();
break;
}



if (inputParts.length == 2
&& ChessUtil.isValidSquareInput(inputParts[0])
&& ChessUtil.isValidSquareInput(inputParts[1])) {


String fromPrint = inputParts[0].toLowerCase(Locale.ROOT);
String toPrint = inputParts[1].toLowerCase(Locale.ROOT);


originPos = ChessUtil.parseSquare(inputParts[0].toUpperCase(Locale.ROOT));
targetPos = ChessUtil.parseSquare(inputParts[1].toUpperCase(Locale.ROOT));


if (originPos == null || targetPos == null) {
System.out.println("Invalid squares: " + fromPrint + " " + toPrint + ". Try \"e2 e4\".");
continue;
}


BasePiece moving = chessBoard.pieceAt(originPos);


if (moving == null) {
System.out.println("No piece at " + fromPrint + ". Try again (e.g., \"e2 e4\").");
continue;
}
if (!moving.isValidMove(chessBoard, targetPos, currentPlayer)) {
System.out.println("That " + moving + " cannot move from " + fromPrint + " to " + toPrint + ".");
continue;
}
if (!isMoveLegal(chessBoard, originPos, targetPos, currentPlayer, waitingPlayer)) {
System.out.println("Illegal move: your king would be in check.");
continue;
}


chessBoard.relocate(originPos, targetPos, waitingPlayer);
System.out.println(currentPlayer + " moves " + fromPrint + " → " + toPrint + ".");
break; 
}


System.out.println("Invalid input"); 
}



if (currentPlayer.getIsForfeit() || isCheckmated(currentPlayer, waitingPlayer, chessBoard)) {
hasEnded = true;
} else {
isLightTurn = !isLightTurn;
}
}


end(); 
}



public boolean isCheckmated(Participant moving, Participant waiting, ChessBoard chessBoard) {
return isThreatened(moving, waiting, chessBoard)
&& moving.getKing().possibleMoves(chessBoard).isEmpty();
}



public boolean isThreatened(Participant moving, Participant waiting, ChessBoard chessBoard) {
return chessBoard.getThreatenedSquares().contains(moving.getKing().getSquare());
}



public boolean isMoveLegal(ChessBoard chessBoard,
Square origin, Square target,
Participant moving, Participant waiting) {
ChessBoard simBoard = new ChessBoard();
Participant movingClone = moving.clone();
Participant waitingClone = waiting.clone();



for (BasePiece p : movingClone.getActivePieces()) simBoard.placePiece(p.getSquare(), p);
for (BasePiece p : waitingClone.getActivePieces()) simBoard.placePiece(p.getSquare(), p);



if (simBoard.isEmpty(origin)) return false;



simBoard.relocate(origin, target, waitingClone);



return !isThreatened(movingClone, waitingClone, simBoard);
}



public void launchGUI() {
chessBoard = new ChessBoard();
whitePlayer = new Participant(true);
blackPlayer = new Participant(false);
whitePlayer.arrangeBackRank(true, chessBoard);
whitePlayer.setupPawns(true, chessBoard);
whitePlayer.initializeActivePieces(chessBoard);
blackPlayer.arrangeBackRank(false, chessBoard);
blackPlayer.setupPawns(false, chessBoard);
blackPlayer.initializeActivePieces(chessBoard);
isLightTurn = true;


JFrame frame = new JFrame("Chess ChessApp - GUI Mode");
JPanel boardPanel = new JPanel(new GridLayout(8, 8));
JButton[][] squares = new JButton[8][8];
final Square[] selected = {null};


for (int rankIndex = 0; rankIndex < 8; rankIndex++) {
for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
Square pos = new Square(rankIndex, fileIndex);
BasePiece piece = chessBoard.pieceAt(pos);
JButton square = new JButton(piece == null ? "" : unicodeGlyph(piece));
square.setFont(new Font("Arial", Font.PLAIN, 36));
square.setOpaque(true);
square.setBorderPainted(false);
square.setBackground(squareColorAt(rankIndex, fileIndex));
int r = rankIndex, c = fileIndex;
square.addActionListener(e -> {
Square clicked = new Square(r, c);
BasePiece clickedPiece = chessBoard.pieceAt(clicked);


if (selected[0] == null) {
if (clickedPiece != null && clickedPiece.isLight() == isLightTurn) {
selected[0] = clicked;
squares[r][c].setBackground(Color.YELLOW);
}
} else {
Square from = selected[0];
Square to = clicked;
BasePiece moving = chessBoard.pieceAt(from);
BasePiece captured = chessBoard.pieceAt(to);
Participant currentPlayer = isLightTurn ? whitePlayer : blackPlayer;


if (moving != null && moving.isValidMove(chessBoard, to, currentPlayer)) {
if (captured instanceof KingPiece) {
JOptionPane.showMessageDialog(frame, (captured.isLight() ? "Black" : "White") + " wins!");
System.exit(0);
}
chessBoard.relocate(from, to, captured != null ? (captured.isLight() ? whitePlayer : blackPlayer) : null);
isLightTurn = !isLightTurn;


// Checkmate detection
Participant nextPlayer = isLightTurn ? whitePlayer : blackPlayer;
Participant opponent = isLightTurn ? blackPlayer : whitePlayer;
chessBoard.updateThreatenedSquares(opponent);


if (isCheckmated(nextPlayer, opponent, chessBoard)) {
JOptionPane.showMessageDialog(frame, "Checkmate! " + (isLightTurn ? "Black" : "White") + " wins!");
System.exit(0);
} else if (isThreatened(nextPlayer, opponent, chessBoard)) {
JOptionPane.showMessageDialog(frame, "Check!");
}
} else {
JOptionPane.showMessageDialog(frame, "Invalid move for " + (moving != null ? moving.toString() : "null"));
}


for (int i = 0; i < 8; i++) {
for (int j = 0; j < 8; j++) {
BasePiece updated = chessBoard.pieceAt(new Square(i, j));
squares[i][j].setText(updated == null ? "" : unicodeGlyph(updated));
squares[i][j].setBackground(squareColorAt(i, j));
}
}
selected[0] = null;
}
});
squares[rankIndex][fileIndex] = square;
boardPanel.add(square);
}
}


frame.add(boardPanel);
frame.pack();
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setLocationRelativeTo(null);
frame.setVisible(true);
}



private Color squareColorAt(int rankIndex, int fileIndex) {
return ((rankIndex + fileIndex) % 2 == 0) ? Color.WHITE : Color.DARK_GRAY;
}



private String unicodeGlyph(BasePiece p) {
if (p == null) return "";
boolean isWhite = p.isLight();
switch (p.toString().toUpperCase()) {
case "P": return isWhite ? "♙" : "♟";
case "R": return isWhite ? "♖" : "♜";
case "N": return isWhite ? "♘" : "♞";
case "B": return isWhite ? "♗" : "♝";
case "Q": return isWhite ? "♕" : "♛";
case "K": return isWhite ? "♔" : "♚";
default: return "?";
}
}



public static void main(String[] args) {
ChessApp game = new ChessApp();
Scanner scanner = new Scanner(System.in);
System.out.println("Start game in GUI mode? (y/n): ");
String choice = scanner.nextLine();


if (choice.equalsIgnoreCase("y")) {
game.launchGUI();
} else {
game.start();
game.play(); 
}
}
}
