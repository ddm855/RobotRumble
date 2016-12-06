package Model;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Random;
import java.util.Stack;

import com.google.gson.*;

public class Interpreter {

	private Robot robot;
	private Stack<String> stack;
	private JsonObject parsedJson; 
	private HashMap<String, String> basicWords;
	private HashMap<String, String> userWords;
	private HashMap<String, String> variables;
	
	/**
	 * Constructor for interpreter.
	 * @param robot The robot the interpreter is tied to.
	 */
	public Interpreter(Robot robot) {
		this.robot = robot;
		this.stack = new Stack<String>();
		basicWords = new HashMap<String, String>();
		userWords = new HashMap<String, String>();
		variables = new HashMap<String, String>();
		
		
		//load basic words
		
		
		//load words from code
		try {
		parsedJson = (new JsonParser().parse(new FileReader(robot.getPath()))).getAsJsonObject();
    	} catch (JsonParseException e) {
    		e.printStackTrace();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Getter for value in JSON for robot string information.
	 * @param key Key for the value to be returned.
	 * @return String value associated with Key.
	 */
	public String getStringValue(String key){
		
		return parsedJson.getAsJsonObject(key).getAsString();
	}
	
	/**
	 * Getter for value in JSON for robot integer information.
	 * @param key Key for the value to be returned.
	 * @return Integer value associated with Key.
	 */
	public int getIntValue(String key){
		
		return parsedJson.getAsJsonObject(key).getAsInt();
	}
	
	/**
	 * Parses code to set-up dictionaries. 
	 */
	public void parseCode(){
		
		JsonArray codeArr;
		
		userWords.clear();
		variables.clear();
		stack.clear();

		codeArr = parsedJson.getAsJsonObject("code").getAsJsonArray();
		
		boolean inComment = false;
		boolean inDefinition = false;
		String functionBody = "";
		String functionName = "";
		
	    for (int i = 0; i < codeArr.size(); i++ ){
	    	String curLine = codeArr.get(i).getAsString(); //get first line
	    	
	    	String[] lineWords = curLine.split("\\s+");
	    	stack.addAll(Arrays.asList(lineWords));
	    }
	    	
    	while(!(stack.isEmpty())){
    		
    		String curWord = stack.pop();
    		
    		if (inComment){//if flagged as currently in forth comment
    			if (curWord.equals(")")){//checks to see if at end of comment
    				inComment = false;
	    		}
    			continue;
    		}
    		if (inDefinition){//if flagged as currently in forth comment
				if (curWord.equals(";")){//checks to see if at end of definition
					inDefinition = false;
					userWords.put(functionName, functionBody);
	    		}
				else{
					functionBody.concat(" ");
					functionBody.concat("curWord");
				}
    			continue;
    		}
    		
    		
    		switch (curWord){
    		case "(": //if start of forth comment is detected
	    		inComment = true;
    		case "variable":
    			variables.put(stack.pop(), "0");
    			stack.pop();
    		case ":":
    			inDefinition = true;
    			functionName = curWord;
    			functionBody = "";
    		}
	    }
	}
	
	/**
	 * Runs the word passed in. I
	 * @param word
	 */
	public void runWord(String word){
		
		if (!(userWords.containsKey(word))){
			
			throw new IllegalArgumentException("runWord: Argument is not a defined word!");
		}
		
		String body = userWords.get(word);
		ListIterator<String> wordsQ = Arrays.asList(body.split("\\s+")).listIterator();
		
		while(wordsQ.hasNext()){
			
			String currWord = wordsQ.next();
			
			if(userWords.containsKey(currWord)){
				
				// runs function code
			    
			}
			if (basicWords.containsKey(currWord)){
				
				//performs defined operation.
			}
		}
	}
    
    
    
    
    void setVariable(String name, String Value){
		
		
	}
	
	String getVariable(String name){
		
		return null;
	}
	
	
	//ARITHMETIC FUNCTIONS

	/**
	 * Subtracts the first int on stack from the second int on the stack, 
	 * and pushes result back onto the stack.
	 */
	void subtract(){
		
		String temp1 = stack.pop();
		String temp2 = stack.pop();		
		stack.push(Integer.toString(Integer.parseInt(temp2)-Integer.parseInt(temp1)));
	}
	
	/**
	 * Adds the first int on stack to the second int on the stack, and pushes
	 * result back onto the stack.
	 */
	void add(){
		
		String temp1 = stack.pop();
		String temp2 = stack.pop();		
		stack.push(Integer.toString(Integer.parseInt(temp2)+Integer.parseInt(temp1)));
	}
    
	/**
	 * Multiplies the first int on stack to the second int on the stack, and 
	 * pushes result back onto the stack.
	 */
	void multiply(){
		
		String temp1 = stack.pop();
		String temp2 = stack.pop();
		stack.push(Integer.toString(Integer.parseInt(temp1)*Integer.parseInt(temp2)));
	}
    
	/**
	 * Divides the first int on stack to the second int on the stack, and pushes 
	 * the quotient and the remainder back onto the stack (in that order).
	 */
	void divide(){
		
		String temp1 = stack.pop();
		String temp2 = stack.pop();
		stack.push(Integer.toString(Integer.parseInt(temp2)%Integer.parseInt(temp1)));
		stack.push(Integer.toString(Integer.parseInt(temp2)/Integer.parseInt(temp1)));
	}
    
    
	//LOGIC FUNCTIONS
    
    void ifCond(ListIterator<String> code){
    	
    	if (stack.pop().compareTo("equal") == 0){
    		
    		return;
    	}
    	else if (stack.pop().compareTo("unequal") == 0){
    		
    		goTo(code, "else");
    	}
    	else{
    		//Invalid value
    	}
    }
    
    
    void elseCond(ListIterator<String> code){
    	
    	goTo(code, "then");
    }
    
    
    
    void nothing(){
    	
    	return;
    }
    
    
    void comparisonFunc(){
    	
    	// if true, pushes "equal" to stack, else pushes "unequal"
    }
    
    
    
    
    
    
    
    //UTILITY FUNCTIONS
    /**
     * Goes to target from current position
     * @param list ListIterator to seek through.
     * @param target Target String.
     */
    void goTo(ListIterator<String> code, String target){
    	
    	int nestLevel = 0;
    	String currWord;
    	while(((currWord = code.next()).compareTo(target) != 0) && nestLevel == 0){
    		
    		if ((currWord.compareTo("if") == 0) || (currWord.compareTo("do") == 0) || (currWord.compareTo("begin") == 0)){
    			nestLevel++;
    		}
    		else if ((currWord.compareTo("then") == 0) || (currWord.compareTo("loop") == 0) || (currWord.compareTo("until") == 0)){
    			nestLevel--;
    		}
    	};
    }
    
    /**
     * Pops, and prints string at top of stack.
     */
    void popPrint(){
    	
    	System.out.println(stack.pop());
    }
    
    /**
     * Generates random number from 0 to i, where i is a popped int from stack.
     */
    void random(){
    	
    	int i = (new Random()).nextInt(Integer.parseInt(stack.pop()));
    	stack.push(Integer.toString(i));
    }
    
    

    //MAIN
    
    
	public static void main(String[] args) {
        Color test = Color.red;
        System.out.println(test.red);

    }
}
