
public class IfNode extends ASTNode{
	ASTNode cond;
	ASTNode body;
	ASTNode elseBody;
	
	public IfNode(String ty, String tok) {
		this.type = ty;
		this.token = tok;
	}
}
