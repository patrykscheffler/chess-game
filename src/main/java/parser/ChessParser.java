package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.BoardPiece.PieceColor;
import parser.BoardPiece.PieceType;

/**
 * Regroups all the methods to parse notations.
 * It is through this method that the user accesses the parser package.
 * Regroups methods to check the notations, get information from it or convert it.
 * @author Clement Gautrais
 * @author Paul Chaignon
 */
public class ChessParser {
	private ChessBoard board;
	private String fen;

	/**
	 * Constructor
	 * Initialize the chess board with the FEN.
	 * @param fen The FEN.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 */
	public ChessParser(String fen) throws IncorrectFENException {
		this.fen = fen;
		this.board = new ChessBoard();
		this.board.loadFEN(this.fen);
	}

	/**
	 * Convert a LAN to a SAN using the FEN.
	 * @param lan The Long Algebraic Notation.
	 * @return The Short Algebraic Notation.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 * @throws IncorrectAlgebraicNotationException If the long algebraic notation is incorrect.
	 */
	public String convertLANToSAN(String lan) throws IncorrectFENException, IncorrectAlgebraicNotationException {
		if(lan.matches("(O-O-O|O-O)\\+?")) {
			return lan;
		}
		return UCItoPGN(lan, this.board);
	}
	
	/**
	 * Convert a SAN to a LAN using the FEN.
	 * @param san The Short Algebraic Notation.
	 * @return The Long Algebraic Notation.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 * @throws IncorrectAlgebraicNotationException If the short algebraic notation is incorrect.
	 */
	public String convertSANToLAN(String san) throws IncorrectFENException, IncorrectAlgebraicNotationException {
		// Castling:
		if(san.matches("(O-O-O|O-O)\\+?")) {
			String move = this.board.castle(san);

			return move;
		}
		// Regular move:
		if(san.matches("[RBQKPN]?[a-h]?[1-8]?[x]?[a-h][1-8][=]?[QNRB]?[+#]?")) {
			String move = parseMove(this.board, san);
			this.board.switchMove();

			return move;
		}
		throw new IncorrectAlgebraicNotationException("The SAN is neither a castling nor a regular move.");
	}
	
