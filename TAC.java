import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;

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
		code += newcode;
	}	
	public static boolean isLiteral(String arg){
		if(arg.startsWith("\"") && arg.endsWith("\""))
				return true;
		return false;
	}
	
	public static void print(String arg)
	{
		append("\tmov rax, 1\n");
		append("\tmov rdi, 1\n");
		JOptionPane.showMessageDialog(null, arg);
		if(isLiteral(arg)){
			data += "\tdata"+datacount + " db " + toString(arg)+"\n";
			append("\tmov rsi, data" + datacount + "\n");
			append("\tmov rdx, " + (arg.length()-2) + "\n");
			datacount++;
		} else {
			append("\tmov rsi, " + arg + "\n");
			append("\tmov rdx, 16\n");
		}
		append("\tsyscall\n\n");
		
	}

	//TODO edit to assembly
	public static void read(String arg)
	{
		append("\tmov rax, 0\n");
		append("\tmov rdi, 0\n");
		append("\tmov rsi, " + arg +"\n");
		append("\tmov rdx, 16\n");
		append("\tsyscall\n\n");
	}

	//TODO edit to assembly
	public static String assignment(String dest, String val)
	{
		return "\t"+dest + " = " + val;
	}

	//TODO edit to assembly
	public static String expr(String op, String op1, String op2)
	{
		String dest = createTemp();
		append("\t"+dest + " = " + op1 + " " + op + " " + op2);
		return dest;
	}

	//TODO edit to assembly
	public static String dec(String type, String dest, String val)
	{
		bss += "\t"+dest + " resb 16\n";
		if(!val.equals(""))
			return "\t"+type + " " + dest + " = " + val;
		else return "";
	}

	//TODO edit to assembly
	public static String compare(String op1, String op, String op2, String tval, String fval)
	{
		return "if" + " " + op1 + " " + op + " " + op2 + " goto " + tval + "\n\tgoto " + fval;
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
			ifelsecode = "\t" + generate(cond, trueLabel, falselabel) + "\n" + trueLabel + "\n" + generate(ifbody, "", "") + "\n" + next;
		}
		else
		{
			trueLabel = createLabel();
			falselabel = createLabel();
			ifelsecode = "\t" + generate(cond, trueLabel, falselabel) + "\n" + trueLabel + "\n" + generate(ifbody, "", "") + "\n\tgoto " + next + "\n" + falselabel + "\n" + generate(elsebody, "", "")+ "\n" + next;
		}

		append(ifelsecode);
	}

	public static String and(ASTNode left, ASTNode right, String truelabel, String falselabel)
	{
		String ltrue = createLabel();
		String lfalse = falselabel;
		String rtrue = truelabel;
		String rfalse = falselabel;
		//String andcode = generate(left, ltrue, lfalse) + "\n" + ltrue + "\n\t" + generate(right, rtrue, rfalse);
		String andcode = generate(right, rtrue, rfalse) + "\n" + ltrue + "\n\t" + generate(left, ltrue, lfalse);
		return andcode;
	}

	public static String or(ASTNode left, ASTNode right, String truelabel, String falselabel)
	{
		String ltrue = truelabel;
		String lfalse = createLabel();
		String rtrue = truelabel;
		String rfalse = falselabel;
		//String orcode = generate(left, ltrue, lfalse) + "\n" + lfalse + "\n\t" + generate(right, rtrue, rfalse);
		String orcode = generate(right, rtrue, rfalse) + "\n" + lfalse + "\n\t" + generate(left, ltrue, lfalse);
		return orcode;
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
				return assignment(node.bodyChildren.get(0).token, generate(node.bodyChildren.get(1), "", ""));
				//return "";
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
					return dec(node.token, generate(node.bodyChildren.get(0), "", ""), "");
				else
					return dec(node.token, generate(node.bodyChildren.get(0), "", ""), generate(node.bodyChildren.get(1), "", ""));
				//return "";
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
				return and(node.bodyChildren.get(0), node.bodyChildren.get(1), arg1, arg2);
				//return "";
			}
			case "||":
			{
				return or(node.bodyChildren.get(0), node.bodyChildren.get(1), arg1, arg2);
				//return "";
			}
			case "<":
			case ">":
			case "<=":
			case ">=":
			case "==":
			case "!=":
			{
				return compare(generate(node.bodyChildren.get(0), "", ""), node.token, generate(node.bodyChildren.get(1), "", ""), arg1, arg2);
				//return ""; 
			}
			case "body":
			{
				for(ASTNode child : node.bodyChildren)
				{
					return generate(child, "", "");
				}
				//return "";
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
		System.out.println(code);
	}
}