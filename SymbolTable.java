import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolTable
{
	ArrayList<String> lexemes = new ArrayList<String>();
	Hashtable<String, String> patterns = new Hashtable<String, String>();
	static ArrayList<Hashtable <String, IdEntry>> idTable = new ArrayList<Hashtable<String, IdEntry>>();
	static int blockLevel = 1;
	static int offset = 0;

	public SymbolTable()
	{

		idTable.add(new Hashtable<String,IdEntry>());

		lexemes.add("YO!");
		lexemes.add("PEACEOUT!");
		lexemes.add("Check'dis");
		lexemes.add("noh");
		lexemes.add("Pop'till");
		lexemes.add("NonStop'till");
		lexemes.add("Do'dis");
		lexemes.add("Yo'wait");
		lexemes.add("How'Bowt");
		lexemes.add("Aight");
		lexemes.add("peace");
		lexemes.add("shoot");
		lexemes.add("Gimme");
		lexemes.add("Sho'me");
		lexemes.add("digits");
		lexemes.add("ride");
		lexemes.add("moolah");
		lexemes.add("boogaloh");
		lexemes.add("&&");
		lexemes.add("||");
		lexemes.add("==");
		lexemes.add("!=");
		lexemes.add(">=");
		lexemes.add("<=");
		lexemes.add("<");
		lexemes.add(">");
		lexemes.add("+");
		lexemes.add("-");
		lexemes.add("*");
		lexemes.add("/");
		lexemes.add("%");
		lexemes.add("=");
		lexemes.add(":");
		lexemes.add("{");
		lexemes.add("}");
		lexemes.add("(");
		lexemes.add(")");
		lexemes.add("Identifier");
		lexemes.add("Literal");
		lexemes.add("legit");
		lexemes.add("tigel");
		patterns.put("[\\\"].+[\\\"]", "39, String Literal");
		patterns.put("[\\\'].[\\\']", "40, ride");
		//Integer literals in an operation without spaces can't be read
		//Float literals can't be read because when the decimal point is read
		//it registers as an integer literal
		patterns.put("[-+]?[0-9]+\\.[0-9]+", "43, moolah");
		patterns.put("[-+]?[0-9]+", "42, digits");
		patterns.put("[a-zA-z][A-Za-z0-9]*", "41, Variable");
	}

	public void initResWords()
	{
		Hashtable<String, IdEntry> resWords = new Hashtable<String,IdEntry>();
		for(String key: lexemes){
			IdEntry newEntry = new IdEntry(key, 1);
			resWords.put(key, newEntry);
		}
		idTable.add(resWords);
	}
	
	public static void enterBlock()
	{
		blockLevel++;
		idTable.add(new Hashtable<String,IdEntry>());
	}
	
	public static void leaveBlock()
	{
		idTable.remove(blockLevel);
		blockLevel--;
	}
	
	public static IdEntry idLookup(String name, int blkLevel) //blkLevel optional
	{
		if(blkLevel > 1){
			Hashtable<String, IdEntry> table = idTable.get(blkLevel);
			if(table.containsKey(name)){
				System.out.println("Found " + name + " at blockLevel " + blkLevel);
				return table.get(name);
			} else 
				return null;
		} else {
			for( blkLevel = blockLevel ; blkLevel > 1 ; blkLevel--)
			{
				Hashtable<String, IdEntry> table = idTable.get(blkLevel);
				if(table.containsKey(name)){
					IdEntry entry = table.get(name);
//					System.out.println("Found " + name + " of data type " + entry.dataType + " at blockLevel " + blkLevel);
					return entry;
				} 
			}
			return null;
		}
	}
	
	public static IdEntry install(String name, int blkLevel, String dataType) //blkLevel optional
	{
		if(blkLevel <= 1)
			blkLevel = blockLevel;
		Hashtable<String, IdEntry> table = idTable.get(blkLevel);
		if(table.containsKey(name)){
			return null;
		}
		IdEntry newEntry = new IdEntry(name, dataType, blkLevel);
		int size;
		switch(dataType){
			case "digits": size = 4; break;
			case "moolah": size = 8; break;
			case "ride": size = 4; break;
			default: size = 0;
		}
		newEntry.offset = offset;
		offset += size;
		table = idTable.get(blkLevel);
		table.put(name, newEntry);
		System.out.println("Created " + name + " of type " + dataType + " offset is now " + offset);
		return newEntry;
	}

	public IdEntry checkToken(String token, int linenum)
	{
		Hashtable<String, IdEntry> varTable = idTable.get(1);
		if(varTable.containsKey(token)){ //check for keywords
			IdEntry newEntry = new IdEntry(token, linenum, token);
			return newEntry;
		} else { //check for literals or variable identifier
			Iterator<String> patternIte = patterns.keySet().iterator();
			while(patternIte.hasNext()) {
				String currPattern = patternIte.next();
				if(checkMatch(token, currPattern)){
					String name = patterns.get(currPattern).split(", ")[1].equals("Variable")? "Identifier" : "Literal";
					IdEntry newEntry = new IdEntry(name, linenum, token);
//					System.out.println("Token tag: " + details[0] + " Lexeme: " + token + " Token type: " + details[1] + " Line_num:" + linenum);
					return newEntry;
				}
			}
		  return null;
		}
	}

	public boolean containsKey(String token){
	    for(String key : idTable.get(1).keySet()){
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
	    return false;
	}

	public boolean checkMatch(String token, String pattern)
	{
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(token);

		return m.matches();
	}
}