	/**
	 * Convert UCI style move into PGN style move
	 * @param ucimove The Long Algebraic Notation.
	 * @param board The chess board.
	 * @return The Short Algebraic Notation.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 * @throws IncorrectAlgebraicNotationException If the long algebraic notation is incorrect.
	 */
	private static String UCItoPGN(String ucimove, ChessBoard board) throws IncorrectFENException, IncorrectAlgebraicNotationException {
		if(ucimove.length()<4) {
			throw new IncorrectAlgebraicNotationException("The LAN misses parameters.");
		}
		
		char fromX = ucimove.charAt(0);
		int fromY = -1;
		try {
			fromY = Integer.parseInt(String.valueOf(ucimove.charAt(1)));
		} catch(NumberFormatException e) {
			throw new IncorrectAlgebraicNotationException("The origin coordinate of the LAN is incorrect.");
		}
		char toX = ucimove.charAt(2);
		int toY = -1;
		try {
			toY = Integer.parseInt(String.valueOf(ucimove.charAt(3)));
		} catch(NumberFormatException e) {
			throw new IncorrectAlgebraicNotationException("The destination coordinate of the LAN is incorrect.");
		}
		
		// Check the origin coordinate:
		if(!ChessBoard.letter.containsKey(fromX) || fromY<1 || fromY>8) {
			throw new IncorrectAlgebraicNotationException("The origin coordinate of the LAN is incorrect.");
		}
		
		// Check the destination coordinate:
		if(!ChessBoard.letter.containsKey(toX) || toY<1 ||  toY>8) {
			throw new IncorrectAlgebraicNotationException("The destination coordinate of the LAN is incorrect.");
		}
		
		PieceType piece = board.squares.get(fromX)[fromY].piece.type;
		boolean capture = false;

		if(board.squares.get(toX)[toY].piece != null) {
			capture = true;
		}
		
		// Castling:
		if(piece==PieceType.KING && Math.abs(ChessBoard.letter.get(fromX)-ChessBoard.letter.get(toX))==2) {
			if(toX=='g') {
				return "O-O";
			}
			return "O-O-O";
		}

		String pgnfromX = "";
		String pgnfromY = "";

		// Determine if we need fromX/fromY coordinates in PGN move:
		if(ChessRules.eval(piece, board, board.currentMove, (char)0, -1, toX, toY, capture).x!=0) {
			pgnfromX = "";
			pgnfromY = "";
		} else if(ChessRules.eval(piece, board, board.currentMove, fromX, -1, toX, toY, capture).x!=0) {
			pgnfromX = String.valueOf(fromX);
			pgnfromY = "";
		} else if(ChessRules.eval(piece, board, board.currentMove, (char)0, fromY, toX, toY, capture).x!=0) {
			pgnfromX = "";
			pgnfromY = String.valueOf(fromY);
		} else if(ChessRules.eval(piece, board, board.currentMove, fromX, fromY, toX, toY, capture).x!=0) {
			pgnfromX = String.valueOf(fromX);
			pgnfromY = String.valueOf(fromY);
		}

		String pgnpiece = PieceType.getLetter(piece, true);

		// En passant capture:
		if(board.enPassant.equals(String.valueOf(toX)+toY) && piece==PieceType.PAWN) {
			capture = true;
		}

		String pgncapture = "";
		if(capture) {
			pgncapture = "x";
		}
		if(capture && piece==PieceType.PAWN) {
			pgnfromX = String.valueOf(fromX);
		}

		// Return the PGN style move.
		return pgnpiece + pgnfromX + pgnfromY + pgncapture + toX + toY;
	}
	
	/**
	 * Parse a short algebraic notation to a long using the chess board.
	 * @param board The chess board.
	 * @param token The Short Algebraic Notation.
	 * @return The Long Algebraic Notation.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 * @throws IncorrectAlgebraicNotationException If the short algebraic notation is incorrect.
	 */
	private String parseMove(ChessBoard board, String token) throws IncorrectFENException, IncorrectAlgebraicNotationException {
		Matcher matcher = Pattern.compile("([RBQKPN])?([a-h])?([1-8])?([x])?([a-h])([1-8])([=]?)([QNRB]?)([+#]?)").matcher(token);
		char[] moveArray = new char[6];
		if(matcher.find()) {
			for(int i=0; i<6; i++) {
				String match = matcher.group(i+1);
				if(match==null) {
					moveArray[i] = 0;
				} else if(match.length()==1) {
					moveArray[i] =  match.charAt(0);
				} else {
					moveArray[i] = 0;
				}
			}
		} else {
			throw new IncorrectAlgebraicNotationException("The short algebraic notation is irregular.");
		}
		
		// Parse the type of the piece:
		PieceType piece = PieceType.getType(moveArray[0]);

		// Parse the origin coordinates:
		char fromX = 0;
		if(moveArray[1]!=0) {
			fromX = moveArray[1];
		}
		int fromY = -1;
		if(moveArray[2]!=0) {
			try {
				fromY = Integer.parseInt(String.valueOf(moveArray[2]));
			} catch(NumberFormatException e) {
				throw new IncorrectAlgebraicNotationException("The origin coordinate of the SAN is incorrect.");
			}
		}

		// Parse the capture character:
		// There should be an 'x' character is there was a capture move.
		boolean capture;
		if(moveArray[3]!=0) {
			capture = true;
		} else {
			capture = false;
		}

		// Parse the destination coordinates:
		char toX = 0;
		int toY = -1;
		if(moveArray[4]!=0) {
			toX = moveArray[4];
		}
		if(moveArray[5]!=0) {
			try {
				toY = Integer.parseInt(String.valueOf(moveArray[5]));
			} catch(NumberFormatException e) {
				throw new IncorrectAlgebraicNotationException("The destination coordinate of the SAN is incorrect.");
			}
		}
		
		// Check the destination coordinate:
		if(!ChessBoard.letter.containsKey(toX) || toY<1 || toY>8) {
			throw new IncorrectAlgebraicNotationException("The destination coordinate of the SAN is incorrect.");
		}

		// Determine the location of the piece to move using chess rules and incomplete information about it.
		BoardSquare pieceXY = ChessRules.eval(piece, board, board.currentMove, fromX, fromY, toX, toY, capture);

		this.board.makeMove(pieceXY.x, pieceXY.y, toX, toY, capture);

		// Return the Long Algebraic Notation.
		return String.valueOf(pieceXY.x)+pieceXY.y+toX+toY;
	}
	
