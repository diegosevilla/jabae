import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Scanner{
	SymbolTable lexemes;

    public Scanner(){
      lexemes = new SymbolTable();
    }

    public ArrayList<IdEntry> scan(String filename){
        ArrayList<IdEntry> tokens = new ArrayList<IdEntry>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int lineNum = 0;
            while((line = br.readLine()) != null){
                lineNum++;
                if(line.startsWith("//")) continue; //skip comments
                String token = "";
                for(int i = 0 ; i < line.length() ; ){
                    do{
                        token += line.charAt(i++);
                        while(token.equals(" ") && i < line.length()){
                          token = "" + line.charAt(i++);
                        }
                        if(i >= line.length()) break; // if i exceeds the length, process the temporary string formed
                        if(token.startsWith("\"") || token.startsWith("\'")){
                          while(true){
                            char nextChar = line.charAt(i++);
                            token += nextChar;
                            if(nextChar == token.charAt(0))
                              break;
                          }
                          break;
                        }
                        char lookahead = line.charAt(i);
                        if(lookahead == ' ')
                            break;
                        if(lookahead == '.'){
                            token += lookahead;
                            i++;
                            if(i  >= line.length()) break;
                            lookahead = line.charAt(i);
                        }
                        boolean currentMatch = lexemes.containsKey(token) || lexemes.hasMatch(token);
                        if(currentMatch && !lexemes.containsKey(token + lookahead) && !lexemes.hasMatch(token + lookahead))
                            break;
                    }while(true);
                    IdEntry tokenDetails = lexemes.checkToken(token, lineNum);
                    if(tokenDetails != null) {
                      tokens.add(tokenDetails);
                      token = "";
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        IdEntry terminal = new IdEntry("$", 1);
        tokens.add(terminal);
        return tokens;
    }
}
