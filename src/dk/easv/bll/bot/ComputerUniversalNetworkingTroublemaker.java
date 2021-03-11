package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Code 4: A New Code
 */

public class ComputerUniversalNetworkingTroublemaker implements IBot {
    private static final String BOTNAME = "C.U.N.T B0T";
    private Random rand = new Random();

    //List of the move-strategy behind the bot
    protected int[][] moveStrat = {
            {1, 1}}; // Rush mid bois


    @Override
    public IMove doMove(IGameState state) {
        List<IMove> moves = state.getField().getAvailableMoves();
        //Find macroboard to play in
        for (int[] move : moveStrat) {
            //Check if there is a winning move on the field, when doMove() is executed.
            List<IMove> winningMove = getWinningMoves(state);
            if (!winningMove.isEmpty()) {
                return winningMove.get(0);
            }
            //Check if there is a blockable move on the field, when doMove() is executed.
            List<IMove> blockingMove = getBlockingMoves(state);
            if (!blockingMove.isEmpty()) {
                return blockingMove.get(0);
            }
            if (state.getField().getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD)){
            //find move to play
                for (int[] selectedMove : moveStrat) {
                    int x = move[0] * 3 + selectedMove[0];
                    int y = move[1] * 3 + selectedMove[1];
                    if (state.getField().getBoard()[x][y].equals(IField.EMPTY_FIELD)) {
                        return new Move(x, y);
                    }
                }
            }
        }
        //NOTE: Something failed, just take the first available move I guess!
        return moves.get(rand.nextInt(moves.size()));
    }


    private boolean isWinningMove (IGameState state, IMove move, String player){
        String[][] board = state.getField().getBoard();
        boolean isRowWin = true;
        // Row checking
        int startX = move.getX() - (move.getX() % 3);
        int endX = startX + 2;
        for (int x = startX; x <= endX; x++) {
            if (x != move.getX())
                if (!board[x][move.getY()].equals(player))
                    isRowWin = false;
        }

        boolean isColumnWin = true;
        // Column checking
        int startY = move.getY() - (move.getY() % 3);
        int endY = startY + 2;
        for (int y = startY; y <= endY; y++) {
            if (y != move.getY())
                if (!board[move.getX()][y].equals(player))
                    isColumnWin = false;
        }


        boolean isDiagWin = true;

        // Diagonal checking left-top to right-bottom
        if (!(move.getX() == startX && move.getY() == startY))
            if (!board[startX][startY].equals(player))
                isDiagWin = false;
        if (!(move.getX() == startX + 1 && move.getY() == startY + 1))
            if (!board[startX + 1][startY + 1].equals(player))
                isDiagWin = false;
        if (!(move.getX() == startX + 2 && move.getY() == startY + 2))
            if (!board[startX + 2][startY + 2].equals(player))
                isDiagWin = false;

        boolean isOppositeDiagWin = true;
        // Diagonal checking left-bottom to right-top
        if (!(move.getX() == startX && move.getY() == startY + 2))
            if (!board[startX][startY + 2].equals(player))
                isOppositeDiagWin = false;
        if (!(move.getX() == startX + 1 && move.getY() == startY + 1))
            if (!board[startX + 1][startY + 1].equals(player))
                isOppositeDiagWin = false;
        if (!(move.getX() == startX + 2 && move.getY() == startY))
            if (!board[startX + 2][startY].equals(player))
                isOppositeDiagWin = false;

        return isColumnWin || isDiagWin || isOppositeDiagWin || isRowWin;
    }

    @Override
    public String getBotName () {
        return BOTNAME;
    }

    private List<IMove> getWinningMoves (IGameState state){
        String player = "1";
        if (state.getMoveNumber() % 2 == 0)
            player = "0";

        List<IMove> avail = state.getField().getAvailableMoves();

        List<IMove> winningMoves = new ArrayList<>();
        for (IMove move : avail) {
            if (isWinningMove(state, move, player)) winningMoves.add(move);
        }
        return winningMoves;
    }
    private List<IMove> getBlockingMoves (IGameState state){
        String player = "0";
        if (state.getMoveNumber() % 2 == 0)
            player = "1";

        List<IMove> avail = state.getField().getAvailableMoves();

        List<IMove> blockingMoves = new ArrayList<>();
        for (IMove move : avail) {
            if (isWinningMove(state, move, player)) blockingMoves.add(move);
        }
        return blockingMoves;
    }
}

