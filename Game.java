package oopchesscode.game;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

import oopchesscode.board.Board;
import oopchesscode.pieces.*;
import oopchesscode.player.Player;
import oopchesscode.position.Position;
import oopchesscode.utils.Utils;

/**
 * Main Game class that manages the chessboard, GUI, and console game modes.
 */
public class Game {
    private Board board;
    private Player white;
    private Player black;
    private boolean isWhiteTurn;
    private boolean gameOver;

    /**
     * Initializes the board and pieces for both players.
     */
    public void start() {
        board = new Board();
        white = new Player(true);
        black = new Player(false);
        white.setUpBackRow(true, board);
        white.setUpPawns(true, board);
        white.initializeAvailable(board);
        black.setUpBackRow(false, board);
        black.setUpPawns(false, board);
        black.initializeAvailable(board);
        isWhiteTurn = true;
        gameOver = false;
    }

    /**
     * Ends the game and displays the winner based on checkmate or forfeit.
     */
    public void end() {
        if (white.getIsForfeit()) {
            System.out.println("White Player has forfeited. Black Player wins!");
        } else if (black.getIsForfeit()) {
            System.out.println("Black Player has forfeited. White Player wins!");
        } else if (isCheckmate(white, black, board)) {
            System.out.println("Checkmate! Black Player wins!");
        } else {
            System.out.println("Checkmate! White Player wins!");
        }
    }

    /**
     * Runs the console-based game loop, accepting user input and processing moves.
     */
    public void play() {
        Scanner scnr = new Scanner(System.in);
        String input;
        String[] inputParts;
        Player currentPlayer;
        Player waitingPlayer;
        Position originPos;
        Position targetPos;

        do {
            board.display();
            currentPlayer = (isWhiteTurn) ? white : black;
            waitingPlayer = (isWhiteTurn) ? black : white;
            board.updateVulnerablePositions(waitingPlayer);

            if (isCheckmate(currentPlayer, waitingPlayer, board)) return;
            if (isInCheck(currentPlayer, waitingPlayer, board)) {
                ((King) currentPlayer.getKing()).setCheckCondition(true);
                System.out.println("Check");
            } else {
                ((King) currentPlayer.getKing()).setCheckCondition(false);
            }

            do {
                System.out.println(currentPlayer + " Enter Move: ");
                input = scnr.nextLine().trim();
                inputParts = input.split(" ");

                if (inputParts.length == 2 && Utils.isValidInput(inputParts[0]) && Utils.isValidInput(inputParts[1])) {
                    originPos = Utils.ultConversion(inputParts[0]);
                    targetPos = Utils.ultConversion(inputParts[1]);

                    if (board.isEmpty(originPos)) {
                        System.out.println("Invalid move");
                        continue;
                    }

                    if (!board.getPiece(originPos).isValidMove(board, targetPos, currentPlayer)) {
                        System.out.println("Invalid move");
                        continue;
                    } else if (!isLegalMove(board, originPos, targetPos, currentPlayer, waitingPlayer)) {
                        System.out.println("Invalid move - king in check");
                        continue;
                    } else {
                        board.movePiece(originPos, targetPos, waitingPlayer);
                    }
                    break;
                } else if (inputParts.length == 1 && inputParts[0].equalsIgnoreCase("f")) {
                    currentPlayer.setIsForfeit();
                    break;
                }

                System.out.println("Invalid input");
            } while (true);

            if (currentPlayer.getIsForfeit() || isCheckmate(currentPlayer, waitingPlayer, board)) {
                gameOver = true;
            }

            isWhiteTurn = !isWhiteTurn;
        } while (!gameOver);
    }

    /**
     * Checks if the current player is in checkmate.
     */
    public boolean isCheckmate(Player moving, Player waiting, Board board) {
        return isInCheck(moving, waiting, board) && moving.getKing().possibleMoves(board).isEmpty();
    }

    /**
     * Checks if the current player's king is under threat.
     */
    public boolean isInCheck(Player moving, Player waiting, Board board) {
        return board.getVulnerablePositions().contains(moving.getKing().getPosition());
    }

