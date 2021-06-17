package flappyCosmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyCosmo implements ActionListener, MouseListener, KeyListener {

    public static FlappyCosmo flappyCosmo;

    public final int WIDTH = 800;

    public final int HEIGHT = 800;

    public Renderer renderer;

    public Rectangle cosmo;

    public ArrayList<Rectangle> columns;

    public Random random;

    public int ticks;

    public int yMotion;

    public boolean gameOver;

    public boolean started;

    public int score;

    public FlappyCosmo() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        random = new Random();
        gameOver = false;

        jframe.add(renderer);
        jframe.setTitle("Flappy Cosmo");
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setVisible(true);

        columns = new ArrayList<>();
        cosmo = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();

    }

    public static void main(String[] args) {

        flappyCosmo = new FlappyCosmo();
    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }


    }

    public void repaint(Graphics g) {

        //background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //floor/dirt
        g.setColor(Color.orange);
        g.fillRect(0,HEIGHT - 120, WIDTH, 120);

        //floor/grass
        g.setColor(Color.green);
        g.fillRect(0,HEIGHT - 120, WIDTH, 20);

        //player
        g.setColor(Color.red);
        g.fillRect(cosmo.x, cosmo.y, cosmo.width, cosmo.height);

        for(Rectangle column : columns) {
            paintColumn(g, column);
        }

        //score box
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 140, 35);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " + score, 0, 27);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " + score, 1, 25);

        if(!started) {
            //Flappy cosmo
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("Flappy Cosmo", 55, HEIGHT / 2 - 45);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("Flappy Cosmo", 60, HEIGHT / 2 - 50);
            
            //tap to start
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Tap to start!", 247, HEIGHT / 2 + 54);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Tap to start!", 250, HEIGHT / 2 + 50);
        }

        if(gameOver) {
            //gameover
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("Game Over!", 96, HEIGHT / 2 - 46);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
        }

    }

    public void paintColumn(Graphics g, Rectangle column) {

        //paints the columns
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() {
        if (gameOver) {
            //start new game
            columns.clear();
            cosmo = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
    
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            //reset score
            score = 0;
            yMotion = 0;
            gameOver = false;
        }
        if (!started) {
            started = true;
        }
        else {
            if (yMotion > 0) {
                yMotion = 0;
            }
    
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;

        int speed = 10;

        if(started) {

            //movement of the columns
            for (Rectangle column : columns) {
                column.x -= speed;
            }

            //time passing for the gravity
            if(ticks % 2 == 0 && yMotion <= 15) {
                yMotion += 2;
            }

            //make columns disappear
            for(int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                
                if(column.x + column.width < 0) {
                    columns.remove(column);
                
                    if(column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            //gravity of cosmo
            cosmo.y += yMotion;

            //check for collision & score
            for (Rectangle column : columns) {
                if(column.intersects(cosmo)) {
                    gameOver = true;
                    cosmo.x = column.x - cosmo.width;
                }

                if (column.y == 0 && !gameOver && cosmo.x + cosmo.width / 2 > column.x + column.width / 2 - 10 && cosmo.x + cosmo.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }
            }

            //check for collision with ground / ceiling
            if (cosmo.y >= HEIGHT - 120 || cosmo.y < 0) {
                gameOver = true;
            }

            //cosmo dies on the ground
            if(cosmo.y + yMotion >= HEIGHT - 120) {
                cosmo.y = HEIGHT - 120 - cosmo.height;
            }           
        }
        renderer.repaint();    
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        jump();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
