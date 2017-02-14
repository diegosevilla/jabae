import java.util.Hashtable;

public class LexDic
{
	Hashtable<String, String> lexemes = new Hashtable<String, String>();
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
		lexemes.put("a", "40, Variable");
		lexemes.put("b", "40, Variable");
	}

	public boolean printDetails(String token, int linenum)
	{
		if(lexemes.containsKey(token)){
			String[] details = lexemes.get(token).split(", ");
			System.out.println("Token tag= " + details[0] + " Lexeme= " + token + " Token type= " + details[1] + " line num:" + linenum);
			return true;
		} else {
			/*System.out.println(
				"Token tag= 39" + 
				" Lexeme= " + token +
				" Token type= String Literal" +
				" line num:" + linenum
			);*/
		      return false;
		}
	}
}
