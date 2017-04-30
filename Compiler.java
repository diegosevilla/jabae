import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
					System.out.println("Recognized!");
					parser.ast.printString("", false);
					//ParseTree.toString(ParseTree.getRoot(parser.pt),"",false);
//					TAC.generate(parser.ast);
				} else
					Error.printErrors();


	}

}
