package pacman;

import javax.swing.JFrame;

public class Pacman extends JFrame{

    public Pacman() {
        add(new Game());
    }

    public static void main(String[] args) {
        Pacman pacman = new Pacman();
        pacman.setTitle("Pacman");
        pacman.setSize(376,439);
        pacman.setVisible(true);
        pacman.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pacman.setLocationRelativeTo(null);
    }
}
