public class InvalidCardNumberException extends RuntimeException
{
	int number;

	public InvalidCardNumberException(int num) {super(); number = num;}
	public InvalidCardNumberException(int num, String message)
	{
		super(message);
		number = num;
	}
	public InvalidCardNumberException(int num, Throwable cause)
	{
		super(cause);
		number = num;
	}
	public InvalidCardNumberException(int num, String message, Throwable cause)
	{
		super(message, cause);
		number = num;
	}

	@Override
	public String toString()
	{
		return super.toString() + "\nNumber: " + number;
	}
}
