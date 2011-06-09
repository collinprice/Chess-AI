package project.chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * COSC 3P71 Chess Project
 * 
 * ChessGUI
 *
 * Jan. 11, 2011
 * 
 **********************************************************************/

@SuppressWarnings("serial")
public class ChessGUI extends JFrame implements ActionListener {

	private static final int 	WIDTH = 1024;
	private static final int 	HEIGHT = 768;
	
	/* Black Chess Pieces */
	private static final ImageIcon bRook = new ImageIcon("./rsrc/chess-pieces/blackCastle.png");
	private static final ImageIcon bBishop = new ImageIcon("./rsrc/chess-pieces/blackBishop.png");
	private static final ImageIcon bKnight = new ImageIcon("./rsrc/chess-pieces/blackKnight.png");
	private static final ImageIcon bKing = new ImageIcon("./rsrc/chess-pieces/blackKing.png");
	private static final ImageIcon bQueen = new ImageIcon("./rsrc/chess-pieces/blackQueen.png");
	private static final ImageIcon bPawn = new ImageIcon("./rsrc/chess-pieces/blackPawn.png");
		
	/* White Chess Pieces */
	private static final ImageIcon wRook = new ImageIcon("./rsrc/chess-pieces/whiteCastle.png");
	private static final ImageIcon wBishop = new ImageIcon("./rsrc/chess-pieces/whiteBishop.png");
	private static final ImageIcon wKnight = new ImageIcon("./rsrc/chess-pieces/whiteKnight.png");
	private static final ImageIcon wKing = new ImageIcon("./rsrc/chess-pieces/whiteKing.png");
	private static final ImageIcon wQueen = new ImageIcon("./rsrc/chess-pieces/whiteQueen.png");
	private static final ImageIcon wPawn = new ImageIcon("./rsrc/chess-pieces/whitePawn.png");
	
	private static final ImageIcon blank = new ImageIcon("./rsrc/chess-pieces/blank.png");
	
	private ChessBoard  chessBoard;
	private JButton		firstButton;
	private boolean 	firstMove = false;
	private ChessGame	game;
	private char[][]	initialBoard;
	
	JButton[][] board = new JButton[8][8];
	
