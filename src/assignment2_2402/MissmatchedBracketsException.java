package assignment2_2402;

import java.io.IOException;

public class MissmatchedBracketsException extends IOException{
	
	private String message = "ERROR: Missmatched Brackets";
	
	public String toString() {return message;}

}
