import java.util.ArrayList;

public class TAC
{
	static int tempcount = 0;
	static int labelcount = 0;

	public static String createTemp()
	{
		return "temp" + tempcount++;
	}
	public static String createLabel()
	{
		return "L" + labelcount++;
	}


	public static void print(String arg)
	{
		System.out.println("\tparam: " + arg);
		System.out.println("\tcall Sho'me");
	}

	public static void read(String arg)
	{
		System.out.println("\tparam: " + arg);
		System.out.println("\tcall Gimme");
	}

	public static void assignment(String dest, String val)
	{
		System.out.println("\t"+dest + " = " + val);
	}

	public static String expr(String op, String op1, String op2)
	{
		String dest = createTemp();
		System.out.println("\t"+dest + " = " + op1 + " " + op + " " + op2);
		return dest;
	}
	public static void dec(String type, String dest, String val)
	{
		if(val.equals(""))
			System.out.println("\t"+type + " " + dest);
		else 
			System.out.println("\t"+type + " " + dest + " = " + val);
	}
	public static void ifElse(ASTNode cond, ASTNode body, String next)
	{
		String trueLabel = createLabel();
		String ifElseCode = "\t"+ generate(cond) + " goto " + trueLabel + "\n\tgoto " + next + "\n" + trueLabel+ ": ";
		System.out.println(ifElseCode);
		generate(body);
		System.out.println(next+ ": ");
	}

	public static String generate(ASTNode node)
	{
		//System.out.println(node.token + " : " + node.type);
		if(node.type.equals("Literal") || node.type.equals("Identifier"))
			return node.token;
		
		switch(node.type)
		{
			case "assignment": 
			{
				assignment(node.bodyChildren.get(0).token, generate(node.bodyChildren.get(1)));
				return "";
			}
			case "exprop":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;
			}
			case "termop":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;	
			}
			case "output":
			{
				print(generate(node.bodyChildren.get(0)));
				return "";
			}
			case "input":
			{
				read(generate(node.bodyChildren.get(0)));
				return "";
			}
			case "declaration":
			{	
				if(node.bodyChildren.size() < 2)
					dec(node.token, generate(node.bodyChildren.get(0)), "");
				else
					dec(node.token, generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return "";
			}
			case "branch":
			{
				String newLabel = createLabel();
				ifElse(node.bodyChildren.get(0), node.bodyChildren.get(1), newLabel);
				return "";
			}
			case "condition":
			{
				return "If " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
			}
			case "body":
			{
				for(ASTNode child : node.bodyChildren)
				{
					generate(child);
				}
				return "";
			}
		}

		for(ASTNode child : node.bodyChildren)
		{
			generate(child);
		}
		return "";
	}

}