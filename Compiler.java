import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Compiler {
	public static int NUMLINES = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

        ParseTable parser = new ParseTable();
        
        if(!args[0].endsWith(".jbe")) {
            System.out.println("Invalid file type");
            return;
        }
       
    	try {
    		//Declaration and reading from file
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            ArrayList<String> lines = new ArrayList<String>();
            String line;
            
            //Retrieve all lines from file
            while((line = br.readLine()) != null){
                lines.add(line);
            	NUMLINES++;
            }
            
            //Lexical Scanning
            Scanner scan = new Scanner(lines);
            for(int i=0; i<scan.tokens.size(); i++){
            	System.out.println(scan.tokens.get(i));
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
