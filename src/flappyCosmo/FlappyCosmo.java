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

    public FlappyCosmo() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        random = new Random();

        jframe.add(renderer);
        jframe.setTitle("Flappy Cosmo");
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //jframe.setResizable(false);
        jframe.setVisible(true);

        columns = new ArrayList<>();
        cosmo = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();

        System.out.println("Hello");

    }

    public static void main(String[] args) {

        flappyCosmo = new FlappyCosmo();
    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start) {

            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT + height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT + height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));

        }


    }

    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0,HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.green);
        g.fillRect(0,HEIGHT - 120, WIDTH, 20);


        g.setColor(Color.red);
        g.fillRect(cosmo.x, cosmo.y, cosmo.width, cosmo.height);

        for(Rectangle r : columns) {
            paintColumn(g, r);
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;

        if(ticks % 2 == 0 && yMotion <= 15) {
            yMotion += 2;
        }
        cosmo.y += yMotion;

        renderer.repaint();
    }
}
