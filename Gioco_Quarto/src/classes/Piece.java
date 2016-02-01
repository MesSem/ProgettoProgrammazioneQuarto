package classes;

import interfaces.I_piece;



public class Piece implements I_piece {
	/*private enum Size{ A, B }; //Tall, Small
	private enum Colour{ W, N };// White, Black
	private enum Shape { T, Q }; // Squared, rounded
	private enum Volume { P, F }; //filled, cave
	Size size;
	Colour colour;
	Shape shape;
	Volume volume;*/
	private char size;
	private char colour;
	private char shape;
	private char volume;
	
	
	
	/*public Piece(Size size, Colour colour, Shape shape, Volume volume){
		this.size=size;
		this.colour=colour;
		this.shape=shape;
		this.volume=volume;
			}*/
	
	public Piece(char [] c){
		size=c[0];
		colour=c[1];
		shape=c[2];
		volume=c[3];
		}


	static Piece checkAndCreate(String pieceDescription) {
		char[] analyzedPiece=pieceDescription.toCharArray();
		String tmpPiece=""; //contains  letters which compose the piece, after i've checked them 
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
					tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
					} else{
						//throws exception:  the analyzedPiece has some wrong parameter
					} break;
				case 'W':
				case 'N':
					if(i==1)
				{
				tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
				} else{
					//throws exception:  the analyzedPiece has some wrong parameter
				} break;
				case 'T':
				case 'Q':
					if(i==2)
					{
					tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
					} else{
						//throws exception:  the analyzedPiece has some wrong parameter
					} break;
				case 'P':
				case 'F':	
					if(i==3)
					{
					tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
					} else{
						//throws exception:  the analyzedPiece has some wrong parameter
					} break;
				default: //throws exception: the analyzedPiece has some wrong parameter
				}
				i++;
			}
			if(i==3){
				//If the program arrives here it means that the piece is okay. 
		Piece p=new Piece(tmpPiece.toCharArray());
		return p;
			}
		} else{
			//throws exception: file is compromised. --->There's a string longer than 4 chars. 
		}
		
		return null;
	}
}

