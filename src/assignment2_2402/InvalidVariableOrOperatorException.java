package assignment2_2402;

import java.io.IOException;

public class InvalidVariableOrOperatorException extends IOException{
	
	private String message = "ERROR: Invalid Variable or Operator";
	
	public String toString() {return message;}

}
