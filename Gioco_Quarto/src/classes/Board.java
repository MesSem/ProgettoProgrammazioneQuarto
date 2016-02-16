package classes;

import java.awt.image.ImagingOpException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;

import Exception.BoardConfigurationException;
import Exception.PieceConfigurationException;
import interfaces.I_board;

public class Board implements I_board {
	
public Piece [] [] board= new Piece [4] [4];
/**
 *  Loads placed pieces on the board.
 *  @param path		contains board file's path.
 *  @throws BoardConfigurationException		Gets threw when an error occurs into board file.
 *  @throws	IOException										Gets threw when an error occurs searching or trying to read the board file.
 */
@Override
	public void loadBoard(String path) throws BoardConfigurationException, IOException {
		try
	{
		File boardfile=new File(path); 	//Creates file-reader variables.
		if(boardfile.exists())
		{
				FileReader br=new FileReader(boardfile);  //opens file, reading mode
				BufferedReader boardReader= new BufferedReader(br);
				String app;
				int cont=0;
				while(((app=boardReader.readLine())!= null) && cont<5 ) //while i'm actually reading something and if i've still not read more than 4 lines i put the value of that line in app
				{	String[] row=app.split(" "); 
					//scans each row's cells and checks its actual value, if  it's okay it becomes a piece on the board.
					for(int c=0; c<4;c++)  
					{  
					try{
						Piece p=Piece.checkAndCreate(row[c]);
					}
					catch(PieceConfigurationException e)
					{
						throw new BoardConfigurationException(e.getMessage());
					}
					
					if(p!=null)
						{
								boolean placed=isPlaced(p); 
								if(placed)
								{
									throw new BoardConfigurationException("This piece is already placed!"); 
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
			throw new IOException("File doesn't exist."); //throws exception, file doesn't exist.
		}
	}
	 catch(IOException e){  //Throws exception: input error
		throw new IOException(e.getMessage());
	}
		
	}

	/**
	 * #TODO: ADD ALL CHECKS.  It can create error caused of a new line at the end of the file
	 */
	@Override
	public void saveBoard(String path) throws IOException { /**Saves board's new configuration*/
		FileWriter fw;
		BufferedWriter out;
		
	try{
		fw=new FileWriter(path);
		out= new BufferedWriter(fw);
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(board[i][j]==null)  //If it's null it's a white space.
					out.write("**** ");
				else
				out.write(board[i][j].toString()+" ");
			}
			out.newLine();
		}
		out.flush();
		out.close();
	}
	catch(IOException e){
		try{
		out.flush();
		out.close();
		} catch(Exception ex){
		
		}finally{ throw new IOException(e.getMessage()); }  
			
		
	}
	}
	}

	@Override
	public int gameSituation() { /**This method is called by the main stream. It checks if the game is ended or not.*/
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
	public boolean isFree(int position) {/**checks if a position on the board is empty or not*/
		// TODO Auto-generated method stub
		int [] index= {position, -1};
		convertIndex(index);  //Calls a method which converts an array index in a couple of indexes for our square matrix 
		//It has an array as input because it's passed by reference so we'll have 2 edited variables into the array as output.
		if(board[index[0]] [index[1]]==null)
		return true;
		else
			return false;
	}

	@Override
	public void putPieceAtPosition(Piece piece, int position) {/**Places a piece into a certain position into the board*/
		int [] index= {position, -1};
		convertIndex(index);
		board[index[0]] [index[1]]=piece;
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public Piece removePieceAtPosition(int position) { /**Assigns "null" as value to the input-given board's position*/
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

	private void convertIndex(int [] index){  /**Converts an Array index into a couple of coordinates for a matrix. Uses an array because it's passed by references. */
		int position=index[0];
		int row=0, column=0;
		column=position%4;
		row= (position-column)/4;
		index[0]=row;
		index[1]=column;
		
				}
}
