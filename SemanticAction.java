public class SemanticAction
{
	public static void checkdec(String type, IdEntry token)
	{
		IdEntry entry = SymbolTable.idLookup(token.token, 0);
		if(type != null)
		{
			if(entry == null)
			{
				entry = SymbolTable.install(token.token, 0, type);
			}
			else
			{
				Error.addError(token.linenum, token.token + " already declared.");
			}
		}
		else
		{
			if(entry == null)
			{
				Error.addError(token.linenum, token.token + " not found.");
			}
		}
	}

	public static String checkMatch(IdEntry opL, String op, IdEntry opR)
	{
		if(opR != null)
		{
			
			System.out.println(opL.name + " of data type " + opL.dataType);
			System.out.println(opR.name + " of data type " + opR.dataType);

			if(opL.dataType.equals(opR.dataType))
				return op;
			else
				Error.addError(opL.linenum, "Invalid operation " + op);
		}
		else
		{
			Error.addError(opL.linenum, "Invalid operation " + op);
		}
		return null;
	}
}