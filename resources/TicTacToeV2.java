package resources;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.io.*;

@SuppressWarnings("serial")
public class TicTacToeV2 {
	
	private int[][] winCases = new int[][] {
			{0,1,2}, {3,4,5}, {6,7,8},	//	The horizontal win cases.
			{0,3,6}, {1,4,7}, {2,5,8},	//	The vertical win cases.
			{0,4,8}, {2,4,6}			// 	The diagonal win cases.
	};
	
	private int turn = 0;
	private boolean firstGame = true;
	
	private JButton[] gameWindowJButtons = new JButton[9];
	private JFrame applicationWindow = new JFrame ("Tic Tac Toe V2");
	private JButton newGameButton = new JButton();
	private JButton creditsButton = new JButton();
	
	private int xScore = 0, oScore = 0;
	private Font scoreFont;
	private Color scoreColor = new Color(0xFC5530);
	private Color backgroundColor = new Color(0xCFD6DE);
	
	private ImageIcon xShapeImage;
	private ImageIcon oShapeImage;
	private ImageIcon xWinsImage;
	private ImageIcon oWinsImage;
	private ImageIcon tieImage;
	private ImageIcon creditsImage;
	
	public static void main(String[] args) {
		
		TicTacToeV2 game = new TicTacToeV2();
		TicTacToeV2.TicTacToeWindow gameWindow = game.new TicTacToeWindow();
		
		gameWindow.initApplicationWindow();
		gameWindow.drawInitialWindow();
		game.applicationWindow.setVisible(true);
		
	}

