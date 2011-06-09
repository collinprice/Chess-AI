package project.chess;

import java.util.Vector;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * COSC 3P71 Chess Project
 * 
 *	ChessBoard
 *	This class contains all of the chess pieces on the board.
 * 
 * Jan. 11, 2011
 * 
 **********************************************************************/

public class ChessBoard {
    
	private Piece[][] board;
	private float score;
	private boolean whiteCastled;
	private boolean blackCastled;
	
	
	private boolean blackCheck;
	private boolean whiteCheck;
	private boolean blackMate;
	private boolean whiteMate;
	private boolean[] safeAreasBlack;
	private boolean[] knightAttackBlack;
	private boolean[] safeAreasWhite;
	private boolean[] knightAttackWhite;
	
	private Vector<Position> whiteEnemies;
	private Vector<Position> blackEnemies;
	
	private class Position {
		int x;
		int y;
		public Position(int xpos, int ypos) {
			this.x = xpos;
			this.y = ypos;
		} // constructor
	} // Position
	
	public ChessBoard(Piece[][] board, boolean wCastle, boolean bCastle) {
		whiteCastled = wCastle;
		blackCastled = bCastle;
		whiteMate = false;
		blackMate = false;
		
		this.board = new Piece[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.board[i][j] = board[i][j].clone();
			}
		}
		
		score = Evaluator.evaluateBoard(this);
		safeAreasBlack = new boolean[8];
		knightAttackBlack = new boolean[8];
		safeAreasWhite = new boolean[8];
		knightAttackWhite = new boolean[8];
		whiteEnemies = new Vector<Position>();
		blackEnemies = new Vector<Position>();

