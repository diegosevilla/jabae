import java.util.ArrayList;

public class TAC
{
	static int tempcount = 0;

	public static String createTemp()
	{
		return "temp" + tempcount++;
	}

	public static void print(String arg)
	{
		System.out.println("param: " + arg);
		System.out.println("call Sho'me");
	}

	public static void read(String arg)
	{
		System.out.println("param: " + arg);
		System.out.println("call Gimme");
	}

	public static void assignment(String dest, String val)
	{
		System.out.println(dest + " = " + val);
	}

	public static void expr(String dest, String op, String op1, String op2)
	{
		System.out.println(dest + " = " + op1 + " " + op + " " + op2);
	}

	public static String generate(ASTNode node)
	{
		if(node.type.equals("Literal") || node.type.equals("Identifier"))
			return node.token;
		
		switch(node.type)
		{
			case "assignment": 
			{
				assignment(node.bodyChildren.get(0).token, generate(node.bodyChildren.get(1)));
				return "";
			}
			case "exprop":
			{
				String temp = createTemp();
				expr(temp, node.token , generate(node.bodyChildren.get(0)), generate(node.bodyChildren.get(1)));
				return temp;
			}
		}

		for(ASTNode child : node.bodyChildren)
		{
			generate(child);
		}
		return "";
	}

}