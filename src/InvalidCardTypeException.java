public class InvalidCardTypeException extends InvalidCardException
{
	int type;

	public InvalidCardTypeException(int t) {super(); type = t;}
	public InvalidCardTypeException(int t, String message)
	{
		super(message);
		type = t;
	}
	public InvalidCardTypeException(int t, Throwable cause)
	{
		super(cause);
		type = t;
	}
	public InvalidCardTypeException(int t, String message, Throwable cause)
	{
		super(message, cause);
		type = t;
	}

	public int getType() {return type; }

	@Override
	public String toString()
	{
		return super.toString() + "\nType: " + type;
	}
}
