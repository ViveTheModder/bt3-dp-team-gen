package cmd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class MainApp 
{
	/* a once-recursive method that generates an array containing random character DPs */
	public static int[] getCharDPs(int[] charDPs, int charCNT, int limit)
	{
		charDPs = new int[5]; //required array reset
		int index; //common index
		int rndIndex = (int) Math.random()*2; //randomly generated index
		
		//increment random elements of the array by 1 (since they're set to 0 by default) until you reach the sum
		for (index=0; index<limit; index++)
			charDPs[(int)(Math.random()*charCNT)]++;
		
		//replace DPs that are higher than 10 with just 10
		for (index=0; index<charCNT; index++)
		{
			if (charDPs[index]>10)
				charDPs[index]=10;
		}
		
		//check if the user has chosen two characters - if so, randomly pick either 0 or 1 and decrement it by 1
		if (charCNT==2 && charDPs[rndIndex]==10)
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
	
	public static void main(String[] args) throws IOException
	{
		boolean reset = false;
		int[] teammateOptions = {2,3,4,5};
		int[] limitOptions = {10,15,20};
		int optionIndex = 0; Scanner sc;
		
		int[] charDPs = null;
		int charCNT, limit;
		
		try
		{
			sc = new Scanner(System.in);
			
			System.out.println("Enter the amount of teammates: \n"
			+ "0 -> 2 characters \n1 -> 3 characters\n2 -> 4 characters\n3 -> 5 characters");
			optionIndex = sc.nextInt();
			charCNT = teammateOptions[optionIndex]; //initialize character count
			
			System.out.println("Enter the DP limit: \n"
			+ "0 -> 10 destruction points \n1 -> 15 destruction points\n2 -> 20 destruction points");
			optionIndex = sc.nextInt();
			limit = limitOptions[optionIndex]; //initialize DP limit
			
			//generate random DPs (with occasional resets)
			do
			{
				charDPs = getCharDPs(charDPs, charCNT, limit);
				reset = hasReset(charDPs, charCNT);
			} 
			while(reset);

			//create array of BT3 characters
			TenkaichiChar[] team = new TenkaichiChar[5];
			//get character names from file
			File csv = new File("bt3-dps.csv");
			//terminate program if file doesn't exist (or is empty)
			if (!csv.exists() || csv.length()==0)
				System.exit(1);
			//make random access instance of the file
			RandomAccessFile file = new RandomAccessFile(csv, "r");
			
			int rndLocation;
			String currName = null, rndLine = null;
			
			for (int i=0; i<charCNT; i++)
			{
				int currDP = 0;
				while ((currDP != charDPs[i]))
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
			file.close();
			//print results
			System.out.println("[Characters]                   [DP Amount]");
			for (int i=0; i<charCNT; i++)
				System.out.println(team[i].toString());
				
		}
		catch (ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("Invalid option.");
		}
	}
}
