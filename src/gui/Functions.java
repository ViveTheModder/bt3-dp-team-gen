package gui;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
public class Functions 
{
	/* a once-recursive method that generates an array containing random character DPs */
	public static int[] getCharDPs(int[] charDPs, int charCnt, int DPlimit)
	{
		charDPs = new int[5]; //required array reset
		int index; //common index
		int rndIndex = (int) Math.random()*2; //randomly generated index
		
		//increment random elements of the array by 1 (since they're set to 0 by default) until you reach the sum
		for (index=0; index<DPlimit; index++)
			charDPs[(int)(Math.random()*charCnt)]++;
		
		//replace DPs that are higher than 10 with just 10
		for (index=0; index<charCnt; index++)
		{
			if (charDPs[index]>10)
				charDPs[index]=10;
		}
		
		//check if the user has chosen two characters - if so, randomly pick either 0 or 1 and decrement it by 1
		if (charCnt==2 && charDPs[rndIndex]==10)
			charDPs[rndIndex]--;
		
		return charDPs;
	}
	
	/* a method that decides whether the DP array should be reset or not based on repeated values */
	public static boolean hasReset(int[] charDPs, int charCnt)
	{
		int fixedDPcnt = 0;
		for (int index=0; index<charCnt; index++)
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
	
	/* a method that returns an array of character names based on the contents of the DP array */
	public static String[] getCharNames(int charCnt, int[] DPset, RandomAccessFile file) throws IOException
	{
		int rndLocation; Scanner sc;
		String[] charNames = new String[5];
		String currName = null, rndLine = null;
		
		for (int i=0; i<charCnt; i++)
		{
			int currDP = 0;
			while ((currDP != DPset[i]))
			{
				//get random location
				rndLocation = (int) (Math.random() * file.length());
				
				file.seek(rndLocation); //go to that location in the file
				file.readLine(); //skip current line
				
				//check if end of file has been reached 
				if (file.getFilePointer()==file.length())
					continue; //if so, move on to the next iteration
				
				rndLine = file.readLine(); //get random line
				sc = new Scanner(rndLine);
				sc.useDelimiter(";"); //change delimiter
				
				while (sc.hasNext())
				{
					currName = sc.next();
					currDP = sc.nextInt();
				}
			}
			//assign current name to each character in the team
			charNames[i] = currName;
		}
		return charNames;
	}
}
