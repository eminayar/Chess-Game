import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Knight extends Piece {
	
	public Knight( int x , int y , boolean isBlack ){
		super( isBlack );
		this.X=x; this.Y=y;
	}
	
	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth){
		// TODO Auto-generated method stub
		BufferedImage img=null;
		try {
			if( this.isBlack ) 	img=ImageIO.read(new File("images/BlackKnight.png"));
			else img=ImageIO.read(new File("images/WhiteKnight.png"));
		} catch (IOException e){}
		g.drawImage(img, positionX, positionY , null );
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		if( Math.abs(x) == 2 && Math.abs(y) == 1 ) return true;
		if( Math.abs(x) == 1 && Math.abs(y) == 2 ) return true;
		return false;
	}

	@Override
	public boolean canCapture(int x, int y) {
		// TODO Auto-generated method stub
		return canMove( x , y );
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if( super.isBlack ) return "black-knight";
		return "white-knight";
	}
}
