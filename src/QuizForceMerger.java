import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class QuizForceMerger
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		ArrayList<Question> file1 = new ArrayList<>();
		String filename = "QuizBruteForcer.txt";
		boolean read = false;
		while(!read)
		{
			try
			{
				file1.addAll(QuizBruteForcer.readFile(filename));
				read = true;
			}
			catch (FileNotFoundException f)
			{
				if(filename.equals("QuizBruteForcer.txt"))
				{
					System.out.println("First file not found with the default file name of QuizBruteForcer.txt");
				}
				else
				{
					System.out.println("First file not found with the name of " + filename);
				}
				System.out.print("Please enter the new name to load from: ");
				filename = scanner.nextLine();
			}
		}
		ArrayList<Question> file2 = new ArrayList<>();
		filename = "QuizBruteForcer2.txt";
		read = false;
		while(!read)
		{
			try
			{
				file2.addAll(QuizBruteForcer.readFile(filename));
				read = true;
			}
			catch (FileNotFoundException f)
			{
				if(filename.equals("QuizBruteForcer2.txt"))
				{
					System.out.println("Second file not found with the default file name of QuizBruteForcer2.txt");
				}
				else
				{
					System.out.println("Second file not found with the name of " + filename);
				}
				System.out.print("Please enter the new name to load from: ");
				filename = scanner.nextLine();
			}
		}
		System.out.println("Searching for similar questions and combining them...");
		ArrayList<Question> questions = new ArrayList<>();
		int count = 0;
		//Merge file1 and file2 here, placing the result into questions
		for(int i = file1.size() - 1; i >= 0; i--)
		{
			for(int j = file2.size() - 1; j >= 0; j--)
			{
				if(file1.get(i).equals(file2.get(j)))
				{
					if(file1.get(i).answerCheck(file2.get(j)))
					{
						questions.add(file1.get(i).merge(file2.get(j)));
						file1.remove(i);
						file2.remove(j);
						count++;
						break;
					}
				}
			}
		}
		System.out.println("Adding all unique questions...");
		questions.addAll(file1);
		questions.addAll(file2);
		System.out.println("Finishing up...");
		Jeremy.randomize(questions);
		System.out.println(count + " identical questions found and combined, along with " + (file1.size() + file2.size()) + " unique questions.");
		System.out.print("Please enter new file name to save as: ");
		try (PrintWriter printWriter = new PrintWriter(scanner.nextLine()))
		{
			printWriter.println(questions.size() + " questions logged with " + QuizBruteForcer.getTotalAnswers(questions) + " answers");
			printWriter.println(QuizBruteForcer.getQuestionsInText(questions).trim());
			printWriter.close();
			System.out.println("Text successfully exported to file.");
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