	public ChessGUI() {
		super.setTitle("JChess");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLocation(100, 100);
		Dimension dim = new Dimension(WIDTH, HEIGHT);
		super.setSize(dim);
		
		
		JPanel panel = new JPanel();
		GridLayout boardLayout = new GridLayout(8,8);
		panel.setLayout(boardLayout);
				
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				panel.add(board[i][j] = new JButton());
				board[i][j].addActionListener(this);
				board[i][j].setName("" + i + "" + j);
				board[i][j].setBackground(Color.WHITE);
			}
		}
		
		super.add(panel);
	} // constructor

	/**
	 * updates the board icons to match the board state
	 * 
	 * @param b new board state
	 */
	public void updateBoard(ChessBoard chess) {
		chessBoard = chess;
		Piece[][] b = chess.getPieces();
		boolean foundWhiteKing = false;
		
		try {
			if (b[0][0].getChar() == 'r') {
				
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Checkmate! White Wins!");
			reloadGame();
			return;
		}
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (b[i][j].getChar() == 'r') {
					board[i][j].setIcon(wRook);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'R') {
					board[i][j].setIcon(bRook);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'b') {
					board[i][j].setIcon(wBishop);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'B') {
					board[i][j].setIcon(bBishop);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'n') {
					board[i][j].setIcon(wKnight);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'N') {
					board[i][j].setIcon(bKnight);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'p') {
					board[i][j].setIcon(wPawn);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'P') {
					board[i][j].setIcon(bPawn);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'k') {
					foundWhiteKing = true;
					board[i][j].setIcon(wKing);
					if (chessBoard.whiteCheck()) {
						board[i][j].setBackground(Color.RED);
					} else {
						board[i][j].setBackground(Color.WHITE);
					}
				} else if (b[i][j].getChar() == 'K') {
					board[i][j].setIcon(bKing);
					if (chessBoard.blackCheck()) {
						board[i][j].setBackground(Color.RED);
					} else {
						board[i][j].setBackground(Color.WHITE);
					}
				} else if (b[i][j].getChar() == 'q') {
					board[i][j].setIcon(wQueen);
					board[i][j].setBackground(Color.WHITE);
				} else if (b[i][j].getChar() == 'Q') {
					board[i][j].setIcon(bQueen);
					board[i][j].setBackground(Color.WHITE);
				} else {
					board[i][j].setIcon(blank);
					board[i][j].setBackground(Color.WHITE);
				}
			}
		}
		if (!foundWhiteKing) {
			JOptionPane.showMessageDialog(this, "Checkmate! Black Wins!");
			reloadGame();
			return;
		} else if (chess.whiteCheck()) {
			JOptionPane.showMessageDialog(this, "White is in Check.");
		}
	} // updateBoard
	
	public ChessBoard getBoard() {
		return chessBoard;
	} // getBoard

	@Override
	public void actionPerformed(ActionEvent event) {
		if (firstMove) {
			JButton secondButton = (JButton) event.getSource();
			secondButton.setIcon(firstButton.getIcon());
			firstButton.setIcon(blank);
			
			String first = firstButton.getName();
			String second = secondButton.getName();
			int f_i = Integer.parseInt("" + first.charAt(0));
			int f_j = Integer.parseInt("" + first.charAt(1));
			int s_i = Integer.parseInt("" + second.charAt(0));
			int s_j = Integer.parseInt("" + second.charAt(1));
			
			chessBoard.movePiece(f_i, f_j, s_i, s_j);
			
			firstMove = false;
			firstButton.setEnabled(true);

			if (chessBoard.blackCheck()) {
				JOptionPane.showMessageDialog(this, "Black is in Check.");
			}
			game.runAI();
		} else {
			firstMove = true;
			firstButton = (JButton) event.getSource();
			firstButton.setEnabled(false);
		}
	} // actionPerformed
	
	/**
	 * Prints out an ascii version of the board.
	 */
	public void printBoard() {
		Piece[][] pieces = chessBoard.getPieces();
		for (int i = 0; i < 17; i++) {
			System.out.print("_");
		}
		System.out.println();
		
		for (int i = 0; i < 8; i++) {
			System.out.print("|");
			for (int j = 0; j < 8; j++) {
				System.out.print(pieces[i][j].getChar() + "|");
			}
			System.out.println();
		}
		
		for (int i = 0; i < 17; i++) {
			System.out.print("-");
		}
		System.out.println();
	} // printBoard
	
	/*
		Restarts the game to its original layout.
	*/
	private void reloadGame() {
		initBoard(initialBoard);
	} // reloadGame
	
	/*
		Initializes the board from the configuration file.
	*/
	public void initBoard(char[][] init) {
		initialBoard = init;
		Piece[][] pieces = new Piece[8][8];
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				switch(init[i][j]) {
					case 'p':
						pieces[i][j] = new Piece(PieceType.Pawn, Player.White);
						break;
					case 'P':
						pieces[i][j] = new Piece(PieceType.Pawn, Player.Black);
						break;
					case 'r':
						pieces[i][j] = new Piece(PieceType.Rook, Player.White);
						break;
					case 'R':
						pieces[i][j] = new Piece(PieceType.Rook, Player.Black);
						break;
					case 'n':
						pieces[i][j] = new Piece(PieceType.Knight, Player.White);
						break;
					case 'N':
						pieces[i][j] = new Piece(PieceType.Knight, Player.Black);
						break;
					case 'b':
						pieces[i][j] = new Piece(PieceType.Bishop, Player.White);
						break;
					case 'B':
						pieces[i][j] = new Piece(PieceType.Bishop, Player.Black);
						break;
					case 'k':
						pieces[i][j] = new Piece(PieceType.King, Player.White);
						break;
					case 'K':
						pieces[i][j] = new Piece(PieceType.King, Player.Black);
						break;
					case 'q':
						pieces[i][j] = new Piece(PieceType.Queen, Player.White);
						break;
					case 'Q':
						pieces[i][j] = new Piece(PieceType.Queen, Player.Black);
						break;
					case '-':
						pieces[i][j] = new Piece(PieceType.Empty, Player.None);
						break;
					default:
						pieces[i][j] = new Piece(PieceType.Empty, Player.None);
				}
			}
		}
		updateBoard(new ChessBoard(pieces, false, false));
	} // initBoard

	public void addNotifier(ChessGame chessGame) {
		game = chessGame;
	} // addActionListener
	
} // ChessGUI
