package cmd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class MainApp 
{
	private static int[] charOptions = {2,3,4,5};
	private static int[] limitOptions = {10,15,20};
	
	/* a once-recursive method that generates an array containing random character DPs */
	public static int[] getCharDPs(int[] charDPs, int[] teamOptions)
	{
		charDPs = new int[5]; //required array reset
		int index; //common index
		int rndIndex = (int) Math.random()*2; //randomly generated index
		
		//increment random elements of the array by 1 (since they're set to 0 by default) until you reach the sum
		for (index=0; index<teamOptions[1]; index++)
			charDPs[(int)(Math.random()*teamOptions[0])]++;
		
		//replace DPs that are higher than 10 with just 10
		for (index=0; index<teamOptions[0]; index++)
		{
			if (charDPs[index]>10)
				charDPs[index]=10;
		}
		
		//check if the user has chosen two characters - if so, randomly pick either 0 or 1 and decrement it by 1
		if (teamOptions[0]==2 && charDPs[rndIndex]==10)
			charDPs[rndIndex]--;
		
		return charDPs;
	}
	
	/* a method that decides whether the DP array should be reset or not based on repeated values */
	public static boolean hasReset(int[] charDPs, int charCNT)
	{
		int fixedDPcnt = 0;
		for (int index=0; index<charCNT; index++)
		{
			//increment counter for each DP set to 1
			if (charDPs[index]==1)
				fixedDPcnt++;
			//check for repeated zeroes and ones
			if (charDPs[index]==0 || fixedDPcnt>1)
				return true;
		}
		return false;
	}
	
	/* a method that allows the user to enter the amount of characters as well as the DP limit for each team */
	public static int[] getOptions()
	{
		Scanner sc = new Scanner(System.in);
		int[] teamOptions = new int[2];
		int optionIndex;
		
		System.out.println("Enter the amount of teammates: \n"
		+ "* 2 characters \n* 3 characters\n* 4 characters\n* 5 characters");
		optionIndex = sc.nextInt();
		teamOptions[0] = charOptions[optionIndex-2]; //initialize character count
		
		System.out.println("Enter the DP limit: \n"
		+ "* 10 Destruction Points \n* 15 Destruction Points\n* 20 Destruction Points");
		optionIndex = sc.nextInt();
		teamOptions[1] = limitOptions[(optionIndex/5)-2]; //initialize DP limit
		
		return teamOptions;
		
	}
	
	public static TenkaichiChar[] getTeam(int[] options1P, int[] DPset1P, RandomAccessFile file) throws IOException
	{
		int rndLocation; Scanner sc;
		TenkaichiChar[] team = new TenkaichiChar[5];
		String currName = null, rndLine = null;
		
		for (int i=0; i<options1P[0]; i++)
		{
			int currDP = 0;
			while ((currDP != DPset1P[i]))
			{
				//get random location
				rndLocation = (int) (Math.random() * file.length());
				
				file.seek(rndLocation); //go to that location in the file
				file.readLine(); //skip current line
				
				//check if end of file has been reached 
				if (file.getFilePointer()==file.length())
					continue; //if so, move on to the next iteration
				
				rndLine = file.readLine(); //get random line
				sc = new Scanner(rndLine); //create new instance of scanner
				sc.useDelimiter(";"); //change delimiter
				
				while (sc.hasNext())
				{
					currName = sc.next();
					currDP = sc.nextInt();
				}
			}
			//assign current name and DP to each TenkaichiChar in the team
			team[i] = new TenkaichiChar(currName,currDP);
		}
		return team;
	}
	public static void main(String[] args) throws IOException
	{
		boolean reset = false;
		int[] DPset1P = null;
		int[] DPset2P = null;
		
		try
		{
			System.out.println("[Player 1 Team Requirements]");
			int[] options1P = getOptions();
			System.out.println("[Player 2 Team Requirements]");
			int[] options2P = getOptions();
			
			//generate random DPs for Player 1's team
			do
			{
				DPset1P = getCharDPs(DPset1P, options1P);
				reset = hasReset(DPset1P, options1P[0]);
			} 
			while(reset);
			
			//generate random DPs for Player 2's team
			do
			{
				DPset2P = getCharDPs(DPset2P, options2P);
				reset = hasReset(DPset2P, options2P[0]);
			} 
			while(reset);
			
			//create teams/arrays storing Tenkaichi characters
			TenkaichiChar[] team1P;
			TenkaichiChar[] team2P;
			//get character names from file
			File csv = new File("bt3-dps.csv");
			//terminate program if file doesn't exist (or is empty)
			if (!csv.exists() || csv.length()==0)
				System.exit(1);
			//make random access instance of the file
			RandomAccessFile file = new RandomAccessFile(csv, "r");
			//generate Tenkaichi characters for each team
			team1P = getTeam(options1P, DPset1P, file);
			team2P = getTeam(options2P, DPset2P, file);
			file.close();
			
			//print both teams' members
			System.out.println("\n         [Player 1 Team Members]\n\n"
					+ "[Characters]                   [DP Amount]");
			for (int i=0; i<options1P[0]; i++)
				System.out.println(team1P[i].toString());
			
			System.out.println("\n         [Player 2 Team Members]\n\n"
					+ "[Characters]                   [DP Amount]");
			for (int i=0; i<options2P[0]; i++)
				System.out.println(team2P[i].toString());
		}
		catch (ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("Invalid option.");
		}
	}
}
