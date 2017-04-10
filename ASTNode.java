import java.util.ArrayList;

public class ASTNode {
	String type;
	String token;
	ArrayList<ASTNode> bodyChildren;
	
	public ASTNode(String ty, String to) {
		type = ty;
		token = to;
		bodyChildren = new ArrayList<ASTNode> ();
	}
	
	public void printString(String space, Boolean isChild) {
		//DFS Printing of ParseTree
		System.out.println(space + (isChild? "-> ":"") + 
				((this.type.equals("id") || this.type.equals("lit"))? this.token + " (Terminal)": this.token));
		if(this.bodyChildren.size() != 0) {
			for(int i = 0; i < this.bodyChildren.size(); i++){
				this.bodyChildren.get(i).printString(space+"    ", true);
			}
		}
	}
}
