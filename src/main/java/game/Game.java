package game;

import parser.ChessParser;
import parser.IncorrectAlgebraicNotationException;
import parser.IncorrectFENException;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public final static int WHITE = 0;
    public final static int BLACK = 1;

    private ChessBoard board;
    private List<String> moves;
    private int playerTurn = 0;
    private int currentTurn = 0;

    public Game(String fileName, ChessBoard board) {
        List<String> moveStrings = PGNLoader.moveStringsFromPGN(PGNLoader.readFile(fileName));
        this.moves = new ArrayList<>();
        this.board = board;

        try {
            ChessParser parser = new ChessParser("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
            for (String move : moveStrings) {
                moves.add(parser.convertSANToLAN(move));
            }
        } catch (IncorrectFENException | IncorrectAlgebraicNotationException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (currentTurn < moves.size() && !this.board.isAnimation()) {
            this.board.setAnimation(true);
            this.board.makeMove(moves.get(currentTurn));
            this.currentTurn++;
        }
    }

    private void changePlayerTurn() {
        playerTurn = 1 - playerTurn;
    }

}
