package classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import interfaces.I_piece;
import interfaces.I_table;

public class Table implements I_table {
	
	ArrayList<Piece> pieceNotUsed;
	Piece pieceToPosition;

	Board board;
	
	public Table(String pathBoardFile, String pathNotUsedPiecesFile) throws PieceConfigurationException, IOException{
		
		//Preparation of free piece
		pieceNotUsed=new ArrayList<>();
		loadNotUsedPieces(pathNotUsedPiecesFile);
		
		//Preparation of the board
		board=new Board();
		board.loadBoard(pathBoardFile);
	}

	/**
	 * #TODO: It must be checked
	 * #TODO: Add check if piece is already in list piecenotused
	 * @param path
	 * @throws PieceConfigurationException
	 * @throws IOException
	 */
	public void loadNotUsedPieces(String path) throws PieceConfigurationException, IOException {
		String line="";
		Piece tmp;
		
		FileReader fr = new FileReader(path);
		
		BufferedReader in = new BufferedReader(fr);
		String firstPiece=in.readLine();
		if(firstPiece!=null )
			pieceToPosition=Piece.checkAndCreate(firstPiece);
		else
			throw new PieceConfigurationException("No piece on file "+ path);
		while((line=in.readLine())!=null){
			tmp=new Piece(line.toCharArray());
			if(! board.isPlaced(tmp))
				pieceNotUsed.add(tmp);
			else
				throw new PieceConfigurationException("This piece " + line+" is already in the board ");
		}
		
		in.close();
	}

	@Override
	public void setEnemyPiece(Piece piece) {
		pieceToPosition=piece;
	}

	@Override
	public void savePieces(String path) {
		//salvare la lista di pedine, ma in prima riga salvare toPosition

	}

	/**
	 * Better than getBoard() but now not work
	 */
	@Override
	public Board getACopyOfTheBoard() {
		
		return null;
	}

	/**
	 * Better than getPieceNotUsed() but now not work
	 */
	@Override
	public ArrayList<Piece> getACopyOfPieceNotUsed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece getPieceToPosition() {
		return pieceToPosition;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public ArrayList<Piece> getPieceNotUsed() {
		return pieceNotUsed;
	}

	@Override
	public void saveBoard(String path) {
		board.saveBoard(path);
		
	}

	@Override
	public void removePieceNotUsedAtPosition(int position) {
		pieceNotUsed.remove(position);
		
	}

	@Override
	public void insertPieceInBoardAtPosition(Piece p,int position) {
		board.putPieceAtPosition(p, position);
		
	}

}