    /**
     * Simulates a move and determines whether the move is legal (king not left in check).
     */
    public boolean isLegalMove(Board board, Position origin, Position target, Player moving, Player waiting) {
        Board simBoard = new Board();
        Player movingClone = moving.clone();
        Player waitingClone = waiting.clone();

        for (Piece p : movingClone.getAvailable()) {
            simBoard.setPiece(p.getPosition(), p);
        }

        simBoard.movePiece(origin, target, waitingClone);
        return !isInCheck(movingClone, waitingClone, simBoard);
    }

    /**
     * Starts the GUI-based chess game using Swing.
     */
    public void startGUI() {
        board = new Board();
        white = new Player(true);
        black = new Player(false);
        white.setUpBackRow(true, board);
        white.setUpPawns(true, board);
        white.initializeAvailable(board);
        black.setUpBackRow(false, board);
        black.setUpPawns(false, board);
        black.initializeAvailable(board);
        isWhiteTurn = true;

        JFrame frame = new JFrame("Chess Game - GUI Mode");
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        JButton[][] squares = new JButton[8][8];
        final Position[] selected = {null};

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                JButton square = new JButton(piece == null ? "" : toUnicode(piece));
                square.setFont(new Font("Arial", Font.PLAIN, 36));
                square.setOpaque(true);
                square.setBorderPainted(false);
                square.setBackground(getSquareColor(row, col));
                int r = row, c = col;
                square.addActionListener(e -> {
                    Position clicked = new Position(r, c);
                    Piece clickedPiece = board.getPiece(clicked);

                    if (selected[0] == null) {
                        if (clickedPiece != null && clickedPiece.isWhite() == isWhiteTurn) {
                            selected[0] = clicked;
                            squares[r][c].setBackground(Color.YELLOW);
                        }
                    } else {
                        Position from = selected[0];
                        Position to = clicked;
                        Piece moving = board.getPiece(from);
                        Piece captured = board.getPiece(to);
                        Player currentPlayer = isWhiteTurn ? white : black;

                        if (moving != null && moving.isValidMove(board, to, currentPlayer)) {
                            if (captured instanceof King) {
                                JOptionPane.showMessageDialog(frame, (captured.isWhite() ? "Black" : "White") + " wins!");
                                System.exit(0);
                            }
                            board.movePiece(from, to, captured != null ? (captured.isWhite() ? white : black) : null);
                            isWhiteTurn = !isWhiteTurn;

                            // Checkmate detection
                            Player nextPlayer = isWhiteTurn ? white : black;
                            Player opponent = isWhiteTurn ? black : white;
                            board.updateVulnerablePositions(opponent);

                            if (isCheckmate(nextPlayer, opponent, board)) {
                                JOptionPane.showMessageDialog(frame, "Checkmate! " + (isWhiteTurn ? "Black" : "White") + " wins!");
                                System.exit(0);
                            } else if (isInCheck(nextPlayer, opponent, board)) {
                                JOptionPane.showMessageDialog(frame, "Check!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid move for " + (moving != null ? moving.toString() : "null"));
                        }

                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                Piece updated = board.getPiece(new Position(i, j));
                                squares[i][j].setText(updated == null ? "" : toUnicode(updated));
                                squares[i][j].setBackground(getSquareColor(i, j));
                            }
                        }
                        selected[0] = null;
                    }
                });
                squares[row][col] = square;
                boardPanel.add(square);
            }
        }

        frame.add(boardPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Returns the appropriate square color based on position.
     */
    private Color getSquareColor(int row, int col) {
        return ((row + col) % 2 == 0) ? Color.WHITE : Color.DARK_GRAY;
    }

    /**
     * Converts a piece to its Unicode chess character.
     */
    private String toUnicode(Piece p) {
        if (p == null) return "";
        boolean isWhite = p.isWhite();
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

    /**
     * Main method that launches the game in either GUI or console mode.
     */
    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Start game in GUI mode? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            game.startGUI();
        } else {
            game.start();
            game.play();
            game.end();
        }
    }
}

