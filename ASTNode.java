import java.util.ArrayList;

public class ASTNode {
	String type;
	String token;
	String value;

	ArrayList<ASTNode> bodyChildren;
	
	public ASTNode(String ty, String to) {
		type = ty;
		token = to;
		if(ty.equals("branch") ||
		  ty.equals("program") ||
		  ty.equals("condition")) {
			bodyChildren = new ArrayList<ASTNode> ();
		}
	}

}