	/*
	 * Imports the Android_7 font used in this application,
	 * creates fonts that are used in the application
	 * and registers them to the graphic environment. 
	 */
	public void createFonts() {
		try {
			scoreFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/resources/android_7.ttf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		scoreFont = scoreFont.deriveFont(Font.BOLD, 28);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(scoreFont);
	}	// end importFont();
	
	/**
	 * Resets the game parameters in order to play another game.
	 */
	public void reset() {
		turn = 0;
		for (int i = 0; i < 9; i++) {
			gameWindowJButtons[i].setIcon(null);
			gameWindowJButtons[i].setName("");
		}
	}	// end reset
	
	/**
	 * Resets the game parameters as well as the scores
	 * in order to play a completely new game.
	 */
	public void newGame() {
		reset();
		xScore = 0;
		oScore = 0;
	}	// end newGame
	
	/**
	 * Checks if there is a pattern on the game grid that corresponds to 
	 * a winning condition and returns an answer if the game is over.
	 * @return true if a player has won.
	 */
	public boolean gameOver() {
		for (int i = 0; i <= 7; i++) {
	        if( gameWindowJButtons[winCases[i][0]].getName().equals(gameWindowJButtons[winCases[i][1]].getName()) && 
	        		gameWindowJButtons[winCases[i][1]].getName().equals(gameWindowJButtons[winCases[i][2]].getName()) && 
	        		!gameWindowJButtons[winCases[i][0]].getName().equals(""))
	                return true;
		}
		return false;
	}	// end checkIfGameOver

	/*
	 * Nested class that deals with the GUI aspect of the game.
	 */
	public class TicTacToeWindow extends JPanel
									implements ActionListener {	
		
		/**
		 * Initialises the application window and sets the layout manager.
		 */
		public void initApplicationWindow() {
			applicationWindow.setSize(400, 475);
			applicationWindow.setLocation(200, 200);
			applicationWindow.setResizable(false);
			applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			applicationWindow.setLayout( new BorderLayout() );
			
			
			UIManager UI = new UIManager();
			UI.put("OptionPane.background",  new ColorUIResource(backgroundColor));
			UI.put("Panel.background", new ColorUIResource(backgroundColor));
			
			/*
			 * Enable text anti aliasing
			*/
			System.setProperty("awt.useSystemAAFontSettings","on");
			System.setProperty("swing.aatext", "true");
		}	// end setupApplicationWindow
	
		/**
		 * Draws the initial (welcome) window along with it's JButtons.
		 */
		public void drawInitialWindow() {
			JLabel backgroundLabel = new JLabel();
			JLabel buttonsLabel = new JLabel();
			
			// Try to retrieve the images for the JButton icons.
			try {
				backgroundLabel.setIcon(new ImageIcon(this.getClass().getResource("/resources/welcomeScreen.png")));
				buttonsLabel.setIcon(new ImageIcon(this.getClass().getResource("/resources/buttonBackground.png")));
				newGameButton.setIcon(new ImageIcon(this.getClass().getResource("/resources/newGameButton.png")));			
				creditsButton.setIcon(new ImageIcon(this.getClass().getResource("/resources/creditsButton.png")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Imports the images that are used in the game.
			xShapeImage = new ImageIcon(this.getClass().getResource("/resources/xShape.png"));
			oShapeImage = new ImageIcon(this.getClass().getResource("/resources/oShape.png"));
			xWinsImage = new ImageIcon(this.getClass().getResource("/resources/xWins.png"));
			oWinsImage = new ImageIcon(this.getClass().getResource("/resources/oWins.png"));
			creditsImage = new ImageIcon(this.getClass().getResource("/resources/credits.png"));
			tieImage = new ImageIcon(this.getClass().getResource("/resources/tie.png"));
			
			// Set the JButton backgrounds to be invisible.
			creditsButton.setOpaque(true);
			creditsButton.setContentAreaFilled(false);
			creditsButton.setBorderPainted(false);
			newGameButton.setOpaque(true);
			newGameButton.setContentAreaFilled(false);
			newGameButton.setBorderPainted(false);
				
			newGameButton.addActionListener(this);
			creditsButton.addActionListener(this);
			
			buttonsLabel.setLayout( new FlowLayout() );
			buttonsLabel.add(newGameButton);
			buttonsLabel.add(creditsButton);
				
			applicationWindow.add(backgroundLabel, BorderLayout.CENTER);
			applicationWindow.add(buttonsLabel, BorderLayout.SOUTH);
		}	// end drawInitialWindow
		
		/**
		 * Adds an image to the game grid, initialises and aligns the JButtons 
		 * that are to be the circles and x's. 
		 * Adds everything to the application window, and repaints it.
		 */
		public void drawGameWindow() {
			/*
			 * Attempt to set up a JPanel as a game grid, retrieve the background image,
			 * add it to the container and repaint the panel.
			 */
			JPanel gameWindow = new JPanel() {
				public void paintComponent(Graphics g) {
					ImageIcon backgroundImage = null;
					try {
						backgroundImage = new ImageIcon(this.getClass().getResource("/resources/background.png"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					g.drawImage(backgroundImage.getImage(), 0, -1, null);
					
					g.setFont(scoreFont);
					g.setColor(scoreColor);
					g.drawString(" | ", 177, 385);
					g.drawString("X:" + xScore + "", 127, 385);
					g.drawString("O:" + oScore + "", 215, 385);
					
					super.repaint();
				}
			};	
			
			/*
			 * Sets the layout of the game grid to GridBagLayout
			 * due to specific positioning requirements of the JButtons.
			 * 
			 */
			gameWindow.setLayout( new GridBagLayout() );
			GridBagConstraints c = new GridBagConstraints();
			alignButtonsToGrid(gameWindow, c);

			applicationWindow.add(gameWindow, BorderLayout.CENTER);
			applicationWindow.revalidate();
			applicationWindow.repaint();
		}	// end drawGameWindow
		
		/**
		 * Aligns JButtons to a 3 by 3 grid, with specific padding
		 * and positioning relative to the background image.
		 * Also adds an actionListener to each JButton.
		 * @param window The JPanel where the buttons will be added.
		 * @param c The GridBagConstraints variable is needed for positioning manipulation.
		 */
		public void alignButtonsToGrid(JPanel window, GridBagConstraints c) {
			c.ipadx = 40;
			c.ipady = 60;
			c.insets = new Insets(-200, 15, 0, 10);
			
			gameWindowJButtons[0] = new JButton();
			c.gridx = 0;
			c.gridy = 0;
			window.add(gameWindowJButtons[0],c);
			
			gameWindowJButtons[1] = new JButton();
			c.gridx = 1;
			c.gridy = 0;
			window.add(gameWindowJButtons[1],c);
			
			gameWindowJButtons[2] = new JButton();
			c.gridx = 2;
			c.gridy = 0;
			window.add(gameWindowJButtons[2],c);
			
			c.insets = new Insets(-200,15,-200,10);
			
			gameWindowJButtons[3] = new JButton();
			c.gridx = 0;
			c.gridy = 1;
			window.add(gameWindowJButtons[3],c);
			
			gameWindowJButtons[4] = new JButton();
			c.gridx = 1;
			c.gridy = 1;
			window.add(gameWindowJButtons[4],c);
			
			gameWindowJButtons[5] = new JButton();
			c.gridx = 2;
			c.gridy = 1;
			window.add(gameWindowJButtons[5],c);
			
			c.insets = new Insets(-200,15,-400,10);
			
			gameWindowJButtons[6] = new JButton();
			c.gridx = 0;
			c.gridy = 2;
			window.add(gameWindowJButtons[6],c);
			
			gameWindowJButtons[7] = new JButton();
			c.gridx = 1;
			c.gridy = 2;
			window.add(gameWindowJButtons[7],c);
			
			gameWindowJButtons[8] = new JButton();
			c.gridx = 2;
			c.gridy = 2;
			window.add(gameWindowJButtons[8],c);
			
			/*
			 * The loop will define the size of the JButtons, add actionListeners,
			 * set names and make the JButton background transparent.
			 */
			for (int i = 0; i < 9; i++) {
				gameWindowJButtons[i].setPreferredSize(new Dimension(35, 12));
				gameWindowJButtons[i].addActionListener(this);
				gameWindowJButtons[i].setName("");
				
				gameWindowJButtons[i].setFocusPainted(false);
				gameWindowJButtons[i].setContentAreaFilled(false);
				gameWindowJButtons[i].setBorderPainted(false);
				gameWindowJButtons[i].setOpaque(false);
			}
		}	// end alignButtonsToGrid
		
		/**
		 * Called when a button is pressed.
		 */
		public void actionPerformed(ActionEvent evt) {
			
			/*
			 * If the newGameButton is pressed for the first time 
			 * then draw the game window and set firstGame to false.
			 * If a window is already drawn and the newGameButton is pressed,
			 * then just reset the game variables. 
			 * This is done as to avoid flickering from overlapping game window panels.
			 */
			if (evt.getSource() == newGameButton) {
				if (firstGame) {	
					createFonts();
					drawGameWindow();
					firstGame = false;
				}
				newGame();
				return;
			}
			
			if (evt.getSource() == creditsButton) {
				// showCredits();
				JOptionPane.showMessageDialog(applicationWindow, creditsImage, "Credits", JOptionPane.PLAIN_MESSAGE, null);
				return;
			}
			
			JButton target = (JButton)evt.getSource();
			if (!target.getName().equals(""))
				return;
			else
				turn++;
			
			if (turn % 2 == 0) {
				try {
					target.setIcon(xShapeImage);
					target.setName("x");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					target.setIcon(oShapeImage);
					target.setName("o");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (gameOver() && turn % 2 == 0) {
				// add logic to announce that the winner is "X"
				JOptionPane.showMessageDialog(applicationWindow, xWinsImage, null, JOptionPane.PLAIN_MESSAGE, null);
				xScore++;
				reset();
			}
			else if (gameOver() && turn % 2 != 0) {
				// add logic to announce that the winner is "O"
				JOptionPane.showMessageDialog(applicationWindow, oWinsImage, null, JOptionPane.PLAIN_MESSAGE, null);
				oScore++;
				reset();
			}
			else if (!gameOver() && turn == 9) {
				// add logic to announce a tie
				JOptionPane.showMessageDialog(applicationWindow, tieImage, null, JOptionPane.PLAIN_MESSAGE, null);
				reset();
			}
		}	// end actionPerformed
		
	}	// end inner class TicTacToeWindow
	
}	// end class TicTacToeV2
