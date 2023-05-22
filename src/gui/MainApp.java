package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application
{
	public static Scene scn;
	
	@Override
	public void start(Stage stg0) throws Exception 
	{
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		scn = new Scene(root);
		Image icon = new Image("icon.png");
		stg0.getIcons().add(icon);
		
		stg0.setTitle("Random DP Team Generator");
		stg0.setScene(scn);
		stg0.setResizable(false);
		scn.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
		stg0.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
