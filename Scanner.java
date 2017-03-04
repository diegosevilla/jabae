import java.io.BufferedReader;
import java.io.FileReader;

public class Scanner{
    public static void main(String args[]){
//        System.out.println(typeOf('a'));
        //initialize lexemes
        LexDic lexemes = new LexDic();
        try{
            //check ba muna kung tamang file type yung ioopen? kunwari check muna kung ".java" siya ganon ganon?
            if(!args[0].endsWith(".jbe")) {
                System.out.println("Invalid file type");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
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
                            if( i >= line.length()) break;
                            lookahead = line.charAt(i);
                        }
                        boolean currentMatch = lexemes.containsKey(token) || lexemes.hasMatch(token);
                        if(currentMatch && !lexemes.containsKey(token + lookahead) && !lexemes.hasMatch(token + lookahead))
                            break;
                    }while(true);
                    if(lexemes.checkToken(token, lineNum)) //reset if valid
		                  token = "";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
