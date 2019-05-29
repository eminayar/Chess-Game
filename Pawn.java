import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pawn extends Piece{
	
	public Pawn( int x , int y , boolean isBlack){
		super( isBlack );
		this.X=x; this.Y=y;
	}

	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		// TODO Auto-generated method stub
		BufferedImage img=null;
		try {
			if( this.isBlack ) 	img=ImageIO.read(new File("images/BlackPawn.png"));
			else img=ImageIO.read(new File("images/WhitePawn.png"));
		} catch (IOException e){}
		g.drawImage(img, positionX, positionY , null );
		
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		boolean ret=false;
		int tempY=6;
		if( this.isBlack ) tempY=1;
		if( this.Y == tempY ){
			if( isBlack && y == 2 && x == 0 && ChessFrame.pieces[this.X][this.Y+1]==null ) ret=true;
			if( !isBlack && y == -2 && x == 0 && ChessFrame.pieces[this.X][this.Y-1]==null ) ret=true;
		}
		if( isBlack && y == 1 && x == 0)	ret=true;
		if( !isBlack && y == -1 && x == 0)	ret=true;
		return ret;
		
	}

	@Override
	public boolean canCapture(int x, int y) {
		// TODO Auto-generated method stub
		if(isBlack){
			if((x == -1 || x == 1) && y == 1)	return true;
			else	return false;
		}
		else{
			if((x == -1 || x == 1) && y == -1)	return true;
			else	return false;
		}
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if( super.isBlack ) return "black-pawn";
		return "white-pawn";
	}

}
