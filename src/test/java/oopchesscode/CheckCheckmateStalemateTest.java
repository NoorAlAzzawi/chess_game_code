package oopchesscode;

import oopchesscode.board.ChessBoard;
import oopchesscode.game.ChessApp;
import oopchesscode.pieces.KingPiece;
import oopchesscode.pieces.QueenPiece;
import oopchesscode.pieces.RookPiece;
import oopchesscode.player.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static oopchesscode.BoardTestSupport.clearBoard;
import static oopchesscode.BoardTestSupport.place;
import static oopchesscode.BoardTestSupport.sq;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Captures on the board, blocked legal lines, illegal king-in-check moves, check, checkmate, stalemate.
 */
class CheckCheckmateStalemateTest {

    private ChessBoard board;
    private Participant white;
    private Participant black;
    private ChessApp app;

    @BeforeEach
    void setup() {
        board = new ChessBoard();
        white = new Participant(true);
        black = new Participant(false);
        app = new ChessApp();
        clearBoard(board, white, black);
    }

    @Test
    void relocateRemovesCapturedFromOwnerList() {
        RookPiece wr = new RookPiece(true, sq("e4"));
        RookPiece br = new RookPiece(false, sq("e8"));
        place(board, white, wr);
        place(board, black, br);
        board.relocate(sq("e4"), sq("e8"), black);
        assertFalse(black.getActivePieces().contains(br));
        assertTrue(white.getActivePieces().contains(wr));
        assertTrue(board.pieceAt(sq("e8")) == wr);
    }

    @Test
    void isMoveLegalBlocksMovingIntoCheck() {
        KingPiece wk = new KingPiece(true, sq("e1"));
        QueenPiece bq = new QueenPiece(false, sq("e8"));
        place(board, white, wk);
        place(board, black, bq);
        assertFalse(app.isMoveLegal(board, sq("e1"), sq("e2"), white, black));
    }

    @Test
    void isMoveLegalAllowsSafeKingMove() {
        KingPiece wk = new KingPiece(true, sq("e1"));
        QueenPiece bq = new QueenPiece(false, sq("h8"));
        place(board, white, wk);
        place(board, black, bq);
        assertTrue(app.isMoveLegal(board, sq("e1"), sq("e2"), white, black));
    }

    @Test
    void isMoveLegalBlocksPinnedPieceFromUnpinningKing() {
        KingPiece wk = new KingPiece(true, sq("e1"));
        RookPiece wr = new RookPiece(true, sq("e4"));
        QueenPiece bq = new QueenPiece(false, sq("e8"));
        place(board, white, wk);
        place(board, white, wr);
        place(board, black, bq);
        assertFalse(app.isMoveLegal(board, sq("e4"), sq("a4"), white, black));
    }

    @Test
    void isThreatenedWhenKingInEnemyRay() {
        KingPiece wk = new KingPiece(true, sq("e1"));
        QueenPiece bq = new QueenPiece(false, sq("e8"));
        place(board, white, wk);
        place(board, black, bq);
        board.updateThreatenedSquares(black);
        assertTrue(app.isThreatened(white, black, board));
    }

    @Test
    void isCheckmatedWhenInCheckAndNoLegalMoves() {
        KingPiece wk = new KingPiece(true, sq("h1"));
        RookPiece br1 = new RookPiece(false, sq("g1"));
        RookPiece br2 = new RookPiece(false, sq("g2"));
        KingPiece bk = new KingPiece(false, sq("a8"));
        place(board, white, wk);
        place(board, black, br1);
        place(board, black, br2);
        place(board, black, bk);
        board.updateThreatenedSquares(black);
        assertTrue(app.isThreatened(white, black, board));
        assertFalse(app.hasAnyLegalMove(white, black, board));
        assertTrue(app.isCheckmated(white, black, board));
    }

    /**
     * Black is stalemated: not in check, but every king move is illegal.
     * Black Ka8, White Qc7, White Kb6 — queen and king cover a7, b7, b8 without checking a8.
     */
    @Test
    void stalemateBlackKingCornerBlockedWithoutCheck() {
        KingPiece bk = new KingPiece(false, sq("a8"));
        QueenPiece wq = new QueenPiece(true, sq("c7"));
        KingPiece wk = new KingPiece(true, sq("b6"));
        place(board, black, bk);
        place(board, white, wq);
        place(board, white, wk);
        board.updateThreatenedSquares(white);
        assertFalse(app.isThreatened(black, white, board), "stalemate requires not to be in check");
        assertFalse(app.hasAnyLegalMove(black, white, board));
        assertFalse(app.isCheckmated(black, white, board));
    }

    @Test
    void hasAnyLegalMoveTrueWhenUnpinnedEscapeExists() {
        KingPiece wk = new KingPiece(true, sq("e1"));
        QueenPiece bq = new QueenPiece(false, sq("h8"));
        place(board, white, wk);
        place(board, black, bq);
        board.updateThreatenedSquares(black);
        assertTrue(app.hasAnyLegalMove(white, black, board));
    }
}
