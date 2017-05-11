import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTable
{
	Hashtable<String, Integer> Row = new Hashtable<String, Integer>();
	Hashtable<String, Integer> Col = new Hashtable<String, Integer>();
	ASTNode ast;
	String[][] Table = new String[26][45];
	String[][] pattern = new String[][]{
								{"[\\\'].[\\\']", "ride"}, 
								{"[-+]?[0-9]+\\.[0-9]+", "moolah"}, 
								{"[-+]?[0-9]+", "digits"}
							 };
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
		Row.put("Case-block'", 9);
		Row.put("Control-block", 10);
		Row.put("Input", 11);
		Row.put("Output", 12);
		Row.put("Assignment", 13);
		Row.put("Assignment'", 14);
		Row.put("Declaration", 15);
		Row.put("Array", 16);
		Row.put("Condition", 17);
		Row.put("Condition'", 18);
		Row.put("Condition''", 19);
		Row.put("Sym", 20);
		Row.put("Expr", 21);
		Row.put("Expr'", 22);
		Row.put("Term", 23);
		Row.put("Term'", 24);
		Row.put("Factor", 25);

		//column names
		Col.put("YO!", 1);
		Col.put("PEACEOUT!", 2);
		Col.put("Check'dis", 3);
		Col.put("noh", 4);
		Col.put("Pop'till", 5);
		Col.put("NonStop'till", 6);
		Col.put("Do'dis", 7);
		Col.put("Yo'wait", 8);
		Col.put("How'Bowt", 9);
		Col.put("Aight", 10);
		Col.put("peace", 11);
		Col.put("shoot", 12);
		Col.put("Gimme", 13);
		Col.put("Sho'me", 14);
		Col.put("digits", 15);
		Col.put("ride", 16);
		Col.put("moolah", 17);
		Col.put("boogaloh", 18);
		Col.put("&&", 19);
		Col.put("||", 20);
		Col.put("==", 21);
		Col.put("!=", 22);
		Col.put(">=", 23);
		Col.put("<=", 24);
		Col.put("<", 25);
		Col.put(">", 26);
		Col.put("+", 27);
		Col.put("-", 28);
		Col.put("*", 29);
		Col.put("/", 30);
		Col.put("%", 31);
		Col.put("=", 32);
		Col.put(":", 33);
		Col.put("{", 34);
		Col.put("}", 35);
		Col.put("(", 36);
		Col.put(")", 37);
		Col.put("[", 38);
		Col.put("]", 39);
		Col.put("Identifier", 40);
		Col.put("Literal", 41);
		Col.put("legit", 42);
		Col.put("tigel", 43);
		Col.put("$", 44);

		//parse table
		//all cells nulled are error cells
		for(int i = 0; i < Table.length; i++)
			for(int j = 0; j < Table[i].length; j++)
				Table[i][j] = null;

		//overwrite cells that are not error cells
		//all production rules reversed for stack
		//Program
		Table[Row.get("Program")][Col.get("YO!")] = "PEACEOUT! Statement YO!";
		
		//Statement
		Table[Row.get("Statement")][Col.get("Check'dis")] = "Statement If-block";
		Table[Row.get("Statement")][Col.get("Pop'till")] = "Statement Loop-block";
		Table[Row.get("Statement")][Col.get("NonStop'till")] = "Statement Loop-block";
		Table[Row.get("Statement")][Col.get("Do'dis")] = "Statement Loop-block";
		Table[Row.get("Statement")][Col.get("Yo'wait")] = "Statement Switch-block";
		Table[Row.get("Statement")][Col.get("Gimme")] = "Statement Input";
		Table[Row.get("Statement")][Col.get("Sho'me")] = "Statement Output";
		Table[Row.get("Statement")][Col.get("digits")] = "Statement Declaration";
		Table[Row.get("Statement")][Col.get("ride")] = "Statement Declaration";
		Table[Row.get("Statement")][Col.get("moolah")] = "Statement Declaration";
		Table[Row.get("Statement")][Col.get("boogaloh")] = "Statement Declaration";
		Table[Row.get("Statement")][Col.get("Identifier")] = "Statement Assignment";
		Table[Row.get("Statement")][Col.get("PEACEOUT!")] = "Epsilon";
		Table[Row.get("Statement")][Col.get("}")] = "Epsilon";
		Table[Row.get("Statement")][Col.get("peace")] = "Epsilon";
		Table[Row.get("Statement")][Col.get("shoot")] = "Epsilon";
		
		//If-block
		Table[Row.get("If-block")][Col.get("Check'dis")] = "Else-block } Statement { ) Condition ( Check'dis";
		
		//Else-block
		Table[Row.get("Else-block")][Col.get("noh")] = "Else-block' noh";
		Table[Row.get("Else-block")][Col.get("PEACEOUT!")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("}")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("peace")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("shoot")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Check'dis")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Pop'till")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("NonStop'till")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Do'dis")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Yo'wait")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Gimme")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Sho'me")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("digits")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("ride")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("moolah")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("boogaloh")] = "Epsilon";
		Table[Row.get("Else-block")][Col.get("Identifier")] = "Epsilon";
		
		//Else-block'
		Table[Row.get("Else-block'")][Col.get("{")] = "} Statement {";
		
		//Loop-block
		Table[Row.get("Loop-block")][Col.get("Pop'till")] = "} Control-block Statement { ) Condition ( Pop'till";
		Table[Row.get("Loop-block")][Col.get("NonStop'till")] = "} Control-block Statement { ) Condition ( NonStop'till";
		Table[Row.get("Loop-block")][Col.get("Do'dis")] = ") Condition ( NonStop'till } Control-block Statement { Do'dis";
		
		//Switch-block
		Table[Row.get("Switch-block")][Col.get("Yo'wait")] = "Epsilon";
		
		//Case-block
		Table[Row.get("Case-block")][Col.get("How'Bowt")] = "Case-block' Control-block Statement : Expr How'Bowt";
		Table[Row.get("Case-block")][Col.get("Aight")] = "Control-block Statement : Aight";
		
		//Case-block'
		Table[Row.get("Case-block'")][Col.get("How'Bowt")] = "Case-block";
		Table[Row.get("Case-block'")][Col.get("Aight")] = "Case-block";
		Table[Row.get("Case-block'")][Col.get("}")] = "Epsilon";

		//Control-block
		Table[Row.get("Control-block")][Col.get("peace")] = "peace";
		Table[Row.get("Control-block")][Col.get("shoot")] = "shoot";
		Table[Row.get("Control-block")][Col.get("}")] = "Epsilon";
		Table[Row.get("Control-block")][Col.get("How'Bowt")] = "Epsilon";
		Table[Row.get("Control-block")][Col.get("Aight")] = "Epsilon";
		
		//Input
		Table[Row.get("Input")][Col.get("Gimme")] = "Identifier Gimme";
		
		//Output
		Table[Row.get("Output")][Col.get("Sho'me")] = "Expr Sho'me";
		
		//Assignment
		Table[Row.get("Assignment")][Col.get("Identifier")] = "Expr = Array Identifier";
			
		//Assignment'
		Table[Row.get("Assignment'")][Col.get("=")] = "Expr =";
		Table[Row.get("Assignment'")][Col.get("}")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Check'dis")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Pop'till")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("NonStop'till")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Do'dis")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Yo'wait")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Gimme")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Sho'me")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("digits")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("ride")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("moolah")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("boogaloh")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("Identifier")] = "Epsilon";
		Table[Row.get("Assignment'")][Col.get("PEACEOUT!")] = "Epsilon";
		
		//Declaration
		Table[Row.get("Declaration")][Col.get("digits")] = "Assignment' Array Identifier digits";
		Table[Row.get("Declaration")][Col.get("ride")] = "Assignment' Array Identifier ride";
		Table[Row.get("Declaration")][Col.get("moolah")] = "Assignment' Array Identifier moolah";
		Table[Row.get("Declaration")][Col.get("boogaloh")] = "Assignment' Array Identifier boogaloh";
		
		//Array
		Table[Row.get("Array")][Col.get("[")] = "] Expr [";
		Table[Row.get("Array")][Col.get("=")] = "Epsilon";
		Table[Row.get("Array")][Col.get("}")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Check'dis")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Pop'till")] = "Epsilon";
		Table[Row.get("Array")][Col.get("NonStop'till")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Do'dis")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Yo'wait")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Gimme")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Sho'me")] = "Epsilon";
		Table[Row.get("Array")][Col.get("digits")] = "Epsilon";
		Table[Row.get("Array")][Col.get("ride")] = "Epsilon";
		Table[Row.get("Array")][Col.get("moolah")] = "Epsilon";
		Table[Row.get("Array")][Col.get("boogaloh")] = "Epsilon";
		Table[Row.get("Array")][Col.get("Identifier")] = "Epsilon";
		Table[Row.get("Array")][Col.get("PEACEOUT!")] = "Epsilon";
		
		//Condition
		Table[Row.get("Condition")][Col.get("(")] = "Condition' Condition''";
		Table[Row.get("Condition")][Col.get("Identifier")] = "Condition' Condition''";
		Table[Row.get("Condition")][Col.get("Literal")] = "Condition' Condition''";
		Table[Row.get("Condition")][Col.get("legit")] = "Condition' Condition''";
		Table[Row.get("Condition")][Col.get("tigel")] = "Condition' Condition''";

		//Condition'
		Table[Row.get("Condition'")][Col.get("&&")] = "Condition &&";
		Table[Row.get("Condition'")][Col.get("||")] = "Condition ||";
		Table[Row.get("Condition'")][Col.get(")")] = "Epsilon";
		
		//Condition''
		Table[Row.get("Condition''")][Col.get("(")] = "Sym Expr";
		Table[Row.get("Condition''")][Col.get("Identifier")] = "Sym Expr";
		Table[Row.get("Condition''")][Col.get("Literal")] = "Sym Expr";
		Table[Row.get("Condition''")][Col.get("legit")] = "Sym Expr";
		Table[Row.get("Condition''")][Col.get("tigel")] = "Sym Expr";
		
		//Sym
		Table[Row.get("Sym")][Col.get("==")] = "Expr ==";
		Table[Row.get("Sym")][Col.get("!=")] = "Expr !=";
		Table[Row.get("Sym")][Col.get(">=")] = "Expr >=";
		Table[Row.get("Sym")][Col.get("<=")] = "Expr <=";
		Table[Row.get("Sym")][Col.get("<")] = "Expr <";
		Table[Row.get("Sym")][Col.get(">")] = "Expr >";
		
		//Expr
		Table[Row.get("Expr")][Col.get("(")] = "Expr' Term";
		Table[Row.get("Expr")][Col.get("Identifier")] = "Expr' Term";
		Table[Row.get("Expr")][Col.get("Literal")] = "Expr' Term";
		Table[Row.get("Expr")][Col.get("legit")] = "Expr' Term";
		Table[Row.get("Expr")][Col.get("tigel")] = "Expr' Term";
		
		//Expr'
		Table[Row.get("Expr'")][Col.get("+")] = "Expr' Term +";
		Table[Row.get("Expr'")][Col.get("-")] = "Expr' Term -";
		Table[Row.get("Expr'")][Col.get("}")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get(")")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get(":")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("==")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("!=")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get(">=")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("<=")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("<")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get(">")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("]")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("&&")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("||")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Check'dis")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Pop'till")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("NonStop'till")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Do'dis")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Yo'wait")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Gimme")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Sho'me")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("digits")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("ride")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("moolah")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("boogaloh")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("Identifier")] = "Epsilon";
		Table[Row.get("Expr'")][Col.get("PEACEOUT!")] = "Epsilon";
		
		//Term
		Table[Row.get("Term")][Col.get("(")] = "Term' Factor";
		Table[Row.get("Term")][Col.get("Identifier")] = "Term' Factor";
		Table[Row.get("Term")][Col.get("Literal")] = "Term' Factor";
		Table[Row.get("Term")][Col.get("legit")] = "Term' Factor";
		Table[Row.get("Term")][Col.get("tigel")] = "Term' Factor";
		
		//Term'
		Table[Row.get("Term'")][Col.get("*")] = "Term' Factor *";
		Table[Row.get("Term'")][Col.get("/")] = "Term' Factor /";
		Table[Row.get("Term'")][Col.get("%")] = "Term' Factor %";
		Table[Row.get("Term'")][Col.get("}")] = "Epsilon";
		Table[Row.get("Term'")][Col.get(")")] = "Epsilon";
		Table[Row.get("Term'")][Col.get(":")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("==")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("!=")] = "Epsilon";
		Table[Row.get("Term'")][Col.get(">=")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("<=")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("<")] = "Epsilon";
		Table[Row.get("Term'")][Col.get(">")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("]")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("&&")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("||")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Check'dis")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Pop'till")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("NonStop'till")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Do'dis")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Yo'wait")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Gimme")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Sho'me")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("digits")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("ride")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("moolah")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("boogaloh")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("Identifier")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("+")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("-")] = "Epsilon";
		Table[Row.get("Term'")][Col.get("PEACEOUT!")] = "Epsilon";
		
		//Factor
		Table[Row.get("Factor")][Col.get("(")] = ") Expr (";
		Table[Row.get("Factor")][Col.get("Identifier")] = "Identifier";
		Table[Row.get("Factor")][Col.get("Literal")] = "Literal";
		Table[Row.get("Factor")][Col.get("legit")] = "legit";
		Table[Row.get("Factor")][Col.get("tigel")] = "tigel";
		
		
	}

	//recognizer
	public boolean Valid(ArrayList<IdEntry> tokens)
	{
        Deque<String> stack = new ArrayDeque<String>();
        Deque<String> opStack = new ArrayDeque<String>();
        HashMap<String, ASTNode> nodes = new HashMap<String, ASTNode>();
        ArrayList<String> operations = new ArrayList<String>() {{
			add("&&");
			add("||");
			add("==");add("!=");add(">=");add("<=");add("<");add(">");
			add("+");add("-");
			add("*");add("/");add("%");
    		add("(");add(")");
        }};
        ArrayList<IdEntry> infix = new ArrayList<IdEntry>();
        stack.push("$");
        stack.push("Program");
        String cell;
        String temp;
        String popd;
        String[] prodrule;
		int i, eCount = 0, tCount = 0;
		String declaration = null;
		Boolean parsingOperations = false, parsingConditions = false, doWhile = false;

		for(i = 0; !stack.peek().equals("$");)
		{
			while(stack.peek().equals("Epsilon")){
				stack.pop();
//				System.out.println(stack.peek());
			}
			System.out.println(stack.peek()+"4");
			System.out.println("Token: " + tokens.get(i).token);
			if(Col.containsKey(stack.peek()))//top is terminal
			{
				if(stack.peek().equals(tokens.get(i).name))
				{
					System.out.println("pop: " + stack.peek() + " token: " + tokens.get(i).name);
					String popped = stack.pop();
					if(popped.equals("YO!")){
						SymbolTable.enterBlock();
						//Starts the creation of AST
						System.out.println("Pushing 'program'");
						opStack.push("program");
						nodes.put(opStack.peek(), new ASTNode(opStack.peek(), opStack.peek()));
					}
					if(popped.equals("PEACEOUT!")) {
						//Pops the top node and uses it as the AST
						System.out.println("Operator: " + opStack.peek());
						if(Error.errors.size() == 0) {
							ast = nodes.get(opStack.peek());
							nodes.remove(opStack.pop());
						} else System.out.println("Error!");
					}
					if(popped.equals("{")){
						SymbolTable.enterBlock();
					}
					if(popped.equals("}")){
						SymbolTable.leaveBlock();
						System.out.println("TOS OP: " + opStack.peek());
						//Add more when grammar ends in this
						switch(opStack.peek()) {
							case "Else-body":
								//Else-body is finished, adding body to if-block
								popd = opStack.pop();
								System.out.println("Popping " + popd + " TOS: " + opStack.peek());
								nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
								nodes.remove(popd);
								
								//Adding if-block to program
								popd = opStack.pop();
								System.out.println("Popping " + popd + " TOS: " + opStack.peek());
								nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
								nodes.remove(popd);
								break;
							case "Loop-body":
								popd = opStack.pop();
								System.out.println("-----------------------------Popping " + popd + " TOS: " + opStack.peek());
								nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
								nodes.remove(popd);
									
								if(!tokens.get(i+1).name.equals("NonStop'till")){
									//Adding if-block to program
									popd = opStack.pop();
									System.out.println("Popping " + popd + " TOS: " + opStack.peek());
									nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
									nodes.remove(popd);
								} else 
									doWhile = true;
								break;
						}
						popd = "";
					}
					if(popped.equals("=")) {
						System.out.println("Current TOS: " + opStack.peek());
						if(opStack.peek().equals("Assignment") 
								|| opStack.peek().equals("Declaration")) {
							System.out.println("Op: "+opStack.peek());
							parsingOperations = true;
						}
					}
					if(popped.equals("digits") || popped.equals("ride") || popped.equals("moolah") || popped.equals("boogaloh"))
					{
						declaration = popped;
						//At declaration, leaf node is created and is put into declaration as lookahead
						if(opStack.peek().equals("Declaration")) {
							System.out.println("Creating node with token " + tokens.get(i+1).token + " and putting to declaration node");
							nodes.get(opStack.peek()).bodyChildren.add(new ASTNode(tokens.get(i+1).name, tokens.get(i+1).token));
						} else {
							System.out.println("error in creating declaration node");
						}
						
						
					}
					if(popped.equals("Identifier") || popped.equals("Literal"))
					{
						if(popped.equals("Identifier")) 
						{
							int size = 1;
							if(tokens.get(i+1).name.equals("["))
							{	size = Integer.parseInt(tokens.get(i+2).token);
								if(size < 1)
								{
									Error.addError(tokens.get(i+2).linenum, tokens.get(i+2).token + " index out of bounds.");
								}
							}
							SemanticAction.checkdec(declaration, tokens.get(i), size);
							

						}

						if(	tokens.get(i+1).name.equals("=") 	|| 
							tokens.get(i+1).name.equals("<") 	|| 
							tokens.get(i+1).name.equals(">") 	|| 
							tokens.get(i+1).name.equals("<=") 	|| 
							tokens.get(i+1).name.equals(">=") 	|| 
							tokens.get(i+1).name.equals("==") 	|| 
							tokens.get(i+1).name.equals("!=") 	||
							tokens.get(i+1).name.equals("+") 	|| 
							tokens.get(i+1).name.equals("-") 	|| 
							tokens.get(i+1).name.equals("*") 	|| 
							tokens.get(i+1).name.equals("/") 	|| 
							tokens.get(i+1).name.equals("%") 	|| 
							tokens.get(i+1).name.equals("||") 	|| 
							tokens.get(i+1).name.equals("&&"))
						{
							IdEntry curr; 
							if(tokens.get(i).name.equals("Identifier"))
								curr = SymbolTable.idLookup(tokens.get(i).token,0);
							else {
								curr = new IdEntry(tokens.get(i).name, getDataType(tokens.get(i)), tokens.get(i).token);
							} 
							curr.linenum = tokens.get(i).linenum;
							String look1 = tokens.get(i+1).name;
							IdEntry look2;
							IdEntry temp1 = tokens.get(i+2);
							if(temp1.name.equals("Identifier"))
								look2 = SymbolTable.idLookup(temp1.token, 0);
							else if(temp1.name.equals("Literal")){
								look2 = new IdEntry(temp1.name, getDataType(temp1), temp1.token);
								look2.linenum = temp1.linenum;
							}
							else 
								look2 = null;

							SemanticAction.checkMatch(curr, look1, look2);
						}
						declaration = null;
						if(parsingOperations) {
							System.out.println("Current lexeme: "+tokens.get(i).token);
							infix.add(tokens.get(i));
						}
					}
					if(parsingOperations && operations.contains(popped)) {
						infix.add(tokens.get(i));
					}
					
//					System.out.println("final: " + stack.peek());
					i++;
				}
				else {
//					System.out.println("Error in " + tokens.get(i).name + " " + stack.peek());
					Error.addError(tokens.get(i).linenum, "Syntax Error: Error at token " + tokens.get(i).name);
					return false;
				}
			}
			else if(Row.containsKey(stack.peek()))//top is non terminal
			{
				cell = Table[Row.get(stack.peek())][Col.get(tokens.get(i).name)];
				if(cell != null)
				{
					// if(cell.equals("Epsilon"))
					// 	continue;
					prodrule = cell.split(" ");
					System.out.println(prodrule.length);
					System.out.println("pop:" + stack.peek() + " token: " + tokens.get(i).name);

					
					//Parse Tree
					temp = stack.pop();
					switch(temp) {
						case "If-block":
							//Push If-block to op stack
							System.out.println("Pushing " + temp + " & creating node with token " + tokens.get(i).name);
							opStack.push(temp);
							nodes.put(temp, new ASTNode("branch", tokens.get(i).name));
							break;
						case "Else-block":
							//If-block body is finished, adding body to if-block
							popd = opStack.pop();
							System.out.println("Popping " + popd + " TOS: " + opStack.peek());
							nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
							nodes.remove(popd);
							if(prodrule[0].equals("Epsilon")) {
								//Adding if-block to program
								popd = opStack.pop();
								System.out.println("Popping " + popd + " TOS: " + opStack.peek());
								nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
								nodes.remove(popd);
							}
							break;
						case "Else-block'":
							//If else block is found, creating body for the else body
							System.out.println("Pushing Else-body & creating it's node");
							opStack.push("Else-body");
							nodes.put("Else-body", new ASTNode("body", "else-body"));
							break;
						case "Loop-block":
							//Push Loop-block to op stack
							System.out.println("Pushing " + temp + " & creating node with token " + tokens.get(i).name);
							opStack.push(temp);
							nodes.put(temp, new ASTNode("loop", tokens.get(i).name));
							switch (tokens.get(i).name) {
								case "Do'dis": 	String label = "Loop-body";
												System.out.println("Pushing " + label + " & creating it's node");
												opStack.push(label);
												nodes.put(label, new ASTNode("body", label.toLowerCase()));
												break;
							}
							break;
						case "Input":
							System.out.println("Creating node with token " + tokens.get(i).name + 
									" and adding child with value " + tokens.get(i+1).token);
							ASTNode inputNode = new ASTNode("input", tokens.get(i).name);
							inputNode.bodyChildren.add(new ASTNode(tokens.get(i+1).name, tokens.get(i+1).token));
							nodes.get(opStack.peek()).bodyChildren.add(inputNode);	
							break;
						case "Output":
							System.out.println("Pushing " + temp + " & creating node with token " + tokens.get(i).name);
							opStack.push(temp);
							nodes.put(temp, new ASTNode("output", tokens.get(i).name));
							parsingOperations = true;
							break;
						case "Assignment":
							//Push Assignment to op stack
							System.out.println("Pushing " + temp + " & creating node with token assign");
							opStack.push(temp);
							nodes.put(temp, new ASTNode("assignment", "assign"));
							
							//lookahead for the id
							System.out.println("Creating node with token " + tokens.get(i).token + " and putting to assignment node");
							nodes.get(opStack.peek()).bodyChildren.add(new ASTNode(tokens.get(i).name, tokens.get(i).token));
							break;
						case "Assignment'":
							if(prodrule[0].equals("Epsilon")) {
								//No value initialized at declaration
								popd = opStack.pop();
								System.out.println("Popping " + popd + " TOS: " + opStack.peek());
								nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
								nodes.remove(popd);
							}
							break;
						case "Declaration":
							//Push Declaration to op stack
							System.out.println("Pushing " + temp + " & creating node with token " + tokens.get(i).name);
							opStack.push(temp);
							nodes.put(temp, new ASTNode("declaration", tokens.get(i).name));
							break;
						case "Condition":
							if(!opStack.peek().equals(temp)) { 
								//Push Condition to op stack
								System.out.println("Pushing " + temp + " & creating node with token 'plchldr'");
								opStack.push(temp);
								nodes.put(temp, new ASTNode("condition", "plchldr"));
								System.out.println("Op: "+opStack.peek());
								parsingOperations = true;
								parsingConditions = true;
							}
							break;
						case "Condition'":
							if(prodrule[0].equals("Epsilon")) {
								//Getting all of the conditions in infix form done
								System.out.println("Infx");
								for(IdEntry tok : infix) {
									System.out.print(tok.token);
								}
								System.out.println("");
								ASTNode created = createFromInfix(infix);
								//Pushing declaration to where it should be
								popd = opStack.pop();
								System.out.println("Replacing temporary " + popd + " TOS: " + opStack.peek());
								
								if(doWhile){
									nodes.remove(popd);
									nodes.get(opStack.peek()).bodyChildren.add(created);
									popd = opStack.pop();
									System.out.println("Popping " + popd + " TOS: " + opStack.peek());
									nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
									nodes.remove(popd);
								} else {
									nodes.remove(popd);
									nodes.get(opStack.peek()).bodyChildren.add(created);
									
									String label = "";
									switch(opStack.peek()){
										case "If-block": label = "If-body"; break;
										case "Loop-block": label = "Loop-body"; break;
										default: label = "";
									}
									//Creating body for the if body
									System.out.println("Pushing " + label + " & creating it's node");
									opStack.push(label);
									nodes.put(label, new ASTNode("body", label.toLowerCase()));
								}
								infix.clear();
								parsingOperations = false;
								parsingConditions = false;
									
							}
							break;
						case "Expr'":
							if(prodrule[0].equals("Epsilon") && !parsingConditions) {
								if(!tokens.get(i).name.equals(")")) {//catcher for ')'
									//Getting all of the operations in infix form done
									System.out.println("Infx");
									for(IdEntry tok : infix) {
										System.out.print(tok.token);
									}
									System.out.println("");
									ASTNode created = createFromInfix(infix);
									//Pushing declaration to where it should be
									popd = opStack.pop();
									System.out.println("Popping " + popd + " TOS: " + opStack.peek());
									nodes.get(popd).bodyChildren.add(created);
									nodes.get(opStack.peek()).bodyChildren.add(nodes.get(popd));
									nodes.remove(popd);
									infix.clear();
									parsingOperations = false;
									
								}
							}	
							break;
					}
					popd = "";
					
					for(int c = 0; c < prodrule.length; c++){
						System.out.println("new: " + prodrule[c]);
						stack.push(prodrule[c].trim());	
					}
				}
				else {
//					System.out.println("Error in " + tokens.get(i).name + " " + stack.peek());
					Error.addError(tokens.get(i).linenum, "Syntax Error: Error at token " + tokens.get(i).name);
					return false;
				}
			}
		}
		if(tokens.get(i).name.equals("$")) {
			System.out.println("Infx");
			for(IdEntry tok : infix) {
				System.out.print(tok.token);
			}
			System.out.println("");
			return true;	
		}
		else
			return false;
	}

	public String getDataType(IdEntry token){
		for(int i = 0 ; i < pattern.length ; i++)
		{
			Pattern pat = Pattern.compile(pattern[i][0]);
			Matcher m = pat.matcher(token.token);

			if(m.matches()) return pattern[i][1];
		}
		return "";
	}
	
	public ASTNode createFromInfix(ArrayList<IdEntry> infix) {        
		ArrayList<String> operations = new ArrayList<String>() {{
			add("&&");
			add("||");
			add("==");add("!=");add(">=");add("<=");add("<");add(">");
			add("+");add("-");
			add("*");add("/");add("%");
    		add("(");add(")");
		}};
		HashMap <String, Integer> precedenceOf = new HashMap<String, Integer>() {{
			put("*", 1);put("/", 1);put("%", 2);
			put("+", 2);put("-", 2);
			put(">=", 3);put("<=", 3);put("<", 3);put(">", 3);
			put("==", 4);put("!=", 4);
			put("&&", 5);
			put("||", 6);
		}};
        Deque<IdEntry> operators = new ArrayDeque<IdEntry>();
        Deque<ASTNode> postf = new ArrayDeque<ASTNode>();
        ArrayList<IdEntry> postfix = new ArrayList<IdEntry>();
		if(infix.size() == 1) {
			return new ASTNode(infix.get(0).name,infix.get(0).token);
		} else {
			System.out.println("OP");
			//Convert to postfix
			while(!infix.isEmpty()) {
				IdEntry token = infix.remove(0);
				System.out.println("Token: " + token.token);
				if(token.name.equals("Literal") 
						|| token.name.equals("Identifier")) {
					postfix.add(token);
				} else if (operations.contains(token.token)) {
					IdEntry topOp = operators.peek();
					if(token.token.equals(")")) {
						while(topOp != null && !topOp.token.equals("(")) {
							postfix.add(topOp);
							operators.pop();
							topOp = operators.peek();
						}
						operators.pop();
					} else if (token.token.equals("(")) {
						operators.push(token);
					} else {
						while(topOp != null && !topOp.token.equals("(") && (precedenceOf.get(topOp.token) 
								< precedenceOf.get(token.token))) {
							postfix.add(topOp);
							operators.pop();
							topOp = operators.peek();
						}
						operators.push(token);
					}
				}
			}
			
			while(!operators.isEmpty()) {
				postfix.add(operators.pop());
			}
			System.out.println("Postfix");
			for(IdEntry pst:postfix) {
				System.out.print(pst.token + " ");
			}
			System.out.println("");
			
			//postfix to astnode
			do{
				if(operations.contains(postfix.get(0).token)) {
					//Pop 2 and push new astnode if not empty
					IdEntry tmpOp = postfix.remove(0);
					ASTNode tmpNode = new ASTNode(tmpOp.name, tmpOp.token);
					
					tmpNode.bodyChildren.add(0,postf.pop());
					tmpNode.bodyChildren.add(0,postf.pop());
					
					if(!postf.isEmpty() || !postfix.isEmpty()) {
						postf.push(tmpNode);
					} else return tmpNode;
				} else {
					//push leaf
					IdEntry tmpOp = postfix.remove(0);
					postf.push(new ASTNode(tmpOp.name, tmpOp.token));
				}
			} while(!postf.isEmpty() || !postfix.isEmpty());
		}
		return null;
	}
}
