import java.util.ArrayList;

public class Scanner{
	LexDic lexemes;
	ArrayList<String> tokens;
	
	public Scanner(ArrayList<String> lines) {
		//initialize lexemes
		lexemes = new LexDic();
		tokens = new ArrayList<String>();
		
		for(int lineNum=0; lineNum<lines.size(); lineNum++){
			String line = lines.get(lineNum);
			if(line.startsWith("//")) continue; //skip comments
			scanTokens(line, lineNum);
		}
	}
	
	public void scanTokens(String line, int lineNum) {
		String token = "";
		
		//Scan line for tokens
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
			
			//If token is accepted, add to tokens array and reset token string
			if(lexemes.checkToken(token, lineNum)) //reset if valid
				//[Q] Token lang at lineNum kailangan diba? di na yung token tag at type?
				tokens.add(lineNum + ", " + token);
				token = "";
		}
	}
}
