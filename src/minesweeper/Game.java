package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener{
	
	public static final int	size = 20;
	public static final int	fontsize = 14;
	public static final int width = 63;		
	public static final int height = 34;		
	public static final int mines = 200;
	
	public static void main(String a []){
		Game g = new Game();
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setSize((width * size) + 7, (height * size) + 30);
		j.setVisible(true);
		j.setLocationRelativeTo(null);
		j.setResizable(false);
		j.add(g);
	}
	
	int boxes [][] = new int [width][height];
	boolean selected [][] = new boolean [width][height];
	boolean detected [][] = new boolean [width][height];
	boolean gameover = false;
	Font font = new Font("Sherif", Font.BOLD, fontsize);
	
	public Game(){
		Timer t = new Timer(5,this);
		t.start();
		addMouseListener(new ML());
		addMouseMotionListener(new ML());
		setFocusable(true);
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				boxes[i][j] = 0;
			}
		}
		generate();
		calculate();
	}
	
	public void generate(){
		Random rand = new Random();
		int lastx = rand.nextInt(width);
		int lasty = rand.nextInt(height);		
		int xx = lastx;
		int yy = lasty;
		for(int i = mines; i > 0; i--){
			do{
				xx = rand.nextInt(width);
			}while(xx == lastx);
			lastx = xx;
			do{
				yy = rand.nextInt(height);
			}while(yy == lasty);
			lasty = yy;
			boxes[xx][yy] = 10;
		}
	}
	
	public void calculate(){
		int sum = 0;
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(boxes[i][j] < 10){
					if(i > 0 && j > 0 && boxes[i - 1][j - 1] == 10){
						sum++;
					}
					if(i > 0 && boxes[i - 1][j] == 10){
						sum++;
					}
					if(i > 0 && j < height - 1 && boxes[i - 1][j + 1] == 10){
						sum++;
					}
					if(j > 0 && boxes[i][j - 1] == 10){
						sum++;
					}
					if(j < height - 1 && boxes[i][j + 1] == 10){
						sum++;
					}
					if(i < width -1 && j > 0 && boxes[i + 1][j - 1] == 10){
						sum++;
					}
					if(i < width - 1 && boxes[i + 1][j] == 10){
						sum++;
					}
					if(i < width - 1 && j < height - 1 && boxes[i + 1][j + 1] == 10){
						sum++;
					}
					boxes[i][j] = sum;
					sum = 0;
				}
			}	
		}
	}
	
	private class ML implements MouseListener, MouseMotionListener{
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){ mouse(e); }
		public void mouseMoved(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mouseClicked(MouseEvent e){}
		public void mouseDragged(MouseEvent e){}
		public void mouse(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			if(SwingUtilities.isLeftMouseButton(e)){
				if(!gameover){
					selected(x,y,1);
				}
			}
			else if(SwingUtilities.isRightMouseButton(e)){
				if(!gameover){
					selected(x,y,2);
				}
			}
		}
	}
	
	public void selected(int xx, int yy, int lr){
		for(int i = 0; i < width; i++){
			if(xx > i * size && xx < (i * size) + size){
				for(int j = 0; j < height; j++){
					if(yy > j * size && yy < (j * size) + size){
						if(lr == 1){
							if(!detected[i][j]){
								selected[i][j] = true;
								if(boxes[i][j] == 0){
									clear(i,j);
								}
							}	
						}
						else if(lr == 2 && !selected[i][j]){
							if(!detected[i][j]){
								detected[i][j] = true;
							}
							else{
								detected[i][j] = false;
							}
						}
					}
				}
			}
		}
	}
	
	ArrayList<Integer> zerox = new ArrayList<Integer>();
	ArrayList<Integer> zeroy = new ArrayList<Integer>();
	
	public void clear(int i, int j){
		zerox.add(i);
		zeroy.add(j);
		selected[zerox.get(0)][zeroy.get(0)] = true; 
		for(int z = 0; z < zerox.size(); z++){
			if((zeroy.get(z) - 1 >= 0 && boxes[zerox.get(z)][zeroy.get(z) - 1] == 0) && !selected[zerox.get(z)][zeroy.get(z) - 1]){		//up
				selected[zerox.get(z)][zeroy.get(z) - 1] = true;
				zerox.add(zerox.get(z));
				zeroy.add(zeroy.get(z) - 1);
			}
			else if((zeroy.get(z) - 1 >= 0 && boxes[zerox.get(z)][zeroy.get(z) - 1] > 0) && !selected[zerox.get(z)][zeroy.get(z) - 1]){
				selected[zerox.get(z)][zeroy.get(z) - 1] = true;
			}
			if((zeroy.get(z) + 1 < height && boxes[zerox.get(z)][zeroy.get(z) + 1] == 0) && !selected[zerox.get(z)][zeroy.get(z) + 1]){		//down
				selected[zerox.get(z)][zeroy.get(z) + 1] = true;
				zerox.add(zerox.get(z));
				zeroy.add(zeroy.get(z) + 1);
			}
			else if((zeroy.get(z) + 1 < height && boxes[zerox.get(z)][zeroy.get(z) + 1] > 0) && !selected[zerox.get(z)][zeroy.get(z) + 1]){	
				selected[zerox.get(z)][zeroy.get(z) + 1] = true;
			}
			if((zerox.get(z) - 1 >= 0 && boxes[zerox.get(z) - 1][zeroy.get(z)] == 0) && !selected[zerox.get(z) - 1][zeroy.get(z)]){		//left
				selected[zerox.get(z) - 1][zeroy.get(z)] = true;
				zerox.add(zerox.get(z) - 1);
				zeroy.add(zeroy.get(z));
			}
			else if((zerox.get(z) - 1 >= 0 && boxes[zerox.get(z) - 1][zeroy.get(z)] > 0) && !selected[zerox.get(z) - 1][zeroy.get(z)]){

				selected[zerox.get(z) - 1][zeroy.get(z)] = true;
			}
			if((zerox.get(z) + 1 < width && boxes[zerox.get(z) + 1][zeroy.get(z)] == 0) && !selected[zerox.get(z) + 1][zeroy.get(z)]){	//right
				selected[zerox.get(z) + 1][zeroy.get(z)] = true;
				zerox.add(zerox.get(z) + 1);
				zeroy.add(zeroy.get(z));
			}
			else if((zerox.get(z) + 1 < width && boxes[zerox.get(z) + 1][zeroy.get(z)] > 0) && !selected[zerox.get(z) + 1][zeroy.get(z)]){	
				selected[zerox.get(z) + 1][zeroy.get(z)] = true;
			}
			
			if((zerox.get(z) - 1 >= 0 && zeroy.get(z) - 1 >= 0 && boxes[zerox.get(z) - 1][zeroy.get(z) - 1] > 0) && !selected[zerox.get(z) - 1][zeroy.get(z) - 1]){ //up-left
				selected[zerox.get(z) - 1][zeroy.get(z) - 1] = true;
			}	
			if((zerox.get(z) - 1 >= 0 && zeroy.get(z) + 1 < height && boxes[zerox.get(z) - 1][zeroy.get(z) + 1] > 0) && !selected[zerox.get(z) - 1][zeroy.get(z) + 1]){ //down-left
				selected[zerox.get(z) - 1][zeroy.get(z) + 1] = true;
			}	
			if((zerox.get(z) + 1 < width && zeroy.get(z) - 1 >= 0 && boxes[zerox.get(z) + 1][zeroy.get(z) - 1] > 0) && !selected[zerox.get(z) + 1][zeroy.get(z) - 1]){ //up-right
				selected[zerox.get(z) + 1][zeroy.get(z) - 1] = true;
			}	
			if((zerox.get(z) + 1 < width && zeroy.get(z) + 1 < height && boxes[zerox.get(z) + 1][zeroy.get(z) + 1] > 0) && !selected[zerox.get(z) + 1][zeroy.get(z) + 1]){ //down-right
				selected[zerox.get(z) + 1][zeroy.get(z) + 1] = true;
			}	
		}
		zerox.clear();
		zeroy.clear();
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.DARK_GRAY);
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(detected[i][j]){
					g.fillRect(i * size, j * size, size, size);	
				}
			}
		}
		g.setFont(font);
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(selected[i][j]){
					if(boxes[i][j] == 0){
						g.setColor(Color.WHITE);
						g.fillRect(i * size, j * size, size, size);
					}
					if(boxes[i][j] < 10 && boxes[i][j] > 0){
						if(boxes[i][j] == 1){
							g.setColor(Color.BLUE);
						}
						else if(boxes[i][j] == 2){
							g.setColor(Color.getHSBColor(116, 68, 66));
						}	
						else if(boxes[i][j] == 3){
							g.setColor(Color.RED);
						}	
						else if(boxes[i][j] == 4){
							g.setColor(Color.MAGENTA);
						}
						else if(boxes[i][j] == 5){
							g.setColor(Color.ORANGE);
						}
						else if(boxes[i][j] == 6){
							g.setColor(Color.GRAY);
						}
						else if(boxes[i][j] == 7){
							g.setColor(Color.DARK_GRAY);
						}
						else if(boxes[i][j] == 8){
							g.setColor(Color.BLACK);
						}
						g.drawString("" + boxes[i][j], i * size + ((size / 4) + (size / 8)), j * size + size - ((size / 4)));
					}
					else if(boxes[i][j] == 10){
						g.setColor(Color.BLACK);
						for(int ii = 0; ii < width; ii++){
							for(int jj = 0; jj < height; jj++){
								if(boxes[ii][jj] == 10){
									selected[ii][jj] = true;
									gameover = true;
								}
							}
						}	
						g.fillOval(i * size + (size / 4), j * size + (size / 4), size / 2, size / 2);
					}
				}
			}
		}	
		g.setColor(Color.BLACK);
		for(int i = 0; i < (width * size) + size; i = i + size){
			g.drawLine(i, 0, i, height * size);
		}
		for(int i = 0; i < (height * size) + size; i = i + size){
			g.drawLine(0, i, width * size, i);
		}	
	}
}