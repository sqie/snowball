/*Stephanie Qie
 * 
 * Game GUI:
 * Sets up and runs the game
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameGUI extends JFrame implements KeyListener{

	private BufferedImage [] images = new BufferedImage[8];
	public final String [] imageNames = {"tile.png", "forwardSprite.png", "backwardSprite.png", "leftSprite.png", "rightSprite.png", "shadow1.png", "shadow2.png", "snowball.png"};
	private static PicPanel[][] allPanels;
	private Sprite sprite;
	private static ArrayList<Snowball> snowballs = new ArrayList<Snowball>();
	
	//sets up the GUI
	public GameGUI() {

		//set up the frame
		setTitle("Snowball");
		setSize(1000, 1000);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.white);

		this.addKeyListener(this);

		readInPictures();

		allPanels = new PicPanel[10][10];
		setLayout(new GridLayout(10, 10));

		//create the tiles
		for(int row = 0; row < allPanels.length; row ++)
			for(int col = 0; col < allPanels[row].length; col++){
				allPanels[row][col] = new PicPanel(row, col);
			}

		sprite = new Sprite();
		allPanels[sprite.getCurRow()][sprite.getCurCol()].updateImage(1, -1);

		//add the picture panels to the GUI
		for(int row = 0; row < allPanels.length; row ++)
			for(int col = 0; col < allPanels[row].length; col++)
				this.add(allPanels[row][col]);

		setVisible(true);
	}

	//PicPanel -- represents the pictures in the grid
	class PicPanel extends JPanel{

		private int width = 100;
		private int height = 100;		
		private JLabel text;
		private int tileIndex;
		private int spriteIndex;
		private int snowballIndex;

		//initialize the picture panel
		public PicPanel(int r, int c){

			tileIndex = 0;
			spriteIndex = -1;
			snowballIndex = -1;
			
			repaint();
		}		

		//getPreferredSize -- returns the size
		public Dimension getPreferredSize(){
			return new Dimension(width,height);
		}

		//paintComponent -- draws the image or number
		public void paintComponent(Graphics g){

			super.paintComponent(g);
			g.drawImage(images[tileIndex], 0, 0, this);
			
			//if there is a snowball/shadow
			if(snowballIndex != -1) 
				g.drawImage(images[snowballIndex], 0, 0, this);
			
			//if there is a sprite
			if(spriteIndex != -1)
				g.drawImage(images[spriteIndex], 0, 0, this);
		}

		//update the sprite and snowball indexes
		public void updateImage(int spriteIndex, int snowballIndex) {

			this.spriteIndex = spriteIndex;
			this.snowballIndex = snowballIndex;
			
			repaint();
		}
		
		//returns the sprite index
		public int getSpriteIndex() {
			return spriteIndex;
		}
		
		//returns the snowball index
		public int getSnowballIndex() {
			return snowballIndex;
		}
	}
	
	//readInPicture -- read in the chosen picture
	private void readInPictures(){

		//go through the images
		for(int i = 0; i < imageNames.length; i++) {

			//try to read in the picture
			try{

				images[i] = ImageIO.read(new File(imageNames[i]));

			}catch (IOException ioe){

				JOptionPane.showMessageDialog(null, "Could not read in the pic");
				System.exit(0);
			}	
		}
	}

	//move the sprite if the key is pressed
	public void keyPressed(KeyEvent event) {

		int keyVal = event.getKeyCode();		
		int curSpriteIndex = sprite.getCurImageIndex();
		int spriteRow = sprite.getCurRow();
		int spriteCol = sprite.getCurCol();
		
		//if left key pressed, move sprite left
		if(keyVal == KeyEvent.VK_LEFT) {

			//if currently facing right, turn forward
			if(curSpriteIndex == 4) 
				moveSprite(spriteRow, spriteCol - 1, 1);

			//otherwise turn left
			else 
				moveSprite(spriteRow, spriteCol - 1, 3);
		}

		//if right key pressed, move right
		else if(keyVal == KeyEvent.VK_RIGHT) {

			//if currently facing left, turn forward
			if(curSpriteIndex == 3)
				moveSprite(spriteRow, spriteCol + 1, 1);

			//otherwise turn right
			else
				moveSprite(spriteRow, spriteCol + 1, 4);
		}

		//if up key pressed, move up
		else if(keyVal == KeyEvent.VK_UP) {
			moveSprite(spriteRow - 1, spriteCol, 2);
		}

		//if down key pressed, move down
		else if(keyVal == KeyEvent.VK_DOWN) {
			moveSprite(spriteRow + 1, spriteCol, 1);
		}
	}

	//tries to move the sprite to the new location
	private void moveSprite(int newRow, int newCol, int newSpriteIndex) {

		//if in bounds, move the sprite to the new location
		if(isInBounds(newRow, newCol)){

			int curRow = sprite.getCurRow();
			int curCol = sprite.getCurCol();
			
			allPanels[curRow][curCol].updateImage(-1, allPanels[curRow][curCol].getSnowballIndex());
			allPanels[newRow][newCol].updateImage(newSpriteIndex, allPanels[newRow][newCol].getSnowballIndex());

			sprite.updateRow(newRow);
			sprite.updateCol(newCol);
			sprite.updateImageIndex(newSpriteIndex);
		}
	}

	//isInBounds -- returns whether the new location is in bounds
	private boolean isInBounds(int newRow, int newCol){
		return newRow < allPanels.length && newRow >= 0 && newCol < allPanels[0].length && newCol >= 0;
	}

	//don't handle the keys being released
	public void keyReleased(KeyEvent arg0) {
	}

	//don't handle the keys being typed
	public void keyTyped(KeyEvent arg0) {
	}

	//run the GUI
	public static void main(String [] args) {
		
		GameGUI game = new GameGUI();
		boolean gameOver = false;
		
		ZonedDateTime start = ZonedDateTime.now();
		int curSec = 0;
		
		//while the game isn't over
		while(!gameOver) {
		
			Duration duration = Duration.between(start, ZonedDateTime.now());
			int secondsPassed = (int)(duration.getSeconds());
			
			//if a second has passed
			if(curSec < secondsPassed) {
			
				//update the snowballs
				for(int i = 0; i < snowballs.size(); i++) {
					
					Snowball curSnowball = snowballs.get(i);
					int curRow = curSnowball.getRow();
					int curCol = curSnowball.getCol();
					
					curSnowball.updateCurImageIndex();
					
					//if still a shadow/snowball
					if(curSnowball.getCurImageIndex() <= 7) {
						
						allPanels[curRow][curCol].updateImage(allPanels[curRow][curCol].getSpriteIndex(), curSnowball.getCurImageIndex());
						
						//if snowball hit the sprite
						if(curSnowball.getCurImageIndex() == 7 && allPanels[curRow][curCol].getSpriteIndex() != -1) {
							
							allPanels[curRow][curCol].updateImage(-1, curSnowball.getCurImageIndex());
							gameOver = true;
							break;
						}
					}
					
					//if been a snowball for two seconds
					if(curSnowball.getCurImageIndex() == 9) {
						
						allPanels[curRow][curCol].updateImage(allPanels[curRow][curCol].getSpriteIndex(), -1);
						snowballs.remove(i);
					}
				}
				
				//add more snowballs
				for(int i = 0; i < curSec; i++) {
					
					Snowball newSnowball = new Snowball();
					int curRow = newSnowball.getRow();
					int curCol = newSnowball.getCol();
					PicPanel curPanel = allPanels[curRow][curCol];
					
					//if a snowball isn't on that tile yet
					if(curPanel.getSnowballIndex() == -1) {
						
						curPanel.updateImage(curPanel.getSpriteIndex(), newSnowball.getCurImageIndex());
						snowballs.add(newSnowball);
					}
				}
			}
			
			curSec = secondsPassed;
		}
		
		game.removeKeyListener(game);
	}
}