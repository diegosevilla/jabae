import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class TAC
{
	static int tempcount = 8;
	static int labelcount = 0;
	static int datacount = 0;
	static String data = "section .data\n\tnewline db 10\n";
	static String bss = "\nsection .bss\n\tnumbuff resb 8\nbuffer resb 8\n";
	static String text = "\nsection .text\n\tglobal _start\n\n";
	static String code = "_start:\n";
	
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
		String temp = "r" + tempcount;
		tempcount = (tempcount + 1) % 4 + 8;
		return temp;
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
		if(isLiteral(arg)){
			data += "\tdata"+datacount + " db " + toString(arg)+"\n";
			append("\tmov rax, 1\n");
			append("\tmov rdi, 1\n");
			append("\tmov rsi, data" + datacount + "\n");
			append("\tmov rdx, " + (arg.length()-2) + "\n");
			datacount++;
		} else if(SymbolTable.idLookup(arg, 0) != null){
			if(SymbolTable.getType(arg).equals("digits")){
				append("\tcall clearBuffer\n");
				append("\tmov eax, 0\n");
				append("\tmov al, [" + arg + "]\n");
				append("\tcall intToStr\n");
				append("\tmov rax, 1\n");
				append("\tmov rdi, 1\n");
				append("\tmov rsi, numbuff\n");
				append("\tmov rdx, 8\n");
			} else {
				append("\tmov rax, 1\n");
				append("\tmov rdi, 1\n");
				append("\tmov rsi, " + arg + "\n");
				append("\tmov rdx, 1\n");
			}
		} else {
			append("\tcall clearBuffer\n");
			append("\tmov eax, 0\n");
			append("\tmov al, cl\n");
			append("\tcall intToStr\n");
			append("\tmov rax, 1\n");
			append("\tmov rdi, 1\n");
			append("\tmov rsi, numbuff,\n");
			append("\tmov rdx, 8\n");
		}
		append("\tsyscall\n\n");
		
		append("\tmov rax, 1\n");
		append("\tmov rdi, 1\n");
		append("\tmov rsi, newline\n");
		append("\tmov rdx, 1\n");
		append("\tsyscall\n\n");
		
	}

	public static void read(String arg)
	{
		append("\tmov rax, 0\n");
		append("\tmov rdi, 0\n");
		append("\tmov rsi, " + arg +"\n");
		append("\tmov rdx, 8\n");
		append("\tsyscall\n\n");
		
		if(SymbolTable.getType(arg).equals("digits")){
			append("\tmov rsi, " + arg + "\n");
			append("\tcall strToInt\n");
			append("\tmov [" + arg + "], eax\n\n");
		}
	}

	public static void assignment(String dest, String val)
	{	
		if(SymbolTable.idLookup(val, 0) != null)
		{
			append("\tmov eax, [" + val + "]\n");
			append("\tmov [" + dest + "], eax\n\n");
		} else if(isLiteral(val)){
			append("\tmov byte ["+dest+"], "+ val +"\n\n");
		} else 
			append("\tmov ["+dest+"], "+ val +"\n\n");
	}
	
	public static String expr(String op, String op1, String op2)
	{
		if(op.equals("+") || op.equals("-"))
		{
			if((SymbolTable.idLookup(op2, 0) != null))
				op2 = "["+op2+"]";
			append("\tmov eax, " + op2 + "\n");
			
			if((SymbolTable.idLookup(op1, 0) != null))
				op1 = "["+op1+"]";
			append("\tmov ebx, " + op1 + "\n");
			
			append(op.equals("+")? "\tadd eax, ebx\n" : "\tsub eax, ebx\n");
		}
		else if(op.equals("*"))
		{
			if((SymbolTable.idLookup(op2, 0) != null))
				op2 = "["+op2+"]";
			append("\tmov eax, " + op2 + "\n");
			
			if((SymbolTable.idLookup(op1, 0) != null))
				op1 = "["+op1+"]";
			append("\tmov ebx, " + op1 + "\n");
			append("\tmul ebx\n\n");
		}
		else if(op.equals("/"))
		{
			if((SymbolTable.idLookup(op2, 0) != null))
				op2 = "["+op2+"]";
			append("\tmov eax, " + op2 + "\n");
			
			if((SymbolTable.idLookup(op1, 0) != null))
				op1 = "["+op1+"]";
			append("\tmov ebx, " + op1 + "\n");
			append("\tdiv ebx\n\n");
		}
		else if(op.equals("%"))
		{
			if((SymbolTable.idLookup(op2, 0) != null))
				op2 = "["+op2+"]";
			append("\tmov eax, " + op2 + "\n");
			
			if((SymbolTable.idLookup(op1, 0) != null))
				op1 = "["+op1+"]";
			append("\tmov ebx, " + op1 + "\n");
			append("\tdiv ebx\n");
			append("\tmov eax, edx\n");
		}
		append("\tmov ecx, eax\n\n");
		return "ecx";
	}

	public static void dec(String type, String dest, String val)
	{
		bss += "\t"+dest + " resb 8\n";
		if(!val.equals(""))
			assignment(dest, val);
	}

	public static void compare(String op1, String op, String op2, String tval, String fval)
	{
		append("\txor eax, eax\n\txor ebx, ebx\n");
		if((SymbolTable.idLookup(op1, 0) != null))
			op1 = "["+op1+"]";
		append("\tmov eax, " + op1 + "\n");
		if((SymbolTable.idLookup(op2, 0) != null))
			op2 = "["+op2+"]";
		append("\tmov ebx, " + op2 + "\n");
		append("\tcmp al, bl\n");
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
			generate(cond, trueLabel, falselabel);
			append(trueLabel + ": \n");
			generate(ifbody, "", "");
			append("\tjmp " + next + "\n");
			append(falselabel + ": \n");
			generate(elsebody, "", "");
			append(next + ": \n");
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
		append("\n" + ltrue+":\n");
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
		append("\n" + lfalse + ":\n");
		generate(right, rtrue, rfalse);
		//return orcode;
}
	
	public static void whileloop(ASTNode cond, ASTNode loopbody, String next)
	{
		String begin = createLabel();
		String trueLabel = createLabel();
		String falseLabel = next;
		append(begin + ": \n");
		generate(cond, trueLabel, falseLabel);
		append(trueLabel + ": \n");
		generate(loopbody, "", "");
		append("\tjmp " + begin +"\n");
		append(next + ": \n");
	}
	
	public static void dowhileloop(ASTNode cond, ASTNode loopbody, String next)
	{
		String begin = createLabel();
		append(begin+": \n");
		generate(loopbody, "", "");
		generate(cond, begin, next);
		append(next+": \n");
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
				String op1 = generate(node.bodyChildren.get(1), "", "");
				String op2 = generate(node.bodyChildren.get(0), "", "");
				String temp = expr(node.token , op1, op2);
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
		createFunctions();
		generate(node, "", "");
		append("\tmov rax, 60\n\tmov rdi, 0\n\tsyscall\n\n");
		try{
			File file = new File(filename);

			file.createNewFile();

			FileWriter writer = new FileWriter(file);
			writer.write(data+bss+text+code);
			writer.flush();
			writer.close();
		} catch(Exception e){}
		//System.out.println(code);
	}

	private static void createFunctions() {
		text += "strToInt:\n";
		text += "\txor eax, eax\n";
		text += "\tmov ebx, 10\n\n";
		text += "\tstartStrToInt: \n";
		text += "\t\tcmp byte [rsi], 10\n";
		text += "\t\tje endStrToInt\n";
		text += "\t\tcmp byte [rsi], '0'\n";
		text += "\t\tjl endStrToInt\n";
		text += "\t\tcmp byte [rsi], '9'\n";
		text += "\t\tjg endStrToInt\n\n";
		text += "\t\tsub byte [rsi], '0'\n";
		text += "\t\tmul ebx\n";
		text += "\t\tadd eax, [rsi]\n";
		text += "\t\tinc rsi\n";
		text += "\t\tjmp startStrToInt\n\n";
		text += "\tendStrToInt:\n";
		text += "\t\tret\n\n";
		
		text += "intToStr:\n";
		text += "\tlea r8, [numbuff+8]\n";
		text += "\tloopIntToStr:\n";
		text += "\t\txor edx, edx\n";
		text += "\t\tmov ecx, 10\n";
		text += "\t\tdiv ecx\n";
		text += "\t\tadd dl, '0'\n";
		text += "\t\tdec r8\n";
		text += "\t\tmov byte [r8], dl\n";
		text += "\t\tcmp eax, 0\n";
		text += "\t\tjne loopIntToStr\n";
		text += "\tret\n\n";
		
		text += "clearBuffer:\n";
		text += "\tmov esi, 8\n";
		text += "\tstartLoop:\n";
		text += "\t\tdec esi\n";
		text += "\t\tmov byte [numbuff+esi], 0\n";
		text += "\t\tcmp esi, 0\n";
		text += "\t\tjnl startLoop\n";
		text += "\tret\n\n";
	}
}