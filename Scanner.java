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
                for(int i = 0 ; i < line.length() ; i++){
                    if(line.charAt(i) == ' '){ //if space is encountered, check if the token built generates a keyword or a literal
                       while(i < line.length() && line.charAt(i) == ' ')  i++;
                       if(!token.equals("") && lexemes.checkToken(token, lineNum)){ //if it matches a keyword or literal, print the details and reset token
                            token = "";
                       } else iflexemes.checkLongestMatch(token, lineNum)) //else check if token is made up of different lexemes not separated by spaces
                            token = "";
                    }
                    if((line.charAt(i) == '"') || (line.charAt(i) == '\'' && line.charAt(i+2) == '\'')){ //if char or string literal
                    	//Added quote sign to differentiate string literals
                       char ide = line.charAt(i);
                       token += ide;
                       for(i++ ; line.charAt(i) != ide; i++){ //get all characters inside the delimiter (" or ')
                          token+= line.charAt(i);
                       }
                       token += ide;
                    } else 
                       token += line.charAt(i);
                }
                if(!token.equals("") && lexemes.checkToken(token, lineNum)) { //check if whole word matches a keyword or a literal
                    token = "";
                } else if(lexemes.checkLongestMatch(token, lineNum)){ // else check if made up of different lexemes not separated by spaces
                    token = "";
                }                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


