package oopchesscode;

import oopchesscode.board.ChessBoard;
import oopchesscode.game.ChessApp;
import oopchesscode.player.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oopchesscode.BoardTestSupport.sq;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fool's Mate: 1.f3 e5 2.g4 Qh4#
 * After Black's queen move, White is checkmated (not stalemated).
 */
class FoolsMateTest {

    @Test
    @DisplayName("Fool's Mate: after 1.f3 e5 2.g4 Qh4 Black has mated White")
    void foolsMateIsCheckmateNotStalemate() {
        ChessBoard board = new ChessBoard();
        Participant white = new Participant(true);
        Participant black = new Participant(false);

        white.arrangeBackRank(true, board);
        white.setupPawns(true, board);
        black.arrangeBackRank(false, board);
        black.setupPawns(false, board);

        // 1. f2 f3
        board.relocate(sq("f2"), sq("f3"), black);
        // 2. e7 e5
        board.relocate(sq("e7"), sq("e5"), white);
        // 3. g2 g4
        board.relocate(sq("g2"), sq("g4"), black);
        // 4. d8 h4 (Qh4#)
        board.relocate(sq("d8"), sq("h4"), white);

        ChessApp app = new ChessApp();
        // Attacks by the side that just moved (Black) — must match post-move logic in ChessApp.play().
        board.updateThreatenedSquares(black);

        assertTrue(app.isThreatened(white, black, board), "White king should be in check from Black");
        assertFalse(
                app.hasAnyLegalMove(white, black, board),
                "White should have no legal moves");
        assertTrue(app.isCheckmated(white, black, board));
    }
}
