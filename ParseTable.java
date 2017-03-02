import java.util.Hashtable;

public class ParseTable
{
	Hashtable<String, Integer> Row = new Hashtable<String, Integer>();
	Hashtable<String, Integer> Col = new Hashtable<String, Integer>();
	String[][] Table = new String[25][42];
	
	//values with 0 not valid
	public ParseTable()
	{
		//row names
		Row.put("Program", 1);
		Row.put("Statement", 2);
		Row.put("If-block", 3);
		Row.put("Else-block", 4);
		Row.put("Else-block'", 5);
		Row.put("Loop-block", 6);
		Row.put("Switch-block", 7);
		Row.put("Case-block", 8);
		Row.put("Cas-block'", 9);
		Row.put("Control-block", 10);
		Row.put("Input", 11);
		Row.put("Output", 12);
		Row.put("Assignment", 13);
		Row.put("Assignment'", 14);
		Row.put("Declaration", 15);
		Row.put("Condition", 16);
		Row.put("Condition'", 17);
		Row.put("Condition''", 18);
		Row.put("Sym", 19);
		Row.put("Expr", 20);
		Row.put("expr'", 21);
		Row.put("Term", 22);
		Row.put("Term'", 23);
		Row.put("Factor", 24);
		
		//column names
		Col.put("+", 1);
		Col.put("-", 2);
		Col.put("*", 3);
		Col.put("/", 4);
		Col.put("%", 5);
		Col.put("YO!", 6);
		Col.put("PEACEOUT!", 7);
		Col.put("Check'dis", 8);
		Col.put("noh", 9);
		Col.put("(", 10);
		Col.put(")", 11);
		Col.put("{", 12);
		Col.put("}", 13);
		Col.put("Pop'till", 14);
		Col.put("NonStop'till", 15);
		Col.put("Do'dis", 16);
		Col.put("Yo'wait", 17);
		Col.put("How'bowt", 18);
		Col.put("Aight", 19);
		Col.put(":", 20);
		Col.put("peace", 21);
		Col.put("shoot", 22);
		Col.put("Gimme", 23);
		Col.put("Sho'me", 24);
		Col.put("=", 25);
		Col.put("digits", 26);
		Col.put("ride", 27);
		Col.put("moolah", 28);
		Col.put("boogaloh", 29);
		Col.put("&&", 30);
		Col.put("||", 31);
		Col.put("==", 32);
		Col.put("!=", 33);
		Col.put(">=", 34);
		Col.put("<=", 35);
		Col.put("<", 36);
		Col.put(">", 37);
		Col.put("id", 38);
		Col.put("lit", 39);
		Col.put("legit", 40);
		Col.put("tigel", 41);
		Col.put("$", 42);
		
		//parse table
		Table[0][0] = "";
	}
}
