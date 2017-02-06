import java.io.BufferedReader;
import java.io.FileReader;

public class Scanner{
    public static void main(String args[]){
        try{
            //check ba muna kung tamang file type yung ioopen? kunwari check muna kung ".java" siya ganon ganon?
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String line;
            while((line = br.readLine()) != null){
                System.out.println(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
