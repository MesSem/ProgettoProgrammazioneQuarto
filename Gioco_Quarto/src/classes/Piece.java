package classes;

import interfaces.I_piece;



public class Piece implements I_piece {
	private enum Size{ A, B }; //Tall, Small
	private enum Colour{ W, N };// White, Black
	private enum Shape { T, Q }; // Squared, rounded
	private enum Volume { P, F }; //filled, cave
	Size size;
	Colour colour;
	Shape shape;
	Volume volume;
	
	
	/*public Piece(Size size, Colour colour, Shape shape, Volume volume){
		this.size=size;
		this.colour=colour;
		this.shape=shape;
		this.volume=volume;
			}*/
	
	public Piece(char [] c){
		this.size=size.valueOf("");
		this.colour=colour;
		this.shape=shape;
		this.volume=volume;
			}


	static Piece checkAndCreate(String pieceDescription) {
		char[] analyzedPiece=pieceDescription.toCharArray();
		int i=0;
		if(analyzedPiece.length<4)
		{		
			for (char c : analyzedPiece) {
				switch(c){
				case '*': return null ;
				case 'A':
				case 'B': 
					if(i==0)
					{
						
					}
				case 'W':
				case 'N':
				case 'T':
				case 'Q':
				case 'P':
				case 'F':	Piece p=new Piece(analyzedPiece); return p;
				}
				i++;
			}
		} else{
			//throws exception: file is compromised.
		}
			
		return null;
	}
}

