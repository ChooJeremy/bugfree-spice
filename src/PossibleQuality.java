import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class PossibleQuality implements Serializable, Comparable<PossibleQuality>
{
	private ArrayList<Integer> theQuality;
	private String name;

	@SuppressWarnings("unchecked")
	public PossibleQuality(ArrayList<Integer> possibleQuality)
	{
		theQuality = (ArrayList<Integer>) possibleQuality.clone();
		Collections.sort(theQuality);
	}

	public String getName() { return name;}
	public void setName(String n) { name = n;}

	public ArrayList<Integer> getTheQuality()
	{
		return theQuality;
	}

	@SuppressWarnings("unchecked")
	public boolean qualityEquals(ArrayList<Integer> otherQuality)
	{
		ArrayList<Integer> qualityToCheck = (ArrayList<Integer>) otherQuality.clone();
		Collections.sort(qualityToCheck);
		return theQuality.equals(qualityToCheck);
	}

	public int getTotal()
	{
		return PoeQuality.getTotal(theQuality);
	}

	@Override
	public String toString()
	{
		return name + ": " + theQuality.toString();
	}

	@Override
	public int compareTo(PossibleQuality other)
	{
		return getTotal() - other.getTotal();
	}
}
