import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

@SuppressWarnings("serial")
public class Canvas extends JPanel {
	GUI gui;
	JFrame frame;
	private Image player, goal, wall, bg;
	private static int WIDTH = 720;
	private static int HEIGHT = 720;
	//** 0 Jugador, 1 Abierto, 2 Meta, 3 Obstaculo **//
	private int[][] grid = new int[9][9];
	int i = 0;
	int j = 0;
	int up = 0;
	int down = 0;
	int left = 0;
	int right = 0;
	int playerX = 4;
	int playerY = 4;
	int metas = 0;
	int steps = 0;
	Random rand;
	int rand1, rand2;
	Timer t1, t2, t3, t4;
	boolean canMove = true;
	boolean timerRunning = false;
	File soundFile;
	String getCoin = "coin.wav";
	String crash = "crash.wav";
	String fail = "fail.wav";
	
	public Canvas(GUI gui) {
		ImageIcon imagen = new ImageIcon("player.png");
	    player = imagen.getImage();
	    imagen = new ImageIcon("coin.png");
	    goal = imagen.getImage();
	    imagen = new ImageIcon("spike.png");
	    wall = imagen.getImage();
	    imagen = new ImageIcon("background.jpg");
	    bg = imagen.getImage();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.gui = gui;
		this.frame = gui.getFrame();
		this.i = 0;
		this.j = 0;
		this.rand1 = 0;
		this.rand2 = 0;
		this.metas = 0;
		this.steps = 0;
		this.playerX = 4;
		this.playerY = 4;
		this.canMove = true;
		this.timerRunning = false;
		t1 = new Timer(400, null);
		t1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(up < steps) {
					if(playerY > 0 && playerY < 9) {
						playerY = playerY - 1;
						if(grid[playerX][playerY] == 3) {
							crashed();
							setTimerRunning(false);
							t1.stop();
							throw new Error("CRASHED!");
						}
						if(grid[playerX][playerY] == 2) {
							grid[playerX][playerY] = 1;
							metas -= 1;
							playSoundEffect(getCoin);
							if(metas <= 0){
								setTimerRunning(false);
								t1.stop();
								winner();
							}
						}
						System.out.println("UP");
						validate(); repaint();
						frame.validate(); frame.repaint();
					} else {
						outOfBounds();
						setTimerRunning(false);
						t1.stop();
						throw new Error("OUT!");
					}
				} else {
					setTimerRunning(false);
					t1.stop();
				}
				up++;
			}
		});
		t2 = new Timer(400, null);
		t2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(down < steps) {
					if(playerY >= 0 && playerY < 8) {
						playerY = playerY + 1;
						if(grid[playerX][playerY] == 3) {
							crashed();
							setTimerRunning(false);
							t2.stop();
							throw new Error("CRASHED!");
						}
						if(grid[playerX][playerY] == 2) {
							grid[playerX][playerY] = 1;
							metas -= 1;
							playSoundEffect(getCoin);
							if(metas <= 0){
								setTimerRunning(false);
								t2.stop();
								winner();
							}
						}
						System.out.println("DOWN");
						validate(); repaint();
						frame.validate(); frame.repaint();
					} else {
						outOfBounds();
						setTimerRunning(false);
						t2.stop();
						throw new Error("enunciado invalido");
					}
				} else {
					setTimerRunning(false);
					t2.stop();
				}
				down++;
			}
		});
		t3 = new Timer(400, null);
		t3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(left < steps) {
					if(playerX > 0 && playerX < 9) {
						playerX = playerX - 1;
						if(grid[playerX][playerY] == 3) {
							crashed();
							t3.stop();
							setTimerRunning(false);
							throw new Error("CRASHED!");
						}
						if(grid[playerX][playerY] == 2) {
							grid[playerX][playerY] = 1;
							metas -= 1;
							playSoundEffect(getCoin);
							if(metas <= 0){
								setTimerRunning(false);
								t3.stop();
								winner();
							}
						}
						System.out.println("LEFT");
						validate(); repaint();
						frame.validate(); frame.repaint();
					} else {
						outOfBounds();
						setTimerRunning(false);
						t3.stop();
						throw new Error("enunciado invalido");
					}
				} else {
					setTimerRunning(false);
					t3.stop();
				}
				left++;
			}
		});
		t4 = new Timer(400, null);
		t4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(right < steps) {
					if(playerX >= 0 && playerX < 8) {
						playerX = playerX + 1;
						if(grid[playerX][playerY] == 3) {
							crashed();
							setTimerRunning(false);
							t4.stop();
							throw new Error("CRASHED!");
						}
						if(grid[playerX][playerY] == 2) {
							grid[playerX][playerY] = 1;
							metas -= 1;
							playSoundEffect(getCoin);
							if(metas <= 0) {
								setTimerRunning(false);
								t4.stop();
								winner();
							}
						}
						System.out.println("RIGHT");
						validate(); repaint();
						frame.validate(); frame.repaint();
					} else {
						outOfBounds();
						setTimerRunning(false);
						t4.stop();
						throw new Error("enunciado invalido");
					}
				} else {
					setTimerRunning(false);
					t4.stop();
				}
				right++;
			}
		});
		canMove = true;
		for(i=0; i<9; i++) {
			for(j=0; j<9; j++) {
				grid[i][j] = 1;
			}
		}
		// Colocar obstáculos
		grid[1][1] = 3; grid[3][3] = 3; grid[5][5] = 3; grid[7][7] = 3;
		grid[1][7] = 3; grid[3][5] = 3; grid[5][3] = 3; grid[7][1] = 3;
		grid[0][4] = 3; grid[8][4] = 3; grid[4][0] = 3; grid[4][8] = 3;
		grid[playerX][playerY] = 0;
		rand = new Random();
		while(metas < 6) {
			rand1 = rand.nextInt(9);
			rand2 = rand.nextInt(9);
			if(grid[rand1][rand2] == 1) {
				grid[rand1][rand2] = 2;
				metas += 1;
			}
		}
	}
	public void playSoundEffect(String file) {
		Thread playing = new Thread() {
		    public void run() {
	        	soundFile = new File(file);
	    		try ( // Open an audio input stream.            
	    			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);            
	    			// Get a sound clip resource.
	    			Clip clip = AudioSystem.getClip()) {
	    			// Open audio clip and load samples from the audio input stream.
	    			clip.open(audioIn);
	    			clip.start();
	    			Thread.sleep(1000);
	    		} catch (UnsupportedAudioFileException e) {
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		} catch (LineUnavailableException e) {
	    			e.printStackTrace();
	    		} catch (InterruptedException e) {
	    			e.printStackTrace();
	    		}
		    }  
		};
		playing.start();
	}
	public void resetGame(int nivel) {
		this.i = 0;
		this.j = 0;
		this.rand1 = 0;
		this.rand2 = 0;
		this.metas = 0;
		this.steps = 0;
		this.playerX = 4;
		this.playerY = 4;
		this.canMove = true;
		this.timerRunning = false;
		for(i=0; i<9; i++) {
			for(j=0; j<9; j++) {
				grid[i][j] = 1;
			}
		}
		// Colocar obstáculos
		grid[1][1] = 3; grid[3][3] = 3; grid[5][5] = 3; grid[7][7] = 3;
		grid[1][7] = 3; grid[3][5] = 3; grid[5][3] = 3; grid[7][1] = 3;
		grid[0][4] = 3; grid[8][4] = 3; grid[4][0] = 3; grid[4][8] = 3;
		grid[playerX][playerY] = 0;
		rand = new Random();
		while(metas < nivel) {
			rand1 = rand.nextInt(9);
			rand2 = rand.nextInt(9);
			if(grid[rand1][rand2] == 1) {
				grid[rand1][rand2] = 2;
				metas += 1;
			}
		}
		repaint();
		revalidate();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.drawImage(bg, 0, 0, 720, 720, null);
		for(i=0; i<9; i++) {
			for(j=0; j<9; j++) {
				g.setColor(Color.WHITE); g.drawRect(i*75+20, j*75+20, 75, 75);
				switch(grid[i][j]) {
					case 0: g.drawImage(player, playerX*75+20, playerY*75+20, 70, 70, null); break; //g.fillRect(playerX*75+20, playerY*75+20, 75, 75);
					case 1: break;
					case 2: g.drawImage(goal, i*75+35, j*75+35, 50, 50, null); break; //g.fillRect(i*75+20, j*75+20, 75, 75);
					case 3: g.drawImage(wall, i*75+20, j*75+20, 75, 75, null); break; //g.fillRect(i*75+20, j*75+20, 75, 75);
				}
			}
		}
	}
	public void nextMove(String dir, int steps) {
		switch(dir.toUpperCase()) {
			case "ARRIBA":
			case "UP":		up = 0;
							if(canMove) {
								this.steps = steps;
								t1.setRepeats(true);
								t1.start();
							}
							break;
			case "ABAJO":
			case "DOWN":	down = 0;
							if(canMove) {
								this.steps = steps;
								t2.setRepeats(true);
								t2.start();	
							}
							break;
			case "IZQUIERDA":
			case "LEFT":	left = 0;
							if(canMove) {
								this.steps = steps;
								t3.setRepeats(true);
								t3.start();	
							}
							break;
			case "DERECHA":
			case "RIGHT":	right = 0;
							if(canMove) {
								this.steps = steps;
								t4.setRepeats(true);
								t4.start();	
							}
							break;
			default:	JOptionPane.showMessageDialog(this, "COMANDO INVALIDO!", "ERROR", JOptionPane.WARNING_MESSAGE);
						throw new Error("comando invalido");
		}
	}
	public void winner() {
		canMove = false;
		gui.setResetEnabled(true);
		JOptionPane.showMessageDialog(this, "GANASTE!", "GAME OVER", JOptionPane.WARNING_MESSAGE);
	}
	public void loser() {
		canMove = false;
		gui.setResetEnabled(true);
		JOptionPane.showMessageDialog(this, "PERDISTE...", "GAME OVER", JOptionPane.WARNING_MESSAGE);
	}
	public void outOfBounds() {
		canMove = false;
		playSoundEffect(fail);
		gui.setResetEnabled(true);
		JOptionPane.showMessageDialog(this, "No puedes salir de la cuadrícula!", "GAME OVER", JOptionPane.WARNING_MESSAGE);
	}
	public void crashed() {
		playSoundEffect(crash);
		gui.setResetEnabled(true);
		JOptionPane.showMessageDialog(this, "No puedes chocar con obstáculos!", "GAME OVER", JOptionPane.WARNING_MESSAGE);
	}
	public JPanel getJPanel() {
		return this;
	}
	public int getPlayerX() {
		return this.playerX;
	}
	public int getPlayerY() {
		return this.playerY;
	}
	public int getGridValue(int x, int y) {
		return grid[x][y];
	}
	public int getMetas() {
		return this.metas;
	}
	public boolean getCanMove() {
		return this.canMove;
	}
	public boolean getTimerRunning() {
		return this.timerRunning;
	}
	public void setTimerRunning(boolean bool) {
		this.timerRunning = bool;
	}
}
