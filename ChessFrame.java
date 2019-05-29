import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

class Move{
	String from,to,event,details;
	public Move( String a , String b , String c , String d ){
		from=a; to=b; event=c; details=d;
	}
}

public class ChessFrame extends JFrame implements MouseListener{

	public static final int SQUARE_WIDTH = 55;
	public static final int BOARD_MARGIN = 70;
	int selectedSquareX = -1;
	int selectedSquareY = -1;
	boolean turn=false;
	static Piece pieces[][] = new Piece[8][8];
	Stack <Move> st = new Stack <Move>();
	
	public ChessFrame(){
		initializeChessBoard();
		setTitle("Chess Game");
		//let the screen size fit the board size
		setSize(SQUARE_WIDTH*8+BOARD_MARGIN*2, SQUARE_WIDTH*9+BOARD_MARGIN*2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel undoPanel= new JPanel();
		JPanel savePanel= new JPanel();
		JButton undoButton = new JButton("undo");
		JButton saveButton = new JButton("save");
		undoButton.addActionListener( new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  undo();
			  }
			});
		saveButton.addActionListener( new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  try {
					save("game.txt");
				} catch (Exception e1) {}
			  }
			});
		undoPanel.add( undoButton );
		savePanel.add( saveButton );
		this.add( undoPanel );
		this.add( savePanel );
		undoPanel.setBounds( 140 , 535 , 100 , 100 );
		savePanel.setBounds( 340 , 535 , 100 , 100 );
		this.getContentPane().setLayout(null);
		addMouseListener(this);
	}
	
	public void initializeChessBoard(){
		for(int i = 0; i<8; i++)
			for(int j = 0; j<8; j++){	
				if( j == 0 || j == 7 ){
					if( i == 0 || i == 7 ) pieces[i][j] = new Rook( i , j , j == 0 );
					if( i == 1 || i == 6 ) pieces[i][j] = new Knight( i , j , j == 0 );
					if( i == 2 || i == 5 ) pieces[i][j] = new Bishop( i , j , j == 0 );
					if( i == 3 ) pieces[i][j] = new Queen( i , j , j == 0 );
					if( i == 4 ) pieces[i][j] = new King( i , j , j == 0 );
				}
				else if(j == 1 || j == 6 )	pieces[i][j] = new Pawn( i , j , j == 1 );
				else	pieces[i][j] = null;
			}
	}
	
	public boolean castling( boolean isKingSide ){
		int tempY=7;
		String king="white-king";
		String rook="white-rook";
		System.out.println(""+isKingSide);
		if( turn ){
			tempY=0;
			king="black-king";
			rook="black-rook";
		}
		if( pieces[4][tempY] == null || !pieces[4][tempY].getName().equals(king) ) return false;
		if( isKingSide ){
			if( pieces[7][tempY] == null || !pieces[7][tempY].getName().equals(rook) || pieces[5][tempY] != null || pieces[6][tempY] != null ) return false;
			pieces[7][tempY]=pieces[4][tempY]=null;
			pieces[6][tempY]=new King( 6 , tempY , turn );
			pieces[5][tempY]=new Rook( 5 , tempY , turn );
			st.push( new Move( "a1" , "a1" , "castling" , "kingside") );
		}else{
			if( pieces[0][tempY] == null || !pieces[0][tempY].getName().equals(rook) || pieces[1][tempY] != null || pieces[2][tempY] != null || pieces[3][tempY] != null ) return false;
			pieces[0][tempY]=pieces[4][tempY]=null;
			pieces[2][tempY]=new King( 2 , tempY , turn );
			pieces[3][tempY]=new Rook( 3 , tempY , turn );
			st.push( new Move( "a1" , "a1" , "castling" , "queenside") );
		}
		repaint();
		turn=!turn;
		return true;
	}
	
	public boolean isInCheck(){
		String king="white-king";
		if( turn ) king="black-king";
		int curX=0,curY=0;
		for( int i=0 ; i<8 ; i++ )
			for( int j=0 ; j<8 ; j++ ) if( pieces[i][j]!=null &&  pieces[i][j].getName().equals(king) ){ curX=i; curY=j; break; }
		for( int i=0 ; i<8 ; i++ )
			for( int j=0 ; j<8 ; j++ )
				if( pieces[i][j]!=null && pieces[i][j].getColor() == !turn ){
					int diffX=curX-i;
					int diffY=curY-j;
					if( pieces[i][j].canCapture(diffX, diffY) ) return true;
				}
		return false;
	}
	
	public boolean isStalemate(){
		boolean ret=true;
		if( isInCheck() ) return false;
		for( int i=0 ; i<8 ; i++ )
			for( int j=0 ; j<8 ; j++ ) if( pieces[i][j] != null && pieces[i][j].getColor() == turn ){
				for( int x=0 ; x<8 ; x++ )
					for( int y=0 ; y<8 ; y++ ){
						boolean cando=executeMove( ""+(char)('a'+i)+(char)('0'+8-j) , ""+(char)('a'+x)+(char)('0'+8-y) );
						if( cando ) turn=!turn;
						boolean notsafe=isInCheck();
						if( cando ) turn=!turn;
						if( cando ) undo();
						if( !notsafe ) return false;
					}				
			}
		return ret;
	}
	
	public boolean isCheckmate(){
		if( !isInCheck() ) return false;
		for( int i=0 ; i<8 ; i++ )
			for( int j=0 ; j<8 ; j++ ) if( pieces[i][j] != null && pieces[i][j].getColor() == turn ){
				for( int x=0 ; x<8 ; x++ )
					for( int y=0 ; y<8 ; y++ ){
						boolean cando=executeMove( ""+(char)('a'+i)+(char)('0'+8-j) , ""+(char)('a'+x)+(char)('0'+8-y) );
						if( cando ) turn=!turn;
						boolean notsafe=isInCheck();
						if( cando ) turn=!turn;
						if( cando ) undo();
						if( !notsafe ){
							System.out.println(""+(char)('a'+i)+(char)('0'+8-j)+","+(char)('a'+x)+(char)('0'+8-y));
							return false;
						}
					}
			}
		return true;
	} 
	
	public void undo(){
		if( st.isEmpty() ) return;
		System.out.println("in undo");
		turn=!turn;
		Move thisMove=st.pop();
		int prevSquareX=thisMove.from.charAt(0)-'a';
		int prevSquareY=8-(thisMove.from.charAt(1)-'0');
		int nextSquareX=thisMove.to.charAt(0)-'a';
		int nextSquareY=8-(thisMove.to.charAt(1)-'0');
		if( thisMove.event.equals("castling") ){
			if( thisMove.details.equals("kingside") ){
				int tempY=7;
				if( turn ) tempY=0;
				pieces[4][tempY]=new King( 4 , tempY , turn );
				pieces[7][tempY]=new Rook( 7 , tempY , turn );
				pieces[5][tempY]=pieces[6][tempY]=null;
			}
			else{
				int tempY=7;
				if( turn ) tempY=0;
				pieces[4][tempY]=new King( 4 , tempY , turn );
				pieces[0][tempY]=new Rook( 0 , tempY , turn );
				pieces[2][tempY]=pieces[3][tempY]=null;
			}
		}else if( thisMove.event.equals("capture") ){
			pieces[prevSquareX][prevSquareY]=pieces[nextSquareX][nextSquareY];
			pieces[prevSquareX][prevSquareY].X=prevSquareX;
			pieces[prevSquareX][prevSquareY].Y=prevSquareY;
			if( thisMove.details.equals("knight") ) pieces[nextSquareX][nextSquareY]= new Knight( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("bishop") ) pieces[nextSquareX][nextSquareY]= new Bishop( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("queen") ) pieces[nextSquareX][nextSquareY]= new Queen( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("rook") ) pieces[nextSquareX][nextSquareY]= new Rook( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("pawn") ) pieces[nextSquareX][nextSquareY]= new Pawn( nextSquareX , nextSquareY , !turn );
		}else if( thisMove.event.equals("move") ){
			pieces[prevSquareX][prevSquareY]=pieces[nextSquareX][nextSquareY];
			pieces[nextSquareX][nextSquareY]=null;
			pieces[prevSquareX][prevSquareY].X=prevSquareX;
			pieces[prevSquareX][prevSquareY].Y=prevSquareY;
		}else if( thisMove.event.equals("promotion") ){
			pieces[prevSquareX][prevSquareY]= new Pawn( prevSquareX , prevSquareY , turn );
			pieces[nextSquareX][nextSquareY]= null;
		}else if( thisMove.event.equals("capture-promotion") ){
			pieces[prevSquareX][prevSquareY]= new Pawn( prevSquareX , prevSquareY , turn );
			if( thisMove.details.equals("knight") ) pieces[nextSquareX][nextSquareY]= new Knight( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("bishop") ) pieces[nextSquareX][nextSquareY]= new Bishop( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("queen") ) pieces[nextSquareX][nextSquareY]= new Queen( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("rook") ) pieces[nextSquareX][nextSquareY]= new Rook( nextSquareX , nextSquareY , !turn );
			if( thisMove.details.equals("pawn") ) pieces[nextSquareX][nextSquareY]= new Pawn( nextSquareX , nextSquareY , !turn );
		}
		repaint();
	}
	
	public boolean executeMove( String from , String to ){
		selectedSquareX=from.charAt(0)-'a';
		selectedSquareY=8-(from.charAt(1)-'0');
		int targetSquareX=to.charAt(0)-'a';
		int targetSquareY=8-(to.charAt(1)-'0');
		boolean ret=false;
		String capture="";
		//if these are inside the board
		if(selectedSquareX >= 0 && selectedSquareY >= 0 &&
				selectedSquareX < 8 && selectedSquareY < 8 &&
				targetSquareX >= 0 && targetSquareY >= 0 &&
						targetSquareX < 8 && targetSquareY < 8)
		{
			System.out.println("inside");
			if(pieces[selectedSquareX][selectedSquareY] != null && pieces[selectedSquareX][selectedSquareY].getColor() == turn )
			{
				System.out.println("selected");
				int diffX = targetSquareX - selectedSquareX;
				int diffY = targetSquareY - selectedSquareY;
				if(pieces[targetSquareX][targetSquareY] != null ){
					System.out.println("a target");
					if( pieces[targetSquareX][targetSquareY].getColor() != pieces[selectedSquareX][selectedSquareY].getColor() && pieces[selectedSquareX][selectedSquareY].canCapture(diffX, diffY) ){
						System.out.println("can capture");
						capture=pieces[targetSquareX][targetSquareY].getName();
						if( capture.contains("pawn") ) capture="pawn";
						if( capture.contains("rook") ) capture="rook";
						if( capture.contains("bishop") ) capture="bishop";
						if( capture.contains("queen") ) capture="queen";
						if( capture.contains("knight") ) capture="knight";
						pieces[targetSquareX][targetSquareY] = pieces[selectedSquareX][selectedSquareY];
						pieces[selectedSquareX][selectedSquareY] = null;
						turn=!turn;
						ret=true;
					}
				}
				else{
					System.out.println("no target");
					if( pieces[selectedSquareX][selectedSquareY].canMove(diffX, diffY)){
						System.out.println("can move");
						pieces[targetSquareX][targetSquareY] = pieces[selectedSquareX][selectedSquareY];
						pieces[selectedSquareX][selectedSquareY] = null;
						if( !(targetSquareX == selectedSquareX && targetSquareY == selectedSquareY) ){
							turn=!turn;
							ret=true;
						}
					}
				}
				if( pieces[targetSquareX][targetSquareY] != null ){
					pieces[targetSquareX][targetSquareY].X = targetSquareX;
					pieces[targetSquareX][targetSquareY].Y = targetSquareY;
				}
				if( ret ){
					String pawn="white-pawn";
					if( !turn ) pawn="black-pawn";
					System.out.println(pieces[targetSquareX][targetSquareY].getName());
					if( pieces[targetSquareX][targetSquareY].getName().equals(pawn) && ( targetSquareY == 0 || targetSquareY == 7) ){
						pieces[targetSquareX][targetSquareY]=new Queen( targetSquareX , targetSquareY , !turn );
						if( !capture.equals("") )	st.push( new Move( ""+(char)('a'+selectedSquareX)+(char)('0'+8-selectedSquareY) , ""+(char)('a'+targetSquareX)+(char)('0'+8-targetSquareY) , "capture-promotion" , capture ) );
						else	st.push( new Move( ""+(char)('a'+selectedSquareX)+(char)('0'+8-selectedSquareY) , ""+(char)('a'+targetSquareX)+(char)('0'+8-targetSquareY) , "promotion" , "" ) );
					}else{
						if( !capture.equals("") ) st.push( new Move( ""+(char)('a'+selectedSquareX)+(char)('0'+8-selectedSquareY) , ""+(char)('a'+targetSquareX)+(char)('0'+8-targetSquareY) , "capture" , capture ) );
						else st.push( new Move( ""+(char)('a'+selectedSquareX)+(char)('0'+8-selectedSquareY) , ""+(char)('a'+targetSquareX)+(char)('0'+8-targetSquareY) , "move" , "" ) );
					}
				}
			}
		}
		return ret;
	}
	
	public void save( String fileName ){
		File f=new File(fileName);
		PrintStream prnt = null;
		try {
			prnt = new PrintStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("damn");
		}
		if( turn ) prnt.println("black");
		else prnt.println("white");
		for( int i=0 ; i<8 ; i++ )
			for( int j=0 ; j<8 ; j++ ) if( pieces[i][j] != null ){
				prnt.println(at(""+(char)('a'+i)+(char)('0'+8-j))+"-"+(char)('a'+i)+(char)('0'+8-j));
			}
	}
	
	public static ChessFrame load( String fileName ){
		File f=new File(fileName);
		ChessFrame loadFrame = new ChessFrame();
		for( int i=0 ; i<8 ; i++ )
			for( int j=0 ; j<8 ; j++ ) loadFrame.pieces[i][j]=null;
		Scanner read = null;
		try {
			read = new Scanner( f );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("damn");
		}
		String trn=read.next();
		loadFrame.turn=trn.equals("black");
		while( read.hasNext() ){
			String st=read.next();
			int size=st.length();
			String where=st.substring( size-2 );
			boolean type=st.contains("black");
			int x=where.charAt(0)-'a';
			int y=8-(where.charAt(1)-'0');
			if( st.contains("rook") )	loadFrame.pieces[x][y]= new Rook( x , y , type );
			if( st.contains("king") )	loadFrame.pieces[x][y]= new King( x , y , type );
			if( st.contains("queen") )	loadFrame.pieces[x][y]= new Queen( x , y , type );
			if( st.contains("pawn") )	loadFrame.pieces[x][y]= new Pawn( x , y , type );
			if( st.contains("bishop") )	loadFrame.pieces[x][y]= new Bishop( x , y , type );
			if( st.contains("knight") )	loadFrame.pieces[x][y]= new Knight( x , y , type );
		}
		return loadFrame;
	}
	
	public void gameOver( boolean flag ){
		JFrame gameover = new JFrame();
		JButton gameoverButton;
		if( flag ) gameoverButton= new JButton("game over check mate");
		else gameoverButton= new JButton("game over stalemate");
		gameover.setSize(500, 500);
		gameover.add(gameoverButton);
		gameover.setVisible(true);
		gameoverButton.addActionListener( new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  gameover.setVisible(false);
			  }
			});
		
	}
	
	public String at(String pos){
		String ret="";
		int currentSquareX=pos.charAt(0)-'a';
		int currentSquareY=8-(pos.charAt(1)-'0');
		if( pieces[currentSquareX][currentSquareY] != null ) 	ret+=pieces[currentSquareX][currentSquareY].getName();
		return ret;
	}
	
	public boolean move( String from, String to ){
		boolean ret=executeMove( from , to );
		if( !ret && (!turn && ( from.equals("e1") && ( to.equals("h1") || to.equals("a1") ) ) && pieces[4][7].getName().equals("white-king") ) || 
				(turn && ( from.equals("e8") && ( to.equals("h8") || to.equals("a8") ) ) && pieces[4][0].getName().equals("black-king") ) ){
			System.out.println("castling");
			if( to.charAt(0) == 'h' ) ret=castling( true );
			else ret=castling( false );
		}
		if( ret ){
			boolean flag=true;
			turn=!turn;
			if( isInCheck() ){
				flag=false;
				turn=!turn;
				undo();
			}
			if( flag ) turn=!turn;
		}
		System.out.println("repainting");
		if( ret && isCheckmate() )	gameOver( true );
		else if( ret && isStalemate() )   gameOver( false );
		repaint();
		return ret;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		//print the board 's lines to show squares
		for(int i = 0; i<=8; i++)
		{
			if( i < 8 ) g.drawString( ""+(8-i) , BOARD_MARGIN-SQUARE_WIDTH/2 , BOARD_MARGIN+(i)*SQUARE_WIDTH+5*SQUARE_WIDTH/8 );
			if( i < 8 ) g.drawString( ""+(8-i) , BOARD_MARGIN-SQUARE_WIDTH/2+9*SQUARE_WIDTH , BOARD_MARGIN+(i)*SQUARE_WIDTH+5*SQUARE_WIDTH/8 );
			if( i < 8 ) g.drawString( ""+(char)('a'+i) , BOARD_MARGIN+(i)*SQUARE_WIDTH+SQUARE_WIDTH/2 , BOARD_MARGIN-SQUARE_WIDTH/3 );
			if( i < 8 ) g.drawString( ""+(char)('a'+i) , BOARD_MARGIN+(i)*SQUARE_WIDTH+SQUARE_WIDTH/2 , BOARD_MARGIN-SQUARE_WIDTH/3+SQUARE_WIDTH*9 );
		}
		//print the pieces
		for(int i = 0; i<8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				g.drawRect( i*SQUARE_WIDTH+BOARD_MARGIN , j*SQUARE_WIDTH+BOARD_MARGIN , SQUARE_WIDTH , SQUARE_WIDTH);
				if( ((i+j)&1) == 1) g.setColor(Color.getHSBColor( 100 , 200 , 100 ));
				else g.setColor(Color.white);
				g.fillRect(  i*SQUARE_WIDTH+BOARD_MARGIN , j*SQUARE_WIDTH+BOARD_MARGIN , SQUARE_WIDTH , SQUARE_WIDTH );
				if(pieces[i][j] != null)
				{
					pieces[i][j].drawYourself(g, i*SQUARE_WIDTH+BOARD_MARGIN, 
							j*SQUARE_WIDTH+BOARD_MARGIN, SQUARE_WIDTH);
				}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Clicked");
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Pressed");
		//calculate which square is selected 
		selectedSquareX = (e.getX()-BOARD_MARGIN)/SQUARE_WIDTH;
		selectedSquareY = (e.getY()-BOARD_MARGIN)/SQUARE_WIDTH;
		System.out.println(selectedSquareX+","+selectedSquareY);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Released");
		//calculate which square is targeted
		int targetSquareX = (e.getX()-BOARD_MARGIN)/SQUARE_WIDTH;
		int targetSquareY = (e.getY()-BOARD_MARGIN)/SQUARE_WIDTH;
		System.out.println(targetSquareX+","+targetSquareY+"\n");
		move( ""+(char)('a'+selectedSquareX)+(char)('0'+8-selectedSquareY) , ""+(char)('a'+targetSquareX)+(char)('0'+8-targetSquareY) );
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Entered");
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Exited");
		
	}
}
