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
				if(parser.Valid(tokens)) {
					System.out.println("Recognized!");
					//ParseTree.toString(ParseTree.getRoot(parser.pt),"",false);
				} else
					System.out.println("error");

	}

}
