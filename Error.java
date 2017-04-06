import java.util.ArrayList;

public class Error
{
	static ArrayList<ErrorDetails> errors = new ArrayList<ErrorDetails>();

	public static void addError(int linenum, String details)
	{
		errors.add(new ErrorDetails(linenum, details));
	}

	public static void printErrors()
	{
		System.out.println(errors.size() + " error/s found!");
		for(ErrorDetails e : errors)
		{
			System.out.println("Error at line " + e.linenum + " : " + e.details);
		}
	}

	public static class ErrorDetails
	{
		int linenum;
		String details;

		public ErrorDetails(int linenum, String details)
		{
			this.linenum = linenum;
			this.details = details;
		}
	}
}