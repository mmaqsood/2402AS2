package assignment2_2402;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import song_charts_2402.Song;

/*
 * Class validates passed XML and returns result of validation.
 */
public class Validator {
	
	public static boolean validate(File inputFile){
		ArrayStack<String> xmlStack = new ArrayStack();
		String xmlFileTag = "<?xml version = \"1.0\"?>";
		
		BufferedReader inputFileReader;
		
		//current input line
	    String inputLine; 
		try{
			   inputFileReader= new BufferedReader(new FileReader(inputFile));
			    
			   while((inputLine = inputFileReader.readLine()) != null){
				   
				   //Read in xml line
				   inputLine = inputLine.trim();
				   // Make sure the line is not the <xml> start tag and is not a whitespace and is not a one liner (i.e <tag>s</tag>)
				   if (!inputLine.startsWith(xmlFileTag) && inputLine.length() > 0 && !Validator.isOneLineTag(inputLine)){
					   // Make sure there is something on the stack.
					   if (xmlStack.size() > 0){
						   // Validate
						   if(!Validator.isEndTag(inputLine)){
							   xmlStack.push(inputLine);
						   }
						   // This is an end tag, so make sure it matches the parent tag
						   else if (!Validator.isEndTagOfParent(xmlStack.top(), inputLine)){
							   return false;
						   }
						   else {
							   //We've matched a beginning with an end tag, pop it from the stack
							   xmlStack.pop();
						   }
					   }
					   else {
						   xmlStack.push(inputLine);
					   }
				   }
				  
				   
			   } //end while
			   
			   return true;
			   
		}catch (EOFException e) {
            System.out.println("File Read Error: EOF encountered, file may be corrupted.");
        } catch (IOException e) {
            System.out.println("File Read Error: Cannot read from file.");
        }
		
		return false;
	}

	// Determine if this is a "one line xml element", ex. <chord>a</chord>
	// or a self closing element, ex. <chord />
	public static boolean isOneLineTag(String line){
		// Check if the tag is self closing i.e <close />;
		if (line.charAt(line.length()-2) == '/'){
			return true;
		}
		
		String beginningTag = line.substring(line.indexOf("<"), line.indexOf(">"));
		String endingTag = line.substring(line.lastIndexOf("<"), line.lastIndexOf(">"));
		
		return !beginningTag.equals(endingTag);
	}
	
	// Determine if an end tag is the end tag of the parent tag
	// ex. </car> being the end tag for <car>
	public static boolean isEndTagOfParent(String parentTag, String tag){
		//Compares two tags as xmlStartTag> == xmlStartTag> instead of <xmlStartTag> == </xmlStartTag>
		return parentTag.substring(1).equals(tag.substring(2));
	}
	
	// Determine if a tag is an end tag ex. </car>
	public static boolean isEndTag(String tag){
		return tag.charAt(1) == '/';
	}
}
