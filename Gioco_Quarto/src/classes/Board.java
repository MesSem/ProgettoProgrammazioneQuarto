package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import interfaces.I_board;

public class Board implements I_board {
	
public Piece [] [] board= new Piece [4] [4];
	@Override
	public void loadBoard(String path) throws PieceConfigurationException {
	try
	{
		//creation of variables to read the file.
		File boardfile=new File(path);
		if(boardfile.exists())
		{
				FileReader br=new FileReader(boardfile);
				BufferedReader boardReader= new BufferedReader(br);
				String app;
				int cont=0;
				//for(int r=0;r<4;r++)
				while(((app=boardReader.readLine())!= null) && cont<5 )
				{//read a whole row, split it , fill an array of strings called 4ex: row. ---> Split: String [] row= app.Split(' ');
					String[] row=app.split(" ");
					for(int c=0; c<4;c++)
					{  // calls an input-checking method 4ex: boolean okay= checkInput( app )
						// if it's okay then i can insert values into the board.
						//puts the values into the board. 4ex: board[r,c]=row[c];
						Piece p=Piece.checkAndCreate(row[c]);
					if(p!=null)
						{
								boolean placed=isPlaced(p); 
								if(placed)
								{
									//throws exception: Piece is already placed
								} else{
									board[cont][c]=p;
								}
							}
				} 
				cont++;
				}
			boardReader.close();
		}
		else{
			//throws exception, file doesn't exist.
		}
	}
	 catch(IOException e){  //Throws exception: input error
		
	}
		
	}

	/**
	 * #TODO: ADD ALL CHECKS.  It can create error caused of a new line at the end of the file
	 */
	@Override
	public void saveBoard(String path) throws IOException {
		FileWriter fw=new FileWriter(path);
		BufferedWriter out=new BufferedWriter(fw);
		
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(board[i][j]==null)
					out.write("* ");
				else
				out.write(board[i][j].toString()+" ");
			}
			out.newLine();
		}
		out.flush();
		out.close();
	}

	@Override
	public int gameSituation() {
		// TODO Auto-generated method stub
		for(int r=0;r<4;r++)//scans each row of the board, checking if there's any victory .
		{
			if((board[r] [0]!=null)&&( board[r] [1]!=null)&& (board[r] [2]!=null)&&( board[r] [3]!=null)) //Checks if there actually are pieces in those positions
				if(Piece.victory(board[r] [0], board[r] [1], board[r] [2], board[r] [3]) )	//Checks if those pieces have something in common
					return 1; //if they all have something in common you win.
		}
		for(int c=0;c<4;c++)//scans each column of the board, checking if there's any victory .
		{
			if((board[0] [c]!=null)&&( board[1] [c]!=null)&& (board[2] [c]!=null)&&( board[3] [c]!=null))  //Checks if there actually are pieces in those positions
				if(Piece.victory(board[0] [c], board[1] [c], board[2] [c], board[3] [c]) )
					return 1; //vittoria
		}
		if((board[0] [0]!=null)&&( board[1] [1]!=null)&& (board[2] [2]!=null)&&( board[3] [3]!=null))  //Checks diagonal
			if(Piece.victory(board[0] [0], board[1] [1], board[2] [2], board[3] [3]) )
				return 1; //vittoria
		if((board[0] [3]!=null)&&( board[1] [2]!=null)&& (board[2] [1]!=null)&&( board[3] [0]!=null))  //Checks diagonal
			if(Piece.victory(board[0] [3], board[1] [2], board[2] [1], board[3] [0]) )
				return 1;
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				if(board[i][j]==null)
					return 1000;
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 16;
	}

	@Override
	public boolean isFree(int position) {
		// TODO Auto-generated method stub
		//checks if a position is null or not
		int [] index= {position, -1};
		convertIndex(index);
		if(board[index[0]] [index[1]]==null)
		return true;
		else
			return false;
	}

	@Override
	public void putPieceAtPosition(Piece piece, int position) {
		int [] index= {position, -1};
		convertIndex(index);
		board[index[0]] [index[1]]=piece;
		// TODO Auto-generated method stub
		//Places a piece into a certain position into the board
		
	}

	@Override
	public Piece removePieceAtPosition(int position) { //Puts null to a certain position in the Board.
		// TODO Auto-generated method stub
		int [] index= {position, -1};
		convertIndex(index);
		board[index[0]] [index[1]]=null;
		
		return null;
	}
	
	public boolean isPlaced(Piece p){
				for(int r=0;r<4;r++)
				{ 
					for(int c=0;c<4;c++)
						{
						if( (board[r][c] != null) && (p.isEqualTo(board[r][c]) ) ) 
							return true;
						}
				}
				return false;
	}

	private void convertIndex(int [] index){  //Converts an Array index into a couple of coordinates for a matrix. Uses an array because it's passed by references. 
		int position=index[0];
		int row=0, column=0;
		column=position%4;
		row= (position-column)/4;
		index[0]=row;
		index[1]=column;
		
				}
}
