package project.chess;

import java.util.Vector;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * COSC 3P71 Chess Project
 * 
 *	ChessGame
 *	This class is used to operate the chess game. It initializes the game 
 *	board and its parameters from the configuartion file. The ChessGUI will 
 *	notifiy this class when the human player has made their move and will 
 *	generate the computer players move using the alphabeta function.
 * 
 * Jan. 11, 2011
 * 
 **********************************************************************/

public class ChessGame {
	
	private int ply_depth;
	private InputParameters params;
	private ChessGUI gui;
	
	public ChessGame() {
		gui = new ChessGUI();
		gui.setVisible(true);
		
		params = new InputParameters("./rsrc/parameters/chess.conf");
		System.out.println("Player Name = " + params.getPlayerName());
		System.out.println("Ply Level = " + params.getPlyLevel());
		ply_depth = params.getPlyLevel();
		gui.initBoard(params.getBoard());
		
		gui.addNotifier(this);

	} // constructor

	/**
	 * Notifies ChessGame that it is the AI's turn.
	 */
	public void runAI() {
		ChessBoard ab = alphabeta(gui.getBoard(), ply_depth, ChessBoard.ChessBoardScoreFactory(Float.NEGATIVE_INFINITY), ChessBoard.ChessBoardScoreFactory(Float.POSITIVE_INFINITY), Player.Black);
		gui.updateBoard(ab);
	} // runAI
	
	
	
	
	
	/*__________________Alpha-Beta Algorithm__________________*/
	
	private ChessBoard alphabeta(ChessBoard b, int depth, ChessBoard alpha, ChessBoard beta, Player player) {
		if (depth == 0 || b.checkMate()) {
			return b;
		}
		
		if (player == Player.Black) {
			Vector<ChessBoard> children = b.getChildren(player);
			for (ChessBoard child : children) {
				ChessBoard new_child = alphabeta(child, depth-1, alpha, beta, Player.White);
				if (alpha.getScore() < new_child.getScore()) {
					alpha = new_child;
				}
				if (beta.getScore() <= alpha.getScore()) {
					break;
				}
			}
			
			if (depth == ply_depth) {
				return alpha;
			} else {
				b.setScore(alpha.getScore());
				return b;
			}
		} else {
			Vector<ChessBoard> children = b.getChildren(player);
			for (ChessBoard child : children) {
				ChessBoard new_child = alphabeta(child, depth-1, alpha, beta, Player.Black);
				if (beta.getScore() > new_child.getScore()) {
					beta = new_child;
				}
				if (beta.getScore() <= alpha.getScore()) {
					break;
				}
			}
			
			if (depth == ply_depth) {
				return beta;
			} else {
				b.setScore(beta.getScore());
				return b;
			}
		}
	} // alphabeta
	
	/*^^^^^^^^^^^^^^^^^^Alpha-Beta Algorithm^^^^^^^^^^^^^^^^^^*/
	
	public static void main (String args[]) {	
		new ChessGame();
	} // main
} // ChessGame
