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

	public Piece(char [] c){ /**Constructor*/
		size=c[0];
		colour=c[1];
		shape=c[2];
		volume=c[3];
		}

	//ATTENZIONE -******************* ATTENZIONE**********
	//*****************************
	//*****************************
	//***************************
//#ATTENTION #TODO attenzione, da ok anche se c'è un asterisco, non va sempre bene, se è chiamato da table, asterico deve essere segnato come errore
	static Piece checkAndCreate(String pieceDescription) throws PieceConfigurationException {/**Creates a new piece it is an actual piece.*/
		char[] analyzedPiece=pieceDescription.toCharArray();
		String tmpPiece=""; //contains  letters which compose the piece, after i've checked them 
		int i=0;
		if(analyzedPiece.length==4 || analyzedPiece.length==1)
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
						throw new PieceConfigurationException("An error has occurred. First character should always be A or B.");
					} break;
					
				case 'W':
				case 'N':
					if(i==1)
				{
				tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
				} else{
					//throws exception:  the analyzedPiece has some wrong parameter
					throw new PieceConfigurationException("An error has occurred. Second character should always be W or N.");
				} break;
				
				case 'T':
				case 'Q':
					if(i==2)
					{
					tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
					} else{
						//throws exception:  the analyzedPiece has some wrong parameter
						throw new PieceConfigurationException("An error has occurred. Third character should always be T or Q.");
					} break;
					
				case 'P':
				case 'F':	
					if(i==3)
					{
					tmpPiece=tmpPiece+c;  //i'm building a string which contains the letters, if they're right.
					} else{
						//throws exception:  the analyzedPiece has some wrong parameter
						throw new PieceConfigurationException("An error has occurred. Fourth character should always be P or F.");
					} break;
				default:  throw new PieceConfigurationException("An error has occurred. Second character should always be W or N.");
				}
				i++;
			}
			if(i==4){
				//If the program arrives here it means that the piece is okay. 
		Piece p=new Piece(tmpPiece.toCharArray());
		return p;
			}
		} else{
			throw new PieceConfigurationException("An error has occurred. file is compromised. --->There's a string longer or shorter than 4 chars."); 
		}
		
		return null;
	}


	@Override
	public boolean isEqualTo(Piece p) {  /**Checks if an input-given piece is equal to the current piece.*/
		// TODO Auto-generated method stub
		if( (this.colour==p.colour)&& (this.size==p.size)&& (this.volume==p.volume)&&(this.shape==p.shape) )
		return true;
		else { return false;} 
	}
	
	@Override
	public String toString() {
		return   new StringBuilder().append(size).append(colour).append(shape).append(volume).toString();
	}

	static boolean victory(Piece p0,Piece p1, Piece p2, Piece p3){  /**Checks if 4 input-given pieces have something in common*/
	 
		if( ( (p0.colour==p1.colour)&&(p0.colour==p2.colour) && (p0.colour==p3.colour) )  
				|| ( (p0.size==p1.size)&&(p0.size==p2.size) && (p0.size==p3.size))
				|| ( (p0.volume==p1.volume)&&(p0.volume==p2.volume) && (p0.volume==p3.volume))
				|| ( (p0.shape==p1.shape)&&(p0.shape==p2.shape) && (p0.shape==p3.shape))) 
			return true;
		else			
		return false;
	}
}

