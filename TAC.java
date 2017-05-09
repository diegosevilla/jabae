import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class TAC
{
	static int tempcount = 0;
	static int labelcount = 0;
	static int datacount = 0;
	static String data = "section .data\n";
	static String bss = "\nsection .bss\n";
	static String text = "\nsection .text\n\tglobal _start\n";
	static String code = "\n_start:\n";

	public static String toString(String arg){
		if(arg.startsWith("\"") && arg.endsWith("\""))
			return arg;
		else 
			return "\""+ arg + "\"";
	}

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
		code = code + "\n" + newcode;
	}
	public static void declare(String newVar)
	{
		bss += "\n";
	}
	

	public static void print(String arg)
	{
		arg = toString(arg);
		data += "\tdata"+datacount + " db " + arg + ",10\n";
		append("\tmov rax, 1\n\tmov rdi, 1\n\tmov rsi, data" + datacount + "\n\tmov rdx, "+(arg.length()-1)+"\n\tsyscall\n");
		datacount++;
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

	public static String compare(String op1, String op, String op2)
	{
		return (op1 + " " + op + " " + op2);
	}

	public static void ifElse(ASTNode cond, ASTNode ifbody, ASTNode elsebody, String next)
	{
		String trueLabel;
		String falselabel;
		String ifelsecode;

		if(elsebody == null)
		{
			trueLabel = createLabel();
			falselabel = next;
			ifelsecode = "\t" + generate(cond, trueLabel, falselabel) + "\n" + trueLabel + "\n" + generate(ifbody, "", "");
		}
		else
		{
			trueLabel = createLabel();
			falselabel = createLabel();
			ifelsecode = "\t" + generate(cond, trueLabel, falselabel) + "\n" + trueLabel + "\n" + generate(ifbody, "", "") + "\n\tgoto " + next + "\n" + falselabel + "\n" + generate(elsebody, "", "");
		}

		append(ifelsecode);
	}

	public static void and(String left, String right, String truelabel, String falselabel)
	{
		String ltrue = createLabel();
		String lfalse = falselabel;
		String rtrue = truelabel;
		String rfalse = falselabel;
		String andcode = "\t" + left + "\n" + ltrue + "\n\t" + right;
	}

	public static void or(String left, String right, String truelabel, String falselabel)
	{
		String ltrue = truelabel;
		String lfalse = createLabel();
		String rtrue = truelabel;
		String rfalse = falselabel;
		String andcode = "\t" + left + "\n" + lfalse + "\n\t" + right;
	}
	
	public static void whileloop()
	{

	}

	public static String generate(ASTNode node, String arg1, String arg2)
	{
		System.out.println(node.token + " : " + node.type);
		if(node.type.equals("Literal") || node.type.equals("Identifier"))
			return node.token;
		
		switch(node.type)
		{
			case "assignment": 
			{	
				assignment(node.bodyChildren.get(0).token, generate(node.bodyChildren.get(1), "", ""));
				return "";
			}
			case "+":
			case "-":
			case "*":
			case "/":
			case "%":
			{
				String temp = expr(node.token , generate(node.bodyChildren.get(0), "", ""), generate(node.bodyChildren.get(1), "", ""));
				return temp;
			}
			case "output":
			{
				print(generate(node.bodyChildren.get(0), "", ""));
				return "";
			}
			case "input":
			{
				read(generate(node.bodyChildren.get(0), "", ""));
				return "";
			}
			case "declaration":
			{	
				if(node.bodyChildren.size() < 2)
					dec(node.token, generate(node.bodyChildren.get(0), "", ""), "");
				else
					dec(node.token, generate(node.bodyChildren.get(0), "", ""), generate(node.bodyChildren.get(1), "", ""));
				return "";
			}
			case "branch":
			{
				String newLabel = createLabel();
				if(node.bodyChildren.size() < 3)
					ifElse(node.bodyChildren.get(0), node.bodyChildren.get(1), null, newLabel);
				else
					ifElse(node.bodyChildren.get(0), node.bodyChildren.get(1), node.bodyChildren.get(2), newLabel);
				return "";
			}
			case "&&":
			{
				and(generate(node.bodyChildren.get(0), "", ""), generate(node.bodyChildren.get(1), "", ""), arg1, arg2);
				return "";
			}
			case "||":
			{
				or(generate(node.bodyChildren.get(0), "", ""), generate(node.bodyChildren.get(1), "", ""), arg1, arg2);
				return "";
			}
			case "<":
			case ">":
			case "<=":
			case ">=":
			case "==":
			case "!=":
			{
				compare(generate(node.bodyChildren.get(0), "", ""), node.token, generate(node.bodyChildren.get(1), "", ""));
				return ""; 
			}
			case "body":
			{
				for(ASTNode child : node.bodyChildren)
				{
					generate(child, "", "");
				}
				return "";
			}
			case "loop":
			{
				if(node.token.equals("NonStop'till"))
					whileloop();
				return "";

			}
		}

		for(ASTNode child : node.bodyChildren)
		{
			generate(child, "", "");
		}
		return "";
	}

	public static void genASM(ASTNode node, String filename)
	{
		generate(node, "", "");
		append("\tmov rax, 60\n\tmov rdi, 0\n\tsyscall\n");
		try{
			File file = new File(filename);

			file.createNewFile();

			FileWriter writer = new FileWriter(file);
			writer.write(data+bss+text+code);
			writer.flush();
			writer.close();
		} catch(Exception e){}
	}
}