/*Stephanie Qie
 * 
 * Snowball:
 * Represents a snowball
 */
import java.util.*;

public class Snowball {

	private int row;
	private int col;
	private int curImageIndex;
	
	//initialize the snowball
	public Snowball() {
		
		row = (int)(Math.random() * 10);
		col = (int)(Math.random() * 10);
		curImageIndex = 5;
	}
	
	//update the current image index
	public void updateCurImageIndex() {
		curImageIndex++;
	}
	
	//returns the snowball's row
	public int getRow() {
		return row;
	}
	
	//returns the snowball's col
	public int getCol() {
		return col;
	}
	
	//returns the snowball's current image index
	public int getCurImageIndex() {
		return curImageIndex;
	}
}
