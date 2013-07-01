//Calculator Class to do some calculation.

public abstract class Calculator
{
	//Special symbol to represent implicit multiplication precedence
	public static final char implicitMultiplication = 'å';
	//Used for brackets (i.e. 5(2)) will become 5å2. Basically, this symbol has a lower priority then the expoential symbol,
	//but a higher priority then multiplication or division.

	public static String validateString(String target)
	{
		//Firstly, check if the string is empty.
		if (target.length() == 0)
		{
			return "";
		}
		char[] targetArr = target.toCharArray();
		Boolean hasDecimal = false;
		String response;
		int previousSymbolLocation = -1;
		int previousBracketLocation = -1;
		//Brackets is used for brackets. When the method finds an open bracket '(', Brackets++.
		//When it finds a closed bracket ')', Brackets--;
		//then, it ensures that the string does not close when there has been no open brackets, and it does not end while
		//brackets is more than 1.
		int brackets = 0;

		for(int i = 0; i < targetArr.length; i++)
		{
			//If the character is not a number, this will evaluate to TRUE
			if((targetArr[i] < '0' || targetArr[i] > '9') && (targetArr[i] != '.' || hasDecimal))
			{
				//This is exceuted if the item is not a number OR a symbol
				if((targetArr[i] != '-' && targetArr[i] != '/' && targetArr[i] != '*' && targetArr[i] != '+' && targetArr[i] != '^' && targetArr[i] != ')' && targetArr[i] != '(') || targetArr[i] == '.')
				{
					//This is exceuted if the error has multiple decimal points
					if(targetArr[i] == '.')
					{
						response = "An error occurred! " + target + " has multiple decimal points, found on position " + (i + 1) + "!";
						return response;
					}
					else
					{
						response = "An error occurred! " + target + " has an unrecongnized symbol, found on position " + (i + 1) + "!";
						return response;
					}
				}
				else
				{
					//ensure there is no two symbols in a row
					if (previousSymbolLocation != -1)
					{
						if (previousSymbolLocation == i - 1)
						{
							//unless one of those symbol is a bracket
							if (!(targetArr[i] == ')' || targetArr[i] == '(' || targetArr[i - 1] == ')' || targetArr[i - 1] == '('))
							{
								//and unless the second symbol is a - sign
								if (targetArr[i] != '-')
								{
									response = "An error occurred! " + target + " has two mathematical symbols located beside each other, found on position " + (i + 1) + "!";
									return response;
								}
							}
							//Additionally, if it's 50(*2), it should complain. So:
							else if (targetArr[i - 1] == '(' && (targetArr[i] == '*' || targetArr[i] == '^' || targetArr[i] == '/'))
							{
								response = "An error occurred! " + target + " has a mathematical symbol after a bracket, located at position " + (i - 1) + "!";
								return response;
							}
						}
					}
					else
					{
						//ensure that the equation cannot start with a symbol
						if (i == 0)
						{
							//unless it's a bracket
							if (targetArr[i] != '(')
							{
								//or a sign
								if (targetArr[i] != '-' && targetArr[i] != '+')
								{
									response = "An error occurred! " + target + " begins with a symbol!";
									return response;
								}
							}
						}
					}
					//ensure it cannot end with a symbol
					if (i == target.length() - 1)
					{
						//unless it's a bracket
						if (targetArr[i] != ')')
						{
							response = "An error occurred! " + target + " ends with a symbol!";
							return response;
						}
					}
					//Brackets check ()
					if (targetArr[i] == '(')
					{
						brackets++;
						previousBracketLocation = i;
					}
					else if (targetArr[i] == ')')
					{
						if (brackets <= 0)
						{
							response = "An error occurred! " + target + " has a closing bracket at position " + i + ", but there's no open bracket before that!";
							return response;
						}
						//ensure it doesn't open and then close immediately.
						else
						{
							if (previousBracketLocation == i - 1)
							{
								response = "An error occurred! " + target + " has an open bracket at position " + (i - 1) + ", which is then closed immediately!";
								return response;
							}
							//Ensure that it doesn't have a symbol then a close bracket.
							else if (previousSymbolLocation == i - 1)
							{
								//unless the symbol is a close bracket as well
								if (targetArr[i - 1] != ')')
								{
									response = "An error occurred! " + target + " has a mathematical symbol at position " + (i - 1) + ", after which there is an immediate closing bracket!";
									return response;
								}
							}
						}
						//ensure it doesn't have a number after that - 5(1)3
						//ensure the app doesn't crash with an ArrayOutOfBoundsException (It can end with a bracket)
						if (targetArr.length - 1 != i)
						{
							if (targetArr[i + 1] >= '0' && targetArr[i + 1] <= '9')
							{
								response = "An error occurred! " + target + " has a closing bracket, then a number right after that at position " + (i + 1)  + "!";
								return response;
							}
						}
						brackets--;
					}
					previousSymbolLocation = i;
					//now there can be a decmial again
					hasDecimal = false;
				}
			}
			else if (targetArr[i] == '.')
			{
				hasDecimal = true;
			}
		}
		//Brackets check
		if (brackets > 0)
		{
			response = "An error occurred! " + target + " has an open bracket that was not closed!";
			return response;
		}
		return "OK";
	}

