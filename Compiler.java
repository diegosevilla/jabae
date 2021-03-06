import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Compiler {
	public static int NUMLINES = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(!args[0].endsWith(".jbe")) {
            System.out.println("Invalid file type");
            return;
        }
		
		Scanner scanner = new Scanner();
		scanner.lexemes.initResWords();
		ParseTable parser = new ParseTable();
		ArrayList<IdEntry> tokens = scanner.scan(args[0]);
		for(IdEntry a : tokens) System.out.println(a.name + " " + a.token);
		if(parser.Valid(tokens) && Error.errors.size() == 0) {
			//System.out.println("Recognized!");
			parser.ast.printString("", false);
			//ParseTree.toString(ParseTree.getRoot(parser.pt),"",false);
			TAC.genASM(parser.ast, args[0].split("\\.")[0] + ".asm");
			createExecutable(args[0].split("\\.")[0]);
		} else
			Error.printErrors();
	}

	public static void createExecutable(String filename){
		try{
			Process proc = Runtime.getRuntime().exec("nasm -f elf64 -o " + filename + ".o " + filename + ".asm");
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String s = "";
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}
			proc = Runtime.getRuntime().exec("ld -o " + filename + " " + filename + ".o");
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}
			//Runtime.getRuntime().exec("rm " + filename + ".asm");
//			Runtime.getRuntime().exec("rm " + filename + ".o");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
