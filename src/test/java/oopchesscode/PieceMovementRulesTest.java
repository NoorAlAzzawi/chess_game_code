package oopchesscode;

import oopchesscode.board.ChessBoard;
import oopchesscode.pieces.BishopPiece;
import oopchesscode.pieces.KingPiece;
import oopchesscode.pieces.KnightPiece;
import oopchesscode.pieces.PawnPiece;
import oopchesscode.pieces.QueenPiece;
import oopchesscode.pieces.RookPiece;
import oopchesscode.player.Participant;
import oopchesscode.position.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static oopchesscode.BoardTestSupport.clearBoard;
import static oopchesscode.BoardTestSupport.place;
import static oopchesscode.BoardTestSupport.sq;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Movement geometry and pseudo-legal rules per piece type (not king-in-check filtering).
 */
class PieceMovementRulesTest {

    private final ChessBoard board = new ChessBoard();
    private final Participant white = new Participant(true);
    private final Participant black = new Participant(false);
    private final Participant whiteP = white;
    private final Participant blackP = black;

    @BeforeEach
    void emptyBoard() {
        clearBoard(board, white, black);
    }

    @Test
    void pawnSingleStepForwardEmpty() {
        // White advances toward rank 8 (increasing rank index: e4 -> e5).
        PawnPiece pw = new PawnPiece(true, sq("e4"));
        place(board, white, pw);
        assertTrue(pw.isValidMove(board, sq("e5"), whiteP));
        assertTrue(pw.possibleMoves(board).contains(sq("e5")));
    }

    @Test
    void pawnDoubleStepFromStartingRankOnly() {
        PawnPiece pw = new PawnPiece(true, sq("e2"));
        place(board, white, pw);
        assertTrue(pw.isValidMove(board, sq("e3"), whiteP));
        assertTrue(pw.isValidMove(board, sq("e4"), whiteP));
    }

    @Test
    void pawnDoubleStepBlockedByPieceOnMiddle() {
        PawnPiece pw = new PawnPiece(true, sq("e2"));
        PawnPiece blocker = new PawnPiece(false, sq("e3"));
        place(board, white, pw);
        place(board, black, blocker);
        assertFalse(pw.isValidMove(board, sq("e4"), whiteP));
    }

    @Test
    void pawnCannotDoubleAfterLeavingStartRank() {
        PawnPiece pw = new PawnPiece(true, sq("e5"));
        place(board, white, pw);
        assertFalse(pw.isValidMove(board, sq("e7"), whiteP));
    }

    @Test
    void pawnDiagonalCaptureEnemy() {
        PawnPiece pw = new PawnPiece(true, sq("e4"));
        PawnPiece victim = new PawnPiece(false, sq("d5"));
        place(board, white, pw);
        place(board, black, victim);
        assertTrue(pw.isValidMove(board, sq("d5"), whiteP));
        assertTrue(pw.possibleMoves(board).contains(sq("d5")));
    }

    @Test
    void pawnCannotCaptureStraight() {
        PawnPiece pw = new PawnPiece(true, sq("e4"));
        PawnPiece victim = new PawnPiece(false, sq("e5"));
        place(board, white, pw);
        place(board, black, victim);
        assertFalse(pw.isValidMove(board, sq("e5"), whiteP));
    }

    @Test
    void blackPawnMovesDownBoard() {
        // Black advances toward rank 1 (decreasing rank index: e7 -> e6).
        PawnPiece pb = new PawnPiece(false, sq("e7"));
        place(board, black, pb);
        assertTrue(pb.isValidMove(board, sq("e6"), blackP));
        assertTrue(pb.isValidMove(board, sq("e5"), blackP));
    }

    @Test
    void rookSlidesAndCapturesEnemy() {
        RookPiece wr = new RookPiece(true, sq("e4"));
        place(board, white, wr);
        assertTrue(wr.isValidMove(board, sq("e8"), whiteP));
        assertTrue(wr.isValidMove(board, sq("a4"), whiteP));
        RookPiece br = new RookPiece(false, sq("e8"));
        place(board, black, br);
        assertTrue(wr.isValidMove(board, sq("e8"), whiteP));
    }

