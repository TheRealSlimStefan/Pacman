package pacman;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

import static java.lang.System.exit;

public class Game extends JPanel implements ActionListener {

    private Dimension dimension = new Dimension(360, 400);

    private final Font bigFont = new Font("Roboto", Font.BOLD, 50);
    private final Font middleFont = new Font("Roboto", Font.BOLD, 30);
    private final Font smallFont = new Font("Roboto", Font.BOLD, 18);

    private boolean inGame = false;
    private boolean win = false;
    private boolean finished = false;
    private boolean settings = false;

    private int enemies = 1;
    private int lives = 3;

    private boolean startButton = true;
    private boolean exitButton = false;
    private boolean optionsButton = false;

    private boolean enemiesButton = false;
    private boolean livesButton = false;

    private int offset = 10;

    private int pacmanX;
    private int pacmanY;
    private int pacmanDX;
    private int pacmanDY;
    private int pacmanDirectionX;
    private int pacmanDirectionY;

    private int [] ghostX, ghostY, ghostSpeedX, ghostSpeedY, ghostDirectionX, ghostDirectionY;
    private int enemiesMAX = 3;

    private int score;
    private int points;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private Image up, down, left, right, heart, ghost;

    //mapka
    private short map[][] = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    //aktualnie wgrana mapka
    private short levelData[][] = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    //konstruktor
    public Game() {
        ghostSpeedY = new int[enemiesMAX];
        ghostSpeedX = new int[enemiesMAX];
        ghostX = new int[enemiesMAX];
        ghostY = new int[enemiesMAX];
        ghostDirectionX = new int[enemiesMAX];
        ghostDirectionY = new int[enemiesMAX];
        loadImages();
        addKeyListener(new TAdapter());
        setFocusable(true);
    }

    //obsługa zainiciowanie gry
    private void initGame(){
        pacmanX = 0;
        pacmanY = 0;
        pacmanDX = 6;
        pacmanDY = 6;
        pacmanDirectionX = 1;
        pacmanDirectionY = 0;

        for(int i = 0; i < enemies; i++){
            ghostX[i] = dimension.width - 24;
            ghostY[i] = dimension.height - 64;

            Random random = new Random();
            ghostSpeedX[i] = random.nextInt(5) + 1;
            ghostSpeedY[i] = random.nextInt(5) + 1;
            ghostDirectionX[i] = random.nextInt(1);

            if(ghostDirectionX[i] == 0) ghostDirectionX[i] = -1;
            ghostSpeedX[i] *= ghostDirectionX[i];

            ghostDirectionY[i] = random.nextInt(1);
            if(ghostDirectionY[i] == 0) ghostDirectionY[i] = -1;
            ghostSpeedY[i] *= ghostDirectionY[i];

        }

        score = 0;
        points = countPoints();
    }

