package oopchesscode;

import oopchesscode.board.ChessBoard;
import oopchesscode.game.ChessApp;
import oopchesscode.pieces.BishopPiece;
import oopchesscode.pieces.KingPiece;
import oopchesscode.pieces.KnightPiece;
import oopchesscode.pieces.PawnPiece;
import oopchesscode.pieces.QueenPiece;
import oopchesscode.pieces.RookPiece;
import oopchesscode.player.Participant;
import oopchesscode.position.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static oopchesscode.BoardTestSupport.clearBoard;
import static oopchesscode.BoardTestSupport.place;
import static oopchesscode.BoardTestSupport.sq;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Extended coverage: illegal pseudo-moves, friendly captures, ray blocks,
 * piece-specific attack patterns for check, mirror checkmate, pins.
 */
class ChessComprehensiveRulesTest {

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

    @Nested
    @DisplayName("Pawn — illegal pseudo-moves")
    class PawnIllegal {

        @Test
        void whiteCannotMoveBackward() {
            PawnPiece pw = new PawnPiece(true, sq("e4"));
            place(board, white, pw);
            assertFalse(pw.isValidMove(board, sq("e3"), white));
        }

        @Test
        void whiteCannotMoveSideways() {
            PawnPiece pw = new PawnPiece(true, sq("e4"));
            place(board, white, pw);
            assertFalse(pw.isValidMove(board, sq("d4"), white));
            assertFalse(pw.isValidMove(board, sq("f4"), white));
        }

        @Test
        void cannotCaptureEmptyDiagonal() {
            PawnPiece pw = new PawnPiece(true, sq("e4"));
            place(board, white, pw);
            assertFalse(pw.isValidMove(board, sq("f5"), white));
        }

        @Test
        void cannotCaptureFriendlyOnDiagonal() {
            PawnPiece pw = new PawnPiece(true, sq("e4"));
            PawnPiece friend = new PawnPiece(true, sq("f5"));
            place(board, white, pw);
            place(board, white, friend);
            assertFalse(pw.isValidMove(board, sq("f5"), white));
        }

        @Test
        void singleStepBlockedMeansNoForwardMoves() {
            PawnPiece pw = new PawnPiece(true, sq("e4"));
            PawnPiece blocker = new PawnPiece(false, sq("e5"));
            place(board, white, pw);
            place(board, black, blocker);
            assertFalse(pw.isValidMove(board, sq("e5"), white));
        }

        @Test
        void cannotJumpTwoIfSingleStepBlocked() {
            PawnPiece pw = new PawnPiece(true, sq("e2"));
            PawnPiece blocker = new PawnPiece(false, sq("e3"));
            place(board, white, pw);
            place(board, black, blocker);
            assertFalse(pw.isValidMove(board, sq("e4"), white));
        }
    }

    @Nested
    @DisplayName("Rook — captures and illegal targets")
    class RookRules {

        @Test
        void cannotCaptureOrStepOntoFriendly() {
            RookPiece wr = new RookPiece(true, sq("e4"));
            place(board, white, wr);
            place(board, white, new PawnPiece(true, sq("e6")));
            assertFalse(wr.isValidMove(board, sq("e6"), white));
        }

        @Test
        void cannotLeapPastBlocker() {
            RookPiece wr = new RookPiece(true, sq("e4"));
            place(board, white, new PawnPiece(true, sq("e6")));
            RookPiece target = new RookPiece(false, sq("e8"));
            place(board, black, target);
            assertFalse(wr.isValidMove(board, sq("e8"), white));
        }

        @Test
        void possibleMovesEmptySquaresMatchIsValidMove() {
            RookPiece wr = new RookPiece(true, sq("e4"));
            place(board, white, wr);
            for (Square to : wr.possibleMoves(board)) {
                if (board.isEmpty(to)) {
                    assertTrue(wr.isValidMove(board, to, white), "to " + to);
                }
            }
        }
    }

    @Nested
    @DisplayName("Bishop — illegal lines and friendly capture")
    class BishopRules {

