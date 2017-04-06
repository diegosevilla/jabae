
public class IdEntry {
	String name;
	String token;
	String dataType;
	int blockLevel;
	String scope;
	int offset;
	int linenum;
	
	public IdEntry(String name, String dataType, int blockLevel) {
		this.name = name;
		this.dataType = dataType;
		this.blockLevel = blockLevel;
	}

	public IdEntry(String name, int blockLevel) {
		this.name = name;
		this.blockLevel = blockLevel;
	}

	public IdEntry(String name, int linenum, String token) {
		this.name = name;
		this.token = token;
		this.linenum = linenum;
	}

	public IdEntry(String name, String dataType, String token) {
		this.name = name;
		this.token = token;
		this.dataType = dataType;
	}
	
	public String toString(){
		String print = "";
			if(name.equals("id"))
				print = token;
			else
				print = name;
		return print + " blockLevel: " + blockLevel; 
	}
	
	
	
	
	
}
