import java.io.PrintWriter;
import java.util.*;

public class QuizFileCreator
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		ArrayList<Question> questions = new ArrayList<>();
		String userInput;

		System.out.println("Accepting input now. Please enter \"e\" to end a question and \"E\" to end everything");
		do
		{
			Question question = new Question();
			boolean titleSet = false;
			userInput = "";
			while(!userInput.equalsIgnoreCase("e"))
			{
				userInput = scanner.nextLine();
				if(!userInput.trim().equals("") && !userInput.equals("\t\t") && !userInput.equalsIgnoreCase("E"))
				{
					if(!userInput.trim().equalsIgnoreCase("answer") && !userInput.trim().equalsIgnoreCase("Select the most appropriate fact-finding technique."))
					{
						if(!titleSet)
						{
							question.setTitle(userInput);
							titleSet = true;
						}
						else
						{
							question.addOption(userInput);
						}
					}
				}
			}
			if(Jeremy.searchInArrayList(questions, question) == -1)
			{
				System.out.println("Question recorded: \n" + question.toString());
				questions.add(question);
			}
			else
			{
				boolean match = false;
				for(Question aQuestion : questions)
				{
					if(aQuestion.equals(question) && aQuestion.answerCheck(question))
					{
						System.out.println("Question already recorded, you fool!");
						System.out.println("Answer status for that question: " + aQuestion.hasAnswer());
						match = true;
					}
				}
				if(!match)
				{
					System.out.println("Question recorded: \n" + question.toString());
					questions.add(question);
				}
			}
		} while (!userInput.equals("E"));


		System.out.print("Enter file name to save as: ");
		//consume all further newlines
		String filename;
		do
		{
			filename = scanner.nextLine();
		} while (filename.trim().equals(""));
		try (PrintWriter printWriter = new PrintWriter(filename))
		{
			printWriter.println(questions.size() + " questions logged with " + QuizBruteForcer.getTotalAnswers(questions) + " answers");
			printWriter.println(QuizBruteForcer.getQuestionsInText(questions).trim());
			printWriter.close();
			System.out.println("Text successfully exported to file " + filename + ".");
		}
		catch (Exception e)
		{
			System.out.println("A problem was encountered while writing! Error: ");
			Jeremy.pause(1);
			e.printStackTrace();
			Jeremy.pause(1);
		}
	}
}