    //uruchomienie gry
    private void playGame(Graphics2D g2d) {
        try {
            TimeUnit.MILLISECONDS.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drawBoard(g2d);
        drawPacman(g2d);
        moveGhost();
        drawGhost(g2d);
        checkBoard();
    }

    //zliczanie możliwych punktów do zdobycia
    private int countPoints(){
        int points = 0;

        for(int i = 0; i < N_BLOCKS; i++){
            for(int j = 0; j < N_BLOCKS; j++){
                if(levelData[i][j] == 1) points++;
            }
        }

        return points;
    }

    //pilnowanie rozgrywki
    private void checkBoard(){
        wining();
        death();
    }

    //obsługa wygranej
    private void wining() {
        if(score == points) {
            win = true;
            finished = true;
        }
    }

    //obsługa śmierci
    private void death() {
        int indexPacmanX = (pacmanX - (pacmanX % 24)) / 24;
        int indexPacmanY = (pacmanY - (pacmanY % 24)) / 24;


        for(int i = 0; i < enemies; i++){
            int indexGhostX = (ghostX[i] - (ghostX[i] % 24)) / 24;
            int indexGhostY = (ghostY[i] - (ghostY[i] % 24)) / 24;

            if(indexGhostX == indexPacmanX && indexGhostY == indexPacmanY) lives--;
            if(lives == 0) {
                win = false;
                finished = true;
            }
        }
    }

    //ładowanie zdjęć z pliku
    private void loadImages() {
        down = new ImageIcon("src/images/down.gif").getImage();
        up = new ImageIcon("src/images/up.gif").getImage();
        left = new ImageIcon("src/images/left.gif").getImage();
        right = new ImageIcon("src/images/right.gif").getImage();
        ghost = new ImageIcon("src/images/ghost.gif").getImage();
        heart = new ImageIcon("src/images/heart.png").getImage();
    }

    //rysowanie mapki
    private void drawBoard(Graphics2D g2d){
        for(int i = 0; i < N_BLOCKS; i++){
            for(int j = 0; j < N_BLOCKS; j++){
                if(levelData[j][i] == 0) {
                    g2d.setColor(new Color(26, 188, 156));
                    g2d.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                } else if(levelData[j][i] == 1) {
                    g2d.setColor(new Color(000));
                    g2d.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.fillOval(i * BLOCK_SIZE + 8, j * BLOCK_SIZE + 8, 4, 4);
                } else if(levelData[j][i] == 2){
                    g2d.setColor(new Color(000));
                    g2d.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, dimension.height - 40, dimension.width, 2);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, dimension.height - 40, dimension.width, 2);

        String text = "score: " + score;
        g2d.setFont(smallFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, dimension.width - 95, dimension.height - 15);

        if(lives != -1){
            for(int i = 0; i < (lives % (lives + 1)); i++)
                g2d.drawImage(heart, 10 + i * 25, dimension.height - 30, this);
        }

    }

    //wykrywanie kolizji pacmana z elementami otoczenia
    private void collisionDetection(char direction){
        int indexX = (pacmanX - (pacmanX % 24)) / 24;
        int indexY = (pacmanY - (pacmanY % 24)) / 24;

        switch(direction){
            case 'u':
                indexY = (pacmanY + BLOCK_SIZE - (pacmanY + BLOCK_SIZE) % 24) / 24;

                if(indexY != 0 && levelData[indexY - 1][indexX] == 0) {
                    pacmanY += pacmanDY;
                }

                if(levelData[indexY][indexX] == 1) {
                    levelData[indexY][indexX] = 2;
                    score++;
                }
                break;
            case 'd':
                if(indexY != 14 && levelData[indexY + 1][indexX] == 0){
                    pacmanY -= pacmanDY;
                }

                if(levelData[indexY][indexX] == 1) {
                    levelData[indexY][indexX] = 2;
                    score++;
                }
                break;
            case 'r':
                if(indexX != 14 && levelData[indexY][indexX + 1] == 0) {
                    pacmanX -= pacmanDX;
                }

                if(levelData[indexY][indexX] == 1) {
                    levelData[indexY][indexX] = 2;
                    score++;
                }
                break;
            case 'l':
                indexX = (pacmanX + BLOCK_SIZE - (pacmanX + BLOCK_SIZE) % 24) / 24;

                if(indexX != 0 && levelData[indexY][indexX - 1] == 0){
                    pacmanX += pacmanDX;
                }

                if(levelData[indexY][indexX] == 1) {
                    levelData[indexY][indexX] = 2;
                    score++;
                }
                break;
        }
    }

    //prouszanie się pacmana
    private void movePacman(char direction){
        switch(direction){
            case 'u':
                if(pacmanY <= 0) pacmanY = 0;
                else pacmanY -= pacmanDY;
                pacmanDirectionY = 1;
                pacmanDirectionX = 0;
                collisionDetection(direction);
                break;
            case 'd':
                if(pacmanY >= dimension.height - 64) pacmanY = dimension.height - 64;
                else pacmanY += pacmanDY;
                pacmanDirectionY = -1;
                pacmanDirectionX = 0;
                collisionDetection(direction);
                break;
            case 'r':
                if(pacmanX >= dimension.width - 22) pacmanX = dimension.width - 22;
                else pacmanX += pacmanDX;
                pacmanDirectionY = 0;
                pacmanDirectionX = 1;
                collisionDetection(direction);
                break;
            case 'l':
                if(pacmanX <= 0) pacmanX = 0;
                else pacmanX -= pacmanDX;
                pacmanDirectionY = 0;
                pacmanDirectionX = -1;
                collisionDetection(direction);
                break;
        }
    }

    //poruszanie się duszków
    private void moveGhost(){
        for(int i = 0; i < enemies; i++) {

            ghostX[i] += ghostSpeedX[i];
            ghostY[i] += ghostSpeedY[i];

            if (ghostX[i] < 0) {
                ghostX[i] = 0;
                ghostSpeedX[i] = -ghostSpeedX[i];
                ghostX[i] += ghostSpeedX[i];
            } else ghostX[i] -= ghostSpeedX[i];

            if (ghostY[i] < 0) {
                ghostY[i] = 0;
                ghostSpeedY[i] = -ghostSpeedY[i];
                ghostX[i] += ghostSpeedY[i];
            } else ghostY[i] -= ghostSpeedY[i];

            if (ghostX[i] > dimension.width - 24) {
                ghostX[i] = dimension.width - 24;
                ghostSpeedX[i] = -ghostSpeedX[i];
                ghostX[i] += ghostSpeedX[i];
            } else ghostX[i] += ghostSpeedX[i];

            if (ghostY[i] > dimension.height - 64) {
                ghostY[i] = dimension.height - 64;
                ghostSpeedY[i] = -ghostSpeedY[i];
                ghostY[i] += ghostSpeedY[i];
            } else ghostY[i] += ghostSpeedY[i];
        }
    }

    //rysowanie menu głównego
    private void drawStartScreen(Graphics2D g2d){
        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, 0, dimension.width, 10);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, 0, 10, dimension.height);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, dimension.height - 10, dimension.width, 10);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(dimension.width - 10, 0, 10, dimension.height);

