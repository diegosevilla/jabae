import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexDic
{
	Hashtable<String, String> lexemes = new Hashtable<String, String>();
	Hashtable<String, String> patterns = new Hashtable<String, String>();
	public LexDic()
	{
		lexemes.put("+", "1, Arithmetic");
		lexemes.put("-", "2, Arithmetic");
		lexemes.put("*", "3, Arithmetic");
		lexemes.put("/", "4, Arithmetic");
		lexemes.put("%", "5, Arithmetic");
		lexemes.put("(", "6, Group Symbol");
		lexemes.put(")", "7, Group Symbol");
		lexemes.put("<", "8, Relational");
		lexemes.put(">", "9, Relational");
		lexemes.put("==", "10, Relational");
		lexemes.put("<=", "11, Relational");
		lexemes.put(">=", "12, Relational");
		lexemes.put("!=", "13, Relational");
		lexemes.put("&&", "14, Logical");
		lexemes.put("||", "15, Logical");
		lexemes.put("++", "16, Unary");
		lexemes.put("--", "17, Unary");
		lexemes.put("=", "18, Assignment");
		lexemes.put("Check'dis", "19, Control");
		lexemes.put("noh", "20, Control");
		lexemes.put("peace", "21, Control");
		lexemes.put("shoot", "22, Control");
		lexemes.put("Pop'till", "23, Control");
		lexemes.put("NonStop'till", "24, Control");
		lexemes.put("Do'dis", "25, Control");
		lexemes.put("Yo'Wait", "26, Control");
		lexemes.put("How'Bowt", "27, Control");
		lexemes.put("Aight", "28, Control");
		lexemes.put("Sho'me", "29, Output");
		lexemes.put("Gimme", "30, Input");
		lexemes.put("digits", "31, Data Type");
		lexemes.put("ride", "32, Data Type");
		lexemes.put("moolah", "33, Data Type");
		lexemes.put("boogaloh", "34, Data Type");
		lexemes.put("legit", "35, Boolean Value");
		lexemes.put("tigel", "36, Boolean Value");
		lexemes.put("YO!", "37, Keyword");
		lexemes.put("PEACE'OUT!", "38, Keyword");
		
		patterns.put("[\\\"].+[\\\"]", "39, String Literal");
		patterns.put("[\\\'].[\\\']", "40, Character Literal");
		//Integer literals in an operation without spaces can't be read
		//Float literals can't be read because when the decimal point is read
		//it registers as an integer literal
		patterns.put("[-+]?[0-9]+\\.[0-9]+", "43, Float Literal");
		patterns.put("[-+]?[0-9]+", "42, Integer Literal");
		patterns.put("[a-zA-z][A-Za-z0-9]*", "41, Variable");
	}

	public boolean checkToken(String token, int linenum)
	{	
		String[] details;
		if(lexemes.containsKey(token)){ //check for keywords
			details = lexemes.get(token).split(", ");
			System.out.println("Token tag: " + details[0] + " Lexeme: " + token + " Token type: " + details[1] + " Line_num:" + linenum);
			return true;
		} else { //check for literals or variable identifier
			Iterator<String> patternIte = patterns.keySet().iterator();
			while(patternIte.hasNext()) {
				String currPattern = patternIte.next();
				if(checkMatch(token, currPattern)){
					details = patterns.get(currPattern).split(", ");
					System.out.println("Token tag: " + details[0] + " Lexeme: " + token + " Token type: " + details[1] + " Line_num:" + linenum);
					return true;
				}
			}
		    return false;
		}
	}
	
	public boolean checkLongestMatch(String token, int lineNum)
	{
	    String tempString = "";
        for(int i = 0 ; i < token.length() ; ){
	       do{
	         tempString += token.charAt(i++); //create a temp string for holding temp chars
	         if(i < token.length() && token.charAt(i) =='.')  //for float literals
	            tempString += token.charAt(i++);
	         if(i >= token.length()) break; // if i exceeds the length, process the temporary string formed
	         if(lexemes.containsKey(tempString)){ //check if already a valid lexeme
	             if(!lexemes.containsKey(tempString+token.charAt(i))) //look ahead, if the next character is added, and it is no longer a valid lexeme, break the loop and process the temp string
	                break;
	             else continue;
	         } 
	         if(!checkMatch(tempString + token.charAt(i))) //if the next letter is added, and it no longer matches a literal or valid identifier, break the loop and process the temp string
	           break;
	       }while(true);
	       if(checkToken(tempString, lineNum)) //reset if valid 
		       tempString = "";
        }	    
	    return true;
	}

	public boolean checkMatch(String token){
        Iterator<String> patternIte = patterns.keySet().iterator();
	    while(patternIte.hasNext()) {
		    String currPattern = patternIte.next();
		    if(checkMatch(token, currPattern)){
			    return true;
		    }
	    } 
	    return false        ;	
	}
	
	public boolean checkMatch(String token, String pattern) 
	{
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(token);
		
		return m.matches();
	}
}
