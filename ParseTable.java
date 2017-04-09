import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

public class ParseTable
{
	Hashtable<String, Integer> Row = new Hashtable<String, Integer>();
	Hashtable<String, Integer> Col = new Hashtable<String, Integer>();
	String[][] Table = new String[25][42];
	String[][] pattern = new String[][]{
								{"[\\\'].[\\\']", "ride"}, 
								{"[-+]?[0-9]+\\.[0-9]+", "moolah"}, 
								{"[-+]?[0-9]+", "digits"}
							 };
	BodyNode ast;
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
		Row.put("Condition", 16);
		Row.put("Condition'", 17);
		Row.put("Condition''", 18);
		Row.put("Sym", 19);
		Row.put("Expr", 20);
		Row.put("Expr'", 21);
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
		Col.put("PEACE'OUT!", 7);
		Col.put("Check'dis", 8);
		Col.put("noh", 9);
		Col.put("(", 10);
		Col.put(")", 11);
		Col.put("{", 12);
		Col.put("}", 13);
		Col.put("Pop'till", 14);
		Col.put("NonStop'till", 15);
		Col.put("Do'dis", 16);
		Col.put("Yo'Wait", 17);
		Col.put("How'Bowt", 18);
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
		//all cells nulled are error cells
		for(int i = 0; i < Table.length; i++)
			for(int j = 0; j < Table[i].length; j++)
				Table[i][j] = null;

		//overwrite cells that are not error cells
		//all production rules reversed for stack
		//Program
		Table[1][6] = "PEACE'OUT! Statement YO!";

		//Statement
		Table[2][8] = "Statement If-block";
		Table[2][14] = "Statement Loop-block";
		Table[2][15] = "Statement Loop-block";
		Table[2][16] = "Statement Loop-block";
		Table[2][17] = "Statement Switch-block";
		Table[2][23] = "Statement Input";
		Table[2][24] = "Statement Output";
		Table[2][26] = "Statement Declaration";
		Table[2][27] = "Statement Declaration";
		Table[2][28] = "Statement Declaration";
		Table[2][29] = "Statement Declaration";
		Table[2][38] = "Statement Assignment";
		Table[2][7] = "Epsilon";
		Table[2][13] = "Epsilon";
		Table[2][21] = "Epsilon";
		Table[2][22] = "Epsilon";

		//If-block
		Table[3][8] = "Else-block } Statement { ) Condition ( Check'dis";

		//Else-block
		Table[4][7] = "Epsilon";
		Table[4][9] = "Else-block' noh";
		Table[4][8] = "Epsilon";
		Table[4][14] = "Epsilon";
		Table[4][15] = "Epsilon";
		Table[4][16] = "Epsilon";
		Table[4][17] = "Epsilon";
		Table[4][23] = "Epsilon";
		Table[4][24] = "Epsilon";
		Table[4][26] = "Epsilon";
		Table[4][27] = "Epsilon";
		Table[4][28] = "Epsilon";
		Table[4][29] = "Epsilon";
		Table[4][38] = "Epsilon";

		//Else-block'
		Table[5][12] = "} Statement {";
		Table[5][8] = "If-block";

		//Loop-block
		Table[6][14] = "} Control-block Statement { ) Condition ( Pop'till";
		Table[6][15] = "} Control-block Statement { ) Condition ( NonStop'till";
		Table[6][16] = ") Condition ( NonStop'till } Control-block Statement { Do'dis";
		Table[6][7] = "Epsilon";

		//Switch-block
		Table[7][17] ="} Case-block { ) Expr ( Yo'Wait";
		Table[7][7] = "Epsilon";

		//Case-block
		Table[8][18] = "Case-block' Control-block Statement : Expr How'Bowt";
		Table[8][19] = "Control-block Statement : Aight";

		//Case-block'
		Table[9][18] = "Case-block";
		Table[9][19] = "Case-block";
		Table[9][13] = "Epsilon";

		//Control-block
		Table[10][21] = "peace";
		Table[10][22] = "shoot";
		Table[10][13] = "Epsilon";
		Table[10][18] = "Epsilon";
		Table[10][19] = "Epsilon";

		//Input
		Table[11][23] = "id Gimme";

		//Output
		Table[12][24] = "Expr Sho'me";

		//Assignment
		Table[13][38] = "Expr = id";

