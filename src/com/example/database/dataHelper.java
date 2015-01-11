package com.example.database;

/**
 * This Class contains methods to turn floats into strings
 * @author Nathan Hallahan
 *
 */
public class dataHelper {

	/**
	 * convert a float to a string. 5.0 becomes "5" or 5.23 becomes "5.23"
	 * @param theFloat
	 * @return
	 */
	public static String floatAsString(float theFloat) {
		String asString = String.valueOf(theFloat);
		//split the string at the decimal point
		String[] parts = asString.split("\\.");
		
		String theString = "make me a number";
		
		if (parts.length>0)//if there are was a decimal
		{
			String part1 = parts[0];
			String part2 = parts[1];
			
			if (Integer.parseInt(part2) > 0) //if there was a number other than 0 after the decimal
			{
				theString = asString;//theString will be the float
			}
			else 
			{
				theString = part1;//we will see it as an int, in string form
			}
		}
		else 
		{
			theString = asString;
		}

		return theString;

	}
	
	/**
	 * Use to remove the .0 from a string so it looks like an int
	 * @param theFloat
	 * @return
	 */
	public static String floatAsString(String theFloat) {
		String asString = theFloat;
		
		String[] parts = asString.split("\\.");
		
		
		
		String theString = "make me a number";
		
		if (parts.length>0)
		{
			String part1 = parts[0];
			String part2 = parts[1];
			
			
			if (Integer.parseInt(part2) > 0)
			{
				theString = asString;
			}
			else 
			{
				theString = part1;
			}
		}
		else 
		{
			theString = asString;
		}

		return theString;

	}

}
