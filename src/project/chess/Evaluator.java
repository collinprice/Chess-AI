package project.chess;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * COSC 3P71 Chess Project
 * 
 *  Evaluator
 *	This class evaluates a board and returns a score for the board.
 *	The idea behind this evalution algorithm was taking from Adam Berent's website ChessBin.com.
 *	The following url is where I researched his algoithm:
 *	http://www.chessbin.com/post/Chess-Board-Evaluation.aspx
 *
 * Jan. 11, 2011
 * 
 **********************************************************************/

public class Evaluator {

	private static final int[][] PawnTable = {
		{0,  0,  0,  0,  0,  0,  0,  0},
	    {50, 50, 50, 50, 50, 50, 50, 50},
		{10, 10, 20, 30, 30, 20, 10, 10},
	   	{5,  5, 10, 27, 27, 10,  5,  5},
		{0,  0,  0, 25, 25,  0,  0,  0},
	    {5, -5,-10,  0,  0,-10, -5,  5},
		{5, 10, 10,-25,-25, 10, 10,  5},
	    {0,  0,  0,  0,  0,  0,  0,  0}
	};
	
	private static final int[][] KnightTable = {
		    {-50,-40,-30,-30,-30,-30,-40,-50},
		    {-40,-20,  0,  0,  0,  0,-20,-40},
		    {-30,  0, 10, 15, 15, 10,  0,-30},
		    {-30,  5, 15, 20, 20, 15,  5,-30},
		    {-30,  0, 15, 20, 20, 15,  0,-30},
		    {-30,  5, 10, 15, 15, 10,  5,-30},
		    {-40,-20,  0,  5,  5,  0,-20,-40},
		    {-50,-40,-20,-30,-30,-20,-40,-50}
		};
	
	private static final int[][] BishopTable = {
		    {-20,-10,-10,-10,-10,-10,-10,-20},
		    {-10,  0,  0,  0,  0,  0,  0,-10},
		    {-10,  0,  5, 10, 10,  5,  0,-10},
		    {-10,  5,  5, 10, 10,  5,  5,-10},
		    {-10,  0, 10, 10, 10, 10,  0,-10},
		    {-10, 10, 10, 10, 10, 10, 10,-10},
		    {-10,  5,  0,  0,  0,  0,  5,-10},
		    {-20,-10,-40,-10,-10,-40,-10,-20}
		};
	
	private static final int[][] KingTable = {
		   {-30, -40, -40, -50, -50, -40, -40, -30},
		   {-30, -40, -40, -50, -50, -40, -40, -30},
		   {-30, -40, -40, -50, -50, -40, -40, -30},
		   {-30, -40, -40, -50, -50, -40, -40, -30},
		   {-20, -30, -30, -40, -40, -30, -30, -20},
		   {-10, -20, -20, -20, -20, -20, -20, -10}, 
		   {20,  20,   0,   0,   0,   0,  20,  20},
		   {20,  30,  10,   0,   0,  10,  30,  20}
		};
	
	/**
		Evalutes a ChessBoard and returns the score it received.
	*/
	public static float evaluateBoard(ChessBoard board) {
		Piece[][] pieces = board.getPieces();
		float score = 0f;
		float overall_score = 0f;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j].getPieceType() != PieceType.Empty) {
					int index_x = j;
					int index_y = i;
					if (pieces[i][j].getPlayer() == Player.Black) {
						index_x = 7 - j;
						index_y = 7 - i;
					}
					
					score += pieces[i][j].getValue();
					
					if (pieces[i][j].getPieceType() == PieceType.Bishop) {
						score += BishopTable[index_y][index_x];
					} else if (pieces[i][j].getPieceType() == PieceType.Knight) {
						score += KnightTable[index_y][index_x];
					} else if (pieces[i][j].getPieceType() == PieceType.Pawn) {
						score += PawnTable[index_y][index_x];
					} else if (pieces[i][j].getPieceType() == PieceType.Rook) {
						// something
					} else if (pieces[i][j].getPieceType() == PieceType.Queen) {
						if (pieces[i][j].hasMoved()) {
							score -= 10;
						}
					} else if (pieces[i][j].getPieceType() == PieceType.King) {
						score += KingTable[index_y][index_x];
					}
					
					if (pieces[i][j].getPlayer() == Player.Black) {
						overall_score += score;
						score = 0f;
					} else {
						overall_score -= score;
						score = 0f;
					}
				}
			}
		}
		
		return overall_score;
	} // evaluateBoard
	
} // Evaluator
