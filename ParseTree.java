import java.util.ArrayList;
import java.util.Iterator;

public class ParseTree implements Iterable<ParseTree> {
	String symbol;
	ParseTree parent = null;
	ArrayList<ParseTree> children;
	Boolean isTerminal = false;
	
	public ParseTree(String token, ArrayList<ParseTree> children){
		this.symbol = token;
		this.children = children;
	}
	
	public ParseTree(String token, ParseTree parent){
		this.symbol = token;
		this.children = new ArrayList<ParseTree>();
		this.parent = parent;
	}
	
	public ParseTree(String token){
		this(token, new ArrayList<ParseTree>());
	}
	
	public void addChildren(String[] children) {		
		for(String pr : children){
			ParseTree child = new ParseTree(pr.trim(), this);
			System.out.println("Child: " + child.symbol + " of Parent: " + child.parent.symbol);
			this.children.add(child);
		}
		System.out.println("Parent: " + this.symbol + " has " + this.children.size() + " children");
	}
	
	public void toggleTerminal(){
		this.isTerminal = true;
	}
	
	public static ParseTree getRoot(ParseTree curr){
		if(curr.parent != null){
			return ParseTree.getRoot(curr.parent);
		}
		return curr;
	}
	
	public static ParseTree findCurrent(String symbol, ParseTree curr){
		if(curr.symbol.equals(symbol)){
			return curr;
		} else {
			for(ParseTree cd : curr.children){
				curr = ParseTree.findCurrent(symbol, cd);
				if(curr != null && curr.symbol.equals(symbol)) return curr;
			}
		}
		
		return null;
	}
//	
//	public static void toStringAll(ParseTree root) {
//		System.out.println(root.symbol);
//		System.out.print("-> ");
//		for
//	}
	@Override
	public Iterator<ParseTree> iterator() {
		// TODO Auto-generated method stub
		return children.iterator();
	}
}
