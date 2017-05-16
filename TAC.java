import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static boolean isLiteral(String arg){
		String[] patterns = new String[4];
		patterns[0] = "[\\\"].+[\\\"]";
		patterns[1] = "[\\\'].[\\\']";
		patterns[2] = "[-+]?[0-9]+\\.[0-9]+";
		patterns[3] = "[-+]?[0-9]+";
		for(int i = 0 ; i < patterns.length ; i++){
			Pattern pat = Pattern.compile(patterns[i]);
			Matcher m = pat.matcher(arg);
			if(m.matches())
				return true;
		}
		return false;
	}
	
	public static String getJumpValue(String op){
		switch(op)
		{
			case "==": return "je ";
			case "!=": return "jne ";
			case ">" : return "jg ";
			case "<" : return "jl ";
			case ">=": return "jge ";
			case "<=": return "jle ";
		}
		return "";
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
	
	public static void print(String arg)
	{
		append("\n");
		append("\tmov rax, 1\n");
		append("\tmov rdi, 1\n");
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

	public static void read(String arg)
	{
		append("\n");
		append("\tmov rax, 0\n");
		append("\tmov rdi, 0\n");
		append("\tmov rsi, " + arg +"\n");
		append("\tmov rdx, 16\n");
		append("\tsyscall\n\n");
	}

	public static void assignment(String dest, String val)
	{
		append("\n");
		if(isLiteral(val))
			append("\tmov byte [" + dest + "], " + val + "\n");
		else
		{
			append("\tmov byte al, [" + val + "]\n");
			append("\tmov byte [" + dest + "], al\n");
		}
	}
	
	public static String expr(String op, String op1, String op2)
	{
		String dest = createTemp();
		append("\n");
		if(op.equals("+") || op.equals("-"))
		{
			if(isLiteral(op1))
			{
				append("\tmov eax, " + op1 + "\n");
			}
			else
			{
				append("\tmov eax, [" + op1 + "]\n");
			}
			append("\tsub eax, '0'\n");
			
			if(isLiteral(op2))
			{
				append("\tmov ebx, " + op2 + "\n");
			}
			else
			{
				append("\tmov ebx, [" + op2 + "]\n");
			}
			append("\tsub ebx, '0'\n");
			
			append("\tadd eax, abx\n");
			append("\tadd eax, '0'\n");
			append("\tmov [" + dest + "], eax\n");
		}
		else if(op.equals("*"))
		{
			if(isLiteral(op1))
			{
				append("\tmov al, " + op1 + "\n");
			}
			else
			{
				append("\tmov al, [" + op1 + "]\n");
			}
			append("\tsub al, '0'\n");
			
			if(isLiteral(op2))
			{
				append("\tmov bl, " + op2 + "\n");
			}
			else
			{
				append("\tmov bl, [" + op2 + "]\n");
			}
			append("\tsub bl, '0'\n");

			append("\tmul bl\n");
			append("\tadd al, '0'\n");
			append("\tmov [" + dest + "], al\n");
		}
		else if(op.equals("/"))
		{
			if(isLiteral(op1))
			{
				append("\tmov ax, " + op1 + "\n");
			}
			else
			{
				append("\tmov ax, [" + op1 + "]\n");
			}
			append("\tsub al, '0'\n");
			
			if(isLiteral(op2))
			{
				append("\tmov bl, " + op2 + "\n");
			}
			else
			{
				append("\tmov bl, [" + op2 + "]\n");
			}
			append("\tsub bl, '0'\n");

			append("\tdiv bl\n");
			append("\tadd ax, '0'\n");
			append("\tmov [" + dest + "], al\n");
		}
		else if(op.equals("%"))
		{
			if(isLiteral(op1))
			{
				append("\tmov ax, " + op1 + "\n");
			}
			else
			{
				append("\tmov ax, [" + op1 + "]\n");
			}
			append("\tsub al, '0'\n");
			
			if(isLiteral(op2))
			{
				append("\tmov bl, " + op2 + "\n");
			}
			else
			{
				append("\tmov bl, [" + op2 + "]\n");
			}
			append("\tsub bl, '0'\n");

			append("\tdiv bl\n");
			append("\tadd ax, '0'\n");
			append("\tmov [" + dest + "], ah\n");
		}
		return dest;
	}

	public static void dec(String type, String dest, String val)
	{
		bss += "\t"+dest + " resb 16\n";
		if(!val.equals(""))
			assignment(dest, val);
	}

	public static void compare(String op1, String op, String op2, String tval, String fval)
	{
		append("\n");
		boolean op1Lit = isLiteral(op1);
		boolean op2Lit = isLiteral(op2);
		if(op1Lit && op2Lit)
		{
			append("\tmov byte al, " + op1 +"\n");
			append("\tmov byte bl, " + op2 + "\n");
			append("\tcmp byte al, bl\n");
		}
		else if(!op1Lit && op2Lit)
		{
			append("\tcmp byte ["+ op1 + "], " + op2 + "\n");
		} else if(op1Lit && !op2Lit){
			append("\tmov byte al, " + op1 + "\n");
			append("\tcmp byte al, [" + op2 + "]\n");
		} else
		{
			append("\tmov byte al, [" + op1 + "]\n");
			append("\tcmp byte al, [" + op2 + "]\n");
		}
		
		append("\t" + getJumpValue(op) + tval + "\n");
		append("\tjmp " + fval +  "\n\n");

	}

	public static void ifElse(ASTNode cond, ASTNode ifbody, ASTNode elsebody, String next)
	{
		String trueLabel;
		String falselabel;
		//String ifelsecode;

		if(elsebody == null)
		{
			trueLabel = createLabel();
			falselabel = next;
			generate(cond, trueLabel, falselabel);
			append(trueLabel + ": \n");
			generate(ifbody, "", "");
			append(next + ": \n");
		}
		else
		{
			trueLabel = createLabel();
			falselabel = createLabel();
			//ifelsecode = "\t" + generate(cond, trueLabel, falselabel) + "\n" + trueLabel + "\n" + generate(ifbody, "", "") + "\n\tgoto " + next + "\n" + falselabel + "\n" + generate(elsebody, "", "")+ "\n" + next;
			generate(cond, trueLabel, falselabel);
			append("\n" + trueLabel + ": \n");
			generate(ifbody, "", "");
			append("\tjmp " + next + "\n\n");
			append(falselabel + ": \n");
			generate(elsebody, "", "");
			append("\n" + next + ": \n");
		}

		//append(ifelsecode);
	}

	public static void and(ASTNode left, ASTNode right, String truelabel, String falselabel)
	{
		String ltrue = createLabel();
		String lfalse = falselabel;
		String rtrue = truelabel;
		String rfalse = falselabel;
		//String andcode = generate(left, ltrue, lfalse) + "\n" + ltrue + "\n\t" + generate(right, rtrue, rfalse);
		generate(left, ltrue, lfalse);
		append("\n" + ltrue);
		generate(right, rtrue, rfalse);
		//return andcode;
	}

	public static void or(ASTNode left, ASTNode right, String truelabel, String falselabel)
	{
		String ltrue = truelabel;
		String lfalse = createLabel();
		String rtrue = truelabel;
		String rfalse = falselabel;
		//String orcode = generate(left, ltrue, lfalse) + "\n" + lfalse + "\n\t" + generate(right, rtrue, rfalse);
		generate(left, ltrue, lfalse);
		append("\n" + lfalse);
		generate(right, rtrue, rfalse);
		//return orcode;
	}
	
	public static void whileloop(ASTNode cond, ASTNode loopbody, String next)
	{
		String begin = createLabel();
		String trueLabel = createLabel();
		String falseLabel = next;
		append("\n" + begin);
		generate(cond, trueLabel, falseLabel);
		append("\n" + trueLabel);
		generate(loopbody, "", "");
		append("\n\tgoto" + begin);
		append("\n" + next);
	}
	
	public static void dowhileloop(ASTNode cond, ASTNode loopbody, String next)
	{
		String begin = createLabel();
		append("\n" + begin);
		generate(loopbody, "", "");
		generate(cond, begin, next);
		append("\n" + next);
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
				String temp = expr(node.token , generate(node.bodyChildren.get(1), "", ""), generate(node.bodyChildren.get(0), "", ""));
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
				and(node.bodyChildren.get(0), node.bodyChildren.get(1), arg1, arg2);
				return "";
			}
			case "||":
			{
				or(node.bodyChildren.get(0), node.bodyChildren.get(1), arg1, arg2);
				return "";
			}
			case "<":
			case ">":
			case "<=":
			case ">=":
			case "==":
			case "!=":
			{
				compare(generate(node.bodyChildren.get(0), "", ""), node.token, generate(node.bodyChildren.get(1), "", ""), arg1, arg2);
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
				String newLabel = createLabel();
				if(node.token.equals("NonStop'till"))
				{
					whileloop(node.bodyChildren.get(0), node.bodyChildren.get(1), newLabel);
				}
				if(node.token.equals("Do'dis"))
				{
					dowhileloop(node.bodyChildren.get(1), node.bodyChildren.get(0), newLabel);
				}
				if(node.token.equals("Pop'till"))
				{
					whileloop(node.bodyChildren.get(0), node.bodyChildren.get(1), newLabel);
				}
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
		append("\n\tmov rax, 60\n\tmov rdi, 0\n\tsyscall\n\n");
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