    @Test
    void rookBlockedByOwnPiece() {
        RookPiece wr = new RookPiece(true, sq("e4"));
        PawnPiece blocker = new PawnPiece(true, sq("e6"));
        place(board, white, wr);
        place(board, white, blocker);
        assertFalse(wr.isValidMove(board, sq("e8"), whiteP));
    }

    @Test
    void rookIllegalDiagonal() {
        RookPiece wr = new RookPiece(true, sq("e4"));
        place(board, white, wr);
        assertFalse(wr.isValidMove(board, sq("f5"), whiteP));
    }

    @Test
    void bishopDiagonalAndCapture() {
        BishopPiece wb = new BishopPiece(true, sq("e4"));
        place(board, white, wb);
        assertTrue(wb.isValidMove(board, sq("h7"), whiteP));
        BishopPiece bb = new BishopPiece(false, sq("b7"));
        place(board, black, bb);
        assertTrue(wb.isValidMove(board, sq("b7"), whiteP));
    }

    @Test
    void bishopBlocked() {
        BishopPiece wb = new BishopPiece(true, sq("e4"));
        PawnPiece blocker = new PawnPiece(true, sq("f5"));
        place(board, white, wb);
        place(board, white, blocker);
        assertFalse(wb.isValidMove(board, sq("g6"), whiteP));
    }

    @Test
    void bishopPossibleMovesNonEmpty() {
        BishopPiece wb = new BishopPiece(true, sq("e4"));
        place(board, white, wb);
        assertFalse(wb.possibleMoves(board).isEmpty());
        assertTrue(wb.possibleMoves(board).contains(sq("f5")));
    }

    @Test
    void knightJumpsAndIgnoresBlockers() {
        KnightPiece wn = new KnightPiece(true, sq("e4"));
        PawnPiece wall = new PawnPiece(true, sq("e5"));
        place(board, white, wn);
        place(board, white, wall);
        assertTrue(wn.isValidMove(board, sq("f6"), whiteP));
        assertTrue(wn.possibleMoves(board).contains(sq("f6")));
    }

    @Test
    void knightIllegalOrFriendlyDestination() {
        KnightPiece wn = new KnightPiece(true, sq("e4"));
        PawnPiece friend = new PawnPiece(true, sq("f6"));
        place(board, white, wn);
        place(board, white, friend);
        assertFalse(wn.isValidMove(board, sq("f6"), whiteP));
        assertFalse(wn.isValidMove(board, sq("e6"), whiteP));
    }

    @Test
    void queenCombinesRookAndBishop() {
        QueenPiece wq = new QueenPiece(true, sq("e4"));
        place(board, white, wq);
        assertTrue(wq.isValidMove(board, sq("e8"), whiteP));
        assertTrue(wq.isValidMove(board, sq("h7"), whiteP));
    }

    @Test
    void queenBlockedMidRay() {
        QueenPiece wq = new QueenPiece(true, sq("e4"));
        place(board, white, new PawnPiece(true, sq("e6")));
        assertFalse(wq.isValidMove(board, sq("e8"), whiteP));
    }

    @Test
    void kingOneStepAndCapture() {
        KingPiece wk = new KingPiece(true, sq("e4"));
        place(board, white, wk);
        assertTrue(wk.isValidMove(board, sq("e5"), whiteP));
        assertFalse(wk.isValidMove(board, sq("e6"), whiteP));
        KingPiece bk = new KingPiece(false, sq("e5"));
        place(board, black, bk);
        assertTrue(wk.isValidMove(board, sq("e5"), whiteP));
    }

    @Test
    void kingRejectsOffBoardTarget() {
        KingPiece wk = new KingPiece(true, sq("h1"));
        place(board, white, wk);
        assertFalse(wk.isValidMove(board, new Square(-1, 7), whiteP));
        assertFalse(wk.isValidMove(board, new Square(0, 8), whiteP));
    }
}
