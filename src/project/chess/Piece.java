package project.chess;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * COSC 3P71 Chess Project
 * 
 *	Piece
 *	This class represents a chess Piece.
 * 
 * Jan. 11, 2011
 * 
 **********************************************************************/

public class Piece {
	
	private PieceType type;		// Chess Piece Type
	private Player player;		// Black or White
	private boolean moved;		// Has piece been moved?
	private boolean stayMoved;
	
	public Piece(PieceType type, Player player) {
		this.type = type;
		this.player = player;
		moved = false;
		stayMoved = false;
	} // construtor
	
	public Piece(PieceType type, Player player, boolean moved, boolean stay) {
		this.type = type;
		this.player = player;
		this.moved = moved;
		stayMoved = stay;
	} // copy constructor
	
	public Piece clone() {
		return new Piece(type, player, moved, stayMoved);
	} // clone
	
	/*
		Generates a blank chess space.
	*/
	public static Piece space() {
		return new Piece(PieceType.Empty, Player.None);
	} // space
	
	public Player getPlayer() {
		return player;
	} // getPlayer
	
	public PieceType getPieceType() {
		return type;
	} // getPieceType
	
	public void pieceMoved() {
		if (moved) {
			stayMoved = true;
		} else {
			moved = true;			
		}
	} // pieceMoved
	
	public void unMoved() {
		if (!stayMoved) {
			moved = false;
		}
	} // unMoved
	
	/*
		Returns true if the peice has been moved from its original position on the board.
	*/
	public boolean hasMoved() {
		return moved;
	} // hasPieceMoved
	
	public char getChar() {
		return getPieceChar(this);
	} // getChar
	
	public int getValue() {
		return getPieceValue(this.getPieceType());
	} // getValue
	
	public static int getPieceValue(PieceType t) {
		switch(t) {
			case King:
				return 32767;
			case Queen:
				return 975;
			case Rook:
				return 500;
			case Bishop:
				return 325;
			case Knight:
				return 320;
			case Pawn:
				return 100;
			default:
				return 0;	
		}
	} // getPieceValue
	
	public static char getPieceChar(Piece p) {
		if (p.getPlayer() == Player.Black) {
			switch(p.getPieceType()) {
				case King:
					return 'K';
				case Queen:
					return 'Q';
				case Rook:
					return 'R';
				case Bishop:
					return 'B';
				case Knight:
					return 'N';
				case Pawn:
					return 'P';
				default:
					return '-';	
			}
		} else if (p.getPlayer() == Player.White) {
			switch(p.getPieceType()) {
				case King:
					return 'k';
				case Queen:
					return 'q';
				case Rook:
					return 'r';
				case Bishop:
					return 'b';
				case Knight:
					return 'n';
				case Pawn:
					return 'p';
				default:
					return '-';	
			}
		} else {
			return '-';
		}
	} // getPieceChar
	
} // Piece
