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
			System.out.println("Child of " + this.symbol + " : " + child.symbol);
			//Make terminal if it is epsilon
			if(child.symbol.equals("Epsilon")) child.toggleTerminal();
			this.children.add(child);
		}
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
	
	public static ParseTree goToParent(ParseTree curr, String next){
		if(curr.parent == null) return curr;
		if(curr.children.size() == 0){
			return ParseTree.goToParent(curr.parent, next);
		} else {
			for(ParseTree cd : curr.children){
				if(cd.symbol.equals(next)) return curr;
			}
			return ParseTree.goToParent(curr.parent, next);
		}
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
	
	public static void toString(ParseTree root, String space, Boolean isChild) {
		//DFS Printing of ParseTree
		System.out.println(space + (isChild? "-> ":"") + root.symbol + (root.isTerminal?" (Terminal)":""));
		for(int i = (root.children.size()-1); i >= 0; i--){
			ParseTree.toString(root.children.get(i), space+"    ", true);
		}
	}
	@Override
	public Iterator<ParseTree> iterator() {
		// TODO Auto-generated method stub
		return children.iterator();
	}
}