		//Assignment'
		Table[14][Col.get("PEACE'OUT!")] = "Epsilon";
		Table[14][25] = "Expr =";
		Table[14][8] = "Epsilon";
		Table[14][14] = "Epsilon";
		Table[14][15] = "Epsilon";
		Table[14][16] = "Epsilon";
		Table[14][17] = "Epsilon";
		Table[14][23] = "Epsilon";
		Table[14][24] = "Epsilon";
		Table[14][26] = "Epsilon";
		Table[14][27] = "Epsilon";
		Table[14][28] = "Epsilon";
		Table[14][29] = "Epsilon";
		Table[14][38] = "Epsilon";

		//Declaration
		Table[15][26] = "Assignment' id digits";
		Table[15][27] = "Assignment' id ride";
		Table[15][28] = "Assignment' id moolah";
		Table[15][29] = "Assignment' id boogaloh";

		//Condition
		Table[16][10] = "Condition' Condition''";
		Table[16][38] = "Condition' Condition''";
		Table[16][39] = "Condition' Condition''";
		Table[16][40] = "Condition' Condition''";
		Table[16][41] = "Condition' Condition''";
		Table[16][11] = "Epsilon";

		//Condition'
		Table[17][30] = "Condition &&";
		Table[17][31] = "Condition ||";
		Table[17][11] = "Epsilon";

		//Condition''
		Table[18][10] = "Sym Expr";
		Table[18][38] = "Sym Expr";
		Table[18][39] = "Sym Expr";
		Table[18][40] = "Sym Expr";
		Table[18][41] = "Sym Expr";

		//Sym
		Table[19][32] = "Expr ==";
		Table[19][33] = "Expr !=";
		Table[19][34] = "Expr >=";
		Table[19][35] = "Expr <=";
		Table[19][36] = "Expr <";
		Table[19][37] = "Expr >";

		//Expr
		Table[20][10] = "Expr' Term";
		Table[20][38] = "Expr' Term";
		Table[20][39] = "Expr' Term";
		Table[20][40] = "Expr' Term";
		Table[20][41] = "Expr' Term";
		Table[20][7] = "Epsilon";

		//Expr'
		Table[21][1] = "Expr' Term +";
		Table[21][2] = "Expr' Term -";
		Table[21][7] = "Epsilon";
		Table[21][30] = "Epsilon";
		Table[21][31] = "Epsilon";
		Table[21][11] = "Epsilon";
		Table[21][Col.get("}")] = "Epsilon";
		Table[21][Col.get(">")] = "Epsilon";
		Table[21][Col.get(">")] = "Epsilon";
		Table[21][Col.get("==")] = "Epsilon";
		Table[21][8] = "Epsilon";
		Table[21][14] = "Epsilon";
		Table[21][15] = "Epsilon";
		Table[21][16] = "Epsilon";
		Table[21][17] = "Epsilon";
		Table[21][23] = "Epsilon";
		Table[21][24] = "Epsilon";
		Table[21][26] = "Epsilon";
		Table[21][27] = "Epsilon";
		Table[21][28] = "Epsilon";
		Table[21][29] = "Epsilon";
		Table[21][38] = "Epsilon";

		//Term
		Table[22][10] = "Term' Factor";
		Table[22][38] = "Term' Factor";
		Table[22][39] = "Term' Factor";
		Table[22][40] = "Term' Factor";
		Table[22][41] = "Term' Factor";

		//Term'
		Table[23][3] = "Term' Factor *";
		Table[23][4] = "Term' Factor /";
		Table[23][5] = "Term' Factor %";
		Table[23][11] = "Epsilon";
		Table[23][1] = "Epsilon";
		Table[23][2] = "Epsilon";
		Table[23][7] = "Epsilon";
		Table[23][Col.get("&&")] = "Epsilon";
		Table[23][Col.get("||")] = "Epsilon";
		Table[23][Col.get(">")] = "Epsilon";
		Table[23][Col.get("<")] = "Epsilon";
		Table[21][Col.get(">")] = "Epsilon";
		Table[23][Col.get("}")] = "Epsilon";
		Table[23][Col.get("==")] = "Epsilon";
		Table[23][8] = "Epsilon";
		Table[23][14] = "Epsilon";
		Table[23][15] = "Epsilon";
		Table[23][16] = "Epsilon";
		Table[23][17] = "Epsilon";
		Table[23][23] = "Epsilon";
		Table[23][24] = "Epsilon";
		Table[23][26] = "Epsilon";
		Table[23][27] = "Epsilon";
		Table[23][28] = "Epsilon";
		Table[23][29] = "Epsilon";
		Table[23][38] = "Epsilon";
		