	/**
	 * Set enPassant parameter at - in FEN if no pawn can play enPassant.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 */
	public void checkEnPassant() throws IncorrectFENException {
		boolean needEnPassant = false;
		if(!this.board.enPassant.equals("-")) {
			int mod;
			if(board.currentMove==PieceColor.WHITE) {
				mod = 1;
			} else {
				mod = -1;
			}
			char enPassantX = this.board.enPassant.charAt(0);
			int enPassantY;
			try {
				enPassantY = Integer.parseInt(""+this.board.enPassant.charAt(1));
			} catch(NumberFormatException e) {
				throw new IncorrectFENException("The FEN is incorrect.");
			}
			char enPassantGauche = enPassantX;
			enPassantGauche -= 1;
			char enPassantDroite = enPassantX;
			enPassantDroite += 1;

			if(enPassantX!='a' && this.board.squares.get(enPassantGauche)[enPassantY-mod]!=null 
					&& this.board.squares.get(enPassantGauche)[enPassantY-mod].piece!=null 
					&& this.board.squares.get(enPassantGauche)[enPassantY-mod].piece.type==PieceType.PAWN 
					&& this.board.squares.get(enPassantGauche)[enPassantY-mod].piece.color==board.currentMove) {
				needEnPassant = true;
			} else if(enPassantX!='h' && this.board.squares.get(enPassantDroite)[enPassantY-mod]!=null 
					&& this.board.squares.get(enPassantDroite)[enPassantY-mod].piece!=null 
					&& this.board.squares.get(enPassantDroite)[enPassantY-mod].piece.type==PieceType.PAWN 
					&& this.board.squares.get(enPassantDroite)[enPassantY-mod].piece.color==board.currentMove) {
				needEnPassant = true;
			}
		}
		if(!needEnPassant) {
			this.board.enPassant = "-";
		}
	}
	
	/**
	 * Re-build the FEN from the board.
	 * @param reduced True if the FEN need to be reduced.
	 * @return The FEN.
	 */
	public String getFEN(boolean reduced) {
		return this.board.currentFEN(reduced);
	}
	
	/**
	 * Check is the FEN is correct.
	 * The FEN must contain at least 3 white spaces and exactly 8 slashes for the board description.
	 * @param fen The FEN to check.
	 * @return True if the FEN is correct.
	 */
	public static boolean isCorrectFEN(String fen) {
		// Check the white spaces:
		String[] fenArray = fen.split(" ");
		if(fenArray.length<=3) {
			return false;
		}
		// Check the slashes:
		String[] boardArray = fenArray[0].split("/");
		if(boardArray.length==8) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sees if board is in check state for the current player.
	 * @param current True to check the next player.
	 * @return True if the board is in check state.
	 * @throws IncorrectFENException If the FEN is incorrect.
	 */
	public boolean check(boolean current) throws IncorrectFENException {
		if(current) {
			return ChessRules.check(this.board, this.board.currentMove);
		}
		return ChessRules.check(this.board, PieceColor.invert(this.board.currentMove));
	}
}