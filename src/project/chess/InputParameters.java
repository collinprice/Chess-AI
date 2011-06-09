package project.chess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * COSC 3P71 Chess Project
 * 
 *	InputParameters
 * 
 * Jan. 11, 2011
 * 
 **********************************************************************/

public class InputParameters {

	private String name;
	private int ply_level;
	private char[][] board;
	
	/* Standard Chess Board */
	private static final char[][] initBoard = {
		{'R','N','B','K','Q','B','N','R'},
		{'P','P','P','P','P','P','P','P'},
		{'-','-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-','-'},
		{'p','p','p','p','p','p','p','p'},
		{'r','n','b','k','q','b','n','r'}
	};
	
	public InputParameters(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			String line = "";
			while ((line = in.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(line);
				String parameter_type = tokens.nextToken();
				
				if (parameter_type.equalsIgnoreCase("playername")) {
					tokens.nextToken();
					name = tokens.nextToken();
				} else if (parameter_type.equalsIgnoreCase("plylevel")) {
					tokens.nextToken();
					ply_level = Integer.parseInt(tokens.nextToken());
				} else if (parameter_type.equalsIgnoreCase("standardboard")) {
					tokens.nextToken();
					if (Boolean.parseBoolean(tokens.nextToken())) {
						board = initBoard;
					} else {
						in.readLine();
						char[][] newBoard = {
							in.readLine().toCharArray(),
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray()	
						};
						board = newBoard;
					}
					break;
				} else if (parameter_type.equalsIgnoreCase("customboard")) {
					if (board == null) {
						char[][] newBoard = {
							in.readLine().toCharArray(),
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray(),	
							in.readLine().toCharArray()	
						};
						board = newBoard;
					}
					break;
				} else {
					System.err.println("Invalid parameter type.");
				}
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("File: " + fileName + " was not found.");
		} catch (IOException e) {
			System.err.println("IO Exception while reading file: " + fileName);
		}
	} // constructor
	
	/**
	 * 
	 * @return Human players name
	 */
	public String getPlayerName() {
		return name;
	} // getPlayerName
	
	/**
	 * 
	 * @return ply level for alpha-beta searching
	 */
	public int getPlyLevel() {
		return ply_level;
	} // getPlyLevel
	
	/**
	 * 
	 * @return chess board piece configuration
	 */
	public char[][] getBoard() {
		return board;
	} // getBoard
	
} // InputParameters
