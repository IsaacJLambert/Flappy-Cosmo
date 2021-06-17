package flappyCosmo;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Renderer extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FlappyCosmo.flappyCosmo.repaint(g);
    }
}
