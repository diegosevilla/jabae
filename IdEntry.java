
public class IdEntry {
	String name;
	String token;
	String dataType;
	int blockLevel;
	String scope;
	int offset;
	
	public IdEntry(String name, String dataType, int blockLevel, String scope, int offset) {
		this.name = name;
		this.dataType = dataType;
		this.blockLevel = blockLevel;
		this.scope = scope;
		this.offset = offset;
	}

	public IdEntry(String name, int blockLevel) {
		this.name = name;
		this.blockLevel = blockLevel;
	}

	public IdEntry(String name, String token) {
		this.name = name;
		this.token = token;
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
