import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TAC
{
	static int tempcount = 0;
	static int labelcount = 0;
	static String code = "";

	public static String createTemp()
	{
		return "temp" + tempcount++;
	}
	public static String createLabel()
	{
		return "L" + labelcount++;
	}

	public static void append(String newcode)
	{
		code = code + "\n\n" + newcode;
	}

	public static void print(String arg)
	{
		append("\tparam: " + arg);
		append("\tcall Sho'me");
	}

	public static void read(String arg)
	{
		append("\tparam: " + arg);
		append("\tcall Gimme");
	}

	public static void assignment(String dest, String val)
	{
		append("\t"+dest + " = " + val);
	}

	public static String expr(String op, String op1, String op2)
	{
		String dest = createTemp();
		append("\t"+dest + " = " + op1 + " " + op + " " + op2);
		return dest;
	}
	public static void dec(String type, String dest, String val)
	{
		if(val.equals(""))
			append("\t"+type + " " + dest);
		else 
			append("\t"+type + " " + dest + " = " + val);
	}
	public static void ifElse(ASTNode cond, ASTNode body, String next)
	{
		String trueLabel = createLabel();
		String ifElseCode = "\tif "+ generate(cond) + " goto " + trueLabel + "\n\tgoto " + next + "\n" + trueLabel+ ": ";
		append(ifElseCode);
		generate(body);
		append(next+ ": ");
	}

	public static String generate(ASTNode node)
	{
		System.out.println(node.token + " : " + node.type);
		if(node.type.equals("Literal") || node.type.equals("Identifier"))
			return node.token;
		
		switch(node.type)
		{
			case "program":
			{
				append("\tStart asm code:");
				break;
			}
			case "assignment": 
			{	
				assignment(node.bodyChildren.get(0).token, generate(node.bodyChildren.get(1)));
				return "";
			}
			case "+":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;
			}
			case "-":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;
			}
			case "*":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;
			}
			case "/":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;
			}
			case "%":
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
			case "<":
			{
				return " " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
			}
			case ">":
			{
				return " " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
			}
			case "<=":
			{
				return " " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
			}
			case ">=":
			{
				return " " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
			}
			case "==":
			{
				return " " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
			}
			case "!=":
			{
				return " " + generate(node.bodyChildren.get(0)) + " " + node.token + " " +  generate(node.bodyChildren.get(1)); 
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

	public static void genASM(ASTNode node, String filename)
	{
		generate(node);
		System.out.println(filename);
		System.out.println(code);
	}
}