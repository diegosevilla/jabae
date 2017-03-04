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
		lexemes.put("YO!", "6, Program Start");
		lexemes.put("PEACE'OUT!", "7, Program End");
		lexemes.put("Check'dis", "8, If");
		lexemes.put("noh", "9, Else");
		lexemes.put("(", "10, Group Symbol");
		lexemes.put(")", "11, Group Symbol");
		lexemes.put("{", "12, Group Symbol");
		lexemes.put("}", "13, Group Symbol");
		lexemes.put("Pop'till", "14, Loop");
		lexemes.put("NonStop'till", "15, Loop");
		lexemes.put("Do'dis", "16, Loop");
		lexemes.put("Yo'Wait", "17, Switch");
		lexemes.put("How'Bowt", "18, Case");
		lexemes.put("Aight", "19, Default Case");
		lexemes.put(":", "20, Keword");
		lexemes.put("peace", "21, Break");
		lexemes.put("shoot", "22, Continue");
		lexemes.put("Gimme", "23, Input");
		lexemes.put("Sho'me", "24, Output");
		lexemes.put("=", "25, Assignment");
		lexemes.put("digits", "26, Integer");
		lexemes.put("ride", "27, Character");
		lexemes.put("moolah", "28, Float");
		lexemes.put("boogaloh", "29, Boolean");
		lexemes.put("&&", "30, And");
		lexemes.put("||", "31, Or");
		lexemes.put("==", "32, Relational");
		lexemes.put("!=", "33, Relational");
		lexemes.put(">=", "34, Relational");
		lexemes.put("<=", "35, Relational");
		lexemes.put("<", "36, Relational");
		lexemes.put(">", "37, Relational");
		lexemes.put("id", "38, Variable");
		lexemes.put("lit", "39, String Literal");
		lexemes.put("legit", "40, True");
		lexemes.put("tigel", "41, False");
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

	public boolean containsKey(String token){
	    for(String key : lexemes.keySet()){
				if(key.startsWith(token)) return true;
			}
			return false;
	}

	public boolean hasMatch(String token){
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
