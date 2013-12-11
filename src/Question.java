import java.util.*;

public class Question implements Comparable<Question>
{
	public static int totalQuestion = 0;
	public int id;
	public String title;
	public ArrayList<Answer> options;

	public Question()
	{
		id = totalQuestion;
		totalQuestion++;
		options = new ArrayList<>();
		title = "";
	}

	public boolean hasAnswer()
	{
		for(Answer anOption : options)
		{
			if(anOption.status == Answer.CORRECT)
			{
				return true;
			}
		}
		return false;
	}

	public boolean hasWrong()
	{
		for(Answer anOption : options)
		{
			if(anOption.status == Answer.WRONG)
			{
				return true;
			}
		}
		return false;
	}

	public void setTitle(String newTitle)
	{
		title = newTitle;
	}

	public void addOption(String newOption)
	{
		if(newOption.length() < 4)
		{
			options.add(new Answer(newOption));
		}
		//read status properly, is it recorded as an answer?
		else if(newOption.substring(0, 4).equals(" -> "))
		{
			options.add(new Answer(newOption.substring(4)));
			options.get(options.size() - 1).setStatus(Answer.CORRECT);
		}
		else if(newOption.substring(0, 4).equals(" xx "))
		{
			options.add(new Answer(newOption.substring(4)));
			options.get(options.size() - 1).setStatus(Answer.WRONG);
		}
		else if(newOption.substring(0, 4).equals(" -? "))
		{
			options.add(new Answer(newOption.substring(4)));
			options.get(options.size() - 1).setStatus(Answer.TENTATIVE);
		}
		else if(newOption.substring(0, 4).equals(" ?? "))
		{
			options.add(new Answer(newOption.substring(4)));
			options.get(options.size() - 1).setStatus(Answer.UNKNOWN);
		}
		else if(newOption.substring(0, 4).equals("    "))
		{
			options.add(new Answer(newOption.substring(4)));
		}
		else
		{
			options.add(new Answer(newOption.trim()));
		}
	}

	public int totalOptions()
	{
		return options.size();
	}

	public void setAnswerType(int number, int status)
	{
		options.get(number).setStatus(status);
	}

	public boolean hasSameOptions(Question q)
	{
		for(int i = 0; i < options.size(); i++)
		{
			if(!options.get(i).equals(q.options.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	public Question merge(Question q)
	{
		if(title.equals(q.title) && options.size() == q.options.size())
		{
			boolean sameOptions = true;
			Question question = new Question();
			question.setTitle(title);
			if(!hasSameOptions(q))
			{
				Collections.sort(options);
				Collections.sort(q.options);
				sameOptions = false;
			}
			for(int i = 0; i < options.size(); i++)
			{
				question.options.add(options.get(i).merge(q.options.get(i)));
			}
			if(!sameOptions)
			{
				Jeremy.randomize(question.options);
			}
			return question;
		}
		else
		{
			throw new RuntimeException("InvalidQuestionMatchException: The two questions given do not match." +
					"\nThis: " + this.toString() + "\nQuestion to combine with: " + q.toString());
		}
	}

	@Override
	public String toString()
	{
		String result = title + "\n";
		for(Answer anOption : options)
		{
			result = result + anOption.toString() + "\n";
		}
		return result;
	}

	@Override
	public int compareTo(Question o)
	{
		return title.compareTo(o.title);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Question)
		{
			if(title.equals(((Question) o).title))
			{
				return true;
			}
		}
		return false;
	}

	//It's ok cause it's just casting from a clone
	@SuppressWarnings("unchecked")
	public boolean answerCheck(Question q)
	{
		ArrayList<Answer> thisOptions = (ArrayList<Answer>) options.clone();
		ArrayList<Answer> otherOptions = (ArrayList<Answer>) q.options.clone();
		Collections.sort(thisOptions);
		Collections.sort(otherOptions);
		if(thisOptions.size() != otherOptions.size())
		{
			return false;
		}
		for(int i = 0; i < thisOptions.size(); i++)
		{
			if(!thisOptions.get(i).equals(otherOptions.get(i)))
			{
				return false;
			}
		}
		return true;
	}
}
