import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Queen extends Piece {
	
	public Queen( int x , int y , boolean isBlack ){
		super( isBlack );
		this.X=x; this.Y=y;
	}
	
	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		// TODO Auto-generated method stub
		BufferedImage img=null;
		try {
			if( this.isBlack ) 	img=ImageIO.read(new File("images/BlackQueen.png"));
			else img=ImageIO.read(new File("images/WhiteQueen.png"));
		} catch (IOException e){}
		g.drawImage(img, positionX, positionY , null );
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		if( ( Math.abs(x) == Math.abs(y) ) || ( Math.abs(x)+Math.abs(y) != 0 && ( x == 0 || y == 0 ) ) ){
			int tx=x,ty=y;
			if( x != 0 ) tx/=Math.abs( x );
			if( y != 0 ) ty/=Math.abs( y );
			for( int gx=tx,gy=ty ; gx!=x || gy != y ; gx+=tx , gy+=ty )
				if( ChessFrame.pieces[this.X+gx][this.Y+gy] != null ) return false;
			return true;
		}
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
		if( super.isBlack ) return "black-queen";
		return "white-queen";
	}

}
