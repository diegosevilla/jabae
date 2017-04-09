import java.util.ArrayList;

public class BodyNode extends ASTNode{
	ArrayList<ASTNode> bodyChildren;

	public BodyNode(String ty, String tok) {
		this.type = ty;
		this.token = tok;
	}
	
//	public void printString(String space) {
//		System.out.println(token);
//		foreach(ASTNode a)
//	}
}
