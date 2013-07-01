import java.util.*;

public class MappingConverter
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		ArrayList<String> maps = new ArrayList<String>();
		System.out.println("Enter exit to exit:");
		String userInput, result = "";
		do {
			userInput = scanner.nextLine();
			if(!userInput.equals(""))
			{
				maps.add(userInput);
			}
		} while(!userInput.toLowerCase().equals("exit"));
		maps.remove(maps.size() - 1);
		//Remove spaces
		for(int i = maps.size() - 1; i >= 0; i--)
		{
			maps.add(maps.get(i).replaceAll("\\s", ""));
			maps.remove(i);
		}
		String[] mappingData = maps.toString().substring(1, maps.toString().length() - 1).replaceAll("\\{", "}").split("}");
		String[] attributes;
		boolean isForeign;
		for(int i = 0; i < mappingData.length; i+=2)
		{
			if(i == 0)
			{
				mappingData[i] = ", " + mappingData[i];
			}
			result += mappingData[i].substring(2) + " table:\n";
			attributes = mappingData[i + 1].split(",");
			result += "Attribute\tDescription\tData Type\tConstraint\tNull Value\n";
			System.out.println("For the table " + mappingData[i].substring(2) + ",");
			for(String anAttribute : attributes)
			{
				isForeign = false;
				if(anAttribute.charAt(0) == '<')
				{
					isForeign = true;
					anAttribute = anAttribute.substring(1);
					anAttribute = anAttribute.substring(0, anAttribute.length() - 1);
				}
				result += anAttribute + "\t";
				System.out.println("\tFor the attribute " + anAttribute + ",");
				System.out.println("\t\tDescription?");
				System.out.print("\t\t");
				result += scanner.nextLine() + "\t";
				System.out.println("\t\tData Type?");
				System.out.print("\t\t");
				result += scanner.nextLine() + "\t";
				if(isForeign)
				{
					System.out.println("\t\tWhat is the foreign table this attribute is in?");
					System.out.print("\t\t");
					result += "Foreign Key -> " + scanner.nextLine() + " (" + anAttribute + ") ";
				}
				System.out.println("\t\tConstraint?");
				System.out.println("\t\t\tReference: \"Values: a / b\", \"Must be X\", \"Cannot be X\"");
				System.out.print("\t\t");
				result += scanner.nextLine() + "\t";
				System.out.println("\t\tNull value?");
				System.out.print("\t\t");
				result += scanner.nextLine() + "\n";
			}
		}
		System.out.println("Done! Please copy and paste the following into a word document and convert the appropriate text to a table");
		System.out.println(result);
	}
}
