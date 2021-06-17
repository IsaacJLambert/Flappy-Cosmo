package flappyCosmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyCosmo implements ActionListener {

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

    public boolean started = true;

    public FlappyCosmo() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        random = new Random();
        gameOver = false;

        jframe.add(renderer);
        jframe.setTitle("Flappy Cosmo");
        jframe.setSize(WIDTH, HEIGHT);
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

        //bacakground
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

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if(!started) {
            g.drawString("Flappy Cosmo", 100, HEIGHT / 2 -50);
        }

        if(gameOver) {
            g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
        }

    }

    public void paintColumn(Graphics g, Rectangle column) {

        //paints the columns
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;

        int speed = 10;

        if(started) {

            //movement of the columns
            for(int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
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

            //check for collision
            for (Rectangle column : columns) {
                if(column.intersects(cosmo)) {
                    gameOver = true;
                    cosmo.x = column.x - cosmo.width;
                }
            }

            //check for collision with ground / ceiling
            if (cosmo.y >= HEIGHT - 120 || cosmo.y < 0) {
                gameOver = true;
            }

            //cosmo is dead on the ground
            if(gameOver) {
                cosmo.y = HEIGHT - 120 - cosmo.height;
            }

           
        }
        renderer.repaint();    
    }
}