        @Test
        void cannotMoveOrthogonally() {
            BishopPiece wb = new BishopPiece(true, sq("e4"));
            place(board, white, wb);
            assertFalse(wb.isValidMove(board, sq("e8"), white));
            assertFalse(wb.isValidMove(board, sq("a4"), white));
        }

        @Test
        void cannotCaptureFriendly() {
            BishopPiece wb = new BishopPiece(true, sq("e4"));
            place(board, white, wb);
            place(board, white, new PawnPiece(true, sq("f5")));
            assertFalse(wb.isValidMove(board, sq("f5"), white));
        }

        @Test
        void possibleMovesEmptySquaresMatchIsValidMove() {
            BishopPiece wb = new BishopPiece(true, sq("c1"));
            place(board, white, wb);
            for (Square to : wb.possibleMoves(board)) {
                if (board.isEmpty(to)) {
                    assertTrue(wb.isValidMove(board, to, white), "to " + to);
                }
            }
        }
    }

    @Nested
    @DisplayName("Knight — illegal geometry")
    class KnightRules {

        @Test
        void rejectsNonKnightStep() {
            KnightPiece wn = new KnightPiece(true, sq("e4"));
            place(board, white, wn);
            assertFalse(wn.isValidMove(board, sq("e6"), white));
            assertFalse(wn.isValidMove(board, sq("f5"), white));
        }

        @Test
        void rejectsSameSquare() {
            KnightPiece wn = new KnightPiece(true, sq("e4"));
            place(board, white, wn);
            assertFalse(wn.isValidMove(board, sq("e4"), white));
        }
    }

    @Nested
    @DisplayName("Queen — illegal knight hop and friendly")
    class QueenRules {

        @Test
        void cannotMoveLikeKnight() {
            QueenPiece wq = new QueenPiece(true, sq("e4"));
            place(board, white, wq);
            assertFalse(wq.isValidMove(board, sq("f6"), white));
        }

        @Test
        void cannotCaptureFriendly() {
            QueenPiece wq = new QueenPiece(true, sq("e4"));
            place(board, white, wq);
            place(board, white, new PawnPiece(true, sq("e6")));
            assertFalse(wq.isValidMove(board, sq("e6"), white));
        }
    }

    @Nested
    @DisplayName("King — illegal leaps and same square")
    class KingRules {

        @Test
        void cannotMoveTwoSquaresOrthogonal() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            place(board, white, wk);
            assertFalse(wk.isValidMove(board, sq("e3"), white));
        }

