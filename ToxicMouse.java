import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WizardMouse extends JFrame implements KeyListener {
    private volatile boolean isPaused = true; // Initially paused
    private Thread mouseThread; // Reference to the mouse movement thread
    //ImageIcon icon = new ImageIcon(getClass().getResource("/image/Wizard.jpg"));

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WizardMouse wizardMouse = new WizardMouse();
            wizardMouse.setupUI();
        });
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 80);
        setLocationRelativeTo(null);
        setTitle("Wizard Mouse");

        setMyIcon("/image/Wizard.jpg");

        // sets buttons
        JButton pauseButton = new JButton("Pause");
        JButton playButton = new JButton("Play");

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setMyIcon("/image/Wizard.jpg");
                System.out.println("Pause Button Pressed");
                mouseThread.interrupt();
                isPaused = true;
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setMyIcon("/image/RedWizard.jpg");
                isPaused = false;
                System.out.println("Play Button Pressed");
                if (mouseThread == null || !mouseThread.isAlive()) {
                    mouseThread = new Thread(() -> jiggleMouse());
                    mouseThread.start(); // Start mouse movement in a new thread
                }
            }
        });

        setLayout(new FlowLayout());
        add(pauseButton);
        add(playButton);

        addKeyListener(this);
        setVisible(true);
    }

    public void setMyIcon(String X){
        // Set custom icon
        ImageIcon icon = new ImageIcon(getClass().getResource(X));
        if (icon.getImage() == null) {
            System.out.println("Icon image not loaded!");
        }
        setIconImage(icon.getImage());
    }

    public void jiggleMouse() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        int radius = 100;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        while (!isPaused) {
            for (int angle = 0; angle < 360; angle += 10) {

                int x = centerX + (int) (radius * Math.cos(Math.toRadians(angle)));
                int y = centerY + (int) (radius * Math.sin(Math.toRadians(angle)));

                moveMouse(x, y);

                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    //System.out.println("Thread interrupted");
                    return; // Exit the method if interrupted
                }
            }
        }
    }

    public void moveMouse(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            System.out.println("Moved");
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused, but required for KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'M') {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Unused, but required for KeyListener interface
    }
}