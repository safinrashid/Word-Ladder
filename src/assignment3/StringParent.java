/* WORD LADDER StringParent.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name> Safin Rashid
 * <Student1 EID> srr3288
 * <Student1 5-digit Unique No.> 17155
 * <Student2 Name> Roy Mor
 * <Student2 EID> rm58977
 * <Student2 5-digit Unique No.> 17150
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/sp-23-assignment-3-sp23-pair-4.git
 * Spring 2023
 */

package assignment3;

/**
 * A support class which functions as a String with a reference to a parent string, essentially a linked list node
 * Used to find the path between start and end word 
 */
public class StringParent {
	StringParent parent;
	String value;
	
	/**
	 * Constructs a StringParent with a parent and a value
	 * @param parent the parent of the string
	 * @param value the string to be saved
	 */
	public StringParent(StringParent parent, String value) {
		this.parent = parent;
		this.value = value;
	}
	
	/**
	 * Checks if the values of two StringParents are equal, implemented for contains
	 */
	public boolean equals(Object o) {
		if(o == null) return false;
		
		StringParent object = (StringParent)o;
		if(object.value.equals(this.value)) return true;
		
		return false;
	}
}
