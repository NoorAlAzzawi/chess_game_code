package oopchesscode.game;

import oopchesscode.board.ChessBoard;
import oopchesscode.pieces.BasePiece;
import oopchesscode.pieces.KingPiece;
import oopchesscode.pieces.PawnPiece;
import oopchesscode.pieces.QueenPiece;
import oopchesscode.player.Participant;
import oopchesscode.position.Square;
import oopchesscode.utils.ChessUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class ChessApp {

    private enum EndState {
        ONGOING,
        WHITE_FORFEIT,
        BLACK_FORFEIT,
        WHITE_WIN_CHECKMATE,
        BLACK_WIN_CHECKMATE,
        STALEMATE
    }

    private ChessBoard chessBoard;
    private Participant whitePlayer;
    private Participant blackPlayer;
    private boolean isLightTurn;
    private boolean hasEnded;
    private EndState endState = EndState.ONGOING;

    private void setupSide(Participant p, ChessBoard board) {
        p.arrangeBackRank(p.isLight(), board);
        p.setupPawns(p.isLight(), board);
        p.initializeActivePieces(board);
    }

    public void start() {
        chessBoard = new ChessBoard();
        whitePlayer = new Participant(true);
        blackPlayer = new Participant(false);
        setupSide(whitePlayer, chessBoard);
        setupSide(blackPlayer, chessBoard);
        isLightTurn = true;
        hasEnded = false;
        endState = EndState.ONGOING;
    }

    public void end() {
        String msg;
        switch (endState) {
            case WHITE_FORFEIT:
                msg = "White has forfeited. Black wins.";
                break;
            case BLACK_FORFEIT:
                msg = "Black has forfeited. White wins.";
                break;
            case WHITE_WIN_CHECKMATE:
                msg = "Checkmate. White wins.";
                break;
            case BLACK_WIN_CHECKMATE:
                msg = "Checkmate. Black wins.";
                break;
            case STALEMATE:
                msg = "Stalemate. The game is a draw.";
                break;
            case ONGOING:
            default:
                msg = "Game over.";
                break;
        }
        System.out.println(msg);
    }

    /**
     * True if {@code sideToMove} has at least one move that does not leave their king in check.
     */
    public boolean hasAnyLegalMove(Participant sideToMove, Participant opponent, ChessBoard board) {
        for (BasePiece piece : sideToMove.getActivePieces()) {
            Square from = piece.getSquare();
            if (from == null || !board.onBoard(from)) {
                continue;
            }
            if (board.pieceAt(from) != piece) {
                continue;
            }
            for (Square to : piece.possibleMoves(board)) {
                if (isMoveLegal(board, from, to, sideToMove, opponent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void play() {
        try (Scanner inputScanner = new Scanner(System.in)) {
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

                if (!hasAnyLegalMove(currentPlayer, waitingPlayer, chessBoard)) {
                    if (isThreatened(currentPlayer, waitingPlayer, chessBoard)) {
                        endState = currentPlayer.isLight() ? EndState.BLACK_WIN_CHECKMATE : EndState.WHITE_WIN_CHECKMATE;
                    } else {
                        endState = EndState.STALEMATE;
                    }
                    hasEnded = true;
                    break;
                }

                boolean inCheck = isThreatened(currentPlayer, waitingPlayer, chessBoard);
                BasePiece king = currentPlayer.getKing();
                if (king instanceof KingPiece) {
                    ((KingPiece) king).setCheckFlag(inCheck);
                }
                if (inCheck) {
                    System.out.println("Check");
                }

                while (true) {
                    System.out.println(currentPlayer + " enter move (e.g. e2 e4), or f to forfeit: ");
                    input = inputScanner.nextLine().trim();
                    inputParts = input.split("\\s+");

                    if (inputParts.length == 1 && inputParts[0].equalsIgnoreCase("f")) {
                        currentPlayer.setIsForfeit();
                        endState = currentPlayer.isLight() ? EndState.WHITE_FORFEIT : EndState.BLACK_FORFEIT;
                        break;
                    }

                    if (inputParts.length == 2
                            && ChessUtil.isValidSquareInput(inputParts[0])
                            && ChessUtil.isValidSquareInput(inputParts[1])) {

                        String fromPrint = inputParts[0].toLowerCase();
                        String toPrint = inputParts[1].toLowerCase();

                        originPos = ChessUtil.parseSquare(inputParts[0]);
                        targetPos = ChessUtil.parseSquare(inputParts[1]);

                        if (originPos == null || targetPos == null) {
                            System.out.println("Invalid squares: " + fromPrint + " " + toPrint + ". Try \"e2 e4\".");
                            continue;
                        }

                        BasePiece moving = chessBoard.pieceAt(originPos);

                        if (moving == null) {
                            System.out.println("No piece at " + fromPrint + ". Try again (e.g., \"e2 e4\").");
                            continue;
                        }
                        if (moving.isLight() != currentPlayer.isLight()) {
                            System.out.println("That is not your piece. Try again.");
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
                        applyPawnPromotion(currentPlayer, targetPos);
                        System.out.println(currentPlayer + " moves " + fromPrint + " → " + toPrint + ".");
                        break;
                    }

                    System.out.println("Invalid input. Use two squares like \"e2 e4\", or f to forfeit.");
                }

                if (currentPlayer.getIsForfeit()) {
                    hasEnded = true;
                } else {
                    // Opponent's king uses attacks by the player who just moved (same as GUI refresh).
                    chessBoard.updateThreatenedSquares(currentPlayer);
                    if (!hasAnyLegalMove(waitingPlayer, currentPlayer, chessBoard)) {
                        if (isThreatened(waitingPlayer, currentPlayer, chessBoard)) {
                            endState = waitingPlayer.isLight()
                                    ? EndState.BLACK_WIN_CHECKMATE
                                    : EndState.WHITE_WIN_CHECKMATE;
                        } else {
                            endState = EndState.STALEMATE;
                        }
                        hasEnded = true;
                    } else {
                        isLightTurn = !isLightTurn;
                    }
                }
            }

            end();
        }
    }

    public boolean isCheckmated(Participant moving, Participant waiting, ChessBoard board) {
        return isThreatened(moving, waiting, board) && !hasAnyLegalMove(moving, waiting, board);
    }

    public boolean isThreatened(Participant moving, Participant waiting, ChessBoard board) {
        return board.getThreatenedSquares().contains(moving.getKing().getSquare());
    }

    public boolean isMoveLegal(ChessBoard board, Square origin, Square target,
                               Participant moving, Participant waiting) {
        ChessBoard simBoard = new ChessBoard();
        Participant waitingSim = waiting.clone();

        for (BasePiece p : moving.getActivePieces()) {
            simBoard.placePiece(p.getSquare(), p);
        }
        for (BasePiece p : waitingSim.getActivePieces()) {
            simBoard.placePiece(p.getSquare(), p);
        }

        if (simBoard.isEmpty(origin)) {
            return false;
        }

        BasePiece movingPiece = simBoard.pieceAt(origin);
        try {
            simBoard.relocate(origin, target, waitingSim);
            simBoard.updateThreatenedSquares(waitingSim);
            return !isThreatened(moving, waitingSim, simBoard);
        } finally {
            if (movingPiece != null) {
                movingPiece.setSquare(origin);
            }
        }
    }

    private void applyPawnPromotion(Participant mover, Square target) {
        BasePiece at = chessBoard.pieceAt(target);
        if (!(at instanceof PawnPiece)) {
            return;
        }
        int r = target.getRankIndex();
        if (r != 0 && r != 7) {
            return;
        }
        QueenPiece queen = new QueenPiece(at.isLight(), target);
        mover.getActivePieces().remove(at);
        mover.getActivePieces().add(queen);
        chessBoard.placePiece(target, queen);
    }

    public void launchGUI() {
        chessBoard = new ChessBoard();
        whitePlayer = new Participant(true);
        blackPlayer = new Participant(false);
        setupSide(whitePlayer, chessBoard);
        setupSide(blackPlayer, chessBoard);
        isLightTurn = true;
        endState = EndState.ONGOING;

        JFrame frame = new JFrame("Chess — GUI");
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        JButton[][] squares = new JButton[8][8];
        final Square[] selected = {null};

        // Rank 8 at top of window (same orientation as CLI render).
        for (int row = 0; row < 8; row++) {
            final int rankIndex = 7 - row;
            for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
                Square pos = new Square(rankIndex, fileIndex);
                BasePiece piece = chessBoard.pieceAt(pos);
                JButton square = new JButton(piece == null ? "" : unicodeGlyph(piece));
                square.setFont(new Font("SansSerif", Font.PLAIN, 36));
                square.setOpaque(true);
                square.setBorderPainted(false);
                square.setBackground(squareColorAt(rankIndex, fileIndex));
                final int r = rankIndex;
                final int c = fileIndex;
                final int displayRow = row;
                square.addActionListener(e -> {
                    Square clicked = new Square(r, c);
                    BasePiece clickedPiece = chessBoard.pieceAt(clicked);

                    if (selected[0] == null) {
                        if (clickedPiece != null && clickedPiece.isLight() == isLightTurn) {
                            selected[0] = clicked;
                            squares[displayRow][c].setBackground(Color.YELLOW);
                        }
                    } else {
                        Square from = selected[0];
                        Square to = clicked;
                        BasePiece moving = chessBoard.pieceAt(from);
                        Participant currentPlayer = isLightTurn ? whitePlayer : blackPlayer;
                        Participant opponent = isLightTurn ? blackPlayer : whitePlayer;

                        if (moving != null
                                && moving.isLight() == currentPlayer.isLight()
                                && moving.isValidMove(chessBoard, to, currentPlayer)
                                && isMoveLegal(chessBoard, from, to, currentPlayer, opponent)) {

                            BasePiece captured = chessBoard.pieceAt(to);
                            Participant captureOwner = captured != null
                                    ? (captured.isLight() ? whitePlayer : blackPlayer)
                                    : null;
                            chessBoard.relocate(from, to, captureOwner);
                            applyPawnPromotion(currentPlayer, to);
                            isLightTurn = !isLightTurn;

                            Participant nextPlayer = isLightTurn ? whitePlayer : blackPlayer;
                            Participant nextOpponent = isLightTurn ? blackPlayer : whitePlayer;
                            chessBoard.updateThreatenedSquares(nextOpponent);

                            if (!hasAnyLegalMove(nextPlayer, nextOpponent, chessBoard)) {
                                if (isThreatened(nextPlayer, nextOpponent, chessBoard)) {
                                    endState = nextPlayer.isLight() ? EndState.BLACK_WIN_CHECKMATE : EndState.WHITE_WIN_CHECKMATE;
                                    JOptionPane.showMessageDialog(frame,
                                            endState == EndState.WHITE_WIN_CHECKMATE
                                                    ? "Checkmate. White wins."
                                                    : "Checkmate. Black wins.");
                                } else {
                                    endState = EndState.STALEMATE;
                                    JOptionPane.showMessageDialog(frame, "Stalemate. Draw.");
                                }
                                frame.dispose();
                                return;
                            }
                            if (isThreatened(nextPlayer, nextOpponent, chessBoard)) {
                                JOptionPane.showMessageDialog(frame, "Check!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Invalid move for " + (moving != null ? moving.toString() : "empty square"));
                        }

                        for (int dr = 0; dr < 8; dr++) {
                            int rank = 7 - dr;
                            for (int j = 0; j < 8; j++) {
                                BasePiece updated = chessBoard.pieceAt(new Square(rank, j));
                                squares[dr][j].setText(updated == null ? "" : unicodeGlyph(updated));
                                squares[dr][j].setBackground(squareColorAt(rank, j));
                            }
                        }
                        selected[0] = null;
                    }
                });
                squares[row][fileIndex] = square;
                boardPanel.add(square);
            }
        }

        frame.add(boardPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static Color squareColorAt(int rankIndex, int fileIndex) {
        return ((rankIndex + fileIndex) % 2 == 0) ? Color.WHITE : Color.DARK_GRAY;
    }

    private static String unicodeGlyph(BasePiece p) {
        if (p == null) {
            return "";
        }
        boolean isWhite = p.isLight();
        switch (p.toString().toUpperCase()) {
            case "P":
                return isWhite ? "\u2659" : "\u265F";
            case "R":
                return isWhite ? "\u2656" : "\u265C";
            case "N":
                return isWhite ? "\u2658" : "\u265E";
            case "B":
                return isWhite ? "\u2657" : "\u265D";
            case "Q":
                return isWhite ? "\u2655" : "\u265B";
            case "K":
                return isWhite ? "\u2654" : "\u265A";
            default:
                return "?";
        }
    }

    public static void main(String[] args) {
        ChessApp game = new ChessApp();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Start game in GUI mode? (y/n): ");
            String choice = scanner.nextLine();

            if (choice != null && choice.trim().equalsIgnoreCase("y")) {
                SwingUtilities.invokeLater(game::launchGUI);
            } else {
                game.start();
                game.play();
            }
        }
    }
}
