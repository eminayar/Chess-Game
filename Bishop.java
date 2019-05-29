import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bishop extends Piece {
	
	public Bishop( int x , int y , boolean isBlack ){
		super( isBlack );
		this.X=x; this.Y=y;
	}
	
	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		// TODO Auto-generated method stub
		BufferedImage img=null;
		try {
			if( this.isBlack ) 	img=ImageIO.read(new File("images/BlackBishop.png"));
			else img=ImageIO.read(new File("images/WhiteBishop.png"));
		} catch (IOException e){}
		g.drawImage(img, positionX, positionY , null );
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		if( Math.abs(x) != Math.abs(y) || x == 0 || y == 0 ) return false;
		int tx=x/Math.abs(x),ty=y/Math.abs(y);
		for( int gx=tx,gy=ty ; gx!=x ; gx+=tx , gy+=ty )
			if( ChessFrame.pieces[this.X+gx][this.Y+gy] != null ) return false;
		return true;
	}

	@Override
	public boolean canCapture(int x, int y) {
		// TODO Auto-generated method stub
		return canMove( x , y );
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if( super.isBlack ) return "black-bishop";
		return "white-bishop";
	}

}
