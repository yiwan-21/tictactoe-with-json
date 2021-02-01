import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class tictactoe {
	
	static String[] slots = new String[9];
	static long[] score = new long[4]; //score[0] = player 1 score, score[1] = bot score, score[2] = player 1 score, score[3] = player 2 score
	static Scanner input = new Scanner(System.in);
	static boolean singlePlayer;
	
	public static void main(String[] args) {
		boolean Continue = false;
		checkRecord();
		System.out.println("-----Tic Tac Toe-----");
		System.out.println("1 -> Single Player\n2 -> Two Players");
		System.out.print("Select Mode : ");
		int mode = input.nextInt();
		
		if (mode != 1 && mode != 2) {
			while(mode != 1 && mode != 2) {
				System.out.print("Please choose only 1 or 2: ");
				mode = input.nextInt();
			}
		}
		
		if (mode == 1)
			singlePlayer = true;
		else
			singlePlayer = false;
			
		System.out.printf("Index for slots : %n"
						+ " %d | %d | %d %n"
						+ "---+---+---%n"
						+ " %d | %d | %d %n"
						+ "---+---+---%n"
						+ " %d | %d | %d %n"
						, 0, 1, 2
						, 3, 4, 5
						, 6, 7, 8);
		if (singlePlayer) {
			System.out.println("**Player : O\n**Bot : X");
			System.out.println("Score Record : ");
			System.out.println("Player : Bot = " + score[0] + " : " + score[1]);
		}
		else {
			System.out.println("**Player 1: O\n**Player 2: X");
			System.out.println("Score Record : ");
			System.out.println("Player 1 : Player 2 = " + score[2] + " : " + score[3]);
		}
		System.out.print("Press Enter to start (\"reset\" to reset score) ");
		input.nextLine();
		String reset = input.nextLine();
		if (reset.equalsIgnoreCase("reset")) 
			saveRecord("reset");
		
		do {
			if (singlePlayer)
				System.out.println("Player : Bot = " + score[0] + " : " + score[1]);
			else
				System.out.println("Player 1 : Player 2 = " + score[2] + " : " + score[3]);
			run();
			System.out.print("\nPlay again? [Y/N] ");
			input.nextLine();
			String playAgain = input.nextLine();
			if (playAgain.equals("Y")) {
				System.out.println("New Game\n");
				slots = new String[9];
				Continue = true;
			}
			else
				Continue = false;
		} while (Continue);
	}
	
	public static void run() {
		Random r = new Random();
		int i = 0; //selection
		int filledSlot = 0;
		boolean tied = false;
		
		System.out.print((singlePlayer)? "\nPlayer or Bot first[P/B] : " : "\nPlayer 1 or Player 2 first[1/2] ");
		String first = input.nextLine();
		if (first.equalsIgnoreCase("B")) {
				System.out.println("\tBot turn");
				i = r.nextInt(9);
				slots[i] = "B";
				filledSlot++;
				printPattern();
				System.out.println("\n");
		}
		if (first.equals("2")) {
			System.out.print("Enter index of the slot : [0-8] ");
			i = input.nextInt();
			slots[i] = "B";
			filledSlot++;
			printPattern();
			System.out.println("\n");
		}
		
		do {
			//first player
			if (filledSlot == 9) {
				tied = true;
				break;
			}
			System.out.println((singlePlayer)? "\tPlayer turn" : "\tPlayer 1 turn");
			System.out.print("Enter index of the slot : [0-8] ");
				i = input.nextInt();
				if (slots[i] != null) {
					while (slots[i] != null) {
						System.out.print("Please select an empty slot : ");
						i = input.nextInt();
					}
				}
			slots[i] = "P";
			filledSlot++;
			printPattern();
			System.out.println("\n");
			
			if (checkWinner() == "player")
				break;
			
			//second player
			if (filledSlot == 9) {
				tied = true;
				break;
			}
			System.out.println((singlePlayer)? "\tBot turn" : "\tPlayer 2 turn");
			if (singlePlayer) {
				do {
					i = r.nextInt(9);
				} while (slots[i] != null);
			}
			else {
				System.out.print("Enter index of the slot : [0-8] ");
				i = input.nextInt();
				if (slots[i] != null) {
					while (slots[i] != null) {
						System.out.print("Please select an empty slot : ");
						i = input.nextInt();
					}
				}
			}
			slots[i] = "B";
			filledSlot++;
			printPattern();
			System.out.println("\n");
		} while (checkWinner() == null);
		
		if (tied) {
			System.out.println("Tied!");
		}
		else if (checkWinner().equals("player")) {
			if (singlePlayer) {
				System.out.println("Player wins");
				saveRecord("player");
			}
			else {
				System.out.println("Player 1 wins");
				saveRecord("player1");
			}
		}
		else if (checkWinner().equals("bot")) {
			if (singlePlayer) {
				System.out.println("Bot wins");
				saveRecord("bot");
			}
			else {
				System.out.println("Player 2 wins");
				saveRecord("player2");
			}
		}
	}

	public static void printPattern() {
		String[] temp = new String[9];
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == (null))
				temp[i] = " ";
			else if (slots[i].equals("P"))
				temp[i] = "O";
			else if (slots[i].equals("B"))
				temp[i] = "X";
		}
		String format = String.format(" %s | %s | %s %n"
									+ "---+---+---%n"
									+ " %s | %s | %s %n"
									+ "---+---+---%n"
									+ " %s | %s | %s %n"
									, temp[0], temp[1], temp[2]
									, temp[3], temp[4], temp[5]
									, temp[6], temp[7], temp[8]);
		System.out.println(format);
	}
	
	public static String checkWinner() {
			 if (slots[0] == ("P") && slots[1] == ("P") && slots[2] == ("P"))
			return "player";
		else if (slots[3] == ("P") && slots[4] == ("P") && slots[5] == ("P"))
			return "player";
		else if (slots[6] == ("P") && slots[7] == ("P") && slots[8] == ("P"))
			return "player";
		else if (slots[0] == ("P") && slots[3] == ("P") && slots[6] == ("P"))
			return "player";
		else if (slots[1] == ("P") && slots[4] == ("P") && slots[7] == ("P"))
			return "player";
		else if (slots[2] == ("P") && slots[5] == ("P") && slots[8] == ("P"))
			return "player";
		else if (slots[0] == ("P") && slots[4] == ("P") && slots[8] == ("P"))
			return "player";
		else if (slots[2] == ("P") && slots[4] == ("P") && slots[6] == ("P"))
			return "player";
			 
		else if (slots[0] == ("B") && slots[1] == ("B") && slots[2] == ("B"))
			return "bot";
		else if (slots[3] == ("B") && slots[4] == ("B") && slots[5] == ("B"))
			return "bot";
		else if (slots[6] == ("B") && slots[7] == ("B") && slots[8] == ("B"))
			return "bot";
		else if (slots[0] == ("B") && slots[3] == ("B") && slots[6] == ("B"))
			return "bot";
		else if (slots[1] == ("B") && slots[4] == ("B") && slots[7] == ("B"))
			return "bot";
		else if (slots[2] == ("B") && slots[5] == ("B") && slots[8] == ("B"))
			return "bot";
		else if (slots[0] == ("B") && slots[4] == ("B") && slots[8] == ("B"))
			return "bot";
		else if (slots[2] == ("B") && slots[4] == ("B") && slots[6] == ("B"))
			return "bot";
			 
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void checkRecord() {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonRead = (JSONObject) parser.parse(new FileReader("record.json"));
			score[0] = (long) jsonRead.get("player");
			score[1] = (long) jsonRead.get("bot");
			score[2] = (long) jsonRead.get("player1");
			score[3] = (long) jsonRead.get("player2");
		} catch (FileNotFoundException e) { 
			try {
				PrintWriter out = new PrintWriter(new FileOutputStream("record.json"));
				JSONObject jsonWrite = new JSONObject();
				jsonWrite.put("player", 0);
				jsonWrite.put("bot", 0);
				jsonWrite.put("player1", 0);
				jsonWrite.put("player2", 0);
				out.write(jsonWrite.toString());
				out.close();
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
			checkRecord();
		} catch (ParseException e) {
			e.printStackTrace(); 
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void saveRecord(String result) { //"reset" = reset score
		if (result.equals("player")) 
			score[0]++;
		else if (result.equals("bot")) 
			score[1]++;
		else if (result.equals("player1"))
			score[2]++;
		else if (result.equals("player2"))
			score[3]++;
		else if (result.equals("reset")) {
			if (singlePlayer) {
				score[0] = 0;
				score[1] = 0;
			}
			else {
				score[2] = 0;
				score[3] = 0;
			}
		}
		
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("record.json"));
			JSONObject jsonWrite = new JSONObject();
			jsonWrite.put("player", score[0]);
			jsonWrite.put("bot", score[1]);
			jsonWrite.put("player1", score[2]);
			jsonWrite.put("player2", score[3]);
			out.write(jsonWrite.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}	
}