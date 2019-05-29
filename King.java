import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class King extends Piece{
	
	public King( int x , int y , boolean isBlack ){
		super( isBlack );
		this.X=x; this.Y=y;
	}
	
	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		// TODO Auto-generated method stub
		BufferedImage img=null;
		try {
			if( this.isBlack ) 	img=ImageIO.read(new File("images/BlackKing.png"));
			else img=ImageIO.read(new File("images/WhiteKing.png"));
		} catch (IOException e){}
		g.drawImage(img, positionX, positionY , null );
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		return (Math.abs(x) <= 1 && Math.abs(y) <= 1);
	}

	@Override
	public boolean canCapture(int x, int y) {
		// TODO Auto-generated method stub
		return canMove( x , y );
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if( super.isBlack ) return "black-king";
		return "white-king";
	}

}
