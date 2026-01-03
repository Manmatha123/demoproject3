package gameEngine;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GameEngine {

    public static int WIDTH=1980;
    public static int HEIGHT=1080;

    public static  void main(String []  args){


        JFrame frame = new JFrame("3D Field");
        GameLoop m = new GameLoop(WIDTH, HEIGHT);
        m.setSize(new Dimension(WIDTH, HEIGHT));

        frame.add(m);
        frame.revalidate();
        frame.repaint();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);

    }
}
        