		//Factor
		Table[24][10] = ") Term (";
		Table[24][38] = "id";
		Table[24][39] = "lit";
		Table[24][40] = "legit";
		Table[24][41] = "tigel";
		Table[24][8] = "Epsilon";
		Table[24][14] = "Epsilon";
		Table[24][15] = "Epsilon";
		Table[24][16] = "Epsilon";
		Table[24][17] = "Epsilon";
		Table[24][23] = "Epsilon";
		Table[24][24] = "Epsilon";
		Table[24][26] = "Epsilon";
		Table[24][27] = "Epsilon";
		Table[24][28] = "Epsilon";
		Table[24][29] = "Epsilon";
	}

	//recognizer
	public boolean Valid(ArrayList<IdEntry> tokens)
	{
        Deque<String> stack = new ArrayDeque<String>();
        Deque<ASTNode> nodeStack = new ArrayDeque<ASTNode>();
        
        stack.push("$");
        stack.push("Program");
        String cell;
        String temp;
        String[] prodrule;
		int i;
		String declaration = null;

		for(i = 0; !stack.peek().equals("$");)
		{
			while(stack.peek().equals("Epsilon")){
				stack.pop();
			}
			if(Col.containsKey(stack.peek()))//top is terminal
			{
				if(stack.peek().equals(tokens.get(i).name))
				{
					System.out.println("pop: " + stack.peek() + " token: " + tokens.get(i).name + " val: " + tokens.get(i).token);
					String popped = stack.pop();
					if(popped.equals("YO!")){
						SymbolTable.enterBlock();
						nodeStack.push(new BodyNode("body","program"));
					}
					if(popped.equals("PEACE'OUT!")) {
						ast = (BodyNode) nodeStack.pop();
					}
					if(popped.equals("{")){
						SymbolTable.enterBlock();
					}
					if(popped.equals("}")){
						SymbolTable.leaveBlock();
					}
					if(popped.equals("digits") || popped.equals("ride") || popped.equals("moolah") || popped.equals("boogaloh"))
					{
						declaration = popped;
					}
					if(popped.equals("id") || popped.equals("lit"))
					{	
//						if(nodeStack.peek() instanceof OpNode) {
//							OpNode tmp = (OpNode) nodeStack.peek();
//							if(tmp.token.equals("plchldr")) {
//								tmp.left = new LeafNode(popped, tokens.get(i).token);
//							} else if(tmp.type.equals("")){
//								tmp.right = new LeafNode(popped, tokens.get(i).token)
//							}
//						} else
//						
						if(popped.equals("id")) 
							SemanticAction.checkdec(declaration, tokens.get(i));

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
							if(tokens.get(i).name.equals("id"))
								curr = SymbolTable.idLookup(tokens.get(i).token,0);
							else {
								curr = new IdEntry(tokens.get(i).name, getDataType(tokens.get(i)), tokens.get(i).token);
							} 
							curr.linenum = tokens.get(i).linenum;
							String look1 = tokens.get(i+1).name;
							IdEntry look2;
							IdEntry temp1 = tokens.get(i+2);
							if(temp1.name.equals("id"))
								look2 = SymbolTable.idLookup(temp1.token, 0);
							else if(temp1.name.equals("lit")){
								look2 = new IdEntry(temp1.name, getDataType(temp1), temp1.token);
								look2.linenum = temp1.linenum;
							}
							else 
								look2 = null;

							SemanticAction.checkMatch(curr, look1, look2);
						}
						declaration = null;
					
					i++;
				}
				else {
//					System.out.println("Error in " + tokens.get(i).name + " " + stack.peek());
					Error.addError(tokens.get(i).linenum, "Syntax Error: Error at token " + tokens.get(i).name);
					return false;
				}
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
					System.out.println("pop:" + stack.peek() + " token: " + tokens.get(i).name + " val: " + tokens.get(i).token);
					
					//Parse Tree
					temp = stack.pop().trim();
					if(temp.equals("If-block")) {
						nodeStack.push(new IfNode("branch", tokens.get(i).name));
					
					} else if(temp.equals("Else-block")) {
						if(!prodrule[0].equals("Epsilon") && prodrule[0].equals("{")) {
								IfNode tmp = (IfNode) nodeStack.peek();
								tmp.elseBody = new BodyNode("else", "else");
							
						} else {
							IfNode tmp1 = (IfNode) nodeStack.pop();
							BodyNode tmp2 = (BodyNode) nodeStack.peek();
							tmp2.bodyChildren.add(tmp1);
						}
				    }
					
					for(int c = 0; c < prodrule.length; c++){
						stack.push(prodrule[c]);
						System.out.println("new: " + stack.peek());
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
}
