import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class QuizBruteForcer
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		String userInput;
		ArrayList<Question> questions = new ArrayList<>();

		while(true)
		{
			showMenu();
			do
			{
				userInput = scanner.nextLine().toUpperCase();
            } while (userInput.equals(""));
			switch(userInput)
			{
				case "Q":
					System.out.println("Waiting for question..., E to exit");
					Question question = new Question();
					boolean titleSet = false;
					while(!userInput.equalsIgnoreCase("E"))
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
					break;
				case "A":
					System.out.println(questions.size() + " question(s) in the database. Retrieving list... please wait");
					System.out.println(getQuestionsInText(questions));
					System.out.println(questions.size() + " question(s) printed, " + getTotalAnswers(questions) + " with answers.");
					break;
				case "S":
					System.out.println("Enter question title: ");
					do
					{
						userInput = scanner.nextLine();
					} while (userInput.equals(""));
					Question question2 = new Question();
					question2.setTitle(userInput);
					int result = Jeremy.searchInArrayList(questions, question2);
					if(result == -1)
					{
						System.out.println("Question not found!");
						//try string matching
						ArrayList<String> titles = new ArrayList<>();
						for(Question aQuestion : questions)
						{
							titles.add(aQuestion.title);
						}
						ArrayList<Integer> matches = Jeremy.findSimilarString(titles, question2.title);
						if(matches.size() != 0)
						{
							System.out.println("At least 1 similar question was found!");
							for(Integer aMatch : matches)
							{
								System.out.println(questions.get(aMatch).toString());
							}
						}
					}
					else
					{
						System.out.println("Question found!");
						System.out.println(questions.get(result).toString());
					}
					break;
				case "F":
					@SuppressWarnings("unchecked")
					ArrayList<Question> question3 = (ArrayList<Question>) questions.clone();
					Collections.sort(question3);
					System.out.println(getQuestionsInText(question3));
					System.out.print("Export this to a file? ");
					if(Jeremy.getBoolean())
					{
						System.out.print("Enter file name to save as: ");
						String fileName = scanner.nextLine();
						try (PrintWriter printWriter = new PrintWriter(fileName))
						{
							printWriter.println(question3.size() + " questions logged with " + getTotalAnswers(question3) + " answers");
							printWriter.println(getQuestionsInText(question3).trim());
							printWriter.close();
							System.out.println("Text successfully exported to file \"" + fileName + "\".");
						}
						catch (Exception e)
						{
							System.out.println("A problem was encountered while writing! Error: ");
							Jeremy.pause(1);
							e.printStackTrace();
							Jeremy.pause(1);
						}
					}
					break;
				case "D":
					System.out.println("Enter question title: ");
					do
					{
						userInput = scanner.nextLine();
					} while (userInput.equals(""));
					Question question4 = new Question();
					question4.setTitle(userInput);
					int location = Jeremy.searchInArrayList(questions, question4);
					if(location == -1)
					{
						System.out.println("Question not found");
					}
					else
					{
						System.out.println("Question found!");
						question4 = questions.get(location);
						System.out.println(question4.toString());
						System.out.println("Enter a number to set the question's options, or an invalid value to exit");
						int userNumberInput = Jeremy.getInteger() - 1;
						if(userNumberInput < 0 || userNumberInput >= question4.options.size())
						{
							break;
						}
						else
						{
							System.out.println("Option: ");
							System.out.println(question4.options.get(userNumberInput));
							System.out.println("Set to: 0 = none, 1 = correct, 2 = wrong, 3 = tentative");
							question4.options.get(userNumberInput).setStatus(Jeremy.getInteger());
							System.out.println("Status set. Final result: ");
							System.out.println(question4);
						}
					}
					break;
				case "U":
					for(Question aQuestion : questions)
					{
						if(!aQuestion.hasAnswer())
						{
							System.out.println(aQuestion);
						}
					}
					break;
				case "W":
					for(Question aQuestion : questions)
					{
						if(aQuestion.hasWrong())
						{
							System.out.println(aQuestion);
						}
					}
					break;
				case "I":
					String filename = "QuizBruteForcer.txt";
					boolean read = false;
					while(!read)
					{
						try
						{
							questions.addAll(readFile(filename));
							read = true;
						}
						catch (FileNotFoundException f)
						{
							if(filename.equals("QuizBruteForcer.txt"))
							{
								System.out.println("File not found with the default file name of QuizBruteForcer.txt");
							}
							else
							{
								System.out.println("File not found with the name of " + filename);
							}
							System.out.print("Please enter the new name to load from: ");
							filename = scanner.nextLine();
						}
					}
					break;
				case "O":
					try (PrintWriter printWriter = new PrintWriter("QuizBruteForcer.txt"))
					{
						printWriter.println(questions.size() + " questions logged with " + getTotalAnswers(questions) + " answers");
						printWriter.println(getQuestionsInText(questions).trim());
						printWriter.close();
						System.out.println("Text successfully exported to file \"QuizBruteForcer.txt\".");
					}
					catch (Exception e)
					{
						System.out.println("A problem was encountered while writing! Error: ");
						Jeremy.pause(1);
						e.printStackTrace();
						Jeremy.pause(1);
					}
					break;
				case "E":
					System.out.println("Confirm exit: ");
					if(Jeremy.getBoolean())
					{
						System.out.println(System.getenv());
						System.exit(0);
					}
					break;
				default:
					System.out.println("Unrecognized command. Please try again.");
					break;
			}
		}
	}

	public static void showMenu()
	{
		System.out.println("\"Q\" to add a new question," +
				"\n\"A\" to print all questions," +
				"\n\"S\" to search for a specific question," +
				"\n\"F\" to sort all questions and print them," +
				"\n\"D\" to edit a specific question," +
				"\n\"U\" to find all unanswered questions, " +
				"\n\"W\" to find all questions with at least one wrong answer, " +
				"\n\"O\" to export text to file," +
				"\n\"I\" to import text from file," +
				"\n\"E\" to exit.");
	}

	public static String getQuestionsInText(ArrayList<Question> questions)
	{
		String result = "";
		for(int i = 1; i <= questions.size(); i++)
		{
			result = result + questions.get(i - 1).toString();
			result = result + "\n";
		}
		return result;
	}

	public static int getTotalAnswers(ArrayList<Question> questions)
	{
		int totalAnswers = 0;
		for(Question aQuestion : questions)
		{
			if(aQuestion.hasAnswer())
			{
				totalAnswers++;
			}
		}
		return totalAnswers;
	}

	public static ArrayList<Question> readFile(String filename) throws FileNotFoundException
	{
		ArrayList<Question> questions = new ArrayList<>();
		String fileInput;
		try (Scanner file = new Scanner(new File(filename)))
		{
			System.out.println("Beginning to read file: " + filename);
			boolean firstLine = true;
			int totalQuestions = 0, questionsEncountered = 0;
			while(file.hasNextLine())
			{
				if(firstLine)
				{
					totalQuestions = Integer.parseInt(file.nextLine().split(" ")[0]);
					firstLine = false;
				}
				else
				{
					//read the question.
					Question question1 = new Question();
					question1.setTitle(file.nextLine());
					while(file.hasNextLine())
					{
						fileInput = file.nextLine();
						if(fileInput.equals(""))
						{
							break;
						}
						else
						{
							question1.addOption(fileInput);
						}
					}
					questions.add(question1);
					questionsEncountered++;
				}
			}
			System.out.println("File noted that it had " + totalQuestions + " question(s), "
					+ questionsEncountered + " question(s) were found.");
		}
		catch (FileNotFoundException f)
		{
			throw f;
		}
		catch (Exception e)
		{
			System.out.println("An error occurred! Details:");
			Jeremy.pause(1);
			e.printStackTrace();
			Jeremy.pause(1);
		}
		return questions;
	}
}
