import java.util.*;


public class Answer implements Comparable<Answer>
{
	public String title;
	public int status;
	public static final int CORRECT = 1;
	public static final int WRONG = 2;
	public static final int TENTATIVE = 3;
	public static final int UNKNOWN = 4;

	public Answer(String theTitle)
	{
		title = theTitle;
		status = 0;
	}

	public void setStatus(int type)
	{
		status = type;
	}

	@Override
	public String toString()
	{
		String result = "    ";
		if(status == 0)
		{
			//do nothing, it's already set correctly
		}
		else if(status == CORRECT)
		{
			result = " -> ";
		}
		else if(status == WRONG)
		{
			result = " xx ";
		}
		else if(status == TENTATIVE)
		{
			result = " -? ";
		}
		else if(status == UNKNOWN)
		{
			result = " ?? ";
		}
		else
		{
			System.out.println("Incorrect status: " + status);
		}
		return result + title;
	}

	public Answer merge(Answer a)
	{
		if(title.equals(a.title))
		{
			Answer answer = new Answer(title);
			if(status == a.status)
			{
				answer.setStatus(status);
			}
			else if(status == 0)
			{
				answer.setStatus(a.status);
			}
			else if(a.status == 0)
			{
				answer.setStatus(status);
			}
			else if(status == TENTATIVE)
			{
				answer.setStatus(a.status);
			}
			else if(a.status == TENTATIVE)
			{
				answer.setStatus(status);
			}
			else
			{
				answer.setStatus(UNKNOWN);
			}
			return answer;
		}
		else
		{
			throw new RuntimeException("InvalidAnswerMatchException: The two answers given do not match." +
					"\nThis: " + this.toString() + "\nAnswer to combine with: " + a.toString());
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Answer)
		{
			return title.equals(((Answer) o).title);
		}
		else
		{
			return false;
		}
	}

	@Override
	public int compareTo(Answer o)
	{
		return title.compareTo(o.title);
	}
}