        @Test
        void cannotMoveTwoSquaresDiagonal() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            place(board, white, wk);
            assertFalse(wk.isValidMove(board, sq("g3"), white));
        }

        @Test
        void cannotCaptureFriendly() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            place(board, white, wk);
            place(board, white, new PawnPiece(true, sq("e2")));
            assertFalse(wk.isValidMove(board, sq("e2"), white));
        }

        @Test
        void rejectsSameSquare() {
            KingPiece wk = new KingPiece(true, sq("e4"));
            place(board, white, wk);
            assertFalse(wk.isValidMove(board, sq("e4"), white));
        }
    }

    @Nested
    @DisplayName("Check — threatened king by each piece type")
    class CheckDetection {

        @Test
        void rookDeliversCheck() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            RookPiece br = new RookPiece(false, sq("e8"));
            place(board, white, wk);
            place(board, black, br);
            board.updateThreatenedSquares(black);
            assertTrue(app.isThreatened(white, black, board));
        }

        @Test
        void bishopDeliversCheck() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            BishopPiece bb = new BishopPiece(false, sq("a5"));
            place(board, white, wk);
            place(board, black, bb);
            board.updateThreatenedSquares(black);
            assertTrue(app.isThreatened(white, black, board));
        }

        @Test
        void knightDeliversCheck() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            KnightPiece bn = new KnightPiece(false, sq("f3"));
            place(board, white, wk);
            place(board, black, bn);
            board.updateThreatenedSquares(black);
            assertTrue(app.isThreatened(white, black, board));
        }

        @Test
        void queenDeliversDiagonalCheck() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            QueenPiece bq = new QueenPiece(false, sq("a5"));
            place(board, white, wk);
            place(board, black, bq);
            board.updateThreatenedSquares(black);
            assertTrue(app.isThreatened(white, black, board));
        }

        @Test
        void notInCheckWhenAttackerBlocked() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            RookPiece br = new RookPiece(false, sq("e8"));
            PawnPiece blocker = new PawnPiece(true, sq("e4"));
            place(board, white, wk);
            place(board, white, blocker);
            place(board, black, br);
            board.updateThreatenedSquares(black);
            assertFalse(app.isThreatened(white, black, board));
        }
    }

    @Nested
    @DisplayName("Checkmate / stalemate — additional positions")
    class MateAndStalemate {

        @Test
        @DisplayName("White mated: queen + king box on back rank")
        void checkmateWhiteSmotheredQueenAndKing() {
            KingPiece wk = new KingPiece(true, sq("h1"));
            QueenPiece bq = new QueenPiece(false, sq("g2"));
            KingPiece bk = new KingPiece(false, sq("g3"));
            place(board, white, wk);
            place(board, black, bq);
            place(board, black, bk);
            board.updateThreatenedSquares(black);
            assertTrue(app.isThreatened(white, black, board));
            assertFalse(app.hasAnyLegalMove(white, black, board));
            assertTrue(app.isCheckmated(white, black, board));
        }

        @Test
        @DisplayName("Black mated: white queen supported delivers mate")
        void checkmateBlackKingCorner() {
            KingPiece bk = new KingPiece(false, sq("h8"));
            QueenPiece wq = new QueenPiece(true, sq("g7"));
            KingPiece wk = new KingPiece(true, sq("g6"));
            place(board, black, bk);
            place(board, white, wq);
            place(board, white, wk);
            board.updateThreatenedSquares(white);
            assertTrue(app.isThreatened(black, white, board));
            assertFalse(app.hasAnyLegalMove(black, white, board));
            assertTrue(app.isCheckmated(black, white, board));
        }

        @Test
        void stalemateBlackKa8WhiteQc7Kb6() {
            KingPiece bk = new KingPiece(false, sq("a8"));
            QueenPiece wq = new QueenPiece(true, sq("c7"));
            KingPiece wk = new KingPiece(true, sq("b6"));
            place(board, black, bk);
            place(board, white, wq);
            place(board, white, wk);
            board.updateThreatenedSquares(white);
            assertFalse(app.isThreatened(black, white, board));
            assertFalse(app.hasAnyLegalMove(black, white, board));
            assertFalse(app.isCheckmated(black, white, board));
        }
    }

    @Nested
    @DisplayName("Legal move filter — pins and blocks")
    class LegalMoveFilter {

        @Test
        void pinnedRookCannotLeaveFile() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            RookPiece wr = new RookPiece(true, sq("e4"));
            QueenPiece bq = new QueenPiece(false, sq("e8"));
            place(board, white, wk);
            place(board, white, wr);
            place(board, black, bq);
            assertFalse(app.isMoveLegal(board, sq("e4"), sq("h4"), white, black));
            assertTrue(app.isMoveLegal(board, sq("e4"), sq("e6"), white, black));
        }

        @Test
        void legalCaptureOfChecker() {
            KingPiece wk = new KingPiece(true, sq("e1"));
            RookPiece br = new RookPiece(false, sq("e2"));
            place(board, white, wk);
            place(board, black, br);
            board.updateThreatenedSquares(black);
            assertTrue(app.isThreatened(white, black, board));
            assertTrue(app.isMoveLegal(board, sq("e1"), sq("e2"), white, black));
        }
    }

    @Nested
    @DisplayName("Board / relocate")
    class BoardRelocate {

        @Test
        void relocateWithNullOpponentSkipsCaptureListRemoval() {
            RookPiece wr = new RookPiece(true, sq("e4"));
            place(board, white, wr);
            board.relocate(sq("e4"), sq("e5"), null);
            assertTrue(board.pieceAt(sq("e5")) == wr);
            assertTrue(board.isEmpty(sq("e4")));
        }
    }
}