	//Optional parameter for calculate: useImplicitPriority. if undefined, defaults to true
	public static double calculate(String target) {return calculate(target, true); }

	public static double calculate(String target, boolean useImplictPriority)
	{
		double result = 0;
		//array that stores all the numbers where a +-*/ occurs. To ensure no crashes, the array's length is equal to target's length
		int[] breaks = new int[target.length()];
		int count = 0;
		//We also need to store whether the symbol is a priority or not
		//We need to calculate certain symbols first so the symbol's priority has to be stored
		//+ -                       = priority 0
		//* /                       = priority 1
		//implicitMultiplication     = priority 2
		//^                         = priority 3
		int[] priority = new int[target.length()];
		//initalize as all lowest priority
		for(int i = 0; i < priority.length; i++)
		{
			priority[i] = 0;
		}
		//array used to store the previous position of the math symbol for subString
		//It is also used later for calculation to store the position where the number should go in Numbers2
		int previousPosition = 0;

		//Turn it to a character array
		char[] targetArr = target.toCharArray();

		//First off, brackets have to be handled. Additionally, targetArr would have to change everytime a bracket is found.
		//Now, there might be multiple opens and close, for example, 3+(3+(3+3)). Therefore, if we just find the next closing
		//bracket, that won't work. We need to know that there has been x opening bracket, skip the next x closing bracket.
		int brackets = 0;
		int firstBracketLocation = -1;
		String targetFirst;
		String targetSecond;
		double bracketResult;
		for (int i = 0; i < targetArr.length; i++)
		{
			if (targetArr[i] == '(')
			{
				if (firstBracketLocation == -1)
				{
					firstBracketLocation = i;
				}
				else
				{
					brackets++;
				}
				//If this is not the first
				if(i != 0)
				{
					//if it sees a bracket like 50(1), it will say 501. To make it correct, we need to insert a multiply symbol
					//between 0 and (
					if (targetArr[i - 1] != '*' && targetArr[i - 1] != '/' && targetArr[i - 1] != '+' && targetArr[i - 1] != '-' && targetArr[i - 1] != '^' && targetArr[i - 1] != '(' && targetArr[i - 1] != implicitMultiplication)
					{
						targetFirst = target.substring(0, i);
						targetSecond = target.substring(i);
						if (useImplictPriority)
						{
							target = targetFirst + implicitMultiplication + targetSecond;
						}
						else
						{       //If not specified, use "*" for the old conversion.
							target = targetFirst + "*" + targetSecond;
						}
						targetArr = target.toCharArray();
						//AND DO IT ALL OVER AGAIN!
						i = -1;
						firstBracketLocation = -1;
						brackets = 0;
					}
				}
			}
			else if (targetArr[i] == ')')
			{
				if (brackets > 0)
				{
					brackets--;
				}
				else
				{
					//Get the first and second part of the string
					targetFirst = target.substring(0, firstBracketLocation);
					bracketResult = calculate(target.substring(firstBracketLocation + 1, i));
					targetSecond = target.substring(i + 1);
					target = targetFirst + bracketResult + targetSecond;
					targetArr = target.toCharArray();
					//AND DO IT ALL OVER AGAIN!
					i = -1;
					firstBracketLocation = -1;
					brackets = 0;
				}
			}
		}

		//now parse through the array, splitting it up to:
		//Numbers
		//And symbols (+, -, *, /)

		//Locating where are the symbols are and setting them
		for(int i = 0; i < targetArr.length; i++)
		{
			if (targetArr[i] == '^')
			{
				breaks[count] = i;
				priority[count] = 3;
				count++;
			}
			else if (targetArr[i] == implicitMultiplication)
			{
				breaks[count] = i;
				priority[count] = 2;
				count++;
			}
			else if (targetArr[i] == '*' || targetArr[i] == '/')
			{
				breaks[count] = i;
				priority[count] = 1;
				count++;
			}
			else if (targetArr[i] == '+' || targetArr[i] == '-')
			{
				//if it's the first number, ignore
				if (i != 0)
				{
					//If this is the first one, set
					if (count == 0)
					{
						breaks[count] = i;
						count++;
					}
					else //Otherwise, we should ignore negatives if it appears after a symbol
					{
						if (breaks[count - 1] == i - 1)
						{
							if (targetArr[i] == '+')
							{
								breaks[count] = i;
								count++;
							}
						}
						else
						{
							breaks[count] = i;
							count++;
						}
					}
				}
			}
		}

		//new array to store the number values between the symbols
		String[] numbersInString = new String[count + 1];
		for(int i = 0; i < count; i++)
		{
			numbersInString[i] = target.substring(previousPosition, breaks[i]);
			previousPosition = breaks[i] + 1;
		}
		//Get the last one
		numbersInString[numbersInString.length - 1] = target.substring(previousPosition, target.length());
		//parse the results into a string. This will also be used as the last array, after * and /
		double[] numbers = new double[numbersInString.length];
		//This array will be used after calculation of ^.
		double[] numbers2 = new double[numbersInString.length];
		for(int i = 0; i < numbersInString.length; i++)
		{
			numbers[i] = Double.parseDouble(numbersInString[i]);
		}

		//NOW: THE CALCULATION!
		//We need to do this four times. '^', then implicitMultiplication, then * and /, then + and -
		previousPosition = 0;
		for (int i = 0; i < count; i++)
		{
			if (priority[i] == 3)
			{
				if (targetArr[breaks[i]] == '^')
				{
					numbers[previousPosition + 1] = Math.pow(numbers[previousPosition], numbers[previousPosition + 1]);
				}
				else
				{
					throw new RuntimeException("Priority was set to 3, but targetArr did not contain a ^!");
				}
				//clear the original value
				numbers[previousPosition] = 0.0000000000005;
			}
			previousPosition++;
		}

		//now to port the array over. Unused values are ignored.
		previousPosition = 0;
		for (int i = 0; i < numbers.length; i++)
		{
			if (numbers[i] != 0.0000000000005)
			{
				numbers2[previousPosition] = numbers[i];
				previousPosition++;
			}
		}

		//Now for implict multiplication
		previousPosition = 0;
		for (int i = 0; i < count; i++)
		{
			if (priority[i] == 2)
			{
				if (targetArr[breaks[i]] == implicitMultiplication)
				{
					numbers2[previousPosition + 1] = numbers2[previousPosition] * numbers2[previousPosition + 1];
				}
				else
				{
					throw new RuntimeException("Priority was set to 2, but targetArr did not contain an implict multiplication!");
				}
				//clear the original value
				numbers2[previousPosition] = 0.0000000000005;
			}
			if (priority[i] != 3)
			{
				previousPosition++;
			}
		}

		//now to port the array over. Unused values are ignored.
		previousPosition = 0;
		for (int i = 0; i < numbers2.length; i++)
		{
			if (numbers2[i] != 0.0000000000005)
			{
				numbers[previousPosition] = numbers2[i];
				previousPosition++;
			}
		}

		//Now port it back because the next code users numbers2 -.-
		previousPosition = 0;
		for (int i = 0; i < numbers.length; i++)
		{
			if (numbers[i] != 0.0000000000005)
			{
				numbers2[previousPosition] = numbers[i];
				previousPosition++;
			}
		}

		previousPosition = 0;
		for(int i = 0; i < count; i++)
		{
			if(priority[i] == 1)
			{
				if(targetArr[breaks[i]] == '*')
				{
					numbers2[previousPosition + 1] = numbers2[previousPosition] * numbers2[previousPosition + 1];
				}
				else if(targetArr[breaks[i]] == '/')
				{
					numbers2[previousPosition + 1] = numbers2[previousPosition] / numbers2[previousPosition+1];
				}
				else
				{
					throw new RuntimeException("Priority was set to 1, but breaks did not contain a * or /!");
				}
				//clear the original value
				numbers2[previousPosition] = 0.0000000000005;
			}
			if (priority[i] != 2 && priority[i] != 3)
			{
				previousPosition++;
			}
		}

		//now to port the array over. Unused values are ignored.
		previousPosition = 0;
		for(int i = 0; i < numbers2.length; i++)
		{
			if(numbers2[i] != 0.0000000000005)
			{
				numbers[previousPosition] = numbers2[i];
				previousPosition++;
			}
		}

		//now calculate addition and subtraction
		result = numbers[0];
		previousPosition = 1;
		for(int i = 0; i < count; i++)
		{
			if(priority[i] == 0)
			{
				if(targetArr[breaks[i]] == '+')
				{
					result += numbers[previousPosition];
				}
				else if(targetArr[breaks[i]] == '-')
				{
					result -= numbers[previousPosition];
				}
				else
				{
					throw new RuntimeException("Priority was set to 0, but breaks did not contain + or -!");
				}
				previousPosition++;
			}
		}
		return result;
	}

	public static String removeSpacesAndComma(String target)
	{
		String answer = "";
		//remove spaces. Cause matt wants them removed.
		//remove Commas. Cause David wants them removed.
		char[] textArr = target.toCharArray();
		for(char aTextArr : textArr)
		{
			if(aTextArr != ' ' && aTextArr != ',')
			{
				answer = answer + aTextArr;
			}
		}

		return answer;
	}

	public static String makeErrorMessage(String methodName, String input, Exception exception)
	{
		String answer;
		answer = exception.toString();
		answer = "Action: \"" + methodName + "\", Input: \"" + input + "\"\n" + answer;
		return answer;
	}

	public static String leaveOnlyNumbers(String target)
	{
		char[] targetArr = target.toCharArray();
		Boolean[] alphabet = new Boolean[target.length()];
		for(int i = 0; i < targetArr.length; i++)
		{
			if(!isDigit(targetArr[i]))
			{
				alphabet[i] = true;
			}
		}
		String result = "";
		for (int i = 0; i < targetArr.length; i++ )
		{
			if (!alphabet[i])
			{
				result = result + targetArr[i];
			}
		}
		return result;
	}

	private static boolean isDigit(char c)
	{
		return (c >= '0' && c <= '9');
	}
}
