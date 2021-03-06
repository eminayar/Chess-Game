import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Rook extends Piece{
	
	public Rook( int x , int y , boolean isBlack ){
		super( isBlack );
		this.X=x; this.Y=y;
	}
	
	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		// TODO Auto-generated method stub
		BufferedImage img=null;
		try {
			if( this.isBlack ) 	img=ImageIO.read(new File("images/BlackRook.png"));
			else img=ImageIO.read(new File("images/WhiteRook.png"));
		} catch (IOException e){}
		g.drawImage(img, positionX, positionY , null );
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		if( (x !=0 && y!=0) || ( x == 0 && y == 0 ) ) return false;
		if( x != 0 ){
			int tx=x/Math.abs(x);
			for( int gx=tx ; gx!=x ; gx+=tx )
				if( ChessFrame.pieces[this.X+gx][this.Y] != null ) return false;
		}else{
			int ty=y/Math.abs(y);
			for( int gy=ty ; gy!=y ; gy+=ty )
				if( ChessFrame.pieces[this.X][this.Y+gy] != null ) return false;
		}
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
		if( super.isBlack ) return "black-rook";
		return "white-rook";
	}

}
