package gui;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class Controller
{
	@FXML private RadioButton DP1Pbtn1, DP1Pbtn2, DP1Pbtn3;
	@FXML private RadioButton DP2Pbtn1, DP2Pbtn2, DP2Pbtn3;
	@FXML private RadioButton char1Pbtn2, char1Pbtn3, char1Pbtn4, char1Pbtn5;
	@FXML private RadioButton char2Pbtn2, char2Pbtn3, char2Pbtn4, char2Pbtn5;
	@FXML private ListView<String> charList1, charList2;
	
	private int DPlimit1 = 10;
	private int DPlimit2 = 10;
	private int charCnt1 = 2;
	private int charCnt2 = 2;
	private String[] charNames1 = new String[5], charNames2 = new String[5];
	private String darkThemeURL = getClass().getResource("dark.css").toExternalForm();
	private String lightThemeURL = getClass().getResource("light.css").toExternalForm();
	
	public void setDPLimitForPlayer1(ActionEvent e)
	{
		RadioButton[] btnArray = {DP1Pbtn1, DP1Pbtn2, DP1Pbtn3};
		for (RadioButton btn: btnArray)
		{
			if (btn.isSelected())
				DPlimit1 = Integer.parseInt(btn.getText());
		}
	}
	
	public void setDPLimitForPlayer2(ActionEvent e)
	{
		RadioButton[] btnArray = {DP2Pbtn1, DP2Pbtn2, DP2Pbtn3};
		for (RadioButton btn: btnArray)
		{
			if (btn.isSelected())
				DPlimit2 = Integer.parseInt(btn.getText());
		}
	}
	
	public void setCharCountForPlayer1(ActionEvent e)
	{
		RadioButton[] btnArray = {char1Pbtn2, char1Pbtn3, char1Pbtn4, char1Pbtn5};
		for (RadioButton btn: btnArray)
		{
			if (btn.isSelected())
				charCnt1 = Integer.parseInt(btn.getText());
		}
	}
	
	public void setCharCountForPlayer2(ActionEvent e)
	{
		RadioButton[] btnArray = {char2Pbtn2, char2Pbtn3, char2Pbtn4, char2Pbtn5};
		for (RadioButton btn: btnArray)
		{
			if (btn.isSelected())
				charCnt2 = Integer.parseInt(btn.getText());
		}
	}

	public void generate(ActionEvent e) throws IOException
	{
		boolean reset = false;
		int[] DPset1P = null, DPset2P = null;
		
		do //generate random DPs for Player 1's team
		{
			DPset1P = Functions.getCharDPs(DPset1P, charCnt1, DPlimit1);
			reset = Functions.hasReset(DPset1P, charCnt1);
		} 
		while(reset);
		
		do //generate random DPs for Player 2's team
		{
			DPset2P = Functions.getCharDPs(DPset2P, charCnt2, DPlimit2);
			reset = Functions.hasReset(DPset2P, charCnt2);
		} 
		while(reset);
		
		File csv = new File("bt3-dps.csv");
		if (!csv.exists() || csv.length()==0) //check if csv doesn't exist (or is empty)
			System.exit(1);
		
		RandomAccessFile file = new RandomAccessFile(csv, "r");
		charNames1 = Functions.getCharNames(charCnt1, DPset1P, file);
		charNames2 = Functions.getCharNames(charCnt2, DPset2P, file);
		file.close();
		
		for (int i=0; i<charNames1.length; i++)
		{	
			if (charNames1[i] == null)
				charNames1[i] = "";
		}
			
		for (int i=0; i<charNames2.length; i++)
		{
			if (charNames2[i] == null)
				charNames2[i] = "";
		}
		
		charList1.getItems().clear();
		charList2.getItems().clear();
		charList1.getItems().addAll(charNames1);
		charList2.getItems().addAll(charNames2);
	}
	
	public void toggleTheme(ActionEvent e) throws IOException
	{
		MainApp.scn.getStylesheets().remove(darkThemeURL);
		
		if (!MainApp.scn.getStylesheets().contains(lightThemeURL))
			MainApp.scn.getStylesheets().add(lightThemeURL);
		else
		{
			MainApp.scn.getStylesheets().remove(lightThemeURL);
			MainApp.scn.getStylesheets().add(darkThemeURL);
		}
	}
}