		detectBlackCheck();
		detectWhiteCheck();
	} // constructor

	private ChessBoard(float value) {
		score = value;
	} // dummy constructor
	
	public static ChessBoard ChessBoardScoreFactory(float number) {
		return new ChessBoard(number);
	} // ChessBoardScoreFactory
	
	private void detectBlackCheck() {
		blackEnemies.clear();
		blackCheck = false;
		blackMate = false;
		
		for (int i = 0; i < 8; i++) {
			safeAreasBlack[i] = true;
			knightAttackBlack[i] = true;
		}
		
		/* _______________________________________ BLACK CHECK _______________________________________ */
		
		// find king
		int x_position = -1;
		int y_position = -1;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j].getPieceType() == PieceType.King 
						&& board[i][j].getPlayer() == Player.Black) {
					x_position = j;
					y_position = i;
					break;
				}
			}
		}
		
		if (x_position == -1) {
			return;
		}
		
		// check up
		for (int i = y_position-1; i > -1; i--) {
			if (i <= -1 || i >= 8) continue;
			if (board[i][x_position].getPlayer() == Player.Black) {
				break;
			} else if (board[i][x_position].getPieceType() == PieceType.Empty
						||  board[i][x_position].getPieceType() == PieceType.Bishop
							|| board[i][x_position].getPieceType() == PieceType.Knight
								|| board[i][x_position].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				blackEnemies.add(new Position(x_position, i));
				safeAreasBlack[1] = false;
				blackCheck = true;
			}
		}
		
		// check down
		for (int i = y_position+1; i < 8; i++) {
			if (i <= -1 || i >= 8) break;
			if (board[i][x_position].getPlayer() == Player.Black) {
				break;
			} else if (board[i][x_position].getPieceType() == PieceType.Empty
						|| board[i][x_position].getPieceType() == PieceType.Bishop
							|| board[i][x_position].getPieceType() == PieceType.Knight
								|| board[i][x_position].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				blackEnemies.add(new Position(x_position, i));
				safeAreasBlack[5] = false;
				blackCheck = true;
			}
		}
		
		// check left
		for (int j = x_position-1; j > -1; j--) {
			if (j <= -1 || j >= 8) continue;
			if (board[y_position][j].getPlayer() == Player.Black) {
				break;
			} else if (board[y_position][j].getPieceType() == PieceType.Empty
					|| board[y_position][j].getPieceType() == PieceType.Bishop
						|| board[y_position][j].getPieceType() == PieceType.Knight
							|| board[y_position][j].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				blackEnemies.add(new Position(j, y_position));
				safeAreasBlack[7] = false;
				blackCheck = true;
			}
		}
		
		// check right
		for (int j = x_position+1; j < 8; j++) {
			if (j <= -1 || j >= 8) continue;
			if (board[y_position][j].getPlayer() == Player.Black) {
				break;
			} else if (board[y_position][j].getPieceType() == PieceType.Empty
					|| board[y_position][j].getPieceType() == PieceType.Bishop
						|| board[y_position][j].getPieceType() == PieceType.Knight
							|| board[y_position][j].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				blackEnemies.add(new Position(j, y_position));
				safeAreasBlack[3] = false;
				blackCheck = true;
			}
		}
		int x = x_position;
		int y = y_position;
		while (x != 0 && y != 0) {
			x--;
			y--;
			if (board[y][x].getPlayer() == Player.Black) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				blackEnemies.add(new Position(x,y));
				safeAreasBlack[0] = false;
				blackCheck = true;
			}
		}
		x = x_position;
		y = y_position;
		while (x != 0 && y != 7) {
			x--;
			y++;
			if (board[y][x].getPlayer() == Player.Black) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				blackEnemies.add(new Position(x,y));
				safeAreasBlack[6] = false;
				blackCheck = true;
			}
		}
		x = x_position;
		y = y_position;
		while (x != 7 && y != 0) {
			x++;
			y--;
			if (board[y][x].getPlayer() == Player.Black) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				blackEnemies.add(new Position(x,y));
				safeAreasBlack[2] = false;
				blackCheck = true;
			}
		}
		x = x_position;
		y = y_position;
		while (x != 7 && y != 7) {
			x++;
			y++;
			if (board[y][x].getPlayer() == Player.Black) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				blackEnemies.add(new Position(x,y));
				safeAreasBlack[4] = false;
				blackCheck = true;
			}
		}
		
		// check for left pawn
		if (y_position < 7 && x_position > 0 
				&& board[y_position+1][x_position-1].getPlayer() == Player.White && board[y_position+1][x_position-1].getPieceType() == PieceType.Pawn) {
			blackEnemies.add(new Position(x_position-1, y_position+1));
			safeAreasBlack[6] = false;
			blackCheck = true;
		}
		
		// check for right pawn
		if (y_position < 7 && x_position < 7 
				&& board[y_position+1][x_position+1].getPlayer() == Player.White && board[y_position+1][x_position+1].getPieceType() == PieceType.Pawn) {
			blackEnemies.add(new Position(x_position+1, y_position+1));
			safeAreasBlack[4] = false;
			blackCheck = true;
		}
		
		// check knights
		if (y_position - 2 > -1 && x_position - 1 > -1 && board[y_position-2][x_position-1].getPlayer() != Player.Black && board[y_position-2][x_position-1].getPieceType() == PieceType.Knight) {
			knightAttackBlack[0] = false;
			blackCheck = true;
		}
		if (y_position - 2 > -1 && x_position + 1 < 8 && board[y_position-2][x_position+1].getPlayer() != Player.Black && board[y_position-2][x_position+1].getPieceType() == PieceType.Knight) {
			knightAttackBlack[1] = false;
			blackCheck = true;
		}
		if (y_position + 2 < 8 && x_position - 1 > -1 && board[y_position+2][x_position-1].getPlayer() != Player.Black && board[y_position+2][x_position-1].getPieceType() == PieceType.Knight) {
			knightAttackBlack[5] = false;
			blackCheck = true;
		}
		if (y_position + 2 < 8 && x_position + 1 < 8 && board[y_position+2][x_position+1].getPlayer() != Player.Black && board[y_position+2][x_position+1].getPieceType() == PieceType.Knight) {
			knightAttackBlack[4] = false;
			blackCheck = true;
		}
		
		if (y_position - 1 > -1 && x_position - 2 > -1 && board[y_position-1][x_position-2].getPlayer() != Player.Black && board[y_position-1][x_position-2].getPieceType() == PieceType.Knight) {
			knightAttackBlack[7] = false;
			blackCheck = true;
		}
		if (y_position - 1 > -1 && x_position + 2 < 8 && board[y_position-1][x_position+2].getPlayer() != Player.Black && board[y_position-1][x_position+2].getPieceType() == PieceType.Knight) {
			knightAttackBlack[2] = false;
			blackCheck = true;
		}
		if (y_position + 1 < 8 && x_position - 2 > -1 && board[y_position+1][x_position-2].getPlayer() != Player.Black && board[y_position+1][x_position-2].getPieceType() == PieceType.Knight) {
			knightAttackBlack[6] = false;
			blackCheck = true;
		}
		if (y_position + 1 < 8 && x_position + 2 < 8 && board[y_position+1][x_position+2].getPlayer() != Player.Black && board[y_position+1][x_position+2].getPieceType() == PieceType.Knight) {
			knightAttackBlack[3] = false;
			blackCheck = true;
		}
		
		int i = y_position;
		int j = x_position;
		
		if (i - 1 > -1 && board[i-1][j].getPlayer() == Player.Black) {
			safeAreasBlack[0] = false;
		}
		if (i + 1 < 8 && board[i+1][j].getPlayer() == Player.Black) {
			safeAreasBlack[5] = false;
		}
		if (j - 1 > -1 && board[i][j-1].getPlayer() == Player.Black) {
			safeAreasBlack[7] = false;
		}
		if (j + 1 < 8 && board[i][j+1].getPlayer() == Player.Black) {
			safeAreasBlack[3] = false;
		}
		if (i + 1 < 8 && j + 1 < 8 && board[i+1][j+1].getPlayer() == Player.Black) {
			safeAreasBlack[4] = false;
		}
		if (i - 1 > -1 && j + 1 < 8 && board[i-1][j+1].getPlayer() == Player.Black) {
			safeAreasBlack[2] = false;
		}
		if (i + 1 < 8 && j - 1 > -1 && board[i+1][j-1].getPlayer() == Player.Black) {
			safeAreasBlack[6] = false;
		}
		if (i - 1 > -1 && j - 1 > -1 && board[i-1][j-1].getPlayer() == Player.Black) {
			safeAreasBlack[0] = false;
		}
		
		/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ BLACK CHECK ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */
		
	} // detectBlackCheck
	
	private void detectWhiteCheck() {
		whiteEnemies.clear();
		whiteCheck = false;
		whiteMate = false;
		
		for (int i = 0; i < 8; i++) {
			safeAreasWhite[i] = true;
			knightAttackWhite[i] = true;
		}
		
		/* _______________________________________ WHITE CHECK _______________________________________ */
		// find king
		int x_position = -1;
		int y_position = -1;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j].getPieceType() == PieceType.King 
						&& board[i][j].getPlayer() == Player.White) {
					x_position = j;
					y_position = i;
					break;
				}
			}
		}
		if (x_position == -1) {
			return;
		}
		
		// check up
		for (int i = y_position-1; i > -1; i--) {
			if (i <= -1 || i >= 8) continue;
			if (board[i][x_position].getPlayer() == Player.White) {
				break;
			} else if (board[i][x_position].getPieceType() == PieceType.Empty
						||  board[i][x_position].getPieceType() == PieceType.Bishop
							|| board[i][x_position].getPieceType() == PieceType.Knight
								|| board[i][x_position].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				whiteEnemies.add(new Position(x_position, i));
				safeAreasWhite[1] = false;
				whiteCheck = true;
			}
		}
		
		// check down
		for (int i = y_position+1; i < 8; i++) {
			if (i <= -1 || i >= 8) continue;
			if (board[i][x_position].getPlayer() == Player.White) {
				break;
			} else if (board[i][x_position].getPieceType() == PieceType.Empty
						|| board[i][x_position].getPieceType() == PieceType.Bishop
							|| board[i][x_position].getPieceType() == PieceType.Knight
								|| board[i][x_position].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				whiteEnemies.add(new Position(x_position, i));
				safeAreasWhite[5] = false;
				whiteCheck = true;
			}
		}
		
		// check left
		for (int j = x_position-1; j > -1; j--) {
			if (j <= -1 || j >= 8) continue;
			if (board[y_position][j].getPlayer() == Player.White) {
				break;
			} else if (board[y_position][j].getPieceType() == PieceType.Empty
					|| board[y_position][j].getPieceType() == PieceType.Bishop
						|| board[y_position][j].getPieceType() == PieceType.Knight
							|| board[y_position][j].getPieceType() == PieceType.Pawn)  {
				break;
			} else {
				whiteEnemies.add(new Position(j, y_position));
				safeAreasWhite[7] = false;
				whiteCheck = true;
			}
		}
		
		// check right
		for (int j = x_position+1; j < 8; j++) {
			if (j <= -1 || j >= 8) continue;
			if (board[y_position][j].getPlayer() == Player.White) {
				break;
			} else if (board[y_position][j].getPieceType() == PieceType.Empty
					|| board[y_position][j].getPieceType() == PieceType.Bishop
						|| board[y_position][j].getPieceType() == PieceType.Knight
							|| board[y_position][j].getPieceType() == PieceType.Pawn
								|| board[y_position][j].getPieceType() == PieceType.King)  {
				break;
			} else {
				whiteEnemies.add(new Position(j, y_position));
				safeAreasWhite[3] = false;
				whiteCheck = true;
			}
		}
		
		int x = x_position;
		int y = y_position;
		while (x != 0 && y != 0) {
			x--;
			y--;
			if (board[y][x].getPlayer() == Player.White) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				whiteEnemies.add(new Position(x,y));
				safeAreasWhite[0] = false;
				whiteCheck = true;
			}
		}
		x = x_position;
		y = y_position;
		while (x != 0 && y != 7) {
			x--;
			y++;
			if (board[y][x].getPlayer() == Player.White) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				whiteEnemies.add(new Position(x,y));
				safeAreasWhite[6] = false;
				whiteCheck = true;
			}
		}
		x = x_position;
		y = y_position;
		while (x != 7 && y != 0) {
			x++;
			y--;
			if (board[y][x].getPlayer() == Player.White) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				whiteEnemies.add(new Position(x,y));
				safeAreasWhite[2] = false;
				whiteCheck = true;
			}
		}
		x = x_position;
		y = y_position;
		while (x != 7 && y != 7) {
			x++;
			y++;
			if (board[y][x].getPlayer() == Player.White) {
				break;
			} else if (board[y][x].getPieceType() == PieceType.Empty
					|| board[y][x].getPieceType() == PieceType.Knight
						||board[y][x].getPieceType() == PieceType.Rook
							|| board[y][x].getPieceType() == PieceType.Pawn
								|| board[y][x].getPieceType() == PieceType.King) {
				continue;
			} else {
				whiteEnemies.add(new Position(x,y));
				safeAreasWhite[4] = false;
				whiteCheck = true;
			}
		}
		
		// check for left pawn
		if (y_position > 0 && x_position > 0 
				&& board[y_position-1][x_position-1].getPlayer() == Player.Black && board[y_position-1][x_position-1].getPieceType() == PieceType.Pawn) {
			whiteEnemies.add(new Position(x_position-1, y_position-1));
			safeAreasWhite[0] = false;
			whiteCheck = true;
		}
		
		// check for right pawn
		if (y_position > 0 && x_position < 7 
				&& board[y_position-1][x_position+1].getPlayer() == Player.Black && board[y_position-1][x_position+1].getPieceType() == PieceType.Pawn) {
			whiteEnemies.add(new Position(x_position+1, y_position-1));
			safeAreasWhite[2] = false;
			whiteCheck = true;
		}
		
		// check knights
		if (y_position - 2 > -1 && x_position - 1 > -1 && board[y_position-2][x_position-1].getPlayer() != Player.White && board[y_position-2][x_position-1].getPieceType() == PieceType.Knight) {
			knightAttackWhite[0] = false;
			whiteCheck = true;
		}
		if (y_position - 2 > -1 && x_position + 1 < 8 && board[y_position-2][x_position+1].getPlayer() != Player.White && board[y_position-2][x_position+1].getPieceType() == PieceType.Knight) {
			knightAttackWhite[1] = false;
			whiteCheck = true;
		}
		if (y_position + 2 < 8 && x_position - 1 > -1 && board[y_position+2][x_position-1].getPlayer() != Player.White && board[y_position+2][x_position-1].getPieceType() == PieceType.Knight) {
			knightAttackWhite[5] = false;
			whiteCheck = true;
		}
		if (y_position + 2 < 8 && x_position + 1 < 8 && board[y_position+2][x_position+1].getPlayer() != Player.White && board[y_position+2][x_position+1].getPieceType() == PieceType.Knight) {
			knightAttackWhite[4] = false;
			whiteCheck = true;
		}
		
		if (y_position - 1 > -1 && x_position - 2 > -1 && board[y_position-1][x_position-2].getPlayer() != Player.White && board[y_position-1][x_position-2].getPieceType() == PieceType.Knight) {
			knightAttackWhite[7] = false;
			whiteCheck = true;
		}
		if (y_position - 1 > -1 && x_position + 2 < 8 && board[y_position-1][x_position+2].getPlayer() != Player.White && board[y_position-1][x_position+2].getPieceType() == PieceType.Knight) {
			knightAttackWhite[2] = false;
			whiteCheck = true;
		}
		if (y_position + 1 < 8 && x_position - 2 > -1 && board[y_position+1][x_position-2].getPlayer() != Player.White && board[y_position+1][x_position-2].getPieceType() == PieceType.Knight) {
			knightAttackWhite[6] = false;
			whiteCheck = true;
		}
		if (y_position + 1 < 8 && x_position + 2 < 8 && board[y_position+1][x_position+2].getPlayer() != Player.White && board[y_position+1][x_position+2].getPieceType() == PieceType.Knight) {
			knightAttackWhite[3] = false;
			whiteCheck = true;
		}

		int i = y_position;
		int j = x_position;
		
		if (i - 1 > -1 && board[i-1][j].getPlayer() == Player.White) {
			safeAreasWhite[0] = false;
		}
		if (i + 1 < 8 && board[i+1][j].getPlayer() == Player.White) {
			safeAreasWhite[5] = false;
		}
		if (j - 1 > -1 && board[i][j-1].getPlayer() == Player.White) {
			safeAreasWhite[7] = false;
		}
		if (j + 1 < 8 && board[i][j+1].getPlayer() == Player.White) {
			safeAreasWhite[3] = false;
		}
		if (i + 1 < 8 && j + 1 < 8 && board[i+1][j+1].getPlayer() == Player.White) {
			safeAreasWhite[4] = false;
		}
		if (i - 1 > -1 && j + 1 < 8 && board[i-1][j+1].getPlayer() == Player.White) {
			safeAreasWhite[2] = false;
		}
		if (i + 1 < 8 && j - 1 > -1 && board[i+1][j-1].getPlayer() == Player.White) {
			safeAreasWhite[6] = false;
		}
		if (i - 1 > -1 && j - 1 > -1 && board[i-1][j-1].getPlayer() == Player.White) {
			safeAreasWhite[0] = false;
		}
		
		/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ WHITE CHECK ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */
		
	} // detectWhiteCheck
	
	public boolean blackCheck() {
		return blackCheck;
	}
	
	public boolean whiteCheck() {
		return whiteCheck;
	}
	
	public boolean blackMate() {
		return blackMate;
	}
	
	public boolean whiteMate() {
		return whiteMate;
	}
	
	public float getScore() {
		return score;
	} // getScore
	
	public void setScore(Float s) {
		score = s;
	} // setScore

	public Vector<ChessBoard> getChildren(Player player) {
		if (player == Player.Black) {
			if (blackCheck) {
				return getSafeChildren(player);
			} else {
				return getAllChildren(player);
			}
		} else {
			if (whiteCheck) {
				return getSafeChildren(player);
			} else {
				return getAllChildren(player);
			}
		}
	} // getChildren
	
	private Vector<ChessBoard> getSafeChildren(Player player) {
		Vector<ChessBoard> children = new Vector<ChessBoard>();
		int i = 0;
		int j = 0;
		
		if (player == Player.Black) {
			for (int m = 0; m < 8; m++) {
				for (int n = 0; n < 8; n++) {
					if (board[m][n].getPieceType() == PieceType.King 
							&& board[m][n].getPlayer() == Player.Black) {
						i = m;
						j = n;
						break;
					}
				}
			}
			
			/* Move the king to adjacent square */
			if (i - 1 > -1 && safeAreasBlack[1]) {
				Piece temp = board[i-1][j];
				board[i-1][j] = board[i][j];
				board[i-1][j].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i-1][j];
				board[i][j].unMoved();
				board[i-1][j] = temp;
			}
			if (i + 1 < 8 && safeAreasBlack[5]) {
				Piece temp = board[i+1][j];
				board[i+1][j] = board[i][j];
				board[i+1][j].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i+1][j];
				board[i][j].unMoved();
				board[i+1][j] = temp;
			}
			if (j - 1 > -1 && safeAreasBlack[7]) {
				Piece temp = board[i][j-1];
				board[i][j-1] = board[i][j];
				board[i][j-1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i][j-1];
				board[i][j].unMoved();
				board[i][j-1] = temp;
			}
			if (j + 1 < 8 && safeAreasBlack[3]) {
				Piece temp = board[i][j+1];
				board[i][j+1] = board[i][j];
				board[i][j+1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i][j+1];
				board[i][j].unMoved();
				board[i][j+1] = temp;
			}
			if (i + 1 < 8 && j + 1 < 8 && safeAreasBlack[4]) {
				Piece temp = board[i+1][j+1];
				board[i+1][j+1] = board[i][j];
				board[i+1][j+1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i+1][j+1];
				board[i][j].unMoved();
				board[i+1][j+1] = temp;
			}
			if (i - 1 > -1 && j + 1 < 8 && safeAreasBlack[2]) {
				Piece temp = board[i-1][j+1];
				board[i-1][j+1] = board[i][j];
				board[i-1][j+1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i-1][j+1];
				board[i][j].unMoved();
				board[i-1][j+1] = temp;
			}
			if (i + 1 < 8 && j - 1 > -1 && safeAreasBlack[6]) {
				Piece temp = board[i+1][j-1];
				board[i+1][j-1] = board[i][j];
				board[i+1][j-1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i+1][j-1];
				board[i][j].unMoved();
				board[i+1][j-1] = temp;
			}
			if (i - 1 > -1 && j - 1 > -1 && safeAreasBlack[0]) {
				Piece temp = board[i-1][j-1];
				board[i-1][j-1] = board[i][j];
				board[i-1][j-1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i-1][j-1];
				board[i][j].unMoved();
				board[i-1][j-1] = temp;
			}
			
			/* Capture the checking piece */
			while(!blackEnemies.isEmpty()) {
				// attempt to kill p!
				Position p = blackEnemies.remove(0);
				// check up
				for (int m = p.y-1; m > -1; m--) {
					if (board[m][p.x].getPlayer() == Player.Black) {
						if (board[m][p.x].getPieceType() == PieceType.Queen
								|| board[m][p.x].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[m][p.x];
							board[m][p.x] = board[p.y][p.x];
							board[m][p.x].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[m][p.x];
							board[p.y][p.x].unMoved();
							board[m][p.x] = temp;
						}
					} else if (board[m][p.x].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check down
				for (int m = p.y+1; m < 8; m++) {
					if (board[m][p.x].getPlayer() == Player.Black) {
						if (board[m][p.x].getPieceType() == PieceType.Queen
								|| board[m][p.x].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[m][p.x];
							board[m][p.x] = board[p.y][p.x];
							board[m][p.x].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[m][p.x];
							board[p.y][p.x].unMoved();
							board[m][p.x] = temp;
						}
					} else if (board[m][p.x].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check left
				for (int m = p.x-1; m > -1; m--) {
					if (board[p.y][m].getPlayer() == Player.Black) {
						if (board[p.y][m].getPieceType() == PieceType.Queen
								|| board[p.y][m].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[p.y][m];
							board[p.y][m] = board[p.y][p.x];
							board[p.y][m].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[p.y][m];
							board[p.y][p.x].unMoved();
							board[p.y][m] = temp;
						}
					} else if (board[p.y][m].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check right
				for (int m = p.x+1; m < 8; m++) {
					if (board[p.y][m].getPlayer() == Player.Black) {
						if (board[p.y][m].getPieceType() == PieceType.Queen
								|| board[p.y][m].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[p.y][m];
							board[p.y][m] = board[p.y][p.x];
							board[p.y][m].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[p.y][m];
							board[p.y][p.x].unMoved();
							board[p.y][m] = temp;
						}
					} else if (board[p.y][m].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check up left
				int ypos = p.y;
				int xpos = p.x;
				while (xpos != 0 && ypos != 0) {
					xpos--;
					ypos--;
					if (board[ypos][xpos].getPlayer() == Player.Black) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check up right
				ypos = p.y;
				xpos = p.x;
				while (xpos != 7 && ypos != 0) {
					xpos++;
					ypos--;
					if (board[ypos][xpos].getPlayer() == Player.Black) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check down left
				ypos = p.y;
				xpos = p.x;
				while (xpos != 0 && ypos != 7) {
					xpos--;
					ypos++;
					if (board[ypos][xpos].getPlayer() == Player.Black) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.White) {
						break;
					}
				}
				
				// check down right
				ypos = p.y;
				xpos = p.x;
				while (xpos != 7 && ypos != 7) {
					xpos++;
					ypos++;
					if (board[ypos][xpos].getPlayer() == Player.Black) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.blackCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.White) {
						break;
					}
				}
				
				/* block the check */
				if (i == p.y) { // same vertical
					if (j > p.x) { // below king
						int temp = p.x;
						while (j != temp) {
							temp++;
							blackEnemies.add(new Position(temp, i));
						}
					} else {
						int temp = p.x;
						while (j != temp) {
							temp--;
							blackEnemies.add(new Position(temp, i));
						}
					}
				}
				if (j == p.x) { // same horizontal
					if (i > p.y) { // 
						int temp = p.y;
						while (i != temp) {
							temp++;
							blackEnemies.add(new Position(j, temp));
						}
					} else {
						int temp = p.y;
						while (i != temp) {
							temp--;
							blackEnemies.add(new Position(j, temp));	// infinite loop!
						}
					}
				}
				
				if (Math.abs(i - p.y) == Math.abs(j - p.x)) { // same diagonal
					if (i > p.y && j > p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy++;
							tempx++;
							blackEnemies.add(new Position(tempx, tempy));
						}
					} else if (i < p.y && j > p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy--;
							tempx++;
							blackEnemies.add(new Position(tempx, tempy));
						}
					} else if (i > p.y && j < p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy++;
							tempx--;
							blackEnemies.add(new Position(tempx, tempy));
						}
					} else if (i < p.y && j < p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy--;
							tempx--;
							blackEnemies.add(new Position(tempx, tempy));
						}
					}
				}
				
			}
			
			if (children.size() == 0) { // there are no safe places to move. therefore checkmate.
				blackMate = true;
			}
		} else {
			for (int m = 0; m < 8; m++) {
				for (int n = 0; n < 8; n++) {
					if (board[m][n].getPieceType() == PieceType.King 
							&& board[m][n].getPlayer() == Player.White) {
						i = m;
						j = n;
						break;
					}
				}
			}
			
			/* Move the king to adjacent square */
			if (i - 1 > -1 && safeAreasWhite[1]) {
				Piece temp = board[i-1][j];
				board[i-1][j] = board[i][j];
				board[i-1][j].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i-1][j];
				board[i][j].unMoved();
				board[i-1][j] = temp;
			}
			if (i + 1 < 8 && safeAreasWhite[5]) {
				Piece temp = board[i+1][j];
				board[i+1][j] = board[i][j];
				board[i+1][j].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i+1][j];
				board[i][j].unMoved();
				board[i+1][j] = temp;
			}
			if (j - 1 > -1 && safeAreasWhite[7]) {
				Piece temp = board[i][j-1];
				board[i][j-1] = board[i][j];
				board[i][j-1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i][j-1];
				board[i][j].unMoved();
				board[i][j-1] = temp;
			}
			if (j + 1 < 8 && safeAreasWhite[3]) {
				Piece temp = board[i][j+1];
				board[i][j+1] = board[i][j];
				board[i][j+1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i][j+1];
				board[i][j].unMoved();
				board[i][j+1] = temp;
			}
			if (i + 1 < 8 && j + 1 < 8 && safeAreasWhite[4]) {
				Piece temp = board[i+1][j+1];
				board[i+1][j+1] = board[i][j];
				board[i+1][j+1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i+1][j+1];
				board[i][j].unMoved();
				board[i+1][j+1] = temp;
			}
			if (i - 1 > -1 && j + 1 < 8 && safeAreasWhite[2]) {
				Piece temp = board[i-1][j+1];
				board[i-1][j+1] = board[i][j];
				board[i-1][j+1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i-1][j+1];
				board[i][j].unMoved();
				board[i-1][j+1] = temp;
			}
			if (i + 1 < 8 && j - 1 > -1 && safeAreasWhite[6]) {
				Piece temp = board[i+1][j-1];
				board[i+1][j-1] = board[i][j];
				board[i+1][j-1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i+1][j-1];
				board[i][j].unMoved();
				board[i+1][j-1] = temp;
			}
			if (i - 1 > -1 && j - 1 > -1 && safeAreasWhite[0]) {
				Piece temp = board[i-1][j-1];
				board[i-1][j-1] = board[i][j];
				board[i-1][j-1].pieceMoved();
				board[i][j] = Piece.space();
				ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
				if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
					children.add(tempBoard);
				}
				board[i][j] = board[i-1][j-1];
				board[i][j].unMoved();
				board[i-1][j-1] = temp;
			}
			
			/* Capture the checking piece */
			while(!whiteEnemies.isEmpty()) {
				// attempt to kill p!
				Position p = whiteEnemies.remove(0);
				// check up
				for (int m = p.y-1; m > -1; m--) {
					if (board[m][p.x].getPlayer() == Player.White) {
						if (board[m][p.x].getPieceType() == PieceType.Queen
								|| board[m][p.x].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[m][p.x];
							board[m][p.x] = board[p.y][p.x];
							board[m][p.x].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[m][p.x];
							board[p.y][p.x].unMoved();
							board[m][p.x] = temp;
						}
					} else if (board[m][p.x].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check down
				for (int m = p.y+1; m < 8; m++) {
					if (board[m][p.x].getPlayer() == Player.White) {
						if (board[m][p.x].getPieceType() == PieceType.Queen
								|| board[m][p.x].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[m][p.x];
							board[m][p.x] = board[p.y][p.x];
							board[m][p.x].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[m][p.x];
							board[p.y][p.x].unMoved();
							board[m][p.x] = temp;
						}
					} else if (board[m][p.x].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check left
				for (int m = p.x-1; m > -1; m--) {
					if (board[p.y][m].getPlayer() == Player.White) {
						if (board[p.y][m].getPieceType() == PieceType.Queen
								|| board[p.y][m].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[p.y][m];
							board[p.y][m] = board[p.y][p.x];
							board[p.y][m].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[p.y][m];
							board[p.y][p.x].unMoved();
							board[p.y][m] = temp;
						}
					} else if (board[p.y][m].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check right
				for (int m = p.x+1; m < 8; m++) {
					if (board[p.y][m].getPlayer() == Player.White) {
						if (board[p.y][m].getPieceType() == PieceType.Queen
								|| board[p.y][m].getPieceType() == PieceType.Rook) {
							// kill piece
							Piece temp = board[p.y][m];
							board[p.y][m] = board[p.y][p.x];
							board[p.y][m].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[p.y][m];
							board[p.y][p.x].unMoved();
							board[p.y][m] = temp;
						}
					} else if (board[p.y][m].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check up left
				int ypos = p.y;
				int xpos = p.x;
				while (xpos != 0 && ypos != 0) {
					xpos--;
					ypos--;
					if (board[ypos][xpos].getPlayer() == Player.White) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check up right
				ypos = p.y;
				xpos = p.x;
				while (xpos != 7 && ypos != 0) {
					xpos++;
					ypos--;
					if (board[ypos][xpos].getPlayer() == Player.White) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check down left
				ypos = p.y;
				xpos = p.x;
				while (xpos != 0 && ypos != 7) {
					xpos--;
					ypos++;
					if (board[ypos][xpos].getPlayer() == Player.White) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.Black) {
						break;
					}
				}
				
				// check down right
				ypos = p.y;
				xpos = p.x;
				while (xpos != 7 && ypos != 7) {
					xpos++;
					ypos++;
					if (board[ypos][xpos].getPlayer() == Player.White) {
						if (board[ypos][xpos].getPieceType() == PieceType.Queen
								|| board[ypos][xpos].getPieceType() == PieceType.Bishop) {
							// kill piece
							Piece temp = board[ypos][xpos];
							board[ypos][xpos] = board[p.y][p.x];
							board[ypos][xpos].pieceMoved();
							board[p.y][p.x] = Piece.space();
							ChessBoard tempBoard = new ChessBoard(board, whiteCastled, blackCastled);
							if (!tempBoard.whiteCheck()) {			// only add move if result is not check as well
								children.add(tempBoard);
							}
							board[p.y][p.x] = board[ypos][xpos];
							board[p.y][p.x].unMoved();
							board[ypos][xpos] = temp;
						}
					} else if (board[ypos][xpos].getPlayer() == Player.Black) {
						break;
					}
				}
				
				/* block the check */
				if (i == p.y) { // same vertical
					if (j > p.x) { // below king
						int temp = p.x;
						while (j != temp) {
							temp++;
							whiteEnemies.add(new Position(temp, i));
						}
					} else {
						int temp = p.x;
						while (j != temp) {
							temp--;
							whiteEnemies.add(new Position(temp, i));
						}
					}
				}
				if (j == p.x) { // same horizontal
					if (i > p.y) { // 
						int temp = p.y;
						while (i != temp) {
							temp++;
							whiteEnemies.add(new Position(j, temp));
						}
					} else {
						int temp = p.y;
						while (i != temp) {
							temp--;
							whiteEnemies.add(new Position(j, temp));
						}
					}
				}
				
				if (Math.abs(i - p.y) == Math.abs(j - p.x)) { // same diagonal
					if (i > p.y && j > p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy++;
							tempx++;
							whiteEnemies.add(new Position(tempx, tempy));
						}
					} else if (i < p.y && j > p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy--;
							tempx++;
							whiteEnemies.add(new Position(tempx, tempy));
						}
					} else if (i > p.y && j < p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy++;
							tempx--;
							whiteEnemies.add(new Position(tempx, tempy));
						}
					} else if (i < p.y && j < p.x) {
						int tempx = p.x;
						int tempy = p.y;
						while (i != tempy && j != tempx) {
							tempy--;
							tempx--;
							whiteEnemies.add(new Position(tempx, tempy));
						}
					}
				}
			}
			if (children.size() == 0) { // there are no safe places to move. therefore checkmate.
				whiteMate = true;
			}
		}
		
		
		return children;
	} // getSafeChildren
	
	/**
	 * 
	 * @param player alpha = true, beta = false
	 * @return
	 */
	private Vector<ChessBoard> getAllChildren(Player player) {
		Vector<ChessBoard> children = new Vector<ChessBoard>();
		
		if (player == Player.Black) {									// --- Generate Moves for Black Pieces ---
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board[i][j].getPlayer() == Player.Black) {		// First make sure the piece is black
						switch(board[i][j].getPieceType()) {			// Then select which piece it is
							case Pawn:
								/* make a double move if both spaces are empty and pawn has never moved */
								if (i == 1 
										&& board[i+1][j].getPieceType() == PieceType.Empty 
											&& board[i+2][j].getPieceType() == PieceType.Empty) {
									Piece temp = board[i+2][j];
									board[i+2][j] = board[i][j];
									board[i+2][j].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+2][j];
									board[i][j].unMoved();
									board[i+2][j] = temp;
								}
								
								/* move forward one space if spot of empty */
								if (i + 1 < 8 && board[i+1][j].getPieceType() == PieceType.Empty) {
									Piece temp = board[i+1][j];
									board[i+1][j] = board[i][j];
									board[i+1][j].pieceMoved();
									board[i][j] = Piece.space();
									if (i + 1 == 7) {
										board[7][j] = new Piece(PieceType.Queen, Player.Black);
										board[7][j].pieceMoved();
										board[7][j].pieceMoved();
									}
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									if (i + 1 == 7) {
										board[6][j] = new Piece(PieceType.Pawn, Player.Black);
										board[6][j].pieceMoved();
										board[6][j].pieceMoved();
									} else {
										board[i][j] = board[i+1][j];
									}
									board[i][j].unMoved();
									board[i+1][j] = temp;
								}
								
								/* attack right */
								if (i + 1 < 8 && j + 1 < 8 && board[i+1][j+1].getPlayer() == Player.White) {
									Piece temp = board[i+1][j+1];
									board[i+1][j+1] = board[i][j];
									board[i+1][j+1].pieceMoved();
									board[i][j] = Piece.space();
									if (i + 1 == 7) {
										board[7][j+1] = new Piece(PieceType.Queen, Player.Black);
										board[7][j+1].pieceMoved();
										board[7][j+1].pieceMoved();
									}
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									if (i + 1 == 7) {
										board[6][j] = new Piece(PieceType.Pawn, Player.Black);
										board[6][j].pieceMoved();
										board[6][j].pieceMoved();
									} else {
										board[i][j] = board[i+1][j+1];
									}
									board[i][j].unMoved();
									board[i+1][j+1] = temp;
								}
								
								/* attack left */
								if (i + 1 < 8 && j - 1 > -1 && board[i+1][j-1].getPlayer() == Player.White) {
									Piece temp = board[i+1][j-1];
									board[i+1][j-1] = board[i][j];
									board[i+1][j-1].pieceMoved();
									board[i][j] = Piece.space();
									if (i + 1 == 7) {
										board[7][j-1] = new Piece(PieceType.Queen, Player.Black);
										board[7][j-1].pieceMoved();
										board[7][j-1].pieceMoved();
									}
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									if (i + 1 == 7) {
										board[6][j] = new Piece(PieceType.Pawn, Player.Black);
										board[6][j].pieceMoved();
										board[6][j].pieceMoved();
									} else {
										board[i][j] = board[i+1][j-1];
									}
									board[i][j].unMoved();
									board[i+1][j-1] = temp;
								}
								break;
							case Knight:
								if (i - 2 > -1 && j - 1 > -1 && board[i-2][j-1].getPlayer() != Player.Black) {
									Piece temp = board[i-2][j-1];
									board[i-2][j-1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-2][j-1];
									board[i-2][j-1] = temp;
								}
								if (i - 2 > -1 && j + 1 < 8 && board[i-2][j+1].getPlayer() != Player.Black) {
									Piece temp = board[i-2][j+1];
									board[i-2][j+1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-2][j+1];
									board[i-2][j+1] = temp;
								}
								if (i + 2 < 8 && j - 1 > -1 && board[i+2][j-1].getPlayer() != Player.Black) {
									Piece temp = board[i+2][j-1];
									board[i+2][j-1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+2][j-1];
									board[i+2][j-1] = temp;
								}
								if (i + 2 < 8 && j + 1 < 8 && board[i+2][j+1].getPlayer() != Player.Black) {
									Piece temp = board[i+2][j+1];
									board[i+2][j+1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+2][j+1];
									board[i+2][j+1] = temp;
								}
								
								if (i - 1 > -1 && j - 2 > -1 && board[i-1][j-2].getPlayer() != Player.Black) {
									Piece temp = board[i-1][j-2];
									board[i-1][j-2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j-2];
									board[i-1][j-2] = temp;
								}
								if (i - 1 > -1 && j + 2 < 8 && board[i-1][j+2].getPlayer() != Player.Black) {
									Piece temp = board[i-1][j+2];
									board[i-1][j+2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j+2];
									board[i-1][j+2] = temp;
								}
								if (i + 1 < 8 && j - 2 > -1 && board[i+1][j-2].getPlayer() != Player.Black) {
									Piece temp = board[i+1][j-2];
									board[i+1][j-2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j-2];
									board[i+1][j-2] = temp;
								}
								if (i + 1 < 8 && j + 2 < 8 && board[i+1][j+2].getPlayer() != Player.Black) {
									Piece temp = board[i+1][j+2];
									board[i+1][j+2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j+2];
									board[i+1][j+2] = temp;
								}
								break;
							case Bishop:
								int x = j;
								int y = i;
								while (x != 0 && y != 0) {
									x--;
									y--;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 7) {
									x++;
									y++;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 0 && y != 7) {
									x--;
									y++;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 0) {
									x++;
									y--;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								break;
							case Rook:
								int position = i;
								while (position != 0) {
									position--;
									if (board[position][j].getPlayer() == Player.Black) {
										break;
									} else if (board[position][j].getPlayer() == Player.White) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								position = i;
								while (position != 7) {
									position++;
									if (board[position][j].getPlayer() == Player.Black) {
										break;
									} else if (board[position][j].getPlayer() == Player.White) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								
								position = j;
								while (position != 0) {
									position--;
									if (board[i][position].getPlayer() == Player.Black) {
										break;
									} else if (board[i][position].getPlayer() == Player.White) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								position = j;
								while (position != 7) {
									position++;
									if (board[i][position].getPlayer() == Player.Black) {
										break;
									} else if (board[i][position].getPlayer() == Player.White) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								break;
							case Queen:
								x = j;
								y = i;
								while (x != 0 && y != 0) {
									x--;
									y--;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 7) {
									x++;
									y++;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 0 && y != 7) {
									x--;
									y++;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 0) {
									x++;
									y--;
									if (board[y][x].getPlayer() == Player.Black) {
										break;
									} else if (board[y][x].getPlayer() == Player.White){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								
								position = i;
								while (position != 0) {
									position--;
									if (board[position][j].getPlayer() == Player.Black) {
										break;
									} else if (board[position][j].getPlayer() == Player.White) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								position = i;
								while (position != 7) {
									position++;
									if (board[position][j].getPlayer() == Player.Black) {
										break;
									} else if (board[position][j].getPlayer() == Player.White) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								
								position = j;
								while (position != 0) {
									position--;
									if (board[i][position].getPlayer() == Player.Black) {
										break;
									} else if (board[i][position].getPlayer() == Player.White) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								position = j;
								while (position != 7) {
									position++;
									if (board[i][position].getPlayer() == Player.Black) {
										break;
									} else if (board[i][position].getPlayer() == Player.White) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								
								break;
							case King:
								/* Check for Castling */
								if (!blackCastled && !board[0][4].hasMoved() && !blackCheck) { // castling has not occurred and king has not moved
									if (board[0][0].getPieceType() == PieceType.Rook
											&& !board[0][0].hasMoved()
												&& board[0][1].getPieceType() == PieceType.Empty
													&& board[0][2].getPieceType() == PieceType.Empty
														&& board[0][3].getPieceType() == PieceType.Empty) {
										// attempt left castle
										board[0][2] = board[0][4];
										board[0][2].pieceMoved();
										board[0][4] = Piece.space();
										board[0][3] = board[0][0];
										board[0][3].pieceMoved();
										board[0][0] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, true));
										board[0][4] = board[0][2];
										board[0][4].unMoved();
										board[0][0] = board[0][3];
										board[0][0].unMoved();
										board[0][3] = Piece.space();
										board[0][2] = Piece.space();
									}
									if (board[0][7].getPieceType() == PieceType.Rook 
											&& !board[0][7].hasMoved()
												&& board[0][6].getPieceType() == PieceType.Empty
													&& board[0][5].getPieceType() == PieceType.Empty) {
										// attempt right castle
										board[0][6] = board[0][4];
										board[0][6].pieceMoved();
										board[0][4] = Piece.space();
										board[0][5] = board[0][0];
										board[0][5].pieceMoved();
										board[0][7] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, true));
										board[0][4] = board[0][6];
										board[0][4].unMoved();
										board[0][7] = board[0][5];
										board[0][7].unMoved();
										board[0][5] = Piece.space();
										board[0][6] = Piece.space();
									}
								}
								if (i - 1 > -1 && board[i-1][j].getPlayer() != Player.Black) {
									Piece temp = board[i-1][j];
									board[i-1][j] = board[i][j];
									board[i-1][j].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j];
									board[i][j].unMoved();
									board[i-1][j] = temp;
								}
								if (i + 1 < 8 && board[i+1][j].getPlayer() != Player.Black) {
									Piece temp = board[i+1][j];
									board[i+1][j] = board[i][j];
									board[i+1][j].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j];
									board[i][j].unMoved();
									board[i+1][j] = temp;
								}
								if (j - 1 > -1 && board[i][j-1].getPlayer() != Player.Black) {
									Piece temp = board[i][j-1];
									board[i][j-1] = board[i][j];
									board[i][j-1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i][j-1];
									board[i][j].unMoved();
									board[i][j-1] = temp;
								}
								if (j + 1 < 8 && board[i][j+1].getPlayer() != Player.Black) {
									Piece temp = board[i][j+1];
									board[i][j+1] = board[i][j];
									board[i][j+1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i][j+1];
									board[i][j].unMoved();
									board[i][j+1] = temp;
								}
								if (i + 1 < 8 && j + 1 < 8 && board[i+1][j+1].getPlayer() != Player.Black) {
									Piece temp = board[i+1][j+1];
									board[i+1][j+1] = board[i][j];
									board[i+1][j+1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j+1];
									board[i][j].unMoved();
									board[i+1][j+1] = temp;
								}
								if (i - 1 > -1 && j + 1 < 8 && board[i-1][j+1].getPlayer() != Player.Black) {
									Piece temp = board[i-1][j+1];
									board[i-1][j+1] = board[i][j];
									board[i-1][j+1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j+1];
									board[i][j].unMoved();
									board[i-1][j+1] = temp;
								}
								if (i + 1 < 8 && j - 1 > -1 && board[i+1][j-1].getPlayer() != Player.Black) {
									Piece temp = board[i+1][j-1];
									board[i+1][j-1] = board[i][j];
									board[i+1][j-1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j-1];
									board[i][j].unMoved();
									board[i+1][j-1] = temp;
								}
								if (i - 1 > -1 && j - 1 > -1 && board[i-1][j-1].getPlayer() != Player.Black) {
									Piece temp = board[i-1][j-1];
									board[i-1][j-1] = board[i][j];
									board[i-1][j-1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j-1];
									board[i][j].unMoved();
									board[i-1][j-1] = temp;
								}
								break;
						}
					}
				}
			}
		} else if (player == Player.White) {							// --- Generate Moves for White Pieces ---
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board[i][j].getPlayer() == Player.White) {		// First make sure the piece is white
						switch(board[i][j].getPieceType()) {			// Then select which piece it is
							case Pawn:
								/* make a double move if both spaces are empty and pawn has never moved */
								if (i == 6 
										&& board[5][j].getPieceType() == PieceType.Empty 
											&& board[4][j].getPieceType() == PieceType.Empty) {
									Piece temp = board[i-2][j];
									board[i-2][j] = board[i][j];
									board[i-2][j].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-2][j];
									board[i][j].unMoved();
									board[i-2][j] = temp;
								}
								
								/* move forward one space if spot of empty */
								if (i - 1 > -1 && board[i-1][j].getPieceType() == PieceType.Empty) {
									Piece temp = board[i-1][j];
									board[i-1][j] = board[i][j];
									board[i-1][j].pieceMoved();
									board[i][j] = Piece.space();
									if (i - 1 == 0) {
										board[0][j] = new Piece(PieceType.Queen, Player.White);
										board[0][j].pieceMoved();
										board[0][j].pieceMoved();
									}
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									if (i - 1 == 0) {
										board[1][j] = new Piece(PieceType.Pawn, Player.White);
										board[1][j].pieceMoved();
										board[1][j].pieceMoved();
									} else {
										board[i][j] = board[i-1][j];
									}
									board[i][j].unMoved();
									board[i-1][j] = temp;
								}
								
								/* attack right */
								if (i - 1 > -1 && j + 1 < 8 && board[i-1][j+1].getPlayer() == Player.Black) {
									Piece temp = board[i-1][j+1];
									board[i-1][j+1] = board[i][j];
									board[i-1][j+1].pieceMoved();
									board[i][j] = Piece.space();
									if (i - 1 == 0) {
										board[0][j+1] = new Piece(PieceType.Queen, Player.White);
										board[0][j+1].pieceMoved();
										board[0][j+1].pieceMoved();
									}
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									if (i - 1 == 0) {
										board[1][j] = new Piece(PieceType.Pawn, Player.White);
										board[1][j].pieceMoved();
										board[1][j].pieceMoved();
									} else {
										board[i][j] = board[i-1][j+1];
									}
									board[i][j].unMoved();
									board[i-1][j+1] = temp;
								}
								
								/* attack left */
								if (i - 1 > -1 && j - 1 > -1 && board[i-1][j-1].getPlayer() == Player.Black) {
									Piece temp = board[i-1][j-1];
									board[i-1][j-1] = board[i][j];
									board[i-1][j-1].pieceMoved();
									board[i][j] = Piece.space();
									if (i - 1 == 0) {
										board[0][j-1] = new Piece(PieceType.Queen, Player.White);
										board[0][j-1].pieceMoved();
										board[0][j-1].pieceMoved();
									}
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									if (i - 1 == 0) {
										board[1][j] = new Piece(PieceType.Pawn, Player.White);
										board[1][j].pieceMoved();
										board[1][j].pieceMoved();
									} else {
										board[i][j] = board[i-1][j-1];
									}
									board[i][j].unMoved();
									board[i-1][j-1] = temp;
								}
								break;
							case Knight:
								if (i - 2 > -1 && j - 1 > -1 && board[i-2][j-1].getPlayer() != Player.White) {
									Piece temp = board[i-2][j-1];
									board[i-2][j-1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-2][j-1];
									board[i-2][j-1] = temp;
								}
								if (i - 2 > -1 && j + 1 < 8 && board[i-2][j+1].getPlayer() != Player.White) {
									Piece temp = board[i-2][j+1];
									board[i-2][j+1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-2][j+1];
									board[i-2][j+1] = temp;
								}
								if (i + 2 < 8 && j - 1 > -1 && board[i+2][j-1].getPlayer() != Player.White) {
									Piece temp = board[i+2][j-1];
									board[i+2][j-1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+2][j-1];
									board[i+2][j-1] = temp;
								}
								if (i + 2 < 8 && j + 1 < 8 && board[i+2][j+1].getPlayer() != Player.White) {
									Piece temp = board[i+2][j+1];
									board[i+2][j+1] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+2][j+1];
									board[i+2][j+1] = temp;
								}
								
								if (i - 1 > -1 && j - 2 > -1 && board[i-1][j-2].getPlayer() != Player.White) {
									Piece temp = board[i-1][j-2];
									board[i-1][j-2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j-2];
									board[i-1][j-2] = temp;
								}
								if (i - 1 > -1 && j + 2 < 8 && board[i-1][j+2].getPlayer() != Player.White) {
									Piece temp = board[i-1][j+2];
									board[i-1][j+2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j+2];
									board[i-1][j+2] = temp;
								}
								if (i + 1 < 8 && j - 2 > -1 && board[i+1][j-2].getPlayer() != Player.White) {
									Piece temp = board[i+1][j-2];
									board[i+1][j-2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j-2];
									board[i+1][j-2] = temp;
								}
								if (i + 1 < 8 && j + 2 < 8 && board[i+1][j+2].getPlayer() != Player.White) {
									Piece temp = board[i+1][j+2];
									board[i+1][j+2] = board[i][j];
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j+2];
									board[i+1][j+2] = temp;
								}
								break;
							case Bishop:
								int x = j;
								int y = i;
								while (x != 0 && y != 0) {
									x--;
									y--;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 7) {
									x++;
									y++;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 0 && y != 7) {
									x--;
									y++;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 0) {
									x++;
									y--;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[y][x] = temp;
									}
								}
								break;
							case Rook:
								int position = i;
								while (position != 0) {
									position--;
									if (board[position][j].getPlayer() == Player.White) {
										break;
									} else if (board[position][j].getPlayer() == Player.Black) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								position = i;
								while (position != 7) {
									position++;
									if (board[position][j].getPlayer() == Player.White) {
										break;
									} else if (board[position][j].getPlayer() == Player.Black) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								
								position = j;
								while (position != 0) {
									position--;
									if (board[i][position].getPlayer() == Player.White) {
										break;
									} else if (board[i][position].getPlayer() == Player.Black) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								position = j;
								while (position != 7) {
									position++;
									if (board[i][position].getPlayer() == Player.White) {
										break;
									} else if (board[i][position].getPlayer() == Player.Black) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								break;
							case Queen:
								x = j;
								y = i;
								while (x != 0 && y != 0) {
									x--;
									y--;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 7) {
									x++;
									y++;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 0 && y != 7) {
									x--;
									y++;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								x = j;
								y = i;
								while (x != 7 && y != 0) {
									x++;
									y--;
									if (board[y][x].getPlayer() == Player.White) {
										break;
									} else if (board[y][x].getPlayer() == Player.Black){
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[y][x].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
										break;
									} else {
										Piece temp = board[y][x];
										board[y][x] = board[i][j];
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[y][x];
										board[i][j].unMoved();
										board[y][x] = temp;
									}
								}
								
								position = i;
								while (position != 0) {
									position--;
									if (board[position][j].getPlayer() == Player.White) {
										break;
									} else if (board[position][j].getPlayer() == Player.Black) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								position = i;
								while (position != 7) {
									position++;
									if (board[position][j].getPlayer() == Player.White) {
										break;
									} else if (board[position][j].getPlayer() == Player.Black) {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
										break;
									} else {
										Piece temp = board[position][j];
										board[position][j] = board[i][j];
										board[position][j].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[position][j];
										board[i][j].unMoved();
										board[position][j] = temp;
									}
								}
								
								position = j;
								while (position != 0) {
									position--;
									if (board[i][position].getPlayer() == Player.White) {
										break;
									} else if (board[i][position].getPlayer() == Player.Black) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								position = j;
								while (position != 7) {
									position++;
									if (board[i][position].getPlayer() == Player.White) {
										break;
									} else if (board[i][position].getPlayer() == Player.Black) {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
										break;
									} else {
										Piece temp = board[i][position];
										board[i][position] = board[i][j];
										board[i][position].pieceMoved();
										board[i][j] = Piece.space();
										children.add(new ChessBoard(board, whiteCastled, blackCastled));
										board[i][j] = board[i][position];
										board[i][j].unMoved();
										board[i][position] = temp;
									}
								}
								
								break;
							case King:
								/* Check for Castling */
								if (!whiteCastled && !board[7][4].hasMoved() && !whiteCheck) { // castling has not occurred and king has not moved
									if (board[7][0].getPieceType() == PieceType.Rook
											&& !board[7][0].hasMoved()
												&& board[7][1].getPieceType() == PieceType.Empty
													&& board[7][2].getPieceType() == PieceType.Empty
														&& board[7][3].getPieceType() == PieceType.Empty) {
										// attempt left castle
										board[7][2] = board[7][4];
										board[7][2].pieceMoved();
										board[7][4] = Piece.space();
										board[7][3] = board[7][0];
										board[7][3].pieceMoved();
										board[7][0] = Piece.space();
										children.add(new ChessBoard(board, true, blackCastled));
										board[7][4] = board[7][2];
										board[7][4].unMoved();
										board[7][0] = board[7][3];
										board[7][0].unMoved();
										board[7][3] = Piece.space();
										board[7][2] = Piece.space();
									}
									if (board[7][7].getPieceType() == PieceType.Rook 
											&& !board[7][7].hasMoved()
												&& board[7][6].getPieceType() == PieceType.Empty
													&& board[7][5].getPieceType() == PieceType.Empty) {
										// attempt right castle
										board[7][6] = board[7][4];
										board[7][6].pieceMoved();
										board[7][4] = Piece.space();
										board[7][5] = board[7][0];
										board[7][5].pieceMoved();
										board[7][7] = Piece.space();
										children.add(new ChessBoard(board, true, blackCastled));
										board[7][4] = board[7][6];
										board[7][4].unMoved();
										board[7][7] = board[7][5];
										board[7][7].unMoved();
										board[7][5] = Piece.space();
										board[7][6] = Piece.space();
									}
								}
								if (i - 1 > -1 && board[i-1][j].getPlayer() != Player.White) {
									Piece temp = board[i-1][j];
									board[i-1][j] = board[i][j];
									board[i-1][j].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j];
									board[i][j].unMoved();
									board[i-1][j] = temp;
								}
								if (i + 1 < 8 && board[i+1][j].getPlayer() != Player.White) {
									Piece temp = board[i+1][j];
									board[i+1][j] = board[i][j];
									board[i+1][j].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j];
									board[i][j].unMoved();
									board[i+1][j] = temp;
								}
								if (j - 1 > -1 && board[i][j-1].getPlayer() != Player.White) {
									Piece temp = board[i][j-1];
									board[i][j-1] = board[i][j];
									board[i][j-1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i][j-1];
									board[i][j].unMoved();
									board[i][j-1] = temp;
								}
								if (j + 1 < 8 && board[i][j+1].getPlayer() != Player.White) {
									Piece temp = board[i][j+1];
									board[i][j+1] = board[i][j];
									board[i][j+1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i][j+1];
									board[i][j].unMoved();
									board[i][j+1] = temp;
								}
								if (i + 1 < 8 && j + 1 < 8 && board[i+1][j+1].getPlayer() != Player.White) {
									Piece temp = board[i+1][j+1];
									board[i+1][j+1] = board[i][j];
									board[i+1][j+1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j+1];
									board[i][j].unMoved();
									board[i+1][j+1] = temp;
								}
								if (i - 1 > -1 && j + 1 < 8 && board[i-1][j+1].getPlayer() != Player.White) {
									Piece temp = board[i-1][j+1];
									board[i-1][j+1] = board[i][j];
									board[i-1][j+1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j+1];
									board[i][j].unMoved();
									board[i-1][j+1] = temp;
								}
								if (i + 1 < 8 && j - 1 > -1 && board[i+1][j-1].getPlayer() != Player.White) {
									Piece temp = board[i+1][j-1];
									board[i+1][j-1] = board[i][j];
									board[i+1][j-1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i+1][j-1];
									board[i][j].unMoved();
									board[i+1][j-1] = temp;
								}
								if (i - 1 > -1 && j - 1 > -1 && board[i-1][j-1].getPlayer() != Player.White) {
									Piece temp = board[i-1][j-1];
									board[i-1][j-1] = board[i][j];
									board[i-1][j-1].pieceMoved();
									board[i][j] = Piece.space();
									children.add(new ChessBoard(board, whiteCastled, blackCastled));
									board[i][j] = board[i-1][j-1];
									board[i][j].unMoved();
									board[i-1][j-1] = temp;
								}
								break;
						}
					}
				}
			}
		} else {
			System.err.println("WHOS MOVES?");
		}
		
		return children;
	} // getAllChildren
	
	/**
	 * Prints out an ascii version of the board.
	 */
	public void printBoard() {
		for (int i = 0; i < 17; i++) {
			System.out.print("_");
		}
		System.out.println();
		
		for (int i = 0; i < 8; i++) {
			System.out.print("|");
			for (int j = 0; j < 8; j++) {
				System.out.print(board[i][j].getChar() + "|");
			}
			System.out.println();
		}
		
		for (int i = 0; i < 17; i++) {
			System.out.print("-");
		}
		System.out.println();
	} // printBoard

	public Piece[][] getPieces() {
		return board;
	}

	public boolean checkMate() {
		return whiteMate || blackMate;
	} // checkMate

	/*
		Moves piece f to position s.
	*/
	public void movePiece(int fI, int fJ, int sI, int sJ) {
		board[sI][sJ] = board[fI][fJ];
		board[fI][fJ] = new Piece(PieceType.Empty, Player.None);
		detectBlackCheck();
		detectWhiteCheck();
	} // movePiece
	
} // ChessBoard