        String text = "PACMAN";
        g2d.setFont(bigFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 110, (dimension.height) / 4);

        text = "START";
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 50, (dimension.height) / 2 + 25);

        text = "OPTIONS";
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 70, (dimension.height) / 2 + 75);

        text = "EXIT";
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 30, (dimension.height) / 2 + 125);

        drawOptions(g2d, offset);
    }

    //rysowanie menu ustawień
    private void drawSettings(Graphics2D g2d){
        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, 0, dimension.width, 10);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, 0, 10, dimension.height);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(0, dimension.height - 10, dimension.width, 10);

        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRect(dimension.width - 10, 0, 10, dimension.height);

        String text = "SETTINGS ";
        g2d.setFont(bigFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 130, (dimension.height) / 4);

        text = "ENEMIES";
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 140, (dimension.height) / 2 + 75);

        text = "LIVES";
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 140, (dimension.height) / 2 + 25);

        text = "" + enemies;
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 + 70, (dimension.height) / 2 + 75);

        text = "" + lives;
        g2d.setFont(middleFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 + 70, (dimension.height) / 2 + 25);

        drawOptions(g2d, offset);
    }

    //rysowanie ekranu przegranej
    private void drawLoseScreen(Graphics2D g2d){
        String text = "You lose!";
        g2d.setFont(bigFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 110, (dimension.height) / 2 - 25);

        text = "Press escape button to continue!";
        g2d.setFont(smallFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 160, (dimension.height) / 2 + 20);
    }

    //rysowanie ekranu zwycięstwa
    private void drawWonScreen(Graphics2D g2d){
        String text = "You won!";
        g2d.setFont(bigFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 110, (dimension.height) / 2 - 25);

        text = "Press escape button to continue!";
        g2d.setFont(smallFont);
        g2d.setColor(Color.yellow);
        g2d.drawString(text, (dimension.width) / 2 - 160, (dimension.height) / 2 + 20);
    }

    //rysowanie kontrolek w menu
    private void drawOptions(Graphics2D g2d, int offset){
        if(!settings){
            g2d.setColor(new Color(26, 188, 156));
            g2d.fillRect(95, (dimension.height) / 2 + offset, 10, 10);

            g2d.setColor(new Color(26, 188, 156));
            g2d.fillRect(250, (dimension.height) / 2 + offset, 10, 10);
        } else {
            g2d.setColor(new Color(26, 188, 156));
            g2d.fillRect(220, (dimension.height) / 2 + offset, 18, 10);

            g2d.setColor(new Color(26, 188, 156));
            g2d.fillRect(281, (dimension.height) / 2 + offset - 10, 6, 10);

            g2d.setColor(new Color(26, 188, 156));
            g2d.fillRect(275, (dimension.height) / 2 + offset, 18, 10);

            g2d.setColor(new Color(26, 188, 156));
            g2d.fillRect(281, (dimension.height) / 2 + offset + 10, 6, 10);
        }
    }

    //rysowanie pacmana
    private void drawPacman(Graphics2D g2d){
        if(pacmanDirectionX == 1){
            g2d.drawImage(right, pacmanX, pacmanY, this);
        }
        if(pacmanDirectionX == -1){
            g2d.drawImage(left, pacmanX, pacmanY, this);
        }
        if(pacmanDirectionY == 1){
            g2d.drawImage(up, pacmanX, pacmanY, this);
        }
        if(pacmanDirectionY == -1){
            g2d.drawImage(down, pacmanX, pacmanY, this);
        }
    }

    //rysowanie duszka
    private void drawGhost(Graphics2D g2d){
        for(int i = 0; i < enemies; i++){
            g2d.drawImage(ghost, ghostX[i], ghostY[i], this);
        }
    }

    //resetowanie do ustawień domyślnych po zakończeniu rozgrywki
    public void resetToDefault(){
        enemies = 1;
        lives = 3;

        for(int i = 0; i < N_BLOCKS; i++){
            for(int j = 0; j < N_BLOCKS; j++){
                levelData[i][j] = map[i][j];
            }
        }
    }

    //rysowanie odpowiednich elementów na ekranie
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, dimension.width, dimension.height);

        if (settings){
            drawSettings(g2d);
        } else if(inGame && !finished){
            playGame(g2d);
        } else if(!win && finished) {
            drawLoseScreen(g2d);
        } else if(win && finished) {
            drawWonScreen(g2d);
        } else {
            drawStartScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            //obsługa klaawisza stzałki do góry w menu głównym
            if(!inGame && !settings && key == KeyEvent.VK_UP){
                if(startButton){
                    startButton = false;
                    optionsButton = false;
                    exitButton = true;
                    offset = 110;
                } else if(optionsButton){
                    startButton = true;
                    optionsButton = false;
                    exitButton = false;
                    offset = 10;
                } else if(exitButton){
                    startButton = false;
                    optionsButton = true;
                    exitButton = false;
                    offset = 60;
                    System.out.println("startButton: " + startButton);
                    System.out.println("optionsButton: " + optionsButton);
                    System.out.println("exitButton: " + exitButton);
                }
            }

            //obsługa klaawisza stzałki do dół w menu głównym
            if(!inGame && !settings && key == KeyEvent.VK_DOWN){
                if(startButton){
                    startButton = false;
                    optionsButton = true;
                    enemiesButton = true;
                    exitButton = false;
                    offset = 60;
                } else if(optionsButton){
                    startButton = false;
                    optionsButton = false;
                    exitButton = true;
                    offset = 110;
                } else if(exitButton){
                    startButton = true;
                    optionsButton = false;
                    exitButton = false;
                    offset = 10;
                    System.out.println("startButton: " + startButton);
                    System.out.println("optionsButton: " + optionsButton);
                    System.out.println("exitButton: " + exitButton);
                }
            }

            //obsługa klawisza ENTER po wybraniu w menu opcji OPTIONS
            if(!inGame && key == KeyEvent.VK_ENTER && optionsButton){
                settings = true;
                livesButton = true;
                enemiesButton = false;
                offset = 10;
                optionsButton = false;
            }

            //obsługa klawisza STRZALKI (góra)
            if(!inGame && settings && key == KeyEvent.VK_UP){
                if(livesButton) {
                    enemiesButton = true;
                    livesButton = false;
                    offset = 60;
                } else if(enemiesButton) {
                    enemiesButton = false;
                    livesButton = true;
                    offset = 10;
                }
            }

            //obsługa klawisza STRZALKI (dol)
            if(!inGame && settings && key == KeyEvent.VK_DOWN){
                if(livesButton) {
                    enemiesButton = true;
                    livesButton = false;
                    offset = 60;
                } else if(enemiesButton) {
                    enemiesButton = false;
                    livesButton = true;
                    offset = 10;
                }
            }

            //obsługa klawisza STRZALKI (lewo) po wybraniu opcji lives
            if(!inGame && settings && livesButton && key == KeyEvent.VK_LEFT && lives >= 2){
                lives--;
            }

            //obsługa klawisza STRZALKI (prawo) po wybraniu opcji lives
            if(!inGame && settings && livesButton && key == KeyEvent.VK_RIGHT && lives < 5){
                lives++;
            }

            //obsługa klawisza STRZALKI (lewo) po wybraniu opcji enemies
            if(!inGame && settings && enemiesButton && key == KeyEvent.VK_LEFT && enemies >= 2){
                enemies--;
            }

            //obsługa klawisza STRZALKI (prawo) po wybraniu opcji enemies
            if(!inGame && settings && enemiesButton && key == KeyEvent.VK_RIGHT && enemies < 3){
                enemies++;
            }

            //obsługa klawisza BACKSPACE po wybraniu w menu ustawień SETTINGS
            if(!inGame && settings && key == KeyEvent.VK_ESCAPE) {
                settings = false;
                optionsButton = true;
                offset = 60;
            }

            //obsługa klawisza ENTER po wybraniu opcji uruchomienia gry
            if(!inGame && key == KeyEvent.VK_ENTER && startButton){
                initGame();
                inGame = true;
            }

            //obsługa klawisza ENTER po wybraniu w menu opcji exit
            if(!inGame && key == KeyEvent.VK_ENTER && exitButton){
                exit(0);
            }

            //obsługa zakończenia gry, po stracie wszystkich żyć, po wygranej i po wciśnięciu ESCAPE
            if(inGame && (points == score || key == KeyEvent.VK_ESCAPE || lives == 0 || key == KeyEvent.VK_ESCAPE)){
                offset = 10;
                startButton = true;
                inGame = false;
                finished = false;
                resetToDefault();
            }

            //sterowanie pacmanem
            if(inGame && key == KeyEvent.VK_UP) movePacman('u');
            if(inGame && key == KeyEvent.VK_DOWN) movePacman('d');
            if(inGame && key == KeyEvent.VK_RIGHT) movePacman('r');
            if(inGame && key == KeyEvent.VK_LEFT) movePacman('l');

            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}