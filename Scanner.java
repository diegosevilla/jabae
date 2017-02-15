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
                    if(line.charAt(i) == ' '){
                       lexemes.printDetails(token, lineNum);
                       while(line.charAt(i) == ' ')  i++;
                       token = "";
                    }
                    if((line.charAt(i) == '"') || (line.charAt(i) == '\'' && line.charAt(i+2) == '\'')){
                    	//Added quote sign to differentiate string literals
                       char ide = line.charAt(i);
                       token += ide;
                       for(i++ ; line.charAt(i) != ide; i++){
                          token+= line.charAt(i);
                       }
                       token += ide;
                    } else 
                       token += line.charAt(i);
                    if(i+1 < line.length() && line.charAt(i+1) != ' ' && lexemes.printDetails(token+line.charAt(i+1), lineNum)){
                        i++;
                        token = "";
                    } else if(lexemes.printDetails(token, lineNum)){
                        token = "";
                    }          
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


