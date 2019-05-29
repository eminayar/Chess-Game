import javax.swing.JButton;
import javax.swing.JPanel;
import javafx.scene.*;

public class MainClass {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		ChessFrame theFrame = new ChessFrame();
/*		ChessFrame theFrame = null;
		try {
			theFrame = ChessFrame.load("game.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("couldn't load");
		}
	*/	
		theFrame.setVisible(true);
	}

}
