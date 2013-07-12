public class InvalidCardNumberException extends InvalidCardException
{
	private int number;

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

	public int getNumber() {return number; }

	@Override
	public String toString()
	{
		return super.toString() + "\nNumber: " + number;
	}
}
