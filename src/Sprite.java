/*Stephanie Qie
 * 
 * Sprite:
 * Represents the character in the game
 */
public class Sprite {
	
	private int curRow;
	private int curCol;
	private int curImageIndex;
	
	//initialize the sprite
	public Sprite() {
		
		curRow = 4;
		curCol = 4;
		curImageIndex = 0;
	}
	
	//updates the sprite's row
	public void updateRow(int newRow) {
		curRow = newRow;
	}
	
	//updates the sprite's column
	public void updateCol(int newCol) {
		curCol = newCol;
	}
	
	//updates the sprite's image index
	public void updateImageIndex(int newImageIndex) {
		curImageIndex = newImageIndex;
	}
	
	//return the current row
	public int getCurRow() {
		return curRow;
	}

	//return the current column
	public int getCurCol() {
		return curCol;
	}
	
	//return the current image index
	public int getCurImageIndex() {
		return curImageIndex;
	}
}