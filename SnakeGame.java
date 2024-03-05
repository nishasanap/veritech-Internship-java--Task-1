import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener {

    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private static final int CELL_SIZE = 20;
    private static final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean running;
    private int score;

    public SnakeGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        generateFood();
        direction = KeyEvent.VK_RIGHT;
        running = true;
        score = 0;
        startGame();
    }

    private void startGame() {
        Thread gameThread = new Thread(() -> {
            while (running) {
                move();
                checkCollision();
                checkFood();
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
        }

        snake.add(0, newHead);
        if (!checkFood()) {
            snake.remove(snake.size() - 1);
        }
    }

    private boolean checkFood() {
        if (snake.get(0).equals(food)) {
            score++;
            generateFood();
            return true;
        }
        return false;
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver();
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
            }
        }
    }

    private void generateFood() {
        Random random = new Random();
        int x = random.nextInt(BOARD_WIDTH);
        int y = random.nextInt(BOARD_HEIGHT);
        food = new Point(x, y);
    }

    private void gameOver() {
        running = false;
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
        initGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnake(g);
        drawFood(g);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
                && (direction != KeyEvent.VK_LEFT || key != KeyEvent.VK_RIGHT)
                && (direction != KeyEvent.VK_RIGHT || key != KeyEvent.VK_LEFT)
                && (direction != KeyEvent.VK_UP || key != KeyEvent.VK_DOWN)
                && (direction != KeyEvent.VK_DOWN || key != KeyEvent.VK_UP)) {
            direction = key;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
