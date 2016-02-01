package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
				{//read a whole row, split it , fill an array of strings called 4ex: row. ---> Split: String [] app= row.Split(' ');
					String[] row=app.split(" ");
					for(int c=0; c<4;c++)
					{  // calls an input-checking method 4ex: boolean okay= checkInput( app )
						// if it's okay then i can insert values into the board.
						//puts the values into the board. 4ex: board[r,c]=app[c];
						Piece p=Piece.checkAndCreate(row[c]);
						boolean placed=isPlaced(p); 
						if(placed)
						{
							//throws exception: Piece is already placed
						} else{
							board[cont][c]=p;
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
	 catch(IOException e){ // ***Commento da cancellare*** ora dà errore ma magari poi creando le variabili di I/O non lo dà più.
		
	}
		
	}

	@Override
	public void saveBoard(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public int gameSituation() {
		// TODO Auto-generated method stub
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
		return false;
	}

	@Override
	public void putPieceAtPosition(Piece piece, int position) {
		// TODO Auto-generated method stub
		//Places a piece into a certain position into the board
		
	}

	@Override
	public Piece removePieceAtPosition(int position) { 
		// TODO Auto-generated method stub
		//Puts null to a certain position in the Board.
		return null;
	}
	
	public  boolean isPlaced(Piece p){
		//scorro la scacchiera
		//If( (board[r][c] != null) && (p.isEqualTo(board[r][c]) ) { return true;}
	
		return false;
		
	}

}
