import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.awt.event.MouseEvent;



public class GameOfLife extends JFrame implements ActionListener {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);
    private static final int BLOCK_SIZE = 5;

    private static JToolBar Gol_toolBar;
    private JButton Start;
    private JButton Stop;
    private JButton Reset;
    private static JComboBox c1;
//    private  JButton Autofill;

    private int i_movesPerSecond = 3;
    private GameBoard gb_gameBoard;
    private Thread game;

    public static void main(String[] args) {

        JFrame game = new GameOfLife();
        game.setDefaultCloseOperation(EXIT_ON_CLOSE);
        game.setTitle("Conway's Game of Life");
        Container contentPane = game.getContentPane();
        contentPane.add(Gol_toolBar, BorderLayout.SOUTH);
        game.setSize(DEFAULT_WINDOW_SIZE);
        game.setMinimumSize(MINIMUM_WINDOW_SIZE);
        game.setVisible(true);
        game.setResizable(false);
    }

    public GameOfLife() {

        Gol_toolBar = new JToolBar();
        Gol_toolBar.setBackground(Color.GRAY);
       // Gol_toolBar.setFloatable(false);


        Start = new JButton("Start");
        Start.setBackground(Color.WHITE);
        Gol_toolBar.add(Start);
        Gol_toolBar.addSeparator();
        Stop = new JButton("Stop");
        String s1[] = { "Autofill","20%", "40%", "60%", "80%" };
 
        // create checkbox
        c1 = new JComboBox(s1);
        // c1.setPrototypeDisplayValue("text here");
        c1.setMaximumSize( c1.getPreferredSize() );
        Gol_toolBar.add(Stop);
        Stop.setBackground(Color.WHITE);
        Gol_toolBar.addSeparator();
        Reset = new JButton("Reset");
        Gol_toolBar.add(Reset);
        Gol_toolBar.addSeparator();
        Gol_toolBar.add(c1);
        Reset.setBackground(Color.WHITE);
        Gol_toolBar.addSeparator();
//        Autofill =new JButton("Generate Random Patterns");
//        Gol_toolBar.add(Autofill);
//        Autofill.setBackground(Color.WHITE);
//        Gol_toolBar.addSeparator();

        Start.addActionListener(this);
        Stop.addActionListener(this);
        Reset.addActionListener(this);
        c1.addActionListener(this);

//        Autofill.addActionListener(this);

        // Setup game board
        gb_gameBoard = new GameBoard();
        add(gb_gameBoard);

    }

    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            Start.setEnabled(false);
            Stop.setEnabled(true);
            game = new Thread(gb_gameBoard);
            game.start();
        } else {
            Start.setEnabled(true);
            Stop.setEnabled(false);
            game.interrupt();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
         if (ae.getSource().equals(Reset)) {
            gb_gameBoard.resetBoard();
            gb_gameBoard.repaint();
        } else if (ae.getSource().equals(Start)) {
            setGameBeingPlayed(true);
        } else if (ae.getSource().equals(Stop)) {
            setGameBeingPlayed(false);
        }
       if (ae.getSource().equals(c1)) {
//            final JFrame f_autoFill = new JFrame();
//            f_autoFill.setTitle("Generate Random Patterns");
//            f_autoFill.setSize(360, 100);
//
if (c1.getSelectedIndex() > 0) {
    if(c1.getSelectedItem()!="Autofill"){
      
     gb_gameBoard.resetBoard();
     String s = (String) c1.getSelectedItem();
     String[] str = s.split("%");
     int k = Integer.parseInt(str[0]);
     gb_gameBoard.randomlyFillBoard(k);
 //    f_autoFill.dispose();

    }
}


//            f_autoFill.setResizable(false);
//            JPanel p_autoFill = new JPanel();
//            p_autoFill.setOpaque(false);
//            f_autoFill.add(p_autoFill);
//            p_autoFill.add(new JLabel("What percentage should be filled? "));
//            Object[] percentageOptions = {"Select", 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 95};
//            final JComboBox cb_percent = new JComboBox(percentageOptions);
//            p_autoFill.add(cb_percent);
        //     c1.addActionListener(new ActionListener() {
        //     //    System.out.println("Entered action listener");
        //        @Override
        //        public void actionPerformed(ActionEvent e) {
        //            System.out.println("Hello");
        //            if (c1.getSelectedIndex() > 0) {
        //                if(c1.getSelectedItem()!="Autofill"){
        //                    
        //                 gb_gameBoard.resetBoard();
        //                 String s = (String) c1.getSelectedItem();
        //                 String[] str = s.split("%");
        //                 int k = Integer.parseInt(str[0]);
        //                 gb_gameBoard.randomlyFillBoard(k);
        //             //    f_autoFill.dispose();
                   
        //                }

        //            } 
        //        }
        //    });
        //    f_autoFill.setVisible(true);
        }
    }

    private class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
        private Dimension d_gameBoardSize = null;
        private ArrayList<Point> point = new ArrayList<Point>(0);

        public GameBoard() {
            // Add resizing listener
            addComponentListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        private void updateArraySize() {
            ArrayList<Point> removeList = new ArrayList<Point>(0);
            for (Point current : point) {
                if ((current.x > d_gameBoardSize.width - 1) || (current.y > d_gameBoardSize.height - 1)) {
                    removeList.add(current);
                }
            }
            point.removeAll(removeList);
            repaint();
        }

        public void addPoint(int x, int y) {
            if (!point.contains(new Point(x, y))) {
                point.add(new Point(x, y));
            }
            repaint();
        }

        public void addPoint(MouseEvent me) {
            int x = me.getPoint().x / BLOCK_SIZE - 1;
            int y = me.getPoint().y / BLOCK_SIZE - 1;
            if ((x >= 0) && (x < d_gameBoardSize.width) && (y >= 0) && (y < d_gameBoardSize.height)) {
                addPoint(x, y);
            }
        }

        public void removePoint(int x, int y) {
            point.remove(new Point(x, y));
        }

        public void resetBoard() {
            point.clear();
        }

       
        public void randomlyFillBoard(int k) {
            for (int i = 0; i < d_gameBoardSize.width; i++) {
                for (int j = 0; j < d_gameBoardSize.height; j++) {
                    if (Math.random() * 100 < k) {
                        addPoint(i, j);
                    }
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (Point newPoint : point) {
                    // Draw new point
                    g.setColor(Color.GREEN);
                    g.fillRect(BLOCK_SIZE + (BLOCK_SIZE * newPoint.x), BLOCK_SIZE + (BLOCK_SIZE * newPoint.y), BLOCK_SIZE, BLOCK_SIZE);
                }
            } catch (ConcurrentModificationException cme) {
            }
            // Setup grid
            g.setColor(Color.BLUE);
            for (int i = 0; i <= d_gameBoardSize.width; i++) {
                g.drawLine(((i * BLOCK_SIZE) + BLOCK_SIZE), BLOCK_SIZE, (i * BLOCK_SIZE) + BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE * d_gameBoardSize.height));
            }
            for (int i = 0; i <= d_gameBoardSize.height; i++) {
                g.drawLine(BLOCK_SIZE, ((i * BLOCK_SIZE) + BLOCK_SIZE), BLOCK_SIZE * (d_gameBoardSize.width + 1), ((i * BLOCK_SIZE) + BLOCK_SIZE));
            }
        }

        @Override
        public void componentResized(ComponentEvent e) {
            // Setup the game board size with proper boundries
            d_gameBoardSize = new Dimension(getWidth() / BLOCK_SIZE - 2, getHeight() / BLOCK_SIZE - 2);
            updateArraySize();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Mouse was released (user clicked)
            addPoint(e);

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void run() {
            boolean[][] gameBoard = new boolean[d_gameBoardSize.width + 2][d_gameBoardSize.height + 2];
            for (Point current : point) {
                gameBoard[current.x + 1][current.y + 1] = true;
            }
            ArrayList<Point> survivingCells = new ArrayList<Point>(0);
            // Iterate through the array, follow game of life rules
            for (int i = 1; i < gameBoard.length - 1; i++) {
                for (int j = 1; j < gameBoard[0].length - 1; j++) {
                    int surrounding = 0;
                    
                    int[][]A = {{-1, -1, -1, 0,0,+1, +1, +1},
                                { -1, 0,+1, -1, +1,-1,0,+1}};
                        for(int b=0;b<8;b++){
                            if (gameBoard[i +A[0][b] ][ j +A[1][b] ]) {
                                surrounding++;
                            }
                        }
                    
                    
                    if (gameBoard[i][j]) {
                        // Cell is alive, Can the cell live? (2-3)
                        if ((surrounding == 2) || (surrounding == 3)) {
                            survivingCells.add(new Point(i - 1, j - 1));
                        }
                    } else {
                        // Cell is dead, will the cell be given birth? (3)
                        if (surrounding == 3) {
                            survivingCells.add(new Point(i - 1, j - 1));
                        }
                    }
                }
            }
            resetBoard();
            point.addAll(survivingCells);
            repaint();
            try {
                 Thread.sleep(100);

                run();
            } catch (InterruptedException ex) {
            }
        }
    }
}