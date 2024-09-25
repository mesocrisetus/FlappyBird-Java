import java.awt.*;
import java.awt.event.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;

import javax.print.attribute.standard.PagesPerMinute;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener{
	
	int boardWith = 306;
	int boardHeigth = 640;
	
	// images
	
	Image backgroundImg;
	Image birdImg;
	Image topPipeImage;
	Image bottomPipeImage;
	
	// Bird
	
	int birdX = boardWith /8;
	int birdY = boardHeigth/2;
	int birdWith = 34;
	int birdHeight = 24;
	
	class Bird{
		int x = birdX;
		int y = birdY;
		int width = birdWith;
		int height = birdHeight ;
		Image img;
		
		Bird(Image img){
			this.img = img;
		}
				
	}
	
	// Pipes
	
	int pipeX = boardWith ;
	int pipeY = 0;
	int pipeWidth = 64;
	int pipeHeigth= 512;
	
	class Pipe{
		int x = pipeX;
		int y = pipeY;
		int width = pipeWidth;
		int height = pipeHeigth;
		Image img;
		boolean passed = false;
		
		Pipe(Image img){
			this.img = img;
		}
		
	}
	
	// Game Logic
	
	Bird bird;	
	int velocityX = -4;
	int velocityY = 0;
	int gravity = 1;
	
	ArrayList<Pipe> pipes;
	Random random = new Random();
	
	
	Timer gameLopp;
	Timer placePipesTimer;
	
	boolean gameOver = false;
	double score = 0;
	
	public FlappyBird() {
		setPreferredSize(new Dimension(boardWith,boardHeigth));
		// setBackground(Color.blue);
		
		setFocusable(true);
		addKeyListener(this);
		
		// load images
		
		backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
		birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
		topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
		bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
		
		
		//bird
		bird = new Bird(birdImg);
		pipes = new ArrayList<Pipe>();
		
		// places Pipes Timer
		
		placePipesTimer = new Timer(1500, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				placePipes();
				
			}
		});
		
		placePipesTimer.start(); 
		
		// game Timer
		
		gameLopp = new Timer(1000/60,this);
		gameLopp.start();
	}
	
	public void placePipes() {
		int randomPipeY = (int)(pipeY - pipeHeigth/4 - Math.random()*(pipeHeigth/2));
		int openingSpace = boardHeigth/4;
		
		
		
		
		Pipe topPipe = new Pipe(topPipeImage);
		topPipe.y = randomPipeY;
		pipes.add(topPipe);
		
		Pipe bottomPipe = new Pipe(bottomPipeImage);
		bottomPipe.y = topPipe.y + pipeHeigth + openingSpace;
		pipes.add(bottomPipe);
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		// background
		
		g.drawImage(backgroundImg, 0, 0, boardWith, boardHeigth, null);
		
		// Bird
		
		g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height,null);
		
		// Pipes
		
		for (int i = 0; i < pipes.size();i++) {
			Pipe pipe = pipes.get(i);
			g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width , pipe.height, null);
			}
		
		// score
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN,52));
		if(gameOver) {
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.PLAIN,42));
			g.drawString("Game Over",40,290);
			g.drawString("Score: " +  String.valueOf((int)score),50,330);

		}
		else {
			g.drawString(String.valueOf((int)score),10,55);
		}
	}
	
	public void move() {
		//bird
		velocityY += gravity;
		bird.y  += velocityY;
		bird.y = Math.max(bird.y, 0);
		
		//pipes
		 
		for (int i= 0;i<pipes.size();i++) {
			Pipe pipe = pipes.get(i);
			pipe.x += velocityX;
			
			if (!pipe.passed && bird.x > pipe.x + pipe.width ) {
				pipe.passed = true;
				score += 0.5;
			}
			
			if(collision(bird, pipe)) {
				gameOver = true;
			}
		}
		
		
		
		if (bird.y > boardHeigth) {
			gameOver = true;
		}
	}
	
	public boolean collision(Bird a, Pipe b) {
		return a.x < b.x + b.width &&
				a.x + a.width > b.x &&
				a.y < b.y + b.height&&
				a.y + a.height > b.y;
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		move();
		repaint();
		if (gameOver) {
			placePipesTimer.stop();
			gameLopp.stop();
		}
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			velocityY = -9;
			if (gameOver) {
				// restart the game by resetting the conditions
				bird.y = birdY;
				velocityY = 0;
				pipes.clear();
				score = 0;
				gameOver = false;
				gameLopp.start();
				placePipesTimer.start();
				
			}
		}
	}
	
	
	
	

	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
		
		
	
	
}
