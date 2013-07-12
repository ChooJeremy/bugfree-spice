public class InvalidCardException extends RuntimeException
{
	public InvalidCardException() {super();}
	public InvalidCardException(String message)
	{
		super(message);
	}
	public InvalidCardException(Throwable cause)
	{
		super(cause);
	}
	public InvalidCardException(String message, Throwable cause)
	{
		super(message, cause);
	}


	@Override
	public String toString()
	{
		return super.toString();
	}
}
