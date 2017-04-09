
public class OpNode extends ASTNode{
	ASTNode left;
	ASTNode right;
	
	public OpNode(String ty, String tok) {
		this.type = ty;
		this.token = tok;
	}
	
	public void setOp(String op) {
		this.token = op;
	}
}
