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
				ParseTable parser = new ParseTable();
				ArrayList<String[]> tokens = scanner.scan(args[0]);
				if(parser.Valid(tokens))
					System.out.println("Recognized!");
				else
					System.out.println("error");
//				ParseTree.toStringAll(ParseTree.getRoot(parser.pt));

	}

}
